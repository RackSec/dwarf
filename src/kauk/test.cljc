(ns kauk.test
  (:require #?(:clj [clojure.test :refer [do-report]]
               :cljs [cljs.test :refer [do-report]])
            #?(:clj [clojure.main :refer [demunge]]
               :cljs [cljs.core :refer [demunge]])
            [clojure.string :as str]))

(defn- redef-error
  [original-fn]
  (let [demunged-fn (-> original-fn str demunge)
        fn-name #?(:clj demunged-fn
                   :cljs (-> demunged-fn
                             str
                             (str/split #" \{" 2)
                             first))]
    (do-report
      {:type :fail
       :message (str fn-name
                  " is called somewhere within `with-redef-calls` body.")
       :expected (str fn-name " to be called.")
       :actual (str fn-name " was not called.")})))

(defn- with-redef-call-f
  [[original-fn new-fn] & code]
  `(let [called?# (atom false)
         wrapped-new-fn# (fn [& args#]
                           (reset! called?# true)
                           (apply ~new-fn args#))]
     (with-redefs [~original-fn wrapped-new-fn#]
       ~@code)
     (when-not @called?#
       (redef-error ~original-fn))))


(defn- correct-code-parameter
  "Fixes up the seq which is the code to put in a macro.

   Pass in `((println \"1\"))` and get back `(println \"1\")`;
   pass in `((println \"1\") (println \"2\"))` and get back
   (do (println \"1\")(println \"2\"))."
  [code]
  (if (= 1 (count code))
     ;; Change list with one element which is a list to just a list
    (mapcat identity code)
     ;; Keep the list, but prepend do.
    (cons 'do code)))

(defn with-redef-calls-f
  [& args]
  (let [redef-args (first args)
        code (correct-code-parameter (rest args))
        redef-arg-pairs (reverse (partition 2 redef-args))]
    (reduce
     (fn [current [old-fn new-fn]]
       (with-redef-call-f [old-fn new-fn] current))
     code
     redef-arg-pairs)))

(defmacro with-redef-calls
  "Given a seq of pairs of original vars and new functions, will redefine
  each var with the function and assert the function is called."
  [& args]
  (apply with-redef-calls-f args))

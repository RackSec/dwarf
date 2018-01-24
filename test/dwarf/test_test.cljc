(ns dwarf.test-test
  (:require #?(:clj [clojure.test :refer :all]
               :cljs [cljs.test :refer-macros [deftest is are testing]
                      :refer [do-report]])
            #?(:clj [dwarf.test :refer [with-redef-calls]]
               :cljs [dwarf.test :refer-macros [with-redef-calls]])))

(defn test-fn
  [_]
  :fn)

(deftest with-redef-calls-test
  (testing "Redefined function is called, error is never raised."
    (with-redefs
     [dwarf.test/redef-error
      (fn [f]
        (do-report {:type :fail
                    :expected "dwarf.test/redef-error to never be called."
                    :actual "dwarf.test/redef-error was called."}))]
      (with-redef-calls [test-fn (fn [s]
                                   (is (= s "test")))]
        (test-fn "test"))))
  (testing "Redefined function is not called, error is raised."
    (with-redefs [dwarf.test/redef-error
                  (fn [f]
                    (is (= (f nil) :fn)))]
      (with-redef-calls [test-fn (fn [_])]))))

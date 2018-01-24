(ns dwarf.test-test
  (:require #?(:clj [clojure.test :refer :all]
               :cljs [cljs.test :refer-macros [deftest is are testing]])
            #?(:clj [dwarf.test :refer [with-redef-calls]]
               :cljs [dwarf.test :refer-macros [with-redef-calls]])
            #?(:clj [clojure.main :refer [demunge]]
               :cljs [cljs.core :refer [demunge]])))

(def test-fn :fn)

(deftest with-redef-calls-test
  (testing "Redefed fn is called, error is never raised"
    (with-redefs [dwarf.test/redef-error
                  (fn [f]
                    (with-test-out
                      (inc-report-counter :fail)
                      (println "\nFAIL in with-redef-calls-test:")
                      (println "dwarf.test/redef-error was called.")))]
      (with-redef-calls [test-fn (fn [s]
                                  (is (= s "test")))]
        (test-fn "test"))))
  (testing "Redefed fn is not called so error is raised."
    (with-redefs [dwarf.test/redef-error
                  (fn [f]
                    (is (= f :fn)))]
      (with-redef-calls [test-fn (fn [])]))))

(ns dwarf.dates-test
  (:require #?(:clj [clojure.test :refer :all]
               :cljs [cljs.test :refer-macros [deftest is are testing]])
            #?(:clj [clj-time.core :as tc]
               :cljs [cljs-time.core :as tc])
            #?(:clj [clj-time.format :as tf]
               :cljs [cljs-time.format :as tf])
            [dwarf.dates :as dates]))

(deftest test-parse-date
  (is (= (dates/parse-date nil) nil))
  (is (= (dates/parse-date "bogus") nil))
  (are [input expected]
       #?(:clj (tc/equal? expected (dates/parse-date input))
          :cljs (tc/= expected (dates/parse-date input)))
    "2016-12-2" (tc/date-time 2016 12 2)
    "2016-9-2" (tc/date-time 2016 9 2)
    "2016/11/30" (tc/date-time 2016 11 30)
    "10/31/2016" (tc/date-time 2016 10 31)
    "2016-01-02 03:04:05" (tc/date-time 2016 1 2 3 4 5)
    "2016-01-02T03:04:05" (tc/date-time 2016 1 2 3 4 5)
    "2014-10-05T14:15:16.988Z" (tc/date-time 2014 10 5 14 15 16 988)
    "2014-10-05 14:15:16.988Z" (tc/date-time 2014 10 5 14 15 16 988)))

(deftest test-format-date
  (are [input expected]
       (= (dates/format-date input) expected)
    (dates/format-date "not-a-date") nil
    (tc/date-time 2016 4 8 12 30) "2016-04-08")
  (are [input format expected]
       (= (dates/format-date input format) expected)
    nil nil nil
    "2016-01-02 03:04:05" "MM/dd/yyyy" "01/02/2016"
    "2016-12-28" "MM/dd/yyyy" "12/28/2016"
    "2016-09-07T11:17:13.090Z" "dd MMM" "07 Sep"))

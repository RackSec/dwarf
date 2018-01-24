(ns kauk.dates
  (:require #?(:clj [clj-time.coerce :as tco]
               :cljs [cljs-time.coerce :as tco])
            #?(:clj [clj-time.format :as tf]
               :cljs [cljs-time.format :as tf])
            #?(:clj [clj-time.core :as tc]
               :cljs [cljs-time.core :as tc])))

(defn parse-date
  "Parse a date from a couple of potential date formats."
  ([s]
   (or (tco/to-date-time s)
       (parse-date s [(tf/formatters :mysql)
                      (tf/formatters :year-month-day)
                      (tf/formatters :date-time)
                      (tf/formatters :date)
                      (tf/formatter "MMMM d, yyyy")
                      (tf/formatter "yyyy/M/d")
                      (tf/formatter "M/d/yyyy")
                      (tf/formatter "MM-dd-yyyy")
                      (tf/formatter "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
                      (tf/formatter "yyyy-MM-dd HH:mm:ss.SSSZ")])))
  ([s formatters]
   (when (pos? (count formatters))
     (try
       (-> (first formatters)
           (tf/parse (str s)))
       #?(:clj (catch Exception e (parse-date s (rest formatters)))
          :cljs (catch :default e (parse-date s (rest formatters))))))))

(defn unparse-date
  ([date]
   (unparse-date date (tf/formatter "MM-dd-yyyy")))
  ([date formatter]
   (when-not (nil? date)
     (tf/unparse formatter (tco/to-date-time date)))))

(defn format-date
  ([date]
   (format-date date "YYYY-MM-dd"))
  ([date format]
   (when-let [parsed-date (tco/to-date-time date)]
     (tf/unparse (tf/formatter format) parsed-date))))

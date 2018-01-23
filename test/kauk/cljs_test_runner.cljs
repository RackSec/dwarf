(ns kauk.cljs-test-runner
  (:require [doo.runner :refer-macros [doo-tests]]
            [kauk.dates-test]))

(doo-tests 'kauk.dates-test)

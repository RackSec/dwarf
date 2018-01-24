(ns dwarf.cljs-test-runner
  (:require [doo.runner :refer-macros [doo-tests]]
            [dwarf.dates-test]))

(doo-tests 'dwarf.dates-test)

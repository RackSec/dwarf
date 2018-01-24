(ns dwarf.cljs-test-runner
  (:require [doo.runner :refer-macros [doo-tests]]
            [dwarf.dates-test]
            [dwarf.test-test]))

(doo-tests 'dwarf.dates-test
           'dwarf.test-test)

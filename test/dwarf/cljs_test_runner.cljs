(ns dwarf.cljs-test-runner
  (:require [doo.runner :refer-macros [doo-tests]]
            [dwarf.dates-test]
            [dwarf.mock-test]))

(doo-tests 'dwarf.dates-test
           'dwarf.mock-test)

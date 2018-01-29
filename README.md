# Dwarf

```
A helpful, little dwarf providing helpers/utilities for your Clojure(Script)
projects.
```

# Test utils

### `with-redef-calls`
`with-redef-calls` works like `with-redefs`, but it also makes sure any
redefined function was called in its body. If it wasn't, it would report a test
failure:

```clojure
(ns yourapp.core-test
  (:require #?(:clj [clojure.test :refer :all]
               :cljs [cljs.test :refer-macros [deftest is are testing]])
            #?(:clj [dwarf.mock :refer [with-redef-calls]])
               :cljs [dwarf.mock :refer-macros [with-redef-calls]]))

(deftest foo-test
  (with-redef-calls [yourapp.core/foo (fn [a] (is (= a 1)))]
    (is (= 1 1))))
```

Above will result in the following failure, since `yourapp.core/foo` never fired
 in the test:

```
FAIL in (foo-test) (core_test.cljc:7)
yourapp.core/foo@b91d8c4 is called somewhere within `with-redef-calls` body.
expected: "yourapp.core/foo@b91d8c4 to be called."
  actual: "yourapp.core/foo@b91d8c4 was not called."
```

# Date utils

### `parse-date`
Parses a date from a couple of potential date formats:

```clojure
user=> (:require ''[dwarf.dates :refer [parse-date]]))
nil
user=> (parse-date "10/31/2016")
#object[org.joda.time.DateTime 0x10d10130 "2016-10-31T00:00:00.000Z"]

user=> (parse-date "2016-9-2")
#object[org.joda.time.DateTime 0x77979057 "2016-09-02T00:00:00.000Z"]

user=> (parse-date "2016-01-02 03:04:05")
#object[org.joda.time.DateTime 0x6080252f "2016-01-02T03:04:05.000Z"]

user=> (parse-date "2016-01-02T03:04:05")
#object[org.joda.time.DateTime 0x2a65659f "2016-01-02T03:04:05.000Z"]

user=> (parse-date "2014-10-05T14:15:16.988Z")
#object[org.joda.time.DateTime 0x3d3e6a04 "2014-10-05T14:15:16.988Z"]

user=> (parse-date "2014-10-05 14:15:16.988Z")
#object[org.joda.time.DateTime 0x36efde7d "2014-10-05T14:15:16.988Z"]
```

To see a full list of built-in formatters refer to the source code.
You can also pass your own clj(s)-time formatters (which will override the
default-ones):

```clojure
(:require [clj-time.format :as tf])

(def formatters [(tf/formatter "yyyy M d")
                 (tf/formatter "yyyy MMMM")])

(parse-date "2016 1 1" formatters) ;=> #object[org.joda.time.DateTime 0x32a595d4 "2016-01-01T00:00:00.000Z"]
(parse-date "2016 February") ;=> #object[org.joda.time.DateTime 0x5fa5e51a "2016-02-01T00:00:00.000Z"]
```

### `format-date`
Reformats a given date string:

```clojure
(ns yourapp.core
  (:require [dwarf.dates :refer [format-date]]

(format-date "2016-01-02 03:04:05" "MM/dd/yyyy") ;=> "01/02/2016"
(format-date "2016-09-07T11:17:13.090Z" "dd MMM") ;=> "07 Sep"
(format-date "2016-09-07T11:17:13.090Z") ;=> "2016-09-07"
```


# Development
To run the Clojure tests: `lein test`
To run the ClojureScript tests: `lein doo firefox test`

Cljs tests are ran using (Doo)(https://github.com/bensu/doo), and you'll need a
bunch of dependencies. Refer to Doo's docs for more details, but here's a tldr;

```
npm install karma karma-cljs-test karma-firefox-launcher
```

You also have to have `karma-cli` installed globally:

```
npm install karma-cli -g
```

## License

Copyright © 2018 Rackspace Managed Security.

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.

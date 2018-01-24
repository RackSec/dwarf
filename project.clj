(defproject kauk "0.1.0-SNAPSHOT"
  :description "A bunch of test and date utilities for Clojure(Script) projects."
  :url "https://github.com/RackSec/kauk"
  :lein-release {:deploy-via :clojars}
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.9.946"]
                 [clj-time "0.14.2"]
                 [com.andrewmcveigh/cljs-time "0.5.2"]]
  :plugins [[lein-cljsbuild "1.1.7"]]
  :cljsbuild
  {:builds
   {:default {:source-paths ["src"]
              :compiler {:optimizations :whitespace
                         :pretty-print true}}
    :test {:source-paths ["src" "test"]
           :compiler {:output-to "out/test.js"
                      :optimizations :whitespace
                      :pretty-print true
                      :main "kauk.cljs-test-runner"}}}}
  :profiles {:dev {:dependencies [[doo "0.1.8"]]
                   :cljfmt {:indents {let-flow [[:inner 0]]}
                            :file-pattern #"\.clj[sxc]?$"}
                   :plugins [[lein-doo "0.1.8"]
                             [lein-cljfmt "0.5.7"]]}}
  :uberjar {:aot :all
            :omit-source true
            :cljsbuild {:jar true
                        :builds {:default {:compiler {:optimizations :advanced
                                                      :pretty-print false}}}}})

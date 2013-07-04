(defproject maze "0.1.0-SNAPSHOT"
  :url "http://maze.destructuring-bind.org"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/clojurescript "0.0-1806"]
                 [org.clojure/data.priority-map "0.0.2"]
                 [org.clojure/core.cache "0.6.3"]
                 [ns-tracker "0.2.1"]
                 [noir "1.3.0"]
                 [jayq "2.3.0"]
                 [crate "0.2.4"]
                 [rm-hull/ring-gzip-middleware "0.1.6"]
                 [rm-hull/fetch "0.1.1-SNAPSHOT"]
                 [rm-hull/monet "0.1.7"]]
  :cljsbuild
    {:builds
     [{:source-paths ["src/maze/client"],
       :compiler
       {:pretty-print false,
        :output-to "resources/public/cljs/maze.js",
        :externs ["externs/jquery.js"],
        :optimizations :advanced,
        :print-input-delimiter false}}]}
  :hooks [leiningen.cljsbuild]
  :plugins [[lein-cljsbuild "0.3.2"]]
  :profiles {:dev {:dependencies [[vimclojure/server "2.3.6"]]}}
  :main maze.server
  :min-lein-version "2.2.0"
  :global-vars { *warn-on-reflection* true}
  :description "A web-based maze generator and solver")

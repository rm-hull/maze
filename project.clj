(defproject maze "0.1.0-SNAPSHOT"
  :url "http://maze.destructuring-bind.org"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.0-RC2"]
                 [org.clojure/data.priority-map "0.0.2"]
                 [org.clojure/core.cache "0.6.2"]
                 [ns-tracker "0.2.1"]
                 [noir "1.3.0"]
                 [jayq "2.0.0"]
                 [crate "0.2.3"]
                 [fetch "0.1.0-alpha2"]
                 [rm-hull/ring-gzip-middleware "0.1.4-SNAPSHOT"]
                 [rm-hull/monet "0.1.3-SNAPSHOT"]]
  :cljsbuild
  {:builds
   [{:source-paths ["src/maze/client"],
     :compiler
     {:pretty-print true,
      :output-to "resources/public/cljs/maze.js",
      :externs ["externs/jquery.js"],
      :optimizations :advanced,
      :print-input-delimiter true}}]}
  :hooks [leiningen.cljsbuild]
  :plugins [[lein-cljsbuild "0.3.0"]]
  :profiles {:dev {:dependencies [[vimclojure/server "2.3.6"]]}}
  :main maze.server
  :min-lein-version "2.0.0"
  :warn-on-reflection true
  :description "A web-based maze generator and solver")

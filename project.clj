(defproject maze "0.1.0-SNAPSHOT"
  :url "http://maze.destructuring-bind.org"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.0-beta1"]
                 [noir "1.3.0-beta10"]
                 [jayq "0.1.0-alpha4"]
                 [crate "0.2.1"]
                 [fetch "0.1.0-alpha2"]
                 [org.clojars.rm-hull/monet "0.1.1-SNAPSHOT"]
                 [ring/ring-core "1.1.6"]
                 [ring-basic-authentication "1.0.1"]
                 [ibdknox/ring-gzip-middleware "0.1.1"]
                 [com.cemerick/drawbridge "0.0.6"]]
  :cljsbuild {
    :builds [
      {:source-path "src/maze/client"
       :compiler {:output-to "resources/public/cljs/maze"
                  :optimizations :simple
                  :externs ["https://ajax.googleapis.com/ajax/libs/jquery/1.8.3/jquery.min.js"]
                  :print-input-delimiter true
                  :pretty-print true}}]}
  :hooks [leiningen.cljsbuild]
  :plugins [[lein-cljsbuild "0.2.9"]]
  :profiles {:dev {:dependencies [[vimclojure/server "2.3.6"]]}}
  :main maze.server
  :min-lein-version "2.0.0"
  :warn-on-reflection true
  :description "A web-based maze generator and solver")

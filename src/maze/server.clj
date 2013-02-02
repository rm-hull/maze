(ns maze.server
  (:require [noir.server :as server]
            [noir.fetch.remotes :as remotes]
            [ring.middleware.gzip :as deflate]))

(server/load-views "src/maze/views")

(defn -main [& m]
  (alter-var-root #'*read-eval* (constantly false))
  (let [mode (keyword (or (first m) :dev))
        port (read-string (get (System/getenv) "PORT" "8080"))]
    (server/add-middleware deflate/wrap-gzip)
    (server/start port {:mode mode
                        :ns 'maze})))


(ns maze.views.index
  (:use [noir.core :only [defpage defpartial]]
        [noir.fetch.remotes :only [defremote]]
        [hiccup.core :only [html]] 
        [hiccup.page :only [include-css include-js html5]]
        [hiccup.element :only [javascript-tag]]
        [maze.generator]
        [maze.solver]
    :require [clojure.core.cache :as cache]))

(def C (atom (cache/fifo-cache-factory {})))

(defmulti to-number class)
(defmethod to-number Number [n] n)
(defmethod to-number :default [obj] (binding [*read-eval* false] (read-string obj)))

; When using {:optimizations :whitespace}, the Google Closure compiler combines
; its JavaScript inputs into a single file, which obviates the need for a "deps.js"
; file for dependencies.  However, true to ":whitespace", the compiler does not remove
; the code that tries to fetch the (nonexistent) "deps.js" file.  Thus, we have to turn
; off that feature here by setting CLOSURE_NO_DEPS.
;
; Note that this would not be necessary for :simple or :advanced optimizations.
(defn include-clojurescript [path]
  (list
    (javascript-tag "var CLOSURE_NO_DEPS = true;")
      (include-js path)))

(defpartial layout [& content]
  (html5
    [:head
     [:title "Maze Generator"]
     (include-css "/css/default.css")
     (include-css "/css/spinner.css")
     (include-css "/css/ribbon.css")
     (include-js "https://ajax.googleapis.com/ajax/libs/jquery/1.9.0/jquery.min.js")]
    [:body
     [:div#wrapper
      content]
      (include-clojurescript "/cljs/maze.js")
     ]))

(defpartial spinner [css-class]
  (html
    [:div#spinner {:class css-class }
      [:div {:class "spinner"}
       (for [x (range 1 13)]
          (html 
            [:div {:class (str "bar" x)}]))]]))

(defpartial ribbon [text href]
  (html
    [:div#ribbon
      [:p
        [:a {:href href :title href :rel "me"} text]]]))

(defremote generate-maze [width height]
  (let [w    (to-number width)
        h    (to-number height)
        id   (java.util.UUID/randomUUID)
        maze (assoc (create-maze rand-int w h) :id id)]
    (swap! C cache/miss id maze)
    maze))

(defremote solve [id points]
  (let [maze (cache/lookup @C id)
        f (fn [[x y]] (shortest-path maze x y))]
    (vec (pmap f points))))

(defpage [:get "/"] {:as params}
  (layout
    (html
      [:div
        (spinner "container grey")
        (ribbon "Fork me on GitHub!" "https://github.com/rm-hull/maze")
        [:canvas#world 
          { :data-cell-size (get params :cell-size 10)
            :data-draw      (get params :draw "")
            :data-count     (get params :count 1) }]])))

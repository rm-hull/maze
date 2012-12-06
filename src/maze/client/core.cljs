(ns maze.client.core
  (:require [fetch.remotes :as remotes])
  (:require-macros [fetch.macros :as fm])
  (:use [monet.canvas :only [get-context stroke stroke-style stroke-cap begin-path close-path line-to move-to stroke-width]]
        [jayq.core :only [$ document-ready data attr]]))

(defn coord->pos [[^long x ^long y] [^long w ^long h]]
  (+ 
    (* (rem y h) w)
    (rem x w)))

(defn draw-path-segments [ctx maze cell-size start end]
  (let [[w h] (:size maze)
        offset (inc (quot cell-size 2))]
    (doseq [p (subvec (:path maze) start end)
            :let [x (rem p w)
                  y (rem (quot p w) h)]]
      (line-to ctx (+ (* x cell-size) offset) (+ (* y cell-size) offset)))
    ctx)) ; important to return ctx for threading

(defn eraser [ctx maze cell-size color p]
  (if (and (>= p 0) (< p (dec (apply * (:size maze)))))
    (-> ctx
       (stroke-style color)
       (begin-path)
       (draw-path-segments maze cell-size p (+ p 2))
       (stroke)
       (close-path)))
  ctx) ; important to return ctx for threading

(defn draw-snake [ctx maze cell-size color erase-color start end]
  (-> ctx
      (stroke-width 4)
      (stroke-cap "square")
      (eraser maze cell-size erase-color (dec start))
      (stroke-style color)
      (begin-path)
      (draw-path-segments maze cell-size start end)
      (stroke)
      (close-path)))

(defn draw-cell [ctx x y cell-size walls]
  (let [x (inc (* x cell-size))
        y (inc (* y cell-size))]
    (when (:north walls) (-> ctx (move-to x y) (line-to (+ x cell-size) y)))
    (when (:west walls)  (-> ctx (move-to x y) (line-to x (+ y cell-size))))
    ctx)) ; important to return ctx for threading

(defn draw-cells [ctx maze cell-size]
  (let [[w h] (:size maze)]
    (doseq [[p walls] (map vector (iterate inc 0) (:data maze))
            :let [x (rem p w)
                  y (rem (quot p w) h)]]
      (draw-cell ctx x y cell-size walls)) 
    ctx)) ; important to return ctx for threading

(defn draw-maze [ctx maze cell-size]
  (let [[w h] (:size maze)]
    (-> ctx
        (stroke-width 2)
        (stroke-cap "square")
        (stroke-style "#606060")
        (begin-path)
        (move-to 0 (inc (* h cell-size)))
        (line-to (inc (* w cell-size)) (inc (* h cell-size)))
        (line-to (inc (* w cell-size)) 0)
        (draw-cells maze cell-size)   
        (stroke)
        (close-path))))
 
(def current-cell (atom 0))

(defn animate [ctx maze cell-size color erase-color snake-length]
  (letfn [(loop [] 
    (when (<= @current-cell (- (count (:path maze)) snake-length))
      (.  js/window (requestAnimFrame loop))
      (draw-snake ctx maze cell-size color erase-color @current-cell (+ @current-cell snake-length))
      (swap! current-cell inc)))]
    (do
      (reset! current-cell 0)
      (loop))))
  
(document-ready
  (fn []
    (let [div       ($ :div#wrapper)
          canvas    ($ :canvas#world)
          ctx       (get-context (.get canvas 0) "2d")
          cell-size (data canvas "cell-size")
          draw-cmd  (data canvas "draw")
          width     (quot (.-offsetWidth (first div)) cell-size)
          height    (quot (.-offsetHeight (first div)) cell-size)]
      (-> canvas
          (attr :width  (+ 2 (* cell-size width)))
          (attr :height (+ 2 (* cell-size height))))
      (fm/remote (generate-maze width height) [maze] 
        (draw-maze ctx maze cell-size)
        (case (str draw-cmd) 
          "path"  (draw-snake ctx maze cell-size "red" "red" 0 (count (:path maze)))
          "snake" (animate ctx maze cell-size "#55B95F" "white" 8)
          "trail" (animate ctx maze cell-size "#8182AE" "#E2E2F1" 8))))))

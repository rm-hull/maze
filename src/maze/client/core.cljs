(ns maze.client.core
  (:require [fetch.remotes :as remotes])
  (:require-macros [fetch.macros :as fm])
  (:use [monet.canvas :only [get-context stroke stroke-style stroke-cap begin-path close-path line-to move-to stroke-width]]
        [jayq.core :only [$ document-ready data attr]]))

(defn coord->pos [[^long x ^long y] [^long w ^long h]]
  (+ 
    (* (rem y h) w)
    (rem x w)))

(defn draw-cell [ctx x y len walls]
  (let [x (inc (* x len))
        y (inc (* y len))]
    (when (:north walls) (-> ctx (move-to x y) (line-to (+ x len) y)))
    (when (:west walls)  (-> ctx (move-to x y) (line-to x (+ y len))))
    ctx))

(defn draw-cells [ctx maze len]
  (let [[w h] (:size maze)]
    (doseq [[idx walls] (map vector (iterate inc 0) (:data maze))
            :let [x (rem idx w)
                  y (rem (quot idx w) h)]]
      (draw-cell ctx x y len walls)) 
    ctx))

(defn draw-maze [ctx maze len]
  (let [[w h] (:size maze)]
    (-> ctx
        (stroke-width 2)
        (stroke-cap "square")
        (stroke-style "#606060")
        (begin-path)
        (move-to 0 (inc (* h len)))
        (line-to (inc (* w len)) (inc  (* h len)))
        (line-to (inc  (* w len)) 0)
        (draw-cells maze len)   
        (stroke)
        (close-path))
    ctx))

(document-ready
  (fn []
    (let [div       ($ :div#wrapper)
          canvas    ($ :canvas#world)
          ctx       (get-context (.get canvas 0) "2d")
          cell-size (data canvas "cell-size")
          width     (quot (.-offsetWidth (first div)) cell-size)
          height    (quot (.-offsetHeight (first div)) cell-size)]
      (-> canvas
          (attr :width (+ 2 (* cell-size width)))
          (attr :height (+ 2 (* cell-size height))))
      (fm/remote (generate-maze width height) [maze] 
        (draw-maze ctx maze cell-size)))))

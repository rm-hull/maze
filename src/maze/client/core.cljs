(ns maze.client.core
  (:require [fetch.remotes :as remotes])
  (:require-macros [fetch.macros :as fm])
  (:use [monet.canvas :only [get-context fill-style circle rect alpha begin-path close-path fill]]
        [jayq.core :only [$ document-ready data attr]]))

(defn coord->pos [[^long x ^long y] [^long w ^long h]]
  (+ 
    (* (rem y h) w)
    (rem x w)))

(defn north [ctx x y len]
  (rect ctx {:x (* x len) :y (* y len) :w (+ len 2) :h 2}))

(defn west [ctx x y len]
  (rect ctx {:x (* x len) :y (* y len) :w 2 :h (+ len 2)}))

(defn draw [ctx maze len]
  (let [[w h] (:size maze)]
    (.log js/console maze) 
    (-> ctx
        (fill-style "#606060")
        (rect {:x 0 :y (* h len) :w (+ (* w len) 2) :h 2})
        (rect {:x (* w len) :y 0 :w 2 :h (* h len)})
      )
    (doseq [[idx walls] (map vector (iterate inc 0) (:data maze))
            :let [x (rem idx w)
                  y (rem (quot idx w) h)]]
      (if (:north walls) (north ctx x y len))
      (if (:west  walls) (west  ctx x y len)))))

(document-ready
  (fn []
    (let [canvas    ($ :canvas#world)
          ctx       (get-context (.get canvas 0) "2d")
          cell-size (data canvas "cell-size")
          width     (- (.-innerWidth js/window) 30)
          height    (- (.-innerHeight js/window) 30)]
      (-> canvas
          (attr :width width)
          (attr :height height))
      (fm/remote 
        (generate-maze 
          (quot (- width 2) cell-size) 
          (quot (- height 2) cell-size))
        [maze] (draw ctx maze cell-size)))))

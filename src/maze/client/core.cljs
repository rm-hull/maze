(ns maze.client.core
  (:require [fetch.remotes :as remotes])
  (:require-macros [fetch.macros :as fm])
  (:use [monet.canvas :only [get-context stroke stroke-style stroke-cap begin-path close-path line-to move-to stroke-width]]
        [jayq.core :only [$ document-ready data attr hide]]))

(defn coord->pos [[^long x ^long y] [^long w ^long h]]
  (+ 
    (* (rem y h) w)
    (rem x w)))

(defn draw-path-segments [ctx snake start end]
  (let [[w h] (get-in snake [:maze :size])
        cell-size (get-in snake [:cell-size])
        offset (inc (quot cell-size 2))]
    (doseq [p (subvec (:path snake) start end)
            :let [x (rem p w)
                  y (rem (quot p w) h)]]
      (line-to 
        ctx 
        (+ (* x cell-size) offset) 
        (+ (* y cell-size) offset)))
    ctx)) ; important to return ctx for threading

(defn eraser [ctx snake p]
  (if (and (>= p 0) (< p (:limit snake)))
    (-> ctx
       (stroke-style (:erase-color snake))
       (begin-path)
       (draw-path-segments snake p (+ p 2))
       (stroke)
       (close-path)))
  ctx) ; important to return ctx for threading
 
(defn draw-snake [ctx snake]
   ;(.log js/console (pr-str "draw-snake" ctx snake))
  (let [start  (deref (:counter snake))
        end    (+ start (:snake-length snake))]
  (-> ctx
      (stroke-width 4)
      (stroke-cap "square")
      (eraser snake (dec start))
      (stroke-style (:color snake))
      (begin-path)
      (draw-path-segments snake start end)
      (stroke)
      (close-path))))

(defn draw-cells [ctx maze cell-size]
  (let [[w h] (:size maze)]
    (doseq [[p walls] (map vector (iterate inc 0) (:data maze))
            :let [x (inc (* cell-size (rem p w)))
                  y (inc (* cell-size (rem (quot p w) h)))]]
      (when (:north walls) (-> ctx (move-to x y) (line-to (+ x cell-size) y)))
      (when (:west walls)  (-> ctx (move-to x y) (line-to x (+ y cell-size)))))
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

(defn create-snake [ctx maze callback-fn & snake-attrs]
    (doseq [attrs snake-attrs]
      (fm/letrem [m (solve (:id maze) [[(:start attrs) (:end attrs)]])]
        ;(.log js/console (pr-str "create-snake" m))
        (let [path (get-in m [:paths 0])
              snake-length (get attrs :snake-length (count path))]
        (callback-fn 
          ctx 
          (-> (assoc attrs
                :maze m 
                :path path
                :counter (atom 0) 
                :snake-length snake-length
                :limit (- (count path) snake-length 1))))))))
  
(defn reset-snake [ctx snake callback-fn]
  (let [start (nth (get-in snake [:maze :path]) @(:counter snake))
        end   (rand-int (dec (apply * (get-in snake [:maze :size]))))]
    (create-snake ctx (:maze snake) callback-fn (assoc snake :start start :end end))))

(defn animate [ctx snake]
  (letfn [(loop [] 
            (if (< @(:counter snake) (:limit snake))
              (do
                (. js/window (requestAnimFrame loop))
                (draw-snake ctx snake)
                (swap! (:counter snake) inc))
              ;(reset-snake ctx snake animate)
              ))]
     (loop)))

(defn random-snakes [cell-size limit n]
  (->> (cycle ["#55B95F" "red" "#8182AE" "#AC85B5" "orange" "yellow"])
       (map #(hash-map :start (rand-int limit) :end (rand-int limit) :cell-size cell-size :color % :erase-color "white" :snake-length 8))
       (take n)))

(document-ready
  (fn []
    (let [div       ($ :div#wrapper)
          canvas    ($ :canvas#world)
          ctx       (get-context (.get canvas 0) "2d")
          cell-size (data canvas "cell-size")
          draw-cmd  (data canvas "draw")
          n         (data canvas "count")
          width     (quot (.-offsetWidth (first div)) cell-size)
          height    (quot (.-offsetHeight (first div)) cell-size)
          limit     (dec (* width height))]
      (-> canvas
          (attr :width  (+ 2 (* cell-size width)))
          (attr :height (+ 2 (* cell-size height))))
      (fm/remote (generate-maze width height) [maze] 
        (draw-maze ctx maze cell-size)
        (case (str draw-cmd) 
          "path"  (create-snake ctx maze draw-snake {:start 0 :end limit :cell-size cell-size :color "red" :erase-color "red"})
          "snail" (create-snake ctx maze animate {:start (rand-int limit) :end (rand-int limit) :cell-size cell-size :color "#8182AE" :erase-color "#E2E2F1" :snake-length 3})
          "snake" (apply (partial create-snake ctx maze animate) (random-snakes cell-size limit n))
          nil)
        (hide ($ :div#spinner))))))

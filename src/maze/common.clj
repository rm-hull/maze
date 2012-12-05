(ns maze.common)

(defn coord->pos 
  "Converts [x,y] co-ords into an offset into the maze data"
  [[^long x ^long y] [^long w ^long h]]
  (+ 
    (* (rem y h) w)
    (rem x w)))

(defmacro add-if [pred then-clause xs]
  `(if ~pred 
     (cons ~then-clause ~xs)
     ~xs))

(def neighbours
  (memoize
    (fn [^long p [^long w ^long h]]
      (->> [(- p w) (+ p w)] 
           (add-if (> (rem p w) 0) (dec p))
           (add-if (< (rem p w) (dec w)) (inc p))
           (filter #(and (>= % 0) (< % (* w h))))))))

(defn wall-between? 
  "Checks to see if there is a wall between two (adjacent) points in the
   maze. The return value will indicate the wall type (:north, :west, ..).
   If the points aren't adjacent, nil is returned."
  [maze ^long p1 ^long p2]
  (if (> p1 p2)
    (wall-between? maze p2 p1)
    (let [[w h] (:size maze)]
    (cond 
      (= (- p2 p1) w) (:north ((:data maze) p2))
      (= (- p2 p1) 1) (:west  ((:data maze) p2))))))

(defn print-maze [maze & [path]]
  (let [[w h] (:size maze)
        path (set path)
        wall-at (fn [x y] ((:data maze) (coord->pos [x y] (:size maze))))]
  (doseq [y (range h)] 
    (doseq [x (range w)] 
      (print "+") 
      (print (if (:north (wall-at x y)) "---" "   ")))
    (println "+")
    (doseq [x (range w)] 
      (print (if (:west  (wall-at x y)) "|" " ")) 
      (print (if (path (coord->pos [x y] (:size maze))) " # " "   ")))
    (println "|")) 
  (doseq [x (range w)] 
    (print "+---")) 
  (println "+")))

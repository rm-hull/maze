(ns maze.generator)

(defn coord->pos [[^long x ^long y] [^long w ^long h]]
  (+ 
    (* (rem y h) w)
    (rem x w)))

(defn pos->coord [^long p [^long w ^long h]]
  [(rem p w) (rem (quot p w) h)])

(defn- init-maze
  "Create an initial maze of size (W x H) with all walls set"
  [^long w ^long h]
  { :size [w h]
    :data (into [] (repeat (* w h) #{ :north :west } ))})

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

(defn wall-between? [maze ^long p1 ^long p2]
  (if (> p1 p2)
    (wall-between? maze p2 p1)
    (let [[w h] (:size maze)]
    (cond 
      (= (- p2 p1) w) (:north ((:data maze) p2))
      (= (- p2 p1) 1) (:west  ((:data maze) p2))))))

(defn knockdown-wall [maze ^long p1 ^long p2]
  (if (> p1 p2)
    (knockdown-wall maze p2 p1)
    (let [[w h] (:size maze)
          new-walls (cond 
                      (= (- p2 p1) w) (disj ((:data maze) p2) :north)
                      (= (- p2 p1) 1) (disj ((:data maze) p2) :west))
          new-data (assoc (:data maze) p2 new-walls)]
      (assoc maze :data new-data))))

(defn generate-maze [visitor-fn ^long w ^long h]
  (loop [maze    (init-maze w h)
         visited {0 true}
         stack   [0]]
    (if (empty? stack)
      maze
      (let [n (remove visited (neighbours (peek stack) (:size maze)))]
        (if (empty? n)
          (recur maze visited (pop stack))
          (let [np (nth n (visitor-fn (count n)))
                st (if (= (count n) 1) (pop stack) stack)]
            (recur 
              (knockdown-wall maze (peek stack) np)
              (assoc visited np true)
              (conj st np))))))))

(defn print-maze [maze]
  (let [[w h] (:size maze)
        wall-at (fn [x y] ((:data maze) (coord->pos [x y] (:size maze))))]
  (doseq [y (range h)] 
    (doseq [x (range w)] 
      (print "+") 
      (print (if (:north (wall-at x y)) "---" "   ")))
    (println "+")
    (doseq [x (range w)] 
      (print (if (:west  (wall-at x y)) "|" " ")) 
      (print  "   "))
    (println "|")) 
  (doseq [x (range w)] 
    (print "+---")) 
  (println "+")))

(print-maze (generate-maze rand-int 20 20))

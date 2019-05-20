(ns drunkards-walk
  (:require [map-drawing :as md]))

(defn bound-between [number lower upper]
  (cond
    (< number lower) lower
    (> number upper) upper
    :else            number))

(defn bound [height width [y x]]
  [(bound-between y 0 height) (bound-between x 0 width)])

(defn drunkards-walk
  ([grid number-of-empty-cells]
   (let [height          (count grid)
         width           (count (first grid))
         number-of-cells (* height width)
         point           [(rand-int height) (rand-int width)]]
     (when (> number-of-cells number-of-empty-cells)
       (drunkards-walk grid number-of-empty-cells point 0))))
  ([grid number-of-empty-cells point empty-cell-count]
   (if (= empty-cell-count number-of-empty-cells)
     grid
     (let [cell-was-full? (= (get-in grid point :full))
           direction      (rand-nth [[1 0] [0 1] [-1 0] [0 -1]])]
       (recur (if cell-was-full? (assoc-in grid point :empty) grid)
              number-of-empty-cells
              (bound (count grid) (count (first grid)) (map + point direction))
              (if cell-was-full? (inc empty-cell-count) empty-cell-count))))))

(defn draw []
  (-> (md/full-grid 20 20)
      (drunkards-walk 200)
      md/draw-grid))

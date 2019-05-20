(ns drunkards-walk
  (:require [map-drawing :as md]))

(defn bound-between [number lower upper]
  (cond
    (< number lower)  lower
    (>= number upper) upper
    :else             number))

(defn bound [height width [y x]]
  [(bound-between y 0 height) (bound-between x 0 width)])

(defn drunkards-walk [grid number-of-empty-cells]
  (let [height          (count grid)
        width           (count (first grid))
        number-of-cells (* height width)]
    (when (> number-of-cells number-of-empty-cells)
      (loop [grid             grid
             point            [(rand-int height) (rand-int width)]
             empty-cell-count 0]
        (if (= empty-cell-count number-of-empty-cells)
          grid
          (let [cell-was-full? (= (get-in grid point) :full)
                direction      (rand-nth [[1 0] [0 1] [-1 0] [0 -1]])]
            (recur (if cell-was-full? (assoc-in grid point :empty) grid)
                   (bound height width (map + point direction))
                   (if cell-was-full? (inc empty-cell-count) empty-cell-count))))))))

(defn draw []
  (-> (md/full-grid 20 20)
      (drunkards-walk 100)
      md/draw-grid))

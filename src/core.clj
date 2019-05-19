(ns core
  (:import [java.awt Color Dimension]
           [javax.swing JPanel JFrame]))

(def screen-square-size 10)

(defn grid->indexed-grid [grid]
  (map-indexed (fn [y row]
                 (map-indexed (fn [x type]
                                {:point [x y]
                                 :type  type})
                              row))
               grid))

(defn grid->screen-height [grid]
  (-> (count grid)
      (* screen-square-size)))

(defn grid->screen-width [grid]
  (-> (sort-by count grid)
      last
      count
      (* screen-square-size)))

(defn point->screen-square [[x y]]
  (map #(* screen-square-size %)
       [x y 1 1]))

(defn fill-point [graphic point color]
  (let [[x y width height] (point->screen-square point)]
    (.setColor graphic color)
    (.fillRect graphic x y width height)))

(defn fill-dungeon [graphic grid color]
  (run! (fn [row]
          (run! (fn [{:keys [point type]}]
                  (when (= type :full)
                    (fill-point graphic point color)))
                row))
        grid))

(defn dungeon [grid]
  (proxy [JPanel] []
    (paintComponent [graphic]
      (fill-dungeon graphic grid (Color/decode "#2E3440")))
    (getPreferredSize []
      (Dimension. (grid->screen-width grid)
                  (grid->screen-height grid)))))

(defn draw [grid]
  (let [frame (JFrame. "Dungeon")
        panel (dungeon grid)]
    (doto panel
      (.setFocusable true))
    (doto frame
      (.add panel)
      (.pack)
      (.setVisible true))))

(defn full-grid [width height]
  (vec (repeat height
               (vec (repeat width :full)))))

(comment (-> (full-grid 14 8)
             (assoc-in [1 1] :empty)
             (assoc-in [2 1] :empty)
             (assoc-in [3 1] :empty)
             (assoc-in [4 1] :empty)
             (assoc-in [5 1] :empty)
             (assoc-in [6 1] :empty)
             (assoc-in [1 2] :empty)
             (assoc-in [6 2] :empty)
             (assoc-in [2 3] :empty)
             (assoc-in [5 3] :empty)
             (assoc-in [3 4] :empty)
             (assoc-in [4 4] :empty)
             grid->indexed-grid
             draw))

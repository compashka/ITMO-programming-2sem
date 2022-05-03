(defn size-equals? [x]
  (apply == (mapv count x)))

(defn vector-correct? [x]
  (and (vector? x) (every? number? x)))

(defn matrix-correct? [x]
  (and (vector? x) (every? vector-correct? x) (size-equals? x)))

(defn shape-equals? [& x]
  (and (every? vector? x) (size-equals? x) (every? (partial mapv shape-equals?) x)))

(defn simplex-correct? [x]
  (or
    (vector-correct? x)
    (number? x)
    (and (every? simplex-correct? x) (= (range (count x) 0 -1) (mapv count x)))
    ))

(defn reverse-args [f] (fn [a b] (f b a)))

(defn simplex [f] (fn [& x] (if (vector? (first x)) (apply (partial mapv (simplex f)) x) (apply f x))))

(defn operation [pre-cond post-cond] (fn [f] (fn [& x] {:pre  [(and (pre-cond x) (size-equals? x))]
                                                        :post [(post-cond %)]}
                                               (apply mapv f x))))

(def vector-operation (operation (fn [v] (every? vector-correct? v)) (fn [v] (vector-correct? v))))
(def matrix-operation (operation (fn [m] (every? matrix-correct? m)) (fn [m] (matrix-correct? m))))
(defn simplex-operation [f] ((operation (fn [s] (and (every? simplex-correct? s) (apply shape-equals? s)))
                                        (fn [s] (simplex-correct? s))) (simplex f)))

(def v+ (vector-operation +))
(def v- (vector-operation -))
(def v* (vector-operation *))
(def vd (vector-operation /))

(def m+ (matrix-operation v+))
(def m- (matrix-operation v-))
(def m* (matrix-operation v*))
(def md (matrix-operation vd))

(def x+ (simplex-operation +))
(def x- (simplex-operation -))
(def x* (simplex-operation *))
(def xd (simplex-operation /))

(defn scalar [& x] {:pre  [(every? vector-correct? x)]
                    :post [(number? %)]}
  (reduce + (reduce v* x)))

(defn vect [& xs] {:pre  [(every? vector-correct? xs) (every? (partial = 3) (mapv count xs))]
                   :post [(vector-correct? %)]}
  (reduce (fn [x y] (vector
                      (- (* (nth x 1) (nth y 2)) (* (nth y 1) (nth x 2)))
                      (- (* (nth y 0) (nth x 2)) (* (nth x 0) (nth y 2)))
                      (- (* (nth x 0) (nth y 1)) (* (nth y 0) (nth x 1))))) xs))

(defn v*s [v & s] {:pre  [(vector-correct? v) (every? number? s)]
                   :post [(vector-correct? %)]}
  (mapv (partial * (reduce * s)) v))

(defn m*s [m & s] {:pre  [(matrix-correct? m) (every? number? s)]
                   :post [(matrix-correct? %)]}
  (mapv (partial (reverse-args v*s) (reduce * s)) m))

(defn m*v [m v] {:pre [(matrix-correct? m) (vector-correct? v)]}
  (mapv (partial (reverse-args scalar) v) m))

(defn transpose [x] {:pre [(matrix-correct? x)]}
  (apply mapv vector x))

(defn m*m [& xs] {:pre [(every? matrix-correct? xs)]}
  (reduce (fn [x y] {:pre [(size-equals? [(first x) y])]}
            (transpose (mapv (partial m*v x) (transpose y)))) xs))
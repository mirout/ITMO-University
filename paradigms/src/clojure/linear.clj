(defn vectors-equal? [& vs] (apply = (mapv count vs)))
(defn matrix-equal? [& ms] (and (vectors-equal? (mapv first ms)) (apply vectors-equal? ms)))

(defn is-vector? [v] (and (vector? v) (every? number? v)))
(defn is-matrix? [m] (and (vector? m) (every? is-vector? m) (vectors-equal? m)))
(defn is-simplex?
  ([x] (or (number? x) (is-simplex? x (count x))))
  ([x n] (or (every? number? x)
             (empty? x)
             (every? true? (map-indexed #(= (count %2) (- n %1)) x)))))

(defn type-checker [type-check? size-equal?] (fn [& as] (and (every? type-check? as) (apply size-equal? as))))

(def vector-checker (type-checker is-vector? vectors-equal?))
(def matrix-checker (type-checker is-matrix? matrix-equal?))
(def simplex-checker (type-checker is-simplex? vectors-equal?))

(defn op [pred] (fn [f] (fn [& as] {
                              :pre [(apply pred as)]
                              } (apply mapv f as))))

(def v (op vector-checker))
(def v+ (v +))
(def v* (v *))
(def v- (v -))
(def vd (v /))

(defn v*s [v & s] {
                   :pre [(is-vector? v) (every? number? s)]
                   } (let [sc (apply * v)] (mapv #(* sc %) v)))
(defn scalar [& v] {
                    :pre [(apply vector-checker v)]
                    } (apply + (apply v* v)))

(defn vect [& v] {:pre [(apply vector-checker v)
                        (== 3 (count (first v)))]}
  (
    reduce #(
              vector (- (* (nth %1 1) (nth %2 2)) (* (nth %1 2) (nth %2 1)))
                     (- (* (nth %1 2) (nth %2 0)) (* (nth %1 0) (nth %2 2)))
                     (- (* (nth %1 0) (nth %2 1)) (* (nth %1 1) (nth %2 0)))
                     ) v
           ))

(def m (op matrix-checker))
(def m+ (m v+))
(def m* (m v*))
(def m- (m v-))
(def md (m vd))

(defn m*v [m & v] {
                   :pre [(is-matrix? m) (apply vector-checker v)]
                   } (mapv #(apply scalar % v) m))
(defn m*s [m & s] {
                   :pre [(and (is-matrix? m) (every? number? s))]
                   } (let [sc (apply * s)] (mapv #(v*s % sc) m)))

(defn transpose [m] {
                     :pre [(is-matrix? m)]
                     } (apply mapv vector m))
(defn m*m [& m] (reduce (fn [a b] {
                                   :pre [(is-matrix? a)
                                         (is-matrix? b)
                                         (vectors-equal? (first a) (first (transpose b)))]
                                   } (transpose (mapv (partial m*v a) (transpose b)))) m))



(defn x [f] (fn xf [& ts] {
                           :pre [(or (every? number? ts)
                                     (apply simplex-checker ts))]
                           } (if (number? (first ts))
                               (apply f ts)
                               (apply mapv xf ts))))

(def x+ (x +))
(def x* (x *))
(def x- (x -))
(def xd (x /))
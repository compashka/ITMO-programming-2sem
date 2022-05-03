(defn makeOperation [oper] (fn [& func] (fn [args] (apply oper (mapv (fn [f] (f args)) func)))))

(def constant constantly)
(defn variable [name] (fn [args] (args name)))
(def add (makeOperation +))
(def subtract (makeOperation -))
(def negate subtract)
(def multiply (makeOperation *))
(def divide (makeOperation (fn ([x] (/ 1.0 (double x)))
                             ([x & args] (reduce #(/ (double %1) (double %2)) x args)))))
(defn mean-calc [& args] (/ (double (apply + args)) (count args)))
(defn sqr-calc [x] (* x x))
(def mean (makeOperation mean-calc))
(def varn (makeOperation (fn [& args] (- (apply mean-calc (map sqr-calc args)) (sqr-calc (apply mean-calc args))))))

(def operations
  {'+      add,
   '-      subtract,
   '*      multiply,
   '/      divide,
   'negate negate,
   'mean   mean,
   'varn   varn
   })

(defn parseFunction [expression]
  (letfn [(parse-token [token]
            (cond
              (list? token) (apply (operations (first token)) (mapv parse-token (rest token)))
              (number? token) (constant token)
              :else (variable (str token))
              ))]
    (parse-token (read-string expression))))
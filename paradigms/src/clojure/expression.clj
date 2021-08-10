(defn abstract-op [f]
      (fn [& operands]
          (fn [variables]
              (apply f (mapv #(% variables) operands)))))

(def constant constantly)
(defn variable [name] (fn [arguments] (arguments name)))

(defn div-op
      ([x] (/ 1 (double x)))
      ([x & other] (/ x (double (apply * other)))))
(defn avg-op [& x] (/ (apply + x) (count x)))

(def add (abstract-op +))
(def subtract (abstract-op -))
(def multiply (abstract-op *))
(def divide (abstract-op div-op))

(def negate subtract)
(def sum add)
(def avg (abstract-op avg-op))

(def operations-functional
  {
   '+      add
   '-      subtract
   '*      multiply
   '/      divide
   'negate negate
   'sum    sum
   'avg    avg
   })


(defn parse [operations const variable-function]
      (fn [expression]
          (-> expression
              (read-string)
              ((fn parse [expr] (cond
                                  (seq? expr) (apply (operations (first expr)) (map parse (rest expr)))
                                  (number? expr) (const expr)
                                  (symbol? expr) (variable-function (str expr))
                                  ))))))

(def parseFunction (parse operations-functional constant variable))

;HW 10

(definterface Expression
              (evaluate [variables])
              (diff [variable])
              (toStringInfix []))

(defn evaluate [expression var] (.evaluate expression var))
(defn diff [expression var] (.diff expression var))
(defn toString [expression] (.toString expression))
(defn toStringInfix [expression] (.toStringInfix expression))

(declare ZERO)

(deftype ConstantClass [value]
         Expression
         (evaluate [_ _] value)
         (diff [_ _] ZERO)
         (toStringInfix [this] (toString this))
         Object
         (toString [_] (format "%.1f" (double value))))

(defn Constant [value] (ConstantClass. value))
(def ZERO (Constant 0))
(def ONE (Constant 1))

(deftype VariableClass [name]
         Expression
         (evaluate [_ variables] (variables (str (Character/toLowerCase (char (first name))))))
         (diff [_ variable] (if (= name variable) ONE ZERO))
         (toStringInfix [this] (toString this))
         Object
         (toString [_] name))

(defn Variable [value] (VariableClass. value))

(deftype OperationProto [sign f derivative])

(deftype AbstractOperation [operation args]
         Expression
         (evaluate [_ variables] (apply (.f operation) (map #(.evaluate % variables) args)))
         (diff [_ variable] ((.derivative operation) args (map #(.diff % variable) args)))
         (toStringInfix [_] (if (== (count args) 1)
                              (str (.sign operation) "(" (toStringInfix (first args)) ")")
                              (str "(" (toStringInfix (first args)) " " (.sign operation) " " (toStringInfix (second args)) ")")))

         Object
         (toString [_] (str "(" (.sign operation) " " (clojure.string/join " " (map #(toString %) args)) ")")))

(defn operation-constructor [sign f derivative]
      (let [proto (OperationProto. sign f derivative)]
           (fn [& args] (AbstractOperation. proto args))))

(defmacro deriv [f] `#(apply ~f %2))

(def Add (operation-constructor
           '+
           +
           (deriv Add)))

(def Sum (operation-constructor
           'sum
           +
           (deriv Sum)))

(def Subtract (operation-constructor
                '-
                -
                (deriv Subtract)))

(def Negate (operation-constructor
              'negate
              -
              (deriv Negate)))

(def IPow (operation-constructor
            '**
            #(Math/pow %1 %2)
            nil))

(def ILog (operation-constructor
            (symbol "//")
            #(/ (Math/log (Math/abs %2)) (Math/log (Math/abs %1)))
            nil))


; :NOTE: (perm [] [x1 x2 x3] [d1 d2 d3]) -> [[d1 x2 x3] [x1 d2 x3] [x1 x2 d3]]
(defn perm [fas [t & tas :as poa] [b & bs]]
      (if (empty? poa)
        (list)
        (conj
          (perm (conj fas t) tas bs)
          (concat fas (vector b) tas))))

(def Multiply (operation-constructor
                '*
                *
                (fn [xs dxs] (apply Add (mapv #(apply Multiply %) (perm [] xs dxs))))))
(def Divide (operation-constructor
              '/
              div-op
              (fn [xs dxs]
                  (if (== (count xs) 1)
                    (Divide (apply Negate dxs) (apply Multiply (concat xs xs)))
                    (Divide
                      (apply Subtract (mapv #(apply Multiply %) (perm [] xs dxs)))
                      (apply Multiply (mapv #(Multiply % %) (rest xs))))))))

(def Avg (operation-constructor
           'avg
           avg-op
           (deriv Avg)))

(def operations-object
  {
   '+            Add
   '-            Subtract
   '*            Multiply
   '/            Divide
   'negate       Negate
   'sum          Sum
   'avg          Avg
   '**           IPow
   (symbol "//") ILog})

(def parseObject (parse operations-object Constant Variable))

;HW 11
(load-file "parser.clj")

(def *chars (mapv char (range 0 128)))
(def *space (+char (apply str (filter #(Character/isWhitespace (char %)) *chars))))
(def *ws (+ignore (+star *space)))
(def *digit (+char (apply str (filter #(Character/isDigit (char %)) *chars))))
(defn *string [st] (+str (apply +seq (map (comp +char str) (seq st)))))

; {+-}?[0-9]+(.[0-9]+)?
(def *number (+map (comp Constant read-string)
                   (+str (+seq
                           (+opt (+char "+-"))
                           (+str (+plus *digit))
                           (+opt (+str (+seq
                                         (+char ".")
                                         (+str (+plus *digit)))))))))

(def *variable (+map Variable (+str (+plus (+char "xyzXYZ")))))

(def *num-or-var (+or *variable *number))
(declare *expr)
(declare *value)

(defn *abstract-unary [string] (+map (operations-object (symbol string)) (+seqn 1 *ws (*string string) *ws (delay *value))))

(def *value (+or *num-or-var
                 (*abstract-unary "negate")
                 (+seqn 1 *ws (+char "(") *ws (delay *expr) *ws (+char ")"))))

(defn fold [ass] (fn [expr]
                     (let [f (if ass identity reverse)
                           expr-last (f expr)]
                          (reduce
                            (fn [val now] (let [arg (f [val (second now)])
                                                op (operations-object (symbol (str (first now))))]
                                               (op (first arg) (second arg))))
                            (first expr-last) (partition 2 (rest expr-last))))))

(defn *operation [sign] (apply +or (map *string sign)))

(defn *binary-operation [next f & sign] (+map (fold f)
                                              (+seqf cons *ws next *ws
                                                     (+map (partial apply concat)
                                                           (+star (+seq *ws (*operation sign) *ws next *ws))))))

; :NOTE: infix-left
(def *pow-log (*binary-operation *value false "**" "//"))
(def *term (*binary-operation *pow-log true "*" "/"))
(def *sum (*binary-operation *term true "+" "-"))
(def *expr *sum)

(def parseObjectInfix (+parser *expr))
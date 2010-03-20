(ns simple.expression
  (:gen-class
   :state state
   :init init
   :methods [#^{:static true} [parseExpr [String] Object]
	     #^{:static true} [parseFn [Iterable String] Object]
	     [evalFn [Object Iterable] Object]
	     [evalExpr [Object] Object]
	     ;[evalExprAsBool [Object] boolean]
	     ;[evalExprAsInt [Object] int]
	     ;[evalExprAsStr [Object] String]
	     ]))

(defn -init [environment]
  [[] environment])

; Expect to rebind this any time that it is used.
(def *environment* {})

(def *base-env*
     (let [and-fn (fn [& args] (reduce #(and %1 %2) true args))
	   or-fn (fn [& args] (reduce #(or %1 %2) false args))
	   none (fn [& args] (not (apply or-fn args)))
	   ]

       {"+" +, "-" -, "*" *, "/" /,

	">" >, ">=" >=, "=" =, "<=" <=, "<" <,

	; "contains" is true for all three cases:
	;    (contains "hello world" "world")
	;    (contains [1 2 3] 2)
	;    (contains [1 2 3] [1 3])
	"contains" #(or (.contains %1 %2)
			(if (instance? java.util.Collection %2)
			  (.containsAll %1 %2)
			  false)),

	"all" and-fn, "and" and-fn, 
	"or" or-fn, "some" or-fn, "any" or-fn
	"not" none, "none" none,

	"get" get
	}))

(defn -parseExpr [expr-string]
  (read-string expr-string))

; TODO: This should return an IFn 
(defn -parseFn [fn-args fn-string]
  {:type :function,
   :args (vec fn-args),
   :body (read-string fn-string)})

(defn evaluate 
  ([environment expr] (binding [*environment* environment] (evaluate expr)))
  ([expr] (cond (symbol? expr) (let [val (get *environment* (str expr))]
				 (if (nil? val)
				   (throw (new RuntimeException
					       (str "Unrecognized symbol: "
						    expr)))
				   (recur val)))
		(vector? expr) (vec (map evaluate expr))
		(seq? expr) (apply (evaluate (first expr))
				   (map evaluate (rest expr)))
		:default expr)))

(defn -evalExpr [this expr]
  (evaluate (merge *base-env* 
		   (.state this))
	    expr))

; TODO: This should perform arg replacement and then call evalExpr
(defn -evalFn [this function args]
  false)


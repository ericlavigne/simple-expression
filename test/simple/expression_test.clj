(ns simple.expression-test
  (:use clojure.test
	simple.expression))

(defn simple-eval [expr]
  (evaluate *base-env* expr))

(deftest simple-eval-test
  (are [res expr] (= res (simple-eval expr))
       3 '(+ 1 2)
       3 '(+ 1 (+ 1 1))
       true '(contains "hello world" "llo w")
       false '(contains "hello world" "dog")
       true '(and true true true)
       false '(and true true false)
       false '(or false false)
       true '(or false true false)
       "z" '(get {"w" "x", "y" "z"} "y") 
       ))

(deftest environment-extension-test
  (is "z"
      (.evalExpr (new simple.expression {"letterMap" {"w" "x", "y" "z"}})
		 '(get letterMap "y"))))

(deftest eval-as-bool-test
  (are [res expr] (= res 
		     (.evalExprAsBool (new simple.expression {})
				      expr))
       true '(> 3 2)
       false '(< 3 2)
       true '(+ 1 1)
       false '(- 1 1)
       false []
       true [1]
       false nil
       false false))

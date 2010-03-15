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
       ))


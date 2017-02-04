(ns cbat.ch7.ex2
  (:gen-class))

(apply require clojure.main/repl-requires)



; Currently trying to work out how to exactly recur along the infix list - if there's more than one triplet, I need to recur
; the correct "next" list depending on whether there was a match or not, previously.

(defmacro infix [op inlist]
  (let [[i1 i2 & i3] inlist
        last-group? (nil? (second i3))]  

    ;(println i1 " " i2 " " i3 " " last-group? " islist: " (list? i3))
    (if last-group?
      ;"no more i3 - func call"
      `(if (= ~op ~i2)
        (~i2 ~i1 ~(first i3))
        (~i1 ~i2 ~(first i3)))
      `(if (= ~op ~i2)
         (infix ~op ~(conj (rest i3) (list i2 i1 (first i3)) ))
         (~i1 ~i2 (infix ~op ~i3))
         ))))

;; this is to emulate the final enclosing macro that will run infix for each operator - currently not evaluating anything returned :(
(defmacro test-infix-call-2
  [inlist-top]
  `(infix ~+ ~('infix '* 'inlist-top)))

(defmacro test-infix-call
  [op inlist-top]
  `(infix ~op ~inlist-top))



;; The version below uses an out-list, but doesn't work. Had some previous success, but
;; shelved in favour of no out-list at mo.
(defmacro infix-out-list 
  [op out-list in-list]
  (let [[i1 i2 & i3] in-list]
    (println i1 " " i2 " " i3 " " (empty? i3) " islist: " (list? i3) " rest i3: " (rest i3))
    (println (into out-list (reverse `(~i2 ~i1 ~(first i3)))))
    `(if (empty? (rest ~i3))
        (do (println "got to end cond" ) ~out-list)
        (if (= ~i2 ~op)
          ;(do nil ~(println "match"))
          (infix-out-list ~op ~(into out-list ( list (first i3) i1 i2)) ~i3)
          ))
    ))

;; Deprecated in favour of returning lists in-line
(defn process-triplet
    "Check if three args are an infix operation, if so return list in prefix notation."
      [test-op operand1 in-op operand2]
      (if (= test-op in-op)
        ;"match"
        `(~in-op ~operand1 ~operand2)
        ;"non-match"))
        `(~operand1 ~in-op ~operand2)))

(defmacro get-infix
    "Use this macro when you pine for the notation of your childhood"
      [[operand1 op operand2]]
        `(~op ~operand1 ~operand2))

(defn foo
  "I don't do a whole lot."
  [x]
  (println x "Hello, World!"))

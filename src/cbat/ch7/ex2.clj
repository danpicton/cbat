(ns cbat.ch7.ex2
  (:gen-class))

(apply require clojure.main/repl-requires)


;; Attempting to return back to an infix macro that creates a growing output list on each recur. I 
;; had tried this previously, but had issues with the evaluation side of things.
(defmacro infix 
  [op out-list in-list]
  (let [[i1 i2 & i3] in-list]
    (println i1 " " i2 " " i3 " " (empty? i3) " islist: " (list? i3) " rest i3: " (rest i3))
    (println (into out-list (reverse `(~i2 ~i1 ~(first i3)))))
    `(if (empty? (rest ~i3))
        (do (println "got to end cond" ) ~out-list)
        (if (= ~i2 ~op)
          ;(do nil ~(println "match"))
          (infix ~op ~(into out-list ( list (first i3) i1 i2)) ~i3)
          ))
    ))
  

; Currently trying to work out how to exactly recur along the infix list - if there's more than one triplet, I need to recur
; the correct "next" list depending on whether there was a match or not, previously.

(defmacro infix-no-outlist [op inlist]
  (let [[i1 i2 & i3] inlist
        last-group? (nil? (second i3))]  

    (println i1 " " i2 " " i3 " " last-group? " islist: " (list? i3))
    (if last-group?
      ;"no more i3 - func call"
      `(if (= ~op ~i2)
        (~i2 ~i1 ~(first i3))
        (~i1 ~i2 ~(first i3))
         )
      ;`(process-triplet ~op ~i1 ~i2 ~(first i3)) ; this seems to work, returning an unevaluated list (which is EXACTLY what ` does) regardless of which of its branches runs!! 
      ;"more i3 - recur infix with process triplet"
      ;`(infix ~op ~(into (process-triplet op i1 i2 (first i3)) (reverse (rest i3))))   ; this was initial line, retain 
      ;`(infix ~op ~(conj (process-triplet op i1 i2 (first i3)) (rest i3))) 
      `(if (= ~op ~i2)
         ;(do (println "gotmatch")(infix-no-outlist ~op ~(conj (process-triplet op i1 i2 (first i3)) (rest i3))))
         (do (println "gotmatch")(infix-no-outlist ~op ~(conj (list (second i3) (second (rest i3))) (process-triplet op i1 i2 (first i3)) )))
         (do (println "gothere") (~i1 ~i2 (infix-no-outlist ~op ~i3)))
         ) 
      ;(println (conj (process-triplet op i1 i2 (first i3)) (rest i3)))
     )
    ))

;; this is to emulate the final enclosing macro that will run infix for each operator - currently not evaluating anything returned :(
(defmacro test-infix-call
  [op inlist-top]
  `(infix ~op ~inlist-top))

;; This works, but I think it'll possible need the syntax quoting removed in the final version
;; there's definitely something I'm not fully grokking with macro expansion and read/eval.
;; It may actually be the case that I have to move the conditions out to the infix macro as initially suspected
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

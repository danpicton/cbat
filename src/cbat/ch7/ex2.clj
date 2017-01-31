(ns cbat.ch7.ex2
  (:gen-class))

(apply require clojure.main/repl-requires)


; Currently trying to work out how to exactly recur along the infix list - if there's more than one triplet, I need to recur
; the correct "next" list depending on whether there was a match or not, previously.

(defmacro infix [op inlist]
  (let [[i1 i2 & i3] inlist
        last-group? (nil? (second i3))]  

    (println i1 " " i2 " " i3 " " last-group?)
    (if last-group?
      ;"no more i3 - func call"
      (process-triplet op i1 i2 (first i3)) ; with the new comments below, this will need process-triplet logic too - can look at refactoring later
      "more i3 - recur infix with process triplet"
      ;process-triplet condition needs to be here instead:
      ;if there's a match, we want to recur the rearranged triplet in front of current i3 (i.e. in i1 pos for next call)
      ;if no match, we need to return uneval'ed list of i1 i2 and then recur i3

     )
    ))


; This will need to be have its logic put directly into infix function.
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

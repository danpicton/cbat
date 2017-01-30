(ns cbat.ch7.ex2
  (:gen-class))

(apply require clojure.main/repl-requires)

(defmacro infix [op inlist]
  (let [[i1 i2 & i3] inlist
        last-group? (nil? (second i3))]
    (println i1 " " i2 " " i3 " " last-group?)
    (if last-group?
      "no more i3 - func call"
     (if (= i2 op)
        "match - recur prefix"
        "no-match - recur unchanged infix")
     )
    ))



; Initial plan was to use process-triplet to determine if i2 from infix matched op and if so, change to prefix. When non-match happens though, this tries to return invalid list; going to put conditional in infix for now and see if I can weave this in later on (saves duplication of condition).
(defmacro process-triplet
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

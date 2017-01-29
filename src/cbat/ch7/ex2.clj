(ns cbat.ch7.ex2)

(defmacro infix [op inlist]
  (let [[i1 i2 & i3] inlist]
    (println i1 " " i2 " " i3)
    (if (not i3)
      "no more i3"
     (if (= i2 op)
        "match"
        "no-match"))
    ))

(defmacro get-infix
    "Use this macro when you pine for the notation of your childhood"
      [[operand1 op operand2]]
        `(~op ~operand1 ~operand2))

(defn foo
  "I don't do a whole lot."
  [x]
  (println x "Hello, World!"))

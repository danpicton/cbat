(ns cbat.ch7.ex2
  (:gen-class))

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



(defn foo
  "I don't do a whole lot."
  [x]
  (println x "Hello, World!"))

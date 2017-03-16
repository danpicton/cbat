(ns cbat.ch13.answers
  (:gen-class))

; exercise 1
(defmulti full-moon-behavior 
  (fn [were-creature] (:were-type were-creature)))

(defmethod full-moon-behavior :wolf
  [were-creature]
  (str (:name were-creature) " will howl and murder"))

(defmethod full-moon-behavior :simmons
  [were-creature]
  (str (:name were-creature) " will encourage people and sweat to the oldies"))

(defmethod full-moon-behavior :goat
  [were-creature]
  (str (:name were-creature) " will bleat angrily at the moon, or people"))


(full-moon-behavior {:were-type :goat :name "Cornelius the Wonder Goat"})

; exercise 2
(defprotocol WereCreature
  (full-moon-behaviour [x]))

(defrecord WereSimmons [name title]
  WereCreature
  (full-moon-behaviour [x] 
    (str name " will encourage people and sweat to the oldies, apparently.")))

(full-moon-behaviour (->WereSimmons "Simmons" "Celebrity Reference of Unknown Origin"))

; exercise 3
(defprotocol MyFirstProtocol
  (say-hello [x]))

(extend-protocol MyFirstProtocol
  java.lang.String
  (say-hello [x]
    (str "Oh, hello " x))

  java.lang.Number
  (say-hello [x]
    (str "I don't say anything to numbers, especially you, " x)))

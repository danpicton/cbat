(ns cbat.ch10.answers
  (:gen-class))

;; exercise 1
(def atom1 (atom 0))
@atom1 ; = 0

(doseq [a (range 0 3)] (swap! atom1 inc))
@atom1 ; = 3

;; exercise 2
(def quote-list (atom()))

(defn get-quote-list 
  "Retrieves parameterised number of quotes from 'http://www.braveclojure.com/random-quote' and returns the word count across all quotes"
  [quote-count]
  (let [atom-list (promise)]
    (doseq [a (range 0 quote-count)]
      (future 
        (let [curr-quote (slurp "http://www.braveclojure.com/random-quote")]
          (swap! quote-list conj curr-quote))))
    @quote-list))

(defn get-lazy-word-seq
  [word-list]
  (re-seq #"\w+" (apply str word-list)))

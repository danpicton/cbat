(ns cbat.ch10.answers
  (:gen-class))

;; exercise 1
(def atom1 (atom 0))
@atom1 ; = 0

(doseq [a (range 0 3)] (swap! atom1 inc))
@atom1 ; = 3

(def word-map {})

;; exercise 2
(def quote-list (atom()))

;; manually created list for starting things off
(def word-list '("this" "is" "a" "test" "this" "is" "not" "a" "test" "hello" "hello" "is" "this" "me" "you're" "looking" "for"))

(defn get-list-word-count
  "Counts frequency of words in words-in."
  [words-in]
  (reduce #(update-in %1 [%2] (fnil inc 0)) {} words-in))

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
  "Returns quote-list split into list of regex matched words."
  [quote-list]
  (re-seq #"\w+" (apply str quote-list)))

(defn quote-word-count
  "Retrieves number of quotes from 'http://www.braveclojure.com/random-quote' equal to quote-count and returns a map of the counts of each word. "
  [quote-count]
  (get-list-word-count (get-lazy-word-seq (get-quote-list quote-count))))

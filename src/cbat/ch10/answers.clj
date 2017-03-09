(ns cbat.ch10.answers
  (:gen-class))

;; exercise 1
(def atom1 (atom 0))
@atom1 ; = 0

(doseq [a (range 0 3)] (swap! atom1 inc))
@atom1 ; = 3

(def word-map {})

;; exercise 2

;; manually created list for starting things off
(def word-list '("this" "is" "a" "test" "this" "is" "not" "a" "test" "hello" "hello" "is" "this" "me" "you're" "looking" "for"))

(defn get-list-word-count
  "Counts frequency of words in words-in."
  [words-in]
  (reduce #(update-in %1 [%2] (fnil inc 0)) {} words-in))

(defn get-quote-list 
  "Retrieves parameterised number of quotes from 'http://www.braveclojure.com/random-quote' and returns the word count across all quotes"
  [quote-count]
  (let [quote-list (atom())]
    (doseq [a (range 0 quote-count)]
      (let [curr-quote (future (slurp "http://www.braveclojure.com/random-quote"))]
        (swap! quote-list conj @curr-quote)))
    @quote-list))

(defn get-lazy-word-seq
  "Returns quote-list split into list of regex matched words."
  [quote-list]
  (re-seq #"\w+" (apply str quote-list)))

(defn quote-word-count
  "Retrieves number of quotes from 'http://www.braveclojure.com/random-quote' equal to quote-count and returns a map of the counts of each word. "
  [quote-count]
  (get-list-word-count (get-lazy-word-seq (get-quote-list quote-count))))

;; exercise 3
;; This exercise is a bit vague as it seems like the second character should be giving the first a healing potion, but this isn't clear. Will assume char 2 gives healing potion and char 1 drinks and heals in same transaction.
(defn create-char
  "Returns a fresh character with items in inventory."
  [name & items]
  {:name name
   :hp 40
   :max-hp 40
   :inventory items}
  )

;; Create character refs
(def char-one (ref (create-char "Thandor")))
(def char-two (ref (create-char "Mork" "Healing potion" "Some other item" "Healing potion")))

(defn wound
  "Wounds target character for specified pow. Returns wounded character."
  [character pow]
  (dosync 
    (let [damage (- (:hp @character) pow)
          wound-hp (max 0 damage)]
      (alter character assoc-in [:hp] wound-hp))))


;; found in comment on:
;;  https://programming-puzzler.blogspot.co.uk/2010/07/translating-code-from-python-and-scheme.html
(defn remove-first 
  "Returns Cons of collection removing first instance of i."
  [i [first & rest]]
(cond
(nil? first) ()
(= i first) rest
:else (cons first (remove-first i rest))))


(defn heal
  "Healer heals healee. Returns healed character."
  [healer healee]
  (if (empty? (filter #(= "Healing potion" %) (:inventory @char-two)))
    (println "Nothing to heal with.")
    (dosync
      (alter healer assoc-in [:inventory] (remove-first "Healing potion" (:inventory @char-two)))
      (alter healee assoc-in [:hp] (:max-hp @healee))
      (alter healer assoc-in [:inventory] (conj (:inventory @healer) "Empty bottle")))))

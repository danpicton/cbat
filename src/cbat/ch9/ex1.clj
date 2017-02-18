(ns cbat.ch9.ex1
  (:require [pl.danieljanus.tagsoup :as html])
  (:gen-class))
(apply require clojure.main/repl-requires)

(def owlurl "https://www.bing.com/search#q=owls")
(def user-agent "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.22 (KHTML, like Gecko) Chrome/25.0.1364.172")


(defn get-google-url [search-term]
  (str "https://www.google.com/search?q=" search-term))
(defn get-bing-url [search-term]
  (str "https://www.bing.com/search#q=" search-term))

(defn search [search-term]
  (let [web-html (promise)]
    (doseq [url [get-google-url get-bing-url]]
      (future (if-let [html-string (slurp (url search-term))]
              (deliver web-html html-string))))
    @web-html))


(defn get-pretend []
  (html/parse-string pretend-webpage))

(defn extract-child-node [kw v-in]
  (let [[k tm & cn] v-in]
    (if (= kw k)
      (first cn)
      (recur kw cn)
    )))



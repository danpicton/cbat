(ns cbat.ch9.ex1
  (:require [pl.danieljanus.tagsoup :as html])
  (:gen-class))

(def owlurl "http://www.bing.com/search#q=owls")

(defn get-search-result [url]
  (let [search-html (future (slurp url))]
    (let [parsed-html (html/parse-string (str @search-html))]
      (println (:html :head parsed-html)))))

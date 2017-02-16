(ns cbat.ch9.ex1
  (:require [pl.danieljanus.tagsoup :as html])
  (:gen-class))
(apply require clojure.main/repl-requires)

(def owlurl "https://www.bing.com/search#q=owls")

(def pretend-webpage "<html><head><h1>Page Header</h1></head><body>Meow</body></html>")

;><li class="b_algo"><h2><a href="http://www.owlpages.com/owls/"

(defn get-search-result [url]
  (let [search-html (future (slurp url))]
    (let [parsed-html (html/parse-string (str @search-html))]
      (println (:html :head parsed-html)))))

(defn search [url]
  (let [web-html (promise)]
    (future (if-let [html-string (slurp url)]
              (do (Thread/sleep 3000) (deliver web-html html-string))))
    (spit "out.html" @web-html)))



(defn get-pretend []
  (html/parse-string pretend-webpage))

(defn extract-child-node [kw v-in]
  (let [[k tm & cn] v-in]
    (if (= kw k)
      (first cn)
      (recur kw cn)
    )))

;[:html {} [:head {}] [:body {} [:h1 {} "Page Header"]] [:body {} "Meow"]]


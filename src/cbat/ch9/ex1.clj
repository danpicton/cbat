(ns cbat.ch9.ex1
  (:import [java.net URL URLEncoder])
  (:require [pl.danieljanus.tagsoup :as html])
  (:gen-class))
(apply require clojure.main/repl-requires)

(def user-agent "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2")

(defn get-google-url [search-term]
  (str "https://www.google.com/search?q=" search-term))
(defn get-bing-url [search-term]
  (str "https://www.bing.com/search#q=" search-term))
(defn get-yahoo-url [search-term]
  (str "https://uk.search.yahoo.com/search?p=" search-term))

(def search-engine-list {:google get-google-url
                         :yahoo get-yahoo-url
                         :bing get-bing-url})

;; note - Bing was very inconsistent in returning data, so maybe employs some functionality to prevent crawling of results

(defn open-connection[url]
  (doto (.openConnection url)
    (.setRequestProperty "User-Agent" user-agent)))

(defn get-response [url]
  (let [conn (open-connection url)]
    (slurp (.getInputStream conn))))

;; exercise 1
(defn search
  "Returns HTML string from quickest Google and Yahoo search for search-term."   [search-term]
  (let [web-html (promise)]
    (doseq [url [get-google-url get-yahoo-url]]
      (future (if-let [html-string (java.net.URL. (url search-term))]
              (deliver web-html (get-response html-string)))))
;    (spit "search.out" (deref web-html 5000 :timeout))))
    (deref web-html 5000 :timeout)))

;; exercise 2
(defn get-search-urls
  "Returns lazy sequence of partial search engine URLs (prefixes), based on provided e (:google, :yahoo and/or :bing)."
  [& e]
  (map #(% search-engine-list) e))

(defn search-specific 
  "Returns HTML string from quickest search for search-term on chosen engines (:google, :yahoo and/or :bing)."
  [search-term & engines]
  (let [web-html (promise)]
    (doseq [url (apply get-search-urls engines)]
      (future (if-let [html-string (java.net.URL. (url search-term))]
              (deliver web-html (get-response html-string)))))
;    (spit "search.out" (deref web-html 5000 :timeout))))
    (deref web-html 5000 :timeout)))

;; exercise 3 - only works on Google results due to differences in search results (removing the filtered-uris binding and returning just uris returns data for both engines
(defn get-results-urls 
  "(experimental) Returns lazy sequence of URLs from quickest search for search on chosen engines (:google, :yahoo and/or :bing)."
  [search-term & engines]
  (let [results-html (apply search-specific search-term engines)
        matches (re-seq #"href=\"([^\" ]*)\"" results-html)
        uris (map second matches)
        filtered-uris (filter #(re-find #"http(?s)://www" %) uris)]
  filtered-uris))

;; Last exercise leaned heavily on following solution - used mainly for
;; regex stuff, but chose to imitate the let bindings as it looked much
;; clearer than a bunch of nested functions:
;; https://github.com/mathias/clojure-for-the-brave-and-true-notes/blob/master/src/clojure_for_the_brave_and_true/core.clj

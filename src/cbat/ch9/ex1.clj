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

(defn open-connection[url]
  (doto (.openConnection url)
    (.setRequestProperty "User-Agent" user-agent)))

(defn get-response [url]
  (let [conn (open-connection url)]
    (slurp (.getInputStream conn))))


(defn search [search-term]
  (let [web-html (promise)]
    (doseq [url [get-google-url get-yahoo-url]]
      (future (if-let [html-string (java.net.URL. (url search-term))]
              (deliver web-html (get-response html-string)))))
;    (spit "search.out" (deref web-html 5000 :timeout))))
    (deref web-html 5000 :timeout)))

;; exercise 2
(defn get-search-urls [& e]
  (map #(% search-engine-list) e))

(defn search-specific [search-term & engines]
  (let [web-html (promise)]
    (doseq [url (apply get-search-urls engines)]
 ;     (println "url: " url)
      (future (if-let [html-string (java.net.URL. (url search-term))]
              (deliver web-html (get-response html-string)))))
    (spit "search.out" (deref web-html 5000 :timeout))))
;    (deref web-html 5000 :timeout)))

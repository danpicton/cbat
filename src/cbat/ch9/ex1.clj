(ns cbat.ch9.ex1
  (:import [java.net URL URLEncoder])
  (:require [pl.danieljanus.tagsoup :as html])
  (:gen-class))
(apply require clojure.main/repl-requires)

(def user-agent "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.22 (KHTML, like Gecko) Chrome/25.0.1364.172")

(defn get-google-url [search-term]
  (str "https://www.google.com/search?q=" search-term))
(defn get-bing-url [search-term]
  (str "https://www.bing.com/search#q=" search-term))

(defn open-connection[url]
  (doto (.openConnection url)
    (.setRequestProperty "User-Agent" user-agent)))

(defn get-response [url]
  (let [conn (open-connection url)
        sb (StringBuilder.)]
    (slurp (.getInputStream conn))))

(defn search [search-term]
  (let [web-html (promise)]
    (doseq [url [get-google-url get-bing-url]]
      (future (if-let [html-string (java.net.URL. (url search-term))]
              (deliver web-html (get-response html-string)))))
    (deref web-html 5000 :timeout)))



; 1 Write a function that takes a string as an argument and searches for it on Bing and Google using the slurp function. Your function should return the HTML of the first page returned by the search.
(def bing-search "https://www.bing.com/search?q=")
(def yandex-search "https://yandex.by/search/?text=")

(defn search 
  [query]
  (let [query-promise (promise)]
    (doseq [search [bing-search yandex-search]]
      (future (let [search-result (slurp (str search query))]
                (deliver query-promise search-result))))
   @query-promise))

(search "clojure")


; 2 Update your function so it takes a second argument consisting of the search engines to use.
(def search-map {:bing bing-search, :yandex yandex-search})

(defn search-2
  [query search-engines]
  (let [query-promise (promise)]
    (doseq [search (map search-map search-engines)]
      (future (let [search-result (slurp (str search query))]
                (deliver query-promise search-result))))
    @query-promise))


(search-2 "datascience" [:bing :yandex])

; 3 Create a new function that takes a search term and search engines as arguments, and returns a vector of the URLs from the first page of search results from each search engine.
(defn make-literal [a]
  (.replace a "\"" "\\\""))

(defn extract-anything-between [prefix suffix from-string]
  (let [pattern (str (make-literal prefix) "([\\s\\S]*?)" (make-literal suffix))]
    (second (re-find (re-pattern pattern) from-string))))

(defn extract-anything-between-combine [prefix suffix from-string]
  (let [pattern (str (make-literal prefix) "([\\s\\S]*?)" (make-literal suffix))]
    (map #(second %) (re-seq (re-pattern pattern) from-string))))

(defn parse-bing 
  [text]
  (extract-anything-between-combine 
   "<div class=\"b_title\"><h2><a href=\"" 
   "\" h=\"ID=SERP" 
   text))

(defn parse-yandex
  [text]
  (->> (extract-anything-between-combine
        "tabindex=-1 target=_blank href=\""
        "\" data-counter="
        text)
       (keep-indexed #(if (not= (mod %1 2) 0) %2))
       ))

(def search-map-ext {:bing {:search bing-search, :parse parse-bing}
                     :yandex {:search yandex-search, :parse parse-yandex}})

(defn sm-search 
  [engine]
  (->> search-map-ext
       engine
       :search))

(defn sm-parse
  [engine]
  (->> search-map-ext
       engine
       :parse))

(defn search-3
  [query search-engines]
  (->> search-engines
       (map #(vector (sm-search %) (sm-parse %)))
       (map #(future (let [search (first %)
                           parse (second %)]
                       (-> (str search query)
                           slurp
                           parse))))
       (map deref)
       flatten
       vec))

(search-3 "clojure" [:bing :yandex])
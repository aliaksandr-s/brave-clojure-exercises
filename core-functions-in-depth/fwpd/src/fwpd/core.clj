(ns fwpd.core)

(def filename "suspects.csv")
(def output "filtered.csv")

(def vamp-keys [:name :glitter-index])

(defn str->int
  [str]
  (Integer. str))

(def conversions {:name identity
                  :glitter-index str->int})

(defn convert
  [vamp-key value]
  ((get conversions vamp-key) value))

(defn parse
  "Convert a CSV into rows of columns"
  [string]
  (map #(clojure.string/split % #",")
       (clojure.string/split string #"\n")))

(defn mapify
  "Return a seq of maps like {:name \"Edward Cullen\" :glitter-index 10}"
  [rows]
  (map (fn [unmapped-row]
         (reduce (fn [row-map [vamp-key value]]
                   (assoc row-map vamp-key (convert vamp-key value)))
                 {}
                 (map vector vamp-keys unmapped-row)))
       rows))

(defn glitter-filter
  [minimum-glitter records]
  (filter #(>= (:glitter-index %) minimum-glitter) records))

(def filtered (glitter-filter 3 (mapify (parse (slurp filename)))))

; 1. Turn the result of your glitter filter into a list of names.
(defn to-list-of-names
  "Converts "
  [seq]
  (map #(get % :name) seq))

(to-list-of-names filtered)

; 2. Write a function, append, which will append a new suspect to your list of suspects.
(defn append
  [suspect]
  (spit filename (str "\n" 
                      (:name suspect) 
                      "," 
                      (:glitter-index suspect)) :append true))

; (append {:name "Jason Moron" :glitter-index 5})

; 3. Write a function, validate, which will check that :name and :glitter-index are present when you append. The validate function should accept two arguments: a map of keywords to validating functions, similar to conversions, and the record to be validated.

(def not-nil? (complement nil?))
(def validations {:name not-nil?
                  :glitter-index number?})

(defn validate
  [validations record]
  (every? true? (map (fn [[key val-func]] (val-func (key record)) ) 
        validations)))

(validate validations {:name "Jimmy Carter" :glitter-index 0})
(validate validations {:name "Jimmy Carter" :glitter-index "JJ"})
(validate validations {:glitter-index 3})
(validate validations {:name "Jimmy Carter"})

; 4. Write a function that will take your list of maps and convert it back to a CSV string. You’ll need to use the clojure.string/join function.

(defn convert-to-csv
  [suspects]
  (clojure.string/trim-newline (apply str (map (fn [{name :name g-index :glitter-index}] 
                     (str name "," g-index "\n")) 
                   suspects))))

(convert-to-csv filtered)
(spit output (convert-to-csv filtered))
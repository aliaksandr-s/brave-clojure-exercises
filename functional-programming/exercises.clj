; 1 You used (comp :intelligence :attributes) to create a function that returns a character’s intelligence. Create a new function, attr, that you can call like (attr :intelligence) and that does the same thing.
(def character
  {:name "Smooches McCutes"
   :attributes {:intelligence 10
                :strength 4
                :dexterity 5}})
(def c-int (comp :intelligence :attributes))
(def c-str (comp :strength :attributes))
(def c-dex (comp :dexterity :attributes))

(defn attr 
  [attribute]
  (comp attribute :attributes))
(def c-int-2 (attr :intelligence))

(c-int character)
(c-int-2 character)

; 2 Implement the comp function.
(defn two-comp
  [f g]
  (fn [& args]
    (f (apply g args))))

(def plus-plus (two-comp #(+ 2 %) #(+ 2 %)))
(plus-plus 3)

(defn my-comp [& fs]
  (reduce two-comp identity fs))

(def plus-plus-plus (my-comp #(+ 2 %) #(+ 2 %) #(+ 2 %)))
(plus-plus-plus 3)

; 3 Implement the assoc-in function. Hint: use the assoc function and define its parameters as [m [k & ks] v].
(defn my-assoc-in
  ;; metadata elided
  [m [k & ks] v]
  (if ks
    (assoc m k (assoc-in (get m k) ks v))
    (assoc m k v)))

(my-assoc-in {} [:cookie :monster :vocals] "Finntroll")
; => {:cookie {:monster {:vocals "Finntroll"}}}

; 4 Look up and use the update-in function.
(def users [{:name "James" :age 26}  {:name "John" :age 43}])
(update-in users [1 :age] inc)

(def p {:name "James" :age 26})
(update-in p [:age] inc)

; 5 Implement update-in.
(defn my-update-in
  [m ks f & args]
  (assoc-in m ks (apply f (get-in m ks) args)))

(my-update-in users [1 :age] inc)
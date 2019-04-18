; 1 Use the list function, quoting, and read-string to create a list that, when evaluated, prints your first name and your favorite sci-fi movie.
(eval (list (eval (read-string "'print")) "Alex" "Inception"))

; 2 Create an swap-regular function that takes a list like (1 + 3 * 4 - 5) and transforms it into the lists that Clojure needs in order to correctly evaluate the expression using operator precedence rules.

(defn l-attr
  [seq] (butlast (butlast seq)))
(defn r-attr 
  [seq] (last seq))
(defn opr
  [seq] (last (butlast seq)))
(defn one-elem?
  [seq] (and (= 1 (count seq)) true))
(defn high-prior?
  [elem] (contains? #{'* '/} elem))

(l-attr '(1 + 2))
(l-attr '(1 + 3 * 4))
(r-attr '(1 + 2))
(opr '(1 + 2))
(one-elem? '(1 2))
(high-prior? '*)

(defn swap-prior
  [infixed]
  (if (= (count infixed) 1)
    infixed
    (let [o1 (first infixed)
          opr (second infixed)
          o2 (nth infixed 2)]
      (if (high-prior? opr) 
        (swap-prior (cons (list opr o1 o2) (drop 3 infixed))) 
        (concat (list o1 opr) (swap-prior (drop 2 infixed)))))))

(defn swap-regular
  [infixed]
  (cond
    (one-elem? infixed) (first infixed)
    (one-elem? (l-attr infixed)) (list (opr infixed) (first (l-attr infixed)) (r-attr infixed))
    :else (list (opr infixed) (swap-regular (l-attr infixed)) (r-attr infixed))))

(defn infix
  [infixed]
  (-> infixed
      swap-prior
      swap-regular))

(eval (infix '(1 + 3 * 4 - 5 * 3)))
(eval (infix '(1 + 3 * 4 - 5)))
(eval (infix '(1 + 3)))
(eval (infix '(1 * 3)))
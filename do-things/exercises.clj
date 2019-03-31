; 1 Use the str, vector, list, hash-map, and hash-set functions.

(str "Hello " 4)
(vector 1 2 3 "Hi")
(list 1 2 3 "HI")
(hash-map :key1 "val-1" :key2 "val-2")
(hash-set 1 1 1 2 3 3)

; 2 Write a function that takes a number and adds 100 to it.
(defn adder 
  [number]
  (+ number 100))


; 3 Write a function, dec-maker, that works exactly like the function inc-maker except with subtraction.
(defn dec-maker 
  "Create a custom decrementor"
  [dec-by]
  #(- % dec-by))

; 4 Write a function, mapset, that works like map except the return value is a set:
(defn mapset
  [func vector]
  (set (map func vector)))
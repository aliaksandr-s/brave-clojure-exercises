; 1 Create an atom with the initial value 0, use swap! to increment it a couple of times, and then dereference it.

(def test-atom (atom 0))

(defn increase-test-atom
  [state increase-by]
  (+ state increase-by))

(swap! test-atom increase-test-atom 1)
(swap! test-atom increase-test-atom 1)
(swap! test-atom increase-test-atom 1)


@test-atom

; 2 Create a function that uses futures to parallelize the task of downloading random quotes fromhttp://www.braveclojure.com/random-quote using (slurp "http://www.braveclojure.com/random-quote"). The futures should update an atom that refers to a total word count for all quotes. The function will take the number of quotes to download as an argument and return the atom’s final value. Keep in mind that you’ll need to ensure that all futures have finished before returning the atom’s final value. Here’s how you would call it and an example result: 

; (quote-word-count 5)

(defn quote-word-count
  [n]
  (let [quote-url "https://www.braveclojure.com/random-quote"
        url-vector (repeat n quote-url)
        quoute-count (atom 0)]
    (def count-vector (->> url-vector
                           (map #(future (->> (slurp %)
                                              count)))
                           (map deref)))
    (doseq [c count-vector]
      (swap! quoute-count #(+ % c)))
    @quoute-count))

(quote-word-count 10)

; 3 Create representations of two characters in a game. The first character has 15 hit points out of a total of 40. The second character has a healing potion in his inventory. Use refs and transactions to model the consumption of the healing potion and the first character healing.
(defn vec-remove
  "remove elem in coll"
  [coll pos]
  (vec (concat (subvec coll 0 pos) (subvec coll (inc pos)))))

(def mighty-warrior 
  (ref 
   {:current-health 15
    :max-health 40
    :inventory []}))

(def lazy-healer 
  (ref 
   {:current-health 40
    :max-health 40
    :inventory [:magic-wand :healing-potion :deck-of-cards]}))

(defn heal-with-potion
  [consumer producer]
  (dosync
   (when (.contains (:inventory @producer) :healing-potion) 
     (let [potion-pos (.indexOf (:inventory @producer) :healing-potion)] 
       (alter producer update-in [:inventory] #(vec-remove % potion-pos))
       (alter consumer update-in [:current-health] (fn [_] (:max-health @consumer)))))))

(heal-with-potion mighty-warrior lazy-healer)
(heal-with-potion mighty-warrior mighty-warrior)

@lazy-healer
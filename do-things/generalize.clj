; 6 Create a function that generalizes symmetrize-body-parts and the function you created in Exercise 5. The new function should take a collection of body parts and the number of matching body parts to add. If you’re completely new to Lisp languages and functional programming, it probably won’t be obvious how to do this. If you get stuck, just move on to the next chapter and revisit the problem later.

(ns generalize
  (:require [clojure.string :as str]))

(use '[clojure.test :only [is]])

(def asym-body-parts [{:name "head" :size 3}
                      {:name "left-eye" :size 1}
                      {:name "left-ear" :size 1}])

(defn matching-part
  [part n]
  (if (str/starts-with? (:name part) "left-")
    (vec (map #(hash-map :name (str (str/replace (:name part) #"^left-" "") "-" %)
                    :size (:size part))
         (take n (range 1 (+ n 1)))))
    [part]))

(defn better-symmetrize-body-parts
  "Expects a seq of maps that have a :name and :size"
  [asym-body-parts n]
  (reduce (fn [final-body-parts part]
            (into final-body-parts (matching-part part n)))
          []
          asym-body-parts))

(def expected-body-parts
  [{:name "head" :size 3}
   {:name "eye-1" :size 1}
   {:name "eye-2" :size 1}
   {:name "eye-3" :size 1}
   {:name "ear-1" :size 1}
   {:name "ear-2" :size 1}
   {:name "ear-3" :size 1}])

(is (= [{:name "head" :size 3}] (matching-part {:name "head" :size 3} 2)))
(is (= [{:name "eye-1" :size 3}
        {:name "eye-2" :size 3}]
       (matching-part
        {:name "left-eye" :size 3} 2)))
(is (= expected-body-parts (better-symmetrize-body-parts asym-body-parts 3)))
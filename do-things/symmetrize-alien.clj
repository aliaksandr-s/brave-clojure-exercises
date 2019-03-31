;5 Create a function that’s similar to symmetrize-body-parts except that it has to work with weird space aliens with radial symmetry. Instead of two eyes, arms, legs, and so on, they have five.

(ns aliens
  (:require [clojure.string :as str]))

(use '[clojure.test :only [is]])

(def asym-body-parts [{:name "head" :size 3}
                      {:name "left-eye" :size 1}
                      {:name "left-ear" :size 1}])

(defn matching-part
  [part]
  (if (str/starts-with? (:name part) "left")
    (vec (map #(hash-map :name (str (str/replace (:name part) #"^left-" "") "-" %)
                    :size (:size part))
         [1 2 3 4 5]))
    [part]))

(defn better-symmetrize-body-parts
  "Expects a seq of maps that have a :name and :size"
  [asym-body-parts]
  (reduce (fn [final-body-parts part]
            (into final-body-parts (matching-part part) ))
          []
          asym-body-parts))

(better-symmetrize-body-parts asym-body-parts)

(def expected-body-parts
  [{:name "head" :size 3}
   {:name "eye-1" :size 1}
   {:name "eye-2" :size 1}
   {:name "eye-3" :size 1}
   {:name "eye-4" :size 1}
   {:name "eye-5" :size 1}
   {:name "ear-1" :size 1}
   {:name "ear-2" :size 1}
   {:name "ear-3" :size 1}
   {:name "ear-4" :size 1}
   {:name "ear-5" :size 1}])

(is (= [{:name "head" :size 3}] (matching-part {:name "head" :size 3})))
(is (= [{:name "eye-1" :size 3}
        {:name "eye-2" :size 3}
        {:name "eye-3" :size 3}
        {:name "eye-4" :size 3}
        {:name "eye-5" :size 3}]
       (matching-part
        {:name "left-eye" :size 3})))
(is (= expected-body-parts (better-symmetrize-body-parts asym-body-parts)))

(use '[clojure.test :only [is]])

(def order-details-wrong
  {:name "Mitchard Blimmons"
   :email "mitchard.blimmonsgmail.com"})

(def order-details-correct
  {:name "Mitchard Blimmons"
   :email "mitchard@blimmonsgmail.com"})

(def order-details-validations
  {:name
   ["Please enter a name" not-empty]

   :email
   ["Please enter an email address" not-empty

    "Your email address doesn't look like an email address"
    #(or (empty? %) (re-seq #"@" %))]})

(defn error-messages-for
  "Return a seq of error messages"
  [to-validate message-validator-pairs]
  (map first (filter #(not ((second %) to-validate))
                     (partition 2 message-validator-pairs))))

(defn validate
  "Returns a map with a vector of errors for each key"
  [to-validate validations]
  (reduce (fn [errors validation]
            (let [[fieldname validation-check-groups] validation
                  value (get to-validate fieldname)
                  error-messages (error-messages-for value validation-check-groups)]
              (if (empty? error-messages)
                errors
                (assoc errors fieldname error-messages))))
          {}
          validations))

(validate order-details-wrong order-details-validations)
(validate order-details-correct order-details-validations)

; 1 Write the macro when-valid so that it behaves similarly to when. Here is an example of calling it: 
;(when-valid order-details order-details-validations
;            (println "It's a success!")
;            (render :success))
(defmacro when-valid
  [to-validate validations & body]
  `(if (empty? (validate ~to-validate ~validations)) (do ~@body)))

(when-valid order-details-correct order-details-validations
  (println "It's a success!")
  (println "Huurrray!"))

; 2 You saw that and is implemented as a macro. Implement or as a macro.
(defmacro or
  ([] nil)
  ([x] x)
  ([x & next]
   `(let [or# ~x]
      (if or# or# (or ~@next)))))

(is (= nil (or)))
(is (= 5 (or 5)))
(is (= 5 (or false 5)))

; 3 In Chapter 5 you created a series of functions (c-int, c-str, c-dex) to read an RPG character’s attributes. Write a macro that defines an arbitrary number of attribute-retrieving functions using one macro call. Here’s how you would call it:

; (defattrs c-int :intelligence
;    c-str :strength
;    c-dex :dexterity)

(defmacro defattrs
  ([] nil)
  ([name body] (list 'def name body))
  ([name body & rest] `(do (def ~name ~body) (defattrs ~@rest))))

(def character 
  {:intelligence 10
   :strength 4
   :dexterity 5})

(defattrs 
  c-int :intelligence
  c-str :strength
  c-dex :dexterity)
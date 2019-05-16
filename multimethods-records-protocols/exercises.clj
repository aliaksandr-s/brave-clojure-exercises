; 1. Extend the full-moon-behavior multimethod to add behavior for your own kind of were-creature.
(ns were-creatures)

(defmulti full-moon-behavior (fn [were-creature] (:were-type were-creature)))

(defmethod full-moon-behavior :wolf
  [were-creature]
  (str (:name were-creature) " will howl and murder"))

(defmethod full-moon-behavior :simmons
  [were-creature]
  (str (:name were-creature) " will encourage people and sweat to the oldies"))

(defmethod full-moon-behavior :plumber
  [were-creature]
  (str (:name were-creature) " will unclog the magic stubborn sink"))

(full-moon-behavior {:were-type :wolf
                     :name "Rachel from next door"})

(full-moon-behavior {:name "Andy the baker"
                     :were-type :simmons})

(full-moon-behavior {:name "Johny the plumber"
                     :were-type :plumber})

; 2. Create a WereSimmons record type, and then extend the WereCreature protocol.
(defprotocol WereCreature
  (full-moon-behavior [x]))

(defrecord WereWolf [name title]
  WereCreature
  (full-moon-behavior [x]
    (str name " will howl and murder")))

(full-moon-behavior (map->WereWolf {:name "Lucian" :title "CEO of Melodrama"}))

(defrecord WereSimmons [name job]
  WereCreature
  (full-moon-behavior [x]
                      (str name " will encourage people and sweat to the oldies")))

(full-moon-behavior (->WereSimmons "Andy the baker" "A baker"))

; 3. Create your own protocol, and then extend it using extend-type and extend-protocol
(defprotocol Logging
  "Does some logging stuff"
  (log-info [message] "Basic loggin info")
  (log-warn [message] [message warn] "Warning logger"))

(extend-type java.lang.String
  Logging
  (log-info [message] (str "===" message "==="))
  (log-warn ([message] (str "---" message "---")) 
            ([message warn] (str "> " warn " > " message))))

(log-info "Hello")
(log-warn "message" "Warning")

(extend-protocol Logging
  java.lang.Object
  (log-info [message] (str "-" message "-"))
  (log-warn ([message] (str ">" message "<")) 
            ([message warn] (str "whatever"))))

(log-info 3)
(log-warn 5)
(log-warn 34 34)
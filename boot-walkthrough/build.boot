(deftask what
  "Specify a thing"
  [t thing     THING str  "An object"
   p pluralize       bool "Whether to pluralize"]
  (fn middleware [next-handler]
        (fn handler [fileset]
          (next-handler (merge fileset {:thing thing :pluralize pluralize})))))

(deftask gnomes
  "Announce a thing being overrun with gnomes"
  []
  (fn middleware [next-handler]
    (fn handler [fileset]
      (let [verb (if (:pluralize fileset) "are" "is")]
        (println "My" (:thing fileset) verb "geing overrun with gnomes")
        fileset))))

(deftask fire
  "Announce a thing is on fire"
  []
  (fn middleware [next-handler]
    (fn handler [fileset]
      (let [verb (if (:pluralize fileset) "are" "is")]
        (println "My" (:thing fileset) verb "on fire!")
        fileset))))
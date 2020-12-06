(deftemplate BLINKY
	(slot edible (type SYMBOL)))
	
(deftemplate INKY
	(slot edible (type SYMBOL)))
	
(deftemplate PINKY
	(slot edible (type SYMBOL)))

(deftemplate SUE
	(slot edible (type SYMBOL)))
	
(deftemplate MSPACMAN 
    (slot mindistancePPill))
    
(deftemplate ACTION
	(slot id))   
    
(defrule INKYchases
	(INKY (edible false)) => (assert (ACTION (id INKYchases))))

(defrule INKYrunsAway
	(INKY (edible true)) =>  (assert (ACTION (id INKYrunsAway))))
	
	
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
    
(defrule SUEchases
	(SUE (edible false)) => (assert (ACTION (id SUEchases))))

(defrule BLINKYrunsAway
	(SUE (edible true)) =>  (assert (ACTION (id SUErunsAway))))
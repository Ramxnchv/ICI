;FACTS ASSERTED BY GAME INPUT
(deftemplate BLINKY
	(slot edible (type SYMBOL))
	(slot lairTime (type NUMBER))
	(slot edibleTime (type NUMBER))
	(slot distToPacman (type NUMBER))
	(slot closestToPacman (type SYMBOL)))
	
(deftemplate INKY
	(slot edible (type SYMBOL))
	(slot lairTime (type NUMBER))
	(slot edibleTime (type NUMBER))
	(slot distToPacman (type NUMBER))
	(slot closestToPacman (type SYMBOL)))
	
(deftemplate PINKY
	(slot edible (type SYMBOL))
	(slot lairTime (type NUMBER))
	(slot edibleTime (type NUMBER))
	(slot distToPacman (type NUMBER))
	(slot closestToPacman (type SYMBOL)))

(deftemplate SUE
	(slot edible (type SYMBOL))
	(slot lairTime (type NUMBER))
	(slot edibleTime (type NUMBER))
	(slot distToPacman (type NUMBER))
	(slot closestToPacman (type SYMBOL)))

(deftemplate MSPACMAN 
    (slot mindistancePPill (type NUMBER)) )
    
;DEFINITION OF THE ACTION FACT
(deftemplate ACTION
	(slot id) (slot info (default "")) ) 
   
;RULES

(defrule BLINKYrandom
	(BLINKY (lairTime ?lt) (test > ?lt 0))
	=>
	(assert (ACTION (id BLINKYrandom) (info "BLINKY en la jaula --> salida aleatoria")))
)

(defrule BLINKYchases ;PERSECUCION DIRECTA
	(BLINKY (edible false))
	;(MSPACMAN (mindistancePPill ?d)) (test (> ?d 30))
	(BLINKY (closestToPacman true)) 
	=> 
	(assert (ACTION (id BLINKYchases) (info "BLINKY no comestible y más cercano --> perseguir") )))	

(defrule BLINKYfillsZone ;OCUPAR CUADRANTE
	(BLINKY (edible false))
	;(MSPACMAN (mindistancePPill ?d)) (test (> ?d 30))
	(BLINKY (closestToPacman false))
	=>
	(assert (ACTION (id BLINKYfillsZone) (info "BLINKY no comestible y no más cercano --> acorralar"))) 
) 

(defrule BLINKYrunsAwayMSPACMANclosePPill ;HUIDA PREVENTIVA
	(BLINKY (edible false))
	(MSPACMAN (mindistancePPill ?d)) (test (<= ?d 30))
	(BLINKY (distToPacman ?dp) (test (<= ?dp 30))) 
	=>  
	(assert (ACTION (id BLINKYrunsAway) (info "BLINKY cerca de MSPacman Y éste cerca PPill --> huir"))) )

(defrule BLINKYrunsAway ;HUÍDA DIRECTA
	(BLINKY (edible true))
	(BLINKY (distToPacman ?dp) (test <= ?dp 30)) 
	=>  
	(assert (ACTION (id BLINKYrunsAway) (info "BLINKY comestible y muy cerca --> huir") )))
	
(defrule BLINKYgoesToFarthestNode ;IR AL PUNTO MÁS LEJANO
	(BLINKY (edible true))
	(BLINKY (distToPacman ?dp) (test > ?dp 30))
	=>
	(assert (ACTION (id BLINKYgoesToFarthestNode) (info "BLINKY comestible y no muy cerca --> aumentar distancia"))))

(defrule BLINKYearlyChases ; PERSECUCIÓN PRECOZ
	(BLINKY (edible true))
	(BLINKY (distToPacman ?dp) (test > ?dp 30))
	(BLINKY (edibleTime ?et) (test > ?et 30))
	=>
	(assert (ACTION BLINKYchases) (info "BLINKY comestible por poco tiempo y no muy cerca --> perseguir"))
)	
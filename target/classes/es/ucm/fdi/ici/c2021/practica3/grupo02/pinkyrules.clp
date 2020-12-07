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

(defrule PINKYrandom
	(PINKY (lairTime ?lt)) (test (> ?lt 0))
	=>
	(assert (ACTION (id PINKYrandom) (info "PINKY en la jaula --> salida aleatoria")))
)

(defrule PINKYchases ;PERSECUCION DIRECTA
	(PINKY (edible false))
	;(MSPACMAN (mindistancePPill ?d)) (test (> ?d 30))
	(PINKY (closestToPacman true)) 
	=> 
	(assert (ACTION (id PINKYchases) (info "PINKY no comestible y más cercano --> perseguir") )))	

(defrule PINKYfillsZone ;OCUPAR CUADRANTE
	(PINKY (edible false))
	;(MSPACMAN (mindistancePPill ?d)) (test (> ?d 30))
	(PINKY (closestToPacman false))
	=>
	(assert (ACTION (id PINKYfillsZone) (info "PINKY no comestible y no más cercano --> acorralar"))) 
) 

(defrule PINKYrunsAwayMSPACMANclosePPill ;HUIDA PREVENTIVA
	(PINKY (edible false))
	(MSPACMAN (mindistancePPill ?d)) (test (<= ?d 30))
	(PINKY (distToPacman ?dp)) (test (<= ?dp 30))
	=>  
	(assert (ACTION (id PINKYrunsAway) (info "PINKY cerca de MSPacman Y éste cerca PPill --> huir"))) )

(defrule PINKYrunsAway ;HUÍDA DIRECTA
	(PINKY (edible true))
	(PINKY (distToPacman ?dp)) (test (<= ?dp 30)) 
	=>  
	(assert (ACTION (id PINKYrunsAway) (info "PINKY comestible y muy cerca --> huir") )))
	
(defrule PINKYgoesToFarthestNode ;IR AL PUNTO MÁS LEJANO
	(PINKY (edible true))
	(PINKY (distToPacman ?dp)) (test (> ?dp 30))
	=>
	(assert (ACTION (id PINKYgoesToFarthestNode) (info "PINKY comestible y no muy cerca --> aumentar distancia"))))

(defrule PINKYearlyChases ; PERSECUCIÓN PRECOZ
	(PINKY (edible true))
	(PINKY (distToPacman ?dp)) (test (> ?dp 30))
	(PINKY (edibleTime ?et)) (test (> ?et 30))
	=>
	(assert (ACTION PINKYchases) (info "PINKY comestible por poco tiempo y no muy cerca --> perseguir"))
)	

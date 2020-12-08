;FACTS ASSERTED BY GAME INPUT
(deftemplate BLINKY
	(slot edible (type SYMBOL))
	(slot iniNode (type SYMBOL))
	(slot edibleTime (type NUMBER))
	(slot distToPacman (type NUMBER))
	(slot distToNearestPPill (type NUMBER))
	(slot closestToPacman (type SYMBOL)))
	
(deftemplate INKY
	(slot edible (type SYMBOL))
	(slot iniNode (type SYMBOL))
	(slot edibleTime (type NUMBER))
	(slot distToPacman (type NUMBER))
	(slot distToNearestPPill (type NUMBER))
	(slot closestToPacman (type SYMBOL)))
	
(deftemplate PINKY
	(slot edible (type SYMBOL))
	(slot iniNode (type SYMBOL))
	(slot edibleTime (type NUMBER))
	(slot distToPacman (type NUMBER))
	(slot distToNearestPPill (type NUMBER))
	(slot closestToPacman (type SYMBOL)))

(deftemplate SUE
	(slot edible (type SYMBOL))
	(slot iniNode (type SYMBOL))
	(slot edibleTime (type NUMBER))
	(slot distToPacman (type NUMBER))
	(slot distToNearestPPill (type NUMBER))
	(slot closestToPacman (type SYMBOL)))

(deftemplate MSPACMAN 
    (slot mindistancePPill (type NUMBER)) )
    
;DEFINITION OF THE ACTION FACT
(deftemplate ACTION
	(slot id) (slot info (default "")) ) 
   
;RULES

(defrule BLINKYrandom
	(BLINKY (iniNode true))
	=>
	(assert (ACTION (id BLINKYrandom) (info "BLINKY en la jaula --> salida aleatoria")))
)

(defrule BLINKYchases ;PERSECUCION DIRECTA
	(BLINKY (iniNode false))
	(BLINKY (edible false))
	;(MSPACMAN (mindistancePPill ?d)) (test (> ?d 30))
	(BLINKY (closestToPacman true)) 
	=> 
	(assert (ACTION (id BLINKYchases) (info "BLINKY NO comestible y mas cercano --> perseguir") )))	

(defrule BLINKYfillsZone ;OCUPAR CUADRANTE
	(BLINKY (iniNode false))
	(BLINKY (edible false))
	;(MSPACMAN (mindistancePPill ?d)) (test (> ?d 30))
	(BLINKY (closestToPacman false))
	=>
	(assert (ACTION (id BLINKYfillsZone) (info "BLINKY NO comestible y NO mas cercano --> acorralar"))) 
) 

(defrule BLINKYrunsAwayMSPACMANclosePPill ;HUIDA PREVENTIVA
	(BLINKY (edible false))
	(MSPACMAN (mindistancePPill ?d)) (test (<= ?d 30))
	(BLINKY (distToNearestPPill ?dP)) (MSPACMAN (mindistancePPill ?d)) (test(<= ?dP ?d))
	(BLINKY (distToPacman ?dp)) (test (<= ?dp 30)) 
	=>  
	(assert (ACTION (id BLINKYrunsAway) (info "BLINKY cerca de MSPacman Y bastante cerca PPill --> huir"))) )

(defrule BLINKYrunsAway ;HUIDA DIRECTA
	(BLINKY (edible true))
	(BLINKY (distToPacman ?dp)) (test (<= ?dp 30)) 
	=>  
	(assert (ACTION (id BLINKYrunsAway) (info "BLINKY comestible y muy cerca --> huir") )))
	
(defrule BLINKYgoesToFarthestNode ;IR AL PUNTO MASS LEJANO
	(BLINKY (edible true))
	(BLINKY (distToPacman ?dp)) (test (> ?dp 30))
	=>
	(assert (ACTION (id BLINKYgoesToFarthestNode) (info "BLINKY comestible y NO muy cerca --> aumentar distancia"))))

(defrule BLINKYearlyChases ; PERSECUCION PRECOZ
	(BLINKY (edible true))
	(BLINKY (distToPacman ?dp)) (test (> ?dp 30))
	(BLINKY (edibleTime ?et)) (test (> ?et 30))
	=>
	(assert (ACTION (id BLINKYchases) (info "BLINKY comestible por poco tiempo y NO muy cerca --> perseguir")))
)	
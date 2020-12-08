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

(defrule PINKYrandom
	(PINKY (iniNode true))
	=>
	(assert (ACTION (id PINKYrandom) (info "PINKY en la jaula --> salida aleatoria")))
)

(defrule PINKYchases ;PERSECUCION DIRECTA
	(PINKY (iniNode false))
	(PINKY (edible false))
	;(MSPACMAN (mindistancePPill ?d)) (test (> ?d 30))
	(PINKY (closestToPacman true)) 
	=> 
	(assert (ACTION (id PINKYchases) (info "PINKY no comestible y mÃ¡s cercano --> perseguir") )))	

(defrule PINKYfillsZone ;OCUPAR CUADRANTE
	(PINKY (iniNode false))
	(PINKY (edible false))
	;(MSPACMAN (mindistancePPill ?d)) (test (> ?d 30))
	(PINKY (closestToPacman false))
	=>
	(assert (ACTION (id PINKYfillsZone) (info "PINKY no comestible y no mÃ¡s cercano --> acorralar"))) 
) 

(defrule PINKYrunsAwayMSPACMANclosePPill ;HUIDA PREVENTIVA
	(PINKY (edible false))
	(MSPACMAN (mindistancePPill ?d)) (test (<= ?d 30))
	(PINKY (distToNearestPPill ?dP)) (MSPACMAN (mindistancePPill ?d)) (test(<= ?dP ?d))
	(PINKY (distToPacman ?dp)) (test (<= ?dp 30))
	=>  
	(assert (ACTION (id PINKYrunsAway) (info "PINKY cerca de MSPacman Y Ã©ste cerca PPill --> huir"))) )

(defrule PINKYrunsAway ;HUÃ�DA DIRECTA
	(PINKY (edible true))
	(PINKY (distToPacman ?dp)) (test (<= ?dp 30)) 
	=>  
	(assert (ACTION (id PINKYrunsAway) (info "PINKY comestible y muy cerca --> huir") )))
	
(defrule PINKYgoesToFarthestNode ;IR AL PUNTO MÃ�S LEJANO
	(PINKY (edible true))
	(PINKY (distToPacman ?dp)) (test (> ?dp 30))
	=>
	(assert (ACTION (id PINKYgoesToFarthestNode) (info "PINKY comestible y no muy cerca --> aumentar distancia"))))

(defrule PINKYearlyChases ; PERSECUCIÃ“N PRECOZ
	(PINKY (edible true))
	(PINKY (distToPacman ?dp)) (test (> ?dp 30))
	(PINKY (edibleTime ?et)) (test (> ?et 30))
	=>
	(assert (ACTION (id PINKYchases) (info "PINKY comestible por poco tiempo y no muy cerca --> perseguir")))
)	

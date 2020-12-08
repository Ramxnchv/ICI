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

(defrule INKYrandom
	(INKY (iniNode true))
	=>
	(assert (ACTION (id INKYrandom) (info "INKY en la jaula --> salida aleatoria")))
)
(defrule INKYchases ;PERSECUCION DIRECTA
	(INKY (iniNode false))
	(INKY (edible false))
	;(MSPACMAN (mindistancePPill ?d)) (test (> ?d 30))
	(INKY (closestToPacman true)) 
	=> 
	(assert (ACTION (id INKYchases) (info "INKY no comestible y mÃ¡s cercano --> perseguir") )))	

(defrule INKYfillsZone ;OCUPAR CUADRANTE
	(INKY (iniNode false))
	(INKY (edible false))
	;(MSPACMAN (mindistancePPill ?d)) (test (> ?d 30))
	(INKY (closestToPacman false))
	=>
	(assert (ACTION (id INKYfillsZone) (info "INKT no comestible y no mÃ¡s cercano --> acorralar"))) 
) 

(defrule INKYrunsAwayMSPACMANclosePPill ;HUIDA PREVENTIVA
	(INKY (edible false))
	(MSPACMAN (mindistancePPill ?d)) (test (<= ?d 30))
	(INKY (distToNearestPPill ?dP)) (MSPACMAN (mindistancePPill ?d)) (test(<= ?dP ?d))
	(INKY (distToPacman ?dp)) (test (<= ?dp 30)) 
	=>  
	(assert (ACTION (id INKYrunsAway) (info "INKY cerca de MSPacman Y Ã©ste cerca PPill --> huir"))) )

(defrule INKYrunsAway ;HUÃ�DA DIRECTA
	(INKY (edible true))
	(INKY (distToPacman ?dp)) (test (<= ?dp 30)) 
	=>  
	(assert (ACTION (id INKYrunsAway) (info "INKY comestible y muy cerca --> huir") )))
	
(defrule INKYgoesToFarthestNode ;IR AL PUNTO MÃ�S LEJANO
	(INKY (edible true))
	(INKY (distToPacman ?dp)) (test (> ?dp 30))
	=>
	(assert (ACTION (id INKYgoesToFarthestNode) (info "INKY comestible y no muy cerca --> aumentar distancia"))))

(defrule INKYearlyChases ; PERSECUCIÃ“N PRECOZ
	(INKY (edible true))
	(INKY (distToPacman ?dp)) (test (> ?dp 30))
	(INKY (edibleTime ?et)) (test (> ?et 30))
	=>
	(assert (ACTION (id INKYchases) (info "INKY comestible por poco tiempo y no muy cerca --> perseguir")))
)	

	
	
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

(defrule SUErandom
	(SUE (iniNode true))
	=>
	(assert (ACTION (id SUErandom) (info "SUE en la jaula --> salida aleatoria")))
)

(defrule SUEchases ;PERSECUCION DIRECTA
	(SUE (iniNode false))
	(SUE (edible false))
	;(MSPACMAN (mindistancePPill ?d)) (test (> ?d 30))
	(SUE (closestToPacman true)) 
	=> 
	(assert (ACTION (id SUEchases) (info "SUE no comestible y mÃ¡s cercano --> perseguir") )))	

(defrule SUEfillsZone ;OCUPAR CUADRANTE
	(SUE (iniNode false))
	(SUE (edible false))
	;(MSPACMAN (mindistancePPill ?d)) (test (> ?d 30))
	(SUE (closestToPacman false))
	=>
	(assert (ACTION (id SUEfillsZone) (info "SUE no comestible y no mÃ¡s cercano --> acorralar"))) 
) 

(defrule SUErunsAwayMSPACMANclosePPill ;HUIDA PREVENTIVA
	(SUE (edible false))
	(MSPACMAN (mindistancePPill ?d)) (test (<= ?d 30))
	(SUE (distToNearestPPill ?dP)) (MSPACMAN (mindistancePPill ?d)) (test(<= ?dP ?d))
	(SUE (distToPacman ?dp)) (test (<= ?dp 30)) 
	=>  
	(assert (ACTION (id SUErunsAway) (info "SUE cerca de MSPacman Y Ã©ste cerca PPill --> huir"))))

(defrule SUErunsAway ;HUÃ�DA DIRECTA
	(SUE (edible true))
	(SUE (distToPacman ?dp)) (test (<= ?dp 30))
	=>  
	(assert (ACTION (id SUErunsAway) (info "SUE comestible y muy cerca --> huir") )))
	
(defrule SUEgoesToFarthestNode ;IR AL PUNTO MÃ�S LEJANO
	(SUE (edible true))
	(SUE (distToPacman ?dp)) (test (> ?dp 30))
	=>
	(assert (ACTION (id SUEgoesToFarthestNode) (info "SUE comestible y no muy cerca --> aumentar distancia"))))

(defrule SUEearlyChases ; PERSECUCIÃ“N PRECOZ
	(SUE (edible true))
	(SUE (distToPacman ?dp)) (test (> ?dp 30))
	(SUE (edibleTime ?et)) (test (> ?et 30))
	=>
	(assert (ACTION (id SUEchases) (info "SUE comestible por poco tiempo y no muy cerca --> perseguir")))
)	

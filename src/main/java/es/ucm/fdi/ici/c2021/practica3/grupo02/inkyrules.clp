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

(defrule INKYrandom
	(INKY (lairTime ?lt)) (test (> ?lt 0))
	=>
	(assert (ACTION (id INKYrandom) (info "INKY en la jaula --> salida aleatoria")))
)

(defrule INKYchases ;PERSECUCION DIRECTA
	(INKY (edible false))
	;(MSPACMAN (mindistancePPill ?d)) (test (> ?d 30))
	(INKY (closestToPacman true)) 
	=> 
	(assert (ACTION (id INKYchases) (info "INKY no comestible y más cercano --> perseguir") )))	

(defrule INKYfillsZone ;OCUPAR CUADRANTE
	(INKY (edible false))
	;(MSPACMAN (mindistancePPill ?d)) (test (> ?d 30))
	(INKY (closestToPacman false))
	=>
	(assert (ACTION (id INKYfillsZone) (info "INKT no comestible y no más cercano --> acorralar"))) 
) 

(defrule INKYrunsAwayMSPACMANclosePPill ;HUIDA PREVENTIVA
	(INKY (edible false))
	(MSPACMAN (mindistancePPill ?d)) (test (<= ?d 30))
	(INKY (distToPacman ?dp)) (test (<= ?dp 30)) 
	=>  
	(assert (ACTION (id INKYrunsAway) (info "INKY cerca de MSPacman Y éste cerca PPill --> huir"))) )

(defrule INKYrunsAway ;HUÍDA DIRECTA
	(INKY (edible true))
	(INKY (distToPacman ?dp)) (test (<= ?dp 30)) 
	=>  
	(assert (ACTION (id INKYrunsAway) (info "INKY comestible y muy cerca --> huir") )))
	
(defrule INKYgoesToFarthestNode ;IR AL PUNTO MÁS LEJANO
	(INKY (edible true))
	(INKY (distToPacman ?dp)) (test (> ?dp 30))
	=>
	(assert (ACTION (id INKYgoesToFarthestNode) (info "INKY comestible y no muy cerca --> aumentar distancia"))))

(defrule INKYearlyChases ; PERSECUCIÓN PRECOZ
	(INKY (edible true))
	(INKY (distToPacman ?dp)) (test (> ?dp 30))
	(INKY (edibleTime ?et)) (test (> ?et 30))
	=>
	(assert (ACTION (id INKYchases) (info "INKY comestible por poco tiempo y no muy cerca --> perseguir")))
)	

	
	
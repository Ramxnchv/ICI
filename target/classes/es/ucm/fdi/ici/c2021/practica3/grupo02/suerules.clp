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

(defrule SUErandom
	(SUE (lairTime ?lt)) (test (> ?lt 0))
	=>
	(assert (ACTION (id SUErandom) (info "SUE en la jaula --> salida aleatoria")))
)

(defrule SUEchases ;PERSECUCION DIRECTA
	(SUE (edible false))
	;(MSPACMAN (mindistancePPill ?d)) (test (> ?d 30))
	(SUE (closestToPacman true)) 
	=> 
	(assert (ACTION (id SUEchases) (info "SUE no comestible y más cercano --> perseguir") )))	

(defrule SUEfillsZone ;OCUPAR CUADRANTE
	(SUE (edible false))
	;(MSPACMAN (mindistancePPill ?d)) (test (> ?d 30))
	(SUE (closestToPacman false))
	=>
	(assert (ACTION (id SUEfillsZone) (info "SUE no comestible y no más cercano --> acorralar"))) 
) 

(defrule SUErunsAwayMSPACMANclosePPill ;HUIDA PREVENTIVA
	(SUE (edible false))
	(MSPACMAN (mindistancePPill ?d)) (test (<= ?d 30))
	(SUE (distToPacman ?dp)) (test (<= ?dp 30)) 
	=>  
	(assert (ACTION (id SUErunsAway) (info "SUE cerca de MSPacman Y éste cerca PPill --> huir"))) )

(defrule SUErunsAway ;HUÍDA DIRECTA
	(SUE (edible true))
	(SUE (distToPacman ?dp)) (test (<= ?dp 30))
	=>  
	(assert (ACTION (id SUErunsAway) (info "SUE comestible y muy cerca --> huir") )))
	
(defrule SUEgoesToFarthestNode ;IR AL PUNTO MÁS LEJANO
	(SUE (edible true))
	(SUE (distToPacman ?dp)) (test (> ?dp 30))
	=>
	(assert (ACTION (id SUEgoesToFarthestNode) (info "SUE comestible y no muy cerca --> aumentar distancia"))))

(defrule SUEearlyChases ; PERSECUCIÓN PRECOZ
	(SUE (edible true))
	(SUE (distToPacman ?dp)) (test (> ?dp 30))
	(SUE (edibleTime ?et)) (test (> ?et 30))
	=>
	(assert (ACTION SUEchases) (info "SUE comestible por poco tiempo y no muy cerca --> perseguir"))
)	

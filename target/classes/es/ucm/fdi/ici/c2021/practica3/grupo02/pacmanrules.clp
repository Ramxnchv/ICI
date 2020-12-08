;FACTS ASSERTED BY GAME INPUT

(deftemplate MSPACMAN 
    (slot edibleGhosts (type NUMBER)) 
    (slot nearestGhostEdible (type SYMBOL))
    (slot numberOfGhostsNear (type NUMBER))
    (slot activePowerPills (type NUMBER))
    (slot freeGhostsPath (type SYMBOL))
    (slot distance2Closest (type NUMBER))
    (slot spawnPoint (type SYMBOL)))
    
;DEFINITION OF THE ACTION FACT
(deftemplate ACTION
	(slot id) (slot info (default "")) ) 
   
;RULES

(defrule PACMANrandom ;MOVIMIENTO SPAWN
	(MSPACMAN (spawnPoint true))
	=> 
	(assert (ACTION (id PACMANrespawn) (info "Posicion punto de aparicion  --> Movimiento Aleatorio") ))
)

(defrule PACMANedibleghostnear ;FANTASMA COMESTIBLE CERCA
	(MSPACMAN (nearestGhostEdible true))
	=>
	(assert (ACTION (id PACMANchaseGhost) (info "Fantasma comestible cerca --> Perseguir")))
)

(defrule PACMANnearghostsandfreeghostspath ;FANTASMAS CERCA DE POWERPILL
	(MSPACMAN (activePowerPills ?aPP)) (test (> ?aPP 0))
	(MSPACMAN (numberOfGhostsNear ?n)) (test (>= ?n 3))
	(MSPACMAN (freeGhostsPath true))
	=>
	(assert (ACTION (id PACMANchasePP) (info "Muchos Fantasmas Cerca y Camino hacia PP libre --> Perseguir PowerPill")))
)

(defrule PACMANghostsfar ;FANTASMAS LEJOS DE POWERPILL
	(MSPACMAN (distance2Closest ?dc)) (test (>= ?dc 80))
	(MSPACMAN (nearestGhostEdible false))
	=>
	(assert (ACTION (id PACMANchasePill) (info "Fantasmas Lejos --> Perseguir Pills")))
)

(defrule PACMANdanger ;PELIGRO
	(MSPACMAN (numberOfGhostsNear ?n)) (test (>= ?n 3))
	(MSPACMAN (freeGhostsPath false))
	=>
	(assert (ACTION (id PACMANrunAway) (info "Muchos Fantasmas Cerca --> Peligro")))
)
		
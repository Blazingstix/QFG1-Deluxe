;;; Sierra Script 1.0 - (do not remove this comment)
(script# HQROOM) ;HQROOM = 896
(include system.sh) (include sci2.sh) (include game.sh)
(use Main)
(use _LoadMany)
(use _Menu)
(use _System)

(public
	StartARoom 0
)

(procedure (StartARoom what &tmp i temp1 newEvent)
	(theGame setCursor: waitCursor 1)
	(while ((= newEvent (Event new:)) type?)
		(newEvent dispose:)
	)
	(newEvent dispose:)
	(if (OneOf what 2 9 200 202 203 299 600 601)
		(TheMenuBar hide: state: 0)
	else
		(TheMenuBar draw:)
	)
	;wipe out all memory of dropped inventory when we enter a new room
	(= i 1)
	(while (<= i 41)
		(= [invDropped i] 0)
		(++ i)
	)
	(cls)
	;unload all the game scripts
	(LoadMany FALSE 
		;system-level scripts
		DEMO EXTRA DCICON JUMP SMOOPER REVERSE CHASE FOLLOW WANDER GCONTROL WINDOW CAT FILE
		;HQ-level scripts
		DOORS INPUTCOMP TALKOBJ CURSORCOORDS DEBUG_RM TARGETACTOR 205 
		;Arena COMBAT-level scripts
		ARENA SKILLED WARRIOR MONSTER CLOSECOMBAT ENCOUNTER 
		;Arena Magic Animations
		ARENA_MAGIC ARENA_FLAME ARENA_ZAP ARENA_DAZZLE ARENA_CALM 
		;Arena Fighting Animations
		ARENA_THRUST ARENA_BLOCK ARENA_PARRY ARENA_DODGE ;ARENA_PAIN ;CI:NOTE Why was ARENA_PAIN excluded from this dispose list? 
		;additional scripts
		436 166 167 445 430 460 435 440 450 465 GHOSTS CEMETERY 238 232 221 222 223 224 220 
		;Side fighting combat level scripts
		216 217 218
	)
	(mouseDownHandler release:)
	(keyDownHandler release:)
	(directionHandler release:)
	(FixTime)
	(if (== what 600)
		(Bclr fInMainGame)
		(FixTime 12 0)
		(= isNightTime FALSE)
		(= defaultPalette 0)
	)
	(if
	(and (not monsterInRoom) (!= roomDaggersDropped 73))
		(= daggersInMonster 0)
	)
)

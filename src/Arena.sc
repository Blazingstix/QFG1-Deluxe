;;; Sierra Script 1.0 - (do not remove this comment)
(script# ARENA) ;ARENA = 211
(include system.sh) (include sci2.sh) (include game.sh)
(use Main)
(use _LoadMany)
(use _Game)

(class Arena of Room
	(properties
		;change the default style for Arena encounters
		style  		IRISIN
		;new properties added for Arena
		monster 	NULL
		inTransit 	FALSE
		escaped 	FALSE
		;CI:NOTE: I added this to make it easier to dictate which battles can be escaped or not.
		canEscape	TRUE ;dictates whether this is an encounter that can be escaped from or not.
	)
	
	(method (init)
		(Load RES_SCRIPT WARRIOR)
		(Load RES_SCRIPT 205)
		(LoadMany RES_VIEW hpStatusView (GetEgoViewNumber vEgoFightArmSword) (GetEgoViewNumber vEgoFightArmDagger))
		(super init:)
		(StatusLine enable:)
		(Bclr SAVE_ENABLED)
		(= hadABattle TRUE)
		((ScriptID WARRIOR 0)
			init:
			stopUpd:
			weaponView: (if (ego has: iSword) (GetEgoViewNumber vEgoFightArmSword) else (GetEgoViewNumber vEgoFightArmDagger))
			opponent: monster
			drawStatus:
			startCombat: CLOSECOMBAT
		)
		(monster init: drawStatus:)
	)
	
	(method (doit)
		(cond 
			(inTransit
				(super doit:)
			)
			(escaped	;we've successfully escaped from the monster
				(= gMonsterHealth (monster health?))
				(= inTransit TRUE)
				(monster dispose:)
				((ScriptID WARRIOR 0) dispose:)	;dispose Warrior
				(curRoom newRoom: prevRoomNum)
			)
			((<= (monster health?) 0) ;we've defeated the monster
				(= brigandHead 0)
				(= gMonsterHealth 0)
				(= inTransit TRUE)
				(Animate (cast elements?) 0)
				(monster dispose:)
				((ScriptID WARRIOR 0) dispose:)	;dispose Warrior
				(curRoom newRoom: prevRoomNum)
			)
			(else 
				(super doit:)
			)
		)
	)
	
	(method (dispose)
		(Bset SAVE_ENABLED)
		(super dispose:)
	)
)

;;; Sierra Script 1.0 - (do not remove this comment)
(script# MONSTER) ;MONSTER = 214
(include system.sh) (include sci2.sh) (include game.sh)
(use Main)
(use StatusBar)
(use Skilled)


(class Monster of SkilledActor
	(properties
		;new properties for Monster
		attackRange 	50
		ateEgo 			0	;TRUE/FALSE
		monsterTitle 	NULL
		monsterHP 		NULL
		warriorX 		160
		warriorY 		190
		flameX 			0
		flameY 			0
	)
	
	(method (dispose)
		(if monsterTitle
			(monsterHP dispose:)
			(Display MONSTER 0 108 monsterTitle)
			;Enemy Status
			(= monsterTitle (= monsterHP 0))
		)
		(super dispose:)
	)
	
	(method (getHurt param1)
		(super getHurt: param1)
		(if (> param1 0) (Bset FLAG_233))
		(self setMonsterHP: health)
	)
	
	(method (drawStatus)
		(= opponent (ScriptID WARRIOR 0))
		(if (not (= health gMonsterHealth))
			(= health (self calcHealth:))
		)
		(= stamina (self calcStamina:))
		(= mana (self calcMana:))
		(= monsterTitle
			(Display
				MONSTER 1
				p_width 120
				p_at 228 13
				p_mode teJustLeft
				p_font 300
				p_color hpFontColour
				p_save
			)
		)
		((= monsterHP (StatusBar new:))
			x: 240
			y: 33
			titleCel: 0
			priority: 1
			max: (self calcHealth:)
			value: health
			init:
		)
	)
	
	(method (setMonsterHP param1)
		(monsterHP value: param1 draw:)
	)
)

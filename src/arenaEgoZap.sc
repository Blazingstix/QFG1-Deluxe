;;; Sierra Script 1.0 - (do not remove this comment)
(script# ARENA_ZAP) ;ARENA_ZAP = 148
(include system.sh) (include sci2.sh) (include game.sh)
(use Main)
(use _Motion)
(use _System)

(public
	egoZap 0
)

(local
	theWarrior
	theSpellProp
)
(instance egoZap of Script
	(properties)
	
	(method (init)
		(= theWarrior (ScriptID WARRIOR 0))
		(= theSpellProp (ScriptID CLOSECOMBAT 1))
		(super init: &rest)
	)
	
	(method (dispose)
		(super dispose:)
		(DisposeScript ARENA_ZAP)
	)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(= zapMeleeBonus (+ 5 (/ [egoStats ZAP] 10)))
				(theWarrior canFight: FALSE action: aaMAGIC setCel: 2)
				(= cycles 1)
			)
			(1
				(theSpellProp
					setLoop: 7
					cel: 0
					cycleSpeed: 1
					ignoreActors:
					posn: (- (theWarrior x?) 7) (- (theWarrior y?) 70)
					setPri: (+ (theWarrior priority?) 1)
					init:
					setCycle: EndLoop self
				)
			)
			(2
				(theSpellProp dispose:)
				(theWarrior canFight: TRUE setCel: 0)
				(self dispose:)
			)
		)
	)
)

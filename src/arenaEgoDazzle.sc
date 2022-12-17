;;; Sierra Script 1.0 - (do not remove this comment)
(script# ARENA_DAZZLE) ;ARENA_DAZZLE = 149
(include system.sh) (include sci2.sh) (include game.sh)
(use Main)
(use _Motion)
(use _System)

(public
	egoDazzle 0
)

(local
	theWarrior
	theEgoHand
	theSpellProp
)
(instance egoDazzle of Script
	(properties)
	
	(method (init)
		(= theWarrior (ScriptID WARRIOR 0))
		(= theSpellProp (ScriptID CLOSECOMBAT 1))
		(= theEgoHand (theWarrior egoHand?))
		(super init: &rest)
	)
	
	(method (dispose)
		(super dispose:)
		(DisposeScript ARENA_DAZZLE)
	)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(theWarrior canFight: FALSE action: aaMAGIC)
				(self cue:)
			)
			(1
				(theSpellProp
					setLoop: 5
					cel: 0
					setPri: (- (theEgoHand priority?) 1)
					cycleSpeed: 1
					ignoreActors:
					posn: (- (theWarrior x?) 78) (- (theWarrior y?) 81)
					init:
					setCycle: EndLoop self
				)
			)
			(2
				(theSpellProp dispose:)
				(Bset FLAG_233)
				(= monsterCycles [egoStats DAZZLE])
				(theWarrior canFight: FALSE action: aaNOTHING)
				(self dispose:)
			)
		)
	)
)

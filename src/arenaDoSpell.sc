;;; Sierra Script 1.0 - (do not remove this comment)
(script# ARENA_MAGIC) ;ARENA_MAGIC = 146
(include system.sh) (include sci2.sh) (include game.sh)
(use Main)
(use _Motion)
(use _System)

(public
	doSpell 0
)

(local
	theWarrior
	theEgoHand
	theEgosBack
	theEgoShield
)
(instance doSpell of Script
	(properties
		;register 0 ;The Magic spell to Cast.
		)
	
	(method (init)
		(= theWarrior (ScriptID WARRIOR 0))
		(= theEgoHand ((ScriptID WARRIOR 0) egoHand?))
		(= theEgosBack ((ScriptID WARRIOR 0) egosBack?))
		(= theEgoShield ((ScriptID WARRIOR 0) egoShield?))
		(super init: &rest)
	)
	
	(method (dispose)
		(super dispose:)
		(DisposeScript ARENA_MAGIC)
	)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(= egoCanFight FALSE)
				(theEgoHand setCycle: EndLoop self)
			)
			(1
				(switch register
					(FLAMEDART
						(self setScript: (ScriptID ARENA_FLAME 0) self)
					)
					(ZAP
						(self setScript: (ScriptID ARENA_ZAP 0) self)
					)
					(DAZZLE
						(self setScript: (ScriptID ARENA_DAZZLE 0) self)
					)
					(CALM
						(self setScript: (ScriptID ARENA_CALM 0) self)
					)
				)
			)
			(2
				(= register 0)
				(theEgoHand setCycle: BegLoop self)
			)
			(3
				(theEgoHand stopUpd:)
				(= egoCanFight TRUE)
				(self dispose:)
			)
		)
	)
)

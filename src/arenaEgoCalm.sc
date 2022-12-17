;;; Sierra Script 1.0 - (do not remove this comment)
(script# ARENA_CALM) ;ARENA_CALM = 150
(include system.sh) (include sci2.sh) (include game.sh)
(use Main)
(use _System)

(public
	egoCalm 0
)

(local
	theWarrior
)

(instance egoCalm of Script
	(properties)
	
	(method (init)
		(= theWarrior (ScriptID WARRIOR 0))
		(super init: &rest)
	)
	
	(method (dispose)
		(super dispose:)
		(DisposeScript ARENA_CALM)
	)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(HighPrint ARENA_CALM 0)
				;Confidently, you cast the Calm spell.
				(EgoDead DIE_RETRY DIE_ARENA_CALM ARENA_CALM 1
					#title {You should have studied harder}
					#icon (GetEgoViewNumber vEgoMagicDetect) 1 4
				)
				;Why, how cute!  You cast the Calm spell, and the monster visibly relaxed.
				;Why, now it's calmly and relaxedly ripping you to shreds and eating you.
				(theWarrior resetBeforeArena:)
			)
		)
	)
)

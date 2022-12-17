;;; Sierra Script 1.0 - (do not remove this comment)
(script# ARENA_BLOCK) ;ARENA_BLOCK = 152
(include system.sh) (include sci2.sh) (include game.sh)
(use Main)
(use _System)

(public
	egoBlock 0
)

(local
	theWarrior
	theEgoShield
)
(instance egoBlock of Script
	(properties)
	
	(method (init)
		(= theWarrior (ScriptID WARRIOR 0))
		(= theEgoShield (theWarrior egoShield?))
		(super init: &rest)
	)
	
	(method (dispose)
		(super dispose:)
		(DisposeScript ARENA_BLOCK)
	)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(= egoCanFight FALSE)
				(TrySkill PARRY 0 20)
				(theWarrior
					getTired: 1
					canFight: 0
					action: aaPARRYUP
					posn: (theWarrior baseX?) (+ (theWarrior baseY?) 5)
					stopUpd:
				)
				(theEgoShield
					setCel: 2
					posn: (+ (theEgoShield x?) 22) (- (theEgoShield y?) 10)
				)
				(= cycles 2)
			)
			(1 (= cycles 6))
			(2
				(theEgoShield
					setCel: 0
					posn: (- (theWarrior baseX?) 74) (theWarrior baseY?)
				)
				(= cycles 4)
			)
			(3
				(theEgoShield stopUpd:)
				(theWarrior posn: (theWarrior baseX?) (theWarrior baseY?))
				(= egoCanFight TRUE)
				(self dispose:)
			)
		)
	)
)

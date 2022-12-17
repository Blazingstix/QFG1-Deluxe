;;; Sierra Script 1.0 - (do not remove this comment)
(script# ARENA_DODGE) ;ARENA_DODGE = 154
(include system.sh) (include sci2.sh) (include game.sh)
(use Main)
(use _System)

(public
	egoDodge 0
)

(local
	theWarrior
	theEgoShield
	theEgoHand
	theEgosBack
)
(instance egoDodge of Script
	(properties
		;register 0 ; 0,1.  0 = Dodge Left, 1 = Dodge Right 
		)
	
	(method (init)
		(= theWarrior (ScriptID WARRIOR 0))
		(= theEgoShield (theWarrior egoShield?))
		(= theEgoHand (theWarrior egoHand?))
		(= theEgosBack (theWarrior egosBack?))
		(super init: &rest)
	)
	
	(method (dispose)
		(super dispose:)
		(DisposeScript ARENA_DODGE)
	)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(= egoCanFight FALSE)
				(TrySkill DODGE 0 20)
				(theWarrior
					getTired: 2
					canFight: 0
					action: (if (== register 0) aaDODGELEFT else aaDODGERIGHT)
				)
				(if theEgoShield
					(switch register
						(0
							(theEgoShield posn: (- (theEgoShield x?) 25) (- (theEgoShield y?) 5))
						)
						(1
							(theEgoShield posn: (+ (theEgoShield x?) 40) (- (theEgoShield y?) 5))
						)
					)
				else
					(switch register
						(0
							(theEgoHand posn: (- (theEgoHand x?) 38) (+ (theEgoHand y?) 5))
						)
						(1
							(theEgoHand posn: (+ (theEgoHand x?) 46) (+ (theEgoHand y?) 5))
						)
					)
				)
				(switch register
					(0
						(theWarrior posn: (- (theWarrior x?) 41) (+ (theWarrior y?) 5))
						(theEgosBack posn: (- (theEgosBack x?) 40) (+ (theEgosBack y?) 5))
					)
					(1
						(theWarrior posn: (+ (theWarrior x?) 42) (+ (theWarrior y?) 5))
						(theEgosBack posn: (+ (theEgosBack x?) 40) (+ (theEgosBack y?) 5))
					)
				)
				(= cycles 7)
			)
			(1
				(theWarrior posn: (theWarrior baseX?) (theWarrior baseY?))
				(if theEgoShield
					(theEgoShield posn: (- (theWarrior baseX?) 74) (theWarrior baseY?))
				else
					(theEgoHand posn: (- (theWarrior baseX?) 74) (theWarrior baseY?))
				)
				(theEgosBack posn: (- (theWarrior baseX?) 41) (theWarrior baseY?))
				(self cue:)
			)
			(2
				(= egoCanFight FALSE)
				(self dispose:)
			)
		)
	)
)

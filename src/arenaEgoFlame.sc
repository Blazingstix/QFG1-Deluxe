;;; Sierra Script 1.0 - (do not remove this comment)
(script# ARENA_FLAME) ;ARENA_FLAME = 147
(include system.sh) (include sci2.sh) (include game.sh)
(use Main)
(use _Motion)
(use _System)

(public
	egoFlame 0
)

(local
	theSpellProp
	theWarrior
	local2
	local3
)
(instance egoFlame of Script
	(properties)
	
	(method (init)
		(= theWarrior (ScriptID WARRIOR 0))
		(= theSpellProp (ScriptID CLOSECOMBAT 1))
		(super init: &rest)
	)
	
	(method (dispose)
		(HandsOn)
		(super dispose:)
		(DisposeScript ARENA_FLAME)
	)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(HandsOff)
				(theWarrior canFight: FALSE action: aaMAGIC)
				(self cue:)
			)
			(1
				(theSpellProp
					setLoop: 6
					setCel: 0
					cycleSpeed: 1
					ignoreActors:
					posn: (- (theWarrior x?) 77) (- (theWarrior y?) 87)
					init:
					setCycle: CycleTo 2 cdFORWARD self
				)
			)
			(2
				(= local2
					(/ (- ((theWarrior opponent?) flameX?) (theSpellProp x?)) 3)
				)
				(= local3
					(/ (- (theSpellProp y?) ((theWarrior opponent?) flameY?)) 3)
				)
				(theSpellProp
					setPri: 15
					setCel: 3
					posn: (+ (theSpellProp x?) local2) (- (theSpellProp y?) local3)
				)
				(= cycles 2)
			)
			(3
				(theSpellProp
					setCel: 4
					posn: (+ (theSpellProp x?) local2) (- (theSpellProp y?) local3)
				)
				(= cycles 2)
			)
			(4
				(theSpellProp
					setCel: 5
					posn: (+ (theSpellProp x?) local2) (+ (theSpellProp y?) local3)
				)
				(= cycles 2)
			)
			(5
				(theSpellProp setCel: RELEASE setCycle: EndLoop self)
			)
			(6
				(theSpellProp dispose:)
				((theWarrior opponent?) getHurt: (+ 5 (/ [egoStats FLAMEDART] 3)))
				(theWarrior canFight: TRUE show:)
				(self dispose:)
			)
		)
	)
)

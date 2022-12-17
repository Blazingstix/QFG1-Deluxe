;;; Sierra Script 1.0 - (do not remove this comment)
(script# ARENA_THRUST) ;ARENA_THRUST = 151
(include system.sh) (include sci2.sh) (include game.sh)
(use Main)
(use _Motion)
(use _System)

(public
	egoThrust 0
)

(local
	theWarrior
	successfulAttack
)
(instance egoThrust of Script
	(properties
		;register  NULL ; theWarrior's Loop to use
		)
	
	(method (init)
		(= theWarrior (ScriptID WARRIOR 0))
		(super init: &rest)
	)
	
	(method (dispose)
		(super dispose:)
		(DisposeScript ARENA_THRUST)
	)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(= egoCanFight FALSE)
				(TrySkill WEAPON 0 0)
				(= successfulAttack (theWarrior tryAttack: (theWarrior opponent?)))
				(if
					;if the Minotaur or Brigand Parry, they always successfully block the thrust.
					(and
						(or (== monsterInRoom MINOTAUR) (== monsterInRoom BRIGAND))
						(== ((theWarrior opponent?) action?) aaPARRYUP)
					)
					(= successfulAttack FALSE)
				)
				(if successfulAttack
					(if
						(or
							(== monsterInRoom CHEETAUR)
							(== monsterInRoom BEAR)
							(== monsterInRoom SAURUS)
							(== monsterInRoom MINOTAUR)
							(== monsterInRoom BRIGAND)
						)
						(= register 3)
					)
					(theWarrior
						getTired: 4
						canFight: 0
						action: 1
						setLoop: register
						setPri: (if (== monsterInRoom MANTRAY) 1 else 9)
						setCel: 1
					)
				)
				(= cycles 8)
			)
			(1
				(if successfulAttack
					(= successfulAttack 0)
					(theWarrior doDamage: (theWarrior opponent?) zapMeleeBonus)
					(= zapMeleeBonus 0)
				)
				(theWarrior
					setPri: 11
					setLoop: register
					setCycle: CycleTo 0 cdFORWARD self
				)
			)
			(2
				(= egoCanFight TRUE)
				(self dispose:)
			)
		)
	)
)

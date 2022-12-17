;;; Sierra Script 1.0 - (do not remove this comment)
(script# ARENA_PAIN) ;ARENA_PAIN = 155
(include system.sh) (include sci2.sh) (include game.sh)
(use Main)
(use _System)

(public
	painReaction 0
)

(local
	theWarrior
	theEgoHand
	theEgosBack
	theEgoShield
)
(instance painReaction of Script
	(properties)
	
	(method (init)
		(= theWarrior (ScriptID WARRIOR 0))
		(= theEgoHand ((ScriptID WARRIOR 0) egoHand?))
		(= theEgosBack ((ScriptID WARRIOR 0) egosBack?))
		(= theEgoShield ((ScriptID WARRIOR 0) egoShield?))
		(super init: &rest)
	)
	
	(method (dispose)
		(super dispose:)
		(DisposeScript ARENA_PAIN)
	)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(= egoCanFight FALSE)
				(theEgosBack posn: (- (theEgosBack x?) 3) (+ (theEgosBack y?) 5))
				(theWarrior posn: (- (theWarrior x?) 3) (+ (theWarrior y?) 5))
				(if theEgoShield
					(theEgoShield posn: (- (theEgoShield x?) 5) (+ (theEgoShield y?) 10))
				else
					(theEgoHand posn: (- (theEgoHand x?) 5) (+ (theEgoHand y?) 10))
				)
				(= cycles 3)
			)
			(1
				(if (Btst HERO_KILLED_IN_BATTLE)
					;flag the register as true, this signals the warrier that he has died, and will display the death message as appropriate
					(theWarrior register: TRUE)
;;;					;CI: NOTE: why are we doing this here? Why can't it be part of the Warrior class?
;;;
;;;					(EgoDead DIE_RETRY DIE_ARENA 155 0
;;;						#title {What a monster!}
;;;						#icon (GetEgoViewNumber vEgoDefeatedMagic) 0 9)
;;;					;It was a tough battle, and you lost.
;;;					;Never fear!  All you have to do is restore your game, and...\nWhat do you mean, "Restore WHAT game?"
;;;
;;;					;now we set up what happens if the player selects Retry:
;;;					;restore 1/4 of total health.
;;;					(ReduceHP (- (/ (MaxHealth) 4)))
;;;					;clear the Monster
;;;					(= monsterInRoom NULL)
;;;					;go back to the forest.
;;;					(curRoom newRoom: prevRoomNum)
;;;					;CI: TODO: account for special battles, like bear, or minotaur.
				else
					(theEgosBack posn: (- (theWarrior baseX?) 41) (theWarrior baseY?))
					(theWarrior posn: (theWarrior baseX?) (theWarrior baseY?))
					(if theEgoShield
						(theEgoShield posn: (- (theWarrior baseX?) 74) (theWarrior baseY?))
					else
						(theEgoHand posn: (- (theWarrior baseX?) 73) (theWarrior baseY?))
					)
					(= cycles 1)
				)
				(= egoCanFight TRUE)
			)
			(2 (self dispose:))
		)
	)
)

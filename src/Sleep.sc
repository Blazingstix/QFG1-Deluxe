;;; Sierra Script 1.0 - (do not remove this comment)
(script# EGOSLEEP) ;EGOSLEEP = 7
(include system.sh) (include sci2.sh) (include game.sh)
(use Main)
(use _System)

(public
	EgoSleeps 0
	NeedSleep 1
)

(procedure (EgoSleeps hour minutes &tmp hoursSlept timeBeforeSleep)
	(if (not (NeedSleep))
		(HighPrint 7 0)
		;You just can't sleep during the daytime.
	else
		(= daysWithoutSleep 0)
		(= timeBeforeSleep currentTime)
		(switch argc
			(0 
				(FixTime 5)
				;by default, sleep until 5am.
			)
			(1 
				(FixTime hour)
			)
			(else 
				(FixTime hour minutes)
			)
		)
		;these are the only rooms in the game you can sleep at.
		(if (OneOf curRoomNum 10 40 141 76 83 302 300 310 320 330) ;erana's peace, castle stables, castle, dryad, henry's, anywhere in the town.
;;;			(or
;;;				(== curRoomNum 10)
;;;				(== curRoomNum 40)
;;;				(== curRoomNum 141)
;;;				(== curRoomNum 76)
;;;				(== curRoomNum 83)
;;;				(== curRoomNum 302)
;;;				(== curRoomNum 300)
;;;				(== curRoomNum 310)
;;;				(== curRoomNum 320)
;;;				(== curRoomNum 330)
;;;			)
			(= hoursSlept (/ (mod (- (+ currentTime TICKSPERDAY) timeBeforeSleep) TICKSPERDAY) TICKSPERHOUR) )
			
			(= [egoStats STAMINA] (MaxStamina))
			;reset Stamina to max
			(if (== curRoomNum 10)
				(= [egoStats HEALTH] (MaxHealth))
				(= [egoStats MANA] (MaxMana))
				;if we're in Erana's Peace, reset HP and MP to maximum
			else
				(TakeDamage (- (* hoursSlept 2)))
				(UseMana (- (* hoursSlept 2)))
				;otherwise, restore 2 points for each hour slept.
			)
			(if (> timeBeforeSleep currentTime)
				(AdvanceDay)
			)
			(if
				(not
					(OneOf curRoomNum 300 302 310 320 330)
;;;					(if
;;;						(or
;;;							(== curRoomNum 300)
;;;							(== curRoomNum 302)
;;;							(== curRoomNum 310)
;;;							(== curRoomNum 320)
;;;						)
;;;					else
;;;						(== curRoomNum 330)
;;;					)
				)
				(HighPrint 7 1)
				;You awake as the sun begins to rise.
			)
		else
			(EgoDead DIE_RETRY DIE_NIGHTGAUNT 7 2
				#icon vDeathScenes 1 0
				#title {Night Gaunt Got ya.}
				;While you were asleep, something decided to make a meal of you.
				;You're not sure what it was, but you don't really care at this point.
				;You shouldn't go to sleep where the creatures of the night can get you.
			)
			;CI: NOTE: nothing needed to do to reset this death.
		)
	)
	(DisposeScript EGOSLEEP)
)

(procedure (NeedSleep)
	(return (if (>= timeZone TIME_SUNSET) else daysWithoutSleep))
)

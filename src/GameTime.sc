;;; Sierra Script 1.0 - (do not remove this comment)
(script# TIME) ;TIME = 813
(include system.sh) (include sci2.sh) (include game.sh)
(use Main)

(public
	AdvanceTime 0
)

(procedure (AdvanceTime hours minutes &tmp temp0)
	(switch argc
		(1
			(= temp0 (+ currentTime (* TICKSPERHOUR hours)))
		)
		(2
			(= temp0
				(+ currentTime (* TICKSPERHOUR hours) (/ (* TICKSPERHOUR minutes) 60))
			)
		)
	)
	(= temp0 (^ temp0 $0001))
	(if
		(or
			(and (< currentTime 1100) (>= temp0 1100))
			(and (< currentTime 2500) (or (>= temp0 2500) (< temp0 currentTime)))
		)
		(EatMeal)
	)
	(while (>= temp0 TICKSPERDAY)
		(= temp0 (- temp0 TICKSPERDAY))
		(AdvanceDay)
	)
	(FixTime
		(/ temp0 TICKSPERHOUR)
		(/ (* (mod temp0 TICKSPERHOUR) 60) TICKSPERHOUR)
	)
	(DisposeScript TIME)
)

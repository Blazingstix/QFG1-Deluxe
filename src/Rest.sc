;;; Sierra Script 1.0 - (do not remove this comment)
(script# EGOREST) ; EGOREST = 8
(include system.sh) (include sci2.sh) (include game.sh)
(use Main)
(use _Interface)
(use GameTime)

(public
	EgoRests 0
)

(procedure (EgoRests minutes showPrint)
	(if
		(and
			(<= currentDay lastRestDay)
			(<= currentTime (+ lastRestTime ONEHOUR))
			(> [egoStats STAMINA] (/ (MaxStamina) 2))
		)
		(HighPrint 8 0)
		;You're too impatient to rest right now.
	else
		(= lastRestDay currentDay)
		(= lastRestTime currentTime)
		(UseStamina (- minutes))
		(UseMana (- (/ minutes 5)))
		(TakeDamage (- (/ (+ minutes 5) 15)))
		(if showPrint
			(Printf 8 1 minutes)
			;After %d minutes rest, you feel better.
		)
		(AdvanceTime 0 minutes)
	)
	(DisposeScript EGOREST)
)

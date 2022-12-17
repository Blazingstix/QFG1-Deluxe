;;; Sierra Script 1.0 - (do not remove this comment)
(script# 112)
(include system.sh) (include sci2.sh) (include game.sh)
(use Main)
(use KoboldCave)
(use _Avoider)
(use _Motion)

(public
	searchCave 0
)

(instance searchCave of KScript
	(properties)
	
	(method (doit)
		(if (Btst KOBOLD_AWAKE) (self dispose:) else (super doit:))
	)
	
	(method (dispose)
		(ego setAvoider: 0 setMotion: 0)
		(KoboldFight TRUE)
		(super dispose:)
		(DisposeScript 985)
		(DisposeScript 982)
		(DisposeScript 112)
	)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(HandsOff)
				(ego setAvoider: Avoid)
				(if (< (ego x?) 160)
					(= register 0)
					(ego setMotion: MoveTo 37 128 self)
				else
					(= register 1)
					(ego setMotion: MoveTo 302 158 self)
				)
			)
			(1 (= seconds 3))
			(2
				(if register
					(ego setMotion: MoveTo 37 128 self)
				else
					(ego setMotion: MoveTo 302 158 self)
				)
			)
			(3 (= seconds 2))
			(4
				(ego setMotion: MoveTo 132 163 self)
			)
			(5 (= seconds 2))
			(6
				(KoboldFight TRUE)
				(self dispose:)
			)
		)
	)
)

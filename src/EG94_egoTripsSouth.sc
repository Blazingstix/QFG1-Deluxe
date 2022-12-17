;;; Sierra Script 1.0 - (do not remove this comment)
(script# 189)
(include system.sh) (include sci2.sh) (include game.sh)
(use Main)
(use _Motion)
(use _System)

(public
	egoTripsSouth 0
)

(instance egoTripsSouth of Script
	(properties)
	
	(method (dispose)
		(super dispose:)
		(DisposeScript 189)
	)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(HandsOff)
				(ego
					view: (GetEgoViewNumber vEgoFall2)
					setLoop: 0
					cel: 0
					cycleSpeed: 1
					illegalBits: 0
					setCycle: EndLoop
					setMotion: MoveTo (- (ego x?) 3) (- (ego y?) 12) self
				)
				(if (not ((ScriptID 94 0) notify: 1))
					((ScriptID 94 11) setScript: (ScriptID 192 0))
				)
			)
			(1
				(ego
					view: (GetEgoViewNumber vEgoTired)
					setLoop: 1
					setCel: 4
					cycleSpeed: 3
					setCycle: BegLoop self
				)
			)
			(2
				(StopEgo)
				(ego loop: 3)
				(= cycles 2)
			)
			(3
				(StopEgo)
				(ego loop: 1)
				(= cycles 2)
			)
			(4
				(StopEgo)
				(ego loop: 2)
				(= cycles 2)
			)
			(5 (HandsOn) (self dispose:))
		)
	)
)

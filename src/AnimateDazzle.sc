;;; Sierra Script 1.0 - (do not remove this comment)
(script# 106)
(include system.sh) (include sci2.sh) (include game.sh)
(use Main)
(use _Sound)
(use _Motion)
(use _System)

(public
	AnimateDazzle 0
)

(local
	gEgoObjSignal
	gEgoObjPriority
	gEgoObjIllegalBits
	newSound
)
(procedure (AnimateDazzle who script)
	(cond 
		((< 1 argc) (who setScript: clientCastDazz script))
		(argc (who setScript: clientCastDazz))
		(else (ego setScript: clientCastDazz))
	)
)

(instance clientCastDazz of Script
	(properties)
	
	(method (dispose)
		(HandsOn)
		(super dispose:)
		(DisposeScript 106)
	)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(= gEgoObjSignal (ego signal?))
				(= gEgoObjPriority (ego priority?))
				(= gEgoObjIllegalBits (ego illegalBits?))
				(HandsOff)
				(ego
					view: (GetEgoViewNumber vEgoMagicDetect)
					setLoop: (if (or (== (ego loop?) 1) (== (ego loop?) 3))
						2
					else
						3
					)
				)
				((= newSound (Sound new:))
					number: (GetSongNumber 17)
					priority: 6
					init:
					play:
				)
				(ego cel: 0 cycleSpeed: 2 setCycle: EndLoop self)
			)
			(1 (= cycles 2))
			(2
				(newSound stop: dispose:)
				(StopEgo)
				(ego
					loop: 2
					priority: gEgoObjPriority
					illegalBits: gEgoObjIllegalBits
					signal: gEgoObjSignal
				)
				(self dispose:)
			)
		)
	)
)

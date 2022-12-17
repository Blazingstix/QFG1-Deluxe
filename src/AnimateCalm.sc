;;; Sierra Script 1.0 - (do not remove this comment)
(script# 104)
(include system.sh) (include sci2.sh) (include game.sh)
(use Main)
(use _Sound)
(use _Motion)
(use _System)

(public
	AnimateCalm 0
)

(local
	gEgoObjSignal
	gEgoObjPriority
	gEgoObjIllegalBits
	newSound
)
(procedure (AnimateCalm param1 param2)
	(switch argc
		(0 (ego setScript: castCalm))
		(1 (param1 setScript: castCalm))
		(else 
			(param1 setScript: castCalm param2)
		)
	)
)

(instance castCalm of Script
	(properties)
	
	(method (dispose)
		(newSound stop: dispose:)
		(StopEgo)
		(HandsOn)
		(ego
			loop: 2
			priority: gEgoObjPriority
			illegalBits: gEgoObjIllegalBits
			signal: gEgoObjSignal
		)
		(super dispose:)
		(DisposeScript 104)
	)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				((= newSound (Sound new:))
					number: (GetSongNumber 39)
					priority: 6
					init:
				)
				(= gEgoObjSignal (ego signal?))
				(= gEgoObjPriority (ego priority?))
				(= gEgoObjIllegalBits (ego illegalBits?))
				(HandsOff)
				(StopEgo)
				(ego setLoop: 2)
				(= cycles 1)
			)
			(1
				(ego
					view: (GetEgoViewNumber vEgoMagicDetect)
					setLoop: 1
					setCel: 0
					setPri: (ego priority?)
					cycleSpeed: 2
					setCycle: EndLoop self
				)
				(newSound play:)
			)
			(2
				(TimedPrint 6 104 0)
				;Suddenly a feeling of peace and tranquility permeates the area.
				(= seconds 6)
			)
			(3 (ego setCycle: BegLoop self))
			(4 (self dispose:))
		)
	)
)

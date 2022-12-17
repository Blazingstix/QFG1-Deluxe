;;; Sierra Script 1.0 - (do not remove this comment)
(script# 105)
(include system.sh) (include sci2.sh) (include game.sh)
(use Main)
(use _Sound)
(use _Motion)
(use _System)

(public
	AnimateOpenSpell 0
)

(local
	gEgoObjSignal
	gEgoObjPriority
	gEgoObjIllegalBits
	newSound
)
(procedure (AnimateOpenSpell who script &tmp tmpWho tmpScript)
	(= tmpWho ego)
	(= tmpScript NULL)
	(if argc
		(if (> argc 1) (= tmpScript script))
		(= tmpWho who)
	)
	(tmpWho setScript: clientCastOpen tmpScript)
)

(instance clientCastOpen of Script
	(properties)
	
	(method (dispose)
		(HandsOn)
		(super dispose:)
		(DisposeScript 105)
	)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(= gEgoObjSignal (ego signal?))
				(= gEgoObjPriority (ego priority?))
				(= gEgoObjIllegalBits (ego illegalBits?))
				(HandsOff)
				(StopEgo)
				(ego setLoop: loopS)
				(= cycles 1)
			)
			(1
				(ego
					view: (GetEgoViewNumber vEgoMagicDetect)
					setLoop: loopE
					setCel: 0
					setPri: (ego priority?)
					cycleSpeed: 2
					setCycle: EndLoop self
				)
			)
			(2
				((= newSound (Sound new:))
					number: (GetSongNumber 35)
					priority: 6
					init:
					play:
				)
				(= cycles 3)
			)
			(3
				(newSound stop: dispose:)
				(StopEgo)
				(ego
					loop: loopS
					priority: gEgoObjPriority
					illegalBits: gEgoObjIllegalBits
					signal: gEgoObjSignal
				)
				(self dispose:)
			)
		)
	)
)

;;; Sierra Script 1.0 - (do not remove this comment)
(script# 188)
(include system.sh) (include sci2.sh) (include game.sh)
(use Main)
(use _Motion)
(use _System)

(public
	egoTripsNorth 0
)

(instance egoTripsNorth of Script
	(properties)
	
	(method (dispose)
		(super dispose:)
		(DisposeScript 188)
	)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(HandsOff)
				(ego
					view: (GetEgoViewNumber vEgoFall2)
					setLoop: 0
					cel: 0
					setPri: 3
					cycleSpeed: 0
					illegalBits: 0
					ignoreActors:
					setCycle: EndLoop
					setMotion: MoveTo (- (ego x?) 3) (+ (ego y?) 2) self
				)
			)
			(1
				((ScriptID 94 10)
					view: vAntwerp
					setLoop: 2
					setCel: 2
					setPri: 4
					illegalBits: 0
					ignoreActors:
					ignoreHorizon:
					x: (+ (ego x?) 3)
					y: 20
					yStep: 4
					init:
				)
				((ScriptID 94 10)
					setMotion: MoveTo ((ScriptID 94 10) x?) (- (ego y?) 8) self
				)
			)
			(2
				((ScriptID 94 10)
					moveSpeed: 1
					cycleSpeed: 1
					setCycle: Forward
				)
				(= cycles 20)
			)
			(3
				(if ((ScriptID 94 0) notify: 0)
					((ScriptID 94 1) setScript: (ScriptID 94 5))
					((ScriptID 94 2) setScript: (ScriptID 94 6))
					((ScriptID 94 3) setScript: (ScriptID 94 7))
					((ScriptID 94 4) setScript: (ScriptID 94 8))
				)
				(= cycles 20)
			)
			(4
				(EgoDead DIE_RETRY DIE_ENDGAME_ANTWERP 188 0
					#icon (GetEgoViewNumber vEgoFall2) 0 6
					#title {This is a ridiculous way to die}
				)
				;How embarrassing, to get this far and then fail to see the trip rope.  How did they get that Antwerp up there?
				(= curRoomNum 93)
				(curRoom newRoom: 94)
				(self dispose:)
			)
		)
	)
)

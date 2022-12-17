;;; Sierra Script 1.0 - (do not remove this comment)
(script# 187)
(include system.sh) (include sci2.sh) (include game.sh)
(use Main)
(use _Motion)
(use _System)

(public
	fallChasm 0
)

(instance fallChasm of Script
	(properties)
	
	(method (dispose)
		(super dispose:)
		(DisposeScript 187)
	)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(HandsOff)
				(if (== (ego loop?) loopN) (ego setLoop: loopW))
				(= cycles 5)
			)
			(1
				(ego setLoop: loopS)
				(= cycles 5)
			)
			(2
				(ego
					view: (GetEgoViewNumber vEgoShock)
					setLoop: 0
					cel: 0
					setPri: 7
					illegalBits: 0
					yStep: 8
					cycleSpeed: 5
					posn: (ego x?) (+ (ego y?) 2)
					setCycle: EndLoop self
				)
			)
			(3
				(ego setMotion: MoveTo (ego x?) 200 self)
			)
			(4
				(if ((ScriptID 94 0) notify: 0)
					((ScriptID 94 1) setScript: (ScriptID 94 5))
					((ScriptID 94 2) setScript: (ScriptID 94 6))
					((ScriptID 94 3) setScript: (ScriptID 94 7))
					((ScriptID 94 4) setScript: (ScriptID 94 8))
				)
				(= cycles 20)
			)
			(5
				(EgoDead DIE_RETRY DIE_ENDGAME_PORCUPINE 187 0
					#icon (GetEgoViewNumber vEgoFall2) 0 2
					#title {The thought sends quills up your spine.}
				)
				;You see a sign that reads, "Beware of Porcupine".  That's odd, you don't see a ...
				;You do, however, see many archers leaning over the chasm edge with arrows pointed toward you.
				;You now have a funny feeling you know who the porcupine is going to be in a few minutes.
				(= curRoomNum 93)
				(curRoom newRoom: 94)
				(self dispose:)
			)
		)
	)
)

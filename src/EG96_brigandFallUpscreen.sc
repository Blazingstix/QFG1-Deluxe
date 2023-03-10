;;; Sierra Script 1.0 - (do not remove this comment)
(script# 225)
(include system.sh) (include sci2.sh) (include game.sh)
(use Main)
(use _Motion)
(use _System)

(public
	fallUpscreen 0
	fallDownscreen 1
)

(instance fallUpscreen of Script
	(properties)
	
	(method (dispose)
		(Bset JESTER_NO_MORE_TALKING)
		(super dispose:)
		(DisposeScript 225)
	)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(HandsOff)
				(if (not (Btst PULLED_CHAIN))
					((ScriptID 96 6) setCel: 3)
					((ScriptID 96 5)
						setLoop: 4
						cel: 0
						cycleSpeed: 1
						setCycle: Forward
					)
				)
				(ego view: (GetEgoViewNumber vEgoFall2) setLoop: 0 cel: 0 setCycle: CycleTo 3 cdFORWARD self)
			)
			(1
				((ScriptID 96 16)
					number: (GetSongNumber 9)
					loop: 1
					priority: 2
					play:
				)
				(ego
					setPri: (- (ego priority?) 1)
					yStep: 6
					illegalBits: 0
					setCycle: EndLoop
					setMotion: MoveTo (ego x?) 220 self
				)
			)
			(2
				(if (not (TakeDamage 5))
					(EgoDead DIE_RETRY DIE_ENDGAME_FALLGUY1 225 0
						#title {You're the Fall Guy again.}
						#icon (GetEgoViewNumber vEgoClimbing) 2 5
					)
					;You're mad as heck, and you're not going to take it anymore. As a matter of fact, you CAN'T take it anymore.
					;Restore your strength and health and try again.
					(= [egoStats HEALTH] (MaxHealth))
					(curRoom resetRoom:)
				else
					(Bclr FLAG_258)
					(Bclr OPENING_LEADER_DOOR)
					(Bset FLAG_260)
					(if (Btst FLAG_256)
						(Bclr FLAG_256)
						(ego setScript: (ScriptID 226 0))
						(if (== client (ScriptID 96 4))
							(client setPri: (+ (client priority?) 1))
						)
						(client setCel: 0 setScript: 0)
					else
						(ego setScript: (ScriptID 226 0))
					)
				)
			)
		)
	)
)

(instance fallDownscreen of Script
	(properties)
	
	(method (dispose)
		(Bset JESTER_NO_MORE_TALKING)
		(super dispose:)
		(DisposeScript 225)
	)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(HandsOff)
				(if (not (Btst PULLED_CHAIN))
					((ScriptID 96 6) setCel: 3)
					((ScriptID 96 5)
						setLoop: 4
						cel: 0
						cycleSpeed: 1
						setCycle: Forward
					)
				)
				(ego view: (GetEgoViewNumber vEgoShock) setLoop: 0 cel: 0 setCycle: EndLoop self)
			)
			(1
				((ScriptID 96 16)
					number: (GetSongNumber 9)
					loop: 1
					priority: 2
					play:
				)
				(ego
					setPri: (+ (ego priority?) 1)
					yStep: 12
					illegalBits: 0
					setMotion: MoveTo (ego x?) 220 self
				)
			)
			(2
				(if (not (TakeDamage 5))
					(EgoDead DIE_RETRY DIE_ENDGAME_FALLGUY1 225 1
						#title {You're the Fall Guy again.}
						#icon (GetEgoViewNumber vEgoClimbing) 2 5
					)
					;You're mad as heck, and you just won't take it anymore.  As a matter of fact, you CAN'T take it anymore.
					;Start over, and remember to keep up your strength and health.
					(= [egoStats HEALTH] (MaxHealth))
					(curRoom resetRoom:)
				else
					(Bclr FLAG_258)
					(Bclr OPENING_LEADER_DOOR)
					(Bset FLAG_260)
					(if (Btst FLAG_256)
						(Bclr FLAG_256)
						(ego setScript: (ScriptID 226 0))
						(client setCel: 0 setScript: 0)
					else
						(ego setScript: (ScriptID 226 0))
					)
				)
			)
		)
	)
)

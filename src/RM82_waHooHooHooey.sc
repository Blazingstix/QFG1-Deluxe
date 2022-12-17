;;; Sierra Script 1.0 - (do not remove this comment)
(script# 138)
(include system.sh) (include sci2.sh) (include game.sh)
(use Main)
(use _Motion)
(use _System)

(public
	waHooHooHooey 0
)

(instance waHooHooHooey of Script
	(properties)
	
	(method (dispose)
		(super dispose:)
		(DisposeScript 138)
	)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(HandsOff)
				(ego
					view: (GetEgoViewNumber vEgoClimbing)
					illegalBits: 0
					setLoop: 2
					cel: 0
					x: (- (ego x?) 12)
					setStep: 0 4
					cycleSpeed: 1
					setCycle: EndLoop
					setMotion: MoveTo (ego x?) 106 self
				)
				((ScriptID 82 4)
					number: (GetSongNumber 9)
					loop: 1
					play:
				)
			)
			(1
				((ScriptID 82 4)
					number: (GetSongNumber 10)
					loop: 1
					play:
				)
				(if (not (TakeDamage 10))
					(EgoDead DIE_RETRY DIE_HENRY_FALL 138 0
						#icon (GetEgoViewNumber vEgoClimbing) 2 5
						#title {Your figure remains still and silent.}
						;Had you been healthier, you probably could have survived that fall.  In your weakened condition, however, you succumbed to your injuries.
					)
					(ego setHeroRetry:)
					;then reset all the other flags.
					(Bclr CLIMBED_HENRY_CLIFF)
					(Bclr FLAG_276)
					(HandsOn)
					(ego posn: 35 140 loop: loopE)
					(StopEgo)
					(client setScript: NULL)

				else
					(ego
						view: (GetEgoViewNumber vEgoDefeated)
						setLoop: 4
						cel: 0
						x: (+ (ego x?) 11)
						y: (+ (ego y?) 36)
					)
					(self cue:)
				)
			)
			(2
				(ego cycleSpeed: 1 setCycle: EndLoop self)
			)
			(3
				(Bclr CLIMBED_HENRY_CLIFF)
				(if (Btst FLAG_274) (Bclr FLAG_274) else
					(HighPrint 138 1)
					;Man, that's a narrow ledge up there!
					)
				(HandsOn)
				(ego setLoop: loopS)
				(StopEgo)
				(client setScript: NULL)
			)
		)
	)
)

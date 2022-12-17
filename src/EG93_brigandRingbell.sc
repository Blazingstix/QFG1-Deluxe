;;; Sierra Script 1.0 - (do not remove this comment)
(script# 279)
(include system.sh) (include sci2.sh) (include game.sh)
(use Main)
(use _Motion)
(use _System)

(public
	ringBell 0
)

(enum
	rm93 		;0
	minotaur 	;1
	gate 		;2
	openGate 	;3
	bell 		;4
	archer1 	;5
	archer2 	;6
	archer3 	;7
	archer4 	;8
)

(define ARCHER_RISE 25)

(instance ringBell of Script
	(properties)
	
	(method (dispose)
		(super dispose:)
		(DisposeScript 279)
	)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(HandsOff)
				((ScriptID 93 rm93) notify: 2)				;rm93
				((ScriptID 93 bell) setCycle: Forward)		;bell
				(HighPrint 279 0)
				;'DING DING DING DING....'
				;"Well, maybe the brigands aren't such inhospitable creatures after all."
				(= seconds 5)
			)
			(1
				((ScriptID 93 archer1)	;archer1
					setLoop: 4
					setCel: 0
					illegalBits: 0
					init:
					setMotion: MoveTo ((ScriptID 93 archer1) x?) (- ((ScriptID 93 archer1) y?) ARCHER_RISE)
				)
				(= cycles 2)
			)
			(2
				((ScriptID 93 archer3)	;archer3
					setLoop: 5
					setCel: 0
					illegalBits: 0
					init:
					setMotion: MoveTo ((ScriptID 93 archer3) x?) (- ((ScriptID 93 archer3) y?) ARCHER_RISE)
				)
				(= cycles 2)
			)
			(3
				((ScriptID 93 bell) setCel: 0 setCycle: 0)	;bell
				((ScriptID 93 archer2)	;archer2
					setLoop: 4
					setCel: 0
					illegalBits: 0
					init:
					setMotion: MoveTo ((ScriptID 93 archer2) x?) (- ((ScriptID 93 archer2) y?) ARCHER_RISE)
				)
				(= cycles 2)
			)
			(4
				((ScriptID 93 archer4) ;archer4
					setLoop: 5
					setCel: 0
					illegalBits: 0
					init:
					setMotion: MoveTo ((ScriptID 93 archer4) x?) (- ((ScriptID 93 archer4) y?) ARCHER_RISE)
				)
				(= cycles 2)
			)
			(5 (= seconds 2))
			(6
				((ScriptID 93 archer1) setCycle: EndLoop)	;archer1
				((ScriptID 93 archer2) setCycle: EndLoop)	;archer2
				((ScriptID 93 archer3) setCycle: EndLoop)	;archer3
				((ScriptID 93 archer4) setCycle: EndLoop)	;archer4
				(HighPrint 279 1)
				;Then again, maybe they are.
				(= seconds 4)
			)
			(7
				(EgoDead DIE_RETRY DIE_ENDGAME_RINGBELL 279 2
					#icon (GetEgoViewNumber vEgoDefeatedMagic) 0 9
					#title {You ring the bell and your bell gets rung.}
					;The brigands have an even temperament...all bad!   You seem to have a knack for doing the wrong thing.
				)
				((ScriptID 93 archer1) cel: 0 posn: ((ScriptID 93 archer1) x?) (+ ((ScriptID 93 archer1) y?) ARCHER_RISE))	;archer1
				((ScriptID 93 archer2) cel: 0 posn: ((ScriptID 93 archer2) x?) (+ ((ScriptID 93 archer2) y?) ARCHER_RISE))	;archer2
				((ScriptID 93 archer3) cel: 0 posn: ((ScriptID 93 archer3) x?) (+ ((ScriptID 93 archer3) y?) ARCHER_RISE))	;archer3
				((ScriptID 93 archer4) cel: 0 posn: ((ScriptID 93 archer4) x?) (+ ((ScriptID 93 archer4) y?) ARCHER_RISE))	;archer4
				(client setScript: NULL)
			)
		)
	)
)

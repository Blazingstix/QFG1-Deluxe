;;; Sierra Script 1.0 - (do not remove this comment)
(script# 278)
(include system.sh) (include sci2.sh) (include game.sh)
(use Main)
(use _Motion)
(use _System)

(public
	forceGate 0
)

(instance forceGate of Script
	(properties)
	
	(method (dispose)
		(super dispose:)
		(DisposeScript 278)
	)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(HandsOff)
				((ScriptID 93 0) notify: 2) ;rm93
				((ScriptID 93 0) notify: 1) ;rm93
				(ChangeGait MOVE_WALK 0)
				(ego setMotion: MoveTo 190 151 self)
			)
			(1
				(ego setLoop: loopS)
				(HighPrint 278 0)
				;"Well I'll huff, and I'll puff, and I'll smash the gate down."
				(= cycles 1)
			)
			(2
				(ego view: (GetEgoViewNumber vEgoBreathHeavy) setLoop: 0 setCel: 0 setCycle: Forward)
				(= seconds 3)
			)
			(3
				(StopEgo)
				(ego loop: loopN cel: 0)
				(= cycles 1)
			)
			(4
				(ego view: (GetEgoViewNumber vEgoRunning) setMotion: MoveTo 175 122 self)
			)
			(5
				(ego view: (GetEgoViewNumber vEgoFall) setLoop: 0 setCel: 0 posn: 175 112)
				(= cycles 1)
			)
			(6
				(ego view: (GetEgoViewNumber vEgoFall) setLoop: 0 setCel: 0 posn: 175 108)
				(= cycles 1)
			)
			(7
				(ego setCel: 1 posn: 175 104)
				(= cycles 1)
			)
			(8
				(ego setCel: 1 posn: 175 100)
				(= cycles 1)
			)
			(9
				(ego setCel: 2 setPri: 7 posn: 175 96)
				(= cycles 1)
			)
			(10
				(ego setCel: 2 setPri: 7 posn: 175 92)
				(= cycles 1)
			)
			(11
				(ego view: (GetEgoViewNumber vEgoFallDown) setLoop: 0 setCel: 0 posn: 163 87)
				(= cycles 3)
			)
			(12
				(ego setCel: 1)
				(= cycles 3)
			)
			(13
				(ego setCel: 2 posn: 163 90)
				(= cycles 3)
			)
			(14
				(ego setCel: 3 posn: 163 93)
				(= cycles 3)
			)
			(15
				(ego setCel: 4 posn: 163 98)
				(= cycles 3)
			)
			(16
				(ego
					view: (GetEgoViewNumber vEgoDefeated)
					setLoop: 4
					setCel: RELEASE
					cel: 0
					posn: 160 102
					cycleSpeed: 12
					setCycle: CycleTo 3 cdFORWARD self
				)
			)
			(17
				(ego setCel: RELEASE cel: 3 cycleSpeed: 2 setCycle: EndLoop self)
			)
			(18
				((ScriptID 93 0) notify: 0) ;rm93
				(HighPrint 278 1)
				;"Boy that feels good."
				(if (TrySkill STR tryBreakDownBrigandGate)
					((ScriptID 93 2) setScript: (ScriptID 93 3) self)  ;2= gate prop, 3= openGate script
				else
					(self cue:)
				)
			)
			(19 (HandsOff) (= seconds 3))
			(20
				(ego posn: (+ (ego x?) 9) (ego y?) loop: 2)
				(= seconds 1)
			)
			(21
				(StopEgo)
				(ego posn: (- (ego x?) 9) (+ (ego y?) 2))
				(if (Btst BRIGAND_GATE_OPEN)
					(ego illegalBits: cWHITE)
				else
					(ego illegalBits: (| cWHITE cLRED))
				)
				((ScriptID 93 0) notify: 3) ;rm93
				(HandsOn)
				(self dispose:)
			)
		)
	)
)

;;; Sierra Script 1.0 - (do not remove this comment)
(script# 274)
(include system.sh) (include sci2.sh) (include game.sh)
(use Main)
(use _Motion)
(use _System)

(public
	egoEntersFromCombat 0
)

(instance egoEntersFromCombat of Script
	(properties)
	
	(method (dispose)
		(super dispose:)
		(DisposeScript 274)
	)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(ego setMotion: MoveTo 181 158 self)
				((ScriptID 93 1)	;minotaur
					ignoreActors:
					setPri: 9
					cycleSpeed: 4
					setCycle: EndLoop
				)
			)
			(1 (ego loop: 2) (= cycles 2))
			(2
				(ego
					view: (GetEgoViewNumber vEgoDanceBow)
					setLoop: 0
					setCel: 0
					posn: (- (ego x?) 3) (+ (ego y?) 8)
				)
				(TimedPrint 4 274 0)
				;"I'M BAAD!"
				(= seconds 4)
			)
			(3
				((ScriptID 93 0) notify: 0) ;rm93
				(StopEgo)
				(HandsOn)
				(ego loop: loopS posn: (+ (ego x?) 3) (- (ego y?) 8))
				(if (Btst BRIGAND_GATE_OPEN)
					(ego illegalBits: (| cWHITE cYELLOW))
				else
					(ego illegalBits: (| cWHITE cYELLOW cLMAGENTA))
				)
				(HandsOn)
				(self dispose:)
			)
		)
	)
)

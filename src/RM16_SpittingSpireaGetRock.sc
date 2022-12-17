;;; Sierra Script 1.0 - (do not remove this comment)
(script# 292)
(include system.sh) (include sci2.sh) (include game.sh)
(use Main)
(use _Jump)
(use _Motion)
(use _System)

(public
	getRock 0
	youMissed 1
	heCaughtIt 2
	climbDown 3
)

(instance getRock of Script
	(properties)
	
	(method (dispose)
		(super dispose:)
		(DisposeScript 292)
	)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(HandsOff)
				(ego
					setMotion:
						MoveTo
						(ego x?)
						(if (> (ego y?) 175)
							(- (ego y?) 5)
						else
							(+ (ego y?) 5)
						)
				)
				(= cycles 5)
			)
			(1
				(ego
					view: (GetEgoViewNumber vEgoThrowing)
					setLoop: (Random 0 1)
					cel: 0
					cycleSpeed: 1
					setCycle: EndLoop
				)
				(= cycles 8)
			)
			(2
				(HighPrint 292 0)
				;You pick up a few small rocks.
				(ego setCycle: BegLoop self)
			)
			(3
				(ego get: iRock 10)
				(HandsOn)
				(ego loop: loopN setScript: NULL)
				(StopEgo)
			)
		)
	)
)

(instance youMissed of Script
	(properties)
	
	(method (dispose)
		(super dispose:)
		(DisposeScript 292)
	)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				((ScriptID 16 2)
					setStep: 30 20
					setMotion: MoveTo 0 (- ((ScriptID 16 2) y?) 100) self
				)
			)
			(1
				((ScriptID 16 2)
					setMotion: JumpTo (Random 130 160) (Random 145 160) self
				)
			)
			(2
				(HandsOn)
				((ScriptID 16 2) hide: setScript: NULL)
			)
		)
	)
)

(instance climbDown of Script
	(properties)
	
	(method (dispose)
		(super dispose:)
		(DisposeScript 292)
	)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(HandsOff)
				(if (== spireaStatus 1)
					(self changeState: 3)
				else
					(self cue:)
				)
			)
			(1
				(if (== spireaStatus 4) ((ScriptID 16 1) dispose:))
				(ego
					view: (GetEgoViewNumber vEgo)
					setCycle: Walk
					cycleSpeed: 1
					moveSpeed: 1
					setPri: 4
					setLoop: loopE
					setMotion: MoveTo 157 90 self
				)
			)
			(2
				(ego setLoop: loopS setPri: RELEASE setMotion: MoveTo 148 96 self)
			)
			(3
				(ego view: (GetEgoViewNumber vEgoClimbing) setLoop: 1 cel: 6 posn: 144 105)
				(= cycles 5)
			)
			(4
				(ego cycleSpeed: 2 moveSpeed: 0 setCycle: BegLoop self)
			)
			(5 (= cycles 2))
			(6
				(= spireaStatus 0)
				(StopEgo)
				(ego posn: 139 137 loop: loopN setScript: NULL)
				(HandsOn)
			)
		)
	)
)

(instance heCaughtIt of Script
	(properties)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(HandsOff)
				(ego setCel: 1)
				((ScriptID 16 1) posn: 144 49)
				(= cycles 1)
			)
			(1
				((ScriptID 16 1) hide:)
				(ego setCycle: CycleTo 3 cdFORWARD self)
			)
			(2
				(ego setCel: 4)
				((ScriptID 16 1) posn: 142 56 show:)
				(= seconds 4)
			)
			(3
				(TimedPrint 4 292 1)
				;You put the seed into your pack and climb down.
				(= seedTarget 2)
				(= seedInPlant 2)
				(= seconds 4)
			)
			(4
				(SolvePuzzle POINTS_GETSEED 8)
				(ego get: iSeed)
				(= spireaStatus 4)
				(Bset OBTAINED_SPIREA_SEED)
				(ego setScript: climbDown)
				(Bset SPIREA_INACTIVE)
			)
		)
	)
)

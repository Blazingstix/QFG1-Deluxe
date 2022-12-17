;;; Sierra Script 1.0 - (do not remove this comment)
(script# TAVERN_STOOL) ;TAVERN_STOOL = 338
(include system.sh) (include sci2.sh) (include game.sh)
(use Main)
(use _Motion)
(use _User)
(use _System)

(public
	sitDown 0
	getDown 1
)

(instance sitDown of Script
	(properties)
	
	(method (dispose)
		(super dispose:)
		(DisposeScript TAVERN_STOOL)
	)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(Bset HERO_SITTING)
				(HandsOff)
				(ego illegalBits: 0)
				(if (> (ego x?) 149)
					(ego setMotion: MoveTo 163 (ego y?) self)
				else
					(ego setMotion: MoveTo 134 (ego y?) self)
				)
			)
			(1
				(if (> (ego x?) 149)
					(ego setMotion: MoveTo 163 114 self)
				else
					(ego setMotion: MoveTo 134 114 self)
				)
			)
			(2
				(ego
					view: (GetEgoViewNumber vEgoInsideBar)
					loop: (if (> (ego x?) 149) 1 else 0)
					posn: 149 111
					setPri: 9
				)
				(self cue:)
			)
			(3 (ego setCycle: EndLoop self))
			(4
				(ego loop: 2 cel: 0 stopUpd:)
				((ScriptID TAVERN 5) show: setPri: 8)
				(HandsOn)
				(User canControl: FALSE)
				((ScriptID TAVERN 8) changeState: 6)
				(self dispose:)
			)
		)
	)
)

(instance getDown of Script
	(properties)
	
	(method (dispose)
		(super dispose:)
		(DisposeScript TAVERN_STOOL)
	)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(HandsOff)
				((ScriptID TAVERN 5) hide:)
				(ego loop: 1 cel: 7 setCycle: BegLoop self)
			)
			(1
				(ego
					loop: 2
					cel: 5
					illegalBits: cWHITE
					setCycle: Walk
					posn: 162 114
				)
				(StopEgo)
				(Bclr HERO_SITTING)
				((ScriptID TAVERN 8) changeState: 0)
				(HandsOn)
				(self dispose:)
			)
		)
	)
)

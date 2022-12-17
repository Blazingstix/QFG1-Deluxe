;;; Sierra Script 1.0 - (do not remove this comment)
(script# 272)
(include system.sh) (include sci2.sh) (include game.sh)
(use Main)
(use _Motion)
(use _System)

(public
	egoEntersNorth 0
)

(instance egoEntersNorth of Script
	(properties)
	
	(method (dispose)
		(super dispose:)
		(DisposeScript 272)
	)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(ego illegalBits: 0 setMotion: MoveTo 174 115 self)
			)
			(1
				((ScriptID 93 0) notify: 0) ;aka rm93
				(ego illegalBits: cWHITE)
				(if (not (if (Btst VISITED_BRIGAND_GATE) else (Btst DEFEATED_MINOTAUR)))
					(HighPrint 272 0)
					;"Grumble Grumble Grumble.  Stupid guard duty, third time this week.  Why always me?  Grumble Grumble Grumble."
				)
				(HandsOn)
				(self dispose:)
			)
		)
	)
)

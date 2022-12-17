;;; Sierra Script 1.0 - (do not remove this comment)
(script# 273)
(include system.sh) (include sci2.sh) (include game.sh)
(use Main)
(use _Motion)
(use _System)

(public
	egoEntersSouth 0
)

(instance egoEntersSouth of Script
	(properties)
	
	(method (dispose)
		(super dispose:)
		(DisposeScript 273)
	)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(ego setMotion: MoveTo 235 176 self)
			)
			(1
				(if (Btst SAVED_ELSA)
					(EgoDead DIE_RETRY DIE_ENDGAME_GREEDY 273 0
						#icon (GetEgoViewNumber vEgoDefeatedMagic) 0 9
						#title {Greedy Greedy Greedy .}
						;The brigands were waiting for you to come back.  They expected you to make another try for some of their treasure.
					)
					(Bset DIE_RETRY_INPROGRESS)
					(curRoom newRoom: 84)
					;we'll teleport back to the antwerp room, because that's really the start of this branch of returning to the hideout.
				else
					((ScriptID 93 0) notify: 0) ;rm93
					(if (Btst BRIGAND_GATE_OPEN)
						(ego illegalBits: cWHITE)
					else
						(ego illegalBits: (| cWHITE cLRED))
					)
					(if (not (if (Btst VISITED_BRIGAND_GATE) else (Btst DEFEATED_MINOTAUR)))
						(HighPrint 273 1)
						;"Grumble Grumble Grumble.  Stupid guard duty, third time this week.  Why always me?  Grumble Grumble Grumble."
					)
					(HandsOn)
					(self dispose:)
				)
			)
		)
	)
)

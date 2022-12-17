;;; Sierra Script 1.0 - (do not remove this comment)
(script# 71)
(include system.sh) (include sci2.sh) (include game.sh)
(use Main)
(use RandomEncounter)
(use _Motion)
(use _Game)
(use _User)
(use _System)

(public
	rm71 0
)

(instance rm71 of EncRoom
	(properties
		picture 703
		style DISSOLVE
		horizon 90
		north 63
		east 72
		south 78
		west 70
		encChance 15
	)
	
	(method (init)
		(if (and isNightTime (== prevRoomNum 70))
			(Load RES_SCRIPT 295)
		)
		(super init:)
		(StatusLine enable:)
		(self setLocales: FOREST)
		(StopEgo)
		(if (not monsterInRoom) (ego init:))
		(switch prevRoomNum
			(63
				(ego posn: 190 92 setMotion: MoveTo 190 190)
			)
			(72
				(ego posn: 318 140 setMotion: MoveTo 0 140)
			)
			(70
				(ego posn: 1 140 setMotion: MoveTo 50 140)
				(if (Btst GOT_FAIRIES_ATTENTION)
					(User canControl: FALSE)
					(User canInput: FALSE)
					(Bclr GOT_FAIRIES_ATTENTION)
					(self setScript: (ScriptID 295 0))
				)
			)
			(78
				(ego posn: 200 188 setMotion: MoveTo 200 0)
			)
		)
		(if
			(not
				(OneOf
					prevRoomNum
					BEAR MINOTAUR SAURUS MANTRAY CHEETAUR GOBLIN TROLL OGRE SAURUSREX BRIGAND 470
				)
			)
			(= egoX (ego x?))
			(= egoY (ego y?))
		)
		(self setRegions: ENCOUNTER)
	)
	
	(method (dispose)
		(Bset VISITED_FOREST_71)
		(super dispose:)
	)
)

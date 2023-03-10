;;; Sierra Script 1.0 - (do not remove this comment)
(script# 17)
(include system.sh) (include sci2.sh) (include game.sh)
(use Main)
(use RandomEncounter)
(use _Motion)
(use _Game)
(use _System)

(public
	rm17 0
)

(instance rm17 of EncRoom
	(properties
		picture 703
		style DISSOLVE
		horizon 90
		north 11
		east 18
		south 25
		west 16
		encChance 30
	)
	
	(method (init)
		(super init:)
		(StatusLine enable:)
		(self setLocales: FOREST)
		(StopEgo)
		(if (not monsterInRoom) (ego init:))
		(switch prevRoomNum
			(11
				(ego posn: 188 91 setMotion: MoveTo 190 190)
			)
			(16
				(ego posn: 1 140 setMotion: MoveTo 320 140)
			)
			(18
				(ego posn: 318 140 setMotion: MoveTo 0 140)
			)
			(25
				(ego posn: 160 188 setMotion: MoveTo 160 0)
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
		(Bset VISITED_FOREST_17)
		(super dispose:)
	)
)

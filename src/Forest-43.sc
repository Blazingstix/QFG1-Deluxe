;;; Sierra Script 1.0 - (do not remove this comment)
(script# 43)
(include system.sh) (include sci2.sh) (include game.sh)
(use Main)
(use RandomEncounter)
(use _Motion)
(use _Game)
(use _System)

(public
	rm43 0
)

(instance rm43 of EncRoom
	(properties
		picture 705
		style DISSOLVE
		south 57
		west 42
		encChance 20
		entrances (| reSOUTH reWEST)
	)
	
	(method (init)
		(super init:)
		(StatusLine enable:)
		(self setLocales: FOREST)
		(StopEgo)
		(if (not monsterInRoom) (ego init:))
		(switch prevRoomNum
			(42
				(ego posn: 0 140 setMotion: MoveTo 320 140)
			)
			(57
				(ego posn: 45 189 setMotion: MoveTo 45 0)
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
		(Bset VISITED_FOREST_43)
		(super dispose:)
	)
)

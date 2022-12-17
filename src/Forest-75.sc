;;; Sierra Script 1.0 - (do not remove this comment)
(script# 75)
(include system.sh) (include sci2.sh) (include game.sh)
(use Main)
(use RandomEncounter)
(use _Motion)
(use _Game)
(use _System)

(public
	rm75 0
)

(instance rm75 of EncRoom
	(properties
		picture 707
		style DISSOLVE
		horizon 30
		north 66
		west 74
		encChance 10
		entrances (| reNORTH reWEST)
	)
	
	(method (init)
		(super init:)
		(StatusLine enable:)
		(self setLocales: FOREST)
		(StopEgo)
		(if (not monsterInRoom) (ego init:))
		(switch prevRoomNum
			(66
				(ego posn: 160 32 setMotion: MoveTo 160 189)
			)
			(74
				(ego posn: 1 100 setMotion: MoveTo 320 100)
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
		(Bset VISITED_FOREST_75)
		(super dispose:)
	)
)

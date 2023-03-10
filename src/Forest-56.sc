;;; Sierra Script 1.0 - (do not remove this comment)
(script# 56)
(include system.sh) (include sci2.sh) (include game.sh)
(use Main)
(use RandomEncounter)
(use _Motion)
(use _Game)
(use _System)

(public
	rm56 0
)

(instance rm56 of EncRoom
	(properties
		picture 701
		style DISSOLVE
		horizon 90
		north 42
		east 57
		south 66
		west 54
		encChance 15
		entrances (| reNORTH reEAST reSOUTH)
	)
	
	(method (init)
		(super init:)
		(StatusLine enable:)
		(self setLocales: FOREST)
		(StopEgo)
		(if (not monsterInRoom) (ego init:))
		(switch prevRoomNum
			(42
				(ego posn: 180 92 setMotion: MoveTo 180 190)
			)
			(54
				(ego posn: 1 140 setMotion: MoveTo 320 140)
			)
			(57
				(ego posn: 318 140 setMotion: MoveTo 0 140)
			)
			(66
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
		(Bset VISITED_FOREST_56)
		(super dispose:)
	)
)

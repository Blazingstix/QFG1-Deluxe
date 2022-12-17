;;; Sierra Script 1.0 - (do not remove this comment)
(script# 23)
(include system.sh) (include sci2.sh) (include game.sh)
(use Main)
(use RandomEncounter)
(use _Motion)
(use _Game)
(use _System)

(public
	rm23 0
)

(instance rm23 of EncRoom
	(properties
		picture 704
		style DISSOLVE
		east 24
		south 34
		encChance 30
		entrances (| reEAST reSOUTH)
	)
	
	(method (init)
		(super init:)
		(StatusLine enable:)
		(self setLocales: FOREST)
		(StopEgo)
		(if (not monsterInRoom) (ego init:))
		(switch prevRoomNum
			(24
				(ego posn: 319 140 setMotion: MoveTo 0 140)
			)
			(34
				(ego posn: 275 189 setMotion: MoveTo 275 0)
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
		(Bset VISITED_FOREST_23)
		(super dispose:)
	)
)

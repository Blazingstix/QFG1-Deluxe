;;; Sierra Script 1.0 - (do not remove this comment)
(script# 11)
(include system.sh) (include sci2.sh) (include game.sh)
(use Main)
(use RandomEncounter)
(use _Motion)
(use _Game)
(use _System)

(public
	rm11 0
)

(instance rm11 of EncRoom
	(properties
		picture 704
		style DISSOLVE
		east 12
		south 17
		encChance 30
		entrances (| reEAST reSOUTH)
	)
	
	(method (init)
		(super init:)
		(StatusLine enable:)
		(StopEgo)
		(self setLocales: FOREST)
		(if (not monsterInRoom) (ego init:))
		(switch prevRoomNum
			(12
				(ego posn: 318 140 setMotion: MoveTo 0 140)
			)
			(17
				(ego posn: 275 188 setMotion: MoveTo 275 0)
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
		(Bset VISITED_FOREST_11)
		(super dispose:)
	)
)

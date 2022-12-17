;;; Sierra Script 1.0 - (do not remove this comment)
(script# 19)
(include system.sh) (include sci2.sh) (include game.sh)
(use Main)
(use RandomEncounter)
(use _Motion)
(use _Game)
(use _System)

(public
	rm19 0
)

(instance rm19 of EncRoom
	(properties
		picture 705
		style DISSOLVE
		south 26
		west 18
		encChance 30
		entrances (| reSOUTH reWEST)
	)
	
	(method (init)
		(super init:)
		(StatusLine enable:)
		(self setLocales: FOREST)
		(StopEgo)
		(if (not monsterInRoom) (ego init:))
		(switch prevRoomNum
			(18
				(ego posn: 0 140 setMotion: MoveTo 320 140)
			)
			(26
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
		(Bset VISITED_FOREST_19)
		(super dispose:)
	)
)

;;; Sierra Script 1.0 - (do not remove this comment)
(script# 92)
(include system.sh) (include sci2.sh) (include game.sh)
(use Main)
(use RandomEncounter)
(use _Motion)
(use _Game)
(use _System)

(public
	rm92 0
)

(instance rm92 of EncRoom
	(properties
		picture 707
		style DISSOLVE
		west 91
		encChance 50
		entrances (| reNORTH reWEST)
	)
	
	(method (init)
		(super init:)
		(StatusLine enable:)
		(self setLocales: FOREST)
		(StopEgo)
		(if (Bset VISITED_BRIGAND_COURTYARD) (Bset BRIGANDS_UNAWARE))
		(if (not monsterInRoom) (ego init:))
		(switch prevRoomNum
			(86
				(ego posn: 160 30 setMotion: MoveTo 160 190)
			)
			(91
				(ego posn: 1 100 setMotion: MoveTo 320 100)
			)
		)
		(if
			(not
				(OneOf prevRoomNum
					BEAR MINOTAUR SAURUS MANTRAY CHEETAUR GOBLIN TROLL OGRE SAURUSREX BRIGAND 470
				)
			)
			(= egoX (ego x?))
			(= egoY (ego y?))
		)
		(self setRegions: ENCOUNTER)
	)
	
	(method (doit)
		(if
		(and (== (ego onControl: origin) cLRED) (== (ego loop?) 3))
			(curRoom newRoom: 86)
		)
		(super doit:)
	)
	
	(method (dispose)
		(Bset VISITED_FOREST_92)
		(super dispose:)
	)
)

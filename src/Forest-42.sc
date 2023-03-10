;;; Sierra Script 1.0 - (do not remove this comment)
(script# 42)
(include system.sh) (include sci2.sh) (include game.sh)
(use Main)
(use RandomEncounter)
(use _Motion)
(use _Game)
(use _Actor)
(use _System)

(public
	rm42 0
)

(instance rm42 of EncRoom
	(properties
		picture 703
		style DISSOLVE
		horizon 90
		north 26
		east 43
		south 56
		encChance 20
		entrances (| reNORTH reEAST reSOUTH)
		illBits (| cWHITE cLMAGENTA)
	)
	
	(method (init)
		(super init:)
		(Load RES_VIEW vBushes)
		(StatusLine enable:)
		(self setLocales: FOREST)
		(StopEgo)
		(if (not monsterInRoom) (ego init:))
		(switch prevRoomNum
			(26
				(ego posn: 190 92 setMotion: MoveTo 190 190)
			)
			(43
				(ego posn: 318 140 setMotion: MoveTo 0 140)
			)
			(56
				(ego posn: 200 188 setMotion: MoveTo 200 0)
			)
		)
		(westBush setPri: 6 ignoreActors: init: addToPic:)
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
		(Bset VISITED_FOREST_42)
		(super dispose:)
	)
)

(instance westBush of View
	(properties
		y 150
		x 35
		view vBushes
	)
)

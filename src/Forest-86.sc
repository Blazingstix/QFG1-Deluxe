;;; Sierra Script 1.0 - (do not remove this comment)
(script# 86)
(include system.sh) (include sci2.sh) (include game.sh)
(use Main)
(use RandomEncounter)
(use _Motion)
(use _Game)
(use _Actor)
(use _System)

(public
	rm86 0
)

(instance rm86 of EncRoom
	(properties
		picture 702
		style DISSOLVE
		horizon 90
		south 92
		west 85
		encChance 100
		entrances (| reSOUTH reWEST)
		illBits (| cWHITE cLMAGENTA)
	)
	
	(method (init)
		(super init:)
		(StatusLine enable:)
		(self setLocales: FOREST)
		(StopEgo)
		(if (not monsterInRoom) (ego init:))
		(switch prevRoomNum
			(92
				(ego posn: 120 188 setMotion: MoveTo 120 0)
			)
			(85
				(ego posn: 1 140 setMotion: MoveTo 320 140)
			)
		)
		(northBush init:)
		(addToPics add: northBush doit:)
		(eastBush ignoreActors: setPri: 6 init: addToPic:)
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
	
	(method (dispose)
		(Bset VISITED_FOREST_86)
		(super dispose:)
	)
)

(instance northBush of PicView
	(properties
		y 71
		x 136
		view vBushes
		loop 2
	)
)

(instance eastBush of View
	(properties
		y 147
		x 278
		view vBushes
		loop 1
	)
)

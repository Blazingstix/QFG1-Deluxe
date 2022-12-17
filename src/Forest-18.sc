;;; Sierra Script 1.0 - (do not remove this comment)
(script# 18)
(include system.sh) (include sci2.sh) (include game.sh)
(use Main)
(use RandomEncounter)
(use _Motion)
(use _Game)
(use _Actor)
(use _System)

(public
	rm18 0
)

(instance rm18 of EncRoom
	(properties
		picture 700
		style DISSOLVE
		horizon 90
		east 19
		west 17
		encChance 30
		entrances (| reEAST reWEST)
		illBits (| cWHITE cLRED)
	)
	
	(method (init)
		(super init:)
		(Load RES_VIEW vBushes)
		(StatusLine enable:)
		(self setLocales: FOREST)
		(StopEgo)
		(ego illegalBits: (| cWHITE cLRED))
		(if (not monsterInRoom) (ego init:))
		(switch prevRoomNum
			(17
				(ego posn: 1 140 setMotion: MoveTo 320 140)
			)
			(19
				(ego posn: 318 140 setMotion: MoveTo 0 140)
			)
		)
		(addToPics
			add: northBush southBush
			eachElementDo: #init
			doit:
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
		(Bset VISITED_FOREST_18)
		(super dispose:)
	)
)

(instance northBush of PicView
	(properties
		y 86
		x 133
		view vBushes
		loop 2
	)
)

(instance southBush of PicView
	(properties
		y 207
		x 162
		view vBushes
		loop 2
		cel 1
		priority 15
	)
)

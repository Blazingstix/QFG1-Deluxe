;;; Sierra Script 1.0 - (do not remove this comment)
(script# 12)
(include system.sh) (include sci2.sh) (include game.sh)
(use Main)
(use RandomEncounter)
(use _Motion)
(use _Game)
(use _Actor)
(use _System)

(public
	rm12 0
)

(instance rm12 of EncRoom
	(properties
		picture 700
		style DISSOLVE
		horizon 90
		north 10
		east 13
		west 11
		encChance 30
		entrances (| reEAST reSOUTH)
		illBits (| cWHITE cLRED)
	)
	
	(method (init)
		(Load RES_VIEW vBushes)
		(super init:)
		(StatusLine enable:)
		(self setLocales: FOREST)
		(StopEgo)
		(ego illegalBits: (| cWHITE cLRED))
		(if (not monsterInRoom) (ego init:))
		(switch prevRoomNum
			(10
				(ego posn: 140 91 setMotion: MoveTo 140 190)
			)
			(11
				(ego posn: 0 140 setMotion: MoveTo 320 140)
			)
			(13
				(ego posn: 319 140 setMotion: MoveTo 0 140)
			)
		)
		(addToPics add: southBush eachElementDo: #init doit:)
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
		(Bset VISITED_FOREST_12)
		(super dispose:)
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

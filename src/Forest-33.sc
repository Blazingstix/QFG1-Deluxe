;;; Sierra Script 1.0 - (do not remove this comment)
(script# 33)
(include system.sh) (include sci2.sh) (include game.sh)
(use Main)
(use RandomEncounter)
(use _Sound)
(use _Motion)
(use _Game)
(use _Actor)
(use _System)

(public
	rm33 0
)

(instance tromp of Sound
	(properties
		number 66
		priority 1
	)
)

(instance rm33 of EncRoom
	(properties
		picture 702
		style DISSOLVE
		horizon 90
		north 22
		east 34
		south 45
		encChance 35
		entrances (| reNORTH reEAST reSOUTH)
		illBits (| cWHITE cYELLOW)
	)
	
	(method (init)
		(Load RES_VIEW vBushes)
		(if (== prevRoomNum 22)
			(Load RES_SOUND (GetSongNumber 66))
		)
		(super init:)
		(mouseDownHandler add: self)
		(StatusLine enable:)
		(self setLocales: FOREST)
		(StopEgo)
		(if (not monsterInRoom) (ego init:))
		(switch prevRoomNum
			(22
				(if (Btst BABAYAGA_HUT_OPEN) (HandsOff))
				(ego posn: 130 91 setMotion: MoveTo 130 190)
			)
			(34
				(ego posn: 319 140 setMotion: MoveTo 0 140)
			)
			(45
				(ego posn: 120 189 setMotion: MoveTo 120 0)
			)
		)
		(westBush setPri: 7 ignoreActors: init: addToPic:)
		(if (and (Btst BABAYAGA_HUT_OPEN) (not (Btst BABAYAGA_FLY_AWAY)))
			(Bclr BABAYAGA_HUT_OPEN)
			(self setScript: hutUp)
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
		(mouseDownHandler delete: self)
		(Bset VISITED_FOREST_33)
		(super dispose:)
	)
	
	(method (handleEvent event)
		(cond 
			((super handleEvent: event))
			((MouseClaimed ego event emRIGHT_BUTTON)
				(HighPrint 33 0)
				;My feet are killing me.
				)
		)
	)
)

(instance westBush of View
	(properties
		y 176
		x 24
		view vBushes
	)
)

(instance hutUp of Script
	(properties)
	
	(method (changeState newState)
		(switch (= state newState)
			(0 (= cycles 6))
			(1
				(tromp number: (GetSongNumber 66) init: play:)
				(ShakeScreen 3)
				(= cycles 5)
			)
			(2
				(HighPrint 33 1)
				;You feel the sound of dainty footsteps behind you.
				(self cue:)
			)
			(3 (= cycles 5))
			(4
				(tromp play:)
				(ShakeScreen 3)
				(Bclr BABAYAGA_HUT_OPEN)
				(HandsOn)
				(rm33 setScript: 0)
			)
		)
	)
)

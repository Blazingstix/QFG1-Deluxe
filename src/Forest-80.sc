;;; Sierra Script 1.0 - (do not remove this comment)
(script# 80)
(include system.sh) (include sci2.sh) (include game.sh)
(use Main)
(use RandomEncounter)
(use _Motion)
(use _Game)
(use _User)
(use _Actor)
(use _System)

(public
	rm80 0
)

(local
	brunoDirection
)

(enum -1
	dirRIGHT
	dirCENTER
	dirLEFT
)

(instance bruno of Actor
	(properties
		y 140
		x 240
		view vBruno
	)
	
	(method (handleEvent event)
		(cond 
			((super handleEvent: event))
			(
				(or
					(Said 'look/bruno')
					(MouseClaimed self event emRIGHT_BUTTON)
				)
				(HighPrint 80 0)
				;This man looks very tough.  From his clothing, you guess him to be a member of the Thieves' Guild.
			)
		)
	)
)

(instance knife1 of Actor
	(properties
		view vBruno
	)
)

(instance knife2 of Actor
	(properties
		view vBruno
	)
)

(instance rm80 of EncRoom
	(properties
		picture 701
		style DISSOLVE
		horizon 90
		north 73
		east 81
		south 86
		west 79
		encChance 20
		entrances (| reNORTH reEAST reWEST)
		illBits (| cWHITE cLRED)
	)
	
	(method (init)
		(super init:)
		(StatusLine enable:)
		(self setLocales: FOREST)
		(StopEgo)
		(if (not monsterInRoom) (ego init:))
		(switch prevRoomNum
			(73
				(ego posn: 180 92 setMotion: MoveTo 180 190)
				(= brunoDirection dirRIGHT)
			)
			(79
				(ego posn: 3 140 setMotion: MoveTo 320 140)
				(= brunoDirection dirRIGHT)
			)
			(81
				(ego posn: 318 140 setMotion: MoveTo 0 140)
				(= brunoDirection dirLEFT)
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
		(if (> brutusCountdown BRUTUSCOUNTBRUNO)
			(User canInput: FALSE)
			(self setScript: egoLoses)
		else
			(self setRegions: ENCOUNTER)
		)
	)
	
	(method (dispose)
		(Bset VISITED_FOREST_80)
		(super dispose:)
	)
)

(instance southBush of PicView
	(properties
		y 207
		x 158
		view vBushes
		loop 2
		cel 1
		priority 15
	)
)

(instance egoLoses of Script
	(properties)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(if (== brunoDirection dirLEFT)
					(bruno posn: 90 140)
				)
				(bruno
					init:
					setCycle: Walk
					setMotion: MoveTo (+ (bruno x?) 28) 140 self
				)
			)
			(1
				(HandsOff)
				(bruno
					setLoop: (if (== brunoDirection dirLEFT) 5 else 6)
					cel: 0
					cycleSpeed: 2
					setCycle: CycleTo 4 cdFORWARD self
				)
			)
			(2
				(knife1
					illegalBits: 0
					setLoop: 7
					ignoreActors:
					ignoreHorizon:
					xStep: 6
					yStep: 7
					init:
					setCycle: Forward
					posn: (+ (bruno x?) (* brunoDirection 41)) (- (bruno y?) 24)
					setMotion: MoveTo (ego x?) (- (ego y?) 22)
				)
				(bruno setCycle: CycleTo 5 cdFORWARD self)
			)
			(3
				(bruno setCycle: EndLoop)
				(knife2
					illegalBits: 0
					setLoop: 8
					ignoreActors:
					ignoreHorizon:
					xStep: 6
					yStep: 7
					init:
					setCycle: Forward
					posn: (+ (bruno x?) (* 41 brunoDirection)) (- (bruno y?) 27)
					setMotion: MoveTo (ego x?) (- (ego y?) 25) self
				)
				(ego setLoop: 1)
			)
			(4
				(knife1 dispose:)
				(knife2 dispose:)
				(ego
					view: (GetEgoViewNumber vEgoDefeatedMagic)
					setLoop: 3
					cycleSpeed: 1
					setMotion: 0
					setCycle: EndLoop
				)
				(= seconds 4)
			)
			(5
				(EgoDead DIE_RETRY DIE_RANGE_BRUNO2 80 1
					#icon (GetEgoViewNumber vEgoDefeatedMagic) 2 5
					#title {Timing is everything.}
					;Done in by poisoned daggers and desperate desperadoes!  Try to avoid annoying that deadly duo next time.
				)
				(Bset DIE_RETRY_INPROGRESS)
				(= currentTime prevGameTime)
				(curRoom newRoom: 73) ;go back to the target range.
			)
		)
	)
)

;;; Sierra Script 1.0 - (do not remove this comment)
(script# 85)
(include system.sh) (include sci2.sh) (include game.sh)
(use Main)
(use RandomEncounter)
(use _LoadMany)
(use _Wander)
(use _Sound)
(use _Motion)
(use _Game)
(use _User)
(use _Actor)
(use _System)

(public
	rm85 0
)

(local
	antwerpSplit
	gEgoObjX
	gEgoObjY
)
(instance rm85 of EncRoom
	(properties
		picture 701
		style DISSOLVE
		horizon 90
		east 86
		west 84
		encChance 30
		entrances (| reEAST reWEST)
		illBits (| cWHITE cLRED)
	)
	
	(method (init)
		(super init:)
		(Load RES_VIEW vBushes)
		(if (Btst ANTWERP_SKY)
			(Load RES_SCRIPT WANDER)
			(curRoom encChance: 0)
			(LoadMany RES_VIEW vAntwerp (GetEgoViewNumber vEgoKillAntwerp))
			(LoadMany RES_SOUND
				(GetSongNumber 9)
				(GetSongNumber 10)
				(GetSongNumber 11)
			)
			(antFalls number: (GetSongNumber 9) loop: 1 init:)
			(antSplats number: (GetSongNumber 10) loop: 1 init:)
			(babyBoing number: (GetSongNumber 11) loop: 1 init:)
		)
		(StatusLine enable:)
		(self setLocales: FOREST)
		(StopEgo)
		(if (not monsterInRoom) (ego init:))
		(switch prevRoomNum
			(84
				(ego posn: 1 140 setMotion: MoveTo 32 140)
			)
			(86
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
	
	(method (doit)
		(super doit:)
		(if (and (> (ego x?) 140) (Btst ANTWERP_SKY))
			(Bclr ANTWERP_SKY)
			(curRoom setScript: antwerped)
		)
	)
	
	(method (dispose)
		(Bset VISITED_FOREST_85)
		(DisposeScript WANDER)
		(super dispose:)
	)
	
	(method (handleEvent event)
		(switch (event type?)
			(evSAID
				(cond 
					((super handleEvent: event))
					((Said 'look[/!*,forest,greenery,clearing]')
						(if (> (cast size?) 2)
							(HighPrint 85 0)
							;You seem to have caused an Antwerp POPulation EXPLOSION!
						else
							(event claimed: FALSE)
						)
					)
					((Said 'look/antwerp,baby')
						(if (> (cast size?) 2)
							(HighPrint 85 0)
							;You seem to have caused an Antwerp POPulation EXPLOSION!
						else
							(HighPrint 85 1)
							;You see no Antwerps here.
						)
					)
					((Said 'capture,kill,beat,get,attack,fight,play/antwerp,baby')
						(if (> (cast size?) 2)
							(HighPrint 85 2)
							;The bouncing baby Antwerps are all so cute, you can't bring yourself to interfere with their playing.
						else
							(HighPrint 85 1)
							;You see no Antwerps here.
						)
					)
				)
			)
		)
	)
)

(instance northBush of PicView
	(properties
		y 82
		x 194
		view vBushes
		loop 2
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

(instance antwerp of Actor
	(properties
		yStep 20
		view vAntwerp
		xStep 8
	)
)

(instance antwerped of Script
	(properties)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(Bclr ANTWERP_SKY)
				(antFalls play:)
				(User canControl: FALSE)
				(ego
					setMotion: 0
					view: (GetEgoViewNumber vEgoKillAntwerp)
					setLoop: 1
					cycleSpeed: 1
					setCycle: EndLoop self
				)
			)
			(1
				(User canControl: FALSE)
				(antwerp
					init:
					ignoreActors: TRUE
					ignoreHorizon: TRUE
					illegalBits: 0
					setLoop: 2
					cel: 0
					setCycle: 0
					posn: 90 -30
					setPri: (+ (ego priority?) 1)
					setMotion: MoveTo (ego x?) (+ (ego y?) 4) self
				)
			)
			(2
				(antFalls stop:)
				(antSplats play:)
				(ego
					view: (GetEgoViewNumber vEgoKillAntwerp)
					cel: 0
					setLoop: 2
					setCel: RELEASE
					cycleSpeed: 1
					setCycle: Forward
				)
				(antwerp setCycle: EndLoop self)
			)
			(3
				(antwerp setCycle: BegLoop setMotion: MoveTo 270 0)
				(= cycles 80)
			)
			(4
				(EgoDead DIE_RETRY DIE_ANTWERP_FLATTENED 85 7
					#title {Trounced by a bounce!}
					#icon (GetEgoViewNumber vEgoDeathScenes) 0 0
					;You're obviously in no shape to continue the game.
				)
				;since we died from the antwerp, let's flag that we died, 
				;then go back to the Antwerp room, and try again.
				(Bset DIE_RETRY_INPROGRESS)
				(Bclr ANTWERP_SKY)
				(Bclr ANTWERP_SPLIT)
				(curRoom newRoom: 84) ;antwerp room
			)
		)
	)
	
	(method (handleEvent event)
		(switch (event type?)
			(evSAID
				(cond 
					((super handleEvent: event))
					((Said 'look,search/')
						(CenterPrint 85 3)
						;Looking up, you can see a small, blue dot in the sky, getting bigger... and bigger... and BIGGER!
					)
					((Said 'use,lift,draw/blade,dagger,weapon')
						(if (self state?)
							(CenterPrint 85 4)
							;Too late!
						else
							(= antwerpSplit TRUE)
							(curRoom setScript: splat)
						)
					)
					((Said 'cast/')
						(CenterPrint 85 5)
						;There's no time for that!
					)
					((Said 'run,escape/')
						(CenterPrint 85 6)
						;Where to? There is no escaping the hurtling blue blob.
					)
				)
			)
		)
	)
)

(instance antFalls of Sound
	(properties
		priority 8
	)
)

(instance antSplats of Sound
	(properties
		priority 10
	)
)

(instance babyBoing of Sound
	(properties
		priority 12
	)
)

(instance a1 of Actor
	(properties
		yStep 4
		view vAntwerp
		xStep 4
	)
	
	(method (doit)
		(if (== (self cel?) 0) (babyBoing loop: 1 play:))
		(super doit:)
	)
)

(instance a2 of Actor
	(properties
		yStep 4
		view vAntwerp
		xStep 4
	)
)

(instance a3 of Actor
	(properties
		yStep 4
		view vAntwerp
		xStep 4
	)
)

(instance a4 of Actor
	(properties
		yStep 4
		view vAntwerp
		xStep 4
	)
)

(instance a5 of Actor
	(properties
		yStep 4
		view vAntwerp
		xStep 4
	)
)

(instance splat of Script
	(properties)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(User canControl: FALSE canInput: FALSE)
				(ego view: (GetEgoViewNumber vEgoKillAntwerp) setLoop: 0 cycleSpeed: 0 setCycle: EndLoop self)
			)
			(1
				(antwerp
					init:
					ignoreActors: TRUE
					ignoreHorizon: TRUE
					illegalBits: 0
					setLoop: 2
					cel: 0
					setCycle: 0
					posn: 90 -30
					setPri: (+ (ego priority?) 1)
					setMotion: MoveTo (ego x?) (- (ego y?) 12) self
				)
			)
			(2
				(antSplats play:)
				(ego setCycle: BegLoop self)
				(Bset ANTWERP_SPLIT)
				(antwerp setLoop: 5 setCycle: EndLoop)
			)
			(3
				(= gEgoObjX (ego x?))
				(= gEgoObjY (ego y?))
				(antwerp
					setLoop: 5
					cel: 0
					setStep: 4 4
					posn: gEgoObjX gEgoObjY
					setMotion: MoveTo gEgoObjX (+ gEgoObjY 16)
					cycleSpeed: 1
					setCycle: EndLoop
				)
				(a1
					setLoop: 6
					posn: gEgoObjX gEgoObjY
					ignoreActors:
					init:
					setCycle: Forward
					illegalBits: 0
					setMotion: MoveTo (+ gEgoObjX 16) (+ gEgoObjY 22) self
				)
				(a2
					setLoop: 7
					posn: gEgoObjX gEgoObjY
					ignoreActors:
					init:
					setCycle: Forward
					illegalBits: 0
					setMotion: MoveTo (- gEgoObjX 10) (- gEgoObjY 10)
				)
				(a3
					setLoop: 6
					posn: gEgoObjX gEgoObjY
					ignoreActors:
					init:
					setCycle: Forward
					illegalBits: 0
					setMotion: MoveTo (+ gEgoObjX 8) (+ gEgoObjY 15)
				)
				(if (> detailLevel 0)
					(a4
						setLoop: 7
						posn: gEgoObjX gEgoObjY
						ignoreActors:
						init:
						setCycle: Forward
						illegalBits: 0
						setMotion: MoveTo (- gEgoObjX 14) (- gEgoObjY 5)
					)
					(a5
						setLoop: 6
						posn: gEgoObjX gEgoObjY
						ignoreActors:
						init:
						setCycle: Forward
						illegalBits: 0
						setMotion: MoveTo (+ gEgoObjX 16) (- gEgoObjY 10)
					)
				)
			)
			(4
				(a1 ignoreActors: 0 illegalBits: cWHITE setMotion: Wander)
				(a2 ignoreActors: 0 illegalBits: cWHITE setMotion: Wander)
				(a3 ignoreActors: 0 illegalBits: cWHITE setMotion: Wander)
				(if (> detailLevel DETAIL_LOW)
					(a4 ignoreActors: 0 illegalBits: cWHITE setMotion: Wander)
					(a5 ignoreActors: 0 illegalBits: cWHITE setMotion: Wander)
				)
				(StopEgo)
				(User canControl: TRUE canInput: TRUE)
				(client setScript: NULL)
			)
		)
	)
)

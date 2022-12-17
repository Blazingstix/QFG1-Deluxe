;;; Sierra Script 1.0 - (do not remove this comment)
(script# 73)
(include system.sh) (include sci2.sh) (include game.sh)
(use Main)
(use ThrowFlameDart)
(use ThrowDagger1)
(use ThrowRock)
(use AnimateDazzle)
(use TalkObj)
(use TargActor)
(use _GControl)
(use _Save)
(use _Motion)
(use _Game)
(use _Inventory)
(use _User)
(use _Actor)
(use _System)

(use _Interface)

(public
	rm73 0
)

(local
	roomState		;one of the enum's listed below:
	showShield		;TRUE/FALSE, should the shield and spear be visible
	egoStartHP
	startBrutusTimer
	;local4			;local4
	brutusDirection	;local5
	armorBonus		;local6
	daggersOnTarget
	daggerLimit =  55
	gEgoObjY
	;local10			;local10
	ClaimAllEvents
	brutusDazzled
	brutusThrowing			;brutusThrowing
)

;these are the possible states the room gets init'd as.
(enum 1
	SpyOnBrigands
	AboutToEngageBrutus
	BrutusDead
	KilledBrutusInArena
	SpyOnBrutus
)

(enum -1
	dirRIGHT
	dirCENTER
	dirLEFT
)

(procedure (RetrieveDaggers PrintNoDaggers &tmp ret)
	(= ret 0)
	(cond 
		((and
			(== curRoomNum roomDaggersDropped)
			(or daggersOnTarget daggersInRoom daggersInMonster [invDropped iDagger])
			)
			(ego
				get: iDagger (+ daggersOnTarget daggersInRoom daggersInMonster [invDropped iDagger])
			)
			(HighPrint 73 0)
			;You retrieve your daggers.
			(= ret daggersOnTarget)
		)
		(PrintNoDaggers
			(HighPrint 73 1)
			;There aren't any loose daggers here.
		)
	)
	(= [invDropped iDagger]
		(= daggersOnTarget
			(= daggersInMonster
				(= daggersInRoom (= roomDaggersDropped 0))
			)
		)
	)
	(return ret)
)

(instance dags of Set
	(properties)
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

(instance shield of RPicView
	(properties
		y 132
		x 140
		view vBrutus
		loop 8
	)
	
	(method (handleEvent event)
		(cond 
			((super handleEvent: event))
			(
				(or
					(MouseClaimed self event emRIGHT_BUTTON)
					(Said 'look/stuff,shield,spear')
				)
				(HighPrint 73 2)
				;The objects on the ground are a brigand's shield and spear.
			)
		)
	)
)

(instance brunoWin of SysWindow
	(properties
		color vGREY
		title {Bruno:}
	)
	
	(method (open &tmp temp0)
		(= top (- top (= temp0 (- top 12))))
		(= bottom (- bottom temp0))
		(super open:)
	)
)

(instance brutusWin of SysWindow
	(properties
		color vBLUE
		title {Brutus:}
	)
	
	(method (open &tmp temp0)
		(= top (- top (= temp0 (- top 12))))
		(= bottom (- bottom temp0))
		(super open:)
	)
)

(instance brunoTalk of TalkObj
	(properties
		tLoop 4
	)
)

(instance brutusTalk of TalkObj
	(properties
		tLoop 4
		cSpeed 4
	)
)

(instance brutus of TargActor
	(properties
		y 114
		x 136
		view vBrutus
		loop 4
	)
	
	(method (doit)
		(brutusTalk doit:)
		(super doit:)
	)
	
	(method (handleEvent event)
		(cond 
			(ClaimAllEvents 
				(event claimed: TRUE)
			)
			((super handleEvent: event))
			((or
				(Said 'look/brutus,man,thief,bandit,body')
				(MouseClaimed self event emRIGHT_BUTTON)
				)
				(if (Btst DEFEATED_BRUTUS)
					(HighPrint 73 3)
					;The dead brigand lies very still.
				else
					(HighPrint 73 4)
					;You see a hard-looking character who appears to be a thief.  He must be one of the Brigands!
				)
			)
			((Said 'get/shield')
				(if (Btst DEFEATED_BRUTUS)
					(HighPrint 73 5)
					;The dead brigand's shield is not worth your while.
				else
					(HighPrint 73 6)
					;You're kidding, right?
				)
			)
			((or
				(Said 'get/key')
				(Said 'search/bandit,man,thief,brutus,body')
				)
				(if (Btst DEFEATED_BRUTUS)
					(if (ego inRect: 90 92 230 188)
						(ego setScript: egoSearch)
					else
						(HighPrint 73 7)
						;Get closer to him.
					)
				else
					(HighPrint 73 6)
					;You're kidding, right?
				)
			)
			(else (brutusTalk handleEvent: event))
		)
	)
	
	(method (getHurt amount)
		(if
			(and
				(<= (= gMonsterHealth (- gMonsterHealth amount)) 0)
				(not (Btst DEFEATED_BRUTUS))
			)
			(= zapMeleeBonus 0)
			(= monsterInRoom 0)
			(Bset DEFEATED_BRUTUS)
			(self setScript: brutusDies)
		)
		(= brutusHP gMonsterHealth)
	)
)

(instance bruno of Actor
	(properties
		y 117
		x 102
		view vBruno
		loop 4
	)
	
	(method (doit)
		(brunoTalk doit:)
		(super doit:)
	)
	
	(method (handleEvent event)
		(cond 
			((super handleEvent: event))
			((or
				(Said 'look/bruno')
				(MouseClaimed self event emRIGHT_BUTTON)
				)
				(HighPrint 73 8)
				;This man looks very tough.  From his clothing, you guess him to be a member of the Thieves' Guild.
			)
			(else 
				(brunoTalk handleEvent: event)
			)
		)
	)
)

(instance target of RPicView
	(properties
		y 105
		x 187
		view vThrowingRange
		priority 1
	)
	
	(method (handleEvent event)
		(cond 
			((super handleEvent: event))
			((or
				(MouseClaimed self event emRIGHT_BUTTON)
				(Said 'look/target,board,sign')
				)
				(if daggersOnTarget
					(HighPrint 73 9)
					;You are using the old archery target for dagger practice.
				else 
					(HighPrint 73 10)
					;The old archery target looks as though it has not been used in quite some time.
				)
			)
		)
	)
)

(instance rm73 of Room
	(properties
		picture 73
		style IRISOUT
		east 74
		south 80
		west 72
	)

	;CI: NOTE: This is a manual disassembly of the asm. The original asm has been deleted.
	(method (init param1)
		(= startBrutusTimer brutusCountdown)
		
		(Load RES_VIEW vThrowingRange)
		(Load RES_SOUND (GetSongNumber 31))
		(Load RES_SCRIPT 101)
		(StatusLine enable:)
		(super init: param1)
		(self setLocales: FOREST)
		
		;if we were killed in the screen south of us (or the Brigand Arena), we're going to assume we're here for the first time
		(if (or (== prevRoomNum BRIGAND) (and (Btst DIE_RETRY_INPROGRESS) (== prevRoomNum 80)))
			(Bclr DIE_RETRY_INPROGRESS)
			(= roomState SpyOnBrigands)
			(if (Btst ENTERROOM_WEST)
				(= prevRoomNum 72)
			)
			(if (Btst ENTERROOM_EAST)
				(= prevRoomNum 74)
			)
		)
		
		;(= roomState 0)
		(cond
			;finding the two for the first time.
			((or (== roomState SpyOnBrigands) 
				 (and (Btst BEAR_GONE) (not (Btst SPIED_THIEVES)) (== timeZone TIME_MIDDAY) (or (== prevRoomNum 72) (== prevRoomNum 74)))
				)
				(= roomState SpyOnBrigands)
				
				(= brutusHP MAX_HP_BRUTUS)
				(= gMonsterHealth brutusHP)
				(= monsterInRoom BRIGAND)
				(Load RES_VIEW vBruno)
				(Load RES_VIEW vBrutus)
				(= showShield TRUE)
				(= prevGameTime currentTime)
			)
			;bruno has left (but is still around), and i've killed brutus.
			((and brutusCountdown (Btst DEFEATED_BRUTUS))
				(= roomState BrutusDead)
				(Load RES_VIEW vBrutus)
			)
			;bruno has left (but it still around), and Brusts sees ego enter the side.
			((and brutusCountdown (or (== prevRoomNum 72) (== prevRoomNum 74)) (not (Btst DEFEATED_BRUTUS)))
				(= roomState SpyOnBrutus)
				
				(Load RES_VIEW vBrutus)
				(= gMonsterHealth brutusHP)
				(= monsterInRoom BRIGAND)
				(= showShield TRUE)
			)
			;entered the targetrange from he south, and brutus is still alive.
			((and brutusCountdown (== prevRoomNum 80) (not (Btst DEFEATED_BRUTUS)))
				(= roomState AboutToEngageBrutus)
				
				(Load RES_VIEW vBrutus)
				(= gMonsterHealth brutusHP)
				(= monsterInRoom BRIGAND)
				(= brigandHead 6)
				(brutus posn: 134 120)
				(= showShield TRUE)
			)
			;just came back from a battle with Brutus
			((== prevRoomNum BRIGAND)
				(= roomState KilledBrutusInArena)
				
				(Bset DEFEATED_BRUTUS)
				(Load RES_VIEW vBrigandDefeated)
			)
		)
		
		(if showShield
			;add Brutus's shield and spear lying on the ground.
			(shield init:)
			(addToPics add: shield doit:)
			(features add: shield)
		)

		;these are required whether there is a meeting in the room or not.
		(target init:)
		(addToPics add: target doit:)
		(features add: target)
		(StopEgo)
		(ego init:)
		
		;set up armor bonus
		(= armorBonus 0)
		(if (ego has: iChainmail)
			(= armorBonus 5)
		)
		(if (ego has: iLeather)
			(= armorBonus 3)
		)
		
		(switch prevRoomNum
			(72
				(Bset ENTERROOM_WEST)
				(Bclr ENTERROOM_EAST)
				(ego posn: 2 160 setMotion: MoveTo 57 146)
				(= brutusDirection dirRIGHT) ;$FFFF
			)
			(74
				(Bset ENTERROOM_EAST)
				(Bclr ENTERROOM_WEST)
				(ego posn: 319 140 setMotion: MoveTo 276 133)
				(= brutusDirection dirLEFT)
			)
			(80
				(ego posn: 160 187 setMotion: MoveTo 160 170)
				(= brutusDirection dirCENTER)
			)
			(else
				(ego posn: 165 108 setMotion: MoveTo 165 125)
			)
		)

		;CI: NOTE: I believe the original asm had this before the switch. 
		;I've placed it here, because it appears to make more sense
		(brutus targDeltaX: 
			(if (== brutusDirection dirLEFT) 
				12 
			else 
				(if (== brutusDirection dirRIGHT)
					-12
				)
			)
		)
		
		(switch roomState
			(SpyOnBrigands
				;spying on the meeting.
				(self setScript: brigsMeet)
			)
			(SpyOnBrutus
				;Brutus is alone, but he spots us when we enter the room.
				;what was our health before Brutus started throwing daggers at us?
				(= egoStartHP [egoStats HEALTH])
				(brutus init: setScript: brutusThrows)
			)
			(AboutToEngageBrutus
				;Brutus is alone, and we're in the center section.
				(self setScript: brutusLives)
			)
			(BrutusDead	
				;Brutus is dead.
				(brutus view: vBrigandDefeated loop: 0 cel: 7 init: stopUpd:)
				(dags add:)
				(self setScript: nobodyHere)
				
			)
			(KilledBrutusInArena
				;just finished killing brutus in a fight.
				(= monsterInRoom NULL)
				(ego loop: loopN posn: 140 124)
				(self setScript: brutusDies)
			)
			(else
				;else, just a normal day at the target range.
				(dags add:)
				(self setScript: nobodyHere)
			)
		)
	)
	
	(method (dispose)
		(dags eachElementDo: #dispose #delete release:)
		(dags dispose:)
		;(knife1 dispose:)
		;(knife2 dispose:)
		(if (!= newRoomNum BRIGAND)
			(= monsterInRoom 0)
			(= brigandHead 0)
		)
		(super dispose:)
	)
	
	(method (handleEvent event &tmp spell theBrutus)
		(switch (event type?)
			(evSAID
				(cond 
					((super handleEvent: event))
					((Said 'look/dagger')
						(if daggersOnTarget
							(HighPrint 73 11)
							;The only daggers around here are the ones you've thrown at the target.
						else 
							(event claimed: FALSE)
						)
					)
					((Said 'get/spear')
						(HighPrint 73 12)
						;There are no useable spears near you.
					)
					((Said 'hop/bush')
						(HighPrint 73 13)
						;These bushes are full of thorns. You decide not to risk it.
					)
					((Said 'search,look/body,bandit,bruno,brutus')
						(if (Btst DEFEATED_BRUTUS)
							(HighPrint 73 14)
							;Some marauding beast must have taken the brigand's body away.
						else
							(HighPrint 73 15)
							;He isn't here.
						)
					)
					((Said 'rest[/!*]')
						(if (> brutusCountdown BRUTUSCOUNTREST)
							(= brutusCountdown (- brutusCountdown BRUTUSCOUNTREST))
						)
						(event claimed: FALSE)
					)
					((Said 'cast>')
						(= spell (SaidCast event))
						;CI: NOTE: This was originally a bug where the spell was cast, *then* the code was checked if you could cast the spell
						;It was found by 8bitKittyKat on 8 AUG 2022.
						;see https://sciprogramming.com/community/index.php?topic=2076.msg15503
						;I have fixed the spell by switching on the spell the user typed, then trying to cast the spell
						;(if (CastSpell spell)
						(switch spell
							(DETMAGIC
								(if (CastSpell spell) ;CI: added
									(HighPrint 73 16)
									;You detect no magic here.
								)
							)
							(DAZZLE
								(if (CastSpell spell) ;CI: added
									(if (AnimateDazzle)
										(if
										(or (Btst DEFEATED_BRUTUS) (== (self script?) nobodyHere))
											(HighPrint 73 17)
											;You waste a spell.
										else
											(= brutusDazzled TRUE)
										)
									)
								)
							)
							(FLAMEDART
								(if (CastSpell spell) ;CI: added
									(if
									(or (Btst DEFEATED_BRUTUS) (== (self script?) nobodyHere))
										(AnimateThrowingFlameDart NULL)
									else
										(AnimateThrowingFlameDart brutus)
									)
								)
							)
							(CALM
								(if (CastSpell spell) ;CI: added
									(HighPrint 73 18)
									;You've wasted a spell.
									(if
									(and (cast contains: brutus) (not (Btst DEFEATED_BRUTUS)))
										(HighPrint 73 19)
										;The brigand is too angry to calm.
									)
								)
							)
							(OPEN
								(if (CastSpell spell) ;CI: added
									(HighPrint 73 18)
									;You've wasted a spell.
									(HighPrint 73 20)
									;There's nothing here to open.
								)
							)
							;CI: NOTE: remove ZAP here and leave it to the main script to handle
							(ZAP
								(event claimed: FALSE)
							;	(= zapMeleeBonus (+ 5 (/ [egoStats ZAP] 10)))
							;	(if (or (ego has: iDagger) (ego has: iSword))
							;		(HighPrint 73 21)
							;		;Your weapon is now magically charged.
							;	else
							;		(HighPrint 73 22)
							;		;You don't seem to have a weapon to charge.
							;	)
							)
							(FETCH
								(if (CastSpell spell) ;CI: added
									(if
									(or (Btst DEFEATED_BRUTUS) (== (self script?) nobodyHere))
										(HighPrint 73 23)
										;You waste a spell. Fetch is only good for fetching small, visible objects.
									else
										(HighPrint 73 24)
										;Just wait. The nice brigand will throw you his knife.
									)
								)
							)
							(else
								;CI: NOTE: don't cast the spell
								;but also don't bump any other spells out.
								(HighPrint 73 25)
								;That spell is not useful here.
								)
						)
						;)
					)
					((Said 'throw/dagger,dagger')
						(= theBrutus 0)
						(if
						(and (cast contains: brutus) (not (Btst DEFEATED_BRUTUS)))
							(FaceObject ego brutus)
							(= theBrutus brutus)
						)
						(AnimateThrowingDagger theBrutus)
					)
					((Said 'throw/boulder')
						(= theBrutus 0)
						(if
						(and (cast contains: brutus) (not (Btst DEFEATED_BRUTUS)))
							(FaceObject ego brutus)
							(= theBrutus brutus)
						)
						(AnimateThrowingRock theBrutus)
					)
					((Said 'climb,climb[/wall]')
						(if isNightTime
							(if (< (ego y?) 135)
								(if (TrySkill CLIMB tryClimbIntoTown 0)
									(HighPrint 73 26)
									;After making sure nobody is watching, you climb over the town's wall.
									(curRoom newRoom: 300)
								else
									(HighPrint 73 27)
									;Climbing this wall is too difficult for your level of skill.  Keep practicing.
								)
							else
								(HighPrint 73 28)
								;You're not in a good spot for climbing the wall.
							)
						else
							(HighPrint 73 29)
							;You would have trouble convincing people you are a Hero if you climbed the wall into town during the day.
						)
					)
					((Said 'look/east,south,west,forest,forest')
						(HighPrint 73 30)
						;The forest is very overgrown near here.
						)
					((Said 'look[<at,around][/!*,range,clearing,area,hamlet,wall,north,building]')
						(HighPrint 73 31)
						;The wall and buildings of Spielburg can be seen over the heavy brush.  An old target leans against the town wall.
					)
				)
			)
		)
		(super handleEvent: event)
	)
)

(instance brigsMeet of Script
	(properties)
	
	(method (changeState newState)
		(if client
			(switch (= state newState)
				(0
					(Bset SPIED_THIEVES)
					(brunoTalk caller: self)
					(brutusTalk caller: self)
					(bruno init:)
					(brutus init:)
					(brunoTalk tWindow: brunoWin actor: bruno init:)
					(brutusTalk tWindow: brutusWin actor: brutus init:)
					(mouseDownHandler add: bruno brutus)
					(keyDownHandler add: bruno brutus)
					(= seconds 4)
				)
				(1
					(TalkObjMessages brunoTalk 73 33)
					;"So what's this about, anyway?"
				)
				(2
					(TalkObjMessages brutusTalk 73 34)
					;"Her Nibs is starting to get suspicious about us."
				)
				(3
					(TalkObjMessages brunoTalk 73 35)
					;"What's the bee in her bonnet?"
				)
				(4
					(TalkObjMessages brutusTalk 73 36)
					;"Seems the "hero" wandering around here has her leery.  She thinks he's going to go for the gold on her head."
				)
				(5
					(TalkObjMessages brunoTalk 73 37)
					;"What's it ta do with us?"
				)
				(6
					(TalkObjMessages brutusTalk 73 38    73 39)
					;"She's been asking too many questions 'bout us."
					;"And the laughing jackass' eyeing me.  I had to sneak out."
				)
				(7
					(TalkObjMessages brunoTalk 73 40)
					;"Then we got to avoid the ambush and use the back for a bit, 'til the heat is off."
				)
				(8
					(TalkObjMessages brutusTalk 73 41)
					;"Maybe we should just make our move now."
				)
				(9
					(TalkObjMessages brunoTalk 73 42    73 43    73 44)
					;"Naw, let's wait for the creep to go first."
					;"While she's busy with him, we take over."
					;"She'll take him out easy, then we take her out."
				)
				(10
					(TalkObjMessages brutusTalk 73 45)
					;"Where's the back door, then?"
				)
				(11
					(TalkObjMessages brunoTalk 73 46)
					;"Where the bouncer hops around.  Ya got your key still?"
				)
				(12
					(TalkObjMessages brutusTalk 73 47)
					;"Yeah."
				)
				(13
					(TalkObjMessages brunoTalk 73 48)
					;"Don't lose it.  I got the only other one.  Yull haveta search the rock for the keyhole.
					;It's hidden good.  And remember the 'word'."
				)
				(14
					(TalkObjMessages brutusTalk 73 49)
					;"What Word?"
				)
				(15
					(TalkObjMessages brunoTalk 73 50)
					;"The 'word' what lets ya in so that Fred goes away."
				)
				(16
					(TalkObjMessages brutusTalk 73 51    73 52)
					;"Oh yeah, sure."
					;"What is it?"
				)
				(17
					(TalkObjMessages brunoTalk 73 53)
					;"Hiden Goseke.  Ya better learn it."
				)
				(18
					(TalkObjMessages brutusTalk 73 54)
					;"You think I'm a dummy or somethin'?"
				)
				(19
					(TalkObjMessages brunoTalk 73 55    73 56)
					;"Say the 'word' before ya open the door or ya might make Fred mad."
					;"Ya don't want Fred ta get mad."
				)
				(20
					(TalkObjMessages brutusTalk 73 57)
					;"Hey, no problem.  What's the 'word' again?"
				)
				(21
					(TalkObjMessages brunoTalk 73 58)
					;"Hiden Goseke."
				)
				(22
					(TalkObjMessages brutusTalk 73 59)
					;"Hiden Goseke.  Got it."
				)
				(23
					(TalkObjMessages brunoTalk 73 60)
					;"I gotta get back before the Chief misses me.  Be back in a bit."
				)
				(24
					(TalkObjMessages brutusTalk 73 61)
					;"Hiden Goseke.  See ya."
					(SolvePuzzle POINTS_OVERHEARBRUNO 12)
				)
				(25
					(bruno
						illegalBits: 0
						ignoreActors:
						setLoop: RELEASE
						setCycle: Walk
						setMotion: MoveTo 170 226 self
					)
					(User canControl: FALSE)
				)
				(26
					(bruno dispose:)
					(User canControl: TRUE)
					(= brutusCountdown BRUTUSCOUNT)
					(client setScript: brutusWaits)
				)
			)
		)
	)
	
	(method (handleEvent event)
		(cond 
			((super handleEvent: event))
			((Said 'look/man,man,thief,bandit')
				(HighPrint 73 32)
				;You see a couple of hard-looking characters talking.  One looks like a thief; the other appears to be a fighter of some sort.
				)
			((Said 'ask,chat,throw,cast')
				(brunoTalk caller: NULL endTalk:)
				(brutusTalk caller: NULL endTalk:)
				(bruno setMotion: NULL)
				(client setScript: egoLoses)
			)
		)
	)
)

(instance egoLoses of Script
	(properties)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(HandsOff)
				(bruno
					setLoop: (if (> (ego x?) 160) 5 else 6)
					cel: 0
					cycleSpeed: 2
					setCycle: CycleTo 4 cdFORWARD self
				)
			)
			(1
				(knife1
					illegalBits: 0
					setLoop: 7
					ignoreActors:
					ignoreHorizon:
					xStep: 6
					yStep: 7
					init:
					setPri: (- (ego priority?) 1)
					setCycle: Forward
					posn: (+ (bruno x?) (* brutusDirection 41)) (- (bruno y?) 24)
					setMotion: MoveTo (ego x?) (- (ego y?) 22)
				)
				(bruno setCycle: CycleTo 5 cdFORWARD self)
			)
			(2
				(bruno setCycle: EndLoop)
				(knife2
					illegalBits: 0
					setLoop: 8
					ignoreActors:
					ignoreHorizon:
					xStep: 6
					yStep: 7
					setPri: (- (ego priority?) 1)
					init:
					setCycle: Forward
					posn: (+ (bruno x?) (* 41 brutusDirection)) (- (bruno y?) 27)
					setMotion: MoveTo (ego x?) (- (ego y?) 25) self
				)
				(ego setLoop: 1)
			)
			(3
				(knife1 dispose:)
				(knife2 dispose:)
				(ego
					view: (GetEgoViewNumber vEgoDefeatedMagic)
					setLoop: 3
					cycleSpeed: 1
					setMotion: 0
					setCycle: EndLoop self
				)
			)
			(4 
				(= seconds 4)
			)
			(5
				(EgoDead DIE_RETRY DIE_RANGE_BRUNO 73 62
					#icon (GetEgoViewNumber vEgoDefeatedMagic) 2 5
					#title {Keep your mouth shut.}
					;Done in by poisoned daggers and desperate desperadoes!  Try to avoid annoying that deadly duo next time.
				)
				;reset positions and begin the brigsMeet script over.
				(rm73 setScript: brigsMeet)
				(bruno posn: 102 117 loop: 4)
				(StopEgo)
				(switch prevRoomNum
					(72
						(ego loop: loopE posn: 57 146)
					)
					(74
						(ego loop: loopW posn: 276 133)
					)
				)
				
				(brigsMeet state: 0)
				(egoLoses state: 0)
				;reset the game time
				(= currentTime prevGameTime)
			)
		)
	)
)

(instance brutusWaits of Script
	(properties)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(= seconds brutusCountdown)
			)
			(1
				(if (not (Btst DEFEATED_BRUTUS))
					(brutus
						illegalBits: 0
						ignoreActors:
						setLoop: RELEASE
						loop: 1
						setCycle: Walk
						setMotion: MoveTo 170 224 self
					)
				)
			)
			(2
				(brutus dispose:)
			)
		)
	)
	
	(method (handleEvent event)
		(cond 
			((super handleEvent: event))
			((Said 'ask,chat')
				(brutus setMotion: NULL)
				(= egoStartHP [egoStats HEALTH])
				(client setScript: brutusThrows)
			)
			((and
				(!= (brutus script?) brutusThrows)
				(not (Btst DEFEATED_BRUTUS))
				(Said 'throw,cast')
				)
				(brutus setMotion: NULL)
				(= egoStartHP [egoStats HEALTH])
				(brutus setScript: brutusThrows)
				(event claimed: FALSE)
			)
		)
	)
)

(instance brutusDies of Script
	(properties)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(if brutusThrowing 
					(knife1 dispose:)
				)
				(StopEgo)
				(ego setScript: NULL)
				(HandsOff)
				(if (== prevRoomNum BRIGAND)
					(ego posn: 152 128)
					(brutus view: vBrigandDefeated loop: 0 init:)
				else
					(brutus view: vBrutus setLoop: 7)
				)
				(brutus
					posn: 139 114
					cel: 0
					cycleSpeed: 1
					setCycle: EndLoop self
				)
			)
			(1
				(HandsOn)
				(= ClaimAllEvents FALSE)
				(brutus stopUpd:)
				(dags add:)
				(client setScript: nobodyHere)
			)
		)
	)
)

(instance brutusLives of Script
	(properties)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(HandsOff)
				(brutus
					init:
					setCycle: Walk
					setMotion: MoveTo (ego x?) (ego y?)
				)
				(brutusTalk actor: 0)
				(HighPrint 73 63)
				;"Hey, watchu doin' here? Spyin', I'll wager!"
				(= cycles 11)
			)
			(1
				(curRoom newRoom: BRIGAND)
			)
		)
	)
)

(instance brutusThrows of Script
	(properties)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(brutus
					setLoop: (if (> (ego x?) 160) 5 else 6)
					cel: 0
					cycleSpeed: 2
					setCycle: CycleTo 4 cdFORWARD self
				)
			)
			(1
				(brutus setCycle: CycleTo 5 cdFORWARD self)
			)
			(2
				(if brutusDazzled
					(self cue:)
				else
					(brutus setCycle: EndLoop self)
					(= ClaimAllEvents TRUE)
				)
			)
			(3
				(if brutusDazzled
					(self cue:)
				else
					(= brutusThrowing TRUE)
					(knife1
						illegalBits: 0
						setLoop: 7
						ignoreActors:
						ignoreHorizon:
						xStep: 6
						yStep: 7
						init:
						setPri: (- (ego priority?) 1)
						setCycle: Forward
						posn: (+ (brutus x?) (* brutusDirection 32)) (- (brutus y?) 20)
						setMotion: MoveTo (ego x?) (- (ego y?) 22) self
					)
				)
			)
			(4
				(if brutusThrowing
					(= brutusThrowing FALSE)
					(knife1 dispose:)
					(if (not (TakeDamage (- 10 armorBonus)))
						;we've taken enough damage that we're now dead.
						(self changeState: 7)
					)
					(ego
						view: (GetEgoViewNumber vEgoDaggerDefeated)
						setLoop: (if (== brutusDirection dirLEFT) 3 else 2)
						cycleSpeed: 1
						setMotion: 0
						setCycle: EndLoop
					)
					(= cycles 12)
				else
					(self cue:)
				)
			)
			(5
				(StopEgo)
				(FaceObject ego brutus)
				(= ClaimAllEvents FALSE)
				(if brutusDazzled
					(= seconds 12)
					(= brutusDazzled FALSE)
				else
					(= seconds 3)
				)
			)
			(6
				(self changeState: 0)
			)
			(7
				(ego
					view: (GetEgoViewNumber vEgoDefeatedMagic)
					setLoop: 2
					setCel: RELEASE
					cel: 0
					cycleSpeed: 1
					setMotion: 0
					setCycle: EndLoop ;self
				)
			)
			(8
				(= seconds 2)
			)
			(9
				(EgoDead DIE_RETRY DIE_RANGE_BRUTUS 73 64
					#icon (GetEgoViewNumber vEgoDefeatedMagic) 2 5
					#title {Daggered to death.}
					;Done in by daggers and a desperate desperado!  Try to avoid annoying that deadly duo next time.
				)
				;restore HP to what it was when ego entered the room.
				(= [egoStats HEALTH] egoStartHP)
				
				;reset this so ego can talk
				(= ClaimAllEvents FALSE)
				(StopEgo)
				;(ego init:)
				
				;if we were here when the two guys started meeting.
				(if (== roomState SpyOnBrigands)
					;reset ego's position, based on which side we entered this room from.
					(switch prevRoomNum
						(72
							(ego loop: loopE posn: 57 146)
						)
						(74
							(ego loop: loopW posn: 276 133)
						)
					)
					;reset bruno, brutus, and finish their meeting.
					(bruno posn: 102 117 loop: 4)
					(brutus loop: 4)
					(rm73 setScript: brigsMeet)
					(brigsMeet changeState: 25)
					(= currentTime prevGameTime)
				else
					;then we entered the room after Bruno had already left
					;if we're short on time, then let's add some more time to the countdown.
					(= brutusCountdown startBrutusTimer)
					;now let's go back outside of the room.
					(Bset DIE_RETRY_INPROGRESS)
					(curRoom newRoom: prevRoomNum)
				)
			)
		)
	)
)

(instance nobodyHere of Script
	(properties)
	
	(method (changeState newState &tmp newProp temp1 temp2 temp3)
		(switch (= state newState)
			(1
				(HandsOff)
				(ego illegalBits: 0 ignoreActors:)
				(= gEgoObjY (ego y?))
				(cond 
					((< (ego x?) 129)
						(ego setMotion: MoveTo 134 (ego y?) self)
					)
					((> (ego x?) 180)
						(ego setMotion: MoveTo 173 (ego y?) self)
					)
					((< (ego y?) 110)
						(ego setMotion: MoveTo (ego x?) 116 self)
					)
					(else 
						(self cue:)
					)
				)
			)
			(2
				(ego setMotion: MoveTo 186 109 self)
			)
			(3
				(dags eachElementDo: #dispose #delete release:)
				(= seconds 2)
			)
			(4
				(dags add:)
				(ego
					setMotion: MoveTo 156 (if (> gEgoObjY 115) gEgoObjY else 115) self
				)
			)
			(5
				(StopEgo)
				(ego loop: loopN)
				(HandsOn)
			)
			(6
				(HandsOff)
				(ego
					view: (GetEgoViewNumber vEgoThrowing)
					setLoop: 2
					cel: 0
					cycleSpeed: 2
					setCycle: CycleTo 3 cdFORWARD self
				)
			)
			(7
				(ego setCycle: EndLoop)
				(TrySkill THROW 0 20)
				(= temp3 (/ (- 120 [egoStats THROW]) 10))
				(= temp1 (- (Random 0 temp3) (/ temp3 2)))
				(= temp2 (- (Random 0 temp3) (/ temp3 2)))
				(knife1
					illegalBits: 0
					setLoop: 7
					ignoreActors:
					ignoreHorizon:
					xStep: 6
					yStep: 7
					init:
					setPri: (- (ego priority?) 1)
					setCycle: Forward
					posn: (+ (ego x?) 30) (- (ego y?) 24)
					setMotion:
						MoveTo
						(+ (target x?) temp1)
						(+ (- (target y?) 22) temp2)
						self
				)
			)
			(8
				(if (< daggersOnTarget daggerLimit)
					((= newProp (Prop new:))
						init:
						view: vBruno
						loop: 7
						cel: (Random 0 6)
						posn: (knife1 x?) (knife1 y?)
						stopUpd:
					)
					(dags add: newProp)
				)
				(knife1 dispose:)
				(StopEgo)
				(ego loop: loopN)
				(HandsOn)
			)
		)
	)
	
	(method (handleEvent event)
		(cond 
			((super handleEvent: event))
			((Said 'throw/dagger,dagger')
				(cond 
					((or (== prevRoomNum 72) (== prevRoomNum 74))
						(HighPrint 73 65)
						;There are too many bushes between you and the target.
						)
					((< (ego y?) 135)
						(HighPrint 73 66)
						;You're too close to the target for it to be a challange.
						)
					((ego has: iDagger)
						(if (!= roomDaggersDropped curRoomNum)
							(= roomDaggersDropped curRoomNum)
							(= daggersInRoom (= daggersInMonster 0))
						)
						(-- [invNum iDagger])
						(++ daggersOnTarget)
						(self changeState: 6)
					)
					(else
						(HighPrint 73 67)
						;You have no daggers to throw.
						)
				)
			)
			((and (Said 'get/dagger') (RetrieveDaggers TRUE))
				(self changeState: 1)
			)
		)
	)
)

(instance egoSearch of Script
	(properties)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(HandsOff)
				(ego
					ignoreActors: 1
					illegalBits: 0
					setMotion: MoveTo 145 120 self
				)
			)
			(1
				(FaceObject ego brutus)
				(ego
					loop: (mod (+ (ego loop?) 4) 2)
					view: (GetEgoViewNumber vEgoThrowing)
					setCycle: EndLoop self
				)
			)
			(2
				(RetrieveDaggers FALSE)
				(if (Btst OBTAINED_BRUTUS_KEY)
					(HighPrint 73 68)
					;You find nothing else on the brigand's body.
				else
					(HighPrint 73 69)
					;You find a single key on the brigand's body, and put it away.
					(Bset OBTAINED_BRUTUS_KEY)
					(ego get: iBrassKey)
				)
				(StopEgo)
				(HandsOn)
				(client setScript: NULL)
			)
		)
	)
)

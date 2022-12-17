;;; Sierra Script 1.0 - (do not remove this comment)
(script# 70)
(include system.sh) (include sci2.sh) (include game.sh)
(use Main)
(use ThrowFlameDart)
(use ThrowDagger1)
(use AnimateCalm)
(use AnimateDazzle)
(use _Interface)
(use _LoadMany)
(use _Sound)
(use _Save)
(use _Motion)
(use _Game)
(use _Inventory)
(use _User)
(use _Actor)
(use _System)

(public
	rm70 0
)

;CI:TODO: Rewrite the Faery talking to use a local procedure instead.

(local
	eatMushroom
	fairiesOnScreen
	currentFaeryWindow
	startedRoom
	faeryCountdown
	[mushViews 8]
	[mushX 8] = [62 119 91 145 134 104 75 40]
	[mushY 8] = [109 108 108 107 101 98 97 101]
	[newAFaery 5]
	[faeryX 5] = [74 143 52 90 110]
	[faeryY 5] = [109 120 122 99 108]
	[faeryColor 5] = [vBLUE   vLCYAN vBROWN  vMAGENTA vGREY]
	[faeryBack 5] =  [vYELLOW vGREEN vLGREEN vWHITE   vLRED]
	[newAFaeryWindow 5]
	[newAFaeryScript 5]
	[faeryChaseScripts 5]
	faeryCycles
	[faeryX2 5] = [35 250 265 140 225]
	[faeryY2 5] = [30 35 125 50 45]
	faeryText		;the current text message the faeries are saying
	faeryState		
	faeryTextLast	;this is the last message in this group they're saying.
	danceCycles
	local84
	egoStandX =  137
	egoStandY =  137
	enteredRing		;entered Ring (with Faeries present)
	askedForDust
	local89 =  -1		;not really used for anything... only with an and (faerytext > local89)

	textHi				=  0	;Ooh, looky.  A human!
	textDance 			=  13	;Then dance for us!
	textTeachDance 		=  22	;We can show it how, can't we?
	textBadDance 		=  32	;Hee, Hee!
	textGoodDance 		=  38	;Ooooh!
	textWhatDoYouWant 	=  47	;So what do you want?
	textDeathDance 		=  59	;He came into our circle!
	textFight 			=  69	;Nyaah, nyaah!  You missed me!
	textAskDust 		=  78	;Fairy dust is very magical.
	textGetDust  		=  100	;No peeking!
	textAskMushrooms 	=  104	;Our mushrooms are special!
	textAskMagic 		=  113	;We are magic!
	textAskForest 		=  121	;This is our home.
	textAskDryad 		=  128	;She protects the forest and everything.
	textAskOther 		=  135	;This is boring!
	textRude 			=  141	;How rude!!
	textHiAgain 		=  151	;Oh, it's you again!

	faeriesStartedTalking			;faeries started talking
	egoHoldingOutHands			;ego holding hands out for dust
	faeriesTalking
	mushroomPalette
	ateMushroom
)

(define LEN_HI 				12)
(define LEN_DANCE 			8)
(define LEN_TEACHDANCE 		9)
(define LEN_BADDANCE 		5)
(define LEN_GOODDANCE 		9)
(define LEN_WHATDOYOUWANT 	11)
(define LEN_DEATHDANCE 		9)
(define LEN_FIGHT 			8)
(define LEN_ASKDUST 		22)
(define LEN_GETDUST 		3)
(define LEN_ASKMUSHROOMS 	8)
(define LEN_ASKMAGIC 		7)
(define LEN_ASKFOREST 		6)
(define LEN_ASKDRYAD 		6)
(define LEN_ASKOTHER 		5)
(define LEN_RUDE 			9)
(define LEN_HIAGAIN 		9)

(enum 1 ;faeryStates
	FAERY_1
	FAERY_2
	FAERY_3
	FAERY_4
	FAERY_5
	FAERY_6
	FAERY_7
	FAERY_8
	FAERY_9
	FAERY_10
	FAERY_11
	FAERY_12
	FAERY_13
	FAERY_14
	FAERY_15
	FAERY_16
	FAERY_17
)

(procedure (InitFaeryWindows &tmp i)
	(= i 0)
	(while (< i 5)
		(= [newAFaeryWindow i] (aFaeryWindow new:))
		(if (< colourCount 8)
			([newAFaeryWindow i] color: vBLACK back: vWHITE)
		else
			([newAFaeryWindow i]
				color: [faeryColor i]
				back: [faeryBack i]
			)
		)
		(++ i)
	)
)

(procedure (FairySays timer &tmp x y)
	(= currentFaeryWindow [newAFaeryWindow (Random 0 4)])
	(= x (Random 5 210))
	(= y
		(if (< (ego y?) 140)
			(Random (ego y?) 140)
		else
			(Random 5 (- (ego y?) 80))
		)
	)
	(cls)
	(Print
		&rest
		#at x y
		#width 100
		#mode teJustCenter
		#dispose
		#time timer
		#window currentFaeryWindow
	)
)

(procedure (SetFaeryAttention &tmp i)
	(Bset GOT_FAIRIES_ATTENTION)
	(= i 0)
	(while (< i 5)
		(= [faeryChaseScripts i] (Clone aChaseScript))
		([newAFaery i]
			setStep: 6 4
			setScript: [faeryChaseScripts i] 0 i
		)
		(++ i)
	)
)

(procedure (ClearFaeryAttention &tmp i)
	(Bclr GOT_FAIRIES_ATTENTION)
	(= i 0)
	(while (< i 5)
		(= [newAFaeryScript i] (aFaeryScript new:))
		([newAFaery i]
			setStep: 3 2
			setScript: [newAFaeryScript i] 0 i
		)
		(++ i)
	)
)

(procedure (MoveFaeries &tmp i temp1)
	(if fairiesOnScreen
		(Bclr GOT_FAIRIES_ATTENTION)
		(= faeryCycles 80)
		(= i 0)
		(while (< i 5)
			(= [newAFaeryScript i] (aFaeryScript new:))
			(= temp1 (Random 0 4))
			([newAFaery temp1]
				posn: [faeryX2 temp1] [faeryY2 temp1]
				setScript: [newAFaeryScript i] 0 temp1
			)
			(++ i)
		)
	)
)

(procedure (InitFaeries &tmp i)
	(= i 0)
	(while (< i 5)
		(= [newAFaeryScript i] (aFaeryScript new:))
		(= [newAFaery i] (aFaery new:))
		([newAFaery i]
			setLoop: i
			cel: 0
			ignoreActors:
			ignoreHorizon:
			posn:
				[faeryX i]
				[faeryY i]
			init:
			setCycle: Forward
			setScript: [newAFaeryScript i] 0 i
		)
		(++ i)
	)
)

(procedure (InitMushrooms &tmp i)
	(= i 0)
	(while (< i 8)
		(= [mushViews i] (Clone aMush))
		([mushViews i]
			setLoop: 5
			setCel: i
			posn: [mushX i] [mushY i]
			init:
			stopUpd:
		)
		(++ i)
	)
)

(instance aFaery of Actor
	(properties
		z 25
		view vFaery
		illegalBits $0000
	)
)

(instance aMush of View
	(properties
		view vFaery
	)
)

(instance aFaeryWindow of SysWindow
	(properties)
)

(instance faeryMusic of Sound
	(properties
		number 40
		priority 1
		loop -1
	)
)

(instance egoBoogie of Sound
	(properties
		number 49
		priority 2
		loop -1
	)
)

(instance bopTilYouDrop of Sound
	(properties
		number 64
		priority 2
		loop -1
	)
)

(instance rm70 of Room
	(properties
		picture 70
		style $0000
		horizon 74
		north 62
		east 71
		south 77
		west 69
	)
	
	(method (init)
		(if (= fairiesOnScreen isNightTime)
			(LoadMany RES_VIEW vFaery (GetEgoViewNumber vEgoDance))
			(Load RES_TEXT 296)
			(LoadMany RES_SOUND 40 64)
		)
		(LoadMany RES_VIEW (GetEgoViewNumber vEgoThrowing) (GetEgoViewNumber vEgoGetFaeryDust))
		(if fairiesOnScreen
			(InitFaeries)
			(InitFaeryWindows)
			(keyDownHandler add: self)
			(mouseDownHandler addToFront: self)
			(directionHandler add: self)
			(cSound stop:)
			(faeryMusic init: play:)
			(egoBoogie init:)
			(bopTilYouDrop init:)
		else
			(= startedRoom TRUE)
			(= defaultPalette 0)
		)
		(InitMushrooms)
		(super init:)
		(StatusLine enable:)
		(= faeryText -1)
		(StopEgo)
		(switch prevRoomNum
			(62
				(ego posn: 130 75 init: setMotion: MoveTo 130 85)
			)
			(71
				(ego posn: 318 140 init: setMotion: MoveTo 240 140)
			)
			(69
				(ego posn: 1 140 init: setMotion: MoveTo 35 140)
			)
			(else 
				(ego posn: 120 188 init: setMotion: MoveTo 120 170)
			)
		)
		(self setLocales: FOREST)
	)
	
	(method (doit)
		(cond 
			((== faeryCycles 70)
				(-- faeryCycles)
				(= faeriesStartedTalking TRUE)
				(= faeryText textFight)
				(= faeryTextLast (+ faeryText LEN_FIGHT))
				(= faeryState FAERY_8)
				(rm70 setScript: faeryTalk)
			)
			((> faeryCycles 1)
				(-- faeryCycles)
			)
			((== faeryCycles 1)
				(= faeryCycles 0)
			)
		)
		(cond 
			((> faeryCountdown 1)
				(-- faeryCountdown)
			)
			((== faeryCountdown 1)
				(= faeryCountdown 0)
				(= faeriesStartedTalking TRUE)
				(= faeryText textDance)
				(= faeryTextLast (+ faeryText LEN_DANCE))
				(= faeryState FAERY_2)
				(self setScript: faeryTalk)
			)
		)
		(if
			(and
				(== (ego onControl: origin) cYELLOW) ;ego has walked into the ring
				(== (User canControl:) TRUE)
				(not enteredRing)
				fairiesOnScreen
			)
			(= enteredRing TRUE)	;stepped into ring...
			(SetFaeryAttention)
			(= faeryCountdown 0)
			(= faeriesStartedTalking TRUE)
			(= faeryText textDeathDance)
			(= faeryTextLast (+ faeryText LEN_DEATHDANCE))
			(= faeryState FAERY_7)
			(self setScript: faeryTalk)
		)
		(cond 
			((and (> faeryText local89) (not startedRoom))
				(= startedRoom TRUE)
			)
			((and (== faeryState FAERY_8) (== (rm70 script?) NULL))
				(= faeriesStartedTalking TRUE)
				(= faeryText textTeachDance)
				(= faeryTextLast (+ faeryText LEN_TEACHDANCE))
				(= faeryState FAERY_3)
				(self setScript: faeryTalk)
			)
		)
		(super doit:)
	)
	
	(method (dispose &tmp temp0)
		(if fairiesOnScreen
			(cls)
			(= temp0 0)
			(while (< temp0 5)
				([newAFaeryWindow temp0] dispose:)
				(++ temp0)
			)
		)
		(if ateMushroom 
			(HighPrint 70 0)
			;Your senses gradually return to normal.
		)
		(super dispose:)
	)
	
	(method (handleEvent event &tmp spell)
		(if
		(and (== (event type?) direction) (not startedRoom))
			(event claimed: TRUE)
			(= startedRoom TRUE)
			(if (Btst DANCED_FOR_FAIRIES)
				(= faeryText textHiAgain)
				(= faeryTextLast (+ faeryText LEN_HIAGAIN))
				(= faeryState FAERY_17)
			else
				(= faeryText textHi)
				(= faeryTextLast (+ faeryText LEN_HI))
				(= faeryState FAERY_1)
			)
			(SetFaeryAttention)
			(self setScript: faeryTalk)
		)
		(if
		(and (== (event type?) mouseDown) (not startedRoom))
			(event claimed: TRUE)
			(= startedRoom TRUE)
			(if (Btst DANCED_FOR_FAIRIES)
				(= faeryText textHiAgain)
				(= faeryTextLast (+ faeryText LEN_HIAGAIN))
				(= faeryState FAERY_17)
			else
				(= faeryText textHi)
				(= faeryTextLast (+ faeryText LEN_HI))
				(= faeryState FAERY_1)
			)
			(SetFaeryAttention)
			(self setScript: faeryTalk)
		)
		(if
			(and
				(== (event type?) keyDown)
				(== (event message?) KEY_RETURN)
				script
			)
			(script cue:)
			(event claimed: TRUE)
		)
		(if (== (event type?) evSAID)
			(if (> faeryCountdown 1)
				(cond 
					((Said 'affirmative,please,dance')
						(= faeryCountdown 0)
						(= faeriesStartedTalking TRUE)
						(= faeryText textDance)
						(= faeryTextLast (+ faeryText LEN_DANCE))
						(= faeryState FAERY_2)
						(self setScript: faeryTalk)
					)
					((Said 'n')
						(= faeryCountdown 0)
						(= faeriesStartedTalking TRUE)
						(= faeryText textTeachDance)
						(= faeryTextLast (+ faeryText LEN_TEACHDANCE))
						(= faeryState FAERY_3)
						(self setScript: faeryTalk)
					)
					(else
						(HighPrint 70 1)
						;Don't you want to dance?
						(event claimed: TRUE))
				)
			else
				(if (not startedRoom)
					(event claimed: TRUE)
					(= startedRoom TRUE)
					(if (Btst DANCED_FOR_FAIRIES)
						(= faeryText textHiAgain)
						(= faeryTextLast (+ faeryText LEN_HIAGAIN))
						(= faeryState FAERY_17)
					else
						(= faeryText textHi)
						(= faeryTextLast (+ faeryText LEN_HI))
						(= faeryState FAERY_1)
					)
					(SetFaeryAttention)
					(self setScript: faeryTalk)
				)
				(cond 
					((super handleEvent: event))
					((Said 'dance')
						(if fairiesOnScreen
							(if (and (not script) (== (ego script?) 0))
								(ego setScript: cuteDance)
							)
						else
							(HighPrint 70 2)
							;What for?  There's no one to dance with.
						)
					)
					(
					(or (Said 'fight,kill,beat') (Said 'use/blade,dagger'))
						(if fairiesOnScreen
							(SetFaeryAttention)
							(= faeriesStartedTalking TRUE)
							(= faeryText textRude)
							(= faeryTextLast (+ faeryText LEN_RUDE))
							(= faeryState FAERY_16)
							(self setScript: faeryTalk)
						else
							(HighPrint 70 3)
							;What for?
						)
					)
					((Said 'chat')
						(if (and fairiesOnScreen (== faeryCountdown 0))
							(if (Btst GOT_FAIRIES_ATTENTION)
								(= faeriesStartedTalking TRUE)
								(= faeryText textAskOther)
								(= faeryTextLast (+ faeryText LEN_ASKOTHER))
								(= faeryState FAERY_15)
								(self setScript: faeryTalk)
							else
								(HighPrint 70 4)
								;They seem to be ignoring you.
							)
						else
							(HighPrint 70 5)
							;Who are you talking to?
						)
					)
					((Said 'ask>')
						(if (and fairiesOnScreen (== faeryCountdown 0))
							(= faeriesTalking TRUE)
							(cond 
								((Said '//mushroom,toadstool,ring')
									(SetFaeryAttention)
									(= faeriesStartedTalking TRUE)
									(= faeryText textAskMushrooms)
									(= faeryTextLast (+ faeryText LEN_ASKMUSHROOMS))
									(= faeryState FAERY_11)
									(self setScript: faeryTalk)
								)
								((Said '//dust[<faerie,about]')
									(if (or askedForDust (Btst POINTS_GETFAIRYDUST))
										(HighPrint 70 6)
										;You know all about it, now.
									else
										(SetFaeryAttention)
										(= faeriesStartedTalking TRUE)
										(= faeryText textAskDust)
										(= faeryTextLast (+ faeryText LEN_ASKDUST))
										(= faeryState FAERY_9)
										(self setScript: faeryTalk)
									)
								)
								((Said '//faerie,magic')
									(SetFaeryAttention)
									(= faeriesStartedTalking TRUE)
									(= faeryText textAskMagic)
									(= faeryTextLast (+ faeryText LEN_ASKMAGIC))
									(= faeryState FAERY_12)
									(self setScript: faeryTalk)
								)
								((Said '//forest')
									(SetFaeryAttention)
									(= faeriesStartedTalking TRUE)
									(= faeryText textAskForest)
									(= faeryTextLast (+ faeryText LEN_ASKFOREST))
									(= faeryState FAERY_13)
									(self setScript: faeryTalk)
								)
								((Said '//dryad')
									(SetFaeryAttention)
									(= faeriesStartedTalking TRUE)
									(= faeryText textAskDryad)
									(= faeryTextLast (+ faeryText LEN_ASKDRYAD))
									(= faeryState FAERY_14)
									(self setScript: faeryTalk)
								)
								(else
									(event claimed: TRUE)
									(= faeriesTalking FALSE)
									(if (Btst GOT_FAIRIES_ATTENTION)
										(= faeriesStartedTalking TRUE)
										(= faeryText textAskOther)
										(= faeryTextLast (+ faeryText LEN_ASKOTHER))
										(= faeryState FAERY_15)
										(self setScript: faeryTalk)
									else
										(HighPrint 70 4)
										;They seem to be ignoring you.
									)
								)
							)
							(if faeriesTalking (SolvePuzzle POINTS_TALKTOFAIRIES 1))
						else
							(event claimed: FALSE)
						)
					)
					((Said 'cast>')
						(switch (= spell (SaidCast event))
							(DETMAGIC
								(if (CastSpell spell)
									(if fairiesOnScreen
										(HighPrint 70 7)
										;There is much magic surrounding the fairies and their ring of mushrooms.
									else
										(HighPrint 70 8)
										;You detect faint emanations of magic near the ring of mushrooms.
									)
								)
							)
							(DAZZLE
								(if (CastSpell spell)
									(AnimateDazzle)
									(MoveFaeries)
								)
							)
							(CALM
								(if (CastSpell spell)
									(AnimateCalm)
								)
							)
							(FLAMEDART
								(if (CastSpell spell)
									(AnimateThrowingFlameDart NULL)
									(MoveFaeries)
								)
							)
							(else  
								(event claimed: FALSE)
							)
						)
					)
					((Said 'throw/dagger,dagger')
						(if (ego has: iDagger)
							(AnimateThrowingDagger NULL)
							(MoveFaeries)
						else
							(HighPrint 70 9)
							;You don't have a dagger.
						)
					)
					((Said 'lockpick,get,gather/mushroom')
						(if (and (== faeryCountdown 0) (== (ego script?) NULL))
							(= eatMushroom FALSE)
							(ego setScript: pickEm)
						else
							(HighPrint 70 1)
							;Don't you want to dance?
						)
					)
					((Said 'eat/mushroom')
						(cond 
							((Btst ATE_FAIRY_MUSHROOMS)
								(event claimed: FALSE)
							)
							((and (== faeryCountdown 0) (== (ego script?) NULL))
								(Bset ATE_FAIRY_MUSHROOMS)
								(ego setScript: eatShroom)
							)
							(else (HighPrint 70 1)
								;Don't you want to dance?
							)
						)
					)
					((Said 'get>')
						(if (== faeryCountdown 0)
							(cond 
								((Said '/faerie')
									(if fairiesOnScreen
										(HighPrint 70 10)
										;The fairies can avoid your grasp easily.
									else
										(HighPrint 70 11)
										;Huh?
									)
								)
								((Said '/dust[<faerie]')
									(cond 
										((Btst POINTS_GETFAIRYDUST)
											(HighPrint 70 12)
											;Don't be greedy.  We already gave you some.
											)
										(askedForDust
											(HighPrint 70 13)
											;Perhaps you should be better prepared to get some fairy dust next time.
											)
										((not fairiesOnScreen)
											(HighPrint 70 14)
											;Where could you possibly get that?
											)
										((not startedRoom)
											(HighPrint 70 15)
											;Maybe you should ask the Fairies for some.  That would be the polite thing to do.
											)
										(else
											(= faeriesStartedTalking TRUE)
											(= faeryText textAskDust)
											(= faeryTextLast (+ faeryText LEN_ASKDUST))
											(= faeryState FAERY_9)
											(self setScript: faeryTalk)
										)
									)
								)
							)
						else
							(HighPrint 70 1)
							;Don't you want to dance?
							(event claimed: TRUE)
						)
					)
					((Said 'look>')
						(cond 
							(
							(Said '[<at,around][/!*,forest,greenery,clearing]')
							(HighPrint 70 16)
							;The trees look more vibrant than most of the forest.
							(HighPrint 70 17)
							;There is a ring of mushrooms on the northwest side of the clearing.
							)
							((Said '/ring,mushroom,toadstool')
								(if fairiesOnScreen
									(HighPrint 70 18)
									;The fairy ring is luminous at night.
								else
									(HighPrint 70 19)
									;The ring of mushrooms contains mushrooms slightly larger than the ones you are used to.
								)
							)
							((Said '/faerie,creature,chandelier')
								(if fairiesOnScreen
									(HighPrint 70 20)
									;The fairies look like little dancing lights, but you get a strong sense that they are female with butterfly wings.
								else
									(HighPrint 70 21)
									;There aren't any of those around.
								)
							)
							((Said '/south')
								(HighPrint 70 22)
								;The trees seem more dense and lush than the rest of the woods.
								)
							((Said '/tree')
								(HighPrint 70 16)
								;The trees look more vibrant than most of the forest.
								)
							((Said '/east,north')
								(HighPrint 70 23)
								;You see trees and brush.
								)
							((Said '/west')
								(HighPrint 70 24)
								;The trees here look thicker and healthier than in other parts of the forest.
								)
						)
					)
				)
			)
		)
	)
)

(instance aFaeryScript of Script
	(properties)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(if (> faeryCycles 0)
					(client
						setMotion:
							MoveTo
							(Random
								(- [faeryX2 register] 25)
								(+ [faeryX2 register] 25)
							)
							(Random
								(- [faeryY2 register] 25)
								(+ [faeryY2 register] 25)
							)
							self
					)
				else
					(client
						setMotion:
							MoveTo
							(Random
								(- [faeryX register] 15)
								(+ [faeryX register] 15)
							)
							(Random
								(- [faeryY register] 15)
								(+ [faeryY register] 15)
							)
							self
					)
				)
			)
			(1 (self changeState: 0))
		)
	)
)

(instance aChaseScript of Script
	(properties)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(cond 
					(egoHoldingOutHands
						(client
							setMotion:
								MoveTo
								(- (Random (ego x?) (+ (ego x?) 8)) 18)
								(- (Random (ego y?) (+ (ego y?) 8)) 16)
								self
						)
					)
					((Btst DANCING_FOR_FAIRIES)
						(client
							setMotion:
								MoveTo
								(- (Random (ego x?) (+ (ego x?) 20)) 10)
								(- (Random (ego y?) (+ (ego y?) 30)) 15)
								self
						)
					)
					(else
						(client
							setMotion:
								MoveTo
								(- (Random (ego x?) (+ (ego x?) 80)) 40)
								(- (Random (ego y?) (+ (ego y?) 30)) 15)
								self
						)
					)
				)
			)
			(1 (self changeState: 0))
		)
	)
)

(instance faeryTalk of Script
	(properties)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(if (not isEgoLocked) (HandsOff))
				(= seconds (if faeriesStartedTalking 4 else 3))
				(FairySays seconds 296 faeryText)
			)
			(1
				(= faeriesStartedTalking FALSE)
				(= seconds 0)
				(++ faeryText)
				(cond 
					(
						(or
							(and enteredRing (== faeryText (+ textDeathDance (- LEN_DEATHDANCE 2))))
							(and (== faeryState FAERY_3) (== faeryText (+ textTeachDance (- LEN_TEACHDANCE 2))))
							(and (== faeryState FAERY_16) (== faeryText (+ textRude (- LEN_RUDE 2))))
						)
						(ego setScript: deathDance)
						(self changeState: 0)
					)
					((< faeryText faeryTextLast) (self changeState: 0))
					(else
						(switch faeryState
							(FAERY_1
								(Bset DANCED_FOR_FAIRIES)
								(= faeryCountdown 100)
								(HandsOn)
							)
							(FAERY_17
								(= faeryCountdown 100)
								(HandsOn)
							)
							(FAERY_2
								(ego setScript: cuteDance)
							)
							(FAERY_5
								(= local84 TRUE)
							)
							(FAERY_4
								(= local84 TRUE)
							)
							(FAERY_8
								(= faeryCycles 0)
								(SetFaeryAttention)
							)
							(FAERY_9 
								(ego setScript: getDust)
							)
							(FAERY_10
								(getDust cue:)
							)
							(FAERY_15
								(ClearFaeryAttention)
							)
						)
						(if (== (ego script?) NULL) (cls) (HandsOn))
						(self dispose:)
					)
				)
			)
		)
	)
)

(instance cuteDance of Script
	(properties)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(HandsOff)
				(SolvePuzzle POINTS_DANCEWITHFAIRIES 3)
				(ClearFaeryAttention)
				(ego
					illegalBits: 0
					ignoreActors:
					setMotion: MoveTo egoStandX egoStandY self
				)
			)
			(1
				(if (TrySkill AGIL tryFaeryDance)
					(self cue:)
				else
					(ego setScript: klutzDance)
					(egoBoogie play:)
				)
			)
			(2
				(ego
					view: (GetEgoViewNumber vEgoDance)
					setLoop: 6
					cel: 0
					cycleSpeed: 1
					moveSpeed: 3
					setCycle: Forward
				)
				(self cue:)
			)
			(3
				(++ danceCycles)
				(ego
					setMotion:
						MoveTo
						(Random (- egoStandX 20) (+ egoStandX 20))
						(Random (- egoStandY 10) (+ egoStandY 10))
						self
				)
			)
			(4
				(cond 
					((== danceCycles 2) 
						(Bset DANCING_FOR_FAIRIES) 
						(SetFaeryAttention) 
						(self changeState: 3)
					)
					((== danceCycles 3)
						(= faeriesStartedTalking TRUE)
						(= faeryText textGoodDance)
						(= faeryTextLast (+ faeryText LEN_GOODDANCE))
						(= faeryState FAERY_5)
						(rm70 setScript: faeryTalk)
						(self changeState: 3)
					)
					(local84
						(= local84 FALSE)
						(self cue:)
					)
					((>= danceCycles 40)
						(self changeState: 13)
					)
					(else
						(self changeState: 3)
					)
				)
			)
			(5
				(ClearFaeryAttention)
				(ego setLoop: 5 cel: 0 setCycle: 0)
				(= seconds 2)
			)
			(6
				(ego setCycle: EndLoop)
				(= cycles 10)
			)
			(7
				(ego setCycle: BegLoop)
				(= cycles 10)
			)
			(8
				(ego x: (+ (ego x?) 12) setLoop: 4 cel: 0 setCycle: EndLoop)
				(= cycles 10)
			)
			(9
				(FairySays 2 {Wow!})
				(= seconds 2)
			)
			(10 (ego setCycle: BegLoop self))
			(11
				(StopEgo)
				(ego x: (- (ego x?) 12) loop: 2)
				(= cycles 5)
			)
			(12
				(Bclr DANCING_FOR_FAIRIES)
				(SetFaeryAttention)
				(= faeriesStartedTalking TRUE)
				(= faeryText textWhatDoYouWant)
				(= faeryTextLast (+ faeryText LEN_WHATDOYOUWANT))
				(= faeryState FAERY_6)
				(rm70 setScript: faeryTalk)
				(HandsOn)
				(self dispose:)
			)
			(13
				(HighPrint 70 25)
				;I'll bet you're sorry you said that.
				(= danceCycles 0)
				(HandsOn)
				(StopEgo)
				(ego x: (- (ego x?) 12) loop: 2)
				(self dispose:)
			)
		)
	)
)

(instance klutzDance of Script
	(properties)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(ego
					view: (GetEgoViewNumber vEgoDance)
					setLoop: (Random 0 4)
					cel: 0
					cycleSpeed: (if enteredRing 0 else 1)
					setCycle: EndLoop self
				)
				(++ danceCycles)
			)
			(1
				(cond 
					((== danceCycles 5)
						(= faeriesStartedTalking TRUE)
						(= faeryText textBadDance)
						(= faeryTextLast (+ faeryText LEN_BADDANCE))
						(= faeryState FAERY_4)
						(rm70 setScript: faeryTalk)
						(self changeState: 0)
					)
					(local84
						(= local84 FALSE)
						(egoBoogie stop:)
						(self cue:)
					)
					((>= danceCycles 40)
						(self changeState: 4)
					)
					(else 
						(self changeState: 0)
					)
				)
			)
			(2
				(HandsOn)
				(StopEgo)
				(ego x: (- (ego x?) 12) loop: 2)
				(= cycles 10)
			)
			(3
				(SetFaeryAttention)
				(= faeriesStartedTalking TRUE)
				(= faeryText textWhatDoYouWant)
				(= faeryTextLast (+ faeryText LEN_WHATDOYOUWANT))
				(= faeryState FAERY_6)
				(rm70 setScript: faeryTalk)
				(self dispose:)
			)
			(4
				(HighPrint 70 25)
				(= danceCycles 0)
				(HandsOn)
				(StopEgo)
				(ego x: (- (ego x?) 12) loop: 2)
				(self dispose:)
			)
		)
	)
)

(instance deathDance of Script
	(properties)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(HandsOff)
				(= danceCycles 0)
				(ego
					illegalBits: 0
					ignoreActors:
					setMotion: MoveTo 94 102 self
				)
			)
			(1
				(faeryMusic stop:)
				(bopTilYouDrop play:)
				(ego view: (GetEgoViewNumber vEgoDance) x: (+ (ego x?) 12))
				(self cue:)
			)
			(2
				(ego setLoop: (Random 0 4) cel: 0 setCycle: EndLoop self)
				(++ danceCycles)
			)
			(3
				(cond 
					((== (mod danceCycles 3) 0)
						(FairySays 2 {Dance!})
						(self changeState: 2)
					)
					((== danceCycles 19)
						(self cue:)
					)
					(else
						(self changeState: 2)
					)
				)
			)
			(4
				(bopTilYouDrop stop:)
				(ego setLoop: 7 setCel: 1)
				(= cycles 10)
			)
			(5
				(ego cycleSpeed: 1 setCycle: EndLoop)
				(= cycles 35)
			)
			(6
				(FairySays 4 {He's no fun!__He fell right over!})
				(= seconds 4)
			)
			(7
				(EgoDead DIE_RETRY DIE_FAERY_DANCE 70 26
					#title {Land of 1000 Dances}
					#icon (GetEgoViewNumber vEgoDance) 2 5
					;For such wimpy-looking creatures, those fairies sure can play rough!
					;You never danced so hard in your entire life, which is now over.
				)
				(StopEgo)
				(ClearFaeryAttention)
				(switch prevRoomNum
					(62
						(ego posn: 130 85)
					)
					(71
						(ego posn: 240 140)
					)
					(69
						(ego posn: 35 140)
					)
					(else 
						(ego posn: 120 170)
					)
				)
				(= faeryState 0)
				(= faeryText -1)
				(= enteredRing FALSE)
				(= startedRoom FALSE)
				(rm70 setScript: NULL)
			)
		)
	)
)

(instance pickEm of Script
	(properties)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(HandsOff)
				(if (> (ego x?) 92)
					(ego illegalBits: 0 setMotion: MoveTo 175 109 self)
				else
					(ego illegalBits: 0 setMotion: MoveTo 19 109 self)
				)
			)
			(1
				(ego
					view: (GetEgoViewNumber vEgoThrowing)
					setLoop: (if (< (ego x?) 100) 0 else 1)
					cel: 0
					setCycle: EndLoop self
				)
			)
			(2
				(if eatMushroom
					(TimedPrint 4 70 27)
					;You eat a few of the lovely mushrooms.
				else
					(HighPrint 70 28)
					;You pick a handful of the smaller mushrooms and carefully put them away in your backpack.
					(ego get: iMushroom 3)
					(Bset HAVE_FAIRY_MUSHROOMS)
					(SolvePuzzle POINTS_PICKMUSHROOMS 3)
				)
				(ego setCycle: BegLoop self)
			)
			(3
				(StopEgo)
				(ego loop: 2)
				(HandsOn)
				(self dispose:)
			)
		)
	)
)

(instance getDust of Script
	(properties)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(HandsOff)
				(= askedForDust 1)
				(ego view: (GetEgoViewNumber vEgoGetFaeryDust) loop: 0 cel: 0 setCycle: CycleTo 3 cdFORWARD self)
			)
			(1
				(TimedPrint 5 70 29)
				;You hold out your hand...what else could you do?
				(= egoHoldingOutHands TRUE)
				(= seconds 6)
			)
			(2
				(= faeryState FAERY_10)
				(= faeriesStartedTalking TRUE)
				(= faeryText textGetDust)
				(= faeryTextLast (+ faeryText LEN_GETDUST))
				(rm70 setScript: faeryTalk)
			)
			(3
				(= cycles 10)
				(= egoHoldingOutHands FALSE)
			)
			(4
				(cond 
					((ego has: iFlask)
						(TimedPrint 8 70 30)
						;You place the dust carefully away in an empty flask.
						(ego use: iFlask 1)
						(ego get: iFairyDust)
						(SolvePuzzle POINTS_GETFAIRYDUST 8)
					)
					(
						(or
							(ego has: iHealingPotion)
							(ego has: iManaPotion)
							(ego has: iStaminaPotion)
							(ego has: iGhostOil)
						)
						(TimedPrint 8 70 31)
						;You realize that you need something such as an empty flask to put this fairy dust into.
					)
					(else
						(TimedPrint 8 70 32)
						;As the fairy dust sifts through your fingers, you realize that you needed something to put it into.
						)
				)
				(= seconds 8)
			)
			(5
				(ego setCycle: EndLoop)
				(= cycles 15)
			)
			(6
				(StopEgo)
				(HandsOn)
				(self dispose:)
			)
		)
	)
)

(instance eatShroom of Script
	(properties)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(= eatMushroom TRUE)
				(self setScript: pickEm self)
			)
			(1
				(HandsOff)
				(= mushroomPalette defaultPalette)
				(= defaultPalette 2)
				(DrawPic (curRoom curPic?) 100 dpCLEAR defaultPalette)
				(Animate (cast elements?) 0)
				(= seconds 3)
			)
			(2
				(HighPrint 70 33)
				;Wow!  That was pretty wild!  It's probably not a good idea to eat too many more of these mushrooms, though.
				(= ateMushroom TRUE)
				(= defaultPalette mushroomPalette)
				(HandsOn)
				(self dispose:)
			)
		)
	)
)

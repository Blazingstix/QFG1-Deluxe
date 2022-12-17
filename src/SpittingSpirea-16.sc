;;; Sierra Script 1.0 - (do not remove this comment)
(script# 16)
(include system.sh) (include sci2.sh) (include game.sh)
(use Main)
(use _LoadMany)
(use _Chase)
(use _Sound)
(use _Jump)
(use _Motion)
(use _Game)
(use _Inventory)
(use _Actor)
(use _System)
(use _Interface)

(public
	rm16 0
	flyingSeed 1
	rock 2
)

(local
	[flowerProps 4]
	seedReadyToMove
	local5
	local6
	local7
	local8
	local9
	local10
	local11
	local12
	lassoActive	;capturing seed with lasso
	local14
	local15
)
(procedure (AlreadyKilledFlower)
	(HighPrint 16 0)
	;You've already made a mess of that flower.
)

(procedure (AlreadyGotSeed)
	(HighPrint 16 1)
	;There's not much point in that, now that you've already done what you came here to do.
)

(procedure (SpireaPickUpSeed)
	(cond 
		((Btst SPIREA_INACTIVE)
			(HighPrint 16 2)
			;You don't need a rock for anything.
		)
		((== spireaStatus 0)
			(ego setScript: (ScriptID 292 0))
		)
		(else
			(HighPrint 16 3)
			;Not now!
		)
	)
)

(procedure (OpenAllFlowers)
	(if (!= ([flowerProps 0] loop?) 3)
		([flowerProps 0] setLoop: 2 setCycle: EndLoop)
	)
	(if (!= ([flowerProps 1] loop?) 3)
		([flowerProps 1] setLoop: 2 setCycle: EndLoop)
	)
	(if (!= ([flowerProps 2] loop?) 3)
		([flowerProps 2] setLoop: 2 setCycle: EndLoop)
	)
	(if (!= ([flowerProps 3] loop?) 3)
		([flowerProps 3] setLoop: 2 setCycle: EndLoop)
	)
)

(procedure (StopUpdatingFlowers)
	([flowerProps 0] stopUpd:)
	([flowerProps 1] stopUpd:)
	([flowerProps 2] stopUpd:)
	([flowerProps 3] stopUpd:)
)

(instance flyingSeed of Actor
	(properties
		view vSpittingSpirea
		illegalBits $0000
	)
)

(instance magicLasso of Actor
	(properties
		view vEgoMagicFetch ;CI:TODO: Create new separate view for the Lasso effect
		illegalBits $0000
	)
)

(instance rock of Actor
	(properties
		view vEgoThrowing ;CI:TODO: Create new separate view for the rock view
		illegalBits $0000
	)
)

(instance spitSound of Sound
	(properties
		number 18
		priority 3
	)
)

(instance gulpSound of Sound
	(properties
		number 27
		priority 3
	)
)

(instance rm16 of Room
	(properties
		picture 16
		style WIPELEFT
		east 17
		south 24
	)
	
	(method (init)
		;(Printf {FreeHeap: %d\nLargestPrt: %d\nDiff: %d} (MemoryInfo FreeHeap) (MemoryInfo LargestPtr) (- (MemoryInfo FreeHeap) (MemoryInfo LargestPtr)))
		(LoadMany RES_VIEW vSpittingSpirea (GetEgoViewNumber vEgoCatchSeed) (GetEgoViewNumber vEgoSwordSpirea) (GetEgoViewNumber vEgoClimbing) (GetEgoViewNumber vEgoThrowing))
		;(Printf {FreeHeap: %d\nLargestPrt: %d\nDiff: %d} (MemoryInfo FreeHeap) (MemoryInfo LargestPtr) (- (MemoryInfo FreeHeap) (MemoryInfo LargestPtr)))
		(if (ego knows: FETCH) (Load RES_VIEW (GetEgoViewNumber vEgoMagicFetch)))
		(LoadMany RES_SCRIPT 291 292)
		(LoadMany RES_SOUND (GetSongNumber 18) (GetSongNumber 27))
		(super init:)
		(directionHandler add: self)
		(spitSound number: (GetSongNumber 18) init:)
		(gulpSound number: (GetSongNumber 27) init:)
		(StatusLine enable:)
		(if (or isNightTime (Btst OBTAINED_SPIREA_SEED)) (Bset SPIREA_INACTIVE))
		(= local7 2)
		(StopEgo)
		(ego init:)
		(rock
			setLoop: 4
			setStep: 70 30
			posn: 0 1000
			hide:
			ignoreActors:
			setCycle: Forward
			init:
		)
		(switch prevRoomNum
			(17
				(ego posn: 318 165 setMotion: MoveTo 275 165)
			)
			(else 
				(ego posn: 170 188 setMotion: MoveTo 170 175)
			)
		)
		(if (Btst SMASHED_FLOWER3) (flower0 setLoop: 3 cel: 4))
		(if (Btst SMASHED_FLOWER1) (flower1 setLoop: 3 cel: 4))
		(if (Btst SMASHED_FLOWER2) (flower3 setLoop: 3 cel: 4))
		((= [flowerProps 0] flower0) init: stopUpd:)
		((= [flowerProps 1] flower1) init: stopUpd:)
		((= [flowerProps 2] flower2) init: stopUpd:)
		((= [flowerProps 3] flower3) init: stopUpd:)
		(addToPics
			add: leaf0 leaf1 leaf2 leaf3
			eachElementDo: #init
			doit:
		)
		(if
			(and
				(not (Btst SMASHED_FLOWER1))
				(not (Btst SMASHED_FLOWER2))
				(not (Btst SMASHED_FLOWER3))
			)
			(flyingSeed
				setLoop: 4
				setPri: 5
				ignoreActors:
				posn: 0 1000
				hide:
				setCycle: Forward
				init:
				setScript: spitIt
			)
		)
	)
	
	(method (doit)
		(if seedReadyToMove (flyingSeed setScript: spitIt))
		(super doit:)
	)
	
	(method (dispose)
		(Bset VISITED_SPITTING_SPIREA)
		(Bclr SPIREA_INACTIVE)
		(super dispose:)
	)
	
	(method (handleEvent event &tmp spell)
		(switch (event type?)
			(direction
				(if (== spireaStatus 1)
					(switch (event message?)
						(dirW
							(ego setScript: (ScriptID 291 2))
						)
						(dirS
							(ego setScript: (ScriptID 291 2))
						)
						(dirE
							(= spireaStatus 2)
							(ego setScript: (ScriptID 291 3))
						)
						(dirN
							(= spireaStatus 2)
							(ego setScript: (ScriptID 291 3))
						)
					)
				else
					(event claimed: FALSE)
				)
			)
			(evSAID
				(cond 
					((Said 'look<for/boulder,brick') (SpireaPickUpSeed))
					((Said 'look>')
						(cond 
							((Said '/north,west') (HighPrint 16 4))
							((Said '/south,east') (HighPrint 16 5))
							((Said '[<at,around][/!*,forest]')
								(HighPrint 16 6)
								(if (not (Btst SPIREA_INACTIVE)) (HighPrint 16 7))
							)
							((Said '/cliff,boulder') (HighPrint 16 8))
							((Said '[<down][/ground,needle,moss,grass]') (HighPrint 16 9))
							((Said '[<up][/sky,cloud,star]')
								(if isNightTime
									(HighPrint 16 10)
								else
									(HighPrint 16 11)
								)
							)
							((Said '/birch,tree') (HighPrint 16 12))
							((Said '/seed')
								(cond 
									((ego has: iSeed)
										;don't claim the event here, instead let it trickle down to looking at ego's inventory
										(event claimed: FALSE)
									)
									((Btst SPIREA_INACTIVE)
										(HighPrint 16 13)
										;You can't see it.
									)
									(else 
										(HighPrint 16 14)
										;The large seed spins as it travels through the air.
									)
								)
							)
							((Said '/leaf')
								(HighPrint 16 15)
							)
						)
					)
					(
						(or
							(Said 'chop,beat,fight,kill[/plant,flower,pod]')
							(Said 'use,draw/blade')
						)
						(switch spireaStatus
							(1
								(HighPrint 16 16)
								;As you attempt to draw your weapon, you lose your balance momentarily.
								(ego setScript: (ScriptID 291 2))
							)
							(3
								(HighPrint 16 3)
								;Not now!
							)
							(4 
								(HighPrint 16 3)
								;Not now!
							)
							(0
								(cond 
									(
										(not
											(if
												(or
													(& (ego onControl: origin) cLRED)
													(& (ego onControl: origin) cYELLOW)
												)
											else
												(& (ego onControl: origin) cLMAGENTA)
											)
										)
										(PrintNotCloseEnough)
									)
									((ego has: iSword)
										(ego setScript: smashIt)
										(Bset SPIREA_INACTIVE)
									)
									(else 
										(HighPrint 16 17)
										;If you had a sword, you might be able to do that.
									)
								)
							)
						)
					)
					((Said 'climb,get<down')
						(cond 
							((and (> spireaStatus 1) (== seedTarget 0))
								(HighPrint 16 18)
								;Wait a second!
							)
							((>= spireaStatus 1)
								(ego setScript: (ScriptID 292 3))
							)
							(else 
								(HighPrint 16 19)
								;You're already on the ground.
							)
						)
					)
					((Said 'climb[<up][/cliff,boulder]')
						(cond 
							((>= spireaStatus 1)
								(HighPrint 16 20)
								;You don't think you can climb any further up the cliff.
							)
							((Btst OBTAINED_SPIREA_SEED)
								(AlreadyGotSeed)
							)
							((TrySkill CLIMB tryClimbSpittingSpirea 0)
								(ego setScript: (ScriptID 291 1))
							)
							(else
								(ego setScript: (ScriptID 291 0))
							)
						)
					)
					((Said 'capture[/seed]')
						(cond 
							((Btst SPIREA_INACTIVE)
								(HighPrint 16 21)
								;There's not much point to that now.
							)
							((Btst OBTAINED_SPIREA_SEED)
								(AlreadyGotSeed)
							)
							((== spireaStatus 1)
								(= spireaStatus 2)
								(ego setScript: (ScriptID 291 3))
							)
							((or (== spireaStatus 2) (== spireaStatus 3))
								(HighPrint 16 22)
								;Why do you think you're standing like this.  Trying to catch Flying Water?
							)
							(else 
								(HighPrint 16 23)
								;You can't catch the seed from down on the ground.
							)
						)
					)
					((Said '(lockpick<up),find,search>')
						(cond 
							((Said '/boulder,brick') (SpireaPickUpSeed))
							((Said '/seed')
								(if (Btst OBTAINED_SPIREA_SEED)
									(PrintAlreadyDoneThat)
								else
									(HighPrint 16 24)
									;It's not that easy.
								)
							)
						)
					)
					((Said 'throw>')
						(cond 
							((Said '/boulder,brick')
								(cond 
									((Btst OBTAINED_SPIREA_SEED)
										(AlreadyGotSeed)
									)
									((Btst SPIREA_INACTIVE)
										(HighPrint 16 25)
										;You might have tried to throw a rock at the seed, but it's too late now.
									)
									((== spireaStatus 0)
										(if (ego has: iRock)
											(if (not local10)
												(HighPrint 16 26)
												;You get in position for a good throw.
											)
											(ego setScript: throwIt)
										else
											(HighPrint 16 27)
											;You need a good rock to throw.
										)
									)
									(else 
										(HighPrint 16 3)
										;Not now!
									)
								)
							)
							((Said '/dagger')
								(HighPrint 16 28)
								;Perhaps it would be better to find something else to throw.
							)
							((Said '/*')
								(HighPrint 16 29)
								;That's really not a good idea.
							)
						)
					)
					((Said 'cast>')
						(switch (= spell (SaidCast event))
							(OPEN
								(cond 
									((not (CastSpell spell)))
									((Btst OBTAINED_SPIREA_SEED)
										(AlreadyGotSeed)
									)
									((!= (ego script?) NULL)
										(HighPrint 16 3)
										;Not now!
									)
									((!= spireaStatus 0)
										(HighPrint 16 30)
										;You lose your concentration.
										(ego setScript: (ScriptID 291 2))
									)
									((< [egoStats OPEN] 35)
										(HighPrint 16 31)
										;Your skill with the Open spell is not great enough to affect the plants.
									)
									(else (Bset SPIREA_INACTIVE) (ego setScript: openUp))
								)
							)
							(FETCH
								(cond 
									((not (CastSpell spell)))
									((Btst OBTAINED_SPIREA_SEED) (AlreadyGotSeed))
									((Btst SPIREA_INACTIVE)
										(HighPrint 16 32)
										;You might have tried to cast a spell at the seed, but it's too late now.
									)
									((== spireaStatus 0)
										(if (not local11)
											(HighPrint 16 33)
											;You get into a good position.
										)
										(= local9 1)
										(ego setScript: throwIt)
									)
									(else
										(HighPrint 16 30)
										;You lose your concentration.
										(ego setScript: (ScriptID 291 2))
									)
								)
							)
							(FLAMEDART
								(if (not (Btst OBTAINED_SPIREA_SEED))
									(HighPrint 16 34)
									;That spell would damage the seed.  You don't want to do that.
								else
									(HighPrint 16 35)
									;There is no need to be so destructive.
								)
							)
							(else  (event claimed: FALSE))
						)
					)
					((Said 'get>')
						(cond 
							((Said '/seed')
								(cond 
									((Btst OBTAINED_SPIREA_SEED) (PrintAlreadyDoneThat))
									((Btst SPIREA_INACTIVE)
										(HighPrint 16 36)
										;Since the flowers have stopped spitting, the seed might be a little harder to get to.
									)
									(else 
										(HighPrint 16 37)
										;You'd like to do that, wouldn't you?
									)
								)
							)
							((Said '/plant,flower,pod,leaf')
								(HighPrint 16 38)
								;You have no need to take the flowers.
							)
							((Said '/boulder') (SpireaPickUpSeed))
						)
					)
				)
			)
		)
		(super handleEvent: event)
	)
)

(instance spitIt of Script
	(properties)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(= seedReadyToMove FALSE)
				(= seconds (Random 3 5))
			)
			(1
				;if they're not active, skip to the end.
				(if (Btst SPIREA_INACTIVE)
					(self changeState: 8)
				else
					(= seedInPlant seedTarget)
					(= seedTarget (Random 0 3))
					(if (== local14 seedTarget)
						(while (== local14 seedTarget)
							;
							(if (<= (++ local15) 3)
								(= local15 0)
								(break)
							)
							(if (== seedTarget 1)
								(= seedTarget 2)
							else
								(= seedTarget 1)
							)
						)
					else
						(= local15 0)
					)
					(= local14 seedTarget)
					([flowerProps seedInPlant]
						setLoop: 1
						startUpd:
						setCycle: EndLoop self
					)
					(if (and (!= seedTarget 3) (!= seedInPlant seedTarget) local5)
						(throwIt cue:)
					)
				)
			)
			(2
				(flyingSeed
					show:
					yStep: 10
					posn:
						(switch seedInPlant
							(0 142)
							(1 35)
							(2 66)
							(3 228)
						)
						(switch seedInPlant
							(0 60)
							(1 85)
							(2 19)
							(3 70)
						)
				)
				(if lassoActive
					(lassoSeed cue:)
				else 
					(self cue:)
				)
			)
			(3
				(spitSound play:)
				(flyingSeed
					setMotion:
						MoveTo
						(flyingSeed x?)
						(-
							(flyingSeed y?)
							(if
							(and (== seedInPlant seedTarget) (!= seedInPlant 2))
								30
							else
								5
							)
						)
						self
				)
				([flowerProps seedInPlant] setCycle: BegLoop)
			)
			(4
				(flyingSeed
					yStep: 2
					setMotion:
						JumpTo
						(switch seedTarget
							(0 142)
							(1 35)
							(2 66)
							(3 228)
						)
						(switch seedTarget
							(0 (if (== spireaStatus 3) 48 else 60))
							(1 85)
							(2 19)
							(3 70)
						)
						self
				)
			)
			(5
				(if (and (== spireaStatus 3) (== seedTarget 0))
					(ego setScript: (ScriptID 292 2))		;heCaughtIt
				else
					(self cue:)
				)
			)
			(6
				([flowerProps seedTarget] setLoop: 2 setCycle: EndLoop)
				(flyingSeed
					yStep: 6
					setMotion: MoveTo (flyingSeed x?) (+ (flyingSeed y?) 17) self
				)
			)
			(7
				(gulpSound play:)
				(flyingSeed hide:)
				([flowerProps seedTarget] setCycle: BegLoop self)
			)
			(8
				;close out the script.
				([flowerProps seedTarget] stopUpd:) ;stop the plant the seed left from
				([flowerProps seedInPlant] stopUpd:) ;stop the plant the seed landed in
				(flyingSeed setScript: NULL)
				(if (or (not (Btst SPIREA_INACTIVE)) (not local6))
					(= seedReadyToMove TRUE)
				)
			)
		)
	)
)

(instance smashIt of Script
	(properties)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(SolvePuzzle POINTS_SMASHSPIREA -10)
				(cond 
					((& (ego onControl: origin) cLRED)
						(if (Btst SMASHED_FLOWER1)
							(AlreadyKilledFlower)
						else
							(HandsOff)
							(= local7 1)
							(ego illegalBits: 0 setMotion: MoveTo 57 132 self)
						)
					)
					((& (ego onControl: origin) cYELLOW)
						(if (Btst SMASHED_FLOWER2)
							(AlreadyKilledFlower)
						else
							(HandsOff)
							(= local7 3)
							(ego illegalBits: 0 setMotion: MoveTo 226 140 self)
						)
					)
					((& (ego onControl: origin) cLMAGENTA)
						(if (Btst SMASHED_FLOWER3)
							(AlreadyKilledFlower)
						else
							(HandsOff)
							(= local7 0)
							(ego illegalBits: 0 setMotion: MoveTo 137 132 self)
						)
					)
				)
			)
			(1
				(ego
					view: (GetEgoViewNumber vEgoSwordSpirea)
					setLoop: (if (== local7 1) 2 else 0)
					cel: 0
				)
				([flowerProps 1] setPri: (if (== local7 1) 15 else RELEASE))
				(self cue:)
			)
			(2 (ego setCycle: EndLoop self))
			(3
				(++ local8)
				(ego
					setLoop: (if (== local7 1) 3 else 1)
					setCycle: EndLoop self
				)
			)
			(4
				(if (== local8 2)
					(switch local7
						(0
							([flowerProps 0]
								setLoop: 3
								cel: 0
								cycleSpeed: 1
								setCycle: EndLoop
							)
							(Bset SMASHED_FLOWER3)
						)
						(1
							([flowerProps 1]
								setLoop: 3
								cel: 0
								cycleSpeed: 1
								setCycle: EndLoop
							)
							(Bset SMASHED_FLOWER1)
						)
						(3
							([flowerProps 3]
								setLoop: 3
								cel: 0
								cycleSpeed: 1
								setCycle: EndLoop
							)
							(Bset SMASHED_FLOWER2)
						)
					)
					(if (== local7 seedTarget) (flyingSeed show:))
				)
				(if (> local8 3)
					(= local8 0)
					(= cycles 4)
				else
					(self changeState: 3)
				)
			)
			(5
				(ego setLoop: (if (== local7 1) 2 else 0) cel: 3)
				(= cycles 2)
			)
			(6 (ego setCycle: BegLoop self))
			(7
				(ego
					loop: (if (== local7 1) 1 else 3)
					posn: (if (!= local7 1) (+ (ego x?) 10) else (ego x?)) (ego y?)
				)
				(StopEgo)
				(if
					(and
						(== local7 seedTarget)
						(not local12)
						(not (Btst OBTAINED_SPIREA_SEED))
					)
					(= cycles 3)
				else
					(HandsOn)
					(ego setScript: 0)
				)
				(= local7 2)
			)
			(8
				(HighPrint 16 39)
				(if (== ([flowerProps 1] priority?) 15)
					([flowerProps 1] setPri: 1)
				)
				(= cycles 2)
			)
			(9
				(flyingSeed
					setMotion: MoveTo (flyingSeed x?) (+ (flyingSeed y?) 15) self
				)
			)
			(10
				(flyingSeed dispose:)
				(= cycles 2)
			)
			(11
				(HighPrint 16 40)
				(SolvePuzzle POINTS_GETSEED 8)
				(ego get: iSeed setScript: 0)
				(Bset OBTAINED_SPIREA_SEED)
				(HandsOn)
			)
		)
	)
)

(instance throwIt of Script
	(properties)
	
	(method (dispose)
		(= local9 0)
		(super dispose:)
	)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(HandsOff)
				(ego
					illegalBits: 0
					setMotion: MoveTo (Random 242 258) (Random 160 175) self
				)
			)
			(1
				(ego
					view: (if local9 (GetEgoViewNumber vEgoMagicFetch) else (GetEgoViewNumber vEgoThrowing))
					cycleSpeed: 1
					setLoop: (if local9 0 else 2)
					cel: 0
				)
				(= local5 1)
			)
			(2
				(= local5 0)
				(if local9
					(ego setCycle: EndLoop self)
				else
					(ego setCycle: CycleTo 4 cdFORWARD self)
				)
			)
			(3
				(if local9
					(magicLasso
						ignoreActors:
						posn: (+ (ego x?) 2) (- (ego y?) 36)
						setLoop: 4
						setStep: 20 10
						setCycle: Forward
						init:
					)
					(if (TrySkill FETCH 0 20)
						(magicLasso setScript: lassoSeed)
					else
						(magicLasso setScript: lassoFailed)
					)
					(ego setScript: 0)
					(= local11 1)
				else
					(rock posn: (- (ego x?) 13) (- (ego y?) 34) show:)
					(ego setCycle: EndLoop self)
					(= local10 1)
					(if (TrySkill THROW 0 -10)
						(rock setScript: rockHitsIt)
					else
						(rock setScript: (ScriptID 292 1))
					)
				)
			)
			(4
				(ego use: iRock 1 loop: 1 setScript: 0)
				(StopEgo)
			)
		)
	)
)

(instance rockHitsIt of Script
	(properties)
	
	(method (doit)
		(if
			(and
				(not local12)
				(== (flyingSeed y?) 150)
				(== (rock y?) 160)
			)
			(= local12 1)
			(self cue:)
		)
		(super doit:)
	)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(rock setMotion: Chase flyingSeed 5 self)
			)
			(1
				(Bset SPIREA_INACTIVE)
				(= seedReadyToMove FALSE)
				(flyingSeed setScript: 0 setMotion: 0)
				(rock
					setMotion: MoveTo (+ (flyingSeed x?) 3) (flyingSeed y?) self
				)
				(if (!= ([flowerProps seedTarget] cel?) 0)
					([flowerProps seedTarget] setCycle: BegLoop)
				)
			)
			(2
				(rock setMotion: JumpTo 150 160 self)
				(flyingSeed
					show:
					setStep: 3 15
					setMotion: MoveTo (flyingSeed x?) 150
				)
			)
			(3
				(= seedTarget 2)
				(= seedInPlant 2)
				(flyingSeed setMotion: 0 setCycle: 0 ignoreActors: 0)
				(rock hide:)
				(= cycles 2)
			)
			(4
				(OpenAllFlowers)
				(= cycles 10)
			)
			(5
				(StopUpdatingFlowers)
				(rock hide:)
				(= cycles 1)
			)
			(6
				(flyingSeed setCycle: 0 stopUpd:)
				(StopEgo)
				(ego
					illegalBits: 0
					setMotion:
						MoveTo
						(if (< (flyingSeed x?) (ego x?))
							(+ (flyingSeed x?) 16)
						else
							(- (flyingSeed x?) 16)
						)
						149
						self
				)
			)
			(7
				(ego
					view: (GetEgoViewNumber vEgoThrowing)
					setLoop: (if (< (flyingSeed x?) (ego x?)) 1 else 0)
					cel: 0
					cycleSpeed: 1
					setCycle: EndLoop
				)
				(= cycles 8)
			)
			(8
				(HighPrint 16 40)
				(flyingSeed dispose:)
				(ego setCycle: BegLoop self)
			)
			(9
				(StopEgo)
				(SolvePuzzle POINTS_GETSEED 8)
				(ego get: iSeed setScript: 0)
				(Bset OBTAINED_SPIREA_SEED)
				(HandsOn)
				(rock dispose:)
			)
		)
	)
)

(instance lassoFailed of Script
	(properties)
	
	(method (changeState newState &tmp xDelta yDelta)
		(switch (= state newState)
			(0
				(HandsOff)
				(Bset SPIREA_INACTIVE)
				(= xDelta (/ (- ([flowerProps seedTarget] x?) (magicLasso x?)) 2))
				(= yDelta (/ (- (- ([flowerProps seedTarget] y?) (magicLasso y?)) 30) 2))
				(magicLasso
					setMotion:
						MoveTo
						(+ (magicLasso x?) xDelta)
						(+ (magicLasso y?) yDelta)
						self
				)
			)
			(1 (= seconds 3))
			(2
				(magicLasso dispose:)
				(Bclr SPIREA_INACTIVE)
				(= seedReadyToMove TRUE)
				(StopEgo)
				(HandsOn)
				(HighPrint 16 41)
				(self dispose:)
			)
		)
	)
)

(instance lassoSeed of Script
	(properties)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(Bset SPIREA_INACTIVE)
				(magicLasso
					setMotion:
						MoveTo
						([flowerProps seedTarget] x?)
						(- ([flowerProps seedTarget] y?) 30)
						self
				)
			)
			(1
				(Bclr SPIREA_INACTIVE)
				(= seedReadyToMove TRUE)
				(= lassoActive TRUE)
			)
			(2
				(flyingSeed
					setScript: 0
					setStep: 1 1
					setMotion: MoveTo (flyingSeed x?) (- (magicLasso y?) 2) self
				)
				(spitSound play:)
				([flowerProps seedInPlant] setCycle: BegLoop)
			)
			(3
				(magicLasso
					setPri: 7
					setStep: 6 4
					setMotion: MoveTo (+ (ego x?) 2) (- (ego y?) 36) self
				)
				(flyingSeed
					setPri: 7
					setStep: 6 4
					setMotion: MoveTo (+ (ego x?) 2) (- (ego y?) 38)
				)
			)
			(4 (= seconds 3))
			(5
				(magicLasso hide:)
				(flyingSeed dispose:)
				(= seedTarget 2)
				(= seedInPlant 2)
				(ego setLoop: 2 cel: 0 setCycle: EndLoop self)
			)
			(6
				(HighPrint 16 42)
				(Bset OBTAINED_SPIREA_SEED)
				(SolvePuzzle POINTS_GETSEED 8)
				(ego get: iSeed loop: 1)
				(StopEgo)
				(HandsOn)
				(= cycles 2)
			)
			(7
				(OpenAllFlowers)
				(= cycles 10)
			)
			(8
				(StopUpdatingFlowers)
				(magicLasso dispose:)
			)
		)
	)
)

(instance openUp of Script
	(properties)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(HandsOff)
				(ego setMotion: MoveTo (ego x?) 145 self)
			)
			(1
				(ego view: (GetEgoViewNumber vEgoMagicDetect) setLoop: 0 cycleSpeed: 1 setCycle: EndLoop)
				(= cycles 25)
			)
			(2
				(OpenAllFlowers)
				(flyingSeed
					setPri: (+ ([flowerProps seedTarget] priority?) 1)
					posn: ([flowerProps seedTarget] x?) (- ([flowerProps seedTarget] y?) 15)
					show:
				)
				(= cycles 10)
			)
			(3
				(StopUpdatingFlowers)
				(flyingSeed setMotion: MoveTo (flyingSeed x?) 145 self)
			)
			(4
				(flyingSeed setCycle: 0 stopUpd:)
				(StopEgo)
				(ego
					illegalBits: 0
					setMotion:
						MoveTo
						(if (< (flyingSeed x?) (ego x?))
							(+ (flyingSeed x?) 16)
						else
							(- (flyingSeed x?) 16)
						)
						144
						self
				)
			)
			(5
				(ego
					view: (GetEgoViewNumber vEgoThrowing)
					setLoop: (if (< (flyingSeed x?) (ego x?)) loopW else loopE)
					cel: 0
					cycleSpeed: 1
					setCycle: EndLoop
				)
				(= cycles 8)
			)
			(6
				(HighPrint 16 40)
				(flyingSeed dispose:)
				(ego setCycle: BegLoop self)
			)
			(7
				(StopEgo)
				(SolvePuzzle POINTS_GETSEED 8)
				(ego get: iSeed setScript: NULL)
				(Bset OBTAINED_SPIREA_SEED)
				(HandsOn)
			)
		)
	)
)

(instance leaf0 of PicView
	(properties
		y 93
		x 142
		view vSpittingSpirea
		;loop 0
		;cel 0
	)
)

(instance leaf1 of PicView
	(properties
		y 118
		x 35
		view vSpittingSpirea
		;loop 0
		;cel 0
	)
)

(instance leaf2 of PicView
	(properties
		y 58
		x 67
		view vSpittingSpirea
		;loop 0
		cel 1
	)
)

(instance leaf3 of PicView
	(properties
		y 103
		x 230
		view vSpittingSpirea
		;loop 0
		;cel 0
	)
)

(instance flower0 of Prop
	(properties
		y 89
		x 142
		view vSpittingSpirea
		loop 1
		;cel 0
		cycleSpeed 1
	)
)

(instance flower1 of Prop
	(properties
		y 114
		x 35
		view vSpittingSpirea
		loop 1
		;cel 0
		cycleSpeed 1
	)
)

(instance flower2 of Prop
	(properties
		y 48
		x 66
		view vSpittingSpirea
		loop 1
		;cel 0
		cycleSpeed 1
	)
)

(instance flower3 of Prop
	(properties
		y 99
		x 228
		view vSpittingSpirea
		loop 1
		;cel 0
		cycleSpeed 1
	)
	
	(method (handleEvent event)
		(if
			(or
				(MouseClaimed flower0 event shiftDown)
				(MouseClaimed flower1 event shiftDown)
				(MouseClaimed flower2 event shiftDown)
				(MouseClaimed flower3 event shiftDown)
				(Said 'look/flower,plant,pod')
			)
			(event claimed: TRUE)
			(HighPrint 16 43)
			;You've never seen anything quite like them. They're pretty, in a grotesque way.
			(if (or (Btst SMASHED_FLOWER1) (Btst SMASHED_FLOWER2) (Btst SMASHED_FLOWER3))
				(HighPrint 16 44)
				;At least, they're pretty when they're not dead.
			)
		)
	)
)

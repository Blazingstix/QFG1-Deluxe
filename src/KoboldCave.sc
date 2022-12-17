;;; Sierra Script 1.0 - (do not remove this comment)
(script# 15)
(include system.sh) (include sci2.sh) (include game.sh)
(use Main)
(use ThrowDagger1)
(use ThrowRock)
(use _Interface)
(use TargActor)
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
	rm15 0
	KoboldFight 1
	chest 2
	kobKey 3
	kobDazzle 4
	chestBlows 5
	AwakenKobold 6
	KoboldHurtEgo 7
	egoFighting 8
)

(define CHEST_RANGE 25)

(local
	[dripX 5] = [79 113 172 226 306]
	[dripY 5] = [204 189 193 191 203]
	viewKoboldSitting
	local11
	theCycles
	fightN
	fightW
	fightE
	fightS
	HurtByChest
	soundChestExploding
	[ballX 11] = [17 44 17 251 101 183 283 316 102 244 169]
	[ballY 11] = [47 155 134 186 47 145 124 212 161 51 55]
	[ballStartX 5] = [-24 24 -17 17 -5]
	[ballStartY 5] = [6 6 10 10 13]
	originalHP
)
(procedure (KoboldFight param1)
	;start the kobold fighting.
	
	(if (and param1 (not (Btst KOBOLD_HERO_CAST_FLAMEDART)))
		(HandsOn)
	)
	
	(if fightingKobold
		(User canControl: FALSE)
		(if (ego has: iSword)
			(ego view: (GetEgoViewNumber vEgoFightWithSword) setLoop: egoKoboldBattleLoop)
		else
			(ego view: (GetEgoViewNumber vEgoFightDaggerNoCape) setLoop: (* egoKoboldBattleLoop 5))
		)
		(ego setCel: 0 illegalBits: 0 cycleSpeed: 0)
	else
		(StopEgo)
		(ego illegalBits: koboldIllBits)
	)
)

(procedure (AwakenKobold)
	(return
		(if
			(and
				(not (Btst KOBOLD_AWAKE))
				(not (Btst DEFEATED_KOBOLD))
				(!= (kobold script?) kobWakeUp)
			)
			(kobold setScript: kobWakeUp)
			(return TRUE)
		else
			(return FALSE)
		)
	)
)

(procedure (KoboldHurtEgo damage)
	;reduce ego's HP by the damage amount
	;if he dies, call the egoDies script.
	(if (not (TakeDamage damage)) 
		(ego setScript: egoDies)
	)
)

(procedure (SendKoboldSaidEvent evt str)
	(evt type: speechEvent claimed: FALSE)
	(Parse str evt)
	(User said: evt)
)

(procedure (TimedPrintLocal seconds)
	(Print
		&rest
		#at -1 150
		#width 0
		#mode teJustCenter
		#dispose
		#time seconds
	)
)

(procedure (TimedPrintKobold seconds)
	(Print
		&rest
		#at 150 2
		#width 0
		#mode teJustCenter
		#window kobWin
		#dispose
		#time seconds
	)
)

(procedure (SearchChest obj)
	(Bset KOBOLD_CHEST_SEARCHED)
	(SolvePuzzle POINTS_TAKEKOBOLDMONEY 5)
	(CenterPrint 15 0)
	;You add ten gold and sixty silver coins to your money pouch.
	(if obj (obj dispose:))
	(= koboldIllBits (& koboldIllBits (~ cLMAGENTA)))
	(ego illegalBits: koboldIllBits get: iGold 10 get: iSilver 60)
)

(procedure (KoboldFaceEgo &tmp ang dir)
	(if
		(!=
			(= dir
				(cond 
					(
						(<
							(= ang
								(GetAngle (kobold x?) (kobold y?) (ego x?) (ego y?))
							)
							120
						)
						loopW
					)
					((< ang 150) loopN)
					((> ang 240) loopE)
					((> ang 210) loopS)
					(else 4)
				)
			)
			(kobold loop?)
		)
		(kobold setLoop: dir)
	)
)

(procedure (CastSpellScriptKoboldFighting spell scriptNum scriptIndex)
	;casts an attack spell against the Kobold, calling a specific script
	(if (CastSpell spell)
		(Bset KOBOLD_HAS_SEEN_MAGIC)
		(AwakenKobold)
		(ego setScript: (ScriptID scriptNum scriptIndex))
	)
)

(procedure (CastSpellScriptKoboldSleeping spell scriptNum scriptIndex)
	;casts an area spell against the Kobold, calling a specific script
	(cond 
		((Btst KOBOLD_AWAKE)
			(CenterPrint 15 1)
			;You don't have time for that.
		)
		((CastSpell spell)
			(ego setScript: (ScriptID scriptNum scriptIndex))
		)
	)
)

(class KScript of Script
	(properties
	)
	
	(method (cue)
		(if client (super cue:))
	)
)

(class ballScript of KScript
	(properties
	)
	
	(method (doit)
		(cond 
			((Btst DEFEATED_KOBOLD) 
				(client dispose:)
			)
			((and (< state 2) (not (client inRect: 10 30 310 200)))
				(= register 0)
				(self changeState: 2)
			)
			(else 
				(super doit:)
			)
		)
	)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(ballSound play:)
				;100 - (agility/2) - (distance / 5) >= a Roll of the Dice (from 1-100)
				(if
					(>=
						(- 
							(- 100 (/ [egoStats AGIL] 2)) 
							(/ (ego distanceTo: kobold) 5)
						)
						(RollDice)
					)
					(client setMotion: MoveTo (ego x?) (ego y?) self)
				else
					(= register (Random 0 2))
					(client setMotion: MoveTo [ballX register] [ballY register] self)
				)
			)
			(1
				(cond 
					((< (ego distanceTo: client) 20) 
						(= register 1)
						(self cue:)
					)
					((>= (= register (+ register (Random 1 4))) 11)
						(= register 0)
						(self cue:)
					)
					(else
						(= state 0)
						(client setMotion: MoveTo [ballX register] [ballY register] self)
					)
				)
			)
			(2
				(ballHits play:)
				(client
					setLoop: (if register 7 else 6)
					cel: 0
					setMotion: 0
					setCycle: EndLoop self
				)
				(if register
					(= HurtByChest FALSE)
					(KoboldHurtEgo (if (ego has: iShield) 15 else 20))
				)
			)
			(3 (client dispose:))
		)
	)
)

(instance rm15 of Room
	(properties
		picture 15
		style DISSOLVE
		west 14
	)
	
	(method (init)
		;make a backup measurement of ego's health, in case he dies.
		(= originalHP [egoStats HEALTH])
		(= fightingKobold (= fightingKoboldStart FALSE))
		(if [egoStats MAGIC]
			(LoadMany RES_SCRIPT 110 113 114 115)
			(Load RES_SOUND (GetSongNumber 33))
			(LoadMany RES_VIEW (GetEgoViewNumber vEgoMagicFetch) (GetEgoViewNumber vEgoMagicDetect) (GetEgoViewNumber vEgoMagicFlameDart) vExplosion)
		)
		(if (not (Btst DEFEATED_KOBOLD))
			(Load RES_SCRIPT 111)
			(Load RES_SCRIPT 117)
			(Load RES_SOUND (GetSongNumber 45))
			(Load RES_SOUND (GetSongNumber 34))
			(LoadMany RES_VIEW (GetEgoViewNumber vEgoDaggerDefeated) vKoboldSitting vKoboldFighting vKoboldDead (GetEgoViewNumber vEgoBeginFight))
			(if (ego has: iSword)
				(Load RES_VIEW (GetEgoViewNumber vEgoFightWithSword))
			else
				(Load RES_VIEW (GetEgoViewNumber vEgoFightDaggerNoCape))
				(Load RES_VIEW (GetEgoViewNumber vEgoThrowDagger))
			)
		)
		(if (not (Btst KOBOLD_CHEST_UNLOCKED))
			(Load RES_SCRIPT 116)
			(Load RES_SOUND (GetSongNumber 36))
			(Load RES_SOUND (GetSongNumber 62))
		)
		(Load RES_VIEW (GetEgoViewNumber vEgoThrowing))
		(SolvePuzzle POINTS_ENTERKOBOLDCAVE 2)
		(StatusLine enable:)
		(super init:)
		(keyDownHandler add: self)
		(mouseDownHandler add: self)
		(ChangeGait MOVE_WALK FALSE)
		(StopEgo)
		(ego posn: 1 63 init:)
		(drip init: setScript: dripper)
		(dripper cycles: (Random 5 25))
		(if (not (Btst OBTAINED_TOADSTOOLS)) (toadstools init: setPri: 9))
		(if (not (Btst KOBOLD_CHEST_UNLOCKED))
			(chest init: stopUpd:)
			(= koboldIllBits (| koboldIllBits cLMAGENTA))
		)
		(if (not (Btst OBTAINED_KOBOLD_KEY))
			(if egoKoboldBattleLoop (kobKey posn: 52 84))
			(kobKey
				ignoreActors:
				setPri: (if egoKoboldBattleLoop 5 else 1)
				init:
				stopUpd:
			)
			(if (not (Btst DEFEATED_KOBOLD)) (kobKey hide:))
		)
		(if (not (Btst DEFEATED_KOBOLD))
			(= koboldIllBits (| koboldIllBits cYELLOW))
			(kobold init:)
			(= gKobold kobold)
		)
		(ego illegalBits: koboldIllBits)
		(self setScript: kobEnter)
	)
	
	(method (dispose)
		(Bset VISITED_KOBOLD_CAVE)
		(Bclr KOBOLD_CHEST_KNOWN)
		(super dispose:)
	)
	
	(method (handleEvent event &tmp eventType temp1 spell)
		(cls)
		(= eventType (event type?))
		(cond 
			((super handleEvent: event))
			((== eventType mouseDown)
				(cond 
					(
						(and
							(cast contains: kobKey)
							(MouseClaimed kobKey event emRIGHT_BUTTON)
						)
						(SendKoboldSaidEvent event {look key})
					)
					(
						(and
							(cast contains: treasure)
							(MouseClaimed treasure event emRIGHT_BUTTON)
						)
						(SendKoboldSaidEvent event {look coins})
					)
					(
						(and
							(cast contains: chest)
							(MouseClaimed chest event emRIGHT_BUTTON)
						)
						(SendKoboldSaidEvent event {look chest})
					)
					(
						(and
							(cast contains: toadstools)
							(MouseClaimed toadstools event emRIGHT_BUTTON)
						)
						(SendKoboldSaidEvent event {look mushroom})
					)
					(
						(and
							(not (Btst DEFEATED_KOBOLD))
							(MouseClaimed kobold event emRIGHT_BUTTON)
						)
						(SendKoboldSaidEvent event {look kobold})
					)
				)
			)
			((!= eventType evSAID))
			((and (not (Btst DEFEATED_KOBOLD)) (Said 'chat,ask,awaken'))
				(CenterPrint 15 2)
				;The Kobold seems more interested in frying you with Ball Lightning than in talking to you.
			)
			((Said 'nap,rest') 
				(CenterPrint 15 3)
				;You'd better not.  This cave is not recommended by the Michelin Guide.
			)
			((Said 'search')
				(if (Btst KOBOLD_AWAKE)
					(CenterPrint 15 4)
					;You don't have time for that now.
				else
					(ego setScript: (ScriptID 112 0))
				)
			)
			(
			(Said 'get,grab,get/key[<kobold,brass,big,magic,glowing]')
				(cond 
					((cast contains: kobKey)
						(= temp1 0)
						(cond 
							((> (ego distanceTo: kobKey) 40)
								(CenterPrint 15 5)
								;You can't reach the key from here.
							)
							((Btst DEFEATED_KOBOLD) 
								(CenterPrint 15 6) 
								;You take the Kobold's glowing key.
								(= temp1 TRUE)
							)
							((Btst KOBOLD_AWAKE)
								(CenterPrint 15 7)
								;The Kobold won't let you near his key.
							)
							((TrySkill STEALTH trySneakKobold 0)
								(CenterPrint 15 8)
								;VERY carefully, you remove the key from around the Kobold's neck.
								(= temp1 TRUE)
							)
							(else 
								(CenterPrint 15 9)
								;Uh oh.  You weren't stealthy enough.  You have awoken the Kobold.
								(AwakenKobold)
							)
						)
						(if temp1
							(ego get: iBrassKey)
							(Bset OBTAINED_KOBOLD_KEY)
							(SolvePuzzle POINTS_TAKEKOBOLDKEY 7)
							(kobKey dispose:)
						)
					)
					((ego has: iBrassKey)
						(CenterPrint 15 10)
						;You already have the key.
					)
					((ego pickUp: iBrassKey 1)
						(CenterPrint 15 11)
						;You retrieve the dropped key.
					)
					(else 
						(CenterPrint 15 12)
						;The key is gone.
					)
				)
			)
			((and (Btst KOBOLD_CHEST_KNOWN) (Said 'get,grab,get,move/chest,box,trunk'))
				(CenterPrint 15 13)
				;The chest is far too heavy to move.
			)
			((Said 'get/loot,alm,gold,silver')
				(cond 
					((and
							(< (ego distanceTo: chest) CHEST_RANGE)
							(cast contains: treasure)
						)
						(SearchChest treasure)
					)
					((Btst KOBOLD_CHEST_SEARCHED)
						(CenterPrint 15 14)
						;You've already taken the treasure.
					)
					((not (< (ego distanceTo: chest) CHEST_RANGE))
						(CenterPrint 15 15)
						;You are too far away to get the Kobold's treasure.
					)
					((not (Btst KOBOLD_CHEST_UNLOCKED))
						(CenterPrint 15 16)
						;You will have to open it first.  The chest seems to be locked.
					)
					(else 
						(SearchChest NULL)
					)
				)
			)
			(
				(or
					(Said 'use,turn,open,unlock/key,chest,box,trunk[/key]')
					(Said 'put,fill<in/key/hasp')
				)
				(if (and (Btst KOBOLD_CHEST_KNOWN) (cast contains: chest))
					(CenterPrint 15 17)
					;The key doesn't fit in the chest.  It must be for a different lock.
				else
					(CenterPrint 15 18)
					;You don't see any locks here.
				)
			)
			(
				(or
					(Said 'lockpick/hasp,keyhole,chest,box,trunk')
					(Said 'use,fill/lockpick,(implement,kit<thief)')
				)
				(cond 
					(
					(or (not (Btst KOBOLD_CHEST_KNOWN)) (not (cast contains: chest)))
						(CenterPrint 15 18)
						;You don't see any locks here.
					)
					((Btst KOBOLD_CHEST_UNLOCKED)
						(CenterPrint 15 19)
						;The chest is already open.
					)
					((not (< (ego distanceTo: chest) CHEST_RANGE)) (PrintNotCloseEnough))
					((not [egoStats PICK])
						(CenterPrint 15 20)
						;You have no skill at picking locks.
					)
					(else (ego setScript: (ScriptID 116 0) NULL FALSE)) ;adding arguments to the new Script: caller = NULL, and register = FALSE
				)
			)
			(
			(Said 'force,break,pry/chest,box,trunk,hasp,lid')
				(cond 
					((or (not (Btst KOBOLD_CHEST_KNOWN)) (not (cast contains: chest)))
						(CenterPrint 15 21)
						;You don't see a chest here.
					)
					((Btst KOBOLD_CHEST_UNLOCKED)
						(CenterPrint 15 19)
						;The chest is already open.
					)
					((not (< (ego distanceTo: chest) CHEST_RANGE)) (PrintNotCloseEnough))
					(else (ego setScript: (ScriptID 116 0) NULL TRUE)) ;adding arguments to the new Script: called = NULL, and register = FALSE
				)
			)
			((and (cast contains: toadstools)
					(Said 'get/toadstool,mushroom,food')
				)
				(if fightingKobold
					(CenterPrint 15 22)
					;No time for that now!
				else
					(CenterPrint 15 23)
					;You pick up the Kobold's mushrooms and put them in your pack.
					(Bset OBTAINED_TOADSTOOLS)
					(Bset HAVE_KOBOLD_MUSHROOMS)
					(ego get: iMushroom 6)
					(toadstools dispose:)
				)
			)
			((Said 'get/fungus')
				(CenterPrint 15 24)
				;The fungus is slimy and firmly attached to the cave walls.  You'd better leave it there.
			)
			((Said 'feel/chest,box,trunk')
				(SendKoboldSaidEvent event {look chest})
			)
			((Said 'look>')
				(cond 
					((Said '/west,open,entrance')
						(CenterPrint 15 25)
						;Looking back the way you came, you see a faint reflection of the distant sunlight from the cave entrance.
					)
					((or (Said '/stalactite,stalagmite,boulder,ceiling,roof,floor,ground,formation')
							(Said '<up,down')
						)
						(CenterPrint 15 26)
						;The stalactites look much like the stalagmites, except that they go the other way.
					)
					(
					(Said '[<at,around][/!*,cave,room,area,wall,fungus]')
						(CenterPrint 15 27)
						;This part of the cavern is spacious and multi-leveled.  Phosphorescent fungus illuminates the cavern with an unearthly glow.
						(if (not (Btst DEFEATED_KOBOLD)) 
							(CenterPrint 15 28)
							;On a rocky platform above the cave floor is a Kobold, one of the race of ugly, miserly creatures known for their skill in Magic.
						)
					)
					((and (cast contains: kobKey) (Said '/key'))
						(CenterPrint 15 29)
						;The Kobold's large, brass key glows slightly in the dark cavern.
					)
					((Said '/table,mushroom,toadstool,food')
						(if (cast contains: toadstools)
							(HighPrint 15 30)
							;Some sort of food (mushrooms?) sits in a pile on the stone table.  It looks almost edible... to a Kobold.
						else
							(CenterPrint 15 31)
							;The Kobold's dinner table is now bare.
						)
					)
					((Said '/kobold,creature,man,wizard')
						(if (not (Btst DEFEATED_KOBOLD))
							(CenterPrint 15 32)
							(if (not (Btst OBTAINED_KOBOLD_KEY)) (CenterPrint 15 33))
						else
							(CenterPrint 15 34)
						)
					)
					(
						(or
							(Said '<in/chest,box,trunk')
							(Said '/loot,alm,gold,silver')
						)
						(cond 
							((cast contains: treasure) (CenterPrint 15 35))
							((not (cast contains: chest)) (CenterPrint 15 36))
							((not (Btst KOBOLD_CHEST_KNOWN)) (CenterPrint 15 37))
							((Btst KOBOLD_CHEST_SEARCHED) (CenterPrint 15 38))
							((Btst KOBOLD_CHEST_UNLOCKED) (CenterPrint 15 39))
							(else (CenterPrint 15 40))
						)
					)
					((Said '/chest,box,trunk')
						(cond 
							((not (cast contains: chest)) (CenterPrint 15 41))
							((Btst KOBOLD_CHEST_KNOWN) (CenterPrint 15 42))
							(else (CenterPrint 15 43))
						)
					)
				)
			)
			((Said 'run,escape')
				(CenterPrint 15 44)
				(if (not (Btst DEFEATED_KOBOLD)) (CenterPrint 15 45))
				(if fightingKobold (self setScript: (ScriptID 117 1)))
			)
			((Said 'fight,attack,kill,beat[/kobold,creature,man,wizard]')
				(cond 
					(fightingKobold (CenterPrint 15 46))
					((Btst DEFEATED_KOBOLD) (CenterPrint 15 47))
					((not (if (ego has: iSword) else (ego has: iDagger))) (CenterPrint 15 48))
					(else (self setScript: (ScriptID 117 0)))
				)
			)
			((Said 'throw>')
				(cond 
					(fightingKobold (event claimed: TRUE) 
						(CenterPrint 15 49)
						;You're too close to the Kobold for throwing to be practical.
					)
					((Btst DEFEATED_KOBOLD))
					((Said '/dagger') (AnimateThrowingDagger kobold))
					((Said '/boulder') (AnimateThrowingRock kobold))
				)
			)
			((Said 'cast>')
				(= spell (SaidCast event)) ;get the spell number
				(if (and spell fightingKobold (ego has: iShield))
					(CenterPrint 15 50)
					;You cannot make the arcane gestures to cast spells while carrying your shield.
					(event claimed: TRUE)
					(= spell -1)
					;set the spell to -1, to indicate we can't cast spells
				)
				(switch spell
					(-1) ;ignore a -1 spell -- we're holding a shield
					(0 
						(event claimed: FALSE)
					)
					(DAZZLE
						(if (not (Btst DEFEATED_KOBOLD))
							(CastSpellScriptKoboldFighting DAZZLE 113 0)
						else
							(event claimed: FALSE)
						)
					)
					(FLAMEDART
						(if (not (Btst DEFEATED_KOBOLD))
							(CastSpellScriptKoboldFighting FLAMEDART 110 0)
						else
							(event claimed: FALSE)
						)
					)
					(CALM 
						(CastSpellScriptKoboldFighting 22 114 0)
					)
					(ZAP 
						;bump the zap cast back to the main script
						(event claimed: FALSE)
					)
					(fightingKobold ;we can only cast Detect Magic, Open, Trigger, or Fetch if we're not actively fighting the Kobold.
						;if we're fighting the Kobold, the loop will stop here.
						(CenterPrint 15 1)
						;You don't have time for that.
					)
					(DETMAGIC 
						(CastSpellScriptKoboldSleeping DETMAGIC 113 1)
					)
					(OPEN
						(if
						(and (not (Btst KOBOLD_CHEST_UNLOCKED)) (cast contains: chest))
							(CastSpellScriptKoboldSleeping OPEN 115 0)
						else
							(CenterPrint 15 51)
							;There's nothing to open here.
						)
					)
					(TRIGGER
						(if
						(and (not (Btst KOBOLD_CHEST_UNLOCKED)) (cast contains: chest))
							(CastSpellScriptKoboldSleeping TRIGGER 115 1)
						else
							(CenterPrint 15 52)
							;There are no passive spells to trigger here.
						)
					)
					(FETCH
						(if
						(and (not (Btst DEFEATED_KOBOLD)) (cast contains: kobKey))
							(CastSpellScriptKoboldSleeping FETCH 114 1)
						else
							(CenterPrint 15 53)
							;You don't need to cast Fetch -- everything here is in easy reach.
						)
					)
					(else  
						(event claimed: FALSE)
					)
				)
			)
		)
	)
)

(instance kobKey of Actor
	(properties
		y 85
		x 229
		view vKoboldDead
		loop 6
		cel 12
		illegalBits $0000
	)
)

(instance chest of Prop
	(properties
		y 173
		x 132
		view vKoboldCave
		loop 1
	)
	
	(method (doit)
		(if
		(and (< (ego distanceTo: chest) CHEST_RANGE) (not (Btst KOBOLD_CHEST_KNOWN)))
			(CenterPrint 15 54)
			;You just bumped into something on the floor.  It feels like a large wooden chest, but you don't see anything there.
			(Bset KOBOLD_CHEST_KNOWN)
		)
		(super doit:)
	)
)

(instance chestBlows of KScript
	(properties)
	
	(method (dispose)
		(if (>= state 1) (soundChestExploding dispose:))
		;(= HurtByChest FALSE)
		(super dispose:)
	)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				;make the chest glow
				(client
					setLoop: 2
					cel: 0
					cycleSpeed: 0
					setCycle: CycleTo 4 cdFORWARD self
				)
			)
			(1
				(Bset KOBOLD_CHEST_UNLOCKED)
				;play the exploding sound
				((= soundChestExploding (Sound new:))
					number: (GetSongNumber 62)
					priority: 15
					init:
					play:
				)
				;make the chest explode
				(client setCycle: EndLoop)
				;wake up the kobold (if he's not dead)
				(AwakenKobold)
				;if we're close enough, ego should take damage.
				(if (< (ego distanceTo: chest) CHEST_RANGE)
					(= HurtByChest TRUE)
					(KoboldHurtEgo 20)
					;if we died, the script stops here, and is disposed.
				)
				(= cycles 10)
			)
			(2
				(= cycles 1)
				(if (< (ego distanceTo: chest) CHEST_RANGE)
					(CenterPrint 15 55)
					;Wow!  The chest must have been booby-trapped.  You can really feel the damage from the blast.
					(= cycles 5)
				)
			)
			(3
				;add the treasure to the cast list
				;(for easier looking/searching, I suppose -- CI)
				(treasure init:)
				(KoboldFight TRUE)
				(FaceObject ego treasure)
				;dispose of the chest, because it's no longer needed
				(client dispose:)
			)
		)
	)
)

(instance treasure of View
	(properties
		y 173
		x 132
		view vKoboldCave
		loop 2
		cel 8
	)
)

(instance toadstools of View
	(properties
		y 117
		x 213
		view vKoboldCave
		loop 3
	)
)

(instance drip of Prop
	(properties
		y 204
		x 79
		view vKoboldCave
	)
)

(instance dripEndLoop of EndLoop
	(properties)
)

(instance dripper of KScript
	(properties)
	
	(method (changeState newState &tmp i)
		(switch (= state newState)
			(1
				(= i (Random 0 4))
				(drip
					posn: [dripX i] [dripY i]
					setCycle: dripEndLoop
				)
				(= state 0)
				(= cycles (Random 20 40))
			)
		)
	)
)

(instance kobWin of SysWindow
	(properties
		color vBROWN
	)
)

(instance ballSound of Sound
	(properties
		number 34
		priority 2
	)
)

(instance ballHits of Sound
	(properties
		number 45
		priority 3
	)
)

(instance kobold of TargActor
	(properties
		y 85
		x 229
		view vKoboldSitting
		loop 6
		cycleSpeed 3
		illegalBits $0000
		targDeltaY -15
	)
	
	(method (init)
		(Bclr KOBOLD_BOUNCING_FLAMEDART)
		(Bclr KOBOLD_REVERSAL_ACTIVE)
		(ballSound number: (GetSongNumber 34) init:)
		(ballHits number: (GetSongNumber 45) init:)
		(if (ego knows: FLAMEDART)
			(= damageToKoboldFlame (+ 5 (/ [egoStats FLAMEDART] 3)))
		)
		(= egoKoboldBattleLoop 0)
		(super ignoreActors: posn: 229 85 setPri: 6 init:)
		(if (< colourCount 8) (kobWin color: vBLACK back: vWHITE))
		(if
			(or
				(not (Btst VISITED_KOBOLD_CAVE))
				(and
					(not isNightTime)
					(!= currentDay dayKoboldAwakened)
				)
			)
			(self setScript: kobAsleep)
		else
			(self setScript: kobAwake)
		)
		(= monsterInRoom KOBOLD)
		(= gMonsterHealth koboldHP)
	)
	
	(method (dispose)
		(Bclr KOBOLD_AWAKE)
		(ballHits dispose:)
		(ballSound dispose:)
		(if viewKoboldSitting (viewKoboldSitting dispose:))
		(super dispose:)
	)
	
	(method (getHurt damage)
		(= koboldHP (- koboldHP damage))
		(self setScript: kobHurt)
	)
)

(instance kobDazzle of KScript
	;cast dazzle on the Kobold, to blind him.
	(properties)
	
	(method (dispose)
		(= local11 (* register 5))
		(super dispose:)
	)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(SkillUsed DAZZLE tryStatKoboldDazzle)
				(= register (/ (+ 5 [egoStats DAZZLE]) 10))
				(if (> register 6) (= register 6))
				(client view: vKoboldReflect setCel: 0)
				(= cycles 10)
			)
			(1
				(kobold setCel: -1 cycleSpeed: 2 setCycle: CycleTo 2 cdFORWARD)
				(= cycles 10)
			)
			(2
				(kobold setCel: 3)
				(= cycles 4)
			)
			(3
				(kobold setCel: 4)
				(= cycles 4)
			)
			(4
				(if (> (-- register) 0)
					(self changeState: 2)
				else
					(= cycles 5)
				)
			)
			(5
				(kobold setCel: 5)
				(= cycles 20)
			)
			(6 (client setScript: kobAwake))
		)
	)
)

(instance kobWakeUp of KScript
	(properties)
	
	(method (dispose)
		(if viewKoboldSitting
			(viewKoboldSitting dispose:)
			(= viewKoboldSitting NULL)
			(client setCycle: NULL)
		)
		(if egoKoboldBattleLoop
			(client posn: 52 84)
		else
			(client posn: 229 85)
		)
		(cSound stop:)
		(super dispose:)
	)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(Bset KOBOLD_AWAKE)
				(client setCycle: EndLoop)
				(= cycles 12)
			)
			(1
				(client setLoop: 7 setCycle: Forward)
				(TimedPrintKobold 5 15 56)
				;"You go!  You no stay here!"
				(= seconds 3)
			)
			(2 (client setScript: kobAwake))
		)
	)
)

(instance kobAwake of KScript
	(properties)
	
	(method (doit)
		(KoboldFaceEgo)
		(if (and (Btst KOBOLD_BOUNCING_FLAMEDART) (== state 0))
			(Bclr KOBOLD_BOUNCING_FLAMEDART)
			;skip past the action deciding
			(self changeState: 3)
		)
		(super doit:)
	)
	
	(method (changeState newState &tmp clientLoop)
		(switch (= state newState)
			(0
				(Bset KOBOLD_AWAKE)
				(= dayKoboldAwakened currentDay)
				(client view: vKoboldSitting cel: 0 setCycle: NULL)
				(KoboldFaceEgo)
				(if egoKoboldBattleLoop
					(client posn: 52 84)
				else
					(client posn: 229 85)
				)
				(cond 
					(local11
						(client view: vKoboldReflect setCel: 1)
						(= cycles (Random 25 50))
						(= local11 0)
					)
					((and (Btst KOBOLD_HAS_SEEN_MAGIC) (not (Btst KOBOLD_REVERSAL_ACTIVE)))
						(= cycles (Random 5 15))
					)
					(koboldCycles 
						(= cycles koboldCycles)
						(= koboldCycles 0)
					)
					(theCycles
						(= cycles theCycles)
					)
					(else 
						(= cycles (Random 25 50))
					)
				)
			)
			(1
				(client view: vKoboldSitting)
				(cond 
					((and (Btst KOBOLD_HAS_SEEN_MAGIC) (not (Btst KOBOLD_REVERSAL_ACTIVE)))
						(client setScript: castRev)
					)
					(theCycles 
						(= theCycles 0) 
						(client setScript: castTele)
					)
					(else 
						(client view: vKoboldMagic cycleSpeed: 0 setCycle: EndLoop self)
					)
				)
			)
			(2
				(= clientLoop (client loop?))
				((Actor new:)
					ignoreActors:
					illegalBits: 0
					view: vKoboldMagic
					setLoop: 5
					setStep: 24 16
					posn:
						(+ (client x?) [ballStartX clientLoop])
						(+ (client y?) [ballStartY clientLoop])
						20
					init:
					setCycle: Forward
					setScript: (ballScript new:)
				)
				(= state -1)
				(client view: vKoboldSitting cycleSpeed: 3 setCycle: EndLoop self)
			)
			(3
				(client view: vKoboldDead setCel: 0)
				(= cycles 2)
			)
			(4
				(client setCel: 1)
				(= cycles 2)
			)
			(5
				(client view: vKoboldSitting cel: 0 setCel: RELEASE forceUpd:)
				;back to the beginning
				(= state 0)
				(= cycles 5)
			)
		)
	)
)

(instance kobAsleep of KScript
	(properties)
	
	(method (doit)
		(if
			(and
				(< (ego distanceTo: client) 100)
				(or (!= egoGait MOVE_SNEAK) (< [egoStats STEALTH] 20))
			)
			(TimedPrintLocal 7 15 57)
			;Your foot slips on the wet cavern floor and makes a scuffing noise.
			(ChangeGait MOVE_WALK FALSE)
			(client setScript: kobWakeUp)
		)
		(super doit:)
	)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(= koboldHP MAX_HP_KOBOLD)
				((= viewKoboldSitting (View new:))
					view: vKoboldSitting
					loop: 5
					cel: 0
					ignoreActors:
					posn: 229 85
					setPri: 6
					init:
				)
				(client
					posn: (viewKoboldSitting x?) (- (viewKoboldSitting y?) 17)
					setLoop: 6
					cycleSpeed: 1
				)
			)
		)
	)
)

(instance castRev of KScript
	(properties)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(if (not (Btst HERO_SEEN_REVERSAL))
					(Bset HERO_SEEN_REVERSAL)
					(TimedPrintLocal 7 15 58)
					;The Kobold casts a spell with which you are not familiar.
				)
				(client
					view: vKoboldFighting
					setLoop: 1
					cel: 0
					cycleSpeed: 1
					setCycle: EndLoop
				)
				(= cycles 10)
			)
			(1
				(Bset KOBOLD_REVERSAL_ACTIVE)
				(client setScript: kobAwake)
			)
		)
	)
)

(instance castTele of KScript
	(properties)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(client
					view: vKoboldFighting
					setLoop: 0
					cel: 0
					cycleSpeed: 0
					setCycle: EndLoop self
				)
				(if fightingKobold
					(curRoom setScript: (ScriptID 117 1))
				)
			)
			(1
				(= egoKoboldBattleLoop (- 1 egoKoboldBattleLoop))
				(if fightingKoboldStart
					(StopEgo)
					(ego illegalBits: 0)
					((ScriptID 117 0) changeState: 1)
				)
				(if egoKoboldBattleLoop
					(client posn: 52 84)
				else
					(client posn: 229 85)
				)
				(client setCycle: BegLoop self)
			)
			(2 (client setScript: kobAwake))
		)
	)
)
(enum 1
	kDeath_Chest1
	kDeath_Chest2
	kDeath_Kobold
)

(instance egoDies of KScript
	(properties)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				;first we'll remove any active scripts from the chest.
				(chest setScript: NULL)
				(HandsOff)
				(client
					view: (GetEgoViewNumber vEgoDaggerDefeated)
					setLoop: egoKoboldBattleLoop
					cel: 0
					illegalBits: 0
					cycleSpeed: 1
					setCycle: EndLoop self
				)
			)
			(1
				(cond 
					((Btst KOBOLD_CHEST_EXPLODED)
						;kobold chest exploded from the koboldPickChest script
						(= register kDeath_Chest1)
						(EgoDead DIE_RETRY DIE_KOBOLD_CHEST1 15 59
							#title {Your plan seems to have backfired}
							#icon vKoboldCave 2 6
						)
						;Next time you swing at innocent inanimate objects, make sure you are strong enough to survive when they swing back at you!
					)
					(HurtByChest 
						;kobold chest exploded from the local script
						(= register kDeath_Chest2)
						(EgoDead DIE_RETRY DIE_KOBOLD_CHEST2 15 60 
							#title {Blast!} 
							#icon vKoboldCave 2 6
						)
						;You could not withstand the force of the blast from the Kobold's magically protected chest.
					)
					(else 
						;ego was killed by the kobold's magic
						(= register kDeath_Kobold)
						(EgoDead DIE_RETRY DIE_KOBOLD 15 61 
							#title {You've been deep-fried} 
							#icon vExplosion 0 1
						)
						;You are overcome by the power of the Kobold Wizard's magic.  You need to either practice your skills more or plan a better attack strategy.
					)
				)
				(= debugValue register)
				(= cycles 1)
			)
			(2
				;(Print {%d} deathType)
				(= debugValue register)
				
				(StopEgo)
				;if it's a chest death, we have to reset the chest
				(if (or (== register kDeath_Chest1) (== register kDeath_Chest2))
					;(Print {Chest Death})
					;just have to reset the chest exploding.
					(Bclr KOBOLD_CHEST_EXPLODED)
					(Bclr KOBOLD_CHEST_UNLOCKED)
					(= [egoStats HEALTH] 10) ; give ego 5 HP to continue on.
					(= HurtByChest FALSE)
					(chest loop: 1 cel: 0)
					(FaceObject ego chest)
				)
				
				;then, if the kobold is still alive
				;just reset the whole room, too.
				(if (or (== register kDeath_Kobold) (< koboldStatus koboldDEAD))
					;(Print {Kobold death})
					;have to reset the whole kobold. we'll restore his HP, and let the hero try again from the top.
					(StopEgo)
					(= [egoStats HEALTH] originalHP)
					(Bclr KOBOLD_HAS_SEEN_MAGIC)
					(= fightingKoboldStart (= fightingKobold FALSE))
					;reset the kobold's HP, and position, etc.
					(= koboldHP MAX_HP_KOBOLD)
					(= koboldStatus koboldASLEEP)
					(ego posn: 100 77 loop: loopE)
					(kobold init:)
					(kobold cel: 0)
				)
				(HandsOn)
			)
		)
	)
)

(instance kobHurt of KScript
	(properties)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(if (Btst DEFEATED_KOBOLD)
					(self dispose:)
				else
					(if viewKoboldSitting
						(viewKoboldSitting dispose:)
						(= viewKoboldSitting NULL)
						(if egoKoboldBattleLoop
							(client posn: 52 84)
						else
							(client posn: 229 85)
						)
					)
					(client view: vKoboldDead setCycle: 0 setMotion: 0)
					(KoboldFaceEgo)
					(client setCel: 2)
					(= cycles 5)
				)
			)
			(1
				(client setCel: 3)
				(= cycles 3)
			)
			(2
				(client setCel: RELEASE)
				(if (> koboldHP 0)
					(= theCycles 3)
					(client setScript: kobAwake)
				else
					(client setScript: kobDies)
				)
			)
		)
	)
)

(instance kobDies of KScript
	(properties)
	
	(method (changeState newState &tmp temp0)
		(switch (= state newState)
			(0
				(HandsOff)
				(Bset DEFEATED_KOBOLD)
				(Bclr KOBOLD_AWAKE)
				(ego setScript: NULL)
				(curRoom setScript: NULL)
				(= koboldIllBits (& koboldIllBits (~ cYELLOW)))
				(client
					view: vKoboldDead
					setLoop: 5
					cel: 0
					cycleSpeed: 1
					setCycle: EndLoop self
				)
				(if (not (Btst OBTAINED_KOBOLD_KEY))
					(kobKey
						show:
						illegalBits: 0
						ignoreActors:
						view: vKoboldDead
						posn: (kobold x?) (kobold y?)
						setLoop: 6
						cel: 0
						setPri: (if egoKoboldBattleLoop 5 else 1)
						cycleSpeed: 1
						setCycle: EndLoop
					)
				)
			)
			(1
				(= monsterInRoom (= gMonsterHealth 0))
				(if (or (== heroType FIGHTER) (== heroType MAGE))
					(SolvePuzzle POINTS_KILLKOBOLD 10)
				)
				(cSound number: 20 loop: -1 play:)
				(if fightingKobold
					(curRoom setScript: (ScriptID 117 1))
				else
					(HandsOn)
					(curRoom setScript: NULL)
				)
				(client dispose:)
			)
		)
	)
)

(instance kobEnter of KScript
	(properties)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(HandsOff)
				(if (not (Btst VISITED_KOBOLD_CAVE)) 
					(TimedPrintLocal 7 15 62)
					;This section of cave has an eerie quality.
				)
				(ego setMotion: MoveTo 65 63 self)
			)
			(1
				(ego setMotion: MoveTo 100 77 self)
			)
			(2 (HandsOn) (self dispose:))
		)
	)
)

(enum ;FightAction
	swBlow 
	swDodge
	swParry
	knStab
	knDodge
)

(instance egoFighting of KScript
	(properties)
	
	(method (dispose)
		(directionHandler delete: self)
		(ego setScript: 0 illegalBits: koboldIllBits)
		(super dispose:)
	)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(if (ego has: iSword)
					(= fightN swBlow)
					(= fightW (= fightE swDodge))
					(= fightS swParry)
				else
					(= fightN knStab)
					(= fightW (= fightE (= fightS knDodge)))
				)
				(= fightingKobold TRUE)
				(directionHandler addToFront: self)
				(self cue:)
			)
			(1 
				(KoboldFight TRUE)
				(= state 0)
			)
		)
	)
	
	(method (handleEvent event &tmp temp0)
		(cond 
			((super handleEvent: event))
			(
				(and
					(== (event type?) direction)
					(not (ego script?))
				)
				(HandsOff)
				(switch (event message?)
					(dirN
						(ego setScript: (ScriptID 111 fightN) self 0)
					)
					(dirW
						(ego setScript: (ScriptID 111 fightW) self 1)
					)
					(dirE
						(ego setScript: (ScriptID 111 fightE) self 2)
					)
					(dirS
						(ego setScript: (ScriptID 111 fightS) self 3)
					)
					(else 
						(HandsOn)
						(User canControl: FALSE)
					)
				)
			)
		)
	)
)

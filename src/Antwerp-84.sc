;;; Sierra Script 1.0 - (do not remove this comment)
(script# 84)
(include system.sh) (include sci2.sh) (include game.sh)
(use Main)
(use ThrowFlameDart)
(use ThrowDagger1)
(use ThrowRock)
(use AnimateDazzle)
(use TargActor)
(use _LoadMany)
(use _Sound)
(use _Motion)
(use _Game)
(use _Inventory)
(use _Actor)
(use _System)
(use _Interface)

(public
	rm84 0
)

(local
	antwerpOnScreen
	maxX 	 =  90		;never changes. This is the maximum ± X boundary of the Antwerp's personal space.
	maxY 	 =  40		;never changes. This is the maximum ± Y boundary of the Antwerp's personal space.
	minX 	 =  20		;never changes.
	dX					;deltaX between hero and antwerp. set and used in antwerpFollows and antwerpPushes scripts
	dY					;deltaY between hero and antwerp. set and used in antwerpFollows and antwerpPushes scripts
	antCageX =  114		;never changes. X position of optimal antwerp placement (for fighting, maybe? bouncing out of there?)
	antCageY =  96		;never changes. Y position of optimal antwerp placement (for fighting, maybe? bouncing out of there?)
	bounceCounter 	  =  1		;bounceCounter. changes in antwerpFollow
	bounceCounterInit =  6		;never changes. Initial value for bounceCounter. Gets reset here when bounceCounter==0
	practiceSwings		;countdown for ego to take some prectice swings before fighting antwerp
	fightWithSword		;boolean. set at room initialization. Does ego have a sword?
	egoStartHP			;added by CI. For use with Retry Deaths. How much HP did ego have when he entered the room?
)
(instance rm84 of Room
	(properties
		picture 84
		style DISSOLVE
		north 78
		east 85
	)
	
	(method (init)
		;this is a retry event
		;so let's change the redraw style to match our fav style.
		(if (or (Btst DIE_RETRY_INPROGRESS) (== prevRoomNum TROLL))
			(self style: RETRY_STYLE)
		)
		(super init:)
		(mouseDownHandler add: self)
		(StatusLine enable:)
		(cSound fade:)
		(self setLocales: FOREST)
		;if we've just come from the Troll cave, or the Leader's Room
		(if (or (== prevRoomNum 97) (== prevRoomNum 89))
			(Bset TROLL_DOOR_OPEN)
		)
		(if (Bset VISITED_BRIGAND_COURTYARD) 
			(Bset BRIGANDS_UNAWARE)
		)
		;set up a reminder, in case we get killed
		(= egoStartHP [egoStats HEALTH])
		
		(StopEgo)
		(ego init:)
		(if (not (Btst TROLL_DOOR_OPEN))
			(ego illegalBits: (| cWHITE cBROWN))
			(rock init: stopUpd:)
		)
		;the antwerp's not on screen if he's in the air, or he's been split.
		(if (= antwerpOnScreen (not (if (Btst ANTWERP_SPLIT) else (Btst ANTWERP_SKY))))
			(antwerp init: loop: 3 setScript: bounceAndLook)
			(LoadMany RES_VIEW vAntwerp (GetEgoViewNumber vEgoDaggerDefeated) (GetEgoViewNumber vEgoBeginFight) (GetEgoViewNumber vEgoDefeated) (GetEgoViewNumber vEgoThrowing) vSecretEntranceRock)
			(LoadMany RES_SOUND (GetSongNumber 4) (GetSongNumber 5) (GetSongNumber 6) (GetSongNumber 9) 54)
			(cSound stop:)
			(antSound number: (GetSongNumber 4) init:)
			(antHits init:)
			(cond 
				((= fightWithSword (ego has: iSword)) (Load RES_VIEW (GetEgoViewNumber vEgoFightWithSword)))
				((ego has: iDagger) (Load RES_VIEW (GetEgoViewNumber vEgoFightDaggerNoCape)))
			)
		)
		
		(if (Btst DIE_RETRY_INPROGRESS)
			;this is a retry from being flattened by the Antwerp
			(ego posn: 198 75 loop: loopW)
			(Bclr DIE_RETRY_INPROGRESS)
		else
			(switch prevRoomNum
				(TROLL ;this is a retry, for the fight with Troll
					(ego posn: 36 90 loop: loopW)
				)
				(89	;troll cave
					(ego posn: 25 90 setMotion: MoveTo 36 90)
				)
				(97 ;brigand leader's office
					(ego posn: 25 90 setMotion: MoveTo 36 90)
				)
				(78 ;north screen
					(ego posn: 160 30 setMotion: MoveTo 198 75)
				)
				(85 ;east screen
					(ego posn: 319 100 setMotion: MoveTo 295 100)
				)
			)
		)
	)
	
	(method (doit)
		(cond 
			((and (== (ego onControl: origin) cLRED) (== (ego loop?) loopN))
				(curRoom newRoom: 78)
			)
			((<= (ego x?) 25)
				(curRoom newRoom: 89)
			)
		)
		(super doit:)
	)
	
	(method (dispose)
		(mouseDownHandler delete: self)
		(Bset VISITED_ANTWERP)
		(if (!= newRoomNum 89)
			(Bclr SAID_HIDEN_GOSEKE)
			(if (not (if (Btst DEFEATED_FRED) else (Btst DEFEATED_FRED_89)))
				(Bclr TROLL_DOOR_OPEN)
				(Bclr TROLL_DOOR_UNLOCKED)
			)
		)
		(super dispose:)
	)
	
	(method (handleEvent event &tmp [str 30] spell projectile)
		(switch (event type?)
			(mouseDown
				(cond 
					((super handleEvent: event))
					((MouseClaimed ego event emRIGHT_BUTTON)
						(HighPrint (Format @str 84 0 @userName)
							;By golly, It's %s!
						)
					)
				)
			)
			(evSAID
				(cond 
					((super handleEvent: event))
					((Said 'look,search>')
						(cond 
							((Said '[<at,around][/place,area]')
								(cond 
									((Btst ANTWERP_SPLIT)
										(HighPrint 84 1)
										;Now that the big Antwerp has split, this corner of the forest seems strangely quiet.
										)
									((Btst ANTWERP_SKY)
										(HighPrint 84 2)
										;You look around and see rocks and grass, but no Antwerp.
										)
									(else
										(HighPrint 84 3)
										;You see rocks, grass and an Antwerp.
										)
								)
							)
							(
								(Said
									'[<at,around][/antwerp,monster,creature,beast,animal,bouncer]'
								)
								(cond 
									((Btst ANTWERP_SPLIT)
										(HighPrint 84 4)
										;The only Antwerp known in these parts has split... into parts.
										)
									((Btst ANTWERP_SKY)
										(HighPrint 84 5)
										;The Antwerp seems to have flown the coop.
										)
									(else
										(HighPrint 84 6)
										;Antwerps are on the endangered species list.  They are rarely seen.
										)
								)
							)
							((Said '[<at][/boulder,boulder]')
								(cond 
									((not (ego inRect: 0 72 66 102))
										(HighPrint 84 7)
										;The rocks were left here by some receding glacier.
										)
									((Btst TROLL_DOOR_OPEN)
										(HighPrint 84 8)
										;There is a narrow cave entrance among the rocks.
										)
									(else
										(HighPrint 84 9)
										;You find a keyhole concealed in a crack in the rock.
										)
								)
							)
							((or (Said '/cave,entrance') (Said '<in'))
								(if (Btst TROLL_DOOR_OPEN)
									(HighPrint 84 10)
									;There is a narrow cave entrance among the rocks.  Inside, you see a dark passage through the hillside.
								else
									(HighPrint 84 11)
									;You see nothing like that here.
								)
							)
							((Said '/door,keyhole,hasp')
								(if (ego inRect: 0 72 66 102)
									(HighPrint 84 9)
									;You find a keyhole concealed in a crack in the rock.
								else
									(HighPrint 84 12)
									;You do not find anything nearby.
								)
							)
							((or (Said '<up') (Said '/sky'))
								(HighPrint 84 13)
								;The sky is clear.
								)
							((or (Said '<down') (Said '/ground,grass'))
								(HighPrint 84 14)
								;The grass is luscious, just the thing for hungry herbivores.
								)
							((Said '/south,west')
								(HighPrint 84 15)
								;The way is impassable.  Sheer rock cliffs rise to serious heights.
								)
							((Said '/east,north')
								
								(HighPrint 84 16)
								;The forest extends to the east and north.
								)
						)
					)
					((Said 'cast>')
						(switch (= spell (SaidCast event))
							(DETMAGIC
								(if (CastSpell spell)
									(HighPrint 84 17)
									;You detect no magic here.
									)
							)
							(DAZZLE
								(if (CastSpell spell)
									(AnimateDazzle)
									(if antwerpOnScreen
										(HighPrint 84 18)
										;Antwerps aren't dazzled easily.
										)
								)
							)
							(FLAMEDART
								(if (CastSpell spell)
									(if (cast contains: antwerp)
										(FaceObject ego antwerp)
										(RedrawCast)
									)
									(if antwerpOnScreen
										(AnimateThrowingFlameDart antwerp)
										(antwerp setScript: antwerpAway)
									else
										(AnimateThrowingFlameDart NULL)
									)
								)
							)
							(OPEN
								(cond 
									((not (ego inRect: 0 72 66 102))
										(HighPrint 84 19)
										;You aren't close enough to a lock.
										)
									((CastSpell spell)
										(if (Btst TROLL_DOOR_UNLOCKED)
											(HighPrint 84 20)
											;It's already unlocked.
										else
											(self setScript: sMagicRock)
										)
									)
								)
							)
							(else 
								(event claimed: FALSE)
							)
						)
					)
					((Said 'throw/dagger,dagger')
						(= projectile (if (cast contains: antwerp) antwerp else NULL))
						(if (AnimateThrowingDagger projectile)
							(if (cast contains: antwerp)
								(= daggersInRoom (+ daggersInRoom daggersInMonster))
								(= daggersInMonster 0)
								(FaceObject ego antwerp)
								(RedrawCast)
							)
							(if antwerpOnScreen (antwerp setScript: antwerpAway))
						)
					)
					((Said 'throw/boulder')
						(= projectile (if (cast contains: antwerp) antwerp else NULL))
						(if (AnimateThrowingRock projectile)
							(if (cast contains: antwerp)
								(FaceObject ego antwerp)
								(RedrawCast)
							)
							(if antwerpOnScreen (antwerp setScript: antwerpAway))
						)
					)
					((Said 'fight,kill[/bouncer,antwerp,animal,beast,monster,creature]')
						(cond 
							((Btst ANTWERP_SPLIT)
								(HighPrint 84 4)
								;The only Antwerp known in these parts has split... into parts.
								)
							((Btst ANTWERP_SKY)
								(HighPrint 84 5)
								;The Antwerp seems to have flown the coop.
								)
							((or fightWithSword (ego has: iDagger))
								(antwerp setScript: fightAntwerp)
							)
							(else
								(HighPrint 84 21)
								;You have no weapon with which to fight the Antwerp.
								(HighPrint 84 22)
								;However, you bravely attack the bouncing beast with your bare hands.
								(ego setScript: bareHandAttack)
							)
						)
					)
					((Said 'feed[/antwerp,creature,monster,bouncer,animal,beast]')
						(cond 
							((Btst ANTWERP_SPLIT)
								(HighPrint 84 23)
								;The only Antwerp known to these parts, split ...into parts.
								)
							((Btst ANTWERP_SKY)
								(HighPrint 84 5)
								;The Antwerp seems to have flown the coop.
								)
							(else (HighPrint 84 24)
								;He's on a diet.
								)
						)
					)
					(
						(or
							(Said 'unlock,lockpick/door,boulder,hasp,keyhole')
							(Said 'use/key,lockpick')
							(Said 'open/hasp,keyhole')
							(Said 'put,fill<in/key/hasp')
						)
						(cond 
							((not (ego inRect: 0 72 66 102))
								(HighPrint 84 25)
								;You don't see any locks nearby.
							)
							((Btst TROLL_DOOR_UNLOCKED)
								(HighPrint 84 20)
								;It's already unlocked.
							)
							((and (ego has: iBrassKey) (Btst OBTAINED_BRUTUS_KEY))
								(HighPrint 84 26)
								;The lock in the rock clicks open.
								(Bset TROLL_DOOR_UNLOCKED)
							)
							((ego has: iBrassKey) ;CI: Added extra explaination
								(HighPrint 84 46)
								;The key doesn't fit in this lock. You'll need a different brass key to open this rock.
							)
							((not (CanPickLocks))
								(HighPrint 84 27)
								;You'd have a much easier time of this if you had the key.
							)
							((TrySkill PICK tryPickSecretEntrance lockPickBonus)
								(HighPrint 84 28)
								;Ah, got it!  The lock in the rock clicks open.
								(Bset TROLL_DOOR_UNLOCKED)
							)
							(else
								(HighPrint 84 29)
								;The lock is beyond your present skill.  It might help if you had the key.
								(if (not (ego has: iThiefKit))
									(HighPrint 84 30)
									;... Or at least a better set of tools.
								)
							)
						)
					)
					((Said 'shove,move,force,get,open/boulder,door')
						(if (not (Btst TROLL_DOOR_OPEN))
							(if (Btst TROLL_DOOR_UNLOCKED)
								(if (TrySkill STR tryForceSecretEntrance 0)
									(rock setScript: sMoveRock)
								else
									(HighPrint 84 31)
									;You are not strong enough yet to open the rock door.
								)
							else
								(HighPrint 84 32)
								;Despite your mightiest efforts, the rock does not move.
							)
						else
							(HighPrint 84 33)
							;The rock door has already been opened.
						)
					)
					((or (Said 'say,holler<hiden/goseke') (Said 'hiden/goseke'))
						(if
							(and
								(Btst SPIED_THIEVES)
								(ego inRect: 0 72 66 102)
								(Btst TROLL_DOOR_OPEN)
								(not (Btst SAID_HIDEN_GOSEKE))
								(not (Btst DEFEATED_FRED))
							)
							(Bset SAID_HIDEN_GOSEKE)
							(SolvePuzzle POINTS_GIVECAVEPASSWORD 5)
							(HighPrint 84 34)
							;You hear the sound of someone...or something...moving deeper into the cave to let you pass.
						else
							(HighPrint 84 35)
							;Ok, you say Hiden Goseke.
						)
					)
					((Said 'enter/cave,entrance')
						(HighPrint 84 36)
						;Go ahead. If you dare.
						)
				)
			)
		)
	)
)

(instance antwerp of TargActor
	(properties
		y 96
		x 114
		yStep 4
		view vAntwerp
		loop 2
		cycleSpeed 1
		xStep 4
		moveSpeed 1
	)
	
	(method (doit)
		(if
			(and
				(== (self loop?) 0)
				(== (antSound loop?) 0)
				(== (self cel?) 0)
				(!= (self script?) fightAntwerp)
			)
			(antSound loop: 1 play:)
		)
		(super doit:)
	)
	
	(method (handleEvent event)
		(switch (event type?)
			(mouseDown
				(if (MouseClaimed antwerp event emRIGHT_BUTTON)
					(HighPrint 84 38)
					;By golly, it's an Antwerp!
				)
			)
			(evSAID
				(cond 
					((super handleEvent: event))
					(
						(Said
							'play/[antwerp,monster,creature,bouncer,animal,beast]'
						)
						(HighPrint 84 39)
						;The Antwerp plays rough.
					)
				)
			)
		)
	)
	
	(method (getHurt)
		(HighPrint 84 37)
		;That's funny.  It bounced right off.
	)
)

(instance bounceAndLook of Script
	(properties)
	
	(method (doit)
		(cond 
			(
				(and
					(< (- (antwerp x?) maxX) (ego x?))
					(< (ego x?) (+ (antwerp x?) maxX))
					(< (- (antwerp y?) maxY) (ego y?))
					(< (ego y?) (+ (antwerp y?) maxY))
				)
				(antwerp cycleSpeed: 0 moveSpeed: 0)
				(client setScript: antwerpFollow)
			)
			(
				(or
					(and
						(< (antwerp x?) (ego x?))
						(!= (antwerp loop?) 0)
					)
					(and
						(> (antwerp x?) (ego x?))
						(!= (antwerp loop?) 1)
					)
				)
				(self changeState: 0)
			)
		)
		(super doit:)
	)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(antSound number: (GetSongNumber 4) play:)
				(if (< (antwerp x?) (ego x?))
					(antwerp
						loop: 0
						cycleSpeed: 1
						moveSpeed: 1
						setCycle: EndLoop self
					)
				else
					(antwerp
						loop: 1
						cycleSpeed: 1
						moveSpeed: 1
						setCycle: EndLoop self
					)
				)
			)
			(1 (self changeState: 0))
		)
	)
)

(instance antwerpFollow of Script
	(properties)
	
	;CI: This special doit is included in an attempt to debug the existing 
	(method (doit)
		;first set some conditions, then test what happens.
		;(antwerp x: 114 y: 131)
		;(ego x: 68 y: 137)
		;(Printf {bounceCounter == %d} bounceCounter)
		;(Printf 
		;	{antwerp x/y == %d/%d\nego x/y == %d/%d\n\nbounceCounter == %d}
		;	(antwerp x?) (antwerp y?)
		;	(ego x?) (ego y?)
		;	bounceCounter
		;)
		(self doit2:)
		;(Printf 
		;	{state == %d\ndX/dY == %d/%d} 
		;	(self state?) 
		;	dX dY
		;)
	)
	
	;CI: This is a manual decompilation of the doit method.
	(method (doit2)
		;how far from the hero is the antwerp?
		(= dX (- (ego x?) (antwerp x?)))
		(= dY (- (ego y?) (antwerp y?)))
		
		;negative dY means hero is higher than the antwerp. positive means lower.
		;negative dX means hero is to the left of the antwerp, positive means right.
		
		;if the hero is ±45 pixels x or ±8 pixels y from the antwerp, 
		;the antwerp will respond by pushing the hero.
		(if (and (< -45 dX) (< dX 45) 
				 (< -8 dY)  (< dY 8))
			(antwerp cycleSpeed: 1 moveSpeed: 1)
			(client setScript: antwerpPushes)
		)
		
		;then the antwerp will change state, depending on how far it is from the hero
		;maxX = 90
		;maxY = 40
		;minX 	 =  20		;never changes.
		;antCageX =  114	;never changes. X position of optimal antwerp placement (for fighting, maybe? bouncing out of there?)
		;antCageY =  96		;never changes. Y position of optimal antwerp placement (for fighting, maybe? bouncing out of there?)
		;bounceCounterInit =  6		;never changes. Initial value for bounceCounter. Gets reset here when bounceCounter==0

		(if (not (-- bounceCounter))
			(cond
				;condition 1) delta is not within the max range
				((not (and (< (- maxX) dX) (< dX maxX)
						   (< (- maxY) dY) (< dY maxY)
					  )
				 )
					;antwerp goes back to his home spot
					(self changeState: 7)
				)
				;((and (not (< (- maxX) dX) (< dX maxX))
					  ;(not (< (- maxY) dY) (< dY maxY))
				 ;)
				;	(self changeState: 7)
				;)
				;condition 2) 
				((and (< (- (* maxY dX) (* minX dY)) 0) ;[40 * dX] - [20 * dY] < 0
					  (< (+ (* maxY dX) (* minX dY)) 0) ;[40 * dX] + [20 * dY] < 0
				 )
				 	;antwerp goes home, from the right
				 	(self changeState: 1)
				)
				;condition 3)
				;CI: Here's the trouble state. we're always hitting this condition.
				;((and (> (- (* maxY dX) (* minX dY)) 0) ;[40 * dX] - [20 * dY] > 0
				((and (< (- (* maxY dX) (* minX dY)) 0) ;[40 * dX] - [20 * dY] < 0
					  (< 0 dX))							; 0 < dX
					;antwerp moves closer to the hero, upwards from the right
					(self changeState: 2)
				)
				;condition 4)
				((< 0 (+ (* maxY dX) (* minX dY)))	; 0 < [40 * dX] + [20 * dY]
					;antwerp moves closer to the hero, upwards from the left
					(self changeState: 3)
				)
				;condition 5)
				((> (- (* maxY dX) (* minX dY)) 0)	; [40 * dX] - [20 * dY] > 0
					;antwerp goes home, from the left
					(self changeState: 4)
				)
				;condition 6)
				((and (< (- (* maxY dX) (* minX dY)) 0) ; [40 * dX] - [20 * dY] < 0
					  (> dX 0))							; dX > 0
					;antwerp moves closer to the hero, downwards from the left
					(self changeState: 5)
				)
				;condition else)
				(else
					;antwerp moves closer to the hero, downwards from the right
					(self changeState: 6)
				)
			)
		)

		
		(if (not bounceCounter)
			(= bounceCounter bounceCounterInit)
		)
		
		(super doit:)
	)
	
	;CI: This method was unable to be automatically decompiled with SCICompanion.
	(method (doit0)
		(asm
			;first define the two dX and dY variables
			pushi    #x
			pushi    0
			lag      ego
			send     4
			push    			;push (ego x?) onto stack

			pushi    #x
			pushi    0
			lofsa    antwerp
			send     4			;acc = (antwerp x?)

			sub     			;acc = pop - acc		==> acc = (- (ego x?) (antwerp x?))
			sal      dX			;(= dX (- (ego x?) (antwerp x?)))

			pushi    #y
			pushi    0
			lag      ego
			send     4
			push    			;push (ego y?) onto stack

			pushi    #y
			pushi    0
			lofsa    antwerp
			send     4			;acc = (antwerp y?)

			sub     			;acc = pop - acc		==> acc = (- (ego y?) (antwerp y?))
			sal      dY			;(= dY (- (ego y?) (antwerp y?)))
			
			;now check if we're too close to the antwerp... if we are it means he pushes back.
			pushi    65491			;-45...  push -45 onto stack
			lal      dX				;load dX into acc
			lt?     				;acc = pop() < acc ==> (< -45 dX)
			bnt      code_0bf7		;go to start of cond loop
			pprev   
			ldi      45
			lt?     				;and (< dX 45)
			bnt      code_0bf7		;go to start of cond loop
			pushi    65528			;-8
			lal      dY
			lt?     				;and (< -8 dY)
			bnt      code_0bf7		;go to start of cond loop
			pprev   
			ldi      8
			lt?     				;and (< dY 8)
			bnt      code_0bf7		;go to start of cond loop

			pushi    #cycleSpeed
			pushi    1
			pushi    1
			pushi    #moveSpeed
			pushi    1
			pushi    1
			lofsa    antwerp		
			send     12				;(antwerp cycleSpeed: 1 moveSpeed: 1)

			pushi    #setScript
			pushi    1
			lofsa    antwerpPushes
			push    
			pToa     client			
			send     6				;(client setScript: antwerpPushes)


;only do conditions if --bounceCounter is not true
code_0bf7:
			-al      bounceCounter			;(-- bounceCounter)
			bnt      code_0bff
			jmp      code_0ce6		;if bounceCounter is TRUE (i.e. non-zero) then leave conditions


;;;start the conditions here:
;condition 1
code_0bff:
			lal      maxX			;acc = maxX
			neg     				;acc = -maxX
			push    				;push -maxX to stack
			lal      dX				;acc = dX
			lt?     				;acc = pop < acc ==> (< -maxX dX)
			bnt      code_0c0d
			pprev   				;push dX to stack
			lal      maxX			;acc = maxX
			lt?     				;acc = pop < acc ==> (< dX maxX)
code_0c0d:
			not     				;(not (and ((< -maxX dX) (< dX maxX))))
			bt       code_0c23		;skip to (self changeState: 7)
			lal      maxY			;acc = maxY
			neg     				;acc = -maxY
			push    				;push -maxY to stack
			lal      dY				;acc = dY
			lt?     				;acc = pop < acc ==> (< -maxY dY)
			bnt      code_0c1f		;if not true, then go to next condition
			pprev   				;push dY to stack
			lal      maxY			;acc = maxY
			lt?     				;acc = pop < acc ==> (< dY maxY)
code_0c1f:
			not     
			bnt      code_0c2d
code_0c23:
			pushi    #changeState
			pushi    1
			pushi    7
			self     6				;(self changeState: 7)
			jmp      code_0ce6		;goto final bit

;condition 2
code_0c2d:
			lsl      dX			;push dX onto stack
			lal      maxY		;acc = maxY
			mul     			;acc = pop * acc	==> acc = maxY * dX
			push    			;push (* maxY dX) onto stack
			lsl      dY
			lal      minX
			mul     			;acc = pop * acc 	==> acc = minX * dY
			sub     			;acc = pop - acc 	==> acc = (* maxY dX) - (* minX dY)
			push    			;push (- (* maxY dX) (* minX dY)) onto stack.
			ldi      0
			lt?     			;acc = pop < acc    ==> pop < 0		==> (< (- (* maxY dX) (* minX dY)) 0)
			bnt      code_0c5c	;if false, skip to next condition

			lsl      dY			;push dY onto stack
			lal      minX		;acc = minX
			mul     			;acc = (* minX dY)
			push    			;push (* minX dY) onto stack
			lsl      dX			;push dX onto stack
			lal      maxY		;acc = maxY
			mul     			;acc = (* maxY dX)
			add     			;acc = acc + pop 	==> (+ (* maxY dX) (* minX dY))
			push    			;push (+ (* maxY dX) (* minX dY)) onto stack
			ldi      0			
			lt?     			;acc = pop < acc 	==> pop < 0		==> (< (+ (* maxY dX) (* minX dY)) 0)
			bnt      code_0c5c	;if false, skip to next condition

			pushi    #changeState
			pushi    1
			pushi    1
			self     6				;(self changeState: 1)
			jmp      code_0ce6		;goto final bit

;condition 3
code_0c5c:
			lsl      dX				;push dX onto stack
			lal      maxY
			mul     				;acc = (* maxY dX)
			push    				
			lsl      dY
			lal      minX
			mul     				;acc = (* minX dY)
			sub     				;acc = pop - acc 	==> (- (* maxY dX) (* minX dY))
			push    
			ldi      0
			gt?     				;acc = pop > acc	==> (> (- (* maxY dX) (* minX dY)) 0)
			bnt      code_0c80
			lsl      dX
			ldi      0
			lt?     				;(< 0 dX)
			bnt      code_0c80

			pushi    #changeState
			pushi    1
			pushi    2
			self     6				;(self changeState 2)
			jmp      code_0ce6		;goto final bit

;condition 4
code_0c80:
			lsl      dY
			lal      minX
			mul     				;acc = (* minX dY)
			push    
			lsl      dX		
			lal      maxY
			mul     				;acc = (* maxY dX)
			add     				;acc = (+ (* maxY dX) (* minX dY))
			push    
			ldi      0
			lt?     				;acc = (< 0 (+ (* maxX dY) (* minX dY)))
			bnt      code_0c9d
			
			pushi    #changeState
			pushi    1
			pushi    3
			self     6				;(self changeState: 3)
			jmp      code_0ce6		;goto final bit

;condition 5
code_0c9d:
			lsl      dX
			lal      maxY
			mul     				;acc = (* maxY dX)
			push    
			lsl      dY
			lal      minX
			mul     				;acc = (* minX dY)
			sub     				;acc = pop - acc 	==> (- (* maxY dX) (* minX dY))
			push    
			ldi      0
			gt?     				;acc = pop > acc 	==> (> (- (* maxY dX) (* minX dY)) 0)
			bnt      code_0cba

			pushi    #changeState
			pushi    1
			pushi    4
			self     6				;(self changeState: 4)
			jmp      code_0ce6		;goto final bit

;condition 6
code_0cba:
			lsl      dX
			lal      maxY
			mul     				;acc = (* maxY dX)
			push    
			lsl      dY
			lal      minX
			mul     				;acc = (* minX dY)
			sub     				;acc = pop - acc	==> (- (* maxY dX) (* minX dY))
			push    
			ldi      0
			lt?     				;acc = pop < acc	==> (< (- (* maxY dX) (* minX dY)) 0)
			bnt      code_0cdf
			lsl      dX
			ldi      0
			gt?     				;and 	==> (> dX 0)
			bnt      code_0cdf		;(and (< (- (* maxY dX) (* minX dY)) 0) (> dX 0))
			
			pushi    #changeState
			pushi    1
			pushi    5
			self     6				;(self changeState: 5)
			jmp      code_0ce6		;goto final bit

;condition else
code_0cdf:
			pushi    #changeState
			pushi    1
			pushi    6
			self     6				;(self changeState: 6)

;final bit
code_0ce6:
			lal      bounceCounter			;==> acc = bounceCounter
			not     						;==> acc = not(acc) ==> not(bounceCounter)
			bnt      code_0cf0				;==> if (not bounceCounter)
			lal      bounceCounterInit
			sal      bounceCounter			;(= bounceCounter bounceCounterInit)

code_0cf0:
			pushi    #doit
			pushi    0
			super    Script,  4			;(super doit:)
			ret     
		)
	)
	
	(method (changeState newState)
		(switch (= state newState)
			(0)
			(1 ;antwerp go home from the right to the left, if he can.
				(if
					;ego is within the antwerp's home spot (x114±90, y= 96±40)
					(and
						(< (- antCageX maxX) (ego x?)) (< (ego x?) (+ antCageX maxX))
						(< (- antCageY maxY) (ego y?)) (< (ego y?) (+ antCageY maxY))
					)
					(cond 
						((<= (antwerp x?) antCageX)
							(antwerp
								setLoop: loopW
								setCycle: Forward
								setMotion: MoveTo antCageX (ego y?)
							)
						)
						((<= (/ (+ (antwerp x?) (ego x?)) 2) antCageX)
							(antwerp
								setLoop: loopW
								setCycle: Forward
								setMotion: MoveTo antCageX (ego y?)
							)
						)
						(else
							(antwerp
								setLoop: loopW
								setCycle: Forward
								setMotion: MoveTo (+ (antwerp x?) (ego x?)) (ego y?)
							)
						)
					)
				else
				;send the antwerp to his home spot.
					(antwerp
						setLoop: loopW
						setCycle: Forward
						setMotion: MoveTo antCageX antCageY
					)
				)
			)
			(2	;move closer to the hero, from the right to the left (upwards)
				(antwerp
					setLoop: loopW
					setCycle: Forward
					setMotion:
						MoveTo
						(+ (antwerp x?) (/ dY 2))
						(- (antwerp y?) (/ dY 2))
				)
			)
			(3	;move closer to the hero, from the left to the right (upwards)
				(antwerp
					setLoop: loopE
					setCycle: Forward
					setMotion:
						MoveTo
						(- (antwerp x?) (/ dY 2))
						(- (antwerp y?) (/ dY 2))
				)
			)
			(4	;antwerp go home from the left to the right, if he can
				(if 
					;ego is within the antwerp's home spot (x114±90, y= 96±40)
					(and
						(< (- antCageX maxX) (ego x?)) (< (ego x?) (+ antCageX maxX))
						(< (- antCageY maxY) (ego y?)) (< (ego y?) (+ antCageY maxY))
					)
					(cond 
						((>= (antwerp x?) antCageX)
							(antwerp
								setLoop: loopE
								setCycle: Forward
								setMotion: MoveTo antCageX (ego y?)
							)
						)
						((>= (/ (+ (antwerp x?) (ego x?)) 2) antCageX)
							(antwerp
								setLoop: loopE
								setCycle: Forward
								setMotion: MoveTo antCageX (ego y?)
							)
						)
						(else
							(antwerp
								setLoop: loopE
								setCycle: Forward
								setMotion: MoveTo (+ (antwerp x?) (ego x?)) (ego y?)
							)
						)
					)
				else
					(antwerp
						setLoop: loopE
						setCycle: Forward
						setMotion: MoveTo antCageX antCageY
					)
				)
			)
			(5	;move closer to hero, from the left to the right (downwards)
				(antwerp
					setLoop: loopE
					setCycle: Forward
					setMotion:
						MoveTo
						(- (antwerp x?) (/ dY 2))
						(+ (antwerp y?) (/ dY 2))
				)
			)
			(6	;move closer to the hero, from the right to the left (downwards)
				(antwerp
					setLoop: loopW
					setCycle: Forward
					setMotion:
						MoveTo
						(+ (antwerp x?) (/ dY 2))
						(+ (antwerp y?) (/ dY 2))
				)
			)
			(7	;move to home spot
				(antwerp
					setLoop: loopE
					setCycle: Forward
					setMotion: MoveTo antCageX antCageY self
				)
			)
			(8
				(client setScript: bounceAndLook)
			)
		)
	)
)

(instance antwerpPushes of Script
	(properties)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(HandsOff)
				(if (< (ego x?) (antwerp x?))
					(ego
						ignoreActors:
						setMotion: MoveTo (- (antwerp x?) 30) (antwerp y?) self
					)
				else
					(ego
						ignoreActors:
						setMotion: MoveTo (+ (antwerp x?) 30) (antwerp y?) self
					)
				)
			)
			(1
				(if (< (ego x?) (antwerp x?))
					(antwerp
						setLoop: 1
						cel: 0
						setCycle: Forward
						setMotion: MoveTo (+ (ego x?) 21) (antwerp y?) self
					)
					(ego
						view: (GetEgoViewNumber vEgoDaggerDefeated)
						setLoop: loopS
						cycleSpeed: 1
						setCycle: EndLoop
						setMotion: MoveTo (- (ego x?) 10) (ego y?)
					)
				else
					(antwerp
						setLoop: 0
						cel: 0
						setCycle: Forward
						setMotion: MoveTo (- (ego x?) 21) (antwerp y?) self
					)
					(ego
						view: (GetEgoViewNumber vEgoDaggerDefeated)
						setLoop: loopN
						cycleSpeed: 1
						setCycle: EndLoop
						setMotion: MoveTo (+ (ego x?) 10) (ego y?)
					)
				)
				(antHits play:)
			)
			(2
				(if (not (TakeDamage 10))
					(self changeState: 5)
				else
					(StopEgo)
					(if (not (Btst TROLL_DOOR_OPEN))
						(ego illegalBits: (| cWHITE cBROWN))
					)
					(= dX 45)
					(if (< (ego x?) (antwerp x?))
						(if (> 226 (+ (antwerp x?) dX))
							(= dX (- 226 (antwerp x?)))
						)
						(antwerp
							setLoop: 1
							cel: 0
							setCycle: Forward
							setMotion: MoveTo (+ (antwerp x?) dX) (antwerp y?) self
						)
						(ego loop: loopE)
					else
						(if (> 82 (- (antwerp x?) dX))
							(= dX (- (antwerp x?) 82))
						)
						(antwerp
							setLoop: 0
							cel: 0
							setCycle: Forward
							setMotion: MoveTo (- (antwerp x?) dX) (antwerp y?) self
						)
						(ego loop: loopW)
					)
					(HandsOn)
				)
			)
			(3 
				(= seconds 3)
			)
			(4
				;stop moving the Antwerp
				(antwerp cycleSpeed: 0 moveSpeed: 0)
				;and reset the script back to Antwerp Follow.
				(client setScript: antwerpFollow)
			)
			;only go here if we run out of HP.
			(5
				(= dX 25)
				(if (< (ego x?) (antwerp x?))
					(if (> 226 (+ (antwerp x?) dX))
						(= dX (- 226 (antwerp x?)))
					)
					(antwerp
						setLoop: 1
						cel: 0
						setCycle: Forward
						setMotion: MoveTo (+ (antwerp x?) dX) (antwerp y?)
					)
					(ego loop: loopS)
				else
					(if (> 82 (- (antwerp x?) dX))
						(= dX (- (antwerp x?) 82))
					)
					(antwerp
						setLoop: 0
						cel: 0
						setCycle: Forward
						setMotion: MoveTo (- (antwerp x?) dX) (antwerp y?)
					)
					(ego loop: loopW)
				)
				(ego
					view: (GetEgoViewNumber vEgoDefeated)
					setLoop: loopE
					cel: 0
					cycleSpeed: 3
					setCycle: EndLoop self
				)
			)
			(6
				(EgoDead DIE_RETRY DIE_ANTWERP_BOUNCED 84 40
					#icon (GetEgoViewNumber vEgoClimbing) 2 5
					#title {Your figure remains still and silent.}
					;The old ticker just couldn't keep going.  Maybe you shouldn't have missed the annual visit with your local Healer.
				)
				;reset ego
				(ChangeGait MOVE_NOCHANGE FALSE)
				;set him to the default position, for a north-bound entrance.
				(ego posn: 198 75 loop: loopW)
				;restore the Antwerp to his starting position
				(antwerp init: posn: 114 96 loop: 3 cycleSpeed: 1 xStep: 4 moveSpeed: 1 setScript: bounceAndLook)
				(= [egoStats HEALTH] egoStartHP)
			)
		)
	)
)

(instance fightAntwerp of Script
	(properties)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(HandsOff)
				(TimedPrint 5 84 41)
				;Cockily, you loosen up to fight.
				(antwerp setMotion: MoveTo 114 96)
				(ego ignoreActors: setMotion: MoveTo 250 100 self)
			)
			(1
				(ego
					view: (GetEgoViewNumber vEgoBeginFight)
					setLoop: (if fightWithSword 0 else 2)
					cel: 0
					cycleSpeed: 2
					setCycle: EndLoop self
				)
			)
			(2
				(if fightWithSword
					(ego view: (GetEgoViewNumber vEgoFightWithSword) setLoop: 2)
				else
					(ego view: (GetEgoViewNumber vEgoFightDaggerNoCape) setLoop: 5)
				)
				(ego cel: 0 setCycle: EndLoop self)
			)
			(3
				(ego
					cel: 0
					setLoop: (if fightWithSword 6 else 1)
					setCycle: EndLoop self
				)
			)
			(4
				(ego
					cel: 0
					setLoop: (if fightWithSword 3 else 0)
					setCycle: EndLoop self
				)
			)
			(5
				(ego
					cel: 0
					setLoop: (if fightWithSword 7 else 0)
					setCycle: EndLoop self
				)
			)
			(6
				(if practiceSwings
					(-- practiceSwings)
					(self changeState: 2)
				else
					(= practiceSwings 4)
					(= cycles 4)
				)
			)
			(7
				(ego
					cel: 0
					setLoop: (if fightWithSword 4 else 3)
					setCycle: EndLoop self
				)
			)
			(8
				(if practiceSwings
					(-- practiceSwings)
					(self changeState: 7)
				else
					(ego
						view: (GetEgoViewNumber vEgoBeginFight)
						setLoop: (if fightWithSword 0 else 3)
						cel: 5
						setCycle: BegLoop self
					)
				)
			)
			(9
				(TimedPrint 4 84 42)
				;Confident and loose, you approach the Antwerp.
				(StopEgo)
				(ego
					setLoop: 1
					moveSpeed: 1
					cycleSpeed: 1
					ignoreActors:
					setMotion: MoveTo 158 96 self
				)
			)
			(10
				(ego
					view: (GetEgoViewNumber vEgoBeginFight)
					setLoop: (if fightWithSword 0 else 2)
					cel: 0
					moveSpeed: 0
					cycleSpeed: 0
					setCycle: EndLoop self
				)
			)
			(11
				(antwerp
					setLoop: 0
					setCycle: CycleTo 2 cdFORWARD
					setStep: 1 5
					setMotion: MoveTo 114 0
				)
				(antSound number: (GetSongNumber 4) play:)
				(= cycles 13)
			)
			(12
				(antSound stop:)
				(if fightWithSword
					(ego view: (GetEgoViewNumber vEgoFightWithSword) setLoop: 3)
				else
					(ego view: (GetEgoViewNumber vEgoFightDaggerNoCape) setLoop: 5)
				)
				(ego cel: 0 setCycle: EndLoop self)
			)
			(13
				(ego loop: (if fightWithSword 1 else 8) cel: 0)
				(antwerp setCycle: EndLoop setMotion: MoveTo 114 96 self)
			)
			(14
				(antwerp setCycle: Forward)
				(= cycles 10)
			)
			(15
				(antwerp
					yStep: 7
					setCycle: CycleTo 2 cdFORWARD
					ignoreActors: 1
					ignoreHorizon:
					illegalBits: 0
					setMotion: MoveTo 114 0
				)
				(antSound number: (GetSongNumber 5) play:)
				(= cycles 10)
			)
			(16
				(antSound stop:)
				(if fightWithSword
					(ego view: (GetEgoViewNumber vEgoFightWithSword) setLoop: 3)
				else
					(ego view: (GetEgoViewNumber vEgoFightDaggerNoCape) setLoop: 5)
				)
				(ego cel: 0 setCycle: EndLoop self)
			)
			(17 (= cycles 16))
			(18
				(antwerp setMotion: MoveTo 114 96 self)
				(ego loop: (if fightWithSword 1 else 8) cel: 0)
			)
			;CI: NOTE: Restored missing antwerp music number
			(19
				(antSound number: (GetSongNumber 9) play:)
				(antwerp setCycle: CycleTo 2 cdFORWARD setMotion: MoveTo 114 92 self)
			)
			(20
				(antSound stop:)
				(antwerp setCycle: BegLoop self)
			)
			(21
				(antSound number: (GetSongNumber 6) play:)
				(antwerp yStep: 10 setMotion: MoveTo 114 -10)
				(= cycles 10)
			)
			(22
				(antSound stop:)
				(if fightWithSword
					(ego view: (GetEgoViewNumber vEgoFightWithSword) setLoop: 5 cel: 0)
				else
					(ego view: (GetEgoViewNumber vEgoFightDaggerNoCape) setLoop: 5)
				)
				(ego cel: 0 cycleSpeed: 1 setCycle: EndLoop self)
			)
			(23
				(ego
					view: (GetEgoViewNumber vEgoBeginFight)
					setLoop: (if fightWithSword 0 else 3)
					cel: (if fightWithSword 5 else 2)
					setCycle: BegLoop self
				)
			)
			(24
				(StopEgo)
				(if (not (Btst TROLL_DOOR_OPEN)) (ego illegalBits: (| cWHITE cBROWN)))
				(Bset ANTWERP_SKY)
				(HandsOn)
				(TimedPrint 3 84 43)
				;"Holy Mackerel!"
			)
		)
	)
)

(instance antSound of Sound
	(properties
		priority 5
	)
)

(instance antHits of Sound
	(properties
		number 54
		priority 6
	)
)

(instance rock of Actor
	(properties
		y 89
		x 18
		yStep 1
		view vSecretEntranceRock
		xStep 1
	)
)

(instance sMoveRock of Script
	(properties)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(HandsOff)
				(ego
					view: (GetEgoViewNumber vEgoThrowing)
					setLoop: 1
					cel: 0
					cycleSpeed: 2
					setCycle: CycleTo 2 cdFORWARD self
				)
			)
			(1
				(rock
					setCycle: 0
					startUpd:
					illegalBits: 0
					xStep: 3
					setMotion: MoveTo -18 89 self
				)
				(ego setCycle: BegLoop)
			)
			(2
				(ChangeGait MOVE_WALK 0)
				(ego illegalBits: cWHITE)
				(HandsOn)
				(SolvePuzzle POINTS_FINDSECRETENTRANCE 10)
				(Bset TROLL_DOOR_OPEN)
				(rock dispose:)
			)
		)
	)
)

(instance sMagicRock of Script
	(properties)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(HandsOff)
				(ego
					view: (GetEgoViewNumber vEgoMagicDetect)
					illegalBits: 0
					setLoop: 0
					cycleSpeed: 1
					setCycle: EndLoop self
				)
			)
			(1
				(Bset TROLL_DOOR_UNLOCKED)
				(ChangeGait MOVE_WALK 0)
				(if (< [egoStats OPEN] tryOpenAntwerpRock)
					(HighPrint 84 44)
					;Your spell has unlocked the lock on the rock, but it is not yet powerful enough to open the rock door.
					(HandsOn)
					(StopEgo)
					(ego illegalBits: (| cWHITE cBROWN))
					(client setScript: NULL)
				else
					(rock
						setCycle: 0
						startUpd:
						illegalBits: 0
						setMotion: MoveTo -18 89 self
					)
				)
			)
			(2
				(SolvePuzzle POINTS_FINDSECRETENTRANCE 10)
				(Bset TROLL_DOOR_OPEN)
				(rock dispose:)
				(StopEgo)
				(HandsOn)
			)
		)
	)
)

(instance bareHandAttack of Script
	(properties)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(ego setMotion: MoveTo (antwerp x?) (antwerp y?) self)
			)
			(1 (client setScript: NULL))
		)
	)
)

(instance antwerpAway of Script
	(properties)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(= antwerpOnScreen FALSE)
				(Bset ANTWERP_SKY)
				(antwerp setCycle: EndLoop self)
			)
			(1
				(antwerp
					setLoop: 2
					cel: 0
					ignoreHorizon:
					illegalBits: 0
					setPri: (antwerp priority?)
					setStep: 4 18
					setCycle: BegLoop
					setMotion: MoveTo 200 -10 self
				)
			)
			(2
				(HighPrint 84 45)
				;You seem to have scared the Antwerp with your behavior.
				)
		)
	)
)

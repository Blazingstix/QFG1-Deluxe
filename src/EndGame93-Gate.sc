;;; Sierra Script 1.0 - (do not remove this comment)
(script# 93)
(include system.sh) (include sci2.sh) (include game.sh)
(use Main)
(use ThrowFlameDart)
(use ThrowDagger1)
(use ThrowRock)
(use AnimateCalm)
(use AnimateOpen)
(use AnimateDazzle)
(use TargActor)
(use _LoadMany)
(use _GControl)
(use _Sound)
(use _Motion)
(use _Game)
(use _Inventory)
(use _Actor)
(use _System)

(public
	rm93 0
	minotaur 1
	gate 2
	openGate 3
	bell 4
	archer1 5
	archer2 6
	archer3 7
	archer4 8
)

(local
	enteredViaCave
	local1
	local2
	minotaurState
	local4
	spellCast
	local6
	local7
	minotaurSight =  5
	local9
	searchedMinotaur
	minotaurMutter
)

(enum ;minotaur States
	mino0
	mino1
	mino2
	mino3
)

(procedure (MinotaurSeesEgo &tmp ret)
	(= ret TRUE)
	(switch minotaurState
		(mino0
			(if (< (+ (minotaur x?) minotaurSight) (ego x?))
				(= ret FALSE)
			)
		)
		(mino1
			(if (< (ego x?) (- (minotaur x?) minotaurSight))
				(= ret FALSE)
			)
		)
		(mino2
			(if (< (+ (minotaur y?) minotaurSight) (ego y?))
				(= ret FALSE)
			)
		)
		(else 
			(if (< (ego y?) (- (minotaur y?) minotaurSight))
				(= ret FALSE)
			)
		)
	)
	(return ret)
)

(procedure (MinotaurHearsEgo)
	(return
		(not
			(if
				(or
					local6
					local7
					local9
					(ego inRect: 0 120 31 130)
					(ego inRect: 238 0 330 106)
					(ego inRect: 61 153 82 154)
					(and
						(== egoGait MOVE_SNEAK)
						(>= [egoStats STEALTH] trySneakMinotaurBush)
						(MinotaurSeesEgo)
					)
					(== (minotaur script?) minotaurLooks)
				)
			else
				(Btst DEFEATED_MINOTAUR)
			)
		)
	)
)

(instance rm93 of Room
	(properties
		picture 93
		style DISSOLVE
	)
	
	(method (init)
		;this is a retry event, so let's reset positions.
		(if (and (== prevRoomNum MINOTAUR) (== monsterInRoom NULL))
			(cond
				((Btst ENTERROOM_WEST)
					(= prevRoomNum 89)
				)
				((Btst ENTERROOM_EAST)
					(= prevRoomNum 91)
				)
				(else
					(= prevRoomNum 94)
				)
			)
		)

		(LoadMany RES_VIEW vBrigandEntrance (GetEgoViewNumber vEgoBreathHeavy) (GetEgoViewNumber vEgoRunning) (GetEgoViewNumber vEgoFall) (GetEgoViewNumber vEgoThrowing) (GetEgoViewNumber vEgoFallDown) vMinotaurDefeated (GetEgoViewNumber vEgoDefeated) (GetEgoViewNumber vEgoThrowDagger))
		(Load RES_SOUND 23)
		(cSound priority: 1 number: 23 loop: -1 play:)
		(super init:)
		(mouseDownHandler add: self)
		(self
			setFeatures: onBell onSign onDoor onBush onRock onFort
		)
		(StatusLine enable:)
		(StopEgo)
		(HandsOff)
		(gateSign setPri: 6 init: addToPic:)
		(bell setPri: 6 init: stopUpd:)
		(bush init: stopUpd:)
		(if (Btst BRIGAND_GATE_OPEN)
			(gate setCel: 3 setPri: 6 ignoreActors: init: stopUpd:)
			(ego illegalBits: cWHITE)
		else
			(gate setCel: 0 setPri: 6 init: stopUpd:)
			(ego illegalBits: (| cWHITE cLRED))
		)
		(if (not (Btst DEFEATED_MINOTAUR))
			(= monsterInRoom MINOTAUR)
			(= gMonsterHealth MAX_HP_MINOTAUR)
		)
		
		(switch prevRoomNum
			(89 ;came from the cave
				(= enteredViaCave TRUE)
				(Bset ENTERROOM_WEST)
				(Bclr ENTERROOM_EAST)
				(if (not (Btst DEFEATED_MINOTAUR))
					(Load RES_VIEW vMinotaur)
					(= local4 1)
					(= minotaurState mino0)
					(minotaur illegalBits: 0 init: setScript: patrol)
				)
				(ego posn: 2 123 init: setScript: (ScriptID 271 0)) ;egoEntersWest
			)
			(94 ;came from inside the brigand complex
				(Bclr ENTERROOM_EAST)
				(Bclr ENTERROOM_WEST)
				(if (not (Btst DEFEATED_MINOTAUR))
					(Load RES_VIEW vMinotaur)
					(= local4 0)
					(= minotaurState mino1)
					(minotaur
						setLoop: 1
						posn: 145 162
						illegalBits: 0
						init:
						setScript: patrol
					)
				)
				(if (Btst BRIGAND_GATE_OPEN)
					(ego posn: 174 98 init: setScript: (ScriptID 272 0)) ;egoEntersNorth
				else
					(ego posn: 143 108 init: setScript: (ScriptID 275 0)) ;egoClimbsDown
				)
			)
			(MINOTAUR ;came from the arena, having just defeated the minotaur
				(Load RES_VIEW (GetEgoViewNumber vEgoDanceBow))
				(Load RES_VIEW vMinotaurDefeated)
				(Bset DEFEATED_MINOTAUR)
				(minotaur
					view: vMinotaurDefeated
					setLoop: 0
					setCel: 0
					init:
					posn: 230 145
				)
				(ego posn: 235 158 init: setScript: (ScriptID 274 0)) ;egoEntersFromCombat
			)
			(else  ;came from the west room (aka, brigand ambush)
				(Bset ENTERROOM_EAST)
				(Bclr ENTERROOM_WEST)
				(if (not (Btst DEFEATED_MINOTAUR))
					(Load RES_VIEW vMinotaur)
					(= local4 0)
					(= minotaurState mino1)
					(minotaur illegalBits: 0 init: setScript: patrol)
				)
				(ego posn: 235 188 init: setScript: (ScriptID 273 0)) ;egoEntersSouth
			)
		)
	)
	
	(method (doit)
		(cond 
			((and local1 (== (ego edgeHit?) WEST))
				(Bset VISITED_BRIGAND_GATE)
				(= monsterInRoom (= gMonsterHealth 0))
				(curRoom newRoom: 89)
			)
			((and local1 (& (ego onControl: origin) cLRED))
				(Bset VISITED_BRIGAND_GATE)
				(= monsterInRoom (= gMonsterHealth 0))
				(curRoom newRoom: 94)
			)
			(
			(or (== (ego edgeHit?) SOUTH) (== (ego edgeHit?) EAST))
				(Bset VISITED_BRIGAND_GATE)
				(= monsterInRoom (= gMonsterHealth 0))
				(curRoom newRoom: 91)
			)
		)
		(super doit:)
	)
	
	(method (dispose)
		(mouseDownHandler delete: self)
		(super dispose:)
	)
	
	(method (handleEvent event)
		(switch (event type?)
			(mouseDown
				(cond 
					((super handleEvent: event))
					((MouseClaimed ego event emRIGHT_BUTTON)
						(HighPrint 93 0)
						;To the Minotaur, you look like a nice meal.
						)
				)
			)
			(evSAID
				(cond 
					((super handleEvent: event))
					((Said 'rest,nap')
						(EgoDead DIE_RETRY DIE_ENDGAME_REST 93 1
							#title {Z-Z-Z-Z-Z-Z-Z-Z-Z-Z}
							#icon (GetEgoViewNumber vEgoDeathScenes) 0 0)
							;The Brigands oblige you by making your pleasant rest permanent!
						)
						;nothing else needed to retry from this death.
					((Said 'look>')
						(cond 
							((Said '[<at,around][/place,area]')
								(HighPrint 93 2)
								;Sheer cliff walls form a narrow box canyon where the brigands have built a fortress.
								)
							((Said '[<at]/boulder')
								(HighPrint 93 3)
								;The rocks look to be good hiding places.
								)
							((Said '[<at]/cliff')
								(HighPrint 93 4)
								;The rock walls look steep.
								)
							((Said '[<at]/log,gate')
								(HighPrint 93 5)
								;The gate appears to be fastened somehow on the inside.
								)
							((or (Said '<up') (Said '/sky'))
								(HighPrint 93 6)
								;The cliff seems very hard to climb.
								)
							((or (Said '<down') (Said '/ground,grass'))
								(HighPrint 93 7)
								;You can see marks in the ground, leading towards the bush.
								)
							((Said '/bush')
								(if enteredViaCave
									(HighPrint 93 8)
									;You see a bush that conceals the secret entrance.
								else
									(HighPrint 93 9)
									;The bush sits next to the cliff.  There are marks on the ground near the bush.
								)
							)
							((Said '/west')
								(if enteredViaCave
									(HighPrint 93 10)
									;You see that where the steep cliff walls meet the fortress, there is a pile of gravel.
									;Once away from the secret passage exit, the fortress is entirely obscured by rocks and brush.
									else
									(HighPrint 93 11)
									;The cliff to the west looks too sheer to climb.  There is a bush growing close to the cliff.
									)
								)
							((Said '/north')
								(HighPrint 93 12)
								;You see what must be the log walls and gate of the brigand fortress.
								)
							((Said '/east')
								(HighPrint 93 13)
								;You see where the sheer cliff walls meet the fortress wall with a pile of rocks.
								)
							((Said '/south')
								(HighPrint 93 14)
								;The walls of the canyon turn to the west.  You can see a large Minotaur patrolling the canyon.
								)
							((Said '/bell')
								(HighPrint 93 15)
								;A warning bell to alert all the brigands in the fortress.
								)
							((Said '/sign')
								(if (ego inRect: 150 92 250 140)
									(HighPrint 93 16)
									;The sign reads "Ring bell".
								else
									(HighPrint 93 17)
									;Step over and get a close look.
								)
							)
						)
					)
					((Said 'cast>')
						(= spellCast (SaidCast event))
						;CI: NOTE: This was originally a bug where the spell was cast, *then* the code was checked if you could cast the spell
						;It was found by 8bitKittyKat on 8 AUG 2022.
						;see https://sciprogramming.com/community/index.php?topic=2076.msg15503
						;I have fixed the spell by switching on the spell the user typed, then trying to cast the spell
						;(if (CastSpell spellCast)
						(switch spellCast
							(DETMAGIC
								(if (CastSpell spellCast) ;CI: added
									(HighPrint 93 18)
									;There is no magical aura present.
								)
							)
							(DAZZLE
								(if (CastSpell spellCast) ;CI: added
									(cond 
										((or (Btst DEFEATED_MINOTAUR) local6 local9 local7)
											(HighPrint 93 19)
											;You waste a spell.
											)
										((AnimateDazzle ego minotaurDazzled)
											(minotaur setScript: minotaurDazzled)
										)
									)
								)
							)
							(FLAMEDART
								(cond 
									(
										(or
											(ego inRect: 47 141 90 163)
											(ego inRect: 0 119 35 126)
											(ego inRect: 238 0 330 106)
										)
										(HighPrint 93 20)
										;You'll have to give yourself room.
									)
									((Btst DEFEATED_MINOTAUR)
										(if (CastSpell spellCast) ;CI: added
											(AnimateThrowingFlameDart NULL)
										)
									)
									(else
										(if (CastSpell spellCast) ;CI: added
											(if (< (ego x?) 160)
												(minotaur targDeltaX: -25)
											else
												(minotaur targDeltaX: 25)
											)
											(AnimateThrowingFlameDart minotaur)
											(if (or local6 local7)
												(minotaur setScript: patrol)
											)
										)
									)
								)
							)
							(CALM
								(if (CastSpell spellCast) ;CI: added
									(cond 
										((or (Btst DEFEATED_MINOTAUR) local6)
											(HighPrint 93 19)
											;You waste a spell.
											)
										((AnimateCalm ego minotaurCalmed)
											(minotaur setScript: minotaurCalmed)
										)
									)
								)
							)
							(OPEN
								(if (CastSpell spellCast) ;CI: added
									(if (or local2 (Btst BRIGAND_GATE_OPEN))
										(HighPrint 93 19)
										;You waste a spell.
									else
										(AnimateOpenSpell ego openMess)
										(gate setScript: openMess)
									)
								)
							)
							(ZAP
								;bump the ZAP spell back to the main script.
								(event claimed: FALSE)
							)
							(else
								(HighPrint 93 19)
								;You waste a spell.
							)
						)
						;)
					)
					((Said 'throw/dagger,dagger')
						(cond 
							(
								(or
									(ego inRect: 47 141 90 163)
									(ego inRect: 0 119 35 126)
									(ego inRect: 238 0 330 106)
								)
								(HighPrint 93 20)
								;You'll have to give yourself room.
							)
							((Btst DEFEATED_MINOTAUR) (AnimateThrowingDagger 0))
							(else
								(if (< (ego x?) 160)
									(minotaur targDeltaX: -25)
								else
									(minotaur targDeltaX: 25)
								)
								(AnimateThrowingDagger minotaur)
								(if (or local6 local7)
									(minotaur setScript: patrol)
								)
							)
						)
					)
					((Said 'throw/boulder')
						(if
						(and (AnimateThrowingRock minotaur) (or local6 local7))
							(minotaur setScript: patrol)
						)
					)
					((Said 'get/boulder')
						(ego setScript: (ScriptID 103 0)) ;getRock
					)
					((Said 'climb')
						(cond 
							((ego inRect: 92 95 214 107)
								(HighPrint 93 21)
								;The logs of the wall are too slick, and there is no place to get a good hold on them.  You'll have to try somewhere else.
								)
							((ego inRect: 269 120 303 151)
								(HighPrint 93 22)
								;There is no good climbing place on the rock cliff to the east.
								)
							((and (< (ego x?) 97) (< (ego y?) 108))
								(ego setScript: (ScriptID 277 0))  ;climbWestCliff
							)
							((ego inRect: 238 0 330 106)
								(if (TrySkill CLIMB tryClimbBrigandGate)
									(ego setScript: (ScriptID 276 0)) ;egoClimbsUp
								else
									(HighPrint 93 23)
									;You don't have enough climbing skill to scale the wall.
								)
							)
							((ego inRect: 47 141 90 163)
								(HighPrint 93 24)
								;You can't climb the rock.
								)
							(else
								(HighPrint 93 25)
								;You're not in a good spot for climbing.
								)
						)
					)
					((Said 'open[/gate,door]')
						(cond 
							((Btst BRIGAND_GATE_OPEN)
								(HighPrint 93 26)
								;The gate is open.
								)
							((and local2 (ego inRect: 128 95 186 106))
								(ego setScript: openGate)
							)
							(local2
								(HighPrint 93 27)
								;You must get closer.
								)
							(else
								(HighPrint 93 28)
								;The gate appears to be fastened somehow on the inside.  You'll have to force the gate open.
								)
						)
					)
					((Said 'close,close[/gate,door]')
						(cond 
							((and (ego inRect: 128 95 186 106) (Btst BRIGAND_GATE_OPEN))
								(ego setScript: closeGate)
							)
							((Btst BRIGAND_GATE_OPEN)
								(HighPrint 93 27)
								;You must get closer.
								)
							(else
								(HighPrint 93 29)
								;The gate is closed.
								)
						)
					)
					((Said 'break,force,beat/gate,door')
						(cond 
							((Btst BRIGAND_GATE_OPEN)
								(HighPrint 93 30)
								;What for?
								)
							(
								(and
									(< 95 (ego x?))
									(< (ego x?) 230)
									(< (ego y?) 150)
								)
								(ego setScript: (ScriptID 278 0)) ;forceGate
							)
							(else
								(HighPrint 93 27)
								;You must get closer.
								)
						)
					)
					((Said 'knock/door,gate')
						(HighPrint 93 31)
						;Why knock?  Use the bell.
						)
					((or (Said 'look/sign') (Said 'read/sign'))
						(if (ego inRect: 150 92 250 140)
							(HighPrint 93 16)
							;The sign reads "Ring bell".
						else
							(HighPrint 93 17)
							;Step over and get a close look.
						)
					)
					((Said 'ring/bell')
						(if (ego inRect: 150 92 250 140)
							(bell setScript: (ScriptID 279 0)) ;ringBell
						else
							(HighPrint 93 32)
							;Unless you're Rubber Man you'll have to get closer.
						)
					)
				)
			)
		)
	)
	
	(method (notify param1)
		(switch param1
			(0 (= local1 1))
			(1 (= local1 0))
			(2 (= local9 1))
			(3 (= local9 0))
		)
	)
)

(instance gate of Prop
	(properties
		y 95
		x 129
		view vBrigandEntrance
	)
)

(instance bell of Prop
	(properties
		y 69
		x 197
		z 1
		view vBrigandEntrance
		loop 1
	)
)

(instance gateSign of Prop
	(properties
		y 57
		x 197
		z 1
		view vBrigandEntrance
		loop 2
	)
)

(instance bush of Prop
	(properties
		y 128
		x 23
		view vBrigandEntrance
		loop 3
	)
	
	(method (doit)
		(if
			(and
				;CI: TODO: convert this into a proper (TrySkill SNEAK 50 0)
				(not (if (== egoGait MOVE_SNEAK) (>= [egoStats STEALTH] trySneakMinotaurBush)))
				(ego inRect: 8 119 35 126)
				(== (bush script?) NULL)
			)
			(bush setScript: jerkBush)
		)
		(super doit:)
	)
)

(instance archer1 of Actor
	(properties
		y 57
		x 247
		view vBrigandEntrance
		loop 4
		priority 5
	)
)

(instance archer2 of Actor
	(properties
		y 56
		x 207
		view vBrigandEntrance
		loop 4
		priority 5
	)
)

(instance archer3 of Actor
	(properties
		y 56
		x 109
		view vBrigandEntrance
		loop 5
		priority 5
	)
)

(instance archer4 of Actor
	(properties
		y 56
		x 69
		view vBrigandEntrance
		loop 5
		priority 5
	)
)

(instance lockSound of Sound
	(properties
		number 35
		priority 3
	)
)
;EO: The following messages seem very basic. Were these strings intended as placeholders?
;CI: These messages are when right-clicking on signs, and are terse by design. We're only reading the brigands' signs.
(instance onBell of RFeature
	(properties
		nsTop 56
		nsLeft 193
		nsBottom 68
		nsRight 202
	)
	
	(method (handleEvent event)
		(cond 
			((super handleEvent: event))
			((MouseClaimed onBell event emRIGHT_BUTTON)
				(HighPrint 93 33)
				;Bell to alert gate guard.
				)
		)
	)
)

(instance onSign of RFeature
	(properties
		nsTop 48
		nsLeft 191
		nsBottom 55
		nsRight 201
	)
	
	(method (handleEvent event)
		(cond 
			((super handleEvent: event))
			((MouseClaimed onSign event emRIGHT_BUTTON)
				(HighPrint 93 34)
				;Sign reading "Ring Bell".
				)
		)
	)
)

(instance onDoor of RFeature
	(properties
		nsTop 38
		nsLeft 128
		nsBottom 95
		nsRight 186
	)
	
	(method (handleEvent event)
		(cond 
			((super handleEvent: event))
			((MouseClaimed onDoor event emRIGHT_BUTTON)
				(HighPrint 93 35)
				;Gate to fortress.  A very strong gate.
				)
		)
	)
)

(instance onBush of RFeature
	(properties
		nsTop 85
		nsLeft 9
		nsBottom 128
		nsRight 34
	)
	
	(method (handleEvent event)
		(cond 
			((super handleEvent: event))
			((MouseClaimed onBush event emRIGHT_BUTTON)
				(HighPrint 93 36)
				;Bush, a good place to hide.
				)
		)
	)
)

(instance onRock of RFeature
	(properties
		nsTop 125
		nsLeft 47
		nsBottom 159
		nsRight 94
	)
	
	(method (handleEvent event)
		(cond 
			((super handleEvent: event))
			((MouseClaimed onRock event emRIGHT_BUTTON)
				(HighPrint 93 37)
				;A good place to hide.
				)
		)
	)
)

(instance onFort of RFeature
	(properties
		nsBottom 24
		nsRight MAXRIGHT
	)
	
	(method (handleEvent event)
		(cond 
			((super handleEvent: event))
			((MouseClaimed onFort event emRIGHT_BUTTON)
				(HighPrint 93 38)
				;Brigands' fortress.
				)
		)
	)
)

(instance minotaur of TargActor
	(properties
		y 162
		x 60
		view vMinotaur
		targDeltaX 25
		targDeltaY -15
	)
	
	(method (doit)
		(if
			(and
				(MinotaurHearsEgo)
				(not (> (ego distanceTo: minotaur) 100))
			)
			(self setScript: minotaurLooks)
		)
		(super doit:)
	)
	
	(method (handleEvent event)
		(switch (event type?)
			(mouseDown
				(cond 
					((super handleEvent: event))
					((not (MouseClaimed self event emRIGHT_BUTTON)))
					((Btst DEFEATED_MINOTAUR)
						(HighPrint 93 39)
						;The Minotaur is out for the count.
						)
					(else
						(HighPrint 93 40)
						;The Minotaur seems to have very sensitive hearing.  He looks around every time the bush rustles.
						)
				)
			)
			(evSAID
				(cond 
					((Said 'search[/bull,body,flail]')
						(cond 
							(local6 
								(HighPrint 93 41)
								;As you attempt the search, the sleeping Minotaur awakes, and...
								(curRoom newRoom: MINOTAUR)
							)
							((Btst DEFEATED_MINOTAUR)
								(if searchedMinotaur
									(HighPrint 93 42)
									;You've got all the loot you're going to find.
								else
									(= searchedMinotaur TRUE)
									(if
										(and
											(== curRoomNum roomDaggersDropped)
											(or daggersInRoom daggersInMonster [invDropped iDagger])
										)
										(ego get: iDagger (+ daggersInRoom daggersInMonster [invDropped iDagger]))
										(HighPrint 93 43)
										;You retrieve your daggers.
									)
									(= [invDropped iDagger]
										(= daggersInMonster
											(= daggersInRoom (= roomDaggersDropped 0))
										)
									)
									(HighPrint 93 44)
									;You quickly search the fallen Minotaur and find nothing.
									(HighPrint 93 45)
									;Wait a minute.
									(HighPrint 93 46)
									;The Minotaur's flail looks peculiar.  You pick it up and examine it closely.
									(HighPrint 93 47)
									;Cleverly concealed in the flail are 50 silvers.
									(HighPrint 93 48)
									;"A just reward for defeating such a valiant fighter," you tell yourself as you pocket the silvers.
									(ego get: iSilver 50)
								)
							)
							(else
								(HighPrint 93 49)
								;That's not a good idea.
								)
						)
					)
					((Said 'look/bull,monster,creature')
						(if (Btst DEFEATED_MINOTAUR)
							(HighPrint 93 50)
							;If you've seen one, you've seen them all.
						else
							(HighPrint 93 51)
							;Good ears but very poor peripheral vision.
						)
					)
					((Said 'kill,chop,beat/bull,monster,creature')
						(if (Btst DEFEATED_MINOTAUR)
							(HighPrint 93 52)
							;Why bother?  He's out for the count.
						else
							(HighPrint 93 53)
							;OK.  Here's your chance.
							(curRoom newRoom: MINOTAUR)
						)
					)
				)
			)
		)
	)
	
	(method (getHurt amount)
		(cond 
			((<= (= gMonsterHealth (- gMonsterHealth amount)) 0)
				(self setScript: minotaurDies)
				(Bset DEFEATED_MINOTAUR)
			)
			((not local6)
				(rm93 setScript: delayLook)
			)
		)
	)
)

(instance delayLook of Script
	(properties)
	
	(method (changeState newState)
		(switch (= state newState)
			(0 (= seconds 3))
			(1
				(if (MinotaurHearsEgo)
					(minotaur setScript: minotaurLooks)
				)
			)
		)
	)
)

(instance minotaurLooks of Script
	(properties)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(minotaur
					setLoop: 2
					setCel: 0
					setMotion: 0
					cycleSpeed: 2
					setCycle: EndLoop self
				)
			)
			(1
				(= minotaurState mino2)
				(if (MinotaurSeesEgo)
					(minotaur setLoop: 3 setCel: 1)
					(= cycles 1)
				else
					(self changeState: 3)
				)
			)
			(2
				(minotaur setCel: 2)
				(= minotaurState mino3)
				(= cycles 1)
			)
			(3
				(if
					(or
						(ego inRect: 0 120 31 130)
						(ego inRect: 238 0 330 106)
						(ego inRect: 61 153 82 154)
					)
					(if (< 5 minotaurMutter) (= minotaurMutter 0) else (++ minotaurMutter))
					(switch minotaurMutter
						(0
							(HighPrint 93 54)
							;"I've been out here too long.  My imagination is taking over."
							)
						(1
							(HighPrint 93 55)
							;"What was that noise?"
							)
						(2
							(HighPrint 93 56)
							;"Must be those Brigands playing games."
							)
						(3
							(HighPrint 93 57)
							;"I guess I'm just getting jumpy."
							)
						(4
							(HighPrint 93 58)
							;"Is anyone there?"
							)
						(5
							(HighPrint 93 59)
							;"This place is spooky."
							)
					)
					(client setScript: patrol)
				else
					(curRoom newRoom: MINOTAUR)
				)
			)
		)
	)
)

(instance jerkBush of Script
	(properties)
	
	(method (doit)
		(if (and (== egoGait MOVE_SNEAK) (>= [egoStats STEALTH] trySneakMinotaurBush))
			(self dispose:)
		)
		(super doit:)
	)
	
	(method (changeState newState)
		(switch (= state newState)
			(0 (bush setCycle: EndLoop self))
			(1
				(if (MinotaurHearsEgo)
					(minotaur setScript: minotaurLooks)
				)
				(= seconds 17)
			)
			(2 (self dispose:))
		)
	)
)

(instance openGate of Script
	(properties)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(HandsOff)
				(gate
					ignoreActors:
					cycleSpeed: 3
					setPri: 4
					setCycle: EndLoop self
				)
			)
			(1
				(Bset BRIGAND_GATE_OPEN)
				(ego illegalBits: cWHITE)
				(HandsOn)
				(self dispose:)
			)
		)
	)
)

(instance closeGate of Script
	(properties)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(HandsOff)
				(gate
					ignoreActors: 1
					setCel: 3
					cycleSpeed: 3
					setCycle: BegLoop self
				)
			)
			(1
				(Bclr BRIGAND_GATE_OPEN)
				(ego illegalBits: (| cWHITE cLRED))
				(HandsOn)
				(self dispose:)
			)
		)
	)
)

(instance openMess of Script
	(properties)
	
	(method (changeState newState)
		(switch (= state newState)
			(1
				(= local9 1)
				(HighPrint 93 60)
				;You hear a snick as the hasp on the gate is opened.
				(lockSound number: (GetSongNumber 35) init: play:)
				(= local2 1)
				(= cycles 1)
			)
			(2
				(lockSound dispose:)
				(= seconds 3)
			)
			(3
				(= local9 0)
				(self dispose:)
			)
		)
	)
)

(instance patrol of Script
	(properties)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(minotaur moveSpeed: 1 cycleSpeed: 1 setCycle: 0)
				(if local6
					(self changeState: 5)
				else
					(= local9 0)
					(switch minotaurState
						(mino0 
							(self changeState: 10)
						)
						(mino1 
							(self changeState: 14)
							)
						(mino2
							(if local4
								(self changeState: 17)
							else
								(self changeState: 13)
							)
						)
						(mino3
							(if local4
								(self changeState: 3)
							else
								(self changeState: 1)
							)
						)
					)
				)
			)
			(1
				(= local4 0)
				(= minotaurState mino1)
				(minotaur view: vMinotaur setLoop: 3 setCel: 0)
				(= cycles 1)
			)
			(2 (self changeState: 14))
			(3
				(= local4 1)
				(= minotaurState mino0)
				(minotaur view: vMinotaur setLoop: 3 setCel: 1)
				(= cycles 1)
			)
			(4 (self changeState: 10))
			(5
				(minotaur cycleSpeed: 4 setCycle: BegLoop self)
			)
			(6
				(= local6 0)
				(= minotaurState mino2)
				(self changeState: 0)
			)
			(10
				(= local4 1)
				(= minotaurState mino0)
				(minotaur
					view: vMinotaur
					setLoop: 0
					setCel: 0
					setCycle: Forward
					setMotion: MoveTo 279 (minotaur y?) self
				)
			)
			(11
				(= local4 0)
				(minotaur setLoop: 2 setCel: 3)
				(= cycles 1)
			)
			(12
				(minotaur setLoop: 2 setCel: 2)
				(= cycles 1)
			)
			(13
				(= minotaurState mino1)
				(minotaur view: vMinotaur setLoop: 2 setCel: 1)
				(= cycles 1)
			)
			(14
				(= local4 0)
				(= minotaurState mino1)
				(minotaur
					view: vMinotaur
					setLoop: 1
					setCel: 0
					setCycle: Forward
					setMotion: MoveTo 60 (minotaur y?) self
				)
			)
			(15
				(= local4 1)
				(minotaur setLoop: 2 setCel: 1)
				(= cycles 1)
			)
			(16
				(minotaur setLoop: 2 setCel: 2)
				(= cycles 1)
			)
			(17
				(= minotaurState mino0)
				(minotaur view: vMinotaur setLoop: 2 setCel: 3)
				(= cycles 1)
			)
			(18 
				(self changeState: 10)
			)
		)
	)
)

(instance minotaurCalmed of Script
	(properties)
	
	(method (doit)
		(if (and (not (ego script?)) (not isEgoLocked))
			(HandsOff)
		)
		(super doit:)
	)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(= local9 1)
				(HandsOff)
				(minotaur setCycle: 0 setMotion: 0)
			)
			(1
				(switch minotaurState
					(mino0 (self changeState: 3))
					(mino1 (self changeState: 8))
					(mino2 (self changeState: 10))
					(mino3 (self changeState: 2))
				)
			)
			(2
				(minotaur setLoop: 0 setCel: 0)
				(= cycles 1)
			)
			(3
				(minotaur setLoop: 2 setCel: 3)
				(= cycles 1)
			)
			(4
				(minotaur setLoop: 2 setCel: 2)
				(= cycles 1)
			)
			(5 (self changeState: 10))
			(8
				(= local4 1)
				(minotaur setLoop: 2 setCel: 1)
				(= cycles 1)
			)
			(9
				(minotaur setLoop: 2 setCel: 2)
				(= cycles 1)
			)
			(10
				(minotaur
					setLoop: 4
					setCel: 0
					cycleSpeed: 8
					setCycle: EndLoop self
				)
			)
			(11
				(client setScript: minotaurSleeps)
			)
		)
	)
)

(instance minotaurDazzled of Script
	(properties)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(HandsOff)
				(= local9 1)
				(minotaur setCycle: 0 setMotion: 0)
			)
			(1
				(minotaur setLoop: 4 setCel: 1)
				(= cycles 5)
			)
			(2
				(minotaur setCel: 0)
				(= cycles 5)
			)
			(3
				(minotaur setCel: 1)
				(= cycles 2)
			)
			(4
				(minotaur setCel: 0)
				(= cycles 2)
			)
			(5
				(minotaur setCel: 1)
				(= local7 1)
				(HandsOn)
				(= seconds 12)
			)
			(6
				(= local7 0)
				(= local9 0)
				(client setScript: patrol)
			)
		)
	)
)

(instance minotaurSleeps of Script
	(properties)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(HandsOff)
				(if (< (minotaur x?) 79)
					(minotaur
						setLoop: 0
						cycleSpeed: 1
						moveSpeed: 1
						setCycle: Walk
						setMotion: MoveTo 79 162 self
					)
				else
					(minotaur
						setLoop: 1
						cycleSpeed: 1
						moveSpeed: 1
						setCycle: Walk
						setMotion: MoveTo 79 162 self
					)
				)
			)
			(1
				(= local6 1)
				(minotaur
					view: vMinotaurDefeated
					setLoop: 0
					cel: 0
					cycleSpeed: 4
					illegalBits: 0
					setCycle: CycleTo 3 cdFORWARD self
				)
			)
			(2
				(= local9 0)
				(minotaur stopUpd:)
				(HandsOn)
				(self dispose:)
			)
		)
	)
)

(instance minotaurDies of Script
	(properties)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(minotaur
					view: vMinotaurDefeated
					setLoop: 0
					cel: 0
					setMotion: 0
					cycleSpeed: 4
					illegalBits: 0
					setCycle: EndLoop self
				)
			)
			(1
				(minotaur stopUpd:)
				(self dispose:)
			)
		)
	)
)

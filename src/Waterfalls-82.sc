;;; Sierra Script 1.0 - (do not remove this comment)
(script# 82)
(include system.sh) (include sci2.sh) (include game.sh)
(use Main)
(use ThrowFlameDart)
(use AnimateOpen)
(use AnimateDazzle)
(use _Interface)
(use Door)
(use _LoadMany)
(use _Sound)
(use _Jump)
(use _Motion)
(use _Game)
(use _Inventory)
(use _Actor)
(use _System)

(public
	rm82 0
	hermitHead 1
	goOnIn 2
	squashed 3
	rm82Sound 4
)

(local
	drinkWaterCount
	rockHitDoor
	rockHitDoorAgain
	seenByHenry
	rockKnockCount
	lookedAtDoor
	[waterfallProps 8]
	[waterfallScripts 8]
	[rippleActors 4]
	[xMidDetail 4] = [200 225 219 251]
	[yMidDetail 4] = [-5 25 55 85]
	[xLowDetail 2] = [211 232]
	[yLowDetail 2] = [25 85]
	[xHighDetail 8] = [200 219 225 251 225 251 200 219]
	[yHighDetail 8] = [-5 25 55 85 -5 25 55 85]
)
(procedure (TryGetWater bool)
	(if (Btst CLIMBED_HENRY_CLIFF)
		(HighPrint 82 0)
		;You'll have to climb down to do that.
	else
		(ego setScript: getWater 0 bool)
	)
)

(procedure (PrintAtTop &tmp [str 200])
	(Format @str &rest)
	(Print @str #at 120 10)
)

(procedure (SetWaterRippleDetail)
	(= [rippleActors 0] (Clone aRipple))
	(= [rippleActors 1] (Clone aRipple))
	([rippleActors 0]
		setLoop: 4
		ignoreActors:
		init:
		cycleSpeed: (if (== detailLevel 3) 2 else 1)
		setCycle: Forward
		setScript: (Clone aRippleScript)
	)
	([rippleActors 1]
		setLoop: 4
		ignoreActors:
		posn: 150 200
		init:
		cycleSpeed: (if (== detailLevel 3) 2 else 1)
		setCycle: Forward
	)
)

(procedure (SetWaterfallLowDetail &tmp i)
	(= i 0)
	(while (< i 2)
		([waterfallProps i]
			setLoop: 0
			ignoreActors:
			x: [xLowDetail i]
			y: [yLowDetail (= [waterfallScripts i] (Clone aFallScript))]
			init:
			setPri: 1
			cycleSpeed: 3
			setScript:
				[waterfallScripts (= [waterfallProps i] (Clone waterFalling))]
				0
				i
		)
		(++ i)
	)
)

(procedure (SetWaterfallMidDetail &tmp i)
	(= i 0)
	(while (< i 4)
		([waterfallProps i]
			setLoop: 0
			ignoreActors:
			x: [xMidDetail i]
			y: [yMidDetail (= [waterfallScripts i] (Clone aFallScript))]
			init:
			setPri: 1
			cycleSpeed: 3
			setScript:
				[waterfallScripts (= [waterfallProps i] (Clone waterFalling))]
				0
				i
		)
		(++ i)
	)
)

(procedure (SetWaterfallHighDetail &tmp i)
	;CI: NOTE: SCICompanion incorrectly decompiled this script, as follows:
	;
	;y: 		[yHighDetail (= [waterfallScripts i] (Clone aFallScript))]
	;setScript: [waterfallScripts (= [waterfallProps i] (Clone waterFalling))] 0 i
	;
	;instead of calling the Cloning and assignment first. This caused this room to 
	;crash immedaitely upon the room's init.

	(= i 0)
	(while (< i 8)
		(= [waterfallProps i] (Clone waterFalling))
		(= [waterfallScripts i] (Clone aFallScript))

		([waterfallProps i]
			setLoop: 		0
			ignoreActors:
			x: 				[xHighDetail i]
			y: 				[yHighDetail i]
			init:
			setPri: 		1
			cycleSpeed: 	3
			setScript: 		[waterfallScripts i] NULL i
		)
		(++ i)
	)
)

(instance Magic of Prop
	(properties
		view vTeleportGreen
		cycleSpeed 2
	)
)

(instance rock of Actor
	(properties
		view vEgoThrowing ;CI:TODO: Create new separate view
		illegalBits $0000
	)
)

(instance rm82Sound of Sound
	(properties
		number 28
		priority 4
	)
)

(instance splashSound of Sound
	(properties
		number 72
		priority 2
		loop -1
	)
)

(instance hermitDoor of Door
	(properties
		y 49
		x 53
		view vHenryOutside
		loop 2
		entranceTo 83
		locked 1
		doorControl cLRED
	)
)

(instance hermitHead of View
	(properties
		view vHenryDoor
		loop 4
	)
)

(instance hermit of Actor
	(properties
		y 50
		x 88
		view vHenryDoor
		;loop 0
		illegalBits $0000
	)
)

(instance ladder of Prop
	(properties
		y 141
		x 87
		view vHenryOutside
		loop 5
	)
)

(instance spray of Prop
	(properties
		y 121
		x 182
		view vHenryOutside
		loop 1
		cel 2
		cycleSpeed 2
	)
)

(instance wave of Prop
	(properties
		y 139
		x 254
		view vHenryOutside
		loop 3
		cycleSpeed 2
	)
)

(instance aRipple of Actor
	(properties
		y 150
		x 216
		yStep 1
		view vHenryOutside
		illegalBits $0000
		xStep 1
	)
)

(instance waterFalling of Prop
	(properties
		view vHenryOutside
	)
)

(instance rm82 of Room
	(properties
		picture 82
		style DISSOLVE
		west 81
	)
	
	(method (init)
		(LoadMany RES_VIEW vHenryOutside (GetEgoViewNumber vEgoThrowing) vHenryDoor (GetEgoViewNumber vEgoShock) (GetEgoViewNumber vEgoFallDown))
		(LoadMany RES_SCRIPT 103 JUMP 137 138 139)
		(LoadMany RES_SOUND
			(GetSongNumber 78)
			(GetSongNumber 9)
			(GetSongNumber 10)
			(GetSongNumber 84)
		)
		(LoadMany RES_TEXT 137 138 139)
		(Load RES_SOUND 72)
		(if (Btst HENRY_SAFE_TP) (Load RES_VIEW vTeleportGreen))
		(super init: &rest)
		(cSound stop:)
		(splashSound number: 72 init: play:)
		(rm82Sound number: (GetSongNumber 78) init:)
		(StatusLine enable:)
		(hermitDoor init: stopUpd:)
		(ladder
			setPri: 1
			ignoreActors:
			init:
			cycleSpeed:
			(switch detailLevel
				(0 0)
				(1 1)
				(else  2)
			)
			stopUpd:
		)
		(if (> detailLevel DETAIL_LOW)
			(spray init: setScript: sprayScript)
		)
		(switch detailLevel
			(DETAIL_LOW
				(SetWaterfallLowDetail)
				(spray init: stopUpd: addToPic:)
				(wave init: setCel: 2 stopUpd: addToPic:)
			)
			(DETAIL_MID
				(SetWaterfallMidDetail)
				(wave init: setCel: 2 stopUpd: addToPic:)
			)
			(else 
				(SetWaterfallHighDetail)
				(wave init: setCycle: Forward startUpd:)
			)
		)
		(SetWaterRippleDetail)
		(StopEgo)
		(ego init:)
		(if (== prevRoomNum 83)
			(cond 
				((Btst HENRY_DEADLY_TP)
					(ego
						view: (GetEgoViewNumber vEgoTeleportHenry)
						illegalBits: 0
						setPri: 1
						setLoop: 0
						cel: 0
						posn: 219 55
						setScript: deadlyTP
					)
					(hermitDoor setCel: 0 doorState: 0 stopUpd:)
				)
				((Btst HENRY_SAFE_TP)
					(ego posn: 75 155 loop: loopS cel: 1 hide: setScript: safeTP)
					(hermitDoor setCel: 0 doorState: 0 stopUpd:)
				)
				(else (ego posn: 88 50 setMotion: MoveTo 88 57) (Bset CLIMBED_HENRY_CLIFF))
			)
		else
			(ego posn: 0 140 setMotion: MoveTo 35 140)
		)
		(if (or (Btst HENRY_DEADLY_TP) (Btst HENRY_SAFE_TP))
			(Magic
				posn: (ego x?) (ego y?)
				setPri: (+ (ego priority?) 1)
				ignoreActors:
				init:
			)
		)
		(if (not (Btst VISITED_FLYINGFALLS)) (self setScript: firstMsg))
	)
	
	(method (doit)
		(cond 
			(
				(and
					(> ([rippleActors 0] y?) 165)
					(> ([rippleActors 1] y?) 190)
				)
				([rippleActors 1] setScript: (Clone aRippleScript))
			)
			(
				(and
					(> ([rippleActors 1] y?) 165)
					(> ([rippleActors 0] y?) 190)
				)
				([rippleActors 0] setScript: (Clone aRippleScript))
			)
		)
		(if
			(and
				(Btst CLIMBED_HENRY_CLIFF)
				(not (Btst FLAG_274))
				(== (ego onControl: origin) cLMAGENTA)
			)
			(Bclr CLIMBED_HENRY_CLIFF)
			(Bclr FLAG_276)
			(ego setScript: (ScriptID 138 0))
		)
		(super doit:)
	)
	
	(method (dispose)
		(Bclr HENRY_DOOR_OPEN)
		(Bset VISITED_FLYINGFALLS)
		(super dispose:)
	)
	
	(method (handleEvent event &tmp spell)
		(switch (event type?)
			(evSAID
				(cond 
					((super handleEvent: event))
					((Said 'look>')
						(cond 
							(
							(Said '[<at,around,up][/area,scenery,cliff,north]')
							(HighPrint 82 1)
							;A river plunges for more than a hundred feet down the face of a cliff.
							;A doorway has been built into the side of the cliff.
							)
							((Said '/water,fall,cascade')
								(HighPrint 82 2)
								;The water seems to be trying to fly as it leaps from the mountain above.
								)
							((Said '/east')
								(HighPrint 82 3)
								;You see slick, vertical cliff walls.
								)
							((Said '/west,tree,forest')
								(HighPrint 82 4)
								;You see the forest.
								)
							((Said '/south,river,boulder')
								(HighPrint 82 5)
								;The water is white as it splashes against the rocks with great force
								;and purpose and disappears into a narrow canyon to the south.
								)
							((or (Said '<down') (Said '/ground'))
								(HighPrint 82 6)
								;The grass is green with the freshness of new spring.
								)
							((Said '/ledge,shelf')
								(HighPrint 82 7)
								;There is a narrow ledge in front of the wide door.
								)
							((Said '/ladder') (if (Btst HENRY_LADDER_KNOWN)
									(HighPrint 82 8)
									;You can't see it.
									else
									(HighPrint 82 9)
									;You don't see a ladder here.
									)
								)
							((Said '/door')
								(HighPrint 82 10)
								;The door is built into the face of the mountainside.
								;You suspect that the person who lives behind it treasures privacy.
								(if (not lookedAtDoor)
									(= lookedAtDoor TRUE)
									(HighPrint 82 11)
									;It's a large door.  It must open pretty wide.
								)
							)
						)
					)
					((Said 'lockpick[<up]/boulder,brick') (ego setScript: (ScriptID 103 0)))
					((Said 'drink[/water]') (TryGetWater FALSE))
					(
					(or (Said 'put/water/bottle') (Said 'fill/bottle'))
						(if (ego has: iFlask)
							(TryGetWater TRUE)
						else
							(HighPrint 82 12)
							;you don't have a flask
						)
					)
					((Said 'get>')
						(cond 
							((or (Said '/water') (Said 'bottle/water'))
								(if [invDropped iFlyingWater]
									(event claimed: FALSE)
								else
									(TryGetWater TRUE)
								)
							)
							((Said '/boulder,brick') (ego setScript: (ScriptID 103 0)))
						)
					)
					((Said 'throw/boulder,brick')
						(if (ego has: iRock)
							(ego setScript: throwIt)
						else
							(HighPrint 82 13)
							;You don't have a rock to throw.
						)
					)
					((or (Said 'swim') (Said 'go/swim'))
						(HighPrint 82 14)
						;The water comes from melting snow.  This early in the season, it's much too cold to swim.
						)
					((Said 'open/door')
						(if (Btst CLIMBED_HENRY_CLIFF)
							(HighPrint 82 15)
							;The door is securely locked.
						else
							(event claimed: FALSE)
						)
					)
					((Said 'lockpick,unlock/hasp,door')
						(if (Btst CLIMBED_HENRY_CLIFF)
							(HighPrint 82 16)
							;The door is so securely locked that it defies your abilities to unlock it.
						else
							(event claimed: 0)
						)
					)
					((Said 'knock')
						(if (Btst CLIMBED_HENRY_CLIFF)
							(self setScript: knockScript)
							(SolvePuzzle POINTS_KNOCKONHERMITDOOR 1)
						else
							(HighPrint 82 17)
							;It's a little hard to knock on the door from where you're standing.
						)
					)
					((Said 'climb>')
						(cond 
							((Said '/ladder')
								(cond 
									((Btst CLIMBED_HENRY_CLIFF) (ego setScript: (ScriptID 137 2)))
									((Btst HENRY_LADDER_KNOWN) (ego setScript: (ScriptID 137 0)))
									(else
										(HighPrint 82 9)
										;You don't see a ladder here.
										)
								)
							)
							((Said '[<up][/boulder,cliff]')
								(cond 
									((Btst CLIMBED_HENRY_CLIFF)
										(Print 82 18 #mode teJustCenter)
										; You're in front of the door already.  You don't need to climb any farther.
									)
									((Btst HENRY_LADDER_KNOWN) (ego setScript: (ScriptID 137 0)))
									;CI: NOTE: if you know about the ladder, you no longer gain any climbing skill
									; by climbing up. This is unfortunate, because there are so few places to improve your climbing skill.
									((TrySkill CLIMB tryClimbWaterfall) (ego setScript: (ScriptID 137 0)))
									(else (ego setScript: (ScriptID 137 1)))
								)
							)
							((Said '[<down][/boulder,cliff]')
								(if (Btst CLIMBED_HENRY_CLIFF)
									(ego setScript: (ScriptID 137 2))
								else
									(HighPrint 82 19)
									;Huh?
								)
							)
							(else (event claimed: TRUE)
								(HighPrint 82 20)
								;Climb what?
								)
						)
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
									(Bset HENRY_LADDER_KNOWN)
									(ladder setCycle: EndLoop)
								)
							)
							(TRIGGER
								(if (CastSpell spell) ;CI: added
									(Bset HENRY_LADDER_KNOWN)
									(ladder setCycle: EndLoop)
								)
							)
							(OPEN
								(if (CastSpell spell) ;CI: added
									(cond 
									((Btst HENRY_DOOR_OPEN)
										(HighPrint 82 21)
										;The door is already open.
										)
									((and (Btst CLIMBED_HENRY_CLIFF) (> [egoStats OPEN] 5))
										(CenterPrint 82 22)
										;As you prepare your spell...
										(ego setMotion: MoveTo (ego x?) 60)
										(hermitDoor setScript: (ScriptID 140 0))
									)
									((TrySkill MAGIC tryCastOpenHenry)
										(AnimateOpenSpell ego)
										(hermitDoor setScript: (ScriptID 140 0))
									)
									(else
										(HighPrint 82 23)
										;The only thing you can open here is the door, and you're not skilled enough to do that.
										)
									)
								)
							)
							(FLAMEDART
								(if (CastSpell spell) ;CI: added
									(AnimateThrowingFlameDart NULL)
									(HighPrint 82 24)
									;Wheeee!
								)
							)
							(DAZZLE
								(if (CastSpell spell) ;CI: added
									(AnimateDazzle ego)
									(HighPrint 82 25)
									;Wow!
								)
							)
							(else  
								(event claimed: FALSE)
							)
						)
						;)
					)
				)
			)
		)
	)
)

(instance aFallScript of Script
	(properties)
	
	(method (doit)
		(if (>= (client y?) 115) (client y: -5))
		(super doit:)
	)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(client
					x: (client x?)
					y: (+
						(client y?)
						(switch detailLevel
							(0 30)
							(1 20)
							(else  15)
						)
					)
				)
				(= cycles 2)
			)
			(1 (self changeState: 0))
		)
	)
)

(instance sprayScript of Script
	(properties)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(client
					posn: (Random 180 205) (Random 118 126)
					setCycle: EndLoop self
				)
			)
			(1 (self changeState: 0))
		)
	)
)

(instance aRippleScript of Script
	(properties)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(client
					show:
					posn: 232 142
					setMotion: MoveTo (Random 83 127) 195
				)
			)
		)
	)
)

(instance firstMsg of Script
	(properties)
	
	(method (changeState newState)
		(switch (= state newState)
			(0 (= cycles 8))
			(1
				(HighPrint 82 26)
				;The roar of the waterfall fills your ears, and the cold spray dampens your face as you approach.
				(client setScript: NULL)
			)
		)
	)
)

(instance goOnIn of Script
	(properties)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(HandsOff)
				(ego illegalBits: 0 setMotion: MoveTo 84 54 self)
			)
			(1
				(ego setMotion: MoveTo 84 48 self)
			)
			(2
				(hermitHead dispose:)
				(ego hide:)
				(hermitDoor setLoop: 3 cel: 0 setCycle: EndLoop self)
			)
			(3 (curRoom newRoom: 83))
		)
	)
)

(instance throwIt of Script
	(properties)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(HandsOff)
				(if (< (ego x?) 90)
					(ego illegalBits: 0 setMotion: MoveTo 18 155 self)
				else
					(ego illegalBits: 0 setMotion: MoveTo 162 145 self)
				)
			)
			(1
				(ego
					view: (GetEgoViewNumber vEgoThrowing)
					cycleSpeed: 1
					setLoop: (if (< (ego x?) 90) 3 else 2)
					cel: 0
				)
				(= cycles 2)
			)
			(2 (ego setCycle: CycleTo 4 cdFORWARD self))
			(3
				(rock
					setLoop: 4
					setPri: 15
					setStep: 35 20
					ignoreActors:
					ignoreHorizon:
					setCycle: Forward
					posn:
						(if (< (ego x?) 90)
							(+ (ego x?) 13)
						else
							(- (ego x?) 13)
						)
						(- (ego y?) 34)
					init:
				)
				(if (and (TrySkill THROW tryThrowHenry) (not (Btst HENRY_DOOR_OPEN)))
					(++ rockKnockCount)
					(= rockHitDoor TRUE)
					(rock
						setMotion:
							MoveTo
							(+ (hermitDoor x?) (Random 20 30))
							(- (hermitDoor y?) (Random 20 30))
							self
					)
				else
					(rock
						setMotion:
							MoveTo
							(if (< (ego x?) 90)
								(+ (hermitDoor x?) (Random 60 85))
							else
								(- (hermitDoor x?) (Random 25 50))
							)
							(- (hermitDoor y?) (Random 20 30))
							self
					)
				)
				(ego setCycle: EndLoop)
			)
			(4
				(if rockHitDoor
					(if rockHitDoorAgain
						(TimedPrint 2 82 27)
						;RAP!
					else
						(= rockHitDoorAgain TRUE)
						(HighPrint 82 28)
						;The rock makes a sharp sound as it hits the door.
					)
				)
				(rock
					setMotion:
						JumpTo
						(cond 
							((< (ego x?) 90) (if rockHitDoor 40 else 225))
							(rockHitDoor 140)
							(else 0)
						)
						(if (< (ego x?) 90) 145 else 140)
						self
				)
			)
			(5
				(if (not rockHitDoor)
					(Print 82 29)
					; Missed
				else
					(= rockHitDoor 0)
				)
				(if (== rockKnockCount 3)
					(= rockKnockCount 0)
					(if seenByHenry
						(HighPrint 82 30)
						;"All right, already!"
					else
						(HighPrint 82 31)
						;"Is someone there?"
					)
					(hermitDoor setScript: answerKnock)
				else
					(HandsOn)
				)
				(rock dispose:)
				(StopEgo)
				(ego
					use: 21 1
					loop: (if (< (ego x?) 90) 0 else 1)
					setScript: NULL
				)
			)
		)
	)
)

(instance answerKnock of Script
	(properties)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(hermitDoor
					view: vHenryOutside
					loop: 2
					cel: 0
					cycleSpeed: 1
					setCycle: CycleTo 2 cdFORWARD self
				)
			)
			(1
				(HandsOff)
				(hermitDoor setCycle: EndLoop self)
				(hermit
					setLoop: 0
					init:
					ignoreActors:
					setCycle: Walk
					setMotion: MoveTo 88 57
				)
			)
			(2
				(if seenByHenry
					(PrintAtTop 82 32)
					;"You again?  Either come up, or go away.   I really doesn't cares which."
					(self cue:)
				else
					(= seenByHenry TRUE)
					(PrintAtTop 82 33)
					;"Oh, 'ello.  Come right up."
					(PrintAtTop 82 34)
					;"Just climb the ladder."
					(ladder setCycle: EndLoop self)
					(Bset HENRY_LADDER_KNOWN)
				)
			)
			(3
				(hermitDoor setCycle: CycleTo 4 cdBACKWARD self)
			)
			(4
				(hermitDoor setCycle: BegLoop)
				(hermit setLoop: 1 setMotion: MoveTo 88 50 self)
			)
			(5
				(HandsOn)
				(hermit dispose:)
				(client setScript: NULL)
			)
		)
	)
)

(instance getWater of Script
	(properties)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(HandsOff)
				(ego illegalBits: 0)
				(if (< (ego x?) 90)
					(ego setMotion: MoveTo 95 150 self)
				else
					(self cue:)
				)
			)
			(1
				(ego setMotion: MoveTo 169 150 self)
			)
			(2
				(ego view: (GetEgoViewNumber vEgoThrowing) loop: 0 cel: 0 setCycle: EndLoop self)
			)
			(3
				(if (or (not register) (not (ego has: iFlask)))
					(switch drinkWaterCount
						(0
							(HighPrint 82 35)
							;You take a drink of water from the icy mountain river.  It refreshes you.
							)
						(1
							(HighPrint 82 36)
							;You take another drink of water.  You were thirsty!
							)
						(else
							(HighPrint 82 37)
							;BOY!  You must have really been thirsty!
							)
					)
					(++ drinkWaterCount)
				else
					(ego get: iFlyingWater use: iFlask 1)
					(Bset HAVE_FLYING_WATER)
					(SolvePuzzle POINTS_GETFLYINGWATER 3)
					(HighPrint 82 38)
					;You fill an empty flask with crystal-clear water from the waterfall.
				)
				(= cycles 5)
			)
			(4 (ego setCycle: BegLoop self))
			(5
				(StopEgo)
				(ego illegalBits: 0 setMotion: MoveTo 150 150 self)
			)
			(6
				(ego illegalBits: cWHITE)
				(HandsOn)
				(client setScript: NULL)
			)
		)
	)
)

(instance squashed of Script
	(properties)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(HandsOff)
				(ego cycleSpeed: 3 setCycle: EndLoop self)
			)
			(1
				(HighPrint 82 39)
				;Flat...you feel very, very flat.
				(if (TakeDamage 10)
					(= cycles 5)
				else
					(EgoDead DIE_RETRY DIE_HENRY_CRUSHED 82 40
						#icon (GetEgoViewNumber vEgoFallDown) 0 0
						#title {...and thin...}
						;And dead...you feel dead, too.  In your weakened condition, you succumbed to
						;a mild-mannered hermit's propensity for Tarzan imitations.  Back up and play it again, Sam.
					)
					;restore a quarter of ego's max HP
					(ego setHeroRetry:)
					;then reset all the other flags.
					(Bclr FLAG_276)
					(StopEgo)
					(ego posn: 86 55 setLoop: loopN)
					(HandsOn)
					(client setScript: NULL)
				)
			)
			(2
				(ego
					view: (GetEgoViewNumber vEgoDefeated)
					setLoop: 4
					cel: 0
					x: (- (ego x?) 4)
					cycleSpeed: 1
					setCycle: EndLoop self
				)
			)
			(3
				(Bclr FLAG_276)
				(StopEgo)
				(HandsOn)
				(client setScript: NULL)
			)
		)
	)
)

(instance safeTP of Script
	(properties)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(HandsOff)
				(Magic setCycle: CycleTo 6 cdFORWARD self)
			)
			(1
				(ego show:)
				(Magic setCycle: EndLoop self)
			)
			(2
				(Bclr HENRY_SAFE_TP)
				(client setScript: NULL)
				(HandsOn)
			)
		)
	)
)

(instance deadlyTP of Script
	(properties)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(HandsOff)
				(ego cycleSpeed: 2 setCycle: EndLoop)
				(= cycles 20)
			)
			(1
				(ego view: (GetEgoViewNumber vEgoShock) setLoop: 0 setCycle: EndLoop)
				(= cycles 12)
			)
			(2
				(ego yStep: 20 setMotion: MoveTo (ego x?) 200 self)
			)
			(3
				(EgoDead DIE_RETRY DIE_HENRY_TELEPORT 82 41
					#title {You're all wet}
					#icon (GetEgoViewNumber vEgoShock) 0 2
					;That hermit seems to know his "Trigger" spells pretty well.
					;He sure pulled the trigger on you (not to mention the plug) by teleporting you to the top of the falls without a barrel.
				)
				;clear the deadly flag
				(Bclr HENRY_DEADLY_TP)
				;set our own flag
				(Bset DIE_RETRY_INPROGRESS)
				;then bring us back into Henry's room.
				(curRoom newRoom: prevRoomNum)
			)
		)
	)
)

(instance knockScript of Script
	(properties)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(HandsOff)
				(Bset KNOCKED_HENRY_DOOR)
				(rm82Sound number: (GetSongNumber 78) loop: 1 play: self)
			)
			(1
				(Print 82 42)
				(PrintAtTop 82 43)
				(hermitDoor setScript: (ScriptID 139 0))
				(self dispose:)
				; You knock three times.
				;"Just a minute!  Just a minute!"
			)
		)
	)
)

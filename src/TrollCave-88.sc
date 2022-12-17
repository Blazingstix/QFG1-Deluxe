;;; Sierra Script 1.0 - (do not remove this comment)
(script# 88)
(include system.sh) (include sci2.sh) (include game.sh)
(use Main)
(use _Sound)
(use _Motion)
(use _Game)
(use _Inventory)
(use _Actor)
(use _System)

(public
	rm88 0
)

(enum
	searchTROLL
	searchJUNK
	searchBEARD
)

(enum ;trollState
	trollNONE	;0
	trollHIDING	;1
	trollATTACKING	;2
	trollDYING	;3
	trollDEAD	;4
)

(local
	trollState
	;local1 ;CI: NOTE: this was unused in original script, so I've removed it.
	searchWhat
	;CI: NOTE: original code had improper references for the drips. I don't know how it worked... maybe it was decompiled incorrectly?
	dripIndex
	[dripX 5]
	[dripY 5]
	;CI: original local variables from local3 onwards.
	;[local3 4]
	;dripX
	;dripY
	;dripIndex
	;[local10 4]
)
(instance magicHit of Sound
	(properties
		number 45
		priority 1
	)
)

(instance egoShoots of Sound
	(properties
		number 33
		priority 2
	)
)

(instance rm88 of Room
	(properties
		picture 88
		style WIPELEFT
		east 89
	)
	
	(method (init)
		(super init:)
		(pile init: addToPic:)
		(drip init: setScript: dripScript)
		(= dripIndex (Random 0 5))
		(= [dripX 0] 36)
		(= [dripY 0] 133)
		(= [dripX 1] 94)
		(= [dripY 1] 146)
		(= [dripX 2] 135)
		(= [dripY 2] 158)
		(= [dripX 3] 212)
		(= [dripY 3] 160)
		(= [dripX 4] 292)
		(= [dripY 4] 161)
		(= [dripX 5] 67)
		(= [dripY 5] 153)
		(StatusLine enable:)
		(StopEgo)
		(ego init:)
		(if
			(==
				(= trollState
					(cond 
						((Btst DEFEATED_FRED_89) 
							trollNONE ;Troll's body is in the other screen
						)
						((Btst DEFEATED_FRED)
							(troll
								view: vTrollDefeated
								posn: 80 158
								setLoop: 0
								setCel: 10
								init:
								addToPic:
							)
							trollDEAD ;troll dead.
						)
						(else 
							trollHIDING ;troll here
						)
					)
				)
				trollHIDING
			)
			(Load RES_VIEW vTroll)
			(Load RES_SCRIPT CHASE)
			(if (ego knows: FLAMEDART)
				(Load RES_VIEW (GetEgoViewNumber vEgoMagicFlameDart))
				(Load RES_SOUND (GetSongNumber 33))
				(Load RES_SOUND (GetSongNumber 45))
				(egoShoots number: (GetSongNumber 33) init:)
				(magicHit number: (GetSongNumber 45) init:)
			)
		)
		;if we've just come from an arena, but there's no monster in record, then it's a retry event
		;so let's just pretend we're entering here again.
		(if (and (== prevRoomNum TROLL) (== monsterInRoom 0))
			(= prevRoomNum 89)
			(Bclr VISITED_TROLLCAVE88)
		)
		(switch prevRoomNum
			(TROLL
				(ego posn: 135 160 loop: loopW)
				(= monsterInRoom 0)
				(self setScript: trollDies)
			)
			(else 
				(ego posn: 318 150 setMotion: MoveTo 255 150)
				(if (not (Btst VISITED_TROLLCAVE88))
					(HighPrint 88 0)
					;There is a strong odor of decay and other nasty smells filling the air.
					;A pile of something unpleasant lies near the center of this cave.
				)
			)
		)
	)
	
	(method (doit)
		(super doit:)
		(if (< (ego x?) 140)
			(switch trollState
				(trollHIDING 
					(self setScript: trollOut)
				)
			)
		)
	)
	
	(method (dispose)
		(Bset VISITED_TROLLCAVE88)
		(super dispose:)
	)
	
	(method (handleEvent event &tmp spell)
		(switch (event type?)
			(evSAID
				(cond 
					((Said 'look>')
						(cond 
							((Said '/down,ground,floor,stalactite')
								(HighPrint 88 1)
								;You can watch the water ooze down the sides of the stalactites and drip to the ground.
								)
							((Said '/up,ceiling,stalagmite')
								(HighPrint 88 2)
								;You can watch the water drop down from the stalactites and ooze down the sides of the stalagmites.
								)
							((Said '[<at,around][/!*,cave,room]')
								(HighPrint 88 0)
								;There is a strong odor of decay and other nasty smells filling the air.
								;A pile of something unpleasant lies near the center of this cave.
								)
							((Said '/water')
								(HighPrint 88 3)
								;The water has been polluted by various other disgusting liquids.
								)
							((Said '/collection,item,bone,armor')
								(HighPrint 88 4)
								;It looks as if someone has piled up bones, bits of armor, and things you can't quite
								;make out and probably don't want to know about. It smells disgusting.
								)
							((Said '/troll,monster,creature')
								(switch trollState
									(trollATTACKING	;troll engaging
										(HighPrint 88 5)
										;A mean-looking Troll looms before you, a snarl on his lips.
										)
									(trollDYING	;troll dying
										(HighPrint 88 6)
										;The Troll lies dying upon the slimy floor of the cave.
										)
									(trollDEAD	;troll dead
										(HighPrint 88 7)
										;A dead Troll lies in a pool of blue blood.
										)
									(else
										(HighPrint 88 8)
										;You see no such creature here.
										)
								)
							)
							((Said '/north,west,south,east')
								(HighPrint 88 9)
								;You have lost your sense of direction, as the cave passage twists around.
								)
							((Said '/boulder')
								(HighPrint 88 10)
								;The rocks look slick and are slimy to the touch.
								)
							((Said '/fungus')
								(HighPrint 88 11)
								;The light from the fungus is eerie and vaguely unpleasant.
								)
							((Said '/entrance,open')
								(HighPrint 88 12)
								;The only entrance to this cave is the one you came through.
								)
						)
					)
					((Said 'throw/')
						(HighPrint 88 13)
						;There is nothing here to throw it at.
						)
					(
					(Said 'search/troll,monster,creature,body,enemy')
						(cond 
							((!= trollState trollDEAD)	;troll not dead
								(HighPrint 88 14)
								;You can't do that.
								)
							((ego inRect: 64 144 106 165)
								(= searchWhat searchTROLL)
								(self setScript: egoSearch)
							)
							(else
								(HighPrint 88 15)
								;You need to get closer to the dead Troll.
								)
						)
					)
					((Said 'hiden')
						(HighPrint 88 16)
						;The password is for the hidden passage, not the troll's home.
						)
					((Said 'get,search/collection,junk,item,collection,silver,gold,loot,[!*]')
						(if (ego inRect: 39 140 120 157)
							(= searchWhat searchJUNK)
							(self setScript: egoSearch)
						else
							(HighPrint 88 17)
							;Get closer to the pile of junk.
						)
					)
					((Said 'get,get>')
						(cond 
							((Said '/hair,beard')
								(cond 
									((!= trollState trollDEAD)
										(HighPrint 88 14)
										;You can't do that.
									)
									((ego inRect: 64 144 106 165)
										(= searchWhat searchBEARD)
										(self setScript: egoSearch)
									)
									(else
										(HighPrint 88 15)
										;You need to get closer to the dead Troll.
									)
								)
							)
							((Said '/club,weapon')
								(cond 
									((== trollState trollATTACKING)	;engaging
										(HighPrint 88 18)
										;You're kidding, right?
										)
									((!= trollState trollDEAD)	;not dead
										(HighPrint 88 14)
										;You can't do that.
										)
									(else
										(HighPrint 88 19)
										;The dead Troll's huge club is much too heavy for you to lift.
										)
								)
							)
							((Said '/fungus')
								(HighPrint 88 20)
								;The fungus is slimy and stuck tight to the cave walls.
								)
							((Said '/troll,stalactite,stalagmite')
								(HighPrint 88 18)
								;You're kidding, right?
								)
							((Said '/boulder,water')
								(HighPrint 88 21)
								;You don't need it.
								)
							((Said '/armor,leather,chain')
								(HighPrint 88 22)
								;The armor is corroded and valueless.
								)
						)
					)
					((Said 'feed/troll,monster,creature')
						(if (== trollState trollATTACKING)	;engaging
							(HighPrint 88 23)
							;This Troll looks as though he'd rather eat YOU!
						else
							(HighPrint 88 14)
							;You can't do that.
						)
					)
					((Said 'listen/')
						(if (or (== trollState trollNONE) (== trollState trollDEAD))	;troll body on other screen, or troll dead here
							(HighPrint 88 24)
							;You hear the constant "drip... drip..." of water.
						else
							(HighPrint 88 25)
							;You hear the constant "drip... drip..." of water, and the unmistakeable sound of something breathing nearby.
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
									(HighPrint 88 26)
									;There is no magic in this cave.
								)
							)
							(DAZZLE
								(if (CastSpell spell) ;CI: added
									(HighPrint 88 27)
									;There is nothing here to dazzle.
								)
							)
							(FLAMEDART
								(if (CastSpell spell) ;CI: added
									(HighPrint 88 28)
									;There is nothing here to use that spell on.
								)
							)
							(CALM
								(if (CastSpell spell) ;CI: added
									(HighPrint 88 29)
									;There is nothing here to calm.
								)
							)
							(OPEN
								(if (CastSpell spell) ;CI: added
									(HighPrint 88 30)
									;There is nothing here to open.
								)
							)
							(ZAP
								;bump this back to the main script to cast ZAP
								(event claimed: FALSE)
							)
							(else
								(HighPrint 88 31)
								;That spell is useless here.
								)
						)
						;)
					)
					((Said 'fight')
						(if (Btst DEFEATED_FRED)
							(HighPrint 88 32)
							;The cave troll is dead.
						else
							(curRoom newRoom: TROLL)
						)
					)
				)
			)
		)
		(super handleEvent: event)
	)
)

(instance troll of Actor
	(properties
		yStep 3
		view vTroll
		cycleSpeed 1
		illegalBits $0000
		xStep 5
		moveSpeed 1
	)
)

(instance drip of Prop
	(properties
		y 204
		x 79
		view vTrollCave
		loop 1
	)
)

(instance pile of View
	(properties
		y 150
		x 60
		view vTrollCave
	)
)

(instance trollOut of Script
	(properties)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(= trollState trollATTACKING)	;troll engaging
				(HandsOff)
				(troll
					posn: 249 127
					init:
					setCycle: Walk
					setMotion: MoveTo 183 154 self
				)
			)
			(1
				;CI: I added this random chance ot being able to kill the troll, or it just sneaking up and killing you.
				(if (TrySkill LUCK tryLuckTrollInstakill 10)
					(HighPrint 88 33)
					;Before you can notice him, a Troll sneaks up behind you, and the encounter begins.
					(curRoom newRoom: TROLL)
				else
					(HighPrint 88 42)
					;Before you can even react, a Troll sneaks up behind you and swings his club.
					(client setScript: trollKills)
				)
			)
		)
	)
)

;EO: Was this Troll battle originally going to be a side-view battle?
;CI: Unlikely. More likely, this was going to be an insta-kill, when the Troll sneaks up behind you.
; Instead, the only fight is a standard (albeit un-escapable) arena battle.
;This script is currently unused. So I will not bother with fixing the death, yet.
(instance trollKills of Script
	(properties)
	
	(method (changeState newState)
		(switch (= state newState)
			(0 
				(HandsOff)
				(= cycles 3)
			)
			(1
				(troll loop: 2 cel: 0 setCycle: EndLoop)
				(= cycles 1)
			)
			(2
				(troll loop: 3 cel: 0 setCycle: EndLoop)
				(ego
					view: (GetEgoViewNumber vEgoDefeatedMagic)
					loop: 2
					cel: 0
					cycleSpeed: 1
					setCycle: EndLoop self
				)
			)
			(3 
				(= cycles 12)
			)
			(4
				(EgoDead DIE_RETRY DIE_FRED_INSTAKILL 88 34
					#title {WHATA TROLL!}
					#icon (GetEgoViewNumber vEgoDeathScenes) 0 0
					;The Troll's brute force was too much for your skull.
				)
				;reset ego's look
				(ChangeGait MOVE_NOCHANGE FALSE)
				(Bclr VISITED_TROLLCAVE88)
				;remove the troll
				(troll dispose:)
				(= trollState trollHIDING)
				;reset ego to the start of the room
				(ego posn: 318 150 setMotion: MoveTo 255 150)
				(= cycles 1)
			)
			(5
				;and redisplay the intro message.
				(HighPrint 88 0)
				;There is a strong odor of decay and other nasty smells filling the air.
				;A pile of something unpleasant lies near the center of this cave.
				(HandsOn)
			)
		)
	)
)

(instance trollDies of Script
	(properties)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(= trollState trollDYING)		;troll is dying
				(Bset DEFEATED_FRED)
				(troll
					view: vTrollDefeated
					loop: 0
					cel: 0
					posn: 80 158
					init:
					cycleSpeed: 2
					setCycle: EndLoop self
				)
			)
			(1
				(= trollState trollDEAD)		;troll is dead
				(troll addToPic:)
				(client setScript: NULL)
			)
		)
	)
)

(instance dripScript of Script
	(properties)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(drip
					posn: [dripX dripIndex] [dripY dripIndex]
					setPri: 15
					setCycle: EndLoop self
				)
			)
			(1
				(= dripIndex (Random 0 5))
				(self changeState: 0)
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
				(FaceObject ego troll)
				(ego
					loop: (mod (+ (ego loop?) 4) 2)
					view: (GetEgoViewNumber vEgoThrowing)
					setCycle: EndLoop self
				)
			)
			(1
				(switch searchWhat
					(searchTROLL
						(if (Btst SHAVED_FRED)
							(HighPrint 88 35)
							;There is nothing of value on the Troll.
						else
							(HighPrint 88 36)
							;You find nothing on the Troll but a little hair.
						)
					)
					(searchJUNK
						(HighPrint 88 37)
						;You dig around in the pile until the odor almost makes you sick.
						(if (Btst SEARCHED_FRED_TREASURE)
							(HighPrint 88 38)
							;You find nothing more of value.
						else
							(Bset SEARCHED_FRED_TREASURE)
							(HighPrint 88 39)
							;You find 30 silvers and 5 gold.
							(ego get: iGold 5)
							(ego get: iSilver 30)
						)
					)
					(searchBEARD
						(if (Btst SHAVED_FRED)
							(HighPrint 88 40)
							;There is no beard hair left on the Troll.
						else
							(HighPrint 88 41)
							;You cut some beard hair and put it away.
							(Bset SHAVED_FRED)
							(ego get: iTrollBeard)
						)
					)
				)
				(ego setCycle: BegLoop self)
			)
			(2 
				(StopEgo)
				(HandsOn)
			)
		)
	)
)

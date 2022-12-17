;;; Sierra Script 1.0 - (do not remove this comment)
(script# CLOSECOMBAT) ;CLOSECOMBAT = 215
(include system.sh) (include sci2.sh) (include game.sh)
(use Main)
(use _LoadMany)
(use _Inventory)
(use _Actor)
(use _System)

(use _Interface)

(public
	closeCombat 0
	aSpell 1
)

(local
	spellCast
	theWarrior
	local2
	haveShield
	local4
	local5 =  2
)
(instance aSpell of Prop
	(properties)
)

(instance closeCombat of Script
	(properties)
	
	(method (init)
		(LoadMany RES_SCRIPT ARENA_THRUST ARENA_BLOCK ARENA_PARRY ARENA_DODGE ARENA_PAIN)
		(if (ego has: iShield)
			(Load RES_VIEW (GetEgoViewNumber vEgoFightArmSword))
		else
			;load the magic using arena scripts
			(LoadMany RES_SCRIPT ARENA_MAGIC ARENA_FLAME ARENA_ZAP ARENA_DAZZLE ARENA_CALM)
			(Load RES_VIEW (GetEgoViewNumber vEgoFightArmDagger))
			(aSpell view: (GetEgoViewNumber vEgoFightArmDagger))
		)
		(= egoCanFight TRUE)
		(super init: &rest)
	)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(client drawWeapons:)
				(= theWarrior (ScriptID WARRIOR 0))
				(= haveShield (theWarrior egoShield?))
				(= local2 (theWarrior egoHand?))
				(= local4 (theWarrior egosBack?))
				(directionHandler addToFront: client)
				(mouseDownHandler addToFront: client)
				(if (and (theWarrior noWeapon?) (not haveShield))
					(local2 setLoop: 1 setCel: 2)
				)
				(= cycles 1)
			)
			(1
				(if (theWarrior noWeapon?)
					(self changeState: 3)
				else
					(client
						canFight: 1
						action: 0
						cycleSpeed: 0
						moveSpeed: 0
						view: (theWarrior weaponView?)
						setLoop: 2
						cel: 0
						stopUpd:
					)
				)
			)
			(2
				(HandsOn)
				(self changeState: 1)
			)
			(3
				(EgoDead DIE_RETRY DIE_ARENA_UNARMED CLOSECOMBAT 8
					#title {Bare Hands vs. Teeth and Claws, etc.}
					#icon (GetEgoViewNumber vEgoDefeatedMagic) 0 9
				)
				;Caught in combat with neither sword nor dagger, you are unable to resist the monster's fierce onslaught.
				;Try to be better equipped in your next life.
				(theWarrior resetBeforeArena:)
			)
		)
	)
	
	(method (handleEvent event &tmp temp0 temp1 temp2 temp3)
		(if
		(or (not (client canFight?)) (not egoCanFight))
			(event claimed: TRUE)
			(while ((= temp3 (Event new: 71)) type?)
				(temp3 dispose:)
			)
			(temp3 dispose:)
			(return 1)
		)
		(if script
			(if
				(and
					(== (event type?) direction)
					(!= (event message?) dirN)
				)
				(script cue:)
			else
				(return (event claimed: TRUE))
			)
		)
		(return
			(switch (event type?)
				(evSAID
					(cls)
					(cond 
						((super handleEvent: event))
						((Said 'look')
							(HighPrint 215 0)
							;Quit sightseeing and watch what you're doing!
							)
						((Said 'escape,escape,run')
							;CI:NOTE: I refactored this to have a property of the Arena dictating wether you can escape or not.
							(if (not ((ScriptID curRoomNum) canEscape?))
								;(or
									
									;(== prevRoomNum 14)		;bear Cave
									;(== prevRoomNum 171)	;bear changing to Barronett (note, this is set so that you go immediately to the bear changing screen after killing the bear)
									;(== prevRoomNum 88)		;Troll Cave 1
									;(== prevRoomNum 89)		;Troll Cave 2
									;(== prevRoomNum 93)		;Brigand Gate (i.e. Minotaur fight)
									;(== prevRoomNum 73)		;Target Range (i.e. Brutus)
									;(== prevRoomNum 91)		;Brigand Ambush (i.e. fighting brigand guards)
								;)
								(HighPrint 215 1)
								;You cannot escape this encounter.
							else
								((ScriptID curRoomNum) escaped: TRUE)
							)
						)
						((Said 'cast>')
							(= spellCast (SaidCast event))
							(cond 
								;CI:NOTE: I swapped the order of these conditions. I felt it made more sense to say you can't cast *any* spell now, regardless if you picked a known spell/
								(haveShield
									(HighPrint 215 3)
									;You cannot make the arcane gestures to cast spells while carrying your shield.
								)
								((not spellCast)
									(HighPrint 215 2)
									;That isn't a known spell.
								)
								(
									(and
										(!= spellCast FLAMEDART)
										(!= spellCast ZAP)
										(!= spellCast DAZZLE)
										(!= spellCast CALM)
									)
									(HighPrint 215 4)
									;This might not be the best time for practicing non-combat spells.
								)
								((CastSpell spellCast)
									(client setEgoMP: [egoStats MANA])
									(HandsOff)
									(self setScript: (ScriptID ARENA_MAGIC 0) self spellCast)
								)
							)
						)
						(
							(or
								(Said 'fight,kill,beat,chop')
								(Said 'use/blade,dagger,weapon,shield')
							)
							(HighPrint 215 5)
							;Go ahead!
						)
						((Said 'throw')
							(HighPrint 215 6)
							;You're too close.
							)
						(else
							(HighPrint 215 7)
							;You don't have time for that.
							(event claimed: TRUE))
					)
				)
				(mouseDown
					(= temp0 (event x?))
					(= temp1 (event y?))
					(event claimed: TRUE)
				)
				(direction
					(HandsOff)
					(switch (event message?)
						(dirN
							(= local5 2)
							(self setScript: (ScriptID ARENA_THRUST 0) self local5)
						)
						(dirW
							(= local5 0)
							(self setScript: (ScriptID ARENA_DODGE 0) self local5)
						)
						(dirE
							(= local5 1)
							(self setScript: (ScriptID ARENA_DODGE 0) self local5)
						)
						(dirS
							(= local5 2)
							(cond 
								(haveShield
									(self setScript: (ScriptID ARENA_BLOCK 0) self local5)
								)
								((not (ego has: iSword))
									(self setScript: (ScriptID ARENA_PARRY 0) self local5)
								)
								(else
									(= local5 (Random 0 1))
									(self setScript: (ScriptID ARENA_DODGE 0) self local5)
								)
							)
						)
					)
					(event claimed: TRUE)
				)
			)
		)
	)
)

;;; Sierra Script 1.0 - (do not remove this comment)
(script# 54)
(include system.sh) (include sci2.sh) (include game.sh)
(use Main)
(use ThrowFlameDart)
(use ThrowRock)
(use AnimateDazzle)
(use Door)
(use _LoadMany)
(use _Sound)
(use _Motion)
(use _Game)
(use _Inventory)
(use _Actor)
(use _System)

(public
	rm54 0
	rock 1
	nest 2
	bird 3
	nestDown 4
	flyAway 5
	treeFall 6
)

(local
	local0
	local1
	local2
	local3
	local4
	local5
	fallOffTree
	local7
	saidOnALimb
)
(instance fireDart of Actor
	(properties
		view vEgoMagicFlameDart ;CI:TODO: create new separate view
		illegalBits $0000
	)
)

(instance magicLasso of Actor
	(properties
		view vEgoMagicFetch ;CI:TODO: Create new separate view
		illegalBits $0000
	)
)

(instance rock of Actor
	(properties
		view vEgoThrowing ;CI:TODO: Create new separate view
		illegalBits $0000
	)
)

(instance healerSign of PicView
	(properties
		y 72
		x 212
		view vHealerOutside
		loop 2
		priority 10
	)
)

(instance ring of Prop
	(properties
		y 64
		x 79
		view vHealerOutside
		loop 6
	)
	
;	(method (handleEvent event)
;		(asm
;			pushi    1
;			lofsa    'look/glimmer,reflection,chandelier'
;			push    
;			callk    Said,  2
;			bnt      code_01be
;			pushi    1
;			pushi    OBTAINED_RING
;			callb    Btst,  2
;			not     
;			bnt      code_01b3
;			lsg      nestCondition
;			ldi      0
;			eq?     
;			bnt      code_01a2
;			pushi    1
;			pushi    CLIMBED_HEALER_TREE
;			callb    Btst,  2
;			not     
;			bnt      code_01a2
;			pushi    2
;			pushi    54
;			pushi    0
;			callb    HighPrint,  4
;			jmp      code_0303
;code_01a2:
;			pushi    2
;			pushi    54
;			pushi    1
;			callb    HighPrint,  4
;			pushi    1
;			pushi    LOOKED_IN_PTERESA_NEST
;			callb    Bset,  2
;			jmp      code_0303
;code_01b3:
;			pushi    #claimed
;			pushi    1
;			pushi    0
;			lap      event
;			send     6
;			jmp      code_0303
;code_01be:
;			pushi    1
;			lofsa    'look/ring'
;			push    
;			callk    Said,  2
;			bnt      code_01e8
;			pushi    1
;			pushi    LOOKED_IN_PTERESA_NEST
;			callb    Btst,  2
;			bnt      code_01dd
;			pushi    2
;			pushi    54
;			pushi    2
;			callb    HighPrint,  4
;			jmp      code_0303
;code_01dd:
;			pushi    #claimed
;			pushi    1
;			pushi    0
;			lap      event
;			send     6
;			jmp      code_0303
;code_01e8:
;			pushi    1
;			lofsa    'get/ring'
;			push    
;			callk    Said,  2
;			bnt      code_029b
;			pushi    #pickUp
;			pushi    1
;			pushi    19
;			lag      ego
;			send     6
;			bnt      code_020b
;			pushi    2
;			pushi    54
;			pushi    3
;			callb    HighPrint,  4
;			jmp      code_0303
;code_020b:
;			pushi    1
;			pushi    OBTAINED_RING
;			callb    Btst,  2
;			bnt      code_021c
;			pushi    0
;			callb    PrintAlreadyDoneThat,  0
;			jmp      code_0303
;code_021c:
;			lsg      nestCondition
;			ldi      0
;			eq?     
;			bnt      code_024e
;			pushi    1
;			pushi    CLIMBED_HEALER_TREE
;			callb    Btst,  2
;			not     
;			bnt      code_023b
;			pushi    2
;			pushi    54
;			pushi    4
;			callb    HighPrint,  4
;			jmp      code_0303
;code_023b:
;			pushi    #setScript
;			pushi    1
;			pushi    2
;			pushi    237
;			pushi    0
;			callk    ScriptID,  4
;			push    
;			lag      ego
;			send     6
;			jmp      code_0303
;code_024e:
;			lsg      nestCondition
;			ldi      2
;			eq?     
;			bnt      code_0285
;			pushi    #onControl
;			pushi    1
;			pushi    1
;			lag      ego
;			send     6
;			push    
;			ldi      8192
;			eq?     
;			bt       code_0276
;			pushi    #onControl
;			pushi    1
;			pushi    1
;			lag      ego
;			send     6
;			push    
;			ldi      16384
;			eq?     
;EO: code_0276 would not decompile
;code_0276:
;			not     
;			bnt      code_0285
;			pushi    2
;			pushi    54
;			pushi    5
;			callb    HighPrint,  4
;			jmp      code_0303
;code_0285:
;			pushi    1
;			pushi    LOOKED_IN_PTERESA_NEST
;			callb    Btst,  2
;			not     
;			bnt      code_0303
;			pushi    2
;			pushi    54
;			pushi    6
;			callb    HighPrint,  4
;			jmp      code_0303
;code_029b:
;			pushi    1
;			lofsa    'climb/branch'
;			push    
;			callk    Said,  2
;			bnt      code_0303
;			pushi    1
;			pushi    OBTAINED_RING
;			callb    Btst,  2
;			bnt      code_02b7
;			pushi    0
;			callb    PrintAlreadyDoneThat,  0
;			jmp      code_0303
;code_02b7:
;			lsg      nestCondition
;			ldi      0
;			eq?     
;			bnt      code_02e9
;			pushi    1
;			pushi    278
;			callb    Btst,  2
;			not     
;			bnt      code_02d6
;			pushi    2
;			pushi    54
;			pushi    7
;			callb    HighPrint,  4
;			jmp      code_0303
;code_02d6:
;			pushi    #setScript
;			pushi    1
;			pushi    2
;			pushi    237
;			pushi    0
;			callk    ScriptID,  4
;			push    
;			lag      ego
;			send     6
;			jmp      code_0303
;code_02e9:
;			lsg      nestCondition
;			ldi      2
;			eq?     
;			bt       code_02fb
;			lsg      nestCondition
;			ldi      1
;			eq?     
;			bnt      code_0303
;code_02fb:
;			pushi    2
;			pushi    54
;			pushi    8
;			callb    HighPrint,  4
;code_0303:
;			ret     
;		)
;	)
	
		;CI:NOTE: This was downloaded from Eric's Decompilation Archive
		(method (handleEvent event)
		(cond 
			((Said 'look/glimmer,reflection,chandelier')
				(if (not (Btst OBTAINED_RING))
					(if (and (== nestCondition nestInTree) (not (Btst CLIMBED_HEALER_TREE)))
						(HighPrint 54 0)
						;You can't tell what it is from down on the ground.
					else
						(HighPrint 54 1)
						;It looks like a golden ring, flashing in the sunlight.
						(Bset LOOKED_IN_PTERESA_NEST)
					)
				else
					(event claimed: FALSE)
				)
			)
			((Said 'look/ring')
				(if (Btst LOOKED_IN_PTERESA_NEST)
					(HighPrint 54 2)
					;The ring appears to be made of fine gold.
				else
					(event claimed: FALSE)
				)
			)
			((Said 'get/ring')
				(cond 
					((ego pickUp: invRing)
						(HighPrint 54 3)
						;You retrieve the ring you dropped.
					)
					((Btst OBTAINED_RING)
						(PrintAlreadyDoneThat)
					)
					((== nestCondition nestInTree)
						(if (not (Btst CLIMBED_HEALER_TREE))
							(HighPrint 54 4)
							;You'll think of something.
						else
							(ego setScript: (ScriptID 237 0))
						)
					)
					;EO: this was the undecompilable code
					((== nestCondition nestOnGround)
						(HighPrint 54 5)
						;Go check out the nest.
					)
					;;
					((== nestCondition nestBurnt)
						(or
							(== (ego onControl: origin) cLMAGENTA)
							(== (ego onControl: origin) cYELLOW)
						)
						(if (not (Btst LOOKED_IN_PTERESA_NEST))
							(HighPrint 54 6)
							;How can you get something you can't see?.
						)
					)
				)
			)
			((Said 'climb/branch')
				(cond 
					((Btst OBTAINED_RING)
						(PrintAlreadyDoneThat)
					)
					((== nestCondition nestInTree)
						(if (not (Btst CLIMBED_HEALER_TREE))
							(HighPrint 54 7)
							;Climb the tree first.
						else
							(ego setScript: (ScriptID 237 0))
						)
					)
					((or (== nestCondition nestBurnt) (== nestCondition nestOnGround))
						(HighPrint 54 8)
						;There's nothing out on the limb to risk your neck over.
					)
				)
			)
		)
	)
	
)

(instance healerDoor of Door
	(properties
		y 127
		x 209
		view vHealerOutside
		loop 1
		entranceTo 55
		locked 1
		doorControl cLRED
	)
)

(instance bird of Actor
	(properties
		y 66
		x 71
		view vHealerOutside
		loop 5
		illegalBits $0000
	)
	
	(method (init)
		(super init:)
		(mouseDownHandler add: self)
	)
	
	(method (handleEvent event)
		(cond 
			(
				(or
					(Said 'look/bird,creature,lizard')
					(MouseClaimed bird event emRIGHT_BUTTON)
				)
				(HighPrint 54 9)
			)
			((Said 'eat,get,kill,fight,chop,beat/bird') (HighPrint 54 10))
		)
	)
)

(instance nest of Actor
	(properties
		y 69
		x 73
		view vHealerOutside
		;loop 0
	)
	
	(method (handleEvent event)
		(cond 
			(
				(or
					(Said 'look/nest')
					(MouseClaimed nest event emRIGHT_BUTTON)
				)
				(cond 
					((not (Btst PTERESA_LEFT_NEST)) (HighPrint 54 11) (HighPrint 54 12))
					((== nestCondition 0)
						(HighPrint 54 13)
						(if (Btst CLIMBED_HEALER_TREE)
							(if (not (Btst OBTAINED_RING))
								(HighPrint 54 14)
								(Bset LOOKED_IN_PTERESA_NEST)
							else
								(HighPrint 54 15)
							)
						)
					)
					((== nestCondition 1) (HighPrint 54 16))
					((== nestCondition 2) (HighPrint 54 17))
				)
			)
			((Said 'get/nest')
				(switch nestCondition
					(0
						(if (Btst CLIMBED_HEALER_TREE)
							(HighPrint 54 18)
						else
							(HighPrint 54 19)
						)
					)
					(1 (HighPrint 54 20))
					(2 (HighPrint 54 21))
				)
			)
		)
	)
)

(instance whoosh of Sound
	(properties)
)

(instance rm54 of Room
	(properties
		picture 54
		style DISSOLVE
		east 56
		south 65
		west 53
	)
	
	(method (init)
		(LoadMany RES_VIEW (GetEgoViewNumber vEgoWalkTree) (GetEgoViewNumber vEgoThrowing))
		(LoadMany RES_SCRIPT 103 235 236 237)
		(super init:)
		(cSound fade:)
		(StatusLine enable:)
		(StopEgo)
		(ego init:)
		(HandsOn)
		(addToPics add: healerSign eachElementDo: #init doit:)
		(healerDoor setPri: 8 init: stopUpd:)
		(if (not (Btst OBTAINED_RING)) (ring init: setPri: 15))
		(if (not (Btst FLAG_211))
			(if (== nestCondition 0)
				(nest
					illegalBits: 0
					ignoreActors:
					setPri: 13
					cycleSpeed: 1
					init:
					stopUpd:
				)
			else
				(nest init: hide: stopUpd:)
			)
			(if (not (Btst PTERESA_LEFT_NEST))
				(bird
					ignoreActors:
					init:
					cycleSpeed: 1
					stopUpd:
					setScript: flutter
				)
			)
		)
		(switch prevRoomNum
			(37
				(ego setLoop: 2 setPri: 4 posn: 130 100)
				(= local0 1)
				(Bset VISITED_HEALERHUT_OUTSIDE)
			)
			(53
				(ego posn: 10 135 setMotion: MoveTo 72 135)
			)
			(55
				(curRoom setScript: outOfHouse)
			)
			(56
				(ego posn: 318 153 setMotion: MoveTo 295 153)
			)
			(else 
				(ego posn: 160 188 setMotion: MoveTo 160 170)
			)
		)
	)
	
	(method (doit)
		(super doit:)
		(cond 
			(local0
				(= local0 0)
				(ego setScript: (ScriptID 235 0))
			)
			((not (Btst VISITED_HEALERHUT_OUTSIDE))
				(Bset VISITED_HEALERHUT_OUTSIDE)
				(curRoom setScript: (ScriptID 235 1))
			)
		)
		(cond 
			((and
					(not local1)
					(!= nestCondition nestOnGround)
					(not (Btst OBTAINED_RING))
					(== (ego onControl: origin) cYELLOW)
				)
				(= local1 1)
				(ring cel: 0 setCycle: EndLoop)
			)
			((and local1 (!= (ego onControl: origin) cYELLOW))
				(= local1 0)
			)
		)
		(if
			(and
				(== nestCondition nestBurnt)
				(not (Btst OBTAINED_RING))
				(not (Btst FLAG_211))
				(not local5)
				(or
					(== (ego onControl: origin) cLMAGENTA)
					(== (ego onControl: origin) cYELLOW)
				)
			)
			(= local5 1)
			(ego setScript: pickItUp)
		)
		(if
			(and
				(<= (ego y?) 87)
				(not (Btst FLAG_277))
				(not (ego script?))
			)
			(HandsOff)
			(ego setScript: overTheHill)
		)
	)
	
	(method (dispose)
		(super dispose:)
	)
	
;	(method (handleEvent event &tmp temp0)
;		(asm
;			pushi    #type
;			pushi    0
;			lap      event
;			send     4
;			push    
;			dup     
;			ldi      128
;			eq?     
;			bnt      code_0f9e
;			pushi    #handleEvent
;			pushi    1
;			lsp      event
;			super    Room,  6
;			bnt      code_08f1
;			jmp      code_0f9e
;code_08f1:
;			pushi    1
;			pushi    278
;			callb    Btst,  2
;			bnt      code_091f
;			pushi    1
;			lofsa    'throw,run,walk,sneak'
;			push    
;			callk    Said,  2
;			bnt      code_091f
;			pushi    3
;			pushi    4
;			pushi    54
;			pushi    22
;			callb    TimedPrint,  6
;			pushi    #setScript
;			pushi    1
;			lofsa    treeFall
;			push    
;			lag      ego
;			send     6
;			jmp      code_0f9e
;code_091f:
;			pushi    1
;			lofsa    'wear/ring'
;			push    
;			callk    Said,  2
;			bt       code_0940
;			pushi    1
;			lofsa    'put[<on]/ring,finger'
;			push    
;			callk    Said,  2
;			bt       code_0940
;			pushi    1
;			lofsa    'put/ring/finger'
;			push    
;			callk    Said,  2
;			bnt      code_095f
;code_0940:
;			pushi    #has
;			pushi    1
;			pushi    19
;			lag      ego
;			send     6
;			bnt      code_0958
;			pushi    2
;			pushi    54
;			pushi    23
;			callb    HighPrint,  4
;			jmp      code_0f9e
;code_0958:
;			pushi    0
;			callb    PrintDontHaveIt,  0
;			jmp      code_0f9e
;code_095f:
;			pushi    1
;			lofsa    'cast>'
;			push    
;			callk    Said,  2
;			bnt      code_0a87
;			pushi    1
;			pushi    278
;			callb    Btst,  2
;			bnt      code_0992
;			pushi    2
;			pushi    54
;			pushi    24
;			callb    HighPrint,  4
;			pushi    #setScript
;			pushi    1
;			lofsa    treeFall
;			push    
;			lag      ego
;			send     6
;			pushi    #claimed
;			pushi    1
;			pushi    1
;			lap      event
;			send     6
;			jmp      code_0f9e
;code_0992:
;			pushi    1
;			lsp      event
;			calle    SaidCast,  2
;			sat      temp0
;			pushi    1
;			push    
;			callb    CastSpell,  2
;			bnt      code_0f9e
;			lst      temp0
;			dup     
;			ldi      20
;			eq?     
;			bnt      code_09cf
;			pushi    1
;			lsg      ego
;			calle    AnimateDazzle,  2
;			pushi    1
;			pushi    212
;			callb    Btst,  2
;			not     
;			bnt      code_0a83
;			pushi    #setScript
;			pushi    1
;			lofsa    flyAway
;			push    
;			lofsa    bird
;			send     6
;			jmp      code_0a83
;code_09cf:
;			dup     
;			ldi      23
;			eq?     
;			bnt      code_0a16
;			lsg      nestCondition
;			ldi      0
;			eq?     
;			bnt      code_09ed
;			pushi    #setScript
;			pushi    1
;			lofsa    nestBurn
;			push    
;			lag      ego
;			send     6
;			jmp      code_0a83
;code_09ed:
;			pushi    #inRect
;			pushi    4
;			pushi    182
;			pushi    0
;			pushi    319
;			pushi    106
;			lag      ego
;			send     12
;			bnt      code_0a0d
;			pushi    2
;			pushi    54
;			pushi    25
;			callb    HighPrint,  4
;			jmp      code_0a83
;code_0a0d:
;			pushi    1
;			pushi    0
;			calle    AnimateThrowingFlameDart,  2
;			jmp      code_0a83
;code_0a16:
;			dup     
;			ldi      24
;			eq?     
;			bnt      code_0a7b
;			lsg      nestCondition
;			ldi      0
;			eq?     
;			bnt      code_0a70
;			pushi    1
;			pushi    OBTAINED_RING
;			callb    Btst,  2
;			not     
;			bnt      code_0a70
;			pushi    #onControl
;			pushi    1
;			pushi    1
;			lag      ego
;			send     6
;			push    
;			ldi      8192
;			eq?     
;			bt       code_0a53
;			pushi    #onControl
;			pushi    1
;			pushi    1
;			lag      ego
;			send     6
;			push    
;			ldi      16384
;			eq?     
;			bnt      code_0a65
;code_0a53:
;			ldi      1
;			sal      local4
;			pushi    #setScript
;			pushi    1
;			lofsa    throwIt
;			push    
;			lag      ego
;			send     6
;			jmp      code_0a83
;code_0a65:
;			pushi    2
;			pushi    54
;			pushi    26
;			callb    HighPrint,  4
;			jmp      code_0a83
;code_0a70:
;			pushi    2
;			pushi    54
;			pushi    27
;			callb    HighPrint,  4
;			jmp      code_0a83
;code_0a7b:
;			pushi    #claimed
;			pushi    1
;			pushi    0
;			lap      event
;			send     6
;code_0a83:
;			toss    
;			jmp      code_0f9e
;code_0a87:
;			pushi    1
;			lofsa    'open/door'
;			push    
;			callk    Said,  2
;			bnt      code_0ab5
;			pushi    #onControl
;			pushi    1
;			pushi    1
;			lag      ego
;			send     6
;			push    
;			ldi      4096
;			eq?     
;			bnt      code_0aae
;			pushi    2
;			pushi    54
;			pushi    28
;			callb    HighPrint,  4
;			jmp      code_0f9e
;code_0aae:
;			pushi    0
;			callb    PrintNotCloseEnough,  0
;			jmp      code_0f9e
;code_0ab5:
;			pushi    1
;			lofsa    'lockpick,unlock/hasp,door'
;			push    
;			callk    Said,  2
;			bnt      code_0aeb
;			pushi    3
;			pushi    9
;			pushi    10
;			pushi    0
;			callb    TrySkill,  6
;			bnt      code_0ad8
;			pushi    2
;			pushi    54
;			pushi    29
;			callb    HighPrint,  4
;			jmp      code_0f9e
;code_0ad8:
;			pushi    2
;			pushi    54
;			pushi    30
;			callb    HighPrint,  4
;			pushi    2
;			pushi    54
;			pushi    31
;			callb    HighPrint,  4
;			jmp      code_0f9e
;code_0aeb:
;			pushi    1
;			lofsa    'knock[/door]'
;			push    
;			callk    Said,  2
;			bnt      code_0bd7
;			pushi    #onControl
;			pushi    1
;			pushi    1
;			lag      ego
;			send     6
;			push    
;			ldi      4096
;			eq?     
;			not     
;			bnt      code_0b0f
;			pushi    0
;			callb    PrintNotCloseEnough,  0
;			jmp      code_0f9e
;code_0b0f:
;			lag      isNightTime
;			bnt      code_0b1f
;			pushi    2
;			pushi    54
;			pushi    32
;			callb    HighPrint,  4
;			jmp      code_0f9e
;code_0b1f:
;			pushi    1
;			pushi    STOLE_HEALER_POTIONS
;			callb    Btst,  2
;			bnt      code_0b89
;			lsg      prevRoomNum
;			ldi      55
;			ne?     
;			bnt      code_0b44
;			pushi    2
;			pushi    54
;			pushi    33
;			callb    HighPrint,  4
;			pushi    2
;			pushi    54
;			pushi    34
;			callb    HighPrint,  4
;			jmp      code_0f9e
;code_0b44:
;			pushi    #setLoop
;			pushi    1
;			pushi    3
;			lag      ego
;			send     6
;			pushi    2
;			pushi    54
;			pushi    35
;			callb    HighPrint,  4
;			pushi    2
;			pushi    54
;			pushi    36
;			callb    HighPrint,  4
;			pushi    0
;			callb    HandsOff,  0
;			pushi    336
;			pushi    1
;			pushi    #loop
;			pushi    0
;			lag      ego
;			send     4
;			push    
;			ldi      3
;			eq?     
;			bnt      code_0b79
;			ldi      3
;			jmp      code_0b7b
;code_0b79:
;			ldi      0
;code_0b7b:
;			push    
;			pushi    331
;			pushi    1
;			pushi    0
;			lofsa    healerDoor
;			send     12
;			jmp      code_0f9e
;code_0b89:
;			pushi    #setLoop
;			pushi    1
;			pushi    3
;			lag      ego
;			send     6
;			pushi    2
;			pushi    54
;			pushi    35
;			callb    HighPrint,  4
;			pushi    2
;			pushi    54
;			pushi    36
;			callb    HighPrint,  4
;			pushi    0
;			callb    HandsOff,  0
;			pushi    #setPri
;			pushi    1
;			pushi    9
;			lag      ego
;			send     6
;			pushi    336
;			pushi    1
;			pushi    #loop
;			pushi    0
;			lag      ego
;			send     4
;			push    
;			ldi      3
;			eq?     
;			bnt      code_0bc7
;			ldi      3
;			jmp      code_0bc9
;code_0bc7:
;			ldi      0
;code_0bc9:
;			push    
;			pushi    331
;			pushi    1
;			pushi    0
;			lofsa    healerDoor
;			send     12
;			jmp      code_0f9e
;code_0bd7:
;			pushi    1
;			lofsa    'throw>'
;			push    
;			callk    Said,  2
;			bnt      code_0c7a
;			pushi    1
;			lofsa    '/boulder[/nest]'
;			push    
;			callk    Said,  2
;			bnt      code_0c67
;			pushi    #has
;			pushi    1
;			pushi    21
;			lag      ego
;			send     6
;			not     
;			bnt      code_0c06
;			pushi    2
;			pushi    54
;			pushi    37
;			callb    HighPrint,  4
;			jmp      code_0f9e
;code_0c06:
;			lsg      nestCondition
;			ldi      0
;			ne?     
;			bt       code_0c32
;			pushi    #onControl
;			pushi    1
;			pushi    1
;			lag      ego
;			send     6
;			push    
;			ldi      8192
;			eq?     
;			bt       code_0c2e
;			pushi    #onControl
;			pushi    1
;			pushi    1
;			lag      ego
;			send     6
;			push    
;			ldi      16384
;			eq?     
;code_0c2e:
;			not     
;			bnt      code_0c43
;code_0c32:
;			pushi    2
;			pushi    54
;			pushi    38
;			callb    HighPrint,  4
;			pushi    1
;			pushi    0
;			calle    AnimateThrowingRock,  2
;			jmp      code_0f9e
;code_0c43:
;			pushi    #script
;			pushi    0
;			lofsa    rock
;			send     4
;			bnt      code_0c59
;			pushi    2
;			pushi    54
;			pushi    39
;			callb    HighPrint,  4
;			jmp      code_0f9e
;code_0c59:
;			pushi    #setScript
;			pushi    1
;			lofsa    throwIt
;			push    
;			lag      ego
;			send     6
;			jmp      code_0f9e
;code_0c67:
;			pushi    2
;			pushi    54
;			pushi    40
;			callb    HighPrint,  4
;			pushi    #claimed
;			pushi    1
;			pushi    1
;			lap      event
;			send     6
;			jmp      code_0f9e
;code_0c7a:
;			pushi    1
;			lofsa    'climb>'
;			push    
;			callk    Said,  2
;			bnt      code_0d90
;			pushi    1
;			lofsa    '/tree,oak'
;			push    
;			callk    Said,  2
;			bt       code_0c9b
;			pushi    1
;			lofsa    '<up'
;			push    
;			callk    Said,  2
;			bnt      code_0d1e
;code_0c9b:
;			pushi    #inRect
;			pushi    4
;			pushi    0
;			pushi    103
;			pushi    78
;			pushi    147
;			lag      ego
;			send     12
;			bnt      code_0cba
;			pushi    2
;			pushi    54
;			pushi    41
;			callb    HighPrint,  4
;			jmp      code_0f9e
;code_0cba:
;			pushi    #inRect
;			pushi    4
;			pushi    0
;			pushi    167
;			pushi    112
;			pushi    189
;			lag      ego
;			send     12
;			bnt      code_0d02
;			pushi    3
;			pushi    11
;			pushi    30
;			pushi    0
;			callb    TrySkill,  6
;			bnt      code_0cef
;			pushi    #setScript
;			pushi    1
;			pushi    2
;			pushi    236
;			pushi    0
;			callk    ScriptID,  4
;			push    
;			lag      ego
;			send     6
;			jmp      code_0f9e
;code_0cef:
;			pushi    #setScript
;			pushi    1
;			pushi    2
;			pushi    236
;			pushi    1
;			callk    ScriptID,  4
;			push    
;			lag      ego
;			send     6
;			jmp      code_0f9e
;code_0d02:
;			pushi    1
;			pushi    278
;			callb    Btst,  2
;			bnt      code_0d17
;			pushi    2
;			pushi    54
;			pushi    42
;			callb    HighPrint,  4
;			jmp      code_0f9e
;code_0d17:
;			pushi    0
;			callb    PrintNotCloseEnough,  0
;			jmp      code_0f9e
;code_0d1e:
;			pushi    1
;			lofsa    '<down'
;			push    
;			callk    Said,  2
;			bnt      code_0d51
;			pushi    1
;			pushi    278
;			callb    Btst,  2
;			bnt      code_0d46
;			pushi    #setScript
;			pushi    1
;			pushi    2
;			pushi    237
;			pushi    1
;			callk    ScriptID,  4
;			push    
;			lag      ego
;			send     6
;			jmp      code_0f9e
;code_0d46:
;			pushi    2
;			pushi    54
;			pushi    43
;			callb    HighPrint,  4
;			jmp      code_0f9e
;code_0d51:
;			pushi    1
;			lofsa    '/branch'
;			push    
;			callk    Said,  2
;			bnt      code_0d67
;			pushi    2
;			pushi    54
;			pushi    8
;			callb    HighPrint,  4
;			jmp      code_0f9e
;code_0d67:
;			pushi    1
;			lofsa    '[/!*]'
;			push    
;			callk    Said,  2
;			bnt      code_0d7d
;			pushi    2
;			pushi    54
;			pushi    44
;			callb    HighPrint,  4
;			jmp      code_0f9e
;code_0d7d:
;			pushi    2
;			pushi    54
;			pushi    45
;			callb    HighPrint,  4
;			pushi    #claimed
;			pushi    1
;			pushi    1
;			lap      event
;			send     6
;			jmp      code_0f9e
;code_0d90:
;			pushi    1
;			lofsa    'grab,get,lockpick/herb,flower,plant'
;			push    
;			callk    Said,  2
;			bnt      code_0da6
;			pushi    2
;			pushi    54
;			pushi    46
;			callb    HighPrint,  4
;			jmp      code_0f9e
;code_0da6:
;			pushi    1
;			lofsa    'get>'
;			push    
;			callk    Said,  2
;			bnt      code_0dfa
;			pushi    1
;			lofsa    '/boulder'
;			push    
;			callk    Said,  2
;			bnt      code_0dce
;			pushi    #setScript
;			pushi    1
;			pushi    2
;			pushi    103
;			pushi    0
;			callk    ScriptID,  4
;			push    
;			lag      ego
;			send     6
;			jmp      code_0f9e
;code_0dce:
;			pushi    1
;			lofsa    '/bird,lizard,creature'
;			push    
;			callk    Said,  2
;			bnt      code_0de4
;			pushi    2
;			pushi    54
;			pushi    47
;			callb    HighPrint,  4
;			jmp      code_0f9e
;code_0de4:
;			pushi    1
;			lofsa    '/nest'
;			push    
;			callk    Said,  2
;			bnt      code_0f9e
;			pushi    2
;			pushi    54
;			pushi    47
;			callb    HighPrint,  4
;			jmp      code_0f9e
;code_0dfa:
;			pushi    1
;			lofsa    'look>'
;			push    
;			callk    Said,  2
;			bnt      code_0f9e
;			pushi    1
;			lofsa    '/bird,creature,lizard'
;			push    
;			callk    Said,  2
;			bnt      code_0e1b
;			pushi    2
;			pushi    54
;			pushi    47
;			callb    HighPrint,  4
;			jmp      code_0f9e
;code_0e1b:
;			pushi    1
;			lofsa    '/nest'
;			push    
;			callk    Said,  2
;			bnt      code_0e31
;			pushi    2
;			pushi    54
;			pushi    48
;			callb    HighPrint,  4
;			jmp      code_0f9e
;code_0e31:
;			pushi    1
;			lofsa    '[<at,around][/road]'
;			push    
;			callk    Said,  2
;			bnt      code_0e4f
;			pushi    2
;			pushi    54
;			pushi    49
;			callb    HighPrint,  4
;			pushi    2
;			pushi    54
;			pushi    50
;			callb    HighPrint,  4
;			jmp      code_0f9e
;code_0e4f:
;			pushi    1
;			lofsa    '/house,hut'
;			push    
;			callk    Said,  2
;			bnt      code_0e65
;			pushi    2
;			pushi    54
;			pushi    51
;			callb    HighPrint,  4
;			jmp      code_0f9e
;code_0e65:
;			pushi    1
;			lofsa    '/sign'
;			push    
;			callk    Said,  2
;			bnt      code_0e7b
;			pushi    2
;			pushi    54
;			pushi    52
;			callb    HighPrint,  4
;			jmp      code_0f9e
;code_0e7b:
;			pushi    1
;			lofsa    '/tree,oak'
;			push    
;			callk    Said,  2
;			bnt      code_0ea1
;			pushi    2
;			pushi    54
;			pushi    53
;			callb    HighPrint,  4
;			lsg      nestCondition
;			ldi      0
;			eq?     
;			bnt      code_0f9e
;			pushi    2
;			pushi    54
;			dup     
;			callb    HighPrint,  4
;			jmp      code_0f9e
;code_0ea1:
;			pushi    1
;			lofsa    'branch'
;			push    
;			callk    Said,  2
;			bnt      code_0eb7
;			pushi    2
;			pushi    54
;			pushi    55
;			callb    HighPrint,  4
;			jmp      code_0f9e
;code_0eb7:
;			pushi    1
;			lofsa    '/roof'
;			push    
;			callk    Said,  2
;			bnt      code_0ecd
;			pushi    2
;			pushi    54
;			pushi    56
;			callb    HighPrint,  4
;			jmp      code_0f9e
;code_0ecd:
;			pushi    1
;			lofsa    '/garden,flower,plant,herb'
;			push    
;			callk    Said,  2
;			bnt      code_0ee3
;			pushi    2
;			pushi    54
;			pushi    57
;			callb    HighPrint,  4
;			jmp      code_0f9e
;code_0ee3:
;			pushi    1
;			lofsa    '/window,bottle,pan,curtain'
;			push    
;			callk    Said,  2
;			bnt      code_0f49
;			pushi    #inRect
;			pushi    4
;			pushi    182
;			pushi    0
;			pushi    319
;			pushi    112
;			lag      ego
;			send     12
;			bt       code_0f27
;			pushi    #inRect
;			pushi    4
;			pushi    287
;			pushi    0
;			pushi    319
;			pushi    150
;			lag      ego
;			send     12
;			bnt      code_0f42
;			pushi    #loop
;			pushi    0
;			lag      ego
;			send     4
;			push    
;			ldi      3
;			eq?     
;			bnt      code_0f42
;code_0f27:
;			lag      isNightTime
;			bnt      code_0f37
;			pushi    2
;			pushi    54
;			pushi    58
;			callb    HighPrint,  4
;			jmp      code_0f9e
;code_0f37:
;			pushi    2
;			pushi    54
;			pushi    59
;			callb    HighPrint,  4
;			jmp      code_0f9e
;code_0f42:
;			pushi    0
;			callb    PrintNotCloseEnough,  0
;			jmp      code_0f9e
;code_0f49:
;			pushi    1
;			lofsa    '/east,forest'
;			push    
;			callk    Said,  2
;			bnt      code_0f5f
;			pushi    2
;			pushi    54
;			pushi    60
;			callb    HighPrint,  4
;			jmp      code_0f9e
;code_0f5f:
;			pushi    1
;			lofsa    '/west,field'
;			push    
;			callk    Said,  2
;			bnt      code_0f75
;			pushi    2
;			pushi    54
;			pushi    61
;			callb    HighPrint,  4
;			jmp      code_0f9e
;code_0f75:
;			pushi    1
;			lofsa    '/north,castle'
;			push    
;			callk    Said,  2
;			bnt      code_0f8b
;			pushi    2
;			pushi    54
;			pushi    62
;			callb    HighPrint,  4
;			jmp      code_0f9e
;code_0f8b:
;			pushi    1
;			lofsa    '/south,crossroad'
;			push    
;			callk    Said,  2
;			bnt      code_0f9e
;			pushi    2
;			pushi    54
;			pushi    63
;			callb    HighPrint,  4
;code_0f9e:
;			toss    
;			ret     
;		)
;	)


	;CI: NOTE: This was downloaded from Eric's Decompilation Archive
	(method (handleEvent event &tmp spell)
		(switch (event type?)
			(speechEvent 
				(cond 
					((super handleEvent: event))
					((and (Btst CLIMBED_HEALER_TREE) (Said 'throw,run,walk,sneak'))
						(TimedPrint 4 54 22)
						;Ooooops!
						(ego setScript: treeFall)
					)
					((or	(Said 'wear/ring')
							(Said 'put[<on]/ring,finger')
							(Said 'put/ring/finger')
						)
						(if (ego has: invRing)
							(HighPrint 54 23)
							;You think about it, but you decide to keep it in your pack.
						else
							(PrintDontHaveIt)
						)
					)
					((Said 'cast>')
						(if (Btst CLIMBED_HEALER_TREE)
							(HighPrint 54 24)
							;As you try to cast a spell from your high perch on the tree, you lose your concentration, and...
							(ego setScript: treeFall)
							(event claimed: TRUE)
						else
							(= spell (SaidCast event))
							;CI: NOTE: This was originally a bug where the spell was cast, *then* the code was checked if you could cast the spell
							;It was found by 8bitKittyKat on 8 AUG 2022.
							;see https://sciprogramming.com/community/index.php?topic=2076.msg15503
							;I have fixed the spell by switching on the spell the user typed, then trying to cast the spell
							;(if (CastSpell spell)
							(switch spell
								(DAZZLE
									(if (CastSpell spell) ;CI: added
										(AnimateDazzle ego)
										(if (not (Btst PTERESA_LEFT_NEST))
											(bird setScript: flyAway)
										)
									)
								)
								(FLAMEDART
									(cond 
										((== nestCondition nestInTree)
											(if (CastSpell spell) ;CI: added
												(ego setScript: nestBurn)
											)
										)
										((ego inRect: 182 0 319 106)
											(HighPrint 54 25)
											;You don't have a clear shot.
										)
										(else
											(if (CastSpell spell) ;CI: added 
												(AnimateThrowingFlameDart NULL)
											)
										)
									)
								)
								(FETCH
									(if (and (== nestCondition nestInTree) (not (Btst OBTAINED_RING)))
										(if
											(or
												(== (ego onControl: origin) cLMAGENTA)
												(== (ego onControl: origin) cYELLOW)
											)
											(if (CastSpell spell) ;CI: added
												(= local4 TRUE)
												(ego setScript: throwIt)
											)
										else
											(HighPrint 54 26)
											;Move to a better position.
										)
									else
										(if (CastSpell spell) ;CI: added
											(HighPrint 54 27)
											;That spell is no longer useful here (but you did get some practice).
										)
									)
								)
								(else 
									(event claimed: FALSE)
								)
							)
							;)
						)
					)
					((Said 'open/door')
						(if (== (ego onControl: origin) cLRED)
							(HighPrint 54 28)
							;The door seems to be barred on the inside.  Perhaps you should knock.
						else
							(PrintNotCloseEnough)
						)
					)
					((Said 'lockpick,unlock/hasp,door')
						(if (TrySkill PICK 10 0)
							(HighPrint 54 29)
							;Your lock-picking skill will do you no good here.  The door is barred from the inside.
						else
							(HighPrint 54 30)
							;The door appears to be barred on the inside.
							(HighPrint 54 31)
							;Your skill in lock picking is insufficient to open a sardine can -- with a key!
						)
					)
					((Said 'knock[/door]')
						(cond 
							((not (== (ego onControl: origin) cLRED))
								(PrintNotCloseEnough)
							)
							(isNightTime
								(HighPrint 54 32)
								;There is no answer.
							)
							((Btst STOLE_HEALER_POTIONS)
								(if (!= prevRoomNum 55)
									(HighPrint 54 33)
									;You hear the voice of the healer saying:  "The last time you were here, I found I was missing some potions."
									(HighPrint 54 34)
									;"I don't like thievery.  Go away!"
								else
									(ego setLoop: loopN)
									(HighPrint 54 35)
									;You hear the inside bolt slide open.
									(HighPrint 54 36)
									;"Come on in."
									(HandsOff)
									(healerDoor
										facingLoop: (if (== (ego loop?) loopN) loopN else loopE)
										locked: FALSE
									)
								)
							)
							(else
								(ego setLoop: loopN)
								(HighPrint 54 35)
								;You hear the inside bolt slide open.
								(HighPrint 54 36)
								;"Come on in."
								(HandsOff)
								(ego setPri: 9)
								(healerDoor
									facingLoop: (if (== (ego loop?) loopN) loopN else loopE)
									locked: 0
								)
							)
						)
					)
					((Said 'throw>')
						(if (Said '/boulder[/nest]')
							(cond
								((not (ego has: iRock))
									(HighPrint 54 37)
								)
								((rock script?)
									(HighPrint 54 39)
								)
								(
									(or
										(== (ego onControl: origin) cLMAGENTA)
										(== (ego onControl: origin) cYELLOW)
									)
									(if (== nestCondition nestInTree)
									 	(ego setScript: throwIt)
									 else
										(HighPrint 54 38)
										(AnimateThrowingRock NULL)
									 )
								)
								(else
									(HighPrint 54 40)
									(event claimed: TRUE)
								)
							)
						)
					)
					((Said 'climb>')
						(cond 
							((or (Said '/tree,oak') (Said '<up'))
								(cond 
									((ego inRect: 0 103 78 147)
										(HighPrint 54 41)
									)
									((ego inRect: 0 167 112 189)
										(if (TrySkill CLIMB 30 0)
											(ego setScript: (ScriptID 236 0))
										else
											(ego setScript: (ScriptID 236 1))
										)
									)
									((Btst CLIMBED_HEALER_TREE)
										(HighPrint 54 42)
									)
									(else 
										(PrintNotCloseEnough)
									)
								)
							)
							((Said '<down')
								(if (Btst CLIMBED_HEALER_TREE)
									(ego setScript: (ScriptID 237 1))
								else
									(HighPrint 54 43)
								)
							)
							((Said '/branch')
								(HighPrint 54 8)
							)
							((Said '[/!*]')
								(HighPrint 54 44)
							)
							(else 
								(HighPrint 54 45) 
								(event claimed: TRUE)
							)
						)
					)
					((Said 'grab,get,lockpick/herb,flower,plant') (HighPrint 54 46))
					((Said 'get>')
						(cond 
							((Said '/boulder') (ego setScript: (ScriptID 103 0)))
							((Said '/bird,lizard,creature') (HighPrint 54 47))
							((Said '/nest') (HighPrint 54 47))
						)
					)
					((Said 'look>')
						(cond 
							((Said '/bird,creature,lizard') (HighPrint 54 47))
							((Said '/nest') (HighPrint 54 48))
							((Said '[<at,around][/road]') (HighPrint 54 49) (HighPrint 54 50))
							((Said '/house,hut') (HighPrint 54 51))
							((Said '/sign') (HighPrint 54 52))
							((Said '/tree,oak')
								(HighPrint 54 53)
								(if (== nestCondition nestInTree) (HighPrint 54 54))
							)
							((Said 'branch') (HighPrint 54 55))
							((Said '/roof') (HighPrint 54 56))
							((Said '/garden,flower,plant,herb') (HighPrint 54 57))
							((Said '/window,bottle,pan,curtain')
								(if
									(or
										(ego inRect: 182 0 319 112)
										(and (ego inRect: 287 0 319 150) (== (ego loop?) loopN))
									)
									(if isNightTime
										(HighPrint 54 58)
									else
										(HighPrint 54 59)
									)
								else
									(PrintNotCloseEnough)
								)
							)
							((Said '/east,forest') (HighPrint 54 60))
							((Said '/west,field') (HighPrint 54 61))
							((Said '/north,castle') (HighPrint 54 62))
							((Said '/south,crossroad') (HighPrint 54 63))
						)
					)
				)
			)
		)
	)


)

(instance overTheHill of Script
	(properties)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(ego
					illegalBits: 0
					setPri: 4
					setLoop: 3
					setCycle: Forward
					setMotion: MoveTo (ego x?) 100 self
				)
			)
			(1 (curRoom newRoom: 37))
		)
	)
)

(instance flyAway of Script
	(properties)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(bird setLoop: 3 cel: 0 posn: 73 64)
				(= cycles 1)
			)
			(1 (bird setCycle: EndLoop self))
			(2
				(bird
					setPri: 10
					setCycle: 0
					setStep: 5 3
					setMotion: MoveTo 31 77 self
				)
			)
			(3 (Bset PTERESA_LEFT_NEST) (bird dispose:))
		)
	)
)

(instance nestBurn of Script
	(properties)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(HandsOff)
				(if (not (Btst PTERESA_LEFT_NEST)) (bird setScript: flyAway))
				(ego
					view: (GetEgoViewNumber vEgoMagicFlameDart)
					setLoop: (if (< (ego x?) 73) 0 else 1)
					cel: 0
					setCycle: CycleTo 5 cdFORWARD self
				)
			)
			(1
				(whoosh
					number: (GetSongNumber 33)
					loop: 1
					priority: 3
					init:
					play:
				)
				(ego setCycle: EndLoop)
				(fireDart
					posn: (ego x?) (- (ego y?) 25)
					init:
					setLoop: 2
					setStep: 24 10
					ignoreActors:
					setCycle: Forward
					setMotion: MoveTo (nest x?) (nest y?) self
				)
			)
			(2
				(whoosh dispose:)
				(nest setScript: burnUp)
				(fireDart setLoop: 3 setPri: 13 setCycle: EndLoop self)
			)
			(3
				(fireDart dispose:)
				(StopEgo)
				(client setScript: 0)
			)
		)
	)
)

(instance burnUp of Script
	(properties)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(nest setPri: 12 setCycle: CycleTo 3 cdFORWARD self)
			)
			(1
				(++ local3)
				(nest
					posn: (nest x?) (+ (nest y?) 6)
					cel: (if (== (nest cel?) 3) 4 else 3)
				)
				(= cycles 1)
			)
			(2
				(if (< local3 15)
					(self changeState: 1)
				else
					(self cue:)
				)
			)
			(3
				(nest
					posn: (nest x?) (+ (nest y?) 3)
					cel: (if (== (nest cel?) 3) 4 else 3)
				)
				(= cycles 1)
			)
			(4 (nest setCycle: EndLoop self))
			(5
				(if (Btst OBTAINED_RING)
					(HandsOn)
					(nest hide: setScript: 0)
					(ring hide:)
				else
					(= seconds 3)
				)
			)
			(6
				(ring
					posn: (nest x?) (nest y?)
					setPri: 12
					setCycle: EndLoop self
				)
			)
			(7
				(= nestCondition 2)
				(HandsOn)
				(nest stopUpd: setScript: 0)
			)
		)
	)
)

(instance throwIt of Script
	(properties)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(HandsOff)
				(ego setMotion: MoveTo 235 170 self)
			)
			(1
				(ego
					view: (if local4 (GetEgoViewNumber vEgoMagicFetch) else (GetEgoViewNumber vEgoThrowing))
					cycleSpeed: 1
					setLoop: (if local4 0 else 2)
					cel: 0
				)
				(= cycles 2)
			)
			(2
				(if local4
					(ego setCycle: EndLoop self)
				else
					(ego setCycle: CycleTo 4 cdFORWARD self)
				)
				(if (not (Btst PTERESA_LEFT_NEST)) (bird setScript: flyAway))
			)
			(3
				(if local4
					(magicLasso
						ignoreActors:
						posn: (+ (ego x?) 2) (- (ego y?) 36)
						setLoop: 5
						setStep: 20 10
						setCycle: Forward
						init:
						setScript: lassoNest
					)
					(client setScript: 0)
				else
					(rock
						setLoop: 4
						setPri: 15
						setStep: 25 10
						ignoreActors:
						ignoreHorizon:
						illegalBits: 0
						setCycle: Forward
						posn: (- (ego x?) 13) (- (ego y?) 34)
						init:
						forceUpd:
					)
					(ego setCycle: EndLoop self)
					(if (TrySkill THROW tryThrowNest 0)
						(rock setScript: (ScriptID 235 2))
					else
						(HandsOn)
						(rock setScript: (ScriptID 235 3))
					)
				)
			)
			(4
				(StopEgo)
				(ego use: iRock 1 loop: 1)
				(client setScript: 0)
			)
		)
	)
)

(instance pickItUp of Script
	(properties)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(if local5
					(HandsOff)
					(if (not (Btst OBTAINED_RING))
						(HighPrint 54 64)
						;You see the glinting object in the remains of the nest.
					else
						(HighPrint 54 65)
						;You see nothing in the remains of the nest.
					)
					(ego illegalBits: 0 setMotion: MoveTo 115 170 self)
				else
					(self cue:)
				)
			)
			(1
				(StopEgo)
				(ego
					illegalBits: 0
					setMotion: MoveTo (+ (nest x?) 15) 164 self
				)
			)
			(2
				(ego
					view: (GetEgoViewNumber vEgoThrowing)
					setLoop: 1
					cel: 0
					cycleSpeed: 1
					setCycle: EndLoop
				)
				(= cycles 8)
			)
			(3
				(if (not (Btst OBTAINED_RING))
					(HighPrint 54 66)
					;You pick up a shiny gold ring.
					(ring hide:))
				(if (== (nest loop?) 7)
					(nest setCel: 1)
				else
					(nest hide:)
				)
				(ego setCycle: BegLoop self)
			)
			(4
				(StopEgo)
				(ego
					illegalBits: 0
					setMotion: MoveTo (+ (ego x?) 20) (ego y?) self
				)
			)
			(5
				(ego illegalBits: cWHITE)
				(if (not (Btst OBTAINED_RING))
					(ego get: invRing)
					(Bset OBTAINED_RING)
					(SolvePuzzle POINTS_GETGOLDRING 3)
				)
				(HandsOn)
				(client setScript: 0)
			)
		)
	)
)

(instance lassoNest of Script
	(properties)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(magicLasso setMotion: MoveTo (nest x?) (nest y?) self)
			)
			(1
				(magicLasso
					setPri: 11
					setStep: 6 4
					setMotion: MoveTo (+ (ego x?) 2) (- (ego y?) 36) self
				)
				(nest
					setPri: 11
					setStep: 6 4
					setMotion: MoveTo (+ (ego x?) 2) (- (ego y?) 38)
				)
			)
			(2 (= seconds 3))
			(3
				(magicLasso hide:)
				(nest hide:)
				(ego setLoop: 2 cel: 0 setCycle: EndLoop self)
			)
			(4
				(if (Btst OBTAINED_RING)
					(HighPrint 54 67)
					;You place the nest on the ground. It is of no use to you.
				else
					(HighPrint 54 68)
					;You take a lovely gold ring from the nest.
					(HighPrint 54 69)
					;You place the ring into your pack and the nest on the ground.
					(Bset OBTAINED_RING)
					(ego get: invRing)
					(ring hide:)
					(SolvePuzzle POINTS_GETGOLDRING 3)
				)
				(ego loop: 1)
				(= nestCondition 1)
				(nest setLoop: 7 setCel: 0 posn: 242 163 setPri: RELEASE show:)
				(StopEgo)
				(HandsOn)
				(= cycles 2)
			)
			(5 (magicLasso dispose:))
		)
	)
)

(instance nestDown of Script
	(properties)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(nest
					setLoop: 4
					setStep: 15 7
					cel: 0
					setCycle: EndLoop
					setMotion: MoveTo (nest x?) (+ (nest y?) 42) self
				)
			)
			(1
				(if (< (nest y?) 150)
					(self changeState: 0)
				else
					(self cue:)
				)
			)
			(2
				(nest
					setLoop: 7
					setCel: 0
					setPri: RELEASE
					posn: (nest x?) (+ (nest y?) 9)
					stopUpd:
				)
				(= nestCondition 1)
				(= seconds 1)
			)
			(3
				(if (Btst OBTAINED_RING)
					(self cue:)
				else
					(ring
						posn: (- (nest x?) 1) (nest y?)
						setPri: 12
						setCycle: EndLoop self
					)
				)
			)
			(4
				(if (not (Btst OBTAINED_RING))
					(HighPrint 54 64)
					;You see the glinting object in the remains of the nest.
					(ego setScript: pickItUp)
				else
					(HighPrint 54 70)
					;You see nothing in the nest.
					(ego setScript: 0)
					(HandsOn)
				)
				(nest setScript: 0)
			)
		)
	)
)

(instance treeFall of Script
	(properties)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(HandsOff)
				(ego
					view: (GetEgoViewNumber vEgoClimbing)
					setLoop: 2
					cel: 0
					x: (- (ego x?) 12)
					setStep: 0 5
					cycleSpeed: 3
					moveSpeed: 0
					setCycle: EndLoop
					setMotion: MoveTo (ego x?) 170 self
				)
			)
			(1
				(if (not (TakeDamage 8))
					(EgoDead DIE_RETRY DIE_FALL_HEALERTREE 54 71
						#icon (GetEgoViewNumber vEgoClimbing) 2 5
						#title {Your figure remains still and silent.}
						;Had you been healthier, you probably could have survived that fall.
						;In your weakened condition, however, you succumbed to your injuries.
					)
					;restore hero's HP/SP/MP
					(ego setHeroRetry: cycleSpeed: 1 posn: (+ (ego x?) 11) (+ (ego y?) 18))
					(StopEgo)
					(Bclr CLIMBED_HEALER_TREE)
					(HandsOn)
					(client setScript: NULL)
				else
					(ego
						view: (GetEgoViewNumber vEgoDefeated)
						setLoop: 4
						cel: 0
						x: (+ (ego x?) 11)
						y: (+ (ego y?) 18)
						setPri: 13
						illegalBits: cWHITE
					)
					(switch (= fallOffTree (Random 1 5))
						(0
							(HighPrint 54 72)
							;Experience is the best teacher.
							)
						(1
							(HighPrint 54 73)
							;Practice makes perfect.
							)
						(2
							(HighPrint 54 74)
							;Try, try again etc...
							)
						(3
							(HighPrint 54 75)
							;Take a break.  It's Mueller time.
							)
						(else
							(HighPrint 54 76)
							;Remember what happened to Humpty Dumpty.
							)
					)
					(++ fallOffTree)
					(self cue:)
				)
			)
			(2
				(ego cycleSpeed: 1 setCycle: EndLoop self)
			)
			(3
				(if (not saidOnALimb)
					(= saidOnALimb TRUE)
					(HighPrint 54 77)
					;That's what happens sometimes when you go out on a limb.
				)
				(Bclr CLIMBED_HEALER_TREE)
				(StopEgo)
				(HandsOn)
				(client setScript: NULL)
			)
		)
	)
)

(instance flutter of Script
	(properties)
	
	(method (changeState newState)
		(switch (= state newState)
			(0 (= seconds (Random 5 25)))
			(1 (bird setCycle: EndLoop self))
			(2 (bird setCycle: BegLoop self))
			(3
				(bird stopUpd:)
				(self changeState: 0)
			)
		)
	)
)

(instance outOfHouse of Script
	(properties)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(HandsOff)
				(ego posn: 207 132 setMotion: MoveTo 189 132 self)
			)
			(1
				(HandsOn)
				(curRoom setScript: 0)
			)
		)
	)
)

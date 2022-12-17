;;; Sierra Script 1.0 - (do not remove this comment)
(script# 420)
(include system.sh) (include sci2.sh) (include game.sh)
(use Main)
(use Arena)
(use Monster)
(use _Sound)
(use _Motion)
(use _Actor)
(use _System)

(use _Interface)

(public
	bearArena 0
	bear 1
)

(local
	local0
	[local1 2]
	local3
	[bearArmProps 3]
	[bearArmScripts 3]
	bearArmCel
	local11 =  5
)

(enum
	LEFTARM
	RIGHTARM
)

(procedure (SetBearFightScripts &tmp i)
	(= i 0)
	(while (< i 2)
		(= [bearArmScripts i] (Clone aFightScript))
		([bearArmProps i]
			setScript: [bearArmScripts i] 0
		)
		(++ i)
	)
)

(instance leftArm of Prop
	(properties
		y 39
		x 199
		view vBearFight
		loop 2
	)
)

(instance rightArm of Prop
	(properties
		y 42
		x 115
		view vBearFight
		loop 1
	)
)

(instance bearMusic of Sound
	(properties
		number 2
		priority 2
		loop -1
	)
)

(instance bear of Monster
	(properties
		view 		vBearFight
		strength 	70
		intell 		25
		agil 		40
		vit 		65
		luck 		25
		weap 		50
		dodge 		60
		armorValue 	3
		weapValue 	5
		
		attackRange 65
		warriorX 	170
		flameX 		172
		flameY 		73
	)
	
	(method (die)
		(= local0 1)
		(Bset DEFEATED_BEAR)
		(SolvePuzzle POINTS_KILLBEAR -25)
		(curRoom newRoom: 171) ;ROOM 171 is the ArenaBearChange, where the Bear changes into Bernard.
	)
)

(instance bearArena of Arena
	(properties
		picture 420
	)
	
	(method (init)
		(Load RES_VIEW vBearFight)
		(= monster bear)
		(= monsterInRoom BEAR)
		(= canEscape FALSE)
		;CI NOTE: setting the BearChange room at the beginning of the battle causes problems with the new Retry feature
		;so instead we have to set it immediately after the bear is killed.
		;(= prevRoomNum 171) ;ROOM 171 is the ArenaBearChange, where the Bear changes into Bernard.
		(super init: &rest)
		(leftArm setPri: 12 init: stopUpd:)
		(rightArm setPri: 12 init: stopUpd:)
		(bear
			view: vBearFight
			setLoop: 0
			cel: 0
			posn: 156 68
			setPri: 10
			cycleSpeed: 2
			setScript: bearCycle
		)
		(= [bearArmProps LEFTARM] leftArm)
		(= [bearArmProps RIGHTARM] rightArm)
		(bearMusic number: (GetSongNumber 2) init: play:)
		(SetBearFightScripts)
	)
	
	(method (doit)
		(if (Btst FLAG_233)
			(bear setScript: bearHurt)
		)
		;we've killed the bear, so next we move to the BearChanging room.
		(if (<= (monster health?) 0)
			(= prevRoomNum 171)
		)
		(super doit:)
	)
	
	(method (dispose)
		(bearMusic stop:)
		(if (!= monsterInRoom NULL) 
			;if there's a monster in the room, then play the song.
			(cSoundArena number: (GetSongNumber 38) loop: 1 play:)
		)
		(super dispose:)
	)
)

(instance aFightScript of Script
	(properties)
	
	(method (doit)
		(cond 
			(local0
				(= local0 (= cycles 0))
			)
			((and monsterCycles (== state 0))
				(= cycles (+ cycles monsterCycles))
				(= monsterCycles 0)
				(Bclr FLAG_233)
			)
		)
		(super doit:)
	)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				([bearArmProps register] cel: bearArmCel setCycle: 0 stopUpd:)
				(bear action: aaNOTHING)
				(= cycles (Random 5 10))
			)
			(1
				(= local3 (Random 0 1))
				(bear action: aaTHRUST)
				([bearArmProps register] setCycle: CycleTo local11 cdFORWARD self)
				(if (bear tryAttack: (bear opponent?))
					(bear ateEgo: TRUE)
				)
			)
			(2
				(if (bear ateEgo?)
					(bear doDamage: (bear opponent?))
					(bear ateEgo: FALSE)
				)
				(if local3
					([bearArmProps register]
						setCycle: CycleTo ([bearArmProps register] cel?) cdFORWARD self
					)
				else
					(client setCycle: EndLoop self)
				)
			)
			(3
				(self changeState: 0)
			)
		)
	)
)

(instance bearHurt of Script
	(properties)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(Bclr FLAG_233)
				(bear setLoop: 0 cel: 1 cycleSpeed: 0 setCycle: EndLoop self)
			)
			(1 (= cycles (Random 2 4)))
			(2
				(bear cel: 4)
				(= cycles (Random 2 4))
			)
			(3
				(bear cel: 5)
				(= cycles (Random 2 4))
			)
			(4
				(bear cycleSpeed: 1 setCycle: BegLoop self)
			)
			(5
				(bear cel: 0 cycleSpeed: 2 setScript: bearCycle)
			)
		)
	)
)

(instance bearCycle of Script
	(properties)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(bear setCycle: CycleTo 3 cdFORWARD)
				(= cycles (Random 9 15))
			)
			(1
				(bear setCycle: BegLoop self)
				(= cycles (Random 9 15))
			)
			(2 (self changeState: 0))
		)
	)
)

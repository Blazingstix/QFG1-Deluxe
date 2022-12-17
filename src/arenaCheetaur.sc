;;; Sierra Script 1.0 - (do not remove this comment)
(script# 440)
(include system.sh) (include sci2.sh) (include game.sh)
(use Main)
(use Arena)
(use Monster)
(use _Sound)
(use _Motion)
(use _Actor)
(use _System)

(public
	cheetaurArena 0
	cheetaur 1
)

(local
	local0
	[local1 2]
	local3
	[theCheetaur 3]
	[local7 3]
	[local10 3] = [2]
	[local13 3] = [1 2 2]
)
(procedure (localproc_000e &tmp temp0)
	(= temp0 0)
	(while (< temp0 3)
		([theCheetaur temp0]
			setScript: [local7 temp0] 0 (= [local7 temp0] (Clone aFightScript))
		)
		(++ temp0)
	)
)

(instance leftArm of Prop
	(properties
		y 65
		x 208
		view vCheetaurFight
		loop 2
	)
)

(instance rightArm of Prop
	(properties
		y 73
		x 138
		view vCheetaurFight
		loop 3
	)
)

(instance cheetMusic of Sound
	(properties
		number 2
		priority 2
		loop -1
	)
)

(instance tail of Prop
	(properties
		y 137
		x 230
		view vCheetaurFight
		loop 4
		cel 2
	)
)

(instance chest of PicView
	(properties
		y 110
		x 189
		view vCheetaurFight
		loop 1
		priority 5
	)
)

(instance cheetaur of Monster
	(properties
		view vCheetaurFight
		strength 70
		intell 65
		agil 70
		vit 50
		luck 30
		weap 40
		dodge 60
		armorValue 3
		weapValue 5
		attackRange 65
		warriorX 190
		warriorY 200
		flameX 177
		flameY 89
	)
	
	(method (die)
		(SolvePuzzle POINTS_KILLCHEETAUR 4 FIGHTER)
		(= local0 1)
	)
)

(instance cheetaurArena of Arena
	(properties
		picture 440
	)
	
	(method (init)
		(Load RES_VIEW vCheetaurFight)
		(= monster cheetaur)
		(= monsterInRoom CHEETAUR)
		(super init: &rest)
		(tail setPri: 2 init: setCycle: Forward startUpd:)
		(chest init:)
		(addToPics add: chest doit:)
		(leftArm setPri: 12 init: stopUpd:)
		(rightArm setPri: 12 init: stopUpd:)
		(cheetaur
			view: vCheetaurFight
			setLoop: 5
			cel: 2
			posn: 172 42
			setPri: 10
			stopUpd:
		)
		(= [theCheetaur 0] cheetaur)
		(= [theCheetaur 1] leftArm)
		(= [theCheetaur 2] rightArm)
		(cheetMusic number: (GetSongNumber 2) init: play:)
		(localproc_000e)
	)
	
	(method (doit)
		(if
			(and
				(Btst FLAG_233)
				(== (cheetaur script?) [local7 0])
				(== ([local7 0] state?) 0)
			)
			((cheetaur script?) setScript: cheetaurHurt)
		)
		(super doit:)
	)
	
	(method (dispose)
		(cheetMusic stop:)
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
			((and local0 (== client cheetaur)) (= local0 (= cycles 0)))
			((and monsterCycles (== state 0) (not script))
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
				([theCheetaur register]
					cel: [local10 register]
					setCycle: 0
					stopUpd:
				)
				(if (== client cheetaur)
					(cheetaur y: 42 setLoop: 5 cycleSpeed: 3 setCycle: Forward)
				)
				(cheetaur action: 0)
				(if (< cycles 15) (= cycles (Random 12 20)))
			)
			(1
				(= local3 (Random 0 1))
				(cheetaur action: 1)
				(if (== client cheetaur)
					(cheetaur y: (+ (cheetaur y?) 6) setLoop: 0)
					(self cue:)
				else
					([theCheetaur register]
						setCycle: CycleTo [local13 register] cdFORWARD self
					)
				)
				(if (cheetaur tryAttack: (cheetaur opponent?))
					(cheetaur ateEgo: 1)
				)
			)
			(2
				(if (cheetaur ateEgo?)
					(cheetaur doDamage: (cheetaur opponent?))
					(cheetaur ateEgo: 0)
				)
				(cond 
					((== client cheetaur) (cheetaur cycleSpeed: 1 setCycle: EndLoop self))
					(local3
						([theCheetaur register]
							setCycle: CycleTo ([theCheetaur register] cel?) cdFORWARD self
						)
					)
					(else (client setCycle: EndLoop self))
				)
			)
			(3 (self changeState: 0))
		)
	)
)

(instance cheetaurHurt of Script
	(properties)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(Bclr FLAG_233)
				(cheetaur setLoop: 6 cel: 0 setCycle: Forward)
				(= cycles 8)
			)
			(1 (client setScript: 0))
		)
	)
)

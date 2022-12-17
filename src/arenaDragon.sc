;;; Sierra Script 1.0 - (do not remove this comment)
(script# 460)
(include system.sh) (include sci2.sh) (include game.sh)
(use Main)
(use Arena)
(use Monster)
(use _Sound)
(use _Motion)
(use _Actor)
(use _System)

(public
	dragonArena 0
	dragon 1
)

(local
	local0
	local1
)
(instance dragonArena of Arena
	(properties
		picture 460
	)
	
	(method (init)
		(Load RES_VIEW vSaurusRexFight)
		(= monster dragon)
		(= monsterInRoom SAURUSREX)
		(super init: &rest)
		(dragonTail init:)
		(if (== detailLevel 0)
			(dragonTail addToPic:)
		else
			(dragonTail cycleSpeed: 6 setScript: drTailScript)
		)
		(dragMusic number: (GetSongNumber 2) loop: -1 play:)
		(monster setScript: dragonScript)
	)
	
	(method (dispose)
		(dragMusic stop:)
		(if (!= monsterInRoom NULL) 
			;if there's a monster in the room, then play the song.
			(cSoundArena number: (GetSongNumber 38) loop: 1 play:)
		)
		(super dispose:)
	)
)

(instance dragMusic of Sound
	(properties
		number 2
		priority 2
		loop -1
	)
)

(instance dragon of Monster
	(properties
		y 118
		x 170
		view vSaurusRexFight
		priority 10
		strength 80
		intell 40
		agil 50
		vit 100
		luck 50
		weap 90
		dodge 40
		armorValue 5
		weapValue 8
		attackRange 65
		warriorX 185
		warriorY 210
		flameX 163
		flameY 112
	)
	
	(method (die)
		(SolvePuzzle POINTS_KILLSAURUSREX 4 FIGHTER)
		(self canFight: FALSE)
	)
)

(instance dragonScript of Script
	(properties)
	
	(method (init)
		(super init: &rest)
		(client view: vSaurusRexFight setLoop: 0 setPri: 10 cel: 8)
	)
	
	(method (doit)
		(if (== state 0)
			(cond 
				((and monsterCycles (not script))
					(= cycles (+ cycles monsterCycles))
					(= monsterCycles 0)
					(Bclr FLAG_233)
				)
				((Btst FLAG_233) (= cycles 0) (self setScript: dragonHurt))
			)
		)
		(super doit:)
	)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(client
					action: 0
					cycleSpeed: 0
					setCycle: 0
					posn: 170 118
					stopUpd:
				)
				(cond 
					((>= cycles 7))
					(local1 (= cycles 1))
					(else (= cycles (Random 5 10)))
				)
			)
			(1
				(= local1 0)
				(client action: 1 setCycle: CycleTo 5 cdFORWARD self)
				(if (client tryAttack: (client opponent?))
					(client ateEgo: 1)
				)
			)
			(2
				(if (client ateEgo?)
					(client doDamage: (client opponent?) ateEgo: 0)
				)
				(client cycleSpeed: 1 setCycle: CycleTo 1 cdFORWARD self)
			)
			(3
				(if (= local0 (- 1 local0))
					(client setCycle: CycleTo 8 cdFORWARD self)
				else
					(self cue:)
				)
			)
			(4 (self changeState: 0))
		)
	)
)

(instance dragonTail of Prop
	(properties
		y 121
		x 87
		view vSaurusRexFight
		loop 1
		priority 8
	)
)

(instance drTailScript of Script
	(properties)
	
	(method (changeState newState)
		(switch (= state newState)
			(0 (client setCycle: EndLoop self))
			(1
				(= state -1)
				(client setCycle: BegLoop self)
			)
		)
	)
)

(instance dragonHurt of Script
	(properties)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(Bclr FLAG_233)
				(dragonTail cycleSpeed: 0)
				(dragon setLoop: 2 setCel: 0)
				(= cycles 6)
			)
			(1
				(dragon setLoop: 0 setCel: RELEASE)
				(dragonTail cycleSpeed: 6)
				(= cycles 2)
			)
			(2
				(= local1 1)
				(client changeState: 0)
				(self dispose:)
			)
		)
	)
)

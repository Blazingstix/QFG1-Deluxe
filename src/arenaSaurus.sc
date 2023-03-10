;;; Sierra Script 1.0 - (do not remove this comment)
(script# 430)
(include system.sh) (include sci2.sh) (include game.sh)
(use Main)
(use Arena)
(use Monster)
(use _Sound)
(use _Motion)
(use _Actor)
(use _System)

(public
	saurusArena 0
	saurus 1
)

(local
	local0
	local1
)
(instance saurusArena of Arena
	(properties
		picture 430
	)
	
	(method (init)
		(= monster saurus)
		(= monsterInRoom SAURUS)
		(Load RES_VIEW vSaurusFight)
		(super init: &rest)
		(addToPics
			add: saurusTail leftClaw rightClaw
			eachElementDo: #init
			doit:
		)
		(saurMusic number: (GetSongNumber 3) init: play:)
		(monster setScript: saurusScript)
	)
	
	(method (dispose)
		(saurMusic stop:)
		(if (!= monsterInRoom NULL) 
			;if there's a monster in the room, then play the song.
			(cSoundArena number: (GetSongNumber 7) loop: 1 play:)
		)
		(super dispose:)
	)
)

(instance saurMusic of Sound
	(properties
		number 3
		priority 2
		loop -1
	)
)

(instance saurus of Monster
	(properties
		y 131
		x 153
		view vSaurusFight
		priority 11
		strength 30
		intell 5
		agil 20
		vit 25
		luck 10
		weap 30
		dodge 10
		armorValue 3
		weapValue 5
		attackRange 55
		warriorX 170
		warriorY 205
		flameX 153
		flameY 107
	)
	
	(method (die)
		(SolvePuzzle POINTS_KILLSAURUS 1 FIGHTER)
		(= local1 1)
	)
)

(instance saurusScript of Script
	(properties)
	
	(method (doit)
		(cond 
			(local1 (= local1 (= cycles 0)))
			((and monsterCycles (== state 1))
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
				(client
					view: vSaurusFight
					setLoop: 0
					setPri: 10
					cel: 0
					posn: 147 91
				)
				(= cycles 1)
			)
			(1
				(client action: 0 setLoop: 0 cel: 0 posn: 147 91)
				(if (!= detailLevel DETAIL_LOW)
					(client cycleSpeed: 1 setCycle: Forward)
				)
				(= cycles (Random 6 15))
			)
			(2
				(client
					action: 1
					setLoop: 1
					cel: 0
					cycleSpeed: 0
					setCycle: CycleTo 5 cdFORWARD self
				)
				(if (client tryAttack: (client opponent?))
					(client ateEgo: 1)
				)
			)
			(3
				(if (client ateEgo?)
					(client doDamage: (client opponent?) ateEgo: 0)
				)
				(client cycleSpeed: 1 setCycle: CycleTo 1 cdFORWARD self)
			)
			(4
				(= state 0)
				(if (= local0 (- 1 local0))
					(client setCycle: CycleTo 8 cdFORWARD self)
				else
					(self cue:)
				)
			)
		)
	)
)

(instance saurusTail of PicView
	(properties
		y 139
		x 189
		view vSaurusFight
		loop 2
		priority 8
	)
)

(instance leftClaw of PicView
	(properties
		y 111
		x 131
		view vSaurusFight
		loop 3
		priority 0
	)
)

(instance rightClaw of PicView
	(properties
		y 98
		x 175
		view vSaurusFight
		loop 4
		priority 8
	)
)

;;; Sierra Script 1.0 - (do not remove this comment)
(script# 435)
(include system.sh) (include sci2.sh) (include game.sh)
(use Main)
(use Arena)
(use Monster)
(use _Sound)
(use _Motion)
(use _Actor)
(use _System)

(public
	mantrayArena 0
	mantray 1
)

(local
	[local0 200]
	local200
	local201 =  150
	local202 =  123
	local203
	local204
	local205
	local206
)
(procedure (localproc_01da param1)
	(param1
		posn: (+ (param1 x?) local203) (+ (param1 y?) local204)
	)
)

(procedure (localproc_01fb)
	(if (== detailLevel DETAIL_LOW) (return))
	(= local203 (Random 2 4))
	(= local204 (Random 1 2))
	(if local205 (= local203 (- local203)))
	(if local206 (= local204 (- local204)))
	(localproc_01da body)
	(localproc_01da mouth)
	(localproc_01da leftEye)
	(localproc_01da rightEye)
	(localproc_01da leftWing)
	(localproc_01da rightWing)
	(localproc_01da mantray)
)

(procedure (localproc_0269)
	(if (== detailLevel DETAIL_LOW) (return))
	(localproc_01da body)
	(localproc_01da mouth)
	(localproc_01da leftEye)
	(localproc_01da rightEye)
	(localproc_01da leftWing)
	(localproc_01da rightWing)
	(localproc_01da mantray)
)

(instance body of View
	(properties
		view vMantrayFight
		loop 9
	)
)

(instance leftEye of Prop
	(properties
		view vMantrayFight
	)
)

(instance rightEye of Prop
	(properties
		view vMantrayFight
		loop 1
	)
)

(instance leftWing of Prop
	(properties
		view vMantrayFight
		loop 7
	)
)

(instance rightWing of Prop
	(properties
		view vMantrayFight
		loop 8
	)
)

(instance mouth of Prop
	(properties
		view vMantrayFight
		loop 2
	)
)

(instance mantrayArena of Arena
	(properties
		picture 400
	)
	
	(method (init)
		(Load RES_VIEW vMantrayFight)
		(= local205 (Random 0 1))
		(= local206 (Random 0 1))
		(= monster mantray)
		(= monsterInRoom MANTRAY)
		(super init: &rest)
		(stingerMusic number: (GetSongNumber 2) loop: -1 play:)
		(monster setScript: mantrayScript)
	)
	
	(method (doit)
		(cond 
			(
			(and local205 (< (mantray x?) (- local201 20))) (= local205 0))
			(
			(and (not local205) (> (mantray x?) (+ local201 20))) (= local205 1))
		)
		(cond 
			(
			(and local206 (< (mantray y?) (- local202 8))) (= local206 0))
			(
			(and (not local206) (> (mantray y?) (+ local202 8))) (= local206 1))
		)
		(if (>= local200 3)
			(= local200 0)
			(localproc_01fb)
		else
			(++ local200)
		)
		(if
		(and (Btst FLAG_233) (== (mantrayScript state?) 0))
			(mantrayScript setScript: mantrayHurt)
		)
		(super doit:)
	)
	
	(method (dispose)
		(stingerMusic stop:)
		(if (!= monsterInRoom NULL) 
			;if there's a monster in the room, then play the song.
			(cSoundArena number: (GetSongNumber 38) play:)
		)
		(super dispose:)
	)
)

(instance stingerMusic of Sound
	(properties
		number 2
		priority 2
		loop -1
	)
)

(instance mantray of Monster
	(properties
		y 115
		x 150
		view vMantrayFight
		loop 3
		strength 50
		intell 20
		agil 70
		vit 40
		luck 40
		weap 50
		dodge 50
		armorValue 3
		weapValue 6
		attackRange 85
		warriorX 185
		warriorY 210
		flameX 169
		flameY 92
	)
	
	(method (die)
		(SolvePuzzle POINTS_KILLMANTRAY 2 FIGHTER)
		(self canFight: FALSE)
	)
)

(instance mantrayScript of Script
	(properties)
	
	(method (init)
		(super init: &rest)
		(client
			view: vMantrayFight
			ignoreActors:
			setLoop: 3
			setPri: 0
			cel: 0
			posn: (- local201 1) (+ local202 1)
			setCycle: Forward
		)
		(leftWing
			posn: (- local201 56) (- local202 27)
			ignoreActors:
			setPri: 2
			init:
			setCycle: Forward
		)
		(rightWing
			posn: (+ local201 56) (- local202 27)
			ignoreActors:
			setPri: 2
			init:
			setCycle: Forward
		)
		(body
			setPri: 2
			ignoreActors:
			posn: local201 local202
			init:
			stopUpd:
		)
		(leftEye
			posn: (- local201 36) (- local202 20)
			ignoreActors:
			setPri: 3
			init:
		)
		(rightEye
			posn: (+ local201 37) (- local202 20)
			ignoreActors:
			setPri: 3
			init:
		)
		(mouth
			posn: (+ local201 1) (- local202 19)
			ignoreActors:
			setPri: 3
			init:
		)
		(if (== detailLevel DETAIL_LOW)
			(leftEye addToPic:)
			(rightEye addToPic:)
			(mouth addToPic:)
			(body addToPic:)
		else
			(leftEye cycleSpeed: 1 setCycle: Forward)
			(rightEye cycleSpeed: 1 setCycle: Forward)
			(mouth cycleSpeed: 3 setCycle: Forward)
		)
	)
	
	(method (doit)
		(if (and monsterCycles (== state 0) (not script))
			(= cycles (+ cycles monsterCycles))
			(= monsterCycles 0)
			(Bclr FLAG_233)
		)
		(super doit:)
	)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(client setLoop: 3 setCycle: Forward action: 0)
				(if (< cycles 12) (= cycles (Random 7 18)))
			)
			(1
				(client action: 1 setLoop: (Random 4 6) setCel: 0)
				(if (client tryAttack: (client opponent?))
					(client ateEgo: 1)
				)
				(= cycles 3)
			)
			(2
				(if (client ateEgo?)
					(client doDamage: (client opponent?) ateEgo: 0)
				)
				(client setCycle: EndLoop self)
			)
			(3 (self changeState: 0))
		)
	)
)

(instance mantrayHurt of Script
	(properties)
	
	(method (doit)
		(super doit:)
	)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(Bclr FLAG_233)
				(= local203 -3)
				(= local204 -12)
				(localproc_0269)
				(mouth setLoop: 9 setCel: 1)
				(= cycles 4)
			)
			(1
				(= local203 2)
				(= local204 8)
				(localproc_0269)
				(= cycles 2)
			)
			(2
				(mouth setLoop: 2 setCel: RELEASE)
				(client changeState: 0)
				(self dispose:)
			)
		)
	)
)

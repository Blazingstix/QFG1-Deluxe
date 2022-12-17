;;; Sierra Script 1.0 - (do not remove this comment)
(script# 200)
(include system.sh) (include sci2.sh) (include game.sh)
(use Main)
(use _LoadMany)
(use _Sound)
(use _Motion)
(use _Game)
(use _Menu)
(use _Actor)
(use _System)

(public
	intro 0
)

(local
	local0 =  2
	theSeconds =  2
	[local2 2] = [2 10]
	theCycles =  10
	[local5 4] = [3 3 3 2]
	local9 =  160
	local10 =  120
	local11
	newView
	local13
	newView_2
	local15
	newView_3
	newView_4
	newView_5
	newView_6
	newView_7
	newView_8
	newView_9
	newView_10
	newView_11
	newView_14
	newView_15
	newView_12
	newView_13
	newView_16
	newView_17
	newView_18
	newView_19
	newView_20
	newView_22
	newView_21
	newView_23
	newView_24
	newView_25
	newView_26
	newView_27
	newView_28
	newView_29
	newView_30
	newView_31
)
(procedure (localproc_000c)
	(SetCursor theCursor 1)
	(curRoom newRoom: 202)
)

(instance intro of Room
	(properties
		picture 400
		style DISSOLVE
	)
	
	(method (init)
		(LoadMany RES_PIC 460 400 906)
		(LoadMany RES_VIEW vDragonHead vDragonFire vSierraPresents vQFGshadow vQFG vSubtitle (GetEgoViewNumber vEgoRunning) (GetEgoViewNumber vEgoRunFast) vSaurus vSaurusRex vSaurusRexFight)
		(super init:)
		(HandsOff)
		(cond 
			((== musicChannels 1) (Load RES_SOUND 201) (introMusic number: 201))
			((<= musicChannels 4) (Load RES_SOUND 301) (introMusic number: 301))
			(else (Load RES_SOUND 1) (introMusic number: 1))
		)
		(keyDownHandler add: self)
		(mouseDownHandler add: self)
		(aHero view: (GetEgoViewNumber vEgoRunning))
		(self setScript: page1Script)
	)
	
	(method (handleEvent event)
		(cond 
			((== (event type?) mouseDown) (event claimed: TRUE) (cSound stop:) (localproc_000c))
			((super handleEvent: event))
			((== (event type?) keyDown)
				(switch (event message?)
					(KEY_RETURN
						(event claimed: TRUE)
						(cSound stop:)
						(localproc_000c)
					)
					(KEY_F2 (ToggleSound))
					(KEY_CONTROL (AskQuit))
				)
			)
			(else (event claimed: TRUE))
		)
	)
)

(instance page1Script of Script
	(properties)
	
	(method (doit)
		(switch (introMusic prevSignal?)
			(20 (= local11 4))
			(30 (= local11 6))
			(40 (= local11 9))
			(50 (= local11 13))
		)
		(if (and (> local11 state) (or seconds cycles))
			(= seconds (= cycles 0))
			(self cue:)
		else
			(super doit:)
		)
	)
	
	(method (changeState newState)
		(switch (= state newState)
			(0 (= local11 0) (= cycles 2))
			(1
				(SetCursor theCursor 0)
				(introMusic init: play:)
				(= cycles 1)
			)
			(2
				(curRoom drawPic: 906)
				(if (== detailLevel DETAIL_LOW)
					(self cue:)
				else
					(= seconds theSeconds)
				)
			)
			(3
				(sierraText cel: 0 init: stopUpd:)
				(presentsText cel: 0 init: stopUpd:)
				(= seconds 8)
			)
			(4
				(claw1 cel: 0 init: cycleSpeed: 2 setCycle: EndLoop self)
			)
			(5
				(ShakeScreen 3)
				(= seconds 5)
			)
			(6
				(claw1 stopUpd:)
				(claw2 cel: 0 init: cycleSpeed: 2 setCycle: EndLoop)
				(= cycles theCycles)
			)
			(7
				(claw2 stopUpd:)
				(ShakeScreen 3)
				(head
					setLoop: 2
					cel: 0
					setPri: 6
					posn: 83 68
					init:
					cycleSpeed: (if (== detailLevel DETAIL_LOW) 0 else 2)
					setCycle: EndLoop self
				)
			)
			(8
				(head
					setLoop: 3
					cel: 0
					posn: 87 49
					init:
					setCycle: CycleTo 4 cdFORWARD
				)
				(= seconds 5)
			)
			(9 (head setCycle: CycleTo 5 cdFORWARD self))
			(10
				(flame
					posn: 85 76
					setLoop: 0
					setCel: RELEASE
					cel: 0
					setPri: 8
					ignoreActors:
					illegalBits: 0
					init:
					xStep: 3
					yStep: 6
					setMotion: MoveTo 218 228
					setCycle: EndLoop self
				)
			)
			(11
				(sierraText cycleSpeed: 2 setCycle: CycleTo 2 cdFORWARD self)
				(flame
					setLoop: 1
					cel: 0
					cycleSpeed: (if (== detailLevel DETAIL_LOW) 0 else 1)
					setMotion: MoveTo 158 228
					setCycle: Forward
				)
				(head setCycle: EndLoop)
			)
			(12
				(sierraText setCycle: EndLoop)
				(presentsText cycleSpeed: 2 setCycle: EndLoop)
				(= seconds (+ detailLevel 3))
			)
			(13
				(cast eachElementDo: #dispose eachElementDo: #delete)
				(client setScript: page2Script)
			)
		)
	)
)

(instance page2Script of Script
	(properties)
	
	(method (doit)
		(if (== -1 (introMusic prevSignal?)) (= local11 15))
		(if (and (> local11 state) (or seconds cycles))
			(= seconds (= cycles 0))
			(self cue:)
		else
			(super doit:)
		)
	)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(= local11 0)
				(curRoom drawPic: 400)
				(switch detailLevel
					(DETAIL_LOW
						(questText posn: 117 70 cycleSpeed: 0 cel: 2)
						(forText posn: 162 70 cycleSpeed: 0 cel: 2)
						(gloryText posn: 208 70 cycleSpeed: 0 cel: 2)
					)
					(DETAIL_MID
						(questText posn: 133 115 cycleSpeed: 1 cel: 0)
						(forText posn: 161 115 cycleSpeed: 1 cel: 0)
						(gloryText posn: 189 115 cycleSpeed: 1 cel: 0)
					)
					(DETAIL_HIGH
						(questText posn: 150 160 cycleSpeed: 2 cel: 0)
						(forText posn: 160 160 cycleSpeed: 2 cel: 0)
						(gloryText posn: 170 160 cycleSpeed: 2 cel: 0)
					)
					(else 
						(questText posn: 160 200 cycleSpeed: 2 cel: 0)
						(forText posn: 160 200 cycleSpeed: 2 cel: 0)
						(gloryText posn: 160 200 cycleSpeed: 2 cel: 0)
						(if (> speedCount 80)
							(questText moveSpeed: 1)
							(forText moveSpeed: 1)
							(gloryText moveSpeed: 1)
						)
					)
				)
				(questText
					setLoop: 0
					setPri: 1
					ignoreActors:
					illegalBits: 0
					xStep: 16
					yStep: 10
					init:
					setCycle: EndLoop
					setMotion: MoveTo 76 37 self
				)
				(forText
					setLoop: 1
					setPri: 1
					ignoreActors:
					illegalBits: 0
					xStep: 16
					yStep: 10
					init:
					setCycle: EndLoop
					setMotion: MoveTo 150 59 self
				)
				(gloryText
					setLoop: 2
					setPri: 1
					ignoreActors:
					illegalBits: 0
					xStep: 16
					yStep: 10
					init:
					setCycle: EndLoop
					setMotion: MoveTo 243 99 self
				)
			)
			(1)
			(2)
			(3
				(questText view: vQFG setLoop: 0 cel: 0 setCycle: Forward)
				(forText view: vQFG setLoop: 1 cel: 0 setCycle: Forward)
				(gloryText view: vQFG setLoop: 2 cel: 0 setCycle: Forward)
				(= seconds (if (== detailLevel DETAIL_LOW) 1 else 5))
			)
			(4
				(questText stopUpd:)
				(forText stopUpd:)
				(gloryText stopUpd:)
				(cond 
					((== detailLevel DETAIL_LOW)
						(saurus posn: 200 137 cycleSpeed: 0 moveSpeed: 0)
						(aHero posn: 120 137 cycleSpeed: 0 moveSpeed: 0)
					)
					((== detailLevel DETAIL_MID)
						(saurus posn: 90 137 cycleSpeed: 1 moveSpeed: 1)
						(aHero posn: 0 137 cycleSpeed: 1 moveSpeed: 1)
					)
					((== detailLevel DETAIL_HIGH)
						(saurus posn: 0 137 cycleSpeed: 1 moveSpeed: 1)
						(aHero posn: -100 137 cycleSpeed: 1 moveSpeed: 1)
					)
					(else
						(saurus posn: -50 137 cycleSpeed: 1 moveSpeed: 1)
						(aHero posn: -170 137 cycleSpeed: 1 moveSpeed: 1)
					)
				)
				(saurus
					setLoop: 0
					ignoreActors:
					init:
					setCycle: Forward
					setMotion: MoveTo 400 137
				)
				(aHero
					setLoop: 0
					ignoreActors:
					init:
					setCycle: Forward
					setMotion: MoveTo 340 137 self
				)
			)
			(5
				(saurus hide: stopUpd:)
				(aHero hide: stopUpd:)
				(if (>= detailLevel DETAIL_HIGH)
					(questText startUpd:)
					(forText startUpd:)
					(gloryText startUpd:)
				)
				(if (!= detailLevel DETAIL_LOW)
					(titleSpell
						setLoop: 0
						cel: RELEASE
						setPri: 8
						ignoreActors:
						illegalBits: 0
						xStep: 8
						yStep: 8
						posn: local9 local10
						init:
						setCycle: EndLoop self
					)
					(if (== detailLevel 3)
						(titleSpell cycleSpeed: 2 moveSpeed: 2)
					)
				else
					(self cue:)
				)
			)
			(6
				(if (!= detailLevel DETAIL_LOW)
					(titleSpell2
						setLoop: 0
						cel: (titleSpell cel?)
						setPri: 8
						ignoreActors:
						illegalBits: 0
						xStep: 8
						yStep: 8
						posn: local9 local10
						init:
						setCycle: Forward
						setMotion: MoveTo 231 160
					)
					(titleSpell setCycle: Forward setMotion: MoveTo 89 160 self)
					(if (== detailLevel 3)
						(titleSpell2 cycleSpeed: 2 moveSpeed: 2)
					)
				else
					(self cue:)
				)
			)
			(7
				(soYou init: stopUpd:)
				(want init: stopUpd:)
				(toBeA init: stopUpd:)
				(heroWord init: stopUpd:)
				(if (!= detailLevel DETAIL_LOW)
					(titleSpell setCycle: EndLoop)
					(titleSpell2 setCycle: EndLoop self)
				else
					(self cue:)
				)
			)
			(8
				(if (!= detailLevel DETAIL_LOW)
					(titleSpell dispose:)
					(titleSpell2 dispose:)
				)
				(questText stopUpd:)
				(forText stopUpd:)
				(gloryText stopUpd:)
				(saurus
					view: vSaurusRex
					setLoop: 1
					cel: 0
					xStep: 8
					yStep: 5
					cycleSpeed: 0
					moveSpeed: 0
					show:
				)
				(aHero
					view: (GetEgoViewNumber vEgoRunFast)
					setLoop: 0
					cel: 0
					cycleSpeed: 0
					moveSpeed: 0
					show:
				)
				(switch detailLevel
					(DETAIL_LOW
						(aHero posn: 330 137 setMotion: MoveTo 230 137 self)
						(saurus posn: 390 137 setMotion: MoveTo 250 137)
					)
					(DETAIL_ULTRA
						(aHero posn: 400 137 setMotion: MoveTo 100 137 self)
						(saurus posn: 500 137 setMotion: MoveTo 120 137)
					)
					(else 
						(aHero posn: 350 137 setMotion: MoveTo 140 137 self)
						(saurus posn: 420 137 setMotion: MoveTo 160 137)
					)
				)
			)
			(9
				(cast eachElementDo: #dispose eachElementDo: #delete)
				(curRoom drawPic: 460)
				(quest2Text setPri: 1 ignoreActors: init: stopUpd:)
				(for2Text setPri: 1 ignoreActors: init: stopUpd:)
				(glory2Text setPri: 1 ignoreActors: init: stopUpd:)
				(dragonTail cycleSpeed: 4 init:)
				(if (== detailLevel DETAIL_LOW)
					(dragonTail stopUpd:)
				else
					(dragonTail setScript: drTailScript)
				)
				(dragonHead
					setPri: 10
					init:
					cycleSpeed: 1
					setCycle: CycleTo 5 cdFORWARD self
				)
			)
			(10
				(if (== detailLevel DETAIL_LOW) (self changeState: 14))
				(= cycles 8)
			)
			(11
				(dragonHead cycleSpeed: 2 setCycle: CycleTo 1 cdFORWARD self)
			)
			(12 (= cycles 8))
			(13
				(dragonHead setCycle: CycleTo 5 cdFORWARD self)
			)
			(14
				(dragonHead stopUpd:)
				(dragonTail setCycle: 0 setScript: 0 stopUpd:)
				(quest2Text setCycle: Forward)
				(for2Text setCycle: Forward)
				(glory2Text setCycle: Forward)
				(= seconds 15)
			)
			(15
				(dragonHead stopUpd:)
				(dragonTail setCycle: 0 setScript: 0 stopUpd:)
				(if (== detailLevel DETAIL_LOW) (= cycles 1) else (= seconds 2))
			)
			(16
				(cast eachElementDo: #dispose eachElementDo: #delete)
				(client setScript: creditScript)
			)
		)
	)
)

(instance creditScript of Script
	(properties)
	
	(method (doit)
		(switch (cSound prevSignal?)
			(10 (= local11 3))
			(20 (= local11 6))
			(30 (= local11 9))
			(40 (= local11 12))
			(50 (= local11 15))
			(60 (= local11 18))
			(70 (= local11 21))
		)
		(if (and (> local11 state) (or seconds cycles))
			(= seconds (= cycles 0))
			(self cue:)
		else
			(super doit:)
		)
	)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(= local11 0)
				(Load RES_PIC 903)
				(Load RES_VIEW vCreditsDragon)
				(Load RES_VIEW vCredits)
				(Load RES_SOUND (GetSongNumber 61))
				(Load RES_SOUND (GetSongNumber 73))
				(curRoom drawPic: 903 0)
				(leftDrag
					view: vCreditsDragon
					loop: 0
					cel: 0
					posn: 285 168
					priority: 12
					init:
					stopUpd:
				)
				(rightDrag
					view: vCreditsDragon
					loop: 1
					cel: 0
					posn: 32 168
					priority: 12
					init:
					stopUpd:
				)
				(andAnd view: vCredits loop: 3 cel: 0 posn: 155 23 init: hide:)
				(byBy view: vCredits loop: 0 cel: 1 posn: 155 40 init: hide:)
				(= cycles 1)
			)
			(1
				((= newView (View new:))
					view: vCredits
					loop: 0
					cel: 0
					posn: 110 23
					init:
					stopUpd:
				)
				(andAnd show:)
				((= newView_2 (View new:))
					view: vCredits
					loop: 0
					cel: 3
					posn: 203 23
					init:
					stopUpd:
				)
				(byBy show:)
				((= newView_3 (View new:))
					view: vCredits
					loop: 1
					cel: 0
					posn: 157 90
					init:
					stopUpd:
				)
				(= seconds 4)
			)
			(2
				(cSound number: (GetSongNumber 61) loop: -1 play:)
				(= seconds 5)
			)
			(3
				(newView_3 dispose:)
				(newView dispose:)
				(newView_2 dispose:)
				(byBy hide:)
				(andAnd dispose:)
				(= seconds 1)
			)
			(4
				((= newView_4 (View new:))
					view: vCredits
					loop: 0
					cel: 4
					posn: 157 28
					init:
					stopUpd:
				)
				(if (== detailLevel DETAIL_LOW) (= cycles 1) else (= seconds 1))
			)
			(5
				((= newView_5 (View new:))
					view: vCredits
					loop: 1
					cel: 1
					posn: 158 80
					init:
					stopUpd:
				)
				((= newView_6 (View new:))
					view: vCredits
					loop: 1
					cel: 2
					posn: 158 107
					init:
					stopUpd:
				)
				((= newView_7 (View new:))
					view: vCredits
					loop: 3
					cel: 2
					posn: 158 134
					init:
					stopUpd:
				)
				((= newView_8 (View new:))
					view: vCredits
					loop: 3
					cel: 3
					posn: 158 161
					init:
					stopUpd:
				)
				(= seconds 11)
			)
			(6
				(newView_4 dispose:)
				(newView_5 dispose:)
				(newView_6 dispose:)
				(newView_7 dispose:)
				(newView_8 dispose:)
				(= seconds 1)
			)
			(7
				((= newView_9 (View new:))
					view: vCredits
					loop: 0
					cel: 5
					posn: 143 18
					init:
					stopUpd:
				)
				(byBy loop: 3 cel: 0 posn: 201 18 show:)
				((= newView_10 (View new:))
					view: vCredits
					loop: 0
					cel: 6
					posn: 124 36
					init:
					stopUpd:
				)
				((= newView_11 (View new:))
					view: vCredits
					loop: 0
					cel: 7
					posn: 211 36
					init:
					stopUpd:
				)
				(if (== detailLevel DETAIL_LOW) (= cycles 1) else (= seconds 1))
			)
			(8
				((= newView_12 (View new:))
					view: vCredits
					loop: 1
					cel: 3
					posn: 158 80
					init:
					stopUpd:
				)
				((= newView_13 (View new:))
					view: vCredits
					loop: 3
					cel: 1
					posn: 158 107
					init:
					stopUpd:
				)
				((= newView_14 (View new:))
					view: vCredits
					loop: 1
					cel: 4
					posn: 158 134
					init:
					stopUpd:
				)
				((= newView_15 (View new:))
					view: vCredits
					loop: 5
					cel: 2
					posn: 158 161
					init:
					stopUpd:
				)
				(= seconds 9)
			)
			(9
				(newView_9 dispose:)
				(newView_10 dispose:)
				(newView_11 dispose:)
				(byBy hide:)
				(newView_12 dispose:)
				(newView_14 dispose:)
				(newView_15 dispose:)
				(newView_13 dispose:)
				(= seconds 1)
			)
			(10
				((= newView_16 (View new:))
					view: vCredits
					loop: 5
					cel: 0
					posn: 156 21
					init:
					stopUpd:
				)
				(byBy loop: 0 cel: 1 posn: 156 36 show:)
				(if (== detailLevel DETAIL_LOW) (= cycles 1) else (= seconds 1))
			)
			(11
				((= newView_17 (View new:))
					view: vCredits
					loop: 5
					cel: 1
					posn: 159 98
					init:
					stopUpd:
				)
				(= seconds 7)
			)
			(12
				(newView_16 dispose:)
				(byBy hide:)
				(newView_17 dispose:)
				(= seconds 1)
			)
			(13
				((= newView_18 (View new:))
					view: vCredits
					loop: 0
					cel: 8
					posn: 99 22
					init:
					stopUpd:
				)
				((= newView_19 (View new:))
					view: vCredits
					loop: 0
					cel: 9
					posn: 180 22
					init:
					stopUpd:
				)
				((= newView_20 (View new:))
					view: vCredits
					loop: 0
					cel: 10
					posn: 156 37
					init:
					stopUpd:
				)
				(if (== detailLevel DETAIL_LOW) (= cycles 1) else (= seconds 1))
			)
			(14
				((= newView_21 (View new:))
					view: vCredits
					loop: 2
					cel: 0
					posn: 157 76
					init:
					stopUpd:
				)
				((= newView_22 (View new:))
					view: vCredits
					loop: 2
					cel: 1
					posn: 157 98
					init:
					stopUpd:
				)
				((= newView_23 (View new:))
					view: vCredits
					loop: 2
					cel: 2
					posn: 158 122
					init:
					stopUpd:
				)
				((= newView_24 (View new:))
					view: vCredits
					loop: 2
					cel: 3
					posn: 158 146
					init:
					stopUpd:
				)
				(= seconds 9)
			)
			(15
				(newView_18 dispose:)
				(newView_19 dispose:)
				(newView_20 dispose:)
				(newView_21 dispose:)
				(newView_22 dispose:)
				(newView_23 dispose:)
				(newView_24 dispose:)
				(= seconds 1)
			)
			(16
				((= newView_25 (View new:))
					view: vCredits
					loop: 0
					cel: 11
					posn: 157 23
					init:
					stopUpd:
				)
				(byBy loop: 0 cel: 1 posn: 156 40 show:)
				(if (== detailLevel DETAIL_LOW) (= cycles 1) else (= seconds 1))
			)
			(17
				((= newView_26 (View new:))
					view: vCredits
					loop: 4
					cel: 0
					posn: 106 81
					init:
					stopUpd:
				)
				((= newView_27 (View new:))
					view: vCredits
					loop: 4
					cel: 1
					posn: 159 98
					init:
					stopUpd:
				)
				((= newView_28 (View new:))
					view: vCredits
					loop: 4
					cel: 2
					posn: 211 116
					init:
					stopUpd:
				)
				(= seconds 9)
			)
			(18
				(newView_25 dispose:)
				(byBy dispose:)
				(newView_26 dispose:)
				(newView_27 dispose:)
				(newView_28 dispose:)
				(= seconds 1)
			)
			(19
				((= newView_29 (View new:))
					view: vCredits
					loop: 0
					cel: 12
					posn: 158 21
					init:
					stopUpd:
				)
				((= newView_30 (View new:))
					view: vCredits
					loop: 0
					cel: 13
					posn: 159 36
					init:
					stopUpd:
				)
				(if (== detailLevel DETAIL_LOW) (= cycles 1) else (= seconds 1))
			)
			(20
				((= newView_31 (View new:))
					view: vCredits
					loop: 4
					cel: 3
					posn: 159 98
					init:
					stopUpd:
				)
				(= seconds 10)
			)
			(21 (localproc_000c))
		)
	)
)

(instance claw1 of Prop
	(properties
		y 92
		x 51
		view vDragonHead
		priority 3
	)
)

(instance claw2 of Prop
	(properties
		y 87
		x 127
		view vDragonHead
		loop 1
		cel 1
		priority 3
	)
)

(instance head of Prop
	(properties
		y 49
		x 87
		view vDragonHead
		loop 2
	)
)

(instance flame of Actor
	(properties
		view vDragonFire
	)
)

(instance sierraText of Prop
	(properties
		y 168
		x 128
		view vSierraPresents
		priority 12
	)
)

(instance presentsText of Prop
	(properties
		y 182
		x 130
		view vSierraPresents
		loop 1
		priority 12
	)
)

(instance questText of Actor
	(properties
		y 160
		x 150
		view vQFGshadow
	)
)

(instance quest2Text of Actor
	(properties
		y 37
		x 76
		view vQFG
		illegalBits $0000
	)
)

(instance forText of Actor
	(properties
		y 160
		x 150
		view vQFGshadow
		loop 1
	)
)

(instance for2Text of Actor
	(properties
		y 59
		x 150
		view vQFG
		loop 1
		illegalBits $0000
	)
)

(instance gloryText of Actor
	(properties
		y 160
		x 150
		view vQFGshadow
		loop 2
		priority 12
	)
)

(instance glory2Text of Actor
	(properties
		y 99
		x 243
		view vQFG
		loop 2
		illegalBits $0000
	)
)

(instance titleSpell of Actor
	(properties
		view vSubtitle
	)
)

(instance titleSpell2 of Actor
	(properties
		view vSubtitle
	)
)

(instance soYou of View
	(properties
		y 160
		x 50
		view vSubtitle
		loop 1
		priority 7
	)
)

(instance want of View
	(properties
		y 160
		x 127
		view vSubtitle
		loop 1
		cel 1
		priority 7
	)
)

(instance toBeA of View
	(properties
		y 160
		x 209
		view vSubtitle
		loop 1
		cel 2
		priority 7
	)
)

(instance heroWord of View
	(properties
		y 160
		x 286
		view vSubtitle
		loop 1
		cel 3
		priority 7
	)
)

(instance introMusic of Sound
	(properties
		number 1
	)
)

(instance saurus of Actor
	(properties
		y 137
		x -20
		yStep 4
		view vSaurus
		xStep 6
	)
)

(instance aHero of Actor
	(properties
		y 137
		x -80
		yStep 4
		view vEgoRunning ;CI: NOTE: This gets set to the proper avatar's view in the Room init.
		xStep 7
	)
)

(instance dragonHead of Prop
	(properties
		y 118
		x 170
		view vSaurusRexFight
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

(instance leftDrag of View
	(properties)
)

(instance rightDrag of View
	(properties)
)

(instance andAnd of View
	(properties)
)

(instance byBy of View
	(properties)
)

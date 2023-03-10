;;; Sierra Script 1.0 - (do not remove this comment)
(script# 40)
(include system.sh) (include sci2.sh) (include game.sh)
(use Main)
(use Sleep)
(use GameTime)
(use _Extra)
(use _Sound)
(use _Motion)
(use _Game)
(use _User)
(use _Actor)
(use _System)

(public
	rm40 0
)

(local
	[local0 10]
	[local10 10]
	local20
	local21
	local22
	stablemanAtWindow
	stablemanSpeaks
	local25
	local26
	local27
	local28
	local29
)
(procedure (localproc_0428 &tmp temp0)
	(= temp0 0)
	(while (< temp0 10)
		(if (not [local10 temp0])
			(= [local10 temp0] (Clone aDustScript))
		)
		(if (not [local0 temp0])
			((= [local0 temp0] (Clone aDustCloud))
				setLoop: 9
				setPri: 9
				setStep: 6 4
				cel: 0
				cycleSpeed: 1
				illegalBits: 0
				ignoreActors:
				posn: 0 1000
				init:
			)
		)
		(++ temp0)
	)
)

(procedure (localproc_0497 &tmp temp0)
	(= temp0 0)
	(while (< temp0 10)
		(if [local10 temp0]
			([local10 temp0] dispose:)
			(= [local10 temp0] 0)
		)
		(if [local0 temp0]
			([local0 temp0] dispose:)
			(= [local0 temp0] 0)
		)
		(++ temp0)
	)
)

(procedure (AlreadyClean)
	(TimedPrint 6 40 0)
	;"Haven't you got anything better to do than hang around here?
	;Come back some other time you want to get rich."
)

(instance rakeMusic of Sound
	(properties
		number 77
		priority 1
		loop -1
	)
)

(instance horse of Prop
	(properties
		view vStables
		loop 1
		cel 1
	)
)

(instance horseHead of Extra
	(properties
		view vStables
		loop 6
		cycleSpeed 3
		cycleType 1
		minPause 50
		maxPause 110
		minCycles 1
		maxCycles 1
	)
)

(instance horseTail of Extra
	(properties
		view vStables
		loop 5
		cycleSpeed 2
		cycleType 1
		minPause 60
		maxPause 100
		minCycles 1
		maxCycles 2
	)
)

(instance theFrame of View
	(properties
		y 63
		x 27
		view vStables
		loop 1
	)
)

(instance manure1 of View
	(properties
		y 113
		x 92
		view vEgoWorking ;CI:TODO: create new separate view
		loop 6
	)
)

(instance manure2 of View
	(properties
		y 117
		x 65
		view vEgoWorking ;CI:TODO: Create new separate view
		loop 6
	)
)

(instance manure3 of View
	(properties
		y 138
		x 112
		view vEgoWorking ;CI:TODO: Create new separate view
		loop 6
	)
)

(instance glass of Prop
	(properties
		y 54
		x 27
		view vStables
	)
)

(instance sand of Prop
	(properties
		y 46
		x 27
		view vStables
		loop 2
	)
)

(instance aDustCloud of Actor
	(properties
		view vStables
	)
)

(instance dust of Actor
	(properties
		y 130
		x 150
		view vStables
	)
)

(instance stableMan of Actor
	(properties
		view vStables
	)
)

(instance rm40 of Room
	(properties
		picture 40
		style WIPERIGHT
		horizon 60
	)
	
	(method (init &tmp tmpEgoY)
		(super init: &rest)
		(StatusLine enable:)
		(if (not isNightTime)
			(stableMan
				setLoop: 3
				cel: 0
				illegalBits: 0
				posn: 188 144
				init:
				cycleSpeed: 1
				stopUpd:
			)
			(horse
				posn: (Random 55 90) (Random 95 110)
				setPri: 5
				init:
				cycleSpeed: 1
				stopUpd:
				setScript: horseEats
			)
			(horseHead
				posn: (+ (horse x?) 8) (- (horse y?) 23)
				setPri: 6
				init:
			)
			(horseTail
				posn: (- (horse x?) 13) (- (horse y?) 12)
				setPri: 6
				init:
			)
			(manure1
				setPri: 4
				setCel: (if (Btst STABLE_CLEAN) 1 else 0)
				ignoreActors:
				init:
				stopUpd:
			)
			(manure2
				setPri: 4
				setCel: (if (Btst STABLE_CLEAN) 1 else 0)
				ignoreActors:
				init:
				stopUpd:
			)
			(manure3
				setPri: 4
				setCel: (if (Btst STABLE_CLEAN) 1 else 0)
				ignoreActors:
				init:
				stopUpd:
			)
			(dust
				setLoop: 9
				illegalBits: 0
				ignoreActors:
				cycleSpeed: 1
				init:
			)
		)
		(= timerYesNo 0)
		(= tmpEgoY (ego y?))
		(StopEgo)
		(if (== prevRoomNum 999)
			(ego posn: 148 132 init: setScript: sleepyIntro)
		else
			(ego
				posn: 1 tmpEgoY
				init:
				setMotion: MoveTo 15 (if (< tmpEgoY 115) 115 else tmpEgoY)
			)
		)
		(self setLocales: CASTLE)
	)
	
	(method (doit &tmp temp0)
		(super doit:)
		(if (== (ego edgeHit?) WEST)
			(if (> (ego y?) 125)
				(curRoom newRoom: 39)
			else
				(= temp0 (- (ego y?) 98))
				(ego y: (+ (* temp0 3) 106))
				(curRoom newRoom: 41)
			)
		)
		(cond 
			(
				(or
					(and
						(== (ego onControl: origin) cYELLOW)
						(not stablemanSpeaks)
						(not stablemanAtWindow)
					)
					(and stablemanSpeaks (ego inRect: 95 145 319 189) (not stablemanAtWindow))
				)
				(= stablemanAtWindow 1)
			)
			(
				(or
					(and
						stablemanAtWindow
						(not (== (ego onControl: origin) cYELLOW))
						(not stablemanSpeaks)
					)
					(and
						stablemanAtWindow
						stablemanSpeaks
						(or
							(not (ego inRect: 95 145 319 189))
							(== (ego onControl: origin) cYELLOW)
						)
					)
				)
				(= stablemanAtWindow 0)
			)
		)
		(if
			(and
				(not isNightTime)
				(!= prevRoomNum 999)
				(or
					(ego inRect: 95 145 319 189)
					(== (ego onControl: origin) cYELLOW)
				)
				(not local25)
			)
			(= local25 1)
			(stableMan setScript: intro)
		)
	)
	
	(method (handleEvent event)
		(switch (event type?)
			(evSAID
				(cond 
					((super handleEvent: event))
					((Said 'look>')
						(cond 
							((Said '[<at,around][/stable,shed,hut,building]')
								(cond 
									((Btst STABLE_CLEAN)
										(HighPrint 40 1)
										;The stable looks much better since you cleaned it.
										)
									((== (ego onControl: origin) cYELLOW)
										(HighPrint 40 2)
										;It doesn't look like the stableman spends much of his time cleaning.
										)
									(else
										(HighPrint 40 3)
										;The castle's stable looks like it holds about six horses.  It could use some cleaning up.
										)
								)
							)
							((Said '/horse,animal')
								(HighPrint 40 4)
								;The Baron was once known for his many fine and noble steeds.  Only a few aging animals remain in his stable.
								)
							(
							(or (Said '<up') (Said '/sky,cloud,star,moon'))
							(if isNightTime
								(HighPrint 40 5)
								;The moon lights the Baron's courtyard.
								else
								(HighPrint 40 6)
								;It is a beautiful day.
								)
							)
							(
							(or (Said '<down') (Said '/ground,floor,hay,crap,crap')) (if (Btst STABLE_CLEAN)
								(HighPrint 40 7)
								;You have raked the manure and covered the stable floor with new straw.
								else
								(HighPrint 40 8)
								;In the stable, the manure is beginning to pile up.
								)
							)
							((Said '/north,castle')
								(HighPrint 40 9) ;EO: The castle is to the northWEST, not northEAST.
								;The castle is across the courtyard to the northeast.
								)
							((Said '/east')
								(HighPrint 40 10)
								;The stable marks the eastern boundary of the Baron's courtyard.
								)
							((Said '/south,pit')
								(HighPrint 40 11)
								;Along the wall to the southeast is the gatehouse.  A line of bushes partially obscure a defensive pit along the wall.
								)
							((Said '/west')
								(HighPrint 40 12)
								;Across the main courtyard to the west are the soldiers' barracks.
								)
							((Said '/fence,corral')
								(HighPrint 40 13)
								;There is a fenced corral next to the stable building.
								)
							((Said '/man,stablekeeper,keeper')
								(if stablemanAtWindow
									(HighPrint 40 14)
									;A low-looking lout.  HE could use some cleaning up.
									else
									(HighPrint 40 15)
									;He's not standing where you can see him.
								)
							)
						)
					)
					((Said 'chat/man,stablekeeper') (if stablemanAtWindow
							(HighPrint 40 16)
							;"Don't tire me out with a bunch of questions.  Can't you see what a hard life I have?"
							else
							(HighPrint 40 17)
							;The stableman can't hear you.
							)
						)
					((or (Said 'nap') (Said 'go[<to]/nap'))
						(cond 
							((and (< 750 currentTime) (< currentTime 2550))
								(HighPrint 40 18)
								;It's too early in the day; come back later.
								)
							((not (== (ego onControl: origin) cYELLOW))
								(HighPrint 40 19)
								;It would be more comfortable to sleep on clean hay in the stable.
								)
							((not (Btst STABLE_CLEAN))
								(HighPrint 40 20)
								;"Hey you!  It's bad enough you hang around here all day, but I sure ain't
								;gonna let you sleep here before you've worked enough to earn it!"
								)
							((ego script?)
								(HighPrint 40 21)
								;Wait a minute!
								)
							(else (ego setScript: sleeper))
						)
					)
					((Said 'ask>')
						(cond 
							((not stablemanAtWindow)
								(HighPrint 40 17)
								;The stableman can't hear you.
								(event claimed: TRUE))
							((Said '//nap')
								(HighPrint 40 22)
								;"If you put in a day's work, and you wanna take a nap, it's OK with me."
								)
							((Said '//horse,animal')
								(HighPrint 40 23)
								;"These horses, they act just like animals or somethin'."
								)
							((Said '//baron,barnard,castle,hamlet,valley')
								(HighPrint 40 24)
								;"Just get out and wander around a little.  You'll find out."
								)
							((Said '//magic,baba,zara,erasmus,spell,potion')
								(HighPrint 40 25)
								;"Don't ask me about anything to do with magic.  I don't wanna know."
								)
							((Said '//monster,troll,cheetaur,saurus')
								(HighPrint 40 26)
								;"Those things scare me silly!"
								)
							((Said '//antwerp')
								(HighPrint 40 27)
								;"Ha-ha-ha-ha-ha!  Hee-hee-hee-ho-ho!"
								)
							((Said '//labor,hoe,crap')
								(if (Btst STABLE_CLEAN)
									(AlreadyClean)
								else
									(HighPrint 40 28)
									;"Sure!  C'mon in and grab a rake."
									(ego setScript: goToWork)
								)
							)
							((Said '//*')
								(HighPrint 40 29)
								;"Don't tire me out with a bunch of questions.
								;I don't need your comments, either.  Can't you see what a hard life I have?"
								)
						)
					)
					((Said 'buy,get/horse')
						(HighPrint 40 30)
						;"Sorry, the Baron's horses aren't for sale."
						)
					((Said '/horse')
						(HighPrint 40 31)
						;"Neigh!"  says the horse.
						)
				)
			)
		)
	)
)

(instance horseEats of Script
	(properties)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(= local21 0)
				(= seconds (Random 10 30))
			)
			(1
				(horseHead hide:)
				(horseTail hide:)
				(horse setLoop: 7 cel: 0 setCycle: EndLoop self)
			)
			(2
				(++ local21)
				(if (> (ego distanceTo: horse) 50)
					(horse loop: 8 cycleSpeed: 3 setCycle: Forward)
					(= cycles (Random 15 24))
				else
					(self cue:)
				)
			)
			(3
				(horse setCycle: 0)
				(= cycles (Random 12 24))
			)
			(4
				(if (< local21 (Random 3 6))
					(self changeState: 2)
				else
					(self cue:)
				)
			)
			(5
				(horse setLoop: 7 cel: 3 cycleSpeed: 1 setCycle: BegLoop self)
			)
			(6
				(horse setLoop: 1 cel: 1 stopUpd:)
				(horseHead show:)
				(horseTail show:)
				(self changeState: 0)
			)
		)
	)
)

(instance sandsOfTime of Script
	(properties)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(theFrame setPri: 2 init: stopUpd:)
				(glass setPri: 3 setCel: 0 init: stopUpd:)
				(sand setPri: 1 init: setCycle: Forward startUpd:)
				(= cycles 39)
			)
			(1
				(++ local22)
				(glass setCel: (+ (glass cel?) 1))
				(= cycles 39)
			)
			(2
				(if (> local22 6)
					(glass setCel: 8)
					(sand setCycle: 0)
					(= cycles 10)
				else
					(self changeState: 1)
				)
			)
			(3
				(= local22 0)
				(theFrame dispose:)
				(glass dispose:)
				(sand dispose:)
				(client setScript: 0)
			)
		)
	)
)

(instance goToWork of Script
	(properties)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(HandsOff)
				(ChangeGait MOVE_WALK FALSE)
				(if (== (ego onControl: origin) cYELLOW)
					(self cue:)
				else
					(ego setMotion: MoveTo 110 132 self)
				)
			)
			(1
				(ego illegalBits: 0 setMotion: MoveTo 146 132 self)
			)
			(2
				(SolvePuzzle POINTS_WORKINSTABLES 5)
				(client setScript: egoRakes)
			)
		)
	)
)

(instance egoRakes of Script
	(properties)
	
	(method (init)
		(super init: &rest)
		(rakeMusic init: play:)
		(keyDownHandler add: self)
	)
	
	(method (doit)
		(cond 
			(
				(and
					(== (ego cel?) 1)
					(or (== (self state?) 0) (== (self state?) 4))
				)
				(= local26 1)
			)
			((and local26 (== (ego cel?) 2))
				(= local26 0)
				([local0 (if (== (++ local20) 10) (= local20 0))]
					posn:
						(if local27
							(- (ego x?) (Random 15 30))
						else
							(+ (ego x?) (Random 15 30))
						)
						(ego y?)
					setScript: [local10 local20]
				)
			)
		)
		(super doit:)
	)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(if (not local28) (rm40 setScript: sandsOfTime))
				(HandsOff)
				(localproc_0428)
				(ego
					view: (GetEgoViewNumber vEgoWorking)
					setLoop: 0
					cel: 0
					posn: 150 132
					setStep: (if local28 1 else 2) 1
					setPri: 8
					illegalBits: 0
					cycleSpeed: (if local28 1 else 0)
					setCycle: Forward
					setMotion: MoveTo 80 132 self
				)
			)
			(1
				(ego setLoop: 2 setCycle: Forward)
				(= cycles 30)
			)
			(2
				(if local28
					(ego setLoop: 3 setCycle: EndLoop)
					(= local20 0)
					(rakeMusic pause: 1)
					(= cycles 15)
				else
					(self cue:)
				)
			)
			(3
				(localproc_0497)
				(= local20 0)
				(if local28
					(ego setLoop: 4 cycleSpeed: 2 setCycle: EndLoop)
					(= cycles 10)
				else
					(self cue:)
				)
			)
			(4
				(if local28 (rakeMusic pause: 0))
				(= local27 1)
				(localproc_0428)
				(ego
					posn: 85 132
					setLoop: 1
					cel: 0
					cycleSpeed: (if local28 1 else 0)
					setCycle: Forward
					setMotion: MoveTo 155 132 self
				)
			)
			(5
				(localproc_0497)
				(= local20 0)
				(= cycles 3)
			)
			(6
				(= local27 0)
				(if local28
					(= local28 0)
					(manure3 setCel: 1)
					(ego posn: 146 132)
					(StopEgo)
					(rakeMusic dispose:)
					(keyDownHandler release:)
					(client setScript: endRake)
				else
					(= local28 1)
					(manure1 setCel: 1)
					(manure2 setCel: 1)
					(self changeState: 0)
				)
			)
		)
	)
	
	(method (handleEvent event)
		(if
			(and
				(== (event type?) keyDown)
				(== KEY_RETURN (event message?))
				(== client ego)
			)
			(event claimed: TRUE)
			(= local28 1)
			(= local20 0)
			(StopEgo)
			(sandsOfTime changeState: 3)
			(localproc_0497)
			(manure1 setCel: 1)
			(manure2 setCel: 1)
			(manure3 setCel: 1)
			(self changeState: 6)
			(keyDownHandler release:)
		else
			(event claimed: TRUE)
		)
	)
)

(instance endRake of Script
	(properties)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(AdvanceTime 3)
				(if isNightTime (curRoom drawPic: 40))
				(ego setMotion: MoveTo 110 132 self)
			)
			(1
				(ego
					view: (GetEgoViewNumber vEgoWorking)
					setLoop: 5
					cel: 0
					cycleSpeed: 1
					setCycle: EndLoop self
				)
			)
			(2
				(Bset STABLE_CLEAN)
				(StopEgo)
				(ego x: (- (ego x?) 2) loop: 2)
				(SkillUsed STR tryStatStableStr)
				(SkillUsed VIT tryStatStableVit)
				(client setScript: getPaid)
			)
		)
	)
)

(instance getPaid of Script
	(properties)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(stableMan
					posn: 187 144
					setLoop: 3
					setCel: 0
					setMotion: MoveTo 171 144 self
				)
				(= stablemanSpeaks TRUE)
			)
			(1
				(TimedPrint 3 40 32)
				;"OK.  C'mon over here."
				(stableMan setLoop: 4 cel: 0 setCycle: Forward)
				(= cycles 8)
			)
			(2
				(client setMotion: MoveTo 134 166 self)
			)
			(3
				(client illegalBits: 0 setMotion: MoveTo 152 166 self)
			)
			(4
				(TimedPrint 5 40 33)
				;The stableman hands you some coins and says,  "Now you're five silvers richer."
				(ego get: iSilver 5)
				(= seconds 5)
			)
			(5
				(if isNightTime
					(stableMan
						setLoop: 3
						setCel: 1
						setMotion: MoveTo 188 144 self
					)
				else
					(stableMan setCycle: BegLoop)
				)
				(HandsOn)
				(client illegalBits: cWHITE setScript: 0)
			)
		)
	)
)

(instance sleeper of Script
	(properties)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(HandsOff)
				(ChangeGait MOVE_WALK FALSE)
				(ego hide:)
				(HighPrint 40 34)
				;You curl up on some clean hay and get some rest.
				(self cue:)
			)
			(1
				(if stablemanSpeaks
					(stableMan
						setLoop: 3
						setCel: 1
						setMotion: MoveTo 188 144 self
					)
				else
					(self cue:)
				)
			)
			(2
				(if (not isNightTime)
					(= defaultPalette 1)
					(curRoom drawPic: 40 IRISIN)
					(horse hide: setScript: 0)
					(horseHead hide:)
					(horseTail hide:)
					(manure1 hide:)
					(manure2 hide:)
					(manure3 hide:)
					(= seconds 4)
				else
					(self cue:)
				)
			)
			(3
				(TimedPrint 4 40 35)
				;"Z-Z-Z-Z-Z-Z-Z-Z-Z-Z..."
				(= seconds 4)
			)
			(4
				(if isNightTime
					(EgoSleeps 5 0)
					(= curRoomNum 999)
					(= prevRoomNum 999)
					(curRoom newRoom: 40)
				else
					(HandsOn)
					(EgoSleeps 5 0)
					(= defaultPalette 0)
					(curRoom drawPic: 40 IRISOUT)
					(horse
						loop: 1
						cel: 1
						setCycle: 0
						show:
						setScript: horseEats
					)
					(horseHead show:)
					(horseTail show:)
					(manure1 setCel: 0 show:)
					(manure2 setCel: 0 show:)
					(manure3 setCel: 0 show:)
					(ego show:)
					(client setScript: sleepyIntro)
				)
			)
		)
	)
)

(instance sleepyIntro of Script
	(properties)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(HighPrint 40 36)
				;It IS a rude awakening, however.
				(= cycles 2)
			)
			(1
				(HighPrint 40 37)
				;"WHAT DO YOU THINK THIS IS, A REST HOME???  GET TO WORK!  NOW!!!"
				(= cycles 10)
			)
			(2 (client setScript: goToWork))
		)
	)
)

(instance aDustScript of Script
	(properties)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(client
					setMotion:
						MoveTo
						(if local27
							(+ (ego x?) (Random 70 130))
						else
							(- (ego x?) (Random 70 130))
						)
						(- (+ (ego y?) 10) (Random 0 25))
					setCycle: EndLoop self
				)
			)
			(1
				(client posn: 0 1000 setMotion: 0 setCycle: 0)
			)
		)
	)
)

(instance intro of Script
	(properties)
	
	(method (doit)
		(cond 
			((> timerYesNo 1) (-- timerYesNo))
			((== timerYesNo 1)
				(= timerYesNo 0)
				(TimedPrint 5 40 38)
				;"Well, if you don't want to answer..."
				(self changeState: 5)
			)
		)
		(super doit:)
	)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(if (ego inRect: 95 145 319 189)
					(stableMan
						posn: 187 144
						setLoop: 3
						setCel: 0
						setMotion: MoveTo 171 144 self
					)
					(= stablemanSpeaks 1)
				else
					(self cue:)
				)
			)
			(1
				(cond 
					((Btst STABLE_CLEAN)
						(if
						(and (== timeZone TIME_SUNSET) (== (ego onControl: origin) cYELLOW))
							(client setScript: 0)
						else
							(AlreadyClean)
							(HandsOff)
						)
					)
					((Btst MET_STABLEMAN)
						(TimedPrint 3 40 42)
						;"I see you're back.  Need some work?"
						(= timerYesNo WAIT_STABLES)
						(HandsOff)
						(User canInput: TRUE)
					)
					(else
						(Bset MET_STABLEMAN)
						(HandsOff)
						(User canInput: TRUE)
						(if (== (ego onControl: origin) cYELLOW)
							(HighPrint 40 43)
							;Someone approaches you.
							(TimedPrint 3 40 44)
							;"You lookin' for some work?"
						else
							(TimedPrint 3 40 44)
							;"You lookin' for some work?"
						)
						(= timerYesNo WAIT_STABLES)
					)
				)
				(stableMan setLoop: 4 cel: 0 setCycle: Forward)
				(if (Btst STABLE_CLEAN) (= seconds 6) else (= seconds 3))
			)
			(2
				(if (Btst STABLE_CLEAN)
					(self changeState: 5)
				else
					(stableMan setCycle: BegLoop stopUpd:)
				)
			)
			(3
				(TimedPrint 3 40 45)
				;"Quit fidgeting and answer me...yes or no!"
				(stableMan setCycle: Forward)
				(= seconds 3)
			)
			(4 (stableMan setCycle: 0))
			(5
				(if stablemanSpeaks
					(stableMan
						setLoop: 3
						setCel: 1
						setMotion: MoveTo 188 144 self
					)
				else
					(self cue:)
				)
			)
			(6
				(if (not (ego script?)) (HandsOn))
				(= stablemanSpeaks 0)
				(client setScript: 0)
			)
		)
	)
	
	(method (handleEvent event)
		(if
			(and
				(== (event type?) evSAID)
				(> timerYesNo 1)
			)
			(cond 
				((super handleEvent: event))
				((Said 'affirmative,please')
					(= timerYesNo 0)
					(HighPrint 40 39)
					;"Good!  Come in here and take a rake."
					(ego setScript: goToWork)
					(self changeState: 5)
				)
				((Said 'n')
					(= timerYesNo 0)
					(HighPrint 40 40)
					;Too bad!  I could use some help."
					(self changeState: 5)
				)
				((Said 'ask//labor,hoe,crap')
					(HighPrint 40 41)
					;"That's what I'm asking YOU about, nimrod."
					)
				(else (event claimed: TRUE) (self changeState: 3))
			)
		)
	)
)

;;; Sierra Script 1.0 - (do not remove this comment)
(script# 340)
(include system.sh) (include sci2.sh) (include game.sh)
(use Main)
(use _Interface)
(use dagNabItHelp)
(use _Sound)
(use _Motion)
(use _Game)
(use _Actor)
(use _System)

(public
	dagNabIt 0
)

(local
	newView_3
	newView_4
	forceMenu
	newAct
	newAct_2
	egoNamePlate
	chiefNamePlate
	local7
	local8
	local9
	local10
	local11
	local12
	local13
	egoScore
	chiefScore
	local16
	local17
	local18
	bet =  5
	local20
	local21 =  5
	local22 =  7
	[newView_5 12]
	local35
	theGNewSpeed
	[scoreStr 50]
)
(procedure (localproc_000c param1 param2 param3 param4 param5 param6)
	(return
		(if
			(and
				(<= param3 param1)
				(< param1 param5)
				(<= param4 param2)
			)
			(< param2 param6)
		else
			0
		)
	)
)

(procedure (DisplayDartScore pts fg x y)
	(Display
		(Format @scoreStr 340 0 pts)
		p_at 	x y
		p_mode 	teJustLeft
		p_font 	ARROW_CURSOR
		p_color	fg
		p_back	vBLACK
		p_width	18
	)
)

(procedure (localproc_005a)
	(= local8 (Random 40 100))
	(= local9 (Random 85 100))
)

(procedure (localproc_006f)
	(while (>= (-- local35) 0)
		([newView_5 local35] dispose:)
	)
	(= local35 0)
)

(procedure (localproc_0086 &tmp temp0 temp1)
	(= local10 (+ local8 (* 6 (- (newView_4 cel?) 7))))
	(= local11 (+ local9 (* -8 (newView_3 cel?))))
	(= temp0 (/ (- 100 [egoStats LUCK]) 4))
	(TrySkill LUCK 0 0)
	(if (and (TrySkill THROW 0 0) (> temp0 6)) (= temp0 6))
	(= local10
		(+ local10 (- (= temp1 (Random 0 temp0)) (/ temp0 2)))
	)
	(if temp0
		(= local11
			(+ local11 (- (Random 0 temp0) (/ temp0 2)))
		)
	)
)

(procedure (localproc_0102 &tmp temp0 temp1)
	(if
		(<
			(= temp0 (- local20 (+ dartsBonus (* local16 3))))
			6
		)
		(= temp0 6)
	)
	(if (>= totalDagNabItBet 100) (= temp0 (Random 2 5)))
	(= local10 (+ 70 (- (= temp1 (Random 0 temp0)) (/ temp0 2))))
	(= temp0 (- temp0 temp1))
	(= local11 (+ 45 (- (Random 0 temp0) (/ temp0 2))))
)

(procedure (localproc_015d param1)
	(cond 
		((< param1 0) (= param1 0))
		((> param1 11) (= param1 11))
	)
	(= local21 param1)
	(newView_3 setCel: local21 stopUpd:)
)

(procedure (localproc_0188 param1)
	(cond 
		((< param1 0) (= param1 0))
		((> param1 14) (= param1 14))
	)
	(= local22 param1)
	(newView_4 setCel: local22 stopUpd:)
)

(instance dagNabIt of Room
	(properties
		picture 340
		style $0007
	)
	
	(method (init)
		(super init: &rest)
		(Load RES_VIEW vDartGame)
		(Load RES_VIEW vDartBoard)
		(Load RES_SOUND (GetSongNumber 31))
		(Load RES_SOUND (GetSongNumber 29))
		(Load RES_SOUND (GetSongNumber 30))
		(SolvePuzzle POINTS_PLAYDAGNABIT 3 THIEF)
		(StatusLine code: dagStatus enable:)
		((View new:)
			view: vDartBoard
			loop: 0
			cel: 0
			posn: 70 45
			ignoreActors:
			setPri: 0
			init:
			stopUpd:
			addToPic:
		)
		((View new:)
			view: vDartBoard
			loop: 1
			cel: 0
			posn: 68 183
			ignoreActors:
			setPri: 13
			init:
			stopUpd:
			addToPic:
		)
		((View new:)
			view: vDartBoard
			loop: 2
			cel: 0
			posn: 183 180
			ignoreActors:
			setPri: 1
			init:
			stopUpd:
			addToPic:
		)
		((View new:)
			view: vDartBoard
			loop: 2
			cel: 1
			posn: 272 180
			ignoreActors:
			setPri: 1
			init:
			stopUpd:
			addToPic:
		)
		((= egoNamePlate (View new:))
			view: vDartBoard
			loop: 5
			cel: 0
			ignoreActors:
			posn: 27 178
			setPri: 14
			init:
		)
		((= chiefNamePlate (View new:))
			view: vDartBoard
			loop: 5
			cel: 1
			ignoreActors:
			posn: 110 178
			setPri: 14
			init:
		)
		(dagMusic number: (GetSongNumber 31) init:)
		(self setScript: dagnabitScript)
		(keyDownHandler addToFront: dagnabitScript)
		(directionHandler addToFront: dagnabitScript)
		(mouseDownHandler addToFront: dagnabitScript)
	)
	
	(method (dispose)
		(StatusLine code: dftStatusCode)
		(super dispose:)
	)
)

(instance dagnabitScript of Script
	(properties)
	
	(method (init)
		(= bet 5)
		((= newView_3 (View new:))
			view: vDartBoard
			loop: 3
			cel: local21
			posn: 160 178
			ignoreActors:
			setPri: 2
			init:
			stopUpd:
		)
		((= newView_4 (View new:))
			view: vDartBoard
			loop: 4
			cel: local22
			posn: 272 178
			ignoreActors:
			setPri: 2
			init:
			stopUpd:
		)
		(localproc_005a)
		((= forceMenu (Prop new:))
			view: vDartGame
			setLoop: 2
			cel: 0
			posn: 900 0
			setPri: 6
			ignoreActors:
			init:
			stopUpd:
		)
		((= newAct (Actor new:))
			view: vDartGame
			setLoop: 0
			cel: 0
			posn: 900 100
			setPri: 4
			ignoreActors:
			ignoreHorizon:
			init:
			stopUpd:
		)
		((= newAct_2 (Actor new:))
			view: vDartGame
			setLoop: 6
			cel: 0
			posn: 900 200
			setPri: 7
			ignoreActors:
			init:
			stopUpd:
		)
		(super init: &rest)
	)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				;CI: Here's where the decompile choked... the Repeat.  I've reconsrucuted it best I can from the original asm.
				(repeat
					(= bet (GetNumber {"How much will you bet?"} bet))
					(if (<= bet 0)
						(HighPrint 340 2)
						;"If you don't bet, you can't play."
						(curRoom newRoom: 332)
						(break)
					else
						(if (> bet 50)
							(HighPrint 340 3)
							;"Now, let's not get carried away."
						else
							(if (not (GiveSilverCoins bet))
								(HighPrint 340 4)
								;"Hey!  This is cash on the table.  You can't bet what you don't have."
							else
								(break)
							)
						)
					)
				)
				;; and the rest was able to be decompiled properly
				
				(if (>= totalDagNabItBet 100) 
					(HighPrint 340 5)
					;"So, you think you're pretty hot stuff, eh?  Well, we'll see about that."
				)
				(++ dartsBonus)
				(= local16 (= local12 (= egoScore (= chiefScore 0))))
				(= local20 (- (Random 60 90) bet))
				(= local35 0)
				(DisplayDartScore egoScore 14 47 173)
				(DisplayDartScore chiefScore 11 73 173)
				(self cue:)
			)
			(1
				(HandsOff)
				(= local13 0)
				(= local18 1)
				(localproc_005a)
				(self setScript: blinkScript)
			)
			(2
				(localproc_006f)
				(self setScript: throwScript)
				(self cue:)
			)
			(3
				(forceMenu cel: 0 posn: local8 local9 forceUpd:)
				(newAct_2
					setLoop: 6
					cel: 0
					posn: local8 local9
					setPri: 7
					forceUpd:
				)
				(HandsOn)
				(= local17 1)
			)
			(4
				(= local17 0)
				(HandsOff)
				(localproc_0086)
				(script changeState: 1)
			)
			(5
				(DisplayDartScore egoScore 14 47 173)
				(if (< (++ local13) 3)
					(localproc_005a)
					(self changeState: 3)
				else
					(forceMenu posn: 900 0)
					(self cue:)
				)
			)
			(6
				(egoNamePlate show:)
				(chiefNamePlate hide:)
				(= local18 0)
				(= local13 0)
				(localproc_005a)
				(self setScript: blinkScript)
			)
			(7
				(self setScript: throwScript)
				(self cue:)
			)
			(8
				(forceMenu cel: 0 posn: local8 local9 forceUpd:)
				(= cycles 20)
			)
			(9
				(localproc_0102)
				(++ local16)
				(script changeState: 1)
			)
			(10
				(DisplayDartScore chiefScore 11 73 173)
				(if (< (++ local13) 3)
					(self changeState: 8)
				else
					(chiefNamePlate show:)
					(forceMenu posn: 900 0)
					(++ local12)
					(= cycles 5)
				)
			)
			(11
				(if (< local12 3)
					(self changeState: 1)
				else
					(self cue:)
				)
			)
			(12
				(HandsOn)
				(cond 
					((> egoScore chiefScore)
						(ego get: iSilver (+ bet bet))
						(HighPrint 340 6)
						;"You got me that time.  Say, you're pretty tough!"
						
						(if (>= bet 25) 
							(SolvePuzzle POINTS_WINDAGNABITSMALL 5 THIEF)
						)
						(= totalDagNabItBet (+ totalDagNabItBet bet))
					)
					((< egoScore chiefScore)
						(HighPrint 340 7)
						;"Looks like I won that one.  Must have gotten lucky."
						(= totalDagNabItBet (- totalDagNabItBet bet))
					)
					(else
						(ego get: iSilver bet)
						(if (<= bet 25)
							(HighPrint 340 8)
							;"It's a tie.  Let's double the wager."
							(= bet (+ bet bet))
						else
							(HighPrint 340 9)
							;"Hey, nice game.  It's a tie."
						)
					)
				)
				(localproc_006f)
				(Print 340 10)
				; Shall we play again?
				(self setScript: askPlayAgain self)
			)
			(13
				(if local7
					(self changeState: 0)
				else
					(curRoom newRoom: 332)
				)
			)
		)
	)
	
	(method (handleEvent event &tmp temp0 temp1 temp2)
		(= temp0 (event message?))
		(if (super handleEvent: event) (return 1))
		(return
			(switch (event type?)
				(mouseDown
					(if local17
						(event claimed: TRUE)
						(= temp1 (event x?))
						(= temp2 (event y?))
						(cond 
							((localproc_000c temp1 temp2 33 17 107 76) (self cue:))
							((localproc_000c temp1 temp2 149 154 176 166) (localproc_015d (- local21 1)))
							((localproc_000c temp1 temp2 189 154 218 166) (localproc_015d (+ local21 1)))
							((localproc_000c temp1 temp2 238 150 250 162) (localproc_0188 (- local22 1)))
							((localproc_000c temp1 temp2 294 150 305 162) (localproc_0188 (+ local22 1)))
							((localproc_000c temp1 temp2 155 172 211 180) (localproc_015d (/ (- temp1 160) 4)))
							((localproc_000c temp1 temp2 245 163 298 180) (localproc_0188 (/ (- temp1 249) 3)))
						)
					)
				)
				(keyDown
					(cond 
						((== KEY_QUESTION temp0)
							(event claimed: TRUE)
							(DagNabItHelp)
						)
						((and local17 (== ENTER temp0)) (event claimed: TRUE) (self cue:))
					)
				)
				(speechEvent
					(cond 
						((Said 'cease,end,done,quit,done') (curRoom newRoom: 332))
						(
							(or
								(Said 'ask//game,dagnabit,play,rule')
								(Said 'help,rule')
							)
							(DagNabItHelp)
						)
						((Said 'bet,play,begin')
							(HighPrint 340 1)
							;"Let's finish this game first."
						)
					)
				)
				(direction
					(event claimed: TRUE)
					(if local17
						(switch (event message?)
							(dirN
								(localproc_015d (+ local21 1))
							)
							(dirS
								(localproc_015d (- local21 1))
							)
							(dirE
								(localproc_0188 (+ local22 1))
							)
							(dirW
								(localproc_0188 (- local22 1))
							)
						)
					)
				)
			)
		)
	)
)

(instance askPlayAgain of Script
	(properties)
	
	(method (handleEvent event)
		(cond 
			((super handleEvent: event))
			((!= (event type?) speechEvent))
			((Said 'affirmative,please,play') (= local7 1) (self dispose:))
			((Said 'n,cease,quit') (= local7 0) (self dispose:))
			(else (event claimed: TRUE)
				(HighPrint 340 11)
				;"Just answer the question.  Shall we play again?"
			)
		)
	)
)

(instance throwScript of Script
	(properties)
	
	(method (changeState newState &tmp temp0 temp1)
		(switch (= state newState)
			(0)
			(1
				(= theGNewSpeed speed)
				(theGame setSpeed: 0)
				(forceMenu cycleSpeed: 2 setCycle: CycleTo 2 cdFORWARD self)
				(if local18 (newAct_2 cycleSpeed: 2 setCycle: CycleTo 2 cdFORWARD))
			)
			(2
				(forceMenu cycleSpeed: 0 setCycle: EndLoop self)
				(if local18 (newAct_2 cycleSpeed: 0 setCycle: EndLoop))
				(dagMusic number: (GetSongNumber 31) play:)
			)
			(3
				(forceMenu stopUpd:)
				(if local18 (newAct_2 posn: 900 200 stopUpd:))
				(newAct
					posn: local8 local9
					setPri: 4
					setLoop: (if local18 4 else 0)
					cel: 0
					cycleSpeed: 0
					moveSpeed: 0
					ignoreActors:
					ignoreControl: $FFFF
					setCycle: EndLoop self
					setMotion: MoveTo local10 (+ local11 1)
				)
			)
			(4
				(newAct
					setPri: 3
					setLoop: (if local18 5 else 1)
					cel: 0
					cycleSpeed: 0
					moveSpeed: 0
					setCycle: Forward
					setMotion: MoveTo local10 local11 self
				)
			)
			(5
				(newAct posn: 900 100 stopUpd:)
				(forceMenu cel: 0 forceUpd: stopUpd:)
				((= [newView_5 local35] (View new:))
					view: vDartGame
					loop: (if local18 7 else 3)
					cel: 3
					posn: local10 local11
					ignoreActors:
					setPri: 1
					init:
					stopUpd:
				)
				(++ local35)
				(dagMusic stop:)
				(dagMusic
					number: (if local18 (GetSongNumber 29) else (GetSongNumber 30))
					play:
				)
				(= temp0
					(cond 
						((< local10 65) 0)
						((> local10 75) 2)
						(else 1)
					)
				)
				(= local10 (+ (* (- local10 70) 2) 229))
				(= local11 (+ (* (- local11 45) 2) 61))
				((= [newView_5 local35] (View new:))
					view: vDartGame
					loop: (if local18 7 else 3)
					cel: temp0
					posn: local10 local11
					ignoreActors:
					setPri: 1
					init:
					stopUpd:
				)
				(++ local35)
				(= temp1
					(switch (OnControl CMAP local10 local11)
						(cBLUE 1)
						(cGREEN 2)
						(cCYAN 3)
						(cRED 4)
						(else  0)
					)
				)
				(if local18
					(= egoScore (+ egoScore temp1))
				else
					(= chiefScore (+ chiefScore temp1))
				)
				(theGame setSpeed: theGNewSpeed)
				(client cue:)
			)
		)
	)
)

(instance blinkScript of Script
	(properties)
	
	(method (changeState newState &tmp temp0)
		(= temp0 (if local18 egoNamePlate else chiefNamePlate))
		(switch (= state newState)
			(0 (= register 3) (self cue:))
			(1
				(temp0 hide:)
				(if (not (-- register)) (self changeState: 3))
				(= cycles 6)
			)
			(2
				(temp0 show:)
				(= state 0)
				(= cycles 6)
			)
			(3 (client cue:))
		)
	)
)

(instance dagMusic of Sound
	(properties
		number 31
	)
)

(instance dagStatus of Code
	(properties)
	
	(method (doit strg)
		(Format strg 340 12 score)
		;         Dag-Nab-It  [score %d of 500]
	)
)

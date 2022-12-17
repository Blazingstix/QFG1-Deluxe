;;; Sierra Script 1.0 - (do not remove this comment)
(script# 94)
(include system.sh) (include sci2.sh) (include game.sh)
(use Main)
(use ThrowFlameDart)
(use ThrowDagger1)
(use ThrowRock)
(use AnimateOpen)
(use AnimateDazzle)
(use EG94_brigandCourtyardActions)
(use _LoadMany)
(use _GControl)
(use _Sound)
(use _Motion)
(use _Game)
(use _Inventory)
(use _Actor)
(use _System)
(use _Interface)

(public
	rm94 0
	archer1 1
	archer2 2
	archer3 3
	archer4 4
	archer1Start 5
	archer2Start 6
	archer3Start 7
	archer4Start 8
	unSafeBridge 9
	antwerp 10
	jackCycle 11
	rug 12
	rugs 13
	jack1 14
	jackCycle 15
	jackSound 16
	door 17
	openMess 18
)

(local
	spellCast
	alreadyCastOpen
	archersAreAsleep
	gateIsOpen
	walkingOverRope
	setATrap
	arrowY
	egoLoop
	arrowDirection
	[arrows 28]	;this is for 7 arrows and their details: index = arrowY, index+1 = egoLoop, index+2 = arrowDirection, index+3 = arrowProp
	arrowCel	;cel value of archer?
	distX
	arrowIndex
	rugIsHere = TRUE
)

(procedure (CalcArrowLanding oldEgoDir newEgoDir arrowDir)
	(= distX 0)
	(if setATrap
		(= arrowCel 6)
	else
		(switch oldEgoDir
			(loopE
				(switch newEgoDir
					(loopE
						(if arrowDir
							(= arrowCel 0)
							(= distX 6)
						else
							(= arrowCel 3)
						)
					)
					(loopW
						(if arrowDir
							(= distX -6)
							(= arrowCel 3)
						else
							(= arrowCel 0)
						)
					)
					(else
						(= arrowCel 6)
					)
				)
			)
			(loopW
				(switch newEgoDir
					(loopE
						(if arrowDir
							(= arrowCel 3)
						else
							(= distX 6)
							(= arrowCel 0)
						)
					)
					(loopW
						(if arrowDir
							(= arrowCel 0)
						else
							(= distX -6)
							(= arrowCel 3)
						)
					)
					(else
						(= arrowCel 6)
					)
				)
			)
			(loopS
				(switch newEgoDir
					(loopS
						(if arrowDir
							(= arrowCel 0)
						else
							(= arrowCel 3)
						)
					)
					(loopN
						(if arrowDir
							(= arrowCel 3)
						else
							(= arrowCel 0)
						)
					)
					(else
						(= arrowCel 6)
					)
				)
			)
			(else 
				(switch newEgoDir
					(loopS
						(if arrowDir
							(= arrowCel 3)
						else
							(= arrowCel 0)
						)
					)
					(loopN
						(if arrowDir
							(= arrowCel 0)
						else
							(= arrowCel 3)
						)
					)
					(else
						(= arrowCel 6)
					)
				)
			)
		)
	)
)

(procedure (localproc_015e begX begY endX endY &tmp temp0 temp1)
	(= temp0
		(localproc_0283
			begX
			begY
			(ego x?)
			(- (ego y?) 30)
			(ego x?)
			(- (ego y?) 15)
		)
	)
	(if
		(==
			temp0
			(= temp1
				(localproc_0283
					endX
					endY
					(ego x?)
					(- (ego y?) 30)
					(ego x?)
					(- (ego y?) 15)
				)
			)
		)
		(return FALSE)
	)
	(= temp0
		(localproc_0283
			(ego x?)
			(- (ego y?) 30)
			begX
			begY
			endX
			endY
		)
	)
	(return
		(if
			(==
				temp0
				(= temp1
					(localproc_0283
						(ego x?)
						(- (ego y?) 15)
						begX
						begY
						endX
						endY
					)
				)
			)
			(return FALSE)
		else
			(= arrowY
				(-
					(/
						(+
							(* endY (ego x?))
							(- (* endY begX))
							(- (* begY (ego x?)))
							(* begY endX)
						)
						(- endX begX)
					)
					(ego y?)
				)
			)
			(= egoLoop (ego loop?))
			(if (< (ego x?) begX)
				(= arrowDirection TRUE)
			else
				(= arrowDirection FALSE)
			)
			(return TRUE)
		)
	)
)

(procedure (localproc_0283 x1 y1 x2 y2 x3 y3)
	(return
		(if
			(>
				(+
					(* y3 x2)
					(- (* x3 y2))
					(* x1 (- y2 y3))
					(* y1 (- x3 x2))
				)
				0
			)
			(return TRUE)
		else
			(return FALSE)
		)
	)
)

(procedure (SetupArchers wakeup)
	(if (or wakeup (rm94 notify: 0))
		(archer1 setScript: archer1Start)
		(archer2 setScript: archer2Start)
		(archer3 setScript: archer3Start)
		(archer4 setScript: archer4Start)
	)
)

(class Archer of Script
	(properties
		archerStop 	0
		fequencyA 	3
		fequencyB 	10
		arrowsShot 	0
		maxArrows 	30
		minArrows 	15
		clientP 	0
		startCel 	0
		shootX 		0
		shootY 		0
		hideX 		0
		hideY 		0
		arrowV 		NULL
		arrowL 		NULL
		arrowC 		NULL
		arrowP 		NULL
		startX 		0
		startY 		0
		A1 			0
		B1 			0
		X1 			0
		Y1 			0
		A2 			0
		B2 			0
		X2 			0
		Y2 			0
		A3 			0
		B3 			0
		X3 			0
		Y3 			0
		A4 			0
		B4 			0
		X4 			0
		Y4 			0
		A5 			0
		B5 			0
		X5 			0
		Y5 			0
		A6 			0
		B6 			0
		X6 			0
		Y6 			0
		begX 		0
		begY 		0
		endX 		0
		endY 		0
		archDeltaX 	0
		archDeltaY 	0
	)
	
	(method (doit)
		(if archerStop (-- archerStop))
		(cond 
			(
				(and
					(< state 3)
					(or
						(> arrowsShot maxArrows)
						(and archerStop (!= state 10))
						(and
							(< -20 (- startX (ego x?)))
							(< (- startX (ego x?)) 20)
							(!= state 10)
							(< -20 (- startY (ego y?)))
							(< (- startY (ego y?)) 20)
						)
					)
				)
				(self changeState: 10)
			)
			(
			(and (<= arrowsShot maxArrows) (not archerStop))
				(if
					(and
						(not
							(if (< -50 (- startX (ego x?)))
								(< (- startX (ego x?)) 50)
							)
						)
						(== state 11)
					)
					(self changeState: 0)
				)
			)
		)
		(super doit:)
	)
	
	(method (changeState newState &tmp [temp0 40])
		(switch (= state newState)
			(0
				(client
					setCel: startCel
					illegalBits: 0
					ignoreActors:
					setPri: clientP
					setMotion: MoveTo shootX shootY self
				)
			)
			(1
				(= cycles (Random fequencyA fequencyB))
			)
			(2
				(if (self archerShoot:)
					(self arrowPath:)
					((= arrowP (Prop new:))
						view: arrowV
						setLoop: arrowL
						setCel: arrowC
						setPri: 15
						ignoreActors:
						hide:
						init:
					)
					(client setCel: 0 setCycle: CycleTo startCel cdFORWARD self)
				else
					(self changeState: 1)
				)
			)
			(3
				(self nextPath:)
				(if
					(and
						(== (ego script?) 0)
						(localproc_015e begX begY endX endY)
					)
					(arrowP hide:)
					(ego setScript: egoHit 0 arrowP)
					(self changeState: 1)
				else
					(arrowP show: posn: endX endY)
				)
				(client stopUpd:)
				(= cycles 2)
			)
			(4
				(self nextPath:)
				(if
					(and
						(== (ego script?) 0)
						(localproc_015e begX begY endX endY)
					)
					(arrowP hide:)
					(ego setScript: egoHit 0 arrowP)
					(self changeState: 1)
				else
					(arrowP posn: endX endY)
					(= cycles 2)
				)
			)
			(5
				(self nextPath:)
				(if
					(and
						(== (ego script?) 0)
						(localproc_015e begX begY endX endY)
					)
					(arrowP hide:)
					(ego setScript: egoHit 0 arrowP)
					(self changeState: 1)
				else
					(arrowP posn: endX endY)
					(= cycles 2)
				)
			)
			(6
				(self nextPath:)
				(if
					(and
						(== (ego script?) 0)
						(localproc_015e begX begY endX endY)
					)
					(arrowP hide:)
					(ego setScript: egoHit 0 arrowP)
					(self changeState: 1)
				else
					(arrowP posn: endX endY)
					(= cycles 2)
				)
			)
			(7
				(self nextPath:)
				(if
					(and
						(== (ego script?) 0)
						(localproc_015e begX begY endX endY)
					)
					(arrowP hide:)
					(ego setScript: egoHit 0 arrowP)
					(self changeState: 1)
				else
					(arrowP posn: endX endY)
					(= cycles 2)
				)
			)
			(8
				(++ arrowsShot)
				(if (< arrowsShot minArrows)
					(arrowP addToPic:)
				else
					(arrowP dispose:)
				)
				(self changeState: 1)
			)
			(10
				(client
					setCel: startCel
					setMotion: MoveTo hideX hideY self
				)
			)
			(11 (client stopUpd:))
		)
	)
	
	(method (archerShoot)
		(return
			(cond 
				(
					(and
						(<= 90 (ego y?))
						(<= (ego y?) 100)
						(< A1 (ego x?))
						(< (ego x?) B1)
					)
					(= endX (- X1 (Random 2 8)))
					(= endY (- Y1 (Random 4 16)))
					(return 1)
				)
				(
					(and
						(<= 100 (ego y?))
						(<= (ego y?) 110)
						(< A2 (ego x?))
						(< (ego x?) B2)
					)
					(= endX (- X2 (Random 2 8)))
					(= endY (- Y2 (Random 4 16)))
					(return 1)
				)
				(
					(and
						(<= 110 (ego y?))
						(<= (ego y?) 120)
						(< A3 (ego x?))
						(< (ego x?) B3)
					)
					(= endX (- X3 (Random 2 8)))
					(= endY (- Y3 (Random 4 16)))
					(return 1)
				)
				(
					(and
						(<= 120 (ego y?))
						(<= (ego y?) 130)
						(< A4 (ego x?))
						(< (ego x?) B4)
					)
					(= endX (- X4 (Random 2 8)))
					(= endY (- Y4 (Random 4 16)))
					(return 1)
				)
				(
					(and
						(<= 130 (ego y?))
						(<= (ego y?) 140)
						(< A5 (ego x?))
						(< (ego x?) B5)
					)
					(= endX (- X5 (Random 2 8)))
					(= endY (- Y5 (Random 4 16)))
					(return 1)
				)
				(
					(and
						(<= 140 (ego y?))
						(<= (ego y?) 150)
						(< A6 (ego x?))
						(< (ego x?) B6)
					)
					(= endX (- X6 (Random 2 8)))
					(= endY (- Y6 (Random 4 16)))
					(return 1)
				)
				(else (return 0))
			)
		)
	)
	
	(method (arrowPath)
		(= archDeltaX (- endX startX))
		(= archDeltaX (/ archDeltaX 5))
		(= archDeltaY (- endY startY))
		(= archDeltaY (/ archDeltaY 5))
		(= endX startX)
		(= endY startY)
		(= begX startX)
		(= begY startY)
	)
	
	(method (nextPath)
		(= endX (+ (= begX endX) archDeltaX))
		(= endY (+ (= begY endY) archDeltaY))
	)
)

(instance rm94 of Room
	(properties
		picture 94
		style DISSOLVE
	)
	
	(method (init)
		(LoadMany RES_VIEW vBrigandCourtyard vBrigandArcher vAntwerp (GetEgoViewNumber vEgoFalling) (GetEgoViewNumber vEgoTired) (GetEgoViewNumber vEgoFall2) (GetEgoViewNumber vEgoShock))
		(LoadMany RES_SOUND
			(GetSongNumber 73)
			(GetSongNumber 35)
			(GetSongNumber 81)
		)
		(LoadMany RES_SCRIPT 186 187 188 189 190 191 192 193 194 195 103 102)
		(cSound number: (GetSongNumber 73) loop: -1 play:)
		(super init:)
		(mouseDownHandler add: self)
		(self
			setFeatures:
				onSacks
				onKegs
				onRug
				onRugs
				onWire1
				onUnSafeBridge
				onSafeBridge
				onChasm
				onDoor
				onWindow
				onRCabinet
				onLCabinet
				onLumber1
				onLitter1
		)
		(SolvePuzzle POINTS_ENTERBRIGANDFORTRESS 8)
		(StatusLine enable:)
		(StopEgo)
		(rug ignoreActors: 1 setPri: 11 init: stopUpd:)
		(rugs ignoreActors: 1 setPri: 11 init: stopUpd:)
		(unSafeBridge ignoreActors: 1 setPri: 6 init: stopUpd:)
		(safeBridge ignoreActors: 1 setPri: 6 init: addToPic:)
		(jackCycle init: hide:)
		(archer1 init: stopUpd:)
		(archer2 init: stopUpd:)
		(archer3 init: stopUpd:)
		(archer4 init: stopUpd:)
		(= archersAreAsleep TRUE)
		(if (and (not (Btst BRIGANDS_UNAWARE)) (Btst DEFEATED_MINOTAUR))
			(Bset VISITED_BRIGAND_COURTYARD)
			(= archersAreAsleep FALSE)
			(SetupArchers TRUE)
		)
		(switch prevRoomNum
			(95
				(door setCel: 2 setPri: 1 init:)
				(= gateIsOpen 1)
				(ego posn: 223 76 init: setMotion: MoveTo 190 76)
			)
			(else 
				(door setPri: 1 init:)
				(ego posn: 157 188 init: setMotion: MoveTo 157 172)
			)
		)
	)
	
	;CI: This is a manual decompilation.
	(method (doit)
		(cond 
			((and (not setATrap)
				  (ego inRect: 90 106 116 12)
				)
				(= setATrap TRUE)
				(ego setScript: (ScriptID 186 0)) ;fallbridge
			)
			((and (not setATrap)
				  (not (and (<= 215 (ego x?)) (<= (ego x?) 235)))
				  (<= 108 (ego y?)) (<= (ego y?) 111))
				(= setATrap TRUE)
				(ego setScript: (ScriptID 187 0)) ;fallChasm
			)
			((and (ego inRect: 154 76 167 83)
				  (== loopN (ego loop?))
				  (not walkingOverRope)
				  (not setATrap)
				)
				(= setATrap TRUE)
				(ego setScript: (ScriptID 188 0)) ;egoTripsNorth
			)
			((and (ego inRect: 282 156 293 162)
				  (== loopN (ego loop?))
				  (not walkingOverRope)
				  (!= (ego script?) (ScriptID 189 0))
			 	)
				(ego setScript: (ScriptID 189 0)) ;egoTripsSouth
			)
			((and (ego inRect: 139 145 169 152)
				  (not setATrap)
			 	)
			 	(= setATrap TRUE)
			 	(ego setPri: 11 setScript: (ScriptID 190 0)) ;fallRug
			)
			((and (ego inRect: 140 145 187 152)
				(not setATrap)
			 	)
			 	(= setATrap TRUE)
			 	(ego setScript: (ScriptID 191 0)) ;fallRugs
			)
		)
			
		;now check if we're on the exits
		(if (and gateIsOpen (& (ego onControl: origin) cBLUE))
			(curRoom newRoom: 95)
		else
			(if (and (== (ego edgeHit?) SOUTH) (not setATrap))
				(curRoom newRoom: 93)
			)
		)
		
		(super doit:)
	)
	
;	;CI: This method was not automatically decompiled by SCICompanion.
;	(method (doit)
;		(asm
;
;;condition 1
;			lal      setATrap			;(if not setATrap, continue.
;			not     
;			bnt      code_0ce0		;otherwise bounce to the next line
;			
;			pushi    #inRect
;			pushi    4
;			pushi    90
;			pushi    106
;			pushi    116
;			pushi    112
;			lag      ego
;			send     12
;			bnt      code_0ce0		;(if (ego inRect: 90 106 116 12)
;			
;			ldi      TRUE					;(= setATrap TRUE)
;			sal      setATrap
;			pushi    #setScript
;			pushi    1
;			pushi    2
;			pushi    186
;			pushi    0
;			callk    ScriptID,  4	;(ego setScript: (ScriptID 186 0)) ;fallbridge
;			push    
;			lag      ego
;			send     6
;			jmp      code_0e2e	;jump to 2nd wave
;
;;condition 2			
;code_0ce0:
;			lal      setATrap			;if setATrap
;			not     
;			bnt      code_0d28		;if setATrap then goto next inRect
;			
;			
;			pushi    215
;			pushi    #x
;			pushi    0
;			lag      ego
;			send     4
;			le?     		;(<= 215 (ego x?) )
;			bnt      code_0cf9
;			pprev   
;			ldi      235	
;			le?     		;(<= (ego x?) 235)
;code_0cf9:
;			not     
;			bnt      code_0d28
;			pushi    108
;			pushi    #y
;			pushi    0
;			lag      ego
;			send     4			
;			le?     		;(<= 108 (ego y?))
;			bnt      code_0d28
;			pprev   
;			ldi      111
;			le?     		;(<= (ego y?) 111)
;			bnt      code_0d28
;			ldi      TRUE
;			sal      setATrap		;(= setATrap TRUE)
;			pushi    #setScript
;			pushi    1
;
;			pushi    2
;			pushi    187
;			pushi    0
;			callk    ScriptID,  4
;			push    
;
;			lag      ego		;(ego setScript: (ScriptID 187 0)) ;fallChasm
;			send     6
;
;			jmp      code_0e2e	;jump to 2nd wave
;
;;condition 3
;code_0d28:
;			pushi    #inRect
;			pushi    4
;			pushi    154
;			pushi    76
;			pushi    167
;			pushi    83
;			lag      ego
;			send     12				;(ego inRect 154 76 167 83)
;			bnt      code_0d6f
;			pushi    #loop		
;			pushi    0
;			lag      ego
;			send     4				;(ego loop?)
;			push    
;			ldi      loopN			;(== loopN (ego loop?))
;			eq?     
;			bnt      code_0d6f
;			lal      walkingOverRope
;			not     				;(not walkingOverRope)
;			bnt      code_0d6f
;			lal      setATrap
;			not     				;(not setATrap
;			bnt      code_0d6f
;			ldi      TRUE
;			sal      setATrap		;(= setATrap TRUE)
;			pushi    #setScript
;			pushi    1
;			pushi    2
;			pushi    188
;			pushi    0
;			callk    ScriptID,  4
;			push    
;			lag      ego			;(ego setScript: (ScriptID 188 0)) ;egoTripsNorth
;			send     6
;			jmp      code_0e2e	;jump to 2nd wave
;
;;condition 4
;code_0d6f:
;			pushi    #inRect
;			pushi    4
;			pushi    282
;			pushi    156
;			pushi    293
;			pushi    162
;			lag      ego
;			send     12				;(ego inRect: 282 156 293 162)
;			bnt      code_0dc2
;			pushi    #loop
;			pushi    0
;			lag      ego
;			send     4
;			push    
;			ldi      loopN			;(== loopN (ego loop?))
;			eq?     
;			bnt      code_0dc2
;			lal      walkingOverRope			;(not walkingOverRope)
;			not     
;			bnt      code_0dc2
;			pushi    #script
;			pushi    0
;			lag      ego
;			send     4
;			push    
;			pushi    2
;			pushi    189
;			pushi    0
;			callk    ScriptID,  4
;			ne?     				;(!= (ego script?) (ScriptID 189 0))
;			bnt      code_0dc2
;			pushi    #setScript
;			pushi    1
;			pushi    2
;			pushi    189
;			pushi    0
;			callk    ScriptID,  4
;			push    
;			lag      ego			;(ego setScript: (ScriptID 189 0)) ;egoTripsSouth
;			send     6
;			jmp      code_0e2e	;jump to 2nd wave
;
;;condition 5
;code_0dc2:
;			pushi    #inRect
;			pushi    4
;			pushi    139
;			pushi    145
;			pushi    169
;			pushi    152
;			lag      ego
;			send     12				;(ego inRect: 139 145 169 152)
;			bnt      code_0dfc
;			lal      setATrap		;(not setATrap)
;			not     
;			bnt      code_0dfc
;			ldi      TRUE
;			sal      setATrap		;(= setATrap TRUE)
;			pushi    #setPri
;			pushi    1
;			pushi    11
;			pushi    #setScript
;			pushi    1
;			pushi    2
;			pushi    190
;			pushi    0
;			callk    ScriptID,  4
;			push    
;			lag      ego			;(ego setPri: 11 setScript: (ScriptID 190 0)) ;fallRug
;			send     12
;			jmp      code_0e2e	;jump to 2nd wave
;
;;condition 6
;code_0dfc:
;			pushi    #inRect
;			pushi    4
;			pushi    140
;			pushi    145
;			pushi    187
;			pushi    152
;			lag      ego
;			send     12			;(ego inRect: 140 145 187 152)
;			bnt      code_0e2e	;jump to 2nd wave
;			lal      setATrap
;			not     
;			bnt      code_0e2e	;jump to 2nd wave
;			ldi      TRUE
;			sal      setATrap  ;(= setATrap TRUE)
;			pushi    #setScript
;			pushi    1
;			pushi    2
;			pushi    191
;			pushi    0
;			callk    ScriptID,  4
;			push    
;			lag      ego		;(ego setScript: (ScriptID 191 0)) ;fallRugs
;			send     6
;;end of conditions
;
;
;code_0e2e:							;second wave (outside of cond)
;			lal      gateIsOpen			;(if gateIsOpen
;			bnt      code_0e50
;			pushi    #onControl
;			pushi    1
;			pushi    origin
;			lag      ego			
;			send     6
;			push    
;			ldi      cBLUE
;			and     				;(& (ego onControl: origin) cBLUE)
;			bnt      code_0e50
;			pushi    #newRoom
;			pushi    1
;			pushi    95
;			lag      curRoom		;(curRoom neRoom: 95)
;			send     6
;			jmp      code_0e6f	;goto (super doit:)
;code_0e50:
;			pushi    #edgeHit
;			pushi    0
;			lag      ego
;			send     4
;			push    
;			ldi      SOUTH
;			eq?     
;			bnt      code_0e6f	;goto (super doit:)
;			lal      setATrap
;			not     
;			bnt      code_0e6f	;goto (super doit:)
;			pushi    #newRoom
;			pushi    1
;			pushi    93
;			lag      curRoom
;			send     6
;code_0e6f:
;			pushi    #doit
;			pushi    0
;			super    Room,  4	;(super doit:)
;			ret     
;		)
;	)
	
	(method (dispose)
		(super dispose:)
		(LoadMany FALSE 186 187 188 189 190 191 192 193 194 195)
	)
	
	(method (handleEvent event)
		(switch (event type?)
			(mouseDown
				(cond 
					((super handleEvent: event))
					((MouseClaimed ego event emRIGHT_BUTTON)
						(HighPrint 94 0)
						;You get the feeling that you're not alone.
						)
				)
			)
			(evSAID
				(cond 
					((super handleEvent: event))
					((Said 'rest,nap')
						(EgoDead DIE_RETRY DIE_ENDGAME_REST 94 1
							#title {Z-Z-Z-Z-Z-Z-Z-Z-Z-Z}
							#icon (GetEgoViewNumber vEgoDeathScenes) 0 0
						)
						;The Brigands oblige you by making your pleasant rest permanent!
						(= curRoomNum 93)
						(curRoom newRoom: 94)
					)
					((Said 'cast>')
						(= spellCast (SaidCast event))
						(if (CastSpell spellCast)
							(switch spellCast
								(DETMAGIC
									(HighPrint 94 2)
									;No spells are active here.
									)
								(DAZZLE
									(if (not (rm94 notify: 0))
										(if (AnimateDazzle) (rm94 notify: 2))
									else
										(HighPrint 94 3)
										;That was a useless gesture.
									)
								)
								(FLAMEDART
									(AnimateThrowingFlameDart NULL)
									(SetupArchers 0)
								)
								(CALM
									(if (rm94 notify: 0)
										(HighPrint 94 3)
										;That was a useless gesture.
									else
										(HighPrint 94 4)
										;None of the brigands notices your Calm spell.  There is too much negative thought energy here.
									)
								)
								(OPEN
									(if (rm94 notify: 1)
										(HighPrint 94 5)
										;That was a useless spell.
									else
										(rm94 setScript: openMess)
										(if (AnimateOpenSpell ego openMess)
											(jackCycle setScript: (ScriptID 192 0))
										else
											(openMess changeState: 2)
										)
									)
								)
								;CI: added back in Zap spell, redirecting to the main script
								(ZAP
									(event claimed: FALSE)
								)
								(else
									(HighPrint 94 5)
									;That was a useless spell.
									)
							)
						)
					)
					((Said 'throw/boulder')
						(if (AnimateThrowingRock NULL)
							(SetupArchers FALSE)
						)
					)
					((Said 'throw/dagger')
						(if (AnimateThrowingDagger NULL)
							(SetupArchers FALSE)
						)
					)
					(
					(Said 'step,hop,climb[/above,rope,string,string]')
						(cond 
							((ego inRect: 145 72 175 100)
								(ego setScript: (ScriptID 195 0))
							)
							((ego inRect: 275 152 298 170)
								(ego setScript: (ScriptID 195 1))
							)
							(else 
								(HighPrint 94 6)
							)
						)
					)
					((Said 'open[/gate,door]')
						(cond 
							(gateIsOpen
								(HighPrint 94 7)
								;The door is open.
							)
							((ego inRect: 193 0 320 74)
								(door setScript: (ScriptID 194 0))
							)
							(else
								(HighPrint 94 8)
								;You must get closer.
							)
						)
					)
					((Said 'say,ask')
						(HighPrint 94 9)
						;This is no time for casual conversation.
					)
					(archersAreAsleep
						(SaidEndGameCourtyard event)
					)
					(else (event claimed: TRUE)
						(HighPrint 94 10)
						;You're too busy trying not to become a human pincushion.
					)
				)
			)
		)
	)
	
	(method (notify param1)
		(return
			(switch param1
				(0
					(if (== archersAreAsleep TRUE)
						(= archersAreAsleep FALSE)
						(return TRUE)
					else
						(return FALSE)
					)
				)
				(1
					(if (== alreadyCastOpen TRUE)
						(return TRUE)
					else
						(= alreadyCastOpen TRUE)
						(return FALSE)
					)
				)
				(2
					(archer1Start archerStop: 100)
					(archer2Start archerStop: 100)
					(archer3Start archerStop: 100)
					(archer4Start archerStop: 100)
				)
				(3
					(if (== gateIsOpen TRUE) (return TRUE) else (return FALSE))
				)
				(4 (= gateIsOpen TRUE))
				(5 (= gateIsOpen FALSE))
				(6 (= walkingOverRope TRUE))
				(7 (= walkingOverRope FALSE))
				(8 (= rugIsHere FALSE))
			)
		)
	)
)

(instance rug of Prop
	(properties
		y 163
		x 153
		view vBrigandCourtyard
		loop 0
		priority 9
	)
)

(instance rugs of Prop
	(properties
		y 163
		x 180
		view vBrigandCourtyard
		loop 1
		priority 12
	)
)

(instance unSafeBridge of Prop
	(properties
		y 118
		x 101
		view vBrigandCourtyard
		loop 2
		priority 8
	)
)

(instance safeBridge of Prop
	(properties
		y 118
		x 227
		view vBrigandCourtyard
		loop 3
		priority 8
	)
)

(instance door of Prop
	(properties
		y 59
		x 208
		view vBrigandCourtyard
		loop 4
		priority 1
	)
)

(instance jack1 of View
	(properties
		y 149
		x 307
		view vBrigandCourtyard
		loop 5
		priority 13
	)
)

(instance jackCycle of Prop
	(properties
		y 151
		x 307
		view vBrigandCourtyard
		loop 6
		priority 13
	)
)

(instance jackSound of Sound
	(properties
		number 81
		priority 3
	)
)

(instance archer1 of Actor
	(properties
		y 78
		x 16
		view vBrigandArcher
		priority 4
	)
	
	(method (doit)
		(= arrowIndex 0)
		(while (<= arrowIndex 24)
			(if (!= [arrows arrowIndex] 0)
				(CalcArrowLanding
					[arrows (+ arrowIndex 1)]
					(ego loop?)
					[arrows (+ arrowIndex 2)]
				)
				([arrows (+ arrowIndex 3)]
					setCel: arrowCel
					setPri: (ego priority?)
					posn:
						(+ (ego x?) distX)
						(+ (ego y?) [arrows arrowIndex])
				)
			)
			(= arrowIndex (+ arrowIndex 4))
		)
		(super doit:)
	)
	
	(method (handleEvent event)
		(cond 
			((super handleEvent: event))
			((MouseClaimed archer1 event emRIGHT_BUTTON)
				(HighPrint 94 11)
				;His aim in life is to make shish kebab.  I hope you're not skewered.
				)
		)
	)
)

(instance archer2 of Actor
	(properties
		y 80
		x 296
		view vBrigandArcher
		priority 4
	)
	
	(method (handleEvent event)
		(cond 
			((super handleEvent: event))
			((MouseClaimed archer1 event emRIGHT_BUTTON)
				(HighPrint 94 12)
				;Life as an archer is not dull or pointless.
				)
		)
	)
)

(instance archer3 of Actor
	(properties
		y 106
		x -23
		view vBrigandArcher
		priority 7
	)
	
	(method (handleEvent event)
		(cond 
			((super handleEvent: event))
			((MouseClaimed archer1 event emRIGHT_BUTTON)
				(HighPrint 94 13)
				;He has an arrow with your name on it.
				)
		)
	)
)

(instance archer4 of Actor
	(properties
		y 110
		x 343
		view vBrigandArcher
		priority 7
	)
	
	(method (handleEvent event)
		(cond 
			((super handleEvent: event))
			((MouseClaimed archer1 event emRIGHT_BUTTON)
				(HighPrint 94 14)
				;He has an IQ so low he can't spell IQ.
				)
		)
	)
)

(instance antwerp of Actor
	(properties
		view vAntwerp
		loop 7
		priority 4
	)
)

(instance onSacks of RFeature
	(properties
		nsTop 61
		nsLeft 72
		nsBottom 84
		nsRight 140
	)
	
	(method (handleEvent event)
		(cond 
			((super handleEvent: event))
			((MouseClaimed onSacks event emRIGHT_BUTTON)
				(HighPrint 94 15)
				;Sacks of grain.  Stored for the winter.
				)
		)
	)
)

(instance onKegs of RFeature
	(properties
		nsTop 61
		nsLeft 179
		nsBottom 84
		nsRight 243
	)
	
	(method (handleEvent event)
		(cond 
			((super handleEvent: event))
			((MouseClaimed onKegs event emRIGHT_BUTTON)
				(HighPrint 94 16)
				;Empty wine barrels.  The winters here are very cold.
				)
		)
	)
)

(instance onRug of RFeature
	(properties
		nsTop 139
		nsLeft 130
		nsBottom 159
		nsRight 174
	)
	
	(method (handleEvent event)
		(cond 
			((super handleEvent: event))
			((MouseClaimed onRug event emRIGHT_BUTTON)
				(HighPrint 94 17)
				;Rug with message "Step Here".
				)
		)
	)
)

(instance onRugs of RFeature
	(properties
		nsTop 139
		nsLeft 175
		nsBottom 161
		nsRight 197
	)
	
	(method (handleEvent event)
		(cond 
			((super handleEvent: event))
			((MouseClaimed onRugs event emRIGHT_BUTTON)
				(HighPrint 94 18)
				;These rolled-up rugs are spares for use when the rug at the left is lost.
				)
		)
	)
)

(instance onWire1 of RFeature
	(properties
		nsTop 156
		nsLeft 278
		nsBottom 156
		nsRight 297
	)
	
	(method (handleEvent event)
		(cond 
			((super handleEvent: event))
			(
				(or
					(MouseClaimed onWire1 event emRIGHT_BUTTON)
					(MouseClaimed onWire2 event emRIGHT_BUTTON)
				)
				(HighPrint 94 19)
				;A rope tautly strung.
			)
		)
	)
)

(instance onWire2 of RFeature
	(properties
		nsTop 77
		nsLeft 142
		nsBottom 77
		nsRight 176
	)
)

(instance onUnSafeBridge of RFeature
	(properties
		nsTop 104
		nsLeft 89
		nsBottom 116
		nsRight 110
	)
	
	(method (handleEvent event)
		(cond 
			((super handleEvent: event))
			((MouseClaimed onUnSafeBridge event emRIGHT_BUTTON)
				(HighPrint 94 20)
				;Sturdy looking bridge with one support pole across the chasm.
				)
		)
	)
)

(instance onSafeBridge of RFeature
	(properties
		nsTop 104
		nsLeft 213
		nsBottom 116
		nsRight 240
	)
	
	(method (handleEvent event)
		(cond 
			((super handleEvent: event))
			((MouseClaimed onSafeBridge event emRIGHT_BUTTON)
				(HighPrint 94 21)
				;Sturdy looking bridge with two support poles across the chasm.  A message reading "Cross Here" is on this bridge.
				)
		)
	)
)

(instance onChasm of RFeature
	(properties
		nsTop 104
		nsLeft 36
		nsBottom 116
		nsRight 291
	)
	
	(method (handleEvent event)
		(cond 
			((super handleEvent: event))
			(
				(and
					(not (MouseClaimed onSafeBridge event emRIGHT_BUTTON))
					(not (MouseClaimed onUnSafeBridge event emRIGHT_BUTTON))
					(MouseClaimed onChasm event emRIGHT_BUTTON)
				)
				(HighPrint 94 22)
				;Chasm across the room.  The only safe passage is across the bridges.
			)
		)
	)
)

(instance onDoor of RFeature
	(properties
		nsTop 29
		nsLeft 208
		nsBottom 58
		nsRight 237
	)
	
	(method (handleEvent event)
		(cond 
			((super handleEvent: event))
			((MouseClaimed onDoor event emRIGHT_BUTTON)
				(HighPrint 94 23)
				;Door to some other room.
				)
		)
	)
)

(instance onWindow of RFeature
	(properties
		nsTop 29
		nsLeft 167
		nsBottom 47
		nsRight 197
	)
	
	(method (handleEvent event)
		(cond 
			((super handleEvent: event))
			((MouseClaimed onWindow event emRIGHT_BUTTON)
				(HighPrint 94 24)
				;Window showing vague outlines and shapes.
				)
		)
	)
)

(instance onRCabinet of RFeature
	(properties
		nsTop 141
		nsLeft 299
		nsBottom 175
		nsRight 318
	)
	
	(method (handleEvent event)
		(cond 
			((super handleEvent: event))
			((MouseClaimed onRCabinet event emRIGHT_BUTTON)
				(HighPrint 94 25)
				;Some kind of cabinet showing a flip top secured by a hasp.
				)
		)
	)
)

(instance onLCabinet of RFeature
	(properties
		nsTop 143
		nsBottom 178
		nsRight 12
	)
	
	(method (handleEvent event)
		(cond 
			((super handleEvent: event))
			((MouseClaimed onLCabinet event emRIGHT_BUTTON)
				(HighPrint 94 26)
				;Some kind of cabinet.
				)
		)
	)
)

(instance onLumber1 of RFeature
	(properties
		nsTop 129
		nsLeft 44
		nsBottom 157
		nsRight 124
	)
	
	(method (handleEvent event)
		(cond 
			((super handleEvent: event))
			(
				(or
					(MouseClaimed onLumber1 event emRIGHT_BUTTON)
					(MouseClaimed onLumber2 event emRIGHT_BUTTON)
				)
				(HighPrint 94 27)
				;Fortification for archers to stand behind.
			)
		)
	)
)

(instance onLumber2 of RFeature
	(properties
		nsTop 131
		nsLeft 200
		nsBottom 157
		nsRight 276
	)
)

(instance onLitter1 of RFeature
	(properties
		nsTop 170
		nsLeft 274
		nsBottom 175
		nsRight 287
	)
	
	(method (handleEvent event)
		(cond 
			((super handleEvent: event))
			(
				(or
					(MouseClaimed onLitter1 event emRIGHT_BUTTON)
					(MouseClaimed onLitter2 event emRIGHT_BUTTON)
					(MouseClaimed onLitter3 event emRIGHT_BUTTON)
					(MouseClaimed onLitter4 event emRIGHT_BUTTON)
					(MouseClaimed onLitter5 event emRIGHT_BUTTON)
				)
				(HighPrint 94 28)
				;The stuff that dreams are made of.  Left here by some somnambulistic brigand.
			)
		)
	)
)

(instance onLitter2 of RFeature
	(properties
		nsTop 122
		nsLeft 275
		nsBottom 128
		nsRight 287
	)
)

(instance onLitter3 of RFeature
	(properties
		nsTop 73
		nsLeft 244
		nsBottom 79
		nsRight 251
	)
)

(instance onLitter4 of RFeature
	(properties
		nsTop 83
		nsLeft 53
		nsBottom 92
		nsRight 63
	)
)

(instance onLitter5 of RFeature
	(properties
		nsTop 116
		nsLeft 18
		nsBottom 124
		nsRight 27
	)
)

(instance archer1Start of Archer
	(properties
		fequencyA 5
		fequencyB 15
		clientP 4
		startCel 4
		shootX 49
		shootY 78
		hideX 16
		hideY 78
		arrowV vBrigandArcher
		arrowL 6
		arrowC 3
		startX 84
		startY 65
		B1 320
		X1 260
		Y1 84
		B2 320
		X2 276
		Y2 97
		B3 320
		X3 294
		Y3 113
		A4 160
		B4 320
		X4 315
		Y4 133
		A5 240
		B5 320
		X5 321
		Y5 141
		A6 1
	)
)

(instance archer2Start of Archer
	(properties
		fequencyA 5
		fequencyB 15
		clientP 4
		startCel 4
		shootX 275
		shootY 80
		hideX 296
		hideY 80
		arrowV vBrigandArcher
		arrowL 6
		startX 250
		startY 67
		B1 320
		X1 67
		Y1 77
		B2 320
		X2 47
		Y2 97
		B3 170
		X3 25
		Y3 116
		B4 150
		X4 9
		Y4 132
		B5 145
		X5 5
		Y5 142
		A6 1
	)
)

(instance archer3Start of Archer
	(properties
		fequencyA 5
		fequencyB 15
		clientP 7
		startCel 4
		shootX 10
		shootY 106
		hideX -23
		hideY 106
		arrowV vBrigandArcher
		arrowL 6
		arrowC 3
		startX 35
		startY 93
		A1 200
		B1 320
		X1 262
		Y1 85
		A2 200
		B2 320
		X2 273
		Y2 95
		B3 320
		X3 287
		Y3 105
		B4 320
		X4 296
		Y4 111
		B5 320
		X5 306
		Y5 130
		A6 200
		B6 320
		X6 320
		Y6 142
	)
)

(instance archer4Start of Archer
	(properties
		fequencyA 5
		fequencyB 15
		clientP 7
		startCel 4
		shootX 313
		shootY 110
		hideX 343
		hideY 110
		arrowV vBrigandArcher
		arrowL 6
		startX 288
		startY 97
		B1 120
		X1 75
		Y1 67
		B2 140
		X2 68
		Y2 86
		B3 200
		X3 36
		Y3 104
		B4 320
		X4 23
		Y4 120
		B5 320
		X5 16
		Y5 123
		B6 320
		X6 7
		Y6 135
	)
)

(instance egoHit of Script
	(properties
		;register ;== arrowP (Prop)
	)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(register show:)
				(cond 
					((ego has: iChainmail) (TakeDamage 15))
					((ego has: iLeather) (TakeDamage 30))
					(else (TakeDamage 45))
				)
				(cond 
					((== [arrows 0] NULL)
						(= [arrows 0] arrowY)
						(= [arrows 1] egoLoop)
						(= [arrows 2] arrowDirection)
						(= [arrows 3] register)
					)
					((== [arrows 4] NULL)
						(= [arrows 4] arrowY)
						(= [arrows 5] egoLoop)
						(= [arrows 6] arrowDirection)
						(= [arrows 7] register)
					)
					((== [arrows 8] NULL)
						(= [arrows 8] arrowY)
						(= [arrows 9] egoLoop)
						(= [arrows 10] arrowDirection)
						(= [arrows 11] register)
					)
					((== [arrows 12] NULL)
						(= [arrows 12] arrowY)
						(= [arrows 13] egoLoop)
						(= [arrows 14] arrowDirection)
						(= [arrows 15] register)
					)
					((== [arrows 16] NULL)
						(= [arrows 16] arrowY)
						(= [arrows 17] egoLoop)
						(= [arrows 18] arrowDirection)
						(= [arrows 19] register)
					)
					((== [arrows 20] NULL)
						(= [arrows 20] arrowY)
						(= [arrows 21] egoLoop)
						(= [arrows 22] arrowDirection)
						(= [arrows 23] register)
					)
					((== [arrows 24] NULL)
						(= [arrows 24] arrowY)
						(= [arrows 25] egoLoop)
						(= [arrows 26] arrowDirection)
						(= [arrows 27] register)
					)
					(else 
						(register dispose:)
					)
				)
				(if (<= [egoStats HEALTH] 0)
					(= cycles 3)
				else
					(self dispose:)
				)
			)
			(1
				(EgoDead DIE_RETRY DIE_ENDGAME_ARROWS 94 29
					#icon (GetEgoViewNumber vEgoDefeatedMagic) 0 9
					#title {You knew this job was dangerous....}
				)
				;This was not the ending you had in mind when you read the manual on "How to be a Hero".
				;Either you just aren't ready to take on this situation, or there's got to be a better way to accomplish your mission.
				;we'll just restore ego's health, and let them try again.
				(= [egoStats HEALTH] (MaxHealth))
				(= curRoomNum 93)
				(curRoom newRoom: 94)
				(self dispose:)
			)
		)
	)
)

(instance openMess of Script
	(properties)
	
	(method (changeState newState)
		(switch (= state newState)
			(1
				(HighPrint 94 30)
				;You hear a snick as the hasp on the Jack in the Box is released.
				(lockSound number: (GetSongNumber 35) init: play:)
				(= cycles 8)
			)
			(2
				(lockSound dispose:)
				(self dispose:)
			)
		)
	)
)

(instance lockSound of Sound
	(properties
		number 35
		priority 3
	)
)

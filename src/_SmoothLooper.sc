;;; Sierra Script 1.0 - (do not remove this comment)
;;; SmoothLooper Class
;;; Author J.Mark Hood 3/27/89
;;; Update 6/26/89 queue's any new setMotion request.
;;; loops ego smoothly through transition cels ( vChangeDir)
;;; on a direction change
;;; Usage : set property vChangeDir to view containing transition loops
;;; in the order listed in enum below  and set clients looper property
;;; i.e. 
;;;   (instance mySmoothLooper of SmoothLooper
;;;      (properties
;;;            vChangeDir     myTransitionLoops
;;;      )
;;;   )
;;;   (Actor looper:mySmoothLooper)

(script# SMOOPER) ;SMOOPER = 968
(include system.sh) (include sci2.sh) (include game.sh)
(use _Motion)
(use _System)

(public
	SmoothLooper 0
)

(enum
   lEastToSouth   
   lWestToSouth   
   lSouthToEast
   lSouthToWest
   lNorthToEast   
   lNorthToWest   
   lEastToNorth
   lWestToNorth
)

(define TwoStep  16)

(class SmoothLooper of Code
	(properties
		;new properties for SmoothLooper
		nextLoop 		0
		client 			0
		oldCycler 		0
		oldMover 		0
		newMover 		0
		oldCycleSpeed 	0
		inProgress 		0
		vNormal 		0
		vChangeDir 		0
	)
	
	(method (doit cl theHeading &tmp theLoop changedTheLoop)
		;CI: NOTE: Added missing if/return from EO's decompilation.
		(if (& (cl signal?) fixedLoop) (return))
		
		(= changedTheLoop FALSE)
		(if inProgress
			(if newMover (newMover dispose:))
			(= newMover (cl mover?))
			(cl mover: 0)
			(return)
		else
			(if (not vNormal) (= vNormal (cl view?)))
			(= client cl)
			(= inProgress TRUE)
		)
		(if
		(and (> (client loop?) facingNorth) (== (client view?) vNormal)) ;abnormal loops
			;;ie hero's quest cat
			(if inProgress
				(if (IsObject oldMover) (oldMover dispose:))
			else
				(client view: vNormal)
				(DirLoop client theHeading)
			)
		)
		(switch (= theLoop (client loop?)) ; clients Loop
			(facingNorth
				(cond 
					((or (<= theHeading 45) (> theHeading 315))) ;HEADINGNORTH
					((<= theHeading 135) ;HEADINGEAST
						(= theLoop lNorthToEast) 
						(= nextLoop facingEast) 
						(= changedTheLoop TRUE)
					) 
					((<= theHeading 225) ;HEADINGSOUTH
						(= theLoop lNorthToEast) 
						(= nextLoop (+ TwoStep lEastToSouth)) 
						(= changedTheLoop TRUE)
					) 
					((<= theHeading 315) ;HEADINGWEST
						(= theLoop lNorthToWest) 
						(= nextLoop facingWest) 
						(= changedTheLoop TRUE)
					) 
				)
			)
			(facingEast
				(cond 
					((or (<= theHeading 45) (> theHeading 315)) ;HEADINGNORTH
						(= theLoop lEastToNorth) 
						(= nextLoop facingNorth) 
						(= changedTheLoop TRUE)
					) 
					((<= theHeading 135)) ;HEADINGEAST
					((<= theHeading 225) ;HEADINGSOUTH
						(= theLoop lEastToSouth) 
						(= nextLoop facingSouth) 
						(= changedTheLoop TRUE)
					) 
					((<= theHeading 315) ;HEADINGWEST
						(= theLoop lEastToNorth) 
						(= nextLoop (+ TwoStep lNorthToWest)) 
						(= changedTheLoop TRUE)
					) 
				)
			)
			(facingWest
				(cond 
					((or (<= theHeading 45) (> theHeading 315)) ;HEADINGNORTH
						(= theLoop lWestToNorth) 
						(= nextLoop facingNorth) 
						(= changedTheLoop TRUE)
					)
					((<= theHeading 135) ;HEADINGEAST
						(= theLoop lWestToSouth) 
						(= nextLoop (+ TwoStep lSouthToEast)) 
						(= changedTheLoop TRUE)
					)
					((<= theHeading 225) ;HEADINGSOUTH
						(= theLoop lWestToSouth) 
						(= nextLoop facingSouth) 
						(= changedTheLoop TRUE)
					) 
					;CI: NOTE: When SCICompanion decompiled this, there was a bad branch after the else, signified by $baad (aka -17747).
					;(else (<= theHeading 315) $baad). Based on leaked source code, there should have been no value present there.
					(else (<= theHeading 315)) ;HEADINGWEST
				)
			)
			(facingSouth
				(cond 
					((or (<= theHeading 45) (> theHeading 315)) ;HEADINGNORTH
						(= theLoop lSouthToWest) 
						(= nextLoop (+ TwoStep lWestToNorth)) 
						(= changedTheLoop TRUE)
					)
					((<= theHeading 135) ;HEADINGEAST
						(= theLoop lSouthToEast) 
						(= nextLoop facingEast) 
						(= changedTheLoop TRUE)
					)
					((<= theHeading 225)) ;HEADINGSOUTH
					((<= theHeading 315) ;HEADINGWEST
						(= theLoop lSouthToWest) 
						(= nextLoop facingWest) 
						(= changedTheLoop TRUE)
					) 
				)
			)
		)
		(if changedTheLoop
			(= oldCycler 		(client cycler?))
			(= oldMover 		(client mover?))
			(= oldCycleSpeed 	(client cycleSpeed?))
			(client
				view: 		vChangeDir
				cycleSpeed: 0
				mover: 		0
				cycler: 	0
				loop: 		theLoop
				cel: 		0
				setCycle: 	EndLoop self
			)
		else
			(= inProgress FALSE)
		)
	)
	
	(method (dispose)
		(if oldMover (oldMover dispose:))
		(if newMover (newMover dispose:))
		(if oldCycler (oldCycler dispose:))
		(client view: vNormal looper: NULL) ;CI: added missing looper: NULL from EO's decompilation.
		(DirLoop client (client heading?))
		(super dispose:)
	)
	
	(method (cue &tmp om oc)
		(if (< nextLoop 15)	; single step transition
			(client
				view: 		vNormal
				loop: 		nextLoop
				mover: 		oldMover	; setMotion would be recursive
				cycler: 	oldCycler
				cycleSpeed: oldCycleSpeed
			)
			(= inProgress (= oldCycler (= oldMover 0)))
			(if newMover
				(client setMotion: newMover)
				(= newMover 0)
			)
		else				; two step transition
			(= nextLoop (- nextLoop 16))
			(client loop: nextLoop cel: 0 setCycle: EndLoop self)
			(= nextLoop
				(switch nextLoop
					(lEastToSouth facingSouth)
					(lNorthToWest facingWest)
					(lSouthToEast facingEast)
					(lWestToNorth facingNorth)
				)
			)
		)
	)
)

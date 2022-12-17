;;; Sierra Script 1.0 - (do not remove this comment)
;;;;
;;;;  ACTOR.SC
;;;;
;;;;  (c) Sierra On-Line, Inc, 1992
;;;;
;;;;  Author:  Jeff Stephenson
;;;;  Updated:
;;;;     Brian K. Hughes
;;;;     August 19, 1992
;;;;
;;;;  The classes in this module are the visible/animated objects on the screen.
;;;;
;;;;  Classes:
;;;;	 Feature (SCI0 only)
;;;;	 PV (SCI0 Only
;;;;     View
;;;;     Prop
;;;;     Actor
;;;;	 Block (SCI0 only)
;;;;	 Cage (SCI0 only)

(script# ACTOR) ;ACTOR = 998
(include system.sh) (include sci2.sh) (include game.sh)
(use Main)
(use _Interface)
(use _Motion)
(use _System)


(class Feature of Object
	(properties
		y 		0
		x 		0
		z 		0
		heading 0
	)
	
	(method (handleEvent event)
		(event claimed?)
	)
)

(class PicView of Feature
	(properties
		name "PV"
		;;properties from Feature
		;y 		0
		;x 		0
		;z 		0
		;heading 0
		;new properties to PicView
		view 	NULL
		loop 	NULL
		cel 	NULL
		priority RELEASE
		signal 	$0000
	)
	
	(method (init)
		(addToPics add: self)
	)
	
	(method (showSelf)
		(Print name #icon view loop cel)
	)
)

(class View of Feature
	;;; The view class contains the minimum functionality to put a visible
	;;; object on the screen in a reversible manner (i.e. so that it can
	;;; be erased).

	(properties
		;y 0
		;x 0
		;z 0
		;heading 0
		yStep 2				;here for BaseSetter use only
		view NULL			;view number
		loop NULL			;loop number
		cel NULL			;cell number
		priority 0			;priority of visible object
		underBits 0
		signal (| stopUpdOn staticView)		;=$0101 ;CI: $0001 = stopUpdOn, CI: $0100 = staticView
		
		nsTop 0				;nowSeen - current rectangle
		nsLeft 0
		nsBottom 0
		nsRight 0
		
		lsTop 0				;lastSeen - previous rectangle
		lsLeft 0
		lsBottom 0
		lsRight 0
		
		brTop 0				;base rectangle
		brLeft 0
		brBottom 0
		brRight 0
	)
	
	(method (init)
		;
		; Prepare View and add to the appropriate list

		; Make sure that we're not going to be deleted on the next
		;  animation cycle
		(= signal (& signal (~ delObj)))
		
		; Set lastSeen to null rectangle if View is not currently in the cast
		(if (not (cast contains: self))
			(= lsRight (= lsBottom (= lsLeft (= lsTop 0))))
			;CI: NOTE $ff77 = (~ (& hideActor actorHidden))
			; however, when I tried to put (~ (| hideActor actorHidden))
			; it wouldn't compile properly.
			(= signal (& signal $ff77)) ;$ff77 = (~ (| hideActor actorHidden))
			;(= signal (& signal (~ (| hideActor actorHidden))))
		)
		(BaseSetter self)
		(cast add: self)
	)
	
	(method (dispose)
		;
		; Signal object's imminent demise to (Animate), so that it will be
		;  removed from the screen.  Signal that object is to be deleted later

		(self startUpd: hide:)
		(= signal (| signal delObj))
	)
	
	(method (showSelf)
		(Print name #icon view loop cel)
	)
	
	(method (posn newX newY newZ)
		;
		; Position the View at newX, newY, newZ.  It will appear there on
		; next animation cycle.

		(if (>= argc 1) 		(= x newX)
			(if (>= argc 2) 	(= y newY)
				(if (>= argc 3) (= z newZ)
				)
			)
		)
		
		; Reset base rectangle
		(BaseSetter self)
		; Force the change to the screen
		(self forceUpd:)
	)

;;; stopUpd:, forceUpd:, and startUpd: rely upon the kernel (Animate)
;;; to resolve the state of updating and hidden status.
	
	(method (stopUpd)
		;
		; Set signal bits to request a stop update
		(= signal (| signal stopUpdOn))
		(= signal (& signal (~ startUpdOn)))
	)
	
	(method (forceUpd)
		;
		; Set signal to request one forced update
		(= signal (| signal forceUpdOn))
	)
	
	(method (startUpd)
		;
		; Set signal bits to request a start update
		(= signal (| signal startUpdOn))
		(= signal (& signal (~ stopUpdOn)))
	)
	
	(method (setPri newPri)
		;
		; Action depends on the presence/value of newPri:
		;     not present    fix object's priority at its current value
		;     RELEASE, -1    let (Animate) determine object's priority
		;                    based on its x,y position
		;     else           fix object's priority at the specified value
		(cond 
			((== argc 0) 
				(= signal (| signal fixPriOn))
			)
			((== newPri RELEASE) 
				(= signal (& signal (~ fixPriOn)))
			)
			(else 
				(= priority newPri)
				(= signal (| signal fixPriOn))
			)
		)
		; Force the change to the screen
		(self forceUpd:)
	)
	
	(method (setLoop theLoop)
		;
		; Action depends on the presence/value of newLoop:
		;     not present    fix object's loop at its current value
		;     RELEASE, -1    let a Motion class determine the object's loop
		;                    based on its current direction
		;     else           fix object's loop at the specified value
      
		(cond 
			((== argc 0)
				(= signal (| signal fixedLoop))
			)
			((== theLoop RELEASE)
				(= signal (& signal (~ fixedLoop)))
			)
			(else 
				(= loop theLoop)
				(= signal (| signal fixedLoop))
			)
		)
		(self forceUpd:)
	)
	
	(method (setCel newCel)
		;
		; Action depends on the presence/value of newCel:
		;     not present    fix object's cel at its current value
		;     RELEASE, -1    let a Cycle class determine the object's cel
		;     else           fix object's cel at the specified value
      
		(cond 
			((== argc 0)
				(= signal (| signal skipCheck))
			)
			((== newCel RELEASE)
				(= signal (& signal (~ skipCheck)))
			)
			(else
				(= signal (| signal skipCheck))
				(= cel
					(if (>= newCel (self lastCel:))
						(self lastCel:)
					else
						newCel
					)
				)
			)
		)
		(self forceUpd:)
	)
	
	(method (ignoreActors arg)
		;
		; No or nonzero argument says that this object should not collide with
		;  other animated objects.  A FALSE argument says that it should collide
      
		(if (or (== 0 argc) arg)
			(= signal (| signal ignrAct))
		else
			(= signal (& signal (~ ignrAct)))
		)
	)
	
	(method (hide)
		;
		; Hide the object (remove from screen without removing from cast)
		(= signal (| signal hideActor))
	)
	
	(method (show)
		;
		; Show a hidden object
		(= signal (& signal (~ hideActor)))
	)
	
	(method (delete)
		;
		; This method is called during each animation cycle to purge the cast of
		;  dispose:d actors

		; Are we slated for deletion?
		(if (& signal delObj)
			(if (& signal viewAdded)
				(addToPics
					add:
						((PicView new:)
							view: view
							loop: loop
							cel: cel
							x: x
							y: y
							z: z
							priority: priority
							signal: signal
							yourself:
						)
				)
			)
			(= signal (& signal (~ delObj)))
			(cast delete: self)
			; Unload the underbits in case Animate didn't
			(if underBits
				(UnLoad RES_MEMORY underBits)
				(= underBits 0)
			)
			(super dispose:)
		)
	)
	
	(method (addToPic)
		;
		; This signals that the object should be added to the bitmap
		
		(if (not (cast contains: self)) (self init:))
		(self signal: (| signal ADDTOPIC)); ADDTOPIC = (| delObj stopUpdOn viewAdded)
	)
	
	(method (lastCel)
		;
		; Return the number of the last cel in the current loop of this object

		(return (- (NumCels self) 1))
	)
	
	(method (isExtra makeAnExtra &tmp ret)
		;
		; If no argument is present, return if this Actor is an Extra or not.
		; Otherwise, declare this Actor as an extra or not, depending on the boolean argument passed
		
		(= ret (& signal isExtra)) ;CI: isExtra is the sci.sh defined value. this define was not used or present in SCI1.1, and so I have no way of identifying the original name used.
		(if argc
			(if makeAnExtra
				(= signal (| signal isExtra))
			else
				(= signal (& signal (~ isExtra)))
			)
		)
		(return ret)
	)
	
	(method (motionCue)
	)
)

(class Prop of View
	;;; The Prop class adds cycling capability to the View class.
	;;; It also introduces the ability to have an attached script
	;;; which determines its behaviour.

	(properties
		;y 0
		;x 0
		;z 0
		;heading 0
		;yStep 2
		;view NULL
		;loop NULL
		;cel NULL
		;priority 0
		;underBits 0
		signal $0000
		;nsTop 0
		;nsLeft 0
		;nsBottom 0
		;nsRight 0
		;lsTop 0
		;lsLeft 0
		;lsBottom 0
		;lsRight 0
		;brTop 0
		;brLeft 0
		;brBottom 0
		;brRight 0
		
		cycleSpeed 0	;0 (fastest to n (slowest)
		script 0		;object ID of script
		cycler 0		;object ID of cycle code
		timer 0			;object ID of an attached timer
	)
	
	(method (doit &tmp aState)
		;
		; This is the method called in every animation cycle

		; If we are scheduled for deletion we simply return
		(if (& signal delObj) (return))
		(if script (script doit:))
		
		; If we are not updating (or about to start) we return
		(if (and (& signal notUpd) (not (& signal startUpdOn)))
			(return)
		)
		
		;if we have cylcer code attached, give its doit a shot
		(if cycler (cycler doit:))
	)
	
	(method (handleEvent event)
		(if script (script handleEvent: event))
		(event claimed?)
	)
	
	(method (delete)
		(if (& signal delObj)
			; Dispose of any attached objects
			(self setScript: 0 setCycle: 0)
			(if timer (timer dispose:))
			(super delete:)
		)
	)
	
	(method (motionCue)
		(if (and cycler (cycler completed?))
			(cycler motionCue:)
		)
	)
	
	(method (setCycle cType)
		;
		; Perform cycler actions, depending on cType:
		;  a) a class     - attach a dynamic instance of that class
		;  b) an instance - attach the instance
		;  c) 0           - remove current cycler
      
		; Dispose of existing cycle code
		(if cycler (cycler dispose:))
		(if cType
			; If a Cycling class was specified, attach an instance of the class to
			;  this object and initialize it, else if an instance was specified,
			;  just use it as is

			(self setCel: RELEASE)
			(self startUpd:)
			(= cycler
				(if (& (cType -info-?) CLASS)
					(cType new:)
				else
					cType
				)
			)
			(cycler init: self &rest)
		else
			; If cType is NULL, clear the cycler property
			(= cycler 0)
		)
	)
	
	(method (setScript newScript)
		;
		; Attach a new script to this object, removing any existing one
		(if (IsObject script) (script dispose:))
		(if newScript (newScript init: self &rest))
	)
	
	(method (cue)
		(if script (script cue:))
	)
)

(class Actor of Prop
	;;; The Actor class introduces the ability to move to animated objects.
	(properties
		name "Act"
		;properties from Feature
		;y 0
		;x 0
		;z 0
		;heading 0
		;Properties from View
		;yStep 2
		;view NULL
		;loop NULL
		;cel NULL
		;priority 0
		;underBits 0
		;signal $0000
		;nsTop 0
		;nsLeft 0
		;nsBottom 0
		;nsRight 0
		;lsTop 0
		;lsLeft 0
		;lsBottom 0
		;lsRight 0
		;brTop 0
		;brLeft 0
		;brBottom 0
		;brRight 0
		;properties from Prop
		;cycleSpeed 0
		;script 0
		;cycler 0
		;timer 0
		;new properties to Actor
		illegalBits cWHITE

		xLast 0			;previous x position
		yLast 0			;previous y position
		xStep 3			;how far actor moves in x direction
		moveSpeed 0		;0 (fastest) to n (slowest)
		blocks 0		;any blocks relating to this actor
		baseSetter 0	;ID of Code which sets this objects base rectangle
		mover 0			;object ID of motion code
		looper 0		;set actor's loop based on direction or whatever
		viewer 0		;set actor's view based on some criterion
		avoider 0		;code for getting around obstructions
	)
	
	(method (init)
		(super init:)
		(= xLast x)
		(= yLast y)
	)
	
	(method (doit &tmp temp0 left right)
		;
		; This is the method called in every animation cycle

		; If we are scheduled for deletion we simply return
		(if (& signal delObj) (return))
		(= signal (& signal (~ blocked)))
		(if script (script doit:))
		
		; If we are not updating (or about to start) we return
		(if (and (& signal notUpd) (not (& signal startUpdOn)))
			(return)
		)
		
		; Determine which view of the actor to display
		(if viewer (viewer doit: self))
		
		; If there's an avoider, let it do its thing, likewise for a mover
		(cond 
			(avoider (avoider doit:))
			(mover (mover doit:))
		)
		
		; Now cycle the object
		(if cycler
			; Get the current edges of the baseRect
			(= left brLeft)
			(= right brRight)
			(cycler doit:)
			
			; Set the new baseRect.
			(if baseSetter
				(baseSetter doit: self)
			else
				(BaseSetter self)
			)
			(if
				(and
					(or (!= left brLeft) (!= right brRight))
					(not (self canBeHere:))
				)
				(self findPosn:)
			)
		)
		
		; Update last position.
		(= xLast x)
		(= yLast y)
	)
	
	(method (posn newX newY)
		;
		; Attempt to place actor at x,y.  It will appear here on next
		; animation cycle

		(super posn: newX newY &rest)  ;we may have passed a z too.
		(= xLast newX)
		(= yLast newY)
		(if (not (self canBeHere:)) (self findPosn:))
	)
	
	(method (setLoop theLoop &tmp newLooper)
		;
		; Perform loop/looper actions, depending on theLoop:
		;  a) no args     - call the super's setLoop
		;  b) class       - set the looper to a dynamic instance of that class
		;  c) instance    - set the looper to that instance
		;  d) value       - set the loop to that value

		(if
			(= newLooper
				(cond 
					((== argc 0) 					(super setLoop:) NULL)
					((not (IsObject theLoop)) 		(super setLoop: theLoop &rest) NULL)
					((& (theLoop -info-?) CLASS) 	(theLoop new:))
					(else 							theLoop)
				)
			)
			
			; Get rid of a looper if we have one and want a new one
			(if looper (looper dispose:))
			((= looper newLooper) init: self &rest)
		)
	)
	
	(method (delete)
		;
		; Actually delete the actor.  The dispose: method just marks the actor
		;  for deletion, but leaves it in the cast so that it can be removed from
		;  the screen.  This does the dirty work of real disposal.
      
		(if (& signal delObj)
			(if (!= mover RELEASE) (self setMotion: NULL))
			(self setAvoider: 0)
			(if baseSetter (baseSetter dispose:) (= baseSetter 0))
			(if looper (looper dispose:) (= looper 0))
			(if viewer (viewer dispose:) (= viewer 0))
			(if blocks (blocks dispose:) (= blocks 0))
			(super delete:)
		)
	)
	
	(method (motionCue)
		(if (and mover (mover completed?)) (mover motionCue:))
		(super motionCue:)
	)
	
	(method (setMotion mType &tmp [temp0 40])
		;
		; Perform mover actions, depending on mType:
		;  a) a class     - attach a dynamic instance of that class
		;  b) an instance - attach the instance
		;  c) 0           - remove current mover
      
		; Get rid of any old mover
		(if (and mover (!= mover RELEASE)) (mover dispose:))
		(if mType
			(self startUpd:)
			(= mover
				(if (& (mType -info-?) CLASS)
					(mType new:)
				else
					mType
				)
			)
			(mover init: self &rest)
		else
			(= mover 0)
		)
	)
	
	(method (setAvoider aType)
		;
		; Perform avoider actions, depending on aType:
		;  a) a class     - attach a dynamic instance of that class
		;  b) an instance - attach the instance
		;  c) 0           - remove current mover
      
		; If there is a current avoider, toss it
		(if avoider (avoider dispose:))

		; If the avoider passed is a class, use an instance of it.  Otherwise,
		;  use the instance passed (or 0 if we're clearing the avoider)
		(= avoider
			(if
				(and
					(IsObject aType)
					(& (aType -info-?) CLASS)
				)
				(aType new:)
			else
				aType
			)
		)
		; Initialize the avoider.
		(if avoider (avoider init: self &rest))
	)
	
	(method (ignoreHorizon arg)
		;
		; If arg is not present or nonzero, say that the actor can ignore the
		;  room's horizon.  If arg is zero, require the actor to observe it
      
		(if (or (not argc) arg)
			(= signal (| signal ignrHrz))
		else
			(= signal (& signal (~ ignrHrz)))
		)
	)
	
	(method (observeControl ctrl &tmp i)
		;
		; Set the controls which the actor cannot be on
      
		(= i 0)
		(while (< i argc)
			(= illegalBits (| illegalBits [ctrl i]))
			(++ i)
		)
	)
	
	(method (ignoreControl ctrl &tmp i)
		;
		; Set the controls which the actor can be on

		(= i 0)
		(while (< i argc)
			(= illegalBits (& illegalBits (~ [ctrl i])))
			(++ i)
		)
	)
	
	(method (observeBlocks)
		;
		; Set the blocks (class Block) which an actor cannot be inside of
      
		; Make sure there is a set for the blocks
		(if (not blocks) (= blocks (Set new:)))
		(blocks add: &rest)
	)
	
	(method (ignoreBlocks)
		;
		; Delete specified blocks from those which an actor must stay out of

		(blocks delete: &rest)
		(if (blocks isEmpty:) (blocks dispose:) (= blocks 0))
	)
	
	(method (isStopped)
		;
		; Return TRUE if actor did not move in the previous animation cycle
      
		(cond 
			((not (IsObject mover)))
			;CI: NOTE: Later versions of Actor apparently included a triedToMove method. 
			;This version of QFG1 did not include that.
			;((== x xLast) (if (== y yLast) (mover triedToMove:)))
			;CI: NOTE: This is the code included in QFG1
			((== x (mover xLast?)) (== y (mover yLast?)))
		)
	)
	
	(method (isBlocked)
		;
		; Return TRUE if actor tried to move, but couldn't
      
		(return (& signal blocked))
	)
	
	(method (findPosn &tmp temp0 temp1 theX theY temp4)
		;
		; TODO: Comments for Actor:findPosn
		
		(= theX x)
		(= theY y)
		(= temp4 0)
		(= temp1 1)
		(while (not temp4)
			(= temp0 0)
			(while (and (not temp4) (< temp0 8))
				(= x (+ theX (* temp1 (sign (CosMult (* temp0 45) 100)))))
				(= y (- theY (* temp1 (sign (SinMult (* temp0 45) 100)))))
				(= temp4 (if (self canBeHere:) (self onControl:) else 0))
				(++ temp0)
			)
			(++ temp1)
		)
		(self posn: x y)
	)
	
	(method (inRect lx uy rx by)
		;
		; Return TRUE if actor's origin is within the rectangle specified

		(return
			(if
			(and (<= lx x) (< x rx) (<= uy y))
				(< y by)
			else
				FALSE
			)
		)
	)
	
	(method (onControl org)
		;
		; If org is present and nonzero, return the control under the actor's
		; origin.  Otherwise, return a bit-mapped word indicating the controls
		; which the actor's baseRect is on.
      
		(if (and argc org)
			(OnControl CMAP x y)
		else
			(OnControl CMAP brLeft brTop brRight brBottom)
		)
	)
	
	(method (distanceTo anObj)
		;
		; Return the distance from one actor to another
      
		(GetDistance x y (anObj x?) (anObj y?) perspective)
	)
	
	(method (canBeHere)
		; Check the validity of a new point for the actor and returns:
		;  a) obj ID or control color if blocked
		;  b) -1 if above horizon
		;  c) -2 if blocked by cage or block
		;		NOTE: (Future versions of this method are called cantBeHere, 
		;			which is where these comments are pulled from. Every effort 
		;			is made to make these comments relevant.)


      
		; Set the base rectangle which we check for collisions
		(if baseSetter
			(baseSetter doit: self)	;user-supplied code
		else
			(BaseSetter self)		;use the default kernel call
		)
		(if
			(and
				(or
					(and ;unknown exactly what this is supposed to check for.
						(== illegalBits 0)
						;CI: NOTE: SCICompanion decompiled this line as: "(& signal --UNKNOWN-PROP-NAME--)" which cannot compile. 
						;Based on reviewing decompiled source from SQ3 v1.018), 
						;I believed --UNKNOWN-PROP-NAME-- actually refers to ignrAct aka $4000
						(& signal ignrAct)
					)
					(CanBeHere self (cast elements?)) ; Returns obj id or control color of reason for blockage
				)
				(or
					; Being above the horizon
					(& signal ignrHrz)
					(not (IsObject curRoom))
					(>= y (curRoom horizon?))
				)
			)
			
			; Being in conflict with a Block or Cage
			(if (== blocks 0) else (blocks allTrue: #doit self))
		)
	)
	
	(method (setStep xs ys)
		;
		; Set an actor's stepsize to xs, ys.  If either xs or ys are -1, leave
		;  that stepsize as is.
      
		(if (and (>= argc 1) (!= xs -1)) (= xStep xs))
		(if (and (>= argc 2) (!= ys -1)) (= yStep ys))

		; If the actor is controlled by a MoveTo, recompute the move
		;  based on the new stepsize
		(if (and mover (!= RELEASE mover) (mover isMemberOf: MoveTo))
			((self mover?) init:)
		)
	)
	
	(method (setDirection dir &tmp vx vy xIncr yIncr ang maxCoord)
		;
		; Set an actor to move off screen in a specified direction
      
		(= vx (curRoom vanishingX?))
		(= vy (curRoom vanishingY?))
		;CI: NOTE: added this back in from QFG1 decompile. 
		;This was removed by EO during his restoration, for unknown reasons.
		;if there is no x or y step, then do nothing with set direction.
		(if (and (== xStep 0) (== yStep 0)) 
			(return)
		)
		;end of re-added.
		(= maxCoord (/ 32000 (Max xStep yStep)))  ;CI: 32000... Max divided by 32000, why?
		(switch dir
			(dirStop 
				(self setMotion: 0)
				(return)
			)
			(dirN
				(= xIncr (- vx x))
				(= yIncr (- vy y))
			)
			(dirS
				(= xIncr (- x vx))
				(= yIncr (- y vy))
			)
			(dirE 
				(= xIncr maxCoord)
				(= yIncr 0)
			)
			(dirW
				(= xIncr (- maxCoord))
				(= yIncr 0)
			)
			(else ;we have a semi-quadrant
				;;as of 4/14/89 GetAngle is too inaccurate to use for
				;;anything but semi-quadrants, where accuracy is NOT
				;;critical. -- Pablo Ghenis
				;;dir is 2, 4, 6 or 8 (aka NE, SW, SW, NW)
				;;for 45, 135, 225, 315 if default (distant) vanishing point 
				(if (< 180 (= ang (GetAngle x y vx vy)))
					(= ang (- ang 360))
				)
				(= ang
					(+ (/ (+ ang 90) 2)  	;1st quadrant bisection 
						(* 45 (- dir 2))	;add 90 degrees per extra quadrant
					)
				)
				(= xIncr (SinMult ang 100))
				(= yIncr (- (CosMult ang 100)))	;y-axis is inverted
			)
		)
		; Scale up with caution to avoid overflow!
		(= maxCoord (/ maxCoord 5))
		(while
		(and (< (Abs yIncr) maxCoord) (< (Abs xIncr) maxCoord))
			(= xIncr (* xIncr 5))
			(= yIncr (* yIncr 5))
		)
		(self setMotion: MoveTo (+ x xIncr) (+ y yIncr))
	)
)

(class Block of Object
	;;; A Block is a rectangle meant to exclude Actors.  'theActor' is required
	;;; to stay out of a 'theBlock' by
	;;;   (theActor observeBlocks: theBlock  ...)
	;;; Similarly,
	;;;   (theActor ignoreBlocks: theBlock ...)
	;;; will allow the Actor to be inside the block once more.

	(properties
		name "Blk"
		top 0
		left 0
		bottom 0
		right 0
	)
	
	(method (doit theObj)
		(return
			(if
				(or
					(<= (theObj brBottom?) top)
					(> (theObj brTop?) bottom)
					(< (theObj brRight?) left)
				)
			else
				(>= (theObj brLeft?) right)
			)
		)
	)
)

(class Cage of Block
	;;; A Cage is a Block which requires that an Actor be inside the rectangle
	;;; rather than outside.  It is set and cleared in the same way as Blocks,
	;;; using observeBlocks: and ignoreBlocks:.

	(properties
		;top 0
		;left 0
		;bottom 0
		;right 0
	)
	
	(method (doit theObj)
		(return
			(if
				(and
					(>= (theObj brTop?) top)
					(>= (theObj brLeft?) left)
					(<= (theObj brBottom?) bottom)
				)
				(<= (theObj brRight?) right)
			else
				0
			)
		)
	)
)

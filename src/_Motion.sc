;;; Sierra Script 1.0 - (do not remove this comment)
;;;;
;;;;  MOTION.SC
;;;;
;;;;  (c) Sierra On-Line, Inc, 1988
;;;;
;;;;  Author:  Jeff Stephenson
;;;;  Updated:
;;;;     Martin Peters, Brian K. Hughes
;;;;     August 12, 1992
;;;;
;;;;  Classes for moving and cycling animated objects.
;;;;  Cycling classes change the cel number of their client in a consistent
;;;;  manner in response to the doit: message.  This makes the object seem
;;;;  to animate.
;;;;  Motion classes change the x,y position of their client in response
;;;;  to the doit: message.  Each class implements a different way of
;;;;  moving an object.
;;;;
;;;;  Classes:
;;;;     Cycle
;;;;     Forward
;;;;     Walk
;;;;     CycleTo
;;;;     EndLoop
;;;;     BegLoop
;;;;     SyncWalk
;;;;     Motion
;;;;     MoveTo

(script# MOTION) ;MOTION = 992
(include system.sh) (include sci2.sh) (include game.sh)
(use Main)
(use _System)

;CYCLING CLASSES ---------------------------------------------------------------

(class Cycle of Object
	(properties
		client 		0	;object whose cycling the class is controlling
		caller 		0	;object to notify when cycling is completed
		cycleDir 	cdFORWARD	;cycle direction (1 == forward, -1 == backward)
		cycleCnt 	0	;"speed" related property
		completed 	FALSE
	)
	
	(method (init theObj)
		(if argc 
			;Make theObj our client.
			(= client theObj)
		)

		;Reset cycle counter.
		(= cycleCnt 0)
		(= completed FALSE)
	)
	
	(method (nextCel)
		;; Return client's next logical cel.

		(++ cycleCnt)
		(return
			(if (<= cycleCnt (client cycleSpeed?))
				;Not yet time to change the client's cel.
				(client cel?)
			else
				;Reset counter.
				(= cycleCnt 0)
				(if (& (client signal?) skipCheck)
					(client cel?)	;returns the current cel
				else
					;Change the cel number in the appropriate fashion.
					(+ (client cel?) cycleDir) ;returns the next cell in the direction we're heading (i.e. forward is +1, backwards is -1)
				)
			)
		)
	)
	
	(method (cycleDone)
	)
	
	(method (motionCue)
		;Detach from client.
		(client cycler: 0)
		(if (and completed (IsObject caller)) (caller cue:))
		(self dispose:)
	)
)

(class Forward of Cycle
	;;; Cycles client's cel constantly forward, wrapping to 0 when it exceeds
	;;; the number of cels in the client's loop.
   
	(properties
		name 		"Fwd"
		;client 		0
		;caller 		0
		;cycleDir 	cdFORWARD
		;cycleCnt 	0
		;completed 	FALSE
	)
	
	(method (doit &tmp newCel)
		(if
		(> (= newCel (self nextCel:)) (client lastCel:))
			(self cycleDone:)
		else
			(client cel: newCel)
		)
	)
	
	(method (cycleDone)
		;; When 'done', reset to first cel and keep going.
		(client cel: 0)
	)
)

(class Walk of Forward
	;;; Do a forward cycle of an object only if it has moved.  Otherwise,
	;;; remain motionless.

	(properties
		;client 		0
		;caller 		0
		;cycleDir 	cdFORWARD
		;cycleCnt 	0
		;completed 	FALSE
	)
	
	(method (doit &tmp newCel)
		;; Goes to next cel if client has moved.
		
		(if (not (client isStopped:)) (super doit:))
	)
)

(class CycleTo of Cycle
	;;; Cycle from the current cel of the client to a specified cel,
	;;; cue:ing the caller when the specified cel is reached.

	(properties
		name		"CT"
		;client 		0
		;caller 		0
		;cycleDir 	cdFORWARD
		;cycleCnt 	0
		;completed 	FALSE
		endCel 		0 	;cel to cycle to
	)
	
	(method (init actor toCel dir whoCares &tmp last)
		;; Set up endCel and caller.
      
		(super init: actor)
		
		(= cycleDir dir)
		(if (== argc 4) (= caller whoCares))
		
		;Set endCel to the value passed, or to the last cel of the
		;current loop if the specified cel is too big.
		(= endCel
			(if (> toCel (= last (client lastCel:)))
				last
			else
				toCel
			)
		)
	)
	
	(method (doit &tmp newCel last)

		;Check to see if the current loop of the animated object has fewer
		;cels than our ending cel.  If so, set the last cel of the current
		;loop as the ending cel.
		(if (> endCel (= last (client lastCel:)))
			(= endCel last)
		)
		
		;Move to next cel.
		(= newCel (self nextCel:))
		(client
			cel:
				(cond 
					((> newCel last) 0)
					((< newCel 0) last)
					(else newCel)
				)
		)

		;If at final cel, signal that we're done.
		(if
			(and 
				(== cycleCnt 0) 
				(== endCel (client cel?))
			)
			(self cycleDone:)
		)
	)
	
	(method (cycleDone)
		(= completed TRUE)

		;If we have a caller which needs cue:ing, set the flag for a delayed cue:.
		;Otherwise, just cue: ourselves to complete the motion.
		(if caller (= doMotionCue TRUE) else (self motionCue:))
	)
)

(class EndLoop of CycleTo
	;;; Cycles forward until the last cel of the loop is reached.
	;;; Leaves cel at last cel in loop.

	(properties
		name		"End"
		;client 		0
		;caller 		0
		;cycleDir 	cdFORWARD
		;cycleCnt 	0
		;completed 	FALSE
		;endCel 		0
	)
	
	(method (init actor whoCares)
		(super
			init: actor (actor lastCel:) cdFORWARD (if (== argc 2) whoCares else NULL)
		)
	)
)

(class BegLoop of CycleTo
	;;; Cycles backward until it reaches cel 0.
	;;; Leaves cel at 0.

	(properties
		name		"Beg"
		;client 		0
		;caller 		0
		;cycleDir 	cdFORWARD
		;cycleCnt 	0
		;completed 	FALSE
		;endCel 		0
	)
	
	(method (init actor whoCares)
		(super init: actor 0 cdBACKWARD (if (== argc 2) whoCares else NULL))
	)
)

;;;; MOTION CLASSES ------------------------------------------------------------

;;;; All Actor motion attempts to get from point A to point B in a straight
;;;; line.  This is done using a modified Bresenham algorithm which deals
;;;; with non-unit steps.  See "bresen.doc" for a derivation of the algorithm.
;;;; The bulk of Motion code (particularly the Bresenham algorithm) has been
;;;; moved into the kernel for both speed and heap space savings.
;;;; (See motion.c).
;;;;
;;;; CI: NOTE: This comment refers to the motion algorithms in SCI 1.1+, not in SCI0
;;;;		which is what this game uses.

(class Motion of Object
	;;; Move an Actor from its current position to a destination position.
	
	(properties
		client 		0	;actor we are controlling
		caller 		0	;cue this object when complete (or blocked)
		x 			0	;destination of the move
		y 			0	
		dx 			0	;basic step size
		dy 			0	
		b-moveCnt 	0	;iterations of doit to skip
		b-i1 		0	;increment values
		b-i2 		0	
		b-di 		0	;decision variable
		b-xAxis 	0	;is motion along x-axis or y-axis?
		b-incr 		0	;the Bresenham adjustment to an integral slope line
		completed 	FALSE
		xLast		0
		yLast		0
	)
	
	(method (init actor xTo yTo toCall &tmp DX DY cx cy)
		(if (>= argc 1) 			(= client actor)
			(if (>= argc 2) 		(= x xTo)
				(if (>= argc 3)		(= y yTo)
					(if (>= argc 4) (= caller toCall)
					)
				)
			)
		)
		
		(= completed FALSE)
		(= xLast 0)
		(= yLast 0)
		;; get it to move for sure first ani-cycle
		(= b-moveCnt 0)
		(if (= cy (client cycler?))	; Keep cycler in synch with mover
			(cy cycleCnt: 0)
		)
		(if (GetDistance (= cx (client x?)) (= cy (client y?)) x y)
			(client heading: (GetAngle cx cy x y))
		)
		(if (client looper?)
			((client looper?) doit: client (client heading?))
		else
			(DirLoop client (client heading?))
		)
		
		;Set up for the Bresenham algorithm.
		(InitBresen self)
	)
	
	(method (doit &tmp aState xd yd si1 si2 sdi)
		(if (== b-moveCnt (client moveSpeed?))
			(= xLast (client x?))
			(= yLast (client y?))
		)
		
		(DoBresen self)
		(if (and (== x (client x?)) (== y (client y?)))
			(self moveDone:)
			(return)
		)
	)
	
	(method (moveDone)
		;invoked when Actor reaches its destination
		
		(= completed TRUE)
		
		;If we have a caller which needs cue:ing, set the flag for a delayed cue:.
		;Otherwise, just cue: ourselves to complete the motion.
		(if caller (= doMotionCue TRUE) else (self motionCue:))
	)
	
;CI: NOTE: This isn't actually present in the QFG1 code.
;;;	(method (triedToMove)
;;;		(return (== b-moveCnt 0))
;;;	)
	
	(method (setTarget xTo yTo)
		;resets x and y
		
		(if argc (= x xTo) (= y yTo))
	)
	
	(method (onTarget)
		;TRUE/FALSE
		
		(return (if (== (client x?) x) (== (client y?) y) else FALSE))
	)
	
	(method (motionCue)
		;Detach from client.
		(client mover: 0)
		
		(if (and completed (IsObject caller)) (caller cue:))
		(self dispose:)
	)
)

(class MoveTo of Motion
	;;; Move client to a particular point and signal completion to caller.
	
	(properties)
	
	(method (init)
		(super init: &rest)
	)
	
	(method (onTarget)
		(return
			(and
				(<= (Abs (- (client x?) x)) (client xStep?))
				(<= (Abs (- (client y?) y)) (client yStep?))
			)
		)
	)
)

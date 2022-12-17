;;; Sierra Script 1.0 - (do not remove this comment)
(script# WANDER) ;WANDER = 970
(include system.sh) (include sci2.sh) (include game.sh)
(use _Motion)


(class Wander of Motion
	;;; Wander about the screen.  This motion never terminates.
	
	(properties
		;properties from Motion
		;client 		0
		;caller 		0
		;x 			0
		;y 			0
		;dx 			0
		;dy 			0
		;b-moveCnt 	0
		;b-i1 		0
		;b-i2 		0
		;b-di 		0
		;b-xAxis 	0
		;b-incr 		0
		;completed 	FALSE
		;new properties for Wander
		distance 	30	;the max distance to move on any one leg of the wander
	)
	
	(method (init theObj dist)
		(if (>= argc 1) 	(= client theObj)
			(if (>= argc 2) (= distance dist)
			)
		)
		(self setTarget:)
		(super init: client)
;		(super doit:)
	)
	
	(method (doit)
		;Take the next step.
		(super doit:)
		
		;;If the motion is complete or the client is blocked,
		;;we're finished with this leg.
		(if (client isStopped:) (self moveDone:))
	)
	
	(method (moveDone)
		;;; When done with the current leg of wandering, re-init: the motion
		;;; to continue wandering.
		(self init:)
	)
	
	(method (setTarget &tmp diam)
		;Pick a random position to move to, constrained by 'distance'.

		(= diam (* distance 2))
		(= x (+ (client x?) (- distance (Random 0 diam))))
		(= y (+ (client y?) (- distance (Random 0 diam))))
	)
	
	(method (onTarget)
		(return FALSE) ;we're never done wandering
	)
)

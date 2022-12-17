;;; Sierra Script 1.0 - (do not remove this comment)
(script# FOLLOW) ;FOLLOW = 971
(include system.sh) (include sci2.sh) (include game.sh)
(use _Motion)


(class Follow of Motion
	;;; This class moves its client in such a way as to try and stay within
	;;; a certain distance to another object.

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
		;completed 	0
		;new properties for Follow
		who 		0	;who to follow
		distance 	20	;try to stay at least this close to 'who'
	)
	
	(method (init theObj whom dist)
		(if (>= argc 1) 		(= client theObj)
			(if (>= argc 2) 	(= who whom)
				(if (>= argc 3) (= distance dist))
			)
		)
		
		;If the client is too far from the object being followed, start
		;moving toward it.
		(if (> (client distanceTo: who) distance)
			(super init: client (who x?) (who y?))
;			(super doit:)
		)
	)
	
	(method (doit &tmp angle)
		(if (> (client distanceTo: who) distance)
			;If too far from the object being followed, move toward it.
			(super doit:)
			(if (== b-moveCnt 0)
				(super init: client (who x?) (who y?))
			)
		else
			(= xLast (client x?))
			(= yLast (client y?))
			;The client is just standing around near its destination.  Pick
			;the loop which faces in the destination's direction.
			(= angle (GetAngle xLast yLast (who x?) (who y?)))
			(if (client looper?)
				((client looper?) doit: client angle)
			else
				(DirLoop client angle)
			)
		)
	)
	
	(method (moveDone)
		;;; When done with the current leg of wandering, re-init: the motion
		;;; to continue wandering.
		
;		(self init: client distance)
	)
	
	(method (setTarget)
		(cond 
			(argc (super setTarget: &rest))
			((not (self onTarget:)) (super setTarget: (who x?) (who y?)))
		)
	)
	
	(method (onTarget)
		(return (<= (client distanceTo: who) distance))
	)
)

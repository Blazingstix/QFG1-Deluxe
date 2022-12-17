;;; Sierra Script 1.0 - (do not remove this comment)
;;;;
;;;;  PATH.SC
;;;;
;;;;  (c) Sierra On-Line, Inc, 1992
;;;;
;;;;  Author:  Jeff Stephenson
;;;;  Updated: Brian K. Hughes
;;;;
;;;;  Motion classes for a path -- i.e. moving to a series of pre-defined
;;;;  points.
;;;;
;;;;  Classes:
;;;;     Path
;;;;     RelPath

(script# PATH) ;PATH=983
(include system.sh) (include sci2.sh) (include game.sh)
(use _Interface)
(use _Motion)


(class Path of MoveTo
	(properties
		;properties from Motion
		;client 			0
		;caller 			0
		;x 				0
		;y 				0
		;dx 				0
		;dy 				0
		;b-moveCnt 		0
		;b-i1 			0
		;b-i2 			0
		;b-di 			0
		;b-xAxis 		0
		;b-incr 			0
		;completed 		FALSE
		
		;new properties for Path
		intermediate 	0	;object to cue at intermediate endpoints
		value 			0	;index into path array
	)
	
	(method (init actor toCall inter)
		(= client actor)
		(= caller (if (>= argc 2) toCall else NULL))
		(= intermediate (if (== argc 3) inter else NULL))
		(= value -1)
		(= x (client x?))
		(= y (client y?))
		(if (self atEnd:)
			(self moveDone:)
		else
			(self next:)
			(super init: client x y)
		)
	)
	
	(method (moveDone)
		(if (self atEnd:)
			(super moveDone:)
		else
			(if intermediate (intermediate cue: (/ value 2)))
			(self next:)
			(super init: client x y)
		)
	)
	
	(method (at)
		;return element of control array (NOTE: this function is not completed)
		
		(Printf PATH 0 name)
		;%s needs an 'at:' method.
		(return FALSE)
	)
	
	(method (next)
		;move to next point in path
		
		(= x (self at: (++ value)))
		(= y (self at: (++ value)))
	)
	
	(method (atEnd)
		;are we at the end of the path?
		
		(return
			(if (== (self at: (+ value 1)) PATHEND)
			else
				(== (self at: (+ value 2)) PATHEND)
			)
		)
	)
)

(class RelPath of Path
	(properties
		;properties from Motion
		;client 			0
		;caller 			0
		;x 				0
		;y 				0
		;dx 				0
		;dy 				0
		;b-moveCnt 		0
		;b-i1 			0
		;b-i2 			0
		;b-di 			0
		;b-xAxis 		0
		;b-incr 			0
		;completed 		FALSE
		;properties from Path
		;intermediate 	0
		;value 			0
	)
	
	(method (next)
		(= x (+ x (self at: (++ value))))
		(= y (+ y (self at: (++ value))))
	)
)

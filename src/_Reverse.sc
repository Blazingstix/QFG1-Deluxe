;;; Sierra Script 1.0 - (do not remove this comment)
(script# REVERSE) ;REVERSE = 969
(include system.sh) (include sci2.sh) (include game.sh)
(use _Motion)


(class Reverse of Cycle
	;;; Cycles client's cel constantly in reverse, wrapping to the last cel
	;;; in the client's loop when the cel goes below 0.

	(properties
		name "Rev"
		;properties from Cycle
		;client 		0
		;caller 		0
		cycleDir 	cdBACKWARD  ;default cycle direction updated to Backwards from Forwards
		;cycleCnt 	0
		;completed 	FALSE
	)
	
	(method (doit &tmp newCel)
		(if (< (= newCel (self nextCel:)) 0)
			(self cycleDone:)
		else
			(client cel: newCel)
		)
	)
	
	(method (cycleDone)
		;; When 'done', reset to last cel and keep going.
		(client cel: (client lastCel:))
	)
)

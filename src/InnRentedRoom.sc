;;; Sierra Script 1.0 - (do not remove this comment)
(script# 302)
(include system.sh) (include sci2.sh) (include game.sh)
(use Main)
(use Sleep)
(use _Game)
(use _System)

(public
	rm302 0
)

(local
	local0
)
(procedure (localproc_000c &tmp temp0)
	(while (not ((= temp0 (Event new: 5)) type?))
		(temp0 dispose:)
	)
	(temp0 dispose:)
)

(instance rm302 of Room
	(properties
		picture 400
		style PIXELDISSOLVE
	)
	
	(method (init)
		(HandsOff)
		(super init:)
		(StatusLine enable:)
		(ego view: vInn loop: 5 cel: 6 posn: 160 75 init:)
		(self setScript: rm302Script)
	)
	
	(method (dispose)
		(super dispose:)
	)
)

(instance rm302Script of Script
	(properties)
	
	(method (changeState newState &tmp str temp1)
		(switch (= state newState)
			(0 (= cycles 2))
			(1
				(EgoSleeps 6 0)
				(= str
					{Asleep at the Hero's Tale Inn.\0AThe sleep heals and refreshes you.}
				)
				(= temp1 (if (< colourCount 16) 15 else 13))
				(Display str 105 300 106 200 100 80 133 102 temp1)
				(localproc_000c)
				(= cycles 2)
			)
			(2 (curRoom newRoom: 301))
		)
	)
)

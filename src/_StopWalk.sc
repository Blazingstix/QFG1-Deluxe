;;; Sierra Script 1.0 - (do not remove this comment)
;;;;
;;;;  STOPWALK.SC
;;;;
;;;;
;;;;  (c) Sierra On-Line, Inc, 1992
;;;;
;;;;  Author:  Corey Cole
;;;;  Updated:
;;;;     Brian K. Hughes
;;;;     August 7, 1992
;;;;
;;;;  Changes an actor's view, loop, and/or cel when he stops moving
;;;;
;;;;  Usage example:
;;;;     (actor setCycle: StopWalk stoppedView)
;;;;     (actor setCycle: StopWalk SAMEVIEW)
;;;;
;;;;  The walking view will be the actor's current view at the time
;;;;  the StopWalk was inited.  SAMEVIEW indicates that the stopped
;;;;  cels are in the last loop of the walking view.
;;;;
;;;;  Classes:
;;;;     StopWalk

(script# STOPWALK) ;STOPWALK = 961
(include system.sh) (include sci2.sh) (include game.sh)
(use _Motion)

(public
	StopWalk 0
)

(class StopWalk of Forward
	(properties
		;propertoes from Cycle
		;client 		0
		;caller 		0
		;cycleDir 	cdFORWARD
		;cycleCnt 	0
		;completed 	FALSE
		;new properties for StopWalk
		vWalking 	NULL 	; Normal view for actor (walking view).
		vStopped 	NULL	; View to use when stopped.
	)
	
	(method (init who stopView)
		(if argc 			(= vWalking ((= client who) view?))
			(if (>= argc 2) (= vStopped stopView))
		)
		(super init: client)
	)
	
	(method (doit &tmp curLoop theMover)
		(if (client isStopped:)
			
			; Dual view - we're stopped but haven't changed yet
			(if (== (client view?) vWalking)
				(client view: vStopped)
				(if
					(and
						(= theMover (client mover?))
						(not (theMover completed?))
					)
					(client setMotion: 0)
				)
				(super doit:)
			)
		else
			; Dual view - we're stopped, but we want to continue cycling
			(if (== (client view?) vStopped)
				(client view: vWalking)
			)
			(super doit:)
		)
	)
	
	(method (dispose)
		(if (== (client view?) vStopped)
			(client view: vWalking) ; Leave on normal view
		)
		(super dispose:)
	)
)

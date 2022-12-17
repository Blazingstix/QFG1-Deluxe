;;; Sierra Script 1.0 - (do not remove this comment)
(script# EXTRA) ;EXTRA=988
(include system.sh) (include sci2.sh) (include game.sh)
(use _Motion)
(use _Actor)


(class Extra of Prop
	(properties
		;defined by Feature
		;y 			0
		;x 			0
		;z 			0
		;heading 	0
		
		;defined by View
		;yStep 		2
		;view 		NULL
		;loop 		NULL
		;cel 		NULL
		;priority 	0
		;underBits 	0
		;signal 		$0000
		;nsTop 		0
		;nsLeft 		0
		;nsBottom 	0
		;nsRight 	0
		;lsTop 		0
		;lsLeft 		0
		;lsBottom 	0
		;lsRight 	0
		;brTop 		0
		;brLeft 		0
		;brBottom 	0
		;brRight 	0

		;defined by Prop
		cycleSpeed 	1		;0 (fastest to n (slowest)
		;script 		NULL	;object ID of script
		;cycler 		NULL	;object ID of cycle code
		;timer 		NULL	;object ID of an attached timer
		
		;new properties of Extra
		cycleType 	ExtraForward	;valid values are 0 (ExtraForward), 1 (ExtraEndLoop), 2 (ExtraEndAndBeginLoop)
		hesitation 	0
		pauseCel 	0 	;special values -1 means pause on a random cel, and -2 means pause on the last cel
		minPause 	10
		maxPause 	30
		minCycles 	8
		maxCycles 	20
		counter 	0
		state 		$ffff	;state = 0, 1, 2, 3, or 4
		cycles 		0
	)
	
	(procedure (getPausedCel)
		(switch pauseCel
			(ExtraRandomCel (Random 0 (self lastCel:)))
			(ExtraLastCel (self lastCel:))
			((== cycleType ExtraForward) pauseCel)
		)
	)
	
	
	(method (init)
		(= cel (getPausedCel))
		(self changeState: 0)
		(super init:)
	)
	
	(method (doit)
		(if
			(and
				(== cycleType ExtraEndLoop)
				(== cel pauseCel)
				(!= pauseCel 0)
			)
			(== cycles (+ hesitation 1))
		)
		(if (and cycles (not (-- cycles))) (self cue:))
		(super doit:)
	)
	
	(method (cue)
		(if (& signal (| startUpdOn updOn))
		else
			(self changeState: (+ state 1))
		)
	)
	
	(method (stopExtra)
		(self setCel: (getPausedCel) stopUpd:)
	)
	
	(method (startExtra)
		(self changeState: 1)
	)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(if (== counter 0)
					(= cycles (Random minPause maxPause))
					(if (!= cycleType ExtraForward)
						(= counter (- (Random minCycles maxCycles) 1))
					)
				else
					(-- counter)
					(self cue:)
				)
			)
			(1
				(if (== cycleType ExtraForward)
					(self setCycle: Forward)
					(= cycles (Random minCycles maxCycles))
				else
					(self setCycle: EndLoop self)
				)
			)
			(2
				(if (== cycleType ExtraEndAndBeginLoop)
					(= cycles hesitation)
				else
					(self cue:)
				)
			)
			(3
				(if (== cycleType ExtraEndAndBeginLoop)
					(self setCycle: BegLoop self)
				else
					(self cue:)
				)
			)
			(4
				(self setCel: (getPausedCel))
				(self changeState: 0)
			)
		)
	)
)

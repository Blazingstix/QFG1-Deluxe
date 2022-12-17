;;; Sierra Script 1.0 - (do not remove this comment)
(script# DOORS) ;DOORS = 800
(include system.sh) (include sci2.sh) (include game.sh)
(use Main)
(use _Motion)
(use _Actor)
(use _Interface)

(class Door of Prop
	;;; The Door class is an extension of the Prop class.
	;;; adding events related to opening/closing doors, etc.
	(properties
		;properties from View
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

		;new properties for Door
		entranceTo 	0
		locked 		FALSE
		openSnd 	NULL
		closeSnd 	NULL
		doorControl NULL
		doorState 	doorClosed ;   doorClosed=0, doorOpening=1, doorOpen=2, doorClosing=3
		facingLoop 	loopE		;loopE = 0, loopW = 1, loopS = 2, loopN = 3
		code 		NULL
		illegalBits $0000
	)
	
	(method (init)
		;if we've just left the room this door leads to, then it must be open (becaure we're walkign through it now)
		(= doorState (if (== prevRoomNum entranceTo) doorOpen else doorClosed))
		(= cel (if (== doorState doorClosed) 0 else (- (NumCels self) 1))) ;if door's open, use cel 0, otherwise use the last cel.
		(super init:)
		(self stopUpd: ignoreActors:)
	)
	
	(method (doit)
		(super doit:)
		(if
			(and
				(& (ego onControl: origin) doorControl)
				(or (== facingLoop -1) (== (ego loop?) facingLoop))
			)
			(self open:)
		else
			(self close:)
		)
	)
	
	(method (cue)
		(self stopUpd:)
		(= doorState (if (== doorState doorOpening) doorClosed else doorOpen))
		(if (and (== doorState doorOpen) entranceTo)
			(curRoom newRoom: entranceTo)
		)
	)
	
	(method (open)
		(if
		(and (not locked) (!= doorState doorClosing) (!= doorState doorOpen))
			(= doorState doorClosing)
			(self setCycle: EndLoop self)
			(if openSnd (openSnd doit:))
		)
	)
	
	(method (close)
		(if (and (!= doorState doorOpening) (!= doorState doorClosed))
			(= doorState doorOpening)
			(self setCycle: BegLoop self)
			(if closeSnd (closeSnd doit:))
		)
	)
)

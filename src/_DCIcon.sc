;;; Sierra Script 1.0 - (do not remove this comment)
(script# DCICON) ;DCICON = 967
(include system.sh) (include sci2.sh) (include game.sh)
(use _Interface)
(use _Motion)


(class DCIcon of DIcon
	;;; Cycling Icons are a sub-class of DIcon.
	;;; An instance of DCIcon may be passed to Print for use in
	;;; a dialog.

	(properties
		;properties from DItem
		;type 		dIcon ;defined in DIcon
		;state 		$0000
		;nsTop 		0
		;nsLeft 		0
		;nsBottom 	0
		;nsRight 	0
		;key 		0
		;said 		0
		;value 		0
		;properties from DIcon
		;view 		NULL
		;loop 		NULL
		;cel 		NULL
		;new properties form DCIcon
		;;; additional properties are required to 
		;;;allow cycling via the Cycler classes.
		cycler 		0		; a cycler must be attached dynamically
		cycleSpeed 	6		; 60ths second between cels.
		signal 		$0000	; just to satisfy cycler
	)
	
	(method (init)
		;;; Do not pass a caller to this cycler
		
		((= cycler (Forward new:)) init: self)
	)
	
	(method (dispose)
		;;; A completion type cycler may have already disposed of itself
		
		(if cycler (cycler dispose:))
		(super dispose:)
	)
	
	(method (cycle &tmp oldCel)
		;;; invoked at 60 times per second by the Dialog doit: loop
		
		(if cycler
			;; remember current cel
			(= oldCel cel)
			(cycler doit:)
			
			;;; show new cel if it changed
			(if (!= cel oldCel) (self draw:))
		)
	)
	
	(method (lastCel)
		;; Return the number of the last cel in the current loop of this object.
		;; this method is called by cycler     

		(return (- (NumCels self) 1))
	)
)

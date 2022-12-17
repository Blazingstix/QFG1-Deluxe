;;; Sierra Script 1.0 - (do not remove this comment)
(script# CAT) ;CAT = 976
(include system.sh) (include sci2.sh) (include game.sh)
(use _Interface)
(use _User)
(use _Actor)
(use _System)

(enum -2
   YDOWN    ;-2
   XDOWN    ;-1
   NODIAG   ;zero
   XUP      ;1
   YUP      ;2
)

(enum 
   SELFONLY ;FALSE=only animate yourself
   DODOITS  ;TRUE=execute cast's doits 
)

(class Cat of Actor
	;; This class will track the mouse until the next mouseUp.
	;; A cat can be confined to a rectangular cage by setting the
	;; rectangle properties, and even to movement along one of its
	;; diagonals by setting the diagonal property to one of four
	;; non-null values

	(properties
		;properties from Feature
		;y 		0
		;x 		0
		;z 		0
		;heading 0
		;properties from View
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
		;properties from Prop
		;cycleSpeed 	0
		;script 		0
		;cycler 		0
		;timer 		0
		;properties from Actor
		;illegalBits cWHITE
		;xLast 		0
		;yLast 		0
		;xStep 		3
		;moveSpeed 	0
		;blocks 		0
		;baseSetter 	0
		;mover 		0
		;looper 		0
		;viewer 		0
		;avoider 	0
		;new properties for Cat
		top 		-1	;coordinates of cage edges, -1 means no cage
		left 		-1
		bottom 		-1
		right 		-1
		diagonal 	NODIAG ;see enum at top (XDOWN, YDOWN, NODIAG, XUP, YUP)
		doCast 		SELFONLY	;1=execute cast's doits
								;0=only animate yourself
		outOfTouch 	TRUE	;keep tracking even if cursor is out of touch
		caller 		NULL
		active 		FALSE
		dx 			0
		dy 			0
	)
	
	(method (doit &tmp event)
		(cond 
			((not doCast) (= active FALSE))
			(active
				(= event (User curEvent?))
				(self
					posn: (+ (event x?) dx) (+ (event y?) dy) z
				)
			)
		)
		(super doit:)
	)
	
	(method (handleEvent event)
		(cond 
			((super handleEvent: event))
			(
			(and active (== (event type?) mouseUp)) ; Mouse released -- finish 
				(= active FALSE)
				(event claimed: TRUE)
				(LocalToGlobal event)	;compensate for user
				(self posn: (+ (event x?) dx) (+ (event y?) dy) z)
				(GlobalToLocal event)	;Return event to user's preferred form
				(if caller (caller cue: self))
			)
			((MousedOn self event) (event claimed: TRUE) (self track: event))
		)
	)
	
	(method (posn theX theY theZ &tmp s)
		(if argc 				(= x theX)
			(if (>= argc 2) 	(= y theY)
				(if (>= argc 3) (= z theZ))
			)
		)
		(= s (sign diagonal))
		
		(if (not
				(if
				(and (== -1 top) (== top bottom) (== bottom left)) ;have confining rectangle
					(== left right)
				)
			)
			(switch (Abs diagonal)
				(XUP		;x controls y
					(= y
						(+
							(if (> s 0) top else bottom)
							(/
								(* (- right x) (- bottom top) s)
								(- right left)
							)
						)
					)
				)
				(YUP		;y controls x
					(= x
						(+
							(if (> s 0) left else right)
							(/
								(* (- bottom y) (- right left) s)
								(- bottom top)
							)
						)
					)
				)
			)
			(= x (Max left (Min right x)))
			(= y (Max top (Min bottom y)))
		)
		(super posn: x y z)

		;; code for demons to affect other objects or variables
		;; goes after instance's (super posn &rest) based on final x,y,z
	)
	
	(method (findPosn)
		;;bogus, to avoid infinite recursion with findPosn --Pablo

		(return TRUE)
	)
	
	(method (canBeHere)
		;;bogus, to avoid infinite recursion with findPosn --Pablo

		(return TRUE)
	)
	
	(method (track event &tmp castOfOne)
		(LocalToGlobal event)	;compensate for user
		(= dx (- x (event x?)))
		(= dy (- y (event y?)))
		(if doCast
			(= active TRUE)	;doit method will do mouse tracking
			;; Note -- We leave the event in it's global form so that next
			;;    animation cycle's doit will work correctly.  This should
			;;    be o.k., as we've claimed the event.

		else
			((= castOfOne (Collection new:)) add: self)
			(while
				(and
					(!= mouseUp (event type?))
					(or
						outOfTouch
						(MousedOn self (event type: mouseDown yourself:))
					)
				)
				(self posn: (+ (event x?) dx) (+ (event y?) dy) z)
				(Animate (castOfOne elements?) TRUE)
				
				;;prepare for next loop:
				(GetEvent allEvents event)
			)
			(castOfOne release: dispose:)
			(if caller (caller cue: self))
			(GlobalToLocal event)	;Return event to "normal"
		)
	)
)

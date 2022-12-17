;;; Sierra Script 1.0 - (do not remove this comment)
;;;;
;;;;  GCONTROL.SC
;;;;
;;;;  (c) Sierra On-Line, Inc, 1992
;;;;
;;;;  Author:  J. Mark Hood
;;;;  Updated:
;;;;     Brian K. Hughes
;;;;     July 24, 1992
;;;;
;;;;  Classes:
;;;;     GameControls (SCI1.1 only)
;;;;     ControlIcon  (SCI1.1 only)
;;;;	 RFeature (SCI0 only)
;;;;	 RPicView (SCI0 only)

(script# GCONTROL) ;GCONTROL = 978
(include system.sh) (include sci2.sh) (include game.sh)
(use _Actor)


(class RFeature of Feature
	;Rectangular Feature
	(properties
		;properties from Feature
		;y 			0
		;x 			0
		;z 			0
		;heading 	0
		;new properties to RFeature
		nsTop 		0
		nsLeft 		0
		nsBottom 	0
		nsRight 	0
	)
)

(class RPicView of PicView
	;Rectangular PicView
	(properties
		;properties from Feature
		;y 			0
		;x 			0
		;z 			0
		;heading 	0
		;properties from PicView
		;view 		NULL
		;loop 		NULL
		;cel 		NULL
		;priority 	RELEASE
		;signal 		$0000
		;new properties from RPicView
		nsTop 		0
		nsLeft 		0
		nsBottom 	0
		nsRight 	0
	)
	
	(method (init &tmp w)
		(= w (/ (CelWide view loop cel) 2))
		(= nsBottom (- y z))
		(= nsLeft (- x w))
		(= nsRight (+ x w))
		(= nsTop (- y (+ z (CelHigh view loop cel))))
		(super init:)
	)
)

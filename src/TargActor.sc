;;; Sierra Script 1.0 - (do not remove this comment)
(script# TARGETACTOR) ;TARGETACTOR = 812
(include system.sh) (include sci2.sh) (include game.sh)
(use _Actor)


(class TargActor of Actor
	(properties
		targDeltaX 0
		targDeltaY 0
	)
	
	(method (getHurt)
	)
)

(class TargFeature of Feature
	(properties
		targDeltaX 0
		targDeltaY 0
	)
	
	(method (getHurt)
	)
)

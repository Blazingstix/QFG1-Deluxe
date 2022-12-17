;;; Sierra Script 1.0 - (do not remove this comment)
(script# 299)
(include system.sh) (include sci2.sh) (include game.sh)
(use Main)
(use _Motion)
(use _Game)
(use _Actor)

(public
	speedChecker 0
)

(local
	local0
)
(instance fred of Actor
	(properties)
)

(instance speedChecker of Room
	(properties
		picture 400
		style IRISIN
	)
	
	(method (init)
		(HandsOff)
		(super init:)
		(sounds eachElementDo: #stop)
		(while (u> (GetTime) -1024)
		)
		(fred
			view: vBlack
			setLoop: 0
			illegalBits: 0
			posn: 20 99
			setStep: 1 1
			setMotion: MoveTo 300 100
			setCycle: Forward
			init:
		)
		(theGame setSpeed: 0)
		(= speedCount 0)
	)
	
	(method (doit &tmp [temp0 200])
		(super doit:)
		(if (== (++ speedCount) 1)
			(= local0 (+ 60 (GetTime)))
		)
		(if (u< local0 (GetTime))
			(cond 
				((<= speedCount 25) (= detailLevel DETAIL_LOW))
				((<= speedCount 40) (= detailLevel DETAIL_MID))
				((<= speedCount 60) (= detailLevel DETAIL_HIGH))
				(else (= detailLevel DETAIL_ULTRA)) ;EO: Added in by 1.200. Is this an Ultra-high setting?
			)
			(theGame setSpeed: 5)
			(RedrawCast)
			(curRoom newRoom: startingRoom)
		)
	)
)

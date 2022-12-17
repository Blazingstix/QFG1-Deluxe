;;; Sierra Script 1.0 - (do not remove this comment)
(script# 2)
(include system.sh) (include sci2.sh) (include game.sh)
(use Main)
(use _Interface)
(use _Save)
(use _Game)
(use _System)

(public
	noticeRoom 0
)

(instance noticeRoom of Room
	(properties
		picture pBlueSkyForCarpet
		style VSHUTTER
	)
	
	(method (init)
		(super init: &rest)
		(self setScript: sayRights)
	)
)

(instance rightsWin of SysWindow
	(properties
		back vLCYAN
	)
)

(instance sayRights of Script
	(properties)
	
	(method (changeState newState &tmp [temp0 2])
		(switch (= state newState)
			(0 (= cycles 2))
			(1
				(if (< colourCount 8) 
					(rightsWin color: vBLACK back: vWHITE)
				)
				
				;(Print 2 0 #mode teJustCenter #width 300 #window rightsWin)
				;(Print 2 1 #mode teJustCenter #width 300 #window rightsWin)
				(Print 2 2 #mode teJustCenter #width 300 #window rightsWin)
				(Print 2 3 #mode teJustCenter #width 300 #window rightsWin)
				(= cycles 1)
				; Oh, by the way . . .  You will need the information contained in the printed documentation to successfully complete this game.
				;
				; In other words, it's not just the law . . .
				; It's a good idea.
			)
			(2 
				;and proceed to the game select screen
				(curRoom newRoom: 9)
			)	
		)
	)
)

;;; Sierra Script 1.0 - (do not remove this comment)
(script# 323)
(include system.sh) (include sci2.sh) (include game.sh)
(use Main)
(use Door)
(use _LoadMany)
(use _GControl)
(use _Extra)
(use _Jump)
(use _Motion)
(use _Game)
(use _Actor)
(use _System)
(use _Interface)

(public 
	rm323 0
)


(instance rm323 of Room
	(properties
		picture 323
		east 330
	)
	
	(method (init)
		(super init:)
		(mouseDownHandler add: self)
		(self setLocales: TOWN)
		(StatusLine enable:)
		(switch prevRoomNum
			(else 
				(ego posn: 265 150 loop: loopW)
			)
		)
		(StopEgo)

		;show the message about the shed interior, if it's the first time entering the room.
		(if (== hideDevNoteShed FALSE)
			(= hideDevNoteShed TRUE)
			(DevPrint 323 0)
		)

		(ego init:)

		(self setScript: RoomScript)
	)
	
	(method (dispose)
		(mouseDownHandler delete: self)
		(Bset VISITED_SHED_INSIDE)
		(super dispose:)
		(if script
			(script dispose:)
		)
	)
)

(instance RoomScript of Script
	(properties)
	
	(method (doit)
		(super doit:)
		; code executed each game cycle
	)
	
	(method (handleEvent pEvent)
		(super handleEvent: pEvent)
		; handle Said's, etc...
	)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0 ; Handle state changes
			)
		)
	)
)

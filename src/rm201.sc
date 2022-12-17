;;; Sierra Script 1.0 - (do not remove this comment)
(script# 201)
(include system.sh) (include sci2.sh) (include game.sh)
(use Main)
(use _Motion)
(use _Game)
(use _User)
(use _Actor)
(use _System)

(public
	selectAvatar 0
)

(enum 1
	selectFighter
	selectMage
	selectThief
)

(local
	newProp
	newProp_3
	newProp_2
	newProp_4
	newView_2
	newView_3
	newView_4
	newView_5
	newView_6
	newView_7
	newView_8
	selectedCharacter
	local12
	newView
	local14
	local15
	local16
	local17 =  1
)
(procedure (ToCharAlloc)
	(HandsOff)
	(curRoom newRoom: 202)
)

(procedure (localproc_001c)
	(switch selectedCharacter
		(selectFighter
			(newView setCel: 0 posn: 64 127 stopUpd:)
			(mageScript changeState: 0)
			(thiefScript changeState: 0)
			(fighterScript changeState: 1)
		)
		(selectMage
			(newView setCel: 0 posn: 158 127 stopUpd:)
			(fighterScript changeState: 0)
			(thiefScript changeState: 0)
			(if local16
				(newProp_3 setLoop: 1 setCel: 3)
				(newProp_2 show:)
			else
				(mageScript changeState: 1)
			)
		)
		(selectThief
			(newView setCel: 0 posn: 252 127 stopUpd:)
			(fighterScript changeState: 0)
			(mageScript changeState: 0)
			(thiefScript changeState: 1)
		)
	)
)

(procedure (SelectCharacter)
	(switch selectedCharacter
		(selectFighter
			(= heroType FIGHTER)
			(ToCharAlloc)
		)
		(selectMage
			(= heroType MAGE)
			(ToCharAlloc)
		)
		(selectThief
			(= heroType THIEF)
			(ToCharAlloc)
		)
	)
)

(instance selectAvatar of Room
	(properties
		picture 904
		style DISSOLVE
	)
	
	(method (init)
		(Load rsSOUND (GetSongNumber 73))
		(User canInput: FALSE)
		((= newView (View new:))
			view: vCharSelect
			setLoop: 0
			setCel: 0
			posn: 0 1000
			setPri: 5
			init:
			ignoreActors:
			stopUpd:
		)
		((= newProp (Prop new:))
			view: (GetEgoViewNumber vEgoCharSelect)
			setPri: 5
			init:
			ignoreActors:
			stopUpd:
			setScript: fighterScript
		)
		((= newProp_2 (Prop new:))
			view: (GetEgoViewNumber vEgoCharSelect)
			setLoop: 3
			setCel: 0
			posn: 158 84
			setPri: 6
			init:
			ignoreActors:
		)
		((= newProp_3 (Prop new:))
			view: (GetEgoViewNumber vEgoCharSelect)
			setPri: 5
			init:
			ignoreActors:
			stopUpd:
			setScript: mageScript
		)
		((= newProp_4 (Prop new:))
			view: (GetEgoViewNumber vEgoCharSelect)
			setPri: 5
			init:
			ignoreActors:
			stopUpd:
			setScript: thiefScript
		)
		((= newView_2 (View new:))
			view: vCharSelect
			setLoop: 1
			setCel: 0
			posn: 83 25
			init:
			ignoreActors:
			stopUpd:
		)
		((= newView_3 (View new:))
			view: vCharSelect
			setLoop: 1
			setCel: 1
			posn: 146 27
			init:
			ignoreActors:
			stopUpd:
		)
		((= newView_4 (View new:))
			view: vCharSelect
			setLoop: 1
			setCel: 2
			posn: 220 27
			init:
			ignoreActors:
			stopUpd:
		)
		((= newView_5 (View new:))
			view: vCharSelect
			setLoop: 1
			setCel: 3
			posn: 65 155
			init:
			ignoreActors:
			stopUpd:
		)
		((= newView_6 (View new:))
			view: vCharSelect
			setLoop: 1
			setCel: 4
			posn: 159 147
			init:
			ignoreActors:
			stopUpd:
		)
		((= newView_7 (View new:))
			view: vCharSelect
			setLoop: 1
			setCel: 6
			posn: 161 163
			init:
			ignoreActors:
			stopUpd:
		)
		((= newView_8 (View new:))
			view: vCharSelect
			setLoop: 1
			setCel: 5
			posn: 252 153
			init:
			ignoreActors:
			stopUpd:
		)
		(self setScript: selScript)
		(super init:)
		(mouseDownHandler add: self)
		(keyDownHandler add: self)
		(directionHandler add: self)
		(if
			(or
				(== (cSound state?) 0)
				(!= (cSound number?) (GetSongNumber 61))
			)
			(cSound number: (GetSongNumber 73) loop: -1 play:)
		)
		(cSound prevSignal: 0)
		(Joystick 12 30)
	)
	
	(method (doit)
		(cond 
			(
				(and
					local17
					(or
						(== (cSound signal?) 2)
						(== (cSound prevSignal?) 2)
					)
				)
				(= local17 0)
				(cSound stop:)
				(cSound number: (GetSongNumber 73) loop: -1 play:)
			)
			((and local15 (== (cSound prevSignal?) 3))
				(cSound prevSignal: 0)
				(if (== selectedCharacter 3) (= selectedCharacter 1) else (++ selectedCharacter))
				(= local14 0)
				(localproc_001c)
			)
			(local16 (= local16 0) (SelectCharacter))
		)
		(super doit:)
	)
	
	(method (dispose)
		(Joystick 12 0)
		(super dispose:)
	)
	
	(method (handleEvent pEvent &tmp temp0 temp1 temp2 [temp3 60])
		(if local14
			(if (== (pEvent type?) keyDown)
				(= local15 0)
				(switch (pEvent message?)
					(TAB
						(pEvent type: direction)
						(pEvent message: dirE)
					)
					(SHIFTTAB
						(pEvent type: direction)
						(pEvent message: dirW)
					)
					(ENTER
						(pEvent claimed: TRUE)
						(= local16 1)
					)
				)
			)
			(switch (pEvent type?)
				(mouseDown
					(= temp1 (pEvent x?))
					(= temp2 (pEvent y?))
					(= temp0 (OnControl CMAP temp1 temp2))
					(= local15 0)
					(pEvent claimed: 1)
					(cond 
						((& temp0 cYELLOW) (= selectedCharacter 1) (= local16 1))
						((& temp0 cLMAGENTA) (= selectedCharacter 2) (= local16 1))
						((& temp0 cLRED) (= selectedCharacter 3) (= local16 1))
						(else (pEvent claimed: 0))
					)
					(pEvent claimed: 1)
					(= local14 0)
					(localproc_001c)
				)
				(direction
					(switch (pEvent message?)
						(dirW
							(if (== selectedCharacter 1) (= selectedCharacter 3) else (-- selectedCharacter))
						)
						(dirE
							(if (== selectedCharacter 3) (= selectedCharacter 1) else (++ selectedCharacter))
						)
					)
					(pEvent claimed: TRUE)
					(= local15 0)
					(= local14 0)
					(localproc_001c)
				)
			)
		)
		(super handleEvent: pEvent)
	)
)

(instance fighterScript of Script
	(properties)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(= seconds (= cycles 0))
				(newProp setLoop: 4 setCel: 0 posn: 64 117)
			)
			(1
				(= seconds (= cycles 0))
				(= selectedCharacter selectFighter)
				(newProp setLoop: 0 setCel: 1)
				(= local14 1)
				(= cycles 5)
			)
			(2
				(newProp setCel: 0)
				(= cycles 5)
			)
			(3
				(fighterScript changeState: 1)
			)
		)
	)
)

(instance mageScript of Script
	(properties)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(= seconds (= cycles 0))
				(newProp_3 setLoop: 4 setCel: 1 posn: 158 117)
				(newProp_2 stopUpd: hide:)
			)
			(1
				(= seconds (= cycles 0))
				(= selectedCharacter selectMage)
				(newProp_3 setLoop: 1 setCel: 0)
				(= local14 1)
				(= cycles 3)
			)
			(2
				(newProp_3 setCel: RELEASE setCycle: EndLoop self)
			)
			(3
				(newProp_2 show: setCycle: Forward startUpd:)
				(= seconds 3)
			)
			(4 (self changeState: 3))
		)
	)
)

(instance thiefScript of Script
	(properties)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(= seconds (= cycles 0))
				(newProp_4 setLoop: 4 setCel: 2 posn: 252 117)
			)
			(1
				(= seconds (= cycles 0))
				(= selectedCharacter selectThief)
				(newProp_4 setLoop: 2 setCel: 0)
				(= local14 1)
				(= seconds 2)
			)
			(2
				(newProp_4 setCel: RELEASE setCycle: EndLoop)
				(= seconds 2)
			)
			(3
				(newProp_4 setCycle: BegLoop self)
			)
			(4 (thiefScript changeState: 1))
		)
	)
)

(instance selScript of Script
	(properties)
	
	(method (doit)
		(if
			(and
				(== state 3)
				(not local15)
				(== (cSound prevSignal?) 3)
			)
			(cSound prevSignal: 0)
			(= local14 1)
			(= local15 1)
			(fighterScript changeState: 1)
			(self dispose:)
		else
			(super doit:)
		)
	)
	
	(method (changeState newState)
		(switch (= state newState)
			(0 (= cycles 5))
			(1
				(++ local12)
				(newView_2 hide:)
				(newView_3 hide:)
				(newView_4 hide:)
				(= cycles 3)
			)
			(2
				(newView_2 show:)
				(newView_3 show:)
				(newView_4 show:)
				(= cycles 3)
			)
			(3
				(cSound prevSignal: 0)
				(if (< local12 2) (self changeState: 1))
			)
		)
	)
)

;;; Sierra Script 1.0 - (do not remove this comment)
(script# 288)
(include system.sh) (include sci2.sh) (include game.sh)
(use Main)
(use _Motion)
(use _System)
(use _interface)

(public
	vaseScript 0
	upScript 1
	downScript 2
	rm321Script 3
	raisePainting 4
	lowerPainting 5
)

(enum
	rm321 		;0
	vase 		;1
	miscMusic 	;2
	sneakMusic 	;3
	portrait 	;4
	sheriff 	;5
	otto 		;6
	bottomDoor 	;7
	leftDoor 	;8
	rightDoor 	;9
	musicBox 	;10
)

(local
	egoUpstairs
)

(procedure (BreakInPrint)
	(if egoUpstairs (CenterPrint &rest) else (HighPrint &rest))
)

(instance vaseScript of Script
	(properties)
	
	(method (dispose)
		(super dispose:)
		(DisposeScript 288)
	)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(HandsOff)
				(BreakInPrint 288 0)
				;As you reach toward the painting...
				((ScriptID 321 vase) startUpd: setCycle: EndLoop self)
			)
			(1
				((ScriptID 321 vase) cel: 0 setCycle: EndLoop self)
			)
			(2
				((ScriptID 321 vase)
					setLoop: 6
					setCel: 0
					posn: 253 123
					setStep: 0 8
					setMotion: MoveTo 253 147 self
				)
			)
			(3
				((ScriptID 321 miscMusic)
					loop: 1
					number: (GetSongNumber 14)
					play:
				)
				((ScriptID 321 vase) setCel: 1)
				(= cycles 1)
			)
			(4
				((ScriptID 321 vase) setCel: 2 stopUpd:)
				(HandsOn)
				(= cycles 2)
			)
			(5
				(BreakInPrint 288 1)
				;From somewhere upstairs, you hear the Sheriff shout: "Otto!".
				(ego setScript: (ScriptID 289 0)) ;call bustedScript
				(client setScript: NULL)
				;(Print {call bustedScript})
			)
		)
	)
)

(instance upScript of Script
	(properties)
	
	(method (dispose)
		(super dispose:)
		(DisposeScript 288)
	)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(HandsOff)
				(ego illegalBits: 0 setMotion: MoveTo 83 60 self)
			)
			(1
				(HandsOn)
				(ego illegalBits: (| cWHITE cLCYAN))
				(ego setScript: NULL)
			)
		)
	)
)

(instance downScript of Script
	(properties)
	
	(method (dispose)
		(super dispose:)
		(DisposeScript 288)
	)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(HandsOff)
				(ego illegalBits: 0 setMotion: MoveTo 36 86 self)
			)
			(1
				(HandsOn)
				(ego illegalBits: (| cWHITE cLCYAN))
				(ego setScript: NULL)
			)
		)
	)
)

(instance rm321Script of Script
	(properties)
	
	(method (dispose)
		(super dispose:)
		(DisposeScript 288)
	)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(if (and (!= prevRoomNum 0) (not (Btst VISITED_SHERIFF_HOUSE)))
					(= cycles 8)
				else
					((ScriptID 321 rm321) setScript: NULL)
				)
			)
			(1
				(BreakInPrint 288 2)
				;The people who own this house must have some money. Everything looks new and there is not a speck of dust visible.
				(BreakInPrint 288 3)
				;The room smells vaguely of sauerkraut and bratwurst, with just a faint odor of smoke from pinewood.
				;From somewhere in the house you can hear someone snoring.
				((ScriptID 321 rm321) setScript: NULL)
			)
		)
	)
)

(instance raisePainting of Script
	(properties)
	
	(method (dispose)
		(super dispose:)
		(DisposeScript 288)
	)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				((ScriptID 321 portrait) posn: 277 109)
				(Bset UNCOVERED_SHERIFF_SAFE)
				(= cycles 2)
			)
			(1
				(BreakInPrint 288 portrait)
				;By lifting the painting, you can see what certainly must be a safe, hidden in the wall.
				((ScriptID 321 rm321) setScript: NULL)
			)
		)
	)
)

(instance lowerPainting of Script
	(properties)
	
	(method (dispose)
		(super dispose:)
		(DisposeScript 288)
	)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				((ScriptID 321 portrait) posn: 277 121)
				(Bclr UNCOVERED_SHERIFF_SAFE)
				(= cycles 2)
			)
			(1
				(BreakInPrint 288 5)
				;You carefully lower the painting into its original position.
				((ScriptID 321 rm321) setScript: NULL)
			)
		)
	)
)

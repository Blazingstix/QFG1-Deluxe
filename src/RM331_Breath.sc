;;; Sierra Script 1.0 - (do not remove this comment)
(script# TAVERN_BREATH)
(include system.sh) (include sci2.sh) (include game.sh)
(use Main)
(use _Motion)
(use _System)

(public
	breathScript 0
)

(enum
	rm331 			;0
	DB 				;1
	bartender 		;2
	smoke 			;3
	barSound 		;4
	head 			;5
	trap 			;6
	crusher 		;7
	bartenderScript ;8
)

(instance breathScript of Script
	(properties)
	
	(method (dispose)
		(super dispose:)
		(DisposeScript TAVERN_BREATH)
	)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				((ScriptID TAVERN bartender)
					setLoop: 6
					setMotion: MoveTo 171 80 self
				)
			)
			(1
				((ScriptID TAVERN bartender)
					view: vBartenderPouring
					setLoop: 0
					cel: 0
					posn: 169 80
					setCycle: CycleTo 5 cdFORWARD self
				)
			)
			(2
				((ScriptID TAVERN smoke) hide:)
				((ScriptID TAVERN barSound) number: (GetSongNumber 44) play:)
				((ScriptID TAVERN DB) loop: 2 cel: 0 setCycle: CycleTo 6 cdFORWARD self) ;change DB to the pouring loop
			)
			(3
				((ScriptID TAVERN DB) setCycle: CycleTo 9 cdFORWARD)
				((ScriptID TAVERN bartender) setCycle: EndLoop self)
			)
			(4
				((ScriptID TAVERN DB) setCycle: EndLoop self)
			)
			(5
				;change DB to the skull smoke loop
				((ScriptID TAVERN DB) loop: 3 cel: 0 posn: 159 48)	;CI: change y position to line up better
				(self cue:)
			)
			(6
				((ScriptID TAVERN barSound) number: (GetSongNumber 43) play:)
				((ScriptID TAVERN DB)
					posn: ((ScriptID TAVERN DB) x?) (- ((ScriptID TAVERN DB) y?) 3)
					setCycle: CycleTo (+ ((ScriptID TAVERN DB) cel?) 1) cdFORWARD self
				)
			)
			(7
				(if (!= ((ScriptID TAVERN DB) cel?) 12)
					(self changeState: 6)
				else
					(self cue:)
				)
			)
			(8
				((ScriptID TAVERN DB) posn: 159 68 stopUpd:) ;CI: change DB position t line up better.
				((ScriptID TAVERN smoke) show:)
				((ScriptID TAVERN bartender) cel: 2 setCycle: BegLoop self)
			)
			(9
				((ScriptID TAVERN bartender) view: vBartender setLoop: 1 cel: 0);CI: NOTE: removed the call to stopUpd: so the bartended will resume tending bar after we retry after a bragon's breath death.
				(if (Btst HERO_SITTING)
					(ego loop: 3 cel: 0)
					((ScriptID TAVERN head) hide:)
				)
				(= drinkOrdered drinkNothing)
				(= drinkInHand drinkBreath)
				(HighPrint 335 0 83)
				;"There ya go!"
				(self dispose:)
			)
		)
	)
)

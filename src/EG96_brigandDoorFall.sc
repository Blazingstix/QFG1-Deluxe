;;; Sierra Script 1.0 - (do not remove this comment)
(script# 232)
(include system.sh) (include sci2.sh) (include game.sh)
(use Main)
(use _Motion)
(use _System)

(public
	doorFall 0
	finalExit 1
)

(local
	local0
)
(instance doorFall of Script
	(properties)
	
	(method (dispose)
		(ego illegalBits: (& (ego illegalBits?) $ffff))
		(super dispose:)
		(DisposeScript 232)
	)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(ego illegalBits: (| (ego illegalBits?) cCYAN cMAGENTA))
				(++ local0)
				((ScriptID 96 8)
					posn: (- ((ScriptID 96 8) x?) 1) ((ScriptID 96 8) y?)
				)
				(= cycles 1)
			)
			(1
				((ScriptID 96 8)
					posn: (+ ((ScriptID 96 8) x?) 1) ((ScriptID 96 8) y?)
				)
				(= cycles 1)
			)
			(2
				(if (< local0 7)
					(self changeState: 0)
				else
					(= local0 0)
					(self cue:)
				)
			)
			(3
				((ScriptID 96 12) ignoreActors: setPri: 4 init: stopUpd:) ;reveal the real door
				(cond 
					((and (== (ego onControl: origin) cLRED) (not (ego script?)))
						;if the hero's on the fake door's path, crush them
						(HandsOff)
						(ego hide:)
						;set the view to be the crushed hero.
						;((ScriptID 96 8) setLoop: 7 setCel: 8)
						((ScriptID 96 8) 
							view: (GetEgoViewNumber vEgoFlattenedByJesterDoor) ;CI: switch views to the ego flattened by door view.
							setLoop: 0 
							setCel: 0
						)
						(= cycles 1)
					)
					(((ScriptID 96 1) script?)
						;or else trap1 is active, then reset the doors
						((ScriptID 96 8) setCel: 0 stopUpd:)
						((ScriptID 96 12) delete:)
						(client setScript: NULL)
					)
					(else 
						;otherwise, continue
						(self cue:)
					)
				)
			)
			(4
				((ScriptID 96 8)
					view: vJesterRoom ;CI: reset the view back to the proper door script.
					setLoop: 1
					cel: (if (== (ego onControl: origin) cLRED) 2 else 0)
					setCycle: EndLoop self
				)
			)
			(5
				((ScriptID 96 16)
					number: (GetSongNumber 84)
					loop: 1
					priority: 2
					play:
				)
				(if
				(and (== (ego onControl: origin) cLRED) (not (ego script?)))
					(= cycles 15)
				else
					(self changeState: 12) ;jump a few cycles, if we're not injured.
				)
			)
			(6
				(if (TakeDamage 10)
					(if (== (ego onControl: origin) cLRED)
						((ScriptID 96 8) setCycle: CycleTo 2 cdBACKWARD self)
					else
						((ScriptID 96 8) setCycle: CycleTo 1 cdBACKWARD self)
					)
				else
					(EgoDead DIE_RETRY DIE_ENDGAME_DOORFLAT 232 0
						#icon (GetEgoViewNumber vEgoDefeatedMagic) 5 8
						#title {This way to the Egress}
					)
					;This time the joke fell flat.  That was a truly dirty trick.  Too bad you won't have a chance to get even...or will you?
					(curRoom resetRoom:)
				)
			)
			(7
				(if (== (ego onControl: origin) cLRED)
					;set the view to be the crushed hero.
					;((ScriptID 96 8) setLoop: 7 setCel: 8)
					((ScriptID 96 8) 
						view: (GetEgoViewNumber vEgoFlattenedByJesterDoor) ;CI: switch views to the ego flattened by door view.
						setLoop: 0 
						setCel: 0
					)
					(= cycles 2)
				else
					(self cue:)
				)
			)
			(8
				((ScriptID 96 8)
					view: vJesterRoom ;CI: reset the view back to the proper door script.
					setLoop: 1 
					setCel: 0
				)
				(ego
					view: (GetEgoViewNumber vEgoFallDown)
					setLoop: 0
					cel: 0
					illegalBits: 0
					posn: 106 95
					show:
				)
				((ScriptID 96 12) dispose:)
				(= cycles 5)
			)
			(9
				(ego setCycle: EndLoop setMotion: MoveTo 97 (+ (ego y?) 6))
				(= cycles 15)
			)
			(10
				(ego
					view: (GetEgoViewNumber vEgoDefeated)
					setLoop: 4
					cel: 0
					x: (- (ego x?) 4)
					cycleSpeed: 1
					setCycle: EndLoop self
				)
			)
			(11
				(HighPrint 232 1)
				;What a miserable experience!
				(self cue:)
			)
			(12
				(ego setLoop: loopS)
				(if (not (ego script?)) (StopEgo))
				(= cycles 3)
			)
			(13
				((ScriptID 96 8) stopUpd:)
				;(if (!= ((ScriptID 96 8) cel?) 0) (Bset FLAG_264)) ;CI: This had to be reworked, because the original test is no longer valid, now that we're changing the loop, not just the cel.
				(if (or (!= ((ScriptID 96 8) cel?) 0)
						(== ((ScriptID 96 8) loop?) 0)
					) 
						;we're testing that the fake door is open.
						(Bset FLAG_264)
				)
						 
				(HandsOn)
				(client setScript: NULL)
			)
		)
	)
)

(instance finalExit of Script
	(properties)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(HandsOff)
				(Bset OPENING_LEADER_DOOR)
				((ScriptID 96 12) setCel: 1)
				(= cycles 2)
			)
			(1
				(ego illegalBits: 0 setMotion: MoveTo 109 98 self)
			)
			(2
				(ego setPri: 3 setMotion: MoveTo 118 92 self)
			)
			(3
				(Bclr OPENING_LEADER_DOOR)
				(curRoom newRoom: 97)
			)
		)
	)
)

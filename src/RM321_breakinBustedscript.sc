;;; Sierra Script 1.0 - (do not remove this comment)
(script# 289)
(include system.sh) (include sci2.sh) (include game.sh)
(use Main)
(use _Motion)
(use _User)
(use _Actor)
(use _System)
(use _Interface)

(public
	bustedScript 0
	faceTheMusicScript 1
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
	timesCrackedSafe	;CI: This was a variable in the main Sheriff room (321), and presumably the bustedScript was also in that file originally, too
						;At some point this was moved to its own script, but the lack of global variable was never caught
)

(procedure (BreakInPrint)
	(if egoUpstairs (CenterPrint &rest) else (HighPrint &rest))
)

(instance brokeDoorL of View
	(properties)
)

(instance brokeDoorR of View
	(properties)
)

(instance bustedScript of Script
	;the vase has busted.
	(properties)
	
	(method (dispose)
		(super dispose:)
		(DisposeScript 289)
	)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(User canInput: FALSE)
				((ScriptID 321 otto)
					show:
					setLoop: 0
					cel: 1
					posn: 125 119
					setPri: 8
				)
				((ScriptID 321 sheriff) 
					show:
					setLoop: 2 
					cel: 0 
					posn: 117 48
				)
				((ScriptID 321 bottomDoor)
					loop: 7
					cel: 0
					posn: 109 119
					setPri: 9
					cycleSpeed: 1
					setCycle: EndLoop self
				)
			)
			(1
				((ScriptID 321 miscMusic)
					loop: 1
					number: (GetSongNumber 15)
					play:
				)
				;brokenDoor Left
				(brokeDoorL
					view: vSheriffHouse
					loop: 4
					cel: 5
					posn: 117 128
					init:
					;stopUpd:
				)
				;brokenDoor Right
				(brokeDoorR
					view: vSheriffHouse
					loop: 4
					cel: 6
					posn: 139 128
					init:
				;	stopUpd:
				)

				((ScriptID 321 otto) setPri: 9 setMotion: MoveTo 136 140)
				((ScriptID 321 leftDoor) setCycle: EndLoop self)
			)
			(2
				((ScriptID 321 sheriff) setMotion: MoveTo 122 60 self)
			)
			(3
				((ScriptID 321 sneakMusic) stop:)
				(if (== register 2)
					;CI: NOTE: This condition was originally set to a local variable that was never set
					(EgoDead DIE_RETRY DIE_SHERIFF_POWERGAMING 289 0
						#title { Try to stay "in character" next time_}
						#icon (GetEgoViewNumber vEgoDefeated) 1 0
						;Naughty, naughty.  The Sheriff and Otto arrive on the scene and arrest you for "blatant power-gaming".
						;You have to *work* at it to become a *real* hero!
					)
				else
					(BreakInPrint 289 1)
					;Obviously, you shouldn't have done that!
					(EgoDead DIE_RETRY DIE_SHERIFF_BREAK_VASE 289 2 
						#title { Criminal carelessness._} 
						#icon vOttoAsleep 4 0
						;Now you've done it!  It's hard to be a hero when you're locked up for breaking and entering (or entering and breaking, as the case may be).
						;Be a little more intelligent (and a lot quieter) if you ever try something like this again.
					)
				)
				(= cycles 1)
			)
			(4
				;reset the actors
				((ScriptID 321 sheriff) posn: 0 0 hide:)
				((ScriptID 321 otto) posn: 0 0 hide: setPri: RELEASE setMotion: NULL)
				;hide the broken door bits, and close the doors
				((ScriptID 321 leftDoor) cel: 0)
				((ScriptID 321 bottomDoor) loop: 3 cel: 0 setPri: RELEASE)
				(brokeDoorL dispose:)
				(brokeDoorR dispose:)

				;reset the vase
				(if (not (Btst STOLE_SHERIFF_VASE))
					((ScriptID 321 vase)
						loop: 5 cel: 0
						posn: (if (Btst MOVED_SHERIFF_VASE) 247 else 262) (if (Btst MOVED_SHERIFF_VASE) 136 else 119)
						forceUpd:
					)
				)
				(StopEgo)
				(ego illegalBits: (| cWHITE cLCYAN))
				((ScriptID 321 sneakMusic) play:)
				;need to give the cast a cycle or two do properly dispose of the rubble.
				(= cycles 1)
			)
			(5
				(self dispose:)
			)
		)

	)
)

;this gets called when the hero tries to open the music box.
;Otto sleepwalks to the music box, closes it and goes back to bed.
;Unless
(instance faceTheMusicScript of Script
	(properties)
	
	(method (doit)
		(cond 
			((< (ego distanceTo: (ScriptID 321 otto)) 20)
				;the hero's less than 20 pixels away from Otto, so bring up the death script.
				(self changeState: 9)
			)
			((> (ego y?) 187)
				(User canControl: FALSE)
				(User canInput: FALSE)
			)
		)
		(super doit:)
	)
	
	(method (dispose)
		(super dispose:)
		(DisposeScript 289)
	)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(Bset OTTO_BACK_TO_BED)
				(BreakInPrint 289 3)
				;Gently and stealthily, you lift the lid on the little box.
				(self cue:)
			)
			(1
				(HandsOff)
				((ScriptID 321 musicBox) setCel: 4)
				(Bset OPEN_MUSIC_BOX)
				(Bset OTTO_CLOSES_MUSIC_BOX)
				((ScriptID 321 sneakMusic) stop:)
				((ScriptID 321 miscMusic) loop: 1 number: 13 play:)
				(= seconds 3)
			)
			(2
				(BreakInPrint 289 4 #draw)
				;As the little music box begins to play, you hear the Sheriff yell out: "Otto, stop playing with that music box and GO TO BED!"
				(BreakInPrint 289 5)
				;Boy, did YOU make a mistake!
				(ego setMotion: MoveTo (+ (ego x?) 30) (ego y?))
				(= seconds 2)
			)
			(3
				((ScriptID 321 otto)
					show:
					setLoop: 1
					cel: 1
					posn: 130 118
					setPri: 8
				)
				((ScriptID 321 bottomDoor) setCycle: EndLoop self)
			)
			(4
				(ego loop: loopW)
				((ScriptID 321 otto)
					cycleSpeed: 1
					moveSpeed: 1
					setMotion: MoveTo 130 150 self
				)
			)
			(5
				((ScriptID 321 otto) setLoop: 5 cel: 0 setCycle: Forward)
				(= cycles 20)
			)
			(6
				(cond 
					((ego has: iMusicBox)
						(BreakInPrint 289 6)
						;Otto can't find the music box, but he's too dim and sleepy to figure it out, so he heads on back to bed
						)
					((Btst OPEN_MUSIC_BOX)
						((ScriptID 321 musicBox) setCel: 3 forceUpd:)
						((ScriptID 321 miscMusic) stop:)
						((ScriptID 321 sneakMusic) play:)
						(BreakInPrint 289 7 #draw)
						;Otto, even in his sleepy state, winds the music box and closes the lid before he heads back to bed.
						(Bclr OPEN_MUSIC_BOX)
					)
					(else
						(BreakInPrint 289 8)
						;Otto stares sleepily at the closed music box for a moment and heads back to bed.
						)
				)
				((ScriptID 321 otto)
					setLoop: 2
					setCycle: Walk
					setMotion: MoveTo 130 118 self
				)
			)
			(7
				((ScriptID 321 otto) hide: posn: 0 1000 stopUpd:) ;set him offscreen ... (CI: why not hide: ?)
				((ScriptID 321 bottomDoor) setCycle: BegLoop self)
			)
			(8
				(BreakInPrint 289 9)
				;That was close! The goon must've been so dumb or sleepy or both that he didn't even see you standing there.
				(Bclr OTTO_BACK_TO_BED)
				(HandsOn)
				(ego setScript: NULL)
			)
			(9
				((ScriptID 321 sneakMusic) stop:)
				(EgoDead DIE_RETRY DIE_SHERIFF_MUSICBOX 289 10
					#title { Criminal carelessness._}
					#icon (GetEgoViewNumber vEgoDefeated) 1 0
					;Obviously, getting in the goon's way was not one of your brighter ideas. You've had it now!
				)
				(= cycles 1)
			)
			(10
				;reset the scene for a Retry!
				((ScriptID 321 otto) hide: posn: 0 1000 stopUpd:)
				((ScriptID 321 bottomDoor) cel: 0)
				((ScriptID 321 musicBox) setCel: 3 forceUpd:)

				(Bclr OTTO_BACK_TO_BED)
				(Bclr OPEN_MUSIC_BOX)
				(Bclr OTTO_CLOSES_MUSIC_BOX)
				;reset ego back 30 steps to the right
				(ego setScript: NULL posn: (- (ego x?) 30) (ego y?) loop: loopS illegalBits: (| cWHITE cLCYAN))
				((ScriptID 321 sneakMusic) play:)
			)
		)
	)
)

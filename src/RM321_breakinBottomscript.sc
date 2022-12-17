;;; Sierra Script 1.0 - (do not remove this comment)
(script# 290)
(include system.sh) (include sci2.sh) (include game.sh)
(use Main)
(use _Interface)
(use _Reverse)
(use _Motion)
(use _Actor)
(use _System)

(public
	bottomScript 0
	leftScript 1
	rightScript 2
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
	escapeCount
)

(instance stars of Prop
	(properties)
)

(instance pillow of Prop
	(properties)
)

(instance gerta of Actor
	(properties)
)

(instance egoBody of View
	(properties)
)

(instance brokeDoorL2 of View
	(properties)
)

(instance brokeDoorR2 of View
	(properties)
)

(instance bottomScript of Script
	(properties)

	(method (dispose)
		(super dispose:)
		(DisposeScript 290)
	)
		
	(method (changeState newState)
		(switch (= state newState)
			(0
				(HandsOff)
				(ego illegalBits: 0)
				((ScriptID 321 bottomDoor) setCycle: EndLoop self)
			)
			(1
				(ego setMotion: MoveTo 130 112)
				(= cycles 15)
			)
			(2
				(ego
					setLoop: loopN
					setCycle: Reverse
					moveSpeed: 1
					cycleSpeed: 1
					setMotion: MoveTo 130 130 self
				)
			)
			(3
				((ScriptID 321 otto)
					show:
					setLoop: 0
					posn: 130 112
					cycleSpeed: 1
					moveSpeed: 1
					setMotion: MoveTo 130 120
				)
				(ego
					setLoop: loopS
					setCycle: Forward
					moveSpeed: 0
					cycleSpeed: 0
					setStep: 3 3
					setMotion: MoveTo 130 150 self
				)
			)
			(4
				(ego setMotion: MoveTo 130 146 self)
			)
			(5
				(++ escapeCount)
				(ego setMotion: MoveTo 130 150 self)
			)
			(6
				(if (> escapeCount 4)
					(self cue:)
				else
					(self changeState: 4)
				)
			)
			(7
				((ScriptID 321 sneakMusic) stop:)
				(EgoDead DIE_RETRY DIE_SHERIFF_OTTO_DOOR 290 0
					#title { Criminal carelessness._}
					#icon vOttoAsleep 4 0)
					;It's hard to be a hero when you're sitting in a jail cell.
					;The Sheriff apologized for your broken arm, but he did warn you (didn't he?) that Otto was only partly trained.
					;In the future, you'll probably be more careful when you're robbing someone.
				(= cycles 1)
			)
			(8
				;retry cleanup
				(client setScript: NULL)
				;reset ego and other actors
				(StopEgo)
				(ego loop: loopN illegalBits: (| cWHITE cLCYAN) posn: 130 125)
				((ScriptID 321 bottomDoor) cel: 0 stopUpd:)
				((ScriptID 321 otto) hide: posn: 0 0 setPri: RELEASE setMotion: NULL)
				((ScriptID 321 sneakMusic) play:)
				(HandsOn)
				;(ego setScript: NULL)
			)
		)
	)
)

(instance leftScript of Script
	(properties)
	
	(method (dispose)
		(super dispose:)
		(DisposeScript 290)
	)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(HandsOff)
				(ego illegalBits: 0 ignoreActors:)
				((ScriptID 321 leftDoor) setCycle: EndLoop self)
			)
			(1
				(ego setLoop: loopN setMotion: MoveTo 119 51)
				(= cycles 15)
			)
			(2
				(ego
					setLoop: loopN
					setCycle: Reverse
					moveSpeed: 2
					cycleSpeed: 2
					setMotion: MoveTo 119 59 self
				)
			)
			(3
				(Bset SHERIFF_AWAKENED)
				(ego
					view: (GetEgoViewNumber vEgoBigGrin)
					posn: 117 59
					setLoop: loopW
					cycleSpeed: 0
					moveSpeed: 0
					setCycle: Forward
					setMotion: MoveTo 108 59 self
				)
			)
			(4
				((ScriptID 321 sheriff)
					show: 
					setLoop: 2
					cel: 0
					posn: 117 48
					cycleSpeed: 1
					moveSpeed: 1
					setMotion: MoveTo 122 58
				)
				(ego setMotion: MoveTo 85 59 self)
			)
			(5
				((ScriptID 321 sheriff)
					setLoop: 1
					cycleSpeed: 0
					moveSpeed: 0
					setMotion: MoveTo 85 58
				)
				(StopEgo)
				(ego
					illegalBits: 0
					ignoreActors:
					setLoop: loopW
					setMotion: MoveTo 80 59 self
				)
			)
			(6
				(ego
					view: (GetEgoViewNumber vEgoDeadBrigands)
					setCycle: Forward
					setLoop: 0
					posn: 79 44
					setStep: 6 9
					setMotion: MoveTo 37 67 self
				)
			)
			(7
				(ego setLoop: 5 setMotion: MoveTo 14 97 self)
			)
			(8
				(ego setLoop: 1 setMotion: MoveTo 20 135 self)
			)
			(9
				((ScriptID 321 sheriff) setLoop: 2)
				(ego setLoop: 3 setCel: 1 setPri: 12 posn: 20 134)
				;ego's body sitting on the floor
				(egoBody ;(View new:)
					view: (GetEgoViewNumber vEgoDeadBrigands)
					loop: 2
					cel: 0
					posn: 20 155 ;CI: updated from 19 155 to better align with the head.
					init:
					stopUpd:
				)
				(stars
					view: (GetEgoViewNumber vEgoDeadBrigands)
					loop: 4
					cel: 0
					posn: 19 135
					init:
					setCycle: Forward
					startUpd:
				)
				(= cycles 2)
			)
			(10
				(ego setCel: 2)
				(++ escapeCount)
				(= cycles 2)
			)
			(11
				(ego setCel: 1)
				(= cycles 2)
			)
			(12
				(if (< escapeCount 4)
					(self changeState: 10)
				else
					(stars dispose:)
					(ego setCel: 0)
					(= cycles 2)
				)
			)
			(13
				((ScriptID 321 sneakMusic) stop:)
				(EgoDead DIE_RETRY DIE_SHERIFF_HIS_DOOR 290 1
					#title { Criminal carelessness._}
					#icon (GetEgoViewNumber vEgoDefeated) 1 0)
					;When it comes to breaking in, it looks like the only thing broken is your head!
					;When at last you come to your senses, you find yourself in a jail cell.
					;By the time you get out of here, you'll be too old to be a hero.
				(= cycles 1)
				(egoBody dispose:)
			)
			(14
				;retry cleanup
				;reset ego and other actors
				(StopEgo)
				(ego loop: loopN illegalBits: (| cWHITE cLCYAN) posn: 22 150 ignoreActors: FALSE setScript: NULL)
				((ScriptID 321 leftDoor) cel: 0)
				((ScriptID 321 sheriff) hide: posn: 0 0 forceUpd: )
				(Bclr SHERIFF_AWAKENED)
				((ScriptID 321 sneakMusic) play:)
				(HandsOn)
				;clean up final script
				(client setScript: NULL)
			)
		)
	)
)

(instance rightScript of Script
	(properties)

	(method (dispose)
		(super dispose:)
		(DisposeScript 290)
	)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(HandsOff)
				(ego illegalBits: 0)
				((ScriptID 321 rightDoor) setCycle: EndLoop self)
			)
			(1
				(ego setMotion: MoveTo 164 51)
				(= cycles 15)
			)
			(2
				(Print 290 2)
				;Uh Oh! As you see a pillow flying toward you, you hear the Sheriff's wife say...
				(Print 290 3 #at 90 18 #title {Gerta Meistersson}) ;CI: added a title to show it's the Sheriff's wife screaming.
				; "SCREEEEEEEEEEEECH!"
				(pillow
					view: vOttoAsleep
					loop: 6
					cel: 0
					posn: 166 26
					setPri: 1
					init:
					setCycle: Forward
					cycleSpeed: 1
					startUpd:
				)
				(ego setPri: 0)
				(= cycles 12)
			)
			(3
				((ScriptID 321 leftDoor) setCycle: EndLoop)
				((ScriptID 321 sheriff)
					show:
					setLoop: 2
					cel: 0
					posn: 115 42
					moveSpeed: 1
					setMotion: MoveTo 122 60
				)
				(ego
					setLoop: 3
					setPri: RELEASE
					setCycle: Reverse
					moveSpeed: 2
					cycleSpeed: 2
					setMotion: MoveTo 175 60 self
				)
			)
			(4
				(pillow dispose:)
				((ScriptID 321 leftDoor) stopUpd:)
				(ego
					view: (GetEgoViewNumber vEgoDeadBrigands)
					setLoop: 2
					cel: 1
					posn: 175 43
					setCycle: 0
					setPri: 9
				)
				((ScriptID 321 sheriff) setLoop: 4 setCel: 0)
				(= cycles 8)
			)
			(5
				((ScriptID 321 sheriff) setCel: 1 stopUpd:)
				(ego
					view: (GetEgoViewNumber vEgoDeadBrigands)
					setLoop: 1
					cel: 0
					setStep: 4 14
					moveSpeed: 0
					cycleSpeed: 1
					setCycle: CycleTo 1 cdFORWARD self
				)
			)
			(6
				(ego setPri: 10 setCycle: CycleTo 2 cdFORWARD self)
			)
			(7
				(ego setCycle: Forward setMotion: MoveTo 175 125 self)
				(gerta
					view: vOttoAsleep
					setLoop: 3
					setCel: 0
					posn: 187 44
					illegalBits: 0
					ignoreActors:
					init:
					setCycle: 0
					setMotion: MoveTo 173 44
				)
			)
			(8
				((ScriptID 321 sheriff) setCel: 2)
				(gerta stopUpd:)
				((ScriptID 321 rightDoor) stopUpd:)
				(ego posn: 175 128 setLoop: 6 setCel: 0)
				(stars
					view: (GetEgoViewNumber vEgoDeadBrigands)
					loop: 4
					cel: 0
					posn: 176 141
					init:
					setCycle: Forward
					startUpd:
				)
				(= cycles 2)
			)
			(9
				((ScriptID 321 bottomDoor)
					loop: 7
					cel: 0
					posn: 109 119
					setPri: 9
					cycleSpeed: 1
					setCycle: EndLoop self
				)
				((ScriptID 321 otto)
					show:
					setLoop: 1
					cel: 1
					posn: 125 119
					setPri: 8
				)
			)
			(10
				((ScriptID 321 miscMusic)
					loop: 1
					number: (GetSongNumber 15)
					play:
				)
				(brokeDoorL2
					view: vSheriffHouse
					loop: 4
					cel: 5
					posn: 117 128
					init:
				;	stopUpd:
				)
				(brokeDoorR2
					view: vSheriffHouse
					loop: 4
					cel: 6
					posn: 139 128
					init:
				;	stopUpd:
				)
				((ScriptID 321 otto)
					cycleSpeed: 1
					moveSpeed: 1
					setPri: 9
					setMotion: MoveTo 135 130 self
				)
			)
			(11
				((ScriptID 321 otto) setLoop: 5 setCycle: Forward)
				(= cycles 10)
			)
			(12
				((ScriptID 321 sneakMusic) stop:)
				(EgoDead DIE_RETRY DIE_SHERIFF_HER_DOOR 290 4
					#title { Criminal carelessness._}
					#icon (GetEgoViewNumber vEgoDeadBrigands) 6 0)
					;You never dreamed a feather pillow could be so HARD!  Or was it the floor?
					;You also didn't realize how hard it is for a thief to be a Hero.  The Thief of Baghdad should have this kind of luck!
				(= cycles 1)
			)
			(13
				;retry cleanup
				;reset the global actors
				((ScriptID 321 sheriff) hide: posn: 0 0 stopUpd: forceUpd:)
				((ScriptID 321 otto) hide: posn: 0 0 setPri: RELEASE setMotion: NULL setCycle: NULL cel: 0)

				;reset the local scrip actors
				(stars dispose:)
				(gerta dispose:)
				(brokeDoorL2 dispose:)
				(brokeDoorR2 dispose:)
				
				;reset the doors
				((ScriptID 321 leftDoor) cel: 0)
				((ScriptID 321 rightDoor) cel: 0)
				((ScriptID 321 bottomDoor) loop: 3 cel: 0 setPri: RELEASE)
				
				(StopEgo)
				(ego posn: 22 150 loop: loopN illegalBits: (| cWHITE cLCYAN))
				((ScriptID 321 sneakMusic) play:)
				;clean up final script
				;need to give the cast a couple of cycles to properly delete all of the disposed cast.
				(= cycles 1)
			)
			(14
				(self dispose:)
			)
		)
	)
)

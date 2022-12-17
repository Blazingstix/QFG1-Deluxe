;;; Sierra Script 1.0 - (do not remove this comment)
(script# 294)
(include system.sh) (include sci2.sh) (include game.sh)
(use Main)
(use _Interface)
(use _Chase)
(use _Motion)
(use _User)
(use _System)

(public
	nextWitch 0
)

;enums to use for the Script ID calls
(enum
	id_RM21
	id_BABA
	id_BABAHEAD
	id_BAT
	id_SPIDER
	id_TP
	id_NOBRINGDEATH
	id_BABATALK
	id_TELEPORT
	id_ENDGAME
	id_LIEDEATH
)

(local
	babaTalking
)
(instance nextWitch of Script
	(properties)
	
	(method (dispose)
		(super dispose:)
		(DisposeScript 294)
	)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(HandsOff)
				(ego setMotion: MoveTo 120 152 self)
			)
			(1
				((ScriptID 21 id_BABA) setLoop: 6 posn: 168 122 init:) ;baba Actor from Script 21.
				((ScriptID 21 id_BABAHEAD) ;babaHead Prop
					posn: ((ScriptID 21 id_BABA) x?) (- ((ScriptID 21 id_BABA) y?) 34)
					init:
					setPri: 15
					setCycle: Forward
				)
				((ScriptID 21 id_TELEPORT) play:) ;teleport Sound
				(= cycles 5)
			)
			(2
				(TimedPrint 5 294 0)
				;"Back so soon?"
				((ScriptID 21 id_BAT) setCycle: EndLoop) ;bat Prop
				((ScriptID 21 id_SPIDER) startUpd:)	;spider Prop
				(= babaTalking TRUE)
				(= seconds 3)
			)
			(3
				(ego loop: loopE)
				((ScriptID 21 id_BABAHEAD) setCycle: NULL) ;babaHead Prop
				(= seconds 2)
			)
			(4
				((ScriptID 21 id_BAT) setCycle: NULL) ;bat Prop
				((ScriptID 21 id_SPIDER) setCycle: NULL) ;spider Prop
				(Print 294 1
					#at -1 12
					#width 140
					#mode teJustCenter
					#dispose
					#time 10)
				((ScriptID 21 id_BABAHEAD) setCycle: Forward) ;babaHead Prop
				(= seconds 7)
				; "Spirits of Mist
				; And Creatures of Bog:
				; Transform my guest
				; To the shape of a Frog."
			)
			(5
				(= babaTalking FALSE)
				((ScriptID 21 id_BABA) setCycle: EndLoop) ;baba Actor
				(= seconds 2)
			)
			(6
				(ego view: (GetEgoViewNumber vEgoFrogTransform) loop: 2 cel: 0 setCycle: EndLoop)
				((ScriptID 21 id_TELEPORT) play:) ;teleport Sound
				((ScriptID 21 id_BABAHEAD) setCycle: NULL hide:) ;babaHead Prop
				(= cycles 15)
			)
			(7
				((ScriptID 21 id_SPIDER) setCycle: NULL) ;spider Prop
				((ScriptID 21 id_BABA) ;baba Actor
					setLoop: 8
					setCycle: Forward
					setMotion: Chase ego 20 self
				)
			)
			(8
				((ScriptID 21 id_BABA) setCycle: NULL)
				((ScriptID 21 id_BABAHEAD)
					posn: ((ScriptID 21 id_BABA) x?) (- ((ScriptID 21 id_BABA) y?) 34)
					setPri: (+ ((ScriptID 21 id_BABA) priority?) 1)
					setCycle: Forward
					show:
				)
				(= cycles 2)
			)
			(9
				(= babaTalking TRUE)
				(TimedPrint 7 294 2)
				;"This I vow: Stay there now!"
				(= seconds 4)
			)
			(10
				(= babaTalking FALSE)
				((ScriptID 21 id_BABAHEAD) setCycle: NULL)
				((ScriptID 21 id_BABA) setLoop: 6 cel: 0 setCycle: EndLoop)
				(= seconds 3)
			)
			(11
				(= babaTalking TRUE)
				(TimedPrint 8 294 3)
				;Once again you're a frog, and once again you can't move.  You find it very exasperating!
				(= seconds 8)
			)
			(12		;this is the start of the first question Baba Yaga asks you.
				((ScriptID 21 id_BABAHEAD) setCycle: Forward)
				(TimedPrint 6 294 4)
				;"Yum, Yum.  Froggie Frappe!"
				(= seconds 5)
			)
			(13 
				((ScriptID 21 id_BABAHEAD) setCycle: NULL)
				((ScriptID 21 id_BAT) setLoop: 5 setCycle: Forward)
				((ScriptID 21 id_SPIDER) setCycle: Forward)
				(= seconds 4)
			)
			(14
				((ScriptID 21 id_BAT) setCycle: NULL)
				((ScriptID 21 id_SPIDER) setCycle: NULL)
				((ScriptID 21 id_BABAHEAD) setCycle: Forward)
				(TimedPrint 8 294 5)
				;"Did you bring me my mandrake like you promised?"
				(= seconds 5)
			)
			(15
				(= seconds 0) ;we pause this Script, until it is cue'd from outside.
				(= babaTalking FALSE)
				(= babaYagaStatus babaBRING)
				((ScriptID 21 id_BABAHEAD) setCycle: NULL)
				((ScriptID 21 id_BABA) setScript: (ScriptID 21 id_BABATALK))
				(= timerYesNo WAIT_BABA)
				;allow user to answer the witch.
				(User canInput: TRUE)
				;set the normal cursor, to indicate the user can respond.
				(theGame setCursor: normalCursor (HaveMouse))
			)
			(16
				(= babaTalking TRUE)
				(Print 294 6
					#at -1 130
					#width 250
					#mode teJustCenter
					#dispose
					#time 8)
				(= seconds 8)
				; As you make a feeble croaking sound, you try to nod your head.
			)
			(17
				((ScriptID 21 id_BABAHEAD) setCycle: Forward)
				(TimedPrint 5 294 7)
				;"Well?  Where is it?"
				(= seconds 3)
			)
			(18
				((ScriptID 21 id_BABAHEAD) setCycle: NULL)
				(= seconds 5)
			)
			(19
				((ScriptID 21 id_BABAHEAD) setLoop: 5 setCycle: Forward)
				(TimedPrint 7 294 8)
				;"What's the matter?  Got a frog in your throat?"
				(= seconds 5)
			)
			(20
				((ScriptID 21 id_BAT) setCycle: Forward)
				((ScriptID 21 id_SPIDER) setCycle: Forward)
				((ScriptID 21 id_BABAHEAD) setCycle: NULL)
				(= seconds 3)
			)
			(21
				((ScriptID 21 id_BAT) setCycle: NULL)
				((ScriptID 21 id_SPIDER) setCycle: NULL)
				((ScriptID 21 id_BABAHEAD) setLoop: 4 setCycle: Forward)
				(TimedPrint 14 294 9)
				;"I suppose I'll have to turn you back into whatever it is that you were.  Pity!  You're much more appetizing this way."
				(= seconds 12)
			)
			(22
				((ScriptID 21 id_BABAHEAD) setCycle: NULL)
				(= seconds 2)
			)
			(23
				((ScriptID 21 id_BABAHEAD) setCycle: Forward)
				(Print 294 10
					#at -1 12
					#width 160
					#mode teJustCenter
					#dispose
					#time 12)
				(= seconds 9)
				; "Creatures of Bog
				; And Spirits of Fog:
				; Return the true form
				; To this rather dumb Frog."
			)
			(24
				(= babaTalking FALSE)
				((ScriptID 21 id_BABAHEAD) setCycle: NULL)
				((ScriptID 21 id_BABA) setCycle: EndLoop)
				(= seconds 3)
			)
			(25
				((ScriptID 21 id_TELEPORT) play:)
				(ego setCycle: BegLoop self)
			)
			(26
				(StopEgo)
				(ego loop: loopE)
				((ScriptID 21 id_BABAHEAD) setCycle: Forward)
				(= babaTalking TRUE)
				(TimedPrint 6 294 11)
				;"Now, did you put it in your backpack?"
				(= seconds 5)
			)
			(27
				(= babaTalking FALSE)
				((ScriptID 21 id_BABAHEAD) hide:)
				((ScriptID 21 id_BABA) setMotion: Chase ego 10)
				(= cycles 5)
			)
			(28
				(if (ego has: iMandrake)
					((ScriptID 21 id_BABA) setPri: 9)
					((ScriptID 21 id_BABAHEAD)
						posn: ((ScriptID 21 id_BABA) x?) (- ((ScriptID 21 id_BABA) y?) 34)
						setPri: 10
						setCycle: Forward
						show:
					)
					(= babaTalking TRUE)
					(SolvePuzzle POINTS_GIVEMANDRAKEROOT 3)
					(TimedPrint 5 294 12)
					;"Ah!  Here it is."
					(ego use: iMandrake)
					(Bclr BABAYAGA_CURSE)
					(= seconds 5)
				else
					((ScriptID 21 id_RM21) setScript: (ScriptID 21 id_LIEDEATH)) ;rm21 Room setScript noBringDeath (which will kill the hero).
				)
			)
			(29
				((ScriptID 21 id_BAT) setCycle: Forward)
				((ScriptID 21 id_SPIDER) setCycle: Forward)
				((ScriptID 21 id_BABAHEAD) setLoop: 5)
				(TimedPrint 5 294 13)
				;"Kids!  We have it!"
				(= seconds 4)
			)
			(30
				(= babaTalking FALSE)
				((ScriptID 21 id_BABAHEAD) setCycle: NULL)
				(= seconds 2)
			)
			(31
				((ScriptID 21 id_BAT) setCycle: NULL)
				((ScriptID 21 id_SPIDER) setCycle: NULL)
				((ScriptID 21 id_BABAHEAD) hide:)
				((ScriptID 21 id_BABA)
					setPri: RELEASE
					setMotion:
						MoveTo
						(+ ((ScriptID 21 id_BABA) x?) 10)
						(- ((ScriptID 21 id_BABA) y?) 5)
						self
				)
			)
			(32
				((ScriptID 21 id_BABAHEAD)
					posn: ((ScriptID 21 id_BABA) x?) (- ((ScriptID 21 id_BABA) y?) 34)
					setPri: (+ ((ScriptID 21 id_BABA) priority?) 1)
					setLoop: 4
					setCycle: Forward
					show:
				)
				(= babaTalking TRUE)
				(TimedPrint 10 294 14)
				;"That's it!  The final ingredient.  Now we can make our greatest creation..."
				(= seconds 10)
			)
			(33
				((ScriptID 21 id_BAT) setCycle: Forward)
				((ScriptID 21 id_SPIDER) setCycle: Forward)
				((ScriptID 21 id_BABAHEAD) setLoop: 5)
				(TimedPrint 4 294 15)
				;"MANDRAKE MOUSSE!"
				(= seconds 4)
			)
			(34
				((ScriptID 21 id_BABAHEAD) setCycle: NULL)
				(= seconds 3)
			)
			(35
				((ScriptID 21 id_BABAHEAD) setCycle: Forward)
				(TimedPrint 10 294 16)
				;"What's that, children?  You think we should reward our lackey here?"
				(= seconds 12)
			)
			(36
				((ScriptID 21 id_BABAHEAD) setLoop: 4)
				(TimedPrint 10 294 17)
				;"Very well, ex-frog.  I'll let you live this time.  Next time, though, it's frog legs for sure!"
				(= seconds 8)
			)
			(37
				((ScriptID 21 id_BABAHEAD) setCycle: NULL hide:)
				(= seconds 2)
			)
			(38
				(= babaTalking FALSE)
				((ScriptID 21 id_BABA) setLoop: 6 cel: 0 setCycle: EndLoop self)
				((ScriptID 21 id_BABAHEAD)
					setLoop: 4
					posn: ((ScriptID 21 id_BABA) x?) (- ((ScriptID 21 id_BABA) y?) 34)
					setPri: (+ ((ScriptID 21 id_BABA) priority?) 1)
					show:
					setCycle: Forward
				)
				(TimedPrint 4 294 18)
				;"So...Go!"
			)
			(39
				((ScriptID 21 id_BABAHEAD) hide:)
				((ScriptID 21 id_TP)
					posn: (ego x?) (ego y?)
					setPri: 15
					init:
					setCycle: CycleTo 6 cdFORWARD self
				)
				((ScriptID 21 id_TELEPORT) play:)
			)
			(40
				(ego hide:)
				((ScriptID 21 id_TP) setCycle: EndLoop)
				(= cycles 15)
			)
			(41
				(client setScript: NULL)
				(curRoom newRoom: 22)
			)
		)
	)
)

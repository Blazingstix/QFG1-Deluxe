;;; Sierra Script 1.0 - (do not remove this comment)
(script# 293)
(include system.sh) (include sci2.sh) (include game.sh)
(use Main)
(use _Interface)
(use _Motion)
(use _User)
(use _System)

(public
	firstTalk 0
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
)

(local
	babaTalking
)
(instance firstTalk of Script
	(properties)
	
	(method (dispose)
		(super dispose:)
		(DisposeScript 293)
	)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				((ScriptID 21 id_BABA) ;baba Actor
					setLoop: 0
					setCycle: Forward
					setMotion: MoveTo 119 136 self
				)
			)
			(1
				((ScriptID 21 id_BABA) ;baba Actor
					setLoop: 1 setCycle: NULL cel: 0)
				(ego
					view: vBabaYaga1
					setLoop: 2
					cel: 0
					cycleSpeed: 1
					posn: (- ((ScriptID 21 id_BABA) x?) 13) (- ((ScriptID 21 id_BABA) y?) 25)
					setPri: (+ ((ScriptID 21 id_BABA) priority?) 1)
					show:
				)
				((ScriptID 21 id_SPIDER) setCycle: Forward) ;spider Prop
				((ScriptID 21 id_BAT) setCycle: Forward) ;bat Prop
				(= seconds 3)
			)
			(2
				(= babaTalking TRUE)
				(TimedPrint 4 293 0)
				;"BE STILL!"
				((ScriptID 21 id_BABA) setCel: 1)
				((ScriptID 21 id_BABAHEAD)
					setLoop: 5
					posn: ((ScriptID 21 id_BABA) x?) (- ((ScriptID 21 id_BABA) y?) 34)
					setPri: (+ ((ScriptID 21 id_BABA) priority?) 1)
					setCycle: Forward
					show:
				)
				(= seconds 2)
			)
			(3	;Starting point for nameDeath Retry point.
				((ScriptID 21 id_SPIDER) setCycle: NULL)
				((ScriptID 21 id_BAT) setLoop: 1 cel: 4 setCycle: BegLoop)
				(= seconds 2)
			)
			(4
				(TimedPrint 6 293 1)
				;"Critics!  We can't all be gourmands, I suppose."
				((ScriptID 21 id_BABAHEAD) setLoop: 4)
				(= seconds 4)
			)
			(5
				((ScriptID 21 id_BABAHEAD) setCycle: NULL)
				(= seconds 2)
			)
			(6
				(= timerYesNo 0)
				((ScriptID 21 id_BABA) setCel: 0)
				((ScriptID 21 id_BABAHEAD) setLoop: 3 setCycle: Forward)
				;(Print ((ScriptID 21 id_RM21) script?))
				(TimedPrint 7 293 2)
				;Now, Oh-Soon-To-Be-Supper... I don't suppose you have a name?"
				(= seconds 5)
			)
			(7
				;NOTE: This is where the script pauses to allow the nameDeath to possibly run.
				(= seconds 0) ;pause the script until it is cue'd from outside (specifically from the babaTalk Script)
				(= babaTalking FALSE)
				((ScriptID 21 id_BABAHEAD) setCycle: NULL)
				((ScriptID 21 id_BABA) setScript: (ScriptID 21 id_BABATALK))
				(= timerYesNo WAIT_BABA)
				;allow the user to respond to the witch
				(User canInput: TRUE)
				;set the normal cursor, to indicate the user can respond.
				(theGame setCursor: normalCursor (HaveMouse))
			)
			(8
				(ego setCycle: Forward)
				(= babaTalking TRUE)
				(Print 293 3
					#at -1 130
					#width 250
					#mode teJustCenter
					#dispose
					#time 8)
				(= seconds 8)
				; You try your best to croak out your name, or at least let the witch know that you do indeed have one.
			)
			(9
				(ego setCycle: NULL)
				((ScriptID 21 id_BABAHEAD) setCycle: Forward)
				(= babaTalking TRUE)
				(TimedPrint 9 293 4)
				;"So you're the one who's trying to be a hero around here.  The only good hero's a dead hero, I always say!"
				(= seconds 7)
			)
			(10 	;starting point for braveDeath Retry point.
				((ScriptID 21 id_BABAHEAD) setCycle: NULL)
				(= seconds 2)
			)
			(11
				(TimedPrint 6 293 5)
				;"But I do have a need for a brave fool.  Are you brave?"
				(= babaYagaStatus babaBRAVE)
				((ScriptID 21 id_BABAHEAD) setCycle: Forward)
				(= seconds 4)
			)
			(12
				;NOTE: This is where the script pauses to allow the braveDeath script to possibly run.
				(= seconds 0) ;pause this Script at this state, until the user says or does something, which will advance the script
				(= babaTalking FALSE)
				((ScriptID 21 id_BABAHEAD) setCycle: NULL)
				((ScriptID 21 id_BABA) setScript: (ScriptID 21 id_BABATALK))
				(= timerYesNo WAIT_BABA)
				;allow user to answer the witch.
				(User canInput: TRUE)
				;set the normal cursor, to indicate the user can respond.
				(theGame setCursor: normalCursor (HaveMouse))
			)
			(13
				(ego setCycle: Forward)
				(= babaTalking TRUE)
				(Print 293 6
					#at -1 130
					#width 250
					#mode teJustCenter
					#dispose
					#time 7)
				(= seconds 7)
				; You make little froggy sounds, trying your best to indicate your agreement.
			)
			(14
				(ego setCycle: NULL)
				(TimedPrint 2 293 7)
				;"Wellll..."
				(= seconds 2)
			)
			(15
				((ScriptID 21 id_BABAHEAD) setCycle: Forward)
				(TimedPrint 8 293 8)
				;"If you're willing to do a small little teensy favor for me, I might reconsider having you for supper."
				(= seconds 6)
			)
			(16
				((ScriptID 21 id_BABAHEAD) setCycle: NULL)
				(= seconds 2)
			)
			(17
				((ScriptID 21 id_BABAHEAD) setCycle: Forward)
				(TimedPrint 7 293 9)
				;"I need the root of a mandrake plant that grows in the graveyard.  Will you be a sweet and fetch me some?"
				;
				(= seconds 8)
			)
			(18
				;NOTE: This is where the script pauses to run the noFetchDeath, possibly.
				(= seconds 0) ;pause this Script until ... ?
				(= babaTalking FALSE)
				(= babaYagaStatus babaFETCH)
				((ScriptID 21 id_BABAHEAD) setCycle: NULL)
				((ScriptID 21 id_BABA) setScript: (ScriptID 21 id_BABATALK))
				(= timerYesNo WAIT_BABA)
				;allow the user to answer the witch
				(User canInput: TRUE)
				;set the normal cursor, to indicate the user can respond.
				(theGame setCursor: normalCursor (HaveMouse))
			)
			(19
				(ego setCycle: Forward)
				(= babaTalking TRUE)
				(Print 293 10
					#at -1 130
					#width 250
					#mode teJustCenter
					#dispose
					#time 7)
				(= seconds 7)
				; "Anything but Frog Legs Fricassee!", you think.   You croak your agreement to the task.
			)
			(20
				(ego setCycle: NULL)
				((ScriptID 21 id_SPIDER) setCycle: Forward)
				((ScriptID 21 id_BAT) setLoop: 5 setCycle: Forward)
				((ScriptID 21 id_BABA) setCel: 1)
				((ScriptID 21 id_BABAHEAD) setLoop: 4 setCycle: Forward)
				(TimedPrint 6 293 11)
				;"And I had my mouth watering for frog.  Oh well!"
				(= seconds 4)
			)
			(21
				((ScriptID 21 id_BABA) setCel: 0)
				((ScriptID 21 id_BABAHEAD) setCycle: NULL setLoop: 3)
				(= seconds 3)
			)
			(22
				((ScriptID 21 id_SPIDER) setCycle: NULL)
				((ScriptID 21 id_BAT) setCycle: NULL)
				((ScriptID 21 id_BABAHEAD) setCycle: Forward)
				(Print 293 12
					#at -1 12
					#width 160
					#mode teJustCenter
					#dispose
					#time 9)
				(= seconds 9)
				; "Hear what I say
				; And hear me right:
				; Mandrake must be pulled
				; At precisely Midnight!"
			)
			(23
				(Print 293 13
					#at -1 12
					#width 160
					#mode teJustCenter
					#dispose
					#time 9)
				(= seconds 9)
				; "This I tell you
				; And this I say:
				; Return with the root
				; Ere the break of next day."
			)
			(24
				(Print 293 14
					#at -1 12
					#width 145
					#mode teJustCenter
					#dispose
					#time 9)
				(= seconds 9)
				; "Hear what I say
				; And know I don't lie:
				; Bring back the root
				; Or else you will die!!"
			)
			(25
				(= babaTalking FALSE)
				((ScriptID 21 id_BABAHEAD) hide:)
				(ego hide:)
				((ScriptID 21 id_BABA)
					setLoop: 0
					setCycle: BegLoop
					setMotion: MoveTo 140 136 self
				)
			)
			(26
				((ScriptID 21 id_BABA)
					setLoop: 7
					cel: 5
					setCycle: CycleTo 3 cdBACKWARD self
				)
			)
			(27
				(ego
					view: (GetEgoViewNumber vEgoFrogTransform)
					setLoop: 3
					cel: 7
					posn: (- ((ScriptID 21 id_BABA) x?) 17) (+ ((ScriptID 21 id_BABA) y?) 4)
					show:
				)
				((ScriptID 21 id_BABA) setCycle: BegLoop self)
			)
			(28
				((ScriptID 21 id_SPIDER) setCycle: Forward)
				((ScriptID 21 id_BAT) setCycle: Forward)
				((ScriptID 21 id_BABA) setLoop: 6 cel: 0 setCycle: EndLoop self)
				((ScriptID 21 id_BABAHEAD)
					setLoop: 4
					posn: ((ScriptID 21 id_BABA) x?) (- ((ScriptID 21 id_BABA) y?) 34)
					setPri: (+ ((ScriptID 21 id_BABA) priority?) 1)
					show:
					setCycle: Forward
				)
				(TimedPrint 4 293 15)
				;"You, Shoo!"
			)
			(29
				((ScriptID 21 id_BABAHEAD) hide:)
				((ScriptID 21 id_TP)
					posn: (ego x?) (ego y?)
					setPri: 15
					init:
					setCycle: CycleTo 5 cdFORWARD self
				)
				((ScriptID 21 id_TELEPORT) play:)
			)
			(30
				(ego hide:)
				((ScriptID 21 id_TP) setCycle: EndLoop)
				(= cycles 10)
			)
			(31 
				(curRoom newRoom: 22)
			)
		)
	)
)

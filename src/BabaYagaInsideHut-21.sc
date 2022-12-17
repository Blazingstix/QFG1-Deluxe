;;; Sierra Script 1.0 - (do not remove this comment)
(script# 21)
(include system.sh) (include sci2.sh) (include game.sh)
(use Main)
(use _Interface)
(use InputComparer)
(use _LoadMany)
(use _DCIcon)
(use _Chase)
(use _GControl)
(use _Sound)
(use _Motion)
(use _Game)
(use _User)
(use _Actor)
(use _System)

(public
	rm21 0
	baba 1
	babaHead 2
	bat 3
	spider 4
	TP 5
	noBringDeath 6
	babaTalk 7
	teleport 8
	endGame 9
	noMandrakeDeath 10
)

(local
	babasHere
	babaTalking
)

(procedure (BabaYagaDeath)
	;reset the user input to false
	(User canInput: FALSE)
	;reset the cursor to the inactrion cursor
	(theGame setCursor: waitCursor TRUE)
	(switch babaYagaStatus
		(babaNAME (rm21 setScript: nameDeath))
		(babaBRAVE (rm21 setScript: braveDeath))
		(babaFETCH (rm21 setScript: noFetchDeath))
		(babaBRING (rm21 setScript: noBringDeath))
	)
)

(procedure (BringOutBabaYaga event)
	(if (not babasHere)
		(= babasHere TRUE)
		(cond 
			((ego has: iMagicMirror)				
				;(= babaYagaStatus babaFINALE) 
				(rm21 setScript: lastWitch)
			)
			((not (Btst VISITED_BABAYAGA_INTERIOR))								  
				(rm21 setScript: firstWitch)
			)
			((== babaYagaStatus babaFETCH)			
				(= babaYagaStatus babaBRING)  
				(rm21 setScript: (ScriptID 294 0)) ;nextWitch
			) 
			((== babaYagaStatus babaBRING) 			
				;(= babaYagaStatus babaFINALE) 
				(rm21 setScript: lastWitch)
			)
		)
	else
		(event claimed: TRUE)
	)
)

(instance myIcon of DCIcon
	(properties
		view vBabaYaga1
		loop 9
		cycleSpeed 8
	)
)

(instance teleport of Sound
	(properties
		priority 15
	)
)

(instance bubbleMusic of Sound
	(properties
		number 51
		priority 5
		loop -1
	)
)

(instance reflection of Prop
	(properties
		view vEgoHoldingMirror ;CI:TODO: Create new separate view for mirror reflection
		loop 1
		cycleSpeed 1
	)
)

(instance babaHead of Prop
	(properties
		view vBabaYaga1
		loop 4
		cycleSpeed 1
	)
)

(instance spider of Prop
	(properties
		y 89
		x 245
		view vBabaYaga2
		cycleSpeed 1
	)
)

(instance bat of Prop
	(properties
		y 51
		x 205
		view vBabaYaga2
		loop 1
		cycleSpeed 1
	)
)

(instance TP of Prop
	(properties
		view vTeleportGreen
		cycleSpeed 1
	)
)

(instance cauldron of Prop
	(properties
		y 130
		x 86
		view vBabaYaga2
		loop 2
	)
)

(instance fire of Prop
	(properties
		y 133
		x 86
		view vBabaYaga2
		loop 3
	)
)

(instance blanket of View
	(properties
		y 151
		x 212
		view vBabaYaga2
		loop 4
	)
)

(instance baba of Actor
	(properties
		yStep 1
		view vBabaYaga1
		cycleSpeed 2
		xStep 2
	)
	
	(method (handleEvent event)
		(cond 
			((super handleEvent: event))
			((or    (MouseClaimed self event emRIGHT_BUTTON)
					(Said 'look/baba,ogress')
				)
				(HighPrint 21 0)
				;That's one mean Ogress!
			)
			((Said 'ask,chat/baba,ogress')
				(HighPrint 21 1)
				;There's no time for idle chatter.
			)
		)
	)
)

(instance rm21 of Room
	(properties
		picture 21
		style VSHUTTER
	)
	
	(method (init)
		;CI: set the debug mode on as we enter the room automatically.
		;(= razzleDazzleRootBeer TRUE)
		(LoadMany RES_VIEW vBabaYaga1 vBabaYaga2 vFrogTransform (GetEgoViewNumber vEgoFrogTransform) vTeleportGreen (GetEgoViewNumber vEgoHoldingMirror))
		(LoadMany RES_SOUND 51 (GetSongNumber 28))
		(LoadMany RES_SCRIPT DCICON 293 294)
		(super init:)
		(SolvePuzzle POINTS_ENTERBABAYAGAHUT 2)
		(if (ego has: iMandrake) (Bclr BABAYAGA_CURSE))
		(cSound fade:)
		(teleport number: (GetSongNumber 28) init:)
		(keyDownHandler add: self)
		(mouseDownHandler addToFront: self)
		(directionHandler add: self)
		(StatusLine enable:)
		(Bclr BABAYAGA_HUT_OPEN)
		(bubbleMusic init: play:)
		(StopEgo)
		(= timerYesNo 0)
		(ego loop: loopW posn: 202 165 init:)
		(spider setCycle: Forward init: stopUpd:)
		(bat init:)
		(cauldron setPri: 10 setCycle: Forward init:)
		(fire setCycle: Forward init:)
		(blanket setPri: 2 ignoreActors: init: stopUpd: addToPic:)
	)
	
	(method (doit)
		;CI: for debugging, we'll change the debugValue and update the StatusLine, so we can see how it changes.
		(= debugValue timerYesNo)
		;CI: even though StatusLine doit: is called in the main game doit, it doesn't appear to run as frequently as here
		;so to get a higher resolution of how often the debugValue is changing, we'll also update the status line here.
		(StatusLine doit:)
		(if (> timerYesNo 1)
			;Baba's waiting for a response. decrement the counter
			(-- timerYesNo)
		)
		(if (== timerYesNo 1)
			;Baba's run out of patience. Time to die.
			(= timerYesNo 0)
			(BabaYagaDeath)
		)
		(super doit:)
	)
	
	(method (dispose)
		(Bset VISITED_BABAYAGA_INTERIOR)
		(myIcon dispose:)
		(super dispose:)
	)
	
	(method (handleEvent event)
		(cond 
			((super handleEvent: event))
			((or (Said 'look/bat') (MouseClaimed bat event emRIGHT_BUTTON))
				(HighPrint 21 2)
				;There is a bat, all folded up within his wings, hanging on the wall.
			)
			((or (Said 'look/spider') (MouseClaimed spider event emRIGHT_BUTTON))
				(HighPrint 21 3)
				;Is that a SMILE on that spider's face?
			)
			((or (Said 'look/web') (MouseClaimed web event emRIGHT_BUTTON))
				(HighPrint 21 4)
				;You HATE houses with cobwebs in the corners, especially when there's an immense spider squatting cheerily in the middle of the cobweb.
			)
			((or (Said 'look/window') (MouseClaimed hutWindow event emRIGHT_BUTTON))
				(HighPrint 21 5)
				;The glass seems to distort the view of the outside world.
			)
			((or (Said 'look/caldron,pan') (MouseClaimed cauldron event emRIGHT_BUTTON))
				(HighPrint 21 6)
				;There's a lot of SOMETHING cooking away in the huge black cauldron.
				(HighPrint 21 7)
				;The smell is awful, the color is sickening, and you don't even want to know what's being prepared for dinner.
			)
			((or (Said 'look/fire') (MouseClaimed fire event emRIGHT_BUTTON))
				(HighPrint 21 8)
				;The fire burns hot under the kettle.
			)
			((or (Said 'look/chimney') (MouseClaimed chimney event emRIGHT_BUTTON))
				(HighPrint 21 9)
				;The stone hearth and chimney radiates too much heat.
			)
			((or (Said 'look/cabinet') (MouseClaimed cupboard event emRIGHT_BUTTON))
				(HighPrint 21 10)
				;It's a plain wooden kitchen cupboard.
			)
			((or (Said 'look/dagger,implement') (MouseClaimed knives event emRIGHT_BUTTON))
				(HighPrint 21 11)
				;The kitchen knives look old but sharp.
			)
			((or (Said 'look/hay,bed,blanket') (MouseClaimed blanket event emRIGHT_BUTTON))
				(HighPrint 21 12)
				;The pile of straw covered by a blanket must be the occupant's bed.
			)
			((== (event type?) mouseDown)
				;if ego *left* clicks anywhere, Baba Yaga makes her appearance
				(if (not (& (event modifiers?) shiftDown))
					(BringOutBabaYaga event)
				)
			)
			((== (event type?) direction)
				;if ego tries to move anywhere, Baba Yaga makes her appearance
				(BringOutBabaYaga event)
			)
			((== (event type?) evSAID)
				(cond 
					((or (Said '[get,use,display,hold,prepare]/mirror[<magic]') (Said 'reflect[/spell]'))
						(if (ego has: iMagicMirror)
							(HighPrint 21 13)
							;You furtively grasp the Magic Mirror.
							;we set the flag that says we've now used the mirror against baba yaga.
							(Bset fBabaFrog)
						else
							(PrintDontHaveIt)
						)
					)
					((Said 'get')
						(HighPrint 21 14)
						;You don't seem to be able to move.
						
						;if we try to pick up anything in her hut, Baba Yaga will appear.
						(BringOutBabaYaga event)
					)
					((Said 'chat/baba')
						(HighPrint 21 15)
						;You cannot see the witch anywhere.
						)
					((Said 'chat')
						(HighPrint 21 16)
						;You might as well be talking to yourself.
						)
					((Said 'cast')
						(HighPrint 21 17)
						;Your magic is useless here.
						)
					((Said 'look>')
						(cond 
							((Said '[<at,around][/hut,house,room]')
								(HighPrint 21 18)
								;A quick look around shows that this is a creepy place, despite the pot (cauldron?) simmering briskly on the fire.
								;Look at the size of that spider!
							)
							((Said '/baba,ogress')
								(HighPrint 21 15)
								;You cannot see the witch anywhere.
							)
							((or (Said '<up') (Said '/ceiling,roof'))
								(HighPrint 21 19)
								;The small hut has a beamed ceiling.
							)
							((or (Said '<down') (Said '/floor'))
								(HighPrint 21 20)
								;The floor is tidy, with a pile of straw in one corner.
							)
						)
					)
				)
			)
		)
		(super handleEvent: event)
	)
)

;This script handles when Baba Yaga is waiting for a response.
;if a positive response is given, the appropriate script will be cue'd
;forward. Otherwise, the Death Script will be initiated.
(instance babaTalk of Script
	(properties)
	
	(method (init)
		(super init: &rest)
		(keyDownHandler add: self)
	)
	
	(method (dispose)
		(keyDownHandler delete: self)
		(super dispose:)
	)
	
	(method (handleEvent event)
		(cond 
			;first we let the general Baba Yaga handler handle things
			;this should only encompass things like "Look at Ogress" or "chat Ogress"
			((super handleEvent: event))
			;next, if we're on babaNAME, and she's waiting to hear a response (i.e. she's asked your name)
			((and   (> timerYesNo 0)
					(== babaYagaStatus babaNAME)
					(EventEqualsString event @userName)
				)
				(= timerYesNo 0)
				(baba setScript: NULL)
				;turn off the input ability again.
				(User canInput: FALSE)
				;reset the cursor to the inactrion cursor
				(theGame setCursor: waitCursor TRUE)
				((ScriptID 293 0) cue:) ;firstTalk Script
			)
			;otherwise, we're waiting to hear a yes/no response. 
			;Yes will cue the script (if babaFETCH, then script FirstTalk, if babaBRING then nextWitch script)
			;No (or anything else) will start the Death Script.
			((and   (== (event type?) evSAID)
					(> timerYesNo 1)
				)
				(cond 
					((Said 'affirmative,please')
						(baba setScript: NULL)
						(= timerYesNo 0)
						(cond 
							((<= babaYagaStatus babaFETCH)
								(User canInput: FALSE)
								;reset the cursor to the inactrion cursor
								(theGame setCursor: waitCursor TRUE)
								((ScriptID 293 0) cue:) ;firstTalk Script
							)
							((== babaYagaStatus babaBRING)
								(User canInput: FALSE)
								;reset the cursor to the inactrion cursor
								(theGame setCursor: waitCursor TRUE)
								((ScriptID 294 0) cue:) ;nextWitch Script
							)
						)
					)
					((Said 'n')
						(BabaYagaDeath)
					)
					(else 
						(event claimed: TRUE)
						(BabaYagaDeath)
					)
				)
			)
		)
	)
)

(instance firstWitch of Script
	(properties)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(HandsOff)
				(ego setMotion: MoveTo 143 152 self)
			)
			(1
				(baba posn: 188 125 setLoop: 6 init:)
				(babaHead
					setLoop: 5
					setPri: 15
					posn: 188 91
					init:
					setCycle: Forward
				)
				(teleport play:)
				(= cycles 5)
			)
			(2
				(ego loop: loopE)
				(bat setCycle: EndLoop)
				(spider startUpd:)
				(= babaTalking TRUE)
				(TimedPrint 6 21 21)
				;"Look pets...we have a visitor!"
				(= seconds 5)
			)
			(3
				(bat setLoop: 5 setCycle: EndLoop)
				(= babaTalking FALSE)
				(babaHead setCycle: NULL hide:)
				(= cycles 10)
			)
			(4
				(babaHead setLoop: 4 setCycle: Forward show:)
				(spider setCycle: NULL)
				(= babaTalking TRUE)
				(Print 21 22 #at -1 8 #width 125 #mode teJustCenter #dispose #time 8)
				(= seconds 6)
				; "Powers of Night,
				; Shadows of Day
				; Heed now my Words!
				; Henceforth you STAY!"
			)
			(5
				(= babaTalking FALSE)
				(baba setLoop: 6 setCycle: EndLoop)
				(= seconds 2)
			)
			(6
				(TimedPrint 7 21 23)
				;Your body is frozen by the power of the witch's spell.
				(babaHead setCycle: NULL hide:)
				(= cycles 15)
			)
			(7
				(baba
					setLoop: 8
					setCycle: Forward
					setMotion: Chase ego 20 self
				)
			)
			(8
				(baba setCycle: NULL)
				(babaHead
					setLoop: 5
					setCycle: Forward
					posn: (baba x?) (- (baba y?) 34)
					setPri: (+ (baba priority?) 1)
					show:
				)
				(= cycles 2)
			)
			(9
				(= babaTalking TRUE)
				(TimedPrint 6 21 24)
				;"Well Dearies!  What shall we have for supper today?"
				(= seconds 4)
			)
			(10
				(babaHead setCycle: NULL)
				(spider setCycle: Forward)
				(bat cycleSpeed: 2 setCycle: Forward)
				(= seconds 4)
			)
			(11
				(babaHead setLoop: 4 setCycle: Forward)
				(bat setCycle: NULL)
				(spider setCycle: NULL)
				(TimedPrint 7 21 25)
				;"Hero sandwiches?
				;I had something more formal in mind."
				(= seconds 7)
			)
			(12
				(TimedPrint 6 21 26)
				;"Ah...that's it!
				;Frog Legs Fricassee."
				(spider setCycle: Forward)
				(bat setCycle: Forward)
				(= seconds 6)
			)
			(13
				(babaHead setLoop: 5)
				(TimedPrint 6 21 27)
				;"Now how does that spell go.....?"
				(bat setCycle: NULL)
				(spider setCycle: NULL)
				(= seconds 6)
			)
			(14
				(babaHead setLoop: 4)
				(Print 21 28
					#at -1 12
					#width 125
					#mode teJustCenter
					#dispose
					#time 8
				)
				(= seconds 6)
				; "Hear me, oh Powers
				; Of Klatha and Mana!
				; Turn now my guest
				; Into species called Rana!"
			)
			(15
				(= babaTalking FALSE)
				(babaHead stopUpd: hide:)
				(baba setLoop: 6 setCycle: EndLoop)
				(= cycles 12)
			)
			(16
				(ego view: (GetEgoViewNumber vEgoFrogTransform) loop: 2 cel: 0 setCycle: EndLoop)
				(teleport play:)
				(= cycles 10)
			)
			(17
				(baba setLoop: 8 setMotion: MoveTo 160 148 self)
			)
			(18
				(baba view: vBabaYaga1 setLoop: 7 setCycle: CycleTo 3 cdFORWARD self)
			)
			(19
				;baba yaga picks up the frog hero
				(ego ignoreActors: hide:)
				(baba setCycle: EndLoop self)
			)
			(20
				(babaHead
					setLoop: 5
					posn: (- (baba x?) 1) (- (baba y?) 32)
					setPri: (+ (baba priority?) 1)
					startUpd:
					show:
				)
				(spider setCycle: Forward)
				(bat setCycle: Forward)
				(= babaTalking TRUE)
				(TimedPrint 8 21 29)
				;"I learned that spell from Erasmus, kids. Doesn't it look delicious?"
				(= seconds 8)
			)
			(21
				(= babaTalking FALSE)
				(spider setCycle: NULL)
				(bat setCycle: NULL)
				(babaHead stopUpd: hide:)
				(rm21 setScript: (ScriptID 293 0)) ;set firstTalk Script
			)
		)
	)
)

;CI: NOTE: only gets called from the BabaYagaDeath procedure. So, only one scenario to consider reversing on a Retry.
(instance nameDeath of Script
	(properties)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(baba setScript: NULL)
				(= timerYesNo 0)
				(babaHead setCycle: Forward)
				(TimedPrint 3 21 30)
				;"Stubborn, aren't you?"
				(= seconds 3)
			)
			(1
				(TimedPrint 5 21 31)
				;"Well, you won't be so tough after you've simmered in a little wine sauce."
				(spider setCycle: Forward)
				(bat setCycle: Forward)
				(= seconds 5)
			)
			(2
				(baba setCel: 1)
				(babaHead setLoop: 5)
				(TimedPrint 4 21 32)
				;"Bon appetit, Dearies!"
				(= seconds 4)
			)
			(3
				;(babaHead hide:)
				(EgoDead DIE_RETRY DIE_BABA_NONAME 21 33
					#title { Now you're really in the soup!_}
					#icon myIcon 0 0
				)
				;Next time you are asked a question by someone who has just turned you into a frog, perhaps you should be more polite.
				;It's better than being fricasseed!
				
				;redraw the screen
				;(DrawPic 400 RETRY_STYLE dpCLEAR)
				(= cycles 1)
			)
			(4
				;(DrawPic 21 RETRY_STYLE dpCLEAR)
				;re-add the blanket to the picture
				;(blanket setPri: pGREEN ignoreActors: init: stopUpd: addToPic:)
				(babaHead setCycle: NULL)
				(bubbleMusic init: play:)
				;reset the script to state 5, then cue it to step6
				;(Print (rm21 script?))
				(rm21 setScript:(ScriptID 293 0))
				(baba setLoop: 1 setCel: 0 setCycle: NULL setMotion: NULL)
				(babaHead setLoop: 3)
				((rm21 script?) changeState: 3 cue:)
				;reset the cursor to the inactrion cursor
				(theGame setCursor: waitCursor TRUE)
			)
		)
	)
)

;CI: NOTE: only gets called from the BabaYagaDeath procedure. So, only one scenario to consider reversing on a Retry.
(instance braveDeath of Script
	(properties)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(baba setScript: NULL)
				(= timerYesNo 0)
				(babaHead setCycle: Forward)
				(TimedPrint 4 21 34)
				;"It's just as well.  I'd lose my dinner if you were brave."
				(= seconds 4)
			)
			(1
				(baba setCel: 1)
				(babaHead setLoop: 4)
				(spider setCycle: Forward)
				(bat setCycle: Forward)
				(TimedPrint 5 21 35)
				;"Lovies, it's about time we dine."
				(= seconds 4)
			)
			(2
				(EgoDead DIE_RETRY DIE_BABA_NOBRAVE 21 36
					#title { Shame on you!_}
					#icon myIcon 0 0
				)
				;Didn't anyone ever tell you that a hero is supposed to be brave even when he isn't?
				;Looks like you found the coward's way out...sauteed in wine sauce.

				;redraw the screen
				;(DrawPic 400 RETRY_STYLE dpCLEAR)
				(= cycles 1)
			)
			(3
				;(DrawPic 21 RETRY_STYLE dpCLEAR)
				;re-add the blanket to the picture
				;(blanket setPri: 2 ignoreActors: init: stopUpd: addToPic:)
				(bubbleMusic init: play:)
				;reset the script to state 5, then cue it to step6
				;(Print (rm21 script?))
				(rm21 setScript:(ScriptID 293 0))
				(baba setLoop: 1 setCel: 0 setCycle: NULL setMotion: NULL)
				(babaHead setLoop: 3 setCel: 0)
				((rm21 script?) changeState: 10 cue:)
				;reset the cursor to the inactrion cursor
				(theGame setCursor: waitCursor TRUE)
			)
		)
	)
)

;CI: NOTE: only gets called from the BabaYagaDeath procedure. So, only one scenario to consider reversing on a Retry.
(instance noFetchDeath of Script
	(properties)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(baba setScript: NULL)
				(= timerYesNo 0)
				(babaHead setCycle: Forward)
				(TimedPrint 4 21 37)
				;"Well, if you won't be a sweet, then you'll be my meat!"
				(= seconds 4)
			)
			(1
				(baba setCel: 1)
				(babaHead setLoop: 4)
				(spider setCycle: Forward)
				(bat setCycle: Forward)
				(TimedPrint 4 21 38)
				;"Nothin' says lovin' like frog legs in the oven."
				(= seconds 4)
			)
			(2
				;(babaHead hide:)
				(EgoDead DIE_RETRY DIE_BABA_NOFETCH 21 39
					#title { Of all the lazy....!_}
					#icon myIcon 0 0
				)
				;It seems a shame to have such a promising career go up in flames (assuming she decides to flambe you).
				;Wouldn't it have been better just to do a small favor for such a sweet little old Ogress?

				;redraw the screen
				;(DrawPic 400 RETRY_STYLE dpCLEAR)
				(= cycles 1)
			)
			(3
				;(DrawPic 21 RETRY_STYLE dpCLEAR)
				;re-add the blanket to the picture
				;(blanket setPri: 2 ignoreActors: init: stopUpd: addToPic:)
				(bubbleMusic init: play:)
				;reset the script to state 5, then cue it to step6
				;(Print (rm21 script?))
				(rm21 setScript:(ScriptID 293 0))
				(baba setLoop: 1 setCel: 0 setCycle: NULL setMotion: NULL)
				(babaHead setLoop: 3)
				((rm21 script?) changeState: 16 cue:)
				;reset the cursor to the inactrion cursor
				(theGame setCursor: waitCursor TRUE)
			)
		)
	)
)

;CI: NOTE: This is a public instance, so it could be called from outside scripts.
(instance noBringDeath of Script
	(properties)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(baba setScript: NULL)
				(= timerYesNo 0)
				(babaHead setCycle: Forward)
				(TimedPrint 6 21 40)
				;"No mandrake for me, eh?  What a waste of spells you turned out to be."
				(= seconds 6)
			)
			(1
				(spider setCycle: Forward)
				(bat setCycle: Forward)
				(TimedPrint 8 21 41)
				;"Actually, since it's my breakfast time, we'll have some amphibian omelette with bacon and legs."
				(= seconds 6)
			)
			(2
				;(babaHead hide:)
				(EgoDead DIE_RETRY DIE_BABA_NOBRING 21 42
					#title {Breakfast of Champions?}
					#icon myIcon 0 0
				)
				;At least you could have said "Yes" before you croaked!
				(= cycles 1)
			)
			(3
				(bubbleMusic init: play:)
				;reset the script to state 5, then cue it to step6
				;(Print (rm21 script?))
				(rm21 setScript:(ScriptID 294 0))
				(baba setLoop: 6 setCel: 4 setCycle: NULL setMotion: NULL)
				(ego setLoop: 3 setCel: 7)
				(babaHead setLoop: 4)
				((rm21 script?) changeState: 12 cue:)
				;reset the cursor to the inactrion cursor
				(theGame setCursor: waitCursor TRUE)
			)
		)
	)
)

;CI: NOTE: This is a newly created death, for the case where you tell the witch you have the mandrake, but you really don't.
;This is so we can have a more helpful death message, let the player know what they did wrong.
(instance noMandrakeDeath of Script
	(properties)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(baba setScript: NULL)
				(= timerYesNo 0)
				(babaHead setCycle: Forward)
				(TimedPrint 6 21 40)
				;"No mandrake for me, eh?  What a waste of spells you turned out to be."
				(= seconds 6)
			)
			(1
				(spider setCycle: Forward)
				(bat setCycle: Forward)
				(TimedPrint 8 21 41)
				;"Actually, since it's my breakfast time, we'll have some amphibian omelette with bacon and legs."
				(= seconds 6)
			)
			(2
				(babaHead hide:)
				(EgoDead DIE_RETRY DIE_BABA_LIEBRING 21 51
					#title {Liar Liar, Frog Legs on Fire!}
					#icon myIcon 0 0
				)
				;You can't bluff your way out of this frying pan.  If you promise you're going to do something, well then gosh darnit, you'd better do it!
				;reset the request to bring her the mandrake.
				(= cycles 1)
			)
			(3
				;reset the player to just being sent outside Baba Yaga's hut
				;tasked with getting the mandrake root.
				(= babaYagaStatus babaFETCH)
				(= dayPulledMandrakeRoot 0)
				(curRoom newRoom: 22)
			)
		)
	)
)

(instance lastWitch of Script
	(properties)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(HandsOff)
				(ego setMotion: MoveTo 120 152 self)
			)
			(1
				(baba setLoop: 6 posn: 168 122 init:)
				(babaHead
					posn: (baba x?) (- (baba y?) 34)
					init:
					setPri: 15
					setCycle: Forward
				)
				(= cycles 5)
			)
			(2
				(if (Btst VISITED_BABAYAGA_INTERIOR)
					(TimedPrint 4 21 43)
					;"You again?"
				else
					(TimedPrint 4 21 44)
					;"Hello, Hero."
				)
				(bat setCycle: EndLoop)
				(spider startUpd:)
				(= babaTalking TRUE)
				(= seconds 2)
			)
			(3
				(= seconds 0)
				(ego loop: loopE)
				(= babaTalking FALSE)
				(babaHead setCycle: NULL)
				(User canInput: TRUE)
				;set the normal cursor, to indicate the user can take action.
				(theGame setCursor: normalCursor (HaveMouse))

				;give the user 8 seconds to grab the magic mirror, before baba yaga just turns you into a frog and eats you.
				;they can use the magic mirror in the room's eventHandler, and it will set a flag. 
				;We test that flag a few seconds later in this script.
				(= seconds 8)
			)
			(4
				(bat setCycle: NULL)
				(spider setCycle: NULL)
				(User canInput: FALSE)
				;reset the cursor to the inactrion cursor
				(theGame setCursor: waitCursor TRUE)
				(= babaTalking TRUE)
				;CI: NOTE: Updated script so that if we've never visited here before, the dialog reflects that
				(if (Btst VISITED_BABAYAGA_INTERIOR)
					(Print 21 45
						#at -1 12
						#width 135
						#mode teJustCenter
						#dispose
						#time 10
					)
					; "Powers that rule
					; Over regions soggy:
					; Change this creature
					; Back into a froggy!"
				else
					(Print 21 28
						#at -1 12
						#width 150
						#mode teJustCenter
						#dispose
						#time 8
					)
					; "Hear me, oh Powers
					; Of Klatha and Mana!
					; Turn now my guest
					; Into species called Rana!"
				)
				(babaHead setCycle: Forward)
				(= seconds 7)
			)
			(5
				(if (Btst fBabaFrog)
					;if we've turned baba yaga into a frog, we'll stop this script, and switch to the endGame switch.
					(client setScript: endGame)
				else
					(= babaTalking FALSE)
					(babaHead setCycle: NULL hide:)
					(baba setCycle: EndLoop)
					(= seconds 2)
				)
			)
			(6
				(ego view: (GetEgoViewNumber vEgoFrogTransform) loop: 2 cel: 0 setCycle: EndLoop)
				(babaHead setCycle: NULL hide:)
				(= cycles 15)
			)
			(7
				(spider setCycle: NULL)
				(baba
					setLoop: 8
					setCycle: Forward
					setMotion: Chase ego 20 self
				)
			)
			(8
				(= babaTalking TRUE)
				(babaHead
					posn: (baba x?) (- (baba y?) 34)
					setPri: 15
					setCycle: Forward
					show:
				)
				;CI: NOTE: we've added a condition here to check if this is the hero's first time here, and the dialog now reflects that.
				(if (Btst VISITED_BABAYAGA_INTERIOR)
					(TimedPrint 6 21 46)
					;"Ooooh...yummy!  I'm glad you returned."
				else
					(TimedPrint 6 21 32)
					;"Bon appetit, Dearies!"
				)
				(= seconds 4)
			)
			(9
				(baba setLoop: 6 setCycle: NULL)
				(EgoDead DIE_RETRY DIE_BABA_NOMIRROR 21 47
					#title {Baba Yaga says you have good taste.}
					#icon myIcon 0 0
				)
				;Before you confront someone who is obviously more powerful than you are, give yourself a chance to reflect.
				(= cycles 1)
			)
			(10
				(Bset DIE_RETRY_INPROGRESS)
				(curRoom newRoom: 22)
			)
		)
	)
)

(instance endGame of Script
	(properties)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(= babaYagaStatus babaFINALE)
				(babaHead hide:)
				(baba
					view: vBabaYaga1
					setLoop: 6
					cycleSpeed: 1
					setCycle: CycleTo 3 cdFORWARD self
				)
				(ego
					view: (GetEgoViewNumber vEgoHoldingMirror)
					setLoop: 0
					cel: 0
					cycleSpeed: 1
					setCycle: EndLoop
				)
			)
			(1
				(baba setCycle: EndLoop)
				(reflection
					setLoop: 1
					cel: 0
					posn: (+ (ego x?) 13) (- (ego y?) 33)
					init:
					setCycle: EndLoop self
				)
			)
			(2
				(baba view: vFrogTransform setLoop: 0 cel: 0 setCycle: EndLoop self)
				(ego setCycle: BegLoop)
			)
			(3
				(baba setLoop: 1 cycleSpeed: 0 setCycle: Forward)
				(SolvePuzzle POINTS_TURNBABAYAGAINTOFROG 50)
				(TimedPrint 3 21 48)
				;The witch is hopping mad.
				(= seconds 3)
			)
			(4
				(TimedPrint 8 21 49)
				;"What have you DONE to me?
				;How DARE you use my own spell against me?
				;I'll, I'll....Oh oh!"
				(= seconds 8)
			)
			(5
				(StopEgo)
				(FaceObject ego baba)
				(TimedPrint 10 21 50)
				;"Hear me, Oh Element of Air and Wind.
				;Give me the power to save my own skin."
				(= seconds 8)
			)
			(6
				(teleport play:)
				(TP
					posn: (ego x?) (ego y?)
					setPri: 15
					cel: 0
					cycleSpeed: 4
					init:
					setCycle: CycleTo 6 cdFORWARD self
				)
			)
			(7
				(ego dispose:)
				(TP setCycle: EndLoop self)
			)
			(8
				(TP dispose:)
				(Bset BABAYAGA_HUT_OPEN)
				;send the hero outside the hut, because the hero's been dismissed.
				(curRoom newRoom: 22)
			)
		)
	)
)

(instance web of RFeature
	(properties
		nsTop 70
		nsLeft 228
		nsBottom 117
		nsRight 255
	)
)

(instance hutWindow of RFeature
	(properties
		nsTop 67
		nsLeft 205
		nsBottom 100
		nsRight 227
	)
)

(instance chimney of RFeature
	(properties
		nsTop 57
		nsLeft 56
		nsBottom 142
		nsRight 113
	)
)

(instance cupboard of RFeature
	(properties
		nsTop 61
		nsLeft 111
		nsBottom 127
		nsRight 147
	)
)

(instance knives of RFeature
	(properties
		nsTop 65
		nsLeft 145
		nsBottom 86
		nsRight 165
	)
)

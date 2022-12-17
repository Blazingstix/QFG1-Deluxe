;;; Sierra Script 1.0 - (do not remove this comment)
(script# 318)
(include system.sh) (include sci2.sh) (include game.sh)
(use Main)
(use _Interface)
(use _Game)
(use _User)
(use _Actor)

(public
	rm318 0
)


(local
	newView
	newView_2
	newView_3
	newView_4
	newView_5
	newView_6
	newView_7
	newView_8
	newView_9
	posterView
	whichPoster
)
(enum 1		;posters on the board
   healerRing
   elsa
   warlock
   leader
   spellComponents
   barnard
   exit
)
(procedure (localproc_000c param1 param2)
	(Print param2 #at -1 12 #mode teJustCenter #width param1)
)

(procedure (localproc_0022)
	(switch whichPoster
		(healerRing
			(posterView cel: 0 posn: 60 108 stopUpd:)
		)
		(elsa
			(posterView cel: 1 posn: 120 105 stopUpd:)
		)
		(warlock
			(posterView cel: 0 posn: 197 106 stopUpd:)
		)
		(leader
			(posterView cel: 2 posn: 100 154 stopUpd:)
		)
		(spellComponents
			(posterView cel: 0 posn: 176 155 stopUpd:)
		)
		(barnard
			(posterView cel: 3 posn: 263 146 stopUpd:)
		)
		(exit
			(posterView cel: 4 posn: 247 187 stopUpd:)
		)
	)
)

(procedure (ReadPoster &tmp [temp0 4] [temp4 400])
	(switch whichPoster
		(healerRing
			(Format @temp4 318 0)
			;Reward for return of lost ring. Inquire at the Healer's.
			(localproc_000c 185 @temp4)
		)
		(elsa
			(Format @temp4 318 1)
			;This poster is rather dusty and faded. The picture is of a small child with braids.
			;"Reward of 50 gold coins for the safe return of\nElsa von Spielburg.\nInquire at Spielburg Castle gates."
			(localproc_000c 260 @temp4)
		)
		(warlock
			(Format @temp4 318 2)
			;Reward of 30 gold coins for the Capture or Death of the Brigand Warlock. Description:  short, ugly, and
			;wears brightly colored robes. Has habit of laughing continually. Inquire at Spielburg Castle gates.
			(localproc_000c 200 @temp4)
		)
		(leader
			(Format @temp4 318 3)
			;Wanted:  Brigand Leader. Description:  Unknown appearance, wears a cloak. Must provide proof of leader's identity.
			;Reward of 60 gold coins and title of Hero of the Realm. Inquire at Spielburg Castle gates.
			(localproc_000c 300 @temp4)
		)
		(spellComponents
			(Format @temp4 318 4)
			;Notice:  Spell components needed. Cash or trade for potions. Inquire at the Healer's.
			(localproc_000c 200 @temp4)
		)
		(barnard
			(Bset READ_BARNARD_BULLETIN)
			(Format @temp4 318 5)
			;This poster seems to have been here a while. It has a picture of a handsome, but arrogant young man.
			;"Reward of 50 gold coins for information leading to the return of Baronet Barnard von Spielburg. Inquire at Spielburg Castle gates."
			(localproc_000c 280 @temp4)
		)
		(exit (curRoom newRoom: 311))
	)
)

(instance rm318 of Room
	(properties
		picture 318
		style IRISOUT
	)
	
	(method (init)
		(Load RES_VIEW vNoticeBoard)
		(super init:)
		(mouseDownHandler add: self)
		(keyDownHandler add: self)
		(directionHandler add: self)
		(StatusLine enable:)
		(User canControl: FALSE)
		((= newView (View new:))
			view: vNoticeBoard
			loop: 0
			cel: 1
			posn: 60 76
			setPri: 0
			init:
			ignoreActors:
			stopUpd:
			addToPic:
		)
		((= newView_2 (View new:))
			view: vNoticeBoard
			loop: 0
			cel: 1
			posn: 120 70
			setPri: 0
			init:
			ignoreActors:
			stopUpd:
			addToPic:
		)
		((= newView_3 (View new:))
			view: vNoticeBoard
			loop: 0
			cel: 1
			posn: 197 74
			setPri: 0
			init:
			ignoreActors:
			stopUpd:
			addToPic:
		)
		((= newView_4 (View new:))
			view: vNoticeBoard
			loop: 0
			cel: 0
			posn: 100 124
			setPri: 0
			init:
			ignoreActors:
			stopUpd:
			addToPic:
		)
		((= newView_5 (View new:))
			view: vNoticeBoard
			loop: 0
			cel: 2
			posn: 175 123
			setPri: 0
			init:
			ignoreActors:
			stopUpd:
			addToPic:
		)
		((= newView_6 (View new:))
			view: vNoticeBoard
			loop: 0
			cel: 1
			posn: 263 104
			setPri: 0
			init:
			ignoreActors:
			stopUpd:
			addToPic:
		)
		((= newView_7 (View new:))
			view: vNoticeBoard
			loop: 1
			cel: 0
			posn: 119 84
			setPri: 1
			init:
			ignoreActors:
			stopUpd:
			addToPic:
		)
		((= newView_8 (View new:))
			view: vNoticeBoard
			loop: 1
			cel: 1
			posn: 262 120
			setPri: 1
			init:
			ignoreActors:
			stopUpd:
			addToPic:
		)
		((= newView_9 (View new:))
			view: vNoticeBoard
			loop: 0
			cel: 0
			posn: 100 124
			setPri: 1
			init:
			ignoreActors:
			stopUpd:
			addToPic:
		)
		(= whichPoster 1)
		((= posterView (View new:))
			view: vNoticeBoard
			loop: 2
			cel: 0
			posn: 60 108
			setPri: 1
			init:
			ignoreActors:
			stopUpd:
		)
	)
	
	(method (dispose)
		(mouseDownHandler delete: self)
		(keyDownHandler delete: self)
		(directionHandler delete: self)
		(User canControl: TRUE)
		(super dispose:)
	)
	
	(method (handleEvent event &tmp temp0 temp1)
		(switch (event type?)
			(mouseDown
				(= temp1 0)
				(switch (= temp0 (OnControl CMAP (event x?) (event y?)))
					(cBLUE (= temp1 1))
					(cGREEN (= temp1 2))
					(cCYAN (= temp1 3))
					(cRED (= temp1 4))
					(cMAGENTA (= temp1 5))
					(cBROWN (= temp1 6))
					(cYELLOW
						(= whichPoster 7)
						(localproc_0022)
						(RedrawCast)
						(event claimed: 1)
						(curRoom newRoom: 311)
					)
				)
				(event claimed: 1)
				(localproc_0022)
				(if temp1
					(= whichPoster temp1)
					(localproc_0022)
					(RedrawCast)
					(ReadPoster)
				)
			)
			(evSAID
				(cond 
					((Said 'cease,done,done,done') (event claimed: 1) (curRoom newRoom: 311))
					((Said 'look') (event claimed: 1) (ReadPoster))
				)
			)
			(direction
				(switch (event message?)
					(dirN
						(if (< (= whichPoster (- whichPoster 3)) 1)
							(= whichPoster (+ whichPoster 7))
						)
					)
					(dirE
						(if (> (++ whichPoster) 7) (= whichPoster 1))
					)
					(dirS
						(if (> (= whichPoster (+ whichPoster 3)) 7)
							(= whichPoster (- whichPoster 7))
						)
					)
					(dirW
						(if (< (-- whichPoster) 1) (= whichPoster 7))
					)
				)
				(event claimed: TRUE)
				(localproc_0022)
			)
			(keyDown
				(if (== (event message?) KEY_RETURN)
					(event claimed: TRUE)
					(ReadPoster)
					(if (> (++ whichPoster) 7) (= whichPoster 1))
					(localproc_0022)
				)
			)
		)
		(super handleEvent: event)
	)
)

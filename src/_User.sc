;;; Sierra Script 1.0 - (do not remove this comment)
;;;;
;;;;  USER.SC
;;;;
;;;;  (c) Sierra On-Line, Inc, 1988
;;;;
;;;;  Author:  Jeff Stephenson
;;;;  Updated: Brian K. Hughes
;;;;
;;;;  A User is an object which corresponds to the person playing the
;;;;  game and acts as the intermediary between the person and the
;;;;  other objects in the game.  In the current games there is only
;;;;  one User, and thus we use the class User rather than an instance
;;;;  of the class.
;;;;
;;;;  Classes:
;;;;     User
;;;;	 Ego (SCI0 only)
;;;;     OnMeAndLowY (SCI1.1 only)

(script# USER) ;USER = 996
(include system.sh) (include sci2.sh) (include game.sh)
(use Main)
(use _Interface)
(use _Sound)
(use _Motion)
(use _Menu)
(use _Actor)
(use _System)


(local
	[inputLine 23]
	userAngle
)
(instance uEvt of Event
	(properties)
)

(class User of Object
	(properties
		alterEgo 0				;the object ID of the Ego which User controls
		canInput FALSE			;can the User type input?
		controls 0				;boolean -- does User control alterEgo at present?
		echo 32
		prevDir dirStop			;previous direction in which alterEgo was moving
		prompt {Enter input}
		inputLineAddr 0
		x -1					; upper/left
		y -1					; of user window
		blocks 1
		mapKeyToDir TRUE		;map keys to dirs?
		curEvent NULL			;pointer to instance of Event
	)
	
	(method (init newInput newAngle)
		(= inputLineAddr (if argc newInput else @inputLine))
		(= userAngle (if (== argc 2) newAngle else 45))
		(= curEvent uEvt)
	)
	
	(method (doit)
		;; See if there is an event.  If none, just return.  Otherwise
		;; pass the event to other objects in the game to see if they
		;; want it.
      
		(if (== FALSE isDemoGame)
			(curEvent
				type: nullEvt
				message: 0
				modifiers: 0
				y: 0
				x: 0
				claimed: FALSE
			)
			(GetEvent allEvents curEvent)
			(self handleEvent: curEvent)
		)
	)
	
	(method (canControl theControls)
		;; Doing a (User canControl: FALSE) prevents the user from controlling
		;; the alterEgo using the mouse, arrow keys, etc.  (User canControl: TRUE)
		;; reinstates user control.
      
		(if argc (= controls theControls) (= prevDir dirStop))
		(return controls)
	)
	
	(method (getInput event &tmp paused ret)
		(if (!= (event type?) keyDown) (= inputLine 0))
		(if (!= (event message?) echo)
			(Format @inputLine USER 0 (event message?))
		)
		(= paused (Sound pause: blocks))
		(= ret (GetInput @inputLine userAngle prompt #at x y))
		(Sound pause: paused)
		(return ret)
	)
	
	(method (said event)
		(if useSortedFeatures
			;CI: Note, Proc984_0 is not found, because the script does not exist.
			;This will throw a run-time error if useSortedFeatures is set.
			;Script 984 is SORTCOPY.SC
			(__proc984_0 alterEgo sFeatures cast features)
		else
			(sFeatures add: cast features)
		)
		(if TheMenuBar (sFeatures addToFront: TheMenuBar))
		(sFeatures addToEnd: theGame handleEvent: event release:)
		(if
		(and (== (event type?) speechEvent) (not (event claimed?)))
			(theGame pragmaFail: @inputLine)
		)
	)
	
	(method (handleEvent event &tmp eType eMsg)
		; The path that the event follows is: ( in SCI 1.1)
		;
		;   Speech Handler
		;        |
		;     Prints
		;        |
		;        \-[direction]----: directionHandler
		;        |                  \-[walk]-: ego
		;        |                  pmouse
		;        |
		;        \-[keyDown]------: keyDownHandler
		;        |
		;        \-[mouseDown/Up]-: mouseDownHandler
		;        |
		;     IconBar
		;        |
		;        \-[walkEvent]----: walkHandler
		;        |                  ego
		;        |
		;      Cast :-----\
		;        |        |
		;     Features :--|- (sorted features uses all these with OnMeAndLowY)
		;        |        |
		;     AddToPics :-/
		;        |
		;     Regions
		;        |
		;      Game

		(if (event type?)
			(= lastEvent event)

			; Save some things in temps so we don't have to send messages constantly
			(= eType (event type?))

			; Convert key events to direction events, if appropriate, but
			;  remember what kind of event it was
			(if mapKeyToDir (MapKeyToDir event))
			
			; Give the event to the menu first, if one exists
			(if TheMenuBar (TheMenuBar handleEvent: event eType))
			(GlobalToLocal event)
			(if (not (event claimed?))
				(theGame handleEvent: event eType)
			)
			(if (and controls
					(not (event claimed?))
					(cast contains: alterEgo)
				)
				(alterEgo handleEvent: event)
			)
			(if (and canInput
					(not (event claimed?))
					(== (event type?) keyDown)
					(or
						(== (event message?) echo)
						(and
							(<= KEY_SPACE (event message?))
							(<= (event message?) 255)
						)
					)
					(self getInput: event)
					(Parse @inputLine event)
				)
				(event type: speechEvent)
				(self said: event)
			)
		)
		(= lastEvent 0)
	)
)

(class Ego of Actor
	;;; An Ego is an Actor which can be controlled by a User.
	;;; "ego" is a global var that equals the base room instance of
	;;; the single User currently supported in the system.

	(properties
		;properties from Feature
		;y 0
		;x 0
		;z 0
		;heading 0
		;properties from view
		;yStep 2
		;view NULL
		;loop NULL
		;cel NULL
		;priority 0
		;underBits 0
		signal ignrHrz	;Ego ignores horizon so he can move above it to
						;	set edgeHit
		;nsTop 0
		;nsLeft 0
		;nsBottom 0
		;nsRight 0
		;lsTop 0
		;lsLeft 0
		;lsBottom 0
		;lsRight 0
		;brTop 0
		;brLeft 0
		;brBottom 0
		;brRight 0
		;properties from Prop
		;cycleSpeed 0
		;script 0
		;cycler 0
		;timer 0
		;properties from Actor
		;illegalBits cWHITE
		;xLast 0
		;yLast 0
		;xStep 3
		;moveSpeed 0
		;blocks 0
		;baseSetter 0
		;mover 0
		;looper 0
		;viewer 0
		;avoider 0
		
		edgeHit 0	;edge of screen (or horizon) which the Ego has hit
					;  (NORTH, SOUTH, EAST, WEST)
	)
	
	(method (init)
		(super init:)
		
		;Set cycling to walk.
		(if (not cycler) (self setCycle: Walk))
	)
	
	(method (doit)
		(super doit:)

		;If the Ego has moved either above the horizon or past a screen edge,
		;set the edgeHit to the appropriate edge.
		(= edgeHit
			(cond 
				((<= x westEdge) WEST)
				((<= y (curRoom horizon?)) NORTH)
				((>= x eastEdge) EAST)
				((>= y southEdge) SOUTH)
				(else 0)
			)
		)
	)
	
	(method (handleEvent event &tmp dir)
		(if (not (super handleEvent: event))
			(switch (event type?)
				(mouseDown
					(if
						(and
							(not (& (event modifiers?) shiftDown))
							(User controls?)
						)
						(self setMotion: MoveTo (event x?) (event y?))
						(User prevDir: dirStop)
						(event claimed: TRUE)
					)
				)
				(direction
					(if
						(and
							(==
								(= dir (event message?))
								(User prevDir?)
							)
							(IsObject mover)
						)
						(= dir dirStop)
					)
					(User prevDir: dir)
					(self setDirection: dir)
					(event claimed: TRUE)
				)
			)
		)
		(event claimed?)
	)
	
	(method (get what &tmp i)
		;; Put a number of items into Ego's inventory.

		(= i 0)
		(while (< i argc)
			((inventory at: [what i]) moveTo: self)
			(++ i)
		)
	)
	
	(method (put what recipient)
		;; Put an item of Ego's inventory into the inventory of 'recipient'.
		;; If recipient is not present, put item into limbo (-1 owner).
      
		(if (self has: what)
			((inventory at: what)
				moveTo: (if (== argc 1) -1 else recipient)
			)
		)
	)
	
	(method (has what &tmp theItem)
		;; Return TRUE if Ego has 'what' in inventory.

		(if (= theItem (inventory at: what))
			(theItem ownedBy: self)
		)
	)
)

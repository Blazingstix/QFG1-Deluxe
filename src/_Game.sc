;;; Sierra Script 1.0 - (do not remove this comment)
;;;;
;;;;  GAME.SC
;;;;
;;;;  (c) Sierra On-Line, Inc, 1992
;;;;
;;;;  Author:  Jeff Stephenson
;;;;  Updated:
;;;;     Brian K. Hughes
;;;;     August 19, 1992
;;;;
;;;;  This module contains the classes which implement much of the behavior
;;;;  of an adventure game.
;;;;
;;;;  Classes:
;;;;     Sounds
;;;;     Cue
;;;;     Game
;;;;     Region
;;;;     Room
;;;;     StatusLine
;;;;
;;;;  Procedures
;;;;     PromptForDiskChange

(script# GAME) ;GAME = 994
(include system.sh) (include sci2.sh) (include game.sh)
(use Main)
(use _Interface)
(use _Sound)
(use _Save)
(use _Motion)
(use _Inventory)
(use _User)
(use _System)


(procedure (PromptForDiskChange saveDisk &tmp ret [saveDevice 40] [curDevice 40] [str 40])
	;; Used by restore: to prompt the user to change disks if running
	;; on single-drive removable media.

	(= ret TRUE)
	(DeviceInfo GetDevice curSaveDir @saveDevice)
	(DeviceInfo CurDevice @curDevice)
	(if
		(and
			(DeviceInfo SameDevice @saveDevice @curDevice)
			(DeviceInfo DevRemovable @curDevice)
		)
		(Format
			@str
			GAME 6
			(if saveDisk {SAVE GAME} else {GAME})
			@curDevice
		)
		(DeviceInfo CloseDevice)
		(if
			(==
				(= ret
					(if saveDisk
						(Print
							@str
							#font 0
							#button {OK} 1
							#button {Cancel} 0
							#button {Change Directory} 2
						)
					else
						(Print @str #font 0 #button {OK} 1)
					)
				)
				2 ;Change Directory
			)
			(= ret (GetDirectory curSaveDir))
		)
	)
	(return ret)
)

(instance theCast of EventHandler
	(properties
		name "cast"
	)
)

(instance theFeatures of EventHandler
	(properties
		name "features"
	)
)

(instance theSFeatures of EventHandler
	(properties
		name "sFeatures"
	)
	
	(method (delete collect)
		(super delete: collect)
		(if
			(and
				useSortedFeatures
				(collect isKindOf: Collection)
				(not (OneOf collect regions locales))
			)
			(collect release: dispose:)
		)
	)
)

(instance theSounds of EventHandler
	(properties
		name "sounds"
	)
)

(instance theRegions of EventHandler
	(properties
		name "regions"
	)
)

(instance theLocales of EventHandler
	(properties
		name "locales"
	)
)

(instance theAddToPics of EventHandler
	(properties
		name "addToPics"
	)
	
	(method (doit)
      ;
      ; Call kernel to draw the current list of addToPics
      ; They will not be seen until the next Animate call

		(AddToPic elements)
	)
)

(instance theControls of Controls
	(properties
		name "controls"
	)
)

(instance theTimers of Set
	(properties
		name "timers"
	)
)

(class Game of Object
	;; The Game class implements the game which is being written.  The
	;; game author creates a source file with script number 0 which
	;; contains the instance of the class Game which is the game.  This
	;; instance is where, for example, input not handled by any Actor,
	;; Room, Region, etc. will be handled.  

	(properties
		script NULL 	;a current script for the game as a whole
	)
	
	(method (init &tmp obj)
		;
		; This initializes the generic game system.  The game's 0 module will be
		;  responsible for modifying this to select and start the initial room
		;  of the game.

		; Make sure some important modules are loaded in.
		(= obj Motion)
		(= obj Sound)
		(= obj Save)

		; Initialize the Collections
		((= cast theCast) 				add:)
		((= features theFeatures) 		add:)
		((= sFeatures theSFeatures) 	add:)
		((= sounds theSounds) 			add:)
		((= regions theRegions) 		add:)
		((= locales theLocales) 		add:)
		((= addToPics theAddToPics) 	add:)
		((= timers theTimers) 			add:)
		
		; Set the current save/restore directory		
		(= curSaveDir (GetSaveDir))
		
		; Initialize the inventory
		(Inventory init:)
		
		; Initialize the user.
		(User init:)
	)
	
	(method (doit)
		;
		; This is the code which is repeatedly executed in order to run the game

		; Check all sounds and timers for completion, which will cue if required
		(sounds eachElementDo: #check)
		(timers eachElementDo: #doit)
		(if modelessDialog (modelessDialog check:))


		; Give each character in the cast the chance to do its thing.  Show the
		;  changes on the screen, then delete any cast members who are scheduled
		;  for deletion.
		(Animate (cast elements?) TRUE)
		(if doMotionCue
			(= doMotionCue FALSE)
			(cast eachElementDo: #motionCue)
		)
		
		; Execute any script attached to the game
		(if script (script doit:))
		
		(regions eachElementDo: #doit)
		
		
		; If somebody wants us to change rooms, they set newRoomNum to do so
		(if (!= newRoomNum curRoomNum)
			(self newRoom: newRoomNum)
		else
			; Check for user input, since a room change is not in progress
			(User doit:)
		)
	
		; Remove any expired timers.
		(timers eachElementDo: #delete)
		
		(GameIsRestarting FALSE)
	)
	
	(method (showSelf)
		(regions showSelf:)
	)
	
	(method (play)
		;
		; Invoked from the kernel, this starts the game going, then goes into the
		; main game loop of doit: then wait for the next animation cycle

		(= theGame self)
		(= curSaveDir (GetSaveDir))
		(if (not (GameIsRestarting)) (GetCWD curSaveDir))
		; Put up the 'wait a bit' cursor while initializing the game
		(self setCursor: waitCursor TRUE)
		(self init:)

		; This can't be in the same message as above because normalCursor gets
		;  evaluated first, making it impossible to initialize it in the game's
		;  init method
		(self setCursor: normalCursor (HaveMouse))
		
		; The main game loop
		(while (not quit)
			(self doit:)
			(= overRun (Wait speed))
		)
	)
	
	(method (replay)
		;
		; Invoked from the kernel, this restarts the game from a restore

		; Dispose the event which triggered the save-game which we're restoring
		(if lastEvent (lastEvent dispose:))
		(sFeatures release:)

		; Dispose any modeless dialog present
		(if modelessDialog (modelessDialog dispose:))
		
		; Invalidate any saved background bitmaps which were in the game
		;  being restored
		(cast eachElementDo: #perform RestoreUpdate)
		(theGame setCursor: waitCursor TRUE)
		
		; Draw the picture and put in all the addToPics which were in the game
		;  being restored
		(DrawPic (curRoom curPic?) 100 dpCLEAR defaultPalette)
		(if (!= overlays -1)
			(DrawPic overlays 100 dpNO_CLEAR defaultPalette)
		)
		(if (curRoom controls?) ((curRoom controls?) draw:))
		
		; Redraw the views that we have saved as addToPics
		(addToPics doit:)
		
		;Display the cursor, if there's a mouse
		(theGame setCursor: normalCursor (HaveMouse))
		
		; Redisplay the status line
		(StatusLine doit:)
		
		; Turn sound back on
		(DoSound sndRESUME)
		(Sound pause: FALSE)
		
		; The main game loop
		(while (not quit)
			(self doit:)
			(= overRun (Wait speed))
		)
	)
	
	(method (newRoom n &tmp [temp0 4] evt)
		;
		; Change rooms to room 'n'

		; Dispose of any addToPics
		(addToPics dispose:)

		; Dispose of features
		(features eachElementDo: #dispose release:)
		
		; Dispose the cast, expired timers, and non-kept regions
		(cast eachElementDo: #dispose eachElementDo: #delete)
		(timers eachElementDo: #delete)
		(regions eachElementDo: #perform DisposeNonKeptRegion release:)
		
		; Get rid of any miscellaneous doit-demoning nodes
		(locales eachElementDo: #dispose release:)

		; Dispose lastCast (internal kernel knowledge of the cast during
		;  the previous animation cycle)
		(Animate 0)
		
		(= prevRoomNum curRoomNum)
		(= curRoomNum n)
		(= newRoomNum n)
		
		; If resource usage tracking is enabled, flush all non-purgable resources
		(FlushResources n)
		
		;Set cursor to Wait Cursor
		(self setCursor: waitCursor TRUE)
		
		; Start up the room to which we're going
		(self startRoom: curRoomNum checkAni:)
		(self setCursor: (if isEgoLocked waitCursor else normalCursor) (HaveMouse))
		(SetSynonyms regions)
		(while ((= evt (Event new: 3)) type?)
			(evt dispose:)
		)
		(evt dispose:)
	)
	
	(method (startRoom roomNum)
		;
		; Initialize a new room.  Regions should be initialized in this method in
		;  the instance of Game, so that the Region is loaded into the heap below
		;  the rooms in the Region.

		; This allows us to break when the heap is as free as it gets with
		; the game running, letting us detect any fragmentation in the heap
		(if debugOn (SetDebug))
		
		; Initialize the new room and add it to the front of the region list
		(regions addToFront: (= curRoom (ScriptID roomNum)))
		(curRoom init:)
		(if isDemoGame (curRoom setRegions: DEMO))
	)
	
	(method (restart)
		(if modelessDialog (modelessDialog dispose:))
		(RestartGame)
	)
	
	(method (restore &tmp [comment 20] num oldCur oldVol)
		;
		; Restore a previously saved game.  The user interface work for this is
		;  done in class Restore, the actual save is in the RestoreGame kernel
		;  function

		(Load RES_FONT smallFont)
		(Load RES_CURSOR waitCursor)
		(= oldCur (self setCursor: normalCursor))
		(= oldVol (Sound pause: TRUE))
		(if (PromptForDiskChange TRUE)
			(if modelessDialog (modelessDialog dispose:))
			(if (!= (= num (Restore doit: &rest)) -1)
				(self setCursor: waitCursor TRUE)
				(if (CheckSaveGame name num version)
					(cast eachElementDo: #dispose)
					(cast eachElementDo: #delete)
					(RestoreGame name num version)
				else
					(Print GAME 1 #font 0 #button {OK} TRUE)
					(self setCursor: oldCur (HaveMouse))
					; That game was saved under a different interpreter. It cannot be restored.
				)
			)
			(PromptForDiskChange FALSE)
		)
		(Sound pause: oldVol)
	)
	
	(method (save &tmp [comment 20] num oldCur oldVol)
		;
		; Save the game at its current state.  The user interface work for this
		;  is done in class Save, the actual save in the SaveGame kernel function

		(Load RES_FONT smallFont)
		(Load RES_CURSOR waitCursor)
		(= oldVol (Sound pause: TRUE))
		(if (PromptForDiskChange TRUE)
			(if modelessDialog (modelessDialog dispose:))
			(if (!= (= num (Save doit: @comment)) -1)
				(= oldCur (self setCursor: waitCursor TRUE))
				(if (not (SaveGame name num @comment version))
					(Print GAME 0 #font 0 #button {OK} TRUE)
					; Your save game disk is full. You must either use another disk or save over an existing saved game.
				)
				(self setCursor: oldCur (HaveMouse))
			)
			(PromptForDiskChange FALSE)
		)
		(Sound pause: oldVol)
	)
	
	(method (changeScore delta)
		(= score (+ score delta))
		(StatusLine doit:)
	)
	
	(method (handleEvent event)
		(cond 
			(
				(and
					(not (if useSortedFeatures (== (event type?) speechEvent)))
					(or
						(regions handleEvent: event)
						(locales handleEvent: event)
					)
				)
			)
			(script (script handleEvent: event))
		)
		(event claimed?)
	)
	
	(method (showMem)
		;
		; Display information about free heap and hunk memory

		(Printf
			{Free Heap: %u Bytes\nLargest ptr: %u Bytes\nFreeHunk: %u KBytes\nLargest hunk: %u Bytes}
			(MemoryInfo FreeHeap)
			(MemoryInfo LargestPtr)
			(>> (MemoryInfo FreeHunk) $0006)
			(MemoryInfo LargestHandle)
		)
	)
	
	(method (setSpeed newSpeed &tmp oldSpeed)
		;
		; Set the game speed, returning the previous speed

		(= oldSpeed speed)
		(= speed newSpeed)
		(return oldSpeed)
	)
	
	(method (setCursor cursorNumber &tmp oldCursor)
		;
		; Set the cursor form, returning the previous form

		(= oldCursor theCursor)
		(= theCursor cursorNumber)
		(SetCursor cursorNumber &rest)
		(return oldCursor)
	)
	
	(method (checkAni &tmp extra)
		(Animate (cast elements?) 0)
		(Wait 0)
		(Animate (cast elements?) 0)
		(while (> (Wait 0) animationDelay)
			(breakif (== (= extra (cast firstTrue: #isExtra)) NULL))
			(extra addToPic:)
			(Animate (cast elements?) 0)
			(cast eachElementDo: #delete)
		)
	)
	
	(method (notify)
	)
	
	(method (setScript newScript)
		;
		; Attach a new script to this object, removing any existing one

		(if script (script dispose:))
		(if newScript (newScript init: self &rest))
	)
	
	(method (cue)
		(if script (script cue:))
	)
	
	(method (wordFail word &tmp [str 100])
		(Printf GAME 2 word)
		; I don't understand "%s".
		(return FALSE)
	)
	
	(method (syntaxFail)
		(Print GAME 3)
		; That doesn't appear to be a proper sentence.
	)
	
	(method (semanticFail)
		(Print GAME 4)
		; That sentence doesn't make sense.
	)
	
	(method (pragmaFail)
		(Print GAME 5)
		; You've left me responseless.
	)
)

(class Region of Object
	;;; A Region is an area of a game which is larger than a Room and which
	;;; has global actions associated with it.  Music which needs to be played
	;;; across rooms needs to be owned by a Region so that it is not disposed
	;;; on a room change.

	(properties
		name 	"Rgn"
		script	0		;the ID of a script attached to the Region
		number 	0		;the module number of the Region
		timer 	0		;the ID of a timer attached to the Region
		keep 	0		;0->dispose Region on newRoom:, 1->keep Region on newRoom:
		initialized 0	;has the Region been initialized?
	)
	
	(method (init)
		;
		; Initialize the Region.  Region initialization is controlled by the
		;  'initialized' property, so that the Region is only initialized once,
		;  upon entry, not each time rooms are changed

		(if (not initialized)
			(= initialized TRUE)
			(if (not (regions contains: self))
				(regions addToEnd: self)
			)
			(super init:)
		)
	)
	
	(method (doit)
		(if script (script doit:))
	)
	
	(method (dispose)
		;
		; Delete this region from the region list, then dispose any objects
		;  attached to/owned by it
		
		(regions delete: self)
		(if (IsObject script) (script dispose:))
		(if (IsObject timer) (timer dispose:))
		(sounds eachElementDo: #clean self)
		
		; Remove the Region module from the heap.
		(DisposeScript number)
	)
	
	(method (handleEvent event)
		(if script (script handleEvent: event))
		(event claimed?)
	)
	
	(method (setScript newScript)
		;
		; Attach a new script to this object, removing any existing one

		(if (IsObject script) (script dispose:))
		(if newScript (newScript init: self &rest))
	)
	
	(method (cue)
		(if script (script cue:))
	)
	
	(method (newRoom)
	)
	
	(method (notify)
		;; Handle arbitrary communication between Game, Regions, and Rooms.
		;; Protocol and number of parameters are up to the game programmer.
	)
)

(class Room of Region
	(properties
		name 		"Rm"
		;script 		0
		;number 		0
		;timer 		0
		;keep		0
		;initialized 0

		picture 0		;number of picture for this Room
		style $ffff		;the style in which to draw this Room's picture
		horizon 0		;y coordinate of Room's horizon
		controls 0		;a list of controls (buttons, etc.) in the Room
		north 0			;module number of Room to the north
		east 0			;module number of Room to the east
		south 0			;module number of Room to the south
		west 0			;module number of Room to the west
		curPic 0		;picture number of currently visible picture

		picAngle 0		;how far from vertical is our view? 0-89
		vanishingX 160
		vanishingY -30000
	)
	
	(method (init &tmp temp0)
		(= number curRoomNum)
		(= controls theControls)
		(= perspective picAngle)
		
		; Draw a picture (if non zero) in proper style
		(if picture (self drawPic: picture))
		
		; Reposition ego if he hit an edge in the previous room
		(switch ((User alterEgo?) edgeHit?)
			(NORTH 
				((User alterEgo?) y: (- southEdge 1))
			)
			(WEST
				((User alterEgo?)
					x: (- eastEdge ((User alterEgo?) xStep?))
				)
			)
			(SOUTH
				((User alterEgo?)
					y: (+ horizon ((User alterEgo?) yStep?))
				)
			)
			(EAST ((User alterEgo?) x: (+ westEdge 1)))
		)
		((User alterEgo?) edgeHit: 0)
	)
	
	(method (doit &tmp nRoom)
		;
		; Send the doit: to any script, then check to see if ego has hit the edge
		;  of the screen

		(if script (script doit:))
		(if
			(= nRoom
				(switch ((User alterEgo?) edgeHit?)
					(NORTH north)
					(EAST east)
					(SOUTH south)
					(WEST west)
				)
			)
			(self newRoom: nRoom)
		)
	)
	
	(method (dispose)
		(if controls (controls dispose:))
		(super dispose:)
	)
	
	(method (handleEvent event)
		(cond 
			((super handleEvent: event))
			(controls (controls handleEvent: event))
		)
		(event claimed?)
	)
	
	(method (newRoom n)
		;
		; Remove this Room from the regions, let the rest of the regions know
		;  about the room change, then put ourselves back in the action

		(regions
			delete: self
			eachElementDo: #newRoom n
			addToFront: self
		)
		(= newRoomNum n)
		(super newRoom: n)
	)
	
	(method (setRegions region &tmp i n regID)
		;
		; Set the regions used by a room

		(= i 0)
		(while (< i argc)
			(= n [region i])
			((= regID (ScriptID n))
				number: n
			)
			(regions add: regID)
			(if (not (regID initialized?)) (regID init:))
			(++ i)
		)
	)
	
	(method (setFeatures feature &tmp i n featID)
		(= i 0)
		(while (< i argc)
			(features add: [feature i])
			(++ i)
		)
	)
	
	(method (setLocales locale &tmp i n locID)
		(= i 0)
		(while (< i argc)
			(= n [locale i])
			((= locID (ScriptID n))
				number: n
			)
			(locales add: locID)
			(locID init:)
			(++ i)
		)
	)
	
	(method (drawPic pic theStyle)
		;
		; Draw the given picture in the appropriate style

		; Dispose of addToPics list that is now invalid
		(addToPics dispose:)
		(= curPic pic)
		(= overlays -1)
		(DrawPic pic
			(cond 
				((== argc 2) theStyle)	; use style passed
				((!= style -1) style)	; use default room style
				(else showStyle)		; use global style
			)
			dpCLEAR	; clear buffer before drawing
			defaultPalette
		)
	)
	
	(method (overlay pic theStyle)
		;
		; Overlay the current picture with another

		(= overlays pic)
		(DrawPic pic
			(cond 
				((== argc 2) theStyle)	; use style passed
				((!= style -1) style)	; use default room style
				(else showStyle)		; use global style
			)
			dpNO_CLEAR	; don't clear buffer
			defaultPalette
		)
	)
)

(class Locale of Object
	(properties
		number 0
	)
	
	(method (dispose)
		(locales delete: self)
		(DisposeScript number)
	)
	
	(method (handleEvent event)
		(event claimed?)
	)
)

(class StatusLine of Object
	;;; The StatusLine class provides a status line at the top of the
	;;; screen which is programmer-definable.  When enabled, it overlays
	;;; the menu bar.  The user may still access the menu by pressing Esc
	;;; or positioning the mouse pointer in the status line end pressing
	;;; the mouse button.  The status line usually shows the player's
	;;; score.
	;;; To use a status line in a game, create an instance of class Code
	;;; whose doit: method takes a pointer to an array.  The Code should
	;;; format the desired text string into the array.
	;;; To display the status line, execute (StatusLine enable:).
	(properties
		name 	"SL"
		state 	FALSE	;enabled/disabled
		code 	NULL	;ID of Code to display status line
	)
	
	(method (doit &tmp [theLine 41])
		;
		; This method calls the application code to format the status line string
		;  at theLine, then draws it
		
		(if code
			(code doit: @theLine)
			(DrawStatus (if state @theLine else NULL))
		)
	)
	
	(method (enable)
		;
		; Display the status line

		(= state TRUE)
		(self doit:)
	)
	
	(method (disable)
		;
		; Hide the status line

		(= state FALSE)
		(self doit:)
	)
)

(instance RestoreUpdate of Code
	;;; Used by replay: to properly deal with members of the cast which were
	;;; not updating when the game was saved.
	(properties
		name "RU"
	)
	
	(method (doit obj &tmp sigBits)
		;; If the object has underBits, it was not updating.  Its underBits
		;; property is now invalid, so clear it.  Also, set the signal bits
		;; to draw the object and stopUpd: it.

		(if (obj underBits?)
			(= sigBits
				(&
					(= sigBits (| (= sigBits (obj signal?)) stopUpdOn))
					(~ notUpd)
				)
			)
			(obj underBits: 0 signal: sigBits)
		)
	)
)

(instance DisposeNonKeptRegion of Code
	;;; Used during room changes to dispose any Regions whose 'keep' property
	;;; is not TRUE.

	(properties
		name "DNKR"
	)
	
	(method (doit region)
		(if (not (region keep?)) (region dispose:))
	)
)

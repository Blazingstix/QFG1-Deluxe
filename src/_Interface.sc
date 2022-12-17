;;; Sierra Script 1.0 - (do not remove this comment)
;;;;
;;;;  INTRFACE.SC
;;;;
;;;;  (c) Sierra On-Line, Inc, 1992
;;;;
;;;;  Author:  Bob Heitman
;;;;  Updated: Brian K. Hughes
;;;;
;;;;  Classes which implement the user interface of SCI.  Note that some dialog
;;;;  classes (DIcon, DButton, DEdit, DSelector) have been moved out to DIALOG
;;;;  module to save space.
;;;;
;;;;  Classes:
;;;;     Dialog
;;;;     DItem
;;;;     DText
;;;;     DIcon
;;;;     DButton
;;;;     DSelector
;;;;     Controls
;;;;
;;;;  Procedures:
;;;;     Print
;;;;     PrintD
;;;;     GetInput
;;;;     GetNumber
;;;;	 Printf
;;;;     MousedOn

(script# INTRFACE) ;INTRFACE = 255
(include system.sh) (include sci2.sh) (include game.sh)
(use Main)
(use _System)

(public
	Print 0
	PrintD 1
	GetInput 2
	GetNumber 3
	Printf 4
	MousedOn 5
)

(define BMOD 16) ; width equalizer for buttons

;; Print accepts several pseudo-selectors
;; #mode 	(30) 	- 1 parameter
;; #font 	(33) 	- 1 parameter
;; #width 	(70)	- 1 parameter
;; #time 	(25)	- 1 parameter
;; #title 	(80)	- 1 parameter (title)
;; #at 		(67)	- 2 parameters (x, y)
;; #draw 	(83)	- 0 parameters
;; #edit 	(41)	- 2 parameters (inputText, maxLength)
;; #button 	(81)	- 2 parameters (buttonLabel, buttonValue)
;; #??? 	(150)	- ???
;; #icon 	(82)	- 1 parameters (obj)
;;					  or 3 parameters (viewNum, loopNum, celNum)
;; #dispose	(88)	- 0 parameters
;; #window 	(35)	- 1 parameter (window)
;;
(procedure (Print params 
		&tmp 
			hDialog hDText hDIcon hDEdit 
			btnPressed paramCnt 
			diagX diagY maxWidth 
			newPrintDlg hFirstEnabled 
			oldPort [hButtons 6] btnsWidth 
			buttonHeight	;CI:NOTE: This was present in HQ v1.000 but not in QFG1 v1.200
			buttonCnt btnX 
			[msgBuffer 1000]
			)
					     
	;(= oldPort (GetPort)) ;CI:NOTE: This was here in HQ 1.000 but moved lower in QFG1 v1.200
	(= diagX (= diagY -1))	;reset diagX and Y positions
	;set all temp variables to 0
	(= newPrintDlg (= maxWidth (= btnsWidth (= buttonHeight (= hDIcon (= hDEdit (= buttonCnt 0)))))))
	
	((= hDialog (Dialog new:))
		window: systemWindow
		name: {PrintD}
	)
	(= hDText (DText new:))
	(cond 
		;if the 1st parameter is a number from 0 to 999 (less than 1000) then we read in a script from an external Text reference
		((u< [params 0] 1000)
			(GetFarText [params 0] [params 1] @msgBuffer)
			(= paramCnt 2)
		)
		;otherwise, we use the 1st paramater as the text reference
		([params 0]
			(StrCpy @msgBuffer [params 0])
			(= paramCnt 1)
		)
		(else 
			(= msgBuffer 0)
			(= paramCnt 0)
		)
	)
	(hDText
		text: @msgBuffer
		moveTo: MARGIN MARGIN
		font: userFont
		setSize:
	)
	(hDialog add: hDText)
	(= paramCnt paramCnt)	;CI: why do we assign the variable to itself??
	(while (< paramCnt argc)
		(switch [params paramCnt]
			(#mode
				(++ paramCnt)
				(hDText mode: [params paramCnt])
			)
			(#font
				(++ paramCnt)
				(hDText font: [params paramCnt] setSize: maxWidth)
			)
			(#width
				(= maxWidth [params (++ paramCnt)])
				(hDText setSize: maxWidth)
			)
			(#time
				(++ paramCnt)
				(hDialog time: [params paramCnt])
			)
			(#title
				(++ paramCnt)
				(hDialog text: [params paramCnt])
			)
			(#at
				(= diagX [params (++ paramCnt)])
				(= diagY [params (++ paramCnt)])
			)
			(#draw
				(Animate (cast elements?) FALSE)
			)
			(#edit
				(++ paramCnt)
				((= hDEdit (DEdit new:)) text: [params paramCnt])
				(++ paramCnt)
				(hDEdit max: [params paramCnt] setSize:)
			)
			(#button
				((= [hButtons buttonCnt] (DButton new:))
					text: [params (++ paramCnt)]
					value: [params (++ paramCnt)]
					setSize:
				)
				(= btnsWidth (+ btnsWidth ([hButtons buttonCnt] nsRight?) MARGIN))
				(++ buttonCnt)
			)
			;CI: NOTE: 150 allows you to set the height of the buttons, 
			;but it isn't used anywhere in this game
			;It was present in HQ v1.000, but was removed by QFG 1 v1.200
			(150 ;??
				((= [hButtons buttonCnt] (DButton new:))
					text: [params (++ paramCnt)]
					value: [params (++ paramCnt)]
					setSize:
				)
				(= buttonHeight (+ buttonHeight ([hButtons buttonCnt] nsBottom?) MARGIN))
				(++ buttonCnt)
			)
			(#icon
				(if (IsObject [params (+ paramCnt 1)])
					((= hDIcon ([params (+ paramCnt 1)] new:)) setSize:)
					(= paramCnt (+ paramCnt 1))
				else
					(= hDIcon (DIcon new:))
					(hDIcon
						view: [params (+ paramCnt 1)]
						loop: [params (+ paramCnt 2)]
						cel: [params (+ paramCnt 3)]
						setSize:
					)
					(= paramCnt (+ paramCnt 3))
				)
			)
			(#dispose
				(if modelessDialog (modelessDialog dispose:))
				(= newPrintDlg hDialog)
			)
			(#window
				(++ paramCnt)
				(hDialog window: [params paramCnt])
			)
		)
		(++ paramCnt)
	)

	;if there's an icon (i.e. #icon), then put the icon at position 4, 4 and move the text 4 to offset
	(if hDIcon
		(hDIcon moveTo: MARGIN MARGIN)
		(hDText moveTo: (+ MARGIN (hDIcon nsRight?)) (hDText nsTop?))
		(hDialog add: hDIcon)
	)
	(hDialog setSize:)
	
	;it is an input box (i.e. #edit), then add it.
	(if hDEdit
		(hDEdit moveTo: (hDText nsLeft?) (+ MARGIN (hDText nsBottom?)))
		(hDialog add: hDEdit setSize:)
	)
	
	;CI: NOTE: This is a conditional to choose between setting the button's height or it's width
	; That option was only present in HQ v1.000, but I've left it in here because
	; I like the option of having it available.
	(cond 
		(btnsWidth
			(= btnX
				(if (> btnsWidth (hDialog nsRight?))
					MARGIN
				else
					(- (hDialog nsRight?) btnsWidth)
				)
			)
			(= paramCnt 0)
			(while (< paramCnt buttonCnt)
				([hButtons paramCnt] moveTo: btnX (hDialog nsBottom?))
				(hDialog add: [hButtons paramCnt])
				(= btnX (+ MARGIN ([hButtons paramCnt] nsRight?)))
				(++ paramCnt)
			)
		)
		;CI: Again, this is related to #150 which sets the button's height.
		;It appears button Height and Width is an either/or situation.
		(buttonHeight
			(= btnX (+ (hDialog nsTop?) MARGIN))
			(= paramCnt 0)
			(while (< paramCnt buttonCnt)
				([hButtons paramCnt] moveTo: (hDialog nsRight?) btnX)
				(hDialog add: [hButtons paramCnt])
				(= btnX (+ MARGIN ([hButtons paramCnt] nsBottom?)))
				(++ paramCnt)
			)
		)
	)
	(hDialog setSize: center:)
	
	(if (and hDIcon (not (StrLen @msgBuffer)))
		(hDIcon
			moveTo:
				(/
					(-
						(- (hDialog nsRight?) (hDialog nsLeft?))
						(- (hDIcon nsRight?) (hDIcon nsLeft?))
					)
					2
				)
				MARGIN
		)
	)
	(hDialog
		moveTo:
			(if (== -1 diagX) (hDialog nsLeft?) else diagX)
			(if (== -1 diagY) (hDialog nsTop?) else diagY)
	)
	
	;CI: NOTE: QFG v1.200 moved this line here, from the beginning of the procedure.
	(= oldPort (GetPort))
	
	(hDialog
		open: (if (hDialog text?) wTitled else stdWindow) (if newPrintDlg pWHITE else -1)
	)
	(if newPrintDlg
		(= modelessPort (GetPort))
		(SetPort oldPort)
		(return (= modelessDialog newPrintDlg))
	)
	(if
		(and
			(= hFirstEnabled (hDialog firstTrue: #checkState dActive))
			(not (hDialog firstTrue: #checkState dExit))
		)
		(hFirstEnabled state: (| (hFirstEnabled state?) dExit))
	)
	(if (== (= btnPressed (hDialog doit: hFirstEnabled)) -1)
		(= btnPressed 0)
	)
	
	;loop through the buttons getting the responses
	(= paramCnt 0)
	(while (< paramCnt buttonCnt)
		(if (== btnPressed [hButtons paramCnt])
			(= btnPressed (btnPressed value?))
			(break)
		)
		(++ paramCnt)
	)
	(if (not (hDialog theItem?)) (= btnPressed 1))
	(hDialog dispose:)
	(return btnPressed)
)

(procedure (PrintD str view loop cel)
	(Print str #icon view loop cel &rest)
)

(procedure (GetInput inputStr maxLen str &tmp [temp0 4])
	(if
		(Print
			(if (>= argc 3) str else {})
			#edit
			inputStr
			maxLen
			&rest
		)
		(StrLen inputStr)
	)
)

(procedure (GetNumber str default &tmp [theLine 40])
	(= theLine 0)
	(if (> argc 1) (Format @theLine INTRFACE 0 default))
	(return
		(if (GetInput @theLine 5 str)
			(ReadNumber @theLine)
		else
			-1
		)
	)
)

(procedure (Printf &tmp [str 500])
	(Format @str &rest)
	(Print @str)
)

(procedure (MousedOn obj event modifiers)
	(cond 
		;CI: NOTE This was the 1st conditional in HQ v1.000
		;((!= (event type?) mouseDown) 0)
		;This is the 1st conditional in QFG1 v1.2000
		((or 
			(!= (event type?) mouseDown)
			(and
				(obj respondsTo: #signal)
				(& (obj signal?) actorHidden)
			)
		 )
			0
		)
		((and
			(>= argc 3)
			modifiers
			(== (& (event modifiers?) modifiers) 0)
			)
			0
		)
		((obj respondsTo: #nsLeft)
			(InRect
				(obj nsLeft?)
				(obj nsTop?)
				(obj nsRight?)
				(obj nsBottom?)
				event
			)
		)
	)
)

(procedure (StillDown &tmp evt ret)
	(= ret (!= ((= evt (Event new:)) type?) mouseUp))
	(evt dispose:)
	(return ret)
)

(class MenuBar of Object
	(properties
		name 	NULL
		state 	$0000
	)
	
	(method (draw)
		(= state TRUE)
		(DrawMenuBar TRUE)
	)
	
	(method (hide)
		;(= state FALSE) ;CI: NOTE: This was removed in QFG1 v1.200
		(DrawMenuBar FALSE)
	)
	
	(method (handleEvent event &tmp ret oldJoy)
		(= ret 0)
		(if state
			(= oldJoy (Joystick JoyRepeat 30))
			(= ret (MenuSelect event &rest))
			(Joystick JoyRepeat oldJoy)
		)
		(return ret)
	)
	
	(method (add)
		(AddMenu &rest)
	)
)

(class DItem of Object
;;; The superclass of all items of control in the user interface.
	
	(properties
		name 	 NULL	; don't waste storage on a name string
		type 	 $0000	; the type of this control
		state 	 $0000	; defined by each subclass
		nsTop 	 0		; visible rectangle
		nsLeft 	 0		; in LOCAL coords
		nsBottom 0		; used to select
		nsRight  0		; control via a mouse click
		key 	 0		; key code associated with control
		said 	 0		; said spec associated with control
		value 	 0		; for programmers use
	)
	
	(method (doit)
		;; Default method is to return value.
		;; Will be superceded by user's instances.

		(return value)
	)
	
	(method (enable bool)
		;; Enable/disable this control.
		
		(if bool
			(= state (| state dActive))
		else
			(= state (& state (~ dActive)))
		)
	)
	
	(method (select bool)
		;; Select/deselect this control.

		(if bool
			(= state (| state dSelected))
		else
			(= state (& state (~ dSelected)))
		)
		(self draw:)
	)
	
	(method (handleEvent event &tmp ret evtType evt)
		;; Return ID if this event is yours, else 0.
		
		(if (event claimed?) (return NULL))
		
		; default to not selected
		(= ret NULL)
		(if
			(and
				(& state dActive)
				(or
					;something was 'said'
					(and
						; assign to evtType variable for a slight speed up effort
						(== (= evtType (event type?)) speechEvent)
						(Said said)
					)
					; pressed your key
					(and
						(== evtType keyDown)
						(== (event message?) key)
					)
					; clicked in box
					(and (== evtType mouseDown) (self check: event))
				)
			)
			
			; this was us
			(event claimed: TRUE)
			(= ret (self track: event))
		)
		(return ret)
	)
	
	(method (check event)
		;; Return true if x/y/ in your rectangle.
		
		(return
			(if
				(and
					(>= (event x?) nsLeft)
					(>= (event y?) nsTop)
					(< (event x?) nsRight)
					(< (event y?) nsBottom)
				)
				TRUE
			else
				FALSE
			)
		)
	)
	
	(method (track event &tmp in lastIn)
		;; Track control to confirm selection.
		;; NOTE: Only a mouseDown requires a mouse track.

		(return
			(if (== mouseDown (event type?))
				(= lastIn 0)
				(repeat
					(= event (Event new: leaveIt))
					(GlobalToLocal event)
					(if (!= (= in (self check: event)) lastIn)
						(HiliteControl self)
						(= lastIn in)
					)
					(event dispose:)
					(breakif (not (StillDown)))
				)
				(if in (HiliteControl self))
				(return in)
			else
				(return self)
			)
		)
	)
	
	(method (setSize)
		;; Set the item's rectangle.  Responsibility of subclasses.
	)
	
	(method (move h v)
		;; Move item BY h v.
		
		(= nsRight (+ nsRight h))
		(= nsLeft (+ nsLeft h))
		(= nsTop (+ nsTop v))
		(= nsBottom (+ nsBottom v))
	)
	
	(method (moveTo h v)
		;; Move item TO h v.
		
		(self move: (- h nsLeft) (- v nsTop))
	)
	
	(method (draw)
		;; Draw self per kernel definition.
		
		(DrawControl self)
	)
	
	(method (isType theType)
		;; Return TRUE if this DItem is of type theType.
		
		(return (== type theType))
	)
	
	(method (checkState bit)
		(return (& state bit))
	)
	
	(method (cycle)
		; do something on each cycle through the dialog's doit
	)
)

(class DText of DItem
	;;; A non-editable, generally non-selectable text field.
	
	(properties
		;properties from DItem
		type 		dText	;value updated for DText
		;state 		$0000
		;nsTop 		0
		;nsLeft 		0
		;nsBottom 	0
		;nsRight 	0
		;key 		0
		;said 		0
		;value 		0
		;new properties for DText
		text 		0			;the text in the field
		font 		USERFONT	;font to use for print text
		mode 		teJustLeft	;possible alignment of text
								;  (0) teJustLeft     left justified
								;  (1) teJustCenter   center each line
								;  (-1) teJustRight    right justified
	)
	
	(method (new &tmp newText)
		((super new:) font: userFont yourself:)
	)
	
	(method (setSize w &tmp [r 4])
		;; If w arg is present it is the fixed width of the text rectangle.
		
		(TextSize @[r 0] text font (if argc w else 0))
		(= nsBottom (+ nsTop [r 2]))
		(= nsRight (+ nsLeft [r 3]))
	)
)

(class DIcon of DItem
	;;; Icons are simply a view/loop/cel combination created by the view
	;;; editor VE.  They are generally not selectable.

	(properties
		;properties from DItem
		type 		dIcon ;value updated for DIcon
		;state 		$0000
		;nsTop 		0
		;nsLeft 		0
		;nsBottom 	0
		;nsRight 	0
		;key 		0
		;said 		0
		;value 		0
		;new properties for DIcon
		view 		NULL	; view number
		loop 		NULL	; loop number
		cel 		NULL	; cel number
	)
	
	(method (setSize &tmp [r 4])
		(= nsRight (+ nsLeft (CelWide view loop cel)))
		(= nsBottom (+ nsTop (CelHigh view loop cel)))
	)
)

(class DButton of DItem
	;;; Buttons are selectable items which a user clicks in with the mouse
	;;; or selects with the TAB key and ENTER in order to execute an action.

	(properties
		;properties from DIcon
		type 		dButton ;value updated for DButton
		state 		(| dActive dExit) ;value updated for DButton
		;nsTop 		0
		;nsLeft 		0
		;nsBottom	0
		;nsRight 	0
		;key 		0
		;said 		0
		;value 		0
		;new properties for DButton
		text 		0		;text displayed inside button
		font 		SYSFONT	;should usally be left as the system font
	)
	
	(method (setSize &tmp [r 4])
		(TextSize @[r 0] text font)
		
		; a button box is one pixel larger all around
		(= [r 2] (+ [r 2] 2))
		(= [r 3] (+ [r 3] 2))
		(= nsBottom (+ nsTop [r 2]))
		(= [r 3] (* (/ (+ [r 3] (- BMOD 1)) BMOD) BMOD))
		(= nsRight (+ [r 3] nsLeft))
	)
)

(class DEdit of DItem
	;;; A text field which is editable by the user.
	
	(properties
		;properties from DItem
		type 		dEdit 	;value updated for DEdit
		state 		dActive	;value updated for DEdit
		;nsTop 		0
		;nsLeft 		0
		;nsBottom 	0
		;nsRight 	0
		;key 		0
		;said 		0
		;value 		0
		;new properties for DEdit
		text 		0		;default text when the edit item is drawn
		font 		SYSFONT	;this is often changed to a user font
		max 		0		;maximum number of characters allowed in field
		cursor 		0		;cursor position in field
	)
	
	(method (track evt)
		(EditControl self evt)
		(return self)	;used to return 0, see Corey
	)
	
	(method (setSize &tmp [r 4])
		;; Size and set cursor position to the end of the text.
		
		; box is as sized by max * width of an "M"
		(TextSize @[r 0] {M} font)
		(= nsBottom (+ nsTop [r 2]))
		(= nsRight (+ nsLeft (/ (* [r 3] max 3) 4)))
		(= cursor (StrLen text))
	)
)

(class DSelector of DItem
	;;; Selectors are a list of text items which can be scrolled.  The user
	;;; selects one of the items either by clicking directly on the item
	;;; or by scrolling a high-lighted bar to the selection.

	(properties
		;properties from DItem
		type 		dSelector
		;state 		$0000
		;nsTop 		0
		;nsLeft 		0
		;nsBottom 	0
		;nsRight 	0
		;key 		0
		;said 		0
		;value 		0
		;new properties for DSelector
		font 		SYSFONT
		x 			20		; width of text item (in characters)
		y 			6		; number of items displayed in selector
		text 		0		; the text items to be selected from
		cursor 		0		; the currently selected item
		lsTop 		0		; first line of text shown
		mark 		0		; the LINE of selector that is selected
	)
	
	(method (handleEvent event &tmp ret evtType evt newEvt i [r 4])
		;; Selectors are not really active so they always return 0,
		;; but they may claim the event.

		(if (event claimed?) (return NULL))
		;convert an Up or Down direction event into the equivalent keyDown type event
		;presumably only Up and Down are tracked because the assumption is that Up and Down will
		;comprehensively cycle through all selectors on screen
		; remap some directions into arrows
		(if (== direction (event type?))
			(event type: keyDown)
			(switch (event message?)
				(dirS (event message: DOWNARROW))
				(dirN (event message: UPARROW))
				(else  (event type: direction))
			)
		)
		
		(= ret 0)
		(switch (event type?)
			(keyDown
				(event claimed: TRUE)
				(switch (event message?)
					(HOMEKEY (self retreat: 50)) 			;aka NW (or Numpad7)
					(ENDKEY (self advance: 50)) 			;aka SW (or Numpad1)
					;CI: NOTE Leaked SYSTEM.SH defines PAGEUP = $4900, 
					;but sci.sh defines KEY_PAGEUP = $5100
					(KEY_PAGEUP (self advance: (- y 1))) 	;aka NE (or Numpad9)
					;CI: NOTE Leaked SYSTEM.SH defines PAGEDOWN = $5100, 
					;but sci.sh defines PAGEDOWN = $4900
					(KEY_PAGEDOWN (self retreat: (- y 1))) 	;aka SE (or Numpad3)
					(DOWNARROW (self advance: 1)) 			;aka S
					(UPARROW (self retreat: 1)) 			;aka N
					(else  (event claimed: FALSE))
				)
			)
			
			(mouseDown
				(if (self check: event)
					(event claimed: TRUE)
					
					; determine sub part
					(cond 
						; top bar
						((< (event y?) (+ nsTop 10))
							(repeat
								(self retreat: 1)
								(breakif (not (StillDown)))
							)
						)
						
						; bottom bar
						((> (event y?) (- nsBottom 10))
							(repeat
								(self advance: 1)
								(breakif (not (StillDown)))
							)
						)
						
						; it is in the center
						(else
							; determine line height
							(TextSize @[r 0] {M} font)
							(if
								(>
									(= i (/ (- (event y?) (+ nsTop 10)) [r 2]))
									mark
								)
								; need to advance
								(self advance: (- i mark))
							else
								; need to retreat
								(self retreat: (- mark i))
							)
						)
					)
				)
			)
		)
		(return
			(if (and (event claimed?) (& state dExit))
				self
			else
				NULL
			)
		)
	)
	
	(method (setSize &tmp [r 4])
		(TextSize @[r 0] {M} font)
		(= nsBottom (+ nsTop 20 (* [r 2] y)))
		(= nsRight (+ nsLeft (/ (* [r 3] x 3) 4)))
		(= lsTop (= cursor text))
		(= mark 0)
	)
	
	(method (indexOf what &tmp ptr i)
		;; Return index of this string OR -1.
		
		(= ptr text)
		(= i 0)
		(return
			(while (< i 300)
				(if (== 0 (StrLen ptr)) (return -1))
				(if (not (StrCmp what ptr)) (return i))
				(= ptr (+ ptr x))
				(++ i)
			)
		)
	)
	
	(method (at what)
		;; Return pointer to this index OR 0.
		
		(return (+ text (* x what)))
	)
	
	(method (advance lines &tmp redraw)
		;; Advance requested (or to end) lines.
		
		(= redraw FALSE)
		; is there another line?
		(while (and lines (StrAt cursor x))
			(= redraw TRUE)
			(= cursor (+ cursor x))
			
			; do we scroll?
			(if (< (+ mark 1) y)
				(++ mark)
			else
				(= lsTop (+ lsTop x))
			)
			(-- lines)
		)
		(if redraw (self draw:))
	)
	
	(method (retreat lines &tmp redraw)
		;; Retreat requested (or to top) lines.
		
		(= redraw FALSE)
		; are we at top?
		(while (and lines (!= cursor text))
			(= redraw TRUE)
			(= cursor (- cursor x))
			
			; do we scroll up?
			(if mark 
				(-- mark) 
			else 
				(= lsTop (- lsTop x))
			)
			(-- lines)
		)
		(if redraw (self draw:))
	)
)

(class Dialog of List
	;;; ACTIVE controls can be selected, INACTIVE ones can't.
	;;; EXIT controls return ID when selected.  NON-EXIT controls invoke DOIT.
	;;; All controls will show VISUAL evidence of selection.
	;;; Selection via a mouse click requires a track.

	(properties
		;properties from Collection
		;elements 	0
		;size 		0
		;new properties for Dialog
		text 		0	; title
		window 		0	; pointer to open window
		theItem 	0	; objID of "current" item
		nsTop 		0
		nsLeft 		0
		nsBottom 	0
		nsRight 	0
		time 		0
		busy 		0
		seconds 	0	; the number of seconds to wait before changing state
		lastSeconds 0	; private variable
	)
	
	(method (doit def &tmp done event ret eatTheMice lastTicks temp5)
		;
		; Process the dialog:
		;
		;  - If there are no active items, any event exits
		;  - Pressing ESC returns 0
		;  - Only ACTIVE items may be selected
		;  - Pressing ENTER returns the currently selected object if it is EXIT
		;  - Tab/Shift-tab advances/retreats to the next/previous ACTIVE item

		(= done 0)
		(= busy TRUE)
		(self eachElementDo: #init)

		; If def is not passed or zero, we pick first active item, after first
		;  unmarking the current item (if any)
		(if theItem (theItem select: FALSE))
		
		; The first active item will be either the 'def' passed, or the first
		;  active item
		(= theItem
			(if (and argc def)
				def
			else
				(self firstTrue: #checkState dActive)
			)
		)
		
		; Mark this item (if any)
		(if theItem (theItem select: TRUE))
		
		; If there are no active items then we will eat events, so set up
		;  the variables that determine how long (based on global eatMice)
		(if (not theItem)
			(= eatTheMice 60) ;eatTheMice = eatTheMice
			(= lastTicks (GetTime)) ;lastTicks = lastTicks
		else
			(= eatTheMice 0)
		)
		
		; Now get events and act upon them until we get a return code
		(= ret 0) ;ret = ret
		(while (not ret)
			
			; Call everyone's cycler
			(self eachElementDo: #cycle)
			
			; Create an event...
			(GlobalToLocal (= event (Event new:)))
			
			; Don't process the event if we're supposed to eat them (see above)
			(if eatTheMice
				(-- eatTheMice)
				(if (== (event type?) mouseDown) 
					(event type: 0)
				)
				
				(while (== lastTicks (GetTime))
				)
				(= lastTicks (GetTime))
			)
			
			; Everyone gets a shot at the event...
			(= ret (self handleEvent: event)) ;event = event
			
			; Get rid of the event we made
			(event dispose:)
			
			; If we're being timed, see if we should dispose yet
			(self check:)
			
			; If there are no active items, the user pressed the ENTER key, or
			;  clicked the mouse we return with no value
			(if (or (== ret -1) (not busy))
				(= ret 0)
				(EditControl theItem 0)
				(break)
			)
			(Wait 1)
		)
		(= busy FALSE)
		(return ret)
	)
	
	(method (dispose)
		;
		; Dispose of dialog & its stuff

		; Set system global for the current modeless dialog to zero if it is us
		(if (== self modelessDialog)
			(SetPort modelessPort)
			(= modelessDialog 0)
			(= modelessPort 0)
		)
		
		; Get rid of the window (if any)
		(if window (window dispose:))
		(= theItem (= window 0))
		(super dispose:)
	)
	
	(method (open wtype pri)
		;
		; Create the window & get ourselves on the screen

		(if (and (PicNotValid) cast)
			(RedrawCast)	;CI: NOTE: The decompilation for QFG1 v1.200 shows (Animate (gCast elements?) 0) directly, instead of calling (RedrawCast) to run that code. Unknown if that is a compiler optimization or not.
		)

		;; Get a new window for this dialog.
		; operate with a clone of provided window
		(= window (window new:))
		(window
			top: 		nsTop
			left: 		nsLeft
			bottom: 	nsBottom
			right: 		nsRight
			title: 		text
			type: 		wtype
			priority: 	pri
			open:
		)
		(= seconds time)
		(self draw:)
	)
	
	(method (draw)
		;
		; Draw contents of dialog

		(self eachElementDo: #draw)
	)
	
	(method (cue)
		(if (not busy) (self dispose:) else (= busy 0))
	)
	
	(method (advance &tmp obj node)
		;
		; Highlight next ACTIVE item in list

		(if theItem
			; clear current item
			(theItem select: FALSE)
			
			; we need the node value that we are
			(= node (self contains: theItem))
			(repeat
				(if (not (= node (self next: node)))
					(= node (self first:))
				)
				(= theItem (NodeValue node))
				
				; we break on next active item
				(if (& (theItem state?) dActive) 
					(break)
				)
			)
			(theItem select: TRUE)
		)
	)
	
	(method (retreat &tmp obj node)
		;
		; Highlight previous ACTIVE item in list

		(if theItem
			; clear current item
			(theItem select: FALSE)
			
			; we need the node value that we are
			(= node (self contains: theItem))
			(repeat
				(if (not (= node (self prev: node)))
					(= node (self last:))
				)
				(= theItem (NodeValue node))
				
				; we break on next active item
				(if (& (theItem state?) dActive) 
					(break)
				)
			)
			(theItem select: TRUE)
		)
	)
	
	(method (move h v)
		;
		; Move dialog relative to current position

		(= nsRight (+ nsRight h))
		(= nsLeft (+ nsLeft h))
		(= nsTop (+ nsTop v))
		(= nsBottom (+ nsBottom v))
	)
	
	(method (moveTo h v)
		;
		; Move dialog to absolute position

		(self move: (- h nsLeft) (- v nsTop))
	)
	
	(method (center)
		;
		; Center the dialog on the screen

		(self moveTo:
			(+ (window brLeft?) (/ (- (- (window brRight?) (window brLeft?)) (- nsRight nsLeft)) 2))
			(+ (window brTop?) (/ (- (- (window brBottom?) (window brTop?)) (- nsBottom nsTop)) 2))
		)
	)
	
	(method (setSize &tmp node obj [r 4])
		;
		; Determine the required dimensions to encompass all items

		(if text
			; get textsize in the font without word wrapping
			(TextSize @[r 0] text 0 -1)
			(= nsTop 	[r 0])
			(= nsLeft 	[r 1])
			(= nsBottom [r 2])
			(= nsRight 	[r 3])
		else
			(= nsRight (= nsBottom (= nsLeft (= nsTop 0))))
		)
		
		(= node (self first:))
		(while node
			(if (< ((= obj (NodeValue node)) nsLeft?) nsLeft)
				(= nsLeft (obj nsLeft?))
			)
			; compare to existing size and adjust if neccessary
			(if (< (obj nsTop?) nsTop) 
				(= nsTop (obj nsTop?))
			)
			(if (> (obj nsRight?) nsRight)
				(= nsRight (obj nsRight?))
			)
			(if (> (obj nsBottom?) nsBottom)
				(= nsBottom (obj nsBottom?))
			)
			(= node (self next: node))
		)
		
		; add a border of MARGIN pixels.
		(= nsRight (+ nsRight MARGIN))
		(= nsBottom (+ nsBottom MARGIN))
		(self moveTo: 0 0)
	)
	
	(method (handleEvent event &tmp ret)
		;
		; Respond to the passed event with ID, -1, or -2

		(if (or
				(event claimed?)
				(== (event type?) nullEvt)
				(and
					(!= mouseDown (event type?))
					(!= keyDown (event type?))
					(!= direction (event type?))
					(!= joyDown (event type?))
				)
			)
			(EditControl theItem event)
			(return NULL)
		)

		; does this event belong to any in the list
		(if (= ret (self firstTrue: #handleEvent event))
			(EditControl theItem 0)
			
			; If NOT marked EXIT we doit: and advance to next
			(if (not (ret checkState: dExit))
				(if theItem 
					(theItem select: FALSE)
				)
				((= theItem ret) select: TRUE)
				
				; Send "doit" to object to perform any subclass-specific stuff
				(ret doit:)
				
				; don't report it
				(= ret NULL)
			)
		else
			
			; Check for standard conventions
			(= ret NULL)
			(cond 

				; ENTER key pressed and theItem is active, return object ID

				((and (or 	(== (event type?) joyDown)
							(and 	(== keyDown (event type?))
									(== KEY_RETURN (event message?))
							)
						)
						theItem
						(theItem checkState: dActive)
					)
					(= ret theItem)
					(EditControl theItem 0)
					(event claimed: TRUE)
				)
				
				((or 	
						
						; Any event exits if no active items
						
						(and (not (self firstTrue: #checkState dActive))
						  (or (and
									(== keyDown (event type?))
									(== KEY_RETURN (event message?))
								)
								(== mouseDown (event type?))
								(== joyDown (event type?))
							)
						)
						
						; ESC key pressed
						
						(and
							(== keyDown (event type?))
							(== KEY_ESCAPE (event message?))
						)
					)
					(event claimed: TRUE)
					(= ret -1)
				)
				
				; If we pressed a TAB, go to next item
				
				((and
						(== keyDown (event type?))
						(== KEY_TAB (event message?))
					)
					(event claimed: TRUE)
					(self advance:)
				)
				
				; If we pressed shift-TAB, go to prev item
				
				((and
						(== keyDown (event type?))
						(== KEY_SHIFTTAB (event message?))
					)
					(event claimed: TRUE)
					(self retreat:)
				)
				
				(else 
					(EditControl theItem event)
				)
			)
		)
		(return ret)
	)
	
	(method (check &tmp thisSeconds)
		;
		; Check to see if we can be disposed yet
         
		(if
			(and
				seconds
				(!= lastSeconds (= thisSeconds (GetTime SYSTIME1)))
			)
			(= lastSeconds thisSeconds)
			(if (not (-- seconds)) (self cue:))
		)
	)
)

(class Controls of List
	(properties
		;elements 	0
		;size 		0
	)
	
	(method (draw)
		(self eachElementDo: #setSize)
		(self eachElementDo: #draw)
	)
	
	(method (handleEvent evt &tmp cont)
		;; Find and track an active control.
		
		(if (evt claimed?) (return NULL))
		(if
			(and
				(= cont (self firstTrue: #handleEvent evt))
				(not (cont checkState: dExit))
			)
			(cont doit:)
			(= cont NULL)
		)
		(return cont)
	)
)

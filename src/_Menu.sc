;;; Sierra Script 1.0 - (do not remove this comment)
;**
;**   Sierra On-line basic standard menu
;**
;**      by Al Lowe
;**
;**      adapted by Pablo Ghenis
;**
;**   Last Update:   August 26, 1988
;**


;**   Break lines in "AddMenu" before a divider, for aesthetics
;**   = sets a menu item's starting value
;**   ! makes the item non-selectable
;**   ` denotes the following character as the key for the menu
;**   : separates menu items within a menu stack

(script# MENU) ;MENU = 997
(include system.sh) (include sci2.sh) (include game.sh)
(use Main)
(use _Interface)
(use _Sound)
(use _Save)
(use _User)

(public
	ToggleSound 0
	;setHighSpeedHero 1
	;EO: The High Speed Hero option was removed sometime between 1.000 and 1.200.
	;Could it be added back in with a patch?
	;CI: *Should* it?  It was probably removed because it was a short-sighted hacky way to speed up the hero.
)

; (procedure (setHighSpeedHero enabled)
;	(if (not (Btst HIGH_SPEED_HERO))
;		(if enabled
;			(SetMenu $308 113 TRUE 110 {Normal Speed Hero})
;			(if (not highSpeedHero)
;				(ego setStep: (* (ego xStep?) 2) (* (ego yStep?) 2))
;				(= highSpeedHero TRUE)
;			)
;		else
;			(SetMenu $308 113 FALSE 110 {High Speed Hero})
;			(if highSpeedHero
;				(ego
;					setStep: (/ (+ (ego xStep?) 1) 2) (/ (+ (ego yStep?) 1) 2)
;				)
;				(= highSpeedHero FALSE)
;			)
;		)
;	)
;)


(procedure (ToggleSound)
	(if (GetMenu 774 113)
		(DoSound sndSET_SOUND 0)
		(SetMenu 774 113 0 110 {Turn on})
	else
		(DoSound sndSET_SOUND 1)
		(SetMenu 774 113 1 110 {Turn off})
	)
)

(procedure (setInputText event)
	(if (> argc 1) (Format (User inputLineAddr?) &rest))
	(event claimed: FALSE type: keyDown message: (User echo?)) ;EO: Replaced KEY_DOWN with keyDown. This will allow the auto-type function to work.
)

(instance aWin of SysWindow
	(properties
		back vLCYAN
	)
)

;NOTE: SSCI allowed defining multiple "starting" numbers in a single enum, but SCICompanion only allows once
; so, we have to start multiple enums to define the entirety of the menu system.
(enum
$100 sierraM
	aboutI
	helpI
	sillyClownsI
)
(enum
$200 fileM
	saveI
	restoreI
		divider201I
	restartI
	quitI
)
(enum
$300 gameM
	fasterI
	normalI
	slowerI
		divider301I	
	volumeI
	soundI
		divider302I
;	highSpeedHeroI
)
(enum
$400 actionM
	castI
	fightI
	escapeI
		divider401I
	pauseI
	repeatI
;	bossI
;	triteI
)
(enum
$500 infoM
	invI
	charI
		divider501I
	timeI
	askI
	lookI
		divider502I
	deathsI
	pointsI
)
;(enum
;$600 debugM
;	gameI
;	castI
;	egoI
;	tglDebugI
;	memI
;		divider601I
;	visualI
;	priorityI
;	controlI
;		divider602I
;	gridI
;	writeEgoI
;)
;(enum
;$700 cheatM
;	teleportI
;	rgTimeI
;	clothingI
;	roomI
;	inputI
;	noteI
;	quickQuitI
;)


(class TheMenuBar of MenuBar
	(properties
		;state $0000
	)
	
	(method (init)
		(AddMenu 
			{ \01_} 
			{About Glory I `^g:Help`#1:Silly Clowns`^y=0}
		)
		(AddMenu
			{ File_}
			{Save Game`#5:Restore Game`#7:--!:Restart Game`#9:Quit`^q}
		)
		(AddMenu
			{ Game_}
			{Faster Animation`+:Normal Animation`=:Slower Animation`-:--!:Sound Volume...`^v:Turn Off Sound`#2=1}
			;:--!:High Speed Hero`#4=0
		)
		(AddMenu
			{ Action_}
			{Cast Spell`^c:Fight`^f:Escape`^e:--!:Pause Game`^p:Repeat`#3}
		)
		(AddMenu
			{ Information_}
			;{Inventory`^I:Char Sheet`^S:--!:Time/Day`^T:Ask about`^a:Look at`^l}
			;{Inventory`^I:Char Sheet`^S:--!:Time/Day`^T:Ask about`^a:Look at`^l:--!:Deaths List`^h}
			;{Inventory`^I:Char Sheet`^S:--!:Time/Day`^T:Ask about`^a:Look at`^l:--!:Deaths List`^h:Points List`^O}
			;making the menu short-cuts more in line qith QFG2
			{Inventory`^i:Char Sheet`^s:--!:Time/Day`^d:Ask about`^a:Look at`^l:--!:Deaths List`^h}
		)
		(SetMenu saveI 		109 'save[/game]')
		(SetMenu restoreI 	109 'restore[/game]')
		(SetMenu restartI 	109 'restart[/game]')
		(SetMenu pauseI 	109 'pause[/game]')
		(SetMenu normalI 	109 'normal[/speed]')
		(SetMenu invI 		109 '/inventory')
		(SetMenu timeI 		109 'are<time<what')
	)
	
	(method (handleEvent event &tmp [i 3] newVol [str 300] temp304)
		(switch (super handleEvent: event)
			(aboutI
				(if (< colourCount 8)
					(aWin color: vBLACK back: vWHITE)
				)
				(Print
					(Format @str MENU 0 version)
					#title {__Quest for Glory I Credits__}
					#mode teJustCenter
					#width 160
					#font smallFont
					#at -1 20
					#window aWin
				)
				(Print
					(Format @str MENU 1)
					#title {And last, but not least...}
					#mode teJustCenter
					#width 180
					#font smallFont
					#at -1 20
					#window aWin
				)
			)
			(helpI
				(Print MENU 2 #font smallFont)
				;      DURING THE GAME:
				; Click at the top of the screen or press ESC to use the menus. Additional shortcuts are shown there.
				; Click Right or Shift-Click Left Mouse
				; Button on an object to look at it.
				;
				;     IN TYPING WINDOWS:
				; Arrows, Home and End move the cursor, or click anywhere with the mouse. Ctrl-C clears the line.
				;
				;     IN DIALOG WINDOWS:
				; Select the outlined item with <Enter> and
				; use Tab and Shift-Tab to move between 
				; choices. 
				; Or click with the mouse to select an item.
				;
				; ESC always cancels. 
			)
			
			(sillyClownsI
					(cond
						(sillyClowns
							(= sillyClowns
								(Print
									{}
									#title {Silly Clowns}
									#button {Off} FALSE
									#button {On}  TRUE
									;#button {Auto}
									;116 sillyClowns
								)
							)
						)
						(else
							(= sillyClowns
								(Print
									{}
									#title {Silly Clowns}
									#button {On}  TRUE
									#button {Off} FALSE
									;#button {Auto}
									;116 sillyClowns
								)
							)
						)
					)
			)
			
			(saveI
				(if (Btst SAVE_ENABLED)
					(theGame save:)
				)
			)
			(restoreI 
				(theGame restore:)
			)
			(restartI
				(if
					(Print
						MENU 3
						#button {Restart} 1
						#button {Continue} 0
						#icon vDeathScenes 1 3
					)
					; You can change your mind
					;   if you have one.
					(theGame restart:)
				)
			)
			(quitI
				(AskQuit)
			)
			(fasterI
				(if (> speed 0) (theGame setSpeed: (-- speed)))
			)
			(normalI
				(theGame setSpeed: 6)
			)
			(slowerI
				(if (< speed 16)
					(theGame setSpeed: (++ speed))
				)
			)
			(volumeI
				(if
					(!=
						(= newVol
							(GetNumber {Volume (1 - 16)?} (+ 1 (DoSound ChangeVolume)))
						)
						-1
					)
					(if (< (-- newVol) 0) (= newVol 0))
					(if (> newVol 15) (= newVol 15))
					(DoSound ChangeVolume newVol)
				)
			)
			(soundI (ToggleSound)
				)
			; (highSpeedHeroI
			;	(setHighSpeedHero (not highSpeedHero)
			;	)
			(castI
				(setInputText event {cast_})
			)
			(fightI
				(setInputText event {fight_})
			)
			(escapeI
				(setInputText event {escape_})
			)
			(pauseI
				(= temp304 (Sound pause: TRUE))
				(Print
					MENU 4
					#title {Game Paused}
					#icon (GetEgoViewNumber vEgoDeathScenes) 0 0
					#button {Let's be a hero} 1
				)
				(Sound pause: temp304)
				; Every hero needs a break now and then.  Have a nice nap.
			)
			(repeatI 
				(setInputText event)
			)
			(invI
				(cond 
					((not (HaveMem 2000))
						(HighPrint MENU 5)
						;Sorry, there's not enough room to see that here.
					)
					((not isEgoLocked) 
						((ScriptID 206 0) init: doit:)
					)
				)
			)
			(charI
				(cond 
					((or (not (HaveMem 2000)) (== curRoomNum 95)) ;brigand dining hall (presumably, this room requires a lot of memory)
						(HighPrint MENU 5)
						;Sorry, there's not enough room to see that here.
					)
					((not isEgoLocked)
						((ScriptID 204 0) useWindow: TRUE showBars: TRUE init: doit:)
					)
				)
			)
			(timeI
				(ShowTime)
			)
			(askI
				(setInputText event {ask about_})
			)
			(lookI
				(setInputText event {look at_})
			)
			(deathsI
				;(Print {Not yet implemented.} #title {Deaths List})
				(cond 
					((or (not (HaveMem 2000)) (== curRoomNum 95)) ;brigand dining hall (presumably, this room requires a lot of memory)
						(HighPrint MENU 5)
						;Sorry, there's not enough room to see that here.
					)
					((not isEgoLocked)
						((ScriptID 650 0) init: doit:)
					)
				)

			)
			(pointsI
				(Print {Not yet implemented.} #title {Points List})
			)
		)
	)
)

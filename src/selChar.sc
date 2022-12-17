;;; Sierra Script 1.0 - (do not remove this comment)
(script# 202)
(include system.sh) (include sci2.sh) (include game.sh)
(use Main)
(use _Motion)
(use _Game)
(use _User)
(use _Actor)
(use _System)
(use _Interface)

(public
	selChar 0
)

(local
	propFighter
	propMagicUser
	propMagicEffect
	propThief
	vwTextChoose
	vwTextYour
	vwTextCharacter
	vwTextFighter
	vwTextMagic
	vwTextUser
	vwTextThief
	activeSelection	;The currently Highlighted selection box: 1, 2 or 3
	titleFlashCount ;we'll flash the Choose Your Character message twice before actually startign (just so the player knows what's expected of them).
	viewSelectedBorder
	local14 ;true/false
	local15 ;true/false: auto-increment characters
	local16 ;true/false: selection has been made
	local17 =  TRUE

	;added by CI, to also select between Avatars
	avatarScreen = FALSE
	selectCount = 3
	[scripts 4] ;to store the three scripts in (leaving 0 as empty)
)


(procedure (ToCharAlloc)
	(HandsOff)
	(curRoom newRoom: 203)
)

(procedure (UpdateCharacterAnimations)
	(switch activeSelection
		(1
			(if (== selectCount 3)
				(viewSelectedBorder setCel: 0 posn: 64 127 stopUpd:)
			else
				;else it's only 2, so we place the border in a different place:
				(viewSelectedBorder setCel: 0 posn: 90 127 stopUpd:)
			)
			([scripts 2] changeState: 0)
			(if (> selectCount 2)
				([scripts 3] changeState: 0)
			)
			([scripts 1] changeState: 1)
		)
		(2
			(if (== selectCount 3)
				(viewSelectedBorder setCel: 0 posn: 158 127 stopUpd:)
			else
				;else it's only 2 choices, so we put the border farther over
				(viewSelectedBorder setCel: 0 posn: 231 127 stopUpd:)
			)
			([scripts 1] changeState: 0)
			(if (> selectCount 2)
				([scripts 3] changeState: 0)
			)

			(if (and local16 (not avatarScreen))
				;if this is the magic user selection, we must apply special scripts.
				(propMagicUser setLoop: 5 setCel: 3)
				(propMagicEffect show:)
			else
				;otherwise, just do the normal script 2.
				([scripts 2] changeState: 1)
				;(mageScript changeState: 1)
			)
		)
		(3
			(viewSelectedBorder setCel: 0 posn: 252 127 stopUpd:)
			([scripts 1] changeState: 0)
			([scripts 2] changeState: 0)
			(if (> selectCount 2)
				([scripts 3] changeState: 1)
			)
		)
	)
)

(procedure (SelectCharacter)
	(= heroType (- activeSelection 1))
	(ToCharAlloc)
)

(procedure (SelectAvatar)
	(= egoAvatar (- activeSelection 1))
	(SetJobRoom)
)

(procedure (IncrementSelection)
	(if (== activeSelection selectCount)
		(= activeSelection 1)
	else 
		(++ activeSelection)
	)
)

(procedure (DecrementSelection)
	(if (== activeSelection 1)
		(= activeSelection selectCount)
	else
		(-- activeSelection)
	)
)

(procedure (InitImages)
		;selected border.
		((= viewSelectedBorder (View new:))
			view: vCharSelect setLoop: 0 setCel: 0
			posn: 0 1000 ;start offscreen
			setPri: 5
			init: ignoreActors: stopUpd: ;hide:
		)
		((= propFighter (Prop new:))
			;Selected Fighter animation
			setPri: 5
			init: ignoreActors: stopUpd: hide:
		)
		((= propMagicEffect (Prop new:)) ;magic circle...
			setLoop: 8 setCel: 0
			posn: 158 84
			setPri: 6
			init: ignoreActors: hide:
		)
		((= propMagicUser (Prop new:))
			setPri: 5
			init: ignoreActors: stopUpd: hide:
		)
		((= propThief (Prop new:))
			;view: (GetEgoViewNumber vEgoCharSelect)
			setPri: 5
			init: ignoreActors: stopUpd: hide:
		)
		;now the on-screen Text
		((= vwTextChoose (View new:)) ;Choose
			view: vCharSelect setLoop: 1 setCel: 0
			posn: 83 25
			init: ignoreActors: stopUpd: hide:
		)
		((= vwTextYour (View new:)) ;Your
			view: vCharSelect setLoop: 1 setCel: 1
			posn: 146 27
			init: ignoreActors: stopUpd: hide:
		)
		((= vwTextCharacter (View new:)) ;Character/Profession/Avatar
			view: vCharSelect setLoop: 1 setCel: 2
			posn: 220 27
			init: ignoreActors: stopUpd: hide:
		)
		((= vwTextFighter (View new:)) ;Fighter / Male
			view: vCharSelect setLoop: 1 setCel: 3
			posn: 65 155
			init: ignoreActors: stopUpd: hide:
		)
		((= vwTextMagic (View new:)) ;Magic / Female
			view: vCharSelect setLoop: 1 setCel: 4
			posn: 159 147
			init: ignoreActors: stopUpd: hide:
		)
		((= vwTextUser (View new:)) ;User (second half of Magic User)
			view: vCharSelect setLoop: 1 setCel: 6
			posn: 161 163
			init: ignoreActors: stopUpd: hide:
		)
		((= vwTextThief (View new:)) ;Thief / Other
			view: vCharSelect setLoop: 1 setCel: 5
			posn: 252 153
			init: ignoreActors: stopUpd: hide:
		)
)

;this sets the room to allow an Avatar to be chosen.
;NOTE: if maxAvatars is less than 2, then this is never shown and 
;the game proceeds straight to the character select screen
(procedure (SetAvatarRoom)
	(= selectCount maxAvatars)
	(= avatarScreen TRUE)

	;(self setScript: selScript)
	(= defaultPalette 1)
	(if (== selectCount 2)
		(self drawPic: 904) ;draw the 2-person avatar selection screen
	else
		(self drawPic: 905) ;draw the default 3-person pic
	)
	;Avatar Select Screen
	(= [scripts 1] maleScript)
	(= [scripts 2] femaleScript)
	(= [scripts 3] otherScript)

	;set the 3 profession selections.
	(propFighter ;selection prop #1
		view: vEgoCharSelect
		setLoop: 0 setCel: 0 ;disabled default blank avatar
		setScript: maleScript
		show:
	)
	(propMagicUser ;selection prop #2
		view: (+ vEgoCharSelect (* VIEW_PER_AVATER 1))
		setLoop: 0 setCel: 0 ;disabled default blank avatar
		setScript: femaleScript
		show:
	)
	(if (== selectCount 2)
		(propFighter posn: 89 117)
		(propMagicUser posn: 230 117)
		(propThief ;selection prop #3
			hide:
		)
	else
		(propFighter posn: 64 117)
		(propMagicUser posn: 158 117)
		(propThief ;selection prop #2
			view: (+ vEgoCharSelect (* VIEW_PER_AVATER 2)) ;and use the 3rd avatar pic
			setLoop: 0 setCel: 0 ;disabled default blank avatar
			posn: 252 117
			setScript: otherScript
			show:
		)
	)

	(propMagicEffect hide:)
	
	(vwTextChoose 	posn: 102 25 show:)
	(vwTextYour 	posn: 165 27 show:)
	(vwTextCharacter posn: 220 27 setLoop: 1 setCel: 7 show:) ;alternates between "Character" and "Avatar" (or maybe later "Profession" and "Avatar")
	(if (== selectCount 2)
		(vwTextFighter	posn:  88 147 setLoop: 3 setCel: 0 show:) ;88
		(vwTextMagic 	posn: 230 147 setLoop: 3 setCel: 1 show:) ;230
		(vwTextThief	hide:) ;we're only showing 2, so this one can be hidden
	else
		(vwTextFighter	posn:  63 147 setLoop: 3 setCel: 0 show:)
		(vwTextMagic 	posn: 159 147 setLoop: 3 setCel: 1 show:)
		(vwTextThief	posn: 254 147 setLoop: 3 setCel: 2 show:)
	)
	(vwTextUser		hide:)
	(viewSelectedBorder setCel: 0 posn: 0 1000)
	;(= activeSelection 0)
	;(IncrementSelection)
	;(UpdateCharacterAnimations)
	(self setScript: selScript)
)

;this allows the Room to be set to choose a profession/job
(procedure (SetJobRoom)
	(if (== egoAvatar 1)
		(DevPrint 202 0)
		;Developer Note:\nThe second avatar is a Work In Progress. None of the animations have been changed yet.\n\nIf you would like to contribute, please contact the developer.
	)

	(if (== egoAvatar 2)
		(DevPrint 202 1)
		;Developer Note:\nThe third avatar is a Proof of Concept only. All of the animations are inverted colours, for debugging purposes.\nAny occurances of the hero in the standard palette are bugs and should be reported to the developer.
	)


	(= titleFlashCount 0) ;this allows the title to flash again
	(= selectCount 3)
	(= avatarScreen FALSE)

	(= defaultPalette 0)
	(self drawPic: 905) ;draw the default room pic (which is the 3-person room).
	(= [scripts 1] fighterScript)
	(= [scripts 2] mageScript)
	(= [scripts 3] thiefScript)

	;set the 3 profession selections.
	(propFighter ;selection prop #1
		view: (GetEgoViewNumber vEgoCharSelect)
		setLoop: 2 setCel: 0 ;disabled fighter
		posn: 64 117
		setScript: fighterScript
		show:
	)
	(propMagicUser ;selection prop #2
		view: (GetEgoViewNumber vEgoCharSelect)
		setLoop: 4 setCel: 0 ;disabled magic user
		posn: 158 117
		setScript: mageScript
		show:
	)
	(propThief ;selection prop #3
		view: (GetEgoViewNumber vEgoCharSelect)
		setLoop: 6 setCel: 0 ;disabled thief
		posn: 252 117
		setScript: thiefScript
		show:
	)
	(propMagicEffect view: (GetEgoViewNumber vEgoCharSelect) setLoop: 8 hide:)
	
	(vwTextChoose 	posn: 83 25 show:)
	(vwTextYour 	posn: 146 27 show:)
	(vwTextCharacter posn: 220 27 setLoop: 1 setCel: 2 show:) ;alternates between "Character" and "Avatar" (or maybe later "Profession" and "Avatar")
	(vwTextFighter	posn:  65 155 setLoop: 1 setCel: 3 show:)
	(vwTextMagic 	posn: 159 147 setLoop: 1 setCel: 4 show:)
	(vwTextUser		posn: 161 163 setLoop: 1 setCel: 6 show:)
	(vwTextThief	posn: 252 153 setLoop: 1 setCel: 5 show:)

	(viewSelectedBorder setCel: 0 posn: 0 1000)

	;(= activeSelection 0)
	;(IncrementSelection)
	;(UpdateCharacterAnimations)
	(self setScript: selScript)
)

(instance selChar of Room
	(properties
		picture 905
		style DISSOLVE
	)
	
	(method (init)
		(super init:)
		(Load rsSOUND (GetSongNumber 73))
		(User canInput: FALSE)
		(InitImages)
		(if (<= maxAvatars 1)
			;there's only 1 avatar defined, so by definition, it must be avatar 0.
			(= egoAvatar 0) 
			;Character/Profession Select Screen
			(SetJobRoom)
		else
			(SetAvatarRoom)
		)
		(mouseDownHandler add: self)
		(keyDownHandler add: self)
		(directionHandler add: self)
		(if
			(or
				(== (cSound state?) 0)
				(!= (cSound number?) (GetSongNumber 61))
			)
			(cSound number: (GetSongNumber 73) loop: -1 play:)
		)
		(cSound prevSignal: 0)
		(Joystick JoyRepeat 30)
	)
	
	(method (doit)
		(cond 
			((and
					local17
					(or
						(== (cSound signal?) 2)
						(== (cSound prevSignal?) 2)
					)
				)
				(= local17 FALSE)
				(cSound stop:)
				(cSound number: (GetSongNumber 73) loop: -1 play:)
			)
			((and 
					local15 
					(== (cSound prevSignal?) 3)
				)
				(cSound prevSignal: 0)
				(= local14 FALSE)
				(IncrementSelection)
				(UpdateCharacterAnimations)
			)
			(local16
				 (= local16 FALSE)
				 (if avatarScreen
				 	;this is only the Avatar select screen,
				 	; so we select our Avatar, then reset the screen, so we can now pick a profession.
				 	(SelectAvatar)
				 else
				 	;it's the choose a profession screen, so we now choose our chatacer, and continue with the game
				 	(SelectCharacter)
				 )
			)
		)
		(super doit:)
	)
	
	(method (dispose)
		(Joystick JoyRepeat 0)
		(super dispose:)
	)
	
	(method (handleEvent pEvent &tmp tmpOnControl mX mY [temp3 60])
		(if local14
			(if (== (pEvent type?) keyDown)
				(= local15 FALSE)
				(switch (pEvent message?)
					(TAB
						(pEvent type: direction)
						(pEvent message: dirE)
					)
					(SHIFTTAB
						(pEvent type: direction)
						(pEvent message: dirW)
					)
					(ENTER
						(pEvent claimed: TRUE)
						(= local16 TRUE)
					)
				)
			)
			(switch (pEvent type?)
				(mouseDown
					(= mX (pEvent x?))
					(= mY (pEvent y?))
					(= tmpOnControl (OnControl CMAP mX mY))
					(= local15 FALSE)
					(pEvent claimed: TRUE)
					(cond 
						((& tmpOnControl cYELLOW)
							(= activeSelection 1)
							(= local16 TRUE)
						)
						((& tmpOnControl cLMAGENTA)
							(= activeSelection 2)
							(= local16 TRUE)
						)
						((& tmpOnControl cLRED)
							(= activeSelection 3)
							(= local16 TRUE)
						)
						(else (pEvent claimed: FALSE))
					)
					(pEvent claimed: TRUE)
					(= local14 FALSE)
					(UpdateCharacterAnimations)
				)
				(direction
					(switch (pEvent message?)
						(dirW
							(DecrementSelection)
						)
						(dirE
							(IncrementSelection)
						)
					)
					(pEvent claimed: TRUE)
					(= local15 FALSE)
					(= local14 FALSE)
					(UpdateCharacterAnimations)
				)
			)
		)
		(super handleEvent: pEvent)
	)
)

(instance fighterScript of Script
	(properties)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(= seconds (= cycles 0)) ;and we wait here until manually moved to state 2
				;set to the disabled view.
				(client setLoop: 2 setCel: 0); posn: 64 117)
			)
			(1
				(= seconds (= cycles 0))
				;(= activeSelection 1)
				;set to the enabled view, second animation
				(client setLoop: 3 setCel: 0)
				(= local14 TRUE)
				(= cycles 5)
			)
			(2
				;back to the first animation
				(client setCel: 1)
				(= cycles 5)
			)
			(3
				;and repeat.
				(self changeState: 1)
			)
		)
	)
)

(instance mageScript of Script
	(properties)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				;(Print {Mage State0})
				(= seconds (= cycles 0)) ;and we wait here until manually moved to state 2
				;set to the disabled view
				(client setLoop: 4 setCel: 0 setCycle: NULL); posn: 158 117)
				(propMagicEffect stopUpd: hide:)
			)
			(1
				;(Print {Mage State1})
				(= seconds (= cycles 0))
				;(= activeSelection 2)
				(client setLoop: 5 setCel: 0)
				(= local14 TRUE)
				(= cycles 3)
			)
			(2
				;(Print {Mage State2})
				(client setCel: RELEASE setCycle: EndLoop self)
			)
			(3
				;(Print {Mage State3})
				(propMagicEffect show: setCycle: Forward startUpd:)
				(= seconds 3)
			)
			(4 
				;(Print {Mage State4})
				(self changeState: 3)
			)
		)
	)
)

(instance thiefScript of Script
	(properties)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(= seconds (= cycles 0)) ;and we wait here until manually moved to state 2
				;set to the disabled view
				(client setLoop: 6 setCel: 0 setCycle: NULL); posn: 252 117)
			)
			(1
				(= seconds (= cycles 0))
				;(= activeSelection 3)
				(client setLoop: 7 setCel: 0)
				(= local14 TRUE)
				(= seconds 2)
			)
			(2
				(client setCel: RELEASE setCycle: EndLoop)
				(= seconds 2)
			)
			(3
				(client setCycle: BegLoop self)
			)
			(4 (self changeState: 1))
		)
	)
)

(instance maleScript of Script
	(properties)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(= seconds (= cycles 0)) ;and we wait here until manually moved to state 2
				;set to the disabled view.
				(client setLoop: 0 setCel: 0); posn: 89 117)
			)
			(1
				(= seconds (= cycles 0))
				;(= activeSelection selectFighter)
				;selectedAvatar = Male
				;set to the enabled view, second animation
				(client setLoop: 1 setCel: 0)
				(= local14 TRUE)
				(= cycles 5)
			)
			(2
				;back to the first animation
				(client setCel: 0)
				(= cycles 5)
			)
			(3
				;and repeat.
				(self changeState: 1)
			)
		)
	)
)

(instance femaleScript of Script
	(properties)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(= seconds (= cycles 0)) ;and we wait here until manually moved to state 2
				;set to the disabled view.
				(client setLoop: 0 setCel: 0); posn: 230 117)
			)
			(1
				(= seconds (= cycles 0))
				;(= activeSelection selectFighter)
				;selectedAvatar = Male
				;set to the enabled view, second animation
				(client setLoop: 1 setCel: 0)
				(= local14 TRUE)
				(= cycles 5)
			)
			(2
				;back to the first animation
				(client setCel: 0)
				(= cycles 5)
			)
			(3
				;and repeat.
				(self changeState: 1)
			)
		)
	)
)

(instance otherScript of Script
	(properties)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(= seconds (= cycles 0)) ;and we wait here until manually moved to state 2
				;set to the disabled view.
				(client setLoop: 0 setCel: 0); posn: 230 117)
			)
			(1
				(= seconds (= cycles 0))
				;(= activeSelection selectFighter)
				;selectedAvatar = Male
				;set to the enabled view, second animation
				(client setLoop: 1 setCel: 0)
				(= local14 TRUE)
				(= cycles 5)
			)
			(2
				;back to the first animation
				(client setCel: 0)
				(= cycles 5)
			)
			(3
				;and repeat.
				(self changeState: 1)
			)
		)
	)
)

;blinks the main title 3 times before allowing a selection to be made
(instance selScript of Script
	(properties)
	
	(method (doit)
		(if
			(and
				(== state 3)
				(not local15)
				(== (cSound prevSignal?) 3)
			)
			(cSound prevSignal: 0)
			(= local14 TRUE)
			(= local15 TRUE)
			;start the selection process
			(= activeSelection 0)
			(IncrementSelection)
			(UpdateCharacterAnimations)
			;(fighterScript changeState: 1) ;the first profession
			(self dispose:)
		else
			(super doit:)
		)
	)
	
	(method (changeState newState)
		(switch (= state newState)
			(0 (= cycles 5))
			(1
				(++ titleFlashCount)
				(vwTextChoose hide:)
				(vwTextYour hide:)
				(vwTextCharacter hide:)
				(= cycles 3)
			)
			(2
				(vwTextChoose show:)
				(vwTextYour show:)
				(vwTextCharacter show:)
				(= cycles 3)
			)
			(3
				(cSound prevSignal: 0)
				(if (< titleFlashCount 2) (self changeState: 1))
			)
		)
	)
)
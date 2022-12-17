;;; Sierra Script 1.0 - (do not remove this comment)
(script# DEBUG_RM)
(include system.sh) (include sci2.sh) (include game.sh)
(use Main)
(use _Interface)
(use _Game)
(use _User)
(use _Actor)
(use _System)

(public
	debugRm 0
)

(instance debugRm of Locale
	(properties)
	
	(method (init)
		(super init:)
		(mouseDownHandler add: self)
		(keyDownHandler add: self)
	)
	
	(method (dispose)
		(mouseDownHandler delete: self)
		(keyDownHandler delete: self)
		(super dispose:)
	)
	
	(method (handleEvent event &tmp newEvent temp1 [temp2 2] gCastFirst i [str 40])
		(if (or (not razzleDazzleRootBeer) (event claimed?))
			(return)
		)
		(switch (event type?)
			(mouseDown
				(if (& (event modifiers?) emSHIFT)
					;right-click will show the x/y coordinates of the mouse
					(= temp1
						(Print
							(Format @str DEBUG_RM 0 (event x?) (event y?))
							;%d/%d
							#at 150 100
							#font 999
							#dispose
						)
					)
					(while (!= mouseUp ((= newEvent (Event new:)) type?))
						(newEvent dispose:)
					)
					(temp1 dispose:)
					(newEvent dispose:)
				)
			)
			(keyDown
				(event claimed: TRUE)
				(switch (event message?)
					(KEY_QUESTION
						(Print DEBUG_RM 1)
						; Key commands:
						; ALT-S Show cast
						; ALT-M Show Memory
						; ALT-T Teleport
						; ALT-V Visual
						; ALT-P Priority
						; ALT-C Control
						; ALT-I Get InvItem
						; ALT-B Set Ego's Bucks
						; ALT-K Set one of Ego's sKills
						; ALT-X Make Ego eXtra special
					)
					(KEY_ALT_s
						(= gCastFirst (cast first:))
						(Printf {There are %d items in the cast.} (cast size?))
						(while gCastFirst
							(= temp1 (NodeValue gCastFirst))
							(Print
								(Format
									@str
									DEBUG_RM 2
									;view: %d\nx:%d y:%d\n%s %s illB=$%x
									;CI: NOTE: updated the debug Text to be:
									;V: %d L: %d C: %d\nx:%d y:%d\n%s %s illB=$%x
									(temp1 view?)
									(temp1 loop?)
									(temp1 cel?)
									(temp1 x?)
									(temp1 y?)
									(if (& (temp1 signal?) notUpd) {stopUpd:\0A} else {})
									(if (& (temp1 signal?) ignrAct) {ignoreActors:\0A} else {})
									(if
										(or
											(== (temp1 superClass?) Actor)
											(== (temp1 superClass?) Ego)
										)
										(temp1 illegalBits?)
									else
										$FFFF
									)
								)
								#title (temp1 name?)
								#icon
								(temp1 view?)
								(temp1 loop?)
								(temp1 cel?)
							)
							(= gCastFirst (cast next: gCastFirst))
						)
					)
					(KEY_ALT_y
						;display all addToPics
						(= gCastFirst (addToPics first:))
						(while gCastFirst
							(= temp1 (NodeValue gCastFirst))
							(Print
								(Format
									@str
									DEBUG_RM 2
									;view: %d\nx:%d y:%d\n%s %s illB=$%x
									(temp1 view?)
									(temp1 x?)
									(temp1 y?)
									{}
									{}
									{}
									;(if (& (temp1 signal?) notUpd) {stopUpd:\0A} else {})
									;(if (& (temp1 signal?) ignrAct) {ignoreActors:\0A} else {})
;;;									(if
;;;										(or
;;;											(== (temp1 superClass?) Actor)
;;;											(== (temp1 superClass?) Ego)
;;;										)
;;;										(temp1 illegalBits?)
;;;									else
;;;										$FFFF
;;;									)
								)
								#title (temp1 name?)
								#icon
								(temp1 view?)
								(temp1 loop?)
								(temp1 cel?)
							)
							(= gCastFirst (cast next: gCastFirst))
						)
					)
					(KEY_ALT_e
						(Format
							@str
							DEBUG_RM 3
							;ego\nx:%d y:%d\nloop:%d\ncel:%d
							(ego x?)
							(ego y?)
							(ego loop?)
							(ego cel?)
						)
						(Print @str #icon (ego view?) 6 0 7 0)
					)
					(KEY_ALT_i
						(= gCastFirst (GetNumber {ID number of the object?}))
						(ego get: gCastFirst)
					)
					(KEY_ALT_t
						(curRoom newRoom: (GetNumber {Which room number?}))
					)
					(KEY_ALT_v (Show VMAP))
					(KEY_ALT_p (Show PMAP))
					(KEY_ALT_c (Show CMAP))
					(KEY_ALT_m (theGame showMem:))
					(KEY_ALT_b
						(Print (Format @str DEBUG_RM 4 [invNum iSilver]))
						;Our Hero has %d silvers.
						(= [invNum iSilver] (GetNumber {Enter Silvers.}))
					)
					(KEY_ALT_k
						(= i (GetNumber {Change which Stat/Skill?}))
						(= [egoStats i]
							(GetNumber {Enter new value:} [egoStats i])
						)
					)
					(KEY_ALT_x
						(= temp1 (GetNumber {What should each skill be?} 80))
						(= i 0)
						(while (< i 25)
							(= [egoStats i] temp1)
							(++ i)
						)
						(= [egoStats EXPER] 1900)
						(= [egoStats HEALTH] (MaxHealth))
						(= [egoStats STAMINA] (MaxStamina))
						(= [egoStats MANA] (MaxMana))
						(Print DEBUG_RM 5)
						; Why, you feel better already!
					)
					;CI: NOTE: I've added this additional debug entry (alt-y) for whatever I want to display.
					;in this case, I was trying to find out what's going on in skillThreshold.
					;(KEY_ALT_y	
					;	(Print (Format @str "skillThreshold Vitality: %d" 
					;			[egoStats (= [skillTicks VIT]
					;			(+ [skillTicks VIT] 10)
					;			)]
					;			))
					;)
					(else  (event claimed: FALSE))
				)
			)
		)
	)
)

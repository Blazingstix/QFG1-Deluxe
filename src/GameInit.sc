;;; Sierra Script 1.0 - (do not remove this comment)
(script# 1)
(include system.sh) (include sci2.sh) (include game.sh)
(use Main)
(use _User)
(use _Menu)
(use _Interface)

(public
	SetGameInit 0
	SetGraphicsSoundInit 1
)

(procedure (SetGameInit &tmp [temp0 21])
	(User alterEgo: ego)
	(User prompt: {} blocks: 0 y: 160)
	(= showStyle HSHUTTER)
	(= razzleDazzleRootBeer FALSE)
	(TheMenuBar init:)
	(= thievesGuildPassword (Random 0 6))
	(= possibleScore 500)
	(= userFont 300)
	(= smallFont 999)
	(= bigFont 300)
	(= timerStamina 20)
	(= timerHealth 15)
	(= timerMana 5)
	(SetGraphicsSoundInit)
	(FixTime 11)
	(DoSound sndVOLUME 15)
	(HandsOn)
	(if (HaveMouse)
		;set the cursor. if the user has a mouse, show the cursor wherever the left it.
		(theGame setCursor: normalCursor TRUE)
	else
		;if there's no mouse, show the cursor, but set it in the lower right corner.
		(theGame setCursor: normalCursor TRUE 304 174)
	)
	(Bset SAVE_ENABLED)
	(Joystick 12 0)

	(if (GameIsRestarting)
		(= startingRoom 9)	;if restarting, go to the Game Select Screen
	else
		(= startingRoom 2)	;otherwise, go to the PIRACY notice screen
	)
	
	(ReadFileVariables)
	;(SetDebugVariables)
	
	(theGame newRoom: 299)	;but first, we initialize in the speedCheck room.
)

(procedure (SetGraphicsSoundInit)
	(= musicChannels (DoSound sndCHECK_DRIVER))
	(if (< (= colourCount (Graph grGET_COLOURS)) 8)
		;CGA
		(= hpStatusView vFightUiCGA)
		(= hpFontColour vWHITE)
		(= statColour vBLACK)
		(= statColourNew vRED)
	else
		;EGA
		(= hpStatusView vFightUI)
		(= hpFontColour vLCYAN)
		(= statColour vLBLUE)
		(= statColourNew vLRED)
	)
)

;this attempts to see if files exist, and if they do, sets appropriate flags.
(procedure (ReadFileVariables &tmp fileHandle)
	;first check for the Avatars file.
	;if the file "3AVATARS" exists, then we show all 3 possible avatar selctions
	;if it doesn't, then we check for "2AVATARS" which will show the male/female avatar selections
	;if neither file exists, then it defaults to the single avatar selection.
	(if (!= -1 (= fileHandle (FOpen "3AVATARS" fRead)))
		(= maxAvatars 3)
		(FClose fileHandle)
	else
		(if (!= -1 (= fileHandle (FOpen "2AVATARS" fRead)))
			(= maxAvatars 2)
			(FClose fileHandle)
		else
			(= maxAvatars 1)
		)
	)


	;now check if we should let the town shed be accessible
	;if the file "SHEDOPEN" exists, then we'll unlock the shed door
	(if (!= -1 (= fileHandle (FOpen "SHEDOPEN" fRead)))
		(= shedIsAccessible TRUE)
		(FClose fileHandle)
	else
		(= shedIsAccessible FALSE)
	)
)

(procedure (SetDebugVariables &tmp i)
	;CI: NOTE: This is set for debugging purposes
	(= sillyClowns TRUE)

	;NOTE: CI: egoAvatar will be chosen in a new screen before choosing the character class.
	;but for now for testing purposes, we'll default to avatar 2
	(= egoAvatar 2)

	;set up a good hero
	(Format @userName {TesterMan})
	;(= userName {TesterMan})
	(= i 0)
	(while (< i 25)
		(= [egoStats i] 50)
		(++ i)
	)
	(= [egoStats EXPER] 500)
	(= [egoStats HEALTH] (MaxHealth))
	(= [egoStats STAMINA] (MaxStamina))
	(= [egoStats MANA] (MaxMana))
	(Print DEBUG_RM 5) ;you feel much better already.
	
	;(= [egoStats PICK] 100)
	
	(= speed 10)
	
	(= heroType FIGHTER)
	;(= egoAvatar 1)
	(= [invNum iGold] 4)
	(= [invNum iSilver] 10)
	(= [invNum iRations] 5)

	;(+= [invNum iGold] 100)

	(= [invNum iSword] 1)
	(= [invNum iShield] 1)
	(= [invNum iDagger] 1)
	(= [invNum iLeather] 1)
	(= [invNum iDisenchant] 1)
	(= [invNum iMagicMirror] 1)

	(= [invNum iLockPick] 1)
	(= [invNum iDagger] 5)

	(Bset fInMainGame)
	(Bset ANTWERP_SPLIT)
	(Bset POINTS_FREEBEAR)
	(Bset POINTS_KILLFOX)
	;(Bset BABAYAGA_CURSE)
	;(= dayCursedByBabaYaga 1)
	;(Bset DEFEATED_KOBOLD)
	;(= [egoStats STR] 40) ;set strength high enough to open the chest.
	;(= [egoStats PICK] 20)
	;(= [egoStats STEALTH] 30)
	;(= [egoStats HEALTH] 10)

	(= curRoomNum 96) ;Yorick's Maze
	(= prevRoomNum 96)
	;(= startingRoom 330) ;outside the tavern
	(= startingRoom 97) ;character save screen
	;(Bset DEFEATED_MINOTAUR)
	;(Bset BRIGANDS_UNAWARE)
	;(Bset SAVED_ELSA)
	
	
	;(= currentDay 2)
	;(= currentTime 850)
	;(ego posn: 140 140)
	;(Bset 1)
 	;(Printf "eventFlags: %04x %04x %04x %04x %04x" [eventFlags 0] [eventFlags 1] [eventFlags 2] [eventFlags 3] [eventFlags 4])
)

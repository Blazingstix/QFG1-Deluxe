;;; Sierra Script 1.0 - (do not remove this comment)
;charsave.sc   Quest for Glory...save character stats.

(script# CHARSAVE) ;CHARSAVE = 601
(include system.sh) (include sci2.sh) (include game.sh)
(use Main)
(use _Interface)
(use _File)
(use _Game)
(use _User)
(use _System)

(public
	CharSave 0
)
;; Bits in svMiscEquip
(define  SWORD_BIT   $01)  ; 1
(define  CHAIN_BIT   $02)  ; 2
(define  PICK_BIT    $04)  ; 4
(define  TOOL_BIT    $08)  ; 8
(define  MIRROR_BIT  $10)  ;16
(define  BABA_BIT    $20)  ;32
(define  SCORE_BIT   $40)  ;64

(define  EXTRA_DATA  18)   ; Data items other than stats and name
(define  CHECK_DATA  10)   ; Data items that are in check sums

;;Bogus bytes
; the bogus values were originally conceived as a way of deterring casual file hacking
; however since they're never checked in any game import, we can use them as 6 additional variables
; to maintain compatibility with exsiting saves, we'll consider these to be XOR values to apply
; against the actual variable value.
(define  VAL_BOGUS0	$79)
(define  VAL_BOGUS1	$86)
(define  VAL_BOGUS2	$43)
(define  VAL_BOGUS3	$88)
(define  VAL_BOGUS4	$ad)
(define  VAL_BOGUS5	$f0)

;bonus variables 1of6 (game mod signature)
(define QFGDELUXE_BIT      	$01)  ; 1 ; a flag that indicates this is from QFG1Deluxe
(define RESERVED1_BIT      	$02)  ; 2 ; reserved for QFG1Deluxe VGA (if I ever make it)

;bonus variables 2of6
(define AVATAR2_BIT	    	$01)  ; 4 ; avatar 2 was the player's avatar
(define AVATAR3_BIT	    	$02)  ; 8 ; avatar 3 was the player's avatar
(define ANTWERP_POP_BIT    	$04)  ;16 ; the user exploded the antwerp
(define KILLED_BERNARD_BIT 	$08)  ;32 ; the user killed bernard, instead of rescuing him
(define FREED_BERNARD_BIT  	$10)  ;64 ; the user rescued bernard.
(define RESERVED2_BIT		$20)
(define RESERVED3_BIT		$40)

;bonus variables 3of6
(define KILLEDFOX_BIT 		$01)
(define FREEDFOX_BIT		$02)
(define MINOTAUR_BIT		$04)
(define OGRE_BIT			$08)
(define KOBOLD_BIT			$10)
(define FRED_BIT			$20)
(define WEAPONMASTER_BIT	$40)

;bonus variables 4of6
(define GAVEALMS_BIT 		$01)
(define SIGNLOGBOOK_BIT		$02)
(define SAWBLACKBIRD_BIT	$04)
(define RESERVED4_BIT		$08)
(define RESERVED5_BIT		$10)
(define RESERVED6_BIT		$20)
(define RESERVED7_BIT		$40)

;bonus variable 5of6
; number of rations

;bonus variable 6of6
; number of total deaths (up to 127)

(define CHOPMASK $007f) ;these are the only bits that are included in the export... 127 bits.

(local
;; local data for saving hero stats for next game
                        ;;;;;;;;;;;;;;;;;;start;;;;;;;;;;;;;;;;;;
	statsKey =  $53		;;;;;;;;order dependent variables;;;;;;;;
	;start of the character saved variables
	svCharType			;;;;;;;;order dependent variables;;;;;;;;
	svHighGold			;;;;;;;;order dependent variables;;;;;;;;
	svLowGold			;;;;;;;;order dependent variables;;;;;;;;
	svScore				;;;;;;;;order dependent variables;;;;;;;;
	svMiscEquip			  ;;;;;;order dependent variables;;;;;;;;
	[codedStats NUMSTATS] ;;;;;;order dependent variables;;;;;;;;
	svDaggers			  ;;;;;;order dependent variables;;;;;;;;
	svHealing			;;;;;;;;order dependent variables;;;;;;;;
	svMana				;;;;;;;;order dependent variables;;;;;;;;
	svStamina			;;;;;;;;order dependent variables;;;;;;;;
	svGhostOil			;;;;;;;;order dependent variables;;;;;;;;
	bogus0 =  VAL_BOGUS0		;;;;;;;;order dependent variables;;;;;;;;
	bogus1 =  VAL_BOGUS1		;;;;;;;;order dependent variables;;;;;;;;
	checkSum1			;;;;;;;;order dependent variables;;;;;;;;
	checkSum2			;;;;;;;;order dependent variables;;;;;;;;
	bogus2 =  VAL_BOGUS2		;;;;;;;;order dependent variables;;;;;;;;
	bogus3 =  VAL_BOGUS3		;;;;;;;;order dependent variables;;;;;;;;
	bogus4 =  VAL_BOGUS4		;;;;;;;;order dependent variables;;;;;;;;
	bogus5 =  VAL_BOGUS5		;;;;;;;;order dependent variables;;;;;;;;
	;end of the character saved variables.
	checkSumKey =  $ce	;;;;;;;;order dependent variables;;;;;;;;
						;;;;;;;;;;;;;;;;;;;end;;;;;;;;;;;;;;;;;;;
	[check 2]
	[YNSTR 5]
	[heroFileName 16]
	[bigStr 400]
	hasSaved			;TRUE if hero saved
	[str 40]
)

(enum          ;states of saveHero Script
   askSave
   getInfoFileName
   getInfoFileName2
   openFile
   writeHeroInfo
   writeComplete
   tryAgain
   badAnswer
   saveDone
)

(instance CharSave of Room
	(properties
		picture pBlueSkyForCarpet
		style IRISOUT
	)
	
	(method (init)
		(StatusLine code: endStatus enable:)
		(super init: &rest)
		(cSound stop:)
		
		; don't let'm control anything!
		(User canControl: FALSE canInput: FALSE)
		
		(self setScript: saveHero)
	)
	
	(method (dispose)
		(StatusLine code: dftStatusCode)
		(super dispose:)
	)
)

(instance heroinfo of File
	(properties
		name "glory1.sav"
	)
)

(instance saveHero of Script
	(properties)
	
	(method (changeState newState &tmp whichSkill oldGold)
		(switch (= state newState)
			(askSave
				(Format @heroFileName 601 0)
				;a:glory1.sav
				(if (>= score 500)
					(HighPrint 601 1)
					;CONGRATULATIONS!!  You have successfully completed "Quest for Glory I: So You Want To Be A Hero"
					;with the maximum possible score, 500 of 500!!
					;We welcome you to the ranks of the few, the proud, the True Heroes!
				else
					(HighPrint (Format @bigStr 601 2 score))
					;Congratulations!  You have successfully completed "Quest for Glory I:  So You Want To Be A Hero."
					;Your final score was %d of 500 possible Puzzle Points.
				)
				(HighPrint 601 3)
				;If you have not already done so, we encourage you to play "Quest for Glory I" again with the other two
				;character types; many of the puzzles are different, or have alternate solutions.
				(HighPrint 601 4)
				;In the meantime, you are already a winner!  Please insert a writeable disk in your floppy drive to save your winning Hero for use in
				;"Quest for Glory II:  Trial By Fire."
				(self cue:)
			)
			(getInfoFileName
				(= cycles 2)
				)
			(getInfoFileName2
				(if
					(GetInput
						@heroFileName
						30
						{Disk file in which to save your Hero.}
					)
					(heroinfo name: @heroFileName)
					(= cycles 2)
				else
					(self changeState: tryAgain)
				)
			)
			(openFile
				(if (heroinfo open: fTrunc)
					(heroinfo close:)
					(= seconds 2)
				else
					(HighPrint (Format @bigStr 601 5 (heroinfo name?)))
					;Could not create file -- %s.
					(self changeState: tryAgain)
				)
			)
			(writeHeroInfo
				(if (not (heroinfo open: fAppend))
					(self changeState: tryAgain)
					(return)
				)
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
				;load up the stats into the codedStats cache
				(= whichSkill 0)
				(while (< whichSkill NUMSTATS)
					(= [codedStats whichSkill] [egoStats whichSkill])
					(++ whichSkill)
				)
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
				(= oldGold (+ [invNum iGold] (/ [invNum iSilver] 10)))
				(= svCharType heroType)
				(= svHighGold (/ oldGold 100))
				(= svLowGold (mod oldGold 100))
				(= svScore score)
				(= svMiscEquip 0)	;reset the all MiscEquip flags to empty.
				(if (ego has: iSword) 		(= svMiscEquip (| svMiscEquip SWORD_BIT)))
				(if (ego has: iChainmail)	(= svMiscEquip (| svMiscEquip CHAIN_BIT)))
				(if (ego has: iLockPick) 	(= svMiscEquip (| svMiscEquip PICK_BIT)))
				(if (ego has: iThiefKit) 	(= svMiscEquip (| svMiscEquip TOOL_BIT)))
				(if (ego has: iMagicMirror) (= svMiscEquip (| svMiscEquip MIRROR_BIT)))
				(if (Btst fBabaFrog)		(= svMiscEquip (| svMiscEquip BABA_BIT)))
				(if (< 255 score) 			(= svMiscEquip (| svMiscEquip SCORE_BIT)))
				(= svDaggers [invNum iDagger])
				(= svHealing [invNum iHealingPotion])
				(= svMana [invNum iManaPotion])
				(= svStamina [invNum iStaminaPotion])
				(= svGhostOil [invNum iGhostOil])
				
				;some quick debugging, to see what the Gold amounts are
				;(HighPrint (Format @bigStr {Gold: %d Silver: %d\nOldGold: %d\n\nHiGold: %d LoGold: %d} [invNum iGold] [invNum iSilver] oldGold svHighGold svLowGold))
				
				;now we have several bonus variables we can use in the deluxe version
				;start with the mode signature bit (variable 1of6)
				(= bogus0 0) ;reset the 1st bonus variable
				(= bogus0 (| bogus0 QFGDELUXE_BIT))
				;(= svMiscEquip (| svMiscEquip RESERVED1_BIT))) ;uncomment for QFG1Deluxe VGA
				
				;bonus variable 2of6
				(= bogus1 0)
				(if (== egoAvatar 1) 		(= bogus1 (| bogus1 AVATAR2_BIT)))
				(if (== egoAvatar 2)		(= bogus1 (| bogus1 AVATAR3_BIT)))
				(if (Btst ANTWERP_SPLIT)	(= bogus1 (| bogus1 ANTWERP_POP_BIT)))
				(if (Btst POINTS_KILLBEAR)	(= bogus1 (| bogus1 KILLED_BERNARD_BIT)))
				(if (Btst SAVED_BARNARD)	(= bogus1 (| bogus1 FREED_BERNARD_BIT)))

				;bonus variable 3of6
				(= bogus2 0)
				(if (Btst POINTS_KILLFOX)		(= bogus2 (| bogus2 KILLEDFOX_BIT)))
				(if (Btst POINTS_FREEFOX)		(= bogus2 (| bogus2 FREEDFOX_BIT)))
				(if (Btst DEFEATED_MINOTAUR)	(= bogus2 (| bogus2 MINOTAUR_BIT)))
				(if (Btst DEFEATED_OGRE)		(= bogus2 (| bogus2 OGRE_BIT)))
				(if (Btst DEFEATED_KOBOLD)		(= bogus2 (| bogus2 KOBOLD_BIT)))
				(if (or (Btst DEFEATED_FRED) (Btst DEFEATED_FRED_89))
												(= bogus2 (| bogus2 FRED_BIT)))
				(if (Btst DEFEATED_WEAPON_MASTER)		(= bogus2 (| bogus2 WEAPONMASTER_BIT)))

				;bonus variable 4of6
				(= bogus3 0)
				(if (Btst POINTS_GIVEALMS)		(= bogus3 (| bogus3 GAVEALMS_BIT)))
				(if (Btst POINTS_SIGNLOGBOOK)	(= bogus3 (| bogus3 SIGNLOGBOOK_BIT)))
				(if (Btst SAW_BLACKBIRD)		(= bogus3 (| bogus3 SAWBLACKBIRD_BIT)))

				(= bogus4 [invNum iRations]) ;rations
				(= bogus5 deathCount) ;total deaths
				
				;we are going to XOR the "bogus" values against their ogirinal bogus amount, 
				;so we can use them as secret "Bonus" values
				;in the Deluxe edition.
				(= bogus0 (^ bogus0 VAL_BOGUS0))
				(= bogus1 (^ bogus1 VAL_BOGUS1))
				(= bogus2 (^ bogus2 VAL_BOGUS2))
				(= bogus3 (^ bogus3 VAL_BOGUS3))
				(= bogus4 (^ bogus4 VAL_BOGUS4))
				(= bogus5 (^ bogus5 VAL_BOGUS5))
				
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;; ;calculate the checksum
				;first the odd bytes
				(= checkSum1 checkSumKey)
				(= whichSkill 0)
				(while (< whichSkill (+ NUMSTATS CHECK_DATA))
					;truncate each odd byte
					(= [statsKey (+ whichSkill 1)] (& [statsKey (+ whichSkill 1)] CHOPMASK))
					;the checksum is the initial checksum + each odd byte
					(= checkSum1 (+ checkSum1 [statsKey (+ whichSkill 1)]))
					(= whichSkill (+ whichSkill 2)) ;increment to the next odd byte
				)

				;then the even bytes
				(= checkSum2 0)
				(= whichSkill 1)
				(while (< whichSkill (+ NUMSTATS CHECK_DATA)) ;replace hard-coded 35 with (+ NUMSTATS CHECK_DATA)
					;truncate each even byte
					(= [statsKey (+ whichSkill 1)] (& [statsKey (+ whichSkill 1)] CHOPMASK))
					;the checksum is 0 + each even byte
					(= checkSum2 (+ checkSum2 [statsKey (+ whichSkill 1)]))
					(= whichSkill (+ whichSkill 2)) ;increment to the next even byte
				)
				(= checkSum1 (& checkSum1 CHOPMASK))
				(= checkSum2 (& checkSum2 CHOPMASK))
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
				
				;now we encode each statsKey value.
				(= whichSkill 0)
				(while (< whichSkill (+ NUMSTATS EXTRA_DATA))
					;truncate each one again... this will re-truncate the 1st NUMSTATS + CHECK_DATA, and will truncate the remaining EXTRA_DATA for the 1st time.
					(= [statsKey (+ whichSkill 1)] (& [statsKey (+ whichSkill 1)] CHOPMASK))
					;then encode the stat starting at the 1st variable working our way down through them all for 25 + 18 data points.
					;we start at the second byte. Each byte is itself XOR'd with the previous byte.
					(= [statsKey (+ whichSkill 1)]
						(^ [statsKey (+ whichSkill 1)] [statsKey whichSkill])
					)
					(++ whichSkill)
				)
				
				;then write the username, a new line, then the encoded stats as a 2-digit code.
				(heroinfo write: @userName)
				(heroinfo write: {\n})
				(= whichSkill 1)
				(while (< whichSkill (+ NUMSTATS EXTRA_DATA 1))
					(Format @bigStr 601 6 [statsKey whichSkill])
					;%2x
					(heroinfo write: @bigStr)
					(++ whichSkill)
				)
				;finish off with another new line; then close out.
				(heroinfo write: {\n})
				(heroinfo close:)
				(= seconds 2)
			)
			(writeComplete
				(HighPrint 601 7)
				;The save character file has been created.  Save this disk for use with "Quest for Glory II:  Trial By Fire" from Sierra On-Line.
				(= hasSaved TRUE)
				(= cycles 2)
			)
			(tryAgain
				(Format @YNSTR 601 8)
				;n
				(if
					(GetInput
						@YNSTR
						2
						{If you wish to try saving your character again, type "y", then ENTER.__Otherwise type "n", then ENTER.}
					)
					(if (StrCmp @YNSTR {y})
						(self changeState: saveDone)
					else
						;reset the bogus values
						;CI:NOTE: We no longer need to reset these values, because of how we're using the bogus values as "Bonus" values.
						;(= bogus0 VAL_BOGUS0)
						;(= bogus1 VAL_BOGUS1)
						;(= bogus2 VAL_BOGUS2)
						;(= bogus3 VAL_BOGUS3)
						;(= bogus4 VAL_BOGUS4)
						;(= bogus5 VAL_BOGUS5)
						(self changeState: getInfoFileName)
					)
				else
					(= cycles 2)
				)
			)
			(badAnswer
				(HighPrint 601 9)
				;Please answer "y" or "n".
				(self changeState: tryAgain)
			)
			(saveDone
				(HighPrint 601 10)
				;Thank you for playing "Quest for Glory I:  So You Want To Be A Hero,"
				;and congratulations again on winning.  We'll see you again soon in "Quest for Glory II:  Trial By Fire."
				(= quit TRUE)
			)
		)
	)
)

(instance endStatus of Code
	(properties)
	
	(method (doit strg)
		(Format strg 601 11 score)
		;   Wow!  You're Really A Hero!  [score %d of 500]
	)
)

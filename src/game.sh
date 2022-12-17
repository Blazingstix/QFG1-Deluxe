;;; Sierra Script 1.0 - (do not remove this comment)
(include system.sh)
(include gameEnumVisited.sh)
(include gameEnumFlags.sh)
(include gameEnumDeath.sh)
(include gameEnumPuzzlePoints.sh)
(include gameViews.sh)
(include gamePics.sh)


(define MAZEBUG 238)

; General Game Defines
(define NUMSTATS 	  25)
(define TICKSPERDAY 3600)	;number of ticks per game day
(define TICKSPERHOUR 150) 	;number of ticks per game hour
(define ONEHOUR		 150)	;another way of describing the number of ticks in an hour (a common wait time for responses).

(define WAIT_DRYAD	      150)
(define WAIT_BABA	      100)
(define WAIT_BONEHEAD     100)
(define WAIT_SHEEMA	       50)
(define WAIT_STABLES	  120)
(define WAIT_WEAPONMASTER 100)

(define WAIT_LONG	   100)
(define WAIT_SHORT		80)
(define WAIT_VERYSHORT	50)

; Priority States
(define LowPri 4) ;pRED
(define HighPri 7)	;pLGREY

;Hero's Quest Framework scripts
(define HQROOM		 896)
(define TIME		 813)
(define TARGETACTOR	 812)
(define CURSORCOORDS 810)
(define TALKOBJ		 803)
(define INPUTCOMP 	 802)
(define DOORS 		 800)
(define CHARSAVE 	 601)
(define ENDGAME		 600)

(define EGODRINK 	 005)
(define EGOEAT 		 006)
(define EGOSLEEP 	 007)
(define EGOREST		 008)

; Locales and Regions
(define DEBUG_RM 298) ;Locale
(define TOWN 	 801) ;Locale
(define FOREST 	 804) ;Locale
(define GHOSTS 	 805) ;Actors and Scripts for CEMETERY
(define CEMETERY 806) ;Region
(define CASTLE 	 807) ;Locale
(define STREET 	 811) ;Locale

;Rooms (these will mostly be any rooms that call external scripts)
(define TAVERN			331)
(define TAVERN_BREATH	335)	;scripts related to pouring the Dragon's Breath drink.
(define TAVERN_DRINK	336)
(define TAVERN_CRUSHER 	337)
(define TAVERN_STOOL	338)
(define TAVERN_LOOK 	339)



;Combat Framework
;arena spell animations
(define ARENA_MAGIC  146)
(define ARENA_FLAME  147)
(define ARENA_ZAP 	 148)
(define ARENA_DAZZLE 149)
(define ARENA_CALM 	 150)

;arena combat animations
(define ARENA_THRUST 151)
(define ARENA_BLOCK	 152)
(define ARENA_PARRY	 153)
(define ARENA_DODGE	 154)
(define ARENA_PAIN	 155)

;core arena regions, classes and scripts
(define ENCOUNTER 	210) ;Region
(define ARENA 		211) ;Region
(define SKILLED 	212) ;Class
(define WARRIOR 	213) ;Class
(define MONSTER 	214) ;Class
(define CLOSECOMBAT 215) ;Script


; Character Classes
(enum
	FIGHTER
	MAGE
	THIEF
)

; Ego Moving Modes
(enum -1
	MOVE_NOCHANGE
	MOVE_WALK
	MOVE_RUN
	MOVE_SNEAK
)

; Skills
(enum
	STR			;0
	INT			;1
	AGIL		;2
	VIT			;3
	LUCK		;4
	WEAPON		;5
	PARRY		;6
	DODGE		;7
	STEALTH		;8
	PICK		;9
	THROW		;10
	CLIMB		;11
	MAGIC		;12
	EXPER		;13
	HEALTH		;14
	STAMINA		;15
	MANA		;16
; Magic Spells
	OPEN		;17
	DETMAGIC	;18
	TRIGGER		;19
	DAZZLE		;20
	ZAP			;21
	CALM		;22
	FLAMEDART	;23
	FETCH		;24
)

; ///////////////////////////////////////////////////////
; Inventory
; ///////////////////////////////////////////////////////
(enum 1
	iSilver			;1
	iGold			;2
	iRations		;3
	iMandrake		;4
	iBrassKey		;5
	iSword			;6
	iDagger			;7
	iLeather		;8
	iShield			;9
	iPaper			;10
	iFruit			;11
	iVegetables		;12
	iMagicGem		;13
	iVase			;14
	iCandelabra		;15
	iMusicBox		;16
	iCandlesticks	;17
	iPearls			;18
	invRing			;19
	iSeed			;20
	iRock			;21
	iFlowers		;22
	iLockPick		;23
	iThiefKit		;24
	iThiefLicense	;25
	iFlask			;26
	iGreenFur		;27
	iFairyDust		;28
	iFlyingWater	;29
	iMushroom		;30
	iCheetaurClaw	;31
	iTrollBeard		;32
	iChainmail		;33
	iHealingPotion	;34
	iManaPotion		;35
	iStaminaPotion	;36
	iHeroPotion 	;37		;Called "aa" in-game; according to the inventory script, it was intended to be a Potion of Heroism.
	iDisenchant 	;38
	iGhostOil 		;39
	iMagicMirror 	;40
	iAcorn 			;41
)

; ///////////////////////////////////////////////////////
; Time Zones
; ///////////////////////////////////////////////////////
(enum
	TIME_DAWN
	TIME_MIDMORNING
	TIME_MIDDAY
	TIME_MIDAFTERNOON
	TIME_SUNSET
	TIME_NIGHT
	TIME_MIDNIGHT
	TIME_NOTYETDAWN
)

;TrySkill and SkillUsed amounts
;Skill Used amounts
(define tryStatThrowing 10) ;full define should be (/ [egoStats AGIL] 10) but SCICompanion doesn't support those types of defines
(define tryStatWeaponMaster 50)
(define tryStatKoboldDazzle 5)
(define tryStatStableStr 25)
(define tryStatStableVit 40)
(define tryStatRandomEncounter 12) ;full define should be (/ [monsterHP (GetMonsterIndex monsterInRoom)] 12) but SCICompanion doesn't support those types of defines

;TrySkill thresholds
(define tryPickSecretEntrance 85)
(define tryForceSecretEntrance 40) ;strength
(define tryBreakDownBrigandGate 60) ;strength
(define tryClimbBrigandGate 65)
(define tryClimbIntoTown 35)
(define tryFetchKoboldKey 35)
(define tryForceOpenKoboldChest 40) ;strength
(define tryPickKoboldChest 70)
(define tryCastOpenKoboldChest 50) ;open spell
(define trySneak 5)
(define trySneakKobold 35)
(define tryPickNose 40)
(define tryCastOpenFox 25) ;open spell
(define tryClimbSpittingSpirea 35)
(define tryClimbWaterfall 30)
(define tryCastOpenHenry 45) ;magic, specifically.
(define tryThrowHenry 25)
(define tryFaeryDance 25) ;agility
(define tryThrowNest 25)
(define tryPickLOL 30)
(define tryPickSheriff 50)
(define tryWalkTreeLimb 30) ;agility (the tree outside the healer's)
(define tryPickTavern 10)
(define tryLuckTrollInstakill 50)	;decide if the Troll will instantly kill you or go into an unescapable encounter in the troll cave.
(define tryOpenAntwerpRock 50)
(define trySneakMinotaurBush 50)	
(define tryClimbCastleGateNoSkill 10)
(define tryClimbCastleGate 35)

;Random Encounter Entrances
(define reNORTH 1)
(define reEAST	2)
(define reSOUTH 4)
(define reWEST	8)

;note, the other creatures have their maxHP in a array in RandomEncounters.sc
(define MAX_HP_OGRE 112)
(define MAX_HP_BRUTUS 100)
(define MAX_HP_KOBOLD 67)
(define MAX_HP_BEAR 133)
(define MAX_HP_MINOTAUR 186)

;defines related to the Brutus/Bruno meeting at teh TargetRange
(define BRUTUSCOUNT      300)
(define BRUTUSCOUNTREST   50)
(define BRUTUSCOUNTBRUNO 260)

;Spitting Spire states

;Dryad States

; Nest Condition
(enum
	nestInTree
	nestOnGround
	nestBurnt
)

; Bar Drinks
(enum
	drinkNothing
	drinkAle
	drinkSweat
	drinkBreath
)

; Baba Yaga states
(enum
	babaNAME
	babaBRAVE
	babaFETCH
	babaBRING
	babaFINALE
)

; Baba Yaga's Hut states
(enum
	hutINITIAL
	hutMETSKULL
	hutNODEAL
	hutDEALMADE
	hutGAVEGEM
)

; Inn Meal States
(enum
	mealNOTHING
	mealORDERED
	mealATTABLE
	mealFINISHED
)

; Mages Maze
(define ChaseRange 30)
	; Directions
(enum
	mazeN
	mazeNE
	mazeE
	mazeSE
	mazeS
	mazeSW
	mazeW
	mazeNW
)
; Bug Sizes
(enum
	smallBug
	mediumBug
	largeBug
)
; doCommands
(enum
	mmazeNOTHING
	mmazeCHOOSE
	mmazeFETCH
	mmazeOPEN
	mmazeTRIGGER
	mmazeFLAME
)

; Kobold States
(enum
	koboldASLEEP
	koboldAWAKE
	koboldSTATE2
	koboldSTATE3
	koboldDEAD
)

;Troll states (in the Troll Cave, rooms 88 and 89)
;(enum
;	trollHIDDEN
;	trollVISIBLE
;	trollDYING
;	trollDEAD
;)

;Arena Actions (for the 'Skilled' class)
(enum
	aaNOTHING
	aaTHRUST
	aaSLASH
	aaPARRYUP
	aaPARRYDOWN
	aaDODGELEFT
	aaDODGERIGHT
	aaDUCK
	aaLEAP
	aaACTION_9
	aaDIE
	aaMAGIC
)

; Monster Arenas
(define KOBOLD 15)

(define BEAR 420)
(define MINOTAUR 425)
(define SAURUS 430)
(define MANTRAY 435)
(define CHEETAUR 440)
(define GOBLIN 445)
(define TROLL 450)
(define OGRE 455)
(define SAURUSREX 460)
(define BRIGAND 465)

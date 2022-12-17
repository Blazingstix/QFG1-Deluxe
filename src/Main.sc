;;; Sierra Script 1.0 - (do not remove this comment)
(script# MAIN) ;MAIN = 0
(include system.sh) (include sci2.sh) (include game.sh)
(use GameInit)
(use DrinkItem)
(use EatItem)
(use Sleep)
(use Rest)
(use ThrowFlameDart)
(use ThrowDagger1)
(use ThrowRock)
(use AnimateCalm)
(use AnimateOpen)
(use AnimateDazzle)
(use _Interface)
(use HQRoom)
(use _StopWalk)
(use _Sound)
(use _Save)
(use _Motion)
(use _Game)
(use _Inventory)
(use _User)
(use _Menu)
(use _System)

(public
	Glory 0
	EgoDead 1
	RedrawCast 2
	HaveMem 3
	cls 4
	HandsOff 5
	HandsOn 6
	MouseClaimed 7
	GetSongNumber 8
	Bset 9
	Bclr 10
	Btst 11
	AdvanceDay 12
	Dummy1 13
	StopEgo 14
	PrintNotCloseEnough 15
	PrintAlreadyDoneThat 16
	PrintDontHaveIt 17
	PrintCantDoThat 18
	HighPrint 19
	CenterPrint 20
	TimedPrint 21
	ShowTime 22
	CanPickLocks 23
	FaceObject 24
	GiveSilverCoins 25
	FixTime 26
	AskQuit 27
	UseMana 28
	UseStamina 29
	TrySkill 30
	SkillUsed 31
	SolvePuzzle 32
	GetEgoViewNumber 33 ;CI: Added by Charles for QFG1Deluxe
	DevPrint 34 ;CI: Added by Charles for QFG1Deluxe
	;procedure 33 does not exist in original QFG1
	;procedure 34 does not exist in original QFG1
	RollDice 35
	TakeDamage 36
	MaxMana 37
	MaxStamina 38
	MaxHealth 39
	MaxWeight 40
	CastSpell 41
	ChangeGait 42
	EatMeal 43
)

(local
	;system-defined global variables (largly unchanged from game to game)
	;all games with original SSCI variable names are marked as (SSCI)
	ego					;0  (SSCI) pointer to ego
	theGame				;1  (SSCI) ID of the Game instance
	curRoom				;2  (SSCI) ID of current room
	speed =  6			;3  (SSCI) The number of ticks between animations. This is set, usually as a menu
						;	option, to determine the speed of animation. The default is 6.
	quit				;4  (SSCI) when TRUE, quit game
	cast				;5  (SSCI) The ID of a Set of Actors and Egos which constitutes the characters on the screen.
	regions				;6  (SSCI) The Set of Regions currently in effect.
	timers				;7  (SSCI) list of timers in the game
	sounds				;8  (SSCI) set of sounds being played
	inventory			;9  (SSCI) set of inventory items in game (not used in Hero's Quest)
	addToPics			;10 (SSCI) list of views added to the picture
	curRoomNum			;11 (SSCI) current room number
	prevRoomNum			;12 (SSCI) previous room number
	newRoomNum			;13 (SSCI) number of room to change to
	debugOn				;14 (SSCI) generic debug flag -- set from debug menu
	score				;15 (SSCI) the player's current score
	possibleScore		;16 (SSCI) highest possible score
	showStyle =  IRISOUT ;17 (SSCI) The global style for the transition from one picture to another.  This
     					;	may be overridden by the style property of a given room.  See the
     					;	DrawPic kernel function for the possible styles.
	overRun				;18 (SSCI) The number of timer ticks more than the Game's speed which it took to
     					;	complete the last animation cycle.  A non-zero overRun means that the
     					;	system is not keeping up.
	theCursor			;19 (SSCI) the number of the current cursor
	normalCursor =  ARROW_CURSOR	;20 (SSCI) number of normal cursor form
	waitCursor 	=  HAND_CURSOR		;21 (SSCI) cursor number of "wait" cursor
	userFont 	=  USERFONT			;22 (SSCI) font to use for Print
	smallFont 	=  4				;23 (SSCI) small font for save/restore, etc.
	lastEvent			;24 (SSCI) the last event (used by save/restore game)
	modelessDialog		;25 (SSCI) the modeless Dialog known to User and Intrface
	bigFont 	=  USERFONT			;26 large font
	lastVolume =  12	;27 (SSCI) last volume level set (from 0 to 15, 0 being off, 15 being loudest)
	version =  {ego}	;28   (SSCI) pointer to 'incver' version string
						;	  WARNING!  Must be set in room 0
						;	  (usually to {x.yyy    } or {x.yyy.zzz})
	locales				;29 (SSCI) list of locales used in the game
	curSaveDir			;30 (SSCI) current save drive/directory string [20 chars long]
		global31			;unreferenced (used for curSaveDir)
		global32			;unreferenced (used for curSaveDir)
		global33			;unreferenced (used for curSaveDir)
		global34			;unreferenced (used for curSaveDir)
		global35			;unreferenced (used for curSaveDir)
		global36			;unreferenced (used for curSaveDir)
		global37			;unreferenced (used for curSaveDir)
		global38			;unreferenced (used for curSaveDir)
		global39			;unreferenced (used for curSaveDir)
		global40			;unreferenced (used for curSaveDir)
		global41			;unreferenced (used for curSaveDir)
		global42			;unreferenced (used for curSaveDir)
		global43			;unreferenced (used for curSaveDir)
		global44			;unreferenced (used for curSaveDir)
		global45			;unreferenced (used for curSaveDir)
		global46			;unreferenced (used for curSaveDir)
		global47			;unreferenced (used for curSaveDir)
		global48			;unreferenced (used for curSaveDir)
		global49			;unreferenced (used for curSaveDir)
	animationDelay =  10 ;50 (SSCI) 
	perspective			;51 (SSCI) player's viewing angle: degrees away
						;  	from vertical along y axis
	features			;52 (SSCI) locations that may respond to events
	sFeatures			;53 (SSCI) requires SORTCOPY (script 984)
	useSortedFeatures	;54 (SSCI) enable cast & feature sorting? (default is FALSE)
	isDemoGame			;55 enabled if this is a game demo, and not a full game. ;CI: This might not be an accurate variable name??
	egoBlindSpot		;56 (SSCI) used in 1.200 for SIGHT.SC
	overlays =  -1		;57 (SSCI) 
	doMotionCue			;58 (SSCI) a motion cue has occurred - process it
	systemWindow		;59 (SSCI) ID of standard system window
	demoDialogTime =  3	;60 how long Prints stay up in demo mode
	defaultPalette		;61 (SSCI) 
	modelessPort		;62 (SSCI) 
	global63			;unused
	global64			;unused
	global65			;unused
	global66			;unused
	global67			;unused
	global68			;unused
	global69			;unused
	global70			;unused
	global71			;unused
	global72			;unused
	global73			;unused
	global74			;unused
	global75			;unused
	global76			;unused
	global77			;unused
	global78			;unused
	global79			;unused
	global80			;unused
	global81			;unused
	global82			;unused
	global83			;unused
	global84			;unused
	global85			;unused
	global86			;unused
	global87			;unused
	global88			;unused
	global89			;unused
	global90			;unused
	global91			;unused
	global92			;unused
	global93			;unused
	global94			;unused
	global95			;unused
	global96			;unused
	global97			;unused
	global98			;unused
	lastSysGlobal		;unused
	
	;game-defined global variables
	egoGait					;0 = walk, 1 = run, 2 = sneak
	razzleDazzleRootBeer	=	FALSE	;debug mode starts disabled
	deathCount				;added by CI: the total number of times Ego has died (shown on the Death List)
	deathCountUnique		;added by CI: the number of unique ways Ego has died (shown on the Death List)
	isEgoLocked				
	egoX					;a saved x position for ego (used for Monster Chasing)
	egoY					;a saved y position for ego (used for Monster Chasing)
	speedCount				;used to test how fast the system is
							;and used in determining detail level. (used in conjunction with detailLevel)
	cSound					;music object, current playing music?
	sillyClowns				= FALSE ;unused... added by CI
	daySheriffBreakIn		;this is the game day that you broke into the Sheriff's house.
							;After this day, the door will be barred, and you can no longer break in.
	dayLOLBreakIn			;this is the game day that you broke into the Little Old Lady's house.
							;After this day, the door willbe barred, and you can no longer break in
	dayCursedByBabaYaga		;this is the game day that Baba Yaga placed her curse on you.  
							;If you don't bring her the mandrake root before the time is up, you will die.
	exploringTown			;This is only ever set to FALSE (and never read)
							;And even then, it's only referenced outside the sheriff and lol house, 
							;when leaving to another part of town..
	timerYesNo				;a countdown timer used by several actors when they are waiting for a response
	lastRestTime		
	lastRestDay		
	monsterDistX
	monsterDistY
	prevGameTime			;unused... added by CI: Used for Retry after death events, to turn back the clock
	prevGameDay				;unused... added by CI: Used for Retry after death events, to turn back the clock
	debugValue				;unused... added by CI: Used with Razzle Dazzle Root Beer, when trying to debug a value.
	prevDeathNum			;unused... added by CI: Used to highlight the most recent deaths in the Death List.
	egoAvatar				;unused... added by CI: Used to change the Avatar used to represent the Hero (0=Male, 1=Female, 2=testing male)
	maxAvatars = 1			;unused... added by CI: Used to decide the total number of avatar choices that should be made available to the user
	shedIsAccessible = FALSE	;unused... added by CI: Used to decide if the Town Shed is accessible by the user.
	hideDevNoteShed			;unused... added by CI: Used to determine if we've already shown the special developer message for the Shed interior.
	global127				;unused
	global128				;unused
	global129				;unused
	currentTime				;the current in-game time of the current day, in in-game minutes. (There are 3600 "minutes" per day, or 150 "minutes" per hour.)
	isNightTime
	currentDay
	timeZone				;time of day in game (morning, noon, etc)
	barNote					;EO - Note being passed at the bar
	realTime
	heroType				;The Character Class. 0 = Fighter, 1 = Magic User or 2 = Thief
	detailLevel				;detail level (0 = low, 1 = mid, 2 = high)
	startingRoom
	egoStats					;hero's skills (25 variable array)
		gIntell
		gAgil
		gVit
		gLuck
		gWeap
		gParry
		gDodge
		global147
		global148
		global149
		global150
		gMagic
		global152
		gHealth
		gStamina
		gEgoMagicSkill
		global156
		global157
		global158
		gGGStrength
		global160
		global161
		global162
		global163	;end of skills
	skillTicks			;skillTicks (25 variable array)
		global165			;	determines when the skill is next increased 
		global166			;	When skills are used (either by TrySkill, SkillUsed, or UseSkillBonus)
		global167			;	the egoStatsPoints increase by an amount (either directly entered for SkillUsed and UseSkillBonus, or inciredtly for TrySkill)
		global168			;	When skillTicks goes above egoStats, then skillTicks resets, and egoStat increases by a random amount betweeen 1 and 3
		global169
		global170
		global171
		global172
		global173
		global174
		global175
		global176
		global177
		global178
		global179
		global180
		global181
		global182
		global183
		global184
		global185
		global186
		global187
		global188	;end of skillTicks
	lockPickBonus	;lock picking bonus (LockPick gives you 10 bonus, Tool Kit gives you 35 bonus)
	;these 8 are part of an array
	spellCost 	  =  2 			;SpellMPUsage (Open)
		global191 =  2 			;SpellMPUsage (Detect Magic)
		global192 =  3 			;SpellMPUsage (Trigger)
		global193 =  3 			;SpellMPUsage (Dazzle)
		global194 =  3 			;SpellMPUsage (Zap)
		global195 =  4 			;SpellMPUsage (Calm)
		global196 =  5 			;SpellMPUsage (Flame Dart)
		global197 =  5 			;SpellMPUsage (Fetch)

	global198				;unused
	global199				;unused
	global200				;unused
	highSpeedHero			;double's ego's walking speed (option removed in 1.200)
	magesMazeButtonIndex
	magesMazeCommand
	global204				;unused
	global205				;unused
	global206				;unused
	global207				;unused
	global208				;unused
	global209				;unused
	global210				;unused
	colourCount
	musicChannels
	timerStamina =  20		;How quickly does Stamina replenish?
	timerHealth =  15		;How quickly does Health replenish?
	global215				;unused
	keyDownHandler			;-our EventHandlers, get called by game
	mouseDownHandler		;-our EventHandlers, get called by game
	directionHandler		;-our EventHandlers, get called by game
	mealsPreEaten			;As the game progresses through the day, this gets reduced first. If this is 0, then you eat a ration.
	timerGhostOil			;number of game minutes left until the undead unguent wears off
	global221				;unused
	lastViewedSkills		;(25 variable array) - These are the value of skills as last seen on the character sheet. Changed skills will be shown in red.
		global223
		global224
		global225
		global226
		global227
		global228
		global229
		global230
		global231
		global232
		global233
		global234
		global235
		global236
		global237
		global238
		global239
		global240
		global241
		global242
		global243
		global244
		global245
		global246	;end of lastViewedSkills
	global247				;unused
	global248				;unused
	global249				;unused
	zapMeleeBonus			;the extra damage incurred by a zap-charged weapon
	monsterCycles			;number of animation cycles until the monster attacks again, in the arena
		
	;array of 9 variables, corresponding to direction event messages
	directionAngle 	=  180	;dirStop (degrees)
		global253 	=  0	;dirN (degrees)
		global254 	=  45	;dirNE (degrees)
		global255 	=  90	;dirE (degrees)
		global256 	=  135	;dirSE (degrees)
		global257 	=  180	;dirS (degrees)
		global258 	=  225	;dirSW (degrees)
		global259 	=  270	;dirW (degrees)
		global260 	=  315	;dirNW (degrees)

	flowersSold				;times sold flowers to the healer
	mushroomsSold			;times sold mushrooms to the healer
	flyingWaterGiven		;has the hero given flying water to the healer?
	brigandHead				;which view to use for the current brigand in combat.
	brigandBattleCount		;how many brigands is the hero fighting at once?
	egoCanFight				;during an arena battle, can the hero fight, or is he in the middle of something?
	dayLastFoughtWeaponMaster =  -1	;when did the hero last fight the weapon master
	daysWithoutSleep		;how many days has the hero gone without sleeping?
	totalDagNabItBet		;CI: ?? just a guess, could be something else, but is related to DagNabIt somehow.
	thievesGuildPassword
	daggersInRoom			;EO: Daggers thrown on ground
	daggersInMonster		;EO: Daggers thrown in enemy
	roomDaggersDropped		;the last room the hero dropped daggers into. He can pick up all is daggers in this room.
	statColour 		=  vLBLUE	;The colour of stats on the Character Screen
	statColourNew 	=  vLRED	;The colour of stats that have changed since last viewing, on the Character Screen
	roomShieldDropped		;the room hero dropped his shield in. He can pick it up later.
	dayPulledMandrakeRoot =  -3		;dayPulledMandrakeRoot
	dftStatusCode			;default Status Bar refresh code. i.e. Quest for Glory I [%d of 500]
	;wizard Erasmus variables
	wizGameSpellTime		;time left for Fetch and Flame Dart in the wizard game.
	wizAskedSpells			;What spells has Erasmus asked that you already know?
	wizAskedSpells2			;(second part of the above. What spells?)
	
	koboldIllBits =  cWHITE	;Kobold cave-related
	dayKoboldAwakened =  -1	;The day the Kobold was last awakened
	silversOnMonster		;Number of silvers on the monster in the current room
	gKobold					;global variable holding the kobold TargetActor
	koboldCycles			;not actually used for anything.
	dayLastFoughtOgre		;the day the Ogre was last fought
	timeZoneLastFoughtOgre	;the time zone the Ogre was last fought
	fightingKoboldStart		;the fight with the kobold has begun!
	fightingKobold			;True if in battle with Kobold?
	global291				;unused
	global292				;unused
	global293				;unused
	global294				;unused
	global295				;unused
	global296				;unused
	global297				;unused
	global298				;unused
	global299				;unused
	dartsBonus				;used for DagNabIt
	global301				;unused
	hpStatusView			;the View to use to display battle status
	hpFontColour			;Hit Points Font Colour, during Arena Battles.
	global304				;unused
	hadABattle				;used in Arena (set but never referenced)
	global306				;unused
	global307				;unused
	global308				;unused
	global309				;unused
	erasmusTalking			;musn't interrupt the wizard when he's talking.
	fenrusTalking			;nor his familiar.
	enableErasmusTeaCountdown ;there is a silent countdown to when the wizard takes another sip of tea. When enabled, the countdown continues.
	erasmusTellingJoke		;is Erasmus in the act of telling a joke?
	saidYesToErasmus		;said yes to whatever question the wizard asked.
	koboldStatus			;what is the kobold doing? 0 = asleep, ... 4 = dead.
	global316				;EO: unused in 1.200
	global317				;EO: unused in 1.200
	global318				;EO: unused in 1.200
	global319				;EO: unused in 1.200
	ghostCount				;ghosts and graveyard related.
	global321				;EO: unused in 1.200
	global322				;EO: unused in 1.200
	babaYagaHutStatus		;0, 1, 2, 3, 4 as possible values.
	babaYagaStatus			;0, 1, 2, 3, 4 as possible values.
	deathMusic =  26
	spireaStatus			;spitting seed related
	seedTarget				;which plant is the seed *going* to?
	seedInPlant				;which plant has the seed? (0, 1, 2, 3)
	applesGiven				;The number of apples given to Frost Giant
	nestCondition			;The state of the next outside the Healer's hut (0 = in tree, 1 = on ground, 2 = burnt)
	numDeadGoblins			;The number of dead goblins in the Goblin Ambush
	monsterInRoom			;EO: Monster in the current room
	gMonsterHealth			;HP of the current fighting monster
	brutusCountdown			;this counts down the game seconds until Brutus (the brigand) leaves the target range.  
							;If you wait until this reaches 0, Brutus will pack up and leave the Targe Range.
	lastViewedScore			;The game score last viewed on the Character Sheet
	;Bar variables
	drinkOrdered 			;(0 = nothing, 1 = ale, 2 = Troll's sweat, 3 = Dragon's Breath)
	drinkInHand				;(0 = nothing, 1 = ale, 2 = Troll's sweat, 3 = Dragon's Breath)
	numberOfAlesDrunk
	global339				;unused
	;Inn Variables
	foodOrdered				;ordered food... 0 = nothing, 1 = ordered, 2 = at table, 3 = finished
	teaOrdered				;ordered drink... 0 = nothing, 1 = ordered, 2 = at table, 3 = finished
	serveFoodCountdown		;this counts down the cycles after ordering food until it is served.
	;kobold variables
	koboldHP
	koboldEvade				;the threshold to which the kobold avoids getting hit (lower values means less chance of getting hit)
	damageToKobold			;how much damage will the kobold take if he gets hit?
	damageToKoboldFlame		;how much damage from a flame dart will the kobold take?
	global347				;unused
	egoKoboldBattleLoop		;EO: used for 1.200
	global349				;unused
	eventFlags	;Flag start (probably is several globals worth of spots)
	global351
	global352
	global353
	global354
	global355
	global356
	global357
	global358
	global359
	global360
	global361
	global362
	global363
	global364
	global365
	global366
	global367
	global368
	global369
	global370
	global371
	global372
	global373
	global374
	global375
	global376
	global377
	global378
	global379
	global380
	global381
	global382
	global383
	global384
	global385
	global386
	global387
	global388
	global389
	global390
	global391
	global392
	global393
	global394
	global395
	global396
	global397
	global398
	global399
	global400	;end of eventFlags
	userName	;(40 variable array) - name of the user, as supplied by the user when starting a new game.
	global402
	global403
	global404
	global405
	global406
	global407
	global408
	global409
	global410
	global411
	global412
	global413
	global414
	global415
	global416
	global417
	global418
	global419
	global420
	global421
	global422
	global423
	global424
	global425
	global426
	global427
	global428
	global429
	global430
	global431
	global432
	global433
	global434
	global435
	global436
	global437
	global438
	global439
	global440	;end of userName
	invNum		;inventory quantities (start of 42 variable array)
	global442
	global443
	global444
	global445
	global446
	global447
	global448
	global449
	global450
	global451
	global452
	global453
	global454
	global455
	global456
	global457
	global458
	global459
	global460
	global461
	global462
	global463
	global464
	global465
	global466
	global467
	global468
	global469
	global470
	global471
	global472
	global473
	global474
	global475
	global476
	global477
	global478
	global479
	global480
	global481
	global482	;end of Inventory array
	global483			;unused
	global484			;unused
	global485			;unused
	global486			;unused
	global487			;unused
	global488			;unused
	global489			;unused
	global490			;unused
	invDropped			;dropped inventory (start of 42 variable array)
	global492
	global493
	global494
	global495
	global496
	global497
	global498
	global499
	global500
	global501
	global502
	global503
	global504
	global505
	global506
	global507
	global508
	global509
	global510
	global511
	global512
	global513
	global514
	global515
	global516
	global517
	global518
	global519
	global520
	global521
	global522
	global523
	global524
	global525
	global526
	global527
	global528
	global529
	global530
	global531
	global532
	global533	;end of dropped inventory array
	global534			;unused
	global535			;unused
	global536			;unused
	global537			;unused
	global538			;unused
	global539			;unused
	global540			;unused
	invWeight	;inventory weight (start of 41 variable array)
	global542 =  1
	global543 =  1
	global544 =  20
	global545 =  30
	global546 =  15
	global547 =  420
	global548 =  120
	global549 =  1200
	global550 =  720
	global551 =  1
	global552 =  15
	global553 =  30
	global554 =  6
	global555 =  30
	global556 =  180
	global557 =  45
	global558 =  60
	global559 =  30
	global560 =  10
	global561 =  60
	global562 =  30
	global563 =  1
	global564 =  5
	global565 =  30
	global566 =  2
	global567 =  10
	global568 =  3
	global569 =  10
	global570 =  40
	global571 =  10
	global572 =  20
	global573 =  60
	global574 =  2100
	global575 =  40
	global576 =  40
	global577 =  40
	global578 =  40
	global579 =  40
	global580 =  40
	global581 =  30
	global582 =  3
	gOgreX =  160
	gOgreY =  120
	ogreHP =  93					;ogre's current HP (CI: looks like his max used to 93, but was upped to 112 later on.)
	dayDefeatedOgre =  1000			;day defeated Ogre
	global587						;unused
	global588						;unused
	brutusHP						;HP for Brutus in target range
	dayLastPlayedCribbage =  1000	;day last played Cribbage with Henry
	timerMana =  5					;How quickly does Mana replenish?
	cSoundArena						;music playing in the arenas during a battle
	magesMazePlayCount				;The number of times the hero has played Mage's Maze
	global594						;unused
)

;(procedure (EgoDead param1)
;	(asm
;		pushi    0
;		call     HandsOff,  0	;(HandsOff)

;		pushi    1
;		pushi    100
;		callk    Wait,  2		;(Wait 100)

;		pushi    #setCursor
;		pushi    2
;		lsg      normalCursor
;		pushi    TRUE
;		lag      theGame		;(theGame setCursor: normalCursor TRUE)
;		send     8

;		pushi    #eachElementDo
;		pushi    1
;		pushi    #stop
;		lag      sounds			;(sounds eachElementDo: #stop)
;		send     6

;		lag      deathMusic
;		bnt      code_0fc0
;		pushi    #number
;		pushi    1
;		push    
;		pushi    #priority
;		pushi    1
;		pushi    15
;		pushi    #init
;		pushi    0
;		pushi    #play
;		pushi    0
;		lofsa    music			;(music number: deathMusic init: play:) ?? there's something more at play here.
;		send     20
;code_0fc0:
;		pushi    11
;		&rest    param1
;		pushi    #width
;		pushi    250
;		pushi    #button
;		lofsa    {Restore}
;		push    
;		pushi    1
;		pushi    #button
;		lofsa    { Restart_}
;		push    
;		pushi    2
;		pushi    #button
;		lofsa    { Quit_}
;		push    
;		pushi    3
;		calle    Print,  22		;(Print &rest #width 250 #button {Restore} 1 #button { Restart_} 2 #button { Quit_} 3)
;		push    
;		dup     
;		ldi      1
;		eq?     
;		bnt      code_0ff5
;		pushi    #restore
;		pushi    0
;		lag      theGame		;(theGame restore:)
;		send     4
;		jmp      code_1015		;(break)
;code_0ff5:
;		dup     
;		ldi      2
;		eq?     
;		bnt      code_1007
;		pushi    #restart
;		pushi    0
;		lag      theGame		;(theGame restart:)
;		send     4
;		jmp      code_1015		;(break)
;code_1007:
;		dup     
;		ldi      3
;		eq?     
;		bnt      code_1015
;		ldi      TRUE
;		sag      quit			;(= quit TRUE)
;		jmp      code_1019		;(break)
;code_1015:
;		toss    
;		jmp      code_0fc0
;code_1019:
;		ret     
;	)
;)

(enum 1
	RESTORE
	RESTART
	QUIT
	RETRY
)
	
(procedure (EgoDead what how &tmp printRet)
	;stops all sounds, plays the death music, and gives the player a choice:
	;	Restore, Restart, Quit or
	;	Retry, Restore, Quit
	;
	; 'what' decides what we're doing: Restart or Retry.
	;		if 'what' == DIE_RETRY (-1), it's a Retry death;
	;		if 'what' == DIE_RESTART (-2), it's a Restart death (explicitly defined).
	;		any other values are assumed to be the original EgoDead function, for a Restart death (implicitly defined),
	;		and should be fully passed to the Print procedure.
	;
	; 'how' is this specific death's flag, to be set in the eventFlags variable, via the Bset function
	;
	; (CI: NOTE: This procedure is a recreation, based on Eric Oaklands's SCI01 template)
	; https://github.com/EricOakford/SCI01-Template/blob/master/src/Main.sc
	; it has been modified to more closely match the original asm, which is commented out but intact above,
	; then has been further modified to support a Retry mode.
	
	(HandsOff)
	(Wait 100)
	
	(theGame setCursor: normalCursor TRUE)
	(sounds eachElementDo: #stop)
	
	(if (!= deathMusic NULL)
		(music number: deathMusic priority: 15 init: play:)
	)
	
	;if we're specifying a way the player died, then we'll set the flag for that death method.
	(if (and (== what DIE_RETRY) (!= how DIE_NOFLAG))
		(++ deathCount)
		(= prevDeathNum how) ;we're going to flag out *every* time the hero dies.
		(if (not (Btst how))
			(Bset how)
			(++ deathCountUnique)
		)
	)
	
	(repeat
		(= printRet
			(cond 
				;1st priority: the new Retry dialog
				((== what DIE_RETRY)
					(Print &rest
						#width 250
						#button { Retry_} RETRY
						#button {Restore} RESTORE
						#button { Quit_} QUIT
					)
				)
				;2nd priority: an explicit DIE_RESTART flag with no second parameter
				((== what DIE_RESTART)
					(Print how &rest
						#width 250
						#button {Restore} RESTORE
						#button { Restart_} RESTART
						#button { Quit_} QUIT
					)
				)
				;finally, any original script EgoDeath scripts
				((>= what 0)
					(Print what how &rest
						#width 250
						#button {Restore} RESTORE
						#button { Restart_} RESTART
						#button { Quit_} QUIT
					)
				)
			)
		)
		(switch printRet
			(RESTORE ;restore
				(theGame restore:)
			)
			(RESTART ;restart
				(theGame restart:)
			)
			(QUIT ;quit
				(= quit TRUE)
				(break)
			)
			(RETRY ;retry
				(if (!= deathMusic NULL)
					(music stop:)
				)
				(HandsOn)
				(break)
			)
		)
	)
)

(procedure (RedrawCast)
	(Animate (cast elements?) FALSE)
)

(procedure (HaveMem memSize)
	(return (> (MemoryInfo LargestPtr) memSize))
)

(procedure (cls)
	; (clear screen) clears any modelessDialogs from the screan
	;
	
	(if modelessDialog (modelessDialog dispose:))
)

(procedure (HandsOff)
	; Lock ego control
	;
	
	(= isEgoLocked TRUE)
	(User canControl: FALSE canInput: FALSE)
	(theGame setCursor: waitCursor TRUE)
	(ego setMotion: NULL)
)

(procedure (HandsOn)
	; restore ego control
	;
	
	(= isEgoLocked FALSE)
	(User canControl: TRUE canInput: TRUE)
	(theGame setCursor: normalCursor (HaveMouse))
)

(procedure (MouseClaimed obj event modifiers)
	(return
		(if (MousedOn obj event modifiers)
			(event claimed: TRUE)
			(return TRUE)
		else
			(return FALSE)
		)
	)
)

;songs are split into 4 channel (low-res) and greater than 4 channel (standard res).
;low-res songs have resource numbers in the 100-range,
;while standard res songs have resource numbers in the 000-range
(procedure (GetSongNumber song)
	(return
		(if (> musicChannels 4)
			(return song)
		else
			(return (+ 100 song))
		)
	)
)

(procedure (Bset bit)
	(= [eventFlags (/ bit 16)]
		(|
			[eventFlags (/ bit 16)]
			(>> $8000 (mod bit 16))
		)
	)
)

(procedure (Bclr bit)
	(= [eventFlags (/ bit 16)]
		(&
			[eventFlags (/ bit 16)]
			(~ (>> $8000 (mod bit 16)))
		)
	)
)

(procedure (Btst bit)
	(return
		(&
			[eventFlags (/ bit 16)]
			(>> $8000 (mod bit 16))
		)
	)
)

(procedure (AdvanceDay)
	(++ currentDay)
	(Bclr STABLE_CLEAN)
)

(procedure (Dummy1)
)

(procedure (StopEgo)
	(ChangeGait MOVE_NOCHANGE FALSE)
	(ego
		setPri: RELEASE
		setMotion: NULL
		illegalBits: cWHITE
		ignoreHorizon:
		ignoreActors: FALSE
	)
)

(procedure (PrintNotCloseEnough)
	(HighPrint 0 51)
	;You're not close enough.
)

(procedure (PrintAlreadyDoneThat)
	(HighPrint 0 52)
	;You've already done that.
)

(procedure (PrintDontHaveIt)
	(HighPrint 0 53)
	;You don't have it.
)

(procedure (PrintCantDoThat)
	(HighPrint 0 49)
	;You can't do that now.
)

(procedure (HighPrint &tmp [sizeRect 4] [str 400])
	; Prints a message at the top of the screen (y: 12px)
	;
	
	(cls)
	(Format @str &rest)
	(TextSize @[sizeRect 0] @str userFont 0)
	(Print
		@str
		#at	-1 12
		#width (if (> [sizeRect 2] 24) 300 else 0)
		#mode teJustCenter
	)
)

(procedure (CenterPrint &tmp [sizeRect 4] [str 400])
	; Prints a message in the middle of the screen (y: 115px)
	;
	
	(Format @str &rest)
	(TextSize @[sizeRect 0] @str userFont 0)
	(Print
		@str
		#at -1 115
		#width (if (> [sizeRect 2] 24) 300 else 0)
		#mode teJustCenter
	)
)

(procedure (TimedPrint seconds &tmp [sizeRect 4] [str 400])
	; Clears any existing messages and Prints a new message at the top of the screen 
	; that automatically dissapears after the specified number of seconds
	; 
	
	(cls)
	(Format @str &rest)
	(TextSize @[sizeRect 0] @str userFont 0)
	(Print
		@str
		#at -1 12
		#width (if (> [sizeRect 2] 24) 300 else 0)
		#mode teJustCenter
		#dispose
		#time seconds
	)
)

(procedure (ShowTime &tmp theCurrentDay [str 30] tmpHour tmpMinutes)
	(= theCurrentDay currentDay)
	(if (or (!= timeZone TIME_MIDNIGHT) (> currentTime 500))
		(++ theCurrentDay)
	)
	(HighPrint
		(Format @str
			0 48 ;%s on day %d.
			297 (+ 40 timeZone) ;%s the name of the time of day, from text.297
			theCurrentDay) ; %d the current day
	)

	;now let's print the specific time, since we can.
	(if sillyClowns
		(= tmpHour (/ currentTime TICKSPERHOUR))
		(= tmpMinutes (/ (* (mod currentTime TICKSPERHOUR) 60) TICKSPERHOUR))
		(HighPrint 
			(Format @str
				0 65 ;Your Trusty Timex (TM) says it's precisely %02d:%02d.
				tmpHour
				tmpMinutes
			)
		)
	)
)

(procedure (CanPickLocks)
	(if [egoStats PICK] (if (ego has: iLockPick) else (ego has: iThiefKit)))
)

(procedure (FaceObject who whom)
	(DirLoop
		who
		(GetAngle
			(who x?)
			(who y?)
			(whom x?)
			(whom y?)
		)
	)
	(if (== argc 3)
		(DirLoop
			whom
			(GetAngle
				(whom x?)
				(whom y?)
				(who x?)
				(who y?)
			)
		)
	)
)

(procedure (GiveSilverCoins coins &tmp silver gold)
	; if hero has enough money total (gold + silver), then 
	; hero's silver will be deducted first. If that's not enough to pay the cost, 
	; gold will be automatically converted to silver at 10:1 rate
	; Returns FALSE if hero doesn't have enough money, TRUE if the exchange was successful.
	;
	(= silver [invNum iSilver])
	(= gold [invNum iGold])
	(if (< (+ silver (* gold 10)) coins)
		(return FALSE)
	)
	(= silver (- silver coins))
	(while (< silver 0)
		(-- gold)
		(= silver (+ silver 10))
	)
	(= [invNum iSilver] silver)
	(= [invNum iGold] gold)
	(return TRUE)
)

(procedure (FixTime hour minutes &tmp newTimeZone)
	(if (>= argc 1)
		(= currentTime (* TICKSPERHOUR hour))
		(= realTime (GetTime 1))
		(if (>= argc 2)
			(= currentTime (+ currentTime (/ (* TICKSPERHOUR minutes) 60)))
		)
	)
	(= currentTime (^ currentTime $0001))
	(= newTimeZone timeZone)
	(cond 
		((< currentTime 300) (= timeZone TIME_MIDNIGHT))
		((< currentTime 750) (= timeZone TIME_NOTYETDAWN))
		((< currentTime 1200) (= timeZone TIME_DAWN))
		((< currentTime 1650) (= timeZone TIME_MIDMORNING))
		((< currentTime 2100) (= timeZone TIME_MIDDAY))
		((< currentTime 2550) (= timeZone TIME_MIDAFTERNOON))
		((< currentTime 3000) (= timeZone TIME_SUNSET))
		((< currentTime 3450) (= timeZone TIME_NIGHT))
		(else (= timeZone TIME_MIDNIGHT))
	)
	(if (> timeZone TIME_SUNSET)
		(= isNightTime TRUE)
		(= defaultPalette 1)
	else
		(= isNightTime FALSE)
		(= defaultPalette 0)
	)
	(if (and (== timeZone TIME_MIDNIGHT) (!= newTimeZone TIME_MIDNIGHT))
		(if (== (++ daysWithoutSleep) 1)
			(Print 0 54)
			; You are getting tired.
		else
			(Print 0 55)
			; You are exhausted from lack of sleep.
		)
	)
)

(procedure (AskQuit)
	(= quit
		(Print 0 64
			#title {Giving up, huh?}
			#button {Quit} TRUE
			#button {Don't Quit} FALSE
			#icon (GetEgoViewNumber vEgoDeathScenes) 0 2 ;CI: changed in Deluxe. Was 800 1 4.
		)
	)
	; How about a slice of quiche?
)

(procedure (UseMana amount)
	; Reduces hero's MP. No return value.
	; 
	
	(if [egoStats MAGIC]
		(if (< (= [egoStats MANA] (- [egoStats MANA] amount)) 0)
			(= [egoStats MANA] 0)
		)
		(if (> [egoStats MANA] (MaxMana))
			(= [egoStats MANA] (MaxMana))
		)
		(if (> amount 0)
			;losing MP works out your Intelligence and your Magic (by the amount of MP / 5 and /2 respectively)
			(SkillUsed INT (/ amount 5))
			(SkillUsed MAGIC (/ amount 2))
		)
	)
)

(procedure (UseStamina amount &tmp temp0)
	; Reduces hero's SP. No return value.
	;
	
	(if (> amount 0) 
		;losing SP works out your Vitality.
		(SkillUsed VIT (/ (+ amount 3) 4))
	)
	(= [egoStats STAMINA] (- [egoStats STAMINA] amount))
	(= temp0 [egoStats STAMINA])
	(cond 
		((< temp0 0)
			;if you're out of SP, then we start reducing your HP.
			(TakeDamage (/ (- -3 [egoStats STAMINA]) 4))
			(= [egoStats STAMINA] 0)
			(if (not (Btst HERO_NO_STAMINA))
				(Bset HERO_NO_STAMINA)
				(HighPrint 0 56)
				;You are so exhausted that everything you do hurts.  Better get some rest.
			)
			(if (<= [egoStats HEALTH] 0)
				(EgoDead DIE_RETRY DIE_NOSTAMINA 0 57 
					#title {Death from Overwork}
					#icon (GetEgoViewNumber vEgoDeathScenes) 0 2 ;CI: changed in Deluxe. Was 800 1 4.
					;That was a little too much for you.  You collapse from exhaustion and die.
					;since we died by exhaustion, we'll 
				)
				(ego setHeroRetry:)
				;CI:TODO: If we're in a battle, then we probably just want to cancel the whole battle.
			)
		)
		;if we have > 4 SP, then remove the NO_STAMINA flag.
		((> temp0 4)
			(Bclr HERO_NO_STAMINA)
			(if (> temp0 (MaxStamina))
				(= [egoStats STAMINA] (MaxStamina))
			)
		)
	)
)

(procedure (TrySkill skill threshold bonus &tmp skillValue difference extended ret)
	; Exercises a skill, while also reducing stamina, and exercising any complementary 
	; basic skills (i.e. strength, intelligence, agility, vitality, luck)
	;
	; Returns whether the skill attempt was a success or not. 
	; (success is determined either by the supplied threshold, or a random threshold from 1-100.
	;
	
	;if hero has a 0 in that skill, we can't even tru anything.
	(if (not (= skillValue [egoStats skill]))
		(return FALSE)
	)
	
	;;if we've specified a bonus parameter, add it to our base skill level.
	(if (== argc 3)
		(= skillValue (+ skillValue bonus))
	)
	
	;if there's no specified threshold, then we pick one randomly between 1 and 100.
	;further, if it's a skill above one of the core 5 (STR, VIT, AGL, INT, LUCK), 
	;then it'll take stamina to try: either 1/10th the threshold amount, or a random number between 1-6
	(if threshold
		(if (>= skill WEAPON) (UseStamina (/ threshold 10)))
	else
		(if (>= skill WEAPON) (UseStamina (Random 1 6)))
		(= threshold (RollDice))
	)
	
	;if we get lucky, then we boost our skill value by 1-20 points.
	(if (>= (UseSkillBonus LUCK 1) (Random 1 200))
		(= skillValue (+ skillValue (Random 1 20)))
	)
	;if the threshold is below (or equal) to the skillvalue + bonus, then we return a success.
	;otherwise we'll be returning failure.
	(= ret (<= threshold skillValue))
	;how close to the threshold were we?
	;the theory being that if our skill level is close (over or under) to the threshold
	;the hero is learnign quicker, and so their skills will grow faster.
	(= difference (Abs (- threshold skillValue)))
	;reduce the difference down to (x + 10)/10
	(= difference
		(cond 
			((<= difference 10)
				2
			)
			((<= difference 30)
				4
			)
			((<= difference 50)
				;CI: TODO: this is an easy place to tweak skill growth.
				; if this was a lower number, skills would climb faster 
				; when you've already mastered all the challenges.
				6
			)
			(else 
				(return ret)
			)
		)
	)
	(= extended
		;if it's not a basic skill, then we have to also increase those by varying conditions.
		(cond 
			((== skill WEAPON)
				;weapon use requires strength and agility (2:2)/16
				(/ (+ (UseSkillBonus AGIL 2) (UseSkillBonus STR 2)) 16)) ;i.e. (2*AGIL + 2*STR)/16
			((or (== skill PARRY) (== skill DODGE) (== skill STEALTH))
				;dodge, parry, and stealth require strength and intelligence (3:1)/8
				(/ (+ (UseSkillBonus STR 3) (UseSkillBonus INT 1)) 8))
			((== skill PICK)
				;lock picking requires agility and intelligence (3:1)/4
				(/ (+ (UseSkillBonus AGIL 3) (UseSkillBonus INT 1)) 4))
			((or (== skill THROW) (== skill CLIMB))
				;throwing and climbing requires agility and strength (3:2)/5
				(/ (+ (UseSkillBonus AGIL 3) (UseSkillBonus STR 2)) 5))
			((>= skill OPEN)
				;magic spells require Magic and Intelligence
				(/ (+ (UseSkillBonus MAGIC 4) (UseSkillBonus INT 2)) 6))
			(else 
				; everything else would be the base skills, and magic.
				; CI: NOTE: technically experience, HP, SP, and MP could also be subject to this,
				; but that doesn't make logical sense, so it's best not to actually do that
				; in any code.
				10
			)
		)
	)
	(SkillUsed skill (/ extended difference))
	(return ret)
)

(procedure (SkillUsed skill amount)
	; increases the skillTicks by amount. When skillTicks surpasses egoStats, 
	; then those points are spent, increasing that skill by a random number from 1-3.
	; If the skill is increased, TRUE is returned, otherwise FALSE is returned.
	;
	
	;if the skill is a 0, it can never increase.
	(if (not [egoStats skill])
		(return FALSE)
	)
	
	;if amount is > than the current skill level, then set it to the current skill level
	(= amount (Abs amount))
	(if (> amount [egoStats skill])
		(= amount [egoStats skill])
	)
	;increase experience by amount/4
	(= [egoStats EXPER] (+ [egoStats EXPER] (/ amount 4)))
	
	;increase skillTicks by amount
	(= [skillTicks skill] (+ [skillTicks skill] amount))
	
	(if (>= [skillTicks skill] [egoStats skill] )
		;reduce skillTicks by the current egoStats
		;i.e. if skillTicks is 51 and egoStats is 50, then points becomes 1, and must climb up again to increase the stats
		(= [skillTicks skill] (- [skillTicks skill] [egoStats skill]))
		;increase egoStats by a random amount from 1-3
		(= [egoStats skill] (+ [egoStats skill] (Random 1 3)))
		;if we're above 100, cap it at 100
		(if (> [egoStats skill] 100)
			(= [egoStats skill] 100) ;Stats max out at 100.
		)
		;return TRUE, the skill was increased
		(return TRUE)
	)
	;skill was not increased.
	(return FALSE)
)

(procedure (SolvePuzzle flag points charClass)
	; sets the specified flag, then adds the specified points to the totalScore.
	; if a heroType is specified, the flag and points will only be awarded if we're that heroType.
	;
	
	(if (and (>= argc 3) (!= heroType charClass))
		;if we're specifying a heroType (and this this hero ain't it), then nothing more to do.
		(return)
	)
	(if (not (Btst flag))
		(Bset flag)
		(theGame changeScore: points)
		;increase intelligence every time you solve an in-game puzzle (i.e. get game points)
		(SkillUsed INT points)
	)
)

;Procedure added by Charles Irwin for the Deluxe edition
; Allows multiple different sets of views depending on the avater chosen
; Views 500-599 are reserved for Avatar 0 (i.e. standard Male)
; Views 600-699 are reserved for Avatar 1 (i.e. Female variant)
; Views 700-799 are reserved for Avatar 2 (i.e. palette-swapped Male, used for testing only).
(procedure (GetEgoViewNumber view)
	(return (+ view (* VIEW_PER_AVATER egoAvatar)))
)

;Procedure added by Charles Irwin for the Deluxe edition
; Developer Print. displays a message from the developer, for use in calling out special BETA features.
(procedure (DevPrint &tmp [sizeRect 4] [str 400] oldBG oldFG)
	; Prints a message at the top of the screen (y: 12px)
	; with a title
	;
	;TODO: create a special background color for this message only.
	
	(cls)
	(Format @str &rest)
	(TextSize @[sizeRect 0] @str userFont 0)
	(= oldBG (systemWindow back?))
	(= oldFG (systemWindow color?))
	(systemWindow back: vGREY color: vWHITE)
	(Print
		@str
		#at	-1 12
		#width (if (> [sizeRect 2] 24) 300 else 0)
		#mode teJustCenter
		#title {Developer Note}
	)
	(systemWindow back: oldBG color: oldFG)
)

(procedure (RollDice)
	;returns a random number from 1 to 100
	;
	(return (+ 1 (/ (Random 0 999) 10)))
)

(procedure (TakeDamage amount)
	; reduces ego's HP by the specified amount, 
	; and returns whether the hero is still alive or not.
	;
	
	(if (> amount 0) 
		;losing HP works out your Vitality skill
		(SkillUsed VIT (/ (+ amount 1) 2))
	)
	(if (< (= [egoStats HEALTH] (- [egoStats HEALTH] amount)) 0)
		(= [egoStats HEALTH] 0)
	)
	(if (> [egoStats HEALTH] (MaxHealth))
		(= [egoStats HEALTH] (MaxHealth))
	)
	(return (> [egoStats HEALTH] 0))
)

(procedure (MaxMana &tmp magicSkill)
	; Maximum MP is (intelligence + 2*Magic) / 3
	;
	
	(return
		(if (= magicSkill [egoStats MAGIC])	;If player has any magic skill
			(return (/ (+ [egoStats INT] magicSkill magicSkill) 3))
		else
			(return 0)	;Otherwise, mana will always be at zero
		)
	)
)

(procedure (MaxStamina)
	; Maximum SP is (Agility + Vitality) * 2
	;
	
	(return (* (+ [egoStats AGIL] [egoStats VIT]) 2))
)

(procedure (MaxHealth &tmp temp0)
	; Maximum HP is ((Strength + 2*Vitality) /3) * 2
	;
	
	(return
		(+
			(= temp0 (/ (+ [egoStats STR] [egoStats VIT] [egoStats VIT]) 3))
			temp0
		)
	)
)

(procedure (MaxWeight)
	; Maximum weight (in lbs) carried is 40 + (Strength / 2)
	; 
	
	(return (+ 40 (/ [egoStats STR] 2)))
)

(procedure (CastSpell spell &tmp temp0)
	; Attempts to cast the specified spell.
	; Returns: if casting the spell was successful.
	;	If successful, the skill will be improved, and mana will be depleted.
	;   If unsuccessful, a message will be displayed saying why, and FALSE will be returned.
	;
	
	(cond 
		((not [egoStats MAGIC])
			(HighPrint 0 58))
			;You don't know how to cast spells.
		((not (ego knows: spell))
			(HighPrint 0 59))
			;You don't know that spell.
		((< [egoStats MANA] [spellCost (- spell OPEN)])
			(HighPrint 0 60))
			;You don't have enough Magic Points to cast that spell.
		(else
			(TrySkill spell 0)
			(UseMana [spellCost (- spell OPEN)])
			(return TRUE)
		)
	)
	(return FALSE)
)

(procedure (ChangeGait walkMode keepMoving &tmp temp0)
	(if keepMoving
		(cond 
			((not (User canControl:))
				(HighPrint 0 49)
				;You can't do that now.
				(return)
			)
			((== egoGait walkMode)
				(HighPrint 0 50)
				;Go ahead. Just do it.
				(return)
			)
		)
	)
	(if (!= walkMode MOVE_NOCHANGE)
		(= egoGait walkMode)
	)
	(ego setLoop: RELEASE cycleSpeed: 0 moveSpeed: 0)
	(switch egoGait
		(MOVE_RUN
			(ego view: (GetEgoViewNumber vEgoRunning) setStep: 6 4 setCycle: egoSW (GetEgoViewNumber vEgoStanding))
		)
		(MOVE_SNEAK
			(ego view: (GetEgoViewNumber vEgoSneaking) setStep: 2 1 setCycle: Walk)
		)
		(else 
			(ego view: (GetEgoViewNumber vEgo) setStep: 3 2 setCycle: egoSW (GetEgoViewNumber vEgoStanding))
		)
	)
	(if highSpeedHero	;although the option was removed from the menu, the code is still present.
		(ego setStep: (* (ego xStep?) 2) (* (ego yStep?) 2))
	)
	;regardless of the walking method, if you're carrying to much
	; you walk the slowest increment possible.
	(if (Btst HERO_OVERENCUMBERED) (ego setStep: 1 1))
)

(procedure (EatMeal)
	(cond 
		(mealsPreEaten
			(-- mealsPreEaten)
		)
		([invNum iRations]
			(if (not (-- [invNum iRations]))
				(CenterPrint 0 61)
				;You just ate your last ration; you'd better get some more food soon.
			)
		)		
		((Btst HERO_HUNGRY)
			(Bset HERO_STARVING)
			(CenterPrint 0 62)
			;You're starving.  Better find some food *soon*!
			(if (> [egoStats HEALTH] 2)
				(TakeDamage 1)
				;CI: TODO: this is a potential bug. If the hero has only 1 HP, and skips a meal, 
				;he could die with no death message.
				;to prevent this (slim) bug from happening, we'll not reduce HP if we're already at 2.
			)
		)
		(else (Bset HERO_HUNGRY)
			(CenterPrint 0 63))
			;You're really getting hungry.
	)
)

(procedure (UseSkillBonus skill bonus)
	; Increases the StatPool by the bonus amount, 
	; then returns ego's skill multiplied by that bonus.
	; (Only used by the TrySkill procedure)
	
	(SkillUsed skill bonus)
	(return (* [egoStats skill] bonus))
)
;Procedure from HQ1 that was removed from QFG1
;(procedure (ResetDroppedInventory &tmp i temp1)
;	(= i 1)
;	(while (<= i 41)
;		(= [invDropped i] 0)
;		(++ i)
;	)
;)

(class heroEgo of Ego
	;this is a special subclass of Ego to handle the
	;Quest for Glory specific attributes the hero has 
	;i.e. the more complex inventory system, skill management, magic, etc.	
	(properties
		name "ego"
	)
	
	(method (get item qty &tmp temp0 getAmount currentAmount totalWeight)
		(= getAmount (if (== argc 1) 1 else qty))
		(= currentAmount [invNum item])
		(cond 
			(
				(>
					(= totalWeight
						(+
							(WtCarried)
							(/ (+ 59 (* getAmount [invWeight item])) 60)
						)
					)
					(MaxWeight)
				)
				(if (not (Btst HERO_OVERENCUMBERED))
					(HighPrint 0 0)
					;You are carrying so much that you can hardly move.  You'd better drop something soon.
					(Bset HERO_OVERENCUMBERED)
					(ego setStep: 1 1)
				)
			)
			((Btst HERO_OVERENCUMBERED)
				(Bclr HERO_OVERENCUMBERED)
				(ChangeGait MOVE_NOCHANGE FALSE))
		)
		(if
		(< (= getAmount (+ getAmount currentAmount)) 0)
			(= getAmount 0)
		)
		(= [invNum item] getAmount)
		(return (- getAmount currentAmount))
	)
	
	(method (put item qty &tmp temp0 putAmount)
		(= putAmount (if (== argc 1) 1 else qty))
		(if (not (= putAmount (self use: item putAmount)))
			(return 0)
		)
		(= [invDropped item]
			(+ [invDropped item] putAmount))
		(return (if (not (ego has: iMushroom)) (Bclr HAVE_FAIRY_MUSHROOMS) else 0))
	)
	
	(method (has item)
		(return [invNum item])
	)
	
	(method (use item qty &tmp temp0 usedQty temp2)
		(if
			(>
				(= usedQty (if (== argc 1) 1 else qty))
				[invNum item]
			)
			(= usedQty [invNum item])
		)
		(self get: item (- usedQty))
		(if (and (== item iMushroom) (not [invNum iMushroom])) (Bclr HAVE_KOBOLD_MUSHROOMS))
		(return usedQty)
	)
	
	(method (drop item)
		(self put: item &rest)
	)
	
	(method (pickUp item qty &tmp temp0 pickUpAmount temp2 droppedAmount n)
		(= pickUpAmount (if (== argc 1) 1 else qty))
		(= droppedAmount [invDropped item])
		(= n 0)
		(if droppedAmount
			(= n
				(if (u< droppedAmount pickUpAmount) droppedAmount else pickUpAmount)
			)
			(self get: item n)
			(= [invDropped item] (- [invDropped item] n))
		)
		(return n)
	)
	
	(method (knows skill)
		(return [egoStats skill])
	)
	
	(method (learn skill skillLevel &tmp temp0 newSkillLevel)
		(= newSkillLevel (if (== argc 1) 5 else skillLevel))
		(if (and [egoStats MAGIC] (> newSkillLevel [egoStats skill]))
			(= [egoStats skill] newSkillLevel)
		)
		(return [egoStats skill])
	)

	(method (setHeroRetry)
		;;CI: added May 27, 2019.
		;This will restore ego's health to 1/4 of maximum, stamina to full, and mana to 1/4 of maximum.
		(TakeDamage (- (/ (MaxHealth) 4)))
		(UseMana (- (/ (MaxMana) 4)))
		(UseStamina (- (MaxStamina)))
	)
	
)

(instance egoBase of Code
	(properties)
	
	(method (doit who &tmp h w)
		(= h (who x?))
		(= w (+ 1 (who y?)))
		(who
			brTop: (- w 2)
			brBottom: w
			brLeft: (- h 9)
			brRight: (+ h 9)
		)
	)
)

(instance egoObj of heroEgo
	(properties)
)

(instance contMusic of Sound
	(properties
		number 26
	)
)

(instance endBattle of Sound
	(properties
		number 26
		priority 10
	)
)

(instance music of Sound
	(properties
		number 26
		priority 10
	)
)

(instance statusCode of Code
	(properties)
	
	(method (doit str)
		(if razzleDazzleRootBeer
			(Format str 0 66 debugValue)
			; Razzle Dazzle [debugValue = %d]
		else 
			(Format str 0 1 score)
			;   So You Want To Be A Hero  [score %d of 500]	 ;CI: HQ V1.000 - V1.105
			;   Quest for Glory I         [score %d of 500]  ;CI: QFG1 V1.200
		)
	)
)

(instance keyHandler of EventHandler
	(properties)
	
	(method (handleEvent event)
		(if
			(and
				(not (super handleEvent: event))
				(== (event message?) KEY_RETURN)
			)
			(cls)
			(event claimed: TRUE)
		)
	)
)

(instance dirHandler of EventHandler
	(properties)
)

(instance mouseHandler of EventHandler
	(properties)
)

(instance hSW of SysWindow
	(properties)
)

(instance Glory of Game
	(properties)
	
	(method (init &tmp theStopWalk)
		(Load RES_SCRIPT 1)
		(= systemWindow hSW)
		((= ego egoObj) baseSetter: egoBase)
		(= version {1.310____})
		(= waitCursor HAND_CURSOR)
		(StatusLine code: (= dftStatusCode statusCode))
		(= theStopWalk StopWalk)
		(super init:)
		((= keyDownHandler keyHandler) add:)
		((= mouseDownHandler mouseHandler) add:)
		((= directionHandler dirHandler) add:)
		(= musicChannels (DoSound sndCHECK_DRIVER))
		((= cSound contMusic)
			number: (GetSongNumber 26)
			owner: self
			init:
		)
		((= cSoundArena endBattle)
			number: (GetSongNumber 26)
			owner: self
			init:
		)
		(music number: (GetSongNumber 26) owner: self init:)
		(= deathMusic (GetSongNumber 26))
		(SetGameInit)
	)
	
	(method (doit &tmp theRealTime)
		(super doit:)
		(if
			;if we've started the game, and time has passed since the last loop,
			;run the regular cycle stuff
			(and
				(Btst fInMainGame)
				(!= realTime (= theRealTime (GetTime SYSTIME1)))
			)
			
			;if we've enabled razzle dazzle mode, turn on our special watched variables, on each loop through.
			(if razzleDazzleRootBeer
				(StatusLine doit:)
			)
			
			(= realTime theRealTime)
			(++ currentTime)
			;if it's passed day 7, then time passes twice as fast.
			(if (and (>= currentDay 7) (& currentTime $0001))
				(++ currentTime)
			)
			;if it's passed 3600, change to the next day.
			(if (>= currentTime TICKSPERDAY)
				(= currentTime 0)
				(AdvanceDay)
			)
			;if ego's cursed, and time is up, time to EgoDead.
			(if
				(and
					(Btst BABAYAGA_CURSE)
					(> currentDay dayCursedByBabaYaga)
					(> currentTime 750)
					(not (or (== curRoomNum 21) (== curRoomNum 22))) ;CI: if ego's right at Baba Yaga's door, let's give them a chance to finish before the curse take effect.
				)
				(HandsOff)
				(HighPrint 0 2)
				;Suddenly, you hear voices, seeming to come from everywhere at once.  They all sound like Baba Yaga, and they all say:
				;"Wheeeeeere's myyyyyy
				;Maaaandraaaaake Rooooooooooooot????"
				(EgoDead DIE_RETRY DIE_BABA_CURSE 0 3
					#icon vDeathScenes 1 2 
					#title {Curses!}
				)
				;Because you failed to meet Baba Yaga's DEADline, her curse turns you into a frog on the spot, and you are forced to live
				;out your years dodging Sauruses (Saurii?) with large feet.
				
				;(Bset DIE_RETRY_INPROGRESS)
				(= dayPulledMandrakeRoot 0) ;reset pulling the mandrake root
				(= [invNum iMandrake] 0)	;remove any mandrake roots from inventory (so we can pull it again)
				(= babaYagaStatus babaFETCH)
				(HandsOn)
				(= curRoomNum 21)	;this is a quick hackey way to force the game into thinking we've just left the witches hut to get the mandrake root.
				(curRoom newRoom: 22)		;teleport us to just outside Baba Yaga's hut, and she'll warn us to get the root.
			)
			;if we've spied on Brutus and Bruno's meeting, count down the seconds until Brutus leaves.
			(if brutusCountdown (-- brutusCountdown))
			;eat a ration at noon and dinner.
			(if (or (== currentTime 1100) (== currentTime 2500))
				(EatMeal)
			)
			
			;if ego has used the Undead Unguent, we should decrease it's timer.
			(if (Btst PROTECTED_UNGUENT)
				(switch (-- timerGhostOil)
					(24
						(HighPrint 0 4) ;getting close, so print a warning
						;The tingling sensation is wearing off.
					) 
					
					(0
						(Bclr PROTECTED_UNGUENT)
						(HighPrint 0 5)
						;The tingling sensation is gone.
					)
				)
			)
			
			;every 20 game seconds, ego gets refreshed in Stamina.
			(if (not (-- timerStamina))
				(= timerStamina 20)
				(cond 
					;if the hero's starving, or has gone more than 1 day without sleep, reduce SP by 1
					((or (Btst HERO_STARVING) (> daysWithoutSleep 1))
						(UseStamina 5)
					)
					;if the Hero's running, then reduce SP by 2
					((== egoGait MOVE_RUN)
						(UseStamina 2)
					)
					;if the hero's not getting hungry, and hasn't gone a day without sleep, then regain by 1
					((and (not (Btst HERO_HUNGRY)) (not daysWithoutSleep))
						(UseStamina -1)
					)
				)
				;mana gets refreshed once every 5 stamina refreshes
				(if (not (-- timerMana))
					(= timerMana 5)
					(UseMana -1)
				)
				;health gets refreshed once every 15 stamina refreshes
				(if (not (-- timerHealth))
					(= timerHealth 15)
					(TakeDamage -1)
				)
			)
		)
	)
	
	(method (replay)
		(SetGraphicsSoundInit)
		(if (not (OneOf curRoomNum 200 202 203 299 600))
			(TheMenuBar draw:)
			(StatusLine enable:)
		)
		(if (DoSound sndSET_SOUND)
			(SetMenu $306 113 TRUE 110 {Turn off})
		else
			(SetMenu $306 113 FALSE 110 {Turn on})
		)
		(super replay:)
	)
	
	(method (newRoom newRoomNumber)
		(HandsOn)
		(super newRoom: newRoomNumber)
		(self
			setCursor: (if isEgoLocked waitCursor else normalCursor) (HaveMouse)
		)
	)
	
	(method (startRoom roomNum &tmp temp0)
		(StartARoom roomNum) ;Most of the startRoom method was moved into its own script.
		(DisposeScript HQROOM)
		
		;attempt to detect fragmented memory, and if so abort the game.
		; if FreeHeap > LargestPtr + 20 ==> (FreeHeap - 20) > LargestPtr
		(if (u> (MemoryInfo FreeHeap) (+ 20 (MemoryInfo LargestPtr)))
			(cond 
				(razzleDazzleRootBeer
					(if (Print 0 6 #button {Debug} TRUE #button { Who Cares_} FALSE)
						; Memory fragmented.						
						(SetDebug)
					)
				)
				;out of memory problem. quit now by throwing quit/restart/restore dialog.
				((!= roomNum CHARSAVE)
					(EgoDead 0 7
						#title {Bitten by a program bug})
					;Suddenly, the deadly poison Fragmentation Bug leaps out of a crack in the system,
					;and injects you with its poison.  Alas, there is no cure, save to . . .
				)
				;CI: NOTE: I can't convert this death into a retry death, because it's
				;caused by a memory leak. Hopefully we've done a good enough job everywhere else
				;that there are no memory leaks to cause this in the first place.
			)
		)
		(= razzleDazzleRootBeer FALSE)
		(mouseDownHandler add: cast features)
		StopWalk
		Cycle
		(super startRoom: roomNum)
	)
	
	(method (handleEvent event &tmp temp0 [temp1 4] temp5 temp6)
		(switch (event type?)
			(keyDown
				(keyDownHandler handleEvent: event)
			)
			(mouseDown
				(if
					(and
						(not razzleDazzleRootBeer)
						(not isEgoLocked)
						(not (mouseDownHandler handleEvent: event))
						(not (Random 0 4)) ;1 in 4 chance of displaying a random mouse click message.
						(& (event modifiers?) shiftDown)
					)
					(HighPrint 297 (Random 26 34))
					;random message about random pixels and nothing to see here stuff
				)
			)
			(mouseUp
				(cast handleEvent: event)
			)
			(direction
				(directionHandler handleEvent: event)
			)
			(joyDown
				(event type: keyDown message: ENTER)
				(keyDownHandler handleEvent: event)
			)
			(speechEvent
				(cond 
					((super handleEvent: event))
					((or (Said 'quit') (Said 'done,done/game'))
						(AskQuit))
					(
						(or
							(Said 'kiss,boff,blow,crap,leak')
							(Said 'get/crap,leak')
							(Said 'eat/crap')
						)
						(HighPrint 297 (Random 0 8))
						;random message about manners
					)
					((Said 'get>')
						(cond 
							((== (= temp0 (SaidGet event)) NULL)
								(HighPrint 0 8)
								;You can't get that.
							)
							((== temp0 iShield)
								(if (== curRoomNum roomShieldDropped)
									(ego get: iShield)
									(HighPrint 0 9)
									;You retrieve your shield.
									(= roomShieldDropped 0)
								else
									(HighPrint 0 10)
									;There aren't any here.
								)
							)
							(
							(and (== temp0 iDagger) (== curRoomNum roomDaggersDropped))
								(if (and monsterInRoom (> gMonsterHealth 0))
									(HighPrint 0 11)
									;You don't have time -- there's the slight matter of the monster that wants to eat you.
								else
									(= temp6 0)
									(= roomDaggersDropped 0)
									(if (or daggersInRoom [invDropped iDagger])
										(= temp6 1)
										(ego get: iDagger (+ daggersInRoom [invDropped iDagger]))
										(HighPrint 0 12)
										;You pick up the loose daggers.
									)
									(if daggersInMonster
										(= temp6 1)
										(ego get: iDagger daggersInMonster)
										(HighPrint 0 13)
										;You retrieve your knives from the dead monster's body, and carefully wipe them clean for reuse.
									)
									(= [invDropped iDagger]
										(= daggersInMonster (= daggersInRoom 0))
									)
									(if (not temp6)
										(HighPrint 0 10)
										;There aren't any here.
									)
								)
							)
							((ego pickUp: temp0 -1)
								(HighPrint 0 14)
								;Ok
							)
							(else
								(HighPrint 0 10)
								;There aren't any here.
							)
						)
					)
					((Said 'throw/dagger') 
						(AnimateThrowingDagger 0)
					)
					((Said 'throw/boulder')
						(AnimateThrowingRock 0)
					)
					((Said 'cast>')
						(cond 
							((not (= temp0 (SaidCast event))) 
								(HighPrint 0 15)
								;That isn't a known spell.
							)
							((CastSpell temp0)
								(switch temp0
									(OPEN
										(AnimateOpenSpell)
										(HighPrint 0 16)
										;The spell has no effect.
									)
									(DETMAGIC (HighPrint 0 17)
										;You sense no magic in this area.
									)
									(DAZZLE (AnimateDazzle)
									)
									(ZAP
										(= zapMeleeBonus (+ 5 (/ [egoStats ZAP] 10)))
										(if (or (ego has: iSword) (ego has: iDagger))
											(HighPrint 0 18)
											;Your weapon is now magically charged.
										else
											(HighPrint 0 19)
											;You don't have a weapon to charge.
										)
									)
									(CALM (AnimateCalm))
									(FLAMEDART
										(AnimateThrowingFlameDart 0)
									)
									(else  (HighPrint 0 16)
										;The spell has no effect.
									)
								)
							)
						)
					)
					((Said 'fight')
						(HighPrint 0 20)
						;Aggressive, aren't you?
					)
					((Said 'chat') (HighPrint 0 21)
						;No one responds.
					)
					((or (Said 'put<down>') (Said 'deposit>'))
						(cond 
							((not (= temp0 (SaidGet event)))
								(HighPrint 0 22)
								;I don't know what you're trying to drop.
							)
							([invNum temp0]
								(if (== temp0 iShield)
									(= roomShieldDropped curRoomNum)
									(ego use: iShield)
								else
									(ego drop: temp0 1)
								)
								(HighPrint 0 23)
								;Ok, you drop it.
								(if (not (ego has: iMushroom)) (Bclr HAVE_KOBOLD_MUSHROOMS))
							)
							(else (HighPrint 0 24))
							;You can't drop something you don't have.
						)
					)
					((Said 'walk') 
						(ChangeGait MOVE_WALK TRUE)
					)
					((Said 'run')
						(ChangeGait MOVE_RUN TRUE)
					)
					((or (Said '[use]/stealth') (Said 'sneak'))
						(if (!= egoGait MOVE_SNEAK)
							(if (TrySkill STEALTH trySneak 0)
								(ChangeGait MOVE_SNEAK TRUE)
							else
								(HighPrint 0 25)
								;You're about as stealthy as the average Goon.
							)
						)
					)
					((Said 'unlock,lockpick/door,hasp')
						(HighPrint 0 26)
						;There's no point in trying that here.
					)
					((Said 'lockpick/nose')
						(if (CanPickLocks)
							(HighPrint 0 27)
							;You delicately insert the lockpick into your left nostril.
							(if (not (TrySkill PICK tryPickNose))
								(EgoDead ;DIE_RESTART
									DIE_RETRY DIE_PICKNOSE 
									0 28
									#title {The surgeon general warns . . ._}
									#icon (GetEgoViewNumber vEgoDeathScenes) 0 0
								)
								;Unfortunately, you push it in too far, causing yourself a cerebral hemorrhage.
								;Guess you should have practiced some more on less difficult locks.
							else
								(HighPrint 0 29)
								;Success! You now have an open nose.
							)
						else
							(HighPrint 0 30)
							;You don't have an appropriate tool.
						)
					)
					((Said 'ask')
						(HighPrint 0 31)
						;You get no response.
					)
					((Said 'buy')
						(HighPrint 0 32)
						;It's not for sale.
					)
					((Said 'eat>')
						(Eat event)
					)
					((Said '/ale<root<dazzle<razzle')
						;toggle the debug mode
						(= razzleDazzleRootBeer (^ razzleDazzleRootBeer $0001))
						;if it's turned on, add it to locales.
						(if razzleDazzleRootBeer
							(curRoom setLocales: DEBUG_RM)
						)
						;update the statusline regardless
						;CI: NOTE: do we need to also remove it from the locales?
						(StatusLine doit:)
					)
					((Said 'use,drink>')
						(Drink event 0)
					)
					((Said 'thank')
						(HighPrint 0 33)
						;You're welcome!
					)
					((Said '/sorry')
						(HighPrint 0 34)
						;That's OK.
					)
					((Said 'affirmative,n,please')
						(HighPrint 0 21)
						;No one responds.
					)
					((Said '/hello')
						(HighPrint 0 35)
						;Hi!
					)
					((Said '/bye')
						(HighPrint 0 36)
						;Bye!
					)
					((Said 'open/door')
						(HighPrint 0 37)
						;You're not close enough to a door.
					)
					((Said 'open/box,musicbox')
						(if (ego has: iMusicBox)
							(HighPrint 0 38)
							;You open the music box, which plays Beethoven's "Fur Elise."  After listening to it for a bit, you close the box.
						else
							(HighPrint 0 39)
							;You don't have a box to open.
							
						)
					)
					((Said 'knock')
						(HighPrint 0 40)
						;No one hears you knocking.
					)
					((Said 'sat')
						(HighPrint 0 41)
						;You don't need to.
					)
					((Said 'gave')
						(HighPrint 0 42)
						;You have no reason to do that.
					)
					((Said 'search[/!*,room,area,area]')
						(HighPrint 0 43)
						;You don't find anything interesting.
					)
					((Said 'look,read>')
						(= temp5
							(+
								[invNum (= temp0 (SaidGet event))]
								[invDropped temp0]
							)
						)
						(cond 
							((not temp0) 
								(HighPrint 297 (Random 10 13))
								;you see nothing special, etc.
							)
							(temp5
								(HighPrint 3 (+ temp0 temp0 1))
								;inventory item descriptions
							)
							(else (HighPrint 0 44)
								;You don't see any here.
							)
						)
					)
					((Said 'rest[/!*]')
						(EgoRests 10 1)
					)
					((or (Said 'nap[/!*]') (Said 'go[<to]/nap'))
						(EgoSleeps 5 0)
					)
				)
			)
		)
	)
	
	(method (wordFail word)
		(Printf 0 45 word)
		;You will not need to use the word "%s" in this game.
	)
	
	(method (syntaxFail)
		(HighPrint 0 46)
		;Please try a different way of saying that.
	)
	
	(method (pragmaFail)
		(HighPrint 0 47)
		;I'm not sure what you're trying to do.
	)
)

(instance egoSW of StopWalk
	(properties)
)

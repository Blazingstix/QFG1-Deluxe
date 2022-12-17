;;; Sierra Script 1.0 - (do not remove this comment)

; Ego-Related View
(define VIEW_PER_AVATER 100)
; Basic Ego Animations
(define vEgo 						500); 000)
(define vEgoStanding 				527); 004)
(define vEgoRunning					528); 005)
(define vEgoSneaking 				529); 006)
(define vEgoDeathScenes				575); 800)

; Ego animations for Intro or Character Screens
(define vEgoRunFast 				530); 007)	;used in the intro Only
(define vEgoCharSelect 				505)
(define vEgoCharSheet 				539) ;802)

; Ego animations for special events
(define vEgoSleep 					531); 010)	;used in Erana's Peace, only
(define vEgoDrawWeapon 				553); 068)	;used only in Path-67 (the Fox room): TODO: rename to FreeFox or something. It's not drawing a weapon, but freeing the fox.
(define vEgoGetFaeryDust 			555); 072)	;used in FaeryRing-70 and in Dryad-76. TODO: Rename to HoldOutHands or something... 

; Ego animations for specific Rooms
(define vEgoCatchSeed 				532); 016)	;used in RM16_AnimateClimbing in Spitting Spirea room
(define vEgoFrogTransform 			533); 019)	;used in Baba Yaga's House (or outside the hut in reverse)
(define vEgoJailDeath				534); 039)	;used as death pic when in Castle.
(define vEgoWalkTree				551); 055)	;used only on healerOutOnALimb.sc
(define vEgoPickupMeep 				552); 062)	;used only in Meeps room
(define vEgoDance 					554); 071)	;used only in FaeryRing-70
(define vEgoKilledByDryad 			556); 079)	;used only in Dryad's room (when being transformed into a stag or a spitting spirea


; Ego animations for End-Game
(define vEgoFalling 				526); 003)	;used in EndGame94 only
(define vEgoFlattenedByJesterDoor 	561); 096)	;CI:TODO:
(define vEgoWithMedal 				564); 139)



(define vEgoTeleportHenry 			557); 081)
(define vEgoSittingHenry 			558); 083) 
(define vEgoKillAntwerp 			559); 085)
(define vEgoWaterReflection 		560); 087)
(define vEgoFlyingCarpet			562); 101)
(define vEgoHoldingMirror			563); 122)
(define vEgoWorking					565); 140)
(define vEgoLarge					566); 148)
(define vEgoSwing					567); 195)
(define vEgoJesterRoom 				568); 296)
(define vEgoGrab 					569); 298)
(define vEgoEatingSleeping 			570); 301)
(define vEgoJumping 				571); 400)

(define vEgoFightWithSword 			501)		;used in Antwerp-84, Castle Courtyard, KoboldCave, and Fighter.sc (when fighting the Weapn Master)
(define vEgoBeginFight 				502)		;used in Antwerp-84, KoboldCave, FrostGiant-58, koboldStartStopFight
(define vEgoDefeated 				503)
(define vEgoInsideBar 				504)
(define vEgoDeadBrigands 			507)		;used in SheriffHouse when falling down stairs. TODO: Rename to SherrifHouseFall
(define vEgoHeroPose 				508)		;used in CastleAnteroom (when being praised for saving the Baronett?)
(define vEgoClimbLadder				509)
(define vEgoThrowing 				510)
(define vEgoFightDagger 			511)		;not actually used anywhere?
(define vEgoFightDaggerNoCape 		512)		;used in Antwerp-84, and KoboldCave
(define vEgoDaggerDefeated 			513)
(define vEgoSwordDefeated 			514)
(define vEgoTired 					515)
(define vEgoDefeatedMagic 			516)
(define vEgoClimbing 				517)
(define vEgoSwordSpirea 			518)
(define vEgoDanceBow 				519)		;used in GoblinAmbush (when defeating goblins?), and EndGame93 (when defeating the Minotaur?)
(define vEgoMagicFetch 				520)
(define vEgoMagicDetect 			521)
(define vEgoMagicFlameDart 			522)
(define vEgoFall 					523)		;used in HeaderHutInside, HermitCave-83, EndGame93-Gate
(define vEgoThrowDagger 			524)
(define vEgoBigGrin 				525)		;used in HealerHutInside, EndGame95, TownLOLHouse, TownSheriffHouse

(define vEgoFightArmDagger 			535)
(define vEgoFall2 					536)		;used in EndGame94, and EndGame96
(define vEgoShock 					537)		;used in Waterfalls-82, EndGame94, EndGame96
(define vEgoFallDown 				538)
(define vEgoFightArmSword 			540)
(define vEgoFightHead 				541)
(define vEgoFightArmEmpty 			542)
(define vEgoBreathHeavy 			550)		;used in EndGame93 (taking deep breaths before running into the wall)

(define vEgoDeathByCat  			572) ;636)
(define vEgoTalkWeaponMaster 		573) ;639)
(define vEgoHeadTurn 				574) ;was shared with 300, vTownOutlook





;other general views used through-out game

(define vEranasPeace 010)
(define vKoboldCave 015)
(define vSpittingSpirea 016)
(define vFrogTransform 019)
(define vBabaYaga1	020)
(define vBabaYaga2	021)
(define vBabaHut	022)
(define vBonehead	023)
(define vWizardWarning 028)
(define vWizardDoor	029)
(define vWizardLobby	030)
(define vWizardTable	031)
(define vWizGameUI		032)
(define vWizGameMonster	033)
(define vCastleGate 	037)
(define vBarracksMan 	038)
(define vCastleGuards 	039)
(define vStables 		040)
(define vCastleGuards2 	041)

(define vFarmerRaking	053)
(define vFarmerTalking 	054)
(define vHealerOutside 	055)
(define vHealerInside 	056)
(define vHealer 		057)
(define vFrostGiant 	059)
(define vMeeps 			060)
(define vGreenMeep		061)
(define vMandrake 		063)
(define vGhosts 		064)
(define vBruno 		 	065)
(define vFox 			067)
(define vFaery 			070)
(define vBrutus 		073)
(define vThrowingRange 	074)
(define vDryad 			076)
(define vDryadRoom 		077)
(define vStag 			078)
(define vStagJump 		080)
(define vHenryDoor 		081)
(define vHenryOutside 	082)
(define vHenryInside 	083)
(define vSecretEntranceRock 084)
(define vWater 			087)
(define vTrollCave 		088)
(define vArrows 		090)
(define vArchers 		091)
(define vBrigandEntrance 	093)
(define vBrigandCourtyard 094)
(define vBrigandDiningHall 095)
(define vJesterRoom 	096)
(define vBrigandLeader 097)
(define vGameSelect 	100)
(define vEndGameCast	139)

(define vBaronett 		141)
(define vTownGate		165)
(define vKoboldSitting	175)
(define vKoboldFighting	176)
(define vKoboldMagic	177)
(define vKoboldDead		178)
(define vKoboldReflect 179)
(define vHollowLog		181)
(define vBrigandLarry 	186)
(define vBrigandMoe 	187)
(define vBrigandCurly 	188)
(define vBrigandArcher 	194)
(define vBrigandDoor 196)
(define vFenrus 199)
(define vBushEyes 240)
(define vBrigandTrio 295)
(define vJesterTrapDoor 296)
(define vYorick 297)
(define vYorickLeave 298)
(define vTownOutlook 300)
(define vInn 	301)
(define vInnCast 302)
(define vSheema 303)
(define vSheemaFood 304)
(define vTownMagicOutside 310)
(define vAdventurerGuild 311)
(define vWolfgang 312)
(define vLOLInside 313)
(define vMagicShop 314)
(define vZara 315)
(define vNoticeBoard 318)
(define vTownDryGoodsOutside 320)
(define vSheriffHouse 321)
(define vDryGoodsInside 322)
(define vCentaurGirl 325)
(define vTownBarOutside 330)
(define vBarInside 331)
(define vThiefGuildMaster 332)
(define vAlley 333)
(define vBartender 336)
(define vBartenderPouring 337)
(define vCrusher 338)
(define vDartGame 340)
(define vDartBoard 341)

;All Fighting Characters
(define vBear 			420)
(define vBearDefeated 	421)
(define vBearFight 		422)
(define vBearHumanFight 423)
(define vArenaDazzle 424)
(define vMinotaur 		425)
(define vMinotaurDefeated 426)
(define vMinotaurFight 	427)
(define vSaurus 		430)
(define vSaurusDefeated 431)
(define vSaurusFight 	432)
(define vMantray 		435)
(define vMantrayDefeated 436)
(define vMantrayFight 	437)
(define vCheetaur 		440)
(define vCheetaurDefeated 	441)
(define vCheetaurFight	442)
(define vGoblin 		445)
(define vGoblinDefeated 446)
(define vGoblinFight	447)
(define vGoblinBush 	448)
(define vTroll 			450)
(define vTrollDefeated	451)
(define vTrollFight 	452)
(define vOgre 			455)
(define vOgreDefeated 	456)
(define vOgreFight 		457)
(define vSaurusRex 		460)
(define vSaurusRexDefeated 	461)
(define vSaurusRexFight 	462)
(define vBrigand 		465)
(define vBrigandDefeated 466)
(define vBrigandFight 	467)
(define vBrigandLeaderFight 473)


(define vCharSelect 	1) ;506) ;changed from 506 to 1 for QFG1Deluxe
(define vTeleportPink 	350);530)
(define vTeleportGreen 	351);531)
(define vExplosion 		352);532)
(define vAntwerp 		353);590)
(define vSheriffAsleep 	354);601)
(define vOttoAsleep 	355);602)
(define vOttoYoyo 		356);606)
(define vMagicCat 		357);635)
(define vCat 			359);637)
(define vWeaponMaster 	360);638)
(define vWeaponMasterTalk 361);639)
(define vBushes 		362);700)
(define vSnow 			363);702)
(define vBlack 			364);777)
(define vDeathScenes 800)
(define vCharSheet 802)
(define vFightUI 803)
(define vFightUiCGA 804)
(define vCreditsDragon 901)
(define vCredits 902)
(define vDragonHead 906)
(define vDragonFire 907)
(define vSierraPresents 908)
(define vSubtitle 913)
(define vQFGshadow 918)
(define vQFG 919)
(define vQFG2 920)
(define vTrialByFire 925)
(define vEndCreditsCarpet 925)
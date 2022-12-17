;;; Sierra Script 1.0 - (do not remove this comment)
(script# WARRIOR); WARRIOR = 213
(include system.sh) (include sci2.sh) (include game.sh)
(use Main)
(use StatusBar)
(use Skilled)
(use _Inventory)
(use _Actor)

(use _Interface)

(public
	warrior 0
)

(class Warrior of SkilledActor
	(properties
		register   FALSE	;TRUE/FALSE. Used to determine if Ego died during this turn.
		;change these from the default values
		yStep 		10
		xStep 		16
		;add these on top of 'Skilled'
		heroTitle 	NULL
		egoHP 		NULL
		egoSP 		NULL
		egoMP 		NULL
		weaponView 	0	;the view Number for Ego's weapon.
		egosBack 	0	;a View: ego's backside
		egoShield 	0	;a View: ego's Shield, if one exists
		egoHand 	0	;a Prop, showing the Ego's Hand.
		usingSword 	0	;unused. Presumably intended to know if ego is using a sword or a dagger.
		noWeapon 	FALSE ;set to TRUE if the Hero is entering combat unarmed. (results in death)
		baseX 		190
		baseY 		190
	)
	
	(method (init)
		(= strength [egoStats STR])
		(= intell 	[egoStats INT])
		(= agil 	[egoStats AGIL])
		(= vit 		[egoStats VIT])
		(= luck 	[egoStats LUCK])
		(= weap 	[egoStats WEAPON])
		(= parry 	[egoStats PARRY])
		(= dodge 	[egoStats DODGE])
		(= magic 	[egoStats MAGIC])
		(= stamina 	[egoStats STAMINA])
		(= health 	[egoStats HEALTH])
		(= mana 	[egoStats MANA])
		(super init: &rest)
	)
	
	(method (dispose)
		(Bclr HERO_KILLED_IN_BATTLE)
		(if egoMP (egoMP dispose:) (= egoMP NULL))
		(if heroTitle
			(egoSP dispose:)
			(egoHP dispose:)
			(Display WARRIOR 0 p_restore heroTitle)
			;Hero Status
			(= heroTitle (= egoHP (= egoSP NULL)))
		)
		(super dispose:)
	)
	
	(method (getHurt amount)
		(TakeDamage amount)
		(super getHurt: amount)
		(self setEgoHP: health)
		(cond 
			((or (Btst HERO_KILLED_IN_BATTLE) (<= health 0))
				(self register: TRUE)
			)
			((not (script script?))
				(script setScript: (ScriptID ARENA_PAIN 0)) ;arenaPainReaction
			)
		)
		(if register
			(Animate (cast elements?) FALSE)
			(EgoDead DIE_RETRY DIE_ARENA WARRIOR 2
				#title {What a monster!}
				#icon (GetEgoViewNumber vEgoDefeatedMagic) 0 9
			)
			;It was a tough battle, and you lost. Never fear!
			;All you have to do is restore your game, and...\nWhat do you mean, "Restore WHAT game?"

			(self resetBeforeArena:)
		)
		
	)
	
	(method (getTired amount)
		(UseStamina amount)
		(super getTired: amount)
		(self setEgoSP: stamina)
	)
	
	(method (die)
		(Bset HERO_KILLED_IN_BATTLE)
	)
	
	(method (startCombat whatScript)
		(self setScript: (ScriptID whatScript 0))
	)
	
	(method (drawStatus)
		(= heroTitle
			(Display
				WARRIOR 1
				p_width 80
				p_at 13 13
				p_mode teJustLeft
				p_font 300
				p_color hpFontColour
				p_save
			)
		)
		((= egoHP (StatusBar new:))
			x: 25
			y: 33
			titleCel: 0
			priority: 1
			max: (MaxHealth)
			value: health
			init:
		)
		((= egoSP (StatusBar new:))
			x: 25
			y: 47
			titleCel: 1
			priority: 1
			max: (MaxStamina)
			value: [egoStats STAMINA]
			init:
		)
		(if [egoStats MAGIC]
			((= egoMP (StatusBar new:))
				x: 25
				y: 61
				titleCel: 2
				priority: 1
				max: (MaxMana)
				value: [egoStats MANA]
				init:
			)
		)
	)
	
	(method (drawWeapons &tmp theArmorEnc)
		(= baseX (opponent warriorX?))
		(= baseY (opponent warriorY?))
		(= shieldValue (= egoShield 0))
		(if (ego has: iShield)
			(= shieldValue 10)
			((= egoShield aShield)
				view: (GetEgoViewNumber vEgoFightArmSword)
				setLoop: 1
				setCel: 0
				setPri: 14
				x: (- baseX 74)
				y: baseY
				ignoreActors: 1
				init:
				stopUpd:
			)
		else
			(= shieldValue 0)
			((= egoHand aHand)
				view: (GetEgoViewNumber vEgoFightArmDagger)
				setLoop: 1
				setCel: 0
				setPri: 14
				x: (- baseX 73)
				y: baseY
				ignoreActors: 1
				init:
				stopUpd:
			)
		)
		(= armorEnc (/ (= theArmorEnc (WtCarried)) 2))
		(if (> theArmorEnc (MaxWeight))
			(= armorEnc theArmorEnc)
		)
		(= armorValue 0)
		(cond 
			((ego has: iChainmail) (= armorValue 5))
			((ego has: iLeather) (= armorValue 3))
		)
		(cond 
			((ego has: iSword) (= weaponView (GetEgoViewNumber vEgoFightArmSword)) (self weapValue: 8))
			((ego has: iDagger) (= weaponView (GetEgoViewNumber vEgoFightArmDagger)) (self weapValue: 5))
			(else (= weaponView (GetEgoViewNumber vEgoFightArmEmpty)) (= noWeapon TRUE))
		)
		((= egosBack closeupEgo)
			view: (GetEgoViewNumber vEgoFightHead)
			setLoop: 0
			setCel: 0
			setPri: 15
			x: (- baseX 41)
			y: baseY
			ignoreActors: 1
			init:
			stopUpd:
		)
		(self
			illegalBits: 0
			ignoreActors: 1
			view: weaponView
			setLoop: (if noWeapon 0 else 2)
			cel: 0
			setPri: 11
			posn: baseX baseY
			stopUpd:
		)
	)
	
	(method (setEgoHP theSkills)
		(= [egoStats HEALTH] theSkills)
		(if egoHP (egoHP value: theSkills draw:))
	)
	
	(method (setEgoMP theMana)
		(= mana theMana)
		(if egoMP (egoMP value: theMana draw:))
	)
	
	(method (setEgoSP theSkills)
		(= [egoStats STAMINA] theSkills)
		(if egoSP (egoSP value: theSkills draw:))
	)
	
	(method (resetBeforeArena)
		; Resets the game to before the fight occured.
		; note: skills are not reset, by design. The thinking is that if you died, 
		; then you probably need the boost in skills, and this is still a game, afterall, and supposed to be fun.
		; Since deaths are recorded, anybody who cares about completing without a death can still do so.
		;
		
		;now we set up what happens if the player selects Retry:
		;restore 1/4 of total health.
		(ego setHeroRetry:)
		;clear the Monster
		(= monsterInRoom NULL)
		;returns any thrown daggers to the hero.
		(if daggersInMonster
			(ego get: iDagger daggersInMonster)
			(= daggersInMonster 0)
		)
		;clear the fact the hero's been killed in battle.
		(Bclr HERO_KILLED_IN_BATTLE)
		;undo the death flag (although it will just get disposed anyway, so no big deal really.
		(self register: FALSE)
		;go back to the forest.
		(curRoom newRoom: prevRoomNum)
		;CI: TODO: account for special battles, like bear, or minotaur.
		;Note, nothing special is required for Bear, or Brutus.  Maybe Troll or Minotaur?
	)
)

(instance warrior of Warrior
	(properties)
)

(instance aShield of View
	(properties)
)

(instance aHand of Prop
	(properties)
)

(instance closeupEgo of View
	(properties)
)

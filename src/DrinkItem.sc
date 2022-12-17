;;; Sierra Script 1.0 - (do not remove this comment)
(script# EGODRINK) ;EGODRINK = 5
(include system.sh) (include sci2.sh) (include game.sh)
(use Main)
(use _Inventory)

(public
	Drink 0
)

(procedure (Drink event what &tmp beverage)
	(if what
		(= beverage what)
	else
		(= beverage (SaidGet event))
	)
	(cond 
		((not beverage)
			(event claimed: FALSE)
			(cond 
				((Said '/water') ;Bugged for some reason; claims "That isn't drinkable."
					(if (ego has: iFlyingWater)
						(ego use: iFlyingWater 1)
						(ego get: iFlask)
						(cond 
							((not (ego has: iFlyingWater)) (Bclr HAVE_LAKE_WATER) (Bclr HAVE_FLYING_WATER))
							((Btst HAVE_LAKE_WATER) (Bclr HAVE_LAKE_WATER))
							((Btst HAVE_FLYING_WATER) (Bclr HAVE_FLYING_WATER))
						)
						(HighPrint 5 0)
						;You drink a flask of water.  It tastes great.
						(HighPrint 5 1)
						;(Less filling, too!)
					)
				)
				((Said '/potion')
					(= beverage 0)
					(= what iHealingPotion)
					(while (<= what iGhostOil)
						(cond 
							((and [invNum what] (not beverage)) (= beverage what))
							((and beverage [invNum what]) (= beverage -1) (break))
						)
						(++ what)
					)
					(switch beverage
						(-1 
							(HighPrint 5 2)
							;Perhaps you could tell me which potion you want to drink.							
						)
						(0
							(HighPrint 5 3)
							;You have no potions.	
						)
						(else  (Drink event beverage))
					)
				)
				(else (event claimed: TRUE)
					(HighPrint 5 4)
					;You don't see any here.
				)
			)
		)
		((or (< beverage iHealingPotion) (> beverage iGhostOil))
			(event claimed: FALSE)
			(if (Said 'drink')
				(HighPrint 5 5)
				;That isn't drinkable.
			else
				(HighPrint 5 6)
				;It's not clear how you want to use that.
			)
			(event claimed: TRUE)
		)
		((not [invNum beverage])
			(HighPrint 5 7)
			;You don't have any of those.
		)
		((== beverage iHealingPotion)
			(HighPrint 5 8)
			;The drink soothes as it goes down.
			(TakeDamage (- (/ (MaxHealth) 2)))
			(ego use: iHealingPotion)
			(ego get: iFlask)
		)
		((== beverage iManaPotion)
			(HighPrint 5 9)
			;The drink burns as it goes down.
			(UseMana (- (/ (MaxMana) 2)))
			(ego use: iManaPotion)
			(ego get: iFlask)
		)
		((== beverage iStaminaPotion)
			(HighPrint 5 10)
			;The drink is invigorating.
			(UseStamina (- (MaxStamina)))
			(ego use: iStaminaPotion)
			(ego get: iFlask)
		)
		((== beverage iDisenchant)
			(HighPrint 5 11)
			;You don't feel anything.  Perhaps this was not the correct way to use this potion.
			(ego use: iDisenchant)
			(ego get: iFlask)
			(event claimed: TRUE)
		)
		((== beverage iGhostOil)
			(if (ego has: iGhostOil)
				(HighPrint 5 12)
				;You feel a tingling sensation as you rub the unguent all over your body.
				(Bset PROTECTED_UNGUENT)
				(Bclr GHOSTS_ATTACK)
				(= timerGhostOil ONEHOUR)
				(ego use: iGhostOil)
				(ego get: iFlask)
			else
				;CI: NOTE: This possibility can't ever be reached. The cond checks if we have any of the item, before we ever get here.
				(HighPrint 5 13)
				;You can't.  You don't have any.
			)
		)
	)
	(DisposeScript EGODRINK)
)

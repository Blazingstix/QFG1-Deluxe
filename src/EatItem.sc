;;; Sierra Script 1.0 - (do not remove this comment)
(script# EGOEAT) ;EGOEAT = 6
(include system.sh) (include sci2.sh) (include game.sh)
(use Main)

(public
	Eat 0
)

(procedure (Eat event &tmp food)
	(= food 0)
	(cond 
		((Said '/mushroom,toadstool')
			(cond 
				((and (not (ego has: iMushroom)) (!= curRoomNum 70))
					(HighPrint 6 0)
					;You don't have any.
					)
				((Btst HAVE_KOBOLD_MUSHROOMS)
					(ego use: iMushroom 3)
					(if (Btst HAVE_FAIRY_MUSHROOMS)
						(HighPrint 6 1)
						;The mushrooms from the Fairy Ring are pretty good, so you hear. 
						;However, when they're mixed with Kobold toadstools...that's a horse of a different color.
					)
					(HighPrint 6 2)
					;You eat some of the Kobold's fungus.
					;Within seconds your insides feel like they're on fire.
					;Guess you're not a Kobold, huh?
					(if (<= [egoStats HEALTH] 20)
						(EgoDead DIE_RETRY DIE_EAT_KOBOLDMUSHROOMS 
							{Didn't anyone warn you about eating other people's food?__Death by Toadstools doesn't leave mushroom for improvement.}
							#icon vDeathScenes 1 1
						)
					else
						(TakeDamage 20)
						(HighPrint 6 3)
						;You think that you'd better take it easy for a while until you recover.
					)
					(if (not (ego has: iMushroom))
						(Bclr HAVE_KOBOLD_MUSHROOMS)
					)
				)
				(else
					(ego use: iMushroom 3)
					(if (Btst HERO_STARVING) (Bclr HERO_STARVING) else (Bclr HERO_HUNGRY))
					(HighPrint 6 4)
					;You ingest a few of the mushrooms from the Faery Ring.  They taste delicious.
					(HighPrint 6 5)
					;Wow!  Look at all the nice paisley horses!
					(HighPrint 6 6)
					;Not to mention the beautiful neon sky.
					(if (Btst ATE_FAIRY_MUSHROOMS)
						(EgoDead DIE_RETRY DIE_EAT_MUSHROOMS 6 7
							#title {Where did all the pretty horses go?}
							#icon vDeathScenes 1 1
							;That's funny.  I could have sworn I warned you about eating too many Magic Mushrooms.
							;Your mind permanently warped, you die a garishly polka-dotted death.
						)
					else
						(HighPrint 6 8)
						;Hmm, that was interesting.  But it would probably not be a good idea to repeat the experience.
					)
					(Bset ATE_FAIRY_MUSHROOMS)
				)
			)
		)
		((Said '/acorn,nut')
			(if (not (ego has: iAcorn))
				(HighPrint 6 0)
				;You don't have any.
			else
				(ego use: iAcorn 1)
				(HighPrint 6 9)
				;You eat the Magic Acorn. It tastes terrible. Better stick to real food next time.
			)
		)
		(mealsPreEaten (event claimed: TRUE)
			(HighPrint 6 10)
			;There's no need.  You're not hungry.
		)
		((Said '/apple')
			(if (ego has: iFruit)
				(ego use: iFruit 3)
				(HighPrint 6 11)
				;You eat some of the apples.  They actually taste quite good.
				(= food 1)
			else
				(HighPrint 6 12)
				;You don't have any fruit.
			)
		)
		((Said '/carrot')
			(if (ego has: iVegetables)
				(ego use: iVegetables 2)
				(HighPrint 6 13)
				;You eat some vegetables. You think they would have been better saved for cows or horses.
				;Real Heroes eat preserved dry rations.
				(= food 1)
			else
				(HighPrint 6 14)
				;You don't have any vegetables.
			)
		)
		((or (Said '/ration,food') (Said 'eat[/!*]'))
			(if (ego has: iRations)
				(ego use: iRations 1)
				(HighPrint 6 15)
				;The rations are tasteless but filling.
				(= food 1)
			else
				(HighPrint 6 16)
				;You aren't carrying any rations.
			)
		)
		(else (event claimed: TRUE) (HighPrint 6 17))
		;Ugh. You don't want to eat *that*.
	)
	(if food
		(if (Btst HERO_HUNGRY)
			(Bclr HERO_HUNGRY)
			(Bclr HERO_STARVING)
		else
			(= mealsPreEaten 1)
		)
	)
	(DisposeScript EGOEAT)
)

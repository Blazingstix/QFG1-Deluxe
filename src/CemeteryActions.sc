;;; Sierra Script 1.0 - (do not remove this comment)
(script# CEMETERY) ;CEMETERY = 806
(include system.sh) (include sci2.sh) (include game.sh)
(use Main)
(use _Game)

(public
	Cemetery 0
)

(instance Cemetery of Region
	(properties)
	
	(method (handleEvent event &tmp [temp0 54])
		(if (event claimed?) (return))
		(switch (event type?)
			(evSAID
				(cond 
					((super handleEvent: event))
					((Said 'look>')
						(cond 
							((Said '[<at,around][/!*,forest,greenery]')
								(HighPrint 806 0)
								;The late frosts of winter give way to to the greenery of spring. There is the crisp smell of cedar in the air. This forest seems to be unusually still and quiet.
							)
							((Said '/ghost')
								(if
									(or
										(!= ((ScriptID GHOSTS 5) script?) NULL)
										(!= ((ScriptID GHOSTS 6) script?) NULL)
										(!= ((ScriptID GHOSTS 7) script?) NULL)
									)
									(HighPrint 806 1)
									;It IS a ghost...a transparent, decaying, writhing, slimy, undead fragment of undulating ectoplasm!
									(HighPrint 806 2)
									;You might be near a graveyard!
								else
									(HighPrint 806 3)
									;There are no ghosts here to look at.
								)
							)
							((Said '[<down][/ground,needle,moss,grass]')
								(HighPrint 806 4)
								;Pine needles, moss and early spring grasses cover the forest floor.
							)
							((Said '[<up][/sky,cloud,star,moon]')
								(if isNightTime
									(HighPrint 806 5)
									;The evening is clear and the stars are bright. Dark clouds pass over the moon.
								else
									(HighPrint 806 6)
									;The sky is a piercing blue with scudding white clouds.
								)
							)
							((Said '/birch,tree')
								(HighPrint 806 7)
								;You can see pines, cedars, birches and other trees frequently associated with mountain forests.
							)
							((Said '/bush')
								(HighPrint 806 8)
								;The low-lying bushes form tight tangles between the trees.
							)
							((Said '/boulder')
								(HighPrint 806 9)
								;The valley floor is covered by fine, moist soil with few large rocks.
							)
							((Said '/hill,cliff,peak,pass')
								(HighPrint 806 10)
								;Looking through the surrounding woods, you occasionally are able to catch a glimpse of the snow-capped mountain peaks.
							)
							((Said '/cave')
								(HighPrint 806 11)
								;There are no caves here.
							)
						)
					)
					((Said 'climb')
						(HighPrint 806 12)
						;Climbing would serve no purpose right now.
					)
					((Said 'get/ghost')
						(if isNightTime
							(HighPrint 806 13)
							;You don't get ghosts.  Ghosts get YOU!
						else
							(HighPrint 806 14)
							;There aren't any.
						)
					)
					(
					(Said 'drink/grease,(potion,grease<ghost,ghoul)')
						(if (ego has: iGhostOil)
							(HighPrint 806 15)
							;That isn't drinkable -- it's an oil.
						else
							(HighPrint 806 16)
							;You can't.  You don't have any.
						)
					)
					(
					(Said 'use,rub/grease,(potion,grease<ghost,ghoul)')
						;CI: TODO: Combine this with the Drink procedure
						(if (ego has: iGhostOil)
							(HighPrint 806 17)
							;You feel a tingling sensation as you rub the unguent all over your body.
							(Bset PROTECTED_UNGUENT)
							(Bclr GHOSTS_ATTACK)
							(= timerGhostOil ONEHOUR)
							(ego use: iGhostOil)
							(ego get: iFlask)
							(if isNightTime (SolvePuzzle POINTS_USEUNDEADUNGUENT 2))
						else
							(HighPrint 806 16)
							;You can't.  You don't have any.
						)
					)
				)
			)
		)
	)
)

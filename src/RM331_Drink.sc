;;; Sierra Script 1.0 - (do not remove this comment)
(script# TAVERN_DRINK) ;TAVERN_DRINK = 336
(include system.sh) (include sci2.sh) (include game.sh)
(use Main)
(use _Interface)
(use _Motion)
(use _User)
(use _Actor)
(use _System)

(public
	drinkDown 0
)

(define DB 1)
(define bartender 2)
(define head 5)

(instance drinkDown of Script
	(properties)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(HandsOff)
				(ego setCycle: EndLoop)
				(= cycles 10)
			)
			(1
				(switch drinkInHand
					(drinkAle
						(switch numberOfAlesDrunk
							(1
								(HighPrint 336 0)
								;This tastes as sour as it smells, and it burns your throat as you swallow it. Still, it isn't the worst beer you've ever drunk.
								(= cycles 5)
							)
							(2
								(HighPrint 336 1)
								;You know, that actually tasted fine! This really isn't such a bad place, after all,
								;and the bartender reminds you of an old friend you used to know.
								(= cycles 5)
							)
							(3
								(HighPrint 336 2)
								;Suddenly, you don't feel so good...
								(client setScript: tooDrunk)
							)
						)
					)
					(drinkSweat
						(HighPrint 336 3)
						;Smells like Troll's Sweat. Tastes like Troll's Sweat. By golly, it IS Troll's Sweat....
						(client setScript: tooDrunk)
					)
					(drinkBreath
						(HighPrint 336 4)
						;You've never tasted anything like it before.
						(client setScript: breathDeath)
					)
				)
			)
			(2 
				(ego setCycle: BegLoop self)
			)
			(3
				(= drinkInHand drinkNothing)
				(User canInput: TRUE)
				(client setScript: NULL)
				(DisposeScript TAVERN_DRINK)
			)
		)
	)
)

(instance tooDrunk of Script
	(properties)
	
	(method (dispose)
		(super dispose:)
		(DisposeScript TAVERN_DRINK)
	)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(HandsOff)
				(ego setCycle: BegLoop)
				(= cycles 15)
			)
			(1
				(ego loop: 6 cel: 0)
				(deadMug init:)
				(addToPics add: deadMug doit:)
				(= cycles 2)
			)
			(2
				(switch drinkInHand
					(drinkAle
						(HighPrint 336 5)
						;Too much beer.
						)
					(drinkSweat
						(HighPrint 336 6)
						;...and one Troll's Sweat is too many.
						)
				)
				(= cycles 2)
			)
			(3
				(ego cycleSpeed: 2 setCycle: CycleTo 6 cdFORWARD)
				(= cycles 25)
			)
			(4
				(ego cycleSpeed: 0 setCycle: EndLoop)
				(= cycles 20)
			)
			(5
				(cast eachElementDo: #dispose)
				(curRoom drawPic: 400 16) ;draw "black out" pic.
				(self cue:)
			)
			(6
				(SolvePuzzle POINTS_GOTDRUNK -5)
				(Bset TAVERN_DRUNK)
				(curRoom newRoom: 330)
			)
		)
	)
)

(instance breathDeath of Script
	(properties)
	
	(method (dispose)
		(super dispose:)
		(DisposeScript TAVERN_DRINK)
	)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(HandsOff)
				(Print 336 7)
				; Ohhhhhhhhh woooowwwwwww!!!
				(ego loop: 5 cel: 0 cycleSpeed: 2 setCycle: CycleTo 12 cdFORWARD self)
			)
			(1
				;CI: these add a Dust PicView, which cannot be removed until the pic is redrawn. We don't want that, because of the Retry
				;so we'll try other objects until we find one that works.
				;Prop might work.
				;(dust init:)
				;(addToPics doit:)
				
				;(dust2 init: stopUpd:)
				((ScriptID TAVERN 9) show:) ;show the dust
				(ego setCycle: EndLoop self)
			)
			(2
				(EgoDead DIE_RETRY DIE_DRINK_DRAGONSBREATH 336 8
					#title { Talk about a "fiery brew"._}
					#icon vBarInside 0 0
					;Maybe you really shouldn't have tried the Dragon's Breath!  Better luck next time, and we hope you saved your game.
				)
				
				;CI:TODO remove dust
				;(dust2 dispose:)
				(= drinkInHand drinkNothing)
				(ego loop: 2 cel: 0 cycleSpeed: 0 stopUpd:)
				;CI: TODO: add the money back the same way it was spent (i.e. silvers + gold)
				(= [invNum iSilver] (+ [invNum iSilver] 25))
				((ScriptID TAVERN 9) hide:)
				((ScriptID TAVERN head) show: setPri: 8);show ego's head.
				((ScriptID TAVERN DB) startUpd:)
				(HandsOn)
				(User canControl: FALSE)
				(client setScript: NULL)
				(DisposeScript TAVERN_DRINK)
				;(Printf {TAVERN\_DRINK disposed})
			)
		)
	)
)

(instance deadMug of PicView
	(properties
		y 80
		x 165
		view vBarInside
		cel 2
		priority 12
	)
)

;;;(instance dust of PicView
;;;	(properties
;;;		y 89
;;;		x 149
;;;		view vEgoInsideBar
;;;		loop 2
;;;		cel 4
;;;		priority 15
;;;	)
;;;)
;;;
;;;(instance dust2 of Prop
;;;	(properties
;;;		y 89
;;;		x 149
;;;		view vEgoInsideBar
;;;		loop 2
;;;		cel 4
;;;		priority 15
;;;	)
;;;)

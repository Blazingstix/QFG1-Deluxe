;;; Sierra Script 1.0 - (do not remove this comment)
(script# GHOSTS) ;GHOSTS=805
(include system.sh) (include sci2.sh) (include game.sh)
(use Main)
(use _Reverse)
(use _Follow)
(use _Motion)
(use _Actor)
(use _System)

(public
	twistIt 0
	spinAcross 1
	spinOnTree 2
	swimRight 3
	swimLeft 4
	twister 5
	tumbler 6
	swimmer 7
)

(local
	twisterX
	twisterY
	theTwisterX
	theTwisterY
	local4
)
(instance twister of Actor
	(properties)
)

(instance tumbler of Actor
	(properties)
)

(instance swimmer of Actor
	(properties)
)

(instance twistIt of Script
	(properties)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(twister
					view: vGhosts
					setLoop: 0
					setPri: 12
					ignoreActors:
					ignoreHorizon:
					illegalBits: 0
					posn: 155 -10
					cycleSpeed: 1
					setCycle: Forward
					startUpd:
					setMotion: MoveTo 200 50 self
				)
				(= twisterX (twister x?))
				(= twisterY (twister y?))
			)
			(1
				(= theTwisterX twisterX)
				(= theTwisterY twisterY)
				(= twisterX (Random 20 300))
				(= twisterY (Random 10 130))
				(twister
					setCycle: (if (> twisterX theTwisterX) Forward else Reverse)
				)
				(if (Btst GHOSTS_ATTACK)
					(twister setMotion: Follow ego 30)
				else
					(twister setMotion: MoveTo twisterX twisterY self)
				)
			)
			(2
				(if
					(or
						(< ((ScriptID GHOSTS 5) x?) 30)
						(> ((ScriptID GHOSTS 5) x?) 290)
						(< ((ScriptID GHOSTS 5) y?) 20)
					)
					(self cue:)
				else
					(self changeState: 1)
				)
			)
			(3
				(twister
					setMotion:
						MoveTo
						(cond 
							((< ((ScriptID GHOSTS 5) y?) 20) (twister x?))
							((< ((ScriptID GHOSTS 5) x?) 30) -20)
							((> ((ScriptID GHOSTS 5) x?) 290) 340)
						)
						(if (< ((ScriptID GHOSTS 5) y?) 20)
							-10
						else
							(twister y?)
						)
						self
				)
			)
			(4
				(-- ghostCount)
				(twister stopUpd: setScript: NULL)
			)
		)
	)
)

(instance spinAcross of Script
	(properties)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(tumbler
					view: vGhosts
					setLoop: 8
					setPri: 7
					setStep: 5 3
					ignoreActors:
					ignoreHorizon:
					illegalBits: 0
					posn: -15 (Random 40 110)
					setCycle: Forward
					startUpd:
				)
				(if (Btst GHOSTS_ATTACK)
					(tumbler setMotion: Follow ego 30)
				else
					(tumbler setMotion: MoveTo 345 (Random 0 80) self)
				)
			)
			(1
				(-- ghostCount)
				(tumbler stopUpd: setScript: NULL)
			)
		)
	)
)

(instance spinOnTree of Script
	(properties)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(tumbler
					view: vGhosts
					setLoop: 8
					setStep: 5 3
					ignoreActors:
					ignoreHorizon:
					illegalBits: 0
					posn: 253 -10
					setCel: 0
					setMotion: MoveTo 253 39
					startUpd:
				)
				(= cycles 35)
			)
			(1
				(++ local4)
				(tumbler setCel: RELEASE setCycle: EndLoop self)
			)
			(2
				(if (> local4 (Random 2 4))
					(tumbler setCycle: Forward)
					(if (Btst GHOSTS_ATTACK)
						(tumbler setMotion: Follow ego 30)
					else
						(tumbler setMotion: MoveTo -15 (Random 40 110) self)
					)
				else
					(self changeState: 1)
				)
			)
			(3
				(-- ghostCount)
				(tumbler stopUpd: setScript: NULL)
			)
		)
	)
)

(instance swimRight of Script
	(properties)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(swimmer
					view: vGhosts
					setStep: 6 4
					ignoreActors:
					ignoreHorizon:
					illegalBits: 0
					setLoop: 1
					cycleSpeed: 1
					setCycle: Forward
					posn: -15 (Random 25 65)
					startUpd:
				)
				(if (Btst GHOSTS_ATTACK)
					(swimmer setMotion: Follow ego 30)
				else
					(swimmer setMotion: MoveTo 350 (Random 85 125) self)
				)
			)
			(1
				(-- ghostCount)
				(swimmer stopUpd: setScript: NULL)
			)
		)
	)
)

(instance swimLeft of Script
	(properties)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(swimmer
					view: vGhosts
					setStep: 6 4
					ignoreActors:
					ignoreHorizon:
					illegalBits: 0
					setLoop: 2
					cycleSpeed: 1
					setCycle: Forward
					posn: 325 (Random -10 10)
					startUpd:
				)
				(if (Btst GHOSTS_ATTACK)
					(swimmer setMotion: Follow ego 30)
				else
					(swimmer
						setMotion: MoveTo (Random 115 145) (Random 55 85) self
					)
				)
			)
			(1
				(swimmer setMotion: MoveTo (Random 30 65) -10 self)
			)
			(2
				(-- ghostCount)
				(swimmer stopUpd: setScript: NULL)
			)
		)
	)
)

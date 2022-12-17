;;; Sierra Script 1.0 - (do not remove this comment)
(script# 110)
(include system.sh) (include sci2.sh) (include game.sh)
(use Main)
(use KoboldCave)
(use _Sound)
(use _Motion)
(use _Actor)

(public
	castFlame 0
)

(local
	newSound
	local1
	hitKobold
	gKoboldX
	gKoboldY
	[theGKoboldX 6] = [195 225 262 17 17 44]
	[theGKoboldY 6] = [47 43 92 47 134 155]
)
(instance flScript of KScript
	(properties)
	
	(method (dispose)
		(newSound dispose:)
		(Bclr KOBOLD_HERO_CAST_FLAMEDART)
		(KoboldFight (not (Btst DEFEATED_KOBOLD)))
		(DisposeScript 110)
	)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(client
					setCycle: Forward
					setMotion: MoveTo gKoboldX gKoboldY self
				)
			)
			(1
				(if (and hitKobold (Btst KOBOLD_REVERSAL_ACTIVE))
					(= hitKobold FALSE)
					(Bset KOBOLD_BOUNCING_FLAMEDART)
					(client
						y: (+ (client y?) 15)
						z: 15
						setMotion: MoveTo (ego x?) (ego y?) self
					)
				else
					(self cue:)
				)
			)
			(2
				(if hitKobold 
					(= hitKobold FALSE) 
					(gKobold getHurt: damageToKoboldFlame)
				)
				;if the flame dart goes off too close to ego, ego gets hurt instead.
				;like when the kobold has cast Reversal.
				(if (< (client distanceTo: ego) 20)
					(KoboldHurtEgo damageToKoboldFlame)
				)
				(client
					view: vExplosion
					setLoop: 0
					cel: 0
					cycleSpeed: 1
					setCycle: EndLoop self
				)
				(newSound number: (GetSongNumber 45) play:)
			)
			(3 (client dispose:))
		)
	)
)

(instance castFlame of KScript
	(properties)
	
	(method (changeState newState &tmp temp0 egoLoop)
		(switch (= state newState)
			(0
				(HandsOff)
				(FaceObject ego gKobold)
				(Bset KOBOLD_HERO_CAST_FLAMEDART)
				(= egoLoop (ego loop?))
				(if
					(or
						(and fightingKobold egoLoop)
						(and (not fightingKobold) (& egoLoop $0001))
					)
					(= egoLoop loopW)
				else
					(= egoLoop loopE)
				)
				(ego
					view: (GetEgoViewNumber vEgoMagicFlameDart)
					setLoop: egoLoop
					cel: 0
					setCycle: CycleTo 5 cdFORWARD self
				)
			)
			(1
				(if
				(TrySkill MAGIC 0 (- 50 (/ (ego distanceTo: gKobold) 5)))
					(= hitKobold TRUE)
					(= gKoboldX (gKobold x?))
					(= gKoboldY (gKobold y?))
				else
					(= temp0 (+ (Random 0 2) (* egoKoboldBattleLoop 3)))
					(= hitKobold FALSE)
					(= gKoboldX [theGKoboldX temp0])
					(= gKoboldY [theGKoboldY temp0])
				)
				(ego setCycle: EndLoop)
				((= newSound (Sound new:))
					number: (GetSongNumber 33)
					priority: 3
					init:
					play:
				)
				((Actor new:)
					view: (GetEgoViewNumber vEgoMagicFlameDart)
					setLoop: 2
					setCel: 0
					illegalBits: 0
					ignoreActors:
					x: (if (== (ego loop?) 1)
						(- (ego x?) 19)
					else
						(+ (ego x?) 19)
					)
					y: (- (ego y?) 21)
					setStep: 12 8
					init:
					setScript: flScript
				)
				(self dispose:)
			)
		)
	)
)

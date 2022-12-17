;;; Sierra Script 1.0 - (do not remove this comment)
(script# SKILLED) ;SKILLED = 212
(include system.sh) (include sci2.sh) (include game.sh)
(use Main)
(use _Actor)


(class SkilledActor of Actor
	(properties
		name		"Skilled"
		;stats related to fighting.
		strength 	0
		intell 		0
		agil 		0
		vit 		0
		luck 		0
		weap 		0
		parry 		0
		dodge 		0
		magic 		0
		;HP, SP, and MP
		stamina 	0
		health 		0
		mana 		0
		;offense and defense vales
		armorValue 	0	;add to defense
		armorEnc 	0	;deduct from defense
		shieldValue 0
		weapValue 	0
		canFight 	0	;TRUE/FALSE
		action 		0	;one of 10 values (defined as 'Arena Actions')
		opponent 	0
		fightLeft 	0	;TRUE/FALSE (is the SkilledActor facing left?)
	)
	
	(method (init)
		(= canFight (= action 0))
		(super init: &rest)
	)
	
	(method (attackLevel &tmp ret)
		;basic attack level is the average of 8*weapon, 4*agil, 2*strength, intelligence*1, luck*1
		(= ret
			(/
				(+ (* weap 8) (* agil 4) strength strength intell luck)
				16
			)
		)
		;if we're tired, then reduce the attack level by 10
		(if (<= stamina 30)
			(= ret (- ret 10))
			;if we're *really* tired, take off another 20.
			(if (<= stamina 10) 
				(= ret (- ret 20))
			)
		)
		;take off another 10 per day without sleep.
		(= ret (- ret (* daysWithoutSleep 10)))

		;if we're doing action 1 (thrusting)
		(if (== action aaTHRUST)
			(= ret (+ ret 10))
		)
		(return ret)
	)
	
	(method (defenseLevel &tmp ret parryBonus dodgeBonus)
		;basic defense level is the average of 5*agility, 2*intelligence, 1*luck
		;	less the armorEnc
		(= ret
			(- (/ (+ (* agil 5) intell intell luck) 8) armorEnc)
		)
		;if we're tired, then reduce the defense by 5
		(if (<= stamina 30)
			(= ret (- ret 5))
			;if we're *really* tired, then reduce by another 10.
			(if (<= stamina 10)
				(= ret (- ret 10))
			)
		)
		;take off another 10 per day without sleep
		(= ret (- ret (* daysWithoutSleep 10)))
		
		(= parryBonus (= dodgeBonus 0))
		(if parry (= parryBonus (+ parry shieldValue (/ agil 10))))
		(if dodge (= dodgeBonus (+ dodge (/ agil 5))))
		(switch action
			(1 (= ret (- ret 5)))			;thrust
			(3 (= ret (+ ret parryBonus)))	;parry left?
			(4 (= ret (+ ret parryBonus)))	;parry right?
			(5 (= ret (+ ret dodgeBonus)))	;dodge left?
			(6 (= ret (+ ret dodgeBonus)))	;dodge right?
		)
		(return ret)
	)
	
	(method (tryAttack who &tmp temp0)
		; Returns if the attack was successful or not
		;attackChance will always be between 5 and 95%.
		(cond 
			((>
				(= temp0
					(+ (- (self attackLevel:) (who defenseLevel:)) 50)
				)
				95
				)
				(= temp0 95)
			)
			((< temp0 5) 
				(= temp0 5)
			)
		)
		(return (if (>= temp0 (RollDice))
					(return TRUE) 
				else 
					(return FALSE)
				)
		)
	)
	
	(method (doDamage who bonus &tmp ret)
		(= ret
			(+
				(- (+ weapValue (/ strength 10)) (who armorValue?))
				(Random 1 6)
			)
		)
		(if (>= argc 2)
			(= ret (+ ret bonus))
		)
		(if (< ret 0)
			(= ret 0)
		else
			(who getHurt: ret)
		)
		(return ret)
	)
	
	(method (getHurt amount)
		(if (< (= health (- health amount)) 0)
			(= health 0)
		)
		(if (> health (self calcHealth:))
			(= health (self calcHealth:))
		)
		(if (== health 0) 
			(self die:)
		)
	)
	
	(method (getTired amount)
		(if (< (= stamina (- stamina amount)) 0)
			(self getHurt: (/ (- 0 stamina) 2))
			(= stamina 0)
		)
	)
	
	(method (die)
		(opponent canFight: FALSE)
		(self setScript: NULL)
		(= canFight FALSE)
		(= action 10) ;action 10 = die
	)
	
	(method (calcHealth &tmp ret)
		(return (+ (= ret (/ (+ strength vit vit) 3)) ret))
	)
	
	(method (calcStamina)
		(return (* (+ agil vit) 2))
	)
	
	(method (calcMana)
		(return
			(if magic
				(return (/ (+ intell magic magic) 3))
			else
				(return 0)
			)
		)
	)
)

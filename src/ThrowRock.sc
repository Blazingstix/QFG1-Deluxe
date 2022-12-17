;;; Sierra Script 1.0 - (do not remove this comment)
(script# 102)
(include system.sh) (include sci2.sh) (include game.sh)
(use Main)
(use _Sound)
(use _Motion)
(use _Actor)
(use _System)

(public
	AnimateThrowingRock 0
)

(local
	whoTarget		;who are we throwing the rock at?
	distX
	distY
	targetX
	targetY			
	isHitTarget		;has the projectile hit its target?
	egoSignal
	egoPriority
	egoIllegalBits
	hitSound		;sound effect we play when we hit the target
)

(procedure (AnimateThrowingRock target &tmp temp0 temp1 rockActor)
	(return
		(if (not (ego has: iRock))
			(HighPrint 102 0)
			;You do not have any rocks.
			(DisposeScript 102)
			(return FALSE)
		else
			(ego use: iRock 1)
			(Load RES_SOUND (GetSongNumber 58))
			(if target
				(FaceObject ego target)
				(= targetX (+ (target x?) (target targDeltaX?)))
				(= targetY (+ (target y?) (target targDeltaY?)))
				(= temp0 (- targetX (ego x?)))
				(= temp1 (- targetY (- (ego y?) 34)))
				(while
					(and
						(< westEdge targetX) (< targetX eastEdge)
						(< 0 targetY) (< targetY southEdge)
					)
					(= targetX (+ targetX temp0))
					(= targetY (+ targetY temp1))
				)
				(if
					(not
						(TrySkill THROW 0 (- 50 (/ (ego distanceTo: target) 5)))
					)
					(if (< targetY 0)
						(= targetY (+ targetY (Random 30 100)))
					else
						(= targetY (- targetY (Random 30 100)))
					)
				)
			)
			(if (not target)
				;CI: why call SkillUsed directly, instead of TrySkill
				(SkillUsed THROW (/ [egoStats AGIL] tryStatThrowing))
				(= targetX (if (& (ego loop?) loopW) -10 else 330))
				(= targetY (Random 20 80))
			)
			(= whoTarget target)
			((= rockActor (Actor new:))
				view: (GetEgoViewNumber vEgoThrowing)
				setLoop: 4
				setCel: 0
				illegalBits: 0
				ignoreActors:
				ignoreHorizon:
				z: 20
				posn: (- (ego x?) 10) (- (ego y?) 12)
				setStep: 20 8
				init:
				hide:
				setPri: 15
				setScript: rockScript NULL target ;set the register of rockScipt to be the target.
			)
			(return TRUE)
		)
	)
)

(instance rockScript of Script
	(properties)
	
	;CI: This is a manual decompilation of the asm, which could not be auto-decompiled by the version of SCICompanion I used.
	(method (doit &tmp tmpHitTarget)
		(if (IsObject whoTarget)
			(= targetX (+ (whoTarget x?) (whoTarget targDeltaX?)))
			(= targetY (+ (whoTarget y?) (whoTarget targDeltaY?)))
		)
		(= distX (- targetX (client x?)))
		(= distY (- targetY (client y?)))

		(cond
			(	(and (== state 1) 
					 (or (= tmpHitTarget ;if we're within 15 pixels of the target, consider it a hit.
							(and (< -15 distX) (< distX 15)
								 (< -15 distY) (< distY 15)
							)
						 )	;And if it's a hit, we advance the script to the next state.
						(not (client mover?))	; or if the projectile has stopped moving.
						(not (and (< 0 (client x?)) (< (client x?) 319)))	;or the projectile is offscreen left or right
						(not (and (< 0 (client y?)) (< (client y?) 189)))	;or the projectile is offscreen top or bottom.
					 )
				)
				(if (not register) ;register is the target object. if there is noobject, then set the HitFlag to FALSE.
					(= tmpHitTarget FALSE)
				)
				(= isHitTarget tmpHitTarget)
				(self cue:)
			)
			(else
				(super doit:)
			)
		)
	)
	
;	CI: original asm has been commented out. The manual decompilation is above.	
;	(method (doit &tmp tmpHitTarget)
;		(asm
;			pushi    1					;push TRUE to immediate
;			lsl      whoTarget				;load whoTarget to local stack
;			callk    IsObject,  2		;call IsObject with 2 bytes, i.e. 1 parameter)
;			bnt      code_020e			;(if (IsObject whoTarget)		;if not true, then skip to the next jump.
;
;										;)
;			pushi    #x
;			pushi    0
;			lal      whoTarget
;			send     4					;(whoTarget x?)
;			push    					;push to stack
;			pushi    #targDeltaX
;			pushi    0
;			lal      whoTarget
;			send     4					;(whoTarget targDeltaX?)
;			add     					;add to stack ... i.e. + together
;			sal      targetX				;put in targetX
;			pushi    #y
;			pushi    0
;			lal      whoTarget
;			send     4
;			push    
;			pushi    #targDeltaY
;			pushi    0
;			lal      whoTarget
;			send     4
;			add     
;			sal      targetY			;store accumulator in local variable
;
;
;code_020e:
;			pushi    #x
;			pushi    0
;			pToa     client
;			send     4
;			push    
;			lal      targetX
;			sub     
;			sal      distX			;(= distX (- targetX (client x?)))
;
;			pushi    #y
;			pushi    0
;			pToa     client			;proprty to accumulator, means we'll use a property?
;			send     4
;			push    
;			lal      targetY
;			sub     
;			sal      distY			;(= distY (- targetY (client y?)))
;
;
;									;start of a (cond here
;			pTos     state
;			ldi      1				;(if (== state 1)
;			eq?     
;			bnt      code_0296		;skip to else
;			pushi    65521			;-15
;			lal      distX
;			lt?     
;			bnt      code_024b		;skip the rest of the or
;			pprev   
;			ldi      15
;			lt?     
;			bnt      code_024b		;skip the rest of the or
;			pushi    65521			;-15
;			lal      distY
;			lt?     
;			bnt      code_024b		;skip the rest of the or
;			pprev   
;			ldi      15
;			lt?     
;
;code_024b:
;			sat      tmpHitTarget
;			bt       code_0285			;if true, jump to register section
;										;	otherwise, if tmpHitTarget = FALSE then set the mover
;			
;			
;			pushi    #mover
;			pushi    0
;			pToa     client				;(if (client mover?) 
;			send     4
;			not     
;			bt       code_0285			;is mover? is NULL then jump to code below.
;
;
;			pushi    0
;
;			pushi    #x
;			pushi    0
;			pToa     client				;(if (and (< 0 (client x?)) (< (client x?) 319))
;			send     4
;			lt?     
;			bnt      code_026c			
;			
;			pprev   
;			ldi      319
;			lt?     
;code_026c:
;			not     
;			bt       code_0285
;			pushi    0
;
;			pushi    #y
;			pushi    0
;			pToa     client			;(and (< tmpHitTarget (client y?)) (< tmpHitTarget 189))
;			send     4
;			lt?     
;			bnt      code_0281
;			pprev   
;			ldi      189
;			lt?     
;code_0281:
;			not     
;			bnt      code_0296
;
;
;code_0285:		;set the register
;			pToa     register
;			bnt      code_028c		;(if register = 0 then
;			lat      tmpHitTarget			;(= register tmpHitTarget)
;code_028c:
;			sal      isHitTarget	;(= isHitTarget register)
;			pushi    #cue
;			pushi    0
;			self     4				;(self cue:)
;			jmp      code_029c
;
;
;code_0296:		;else condition
;			pushi    #doit
;			pushi    0
;			super    Script,  4		;(super doit:)
;code_029c:
;			ret     
;		)
;	)
	
	(method (dispose)
		(hitSound dispose:)
		(HandsOn)
		(if (and isHitTarget (IsObject register))
			(register getHurt: TRUE)
		)
		(super dispose:)
		(DisposeScript 102)
	)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(= egoSignal (ego signal?))
				(= egoPriority (ego priority?))
				(= egoIllegalBits (ego illegalBits?))
				(HandsOff)
				(ego
					view: (GetEgoViewNumber vEgoThrowing)
					cycleSpeed: 1
					setLoop: (if (& (ego loop?) loopW) 2 else 3)
					cel: 0
					setCycle: CycleTo 3 cdFORWARD self
				)
				((= hitSound (Sound new:))
					number: (GetSongNumber 58)
					priority: 15
					init:
				)
			)
			(1
				(ego setCycle: EndLoop)
				(client
					x: (if (== (ego loop?) loopS)
						(- (ego x?) 10)
					else
						(+ (ego x?) 10)
					)
					show:
					setCycle: Forward
					setMotion: MoveTo targetX targetY
				)
			)
			(2
				(client hide:)
				(if isHitTarget
					(hitSound play: self)
				else
					(= cycles 1)
				)
			)
			(3
				(StopEgo)
				(ego
					priority: egoPriority
					illegalBits: egoIllegalBits
					signal: egoSignal
				)
				(client dispose:)
			)
		)
	)
)

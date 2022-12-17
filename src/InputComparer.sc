;;; Sierra Script 1.0 - (do not remove this comment)
(script# INPUTCOMP) ;INPUTCOMP = 802
(include system.sh) (include sci2.sh) (include game.sh)
(use _User)

(public
	ToLower 0
	StringEquals 1
	EventEqualsString 2
)

(procedure (ToLower param1)
	(return
		(if (or (< param1 65) (> param1 90))
			(return param1)
		else
			(return (+ (- param1 65) 97))
		)
	)
)

(procedure (StringEquals param1 param2 &tmp temp0 temp1 temp2 temp3)
	(= temp1 (StrLen param2))
	(= temp0 (- (= temp0 (StrLen param1)) temp1))
	(while (>= temp0 0)
		(= temp3 0)
		(= temp2 temp0)
		(while (< temp3 temp1)
			(if
				(!=
					(ToLower (StrAt param2 temp3))
					(ToLower (StrAt param1 temp2))
				)
				(break)
			)
			(++ temp3)
			(++ temp2)
		)
		(if (== temp3 temp1) (return (+ temp3 1)))
		(-- temp0)
	)
	(return FALSE)
)

(procedure (EventEqualsString event param2 &tmp i userInputLineAddr)
	(if
		(and
			(User canInput?)
			(not (event claimed?))
			(== (event type?) keyDown)
			(or
				(== (event message?) (User echo?))
				(and
					(<= 32 (event message?))
					(<= (event message?) 127)
				)
			)
		)
		(event claimed: TRUE)
		(if (User getInput: event)
			(= userInputLineAddr (User inputLineAddr?))
			(= i 1)
			(while (< i argc)
				(if
				(StringEquals userInputLineAddr [param2 (- i 1)])
					(return i)
				)
				(++ i)
			)
			(event type: speechEvent)
			(event claimed: FALSE)
			(Parse userInputLineAddr event)
			(User said: event)
		)
	)
	(return FALSE)
)

;;; Sierra Script 1.0 - (do not remove this comment)
(script# FOREST) ;FOREST = 804
(include system.sh) (include sci2.sh) (include game.sh)
(use Main)
(use _LoadMany)
(use _Game)

(public
	Forest 0
)

(local
	atClearing
)
(instance Forest of Locale
	(properties)
	
	(method (init &tmp curRoomPicture temp1)
		(super init: &rest)
		(Load RES_VIEW (GetEgoViewNumber vEgoThrowing))
		(LoadMany RES_SCRIPT 103 102)
		(if
			(and
				(>= (= curRoomPicture (curRoom picture?)) 704)
				(<= curRoomPicture 707)
			)
			(= atClearing TRUE)
		)
		(= temp1 (if isNightTime 32 else 25))
		(if
			(or
				(== (cSound state?) 0)
				(!= (cSound number?) temp1)
			)
			(cSound stop: number: temp1 loop: -1 priority: 0 play:)
		)
		(if
			(and
				(<= detailLevel DETAIL_MID)
				(>= curRoomPicture 700)
				(<= curRoomPicture 707)
			)
			(= highSpeedHero TRUE)
			(ChangeGait MOVE_NOCHANGE 0)
		)
	)
	
	(method (dispose)
		(= highSpeedHero FALSE)
		(super dispose: &rest)
	)
	
	(method (handleEvent event)
		(if (event claimed?) (return))
		(switch (event type?)
			(evSAID
				(cond 
					((super handleEvent: event))
					((Said 'look>')
						(cond 
							(
							(Said '[<at,around][/!*,forest,greenery,clearing]') (if atClearing (HighPrint 804 0) else (HighPrint 804 1)))
							((Said '[<down][/ground,needle,moss,grass]') (if atClearing (HighPrint 804 2) else (HighPrint 804 3)))
							((Said '[<up][/sky,cloud,star,moon]')
								(if isNightTime
									(HighPrint 804 4)
								else
									(HighPrint 804 5)
								)
							)
							((Said '/birch,tree') (HighPrint 804 6))
							((Said '/bush') (HighPrint 804 7))
							((Said '/boulder') (if atClearing (HighPrint 804 8) else (HighPrint 804 9)))
							((Said '/hill,cliff,peak,pass') (if atClearing (HighPrint 804 8) else (HighPrint 804 10)))
							((Said '/cave') (HighPrint 804 11))
						)
					)
					((Said 'climb') (HighPrint 804 12))
					(
						(or
							(Said 'get/boulder,brick')
							(Said 'lockpick<up,boulder,brick')
						)
						(ego setScript: (ScriptID 103 0))
					)
					((Said 'kiss/tree') (HighPrint 804 13))
				)
			)
		)
	)
)

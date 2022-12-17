;;; Sierra Script 1.0 - (do not remove this comment)
(script# STREET) ;STREET = 811
(include system.sh) (include sci2.sh) (include game.sh)
(use Main)
(use Sleep)
(use _Motion)
(use _Game)
(use _System)

(public
	Street 0
)

(local
	timesPickedLock
	timesClimbedWall
)
(instance Street of Locale
	(properties)
	
	(method (init)
		(if
			(and
				(not isNightTime)
				(or (!= (cSound number?) 93) (== (cSound state?) 0))
			)
			(cSound priority: 0 number: 93 loop: -1 play:)
		)
		(super init: &rest)
	)
	
	(method (handleEvent event &tmp [temp0 3] nextRoom temp4 thisControl)
		(cond 
			((super handleEvent: event))
			((!= (event type?) evSAID))
			((or	(Said 'nap')
					(Said 'go[<to]/nap')
					(Said 'get,get/nap')
				)
				(cond 
					((and (< 700 currentTime) (< currentTime 2550))
						(HighPrint 811 0)
						;You're too awake to sleep now.
					)
					((!= (ego onControl: origin) cBLACK)
						(HighPrint 811 1)
						;This doesn't look like a good place to curl up.
					)
					((== curRoomNum 334)
						;alleyway at night
						(EgoSleeps 5 0)
					)
					(else
						(ego setScript: egoSleepsInStreet)
					)
				)
			)
			((Said 'look/door')
				(HighPrint 811 2)
				;Yep, that's a door, all right.
			)
			((Said '/window') 
				(HighPrint 811 3)
				;The shades are drawn.
			)
			((Said 'open/window') 
				(HighPrint 811 4)
				;The windows appear to be locked.  You certainly don't want to break them and attract attention.
			)
			((Said 'unlock,lockpick/door,hasp')
				(= thisControl (ego onControl: origin))
				(cond 
					((not isNightTime)
						(HighPrint 811 5)
						;In broad daylight?  You've got to be kidding!  You'd be arrested for certain.
					)
					((not [egoStats PICK]) 
						(HighPrint 811 6)
						;You don't know how to pick locks.
					)
					((not (CanPickLocks)) 
						(HighPrint 811 7)
						;You can't pick a lock without a lockpick.
					)
					((== thisControl cYELLOW) 
						(curRoom notify: 1)
					)
					((or (== thisControl cLRED) (== thisControl cLGREEN) (== thisControl cLMAGENTA))
						(if (<= (Random 3 10) (++ timesPickedLock))
							(EgoDead DIE_RETRY DIE_ARRESTED 811 8
								#title { Nobody said this job was easy._}
								#icon (GetEgoViewNumber vEgoDefeated) 1 0
							)
							;Uh oh.  You made too much noise.  The Sheriff and Otto arrive on the scene and arrest you for "criminal carelessness".  Work a little faster next time!
						else
							(TrySkill PICK 0 0)
							(HighPrint 811 9)
							;The door is securely barred from the inside, but you do get some practice.
						)
					)
					(else
						(HighPrint 811 10)
						;You're not near a door.
					)
				)
			)
			((Said 'climb')
				(cond 
					((not [egoStats CLIMB])
						(HighPrint 811 11)
						;Leave the climbing to those who know how.
					)
					((and (not isNightTime) (!= curRoomNum 333))
						;if daytime, and not in the Alley.
						(HighPrint 811 12)
						;The town gate's open.  There's no need to climb over.  Besides, you'd make people nervous.
					)
					((<= (Random 3 10) (++ timesClimbedWall))
						(HighPrint 811 13)
						;You'd better not try to climb the wall any more right now.  You've been at this long enough that someone is likely to notice.
						(= timesClimbedWall 10)
					)
					((TrySkill CLIMB tryClimbIntoTown 0)
						(switch curRoomNum
							(300
								(= nextRoom (if (> (ego x?) 130) 65 else 73))
							)
							(310 (= nextRoom 73))
							(320 (= nextRoom 65))
							(333 (= nextRoom 53))
							(334 (= nextRoom 53))
							(else 
								(HighPrint 811 14)
								;There's no good place to climb the wall here.
								(= nextRoom 0)
							)
						)
						(if nextRoom 
							(HighPrint 811 15)
							;You climb over the town wall.
							(curRoom newRoom: nextRoom)
						)
					)
					(else 
						(HighPrint 811 16)
						;The wall is too smooth to climb with your level of skill, but keep practicing.
					)
				)
			)
			((Said 'look>')
				(cond 
					((or (Said '<down') (Said '/ground'))
						(HighPrint 811 17)
						;There is an absence of the usual litter associated with town streets. This hamlet is kept very clean.
					)
					((or (Said '/sky') (Said '<up'))
						(if isNightTime
							(HighPrint 811 18)
							;The evening is clear and the stars are bright. Dark clouds pass over the moon.
						else
							(HighPrint 811 19)
							;The sky is a piercing blue with scudding white clouds.
						)
					)
					((Said '/wall')
						(HighPrint 811 20)
						;The town wall is made of the same rock as the surrounding mountains.It appears to be very sturdy.
					)
					((Said '/roof')
						(HighPrint 811 21)
						;It keeps the occupants of the building dry when it rains.
					)
					((Said '/hill')
						(HighPrint 811 22)
						;If you look past the town wall, you can see the mountains through which you journeyed to arrive here.
					)
				)
			)
		)
	)
)

(instance egoSleepsInStreet of Script
	(properties)
	
	(method (changeState newState &tmp nextRoom)
		(switch (= state newState)
			(0
				(HandsOff)
				(HighPrint 811 23)
				;You curl up on the hard ground and try to get some rest.
				(ego view: (GetEgoViewNumber vEgoTired) setLoop: 3 cel: 0 setCycle: EndLoop self)
			)
			(1
				(EgoSleeps 5 0)
				(= seconds 5)
			)
			(2
				(= nextRoom curRoomNum)
				;set the currentRoom to a far off number (there's no actual room 999, so no chance we're already on it)
				(= curRoomNum 999) 
				(curRoom newRoom: nextRoom)
			)
		)
	)
)

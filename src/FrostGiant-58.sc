;;; Sierra Script 1.0 - (do not remove this comment)
(script# 58)
(include system.sh) (include sci2.sh) (include game.sh)
(use Main)
(use _Interface)
(use _LoadMany)
(use _Sound)
(use _Save)
(use _Motion)
(use _Game)
(use _Inventory)
(use _Actor)
(use _System)

(public
	rm58 0
)

(local
	giantBored
	throwProjectile
	giantLeft
)
(procedure (GiantSays)
	(Print &rest #width 305 #mode teJustLeft #window giantWin)
)

(procedure (GiantIntro)
	(GiantSays 58 0)
	;"Brauggi I be, to boast of my boldness,
	;Strong as the storm that sends forth the snow.
	;Fiercer in fighting than foes in their fury,
	;Fear now this Frost Giant fighter and flee!"
)

(procedure (GiantWantsFruit)
	(GiantSays 58 1)
	;"Find me some fruit for to mellow my mead horn,
	;Gift I will give of a gem that now glows --
	;Jewel from Jotunheim, flare of the frost flame,
	;Fetch to me fruit that will fill up my fists!"
	(CenterPrint 58 2)
	;(It looks like it will take a lot of fruit to fill up THOSE fists!)
)

(instance giantWin of SysWindow
	(properties
		color 1
	)
	
	(method (open &tmp temp0)
		(= top (+ top (= temp0 (- 188 bottom))))
		(= bottom (+ bottom temp0))
		(super open:)
	)
)

(instance frostStart of Prop
	(properties
		view vFrostGiant
		loop 5
	)
)

(instance frostKill of Prop
	(properties
		view vFrostGiant
		loop 6
	)
)

(instance frostSound of Sound
	(properties
		number 14
		priority 2
	)
)

(instance giant of Actor
	(properties
		y 82
		x 32
		yStep 3
		view vFrostGiant
		cycleSpeed 1
		illegalBits $0000
		xStep 5
		moveSpeed 1
	)
	
	(method (handleEvent event &tmp spell offering)
		(cond 
			((super handleEvent: event))
			(giantLeft TRUE)
			(
				(or
					(MouseClaimed self event emRIGHT_BUTTON)
					(Said 'look/giant,brauggi,man')
				)
				(CenterPrint 58 3)
				;From the blue color of his skin and his huge height, you can tell this is a Frost Giant.
			)
			((== (event type?) evSAID)
				(cond 
					((Said 'chop,beat,fight,kill,damage')
						(self setScript: giantFights)
					)
					((Said 'cast>')
						(switch (= spell (SaidCast event))
							(DETMAGIC
								(if (CastSpell spell)
									(CenterPrint 58 4)
									;The Giant (who does not seem pleased at your casting a spell in his presence) radiates a magical aura.
									;The strongest magic centers on the glowing gem at his belt.
									)
							)
							(TRIGGER
								(giant setScript: giantMagic)
							)
							(DAZZLE
								(giant setScript: giantMagic)
							)
							(ZAP
								(giant setScript: giantMagic)
							)
							(CALM
								(giant setScript: giantMagic)
							)
							(FLAMEDART
								(giant setScript: giantMagic)
							)
							(else  (event claimed: FALSE))
						)
					)
					((Said 'gave,bargain>')
						(cond 
							((Said 'gave,bargain[/!*]')
								(GiantWantsFruit)
							)
							((not (= offering (SaidGet event)))
								(HighPrint 58 5)
								;You can't offer what you don't have.
							)
							((== offering iFruit)
								(ego setScript: giveFruit)
							)
							(else
								(event claimed: TRUE)
								(CenterPrint 58 6)
								;The Giant looks at you in disgust. Apparently, you did not offer what he desires.
							)
						)
					)
					((Said 'made/bargain,deal') (GiantWantsFruit))
					((Said 'bargain,deal') (GiantWantsFruit))
					((Said 'chat') (GiantIntro))
					((Said 'throw[/boulder,dagger]')
						(= throwProjectile TRUE)
						(giant setScript: giantMagic)
					)
					((Said 'ask>')
						(cond 
							((Said '//man,fighter,giant,brauggi,ya')
								(GiantIntro)
							)
							((Said '//home,north,jotunheim,(field[<frost,about]),jotunheim')
								(GiantSays 58 7)
								;"Near now is North, for the winter has wandered,
								;Bringing this brave one to barter so bold.
								;I journey from Jotunheim, home of the giants,
								;Source of all snowstorms, the sender of sleet."
							)
							((Said '//bargain,deal')
								(GiantWantsFruit)
							)
							((Said '//fight,ax,weapon,blade,(attack[<blade,about])')
								(self setScript: Challenge)
							)
							((Said '//hunger')
								(GiantSays 58 8)
								;"Hollow my food house,
								;for my hunt has been hindered,
								;By having to hurry here from my home."
							)
							((Said '//food,apple,apple,beet')
								(GiantSays 58 9)
								;"Highpoint of harvest, the finest of flavor,
								;Apples or oranges, peaches or pears."
							)
							((Said '//mead,horn')
								(GiantSays 58 10)
								;"Mead is the mother's milk, mighty yet mellow,
								;That brings joy to Giants and madness to men."
							)
							((Said '//gem,(flame[<frost,about])')
								(GiantSays 58 11)
								;"Glow of the frost flame that fills up the night field,
								;A jewel that is flawless, the finest of gems."
							)
							(else
								(event claimed: TRUE)
								(switch (++ giantBored)
									(1 
										(GiantSays 58 12)
										;"Bored I become when the banter so blindly
										;Speaks of the subjects I care not about."
									)
									(2
										(GiantSays 58 13)
										;"Annoy me not over matters so minor."
									)
									(3
										(GiantSays 58 14)
										;"Waste not my wits, for I weary of words;
										;My axe it is asking for action and more."
										(= giantBored 0)
										(giant setScript: giantExits)
									)
								)
							)
						)
					)
				)
			)
		)
	)
)

(instance rm58 of Room
	(properties
		picture 59
		style DISSOLVE
		west 57
	)
	
	(method (init)
		(Load RES_TEXT 58)
		(LoadMany RES_SOUND 47 (GetSongNumber 9))
		(LoadMany RES_VIEW (GetEgoViewNumber vEgoRunning) (GetEgoViewNumber vEgoClimbing) (GetEgoViewNumber vEgoDefeated))
		(= giantLeft (Btst OBTAINED_GEM))
		(if (not giantLeft)
			(LoadMany RES_VIEW vFrostGiant (GetEgoViewNumber vEgoBeginFight) (GetEgoViewNumber vEgoDaggerDefeated) (GetEgoViewNumber vEgoTired))
			(LoadMany RES_SOUND 48
				(GetSongNumber 59)
				(GetSongNumber 65)
				(GetSongNumber 14)
			)
		)
		(super init:)
		(cSound fade:)
		(StatusLine enable:)
		(StopEgo)
		(ChangeGait MOVE_WALK FALSE)
		(if giantLeft
			(cSound number: 47)
			(ego posn: 1 140 init: setMotion: MoveTo 35 140)
		else
			(cSound number: 48)
			(frostSound number: (GetSongNumber 14) init:)
			(ego posn: 0 135 init: setScript: egoInit)
			(if (< colourCount 8)
				(giantWin color: vBLACK back: vWHITE)
			)
			(giant
				setPri: 4
				setLoop: 0
				setCycle: Walk
				init:
				setScript: giantInit
			)
		)
		(cSound loop: 1 play: self)
	)
	
	(method (dispose)
		(Bset VISITED_FROST_CAVE)
		(super dispose:)
	)
	
	(method (handleEvent event)
		(cond 
			((super handleEvent: event))
			((== (event type?) evSAID)
				(cond 
					((Said 'run')
						(ego setScript: egoRuns)
					)
					((Said 'look>')
						(cond 
							((Said '[<at,around][/!*,hill,ice,clearing,north,east,south]')
								(HighPrint 58 15)
								;The mountains rise quickly from this point and are covered with snow.
							)
							((Said '/west,forest')
								(HighPrint 58 16)
								;The snow has melted in most of the forest, but here it is still heavy.
							)
							((Said '/gem')
								(if (or (not giantLeft) (ego has: iMagicGem))
									(HighPrint 58 17)
									;The gem glows like an aurora in the night sky.
								else
									(HighPrint 58 18)
									;You don't see a gem here.
								)
							)
							((Said '/giant,brauggi,man')
								(if (Btst OBTAINED_GEM)
									(HighPrint 58 19)
									;Brauggi has departed on the long journey to his homeland.
								else
									(HighPrint 58 20)
									;Brauggi has become impatient with your questions and gone elsewhere.
									;You will have to return another time.
								)
							)
						)
					)
				)
			)
		)
	)
	
	(method (cue)
		(cSound number: 47 loop: -1 play:)
	)
)

(instance egoInit of Script
	(properties)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(HandsOff)
				(client setMotion: MoveTo 130 125 self)
			)
			(1
				(FaceObject client giant)
				(client setScript: NULL)
			)
		)
	)
)

(instance giantInit of Script
	(properties)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(client setMotion: MoveTo 106 82 self)
			)
			(1
				(client setPri: RELEASE setMotion: MoveTo 125 93 self)
			)
			(2 (client setCycle: EndLoop self))
			(3
				(frostSound number: (GetSongNumber 59) play:)
				(client
					setLoop: 2
					cel: 2
					cycleSpeed: 0
					setCycle: CycleTo 0 cdFORWARD self
				)
			)
			(4
				(if (== detailLevel DETAIL_LOW)
					(self cue:)
				else
					(frostSound play:)
					(giant setCycle: CycleTo 0 cdFORWARD self)
				)
			)
			(5
				(HandsOn)
				(if (not (Btst VISITED_FROST_CAVE))
					(GiantSays 58 21)
					;"Far from the frost field, fares forth this fighter,
					;Hunger has hurled me hither from home.
					;My name it is known in the Northlands as Brauggi,
					;Barter with blade's clash, or bargain with me."
					)
				(client setScript: giantBlocks)
			)
		)
	)
)

(instance giantExits of Script
	(properties)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(HandsOff)
				(giant
					setLoop: 4
					setCel: RELEASE
					cel: 0
					setCycle: Forward
					moveSpeed: 1
					cycleSpeed: 1
					illegalBits: 0
					setMotion: MoveTo 106 82 self
				)
			)
			(1
				(giant setMotion: MoveTo -20 75 self)
			)
			(2
				(cSound number: 47 play:)
				(= giantLeft TRUE)
				(HandsOn)
				(giant dispose:)
			)
		)
	)
)

(instance doneDeal of Script
	(properties)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(HandsOff)
				(ego get: iMagicGem 1)
				(GiantSays 58 22)
				;Fruit you have found, to fill all my food stores,
				;Thus filled the bargain, my gem you have bought.
				;Brauggi has bartered, and all has been answered;
				;The mead it may mellow, and now I head home.
				(SolvePuzzle POINTS_GETGLOWINGGEM 8)
				(= seconds 2)
			)
			(1
				(Bset OBTAINED_GEM)
				(giant setScript: giantExits)
			)
		)
	)
)

(instance ShowOff of Script
	(properties)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(frostSound number: (GetSongNumber 59) play:)
				(giant cycleSpeed: 0 setLoop: 2 cel: 0 setCycle: EndLoop self)
			)
			(1
				(if (== detailLevel DETAIL_LOW)
					(self cue:)
				else
					(giant setCycle: EndLoop self)
				)
			)
			(2
				(frostSound stop:)
				(client setScript: 0)
			)
		)
	)
)

(instance Challenge of Script
	(properties)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(self setScript: ShowOff self)
			)
			(1
				(GiantSays 58 23)
				;"Fiercer than frost's bite, my fury is fearless,
				;My axe it is edged as is the ice.
				;This warrior is willing to wield now the war drum,
				;The battleaxe blazes and bellows for blood."
				(client setScript: giantBlocks)
			)
		)
	)
)

(instance WalkToGiant of Script
	(properties)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(HandsOff)
				(ego ignoreActors: 1 illegalBits: 0)
				(if (< (ego y?) (+ (giant y?) 10))
					(ego setMotion: MoveTo (ego x?) (+ (giant y?) 10) self)
				else
					(self cue:)
				)
			)
			(1
				(ego
					setMotion: MoveTo (+ (giant x?) 5) (+ (giant y?) 10) self
				)
			)
			(2
				(ego ignoreActors: 0 illegalBits: cWHITE)
				(HandsOn)
				(self dispose:)
			)
		)
	)
)

(instance giantFights of Script
	(properties)
	
	(method (changeState newState &tmp temp0 egoX_2 tmpEgoX tmpEgoY temp4 temp5 temp6 temp7)
		(switch (= state newState)
			(0
				(ego setScript: WalkToGiant self)
			)
			(1
				(HandsOff)
				(= temp0 (if (ego has: 9) 0 else 2))
				(ego
					illegalBits: 0
					ignoreActors:
					view: (GetEgoViewNumber vEgoBeginFight)
					setLoop: temp0
					cel: 0
					posn: (+ (giant x?) 5) (+ (giant y?) 2)
					setCycle: EndLoop
				)
				(= cycles 4)
			)
			(2
				(frostSound number: (GetSongNumber 59) play:)
				(giant
					setLoop: 3
					cel: 0
					cycleSpeed: 1
					setCycle: CycleTo 5 cdFORWARD self
				)
			)
			(3
				(giant setCycle: EndLoop self)
				(ego
					view: (GetEgoViewNumber vEgoDaggerDefeated)
					posn: (+ (ego x?) 20) (ego y?)
					setLoop: 0
					setCel: RELEASE
					cel: 5
					cycleSpeed: 2
					moveSpeed: 2
					illegalBits: 0
				)
				(= tmpEgoX (ego x?))
				(= tmpEgoY (ego y?))
				(= temp4 260)
				(= temp5 140)
				(= temp6 (/ (- temp4 tmpEgoX) (- (ego lastCel:) 5)))
				(= temp7 (/ (- temp5 tmpEgoY) (- (ego lastCel:) 5)))
				(ego
					xStep: temp6
					yStep: temp7
					setCycle: EndLoop
					setMotion: MoveTo temp4 temp5
				)
			)
			(4 (giant setCycle: BegLoop self))
			(5
				(frostSound stop:)
				(if (or (Btst WHACKED_BY_GIANT) (not (TakeDamage 20)))
					(EgoDead DIE_RETRY DIE_FROST_GIANT_FIGHT 58 24
						#title {The bigger they come, the harder they hit}
						#icon vFrostGiant 3 3
						;Next time, pick on someone your own size.
					)
					(StopEgo)
					(Bclr WHACKED_BY_GIANT)
					(ego posn: 130 125 loop: loopN setHeroRetry:)
					(client posn: 125 93 setScript: giantBlocks) ;note, client is 'giant'
					(HandsOn)
				else
					(Bset WHACKED_BY_GIANT)
					(self cue:)
				)
			)
			(6
				(if (> (= egoX_2 (ego x?)) 237) (= egoX_2 237))
				(giant
					setLoop: 1
					setCel: RELEASE
					cel: 0
					setCycle: Walk
					moveSpeed: 1
					cycleSpeed: 1
					setMotion: MoveTo egoX_2 (giant y?)
				)
				(= seconds 3)
			)
			(7
				(ego view: (GetEgoViewNumber vEgoTired) setLoop: 1)
				(ego
					cel: (ego lastCel:)
					cycleSpeed: 1
					setCycle: BegLoop self
				)
			)
			(8
				(StopEgo)
				(ego loop: loopN forceUpd:)
				(self setScript: ShowOff self)
			)
			(9
				(GiantSays 58 25)
				;"Courage you carry, but skill it is scanty;
				;Face not this foe before fiercer you fight."
				(HandsOn)
				(client setScript: giantBlocks)
			)
		)
	)
)

(instance giantBlocks of Script
	(properties)
	
	(method (init)
		(super init: &rest)
		(client setLoop: 1 setCel: 0)
		(= register 4)
	)
	
	(method (doit &tmp tmpEgoX clientX clientCel)
		(= tmpEgoX (ego x?))
		(= clientX (client x?))
		(= clientCel (client cel?))
		(cond 
			((and (> clientX 117) (<= tmpEgoX (- clientX 5)))
				(= register 4)
				(if (< (-- clientCel) 0)
					(= clientCel (client lastCel:))
				)
				(client
					setCel: clientCel
					posn: (- clientX 7) (client y?)
				)
			)
			((and (< clientX 231) (>= tmpEgoX (+ clientX 5)))
				(= register 4)
				(if (> (++ clientCel) (client lastCel:))
					(= clientCel 0)
				)
				(client
					setCel: clientCel
					posn: (+ clientX 7) (client y?)
				)
			)
			((not (-- register)) (client setCel: 0) (= register 4))
		)
		(super doit:)
	)
	
	(method (dispose)
		(client setLoop: 2 setCel: 0 setCel: RELEASE)
		(super dispose:)
	)
)

(instance giveFruit of Script
	(properties)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(self setScript: WalkToGiant self)
			)
			(1
				(if (ego has: iFruit)
					(= applesGiven (+ applesGiven (ego use: iFruit 50)))
					(cond 
						((>= applesGiven 50)
							(giant setScript: doneDeal)
						)
						((>= applesGiven 40)
							(CenterPrint 58 26)
							;Your fruit definitely makes a generous pile in the Giant's cupped hands, but they're not full yet.
							)
						((== applesGiven 0)
							(HighPrint 58 27)
							;"Oops!"  you say.  "I guess I don't have any fruit for you.  Sorry!"
							)
						(else
							(CenterPrint 58 28)
							;Your meagre selection of fruit makes a paltry pile at the bottom of the Giant's cupped hands.
							;You'll need to get quite a few more apples to fill THOSE hands!
							)
					)
				else
					(HighPrint 58 27)
					;"Oops!"  you say.  "I guess I don't have any fruit for you.  Sorry!"
				)
				(self dispose:)
			)
		)
	)
)

(instance giantMagic of Script
	(properties)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(HandsOff)
				(if throwProjectile
					(TimedPrint 4 58 29)
					;As you start to throw...
				else
					(TimedPrint 4 58 30)
					;As you begin to cast your spell...
				)
				(= seconds 2)
			)
			(1
				(giant setLoop: 2 setCel: 0)
				(= cycles 2)
			)
			(2
				(giant setCel: 1)
				(= cycles 2)
			)
			(3
				(giant stopUpd:)
				(frostStart
					ignoreActors: 1
					posn: (giant x?) (- (giant y?) 26)
					setPri: (giant priority?)
					z: 1
					init:
					setCycle: Forward
				)
				(= cycles 8)
			)
			(4
				(frostKill
					ignoreActors: 1
					posn: (ego x?) (- (ego y?) 25)
					setPri: (ego priority?)
					z: 2
					init:
					cycleSpeed: 2
					setCycle: EndLoop self
				)
			)
			(5
				(frostSound number: (GetSongNumber 65) play: self)
			)
			(6
				(frostSound number: (GetSongNumber 14) play:)
				(frostStart setCycle: 0 stopUpd: hide:)
				(ego
					view: (GetEgoViewNumber vEgoDaggerDefeated)
					posn: (+ (ego x?) 20) (ego y?)
					setLoop: 0
				)
				(ego setCel: (ego lastCel:))
				(frostKill
					setPri: (ego priority?)
					posn: (ego x?) (- (ego y?) 9)
					setCycle: Forward
				)
				(= seconds 5)
			)
			(7
				(frostKill hide:)
				(EgoDead DIE_RETRY DIE_FROST_GIANT_MAGIC 58 31
					#title {Just because he's big doesn't mean he's stupid!_}
					#icon vFrostGiant 0 0
					;"In matters of magic, I am your master; Seek not to slay me with sendings so small."
				)
				(StopEgo)
				(ego posn: 130 125 loop: loopN setHeroRetry:)
				(client posn: 125 93 setPri: 1 setScript: giantBlocks) ;note, client is 'giant'
				;CI: NOTE: there is a bug where the text now shows under the troll's view.
				(HandsOn)
				(frostKill show: dispose:)
				(frostStart show: dispose:)
			)
		)
	)
)

(instance egoRuns of Script
	(properties)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(HandsOff)
				(ego
					illegalBits: 0
					ignoreActors:
					view: (GetEgoViewNumber vEgoRunning)
					setStep: 6 4
					setPri: (+ (giant priority?) 2)
					setCycle: Walk
				)
				(if (< (ego x?) 170)
					(ego setMotion: MoveTo (+ (ego x?) 30) (ego y?) self)
				else
					(ego setMotion: MoveTo (- (ego x?) 30) (ego y?) self)
				)
			)
			(1
				(frostSound number: (GetSongNumber 9) play:)
				(ego
					view: (GetEgoViewNumber vEgoClimbing)
					setLoop: 2
					setCel: RELEASE
					cel: 0
					posn: (- (ego x?) 4) (- (ego y?) 15)
					moveSpeed: 2
					cycleSpeed: 2
					setCycle: EndLoop self
				)
			)
			(2
				(frostSound stop:)
				(= cycles 8)
			)
			(3
				(if (> (ego y?) 148) (ego posn: (ego x?) 148))
				(ego
					view: (GetEgoViewNumber vEgoDefeated)
					setLoop: 4
					setPri: RELEASE
					cel: 0
					posn: (+ (ego x?) 9) (+ (ego y?) 36)
					illegalBits: cWHITE
					setCycle: EndLoop self
				)
			)
			(4 (= seconds 2))
			(5
				(TakeDamage 2)
				(HandsOn)
				(StopEgo)
				(HighPrint 58 32)
				;Running on the ice is apparently not a very good idea.
				(self dispose:)
			)
		)
	)
)

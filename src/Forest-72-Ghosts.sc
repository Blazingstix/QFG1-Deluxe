;;; Sierra Script 1.0 - (do not remove this comment)
(script# 72)
(include system.sh) (include sci2.sh) (include game.sh)
(use Main)
(use _Motion)
(use _Game)

(public
	rm72 0
)

(instance rm72 of Room
	(properties
		picture 700
		style DISSOLVE
		horizon 90
		north 64
		east 73
		south 79
		west 71
	)
	
	(method (init)
		;we're resetting a failed attempt at the target range, 
		;so let's change the redraw style to match our fav style.
		(if (Btst DIE_RETRY_INPROGRESS)
			(self style: RETRY_STYLE)
		)
		
		(if isNightTime (Load RES_SCRIPT GHOSTS))
		(super init: &rest)
		(StatusLine enable:)
		(self setLocales: FOREST)
		(StopEgo)
		(ego init:)
		(if isNightTime
			(switch (Random 1 5)
				(1
					((ScriptID GHOSTS 5) init: setScript: (ScriptID GHOSTS 0))
				)
				(2
					((ScriptID GHOSTS 6) init: setScript: (ScriptID GHOSTS 1))
				)
				(3
					((ScriptID GHOSTS 7) init: setScript: (ScriptID GHOSTS 4))
				)
				(4
					((ScriptID GHOSTS 5) init: setScript: (ScriptID GHOSTS 0))
				)
				(5
					((ScriptID GHOSTS 7) init: setScript: (ScriptID GHOSTS 3))
				)
			)
		)
		(switch prevRoomNum
			(64 ;graveyard
				(if (Btst DIE_RETRY_INPROGRESS)
					;killed in the graveyard, by ghosts
					(ego posn: 140 110 loop: loopN)
					(Bclr DIE_RETRY_INPROGRESS)
				else
					(ego posn: 140 92 setMotion: MoveTo 140 190)
				)
			)
			(71
				(ego posn: 1 140 setMotion: MoveTo 320 140)
			)
			(79
				(ego posn: 160 188 setMotion: MoveTo 160 0)
			)
			(73
				(if (Btst DIE_RETRY_INPROGRESS)
					;killed by Bruno leaving the target range
					(ego posn: 250 140 loop: loopE)
					(Bclr DIE_RETRY_INPROGRESS)
				else
					(ego posn: 318 140 setMotion: MoveTo 0 140)
				)
			)
		)
		(self setRegions: CEMETERY)
	)
	
	(method (dispose)
		(Bset VISITED_FOREST_72)
		(super dispose:)
	)
)

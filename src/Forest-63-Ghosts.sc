;;; Sierra Script 1.0 - (do not remove this comment)
(script# 63)
(include system.sh) (include sci2.sh) (include game.sh)
(use Main)
(use _Motion)
(use _Game)

(public
	rm63 0
)

(instance rm63 of Room
	(properties
		picture 701
		style DISSOLVE
		horizon 90
		north 51
		east 64
		south 71
		west 62
	)
	
	(method (init)
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
			(51
				(ego posn: 180 92 setMotion: MoveTo 180 190)
			)
			(62
				(ego posn: 1 140 setMotion: MoveTo 320 140)
			)
			(64 ;graveyard
				(if (Btst DIE_RETRY_INPROGRESS)
					;we died in the graveyard
					(Bclr DIE_RETRY_INPROGRESS)
					(ego posn: 280 140 loop: loopE)
				else
					(ego posn: 318 140 setMotion: MoveTo 0 140)
				)
			)
			(71
				(ego posn: 160 188 setMotion: MoveTo 160 0)
			)
		)
		(self setRegions: CEMETERY)
	)
	
	(method (dispose)
		(Bset VISITED_FOREST_63)
		(super dispose:)
	)
)

;;; Sierra Script 1.0 - (do not remove this comment)
(script# 52)
(include system.sh) (include sci2.sh) (include game.sh)
(use Main)
(use _Motion)
(use _Game)

(public
	rm52 0
)

(instance rm52 of Room
	(properties
		picture 700
		style DISSOLVE
		horizon 90
		north 35
		east 53
		south 64
		west 51
	)
	
	(method (init)
		(if isNightTime (Load RES_SCRIPT GHOSTS))
		(super init: &rest)
		(StatusLine enable:)
		(self setLocales: FOREST)
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
		(StopEgo)
		(ego init:)
		(switch prevRoomNum
			(35
				(ego posn: 140 92 setMotion: MoveTo 140 190)
			)
			(51
				(ego posn: 1 140 setMotion: MoveTo 320 140)
			)
			(64 ;graveyard
				(if (Btst DIE_RETRY_INPROGRESS)
					;we died in the graveyard
					(Bclr DIE_RETRY_INPROGRESS)
					(ego posn: 160 160 loop: loopS)
				else
					(ego posn: 160 188 setMotion: MoveTo 160 0)
				)
			)
			(53
				(ego posn: 318 140 setMotion: MoveTo 0 140)
			)
		)
		(self setRegions: CEMETERY)
	)
	
	(method (dispose)
		(Bset VISITED_FOREST_52)
		(super dispose:)
	)
)

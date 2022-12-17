;;; Sierra Script 1.0 - (do not remove this comment)
;*********************************************************************
;*
;* SOUND.SC
;*
;*********************************************************************

(script# MUSIC) ;MUSIC = 989
(include system.sh) (include sci2.sh) (include game.sh)
(use Main)
(use _System)


(class Sound of Object
	(properties
		state 		$0000
		number 		0
		priority 	0		;Do not set directly!
		loop 		1		;Do not set directly!
		handle 		NULL
		signal 		$0000
		prevSignal 	0
		client 		NULL
		owner 		NULL
	)
	
	(method (new who)
		((super new:) owner: (if argc who else NULL) yourself:)
	)
	
	(method (init)
		;Put yourself on the sounds list, and allocate a
		;sound node in heap with InitSound kernel call, if
		;there isn't already a node allocated

		(= state (= signal 0))
		(sounds add: self)
		(DoSound InitSound self)
	)
	
	(method (dispose who)
		;Stop the sound, and remove the sound node from heap.
		
		(if (and argc (not who)) (= client NULL))
		(sounds delete: self)
		(if handle (DoSound KillSound handle) (= handle NULL))
		(super dispose:)
	)
	
	(method (play newVol &tmp argCount)
		;Put this objects sound node on the PlayList with PlaySound
		;kernel call.  If no value was passed in newVol, set the
		;volume to maximum (127), else set the volume to the value
		;in newVol.  If the last parameter was an object, set the
		;client property of the sound to be that object.  If there
		;are any MIDI cues in the sound, the object specified in
		;the client property will receive the cue: messages

		(self stop:)
		(if (not loop) (= loop 1))
		(self init:)
		(= client (if argc newVol else 0))
		(DoSound PlaySound self)
	)
	
	(method (playMaybe)
		(self play: &rest)
		(if (== state 2) (self dispose:))
	)
	
	(method (stop who)
		;Take this objects sound node off the PlayList, and free up its
		;voices and channels for other sounds that may need them
		
		(if (and argc (not who)) (= client NULL))
		(if handle (DoSound StopSound handle))
	)
	
	(method (check)
		;Check for MIDI cues and cue: somebody if needed.
		
		(if signal
			;CI: NOTE, This was missing from EO's restoration.
			(if (== signal -1) (DoSound sndSTOP_ALL))
			(if (IsObject client) (client cue: self))
			(= prevSignal signal)
			(= signal 0)
		)
	)
	
	(method (pause value)
		;If value is non-zero, this sound object will be paused, and any
		;voices or channels it owned will be temporarily freed, until it
		;is unpaused by calling this method with a value of 0.  If the
		;Sound class itself receives this method, ALL sounds will be
		;paused.  Normal pause:s may not be nested, however global pause:s
		;are nested.  Therefore, if you have a sound which has been
		;individually pause:d, and then you globally pause everybody by
		;saying (Sound pause:), and then globally unpause everybody, the
		;original paused state of the individual sound will remain intact
		
		(DoSound PauseSound value)
	)
	
	(method (changeState)
		(DoSound ChangeSndState self)
	)
	
	(method (clean who)
		(if (or (not owner) (== owner who))
			(self dispose:)
		)
	)
	
	(method (fade newVol)
		;Fade sound from current volume to newVol.  Fade may go up or
		;down.  Volume ranges from 0 to 127.

		(if (and argc (not newVol)) (= client 0))
		(DoSound FadeSound handle)
	)
)

;;; Sierra Script 1.0 - (do not remove this comment)
(script# 143)
(include system.sh) (include sci2.sh) (include game.sh)
(use Main)
(use TalkObj)
(use _System)

(public
	introToHenry 0
)

(instance introToHenry of Script
	(properties)
	
	(method (dispose)
		((ScriptID 83 1) caller: NULL)
		(super dispose:)
		(DisposeScript 143)
	)
	
	(method (changeState newState)
		(switch (= state newState)
			(0 (HandsOff) (= cycles 10))
			(1
				((ScriptID 83 1) caller: self)
				(if (Btst VISITED_HENRYINSIDE)
					(= state 4)
					(TalkObjMessages (ScriptID 83 1) 143 0)
					;"Come in, come in.  Good to see you again.   Get's kind o' tiresome sitten here an' thinken by meself all the time."
				else
					(TalkObjMessages (ScriptID 83 1) 143 1)
					;"Ello.  'Ow are you?  'Ave we met before?"
				)
			)
			(2
				(TalkObjMessages (ScriptID 83 1) 143 2)
				;"I'm 'Enry the 'ermit, that's me.  Me Farther was an 'ermit and me Murther was an 'ermit sos I come by me job rightly."
			)
			(3
				(TalkObjMessages (ScriptID 83 1) 143 3)
				;"Don't 'ave too many visitors.  'Ermits don't, you know.  (Part o' the job description.) I likes to see a new face, though."
			)
			(4
				(TalkObjMessages (ScriptID 83 1) 143 4)
				;"Good to 'ear anurther's speaking besides meself.  Sos wot can I do for you?"
			)
			(5 (HandsOn))
		)
	)
)
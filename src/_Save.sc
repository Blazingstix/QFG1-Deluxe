;;; Sierra Script 1.0 - (do not remove this comment)
;;;;
;;;;	SAVE.SC
;;;;	(c) Sierra On-Line, Inc, 1988
;;;;
;;;;	Author: Jeff Stephenson
;;;;
;;;;	Classes which create the save/restore game user interface.  Also
;;;;	contains a number of instances of Dialogs and associated DItems
;;;;	used in the interface.
;;;;
;;;;	Classes:
;;;;		SRDialog
;;;;		Save
;;;;		Restore
;;;;
;;;;    Procedures:
;;;;		GetDirectory
;;;;		HaveSpace
;;;;		GetStatus
;;;;		NeedDescription
;;;;

(script# SAVE) ;SAVE = 990
(include sci2.sh) (include SYSTEM.SH)
(use Main)
(use _Interface)
(use _System)

(define	GAMESSHOWN 8)		;the number of games displayed in the selector
(define	MAXGAMES 20)		;maximum number of games in a save directory
(define	COMMENTSIZE 36)		;size of user's description of the game
(define COMMENTBUFF 18) 	;(define	COMMENTBUFF (/ (+ 1 COMMENTSIZE) 2))

(define	DIRECTORYSIZE 29) ;size of the save directory name
 ;(define	DIRECTORYBUFF (/ (+ 1 DIRECTORYSIZE) 2))

(define BUFFERSIZE 361) ;(define	BUFFERSIZE (+ (* MAXGAMES COMMENTBUFF) 1))



(public
	GetDirectory 0
)

(local
	default
	i
	numGames
	selected
	theStatus
	[butbuf1 4] = [{Restore} {__Save__} {Replace} {Replace}]
	[butbuf2 4] = [{Select the game that you would like to restore.} {Type the description of this saved game.} {This directory/disk can hold no more saved games. You must replace one of your saved games or use Change Directory to save on a different directory/disk.} {This directory/disk can hold no more saved games. You must replace one of your saved games or use Change Directory to save on a different directory/disk.}]
)

(enum
	RESTORE			;Restore games
	HAVESPACE		;Save, with space on disk
	NOSPACE			;Save, no space on disk but games to replace
	NOREPLACE		;Save, no space on disk, no games to replace
)

;(procedure (GetDirectory where &tmp result [newDir 33] [str 40])
;	(asm
;code_0748:
;		pushi    13
;		pushi    990
;		pushi    1
;		pushi    #font
;		pushi    SYSFONT
;		pushi    41
;		pushi    2
;		lea      @newDir
;		push    
;		lsp      where
;		callk    StrCpy,  4
;		push    
;		pushi    DIRECTORYSIZE
;		pushi    #button
;		lofsa    {OK}
;		push    
;		pushi    1
;		pushi    #button
;		lofsa    {Cancel}
;		push    
;		pushi    0
;		calle    Print,  26
;		sat      result
;		not     
;		bnt      code_077b
;		ldi      FALSE
;		ret     
;code_077b:
;		pushi    1
;		lea      @newDir
;		push    
;		callk    StrLen,  2
;		not     
;		bnt      code_078f
;		pushi    1
;		lea      @newDir
;		push    
;		callk    GetCWD,  2
;code_078f:
;		pushi    1
;		lea      @newDir
;		push    
;		callk    ValidPath,  2
;		bnt      code_07aa
;		pushi    2
;		lsp      where
;		lea      @newDir
;		push    
;		callk    StrCpy,  4
;		ldi      TRUE
;		ret     
;		jmp      code_0748
;code_07aa:
;		pushi    3
;		pushi    4
;		lea      @str
;		push    
;		pushi    990
;		pushi    2
;		lea      @newDir
;		push    
;		callk    Format,  8
;		push    
;		pushi    #font
;		pushi    SYSFONT
;		calle    Print,  6
;		jmp      code_0748
;		ret     
;	)
;)

;CI:NOTE This GetDirectory procedure was downloaded from Eric's Decompilation Archive. 
;I have nodified it to more closely match the original code.
(procedure (GetDirectory where &tmp result [newDir 33] [str 40])
	(repeat
		(= result
			(Print 
				990 1
				#font SYSFONT
				#edit (StrCpy @newDir where) DIRECTORYSIZE
				#button {OK} TRUE
				#button {Cancel} FALSE
			)
			;New save-game directory:
		)

		;Pressed ESC -- return FALSE.
		(if (not result)
			(return FALSE)
		)

		;No string defaults to current drive.
		(if (not (StrLen @newDir))
			(GetCWD @newDir)
		)

		;If drive is valid, return TRUE, otherwise complain.
		(if (ValidPath @newDir)
			(StrCpy where @newDir)
			(return TRUE)
		else
			(Print
				(Format @str 990 2 @newDir)
				#font SYSFONT
			)
			;%s\nis not a valid directory
		)
	)
)


(procedure (GetStatus)
	(return
		(cond 
			((== self Restore) 	0)
			((HaveSpace) 		1)
			(numGames 			2)
			(else 				3)
		)
	)
)

(procedure (HaveSpace)
	(if (< numGames MAXGAMES) (CheckFreeSpace curSaveDir))
)

(procedure (NeedDescription)
	(Print 990 3 #font 0)
	; You must type a description for the game.
)

(class SysWindow of Object
	(properties
		top 		0
		left 		0
		bottom 		0
		right 		0
		color 		vBLACK	; foreground color
		back 		vWHITE	; background color
		priority 	RELEASE	; priority
		window 		NULL	; handle/pointer to system window
		type 		$0000	; generally corresponds to system window types
		title 		NULL		; text appearing in title bar if present
		;; this rectangle is the working area for X/Y centering
		;; these coordinates can define a subsection of the picture
		;; in which a window will be centered
		brTop 		0
		brLeft 		0
		brBottom 	190
		brRight 	SCRNWIDE
	)
	
	(method (dispose)
		(if window (DisposeWindow window) (= window NULL))
		(super dispose:)
	)
	
	(method (open)
		(= window
			(NewWindow
				top
				left
				bottom
				right
				title
				type
				priority
				color
				back
			)
		)
	)
)

(class SRDialog of Dialog
	(properties
		;elements 0
		;size 0
		;text 0
		;window 0
		;theItem 0
		;nsTop 0
		;nsLeft 0
		;nsBottom 0
		;nsRight 0
		;time 0
		;busy 0
		;seconds 0
		;lastSeconds 0
	)
	
	(method (init param1 param2 param3)
		(= window SysWindow)
		(= nsBottom 0)
		(if
			(==
				(= numGames
					(GetSaveFiles (theGame name?) param2 param3)
				)
				-1
			)
			(return 0)
		)
		(if (== (= theStatus (GetStatus)) 1)
			(editI
				text: (StrCpy param1 param2)
				font: smallFont
				setSize:
				moveTo: 4 4
			)
			(self add: editI setSize:)
		)
		(selectorI
			text: param2
			font: smallFont
			setSize:
			moveTo: 4 (+ nsBottom 4)
			state: dExit
		)
		(= i (+ (selectorI nsRight?) 4))
		(okI
			text: [butbuf1 theStatus]
			setSize:
			moveTo: i (selectorI nsTop?)
			state: (if (== theStatus 3) 0 else 3)
		)
		(cancelI
			setSize:
			moveTo: i (+ (okI nsBottom?) 4)
			state: (& (cancelI state?) $fff7)
		)
		(changeDirI
			setSize:
			moveTo: i (+ (cancelI nsBottom?) 4)
			state: (& (changeDirI state?) $fff7)
		)
		(self add: selectorI okI cancelI changeDirI setSize:)
		(textI
			text: [butbuf2 theStatus]
			setSize: (- (- nsRight nsLeft) 8)
			moveTo: 4 4
		)
		(= i (+ (textI nsBottom?) 4))
		(self eachElementDo: #move 0 i)
		(self add: textI setSize: center: open: 4 15)
		(return 1)
	)
	
;	(method (doit theComment &tmp oldStatus fd rtn offset [names 361] [nums 21] [str 40])
;		(asm
;			pushSelf
;			lofsa    Restore
;			eq?     
;			bnt      code_026d
;			lap      argc
;			bnt      code_026d
;			lap      theComment
;			bnt      code_026d
;			pushi    1
;			pushi    4
;			lea      @str
;			push    
;			pushi    990
;			pushi    0
;			pushi    #name
;			pushi    0
;			lag      theGame
;			send     4
;			push    
;			callk    Format,  8
;			push    
;			callk    FOpen,  2
;			sat      fd
;			push    
;			ldi      65535
;			eq?     
;			bnt      code_0267
;			ret     
;code_0267:
;			pushi    1
;			lst      fd
;			callk    FClose,  2
;code_026d:
;			pushi    #init
;			pushi    3
;			lsp      theComment
;			lea      @names
;			push    
;			lea      @nums
;			push    
;			self     10
;			not     
;			bnt      code_0286
;			ldi      65535
;			ret     
;code_0286:
;			lsl      theStatus
;			dup     
;			ldi      0
;			eq?     
;			bnt      code_02a0
;			lal      numGames
;			bnt      code_02bd
;			lofsa    okI
;			jmp      code_02bd
;			lofsa    changeDirI
;			jmp      code_02bd
;code_02a0:
;			dup     
;			ldi      1
;			eq?     
;			bnt      code_02ad
;			lofsa    editI
;			jmp      code_02bd
;code_02ad:
;			dup     
;			ldi      2
;			eq?     
;			bnt      code_02ba
;			lofsa    okI
;			jmp      code_02bd
;code_02ba:
;			lofsa    changeDirI
;code_02bd:
;			toss    
;			sal      default
;			pushi    #doit
;			pushi    1
;			push    
;			super    Dialog,  6
;			sal      i
;			pushi    #indexOf
;			pushi    1
;			pushi    #cursor
;			pushi    0
;			lofsa    selectorI
;			send     4
;			push    
;			lofsa    selectorI
;			send     6
;			sal      selected
;			push    
;			ldi      18
;			mul     
;			sat      offset
;			lsl      i
;			lofsa    changeDirI
;			eq?     
;			bnt      code_0384
;			pushi    1
;			lsg      curSaveDir
;			call     GetDirectory,  2
;			bnt      code_0286
;			pushi    3
;			pushi    #name
;			pushi    0
;			lag      theGame
;			send     4
;			push    
;			lea      @names
;			push    
;			lea      @nums
;			push    
;			callk    GetSaveFiles,  6
;			sal      numGames
;			push    
;			ldi      65535
;			eq?     
;			bnt      code_031c
;			ldi      65535
;			sat      rtn
;			jmp      code_0494
;code_031c:
;			lal      theStatus
;			sat      oldStatus
;			pushi    0
;			call     GetStatus,  0
;			sal      theStatus
;			push    
;			dup     
;			ldi      0
;			eq?     
;			bnt      code_0332
;			jmp      code_0378
;code_0332:
;			dup     
;			lat      oldStatus
;			eq?     
;			bnt      code_0363
;			pushi    #contains
;			pushi    1
;			lofsa    editI
;			push    
;			self     6
;			bnt      code_0378
;			pushi    #cursor
;			pushi    1
;			pushi    1
;			pushi    2
;			lsp      theComment
;			lea      @names
;			push    
;			callk    StrCpy,  4
;			push    
;			callk    StrLen,  2
;			push    
;			pushi    83
;			pushi    0
;			lofsa    editI
;			send     10
;			jmp      code_0378
;code_0363:
;			pushi    #dispose
;			pushi    0
;			pushi    87
;			pushi    3
;			lsp      theComment
;			lea      @names
;			push    
;			lea      @nums
;			push    
;			self     14
;code_0378:
;			toss    
;			pushi    #draw
;			pushi    0
;			lofsa    selectorI
;			send     4
;			jmp      code_0286
;code_0384:
;			lsl      theStatus
;			ldi      2
;			eq?     
;			bnt      code_03ba
;			lsl      i
;			lofsa    okI
;			eq?     
;			bnt      code_03ba
;			pushi    #doit
;			pushi    1
;			pushi    2
;			lsp      theComment
;			lat      offset
;			leai     @names
;			push    
;			callk    StrCpy,  4
;			push    
;			lofsa    GetReplaceName
;			send     6
;			bnt      code_0286
;			lal      selected
;			lati     nums
;			sat      rtn
;			jmp      code_0494
;			jmp      code_0286
;code_03ba:
;			lsl      theStatus
;			ldi      1
;			eq?     
;			bnt      code_043b
;			lsl      i
;			lofsa    okI
;			eq?     
;			bt       code_03d4
;			lsl      i
;			lofsa    editI
;			eq?     
;			bnt      code_043b
;code_03d4:
;			pushi    1
;			lsp      theComment
;			callk    StrLen,  2
;			push    
;			ldi      0
;			eq?     
;			bnt      code_03e9
;			pushi    0
;			call     NeedDescription,  0
;			jmp      code_0286
;code_03e9:
;			ldi      65535
;			sat      rtn
;			ldi      0
;			sal      i
;code_03f1:
;			lsl      i
;			lal      numGames
;			lt?     
;			bnt      code_0413
;			pushi    2
;			lsp      theComment
;			lsl      i
;			ldi      18
;			mul     
;			leai     @names
;			push    
;			callk    StrCmp,  4
;			sat      rtn
;			not     
;			bnt      code_040e
;code_040e:
;			+al      i
;			jmp      code_03f1
;code_0413:
;			lat      rtn
;			not     
;			bnt      code_0421
;			lal      i
;			lati     nums
;			jmp      code_0433
;code_0421:
;			lsl      numGames
;			ldi      20
;			eq?     
;			bnt      code_0431
;			lal      selected
;			lati     nums
;			jmp      code_0433
;code_0431:
;			lal      numGames
;code_0433:
;			sat      rtn
;			jmp      code_0494
;			jmp      code_0286
;code_043b:
;			lsl      i
;			lofsa    okI
;			eq?     
;			bnt      code_0451
;			lal      selected
;			lati     nums
;			sat      rtn
;			jmp      code_0494
;			jmp      code_0286
;code_0451:
;			lsl      i
;			ldi      0
;			eq?     
;			bt       code_0462
;			lsl      i
;			lofsa    cancelI
;			eq?     
;			bnt      code_046c
;code_0462:
;			ldi      65535
;			sat      rtn
;			jmp      code_0494
;			jmp      code_0286
;code_046c:
;			lsl      theStatus
;			ldi      1
;			eq?     
;			bnt      code_0286
;			pushi    #cursor
;			pushi    1
;			pushi    1
;			pushi    2
;			lsp      theComment
;			lat      offset
;			leai     @names
;			push    
;			callk    StrCpy,  4
;			push    
;			callk    StrLen,  2
;			push    
;			pushi    83
;			pushi    0
;			lofsa    editI
;			send     10
;			jmp      code_0286
;code_0494:
;			pushi    #dispose
;			pushi    0
;			self     4
;			lat      rtn
;			ret     
;		)
;	)

	;CI: NOTE: This was downloaded from Eric's Decompilation Archive for QFG1EGA. 
	;I have modified it where necessary to match the my decompiled output. My original asm decompilation is commented out above.
	(method	(doit theComment
						&tmp 	oldStatus fd rtn offset
								[names BUFFERSIZE] [nums 21]
								[str 40]
				)

		;If restore: is called with a TRUE parameter, do nothing if there
		;are no saved games.  This allows optionally presenting the user
		;with his saved games at the start of the game.
		(if
			(and
				(== self Restore)
				argc
				theComment
			)

			(= fd (FOpen (Format @str 990 0 (theGame name?))))
			;%ssg.dir
			(if (== fd -1)
				;no directory -> no saved games
				(return)
			)
			(FClose fd)
		)

		(if (not (self init: theComment @names @nums))
			(return -1)
		)

		(repeat
			(= default
				(switch theStatus
					(RESTORE
						(if numGames okI else changeDirI)
					)
					(HAVESPACE
						;Edit item of save games is active if present
						editI
					)
					(NOSPACE
						;If there are save-games to replace, 'Replace'
						;button is active.
						okI
					)
					(else
						;Otherwise 'Change Directory' button is active.
						changeDirI
					)
				)
			)

			(= i (super doit: default))

			(= selected (selectorI indexOf: (selectorI cursor?)))
			(= offset (* selected COMMENTBUFF))
			(cond
				((== i changeDirI)
					(if (GetDirectory curSaveDir)
						(= numGames
							(GetSaveFiles (theGame name?) @names @nums)
						)
						(if (== numGames -1)
							(= rtn -1)
							(break)
						)

						(= oldStatus theStatus)
						(= theStatus (GetStatus))
						(switch theStatus
							(RESTORE
							)
							(oldStatus
								(if (self contains: editI)
									(editI
										cursor: (StrLen (StrCpy theComment @names)),
										draw:
									)
								)
							)
							(else
								(self
									dispose:,
									init: theComment @names @nums
								)
							)
						)

						(selectorI draw:)
					)
				)

				((and (== theStatus NOSPACE) (== i okI))
					(if (GetReplaceName doit: (StrCpy theComment @[names offset]))
						(= rtn [nums selected])
						(break)
					)
				)

				((and (== theStatus HAVESPACE) (or (== i okI) (== i editI)))
					(if (== (StrLen theComment) 0)
						(NeedDescription)
						(continue)
					)

					(= rtn -1)
					(for	((= i 0))
							(< i numGames)
							((++ i))

						(= rtn (StrCmp theComment @[names (* i COMMENTBUFF)]))
						(breakif (not rtn))
					)

					(= rtn
						(cond
							((not rtn)
								[nums i]
							)
							((== numGames MAXGAMES)
								[nums selected]
							)
							(else
								numGames
							)
						)
					)
					(break)
				)

				((== i okI)
					(= rtn [nums selected])
					(break)
				)

				((or (== i 0) (== i cancelI))
					(= rtn -1)
					(break)
				)

				((== theStatus HAVESPACE)
					(editI
						cursor:
							(StrLen (StrCpy theComment @[names offset])),
						draw:
					)
				)
			)
		)

		(self dispose:)
		(return rtn)
	)

)



(class Restore of SRDialog
	(properties
		;elements 0
		;size 0
		text {Restore a Game}
		;window 0
		;theItem 0
		;nsTop 0
		;nsLeft 0
		;nsBottom 0
		;nsRight 0
		;time 0
		;busy 0
		;seconds 0
		;lastSeconds 0
	)
)

(class Save of SRDialog
	(properties
		;elements 0
		;size 0
		text {Save a Game}
		;window 0
		;theItem 0
		;nsTop 0
		;nsLeft 0
		;nsBottom 0
		;nsRight 0
		;time 0
		;busy 0
		;seconds 0
		;lastSeconds 0
	)
)

(instance GetReplaceName of Dialog
	(properties)
	
	(method (doit param1 &tmp temp0)
		(= window SysWindow)
		(text1 setSize: moveTo: 4 4)
		(self add: text1 setSize:)
		(oldName
			text: param1
			font: smallFont
			setSize:
			moveTo: 4 nsBottom
		)
		(self add: oldName setSize:)
		(text2 setSize: moveTo: 4 nsBottom)
		(self add: text2 setSize:)
		(newName
			text: param1
			font: smallFont
			setSize:
			moveTo: 4 nsBottom
		)
		(self add: newName setSize:)
		(button1 nsLeft: 0 nsTop: 0 setSize:)
		(button2 nsLeft: 0 nsTop: 0 setSize:)
		(button2
			moveTo: (- nsRight (+ (button2 nsRight?) 4)) nsBottom
		)
		(button1
			moveTo: (- (button2 nsLeft?) (+ (button1 nsRight?) 4)) nsBottom
		)
		(self add: button1 button2 setSize: center: open: 0 15)
		(= temp0 (super doit: newName))
		(self dispose:)
		(if (not (StrLen param1))
			(NeedDescription)
			(= temp0 0)
		)
		(return (if (== temp0 newName) else (== temp0 button1)))
	)
)

(instance selectorI of DSelector
	(properties
		x 36
		y 8
	)
)

(instance editI of DEdit
	(properties
		max 35
	)
)

(instance okI of DButton
	(properties)
)

(instance cancelI of DButton
	(properties
		text { Cancel_}
	)
)

(instance changeDirI of DButton
	(properties
		;CI: This was saved as {Change\0D\nDirectory} by EO in his restoration. A decompile of QFG1 v1.200 had {Change\nDirectory} instead.
		text {Change\nDirectory} 
	)
)

(instance textI of DText
	(properties
		font SYSFONT
	)
)

(instance text1 of DText
	(properties
		text {Replace}
		font SYSFONT
	)
)

(instance text2 of DText
	(properties
		text {with:}
		font SYSFONT
	)
)

(instance oldName of DText
	(properties)
)

(instance newName of DEdit
	(properties
		max 35
	)
)

(instance button1 of DButton
	(properties
		text {Replace}
	)
)

(instance button2 of DButton
	(properties
		text {Cancel}
	)
)

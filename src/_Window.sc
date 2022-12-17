;;; Sierra Script 1.0 - (do not remove this comment)
;; The window class defines visible BORDERED rectangular areas of the screen.
;; A window, ID'ed by systemWindow, is used by Dialog to specify various
;; aspects of its appearence. 

(script# WINDOW) ;WINDOW = 981
(include system.sh) (include sci2.sh) (include game.sh)
(use _Save)


(class Window of SysWindow
	(properties
		;properties from SysWindow
		;top 		0		
		;left 		0		
		;bottom 		0		
		;right 		0		
		;color 		vBLACK	; foreground color
		;back 		vWHITE	; background color
		;priority 	RELEASE		; priority
		;window 		0		; handle/pointer to system window
		;type 		$0000	; generally corresponds to system window types
		;title 		0		; text appearing in title bar if present
		;; this rectangle is the working area for X/Y centering
		;; these coordinates can define a subsection of the picture
		;; in which a window will be centered
		;brTop 		0
		;brLeft 		0
		;brBottom 	190
		;brRight 	SCRNWIDE
		;new properties for Window
		underBits 	0
	)
	
	(method (doit)
	)
	
	(method (dispose)
		(self restore:)
		(if window (DisposeWindow window) (= window NULL))
		(super dispose:)
	)
	
	(method (open)
		;; Open corresponding system window structure
		;; Custom window type 0x81 indicates that system
		;; will NOT draw the window, only get a port and link into list
		(= window
			(NewWindow
				;; port dimensions
				top left bottom right
				
				title
				type
				priority
				color
				back
			)
		)
	)
	
	(method (handleEvent)
		(return FALSE)
	)
	
	(method (setMapSet &tmp mapSet)
		(= mapSet 0)
		(if (!= -1 color) (= mapSet (| mapSet VMAP)))
		(if (!= -1 priority) (= mapSet (| mapSet PMAP)))
		(return mapSet)
	)
	
	(method (move h v)
		(= left (+ left h))
		(= right (+ right v))
		(= right (+ right h))
		(= bottom (+ bottom v))
	)
	
	(method (moveTo h v)
		(self move: (- h left) (- v top))
	)
	
	(method (draw v p)
		(if (>= argc 1) (= color v))
		(if (>= argc 2) (= priority p))
		(Graph
			GFillRect
			top left bottom right
			(self setMapSet:)
			color priority
		)
	)
	
	(method (save)
		(= underBits
			(Graph
				GSaveBits
				top left bottom right
				(self setMapSet:)
			)
		)
	)
	
	(method (restore)
		(if underBits (Graph GRestoreBits underBits))
	)
	
	(method (inset h v)
		(= top (+ top v))
		(= left (+ left h))
		(= bottom (- bottom v))
		(= right (- right h))
	)
	
	(method (show)
		(Graph
			GShowBits
			top left bottom right
			(self setMapSet:)
		)
	)
	
	(method (erase)
		(self draw: back -1)
	)
	
	(method (center)
		(self
			moveTo:
				(/ (- (- brRight left) (- right left)) 2)
				(/ (- (- brBottom top) (- bottom top)) 2)
		)
	)
)

;;; Sierra Script 1.0 - (do not remove this comment)
(script# CURSORCOORDS) ;CURSORCOORDS = 810
(include system.sh) (include sci2.sh) (include game.sh)
(use Main)
(use _System)


(class CursorCoords of Object
	(properties
		cursorX 0
		cursorY 0
	)
)

(class InputList of Set
	(properties
		elements 0
		size 0
	)
	
	(method (handleEvent event &tmp dist thisCoords setCoords temp3 temp4 i temp6 theDirectionAngle ang temp9 eventX eventY x2 y2)
		(if (== (event type?) direction)
			(= theDirectionAngle [directionAngle (event message?)])
			(= temp3 60)
			(= temp4 400)
			(= eventX (event x?))
			(= eventY (event y?))
			(= setCoords NULL)
			(= i 0)
			(while (< i size)
				(= thisCoords (self at: i))
				(= x2 (thisCoords cursorX?))
				(= y2 (thisCoords cursorY?))
				(if (or (!= eventX x2) (!= eventY y2))
					(= ang (GetAngle eventX eventY x2 y2))
					(= dist (GetDistance eventX eventY x2 y2))
					(= temp9 (Abs (- theDirectionAngle ang)))
					(if (> temp9 180)
						(= temp9 (- 360 temp9))
					)
					(= temp6 (<= temp9 (+ temp3 10)))
					(if
						(or
							(<= temp9 (- temp3 10))
							(and temp6 (< (+ temp9 dist) (+ temp3 temp4)))
							(and temp6 (== (+ temp9 dist) (+ temp3 temp4))
								(< temp9 temp3)
							)
						)
						(= temp3 temp9)
						(= temp4 dist)
						(= setCoords thisCoords)
					)
				)
				(++ i)
			)
			(if setCoords
				(SetCursor theCursor TRUE
					(setCoords cursorX?)
					(setCoords cursorY?)
				)
			)
		)
	)
	
	(method (empty &tmp i)
		(if size
			(= i (- size 1))
			(while (>= i 0)
				((self at: i) dispose:)
				(self delete: (self at: i))
				(-- i)
			)
		)
	)
)

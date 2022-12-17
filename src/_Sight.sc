;;; Sierra Script 1.0 - (do not remove this comment)
;;;;
;;;;  SIGHT.SC
;;;;
;;;;  (c) Sierra On-Line, Inc., 1992
;;;;
;;;;  Author:  Unknown
;;;;  Updated: 
;;;;     Brian K. Hughes
;;;;     August 19, 1992
;;;;
;;;;  Procedures:
;;;;     IsOffScreen
;;;;     CantBeSeen
;;;;     AngleDiff


(script# SIGHT)
(include system.sh) (include sci2.sh) (include game.sh)
(use Main)

(public
	IsOffScreen 0
	CantBeSeen 1
	AngleDiff 2
)

(procedure (IsOffScreen theObj)
	(return
		(not
			(if
				(and
					(< 0 (theObj x?))
					(< (theObj x?) 320)
					(< 0 (- (theObj y?) (theObj z?)))
				)
				(< (- (theObj y?) (theObj z?)) 200)
			else
				0
			)
		)
	)
)

(procedure (CantBeSeen theSight optSeer optFieldAngle optFieldDepth 
      &tmp 
      theSeer fieldAngle fieldDepth
      sx sy    ;theSight's x/y
      ex ey    ;typically ego's x/y
   )
   
   ;;is theSight within angle and depth of theSeer's field of vision? 
   ;; theSeer defaults to ego
   ;; vision angle defaults to 360
   ;; depth defaults to "very large"
   ;;by Pablo Ghenis
   
	(= theSeer optSeer)
	(= fieldAngle optFieldAngle)
	(= fieldDepth optFieldDepth)
	(if (< argc 4)
		(= fieldDepth INFINITY)
		(if (< argc 3)
			(if (< argc 2)
				(= theSeer ego)
				)
			(= fieldAngle
				(- 360 (if (== theSeer ego) (* 2 egoBlindSpot) else 0))
			)
		)
	)
	(= sx (theSight x?))
	(= sy (theSight y?))
	(= ex (theSeer x?))
	(= ey (theSeer y?))
	(return
		(if (!= theSight theSeer)
			(if
				(<
					(/ fieldAngle 2)
					(Abs
						(AngleDiff
							(GetAngle ex ey sx sy)
							(theSeer heading?)
						)
					)
				)
			else
				(<
					fieldDepth
					(GetDistance ex ey sx sy perspective)
				)
			)
		else
			0
		)
	)
)

(procedure (AngleDiff ang h)
	;;return the difference between two angles in -179/+180 range
   	;;positive numbers mean shortest turn is clockwise
   	;;by Pablo Ghenis

	(if (>= argc 2) (= ang (- ang h)))	; deviation in -359/+359 range
	(return
		(cond 	; convert to -179/+180 range
			((<= ang -180) (+ ang 360))
			((> ang 180) (- ang 360))
			(else ang)
		)
	)
)

;;; Sierra Script 1.0 - (do not remove this comment)
(script# 205)
(include system.sh) (include sci2.sh) (include game.sh)
(use Main)
(use _Actor)
(use _System)


(class StatusBar of Object
	(properties
		value 0
		max 1000
		x 1000
		y 1000
		priority 15
		titleCel 0
		lineObj 0
	)
	
	(method (init)
		(Load RES_VIEW hpStatusView)
		((PicView new:)
			view: hpStatusView
			loop: 0
			cel: titleCel
			x: x
			y: y
			z: 1
			priority: priority
			init:
		)
		;HP, SP, MP, Time
		((PicView new:)
			view: hpStatusView
			loop: 0
			cel: 3
			x: x
			y: y
			z: 2
			priority: priority
			init:
		)
		;Border around value.
		(addToPics doit:)
		((= lineObj (Prop new:))
			ignoreActors:
			view: hpStatusView
			posn: x y 3
			init:
		)
		;actual percentage bar
		(self draw:)
	)
	
	(method (dispose)
		(if (IsObject lineObj) (lineObj dispose:))
		(super dispose:)
	)
	
	(method (draw &tmp temp0 curValue)
		(if (< max 1) (= max 1))
		(if (> (= curValue value) max) (= curValue max))
		;convert curValue to percentage of max
		(= curValue (/ (+ (* curValue 100) max -1) max))
		;then divide by 10 to get the cell # to show.
		(= temp0 (/ (+ curValue 9) 10))
		(lineObj
			loop: 1
			cel: temp0
			posn: (+ x 2) y
			setPri: priority
			stopUpd:
		)
	)
)

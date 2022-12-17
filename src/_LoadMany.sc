;;; Sierra Script 1.0 - (do not remove this comment)
;;Author: Pablo Ghenis, 7/17/89
;;
;;Usage examples:
;;       (LoadMany VIEW 110 111 201)   ;to load a series of views
;;       (LoadMany SCRIPT 110 111 201) ;to load a series of scripts
;;       (LoadMany FALSE 110 111 201)  ;to use DisposeScript

(script# LOADMANY) ;LOADMANY = 958
(include system.sh) (include sci2.sh) (include game.sh)

(public
	LoadMany 0
)

(procedure (LoadMany what which &tmp i theRes)

	(= argc (- argc 2))
	(= i 0)
	(while (<= i argc)
		(= theRes [which i])
		(if what
			(Load what theRes)
		else
			(DisposeScript theRes)
		)
		(++ i)
	)
	(DisposeScript LOADMANY) ;EO: This was left out of the LOADMANY in the HQ1 source; this must be here to prevent a Program Bug death at startup!
)

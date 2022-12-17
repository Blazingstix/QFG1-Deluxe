;;; Sierra Script 1.0 - (do not remove this comment)
;;;;
;;;;  INVENT.SC
;;;;
;;;;  (c) Sierra On-Line, Inc, 1992
;;;;
;;;;  Author:  Unkown
;;;;  Updated:
;;;;     Brian K. Hughes
;;;;     July 24, 1992
;;;;
;;;;  Procedures:
;;;;	 SaidGet 	(SCI0 only)
;;;;	 WtCarried	(added specially for Hero's Quest)
;;;;	 SaidCast 	(added specially for Hero's Quest)
;;;;
;;;;  Classes:
;;;;     InvItem	(SCI1.1 only)
;;;;     Inventory

(script# INVENT) ;INVENT = 995
(include system.sh) (include sci2.sh) (include game.sh)
(use Main)
(use _System)

(public
	SaidGet 0
	WtCarried 1
	SaidCast 2
)

(local
	[verbInventory 41] = ['/silver,alm,alm' '/gold' '/food,ration' '/mandrake' '/key' '/blade' '/dagger' '/leather,armor' '/shield' '/note,message,note,scroll' '/apple,apple' '/carrot,produce' '/gem[<magic,glowing]' '/vase' '/candelabra' '/box<music' '/candlestick' '/pearl,necklace,string' '/ring[<healer,gold]' '/seed' '/boulder' '/flower' '/lockpick[<hasp]' '/kit[<thief,implement]' '/certificate[<thief]' '/bottle[<empty,!*]' '/fur[<green]' '/dust<faerie,magic' '/water,(bottle<water)' '/mushroom[<magic]' '/claw[<cheetaur]' '/beard[<troll]' '/chainmail,chain,chainmail' '/healing,(potion<healing)' '/potion<mana,magic,power' '/potion<vigor,stamina' '/potion<hero,heroism' '/potion<disenchant,disenchant' '/grease,(potion,grease<ghost,ghoul)' '/mirror[<magic,hand]' '/acorn[<dryad,magic]']
	[verbMagic 8] = ['/open,(spell<open)' '/detect,(spell,magic<detect),(spell<magic<detect)' '/trigger,(spell<trigger)' '/dazzle,(spell<dazzle)' '/zap,(spell<zap)' '/calm,(spell<calm)' '/flame,fire,dart,fireball,(spell<flame,fire,dart,fireball)' '/fetch,(spell<fetch)']
	;HQ1 had two additional magic verbs: "slow" and "reversal".
	;Were those spells originally going to be in the game?
	;Reversal did get used for QFG2.
)
(procedure (SaidGet event &tmp i n)
	(= i 0)
	(while (< i 41)
		(if (Said [verbInventory i]) (return (+ i 1)))
		(++ i)
	)
	(event claimed: TRUE)
	(return NULL)
)

(procedure (WtCarried &tmp i n)
	(= n 1)
	(= i 0)
	(while (<= n 41)
		(= i (+ i (* [invNum n] [invWeight n])))
		(++ n)
	)
	(= i (/ (+ i 59) 60))
)

(procedure (SaidCast event &tmp i n)
	(= i 0)
	(while (< i 8)
		(if (Said [verbMagic i])
			 (return (+ OPEN i))
		)
		(++ i)
	)
	(event claimed: TRUE)
	(return NULL)
)

(class Inventory of Object
	;the standard inventory system used by most Sierra games is 
	;insufficient for the more complicated inventory management 
	;used by Quest for Glory (and later games).
	;So, the Inventory object is largely unused in this game.
	(properties)
	
	(method (init)
		(return TRUE)
	)
)

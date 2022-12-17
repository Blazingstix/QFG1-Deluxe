;;; Sierra Script 1.0 - (do not remove this comment)

;These are death flags. They're used to track which ways the user has died.  
;They will be shown at the end of the game.

; unique deaths
(define DIE_RESTART -2) ;show Restore, Restart, Quit; (same as not specifying anything at all) instead of Retry, Restore, Quit)
(define DIE_RETRY	-1) ;show a Retry, Restore, Quit
(define DIE_NOFLAG 	-1)	; a retry is shown, but nothing is flagged.
(define DIE_START	450)
(define DIE_COUNT	 79)
(define DIE_END		528)
(define RETRY_STYLE 7); 7 = IRISOUT; This is the Screen draw style, when we're doing a Retry.
;there are roughly 100 deaths, so we'll reserve flags 450-550 for them (with room for 50 more in expansion up to flag 600)
(enum 450
	DIE_NOSTAMINA
	DIE_PICKNOSE
	DIE_ARRESTED
	DIE_NIGHTGAUNT

	DIE_ARENA
	DIE_ARENA_CALM
	DIE_ARENA_UNARMED
	
	DIE_DRINK_DRAGONSBREATH		;drank the dragon's breath in the tavern

	DIE_LOL_KITTY				;Annoyed the kitty in LOL's house.
	DIE_LOL_UPSTAIRS			;Tried to go upstairs in the L.O.L.'s house

	DIE_SHERIFF_POWERGAMING		;Practicing your lock pick skills too much on the safe
	DIE_SHERIFF_MUSICBOX		;got in the way of Otto when openning the music box.
	DIE_SHERIFF_BREAK_VASE		;Tried to break into Otto's Room.

	DIE_SHERIFF_OTTO_DOOR		;tried to enter Otto's room (bottom door)
	DIE_SHERIFF_HIS_DOOR		;tried to enter the Sheriff's room (left door)
	DIE_SHERIFF_HER_DOOR		;tried to enter the Mrs' room (right door)

	DIE_CHIEF_THIEF_ACCIDENT	;killed by walking in front of the Chief Thief's dagger throwing.
	DIE_CHIEF_THIEF_ATTACKED	;killed by the Chief thief for trying to fight him

	DIE_BRUNO_OUTLOOK_DAGGER	;tried to fight Bruno outside the Town Gates
	DIE_BRUNO_OUTLOOK_MAGIC		;tried to use magic on Bruno outside the Town Gates
	
	DIE_ALLEY
	DIE_GRAVEYARD_HOLE
	DIE_GRAVEYARD_GHOSTS
	
	DIE_CASTLEBARRACKS
	DIE_CASTLEGUARDS
	DIE_CASTLE_FALL
	DIE_CASTLE_GATE
	
	DIE_FALL_HEALERTREE

	DIE_FROST_GIANT_FIGHT
	DIE_FROST_GIANT_MAGIC

	DIE_HENRY_CRUSHED		;killed by Henry crushing the door on you
	DIE_HENRY_FALL			;killed by being knocked off Henry's ledge
	DIE_HENRY_TELEPORT		;killed by angering Henry
	
	DIE_FALL_SEED			;fell trying to get the Spitting Spirea seed
	DIE_DRYAD_PLANT
	DIE_DRYAD_STAG
	DIE_DRYAD_FIRE
	
	DIE_FAERY_DANCE
	
	DIE_EAT_MUSHROOMS			;ate too many mushrooms
	DIE_EAT_KOBOLDMUSHROOMS		;ate Bad mushrooms.
	
	DIE_BEAR_CLOSE
	DIE_KOBOLD
	DIE_KOBOLD_CHEST1
	DIE_KOBOLD_CHEST2

	DIE_BABAYAGA_CRUSHED		;crushed by Baba Yaga's hut
	DIE_BABA_NONAME				;refused to give Baba Yaga your name
	DIE_BABA_NOBRAVE			;told Baba Yaga you weren't brave
	DIE_BABA_NOFETCH			;refused to get her the mandrake
	DIE_BABA_NOBRING			;didn't bring her the mandrake
	DIE_BABA_LIEBRING			;lied about bringing her the mandrake
	DIE_BABA_NOMIRROR			;visited her one too many times
	DIE_BABA_CURSE				;died from Baba Yaga's curse
	
	DIE_RANGE_BRUNO			;killed by Bruno while Spying on him and Brutus
	DIE_RANGE_BRUNO2		;killed by Bruno while he's leaving the Target Range
	DIE_RANGE_BRUTUS		;killed by Brutus while spying on him after his conversation with Bruno
	DIE_ANTWERP_BOUNCED		;killed by the Antwerp bouncing into you
	DIE_ANTWERP_FLATTENED	;killed by the Antwerp landing on you.
	DIE_FRED_INSTAKILL		;Fred the Troll snuck up on you, and killed you.

	DIE_BRIGAND_AMBUSH		;killed in the brigand ambush
	DIE_ENDGAME_REST		;resting during the end-game is a death.
	DIE_ENDGAME_GREEDY		;came back to the Brigands Lair after defeatign them to try and get more loot!
	DIE_ENDGAME_RINGBELL	;Ring the Doorbell

	DIE_ENDGAME_ARROWS		;Brigand Courtyard Arrows kill ya
	DIE_ENDGAME_ANTWERP		;
	DIE_ENDGAME_PORCUPINE
	DIE_ENDGAME_RUGTRAP
	DIE_ENDGAME_RUGTRAP2
	DIE_ENDGAME_DIVE

	DIE_ENDGAME_BRIGANDS_SOUTH
	DIE_ENDGAME_BRIGANDS_WEST
	DIE_ENDGAME_BRIGANDS_EAST
	DIE_ENDGAME_OUTNUMBERED
	DIE_ENDGAME_STOOGES1
	DIE_ENDGAME_STOOGES2
	
	DIE_ENDGAME_NAP
	DIE_ENDGAME_FALLGUY1
	DIE_ENDGAME_DOORFLAT
	
	DIE_ENDGAME_LEADER
	DIE_ENDGAME_TOOLONG

	
)

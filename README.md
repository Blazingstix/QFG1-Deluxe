## QFG1Deluxe

Custom mod of QFG1 EGA, replacing death screens with a Retry feature, and keeping track of which deaths you've experiences.
Beta features include adding a second playable character, and new rooms/puzzles.
To enable BETA features, place the corresponding file in the game folder (the file can be empty):
 - 2AVATARS : Enables two avatars to pick from: Male or Female
 - 3AVATARS : Enabled three avatars to pick from: Male, Female, or Other.
			  Other is an inverted colour for debugging. This could conceivably be replaced with 
			  any other sprites in the future... an elf, an ogre, whatever.
 - SHEDOPEN : Unlocks the doors to the Town shed.  Note, the room is a very crude drawing.
 			  Technically, this isn't even in BETA, more like ALPHA.

## Changelog

v1.310
 - fixed original QFG1 bug with casting spells accidentally casting twice in some circumstances 
 - fixed missing negative points for killing bear by daggers or magic. 
 - added support for multiple avatars (choose Male, Female or Other)
    o   To enable, there must be the file 2AVATARS or 3AVATARS present in the game folder.
		Female is WIP, and only has a single replaced graphic
		Other is a Proof of Concept, and is for debug testing.
 - added * next to Zap on inventory screen when your weapon is charged (like in QFG2).
 - added the beginnings of the town shed interior. To enable, there must be a 
   file called SHEDOPEN present in the game folder.
 - added additional game flags exported in your character save (at game end)
 - added right-click look on Maltese Falcon in the Brigand Leader's room.

v1.301
 - fixed a fatal bug in the goblin ambush area
 - made minor tweaks to several areas. They shouldn't affect gameplay.
	(rock throwing script, dagger throwing script, flame dart throwing script, 
	antwerp following script, brigand courtyard script, dag-nab-it script, 
	healer's hut tree script)

v1.300
Here’s a quick Changelog:
-	All deaths replaced with a Retry dialog
	o	The Retry will revert only the relevant bits back to just before the 
		death, so as to let the hero try again.
	o	If the hero got killed, I restore some health (sometimes all health… 
		can’t remember which gets what treatment)
	o	Any skill gained between dying and retying are kept (I’m taking a cue 
		from EarthBound here… it is a game, afterall, the point of which is 
		primarily to have fun. Building skills is incidental to that… and 
		anyway, if you died because your skills were too low, you need them 
		higher eventually anyway)
	o	Date/Time are not reset except in certain time-critical instances.  
		Conceivable, if you die hundreds of times in a room, days could pass, 
		but that’s such a small problem, with no real consequences I don’t worry 
		about it
-	Added a new screen that shows how many deaths total, and how many unique deaths 
	you’ve had.
	o	It also lists the first 32 deaths (in a predefined order), with the most 
		recent shown 1st in red.
-	Restored a unique death in the Sheriff Break-In house that was never shown due to 
	an original script bug
	o	If you crack the safe 3 times in a row, the game accuses you of “Power 
		Gaming” to build up your skills, and wakes up the Sheriff.
-	Restored an unused instant-death and animation in the 2nd room of Fred the Troll’s 
	cave. He sneaks up on you, and the game does a Luck Roll to see if you get killed 
	instantly, or have to fight him.
-	Added a text title for the Sheriff’s Wife’s single line of dialog (if you enter 
	her room). She always had a name in the source code, but I don’t believe it was 
	ever visible in game before now.
-	Slightly tweaked the meetings with Baba Yaga to show the sword cursor when she’s 
	expecting you to reply.
-	Changed Date/Time shortcut to ^d to be consistent with QFG2
-	Modified Razzle Dazzle slightly (show different title bar, expanded Alt-S details, 
	let Alt-X ask what amount the skills should be set to.
-	Added a SillyClowns toggle. It modifies the Date/Time message a bit.
-	Changed in-game status bar text back to So You Want To Be A Hero, as it was in the 
	original release of HQ1

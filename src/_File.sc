;;; Sierra Script 1.0 - (do not remove this comment)
;;;;
;;;;  FILE.SC
;;;;
;;;;  (c) Sierra On-Line, Inc, 1988, 1990
;;;;
;;;;  Authors: Jeff Stephenson, Mark Wilden
;;;;  Updated:
;;;;     Brian K. Hughes
;;;;     August 11, 1992
;;;;
;;;;  The File class allows you to open and write to a file on disk. 
;;;;  This is useful for logging user input for which you have no
;;;;  response in the development or beta-test phase, writing utilities
;;;;  which allow you to position Actors on a picture and then write
;;;;  out the coordinates, etc.
;;;;
;;;;  Classes:
;;;;     File

(script# FILE) ;FILE = 993
(include system.sh) (include sci2.sh) (include game.sh)
(use _System)


(class File of Object
	(properties
		handle 0
		name "gamefile.sh"
	)
	
	(method (dispose)
		(self close:)
		(super dispose:)
	)
	
	(method (showStr where)
		(Format where FILE 0 name)
	)
	
	(method (open mode)
		;; Open the file.  'mode' is the mode in which to open the file:
		;;    mode =
		;;       fAppend     Appends to end of file.  Default if 'mode'
		;;                   is not specified.									0 = fAppend
		;;       fRead       Positions at start of file.						1 = fRead
		;;       fTrunc      Truncates the file to zero length when opened.		2 = fTrunc
		;;
		;; open:returns the File ID if file is opened successfully, NULL otherwise.

		(= handle
			(switch argc
				(0 (FOpen name fAppend))
				(1 (FOpen name mode))
				(else NULL)
			)
		)
		(if (== handle -1) (= handle NULL))
		(return (if handle self else NULL))
	)
	
	(method (write str &tmp i)
		;; Write the string pointed to by 'str' to the file.  Multiple strings
		;; may be sent in one call.

		;Open the file if it is not presently open.
		(if (not handle) (self open:))

		;Multiple writes accepted.
		(if handle
			(= i 0)
			(while (< i argc)
				(FPuts handle [str i])
				(++ i)
			)
		)
	)
	
	(method (read str len)
		;; Read len bytes from a file into str
		
		(if (!= argc 2) (return NULL))
		
		;Open the file if it is not presently open.
		(if (not handle) (self open: fRead))
		(return (if handle (FGets str len handle) else NULL))
	)
	
	(method (close)
		;; Close the file.  This makes sure that all writes which were made
		;; actually go to the disk file.

		(if handle (FClose handle) (= handle NULL))
	)
)

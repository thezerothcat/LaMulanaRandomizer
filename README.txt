WARNING: This is an alpha release! There are still a lot of missing features and possible bugs. Use at your own risk.

When using the UI jar for the first time, MAKE SURE you have clean, non-modified copies of script.rcd and script_code.dat in your La-Mulana directory! Failure to do so often results in unwinnable seeds.

How to use:
-----------------------------
The jar can be run from anywhere, and will look for common Windows install paths for La-Mulana. If it doesn't find a correct install path, you can fill yours in.

The jar will look for files called script.rcd.bak and script_code.dat.bak in the current directory. If files are not found, backup copies will be created from your La-Mulana directory, so you'll want to make sure those are unmodified the first time you run the jar. If the script.rcd file is found to be modified, the program will write a warning to the log file and exit.

There are a few configurations available in the UI. Once things are configured to your liking, click on the Apply button. At this point, a subdirectory will be created for the current seed number containing files for the randomized game as well as items.txt (spoiler log for the seed). The files will additionally be copied into your La-Mulana directory.

If you want to play the non-randomized game, there's a Restore button in the UI which should copy the backup files back into your La-Mulana directory.


1.6:
-----------------------------
Added non-categorized shop randomization. Added remaining shops, Diary, Mulana Talisman, Book of the Dead, Key Sword, and xmailer.exe to randomization. Added the ability to generate seeds where not all items are accessible. UI improvement work done by Goost.


1.5:
-----------------------------
Seed generation settings are now saved to file and automatically reused the next time you run the program. There are also some additional items/item types randomized, including a partial randomization of Forbidden Treasure/Bathing Suit by popular request, and a handful of bugfixes.


1.4:
-----------------------------
Updates for ankh jewel logic. Several requirements fixes and a few new items randomized.


1.3:
-----------------------------
Shop randomization exists! There's still more to be done here, but many shops (and their items) are now part of the randomization pool. Also fixed Cog of the Soul chest bug, prevented Ankh Jewels from spawning in some problematic chests, removed Graveyard of the Giants alternate shop, fixed a bug that could break the Mini Doll puzzle, and updated some item and shop requirements.


1.2:
-----------------------------
Randomizer now modifies two files, and can copy them to your La-Mulana directory for you or restore them from backups.


1.1:
-----------------------------
Adds some basic UI and file validation for the randomizer jar, so it doesn't have to be run from command-line.


1.0:
-----------------------------
* Not all items can be randomized yet, but most blue chests and free-floating items are randomized.
* Some items from chests will not be randomized; this is typically due to a known special case for that item which needs to be resolved before the item can safely be shuffled.
* Ankh Jewel locks are possible. To reduce the likelihood of encountering one, 4 ankh jewels will be available with no requirements (matching the non-randomized game)
* There are a number of items made initially accessible to speed up resolution of random seeds. In future versions, this will be made customizable.


Plans for future releases:
-----------------------------
* Better handling for ankh jewel locks.
* Randomization of more types of items, special-case items, which chests are coin chests vs item chests, etc.
* Item requirements updates (better boss requirements, more glitch-enabled requirements, requirements involving damage boosting, etc.)
* Shop randomization expansion (randomizing ammo, shuffling items in the place of weights/ammo)
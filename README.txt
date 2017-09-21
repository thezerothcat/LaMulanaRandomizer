Releases page:
-----------------------------
https://github.com/thezerothcat/LaMulanaRandomizer/releases


Known issues:
-----------------------------
https://github.com/thezerothcat/LaMulanaRandomizer/issues


How to use:
-----------------------------
When using the UI jar for the first time, MAKE SURE you have clean, non-modified copies of script.rcd and script_code.dat in your La-Mulana directory! Failure to do so often results in unwinnable seeds.

The jar can be run from anywhere, and will look for common Windows install paths for La-Mulana. If it doesn't find a correct install path, you can fill yours in.

The jar will look for files called script.rcd.bak and script_code.dat.bak in the current directory. If files are not found, backup copies will be created from your La-Mulana directory, so you'll want to make sure those are unmodified the first time you run the jar. If the script.rcd file is found to be modified, the program will write a warning to the log file and exit.

There are a few configurations available in the UI. Once things are configured to your liking, click on the Apply button. At this point, a subdirectory will be created for the current seed number containing files for the randomized game, which will automatically be copied into your La-Mulana directory. Additonal files will also be created: items.txt and shops.txt (spoiler logs for the seed), and excluded_items.txt (list of items considered out of reach, if allowing some items to remain inaccessible)

Keep in mind that the randomizer may take some time to generate a seed (some settings may lead to faster/slower generation). You'll see it automatically update the seed number entry with a new number when it's finished.

If you want to play the non-randomized game, there's a Restore button in the UI which should copy the backup files back into your La-Mulana directory.


Things to know:
-----------------------------
* The randomizer does not expect you to fail the Chain Whip, Flail Whip, or Angel Shield puzzles, so don't do it!
* A door has been added to allow access to the old Spring in the Sky shop after transforming it.
* A door has been added to the bottom of Endless Corridor to allow access to the original Shrine of the Mother after defeating all 8 guardians (still requires doing the puzzle to enter the Shrine).
* If your Mulana Talisman is replaced by Talisman or Diary, you can get Mulana Talisman back at any time by saving and reloading your game.
* Xelpud has conversations based on collecting Talisman and Diary, but you are no longer required to equip the item.
* The Shrine of the Mother map is the only required map. If you find this item in a shop, it will be called Shrine Map instead of just Map, and if you pick it up elsewhere, a shell horn sound will play (regardless of whether or not you've collected shell horn).
* You are not expected to have the Scalesphere before doing Spring in the Sky (the non-randomized game had it at the top of the area).
* It's possible to purchase actual subweapons from shops. They look the same as ammo for the subweapon, but are not sold out.
* "Lower" boss difficulty adds health/weapon requirements to some of the bosses.
* Initially accessible locations include 9 shops, 26 non-shop locations, and an additional 6 coin chests if coin chests are randomized.


1.12:
-----------------------------
Added in-game Japanese language support (randomizer UI still needs translation). Merged more UI improvements from Goost. Assorted minor requirement fixes.


1.11:
-----------------------------
Added an option to start the game in hardmode. Added a backup door for the shop in Spring in the Sky, which will appear after it's transformed. Made a couple of other fixes for reported issues.


1.10:
-----------------------------
Some coin chests can now contain items configured to be initially accessible, as well as Big Brother's shop. Added a couple of minor config options and started backing up configuration settings for more reliable debugging.


1.9:
-----------------------------
Coin chests can now be randomized in addition to item chests. You can tell whether you'll get coins or an item from a chest based on its color.


1.8:
-----------------------------
Added Dimensional Corridor Sacred Orb to randomization. Added boss difficulty, although only a couple of bosses have difficulty requirements right now. Added some anti-requirements (mostly around getting items in untransformed Shrine). Changed some randomization logic - full-random grail should be less of a softlock risk, although things are probably more restrictive than they need to be for some areas.


1.7:
-----------------------------
Moved steam version pots to the same locations as in 1.3 for consistency with glitch requirements. Added some glitch requirements and some damage-boost requirements.


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
* Area transition randomiztion
* Ability to replace some items with empty chests (or possibly weights).
* Better handling for ankh jewel locks.
* Item requirements expansion (configurable boss requirements, more glitch-enabled requirements, requirements involving damage boosting, etc.)
* Shop randomization expansion (randomizing ammo when shuffling items only)
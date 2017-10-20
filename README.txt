Please see the wiki page on github for information on how to use the randomizer, and other things you may need to know about it.
https://github.com/thezerothcat/LaMulanaRandomizer/wiki


1.18:
-----------------------------
TextTrax randomization is now configurable for those who want to record shops, and recorded shops now include prices. Added more new settings: one to make all chests use coin chest graphics, one to make all maps appearing in chests give you a weight instead of a map, and one to automatically unlock grail warps by walking in front of grail tablets.


1.17:
-----------------------------
Some items can now be placed on the Surface instead of their non-random location. Removed the option to not randomize shops. Damage-boosting logic updates. Misc requirement fixes.


1.16:
-----------------------------
UI changes. Randomizer window is generally smaller, with tabs for a number of subsections. Translation handling refactored for easier adding/changing of messages.


1.15:
-----------------------------
Fixed a mantra-related bug causing the Key Sword to transform earlier than intended. Made Cog of the Soul tablet accessible without solving all the Illusion puzzles.


1.14:
-----------------------------
Reduced some shop prices - 1.13 changes seemed a bit too excessive. Mantras can now be recited in any order. Isis' Pendant chest accessible without visiting the fairy queen. Fixes for some saved settings.


1.13:
-----------------------------
Moved some info from the readme, as well as other useful randomizer info, onto github using the site's wiki feature. When purchasing a Sacred Orb from a shop, that shop will now transform as soon as you leave (partial fix for orb shop bug). Illusion Bomb chest turned into a coin chest and randomized. Requirement changes, including better ammo handling. Revisited shop prices.


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
Seed generation settings are now saved to file and automatically reused the next time you run the program. There are also some additional items/item types randomized, including a partial randomization of Forbidden Treasure/Provocative Bathing Suit by popular request, and a handful of bugfixes.


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
Please see the wiki page on github for information on how to use the randomizer, and other things you may need to know about it.
https://github.com/thezerothcat/LaMulanaRandomizer/wiki

Note: The randomizer is not yet compatible with Java 9+, so you will need Java 8 in order to run it.

1.39:
-----------------------------
Ability to start the game with a subweapon instead of a main weapon, although using quickswap keys will cause the game to crash.

Ring has been added to configurable items. A new setting has been added to spawn a ladder after defeating Ushumgallu in Dimensional Corridor for those who don't like having to do the damage boost.

Custom placement now allows specification of starting weapon and items, mantra skip/automatic upgraded Key Sword, and placement of Medicine of the Mind instead of Vessel.

Assorted bugfixes.

1.38:
-----------------------------
Mostly bugfixes.

1.37:
-----------------------------
Added the ability to declare custom logic. Fixed some logic problems from 1.35 and 1.36.

1.36:
-----------------------------
Expanded support for custom item placement.

1.35:
-----------------------------
Items can now be placed in specific locations through the use of a configuration file. See the wiki for more information.

Re-added support for non-100% access logic when not using randomly removed items.

1.34:
-----------------------------
Added an option to randomize Dracuet's shop.

1.33:
-----------------------------
Added an option to fully randomize the Provocative Bathing Suit.

1.32:
-----------------------------
Anti-softlock insurance around transforming Shrine of the Mother. Assorted logic updates.

1.31:
-----------------------------
Bugfixes.

1.30:
-----------------------------
Re-randomized a coin chest that was unintentionally excluded from randomization. Merged some of Worse Than You's requirement updates.


1.29:
-----------------------------
A new option has been added to start the game already knowing ancient La-Mulanese, allowing the translation tablets to be skipped.


1.28:
-----------------------------
The option to start with Holy Grail, Hermes' Boots, and mirai.exe/Future Development Company has been replaced with individual toggles for configurable items. The new setting will replace the "Original Location" and "Surface" randomization options and now includes the items which had neither option. Item randomization settings have been renamed for clarification.

Spoiler logs may now be translated to Japanese based on language settings. Some English log files have been updated as well; coin chest and trap item naming has been updated, and shop names now reference the in-game NPCs.


1.27:
-----------------------------
Random starting weapons. This will replace the save in your first slot, and that save must be used to apply the random starting weapon. Two items in Temple of Moonlight have been removed from the initial chests pool to account for the fact that access to the Temple of Moonlight pyramid requires Whip or Axe. Also note that the regular Whip cannot be collected due to in-game limitations and will be replaced by the Chain Whip.

Added the ability to receive Holy Grail, Hermes' Boots, and mirai.exe/Future Development Company immediately at the start of the game for racing purposes. If these items are selected to be in a special location, they will instead be randomized normally and then treated as a removed item (replaced with a chest containing weights or coins).

Added the ability to randomize which chests are cursed.


1.26:
-----------------------------
The April Fools' Day build. Included random starting weapons and configurable starting items (see 1.27 for more details).


1.25:
-----------------------------
Ability to randomize the fake Ankh Jewel, fake Sacred Orb, exploding chest in Gate of Illusion, and empty chest in Graveyard of the Giants. The fake items and empty chest will spawn bats, and the exploding chest will explode. Removed items in chests may now randomly give 10 coins instead of a weight.


1.24:
-----------------------------
More bugfixes.


1.23:
-----------------------------
Bugfixes.


1.22:
-----------------------------
Ability to remove a random number of random items from the game to increase challenge. This replaces the old non-100% setting.


1.21:
-----------------------------
Randomizer version and settings string can now be viewed in your inventory by checking the description of the MSX2.


1.20:
-----------------------------
Ability to remove Spaulder from the game.


1.19:
-----------------------------
Seed-sharing features added by Goost.


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
La-Mulana Remake Randomizer v1.0!

Data to be randomized will be read in from files.

Unless you're running this from IDE, make sure you have a clean copy of script.rcd in your La-Mulana directory.

How to use:
-----------------------------
The jar can be run from anywhere, and will look for common Windows install paths for La-Mulana.

When run, the jar will generate random item placements and output the following files into the current directory:
* script.rcd (this should be copied to your La-Mulana directory; make sure to back up your existing one first)
* items<seed number>.txt (spoilers for which items are expected in which locations)
* a "log" file (should be safe to ignore/delete)

You may provide additional command-line arguments for customization:
* -g will enable glitched requirements to be used for access.
* -s<number> (for example, -s2) will seed the random number generator with the provided number; if not provided, the default is 0
* -dir<directory> can be used to specify the La-Mulana directory, if your directory isn't supported by default (example: -dirE:\Steam\steamapps\common\La-Mulana)
* -n<item name without spaces> can be used to force items to remain in their original location; note that some items will do so regardless of configuration (or may be randomized into their original location by chance)
* -ng and -ngrail are shortcuts for making the Holy Grail appear in its original location
* -i<item name without spaces> can be used to force items to be randomized into initially-accessible locations; be careful not to add too many, since it's not checked - I believe it's possible to add up to 18 at this time, but I haven't verified)
* -ig and -igrail are shortcuts for making the Holy Grail initially available
* -igrapple is a shortcut for making the Grapple Claw initially available
* -isw will guarantee you a random subweapon made available initially, provided there's at least one subweapon that hasn't been disabled from randomization

Note that, in order to make Ankh Jewels, Maps, or Sacred Orbs initially available or non-randomized, you'll need to know which one you're putting in the chest (until a future update, at least). See all_items.txt for the names I'm using to reference these items.

1.0:
-----------------------------
* Not all items can be randomized yet, but most blue chests and free-floating items are randomized.
* Some items from chests will not be randomized; this is typically due to a known special case for that item which needs to be resolved before the item can safely be shuffled.
* Ankh Jewel locks are possible. To reduce the likelihood of encountering one, 4 ankh jewels will be available with no requirements (matching the non-randomized game)
* There are a number of items made initially accessible to speed up resolution of random seeds. In future versions, this will be made customizable.


Plans for future releases:
-----------------------------
* Randomization of non-chest items, special-case items, which chests are coin chests vs item chests, etc.
* Ability to configure items that should be available with no requirements.
* Handling for ankh jewel locks.
* Shop randomization (preserving the ratio of weights & ammo to "permanent" items, but not necessarily the ratio of weights to ammo)

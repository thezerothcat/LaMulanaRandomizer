La-Mulana Remake Randomizer v1.0.0!

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


1.0.2:
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

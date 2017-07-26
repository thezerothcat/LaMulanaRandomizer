WARNING: This is an alpha release! There are still a lot of missing features and possible bugs. Use at your own risk.

When using the UI jar for the first time, MAKE SURE you have a clean copy of script.rcd in your La-Mulana directory! Failure to do so often results in unwinnable seeds.

How to use:
-----------------------------
The jar can be run from anywhere, and will look for common Windows install paths for La-Mulana. If it doesn't find a correct install path, you can fill yours in.

The jar will look for a file called script.rcd.bak in the current directory. If none is found, it will create a copy from your La-Mulana directory, so you'll want to make sure that's an unmodified copy for first run. If the script.rcd file is not found to be safe, the program will write a warning to the log file and exit.

There are a few configurations available in the UI. Once things are configured to your liking, click on the Generate button and your configs will be used to create a subdirectory named according to your random seed. The directory will contain script.rcd which should be copied into your La-Mulana directory, and items.txt which contains spoilers for which items can be found in each location.

To play the randomized game, copy the provided script.rcd for your seed into <La-Mulana directory>/data/mapdata, replacing the script.rcd that's already present.

The command-line arguments from previous releases will not work with the jar, but may be added back in for a later release.


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
* Randomization of non-chest items, special-case items, which chests are coin chests vs item chests, etc.
* Ability to configure items that should be available with no requirements.
* Handling for ankh jewel locks.
* Shop randomization (preserving the ratio of weights & ammo to "permanent" items, but not necessarily the ratio of weights to ammo)

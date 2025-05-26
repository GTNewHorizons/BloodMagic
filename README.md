# Blood Magic: Alchemical Wizardry

### Gruesome? Probably. Worth it? Definitely!

## Fork

This mod was forked by the GT:NH Team to continue the development of the 1.7.10 branch.

## Information

Have you ever picked up a magic mod for Minecraft, and thought that it was too tame? Was there not enough danger involved when creating your next high-tech gadget? Bored with all of those peaceful animals just staring at you without a care in the world? Well then, I am glad you came here!

Blood Magic is an arcane art that is practiced by mages who attempt to gather a vast amount of power through utilizing a forbidden material: blood. Even though it does grant a huge amount of power, every single action that is performed with this volatile magic can prove deadly. You have been warned.

## Upstream Blood Magic Links
* Github: [Blood Magic](https://github.com/WayofTime/BloodMagic)
* Twitter: [@WayofTime](https://twitter.com/WayofTime)
* Wiki: Found at [FTBWiki](http://ftbwiki.org/Blood_Magic). Most information is still relevant for this fork.
* [Minecraft Forum Thread](http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/1290532-bm)
* [CurseForge](https://www.curseforge.com/minecraft/mc-mods/blood-magic)
* [Donate to WayofTime on Paypal](https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=J7SNY7L82PQ82)
* [Donate to WayofTime on Patreon](https://www.patreon.com/BloodMagic)

## GT:NH Links
* Wiki: Hosted on [Miraheze](https://wiki.gtnewhorizons.com/wiki/Blood_Magic). Contains both fork-specific and pack-specific information. It is currently very incomplete, so feel free to contribute!
* [Discord](https://discord.gg/gtnh): Talk to other players about Blood Magic and any other mods present in GT:NH.
* [Releases](https://github.com/GTNewHorizons/BloodMagic/releases)

## Issue Reporting

**DO NOT** report issues encountered when running this fork to upstream. Report issues for this mod on [the GT:NH issue tracker](https://github.com/GTNewHorizons/GT-New-Horizons-Modpack/issues/new/choose).

## Development Setup

1. Fork this project to your own Github repository and clone it to your desktop.
2. Navigate to the directory you cloned to. Open a command window there and run (if you use Eclipse) `gradlew eclipse` or (if you use IDEA) `gradlew idea`. 
3. This process will setup [Forge](http://www.minecraftforge.net/forum/), your workspace, and all dependencies.
4. Open the project in your IDE of choice.

[Setup video](https://www.youtube.com/watch?v=8VEdtQLuLO0&feature=youtu.be) by LexManos. For more information, refer to the [Forge Forums](http://www.minecraftforge.net/forum/index.php/topic,14048.0.html).

## Custom Builds

### Custom builds are unsupported. If you have an issue using a build not by GT:NH, it is not guaranteed that you will get support.

#### How to make a custom build:

1. Clone directly from this repository to your desktop.
2. Navigate to the directory you cloned to. Open a command window there and run `gradlew build`
3. Once it completes, your new build will be found at `../build/libs/BloodMagic-*.jar`. You can ignore the `api`, `dev`, and `sources` jars.

## License

![CCA4.0](https://licensebuttons.net/l/by/4.0/88x31.png)

Blood Magic: Alchemical Wizardry by WayofTime is licensed under a [Creative Commons Attribution 4.0 International License](http://creativecommons.org/licenses/by/4.0/).

## Installation Instructions

This mod requires "Minecraft Forge" in order to operate. It is incredibly easy to download and set up, so you might as well get to it!

1. Download [Forge](https://files.minecraftforge.net/net/minecraftforge/forge/index_1.7.10.html). "Recommended" and "Latest" will both yield the same files. Download "Universal" to install it manually. Forge also has an "Installer" option, so all you need to do is launch that program and it will make a lovely Forge profile.
2. Download the latest version of Blood Magic from the [Releases Page](https://github.com/GTNewHorizons/BloodMagic/releases) (ignore versions that end with `-pre` if you are not looking to test in-development features).
3. Place the mod in the `.minecraft/mods` folder. If you use the official Minecraft launcher from Mojang, check [this page](https://minecraft.wiki/w/.minecraft) on the Minecraft Wiki to find it. If you use a different launcher, check its interface for a "Minecraft Folder" button, "View Mods" button, or something similar.

## FAQ

**Q**: What has changed in the GT:NH fork compared to the final 1.7.10 version released by WayofTime?

**A**: Many things, including:
* Better configuration for meteors summoned by the Mark of the Falling Tower ritual. Run the mod once and check config/BloodMagic/meteors/README for a guide on how to edit the default ones and create your own. The old ModTweaker system should still work, but this new system is far more powerful and configurable for pack developers.
* [Buffs](https://github.com/GTNewHorizons/BloodMagic/pull/15) to various Blood Altar upgrade runes.
* The [Rune of Quickness](https://github.com/GTNewHorizons/BloodMagic/pull/51), a new rune which lowers the Blood Altar's delay between crafts.
* [Improvements](https://github.com/GTNewHorizons/BloodMagic/pull/85) to the Orchestra of the Phantom Hands.
* [Buffs](https://github.com/GTNewHorizons/BloodMagic/pull/83) to the Incense Crucible.
* NEI Handlers for the [Blood Altar's Structure](https://github.com/GTNewHorizons/BloodMagic/pull/81) (requires [StructureLib](https://github.com/GTNewHorizons/StructureLib) and [BlockRenderer6343](https://github.com/GTNewHorizons/BlockRenderer6343)), [Meteors](https://github.com/GTNewHorizons/BloodMagic/pull/17), and the [Alchemic Calcinator](https://github.com/GTNewHorizons/BloodMagic/pull/87).
* Configurable LP costs for many items and rituals.
* Many other bug fixes, new localizations, and quality of life improvements!

**Q**: My weak blood orb doesn't show my current LP! Fix it please.

**A**: You need a Divination Sigil to view your essence. It does other things, too, so it is worth it!

**Q**: Why am I dying so much?

**A**: It might be a good idea to make sure that you have enough essence to do a task. If you don't have enough essence for, say, an imperfect ritual or a sigil, it will take it out of your health. If your health reaches 0... Well, you don't have to be a genius to see what would happen. Note that Rituals will only stop working and give you nausea, not kill you, so you don't have to worry about them quite as much.

**Q**: Waffles?

**A**: Waffles!

**Q**: Where is x? When I watched spotlight "w", it had an item called x. Don't you need x to make y, before you can create z?

**A**: It might be wise to look at a spotlight made for 1.7.10 (especially one for GT:NH), or check the changelogs. Versions of Blood Magic for more recent versions of Minecraft have some very different mechanics, which are not present in this version of the mod. 

**Q**: Isn't that armour ...

**A**: Yes, the bound armour and tool textures are from the mod EE2. Pahimar and WayofTime are good friends, and Pahimar has given his express permission for it, so need not worry!
[Proof](https://twitter.com/Pahimar/status/453590600689139712), [Archive](https://web.archive.org/web/20191208030719/https://twitter.com/Pahimar/status/453590600689139712).

**Q**: I've just had an amazing idea! Why not add an in-game book just like the Thaumonomicon?

**A**: The Sanguine Scientiem can be created by dropping a Mail Order Catalogue (craftable with [Guide-API](https://www.curseforge.com/minecraft/mc-mods/guide-api) installed). Some of the formatting is off, and it has not been updated with all the information specific to this fork, so readers beware.

**Q**: Why do I not have a Sacrificial Orb? It's only showing up as a knife!

**A**: This is a config option. The person you saw with an orb had a config that changed the knife to an orb. The orb and knife function exactly the same way, but you can change the way it looks in the configs by looking for the "IDontLikeFun" option.

**Q**: When I respawn, I come back with the "Soul Fray" potion effect and I don't create as much LP with the Sacrificial Dagger/Orb. What's happening?

**A**: This is due to the config option "RespawnWithDebuff", which was made to combat Zerg Rushing. Instead of dying and respawning again and again, why not look into making an [Incense Crucible](https://ftbwiki.org/Incense_Crucible) to boost the LP from your self-sacrifices, brewing a [refillable healing potion](https://ftbwiki.org/Potion_Flask_(Blood_Magic)) with the Alchemic Chemistry Set, or finding some villagers to show your [Dagger of Sacrifice](https://ftbwiki.org/Dagger_of_Sacrifice) to?
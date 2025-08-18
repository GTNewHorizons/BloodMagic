package WayofTime.alchemicalWizardry.client.nei;

import net.minecraft.nbt.NBTTagCompound;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import cpw.mods.fml.common.event.FMLInterModComms;

public class IMCForNEI {

    public static void IMCSender() {
        sendHandler("alchemicalwizardry.meteor", "AWWayofTime:masterStone", 181, 1);
        sendHandler("alchemicalwizardry.calcinator", "AWWayofTime:blockAlchemicCalcinator", 55, 5);
        sendCatalyst("alchemicalwizardry.meteor", "AWWayofTime:masterStone");
        sendCatalyst("alchemicalwizardry.bindingritual", "AWWayofTime:masterStone");
        sendCatalyst("alchemicalwizardry.calcinator", "AWWayofTime:blockAlchemicCalcinator");
        // Blood Altar Progression
        sendInfoPage(
                "AWWayofTime: Altar,sacrificialKnife",
                "nei.infopage.bloodAltar.1." + (AlchemicalWizardry.wimpySettings ? "b" : "a"));
        sendInfoPage("AWWayofTime: Altar", "nei.infopage.bloodAltar.2");
        sendInfoPage("AWWayofTime: Altar,bucketLife", "nei.infopage.bloodAltar.3");
        sendInfoPage("AWWayofTime: Altar,weakBloodOrb", "nei.infopage.bloodAltar.4");
        sendInfoPage(
                "AWWayofTime: Altar,apprenticeBloodOrb|<AWWayofTime:AlchemicalWizardrybloodRune>",
                "nei.infopage.bloodAltar.5");
        sendInfoPage("AWWayofTime: Altar,magicianBloodOrb|<minecraft:glowstone>", "nei.infopage.bloodAltar.6");
        sendInfoPage("AWWayofTime: Altar,masterBloodOrb,largeBloodStoneBrick", "nei.infopage.bloodAltar.7");
        sendInfoPage("AWWayofTime: Altar,archmageBloodOrb|minecraft:beacon", "nei.infopage.bloodAltar.8");
        sendInfoPage("AWWayofTime: Altar,transcendentBloodOrb|<AWWayofTime:blockCrystal>", "nei.infopage.bloodAltar.9");
        // Incense
        sendInfoPage("AWWayofTime: incense,sacrificialKnife,blockCrucible", "nei.infopage.incense.1");
        sendInfoPage(
                "AWWayofTime: incense,sacrificialKnife,blockCrucible",
                "nei.infopage.incense.2." + (AlchemicalWizardry.wimpySettings ? "b" : "a"));
        sendInfoPage("AWWayofTime: incense,sacrificialKnife,blockCrucible", "nei.infopage.incense.3");
        // Sacrifices
        sendInfoPage("AWWayofTime runeOfSacrifice,daggerOfSacrifice", "nei.infopage.sacrifice.1");
        sendInfoPage("AWWayofTime runeOfSacrifice,daggerOfSacrifice", "nei.infopage.sacrifice.2");
        sendInfoPage("AWWayofTime runeOfSacrifice,daggerOfSacrifice", "nei.infopage.sacrifice.3");
        // Altar Runes
        sendInfoPage("<AWWayofTime:AlchemicalWizardrybloodRune:1>", "nei.infopage.rune.augmentedCapacity");
        sendInfoPage("<AWWayofTime:AlchemicalWizardrybloodRune:2>", "nei.infopage.rune.dislocation");
        sendInfoPage("<AWWayofTime:AlchemicalWizardrybloodRune:3>", "nei.infopage.rune.orb");
        sendInfoPage("<AWWayofTime:AlchemicalWizardrybloodRune:4>", "nei.infopage.rune.superiorCapacity");
        sendInfoPage("<AWWayofTime:AlchemicalWizardrybloodRune:5>", "nei.infopage.rune.acceleration");
        sendInfoPage("<AWWayofTime:AlchemicalWizardrybloodRune:6>", "nei.infopage.rune.quickness");
        sendInfoPage("<AWWayofTime:speedRune>", "nei.infopage.rune.speed");
        sendInfoPage("<AWWayofTime:efficiencyRune>", "nei.infopage.rune.efficiency");
        sendInfoPage("AWWayofTime runeOfSacrifice,daggerOfSacrifice", "nei.infopage.rune.sacrifice");
        sendInfoPage("AWWayofTime runeOfSelfSacrifice,sacrificialKnife", "nei.infopage.rune.selfSacrifice");
        sendInfoPage(
                "<AWWayofTime:AlchemicalWizardrybloodRune>|AWWayofTime runeOfSacrifice,daggerOfSacrifice",
                "nei.infopage.rune.blank");
        // Progression Items
        sendInfoPage(
                "AWWayofTime: masterBloodOrb,largeBloodStoneBrick,weakBloodShard,energySword,alchemyFlask",
                "nei.infopage.bloodShards");
        sendInfoPage(
                "AWWayofTime: masterBloodOrb,demonBloodShard,blockPlinth,blockPedestal,aether,aquasalus,incendium,sanctus,tennebrae|<AWWayofTime:terrae>",
                "nei.infopage.demonShards.1");
        sendInfoPage(
                "AWWayofTime: masterBloodOrb,demonBloodShard,blockPlinth,blockPedestal,aether,aquasalus,incendium,sanctus,tennebrae|<AWWayofTime:terrae>",
                "nei.infopage.demonShards.2");
        sendInfoPage(
                "AWWayofTime: demonBloodShard,blockPlinth,blockPedestal,aether,aquasalus,incendium,sanctus,tennebrae|<AWWayofTime:terrae>",
                "nei.infopage.demonShards.3");
        sendInfoPage(
                "AWWayofTime:bloodMagicBaseItems 28,29|<AWWayofTime:blockCrystal>",
                "nei.infopage.convocationShards");
        // Blood Orb / Soul Network Explanation
        sendInfoPage(
                "AWWayofTime: BloodOrb|<avaritia:orb_armok>|<ForbiddenMagic:EldritchOrb>|<BloodArsenal:transparent_orb>",
                "nei.infopage.bloodOrbs.1");
        sendInfoPage(
                "AWWayofTime: BloodOrb|<avaritia:orb_armok>|<ForbiddenMagic:EldritchOrb>|<BloodArsenal:transparent_orb>",
                "nei.infopage.bloodOrbs.2");
        sendInfoPage(
                "AWWayofTime: BloodOrb|<avaritia:orb_armok>|<ForbiddenMagic:EldritchOrb>|<BloodArsenal:transparent_orb>",
                "nei.infopage.bloodOrbs.3");
        sendInfoPage(
                "AWWayofTime: BloodOrb,divinationSigil|<avaritia:orb_armok>|<ForbiddenMagic:EldritchOrb>|<BloodArsenal:transparent_orb>",
                "nei.infopage.bloodOrbs.4");
        // Rituals
        sendInfoPage("<AWWayofTime:imperfectRitualStone>", "nei.infopage.rituals.weak.1");
        sendInfoPage("<AWWayofTime:imperfectRitualStone>", "nei.infopage.rituals.weak.2");
        sendInfoPage(
                "<AWWayofTime:ritualStone>|AWWayofTime masterStone,itemRitualDiviner,ScribeTool|activationCrystal 0,1",
                "nei.infopage.rituals.1");
        sendInfoPage(
                "<AWWayofTime:ritualStone>|AWWayofTime masterStone,itemRitualDiviner,ScribeTool|activationCrystal 0,1",
                "nei.infopage.rituals.2");
        sendInfoPage(
                "<AWWayofTime:ritualStone>|AWWayofTime masterStone,itemRitualDiviner,ScribeTool|activationCrystal 0,1",
                "nei.infopage.rituals.3");
        // Reagents
        sendInfoPage("AWWayofTime masterStone,blockAlchemicCalcinator", "nei.infopage.reagents.1");
        sendInfoPage("AWWayofTime masterStone,blockAlchemicCalcinator|<AWWayofTime:terrae>", "nei.infopage.reagents.2");
        sendInfoPage("AWWayofTime masterStone,blockAlchemicCalcinator,itemAttunedCrystal", "nei.infopage.reagents.3");
        sendInfoPage(
                "AWWayofTime masterStone,blockAlchemicCalcinator,itemTankSegmenter,itemDestinationClearer",
                "nei.infopage.reagents.4");
        sendInfoPage(
                "AWWayofTime masterStone,blockAlchemicCalcinator,blockReagentConduit,CrystalBelljar",
                "nei.infopage.reagents.5");
    }

    private static void sendHandler(String handlerName, String stack, int height, int recipesPerPage) {
        NBTTagCompound NBT = new NBTTagCompound();
        NBT.setString("handler", handlerName);
        NBT.setString("modName", "Blood Magic");
        NBT.setString("modId", "AWWayofTime");
        NBT.setBoolean("modRequired", true);
        NBT.setString("itemName", stack);
        NBT.setInteger("handlerHeight", height);
        NBT.setInteger("maxRecipesPerPage", recipesPerPage);
        FMLInterModComms.sendMessage("NotEnoughItems", "registerHandlerInfo", NBT);
    }

    private static void sendCatalyst(String handlerName, String stack, int priority) {
        NBTTagCompound aNBT = new NBTTagCompound();
        aNBT.setString("handlerID", handlerName);
        aNBT.setString("itemName", stack);
        aNBT.setInteger("priority", priority);
        FMLInterModComms.sendMessage("NotEnoughItems", "registerCatalystInfo", aNBT);
    }

    private static void sendCatalyst(String handlerName, String stack) {
        sendCatalyst(handlerName, stack, 0);
    }

    private static void sendInfoPage(String filter, String page) {
        NBTTagCompound NBT = new NBTTagCompound();
        NBT.setString("filter", filter);
        NBT.setString("page", page);
        FMLInterModComms.sendMessage("NotEnoughItems", "addItemInfo", NBT);
    }
}

package WayofTime.alchemicalWizardry.client.nei;

import net.minecraft.nbt.NBTTagCompound;

import cpw.mods.fml.common.event.FMLInterModComms;

public class IMCForNEI {

    public static void IMCSender() {
        sendHandler("alchemicalwizardry.meteor", "AWWayofTime:masterStone", 181, 1);
        sendHandler("alchemicalwizardry.calcinator", "AWWayofTime:blockAlchemicCalcinator", 55, 5);
        sendHandler("alchemicalwizardry.rituals", "AWWayofTime:ritualDiviner", 181, 5);
        sendCatalyst("alchemicalwizardry.meteor", "AWWayofTime:masterStone");
        sendCatalyst("alchemicalwizardry.bindingritual", "AWWayofTime:masterStone");
        sendCatalyst("alchemicalwizardry.rituals", "AWWayofTime:ritualDiviner");
        sendCatalyst("alchemicalwizardry.calcinator", "AWWayofTime:blockAlchemicCalcinator");
    }

    private static void sendHandler(String handlerName, String stack, int height, int recipes) {
        NBTTagCompound NBT = new NBTTagCompound();
        NBT.setString("handler", handlerName);
        NBT.setString("modName", "Blood Magic");
        NBT.setString("modId", "AWWayofTime");
        NBT.setBoolean("modRequired", true);
        NBT.setString("itemName", stack);
        NBT.setInteger("handlerHeight", height);
        NBT.setInteger("maxRecipesPerPage", recipes);
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
}

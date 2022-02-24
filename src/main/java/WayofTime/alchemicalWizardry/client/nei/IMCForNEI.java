package WayofTime.alchemicalWizardry.client.nei;

import cpw.mods.fml.common.event.FMLInterModComms;
import net.minecraft.nbt.NBTTagCompound;

public class IMCForNEI {
    public static void IMCSender() {
        setNBTAndSend("WayofTime.alchemicalWizardry.client.nei.NEIMeteorRecipeHandler", "AWWayofTime:masterStone", 130);
    }

    private static void setNBTAndSend(String handlerName, String aBlock, int height) {
        NBTTagCompound NBT = new NBTTagCompound();
        NBT.setString("handler", handlerName);
        NBT.setString("modName", "Blood Magic");
        NBT.setString("modId", "AWWayofTime");
        NBT.setBoolean("modRequired", true);
        NBT.setString("itemName", aBlock);
        NBT.setInteger("handlerHeight", height);
        NBT.setInteger("maxRecipesPerPage", 1);
        FMLInterModComms.sendMessage("NotEnoughItems", "registerHandlerInfo", NBT);
    }
}

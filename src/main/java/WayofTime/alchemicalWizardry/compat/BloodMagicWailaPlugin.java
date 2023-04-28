package WayofTime.alchemicalWizardry.compat;

import WayofTime.alchemicalWizardry.common.tileEntity.TEAltar;
import WayofTime.alchemicalWizardry.common.tileEntity.TEMasterStone;
import WayofTime.alchemicalWizardry.common.tileEntity.TETeleposer;
import WayofTime.alchemicalWizardry.common.tileEntity.TEWritingTable;
import cpw.mods.fml.common.event.FMLInterModComms;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.IWailaRegistrar;

public class BloodMagicWailaPlugin {

    public static final String WAILA_CONFIG_ALTAR = "bm.bloodAltar";
    public static final String WAILA_CONFIG_TELEPOSER = "bm.teleposer";
    public static final String WAILA_CONFIG_RITUAL = "bm.ritualController";
    public static final String WAILA_CHEMISTRY_SET = "bm.chemistrySet";

    public static void callbackRegister(IWailaRegistrar register) {
        final IWailaDataProvider wailaProvider = new BloodMagicWailaProvider();

        register.registerBodyProvider(wailaProvider, TEAltar.class);
        register.registerNBTProvider(wailaProvider, TEAltar.class);
        register.addConfig("Blood Magic", WAILA_CONFIG_ALTAR, true);

        register.registerBodyProvider(wailaProvider, TETeleposer.class);
        register.addConfig("Blood Magic", WAILA_CONFIG_TELEPOSER, true);

        register.registerBodyProvider(wailaProvider, TEMasterStone.class);
        register.registerNBTProvider(wailaProvider, TEMasterStone.class);
        register.addConfig("Blood Magic", WAILA_CONFIG_RITUAL, true);

        register.registerBodyProvider(wailaProvider, TEWritingTable.class);
        register.registerNBTProvider(wailaProvider, TEWritingTable.class);
        register.registerTailProvider(wailaProvider, TEWritingTable.class);
        register.addConfig("Blood Magic", WAILA_CHEMISTRY_SET, true);

    }

    public static void init() {
        FMLInterModComms.sendMessage("Waila", "register", BloodMagicWailaPlugin.class.getName() + ".callbackRegister");
    }
}

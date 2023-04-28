package WayofTime.alchemicalWizardry.common.bloodAltarUpgrade;

import com.cricketcraft.chisel.api.carving.CarvingUtils;
import com.cricketcraft.chisel.api.carving.ICarvingGroup;
import com.google.common.base.Strings;

import WayofTime.alchemicalWizardry.api.BlockStack;

public class CompatChecks {

    public static boolean checkChiselBlock(BlockStack blockStack, String groupName) {
        if (blockStack.getBlock() == null) return false;

        ICarvingGroup group = CarvingUtils.getChiselRegistry().getGroup(blockStack.getBlock(), blockStack.getMeta());
        if (group != null) {
            String check = CarvingUtils.getChiselRegistry().getGroup(blockStack.getBlock(), blockStack.getMeta())
                    .getName();

            if (Strings.isNullOrEmpty(check)) return false;

            return check.equals(groupName);
        }

        return false;
    }
}

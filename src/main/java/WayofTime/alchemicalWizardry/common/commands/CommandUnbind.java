package WayofTime.alchemicalWizardry.common.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;

import WayofTime.alchemicalWizardry.api.items.interfaces.IBindable;

public class CommandUnbind extends CommandBase {

    public CommandUnbind() {}

    @Override
    public String getCommandName() {
        return "unbind";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public String getCommandUsage(ICommandSender icommandsender) {
        return "commands.unbind.usage";
    }

    @Override
    public void processCommand(ICommandSender iCommandSender, String[] astring) {
        EntityPlayerMP entityplayermp = getCommandSenderAsPlayer(iCommandSender);
        ItemStack item = entityplayermp.getCurrentEquippedItem();

        if (item != null && item.getItem() instanceof IBindable) {
            if (!IBindable.getOwnerName(item).isEmpty()) {
                item.getTagCompound().removeTag("ownerName");
                func_152373_a(iCommandSender, this, "commands.unbind.success");
            } else {
                throw new CommandException("commands.unbind.failed.notBindable");
            }
        } else if (!(item.getItem() instanceof IBindable)) {
            throw new CommandException("commands.unbind.failed.notBindable");
        }
    }
}

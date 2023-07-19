package WayofTime.alchemicalWizardry.api.sacrifice;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IChatComponent;

public class DamageSourceBloodMagic extends DamageSource
{
    public static final DamageSourceBloodMagic INSTANCE = new DamageSourceBloodMagic();
    public DamageSourceBloodMagic()
    {
        super("bloodMagic");

        setDamageBypassesArmor();
        setDamageIsAbsolute();
    }
    @Override
    public IChatComponent func_151519_b(EntityLivingBase player){
        String s = "death.attack." + this.damageType;

        return new ChatComponentTranslation(s, player.func_145748_c_());
    }
}
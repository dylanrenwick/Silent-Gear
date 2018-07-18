package net.silentchaos512.gear.api.parts;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public final class PartMain extends ItemPart {

    public PartMain(ResourceLocation name) {
        super(name, false);
    }

    public PartMain(ResourceLocation name, boolean userDefined) {
        super(name, userDefined);
    }

    @Override
    public ResourceLocation getTexture(ItemStack stack, String toolClass, int animationFrame) {
        return getTexture(stack, toolClass, animationFrame, "head");
    }

    public ResourceLocation getTexture(ItemStack stack, String toolClass, int animationFrame, String subtype) {
        String frameStr = "bow".equals(toolClass) && animationFrame == 3 ? "_3" : "";
        String subtypePrefix = subtype + (subtype.isEmpty() ? "" : "_");
        String path = "items/" + toolClass + "/" + subtypePrefix + this.textureSuffix + frameStr;
        return new ResourceLocation(this.key.getNamespace(), path);
    }

    @Override
    public ResourceLocation getBrokenTexture(ItemStack gear, String gearClass) {
        return new ResourceLocation(this.key.getNamespace(), "items/" + gearClass + "/_broken");
    }

    @Override
    public String getTypeName() {
        return "main";
    }
}

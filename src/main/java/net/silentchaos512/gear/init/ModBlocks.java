package net.silentchaos512.gear.init;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowerPotBlock;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.WallOrFloorItem;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.silentchaos512.gear.SilentGear;
import net.silentchaos512.gear.block.*;
import net.silentchaos512.gear.block.craftingstation.CraftingStationBlock;
import net.silentchaos512.gear.block.salvager.SalvagerBlock;
import net.silentchaos512.lib.block.IBlockProvider;
import net.silentchaos512.utils.Lazy;

import javax.annotation.Nullable;
import java.util.Locale;
import java.util.Objects;
import java.util.function.Supplier;

public enum ModBlocks implements IBlockProvider, IStringSerializable {
    CRAFTING_STATION(CraftingStationBlock::new),
    SALVAGER(SalvagerBlock::new),
    FLAX_PLANT(() -> new FlaxPlant(false), () -> null),
    WILD_FLAX_PLANT(() -> new FlaxPlant(true), () -> null),
    STONE_TORCH(StoneTorch::new, ModBlocks::getStoneTorchItem),
    WALL_STONE_TORCH(StoneTorchWall::new, () -> null),
    NETHERWOOD_LOG(NetherwoodLog::new),
    NETHERWOOD_PLANKS(NetherwoodPlanks::new),
    NETHERWOOD_SLAB(NetherwoodSlab::new),
    NETHERWOOD_STAIRS(NetherwoodStairs::new),
    NETHERWOOD_LEAVES(NetherwoodLeaves::new),
    NETHERWOOD_SAPLING(NetherwoodSapling::new),
    CRIMSON_IRON_ORE(CrimsonIronOre::new),
    POTTED_NETHERWOOD_SAPLING(() -> makePottedPlant(NETHERWOOD_SAPLING::asBlock), () -> null),
    PHANTOM_LIGHT(PhantomLight::new);

    private final Lazy<Block> block;
    private final Lazy<BlockItem> item;

    ModBlocks(Supplier<Block> blockSupplier) {
        this.block = Lazy.of(blockSupplier);
        this.item = Lazy.of(() -> new BlockItem(this.asBlock(), new Item.Properties().group(SilentGear.ITEM_GROUP)));
    }

    ModBlocks(Supplier<Block> blockSupplier, Supplier<BlockItem> itemBlockSupplier) {
        this.block = Lazy.of(blockSupplier);
        this.item = Lazy.of(itemBlockSupplier);
    }

    public static void registerAll(RegistryEvent.Register<Block> event) {
        if (!event.getName().equals(ForgeRegistries.BLOCKS.getRegistryName())) return;

        for (ModBlocks block : values()) {
            register(block.getName(), block.asBlock(), block.item.get());
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static void registerRenderTypes(FMLClientSetupEvent event) {
        RenderTypeLookup.setRenderLayer(FLAX_PLANT.asBlock(), RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(NETHERWOOD_SAPLING.asBlock(), RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(POTTED_NETHERWOOD_SAPLING.asBlock(), RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(STONE_TORCH.asBlock(), RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(WALL_STONE_TORCH.asBlock(), RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(WILD_FLAX_PLANT.asBlock(), RenderType.getCutout());
    }

    private static void register(String name, Block block, @Nullable BlockItem item) {
        ResourceLocation registryName = new ResourceLocation(SilentGear.MOD_ID, name);
        block.setRegistryName(registryName);
        ForgeRegistries.BLOCKS.register(block);
        if (item != null) {
            ModItems.blocksToRegister.put(name, item);
        }
    }

    private static BlockItem getStoneTorchItem() {
        return new WallOrFloorItem(STONE_TORCH.asBlock(), WALL_STONE_TORCH.asBlock(), new Item.Properties());
    }

    private static FlowerPotBlock makePottedPlant(Supplier<Block> flower) {
        FlowerPotBlock potted = new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT.delegate.get(), flower, Block.Properties.create(Material.MISCELLANEOUS).hardnessAndResistance(0));
        ResourceLocation flowerId = Objects.requireNonNull(flower.get().getRegistryName());
        ((FlowerPotBlock) Blocks.FLOWER_POT).addPlant(flowerId, () -> potted);
        return potted;
    }

    @Override
    public String getName() {
        return name().toLowerCase(Locale.ROOT);
    }

    @Override
    public Block asBlock() {
        return block.get();
    }

    @Override
    public Item asItem() {
        return asBlock().asItem();
    }
}

package com.direwolf20.buildinggadgets;

import com.direwolf20.buildinggadgets.blocks.Models.BakedModelLoader;
import com.direwolf20.buildinggadgets.blocks.templatemanager.TemplateManagerContainer;
import com.direwolf20.buildinggadgets.entities.BlockBuildEntity;
import com.direwolf20.buildinggadgets.entities.BlockBuildEntityRender;
import com.direwolf20.buildinggadgets.entities.ConstructionBlockEntity;
import com.direwolf20.buildinggadgets.entities.ConstructionBlockEntityRender;
import com.direwolf20.buildinggadgets.items.*;
import com.direwolf20.buildinggadgets.tools.PasteContainerMeshDefinition;
import com.direwolf20.buildinggadgets.tools.ToolRenders;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

import static com.direwolf20.buildinggadgets.ModItems.buildingTool;

@Mod.EventBusSubscriber(value = Side.CLIENT, modid = BuildingGadgets.MODID)
public class ClientProxy extends CommonProxy {
    @Override
    public void preInit(FMLPreInitializationEvent e) {
        ModEntities.initModels();
        ModelLoaderRegistry.registerLoader(new BakedModelLoader());
        super.preInit(e);
    }

    @Override
    public void init() {
        super.init();
        KeyBindings.init();
        ModBlocks.initColorHandlers();
    }

    @SubscribeEvent
    public static void registerModels(@SuppressWarnings("unused") ModelRegistryEvent event) {
        ModBlocks.effectBlock.initModel();
        ModBlocks.templateManager.initModel();
        buildingTool.initModel();
        ModItems.exchangerTool.initModel();
        ModItems.copyPasteTool.initModel();
        ModItems.template.initModel();
        if (Config.enableDestructionTool) {
            ModItems.destructionTool.initModel();
        }
        if (Config.enablePaste) {
            ModItems.constructionPaste.initModel();
            ModItems.constructionPasteContainer.initModel();
            ModItems.constructionPasteContainert2.initModel();
            ModItems.constructionPasteContainert3.initModel();
            ModBlocks.constructionBlock.initModel();
            ModBlocks.constructionBlockPowder.initModel();
            ModelLoader.setCustomMeshDefinition(ModItems.constructionPasteContainer, new PasteContainerMeshDefinition());
            ModelLoader.setCustomMeshDefinition(ModItems.constructionPasteContainert2, new PasteContainerMeshDefinition());
            ModelLoader.setCustomMeshDefinition(ModItems.constructionPasteContainert3, new PasteContainerMeshDefinition());
        }
    }

    public void registerEntityRenderers() {
        RenderingRegistry.registerEntityRenderingHandler(BlockBuildEntity.class, new BlockBuildEntityRender.Factory());
        RenderingRegistry.registerEntityRenderingHandler(ConstructionBlockEntity.class, new ConstructionBlockEntityRender.Factory());
    }

    @SubscribeEvent
    public static void renderWorldLastEvent(RenderWorldLastEvent evt) {
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayer p = mc.player;
        ItemStack heldItem = p.getHeldItemMainhand();
        if (!(heldItem.getItem() instanceof GenericGadget)) {
            heldItem = p.getHeldItemOffhand();
            if (!(heldItem.getItem() instanceof GenericGadget)) {
                return;
            }
        }
        if (heldItem.getItem() instanceof BuildingTool) {
            ToolRenders.renderBuilderOverlay(evt, p, heldItem);
        } else if (heldItem.getItem() instanceof ExchangerTool) {
            ToolRenders.renderExchangerOverlay(evt, p, heldItem);
        } else if (heldItem.getItem() instanceof CopyPasteTool) {
            ToolRenders.renderPasteOverlay(evt, p, heldItem);
        } else if (heldItem.getItem() instanceof DestructionTool) {
            ToolRenders.renderDestructionOverlay(evt, p, heldItem);
        }

    }

    @SubscribeEvent
    public static void registerSprites(TextureStitchEvent.Pre event) {
        registerSprite(event, TemplateManagerContainer.TEXTURE_LOC_SLOT_TOOL);
        registerSprite(event, TemplateManagerContainer.TEXTURE_LOC_SLOT_TEMPLATE);
    }

    private static void registerSprite(TextureStitchEvent.Pre event, String loc) {
        event.getMap().registerSprite(new ResourceLocation(loc));
    }
}
package com.minecolonies.coremod.client.gui;

import com.ldtteam.blockout.Color;
import com.ldtteam.blockout.Pane;
import com.ldtteam.blockout.controls.*;
import com.ldtteam.blockout.views.Box;
import com.ldtteam.blockout.views.View;
import com.ldtteam.blockout.views.Window;
import com.ldtteam.blockout.views.ZoomDragView;
import com.ldtteam.structurize.util.LanguageHandler;
import com.minecolonies.api.colony.IColony;
import com.minecolonies.api.colony.IColonyView;
import com.minecolonies.api.colony.buildings.views.IBuildingView;
import com.minecolonies.api.colony.buildings.workerbuildings.ITownHallView;
import com.minecolonies.api.crafting.ItemStorage;
import com.minecolonies.api.research.IGlobalResearch;
import com.minecolonies.api.research.IGlobalResearchTree;
import com.minecolonies.api.research.ILocalResearch;
import com.minecolonies.api.research.ILocalResearchTree;
import com.minecolonies.api.research.util.ResearchState;
import com.minecolonies.api.util.constant.Constants;
import com.minecolonies.coremod.Network;
import com.minecolonies.coremod.colony.Colony;
import com.minecolonies.coremod.colony.buildings.AbstractSchematicProvider;
import com.minecolonies.coremod.colony.buildings.workerbuildings.BuildingTownHall;
import com.minecolonies.coremod.colony.buildings.workerbuildings.BuildingUniversity;
import com.minecolonies.coremod.network.messages.TryResearchMessage;
import com.mojang.datafixers.util.Pair;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.items.wrapper.InvWrapper;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static com.minecolonies.api.util.constant.WindowConstants.*;
import static com.minecolonies.api.util.constant.ColonyConstants.*;

public class WindowMap extends AbstractWindowSkeleton {
    /**
     * the Town Hall building
     */
    private final ITownHallView building;

    /**
     * The previous window
     */
    private final WindowTownHall last;

    private int minX, minZ, sizeX, sizeZ;

    final static int BLOCK_TO_PIXEL = 2;


    /**
     * Create the research tree window
     * @param building the associated Town Hall
     * @param last the GUI we opened this from
     */
    public WindowMap(final ITownHallView building, final WindowTownHall last)
    {
        super(Constants.MOD_ID+MAP_RESOURCE_SUFFIX);

        this.building = building;
        this.last = last;

        final ZoomDragView view = findPaneOfTypeByID(DRAG_VIEW_ID, ZoomDragView.class);

        drawMap(view, building.getColony());

    }

    private void drawMap(final ZoomDragView view, final IColonyView colony)
    {
        List<IBuildingView> buildings = colony.getBuildings();

        AxisAlignedBB bb = new AxisAlignedBB(colony.getCenter());

        for (IBuildingView building : buildings) {
            if (!(building instanceof AbstractSchematicProvider))
                bb = bb.union(new AxisAlignedBB(building.getPosition()));
            else {
                bb = bb.union(((AbstractSchematicProvider) building).getTargetableArea(colony.getWorld()));
            }
        }

        bb = bb.expand(32,0,32).expand(-32, 0,-32);
        minX = (int)bb.minX;
        minZ = (int)bb.minZ;
        sizeX = (int)(bb.maxX-minX);
        sizeZ = (int)(bb.maxZ-minZ);


        MaterialColor[][] data = new MaterialColor[sizeX][sizeZ];

        Gradient background = getGradientFromMaterialColor(MaterialColor.STONE);
        background.setSize(sizeX*BLOCK_TO_PIXEL, sizeZ*BLOCK_TO_PIXEL);
        view.addChild(background);

        for (int i = 0; i < sizeX; i++) {
            for(int j = 0; j < sizeZ; j++) {
                data[i][j] = getMaterialColor((int)bb.minX+i, (int)bb.minZ+j);
                Gradient block = getGradientFromMaterialColor(data[i][j]);
                block.setSize(BLOCK_TO_PIXEL, BLOCK_TO_PIXEL);
                block.setPosition(i*BLOCK_TO_PIXEL,j*BLOCK_TO_PIXEL);
                view.addChild(block);
            }
        }

        final int box_size = 10; //this is temporary, will only be there as long as I can't get bounding boxes
        for (IBuildingView building : colony.getBuildings())
        {
            Box box = new Box();
            box.setColor(0,0,0);
            box.setSize(box_size, box_size);
            Pair<Integer, Integer> coords = getRelativeCoordFromBlockPos(building.getPosition());
            box.setPosition(coords.getFirst() - box_size/2, coords.getSecond()-box_size/2);
            view.addChild(box);

        }

    }

    private MaterialColor getMaterialColor(int x, int z) {
        final World world = building.getColony().getWorld();
        Chunk chunk = world.getChunkAt(new BlockPos(x, 0, z));
        if(chunk.isEmpty()) {
            return MaterialColor.AIR;
        }
        int y = chunk.getTopBlockY(Heightmap.Type.WORLD_SURFACE, x-chunk.getPos().getXStart(), z-chunk.getPos().getZStart());
        BlockState blockState;
        BlockPos.Mutable bp = new BlockPos.Mutable(x, y, z);
        if (y <= 0) {
            blockState = Blocks.BEDROCK.getDefaultState();
        }
        else
        {
            blockState = chunk.getBlockState(bp);
        }
        return blockState.getMaterialColor(world, bp);
    }

    private Pair<Integer, Integer> getRelativeCoordFromBlockPos(BlockPos bp) {
        int x,z;
        x = bp.getX();
        z = bp.getZ();
        return Pair.of((x - minX)*BLOCK_TO_PIXEL, (z-minZ)*BLOCK_TO_PIXEL);
    }

    private Gradient getGradientFromMaterialColor(MaterialColor color)
    {
        Gradient gradient = new Gradient();
        int r,g,b;
        java.awt.Color c = new java.awt.Color(color.colorValue);
        r = c.getRed();
        g = c.getGreen();
        b = c.getBlue();
        gradient.setGradientStart(r,g,b,255);
        gradient.setGradientEnd(r,g,b,255);
        return gradient;
    }


    @Override
    public void onButtonClicked(@NotNull final Button button)
    {
        super.onButtonClicked(button);

        if (button.getID().equals("cancel"))
        {
            this.close();
            last.open();
        }
    }
}

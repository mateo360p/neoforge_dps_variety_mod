package com.dipazio.dpsvarmod.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import java.util.ArrayList;
import java.util.List;

public class BlocksGetter {
    // I won't make a tool to destroy more than a 3x3 area
    public static List<BlockPos> getBlocksIn3x3plane(BlockPos centerBlock, Direction dir, boolean ignoreCorners) {
        List<BlockPos> positions = new ArrayList<>();
        for (int preX = -1; preX <= 1; preX++) {
            for (int preY = -1; preY <= 1; preY++) {
                if (ignoreCorners && (Math.abs(preX) + Math.abs(preY) > 1)) continue;

                BlockPos pos;

                if (dir == Direction.UP || dir == Direction.DOWN) {
                    pos = new BlockPos(centerBlock.getX() + preX, centerBlock.getY(), centerBlock.getZ() + preY);
                } else if (dir == Direction.NORTH || dir == Direction.SOUTH) {
                    pos = new BlockPos(centerBlock.getX() + preX, centerBlock.getY() + preY, centerBlock.getZ());
                } else { // EAST & WEST
                    pos = new BlockPos(centerBlock.getX(), centerBlock.getY() + preY, centerBlock.getZ() + preX);
                }

                positions.add(pos);
            }
        }

        return positions;
    }

    public static List<BlockPos> getBlocksInPlayer3x3plane(BlockPos centerBlock, Player player, boolean ignoreCorners) {
        BlockHitResult hitResult = player.level().clip(
                new ClipContext(player.getEyePosition(1f),
                        player.getEyePosition(1f).add(player.getViewVector(1f).scale(6f)),
                        ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player)
        );

        List<BlockPos> positions = new ArrayList<>();
        if(hitResult.getType() == HitResult.Type.MISS) {
            return positions;
        }

        Direction dir = hitResult.getDirection();
        positions = getBlocksIn3x3plane(centerBlock, dir, ignoreCorners);

        return positions;
    }
}

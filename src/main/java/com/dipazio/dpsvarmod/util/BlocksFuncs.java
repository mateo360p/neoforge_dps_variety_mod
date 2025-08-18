package com.dipazio.dpsvarmod.util;

import com.dipazio.dpsvarmod.item._parents.TripleAreaDestroyerTool;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.level.BlockEvent;

import java.util.*;

public class BlocksFuncs {
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

    /**
     * Equivalent as level.levelEvent(2001,...)
     * But this one has a volume value
     * This should be used only in non-server levels
     */
    public static void breakingBlockDetails(Level level, BlockPos pos, BlockState state, float soundVolume) {
        int data = Block.getId(state);
        BlockState blockstate1 = Block.stateById(data);
        if (!blockstate1.isAir() && !net.neoforged.neoforge.client.extensions.common.IClientBlockExtensions.of(blockstate1).playBreakSound(blockstate1, level, pos)) {
            SoundType soundtype = blockstate1.getSoundType(level, pos, null);
            level.playLocalSound(
                    pos, soundtype.getBreakSound(), SoundSource.BLOCKS, soundVolume * ((soundtype.getVolume() + 1.0F) / 2.0F), soundtype.getPitch() * 0.8F, false
            );
        }

        level.addDestroyBlockEffect(pos, blockstate1);
    }

    /**
     * Be careful with this thing, your ears may be in real danger if you put a limit like 64!
     **/
    public static void breakBlocksWithinLimit(Level level, Player player, BlockPos originPos, int limit) {
        if (level.isClientSide) return;

        Block targetBlock = level.getBlockState(originPos).getBlock();
        Set<BlockPos> visited = new HashSet<>();
        Queue<BlockPos> queue = new ArrayDeque<>();

        visited.add(originPos);
        queue.add(originPos);

        ItemStack handItem = player.getMainHandItem();
        int count = 0;

        while (!queue.isEmpty() && count < limit) {
            BlockPos cur = queue.poll();
            if (!level.isLoaded(cur)) continue;

            BlockState state = level.getBlockState(cur);
            if (state.getBlock() != targetBlock) continue;
            if (!TripleAreaDestroyerTool.canDestroyBlockAt(state, level, cur, player)) continue;

            ServerPlayer serverPlayer = (ServerPlayer)player;
            if (player.getAbilities().instabuild) {
                if (state.onDestroyedByPlayer(level, cur, player, true, state.getFluidState())) {
                    state.getBlock().destroy(level, cur, state);
                    level.removeBlock(cur, false);
                    count++;
                    level.levelEvent(2001, cur, Block.getId(state));
                }
            } else {
                BlockEvent.BreakEvent event = new BlockEvent.BreakEvent(level, cur, state, player);
                NeoForge.EVENT_BUS.post(event);
                if (event.isCanceled()) {
                    serverPlayer.connection.send(new ClientboundBlockUpdatePacket(level, cur));
                    BlockEntity ent = level.getBlockEntity(cur);
                    if (ent != null) {
                        Packet<?> packet = ent.getUpdatePacket();
                        if (packet != null) {
                            serverPlayer.connection.send(packet);
                        }
                    }
                    continue;
                }

                handItem.getItem().mineBlock(handItem, level, state, cur, player);
                state.getBlock().destroy(level, cur, state);
                state.getBlock().playerDestroy(level, player, cur, state, level.getBlockEntity(cur), handItem);
                state.getBlock().popExperience((ServerLevel) level, cur, state.getExpDrop(level, cur, level.getBlockEntity(cur), player, handItem));
                count++;

                level.removeBlock(cur, false);
                level.levelEvent(2001, cur, Block.getId(state));
                serverPlayer.connection.send(new ClientboundBlockUpdatePacket(level, cur));
            }

            for (Direction dir : Direction.values()) {
                BlockPos neighbor = cur.relative(dir);
                if (!visited.contains(neighbor)) {
                    BlockState neighborState = level.getBlockState(neighbor);
                    if (neighborState.getBlock() == targetBlock) {
                        visited.add(neighbor);
                        queue.add(neighbor);
                    }
                }
            }
        }
    }
}

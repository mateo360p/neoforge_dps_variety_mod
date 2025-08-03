package com.dipazio.dpsvarmod.item.tools;

import com.dipazio.dpsvarmod.item.parents.TripleAreaDestroyerTool;
import com.dipazio.dpsvarmod.item.parents.grand.DpDiggerItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.level.BlockEvent;

import javax.annotation.Nonnull;
import java.util.*;

// No, this ain't a fkin' helicopter man!
public class Chopper extends DpDiggerItem {
    public int RANGE = 10;
    public Chopper(ToolMaterial material, int range, Properties properties) {
        super(material, BlockTags.MINEABLE_WITH_AXE, 9.5F, -3.7F, properties); // uhHhHH vanilla values-
        this.RANGE = range;
    }

    @Override
    public boolean canAttackBlock(@Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos, Player player) {
        if (!player.isShiftKeyDown() && player.getMainHandItem().isCorrectToolForDrops(state)) {
            breakBlocks(level, player, pos, RANGE);
            return false; // fkin' shitty bug, I'm to lazy to search how to fix it-
        }

        return true;
    }

    public static void breakBlocks(Level level, Player player, BlockPos originPos, int limit) {
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

                level.removeBlock(cur, false);
                level.levelEvent(2001, cur, Block.getId(state));
                serverPlayer.connection.send(new ClientboundBlockUpdatePacket(level, cur));

                count++;
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

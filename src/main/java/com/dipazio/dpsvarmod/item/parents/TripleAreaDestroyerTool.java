package com.dipazio.dpsvarmod.item.parents;

import com.dipazio.dpsvarmod.item.material.TripleAreaToolMaterial;
import com.dipazio.dpsvarmod.item.parents.grand.DpDiggerItem;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.core.Direction;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.level.BlockEvent;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

// SoOOoOoO I'm adding hammers and diggers! (Pretty original Ik)
public class TripleAreaDestroyerTool extends DpDiggerItem {
    public TripleAreaDestroyerTool(ToolMaterial tier, TagKey<Block> blockTag, float attackDamage, float attackSpeed, Properties properties) {
        super(tier, blockTag, attackDamage, attackSpeed, properties);
        //if (tier == ToolMaterial.WOOD || tier == TripleAreaToolMaterial.WOOD) this.burnTime = 200;
    }

    @Override
    public boolean canAttackBlock(@Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos, Player player) {
        // if is crouching, only breaks one block, else braks 3x3
        if (!player.isShiftKeyDown() && player.getMainHandItem().isCorrectToolForDrops(level.getBlockState(pos))) {
            breakBlocks(level, player, pos);
        }

        return true;
    }

    // I won't make a tool to destroy more than a 3x3 area
    public static List<BlockPos> blocksToDestroy(BlockPos centerBlock, Player player) {
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
        for (int preX = -1; preX <= 1; preX++) {
            for (int preY = -1; preY <= 1; preY++) {
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

    // So, YOU CAN MAKE A CLASS INSIDE A FUNC BUT YOU CAN'T PUT A FUNC INSIDE A FUNCTION ARE YOU SERIOUS?
    // Java noobie understanding this world: *explosions everywhere*
    public static boolean destroyBlockHelper(BlockState state, Level level, BlockPos pos, Player player) {
        double hardness = state.getDestroySpeed(level, pos);
        boolean correctTool = player.getMainHandItem().isCorrectToolForDrops(state);
        boolean compareHard = (0 < hardness && hardness < (level.getBlockState(pos).getDestroySpeed(level, pos) * 5));

        return correctTool && compareHard;
    }

    public static void breakBlocks(Level level, Player player, BlockPos originPos) {
        if (level.isClientSide) return;

        List<BlockPos> blocks = blocksToDestroy(originPos, player);
        ItemStack handItem = player.getMainHandItem();
        blocks.forEach((pos) -> {
            if (pos.equals(originPos)) return;

            BlockState state = level.getBlockState(pos);
            if (!destroyBlockHelper(state, level, originPos, player)) return;

            ServerPlayer serverPlayer = (ServerPlayer)player;
            if (player.getAbilities().instabuild) {
                if (state.onDestroyedByPlayer(level, pos, player, true, state.getFluidState())) {
                    state.getBlock().destroy(level, pos, state);
                }
            } else {
                BlockEvent.BreakEvent event = new BlockEvent.BreakEvent(level, pos, state, player);
                NeoForge.EVENT_BUS.post(event);
                if (event.isCanceled()) {
                    serverPlayer.connection.send(new ClientboundBlockUpdatePacket(level, pos));
                    BlockEntity ent = level.getBlockEntity(pos);
                    if (ent != null) {
                        Packet<?> packet = ent.getUpdatePacket();
                        if (packet != null) {
                            serverPlayer.connection.send(packet);
                        }
                    }
                } else {
                    handItem.getItem().mineBlock(handItem, level, state, pos, player);
                    state.getBlock().destroy(level, pos, state);
                    state.getBlock().playerDestroy(level, player, pos, state, level.getBlockEntity(pos), handItem);
                    state.getBlock().popExperience((ServerLevel)level, pos, state.getExpDrop(level, pos, level.getBlockEntity(pos), player, handItem));

                    level.removeBlock(pos, false);
                    level.levelEvent(2001, pos, Block.getId(state));
                    serverPlayer.connection.send(new ClientboundBlockUpdatePacket(level, pos));
                }
            }
        });
    }
}

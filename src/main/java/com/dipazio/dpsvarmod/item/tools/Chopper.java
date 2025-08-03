package com.dipazio.dpsvarmod.item.tools;

import com.dipazio.dpsvarmod.item._parents.TripleAreaDestroyerTool;
import com.dipazio.dpsvarmod.item._parents.grand.DpDiggerItem;
import com.dipazio.dpsvarmod.util.BlocksGetter;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.level.BlockEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
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

    // This is kinda messy, isn't it?
    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos blockpos = context.getClickedPos();
        Player player = context.getPlayer();

        if (playerHasShieldUseIntent(context)) {
            return InteractionResult.PASS;
        } else {
            Optional<BlockState> optional = this.evaluateNewBlockState(level, blockpos, player, level.getBlockState(blockpos), context);
            if (optional.isEmpty()) {
                return InteractionResult.PASS;
            } else {
                List<BlockPos> positions;
                if (player != null) {
                    if(player.isShiftKeyDown()) {
                        positions = new ArrayList<>();
                        positions.add(blockpos); // Only one block!
                    }
                    else positions = BlocksGetter.getBlocksInPlayer3x3plane(blockpos, player, true);
                }
                else positions = BlocksGetter.getBlocksIn3x3plane(blockpos, context.getHorizontalDirection(), true);

                int actionType = evaluateModifyType(level.getBlockState(blockpos), context);;
                for (BlockPos block : positions) {
                    if (actionType != evaluateModifyType(level.getBlockState(block), context)) continue;

                    optional = this.evaluateNewBlockState(level, block, player, level.getBlockState(block), context);
                    if (optional.isEmpty()) continue;

                    ItemStack itemstack = context.getItemInHand();
                    if (player instanceof ServerPlayer) {
                        CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger((ServerPlayer) player, block, itemstack);
                    }

                    level.setBlock(block, optional.get(), 11);
                    level.gameEvent(GameEvent.BLOCK_CHANGE, block, GameEvent.Context.of(player, optional.get()));
                    if (player != null) {
                        itemstack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(context.getHand()));
                    }
                }

                return InteractionResult.SUCCESS;
            }
        }
    }

    private static boolean playerHasShieldUseIntent(UseOnContext context) {
        Player player = context.getPlayer();
        boolean hasShield = false;
        if (player != null) hasShield = player.getOffhandItem().is(Items.SHIELD); // IDK what context gives so-
        return context.getHand().equals(InteractionHand.MAIN_HAND) && hasShield && !player.isSecondaryUseActive();
    }

    private Optional<BlockState> evaluateNewBlockState(Level level, BlockPos pos, @Nullable Player player, BlockState state, UseOnContext p_40529_) {
        Optional<BlockState> optional = Optional.ofNullable(state.getToolModifiedState(p_40529_, net.neoforged.neoforge.common.ItemAbilities.AXE_STRIP, false));
        if (optional.isPresent()) {
            level.playSound(player, pos, SoundEvents.AXE_STRIP, SoundSource.BLOCKS, 1.0F, 1.0F);
            return optional;
        } else {
            Optional<BlockState> optional1 = Optional.ofNullable(state.getToolModifiedState(p_40529_, net.neoforged.neoforge.common.ItemAbilities.AXE_SCRAPE, false));
            if (optional1.isPresent()) {
                level.playSound(player, pos, SoundEvents.AXE_SCRAPE, SoundSource.BLOCKS, 1.0F, 1.0F);
                level.levelEvent(player, 3005, pos, 0);
                return optional1;
            } else {
                Optional<BlockState> optional2 = Optional.ofNullable(state.getToolModifiedState(p_40529_, net.neoforged.neoforge.common.ItemAbilities.AXE_WAX_OFF, false));
                if (optional2.isPresent()) {
                    level.playSound(player, pos, SoundEvents.AXE_WAX_OFF, SoundSource.BLOCKS, 1.0F, 1.0F);
                    level.levelEvent(player, 3004, pos, 0);
                    return optional2;
                } else {
                    return Optional.empty();
                }
            }
        }
    }

    private int evaluateModifyType(BlockState state, UseOnContext p_40529_) {
        Optional<BlockState> optional = Optional.ofNullable(state.getToolModifiedState(p_40529_, net.neoforged.neoforge.common.ItemAbilities.AXE_STRIP, false));
        if (optional.isPresent()) {
            return 0;
        } else {
            Optional<BlockState> optional1 = Optional.ofNullable(state.getToolModifiedState(p_40529_, net.neoforged.neoforge.common.ItemAbilities.AXE_SCRAPE, false));
            if (optional1.isPresent()) {
                return 1;
            } else {
                Optional<BlockState> optional2 = Optional.ofNullable(state.getToolModifiedState(p_40529_, net.neoforged.neoforge.common.ItemAbilities.AXE_WAX_OFF, false));
                if (optional2.isPresent()) {
                    return 2;
                } else {
                    return -1;
                }
            }
        }
    }

    @Override
    public boolean canPerformAction(ItemStack stack, net.neoforged.neoforge.common.ItemAbility itemAbility) {
        return net.neoforged.neoforge.common.ItemAbilities.DEFAULT_AXE_ACTIONS.contains(itemAbility);
    }
}

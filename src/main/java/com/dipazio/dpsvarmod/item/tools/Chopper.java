package com.dipazio.dpsvarmod.item.tools;

import com.dipazio.dpsvarmod.item._material.DpToolMaterial;
import com.dipazio.dpsvarmod.item._parents.grand.DpDiggerItem;
import com.dipazio.dpsvarmod.util.BlocksFuncs;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
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

import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

// No, this ain't a fkin' helicopter man!
public class Chopper extends DpDiggerItem {
    public int RANGE = 10;
    public Chopper(DpToolMaterial material, int range, float attackDamage, float attackSpeed, Properties properties) {
        super(material, BlockTags.MINEABLE_WITH_AXE, attackDamage, attackSpeed, properties); // uhHhHH vanilla values-
        this.RANGE = range;
    }

    @Override
    public boolean canAttackBlock(@Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos, Player player) {
        if (!player.isShiftKeyDown() && player.getMainHandItem().isCorrectToolForDrops(state)) {
            BlocksFuncs.breakBlocksWithinLimit(level, player, pos, RANGE);
            return false; // fkin' shitty bug, I'm to lazy to search how to fix it-
        }

        return true;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        Component text = Component.translatable("info.megatools.break").append( "" + RANGE).withStyle(ChatFormatting.GRAY);
        tooltipComponents.add(text);
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
                    else positions = BlocksFuncs.getBlocksInPlayer3x3plane(blockpos, player, true);
                }
                else positions = BlocksFuncs.getBlocksIn3x3plane(blockpos, context.getHorizontalDirection(), true);

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

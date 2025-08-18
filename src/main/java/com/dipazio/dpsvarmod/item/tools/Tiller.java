package com.dipazio.dpsvarmod.item.tools;

import com.dipazio.dpsvarmod.item._material.DpToolMaterial;
import com.dipazio.dpsvarmod.item._parents.grand.DpDiggerItem;
import com.dipazio.dpsvarmod.util.BlocksFuncs;
import com.mojang.datafixers.util.Pair;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class Tiller extends DpDiggerItem {
    public int RANGE = 10;
    public Tiller(DpToolMaterial material, int range, float attackDamage, float attackSpeed, Properties properties) {
        super(material, BlockTags.MINEABLE_WITH_HOE, attackDamage, attackSpeed, properties);
        this.RANGE = range;
    }

    @Override
    public boolean canAttackBlock(@Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos, Player player) {
        if (!player.isShiftKeyDown() && player.getMainHandItem().isCorrectToolForDrops(state)) {
            BlocksFuncs.breakBlocksWithinLimit(level, player, pos, RANGE);
            return false;
        }

        return true;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        Component text = Component.literal("Break limit: " + RANGE).withStyle(ChatFormatting.GRAY);
        tooltipComponents.add(text);
    }

    // Could I have copied the exact same code as the digger class?
    // Yea, but I don't know if the hoe method has something special!
    // SoOoOooOo I have redone this sh-
    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos blockpos = context.getClickedPos();
        BlockState toolModifiedState = level.getBlockState(blockpos).getToolModifiedState(context, net.neoforged.neoforge.common.ItemAbilities.HOE_TILL, false);
        Pair<Predicate<UseOnContext>, Consumer<UseOnContext>> pair = toolModifiedState == null ? null : Pair.of(ctx -> true, changeIntoState(toolModifiedState));
        BlockState blockstate = level.getBlockState(blockpos);

        if (pair == null) {
            return InteractionResult.PASS;
        } else {
            Predicate<UseOnContext> predicate = pair.getFirst();
            Consumer<UseOnContext> consumer = pair.getSecond();
            if (predicate.test(context)) {
                Player player = context.getPlayer();
                finishHoeTilling(level, player, blockpos, consumer, context);

                List<BlockPos> positions;
                if (player != null) {
                    if(player.isShiftKeyDown()) {
                        positions = new ArrayList<>();
                    }
                    else positions = BlocksFuncs.getBlocksInPlayer3x3plane(blockpos, player, false);
                }
                else positions = BlocksFuncs.getBlocksIn3x3plane(blockpos, context.getHorizontalDirection(), false);

                positions.remove(blockpos); // oOoOoOooOo

                for (BlockPos block : positions) {
                    if (level.getBlockState(block).getBlock() != blockstate.getBlock()) continue;
                    UseOnContext daContext = new UseOnContext( // madafakin copy
                            context.getPlayer(),
                            context.getHand(),
                            new BlockHitResult(Vec3.atCenterOf(block), context.getClickedFace(), block, false)
                    );
                    BlockState daToolModifiedState = level.getBlockState(block)
                            .getToolModifiedState(daContext, net.neoforged.neoforge.common.ItemAbilities.HOE_TILL, false);
                    if (!level.isClientSide &&daToolModifiedState != null) {
                        Consumer<UseOnContext> daConsumer = changeIntoState(daToolModifiedState);
                        finishHoeTilling(level, player, block, daConsumer, daContext);
                    }
                }

                return InteractionResult.SUCCESS;
            } else {
                return InteractionResult.PASS;
            }
        }
    }

    public void finishHoeTilling(Level level, Player player, BlockPos blockpos, Consumer<UseOnContext> consumer, UseOnContext context) {
        level.playSound(player, blockpos, SoundEvents.HOE_TILL, SoundSource.BLOCKS, 1.0F, 1.0F);
        if (!level.isClientSide) {
            consumer.accept(context);
            if (player != null) {
                context.getItemInHand().hurtAndBreak(1, player, LivingEntity.getSlotForHand(context.getHand()));
            }
        }
    }

    public static Consumer<UseOnContext> changeIntoState(BlockState state) {
        return p_316061_ -> {
            p_316061_.getLevel().setBlock(p_316061_.getClickedPos(), state, 11);
            p_316061_.getLevel().gameEvent(GameEvent.BLOCK_CHANGE, p_316061_.getClickedPos(), GameEvent.Context.of(p_316061_.getPlayer(), state));
        };
    }

    public boolean canPerformAction(ItemStack stack, ItemAbility itemAbility) {
        return ItemAbilities.DEFAULT_HOE_ACTIONS.contains(itemAbility);
    }
}

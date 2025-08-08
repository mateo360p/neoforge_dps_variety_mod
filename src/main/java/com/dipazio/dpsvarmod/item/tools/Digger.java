package com.dipazio.dpsvarmod.item.tools;

import com.dipazio.dpsvarmod.item._material.DpToolMaterial;
import com.dipazio.dpsvarmod.item._parents.TripleAreaDestroyerTool;
import com.dipazio.dpsvarmod.util.BlocksUtilFuncs;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

import java.util.ArrayList;
import java.util.List;

public class Digger extends TripleAreaDestroyerTool {
    public Digger(DpToolMaterial material, float attackDamage, float attackSpeed, Properties properties) {
        super(material, BlockTags.MINEABLE_WITH_SHOVEL, attackDamage, attackSpeed, properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos blockpos = context.getClickedPos();
        BlockState blockstate = level.getBlockState(blockpos);

        if (context.getClickedFace() == Direction.DOWN) {
            return InteractionResult.PASS;
        } else {
            Player player = context.getPlayer();
            BlockState blockstate2 = blockstateHelper(context, blockstate, blockpos, level, player);
            if (blockstate2 != null) {
                if (!level.isClientSide) {
                    level.setBlock(blockpos, blockstate2, 11);
                    level.gameEvent(GameEvent.BLOCK_CHANGE, blockpos, GameEvent.Context.of(player, blockstate2));
                    if (player != null) {
                        context.getItemInHand().hurtAndBreak(1, player, LivingEntity.getSlotForHand(context.getHand()));
                    }
                }

                List<BlockPos> positions = BlocksUtilFuncs.getBlocksIn3x3plane(blockpos, Direction.DOWN, true);
                if (player != null) {
                    if(player.isShiftKeyDown()) {
                        positions = new ArrayList<>(); // The block has been already modified!
                    }
                }

                positions.remove(blockpos); // oOoOoOooOo

                for (BlockPos block : positions) {
                    if (level.getBlockState(block).getBlock() != blockstate.getBlock()) continue;
                    BlockState state = blockstateHelper(context, level.getBlockState(block), block, level, player);
                    if (!level.isClientSide && state != null) {
                        level.setBlock(block, state, 11);
                        level.gameEvent(GameEvent.BLOCK_CHANGE, block, GameEvent.Context.of(player, state));
                        if (player != null) {
                            context.getItemInHand().hurtAndBreak(1, player, LivingEntity.getSlotForHand(context.getHand()));
                        }
                    }
                }

                return InteractionResult.SUCCESS;
            } else {
                return InteractionResult.PASS;
            }
        }
    }

    public BlockState blockstateHelper(UseOnContext context, BlockState blockstate, BlockPos blockpos, Level level, Player player) {
        BlockState blockstate1 = blockstate.getToolModifiedState(context, net.neoforged.neoforge.common.ItemAbilities.SHOVEL_FLATTEN, false);
        BlockState blockstate2 = null;
        if (blockstate1 != null && level.getBlockState(blockpos.above()).isAir()) {
            level.playSound(player, blockpos, SoundEvents.SHOVEL_FLATTEN, SoundSource.BLOCKS, 1.0F, 1.0F);
            blockstate2 = blockstate1;
        } else if ((blockstate2 = blockstate.getToolModifiedState(context, net.neoforged.neoforge.common.ItemAbilities.SHOVEL_DOUSE, false)) != null) {
            if (!level.isClientSide()) {
                level.levelEvent(null, 1009, blockpos, 0);
            }

        }

        return blockstate2;
    }

    @Override
    public boolean canPerformAction(ItemStack stack, net.neoforged.neoforge.common.ItemAbility itemAbility) {
        return net.neoforged.neoforge.common.ItemAbilities.DEFAULT_SHOVEL_ACTIONS.contains(itemAbility);
    }
}

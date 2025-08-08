package com.dipazio.dpsvarmod.item._parents;

import com.dipazio.dpsvarmod.item._material.DpToolMaterial;
import com.dipazio.dpsvarmod.item._parents.grand.DpDiggerItem;
import com.dipazio.dpsvarmod.util.BlocksUtilFuncs;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.level.BlockEvent;

import javax.annotation.Nonnull;
import java.util.List;

// SoOOoOoO I'm adding hammers and diggers! (Pretty original Ik)
public class TripleAreaDestroyerTool extends DpDiggerItem {
    public TripleAreaDestroyerTool(DpToolMaterial tier, TagKey<Block> blockTag, float attackDamage, float attackSpeed, Properties properties) {
        super(tier, blockTag, attackDamage, attackSpeed, properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        Component text = Component.literal("Dig area: 3x3").withStyle(ChatFormatting.GRAY);
        tooltipComponents.add(text);
    }

    @Override
    public boolean canAttackBlock(@Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos, Player player) {
        // if is crouching, only breaks one block, else breaks 3x3
        if (!player.isShiftKeyDown() && player.getMainHandItem().isCorrectToolForDrops(level.getBlockState(pos))) {
            breakBlocks(level, player, pos);
        }

        return true;
    }

    // So, YOU CAN MAKE A CLASS INSIDE A FUNC BUT YOU CAN'T PUT A FUNC INSIDE A FUNCTION ARE YOU SERIOUS?
    // Java noobie understanding this world: *explosions everywhere*
    public static boolean canDestroyBlockAt(BlockState state, Level level, BlockPos pos, Player player) {
        double hardness = state.getDestroySpeed(level, pos);
        boolean correctTool = player.getMainHandItem().isCorrectToolForDrops(state);
        boolean compareHard = (0 < hardness && hardness < (level.getBlockState(pos).getDestroySpeed(level, pos) * 5));

        return correctTool && compareHard;
    }

    public static void breakBlocks(Level level, Player player, BlockPos originPos) {
        if (level.isClientSide) return;

        List<BlockPos> blocks = BlocksUtilFuncs.getBlocksInPlayer3x3plane(originPos, player, false);
        ItemStack handItem = player.getMainHandItem();
        blocks.forEach((pos) -> {
            if (pos.equals(originPos)) return;

            BlockState state = level.getBlockState(pos);
            if (!canDestroyBlockAt(state, level, originPos, player)) return;

            ServerPlayer serverPlayer = (ServerPlayer)player;
            if (player.getAbilities().instabuild) {
                if (state.onDestroyedByPlayer(level, pos, player, true, state.getFluidState())) {
                    state.getBlock().destroy(level, pos, state);
                    level.removeBlock(pos, false);
                    level.levelEvent(2001, pos, Block.getId(state)); // Fancy
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

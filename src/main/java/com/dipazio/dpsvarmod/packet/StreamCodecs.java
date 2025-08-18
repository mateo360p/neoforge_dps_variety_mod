package com.dipazio.dpsvarmod.packet;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.Utf8String;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;

public class StreamCodecs {
    public static final StreamCodec<RegistryFriendlyByteBuf, ItemStack> ITEM_STACK_CODEC;
    public static final net.minecraft.network.codec.StreamCodec<RegistryFriendlyByteBuf, String> STRING_CODEC;
    public static final StreamCodec<RegistryFriendlyByteBuf, InteractionHand> HAND_CODEC;
    public static final StreamCodec<RegistryFriendlyByteBuf, Double> DOUBLE_CODEC;
    public static final StreamCodec<RegistryFriendlyByteBuf, Boolean> BOOLEAN_CODEC;

    static {
        ITEM_STACK_CODEC = ItemStack.OPTIONAL_STREAM_CODEC;
        STRING_CODEC = stringUtf8(32767);
        HAND_CODEC = new StreamCodec<>() {
            @Override
            public void encode(RegistryFriendlyByteBuf buf, InteractionHand hand) {
                buf.writeInt(hand.ordinal());
            }

            @Override
            public InteractionHand decode(RegistryFriendlyByteBuf buf) {
                return InteractionHand.values()[buf.readInt()];
            }
        };
        DOUBLE_CODEC = new StreamCodec<>() {
            @Override
            public void encode(RegistryFriendlyByteBuf buf, Double value) {
                buf.writeDouble(value);
            }

            @Override
            public Double decode(RegistryFriendlyByteBuf buf) {
                return buf.readDouble();
            }
        };
        BOOLEAN_CODEC = new StreamCodec<>() {
            @Override
            public void encode(RegistryFriendlyByteBuf buf, Boolean value) {
                buf.writeBoolean(value);
            }

            @Override
            public Boolean decode(RegistryFriendlyByteBuf buf) {
                return buf.readBoolean();
            }
        };
    }

    static net.minecraft.network.codec.StreamCodec<RegistryFriendlyByteBuf, String> stringUtf8(final int maxLength) {
        return new net.minecraft.network.codec.StreamCodec<RegistryFriendlyByteBuf, String>() {
            public String decode(RegistryFriendlyByteBuf buf) {
                return Utf8String.read(buf, maxLength);
            }
            public void encode(RegistryFriendlyByteBuf buf, String str) {
                Utf8String.write(buf, str, maxLength);
            }
        };
    }
}

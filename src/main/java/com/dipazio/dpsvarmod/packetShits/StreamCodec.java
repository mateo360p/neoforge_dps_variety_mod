package com.dipazio.dpsvarmod.packetShits;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.Utf8String;

public class StreamCodec {
    public static net.minecraft.network.codec.StreamCodec<RegistryFriendlyByteBuf, String> STRING_UTF8 = stringUtf8(32767);

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

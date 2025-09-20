package net.lightcode.v1_17R1.converter;

import net.lightcode.converter.NbtConverter;
import net.minecraft.nbt.TagParser;

public class v1_17R1NbtConverter implements NbtConverter<Object> {

    @Override
    public Object convertStringToNBTCompound(String nbtString) {
        try {
            return TagParser.parseTag(nbtString);
        } catch (Exception exception) {
            throw new RuntimeException("NBT parsing failed", exception);
        }
    }
}
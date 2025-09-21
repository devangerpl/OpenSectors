package io.github.fajzu.nms.v1_19R3.converter;

import io.github.fajzu.nms.api.converter.NbtConverter;
import net.minecraft.nbt.TagParser;

public class v1_19R3NbtConverter implements NbtConverter<Object> {

    @Override
    public Object convertStringToNBTCompound(String nbtString) {
        try {
            return TagParser.parseTag(nbtString);
        } catch (Exception exception) {
            throw new RuntimeException("NBT parsing failed", exception);
        }
    }
}
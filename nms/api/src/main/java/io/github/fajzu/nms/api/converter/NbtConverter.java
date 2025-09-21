package io.github.fajzu.nms.api.converter;

public interface NbtConverter<T> {

    T convertStringToNBTCompound(String nbtString);
}

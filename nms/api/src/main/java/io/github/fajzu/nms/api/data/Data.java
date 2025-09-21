package io.github.fajzu.nms.api.data;

public interface Data<K, T> {

    String saveData(K entityPlayer);

    void loadData(K entityPlayer,
                  T nbtTagCompound);
}

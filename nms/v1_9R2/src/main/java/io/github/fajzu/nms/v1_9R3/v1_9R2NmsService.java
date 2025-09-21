package io.github.fajzu.nms.v1_9R3;

import io.github.fajzu.nms.api.NmsService;
import io.github.fajzu.nms.api.actionbar.Actionbar;
import io.github.fajzu.nms.api.border.Border;
import io.github.fajzu.nms.api.converter.NbtConverter;
import io.github.fajzu.nms.api.data.Data;
import io.github.fajzu.nms.v1_9R3.border.v1_9R2Border;
import io.github.fajzu.nms.api.server.MinecraftServer;
import io.github.fajzu.nms.v1_9R3.actionbar.v1_9R2Actionbar;
import io.github.fajzu.nms.v1_9R3.converter.v1_9R2NbtConverter;
import io.github.fajzu.nms.v1_9R3.data.v1_9R2Data;
import io.github.fajzu.nms.v1_9R3.server.v1_9R2MinecraftServer;

public class v1_9R2NmsService implements NmsService {

    @Override
    public Actionbar actionBar() {
        return new v1_9R2Actionbar();
    }

    @Override
    public NbtConverter nbtConverter() {
        return new v1_9R2NbtConverter();
    }

    @Override
    public Data data() {
        return new v1_9R2Data();
    }

    @Override
    public MinecraftServer minecraftServer() {
        return new v1_9R2MinecraftServer();
    }

    @Override
    public Border border() {
        return new v1_9R2Border();
    }


}

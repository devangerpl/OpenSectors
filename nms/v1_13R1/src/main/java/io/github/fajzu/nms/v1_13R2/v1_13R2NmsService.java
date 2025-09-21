package io.github.fajzu.nms.v1_13R2;

import io.github.fajzu.nms.api.NmsService;
import io.github.fajzu.nms.api.actionbar.Actionbar;
import io.github.fajzu.nms.api.border.Border;
import io.github.fajzu.nms.api.converter.NbtConverter;
import io.github.fajzu.nms.api.data.Data;
import io.github.fajzu.nms.v1_13R2.actionbar.v1_13R2Actionbar;
import io.github.fajzu.nms.v1_13R2.border.v1_13R2Border;
import io.github.fajzu.nms.v1_13R2.converter.v1_13R2NbtConverter;
import io.github.fajzu.nms.v1_13R2.server.v1_13R2MinecraftServer;
import io.github.fajzu.nms.api.server.MinecraftServer;
import io.github.fajzu.nms.v1_13R2.data.v1_13R2Data;

public class v1_13R2NmsService implements NmsService {

    @Override
    public Actionbar actionBar() {
        return new v1_13R2Actionbar();
    }

    @Override
    public NbtConverter nbtConverter() {
        return new v1_13R2NbtConverter();
    }

    @Override
    public Data data() {
        return new v1_13R2Data();
    }

    @Override
    public MinecraftServer minecraftServer() {
        return new v1_13R2MinecraftServer();
    }

    @Override
    public Border border() {
        return new v1_13R2Border();
    }


}

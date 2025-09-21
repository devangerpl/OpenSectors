package io.github.fajzu.sectors.bukkit.user.repository;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.lettuce.core.api.sync.RedisCommands;
import io.github.fajzu.common.network.NetworkService;
import io.github.fajzu.sectors.bukkit.user.User;

public class UserRepository {

    private final RedisCommands<String, String> users;

    private final Gson gson;

    public UserRepository(NetworkService networkService) {
        this.users = networkService.databaseConnection().sync();

        this.gson = new GsonBuilder()
                .disableHtmlEscaping()
                .create();
    }

    public void update(User user) {
        this.users.hset("users", user.name(), this.gson.toJson(user));
    }

    public User fetch(String name) {
        return this.gson.fromJson(this.users.hget("users", name), User.class);
    }
}

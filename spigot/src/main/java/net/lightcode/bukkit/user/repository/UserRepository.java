package net.lightcode.bukkit.user.repository;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.lettuce.core.api.sync.RedisCommands;
import net.lightcode.NetworkService;
import net.lightcode.bukkit.user.User;

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
        return this.gson.fromJson(this.users.hget("users",name),User.class);
    }
}

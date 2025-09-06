package net.lightcode.bukkit.user.service;

import net.lightcode.NetworkService;
import net.lightcode.bukkit.user.User;
import net.lightcode.bukkit.user.repository.UserRepository;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class UserService {

    private final Map<UUID, User> users = new ConcurrentHashMap<>();

    private final UserRepository userRepository;

    public UserService(NetworkService networkService) {
        this.userRepository = new UserRepository(networkService);
    }

    public void create(UUID uuid,String name) {
        this.users.put(uuid,new User(name, uuid));
    }

    public void remove(UUID uuid) {
        this.users.remove(uuid);
    }

    public User find(UUID uuid) {
        return this.users.get(uuid);
    }

    public User fetch(String name) {
        return this.userRepository.fetch(name);
    }

    public Map<UUID, User> users() {
        return this.users;
    }

    public UserRepository userRepository() {
        return this.userRepository;
    }
}
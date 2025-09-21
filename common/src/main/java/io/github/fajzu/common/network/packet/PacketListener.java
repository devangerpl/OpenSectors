package io.github.fajzu.common.network.packet;

import io.lettuce.core.pubsub.RedisPubSubListener;

public abstract class PacketListener<T extends Packet> implements RedisPubSubListener<String, Packet> {
    private final Class<T> packetClass;

    public PacketListener(Class<T> packetClass) {
        this.packetClass = packetClass;
    }

    public abstract void handle(T packet);

    @Override
    public void message(String channel,
                        Packet packet) {
        if (!packet.getClass().isAssignableFrom(this.packetClass)) return;

        this.handle(this.packetClass.cast(packet));
    }


    @Override
    public void message(String pattern,
                        String channel,
                        Packet message) {

    }

    @Override
    public void subscribed(String channel,
                           long count) {

    }

    @Override
    public void psubscribed(String pattern,
                            long count) {

    }

    @Override
    public void unsubscribed(String channel,
                             long count) {

    }

    @Override
    public void punsubscribed(String pattern,
                              long count) {

    }

}
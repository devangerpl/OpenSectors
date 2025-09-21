package net.lightcode.network;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection;
import io.lettuce.core.support.ConnectionPoolSupport;
import net.lightcode.network.exception.ConnectionPublishException;
import net.lightcode.network.packet.Packet;
import net.lightcode.network.codec.MessagePackCodec;
import net.lightcode.network.packet.PacketListener;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import java.util.ArrayList;
import java.util.List;

public class NetworkService {

    private final List<String> subscribedChannels = new ArrayList<>();

    private final GenericObjectPool<StatefulRedisConnection<String, Packet>> connectionPool;
    private final StatefulRedisPubSubConnection<String, Packet> pubSubConnection;
    private final StatefulRedisConnection<String, String> databaseConnection;

    private String packetSender;

    public NetworkService(String address,
                          int port,
                          String password, String packetSender) {
        this.packetSender = packetSender;

        MessagePackCodec messagePackCodec = new MessagePackCodec();

        RedisClient redisClient = RedisClient.create(RedisURI.builder()
                .withHost(address)
                .withPort(port)
                .withPassword(password.toCharArray())
                .build());

        GenericObjectPoolConfig<StatefulRedisConnection<String, Packet>> poolConfig =
                new GenericObjectPoolConfig<>();
        poolConfig.setMinIdle(10);
        poolConfig.setMaxIdle(50);
        poolConfig.setMaxTotal(100);

        this.connectionPool = ConnectionPoolSupport.createGenericObjectPool(
                () -> redisClient.connect(
                        messagePackCodec
                ),
                poolConfig
        );

        this.pubSubConnection = redisClient.connectPubSub(
                messagePackCodec
        );

        this.databaseConnection = redisClient.connect();
    }

    public void shutdown() {
        this.pubSubConnection.sync().unsubscribe(
                this.subscribedChannels.toArray(
                        new String[0]
                )
        );

        this.pubSubConnection.close();
        this.databaseConnection.close();
        this.connectionPool.close();
    }

    public void publish(String channel,
                        Packet packet) {
        packet.sender(this.packetSender);

        try (StatefulRedisConnection<String, Packet> connection = this.connectionPool.borrowObject()) {
            connection.async().publish(
                    channel,
                    packet
            );
        } catch (Exception exception) {
            throw new ConnectionPublishException("Publishing packet failed: " + exception.getMessage());
        }
    }

    public void subscribe(String channel,
                          PacketListener<? extends Packet> listener) {
        this.pubSubConnection.addListener(listener);

        if (this.subscribedChannels.contains(channel)) return;

        this.pubSubConnection.sync().subscribe(channel);
        this.subscribedChannels.add(channel);
    }

    public StatefulRedisConnection<String, String> databaseConnection() {
        return this.databaseConnection;
    }
}
package net.lightcode.redis.codec;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.lettuce.core.codec.RedisCodec;
import net.lightcode.packet.Packet;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class GsonCodec implements RedisCodec<String, Packet> {


    private final Charset charset = StandardCharsets.UTF_8;
    private final Gson gson;

    public GsonCodec() {
        this.gson = new GsonBuilder()
                .serializeNulls()
                .disableHtmlEscaping()
                .setPrettyPrinting()
                .create();
    }

    @Override
    public String decodeKey(ByteBuffer bytes) {
        return charset.decode(bytes).toString();
    }

    @Override
    public Packet decodeValue(ByteBuffer bytes) {
        byte[] buffer = new byte[bytes.remaining()];
        bytes.get(buffer);

        String json = new String(buffer, charset);
        return gson.fromJson(json, Packet.class);
    }

    @Override
    public ByteBuffer encodeKey(String key) {
        return charset.encode(key);
    }

    @Override
    public ByteBuffer encodeValue(Packet value) {
        String json = gson.toJson(value);
        return ByteBuffer.wrap(json.getBytes(charset));
    }
}
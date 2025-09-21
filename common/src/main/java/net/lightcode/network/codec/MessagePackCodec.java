package net.lightcode.network.codec;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.lettuce.core.codec.RedisCodec;
import net.lightcode.network.exception.CodecDeserializationException;
import net.lightcode.network.exception.CodecSerializationException;
import net.lightcode.network.packet.Packet;
import org.msgpack.jackson.dataformat.MessagePackFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class MessagePackCodec implements RedisCodec<String, Packet> {

    private final Charset charset = StandardCharsets.UTF_8;

    private final ObjectMapper objectMapper = new ObjectMapper(new MessagePackFactory())
            .setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL)
            .setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
            .enable(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN)
            .enable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY);

    public MessagePackCodec() {
        this.objectMapper.registerSubtypes(Packet.class);
    }

    @Override
    public String decodeKey(ByteBuffer bytes) {
        return charset.decode(bytes).toString();
    }

    @Override
    public Packet decodeValue(ByteBuffer bytes) {
        byte[] buffer = new byte[bytes.remaining()];
        bytes.get(buffer);

        try {
            return this.objectMapper.readValue(buffer, Packet.class);
        } catch (IOException exception) {
            throw new CodecDeserializationException("decoding packet failed: " + exception.getMessage());
        }
    }

    @Override
    public ByteBuffer encodeKey(String key) {
        return charset.encode(key);
    }

    @Override
    public ByteBuffer encodeValue(Packet value) {
        try {
            return ByteBuffer.wrap(this.objectMapper.writeValueAsBytes(value));
        } catch (JsonProcessingException exception) {
            throw new CodecSerializationException("encoding packet failed: " + exception.getMessage());
        }
    }
}
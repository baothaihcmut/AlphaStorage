package vn.anpha.storage.config;

import java.nio.ByteBuffer;
import java.util.UUID;

import jakarta.persistence.AttributeConverter;

public class UUIDConverter implements AttributeConverter<UUID, byte[]> {
    @Override
    public byte[] convertToDatabaseColumn(UUID attribute) {
        if (attribute == null) {
            return null;
        }
        ByteBuffer buffer = ByteBuffer.wrap(new byte[16]);
        buffer.putLong(attribute.getMostSignificantBits());
        buffer.putLong(attribute.getLeastSignificantBits());
        return buffer.array();
    }

    @Override
    public UUID convertToEntityAttribute(byte[] dbData) {
        if (dbData == null || dbData.length != 16) {
            return null;
        }
        ByteBuffer buffer = ByteBuffer.wrap(dbData);
        long mostSigBits = buffer.getLong();
        long leastSigBits = buffer.getLong();
        return new UUID(mostSigBits, leastSigBits);
    }
}

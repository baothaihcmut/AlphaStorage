package vn.anpha.storage.common;

import java.nio.ByteBuffer;
import java.util.UUID;

public class BaseDTO {
    protected UUID bytetoUuid(byte[] input) {
        if (input == null) {
            return null;
        }
        ByteBuffer buffer = ByteBuffer.wrap(input);
        long mostSignificantBits = buffer.getLong();
        long leastSignificantBits = buffer.getLong();
        return new UUID(mostSignificantBits, leastSignificantBits);
    }
}

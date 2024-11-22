package vn.anpha.storage.config;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;

@Component("uuidConverter")
public class UUIDConverter {
    public UUID fromBytes(Optional<byte[]> bytes) {
        if (!bytes.isPresent()) {
            return null;
        }
        return UUID.nameUUIDFromBytes(bytes.get());
    }
}

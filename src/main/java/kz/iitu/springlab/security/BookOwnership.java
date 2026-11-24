package kz.iitu.springlab.security;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/** Variant 5: who owns which book (kept in memory, like the book catalogue itself). */
@Component("bookOwnership")
public class BookOwnership {

    private final Map<Long, String> owners = new ConcurrentHashMap<>();

    public BookOwnership() {
        owners.put(1L, "user");    // seed: Effective Java belongs to user
        owners.put(2L, "admin");
        owners.put(3L, "admin");
    }

    public boolean isOwner(long bookId, String username) {
        return username != null && username.equals(owners.get(bookId));
    }

    public void assign(Long bookId, String owner) {
        if (bookId != null) owners.put(bookId, owner);
    }

    public Map<Long, String> asMap() {
        return Map.copyOf(owners);
    }
}

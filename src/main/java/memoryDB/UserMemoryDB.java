package memoryDB;


import domain.entity.User;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Getter
@Setter
public class UserMemoryDB {
    public static AtomicLong id = new AtomicLong(0);
    public static ConcurrentHashMap<Long, User> userMemoryDB= new ConcurrentHashMap<>();
}

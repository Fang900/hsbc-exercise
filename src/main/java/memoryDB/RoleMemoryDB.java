package memoryDB;

import domain.entity.Role;

import java.util.HashMap;

public class RoleMemoryDB {
    public static int id = 0;
    public static HashMap<Long, Role> roleMemoryDB= new HashMap<>();
}

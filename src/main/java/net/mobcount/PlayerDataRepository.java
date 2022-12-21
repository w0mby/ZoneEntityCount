package net.mobcount;

import java.util.UUID;

import net.mobcount.entities.PlayerData;

public interface PlayerDataRepository {

    PlayerData get(UUID uuid, String serverName);
    boolean update(PlayerData playerData);
}

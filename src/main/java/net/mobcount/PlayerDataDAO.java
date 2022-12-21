package net.mobcount;

import java.io.IOException;
import java.util.UUID;

import net.mobcount.entities.PlayerData;

public interface PlayerDataDAO {
    PlayerData getOfflinePlayerData(UUID uuid, String serverName);
    void saveOfflinePlayerData(PlayerData playerData) throws IOException;
}

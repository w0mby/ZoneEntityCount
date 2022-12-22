package net.mobcount.application;

import java.io.IOException;
import java.util.UUID;

import com.google.inject.ImplementedBy;

import net.mobcount.domain.entities.PlayerData;
import net.mobcount.infrastructure.PlayerDataGsonDAO;

@ImplementedBy(PlayerDataGsonDAO.class)
public interface PlayerDataDAO {
    PlayerData getOfflinePlayerData(UUID uuid, String serverName);
    void saveOfflinePlayerData(PlayerData playerData) throws IOException;
}

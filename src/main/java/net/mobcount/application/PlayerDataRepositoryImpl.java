package net.mobcount.application;

import java.io.IOException;
import java.util.UUID;

import com.google.inject.Inject;

import net.mobcount.domain.entities.PlayerData;

public class PlayerDataRepositoryImpl implements PlayerDataRepository {
    private PlayerDataDAO playerDataDao;
    
    @Inject
    public PlayerDataRepositoryImpl(PlayerDataDAO playerDataDao)
    {
        this.playerDataDao = playerDataDao;
    }
    
    @Override
    public PlayerData get(UUID uuid, String serverName) {

        PlayerData playerData = playerDataDao.getOfflinePlayerData(uuid, serverName);
        
        return playerData;
    }

    @Override
    public boolean update(PlayerData playerData) {
        try {
            playerDataDao.saveOfflinePlayerData( playerData );
        } catch (IOException e) {
            //adding logs;
            return false;
        }
        return true;
    }

}

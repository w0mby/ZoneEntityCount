package net.mobcount.infrastructure;

import java.io.IOException;
import java.util.UUID;

import net.mobcount.PlayerDataDAO;
import net.mobcount.PlayerDataRepository;
import net.mobcount.entities.PlayerData;

public class PlayerDataRepositoryImpl implements PlayerDataRepository {
    private PlayerDataDAO playerDataDao;
    
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

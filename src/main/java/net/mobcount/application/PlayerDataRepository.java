package net.mobcount.application;

import java.util.UUID;

import com.google.inject.ImplementedBy;

import net.mobcount.domain.entities.PlayerData;

@ImplementedBy(PlayerDataRepositoryImpl.class)
public interface PlayerDataRepository {

    PlayerData get(UUID uuid, String serverName);
    boolean update(PlayerData playerData);
}

package net.mobcount.infrastructure;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import dev.the_fireplace.annotateddi.api.di.Implementation;
import net.minecraft.util.math.Vec3d;
import net.mobcount.application.PlayerDataDAO;
import net.mobcount.domain.entities.PlayerData;

@Implementation
public class PlayerDataGsonDAO implements PlayerDataDAO {

    private final Gson GSON = new GsonBuilder()
            .disableHtmlEscaping()
            .setLenient()
            .registerTypeAdapter(Vec3d.class, new Vec3Adapter())
            .registerTypeAdapterFactory(new RecordTypeAdapterFactory())
            .create();

    @NotNull
    public PlayerData getOfflinePlayerData(UUID uuid, String serverName) {
        PlayerData playerData = null;
        try {
            Path DATA_PATH = ConfigManager.CONFIG_DIR.resolve("data" + uuid.toString() + "_" + serverName + ".json");
            String serialized = FileUtils.readFileToString(new File(DATA_PATH.toUri()), Charset.forName("UTF-8"));
            playerData = GSON.fromJson(serialized, PlayerData.class);
        } catch (IOException e) {
            PlayerData p = new PlayerData();
            p.setPlayerUuid(uuid.toString());
            p.setServerName(serverName);
            return p;
        }
        return playerData;
    }

    public void saveOfflinePlayerData(PlayerData playerData) throws IOException {
        String serialized = GSON.toJson(playerData);
        try {
            Path DATA_PATH = ConfigManager.CONFIG_DIR.resolve("data" + playerData.getUuid() + "_" + playerData.getServerName() + ".json");
            FileUtils.writeStringToFile(new File(DATA_PATH.toUri()), serialized, Charset.forName("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }
}
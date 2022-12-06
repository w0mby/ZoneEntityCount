package net.mobcount.util;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.Vec3d;

public class DataStorage {
    public static Vec3d currentPos1 = null;
    public static Vec3d currentPos2 = null;
    public static String currentType;

    public static final Gson GSON = new GsonBuilder()
            .disableHtmlEscaping()
            .setLenient()
            .registerTypeAdapter(Vec3d.class, new Vec3Adapter())
            .registerTypeAdapterFactory(new RecordTypeAdapterFactory())
            .create();


    @NotNull
    public static PlayerData getOfflinePlayerData(ClientPlayerEntity player) {
        var server = player.getServer();
        String serverName = "local";
        if(server != null)
        {
            serverName = server.getName();
        }
        return getOfflinePlayerData( player.getGameProfile().getId(), serverName);
    }

    @NotNull
    public static PlayerData getOfflinePlayerData(UUID uuid, String serverName) {
        PlayerData playerData = null;
        try {
            Path DATA_PATH = ConfigManager.CONFIG_DIR.resolve("data" + uuid.toString() + "_" + serverName + ".json");
            String serialized = FileUtils.readFileToString(new File(DATA_PATH.toUri()), Charset.forName("UTF-8"));
            playerData = GSON.fromJson(serialized, PlayerData.class);
        } catch (IOException e) {
            PlayerData p = new PlayerData();
            p.PlayerUuid = uuid.toString();
            p.ServerName = serverName;
            return p;
        }
        return playerData;
    }

    public static void saveOfflinePlayerData(PlayerData playerData) {
        String serialized = GSON.toJson(playerData);
        try {
            Path DATA_PATH = ConfigManager.CONFIG_DIR.resolve("data" + playerData.PlayerUuid.toString() + "_" + playerData.ServerName + ".json");
            FileUtils.writeStringToFile(new File(DATA_PATH.toUri()), serialized, Charset.forName("UTF-8"));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
package net.mobcount.domain.entities;

import java.util.HashMap;
import java.util.Map;

public class PlayerData {

    private final Map<String, Zone> zones = new HashMap<>();
    private String uuid;
    private String serverName;
    public Map<String, Zone> getZones() {
        return zones;
    }
    public Zone getZone(String zone) {
        if(!zones.containsKey(zone))
		{
            return null;
		}
        return zones.get(zone);
    }
    public void setPlayerUuid(String playerUuid) {
        this.uuid = playerUuid;
    }
    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getUuid() {
        return this.uuid;
    }
    public String getServerName() {
        return this.serverName;
    }
    public void addZone(String zoneName, Zone zone) {
        zones.put(zoneName, zone);
    }
    public void removeZone(String zoneName) {
        zones.remove(zoneName);
    }

}
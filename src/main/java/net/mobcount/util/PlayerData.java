package net.mobcount.util;

import java.util.HashMap;
import java.util.Map;

public class PlayerData {

    public final Map<String, Zone> zones = new HashMap<>();
    public String PlayerUuid;
    public String ServerName;
    public Map<String, Zone> getZones() {
        return zones;
    }

}
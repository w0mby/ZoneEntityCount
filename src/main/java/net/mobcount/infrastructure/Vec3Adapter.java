package net.mobcount.infrastructure;
import com.google.gson.*;
import net.minecraft.util.math.Vec3d;
import java.lang.reflect.Type;

public class Vec3Adapter implements JsonSerializer<Vec3d>, JsonDeserializer<Vec3d> {

    @Override
    public JsonElement serialize(Vec3d src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("x", src.x);
        jsonObject.addProperty("y", src.y);
        jsonObject.addProperty("z", src.z);
        return jsonObject;
    }

    @Override
    public Vec3d deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        return new Vec3d(
                jsonObject.getAsJsonPrimitive("x").getAsDouble(),
                jsonObject.getAsJsonPrimitive("y").getAsDouble(),
                jsonObject.getAsJsonPrimitive("z").getAsDouble()
        );
    }
}
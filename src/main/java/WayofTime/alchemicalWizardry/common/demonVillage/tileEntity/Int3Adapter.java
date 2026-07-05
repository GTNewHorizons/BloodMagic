package WayofTime.alchemicalWizardry.common.demonVillage.tileEntity;

import java.io.IOException;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import WayofTime.alchemicalWizardry.api.Int3;

public class Int3Adapter extends TypeAdapter<Int3> {

    @Override
    public Int3 read(JsonReader reader) throws IOException {
        int x = 0, y = 0, z = 0;

        reader.beginObject();
        while (reader.hasNext()) {
            switch (reader.nextName()) {
                case "xCoord" -> x = reader.nextInt();
                case "yCoord" -> y = reader.nextInt();
                case "zCoord" -> z = reader.nextInt();
                default -> reader.skipValue();
            }
        }
        reader.endObject();

        return new Int3(x, y, z);
    }

    @Override
    public void write(JsonWriter writer, Int3 int3) throws IOException {
        writer.beginObject();
        writer.name("xCoord").value(int3.x());
        writer.name("yCoord").value(int3.y());
        writer.name("zCoord").value(int3.z());
        writer.endObject();
    }
}

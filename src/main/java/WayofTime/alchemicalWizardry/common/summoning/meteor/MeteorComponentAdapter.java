package WayofTime.alchemicalWizardry.common.summoning.meteor;

import java.io.IOException;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

class MeteorComponentAdapter extends TypeAdapter<MeteorComponent> {

    @Override
    public MeteorComponent read(JsonReader reader) throws IOException {
        return MeteorComponent.parseString(reader.nextString());
    }

    @Override
    public void write(JsonWriter writer, MeteorComponent component) throws IOException {
        // Not implemented because it is not needed.
    }
}

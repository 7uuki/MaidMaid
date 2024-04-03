package de.fhandshit.maidmaid.data;

import androidx.room.TypeConverter;

import java.nio.ByteBuffer;
import java.util.UUID;

import de.fhandshit.maidmaid.data.model.Category;

public class Converters {
    @TypeConverter
    public UUID asUuid(byte[] bytes) {
        ByteBuffer bb = ByteBuffer.wrap(bytes);
        long firstLong = bb.getLong();
        long secondLong = bb.getLong();
        return new UUID(firstLong, secondLong);
    }

    @TypeConverter
    public byte[] asBytes(UUID uuid) {
        ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
        bb.putLong(uuid.getMostSignificantBits());
        bb.putLong(uuid.getLeastSignificantBits());
        return bb.array();
    }

    @TypeConverter
    public static String categoryToNameString(Category category) {
        return  category == null ? null : category.getName();
    }

    @TypeConverter
    public static Category stringToCategory(String name){
        return new Category(name);

    }
}
package mcjty.theoneprobe.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.util.math.BlockPos;

import java.util.Collection;

public class NetworkTools {

    public static CompoundTag readNBT(ByteBuf dataIn) {
        PacketByteBuf buf = new PacketByteBuf(dataIn);
        return buf.readCompoundTag();
    }

    public static void writeNBT(ByteBuf dataOut, CompoundTag nbt) {
        PacketByteBuf buf = new PacketByteBuf(dataOut);
        try {
            buf.writeCompoundTag(nbt);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /// This function supports itemstacks with more then 64 items.
    public static ItemStack readItemStack(ByteBuf dataIn) {
        PacketByteBuf buf = new PacketByteBuf(dataIn);
        CompoundTag nbt = buf.readCompoundTag();
        ItemStack stack = ItemStack.fromTag(nbt);
        stack.setAmount(buf.readInt());
        return stack;
    }

    /// This function supports itemstacks with more then 64 items.
    public static void writeItemStack(ByteBuf dataOut, ItemStack itemStack) {
        PacketByteBuf buf = new PacketByteBuf(dataOut);
        CompoundTag nbt = new CompoundTag();
        itemStack.toTag(nbt);
        try {
            buf.writeCompoundTag(nbt);
            buf.writeInt(itemStack.getAmount());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String readString(ByteBuf dataIn) {
        int s = dataIn.readInt();
        if (s == -1) {
            return null;
        }
        if (s == 0) {
            return "";
        }
        byte[] dst = new byte[s];
        dataIn.readBytes(dst);
        return new String(dst);
    }

    public static void writeString(ByteBuf dataOut, String str) {
        if (str == null) {
            dataOut.writeInt(-1);
            return;
        }
        byte[] bytes = str.getBytes();
        dataOut.writeInt(bytes.length);
        if (bytes.length > 0) {
            dataOut.writeBytes(bytes);
        }
    }

    public static String readStringUTF8(ByteBuf dataIn) {
        int s = dataIn.readInt();
        if (s == -1) {
            return null;
        }
        if (s == 0) {
            return "";
        }
        byte[] dst = new byte[s];
        dataIn.readBytes(dst);
        return new String(dst, java.nio.charset.StandardCharsets.UTF_8);
    }

    public static void writeStringUTF8(ByteBuf dataOut, String str) {
        if (str == null) {
            dataOut.writeInt(-1);
            return;
        }
        byte[] bytes = str.getBytes(java.nio.charset.StandardCharsets.UTF_8);
        dataOut.writeInt(bytes.length);
        if (bytes.length > 0) {
            dataOut.writeBytes(bytes);
        }
    }

    public static BlockPos readPos(ByteBuf dataIn) {
        return new BlockPos(dataIn.readInt(), dataIn.readInt(), dataIn.readInt());
    }

    public static void writePos(ByteBuf dataOut, BlockPos pos) {
        dataOut.writeInt(pos.getX());
        dataOut.writeInt(pos.getY());
        dataOut.writeInt(pos.getZ());
    }

    public static <T extends Enum<T>> void writeEnum(ByteBuf buf, T value, T nullValue) {
        if (value == null) {
            buf.writeInt(nullValue.ordinal());
        } else {
            buf.writeInt(value.ordinal());
        }
    }

    public static <T extends Enum<T>> T readEnum(ByteBuf buf, T[] values) {
        return values[buf.readInt()];
    }

    public static <T extends Enum<T>> void writeEnumCollection(ByteBuf buf, Collection<T> collection) {
        buf.writeInt(collection.size());
        for (T type : collection) {
            buf.writeInt(type.ordinal());
        }
    }

    public static <T extends Enum<T>> void readEnumCollection(ByteBuf buf, Collection<T> collection, T[] values) {
        collection.clear();
        int size = buf.readInt();
        for (int i = 0; i < size; i++) {
            collection.add(values[buf.readInt()]);
        }
    }

    public static void writeFloat(ByteBuf buf, Float f) {
        if (f != null) {
            buf.writeBoolean(true);
            buf.writeFloat(f);
        } else {
            buf.writeBoolean(false);
        }
    }

    public static Float readFloat(ByteBuf buf) {
        if (buf.readBoolean()) {
            return buf.readFloat();
        } else {
            return null;
        }
    }


    public static void writeItemStack(PacketByteBuf to, ItemStack stack) {
        PacketByteBuf pb = new PacketByteBuf(to);
        pb.writeItemStack(stack);
    }

    public static ItemStack readItemStack(PacketByteBuf from) {
        PacketByteBuf pb = new PacketByteBuf(from);
        return pb.readItemStack();
    }


}

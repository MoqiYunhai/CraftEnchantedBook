package org.yunhai.craft_enchanted_book.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import org.yunhai.craft_enchanted_book.pattern.PatternRegistry;

import java.util.BitSet;
import java.util.function.Supplier;

/**
 * 验证图案的网络消息 - 使用 BitSet 优化
 */
public class ValidatePatternMessage {
    private final BitSet pattern;
    private final int quillLevel;
    
    public ValidatePatternMessage(BitSet pattern, int quillLevel) {
        this.pattern = pattern;
        this.quillLevel = quillLevel;
    }
    
    public static void encode(ValidatePatternMessage msg, FriendlyByteBuf buffer) {
        buffer.writeInt(msg.quillLevel);
        // BitSet 序列化
        long[] words = msg.pattern.toLongArray();
        buffer.writeInt(words.length);
        for (long word : words) {
            buffer.writeLong(word);
        }
    }
    
    public static ValidatePatternMessage decode(FriendlyByteBuf buffer) {
        int quillLevel = buffer.readInt();
        int wordCount = buffer.readInt();
        long[] words = new long[wordCount];
        for (int i = 0; i < wordCount; i++) {
            words[i] = buffer.readLong();
        }
        BitSet pattern = BitSet.valueOf(words);
        
        return new ValidatePatternMessage(pattern, quillLevel);
    }
    
    public static void handle(ValidatePatternMessage msg, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            if (context.getDirection().getReceptionSide().isServer()) {
                PatternValidationHandler.handleValidation(msg, context);
            }
        });
        context.setPacketHandled(true);
    }
    
    public BitSet getPattern() {
        return pattern;
    }
    
    public int getQuillLevel() {
        return quillLevel;
    }
}

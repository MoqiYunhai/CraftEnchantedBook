package org.yunhai.craft_enchanted_book.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import org.yunhai.craft_enchanted_book.CraftEnchantedBook;

public class ModNetwork {
    private static final String PROTOCOL_VERSION = "1";
    
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
        new ResourceLocation(CraftEnchantedBook.MODID, "main"),
        () -> PROTOCOL_VERSION,
        PROTOCOL_VERSION::equals,
        PROTOCOL_VERSION::equals
    );
    
    private static int packetId = 0;
    
    public static void register() {
        // 注册验证图案消息
        INSTANCE.registerMessage(
            packetId++,
            ValidatePatternMessage.class,
            ValidatePatternMessage::encode,
            ValidatePatternMessage::decode,
            ValidatePatternMessage::handle
        );
        
        // 注册充能消息
        INSTANCE.registerMessage(
            packetId++,
            ChargeMessage.class,
            ChargeMessage::encode,
            ChargeMessage::decode,
            ChargeMessage::handle
        );
    }
}

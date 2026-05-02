package org.yunhai.craft_enchanted_book.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * 充能网络消息 - 处理羽毛笔充能逻辑
 */
public class ChargeMessage {
    private final InteractionHand hand;
    
    public ChargeMessage(InteractionHand hand) {
        this.hand = hand;
    }
    
    public static void encode(ChargeMessage msg, FriendlyByteBuf buffer) {
        buffer.writeInt(msg.hand.ordinal());
    }
    
    public static ChargeMessage decode(FriendlyByteBuf buffer) {
        int handOrdinal = buffer.readInt();
        InteractionHand hand = InteractionHand.values()[handOrdinal];
        return new ChargeMessage(hand);
    }
    
    public static void handle(ChargeMessage msg, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            if (context.getDirection().getReceptionSide().isServer()) {
                ChargeValidationHandler.handleCharging(msg, context);
            }
        });
        context.setPacketHandled(true);
    }
    
    public InteractionHand getHand() {
        return hand;
    }
}

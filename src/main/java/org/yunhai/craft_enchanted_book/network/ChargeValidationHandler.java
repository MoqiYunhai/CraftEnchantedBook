package org.yunhai.craft_enchanted_book.network;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.network.NetworkEvent;
import org.yunhai.craft_enchanted_book.item.EnchantedQuillItem;

/**
 * 充能验证处理器 - 处理羽毛笔充能逻辑
 */
public class ChargeValidationHandler {
    
    public static void handleCharging(ChargeMessage msg, NetworkEvent.Context context) {
        ServerPlayer player = context.getSender();
        if (player == null) return;
        
        InteractionHand hand = msg.getHand();
        ItemStack quillStack = player.getItemInHand(hand);
        
        // 检查是否是羽毛笔
        if (!(quillStack.getItem() instanceof EnchantedQuillItem)) {
            return;
        }
        
        // 检查另一只手是否持有青金石
        InteractionHand offHand = (hand == InteractionHand.MAIN_HAND) ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
        ItemStack offHandStack = player.getItemInHand(offHand);
        
        if (!offHandStack.is(Items.LAPIS_LAZULI)) {
            return;
        }
        
        // 检查当前能量是否已达上限
        int currentEnergy = EnchantedQuillItem.getEnergy(quillStack);
        int maxEnergy = EnchantedQuillItem.getMaxEnergy(quillStack);
        
        if (currentEnergy >= maxEnergy) {
            // 已达上限，播放失败音效
            player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.UI_BUTTON_CLICK.value(), SoundSource.PLAYERS, 0.5F, 0.5F);
            return;
        }
        
        // 消耗一个青金石并增加1点能量
        offHandStack.shrink(1);
        EnchantedQuillItem.addEnergy(quillStack, 1);
        
        // 播放充能成功音效
        player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 0.8F, 1.0F);
        
        // 同步物品栏到客户端
        player.containerMenu.broadcastChanges();
    }
}

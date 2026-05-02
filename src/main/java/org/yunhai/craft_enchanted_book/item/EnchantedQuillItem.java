package org.yunhai.craft_enchanted_book.item;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.yunhai.craft_enchanted_book.gui.DrawingScreen;
import org.yunhai.craft_enchanted_book.init.ModItems;
import org.yunhai.craft_enchanted_book.network.ModNetwork;
import org.yunhai.craft_enchanted_book.network.ChargeMessage;

import javax.annotation.Nullable;
import java.util.List;

/**
 * 特殊羽毛笔物品 - 右键打开绘制GUI
 * 分三个等级：1=普通, 2=高级, 3=大师
 * 具有成长值系统和能量属性
 */
public class EnchantedQuillItem extends Item {
    private final int level;
    
    public EnchantedQuillItem(Properties properties, int level) {
        super(properties);
        this.level = level;
    }
    
    public int getLevel() {
        return level;
    }
    
    /**
     * 从 ItemStack 获取当前等级
     */
    public static int getLevelFromStack(ItemStack stack) {
        if (stack.getItem() instanceof EnchantedQuillItem quill) {
            return quill.getLevel();
        }
        return 0;
    }
    
    /**
     * 获取当前成长值
     */
    public static int getExperience(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag == null) return 0;
        return tag.getInt("Experience");
    }
    
    /**
     * 设置成长值
     */
    public static void setExperience(ItemStack stack, int exp) {
        CompoundTag tag = stack.getOrCreateTag();
        tag.putInt("Experience", exp);
    }
    
    /**
     * 增加成长值并检查是否升级
     * @return 如果升级了，返回新的 ItemStack；否则返回 null
     */
    public static ItemStack addExperience(ItemStack stack, int amount) {
        int currentExp = getExperience(stack);
        int newExp = currentExp + amount;
        
        // 根据当前等级判断是否需要升级
        EnchantedQuillItem item = (EnchantedQuillItem) stack.getItem();
        
        if (item.level == 1 && newExp >= 10) {
            // 普通→高级（保留多余的经验）
            return createUpgradedQuill(2, newExp - 10);
        } else if (item.level == 2 && newExp >= 30) {
            // 高级→大师（保留多余的经验）
            return createUpgradedQuill(3, newExp - 30);
        } else {
            setExperience(stack, newExp);
            return null; // 没有升级
        }
    }
    
    /**
     * 获取当前能量值
     */
    public static int getEnergy(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag == null) return 0;
        return tag.getInt("Energy");
    }
    
    /**
     * 设置能量值
     */
    public static void setEnergy(ItemStack stack, int energy) {
        CompoundTag tag = stack.getOrCreateTag();
        tag.putInt("Energy", energy);
    }
    
    /**
     * 增加能量值（不超过上限）
     */
    public static void addEnergy(ItemStack stack, int amount) {
        int currentEnergy = getEnergy(stack);
        int maxEnergy = getMaxEnergy(stack);
        int newEnergy = Math.min(currentEnergy + amount, maxEnergy);
        setEnergy(stack, newEnergy);
    }
    
    /**
     * 获取最大能量上限（根据等级）
     */
    public static int getMaxEnergy(ItemStack stack) {
        EnchantedQuillItem item = (EnchantedQuillItem) stack.getItem();
        return switch (item.level) {
            case 1 -> 10;   // 普通羽毛笔上限10
            case 2 -> 20;   // 高级羽毛笔上限20
            case 3 -> 30;   // 大师羽毛笔上限30
            default -> 10;
        };
    }
    
    /**
     * 创建升级后的羽毛笔（保留能量值）
     */
    private static ItemStack createUpgradedQuill(int newLevel, int remainingExp) {
        Item newItem = switch (newLevel) {
            case 2 -> ModItems.QUILL_ADVANCED.get();
            case 3 -> ModItems.QUILL_MASTER.get();
            default -> null;
        };
        
        if (newItem != null) {
            ItemStack newStack = new ItemStack(newItem, 1);
            setExperience(newStack, remainingExp);
            // 升级时保留当前能量值（如果新上限更高）
            return newStack;
        }
        return null;
    }
    
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        
        // 检查是否在充能模式（左手持有青金石）
        InteractionHand offHand = (hand == InteractionHand.MAIN_HAND) ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
        ItemStack offHandStack = player.getItemInHand(offHand);
        
        if (offHandStack.is(Items.LAPIS_LAZULI)) {
            // 充能模式：立即发送充能请求到服务器
            if (level.isClientSide()) {
                ModNetwork.INSTANCE.sendToServer(new ChargeMessage(hand));
            }
            return InteractionResultHolder.success(itemStack);
        }
        
        // 正常模式：打开绘制GUI
        if (level.isClientSide()) {
            Minecraft mc = Minecraft.getInstance();
            if (mc.screen == null) {
                mc.setScreen(new DrawingScreen(this.level));
            }
        }
        
        return InteractionResultHolder.success(itemStack);
    }
    
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag flag) {
        String levelText = switch (level) {
            case 1 -> "§7普通羽毛笔";
            case 2 -> "§a高级羽毛笔";
            case 3 -> "§6大师羽毛笔";
            default -> "未知等级";
        };
        tooltip.add(Component.literal(levelText));
        
        // 显示成长值（只对普通和高级显示）
        if (level < 3) {
            int exp = getExperience(stack);
            int required = (level == 1) ? 10 : 30;
            tooltip.add(Component.literal(String.format("§b成长值: %d / %d", exp, required)));
        } else {
            tooltip.add(Component.literal("§6已达最高等级"));
        }
        
        // 显示能量值
        int energy = getEnergy(stack);
        int maxEnergy = getMaxEnergy(stack);
        tooltip.add(Component.literal(String.format("§9能量: %d / %d", energy, maxEnergy)));
        
        String compatText = switch (level) {
            case 1 -> "§7可制作 I 级附魔";
            case 2 -> "§a可制作 I-II 级附魔";
            case 3 -> "§6可制作 I-III 级附魔";
            default -> "";
        };
        tooltip.add(Component.literal(compatText));
    }
}

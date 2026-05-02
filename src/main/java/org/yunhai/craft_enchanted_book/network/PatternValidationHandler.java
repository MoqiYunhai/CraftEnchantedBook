package org.yunhai.craft_enchanted_book.network;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;
import org.yunhai.craft_enchanted_book.item.EnchantedQuillItem;
import org.yunhai.craft_enchanted_book.pattern.PatternRegistry;

import java.util.BitSet;
import java.util.Optional;
import java.util.Random;

/**
 * 图案验证处理器
 */
public class PatternValidationHandler {
    private static final Random RANDOM = new Random();
    
    public static void handleValidation(ValidatePatternMessage msg, NetworkEvent.Context context) {
        ServerPlayer player = context.getSender();
        if (player == null) return;
        
        // 验证图案
        PatternRegistry.EnchantmentDefinition match = PatternRegistry.matchPattern(msg.getPattern());
        
        if (match != null) {
            // 成功匹配 - 创建附魔书（传递羽毛笔等级）
            handleSuccess(player, match, msg.getQuillLevel());
        } else {
            // 匹配失败
            handleFailure(player);
        }
    }
    
    private static void handleSuccess(ServerPlayer player, PatternRegistry.EnchantmentDefinition match, int quillLevel) {
        // 获取玩家副手物品（应该是书）
        ItemStack offhandItem = player.getOffhandItem();
        
        if (!offhandItem.is(Items.BOOK)) {
            player.sendSystemMessage(Component.literal("§c你的副手必须持有书！"));
            return;
        }
        
        // 获取玩家主手的羽毛笔并增加成长值
        ItemStack mainHandItem = player.getMainHandItem();
        if (mainHandItem.getItem() instanceof EnchantedQuillItem) {
            // 检查能量是否足够
            int currentEnergy = EnchantedQuillItem.getEnergy(mainHandItem);
            int energyCost = calculateEnergyCost(quillLevel);
            
            if (currentEnergy < energyCost) {
                player.sendSystemMessage(Component.literal("§c能量不足！需要 " + energyCost + " 点能量，当前只有 " + currentEnergy + " 点。"));
                player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.UI_BUTTON_CLICK.value(), SoundSource.PLAYERS, 0.5F, 0.5F);
                return;
            }
            
            // 消耗能量
            EnchantedQuillItem.setEnergy(mainHandItem, currentEnergy - energyCost);
            
            // 增加成长值
            ItemStack upgradedStack = EnchantedQuillItem.addExperience(mainHandItem, 1);
            
            // 如果升级了，替换物品
            if (upgradedStack != null) {
                int newLevel = EnchantedQuillItem.getLevelFromStack(upgradedStack);
                String levelName = switch (newLevel) {
                    case 2 -> "高级羽毛笔";
                    case 3 -> "大师羽毛笔";
                    default -> "未知等级";
                };
                player.sendSystemMessage(Component.literal("§a✨ 恭喜！你的羽毛笔升级为 " + levelName + "！"));
                
                // 替换主手物品
                player.getInventory().setItem(player.getInventory().selected, upgradedStack);
            }
            
            // 显示能量消耗信息
            if (energyCost > 0) {
                player.sendSystemMessage(Component.literal("§9消耗了 " + energyCost + " 点能量"));
            } else {
                player.sendSystemMessage(Component.literal("§a✨ 大师技艺！本次未消耗能量！"));
            }
        }
        
        int bookCount = offhandItem.getCount();
        
        // 获取附魔
        Optional<Enchantment> enchantmentOpt = Optional.ofNullable(ForgeRegistries.ENCHANTMENTS.getValue(
                new ResourceLocation("minecraft", match.getEnchantmentId())
        ));
        
        if (enchantmentOpt.isEmpty()) {
            player.sendSystemMessage(Component.literal("§c附魔不存在！"));
            return;
        }
        
        Enchantment enchantment = enchantmentOpt.get();
        
        // 根据羽毛笔等级决定附魔等级（向下兼容）
        int enchantLevel = Math.min(quillLevel, getMaxLevel(enchantment));
        
        // 创建附魔书
        ItemStack enchantedBook = new ItemStack(Items.ENCHANTED_BOOK);
        EnchantmentHelper.setEnchantments(java.util.Map.of(enchantment, enchantLevel), enchantedBook);
        
        // 消耗一本书
        if (bookCount > 1) {
            // 如果书的数量大于1，减少一个并掉落附魔书
            offhandItem.shrink(1);
            
            // 在玩家位置掉落附魔书
            ItemEntity itemEntity = new ItemEntity(
                player.level(),
                player.getX(),
                player.getY(),
                player.getZ(),
                enchantedBook
            );
            player.level().addFreshEntity(itemEntity);
            
            player.sendSystemMessage(Component.literal("§a成功制作附魔书！已掉落在地面上。"));
        } else {
            // 如果只有一本书，直接替换
            player.getInventory().setItem(player.getInventory().selected, enchantedBook);
            player.sendSystemMessage(Component.literal("§a成功制作附魔书！"));
        }
    }
    
    /**
     * 根据羽毛笔等级计算能量消耗
     * @param quillLevel 羽毛笔等级（1=普通，2=高级，3=大师）
     * @return 能量消耗值
     */
    private static int calculateEnergyCost(int quillLevel) {
        return switch (quillLevel) {
            case 1 -> 2; // 普通羽毛笔固定消耗2点
            case 2 -> RANDOM.nextInt(2) + 1; // 高级羽毛笔随机消耗1-2点
            case 3 -> {
                // 大师羽毛笔：30%概率不消耗，70%概率随机消耗1-3点
                if (RANDOM.nextDouble() < 0.3) {
                    yield 0;
                } else {
                    yield RANDOM.nextInt(3) + 1;
                }
            }
            default -> 2;
        };
    }
    
    private static void handleFailure(ServerPlayer player) {
        // 匹配失败，消耗一本书
        ItemStack offhandItem = player.getOffhandItem();
        
        if (offhandItem.is(Items.BOOK)) {
            int bookCount = offhandItem.getCount();
            if (bookCount > 1) {
                offhandItem.shrink(1);
            } else {
                player.getInventory().setItem(player.getInventory().selected, ItemStack.EMPTY);
            }
        }
        
        player.sendSystemMessage(Component.literal("§c绘制失败！图案不匹配，已失去一本书。"));
    }
    
    /**
     * 获取附魔的最大等级
     */
    private static int getMaxLevel(Enchantment enchantment) {
        // 大多数附魔最大等级为3-5，这里设置一个安全值
        return Math.min(enchantment.getMaxLevel(), 5);
    }
}

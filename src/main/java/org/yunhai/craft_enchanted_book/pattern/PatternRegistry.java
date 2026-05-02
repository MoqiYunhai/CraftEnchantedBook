package org.yunhai.craft_enchanted_book.pattern;

import org.yunhai.craft_enchanted_book.CraftEnchantedBook;

import java.util.*;

/**
 * 图案识别系统 - 极致优化版（预定义数据）
 * 所有图案在编译时定义，零计算开销
 */
public class PatternRegistry {
    
    // 画布尺寸
    public static final int WIDTH = 8;
    public static final int HEIGHT = 8;
    private static final int TOTAL_PIXELS = WIDTH * HEIGHT;
    
    // 图案与附魔的映射
    private static final Map<String, EnchantmentDefinition> PATTERNS = new HashMap<>();
    private static boolean initialized = false;
    
    /**
     * 附魔定义类
     */
    public static class EnchantmentDefinition {
        private final String enchantmentId;
        private final String patternName;
        private final BitSet pattern;
        
        public EnchantmentDefinition(String enchantmentId, String patternName, BitSet pattern) {
            this.enchantmentId = enchantmentId;
            this.patternName = patternName;
            this.pattern = pattern;
        }
        
        public String getEnchantmentId() { return enchantmentId; }
        public String getPatternName() { return patternName; }
        public BitSet getPattern() { return pattern; }
    }
    
    /**
     * 初始化所有图案（超快，只是加载预定义数据）
     */
    public static synchronized void init() {
        if (initialized) return;
        
        long startTime = System.currentTimeMillis();
        CraftEnchantedBook.LOGGER.info("Initializing pattern registry (pre-defined)...");
        
        // 保护 - 矩形框
        registerPattern("protection", "protection", createPattern(
            " ###### ",
            " #    # ",
            " #    # ",
            " #    # ",
            " #    # ",
            " #    # ",
            " #    # ",
            " ###### "
        ));
        
        // 锋利 - 剑
        registerPattern("sharpness", "sharpness", createPattern(
            "   ##   ",
            "   ##   ",
            "   ##   ",
            "   ##   ",
            "   ##   ",
            " #####  ",
            " #####  ",
            "   ##   "
        ));
        
        // 效率 - Z字形
        registerPattern("efficiency", "efficiency", createPattern(
            "########",
            "    ### ",
            "     ###",
            "      ##",
            "     ## ",
            "    ### ",
            "   ###  ",
            "  ####  "
        ));
        
        // 耐久 - 心形
        registerPattern("unbreaking", "unbreaking", createPattern(
            "        ",
            " ##  ## ",
            "######  ",
            "######  ",
            " #####  ",
            "  ###   ",
            "   #    ",
            "        "
        ));
        
        // 经验修补 - 星星
        registerPattern("mending", "mending", createPattern(
            "   #    ",
            "  ###   ",
            " #####  ",
            "####### ",
            " #####  ",
            "  ###   ",
            "   #    ",
            "        "
        ));
        
        // 火焰保护 - 三角形
        registerPattern("fire_protection", "fire_protection", createPattern(
            "        ",
            "        ",
            "   #    ",
            "  ###   ",
            " #####  ",
            "####### ",
            "        ",
            "        "
        ));
        
        // 击退 - 波浪线
        registerPattern("knockback", "knockback", createPattern(
            "        ",
            "        ",
            "##  ## #",
            "# ## # #",
            "#  #  # ",
            "# ## # #",
            "##  ## #",
            "        "
        ));
        
        // 火焰附加 - 带火花的剑
        registerPattern("fire_aspect", "fire_aspect", createPattern(
            "  # #   ",
            " # ##   ",
            "  ##    ",
            "  ##    ",
            "  ##    ",
            " #####  ",
            " #####  ",
            "  ##    "
        ));
        
        // 抢夺 - 十字
        registerPattern("looting", "looting", createPattern(
            "        ",
            "   #    ",
            "   #    ",
            "   #    ",
            " #######",
            "   #    ",
            "   #    ",
            "   #    "
        ));
        
        // 时运 - 菱形
        registerPattern("fortune", "fortune", createPattern(
            "        ",
            "   #    ",
            "  ###   ",
            " #####  ",
            " #####  ",
            "  ###   ",
            "   #    ",
            "        "
        ));
        
        // 力量 - 弓
        registerPattern("power", "power", createPattern(
            "  ###   ",
            " #   #  ",
            "#     # ",
            "#     # ",
            "#     # ",
            " #   #  ",
            "  ###   ",
            "        "
        ));
        
        // 无限 - 圆形
        registerPattern("infinity", "infinity", createPattern(
            "        ",
            "  ####  ",
            " #    # ",
            "#      #",
            "#      #",
            " #    # ",
            "  ####  ",
            "        "
        ));
        
        // 精准采集 - 实心正方形
        registerPattern("silk_touch", "silk_touch", createPattern(
            "        ",
            "  ####  ",
            "  ####  ",
            "  ####  ",
            "  ####  ",
            "  ####  ",
            "  ####  ",
            "        "
        ));
        
        // 水下呼吸 - 三个圆
        registerPattern("respiration", "respiration", createPattern(
            "        ",
            " # # #  ",
            "# # # # ",
            "# # # # ",
            "# # # # ",
            " # # #  ",
            "        ",
            "        "
        ));
        
        // 荆棘 - 锯齿线
        registerPattern("thorns", "thorns", createPattern(
            "        ",
            "        ",
            "# # # # ",
            " # # # #",
            "# # # # ",
            "        ",
            "        ",
            "        "
        ));
        
        // 摔落保护 - 对角线
        registerPattern("feather_falling", "feather_falling", createPattern(
            "        ",
            "        ",
            "#       ",
            " ##     ",
            "  ##    ",
            "   ##   ",
            "    ##  ",
            "     ## "
        ));
        
        // 爆炸保护 - 放射状
        registerPattern("blast_protection", "blast_protection", createPattern(
            "   #    ",
            "  # #   ",
            "   #    ",
            " #######",
            "   #    ",
            "  # #   ",
            "   #    ",
            "        "
        ));
        
        // 弹射物保护 - 箭头
        registerPattern("projectile_protection", "projectile_protection", createPattern(
            "        ",
            "        ",
            "      # ",
            "     ## ",
            "########",
            "     ## ",
            "      # ",
            "        "
        ));
        
        // 水下速掘 - T形
        registerPattern("aqua_affinity", "aqua_affinity", createPattern(
            "        ",
            " ###### ",
            "   ##   ",
            "   ##   ",
            "   ##   ",
            "   ##   ",
            "   ##   ",
            "   ##   "
        ));
        
        // 冲击 - 同心圆
        registerPattern("punch", "punch", createPattern(
            "        ",
            "  ####  ",
            " #    # ",
            "# ## # #",
            "# ## # #",
            " #    # ",
            "  ####  ",
            "        "
        ));
        
        // 海之眷顾 - 鱼
        registerPattern("luck_of_the_sea", "luck_of_the_sea", createPattern(
            "        ",
            "        ",
            " ###### ",
            "########",
            " ###### ",
            "     ## ",
            "      # ",
            "        "
        ));
        
        // 饵钓 - 钩子
        registerPattern("lure", "lure", createPattern(
            "        ",
            "   #    ",
            "   #    ",
            "   #    ",
            "   #    ",
            "   #    ",
            "   # ## ",
            "    #   "
        ));
        
        initialized = true;
        long elapsed = System.currentTimeMillis() - startTime;
        CraftEnchantedBook.LOGGER.info("Pattern registry initialized with " + PATTERNS.size() + " patterns in " + elapsed + "ms");
    }
    
    private static void registerPattern(String enchantmentId, String patternName, BitSet pattern) {
        PATTERNS.put(patternName, new EnchantmentDefinition(enchantmentId, patternName, pattern));
    }
    
    /**
     * 从字符串数组创建图案（每行32字符，#表示填充，空格表示空白）
     */
    private static BitSet createPattern(String... rows) {
        BitSet bs = new BitSet(TOTAL_PIXELS);
        for (int y = 0; y < rows.length && y < HEIGHT; y++) {
            String row = rows[y];
            for (int x = 0; x < row.length() && x < WIDTH; x++) {
                if (row.charAt(x) == '#') {
                    bs.set(y * WIDTH + x);
                }
            }
        }
        return bs;
    }
    
    /**
     * 验证图案 - 使用位运算快速匹配
     */
    public static EnchantmentDefinition matchPattern(BitSet playerPattern) {
        double bestMatch = 0.75;
        EnchantmentDefinition bestMatchDef = null;
        
        for (EnchantmentDefinition def : PATTERNS.values()) {
            double similarity = calculateSimilarity(playerPattern, def.getPattern());
            if (similarity > bestMatch) {
                bestMatch = similarity;
                bestMatchDef = def;
            }
        }
        
        return bestMatchDef;
    }
    
    /**
     * 使用 XOR 位运算计算相似度
     */
    private static double calculateSimilarity(BitSet pattern1, BitSet pattern2) {
        BitSet diff = (BitSet) pattern1.clone();
        diff.xor(pattern2);
        
        int diffCount = diff.cardinality();
        int matchCount = TOTAL_PIXELS - diffCount;
        
        return (double) matchCount / TOTAL_PIXELS;
    }
    
    public static Collection<EnchantmentDefinition> getAllPatterns() {
        return Collections.unmodifiableCollection(PATTERNS.values());
    }
}

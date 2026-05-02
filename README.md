# Craft Enchanted Book - 可合成附魔书模组

## 简介

Craft Enchanted Book 是一个 Minecraft 1.20 Forge 模组，允许玩家通过绘制特殊图案来制作附魔书。玩家需要使用特殊的羽毛笔在画布上绘制对应的附魔图案，成功后即可将普通书籍转换为附魔书。

**Minecraft 版本**: 1.20  
**Forge 版本**: 46.x  
**许可证**: GNU GPL 3.0

## 特性

- 🎨 **像素绘制系统**: 8x8 像素的画布，自由绘制附魔图案
- 📖 **丰富的附魔支持**: 支持所有原版附魔，包括不同等级
- 📚 **说明书系统**: 内置说明书查看所有附魔图案
- ✨ **智能图案识别**: 85% 相似度即可成功匹配
- 🎮 **友好的用户界面**: 直观的 GUI 操作
- ⚡ **羽毛笔成长系统**: 羽毛笔可通过使用升级，解锁更高级附魔
- 🔋 **能量充能系统**: 为羽毛笔充能以支持绘制操作

## 物品介绍

### 特殊羽毛笔 (Enchanted Quill)

羽毛笔分为三个等级，每个等级具有不同的功能和能量上限：

#### 🖊️ 普通羽毛笔 (Basic Quill)
- **能量上限**: 10 点
- **可制作附魔**: I 级附魔
- **能量消耗**: 固定 2 点/次
- **升级条件**: 绘制 10 次后自动升级为高级羽毛笔

#### ✨ 高级羽毛笔 (Advanced Quill)
- **能量上限**: 20 点
- **可制作附魔**: I-II 级附魔
- **能量消耗**: 随机 1-2 点/次
- **升级条件**: 绘制 30 次后自动升级为大师羽毛笔

#### 👑 大师羽毛笔 (Master Quill)
- **能量上限**: 30 点
- **可制作附魔**: I-III 级附魔
- **能量消耗**: 30% 概率不消耗能量，70% 概率随机消耗 1-3 点
- **特殊效果**: 已达最高等级，拥有最高的能量效率和附魔等级

### 羽毛笔成长值系统

每次成功绘制附魔书，羽毛笔会获得 1 点成长值。当成长值达到阈值时，羽毛笔会自动升级：
- **普通 → 高级**: 需要 10 点成长值
- **高级 → 大师**: 需要 30 点成长值

在背包中将鼠标悬停在羽毛笔上可查看当前成长值和升级进度。

### 羽毛笔能量系统

#### 充能方法
1. **左手（副手）持有青金石**
2. **右手（主手）持有羽毛笔**
3. **右键点击**
4. 消耗 1 个青金石，羽毛笔能量 +1

#### 能量显示
在背包中将鼠标悬停在羽毛笔上可查看当前能量值和最大能量上限。

#### 能量消耗
每次成功绘制附魔书时，会根据羽毛笔等级消耗相应的能量。如果能量不足，将无法制作附魔书。

### 附魔图案说明书 (Enchantment Pattern Guide)
- 查看所有附魔对应的图案
- 右键使用打开说明书界面
- 翻页浏览所有附魔图案

## 使用方法

### 制作附魔书

1. **准备材料**:
   - 左手（副手）持有书籍
   - 右手（主手）持有特殊羽毛笔
   - 确保羽毛笔有足够的能量（查看 Tooltip）

2. **打开绘制界面**:
   - 右键点击特殊羽毛笔
   - 打开 8x8 像素的绘制画布

3. **绘制图案**:
   - 左键点击或拖动绘制黑色像素
   - 右键点击或拖动擦除像素
   - 参考说明书中的图案进行绘制

4. **提交验证**:
   - 点击“确定”按钮提交图案
   - 系统会自动验证图案相似度

5. **结果**:
   - **成功**: 如果图案匹配度达到 85% 以上
     - 检查能量是否足够（根据羽毛笔等级消耗不同）
     - 如果只有 1 本书：直接替换为附魔书
     - 如果有多本书：消耗 1 本，附魔书掉落在地面
     - 羽毛笔获得 1 点成长值
   - **失败**: 如果图案不匹配
     - 失去一本书
     - 显示失败提示
   - **能量不足**: 如果能量不够
     - 显示所需能量和当前能量
     - 不会消耗书本
     - 需要先为羽毛笔充能

### 为羽毛笔充能

1. **准备材料**:
   - 左手（副手）持有青金石
   - 右手（主手）持有羽毛笔

2. **开始充能**:
   - 右键点击
   - 立即消耗青金石并充能

3. **充能完成**:
   - 消耗 1 个青金石
   - 羽毛笔能量 +1
   - 播放成功音效

4. **注意事项**:
   - 能量达到上限后无法继续充能
   - 不同等级的羽毛笔有不同的能量上限
   - 每次右键点击立即充能

## 支持的附魔图案

### 盔甲附魔
- **保护 (Protection)** I-IV: 盾牌形状
- **火焰保护 (Fire Protection)** I-IV: 火焰形状
- **摔落保护 (Feather Falling)** I-IV: 羽毛形状
- **爆炸保护 (Blast Protection)** I-IV: 爆炸形状
- **弹射物保护 (Projectile Protection)** I-IV: 箭头形状
- **水下呼吸 (Respiration)** I-III: 水泡形状
- **水下速掘 (Aqua Affinity)** I: 镐子形状
- **荆棘 (Thorns)** I-III: 尖刺形状

### 通用附魔
- **耐久 (Unbreaking)** I-III: 心形
- **经验修补 (Mending)** I: 星星形状

### 武器附魔
- **锋利 (Sharpness)** I-V: 剑形状
- **击退 (Knockback)** I-II: 推力波浪
- **火焰附加 (Fire Aspect)** I-II: 燃烧的剑
- **抢夺 (Looting)** I-III: 手掌形状

### 工具附魔
- **效率 (Efficiency)** I-V: 闪电形状
- **精准采集 (Silk Touch)** I: 钻石形状
- **时运 (Fortune)** I-III: 幸运草形状

### 弓附魔
- **力量 (Power)** I-V: 弓形状
- **冲击 (Punch)** I-II: 冲击波
- **无限 (Infinity)** I: 无限符号

### 钓鱼竿附魔
- **海之眷顾 (Luck of the Sea)** I-III: 鱼形状
- **饵钓 (Lure)** I-III: 鱼钩形状

## 图案识别机制

- **画布尺寸**: 128 x 64 像素
- **相似度阈值**: 85%
- **计算方式**: 逐像素比对，相同像素数 / 总像素数
- **容错设计**: 允许一定的绘制误差

## 安装说明

### 前置要求
- Minecraft 1.20
- Forge 46.x

### 安装步骤

1. 下载模组 JAR 文件
2. 将 JAR 文件放入 `.minecraft/mods` 文件夹
3. 启动 Minecraft
4. 享受游戏！

### 从源码构建

```bash
# 克隆仓库
git clone <repository-url>
cd CraftEnchantedBook

# 构建模组
./gradlew build

# 生成的 JAR 文件位于 build/libs 目录
```

## 开发信息

### 项目结构

```
src/main/java/org/yunhai/craft_enchanted_book/
├── CraftEnchantedBook.java          # 主类
├── init/
│   └── ModItems.java                # 物品注册
├── item/
│   ├── EnchantedQuillItem.java      # 特殊羽毛笔
│   └── GuideBookItem.java           # 说明书
├── pattern/
│   └── PatternRegistry.java         # 图案注册与识别
├── network/
│   ├── ModNetwork.java              # 网络消息注册
│   ├── ValidatePatternMessage.java  # 验证图案消息
│   ├── OpenGuideBookMessage.java    # 打开说明书消息
│   ├── PatternValidationHandler.java # 图案验证处理器
│   └── GuideBookOpener.java         # 客户端GUI开启器
└── gui/
    ├── DrawingScreen.java           # 绘制界面
    └── GuideBookScreen.java         # 说明书界面
```

### 添加新图案

在 `PatternRegistry.java` 中：

1. 创建图案生成方法
2. 在 `init()` 方法中注册图案
3. 定义图案名称和对应附魔

```java
// 示例：注册新图案
registerPattern("enchantment_id", level, "pattern_name", createCustomPattern());

private static boolean[][] createCustomPattern() {
    boolean[][] pattern = new boolean[WIDTH][HEIGHT];
    // 绘制图案逻辑
    return pattern;
}
```

## 常见问题

**Q: 图案识别不准确怎么办？**  
A: 尽量按照说明书中的图案精确绘制，确保关键特征清晰可见。

**Q: 为什么绘制失败会失去书本？**  
A: 这是为了增加游戏挑战性和平衡性，请谨慎绘制。

**Q: 可以自定义图案吗？**  
A: 目前图案是固定的，未来版本可能支持自定义图案配置。

**Q: 多人游戏中可以使用吗？**  
A: 可以，模组完全支持多人游戏，图案验证在服务端进行。

## 许可证

本项目采用 GNU GPL 3.0 许可证。详见 [LICENSE](LICENSE) 文件。

## 贡献

欢迎提交 Issue 和 Pull Request！

## 联系方式

- 作者: yunhai
- 项目主页: [GitHub Repository](<repository-url>)

## 致谢

感谢 Minecraft Forge 团队和所有开源贡献者！

---

**祝你游戏愉快！** 🎮✨

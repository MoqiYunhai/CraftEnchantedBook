# Craft Enchanted Book 模组 - 快速开始指南

## 🚀 快速开始

### 1. 构建模组

在项目根目录打开终端，运行：

```bash
# Windows
gradlew build

# 或者使用 gradle wrapper
.\gradlew.bat build
```

构建完成后，JAR 文件将位于 `build/libs/` 目录。

### 2. 测试模组

```bash
# 启动 Minecraft 客户端进行测试
gradlew runClient

# 启动 Minecraft 服务器进行测试
gradlew runServer
```

### 3. 安装到游戏

1. 复制 `build/libs/craft_enchanted_book-1.0-SNAPSHOT.jar` 
2. 粘贴到 `.minecraft/mods/` 文件夹
3. 启动 Minecraft Forge 1.20
4. 享受游戏！

## 📝 重要提示

### 纹理文件缺失

当前代码缺少物品纹理文件。在正式使用前，你需要创建以下纹理：

- `src/main/resources/assets/craft_enchanted_book/textures/item/enchanted_quill.png`
- `src/main/resources/assets/craft_enchanted_book/textures/item/guide_book.png`

**临时解决方案**：
- 可以使用 16x16 的任意 PNG 图片作为占位符
- 或从 Minecraft 原版纹理中复制并修改

详见 [TEXTURE_GUIDE.md](TEXTURE_GUIDE.md)

### 图案识别调整

如果觉得图案识别太严格或太宽松，可以调整 `PatternRegistry.java` 中的相似度阈值：

```java
double bestMatch = 0.85; // 修改这个值（0.0-1.0）
```

- **降低**（如 0.75）：更容易成功，但可能误识别
- **提高**（如 0.90）：更精确，但需要绘制更准确

## 🎮 游戏玩法

### 制作附魔书流程

1. **获取物品**（创造模式或在配置文件中添加合成表）
   - 特殊羽毛笔
   - 说明书
   - 普通书籍

2. **查看图案**
   - 右键说明书查看所有附魔图案
   - 选择你想要的附魔

3. **准备绘制**
   - 左手（副手）持有书籍
   - 右手（主手）持有特殊羽毛笔

4. **绘制图案**
   - 右键羽毛笔打开绘制界面
   - 按照说明书绘制对应图案
   - 左键绘制，右键擦除

5. **提交验证**
   - 点击"确定"按钮
   - 等待验证结果

6. **获得附魔书**
   - 成功：获得对应附魔书
   - 失败：失去一本书

## 🔧 开发相关

### 项目结构

```
CraftEnchantedBook/
├── src/main/java/org/yunhai/craft_enchanted_book/
│   ├── CraftEnchantedBook.java          # 主类
│   ├── init/ModItems.java               # 物品注册
│   ├── item/                            # 物品类
│   ├── pattern/PatternRegistry.java     # 图案系统
│   ├── network/                         # 网络消息
│   └── gui/                             # GUI 界面
├── src/main/resources/
│   ├── assets/craft_enchanted_book/
│   │   ├── lang/                        # 语言文件
│   │   ├── models/item/                 # 物品模型
│   │   └── textures/item/               # 物品纹理（需要创建）
│   └── META-INF/mods.toml              # 模组配置
└── README.md                            # 详细文档
```

### 添加新附魔图案

编辑 `PatternRegistry.java`：

```java
// 在 init() 方法中添加
registerPattern("enchantment_id", level, "pattern_name", createYourPattern());

// 创建图案生成方法
private static boolean[][] createYourPattern() {
    boolean[][] pattern = new boolean[WIDTH][HEIGHT];
    // 你的图案逻辑
    return pattern;
}
```

### 调试技巧

1. **启用日志**：查看控制台输出了解模组加载状态
2. **图案测试**：在创造模式中测试图案识别
3. **网络调试**：检查客户端-服务端通信

## ❓ 常见问题

**Q: 构建失败怎么办？**  
A: 确保已安装 JDK 17，并且 Gradle 版本正确。

**Q: 游戏中看不到物品？**  
A: 检查是否安装了正确的 Forge 版本（46.x），并确认模组已加载。

**Q: 图案总是识别失败？**  
A: 降低相似度阈值，或尽量精确绘制图案。

**Q: 如何添加合成配方？**  
A: 需要创建数据生成器或手动添加 JSON 配方文件。

## 📞 获取帮助

- 查看 [README.md](README.md) 获取详细文档
- 查看 [TEXTURE_GUIDE.md](TEXTURE_GUIDE.md) 了解纹理制作
- 检查 Minecraft 日志文件排查问题

## ✨ 下一步计划

- [ ] 添加物品合成配方
- [ ] 创建实际的纹理文件
- [ ] 支持自定义图案配置
- [ ] 添加更多附魔类型
- [ ] 优化图案识别算法

---

**祝你开发愉快！** 🎉

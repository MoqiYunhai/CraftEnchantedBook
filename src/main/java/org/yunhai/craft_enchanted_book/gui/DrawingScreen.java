package org.yunhai.craft_enchanted_book.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.yunhai.craft_enchanted_book.network.ModNetwork;
import org.yunhai.craft_enchanted_book.network.ValidatePatternMessage;
import org.yunhai.craft_enchanted_book.pattern.PatternRegistry;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

/**
 * 绘制附魔图案的GUI界面 - 8x8画布，左右布局
 */
public class DrawingScreen extends Screen {
    private static final int CANVAS_WIDTH = PatternRegistry.WIDTH;
    private static final int CANVAS_HEIGHT = PatternRegistry.HEIGHT;
    private static final int PIXEL_SIZE = 16; // 每个像素的显示大小（8x8画布，放大显示）
    private static final int TOTAL_PIXELS = CANVAS_WIDTH * CANVAS_HEIGHT;
    
    private BitSet canvas;
    private boolean isDrawing = false;
    private boolean isErasing = false;
    
    private int canvasStartX;
    private int canvasStartY;
    private final int quillLevel;
    
    // 右侧示例相关
    private List<PatternRegistry.EnchantmentDefinition> patterns;
    private int currentPage = 0;
    private int totalPages;
    private int exampleStartX;
    private int exampleStartY;
    
    public DrawingScreen(int quillLevel) {
        super(Component.literal("绘制附魔图案"));
        this.canvas = new BitSet(TOTAL_PIXELS);
        this.quillLevel = quillLevel;
        
        // 加载所有图案用于示例
        this.patterns = new ArrayList<>(PatternRegistry.getAllPatterns());
        this.totalPages = this.patterns.size();
    }
    
    @Override
    protected void init() {
        // 计算画布尺寸
        int canvasWidth = CANVAS_WIDTH * PIXEL_SIZE;
        int canvasHeight = CANVAS_HEIGHT * PIXEL_SIZE;
        
        // 左侧绘制区域 - 垂直居中，向中间靠近
        this.canvasStartX = 80;
        this.canvasStartY = (this.height - canvasHeight) / 2;
        
        // 右侧示例区域 - 与左侧完全对称对齐
        this.exampleStartX = this.width - canvasWidth - 80;
        this.exampleStartY = this.canvasStartY; // 顶部对齐
        
        // 统一按钮设置
        int buttonWidth = 80;
        int buttonHeight = 20;
        int buttonGap = 10;
        int totalButtonWidth = buttonWidth * 2 + buttonGap;
        
        // 计算按钮的Y位置（两个区域使用相同的Y坐标，确保对齐）
        int buttonY = canvasStartY + canvasHeight + 25;
        
        // 左侧按钮组 - 相对于绘制画布居中
        int leftButtonsCenterX = canvasStartX + canvasWidth / 2;
        
        // 清空按钮
        this.addRenderableWidget(Button.builder(
            Component.translatable("craft_enchanted_book.drawing.clear"),
            button -> clearCanvas()
        ).bounds(leftButtonsCenterX - totalButtonWidth / 2, buttonY, buttonWidth, buttonHeight).build());
        
        // 确定按钮
        this.addRenderableWidget(Button.builder(
            Component.translatable("craft_enchanted_book.drawing.confirm"),
            button -> onSubmit()
        ).bounds(leftButtonsCenterX + buttonGap / 2, buttonY, buttonWidth, buttonHeight).build());
        
        // 右侧按钮组 - 相对于示例画布居中
        int rightButtonsCenterX = exampleStartX + canvasWidth / 2;
        
        // 上一页按钮
        this.addRenderableWidget(Button.builder(
            Component.translatable("craft_enchanted_book.drawing.prev_page"),
            button -> prevPage()
        ).bounds(rightButtonsCenterX - totalButtonWidth / 2, buttonY, buttonWidth, buttonHeight).build());
        
        // 下一页按钮
        this.addRenderableWidget(Button.builder(
            Component.translatable("craft_enchanted_book.drawing.next_page"),
            button -> nextPage()
        ).bounds(rightButtonsCenterX + buttonGap / 2, buttonY, buttonWidth, buttonHeight).build());
    }
    
    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(guiGraphics);
        
        // 左侧标题 - 居中对齐（使用本地化）
        String titleKey = switch (quillLevel) {
            case 1 -> "craft_enchanted_book.drawing.title_basic";
            case 2 -> "craft_enchanted_book.drawing.title_advanced";
            case 3 -> "craft_enchanted_book.drawing.title_master";
            default -> "craft_enchanted_book.drawing.title_basic";
        };
        guiGraphics.drawCenteredString(this.font, Component.translatable(titleKey), 
            canvasStartX + CANVAS_WIDTH * PIXEL_SIZE / 2, canvasStartY - 20, 0xFFFFFF);
        guiGraphics.drawCenteredString(this.font, Component.translatable("craft_enchanted_book.drawing.instruction"), 
            canvasStartX + CANVAS_WIDTH * PIXEL_SIZE / 2, canvasStartY - 10, 0xAAAAAA);
        
        // 绘制画布背景
        guiGraphics.fill(canvasStartX - 2, canvasStartY - 2, 
             canvasStartX + CANVAS_WIDTH * PIXEL_SIZE + 2, 
             canvasStartY + CANVAS_HEIGHT * PIXEL_SIZE + 2, 
             0xFF808080);
        
        guiGraphics.fill(canvasStartX, canvasStartY, 
             canvasStartX + CANVAS_WIDTH * PIXEL_SIZE, 
             canvasStartY + CANVAS_HEIGHT * PIXEL_SIZE, 
             0xFFFFFFFF);
        
        // 绘制已绘制的像素
        for (int x = 0; x < CANVAS_WIDTH; x++) {
            for (int y = 0; y < CANVAS_HEIGHT; y++) {
                if (getPixel(x, y)) {
                    int pixelX = canvasStartX + x * PIXEL_SIZE;
                    int pixelY = canvasStartY + y * PIXEL_SIZE;
                    guiGraphics.fill(pixelX, pixelY, pixelX + PIXEL_SIZE, pixelY + PIXEL_SIZE, 0xFF000000);
                }
            }
        }
        
        // 绘制网格线
        RenderSystem.setShaderColor(0.5F, 0.5F, 0.5F, 0.3F);
        for (int x = 0; x <= CANVAS_WIDTH; x++) {
            int lineX = canvasStartX + x * PIXEL_SIZE;
            guiGraphics.fill(lineX, canvasStartY, lineX + 1, 
                 canvasStartY + CANVAS_HEIGHT * PIXEL_SIZE, 0x40000000);
        }
        for (int y = 0; y <= CANVAS_HEIGHT; y++) {
            int lineY = canvasStartY + y * PIXEL_SIZE;
            guiGraphics.fill(canvasStartX, lineY, 
                 canvasStartX + CANVAS_WIDTH * PIXEL_SIZE, lineY + 1, 0x40000000);
        }
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        
        // 右侧示例标题 - 居中对齐（使用本地化）
        guiGraphics.drawCenteredString(this.font, Component.translatable("craft_enchanted_book.drawing.example_title"), 
            exampleStartX + CANVAS_WIDTH * PIXEL_SIZE / 2, exampleStartY - 35, 0xFFFF00);
        
        if (!patterns.isEmpty() && currentPage < patterns.size()) {
            PatternRegistry.EnchantmentDefinition currentPattern = patterns.get(currentPage);
            
            // 使用游戏语言的附魔名称
            net.minecraft.resources.ResourceLocation enchantLoc = 
                new net.minecraft.resources.ResourceLocation("minecraft", currentPattern.getEnchantmentId());
            net.minecraft.world.item.enchantment.Enchantment enchant = 
                net.minecraftforge.registries.ForgeRegistries.ENCHANTMENTS.getValue(enchantLoc);
            
            String enchantName = "未知附魔";
            if (enchant != null) {
                // 获取游戏的本地化名称
                enchantName = enchant.getFullname(1).getString().split(" ")[0];
            }
            
            guiGraphics.drawCenteredString(this.font, enchantName, 
                exampleStartX + CANVAS_WIDTH * PIXEL_SIZE / 2, exampleStartY - 20, 0x00FF00);
            
            // 绘制页码
            guiGraphics.drawCenteredString(this.font, 
                String.format("%d / %d", currentPage + 1, totalPages), 
                exampleStartX + CANVAS_WIDTH * PIXEL_SIZE / 2, exampleStartY - 8, 0xAAAAAA);
            
            // 示例画布位置与绘制画布完全相同
            int startX = exampleStartX;
            int startY = exampleStartY;
            int patternWidth = CANVAS_WIDTH * PIXEL_SIZE;
            int patternHeight = CANVAS_HEIGHT * PIXEL_SIZE;
            
            // 绘制示例背景
            guiGraphics.fill(startX - 2, startY - 2, 
                 startX + patternWidth + 2, startY + patternHeight + 2, 
                 0xFF606060);
            
            guiGraphics.fill(startX, startY, 
                 startX + patternWidth, startY + patternHeight, 
                 0xFFF0F0F0);
            
            // 绘制示例图案
            BitSet pattern = currentPattern.getPattern();
            for (int x = 0; x < CANVAS_WIDTH; x++) {
                for (int y = 0; y < CANVAS_HEIGHT; y++) {
                    int index = y * CANVAS_WIDTH + x;
                    if (pattern.get(index)) {
                        int pixelX = startX + x * PIXEL_SIZE;
                        int pixelY = startY + y * PIXEL_SIZE;
                        guiGraphics.fill(pixelX, pixelY, pixelX + PIXEL_SIZE, pixelY + PIXEL_SIZE, 0xFF000000);
                    }
                }
            }
            
            // 绘制示例网格
            RenderSystem.setShaderColor(0.4F, 0.4F, 0.4F, 0.3F);
            for (int x = 0; x <= CANVAS_WIDTH; x++) {
                int lineX = startX + x * PIXEL_SIZE;
                guiGraphics.fill(lineX, startY, lineX + 1, 
                     startY + patternHeight, 0x30000000);
            }
            for (int y = 0; y <= CANVAS_HEIGHT; y++) {
                int lineY = startY + y * PIXEL_SIZE;
                guiGraphics.fill(startX, lineY, 
                     startX + patternWidth, lineY + 1, 0x30000000);
            }
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            
            // 绘制说明 - 居中对齐（使用本地化）
            guiGraphics.drawCenteredString(this.font, Component.translatable("craft_enchanted_book.drawing.example_instruction"), 
                exampleStartX + CANVAS_WIDTH * PIXEL_SIZE / 2, startY + patternHeight + 15, 0xAAAAAA);
        }
        
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
    }
    
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        // 先检查是否在画布区域内
        if (mouseX >= canvasStartX && mouseX < canvasStartX + CANVAS_WIDTH * PIXEL_SIZE &&
            mouseY >= canvasStartY && mouseY < canvasStartY + CANVAS_HEIGHT * PIXEL_SIZE) {
            
            if (button == 0) {
                isDrawing = true;
                isErasing = false;
                handleMouseInput(mouseX, mouseY, true);
                return true;
            } else if (button == 1) {
                isDrawing = false;
                isErasing = true;
                handleMouseInput(mouseX, mouseY, false);
                return true;
            }
        }
        
        return super.mouseClicked(mouseX, mouseY, button);
    }
    
    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (button == 0) {
            isDrawing = false;
        } else if (button == 1) {
            isErasing = false;
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }
    
    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (mouseX >= canvasStartX && mouseX < canvasStartX + CANVAS_WIDTH * PIXEL_SIZE &&
            mouseY >= canvasStartY && mouseY < canvasStartY + CANVAS_HEIGHT * PIXEL_SIZE) {
            
            if (isDrawing && button == 0) {
                handleMouseInput(mouseX, mouseY, true);
                return true;
            } else if (isErasing && button == 1) {
                handleMouseInput(mouseX, mouseY, false);
                return true;
            }
        }
        
        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }
    
    private void handleMouseInput(double mouseX, double mouseY, boolean draw) {
        if (mouseX >= canvasStartX && mouseX < canvasStartX + CANVAS_WIDTH * PIXEL_SIZE &&
            mouseY >= canvasStartY && mouseY < canvasStartY + CANVAS_HEIGHT * PIXEL_SIZE) {
            
            int x = (int)((mouseX - canvasStartX) / PIXEL_SIZE);
            int y = (int)((mouseY - canvasStartY) / PIXEL_SIZE);
            
            if (x >= 0 && x < CANVAS_WIDTH && y >= 0 && y < CANVAS_HEIGHT) {
                setPixel(x, y, draw);
            }
        }
    }
    
    private void setPixel(int x, int y, boolean value) {
        int index = y * CANVAS_WIDTH + x;
        canvas.set(index, value);
    }
    
    private boolean getPixel(int x, int y) {
        int index = y * CANVAS_WIDTH + x;
        return canvas.get(index);
    }
    
    private void onSubmit() {
        ModNetwork.INSTANCE.sendToServer(new ValidatePatternMessage(canvas, quillLevel));
        this.onClose();
    }
    
    private void clearCanvas() {
        this.canvas.clear();
    }
    
    private void prevPage() {
        if (currentPage > 0) {
            currentPage--;
        } else {
            currentPage = totalPages - 1;
        }
    }
    
    private void nextPage() {
        if (currentPage < totalPages - 1) {
            currentPage++;
        } else {
            currentPage = 0;
        }
    }
    
    @Override
    public boolean shouldCloseOnEsc() {
        return true;
    }
}

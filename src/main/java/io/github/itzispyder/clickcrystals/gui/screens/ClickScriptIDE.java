package io.github.itzispyder.clickcrystals.gui.screens;

import io.github.itzispyder.clickcrystals.client.clickscript.ClickScript;
import io.github.itzispyder.clickcrystals.commands.commands.ReloadCommand;
import io.github.itzispyder.clickcrystals.data.Config;
import io.github.itzispyder.clickcrystals.events.listeners.UserInputListener;
import io.github.itzispyder.clickcrystals.gui.elements.common.AbstractElement;
import io.github.itzispyder.clickcrystals.gui.elements.common.display.LoadingIconElement;
import io.github.itzispyder.clickcrystals.gui.elements.common.interactive.TextFieldElement;
import io.github.itzispyder.clickcrystals.gui.misc.ChatColor;
import io.github.itzispyder.clickcrystals.gui.misc.Gray;
import io.github.itzispyder.clickcrystals.gui.misc.Tex;
import io.github.itzispyder.clickcrystals.gui.misc.brushes.RoundRectBrush;
import io.github.itzispyder.clickcrystals.gui.screens.modulescreen.BrowsingScreen;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.modules.ScriptedModule;
import io.github.itzispyder.clickcrystals.modules.scripts.client.ConfigCmd;
import io.github.itzispyder.clickcrystals.modules.scripts.client.ModuleCmd;
import io.github.itzispyder.clickcrystals.modules.scripts.macros.InputCmd;
import io.github.itzispyder.clickcrystals.modules.scripts.macros.TurnToCmd;
import io.github.itzispyder.clickcrystals.modules.scripts.syntax.IfCmd;
import io.github.itzispyder.clickcrystals.modules.scripts.syntax.OnEventCmd;
import io.github.itzispyder.clickcrystals.util.ArrayUtils;
import io.github.itzispyder.clickcrystals.util.FileValidationUtils;
import io.github.itzispyder.clickcrystals.util.StringUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.RenderUtils;
import io.github.itzispyder.clickcrystals.util.misc.Voidable;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;

import java.io.*;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class ClickScriptIDE extends DefaultBase {

    public static final TextFieldElement.TextHighlighter CLICKSCRIPT_HIGHLIGHTER = new TextFieldElement.TextHighlighter() {{
        ChatColor og = getOriginalColor();
        Function<ChatColor, Function<String, String>> applyColor = c -> s -> "%s%s%s".formatted(c, s, og);
        Function<ChatColor, Function<String, String>> applyUnderline = c -> s -> "%s§n%s§r%s".formatted(c, s, og);
        Function<ChatColor, Function<String, String>> applyItalic = c -> s -> "%s§o%s§r%s".formatted(c, s, og);

        // special
        this.put(s -> StringUtils.startsWithAny(s, ":", "#"), applyColor.apply(ChatColor.DARK_GREEN));
        this.put(s -> s.replaceAll("[0-9><=!.+~-]", "").isEmpty(), applyColor.apply(ChatColor.DARK_AQUA));
        this.put(ChatColor.GRAY, "then", "back", "all");
        // enums-leading
        this.put(s -> ArrayUtils.enumContains(OnEventCmd.EventType.class, s), applyUnderline.apply(ChatColor.YELLOW));
        this.put(s -> ArrayUtils.enumContains(IfCmd.ConditionType.class, s), applyItalic.apply(ChatColor.YELLOW));
        this.put(ChatColor.YELLOW, Arrays.stream(InputCmd.Action.values()).map(e -> e.name().toLowerCase()).toList());
        this.put(ChatColor.YELLOW, Arrays.stream(ModuleCmd.Action.values()).map(e -> e.name().toLowerCase()).toList());
        this.put(ChatColor.YELLOW, Arrays.stream(TurnToCmd.Mode.values()).map(e -> e.name().toLowerCase()).toList());
        this.put(ChatColor.YELLOW, Arrays.stream(ConfigCmd.Type.values()).map(e -> e.name().toLowerCase()).toList());
        // enums-trailing
        this.put(ChatColor.YELLOW, Arrays.stream(IfCmd.Dimensions.values()).map(e -> e.name().toLowerCase()).toList());
        // main keywords
        this.put(ChatColor.ORANGE, ClickScript.collectNames());
    }};
    private final String filename, filepath;
    private final LoadingIconElement loading;
    private final AbstractElement
            saveButton,
            saveAndCloseButton,
            closeButton,
            discardChangesButton,
            openFileButton,
            openScriptsButton,
            deleteButton;

    public TextFieldElement textField = new TextFieldElement(contentX, contentY + 21, contentWidth, contentHeight - 21) {{
        this.setHighlighter(CLICKSCRIPT_HIGHLIGHTER);
        this.setBackgroundColor(ChatColor.RESET);
    }};

    public ClickScriptIDE(ScriptedModule module) {
        this(new File(module.filepath));
    }

    public ClickScriptIDE(File file) {
        super("ClickScript IDE");
        this.addChild(textField);
        this.filename = file.getName();
        this.filepath = file.getPath();

        this.loading = new LoadingIconElement(contentX + contentWidth / 2 - 10, contentY + contentHeight / 2 - 10, 20);
        this.loading.setRendering(false);
        this.addChild(loading);
        this.loadContents();

        // init
        this.navlistModules.forEach(this::removeChild);
        this.removeChild(buttonSearch);

        saveButton = AbstractElement.create().dimensions(navWidth, 12)
                .tooltip("Save contents")
                .onPress(button -> saveContents())
                .onRender((context, mouseX, mouseY, button) -> {
                    if (button.isHovered(mouseX, mouseY)) {
                        RoundRectBrush.drawRoundHoriLine(context, button.x, button.y, navWidth, button.height, Gray.LIGHT_GRAY);
                    }
                    RenderUtils.drawText(context, "Save", button.x + 7, button.y + button.height / 3, 0.7F, false);
                }).build();
        saveAndCloseButton = AbstractElement.create().dimensions(navWidth, 12)
                .tooltip("Save contents then close IDE")
                .onPress(button -> saveContents().accept(f -> f.thenRun(() -> mc.execute(() -> mc.setScreen(new BrowsingScreen())))))
                .onRender((context, mouseX, mouseY, button) -> {
                    if (button.isHovered(mouseX, mouseY)) {
                        RoundRectBrush.drawRoundHoriLine(context, button.x, button.y, navWidth, button.height, Gray.LIGHT_GRAY);
                    }
                    RenderUtils.drawText(context, "Save & Close", button.x + 7, button.y + button.height / 3, 0.7F, false);
                }).build();
        closeButton = AbstractElement.create().dimensions(navWidth, 12)
                .tooltip("Close without saving")
                .onPress(button -> mc.setScreen(new BrowsingScreen()))
                .onRender((context, mouseX, mouseY, button) -> {
                    if (button.isHovered(mouseX, mouseY)) {
                        RoundRectBrush.drawRoundHoriLine(context, button.x, button.y, navWidth, button.height, Gray.GENERIC_LOW);
                    }
                    RenderUtils.drawText(context, "Close", button.x + 7, button.y + button.height / 3, 0.7F, false);
                }).build();
        discardChangesButton = AbstractElement.create().dimensions(navWidth, 12)
                .tooltip("Undo all modifications")
                .onPress(button -> loadContents())
                .onRender((context, mouseX, mouseY, button) -> {
                    if (button.isHovered(mouseX, mouseY)) {
                        RoundRectBrush.drawRoundHoriLine(context, button.x, button.y, navWidth, button.height, Gray.GENERIC_LOW);
                    }
                    RenderUtils.drawText(context, "Discard Changes", button.x + 7, button.y + button.height / 3, 0.7F, false);
                }).build();
        openFileButton = AbstractElement.create().dimensions(navWidth, 12)
                .tooltip("Open file in File Explorer")
                .onPress(button -> system.openFile(filepath))
                .onRender((context, mouseX, mouseY, button) -> {
                    if (button.isHovered(mouseX, mouseY)) {
                        RoundRectBrush.drawRoundHoriLine(context, button.x, button.y, navWidth, button.height, Gray.LIGHT_GRAY);
                    }
                    RenderUtils.drawText(context, "Open .CCS File", button.x + 7, button.y + button.height / 3, 0.7F, false);
                }).build();
        openScriptsButton = AbstractElement.create().dimensions(navWidth, 12)
                .tooltip("Open scripts folder")
                .onPress(button -> system.openFile(Config.PATH_SCRIPTS))
                .onRender((context, mouseX, mouseY, button) -> {
                    if (button.isHovered(mouseX, mouseY)) {
                        RoundRectBrush.drawRoundHoriLine(context, button.x, button.y, navWidth, button.height, Gray.LIGHT_GRAY);
                    }
                    RenderUtils.drawText(context, "Open Scripts", button.x + 7, button.y + button.height / 3, 0.7F, false);
                }).build();
        deleteButton = AbstractElement.create().dimensions(navWidth, 12)
                .tooltip("Delete this script (Can't Undo This!)")
                .onPress(button -> deleteScript())
                .onRender((context, mouseX, mouseY, button) -> {
                    if (button.isHovered(mouseX, mouseY)) {
                        RoundRectBrush.drawRoundHoriLine(context, button.x, button.y, navWidth, button.height, Gray.LIGHT_GRAY);
                    }
                    RenderUtils.drawText(context, "§cDelete File", button.x + 7, button.y + button.height / 3, 0.7F, false);
                }).build();

        this.addChild(saveButton);
        this.addChild(saveAndCloseButton);
        this.addChild(closeButton);
        this.addChild(discardChangesButton);
        this.addChild(openFileButton);
        this.addChild(openScriptsButton);
        this.addChild(deleteButton);

        this.selected = textField;
    }

    @Override
    public void baseRender(MatrixStack context, int mouseX, int mouseY, float delta) {
        renderOpaqueBackground(context);

        context.push();
        context.translate(baseX, baseY, 0);

        // backdrop
        RoundRectBrush.drawRoundRect(context, 0, 0, baseWidth, baseHeight, 10, Gray.BLACK);
        RoundRectBrush.drawTabTop(context, 110, 10, 300, 230, 10, Gray.DARK_GRAY);

        // navbar
        String text;
        int caret = 10;

        RenderUtils.drawTexture(context, Tex.ICON, 8, caret - 2, 10, 10);
        text = "ClickCrystals v%s".formatted(version);
        RenderUtils.drawText(context, text, 22, 11, 0.7F, false);
        caret += 10;
        RenderUtils.drawHorizontalLine(context, 10, caret, 90, 1, Gray.GRAY.argb);
        caret += 6;
        buttonHome.x = baseX + 10;
        buttonHome.y = baseY + caret;
        caret += 12;
        buttonModules.x = baseX + 10;
        buttonModules.y = baseY + caret;
        caret += 12;
        buttonNews.x = baseX + 10;
        buttonNews.y = baseY + caret;
        caret += 12;
        buttonSettings.x = baseX + 10;
        buttonSettings.y = baseY + caret;

        caret += 16;
        RenderUtils.drawHorizontalLine(context, 10, caret, 90, 1, Gray.GRAY.argb);
        caret += 6;
        saveButton.x = baseX + 10;
        saveButton.y = baseY + caret;
        caret += 16;
        saveAndCloseButton.x = baseX + 10;
        saveAndCloseButton.y = baseY + caret;
        caret += 16;
        closeButton.x = baseX + 10;
        closeButton.y = baseY + caret;
        caret += 16;
        discardChangesButton.x = baseX + 10;
        discardChangesButton.y = baseY + caret;

        caret += 16;
        RenderUtils.drawHorizontalLine(context, 10, caret, 90, 1, Gray.GRAY.argb);
        caret += 6;
        openFileButton.x = baseX + 10;
        openFileButton.y = baseY + caret;
        caret += 16;
        openScriptsButton.x = baseX + 10;
        openScriptsButton.y = baseY + caret;
        caret += 16;
        deleteButton.x = baseX + 10;
        deleteButton.y = baseY + caret;

        context.pop();


        // content
        caret = contentY + 10;
        RenderUtils.drawTexture(context, Tex.ICON_CLICKSCRIPT, contentX + 10, caret - 7, 15, 15);
        RenderUtils.drawText(context, "Editing '%s'".formatted(filename), contentX + 30, caret - 4, false);
        caret += 10;
        RenderUtils.drawHorizontalLine(context, contentX, caret, 300, 1, Gray.BLACK.argb);
    }

    public void loadContents() {
        if (loading.isRendering()) {
            return;
        }
        CompletableFuture.runAsync(() -> {
            loading.setRendering(true);
            try {
                File file = new File(filepath);
                if (!FileValidationUtils.validate(file)) {
                    throw new IllegalStateException("File not found!");
                }

                BufferedReader reader = new BufferedReader(new FileReader(file));
                String str = "";

                for (var i = reader.lines().iterator(); i.hasNext();) {
                    str = str.concat(i.next() + "\n");
                }
                reader.close();

                String finalStr = str;
                textField.clear();
                textField.onInput(input -> textField.insertInput(finalStr));
                textField.shiftEnd();
            }
            catch (Exception ex) {
                system.printErr("Failed to load IDE contents: " + ex.getMessage());
                UserInputListener.openPreviousScreen();
            }
        }).thenRun(() -> {
            loading.setRendering(false);
        });
    }

    public Voidable<CompletableFuture<Void>> saveContents() {
        if (loading.isRendering()) {
            return Voidable.of(null);
        }
        return Voidable.of(CompletableFuture.runAsync(() -> {
            loading.setRendering(true);
            try {
                File file = new File(filepath);
                if (!FileValidationUtils.validate(file)) {
                    throw new IllegalStateException("File not found!");
                }

                BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                writer.write(textField.getContent());
                writer.close();
            }
            catch (Exception ex) {
                system.printErr("Error: IDE failed to save script");
                UserInputListener.openPreviousScreen();
            }
            ReloadCommand.reload();
        }).thenRun(() -> {
            loading.setRendering(false);
        }));
    }

    public void deleteScript() {
        try {
            File file = new File(filepath);
            if (file.delete()) {
                ReloadCommand.reload();
            }
            else {
                throw new IllegalStateException("file refused");
            }
        }
        catch (Exception ex) {
            system.printErr("Error: cannot delete script");
            system.printErr(ex.getMessage());
        }

        BrowsingScreen.currentCategory = Categories.SCRIPTED;
        mc.execute(() -> mc.setScreen(new BrowsingScreen()));
    }

    @Override
    public void resize(MinecraftClient client, int width, int height) {
        mc.setScreen(new ClickScriptIDE(new File(filepath)));
    }
}

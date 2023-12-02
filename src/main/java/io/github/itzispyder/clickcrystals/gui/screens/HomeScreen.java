package io.github.itzispyder.clickcrystals.gui.screens;

import io.github.itzispyder.clickcrystals.gui.GuiScreen;
import io.github.itzispyder.clickcrystals.gui.elements.interactive.SearchBarElement;
import io.github.itzispyder.clickcrystals.gui.elements.interactive.SearchResultsElement;
import io.github.itzispyder.clickcrystals.gui.elements.interactive.SuggestionElement;
import io.github.itzispyder.clickcrystals.gui.misc.Gray;
import io.github.itzispyder.clickcrystals.gui.misc.Tex;
import io.github.itzispyder.clickcrystals.gui.misc.brushes.RoundRectBrush;
import io.github.itzispyder.clickcrystals.gui.misc.organizers.GridOrganizer;
import io.github.itzispyder.clickcrystals.util.minecraft.RenderUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class HomeScreen extends GuiScreen {

    public static boolean OPENED_BEFORE = false;
    public final int windowWidth = MinecraftClient.getInstance().getWindow().getScaledWidth();
    public final int windowHeight = MinecraftClient.getInstance().getWindow().getScaledHeight();
    public final int baseWidth = 420;
    public final int baseHeight = 240;
    public final int baseX = (int)(windowWidth / 2.0 - baseWidth / 2.0);
    public final int baseY = (int)(windowHeight / 2.0 - baseHeight / 2.0);
    public final SearchBarElement searchBar = new SearchBarElement(0, 0, 300) {{
        this.height = 15;
    }};
    public final SearchResultsElement searchResults = new SearchResultsElement(searchBar);
    protected final AtomicInteger titleTranslation = new AtomicInteger(-50);
    public final List<SuggestionElement> suggestions = new ArrayList<>();

    public HomeScreen() {
        super("ClickCrystals Home Screen");

        GridOrganizer grid = new GridOrganizer(baseX + 27, baseY + baseHeight - 65, 35, 35, 10, 31);
        suggestions.add(new SuggestionElement(Tex.Socials.DISCORD, "Discord", 0, 0, 35, button -> system.openUrl("https://discord.gg/tMaShNzNtP")));
        suggestions.add(new SuggestionElement(Tex.Socials.MODRINTH, "Modrinth", 0, 0, 35, button -> system.openUrl("https://modrinth.com/mod/clickcrystals")));
        suggestions.add(new SuggestionElement(Tex.Socials.YOUTUBE, "Youtube", 0, 0, 35, button -> system.openUrl("https://youtube.com/@itzispyder")));
        suggestions.add(new SuggestionElement(Tex.Socials.PLANETMC, "Planet MC", 0, 0, 35, button -> system.openUrl("https://planetminecraft.com/mod/clickcrystal")));
        suggestions.add(new SuggestionElement(Tex.ICON, "Official Site", 0, 0, 35, button -> system.openUrl("https://itzispyder.github.io/clickcrystals")));
        suggestions.add(new SuggestionElement(Tex.Icons.MODULES, "Browse Modules", 0, 0, 35, button -> mc.setScreen(new ModuleScreen())));

        suggestions.forEach(grid::addEntry);
        grid.organize();
        grid.createPanel(this, baseHeight / 4);
        grid.addAllToPanel();
        this.addChild(grid.getPanel());

        for (SuggestionElement suggestion : suggestions) {
            suggestion.y -= 50;
        }

        system.scheduler.runRepeatingTask(() -> {
            titleTranslation.getAndIncrement();
            for (SuggestionElement suggestion : suggestions) {
                suggestion.y++;
            }
        }, 0, 1, 50);

        // added last so it renders last and registers first
        this.addChild(searchResults);
        this.addChild(searchBar);
    }

    @Override
    public void baseRender(MatrixStack context, int mouseX, int mouseY, float delta) {
        RenderUtils.fillGradient(context, 0, 0, windowWidth, windowHeight, 0xA03873A9, 0xA0000000);

        // title card
        context.push();
        context.translate(baseX, baseY, 0);

        RoundRectBrush.drawRoundRect(context, 0, 0, baseWidth, baseHeight, 10, Gray.BLACK);
        RoundRectBrush.drawTabBottom(context, 15, baseHeight / 2 - 10, baseWidth - 30, baseHeight / 2, 10, Gray.DARK_GRAY);
        RenderUtils.drawTexture(context, Tex.Backdrops.BACKDROP_0, 10, 10, baseWidth - 20, baseHeight / 2 + 40);

        context.pop();

        // content
        int caret = baseY + 50;
        int titleTrans = titleTranslation.get();

        RenderUtils.drawCenteredText(context, "§lClickCrystals", baseX + baseWidth / 2, caret - titleTrans, 2.0F, false);
        caret += 20;
        RenderUtils.drawCenteredText(context, "Crystal PvP Enhanced", baseX + baseWidth / 2, caret - titleTrans, 1.0F, false);
        caret += 30;
        searchBar.x = baseX + baseWidth / 2 - searchBar.width / 2;
        searchBar.y = caret - titleTrans;
    }

    @Override
    public void resize(MinecraftClient client, int width, int height) {
        client.setScreen(new HomeScreen());
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        super.keyPressed(keyCode, scanCode, modifiers);
        if (keyCode == GLFW.GLFW_KEY_ENTER && this.selected == searchBar && !searchBar.getQuery().isEmpty()) {
            mc.setScreen(new SearchScreen() {{
                this.searchbar.setQuery(HomeScreen.this.searchBar.getQuery());
                this.filterByQuery(this.searchbar);
            }});
        }
        return true;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (!OPENED_BEFORE) {
            mc.setScreen(new DiscordInviteScreen());
            OPENED_BEFORE = true;
            return true;
        }

        super.mouseClicked(mouseX, mouseY, button);
        return true;
    }
}

package io.github.itzispyder.clickcrystals.modules.modules;

import io.github.itzispyder.clickcrystals.client.clickscript.ClickScript;
import io.github.itzispyder.clickcrystals.client.networking.EntityStatusType;
import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.events.client.KeyPressEvent;
import io.github.itzispyder.clickcrystals.events.events.client.MouseClickEvent;
import io.github.itzispyder.clickcrystals.events.events.client.TakeDamageEvent;
import io.github.itzispyder.clickcrystals.events.events.networking.PacketReceiveEvent;
import io.github.itzispyder.clickcrystals.events.events.networking.PacketSendEvent;
import io.github.itzispyder.clickcrystals.events.events.world.*;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import io.github.itzispyder.clickcrystals.util.misc.Timer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ScriptedModule extends ListenerModule {

    public static final String PATH = "ClickCrystalsClient/scripts";
    public final List<ClickListener> clickListeners = new ArrayList<>();
    public final List<KeyListener> keyListeners = new ArrayList<>();
    public final List<MoveListener> moveListeners = new ArrayList<>();
    public final List<TickListener> tickListeners = new ArrayList<>();
    public final List<BlockPlaceListener> blockPlaceListeners = new ArrayList<>();
    public final List<BlockBreakListener> blockBreakListeners = new ArrayList<>();
    public final List<BlockPunchListener> blockPunchListeners = new ArrayList<>();
    public final List<BlockInteractListener> blockInteractListeners = new ArrayList<>();
    public final List<ItemUseListener> itemUseListeners = new ArrayList<>();
    public final List<ItemConsumeListener> itemConsumeListeners = new ArrayList<>();
    public final List<Runnable> totemPopListeners = new ArrayList<>();
    public final List<Runnable> moduleEnableListeners = new ArrayList<>();
    public final List<Runnable> moduleDisableListeners = new ArrayList<>();
    public final List<Runnable> damageListeners = new ArrayList<>();
    public final List<Runnable> deathListeners = new ArrayList<>();
    public final String filepath, filename;

    public ScriptedModule(String name, String description, File file) {
        super(name, Categories.SCRIPTED, description);
        this.filepath = file.getPath();
        this.filename = file.getName();
    }

    @Override
    protected void onEnable() {
        super.onEnable();
        moduleEnableListeners.forEach(Runnable::run);
    }

    @Override
    protected void onDisable() {
        super.onDisable();
        moduleDisableListeners.forEach(Runnable::run);
    }

    public void clearListeners() {
        clickListeners.clear();
        keyListeners.clear();
        moveListeners.clear();
        tickListeners.clear();

        totemPopListeners.clear();
        moduleEnableListeners.clear();
        moduleDisableListeners.clear();
        damageListeners.clear();
        deathListeners.clear();

        blockPlaceListeners.clear();
        blockBreakListeners.clear();
        blockPunchListeners.clear();
        blockInteractListeners.clear();

        itemUseListeners.clear();
        itemConsumeListeners.clear();
    }

    @EventHandler
    public void onMouseClick(MouseClickEvent e) {
        if (e.isScreenNull()) {
            for (ClickListener l : clickListeners) {
                l.pass(e);
            }
        }
    }

    @EventHandler
    public void onTick(ClientTickEndEvent e) {
        for (TickListener l : tickListeners) {
            l.pass(e);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        for (BlockPlaceListener l : blockPlaceListeners) {
            l.pass(e);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        for (BlockBreakListener l : blockBreakListeners) {
            l.pass(e);
        }
    }

    @EventHandler
    public void onItemUse(ItemUseEvent e) {
        for (ItemUseListener l : itemUseListeners) {
            l.pass(e);
        }
    }

    @EventHandler
    public void onItemConsume(ItemConsumeEvent e) {
        for (ItemConsumeListener l : itemConsumeListeners) {
            l.pass(e);
        }
    }

    @EventHandler
    public void onPacketSend(PacketSendEvent e) {
        if (e.getPacket() instanceof PlayerInteractBlockC2SPacket packet) {
            for (BlockInteractListener l : blockInteractListeners) {
                l.pass(packet.getBlockHitResult(), packet.getHand());
            }
        }
        else if (e.getPacket() instanceof PlayerActionC2SPacket packet) {
            if (packet.getAction() == PlayerActionC2SPacket.Action.START_DESTROY_BLOCK) {
                for (BlockPunchListener l : blockPunchListeners) {
                    l.pass(packet.getPos(), packet.getDirection());
                }
            }
        }
        else if (e.getPacket() instanceof PlayerMoveC2SPacket packet) {
            for (MoveListener l : moveListeners) {
                l.pass(packet);
            }
        }
    }

    @EventHandler
    public void onPacketReceive(PacketReceiveEvent e) {
        if (PlayerUtils.playerNull()) {
            return;
        }

        if (e.getPacket() instanceof EntityStatusS2CPacket packet) {
            if (packet.getEntity(PlayerUtils.getWorld()) instanceof PlayerEntity p && p.getId() == PlayerUtils.player().getId()) {
                if (packet.getStatus() == EntityStatusType.TOTEM_POP) {
                    totemPopListeners.forEach(Runnable::run);
                }
                else if (packet.getStatus() == EntityStatusType.DEATH) {
                    deathListeners.forEach(Runnable::run);
                }
            }
        }
    }

    @EventHandler
    public void onKeyPress(KeyPressEvent e) {
        if (e.isScreenNull()) {
            for (KeyListener l : keyListeners) {
                l.pass(e);
            }
        }
    }

    @EventHandler
    public void onDamage(TakeDamageEvent e) {
        damageListeners.forEach(Runnable::run);
    }

    @FunctionalInterface
    public interface ClickListener {
        void pass(MouseClickEvent e);
    }

    @FunctionalInterface
    public interface TickListener {
        void pass(ClientTickEndEvent e);
    }

    @FunctionalInterface
    public interface BlockBreakListener {
        void pass(BlockBreakEvent e);
    }

    @FunctionalInterface
    public interface BlockPlaceListener {
        void pass(BlockPlaceEvent e);
    }

    @FunctionalInterface
    public interface BlockPunchListener {
        void pass(BlockPos pos, Direction dir);
    }

    @FunctionalInterface
    public interface BlockInteractListener {
        void pass(BlockHitResult hit, Hand hand);
    }

    @FunctionalInterface
    public interface ItemUseListener {
        void pass(ItemUseEvent e);
    }

    @FunctionalInterface
    public interface ItemConsumeListener {
        void pass(ItemConsumeEvent e);
    }

    @FunctionalInterface
    public interface KeyListener {
        void pass(KeyPressEvent e);
    }

    @FunctionalInterface
    public interface MoveListener {
        void pass(PlayerMoveC2SPacket e);
    }

    public static void runModuleScripts() {
        File parentFolder = new File(PATH);
        if (!parentFolder.exists() || !parentFolder.isDirectory()) {
            parentFolder.mkdirs();
            return;
        }

        File[] files = parentFolder.listFiles(f -> f.isFile() && f.getPath().endsWith(".ccs"));
        if (files == null || files.length == 0) {
            return;
        }

        Timer timer = Timer.start();
        int total = files.length;

        system.printf("-> executing scripts ({x})...", total);
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            new ClickScript(file).execute();
            system.printf("<- [{x}/{x}] '{x}'", i + 1, total, file.getName());
        }
        system.printf("<- [done] executed ({x}) scripts in {x}", total, timer.end().getStampPrecise());
    }
}
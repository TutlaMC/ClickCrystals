package io.github.itzispyder.clickcrystals.scripting.syntax.macros;

import io.github.itzispyder.clickcrystals.events.listeners.TickEventListener;
import io.github.itzispyder.clickcrystals.modules.keybinds.Keybind;
import io.github.itzispyder.clickcrystals.scripting.ScriptArgs;
import io.github.itzispyder.clickcrystals.scripting.ScriptCommand;
import io.github.itzispyder.clickcrystals.scripting.syntax.InputType;
import io.github.itzispyder.clickcrystals.util.minecraft.InteractionUtils;

// @Format toggle_input <input> (true|false)
// @Format toggle_input key ... (true|false)
// @Format toggle_input cancel
public class ToggleInputCmd extends ScriptCommand {

    public ToggleInputCmd() {
        super("toggle_input");
    }

    @Override
    public void onCommand(ScriptCommand command, String line, ScriptArgs args) {
        if (args.match(0, "cancel")) {
            TickEventListener.cancelToggleInputs();
            return;
        }

        var read = args.getReader();
        InputType a = read.next(InputType.class);

        if (a == InputType.KEY) {
            String key = read.nextStr();
            int parsedKey = Keybind.fromExtendedKeyName(key);
            boolean toggle = read.next().toBool();

            if (parsedKey == Keybind.NONE)
                throw new IllegalArgumentException("not a valid key: '%s'".formatted(key));

            InteractionUtils.toggleKey(parsedKey, Keybind.DEFAULT_SCANCODE, toggle);
            read.executeThenChain();
            return;
        }

        boolean toggle = read.next().toBool();
        a.toggle(toggle);
        read.executeThenChain();
    }
}

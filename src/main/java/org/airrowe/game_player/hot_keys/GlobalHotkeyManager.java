package org.airrowe.game_player.hot_keys;
import com.sun.jna.platform.win32.*;
import com.sun.jna.platform.win32.WinUser.MSG;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class GlobalHotkeyManager {

    private static final User32 USER32 = User32.INSTANCE;
    private static final AtomicInteger ID_GEN = new AtomicInteger(1);
    private static final Map<Integer, Runnable> ACTIONS = new ConcurrentHashMap<>();

    private static volatile boolean running = true;
    private static Thread messageThread;

    // Modifier flags
    public static final int ALT   = WinUser.MOD_ALT;
    public static final int CTRL  = WinUser.MOD_CONTROL;
    public static final int SHIFT = WinUser.MOD_SHIFT;
    public static final int WIN   = WinUser.MOD_WIN;

    static {
        startMessageLoop();
    }

    private static void startMessageLoop() {
        messageThread = new Thread(() -> {
            MSG msg = new MSG();
            while (running && USER32.GetMessage(msg, null, 0, 0) != 0) {
                if (msg.message == WinUser.WM_HOTKEY) {
                    int id = msg.wParam.intValue();
                    Runnable action = ACTIONS.get(id);
                    if (action != null) {
                        action.run();
                    }
                }
            }
        }, "GlobalHotkeyMessageLoop");

        messageThread.setDaemon(true);
        messageThread.start();
    }

    /**
     * Registers a global hotkey
     */
    public static int registerHotkey(int modifiers, int virtualKey, Runnable action) {
        int id = ID_GEN.getAndIncrement();

        if (!USER32.RegisterHotKey(null, id, modifiers, virtualKey)) {
            throw new RuntimeException("Failed to register hotkey: modifiers="
                    + modifiers + " key=" + virtualKey);
        }

        ACTIONS.put(id, action);
        return id;
    }

    /**
     * Unregister a specific hotkey
     */
    public static void unregisterHotkey(int id) {
        USER32.UnregisterHotKey(null, id);
        ACTIONS.remove(id);
    }

    /**
     * Shutdown and unregister all hotkeys
     */
    public static void shutdown() {
        running = false;
        for (int id : ACTIONS.keySet()) {
            USER32.UnregisterHotKey(null, id);
        }
        ACTIONS.clear();
    }
}
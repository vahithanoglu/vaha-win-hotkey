package com.vahabilisim.win.hotkey;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class HotkeyFactory {

    private static final int HOTKEY_SPEED = 100;

    private static final ScheduledExecutorService SERVICE = new ScheduledThreadPoolExecutor(1);

    private static final Map<Long, Hotkey> HOTKEYS = new ConcurrentHashMap<>();

    private static final AtomicLong ID = new AtomicLong(System.currentTimeMillis());

    private static boolean INITIALIZED = false;

    public static synchronized boolean init() {
        if (INITIALIZED) {
            return true;
        }

        final String arch = System.getProperty("sun.arch.data.model", "none");
        switch (arch) {
            case "32":
                INITIALIZED = loadLibrary("win_native_library_Win32.dll");
                break;

            case "64":
                INITIALIZED = loadLibrary("win_native_library_x64.dll");
                break;

            default:
                INITIALIZED = false;
                break;
        }

        if (INITIALIZED) {
            SERVICE.scheduleAtFixedRate(() -> {
                HOTKEYS.values().forEach((hotKey) -> {
                    hotKey.checkKeyPresses();
                    if (hotKey.isPressed(HOTKEY_SPEED)) {
                        hotKey.getCallback().onPressed();
                    }
                });
            }, 0, HOTKEY_SPEED, TimeUnit.MILLISECONDS);

        }

        return INITIALIZED;
    }

    public static long createHotkey(Hotkey.Callback callback, Hotkey.KeyCombination... kc) {
        final long id = ID.getAndIncrement();
        HOTKEYS.put(id, new Hotkey(id, callback, kc));
        return id;
    }

    public static void removeHotkey(long id) {
        HOTKEYS.remove(id);
    }

    private static boolean loadLibrary(String lib) {
        try {
            final File file = File.createTempFile("win_native_library_", ".dll");
            file.deleteOnExit();

            Files.copy(
                    Hotkey.class.getResourceAsStream(lib),
                    file.toPath(),
                    StandardCopyOption.REPLACE_EXISTING);

            System.load(file.getAbsolutePath());
            return true;

        } catch (Throwable t) {
            t.printStackTrace(System.err);
            return false;
        }
    }
}

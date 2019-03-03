package com.vahabilisim.win.hotkey;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Hotkey {

    public native boolean isKeyPressed(int key);

    private final long id;
    private final Callback callback;
    private final Map<KeyCombination, Long> timestamps;
    private final Set<KeyCombination> keyCombinations;

    public Hotkey(long id, Callback callback, KeyCombination... kcs) {
        this.id = id;
        this.callback = callback;

        timestamps = new ConcurrentHashMap<>();
        keyCombinations = Stream.of(kcs).collect(Collectors.toSet());
    }

    public Callback getCallback() {
        return callback;
    }

    public boolean isPressed(int interval) {
        long min = Long.MAX_VALUE;
        long max = Long.MIN_VALUE;

        for (KeyCombination kc : keyCombinations) {
            long time = timestamps.getOrDefault(kc, Long.MIN_VALUE);
            min = Math.min(min, time);
            max = Math.max(max, time);
        }

        return min != Long.MIN_VALUE
                && max != Long.MIN_VALUE
                && (max - min) <= interval;
    }

    public void checkKeyPresses() {
        keyCombinations.forEach(kc -> {
            timestamps.put(kc, isKeyPressed(kc.key) ? System.currentTimeMillis() : Long.MIN_VALUE);
        });
    }

    public static interface Callback {

        public void onPressed();
    }

    public static enum KeyCombination {

        SHIFT(16),
        CTRL(17),
        ALT(18),
        F9(120),
        F10(121),
        F11(122),
        F12(123);

        public final int key;

        private KeyCombination(int key) {
            this.key = key;
        }
    }
}

package com.vahabilisim.win.hotkey.example;

import com.vahabilisim.win.hotkey.Hotkey;
import com.vahabilisim.win.hotkey.HotkeyFactory;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Example {

    public static void main(String args[]) {
        if (HotkeyFactory.init()) {
            createExampleHotKey("F11 Frame", Hotkey.KeyCombination.F11);
            createExampleHotKey("SHIFT Frame", Hotkey.KeyCombination.SHIFT);
            createExampleHotKey("CTRL ALT Frame", Hotkey.KeyCombination.ALT, Hotkey.KeyCombination.CTRL);
        }
    }

    private static void createExampleHotKey(String frameTitle, Hotkey.KeyCombination... kc) {
        final JFrame frame = new JFrame(frameTitle);
        final Dimension dim = new Dimension(400, 300);
        frame.setSize(dim);
        frame.setMinimumSize(dim);
        frame.setMaximumSize(dim);
        frame.setPreferredSize(dim);
        frame.setResizable(false);
        frame.setAlwaysOnTop(true);
        frame.setLocationByPlatform(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        HotkeyFactory.createHotkey(() -> {
            SwingUtilities.invokeLater(() -> {
                frame.setVisible(!frame.isVisible());
            });
        }, kc);
    }

}

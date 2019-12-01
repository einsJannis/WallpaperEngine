package dev.ludium.wallpaperengine;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.win32.W32APIOptions;

public interface User32 extends Library {
    User32 INSTANCE = (User32) Native.load("user32",User32.class, W32APIOptions.DEFAULT_OPTIONS);
    boolean SystemParametersInfo (int one, int two, String s ,int three);
}

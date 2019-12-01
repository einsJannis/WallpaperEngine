package dev.ludium.wallpaperengine.util;

public class PathUtil {
    public static String parseToAbsolutePath(String currentPath, String relativePath) {
        if (isPathAbsolute(relativePath)) {
            return relativePath;
        } else if (relativePath.startsWith("~")) {
            return System.getProperty("user.home") + currentPath.substring(1);
        } else if (relativePath.startsWith("..")) {
            relativePath = relativePath.substring(2);
            String path = currentPath.substring(0,currentPath.lastIndexOf("\\"));
            while (relativePath.startsWith("..")) {
                path.substring(0,path.lastIndexOf("\\"));
                relativePath = relativePath.substring(3);
            }
            return path + relativePath;
        } else if (relativePath.startsWith(".")) {
            return currentPath + relativePath.substring(1);
        } else {
            return currentPath + "\\" + relativePath;
        }
    }

    public static boolean isPathAbsolute(String relativePath) {
        boolean b = false;
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        for (int i = 0; i < alphabet.length() && !b; i++) {
            b = relativePath.startsWith(alphabet.charAt(i) + ":");
        }
        return b;
    }
}

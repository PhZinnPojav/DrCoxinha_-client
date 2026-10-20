package com.drcoxinha.client.gui;

import org.lwjgl.glfw.GLFW;

public final class KeyNames {
    private KeyNames() {}

    public static String of(int key) {
        if (key < 0) return "Nenhuma";
        if (key >= GLFW.GLFW_KEY_A && key <= GLFW.GLFW_KEY_Z) return String.valueOf((char) key);
        if (key >= GLFW.GLFW_KEY_0 && key <= GLFW.GLFW_KEY_9) return String.valueOf((char) key);
        if (key >= GLFW.GLFW_KEY_F1 && key <= GLFW.GLFW_KEY_F12) return "F" + (key - GLFW.GLFW_KEY_F1 + 1);
        return switch (key) {
            case GLFW.GLFW_KEY_SPACE -> "Espaco";
            case GLFW.GLFW_KEY_TAB -> "Tab";
            case GLFW.GLFW_KEY_CAPS_LOCK -> "Caps Lock";
            case GLFW.GLFW_KEY_LEFT_SHIFT -> "Shift Esq.";
            case GLFW.GLFW_KEY_RIGHT_SHIFT -> "Shift Dir.";
            case GLFW.GLFW_KEY_LEFT_CONTROL -> "Ctrl Esq.";
            case GLFW.GLFW_KEY_RIGHT_CONTROL -> "Ctrl Dir.";
            case GLFW.GLFW_KEY_LEFT_ALT -> "Alt Esq.";
            case GLFW.GLFW_KEY_RIGHT_ALT -> "Alt Dir.";
            case GLFW.GLFW_KEY_UP -> "Seta Cima";
            case GLFW.GLFW_KEY_DOWN -> "Seta Baixo";
            case GLFW.GLFW_KEY_LEFT -> "Seta Esq.";
            case GLFW.GLFW_KEY_RIGHT -> "Seta Dir.";
            case GLFW.GLFW_KEY_INSERT -> "Insert";
            case GLFW.GLFW_KEY_HOME -> "Home";
            case GLFW.GLFW_KEY_END -> "End";
            default -> {
                String n = GLFW.glfwGetKeyName(key, 0);
                yield n != null ? n.toUpperCase() : "Tecla " + key;
            }
        };
    }
}

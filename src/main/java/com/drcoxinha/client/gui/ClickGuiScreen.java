package com.drcoxinha.client.gui;

import com.drcoxinha.client.config.ConfigManager;
import com.drcoxinha.client.module.Category;
import com.drcoxinha.client.module.Module;
import com.drcoxinha.client.module.ModuleManager;
import com.drcoxinha.client.setting.*;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Menu do client. Toda a entrada (mouse/teclado) e lida por polling do GLFW dentro do
 * render(), de proposito: assim o menu nao depende das assinaturas de mouseClicked/keyPressed,
 * que mudaram entre versoes do Minecraft.
 */
public class ClickGuiScreen extends Screen {
    private static final int W = 400;
    private static final int H = 250;
    private static final int ROW_H = 20;
    private static final int VISIBLE_ROWS = 8;

    private Category tab = Category.PVP;
    private Module selected;

    private boolean prevLeft;
    private boolean prevRight;
    private final boolean[] prevKeys = new boolean[GLFW.GLFW_KEY_LAST + 1];

    private KeySetting listening;

    private int scroll;
    private Module lastSelected;
    private final Set<ColorSetting> expanded = new HashSet<>();

    private NumberSetting dragNum;
    private ColorSetting dragCol;
    private int dragChannel;
    private int dragX;
    private int dragW;

    public ClickGuiScreen() {
        super(Text.literal("DrCoxinha_ Client"));
        this.selected = first();
    }

    private Module first() {
        List<Module> l = ModuleManager.byCategory(tab);
        return l.isEmpty() ? null : l.get(0);
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return listening == null;
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (verticalAmount > 0) {
            scroll--;
        } else if (verticalAmount < 0) {
            scroll++;
        }
        return true;
    }

    @Override
    public void removed() {
        ConfigManager.save();
    }

    private static boolean inside(int mx, int my, int x, int y, int w, int h) {
        return mx >= x && mx < x + w && my >= y && my < y + h;
    }

    private static double clamp01(double v) {
        return Math.max(0.0, Math.min(1.0, v));
    }

    private void text(DrawContext ctx, String s, int x, int y, int color) {
        ctx.drawTextWithShadow(this.textRenderer, s, x, y, color);
    }

    private void pollKeys(long win) {
        for (int k = GLFW.GLFW_KEY_SPACE; k <= GLFW.GLFW_KEY_LAST; k++) {
            boolean down = GLFW.glfwGetKey(win, k) == GLFW.GLFW_PRESS;
            if (down && !prevKeys[k] && listening != null) {
                if (k == GLFW.GLFW_KEY_ESCAPE) {
                    listening = null;
                } else if (k == GLFW.GLFW_KEY_BACKSPACE) {
                    listening.set(KeySetting.NONE);
                    listening = null;
                } else if (k != GLFW.GLFW_KEY_RIGHT_SHIFT) {
                    listening.set(k);
                    listening = null;
                }
            }
            prevKeys[k] = down;
        }
    }

    @Override
    public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
        long win = this.client.getWindow().getHandle();
        boolean left = GLFW.glfwGetMouseButton(win, GLFW.GLFW_MOUSE_BUTTON_LEFT) == GLFW.GLFW_PRESS;
        boolean right = GLFW.glfwGetMouseButton(win, GLFW.GLFW_MOUSE_BUTTON_RIGHT) == GLFW.GLFW_PRESS;
        boolean clickL = left && !prevLeft;
        boolean clickR = right && !prevRight;
        prevLeft = left;
        prevRight = right;
        if (!left) {
            dragNum = null;
            dragCol = null;
        }

        pollKeys(win);

        // fundo escurecido
        ctx.fill(0, 0, this.width, this.height, 0x99000000);

        int x0 = (this.width - W) / 2;
        int y0 = (this.height - H) / 2;

        // janela
        ctx.fill(x0 - 1, y0 - 1, x0 + W + 1, y0 + H + 1, Theme.ORANGE);
        ctx.fill(x0, y0, x0 + W, y0 + H, Theme.BG);

        // cabecalho
        ctx.fill(x0, y0, x0 + W, y0 + 24, Theme.HEADER);
        ctx.fill(x0, y0 + 24, x0 + W, y0 + 25, Theme.ORANGE);
        text(ctx, "DrCoxinha_ Client", x0 + 10, y0 + 8, Theme.ORANGE);

        // abas (PvP | Finder), alinhadas a direita
        int tx = x0 + W - 10;
        Category[] cats = Category.values();
        for (int i = cats.length - 1; i >= 0; i--) {
            Category c = cats[i];
            int tw = this.textRenderer.getWidth(c.label) + 20;
            tx -= tw;
            boolean hov = inside(mouseX, mouseY, tx, y0, tw, 24);
            boolean active = c == tab;
            if (hov && !active) ctx.fill(tx, y0, tx + tw, y0 + 24, Theme.ROW_HOVER);
            text(ctx, c.label, tx + 10, y0 + 8, active ? Theme.ORANGE : Theme.MUTED);
            if (active) ctx.fill(tx, y0 + 22, tx + tw, y0 + 25, Theme.ORANGE);
            if (hov && clickL && !active) {
                tab = c;
                selected = first();
                listening = null;
            }
            tx -= 4;
        }

        // lista de modulos
        int lx = x0 + 10;
        int ly = y0 + 34;
        int lw = 160;
        List<Module> list = ModuleManager.byCategory(tab);
        for (int i = 0; i < list.size(); i++) {
            Module m = list.get(i);
            int ry = ly + i * 20;
            boolean hov = inside(mouseX, mouseY, lx, ry, lw, 18);
            int bg = m == selected ? Theme.ROW_SEL : (hov ? Theme.ROW_HOVER : Theme.ROW);
            ctx.fill(lx, ry, lx + lw, ry + 18, bg);
            if (m.isEnabled()) ctx.fill(lx, ry, lx + 3, ry + 18, Theme.ORANGE);
            text(ctx, m.name, lx + 8, ry + 5, m.isEnabled() ? Theme.TEXT : Theme.MUTED);
            text(ctx, ">", lx + lw - 10, ry + 5, m == selected ? Theme.ORANGE : Theme.MUTED);
            if (hov && clickL) {
                if (mouseX >= lx + lw - 18) {
                    selected = m;
                } else {
                    m.toggle();
                }
                listening = null;
            }
            if (hov && clickR) {
                selected = m;
                listening = null;
            }
        }

        // painel de configuracoes
        int sx = x0 + 180;
        int sy = y0 + 34;
        int sw = W - 190;
        ctx.fill(sx - 5, sy - 3, sx - 4, y0 + H - 22, Theme.ORANGE_DARK);
        if (selected != null) {
            text(ctx, selected.name, sx, sy, Theme.ORANGE);
            String desc = this.textRenderer.trimToWidth(selected.description, sw);
            text(ctx, desc, sx, sy + 11, Theme.MUTED);
            if (selected != lastSelected) {
                scroll = 0;
                lastSelected = selected;
            }
            if (selected.settings.isEmpty()) {
                text(ctx, "Sem configuracoes.", sx, sy + 30, Theme.MUTED);
            } else {
                int total = rowCount(selected);
                scroll = Math.max(0, Math.min(scroll, Math.max(0, total - VISIBLE_ROWS)));
                drawSettings(ctx, mouseX, mouseY, clickL, left, sx, sy + 26, sw);
                drawScrollbar(ctx, sx + sw + 3, sy + 26, total);
            }
        }

        // rodape
        text(ctx, "Esq: ligar  Dir/>: configs  Roda: rolar  Shift Dir.: fechar",
                x0 + 10, y0 + H - 14, Theme.MUTED);
    }

    private void slider(DrawContext ctx, int x, int y, int w, String label, String value, double frac, boolean hov) {
        ctx.fill(x, y, x + w, y + 18, hov ? Theme.ROW_HOVER : Theme.ROW);
        int fw = (int) (w * clamp01(frac));
        ctx.fill(x, y, x + fw, y + 18, Theme.ORANGE_DARK);
        text(ctx, label, x + 6, y + 5, Theme.TEXT);
        text(ctx, value, x + w - 6 - this.textRenderer.getWidth(value), y + 5, Theme.ORANGE);
    }

    private void row(DrawContext ctx, int x, int y, int w, String label, String value, int valueColor, boolean hov) {
        ctx.fill(x, y, x + w, y + 18, hov ? Theme.ROW_HOVER : Theme.ROW);
        text(ctx, label, x + 6, y + 5, Theme.TEXT);
        text(ctx, value, x + w - 6 - this.textRenderer.getWidth(value), y + 5, valueColor);
    }

    private int rowCount(Module m) {
        int n = 0;
        for (Setting<?> st : m.settings) {
            n += (st instanceof ColorSetting c && expanded.contains(c)) ? 4 : 1;
        }
        return n;
    }

    private void drawScrollbar(DrawContext ctx, int x, int y, int total) {
        if (total <= VISIBLE_ROWS) return;
        int track = VISIBLE_ROWS * ROW_H - 2;
        int thumb = Math.max(10, track * VISIBLE_ROWS / total);
        int maxScroll = total - VISIBLE_ROWS;
        int pos = (track - thumb) * scroll / maxScroll;
        ctx.fill(x, y, x + 3, y + track, Theme.ROW);
        ctx.fill(x, y + pos, x + 3, y + pos + thumb, Theme.ORANGE);
    }

    private void drawSettings(DrawContext ctx, int mouseX, int mouseY, boolean clickL, boolean left,
                              int sx, int startY, int sw) {
        int r = 0;
        for (Setting<?> s : selected.settings) {
            int y = startY + (r - scroll) * ROW_H;
            boolean visible = r >= scroll && r < scroll + VISIBLE_ROWS;
            boolean hov = visible && inside(mouseX, mouseY, sx, y, sw, 18);

            if (s instanceof ColorSetting c) {
                boolean open = expanded.contains(c);
                if (visible) {
                    ctx.fill(sx, y, sx + sw, y + 18, hov ? Theme.ROW_HOVER : Theme.ROW);
                    text(ctx, (open ? "v " : "> ") + c.name, sx + 6, y + 5, Theme.TEXT);
                    int swx = sx + sw - 6 - 28;
                    ctx.fill(swx - 1, y + 3, swx + 29, y + 15, Theme.MUTED);
                    ctx.fill(swx, y + 4, swx + 28, y + 14, 0xFF000000 | c.get());
                    if (hov && clickL) {
                        if (open) expanded.remove(c); else expanded.add(c);
                    }
                }
                r++;
                if (open) {
                    String[] names = {"  Vermelho", "  Verde", "  Azul"};
                    for (int ch = 0; ch < 3; ch++) {
                        int cy = startY + (r - scroll) * ROW_H;
                        boolean cVisible = r >= scroll && r < scroll + VISIBLE_ROWS;
                        if (cVisible) {
                            boolean h = inside(mouseX, mouseY, sx, cy, sw, 18);
                            int v = c.channel(ch);
                            slider(ctx, sx, cy, sw, names[ch], String.valueOf(v), v / 255.0, h);
                            if (h && clickL) {
                                dragCol = c;
                                dragChannel = ch;
                                dragNum = null;
                                dragX = sx;
                                dragW = sw;
                            }
                        }
                        if (dragCol == c && dragChannel == ch && left) {
                            c.setChannel(ch, (int) Math.round(255.0 * clamp01((mouseX - dragX) / (double) dragW)));
                        }
                        r++;
                    }
                }
                continue;
            }

            if (visible) {
                if (s instanceof BoolSetting b) {
                    boolean on = b.get();
                    row(ctx, sx, y, sw, b.name, on ? "ON" : "OFF", on ? Theme.ORANGE : Theme.MUTED, hov);
                    if (hov && clickL) b.toggle();

                } else if (s instanceof NumberSetting n) {
                    double frac = (n.get() - n.min) / (n.max - n.min);
                    slider(ctx, sx, y, sw, n.name, n.display(), frac, hov);
                    if (hov && clickL) {
                        dragNum = n;
                        dragCol = null;
                        dragX = sx;
                        dragW = sw;
                    }

                } else if (s instanceof KeySetting k) {
                    boolean waiting = listening == k;
                    row(ctx, sx, y, sw, k.name, waiting ? "..." : KeyNames.of(k.get()),
                            waiting ? Theme.ORANGE : Theme.TEXT, hov);
                    if (hov && clickL) listening = waiting ? null : k;

                } else if (s instanceof ModeSetting m) {
                    row(ctx, sx, y, sw, m.name, m.get(), Theme.ORANGE, hov);
                    if (hov && clickL) m.next();
                }
            }
            if (s instanceof NumberSetting n && dragNum == n && left) {
                n.setFromFraction((mouseX - dragX) / (double) dragW);
            }
            r++;
        }
        if (listening != null) {
            text(ctx, "Aperte uma tecla (Esc cancela, Backspace limpa)", sx, startY + VISIBLE_ROWS * ROW_H, Theme.MUTED);
        }
    }
}

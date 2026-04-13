package ui;

import asset.UIAsset;
import main.GamePanel;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.*;

/**
 * Settings screen for Tower Defense game.
 *
 * Tabs: AUDIO | DISPLAY | GAMEPLAY | CONTROLS
 * Each tab renders sliders, toggles, and dropdowns in a dark medieval-gold style.
 */
public class Setting {

    // ── Dependencies ─────────────────────────────────────────────────────────
    private final UIAsset   uiAsset;
    private final GamePanel gp;

    // ── Layout constants ──────────────────────────────────────────────────────
    private static final int PANEL_X      = 0;
    private static final int PANEL_Y      = 0;    
    private static final int PANEL_W      = 1280;
    private static final int PANEL_H      = 768;
    private static final int TAB_NAV_W    = 130;
    private static final int CONTENT_X    = PANEL_X + TAB_NAV_W;
    private static final int CONTENT_Y    = PANEL_Y + 54;
    private static final int CONTENT_W    = PANEL_W - TAB_NAV_W - 2;
    private static final int CONTENT_H    = PANEL_H - 54 - 46;   // minus header & footer
    private static final int FOOTER_Y     = PANEL_Y + PANEL_H - 46;

    // ── Colors ────────────────────────────────────────────────────────────────
    private static final Color COL_BG         = new Color(13,  15,  20);
    private static final Color COL_PANEL      = new Color(20,  24,  32);
    private static final Color COL_BORDER     = new Color(42,  48,  64);
    private static final Color COL_GOLD       = new Color(200, 169,  74);
    private static final Color COL_GOLD_LIGHT = new Color(232, 204, 122);
    private static final Color COL_TEXT       = new Color(212, 201, 168);
    private static final Color COL_TEXT_DIM   = new Color(122, 112,  96);
    private static final Color COL_ACCENT     = new Color( 58, 127, 212);
    private static final Color COL_TOGGLE_OFF = new Color( 42,  48,  64);
    private static final Color COL_SUCCESS    = new Color( 39, 174,  96);

    // ── Tabs ──────────────────────────────────────────────────────────────────
    private static final String[] TAB_LABELS = {"AUDIO", "DISPLAY", "GAMEPLAY", "CONTROLS"};
    private int activeTab = 0;

    // ── Audio settings ────────────────────────────────────────────────────────
    private int masterVolume  = 80;
    private int musicVolume   = 60;
    private int sfxVolume     = 90;
    private int ambientVolume = 40;
    private boolean muteWhenMinimized = true;
    private boolean voiceNarration    = false;

    // ── Display settings ──────────────────────────────────────────────────────
    private int   resolutionIndex = 1;   // 0=720p 1=1080p 2=1440p 3=4K
    private int   qualityIndex    = 2;   // 0=Low … 3=Ultra
    private boolean fullscreen    = true;
    private boolean vsync         = true;
    private boolean showFps       = false;
    private int   brightness      = 100;

    // ── Gameplay settings ─────────────────────────────────────────────────────
    private int     difficultyIndex  = 1;   // 0=Easy 1=Normal 2=Hard 3=Nightmare
    private int     gameSpeed        = 100; // 50-200 in steps of 25
    private boolean showTowerRange   = true;
    private boolean sellConfirm      = true;
    private boolean enemyHealthBars  = true;
    private boolean waveTimer        = true;
    private int     languageIndex    = 0;

    // ── Controls settings ─────────────────────────────────────────────────────
    private boolean scrollToZoom = true;
    private int     edgePanSpeed = 5;

    // ── Drag state ────────────────────────────────────────────────────────────
    private int  draggingSlider  = -1;   // encoded id
    private int  sliderDragStartX;

    // ── Toast / save feedback ─────────────────────────────────────────────────
    private int toastAlpha   = 0;
    private long toastExpiry = 0;

    // ── Hover ─────────────────────────────────────────────────────────────────
    private int hoveredTab   = -1;
    private int hoveredCtrl  = -1;   // lightweight hover tracking

    // ─────────────────────────────────────────────────────────────────────────
    public Setting(GamePanel gamePanel) {
        this.gp      = gamePanel;
        this.uiAsset = UIAsset.getInstance();
    }

    // =========================================================================
    // UPDATE
    // =========================================================================
    public void update() {
        // Fade out toast
        if (toastAlpha > 0 && System.currentTimeMillis() > toastExpiry) {
            toastAlpha = Math.max(0, toastAlpha - 12);
        }
    }

    // =========================================================================
    // RENDER
    // =========================================================================
    public void render(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        drawDimOverlay(g2);
        drawPanelBackground(g2);
        drawCornerAccents(g2);
        drawHeader(g2);
        drawTabNav(g2);
        drawContentArea(g2);
        drawFooter(g2);
        drawToast(g2);
    }

    // ── Overlay ───────────────────────────────────────────────────────────────
    private void drawDimOverlay(Graphics2D g2) {
        g2.setColor(new Color(0, 0, 0, 160));
        g2.fillRect(0, 0, gp.getWidth(), gp.getHeight());
    }

    // ── Panel background ──────────────────────────────────────────────────────
    private void drawPanelBackground(Graphics2D g2) {
        GradientPaint grad = new GradientPaint(
                PANEL_X, PANEL_Y, COL_PANEL,
                PANEL_X, PANEL_Y + PANEL_H, COL_BG);
        g2.setPaint(grad);
        g2.fillRoundRect(PANEL_X, PANEL_Y, PANEL_W, PANEL_H, 6, 6);

        g2.setColor(COL_BORDER);
        g2.setStroke(new BasicStroke(1f));
        g2.drawRoundRect(PANEL_X, PANEL_Y, PANEL_W, PANEL_H, 6, 6);
    }

    // ── Gold corner accents ───────────────────────────────────────────────────
    private void drawCornerAccents(Graphics2D g2) {
        g2.setColor(COL_GOLD);
        g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        int s = 18;
        // TL
        g2.drawLine(PANEL_X, PANEL_Y + s, PANEL_X, PANEL_Y);
        g2.drawLine(PANEL_X, PANEL_Y, PANEL_X + s, PANEL_Y);
        // TR
        int rx = PANEL_X + PANEL_W;
        g2.drawLine(rx - s, PANEL_Y, rx, PANEL_Y);
        g2.drawLine(rx, PANEL_Y, rx, PANEL_Y + s);
        // BL
        int by = PANEL_Y + PANEL_H;
        g2.drawLine(PANEL_X, by - s, PANEL_X, by);
        g2.drawLine(PANEL_X, by, PANEL_X + s, by);
        // BR
        g2.drawLine(rx - s, by, rx, by);
        g2.drawLine(rx, by, rx, by - s);
    }

    // ── Header ────────────────────────────────────────────────────────────────
    private void drawHeader(Graphics2D g2) {
        int headerH = 54;
        // subtle gold tint background
        g2.setColor(new Color(200, 169, 74, 10));
        g2.fillRect(PANEL_X + 1, PANEL_Y + 1, PANEL_W - 2, headerH);

        // bottom border
        g2.setColor(COL_BORDER);
        g2.setStroke(new BasicStroke(1f));
        g2.drawLine(PANEL_X, PANEL_Y + headerH, PANEL_X + PANEL_W, PANEL_Y + headerH);

        // title
        Font titleFont = new Font("Serif", Font.BOLD, 16);
        g2.setFont(titleFont);
        FontMetrics fm = g2.getFontMetrics();
        String title = "\u2699  S E T T I N G S";
        int tx = PANEL_X + (PANEL_W - fm.stringWidth(title)) / 2;
        int ty = PANEL_Y + (headerH + fm.getAscent() - fm.getDescent()) / 2;
        g2.setColor(COL_GOLD_LIGHT);
        g2.drawString(title, tx, ty);

        // decorative lines
        int lineY = PANEL_Y + headerH / 2;
        int lineLen = 80;
        drawFadeLineH(g2, tx - 20 - lineLen, lineY, lineLen, true);
        drawFadeLineH(g2, tx + fm.stringWidth(title) + 20, lineY, lineLen, false);
    }

    private void drawFadeLineH(Graphics2D g2, int x, int y, int len, boolean fadeRight) {
        Color c1 = fadeRight ? COL_GOLD : new Color(200, 169, 74, 0);
        Color c2 = fadeRight ? new Color(200, 169, 74, 0) : COL_GOLD;
        g2.setPaint(new GradientPaint(x, y, c1, x + len, y, c2));
        g2.setStroke(new BasicStroke(1f));
        g2.drawLine(x, y, x + len, y);
    }

    // ── Tab navigation ────────────────────────────────────────────────────────
    private void drawTabNav(Graphics2D g2) {
        int navX = PANEL_X + 1;
        int navY = PANEL_Y + 55;
        int navH = PANEL_H - 55 - 46;

        // background
        g2.setColor(new Color(0, 0, 0, 50));
        g2.fillRect(navX, navY, TAB_NAV_W - 1, navH);

        // right border
        g2.setColor(COL_BORDER);
        g2.setStroke(new BasicStroke(1f));
        g2.drawLine(navX + TAB_NAV_W - 1, navY, navX + TAB_NAV_W - 1, navY + navH);

        int tabH = 44;
        String[] icons = {"\u266A", "\u25C8", "\u2694", "\u2328"};
        Font tabFont = new Font("Serif", Font.PLAIN, 11);
        g2.setFont(tabFont);

        for (int i = 0; i < TAB_LABELS.length; i++) {
            int ty = navY + 12 + i * (tabH + 4);
            boolean active = (i == activeTab);
            boolean hover  = (i == hoveredTab);

            if (active) {
                g2.setColor(new Color(200, 169, 74, 20));
                g2.fillRect(navX, ty, TAB_NAV_W - 1, tabH);
                // active indicator bar
                g2.setColor(COL_GOLD);
                g2.fillRoundRect(navX + TAB_NAV_W - 4, ty + 10, 3, tabH - 20, 2, 2);
            } else if (hover) {
                g2.setColor(new Color(200, 169, 74, 10));
                g2.fillRect(navX, ty, TAB_NAV_W - 1, tabH);
            }

            // icon
            g2.setFont(new Font("Serif", Font.PLAIN, 14));
            g2.setColor(active ? COL_GOLD_LIGHT : COL_TEXT_DIM);
            g2.drawString(icons[i], navX + 14, ty + tabH / 2 + 5);

            // label
            g2.setFont(tabFont);
            g2.drawString(TAB_LABELS[i], navX + 34, ty + tabH / 2 + 4);
        }
    }

    // ── Content area dispatcher ───────────────────────────────────────────────
    private void drawContentArea(Graphics2D g2) {
        // clip to content region
        Shape oldClip = g2.getClip();
        g2.setClip(CONTENT_X, CONTENT_Y, CONTENT_W, CONTENT_H);
        switch (activeTab) {
            case 0 -> drawAudioTab(g2);
            case 1 -> drawDisplayTab(g2);
            case 2 -> drawGameplayTab(g2);
            case 3 -> drawControlsTab(g2);
        }
        g2.setClip(oldClip);
    }

    // ── AUDIO tab ─────────────────────────────────────────────────────────────
    private void drawAudioTab(Graphics2D g2) {
        int y = CONTENT_Y + 16;
        drawSectionLabel(g2, "Volume", CONTENT_X, y);  y += 24;
        y = drawSliderRow(g2, "Master Volume",   masterVolume,  CONTENT_X, y, 0); y += 4;
        y = drawSliderRow(g2, "Music",           musicVolume,   CONTENT_X, y, 1); y += 4;
        y = drawSliderRow(g2, "Sound Effects",   sfxVolume,     CONTENT_X, y, 2); y += 4;
        y = drawSliderRow(g2, "Ambient",         ambientVolume, CONTENT_X, y, 3); y += 16;
        drawSectionLabel(g2, "Options", CONTENT_X, y); y += 24;
        y = drawToggleRow(g2, "Mute when minimized", muteWhenMinimized, CONTENT_X, y, 10); y += 4;
            drawToggleRow(g2, "Voice narration",     voiceNarration,    CONTENT_X, y, 11);
    }

    // ── DISPLAY tab ───────────────────────────────────────────────────────────
    private void drawDisplayTab(Graphics2D g2) {
        int y = CONTENT_Y + 16;
        drawSectionLabel(g2, "Resolution", CONTENT_X, y); y += 24;
        String[] res = {"1280 × 720", "1920 × 1080", "2560 × 1440", "3840 × 2160"};
        y = drawResolutionGrid(g2, res, CONTENT_X, y); y += 14;

        drawSectionLabel(g2, "Graphics", CONTENT_X, y); y += 24;
        String[] q = {"Low", "Medium", "High", "Ultra"};
        y = drawDropdownRow(g2, "Quality", q, qualityIndex, CONTENT_X, y, 20); y += 4;
        y = drawToggleRow(g2, "Fullscreen", fullscreen, CONTENT_X, y, 21);     y += 4;
        y = drawToggleRow(g2, "VSync",      vsync,      CONTENT_X, y, 22);     y += 4;
        y = drawToggleRow(g2, "Show FPS",   showFps,    CONTENT_X, y, 23);     y += 4;
            drawSliderRow(g2, "Brightness", brightness - 50, CONTENT_X, y, 24);
    }

    // ── GAMEPLAY tab ──────────────────────────────────────────────────────────
    private void drawGameplayTab(Graphics2D g2) {
        int y = CONTENT_Y + 16;
        drawSectionLabel(g2, "Difficulty", CONTENT_X, y); y += 24;
        String[] diff = {"Easy", "Normal", "Hard", "Nightmare"};
        y = drawDropdownRow(g2, "Difficulty", diff, difficultyIndex, CONTENT_X, y, 30); y += 4;
        y = drawSliderRow(g2, "Game Speed (" + (gameSpeed / 100) + "×)", gameSpeed - 50, CONTENT_X, y, 31); y += 14;

        drawSectionLabel(g2, "Interface", CONTENT_X, y); y += 24;
        y = drawToggleRow(g2, "Show Tower Range",     showTowerRange,  CONTENT_X, y, 32); y += 4;
        y = drawToggleRow(g2, "Auto-sell confirm",    sellConfirm,     CONTENT_X, y, 33); y += 4;
        y = drawToggleRow(g2, "Enemy health bars",    enemyHealthBars, CONTENT_X, y, 34); y += 4;
            drawToggleRow(g2, "Wave countdown timer", waveTimer,       CONTENT_X, y, 35);
    }

    // ── CONTROLS tab ──────────────────────────────────────────────────────────
    private void drawControlsTab(Graphics2D g2) {
        int y = CONTENT_Y + 16;
        drawSectionLabel(g2, "Keyboard Shortcuts", CONTENT_X, y); y += 24;
        String[][] binds = {
                {"Pause / Resume",  "SPACE"},
                {"Speed Up",        "TAB"},
                {"Select Tower 1",  "1"},
                {"Select Tower 2",  "2"},
                {"Upgrade",         "U"},
                {"Sell Tower",      "DEL"},
                {"Open Settings",   "ESC"},
        };
        for (String[] b : binds) {
            y = drawKeybindRow(g2, b[0], b[1], CONTENT_X, y); y += 2;
        }
        y += 10;
        drawSectionLabel(g2, "Mouse", CONTENT_X, y); y += 24;
        y = drawToggleRow(g2, "Scroll to zoom", scrollToZoom, CONTENT_X, y, 40); y += 4;
            drawSliderRow(g2, "Edge pan speed", (edgePanSpeed - 1) * 11, CONTENT_X, y, 41);
    }

    // ── Footer ────────────────────────────────────────────────────────────────
    private void drawFooter(Graphics2D g2) {
        g2.setColor(new Color(0, 0, 0, 40));
        g2.fillRect(PANEL_X + 1, FOOTER_Y, PANEL_W - 2, 45);
        g2.setColor(COL_BORDER);
        g2.drawLine(PANEL_X, FOOTER_Y, PANEL_X + PANEL_W, FOOTER_Y);

        // Left: back to menu button (danger-tinted)
        drawTextButton(g2, "\u2190  Menu",     PANEL_X + 14,            FOOTER_Y + 10, 100, 26, false, 52);

        // Right: reset & save
        drawTextButton(g2, "Reset Defaults", PANEL_X + PANEL_W - 280, FOOTER_Y + 10, 120, 26, false, 50);
        drawTextButton(g2, "Save Changes",   PANEL_X + PANEL_W - 148, FOOTER_Y + 10, 128, 26, true,  51);
    }

    // ── Toast ─────────────────────────────────────────────────────────────────
    private void drawToast(Graphics2D g2) {
        if (toastAlpha <= 0) return;
        int tw = 160, th = 28;
        int tx = PANEL_X + PANEL_W - tw - 22;
        int ty = FOOTER_Y - th - 8;

        g2.setColor(new Color(20, 24, 32, toastAlpha));
        g2.fillRoundRect(tx, ty, tw, th, 4, 4);
        g2.setColor(new Color(39, 174, 96, toastAlpha));
        g2.setStroke(new BasicStroke(1f));
        g2.drawRoundRect(tx, ty, tw, th, 4, 4);

        g2.setFont(new Font("Serif", Font.PLAIN, 12));
        g2.setColor(new Color(111, 207, 151, toastAlpha));
        g2.drawString("\u2713  Settings Saved", tx + 18, ty + 18);
    }

    // =========================================================================
    // Sub-component drawing helpers
    // =========================================================================

    private void drawSectionLabel(Graphics2D g2, String label, int x, int y) {
        g2.setFont(new Font("Serif", Font.BOLD, 10));
        g2.setColor(COL_GOLD);
        g2.drawString(label.toUpperCase(), x + 8, y + 10);
        g2.setColor(COL_BORDER);
        g2.setStroke(new BasicStroke(1f));
        FontMetrics fm = g2.getFontMetrics();
        int lx = x + 8 + fm.stringWidth(label.toUpperCase()) + 8;
        g2.drawLine(lx, y + 5, x + CONTENT_W - 8, y + 5);
    }

    /** Returns new y after drawing. slotId used to identify slider for mouse interaction. */
    private int drawSliderRow(Graphics2D g2, String name, int value0to100, int x, int y, int slotId) {
        int rowH = 36;
        // row bg (alternating)
        g2.setColor(new Color(255, 255, 255, 4));
        g2.fillRect(x, y, CONTENT_W, rowH);

        // name
        g2.setFont(new Font("Serif", Font.PLAIN, 14));
        g2.setColor(COL_TEXT);
        g2.drawString(name, x + 10, y + rowH / 2 + 5);

        // slider track
        int sliderW = 110;
        int sliderX = x + CONTENT_W - sliderW - 50;
        int sliderY = y + rowH / 2;
        int filled  = (int) (sliderW * (value0to100 / 100.0));

        g2.setColor(COL_BORDER);
        g2.fillRoundRect(sliderX, sliderY - 2, sliderW, 4, 2, 2);
        g2.setColor(COL_GOLD);
        g2.fillRoundRect(sliderX, sliderY - 2, filled, 4, 2, 2);

        // thumb
        g2.setColor(COL_GOLD);
        g2.fillOval(sliderX + filled - 6, sliderY - 6, 13, 13);
        g2.setColor(COL_BG);
        g2.fillOval(sliderX + filled - 4, sliderY - 4, 9, 9);

        // value label
        g2.setFont(new Font("Serif", Font.BOLD, 11));
        g2.setColor(COL_GOLD_LIGHT);
        g2.drawString(value0to100 + "%", x + CONTENT_W - 40, y + rowH / 2 + 5);

        return y + rowH;
    }

    private int drawToggleRow(Graphics2D g2, String name, boolean on, int x, int y, int slotId) {
        int rowH = 36;
        g2.setColor(new Color(255, 255, 255, 4));
        g2.fillRect(x, y, CONTENT_W, rowH);

        g2.setFont(new Font("Serif", Font.PLAIN, 14));
        g2.setColor(COL_TEXT);
        g2.drawString(name, x + 10, y + rowH / 2 + 5);

        // toggle pill
        int tw = 38, th = 20;
        int tx = x + CONTENT_W - tw - 14;
        int ty = y + (rowH - th) / 2;
        g2.setColor(on ? COL_ACCENT : COL_TOGGLE_OFF);
        g2.fillRoundRect(tx, ty, tw, th, th, th);

        g2.setColor(Color.WHITE);
        int thumbX = on ? tx + tw - th + 3 : tx + 3;
        g2.fillOval(thumbX, ty + 3, th - 6, th - 6);

        return y + rowH;
    }

    private int drawDropdownRow(Graphics2D g2, String name, String[] options, int selectedIdx,
                                int x, int y, int slotId) {
        int rowH = 36;
        g2.setColor(new Color(255, 255, 255, 4));
        g2.fillRect(x, y, CONTENT_W, rowH);

        g2.setFont(new Font("Serif", Font.PLAIN, 14));
        g2.setColor(COL_TEXT);
        g2.drawString(name, x + 10, y + rowH / 2 + 5);

        int dw = 100, dh = 22;
        int dx = x + CONTENT_W - dw - 14;
        int dy = y + (rowH - dh) / 2;
        g2.setColor(COL_PANEL);
        g2.fillRoundRect(dx, dy, dw, dh, 3, 3);
        g2.setColor(COL_BORDER);
        g2.setStroke(new BasicStroke(1f));
        g2.drawRoundRect(dx, dy, dw, dh, 3, 3);

        g2.setFont(new Font("Serif", Font.PLAIN, 13));
        g2.setColor(COL_TEXT);
        g2.drawString(options[selectedIdx], dx + 8, dy + 15);
        // arrow
        g2.setColor(COL_TEXT_DIM);
        g2.drawString("\u25BE", dx + dw - 16, dy + 15);

        return y + rowH;
    }

    private int drawResolutionGrid(Graphics2D g2, String[] options, int x, int y) {
        int cellW = (CONTENT_W - 16) / 2;
        int cellH = 28;
        int gap   = 6;
        Font f = new Font("Serif", Font.PLAIN, 12);
        g2.setFont(f);
        FontMetrics fm = g2.getFontMetrics();

        for (int i = 0; i < options.length; i++) {
            int col = i % 2;
            int row = i / 2;
            int cx  = x + 8 + col * (cellW + gap);
            int cy  = y + row * (cellH + gap);
            boolean sel = (i == resolutionIndex);

            g2.setColor(sel ? new Color(200, 169, 74, 20) : COL_PANEL);
            g2.fillRoundRect(cx, cy, cellW, cellH, 3, 3);
            g2.setColor(sel ? COL_GOLD : COL_BORDER);
            g2.setStroke(new BasicStroke(sel ? 1.5f : 1f));
            g2.drawRoundRect(cx, cy, cellW, cellH, 3, 3);

            g2.setColor(sel ? COL_GOLD_LIGHT : COL_TEXT_DIM);
            int tw = fm.stringWidth(options[i]);
            g2.drawString(options[i], cx + (cellW - tw) / 2, cy + 18);
        }
        return y + 2 * (cellH + gap) + 4;
    }

    private int drawKeybindRow(Graphics2D g2, String action, String key, int x, int y) {
        int rowH = 30;
        g2.setColor(new Color(255, 255, 255, 3));
        g2.fillRect(x, y, CONTENT_W, rowH);
        g2.setColor(COL_BORDER);
        g2.setStroke(new BasicStroke(.5f));
        g2.drawLine(x, y + rowH, x + CONTENT_W, y + rowH);

        g2.setFont(new Font("Serif", Font.PLAIN, 14));
        g2.setColor(COL_TEXT);
        g2.drawString(action, x + 10, y + 20);

        // key badge
        FontMetrics fm = g2.getFontMetrics(new Font("Serif", Font.BOLD, 11));
        int kw = fm.stringWidth(key) + 18;
        int kx = x + CONTENT_W - kw - 14;
        int ky = y + 5;
        g2.setColor(COL_PANEL);
        g2.fillRoundRect(kx, ky, kw, 20, 3, 3);
        g2.setColor(COL_BORDER);
        g2.setStroke(new BasicStroke(1f));
        g2.drawRoundRect(kx, ky, kw, 20, 3, 3);

        g2.setFont(new Font("Serif", Font.BOLD, 11));
        g2.setColor(COL_GOLD_LIGHT);
        g2.drawString(key, kx + 9, ky + 14);

        return y + rowH;
    }

    private void drawTextButton(Graphics2D g2, String label, int x, int y,
                                int w, int h, boolean primary, int slotId) {
        boolean hover = (hoveredCtrl == slotId);
        boolean isBack = (slotId == 52);

        if (isBack) {
            g2.setColor(hover ? new Color(192, 57, 43, 55) : new Color(192, 57, 43, 25));
            g2.fillRoundRect(x, y, w, h, 3, 3);
            g2.setColor(hover ? new Color(192, 57, 43, 180) : COL_BORDER);
        } else if (primary) {
            g2.setColor(hover ? new Color(200, 169, 74, 60) : new Color(200, 169, 74, 38));
            g2.fillRoundRect(x, y, w, h, 3, 3);
            g2.setColor(COL_GOLD);
        } else {
            g2.setColor(hover ? new Color(255, 255, 255, 18) : new Color(0, 0, 0, 0));
            g2.fillRoundRect(x, y, w, h, 3, 3);
            g2.setColor(COL_BORDER);
        }
        g2.setStroke(new BasicStroke(1f));
        g2.drawRoundRect(x, y, w, h, 3, 3);

        Font bf = new Font("Serif", Font.BOLD, 12);
        g2.setFont(bf);
        FontMetrics fm = g2.getFontMetrics();
        if (isBack)        g2.setColor(hover ? new Color(231, 76, 60) : COL_TEXT_DIM);
        else if (primary)  g2.setColor(COL_GOLD_LIGHT);
        else               g2.setColor(COL_TEXT_DIM);
        g2.drawString(label, x + (w - fm.stringWidth(label)) / 2, y + h / 2 + 4);
    }

    // =========================================================================
    // INPUT HANDLING
    // =========================================================================
    public void mousePressed(int x, int y) {
        // Tab clicks
        if (x >= PANEL_X + 1 && x <= PANEL_X + TAB_NAV_W) {
            int relY = y - (PANEL_Y + 67);
            if (relY >= 0) {
                int idx = relY / 48;
                if (idx < TAB_LABELS.length) activeTab = idx;
            }
        }
    }

    public void mouseReleased(int x, int y) {
        // Resolution grid
        if (activeTab == 1) {
            int cellW = (CONTENT_W - 16) / 2;
            int cellH = 28, gap = 6;
            int gridY = CONTENT_Y + 16 + 24;
            for (int i = 0; i < 4; i++) {
                int col = i % 2, row = i / 2;
                int cx = CONTENT_X + 8 + col * (cellW + gap);
                int cy = gridY + row * (cellH + gap);
                if (x >= cx && x <= cx + cellW && y >= cy && y <= cy + cellH) {
                    resolutionIndex = i;
                }
            }
        }
        // Footer buttons
        if (y >= FOOTER_Y + 10 && y <= FOOTER_Y + 36) {
            if (x >= PANEL_X + 14            && x <= PANEL_X + 114)             goToMenu();
            if (x >= PANEL_X + PANEL_W - 280 && x <= PANEL_X + PANEL_W - 160) resetDefaults();
            if (x >= PANEL_X + PANEL_W - 148 && x <= PANEL_X + PANEL_W - 20)  saveSettings();
        }
        // Toggle handling (simplified: checks each tab row)
        handleToggleClick(x, y);
        draggingSlider = -1;
    }

    public void mouseMoved(int x, int y) {
        // Tab hover
        hoveredTab = -1;
        if (x >= PANEL_X + 1 && x <= PANEL_X + TAB_NAV_W) {
            int relY = y - (PANEL_Y + 67);
            if (relY >= 0) {
                int idx = relY / 48;
                if (idx < TAB_LABELS.length) hoveredTab = idx;
            }
        }
        // Button hover
        hoveredCtrl = -1;
        if (y >= FOOTER_Y + 10 && y <= FOOTER_Y + 36) {
            if (x >= PANEL_X + 14          && x <= PANEL_X + 114)             hoveredCtrl = 52;
            if (x >= PANEL_X + PANEL_W - 280 && x <= PANEL_X + PANEL_W - 160) hoveredCtrl = 50;
            if (x >= PANEL_X + PANEL_W - 148 && x <= PANEL_X + PANEL_W - 20)  hoveredCtrl = 51;
        }
    }

    // ── Toggle click dispatch ─────────────────────────────────────────────────
    private void handleToggleClick(int x, int y) {
        int toggleX = CONTENT_X + CONTENT_W - 38 - 14;
        if (x < toggleX || x > toggleX + 38) return;

        if (activeTab == 0) {
            int base = CONTENT_Y + 16 + 24;
            int rowH = 36;
            int[] toggleRows = {base + 4 * rowH + 16 + 24, base + 5 * rowH + 16 + 24};
            if      (isInRow(y, toggleRows[0], rowH)) muteWhenMinimized = !muteWhenMinimized;
            else if (isInRow(y, toggleRows[1], rowH)) voiceNarration    = !voiceNarration;
        } else if (activeTab == 1) {
            int base = CONTENT_Y + 16 + 24 + 70 + 14 + 24 + 36 + 4;
            int rowH = 36;
            if      (isInRow(y, base,           rowH)) fullscreen = !fullscreen;
            else if (isInRow(y, base + rowH + 4, rowH)) vsync    = !vsync;
            else if (isInRow(y, base + 2*(rowH+4), rowH)) showFps = !showFps;
        } else if (activeTab == 2) {
            int base = CONTENT_Y + 16 + 24 + 36 + 4 + 36 + 14 + 24;
            int rowH = 36;
            if      (isInRow(y, base,           rowH)) showTowerRange  = !showTowerRange;
            else if (isInRow(y, base + rowH + 4, rowH)) sellConfirm   = !sellConfirm;
            else if (isInRow(y, base + 2*(rowH+4), rowH)) enemyHealthBars = !enemyHealthBars;
            else if (isInRow(y, base + 3*(rowH+4), rowH)) waveTimer   = !waveTimer;
        } else if (activeTab == 3) {
            int base = CONTENT_Y + 16 + 24 + 7 * 32 + 10 + 24;
            int rowH = 36;
            if (isInRow(y, base, rowH)) scrollToZoom = !scrollToZoom;
        }
    }

    private boolean isInRow(int y, int rowY, int rowH) {
        return y >= rowY && y <= rowY + rowH;
    }

    // ── Actions ───────────────────────────────────────────────────────────────
    private void resetDefaults() {
        masterVolume = 80; musicVolume = 60; sfxVolume = 90; ambientVolume = 40;
        muteWhenMinimized = true; voiceNarration = false;
        resolutionIndex = 1; qualityIndex = 2; fullscreen = true;
        vsync = true; showFps = false; brightness = 100;
        difficultyIndex = 1; gameSpeed = 100;
        showTowerRange = true; sellConfirm = true; enemyHealthBars = true; waveTimer = true;
        scrollToZoom = true; edgePanSpeed = 5;
    }

    private void saveSettings() {
        // TODO: persist to file / GamePanel config
        toastAlpha  = 255;
        toastExpiry = System.currentTimeMillis() + 1600;
    }

    private void goToMenu() {
        gp.getGameStateManager().setState(new states.MenuState(gp));
    }

    // ── Getters (used by GamePanel / other systems) ───────────────────────────
    public int  getMasterVolume()  { return masterVolume; }
    public int  getMusicVolume()   { return musicVolume; }
    public int  getSfxVolume()     { return sfxVolume; }
    public boolean isFullscreen()  { return fullscreen; }
    public int  getDifficulty()    { return difficultyIndex; }
    public float getGameSpeed()    { return gameSpeed / 100f; }
}
package ui;

import java.awt.*;
import java.awt.image.BufferedImage;

public class MyButton {

    private int x, y, width, height;
    private String text;
    private Rectangle bounds;

    private boolean mouseOver, mousePressed;

    private BufferedImage normalImage, overImage, pressedImage;
    private Runnable action;

    private boolean isImageButton;

    public enum ButtonType {
        UI, 
        SKILL   
    }

    private ButtonType type = ButtonType.UI;


    private long lastUsedTime = 0;
    private long cooldown = 0;
    private boolean onCooldown = false;

    public MyButton(String text, int width, int height) {
        this.text = text;
        this.width = width;
        this.height = height;
        this.isImageButton = false;
        initBounds();
    }

    public MyButton(BufferedImage normal, BufferedImage over, BufferedImage pressed, int width, int height) {
        this.normalImage = normal;
        this.overImage = over;
        this.pressedImage = pressed;
        this.width = width;
        this.height = height;
        this.isImageButton = true;
        initBounds();
    }

    private void initBounds() {
        bounds = new Rectangle(x, y, width, height);
    }

    public void setButton(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        initBounds();
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setAction(Runnable action) {
        this.action = action;
    }

    public void setType(ButtonType type) {
        this.type = type;
    }

    public void setCooldown(long cooldown) {
        this.cooldown = cooldown;
    }

    public void execute() {
        if (type == ButtonType.SKILL && onCooldown) return;

        if (action != null) {
            action.run();

            if (type == ButtonType.SKILL && cooldown > 0) {
                startCooldown();
            }
        }
    }

    private void startCooldown() {
        onCooldown = true;
        lastUsedTime = System.currentTimeMillis();
    }

    public void update() {
        if (type == ButtonType.SKILL && onCooldown) {
            long now = System.currentTimeMillis();

            if (now - lastUsedTime >= cooldown) {
                onCooldown = false; // 🔥 đảm bảo tắt đúng lúc
            }
        }
    }

    public void draw(Graphics g) {
        if (isImageButton) drawImageButton(g);
        else {
            drawBody(g);
            drawBorder(g);
            drawText(g);
        }

        drawCooldownOverlay(g);
    }

    private void drawBody(Graphics g) {
        if (mouseOver) g.setColor(Color.GRAY);
        else g.setColor(Color.WHITE);

        g.fillRect(x, y, width, height);
    }

    private void drawBorder(Graphics g) {
        g.setColor(Color.BLACK);
        g.drawRect(x, y, width, height);

        if (mousePressed) {
            g.drawRect(x + 1, y + 1, width - 2, height - 2);
        }
    }

    private void drawText(Graphics g) {
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.setColor(Color.BLACK);

        FontMetrics fm = g.getFontMetrics();

        int textX = x + (width - fm.stringWidth(text)) / 2;
        int textY = y + ((height - fm.getHeight()) / 2) + fm.getAscent();

        g.drawString(text, textX, textY);
    }

    private void drawImageButton(Graphics g) {
        if (type == ButtonType.SKILL && onCooldown) {
            g.drawImage(normalImage, x, y, width, height, null);
            return;
        }

        if (mousePressed)
            g.drawImage(pressedImage, x, y, width, height, null);
        else if (mouseOver)
            g.drawImage(overImage, x, y, width, height, null);
        else
            g.drawImage(normalImage, x, y, width, height, null);
    }

    private void drawCooldownOverlay(Graphics g) {
        if (type != ButtonType.SKILL || !onCooldown) return;

        long now = System.currentTimeMillis();
        long elapsed = now - lastUsedTime;

        float progress = (float) elapsed / cooldown;
        progress = Math.min(progress, 1f);


        int overlayHeight = (int) (height * (1 - progress));

        g.setColor(new Color(0, 0, 0, 160));
        g.fillRect(x, y, width, overlayHeight);

        long timeLeft = cooldown - elapsed;
        int remaining = (int) Math.ceil(timeLeft / 1000.0);
        remaining = Math.max(0, remaining);

        if (remaining > 0) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 18));

            String txt = String.valueOf(remaining);
            FontMetrics fm = g.getFontMetrics();

            int tx = x + (width - fm.stringWidth(txt)) / 2;
            int ty = y + height / 2;

            g.drawString(txt, tx, ty);
        }
    }

    public void setMouseOver(boolean mouseOver) {
        if (type == ButtonType.SKILL && onCooldown) return;
        this.mouseOver = mouseOver;
    }

    public void setMousePressed(boolean mousePressed) {
        if (type == ButtonType.SKILL && onCooldown) return;
        this.mousePressed = mousePressed;
    }

    public boolean isMousePressed() {
        return mousePressed;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void resetBooleans() {
        mouseOver = false;
        mousePressed = false;
    }

    public void setIcons(BufferedImage newWallIcon, BufferedImage newWallIcon2, BufferedImage newWallIcon3) {
        this.pressedImage = newWallIcon;
        this.normalImage = newWallIcon2;
        this.overImage = newWallIcon3;
    }
}
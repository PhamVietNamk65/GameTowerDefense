package ui;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

public class ButtonBar {

    private int x, y, width, height;
    private Rectangle bounds;
    public boolean visible = true;
    public List<MyButton> buttons;
    private int orientation = 0; // 0: Ưu tiên ngang (Xuống dòng), 1: Ưu tiên dọc (Sang cột)
    private int gap;

    public ButtonBar(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.buttons = new ArrayList<>();
        initBounds();
    }

    public void setOrientation(int orientation, int gap) {
        this.orientation = orientation;
        this.gap = gap;
    }

    public void draw(Graphics g) {
        if (!visible) return;
        drawButtons(g);
    }

    public void drawButtons(Graphics g) {
        if (buttons.isEmpty()) return;

        int currentX = x;
        int currentY = y;
        int maxStepInLine = 0; 

        for (MyButton button : buttons) {
            int bW = button.getBounds().width;
            int bH = button.getBounds().height;

            if (orientation == 0) { // CHẾ ĐỘ HÀNG NGANG
                // 1. Kiểm tra: Nếu đặt nút này vào mà vượt quá chiều rộng của Bar
                // VÀ đây không phải là nút đầu tiên của hàng (tránh lặp vô hạn)
                if (currentX + bW > x + width && currentX > x) {
                    currentX = x;                    // Quay về lề trái
                    currentY += maxStepInLine + gap; // Xuống dòng
                    maxStepInLine = 0;               // Reset chiều cao hàng mới
                }
            
                button.setButton(currentX, currentY, bW, bH);
            
                // Cập nhật tọa độ cho nút KẾ TIẾP
                currentX += bW + gap;
                // Lưu lại nút cao nhất trong hàng hiện tại
                maxStepInLine = Math.max(maxStepInLine, bH);

            } else { // CHẾ ĐỘ HÀNG DỌC
                // Kiểm tra nếu vượt quá chiều cao
                if (currentY + bH > y + height && currentY > y) {
                    currentY = y;                    // Quay về đỉnh
                    currentX += maxStepInLine + gap; // Sang cột mới
                    maxStepInLine = 0;
                }

                button.setButton(currentX, currentY, bW, bH);
            
                currentY += bH + gap;
                maxStepInLine = Math.max(maxStepInLine, bW);
            }
        
            button.draw(g);
        }
    }
    

    private void initBounds() {
        this.bounds = new Rectangle(x, y, width, height);
    }

    public Rectangle getBounds() { return bounds; }

    public void addButton(MyButton button) {
        buttons.add(button);
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
        this.bounds.setLocation(x, y);
    }
}
// DrawableBox.java
import javax.swing.*;
import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

class DrawableBox implements Serializable {
    static final int BOX_SIZE = 50;
    private int x, y;
    private String name;
    private List<Decorator> decorators;
    private static int boxCounter = 1;
    private static final long serialVersionUID = 1L;

    public DrawableBox(int x, int y) {
        this.x = x;
        this.y = y;
        this.name = String.format("Class%02d", boxCounter++);
        this.decorators = new ArrayList<>();
    }


    public Decorator getDecoratorAt(int clickX, int clickY) {
        // Calculate relative click position
        int relX = clickX - x;
        int relY = clickY - y;

        // Check each decorator
        int decoratorStartX = BOX_SIZE + 10; // starting position for decorators
        int decoratorY = (BOX_SIZE - Decorator.CIRCLE_SIZE) / 2;

        for (int i = 0; i < decorators.size(); i++) {
            Decorator d = decorators.get(i);
            int decoratorX = decoratorStartX + (i * (Decorator.CIRCLE_SIZE + 5));

            // Check if click is within decorator circle
            if (relX >= decoratorX && relX <= decoratorX + Decorator.CIRCLE_SIZE &&
                    relY >= decoratorY && relY <= decoratorY + Decorator.CIRCLE_SIZE) {
                return d;
            }
        }
        return null;
    }


    public void draw(Graphics g) {
        // Draw the main box
        g.setColor(Color.DARK_GRAY);
        g.fillRect(x, y, BOX_SIZE, BOX_SIZE);
        g.setColor(Color.BLACK);
        g.drawRect(x, y, BOX_SIZE, BOX_SIZE);

        // Draw name in white
        g.setColor(Color.WHITE);
        FontMetrics fm = g.getFontMetrics();
        int nameX = x + (BOX_SIZE - fm.stringWidth(name)) / 2;
        int nameY = y + (BOX_SIZE + fm.getAscent()) / 2;
        g.drawString(name, nameX, nameY);

        // Draw decorators to the right of the box
        int decoratorStartX = x + BOX_SIZE + 10;
        int decoratorY = y + (BOX_SIZE - Decorator.CIRCLE_SIZE) / 2;

        for (int i = 0; i < decorators.size(); i++) {
            decorators.get(i).draw(g, decoratorStartX + (i * (Decorator.CIRCLE_SIZE + 5)), decoratorY);
        }
    }


    public void addDecorator(String decoratorType) {
        decorators.add(new Decorator(decoratorType));
    }

    public boolean contains(int clickX, int clickY) {
        return clickX >= x && clickX <= x + BOX_SIZE &&
                clickY >= y && clickY <= y + BOX_SIZE;
    }

    public void move(int newX, int newY) {
        this.x = newX;
        this.y = newY;
    }

    public void changeName(String newName) {
        if (newName != null && !newName.trim().isEmpty()) {
            this.name = newName.trim();
        }
    }

    public String getName() {
        return name;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public List<Decorator> getDecorators() {
        return decorators;
    }
}
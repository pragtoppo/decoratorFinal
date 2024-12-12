// Decorator.java
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
class Decorator {
    private final String type;
    private final Color color;
    private int x, y;
    static final int CIRCLE_SIZE = 30;
    private String label;
    private static final long serialVersionUID = 1L;

    private List<Decorator> connectedDecorators = new ArrayList<>();

    public void addConnectedDecorator(Decorator decorator) {
        connectedDecorators.add(decorator);
    }
    public Decorator(String type) {
        this.type = type;
        this.color = new Color(255, 198, 92); // Orange color
        this.label = getDefaultLabel(type);
    }

    private String getDefaultLabel(String type) {
        switch (type.toLowerCase()) {
            case "observer": return "Ob";
            case "observable": return "Os";
            case "product": return "P";
            case "factory": return "F";
            case "strategy": return "St";
            case "chain member": return "Ch";
            case "decorator": return "D";
            case "singleton": return "Sg";
            case "decoration": return "Dc";
            case "decoratable": return "Dt";
            default: return type.substring(0, Math.min(2, type.length()));
        }
    }


    public void draw(Graphics g, int xPos, int yPos) {
        this.x = xPos;
        this.y = yPos;

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw circle
        g2d.setColor(color);
        g2d.fillOval(x, y, CIRCLE_SIZE, CIRCLE_SIZE);

        // Draw border
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(1));
        g2d.drawOval(x, y, CIRCLE_SIZE, CIRCLE_SIZE);

        // Draw label
        g2d.setColor(Color.BLACK);
        FontMetrics fm = g2d.getFontMetrics();
        int labelX = x + (CIRCLE_SIZE - fm.stringWidth(label)) / 2;
        int labelY = y + ((CIRCLE_SIZE + fm.getAscent()) / 2);
        g2d.drawString(label, labelX, labelY);

        for (Decorator connectedDecorator : connectedDecorators) {
            int x1 = getX();
            int y1 = getY();
            int x2 = connectedDecorator.getX();
            int y2 = connectedDecorator.getY();
            g.setColor(Color.GRAY);
            ((Graphics2D) g).setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{10}, 0));
            g.drawLine(x1, y1, x2, y2);
        }
    }


    public boolean contains(int checkX, int checkY) {
        // Adjust the check to account for the box's position
        int relativeX = checkX - x;
        int relativeY = checkY - y;

        // Check if the point is within the circle
        double centerX = CIRCLE_SIZE / 2.0;
        double centerY = CIRCLE_SIZE / 2.0;
        double distance = Math.sqrt(
                Math.pow(relativeX - centerX, 2) +
                        Math.pow(relativeY - centerY, 2)
        );
        return distance <= CIRCLE_SIZE / 2.0;
    }

    public int getX() {
        return x + CIRCLE_SIZE / 2;
    }

    public int getY() {
        return y + CIRCLE_SIZE / 2;
    }

    public String getType() {
        return type;
    }
}

import java.awt.*;
import java.io.Serializable;

class Connection implements Serializable {
    private final Object source;
    private final Object target;
    private static final int ARROW_SIZE = 10;
    private static final long serialVersionUID = 1L;

    private boolean isDecoratorConnection;

    public Connection(Object source, Object target, boolean isDecoratorConnection) {
        this.source = source;
        this.target = target;
        this.isDecoratorConnection = isDecoratorConnection;
    }

    // Add getters
    public Object getSource() {
        return source;
    }

    public Object getTarget() {
        return target;
    }



    public void draw(Graphics g) {
        Point start = getConnectionPoint(source);
        Point end = getConnectionPoint(target);
        if (start == null || end == null) return;

        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(2));

        if (isDecoratorConnection) {
            g2d.setColor(Color.GRAY);
            g2d.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{10}, 0));
            g2d.drawLine(start.x, start.y, end.x, end.y);
        } else {
            g2d.setColor(Color.BLACK);
            g2d.drawLine(start.x, start.y, end.x, end.y);


        }
    }

    private Point getConnectionPoint(Object obj) {
        if (obj instanceof DrawableBox) {
            DrawableBox box = (DrawableBox) obj;
            return new Point(
                    box.getX() + DrawableBox.BOX_SIZE / 2,
                    box.getY() + DrawableBox.BOX_SIZE / 2
            );
        } else if (obj instanceof Decorator) {
            Decorator dec = (Decorator) obj;
            return new Point(
                    dec.getX() + Decorator.CIRCLE_SIZE / 2,
                    dec.getY() + Decorator.CIRCLE_SIZE / 2
            );
        }
        return null;
    }

    public boolean containsBox(DrawableBox box) {
        return source == box || target == box;
    }

    public void updateEndpoints(DrawableBox movedBox) {
        // Since we're using object references, we don't need to update anything
        // The connection points will automatically update when the box moves
        // because we calculate them dynamically in getConnectionPoint
    }

}

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

class Board implements Serializable {
	private List<DrawableBox> boxes;
	private List<Connection> connections;
	private static final long serialVersionUID = 1L;

	public Board() {
		this.boxes = new ArrayList<>();
		this.connections = new ArrayList<>();
	}

	public void addBox(int x, int y) {
		boxes.add(new DrawableBox(x, y));
	}

	public DrawableBox getBoxAt(int x, int y) {
		for (DrawableBox box : boxes) {
			if (box.contains(x, y)) {
				return box;
			}
		}
		return null;
	}

	public void handleClick(int x, int y) {
		addBox(x - DrawableBox.BOX_SIZE / 2,
				y - DrawableBox.BOX_SIZE / 2);
	}

	public void addConnection(Object source, Object target, boolean isDecoratorConnection) {
		connections.add(new Connection(source, target, isDecoratorConnection));
	}

	public void updateConnections(DrawableBox movedBox) {
		for (Connection conn : connections) {
			if (conn.containsBox(movedBox)) {
				conn.updateEndpoints(movedBox);
			}
		}
	}

	public List<DrawableBox> getBoxes() {
		return boxes;
	}

	public List<Connection> getConnections() {
		return connections;
	}
}
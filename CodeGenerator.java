import java.util.*;

// CodeGenerator.java
class CodeGenerator {
    private final Board board;

    public CodeGenerator(Board board) {
        this.board = board;
    }

    public String generateCode(DrawableBox box) {
        StringBuilder code = new StringBuilder();

        // Add imports
        code.append("import java.util.*;\n");
        code.append("import java.beans.*;\n\n");

        // Class declaration
        code.append("public class ").append(box.getName());

        // Handle inheritance and implementations
        List<Connection> connections = getConnectionsForBox(box);
        Set<String> inheritedClasses = new HashSet<>();
        Set<String> implementedInterfaces = new HashSet<>();

        // Process connections
        for (Connection conn : connections) {
            if (conn.getTarget() instanceof DrawableBox) {
                DrawableBox targetBox = (DrawableBox) conn.getTarget();
                inheritedClasses.add(targetBox.getName());
            }
        }

        // Process decorators for interface implementations
        for (Decorator decorator : box.getDecorators()) {
            switch (decorator.getType().toLowerCase()) {
                case "observer":
                    implementedInterfaces.add("PropertyChangeListener");
                    break;
                case "strategy":
                    implementedInterfaces.add("Strategy");
                    break;
                case "chain member":
                    implementedInterfaces.add("ChainHandler");
                    break;
            }
        }

        // Add extends if there are inherited classes
        if (!inheritedClasses.isEmpty()) {
            code.append(" extends ").append(inheritedClasses.iterator().next());
        }

        // Add implements if there are interfaces
        if (!implementedInterfaces.isEmpty()) {
            code.append(" implements ").append(String.join(", ", implementedInterfaces));
        }

        code.append(" {\n");

        // Fields for decorators
        if (hasDecorator(box, "observable")) {
            code.append("    private PropertyChangeSupport support = new PropertyChangeSupport(this);\n");
        }
        if (hasDecorator(box, "singleton")) {
            code.append("    private static ").append(box.getName()).append(" instance;\n");
        }
        if (hasDecorator(box, "factory")) {
            code.append("    protected Product product;\n");
        }
        if (hasDecorator(box, "chain member")) {
            code.append("    private ChainHandler nextHandler;\n");
        }
        if (hasDecorator(box, "strategy")) {
            code.append("    private Strategy strategy;\n");
        }
        if (hasDecorator(box, "decorator")) {
            code.append("    private Component component;\n");
        }

        // Constructor
        code.append("\n    public ").append(box.getName()).append("() {\n");
        if (hasDecorator(box, "observable")) {
            code.append("        support = new PropertyChangeSupport(this);\n");
        }
        code.append("    }\n\n");

        // Generate methods for each decorator
        for (Decorator decorator : box.getDecorators()) {
            switch (decorator.getType().toLowerCase()) {
                case "observer":
                    code.append("    @Override\n");
                    code.append("    public void propertyChange(PropertyChangeEvent evt) {\n");
                    code.append("        // Handle property change\n");
                    code.append("    }\n\n");
                    break;

                case "observable":
                    code.append("    public void addListener(PropertyChangeListener listener) {\n");
                    code.append("        support.addPropertyChangeListener(listener);\n");
                    code.append("    }\n\n");
                    code.append("    public void removeListener(PropertyChangeListener listener) {\n");
                    code.append("        support.removePropertyChangeListener(listener);\n");
                    code.append("    }\n\n");
                    code.append("    protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {\n");
                    code.append("        support.firePropertyChange(propertyName, oldValue, newValue);\n");
                    code.append("    }\n\n");
                    break;

                case "singleton":
                    code.append("    public static ").append(box.getName()).append(" getInstance() {\n");
                    code.append("        if (instance == null) {\n");
                    code.append("            instance = new ").append(box.getName()).append("();\n");
                    code.append("        }\n");
                    code.append("        return instance;\n");
                    code.append("    }\n\n");
                    break;

                case "factory":
                    code.append("    public Product createProduct() {\n");
                    code.append("        // Create product\n");
                    code.append("        return product;\n");
                    code.append("    }\n\n");
                    break;

                case "chain member":
                    code.append("    public void setNext(ChainHandler handler) {\n");
                    code.append("        this.nextHandler = handler;\n");
                    code.append("    }\n\n");
                    code.append("    public void handleRequest(String request) {\n");
                    code.append("        if (nextHandler != null) {\n");
                    code.append("            nextHandler.handleRequest(request);\n");
                    code.append("        }\n");
                    code.append("    }\n\n");
                    break;

                case "strategy":
                    code.append("    public void setStrategy(Strategy strategy) {\n");
                    code.append("        this.strategy = strategy;\n");
                    code.append("    }\n\n");
                    code.append("    public void executeStrategy() {\n");
                    code.append("        if (strategy != null) {\n");
                    code.append("            strategy.execute();\n");
                    code.append("        }\n");
                    code.append("    }\n\n");
                    break;

                case "decorator":
                    code.append("    public void operation() {\n");
                    code.append("        if (component != null) {\n");
                    code.append("            component.operation();\n");
                    code.append("        }\n");
                    code.append("        // Add decorator behavior\n");
                    code.append("    }\n\n");
                    break;
            }
        }

        code.append("}\n");
        return code.toString();
    }

    private boolean hasDecorator(DrawableBox box, String type) {
        return box.getDecorators().stream()
                .anyMatch(d -> d.getType().equalsIgnoreCase(type));
    }

    private List<Connection> getConnectionsForBox(DrawableBox box) {
        List<Connection> connections = new ArrayList<>();
        for (Connection conn : board.getConnections()) {
            if (conn.getSource() == box) {
                connections.add(conn);
            }
        }
        return connections;
    }

    public Map<String, String> generateProjectCode() {
        Map<String, String> files = new HashMap<>();

        // Generate only box classes
        for (DrawableBox box : board.getBoxes()) {
            String fileName = box.getName() + ".java";
            String fileContent = generateCode(box);
            files.put(fileName, fileContent);
        }

        return files;
    }
}
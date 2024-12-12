import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Map;

class BoardPanel extends JPanel {
    private Board board;
    private DrawableBox draggedBox = null;
    private Object selectedFirstObject = null;
    private int dragOffsetX, dragOffsetY;
    private boolean connectionMode = false;
    private String currentConnectorType = "Association";
    private CodeViewer codeViewer;

    public BoardPanel(Board board) {
        this.board = board;
        setPreferredSize(new Dimension(800, 600));
        setupMouseListeners();
    }

    public void setCodeViewer(CodeViewer viewer) {
        this.codeViewer = viewer;
    }

    private void setupMouseListeners() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Object clickedObject = getClickedObject(e.getX(), e.getY());
                if (SwingUtilities.isRightMouseButton(e)) {
                    handleRightClick(e, clickedObject);
                } else if (SwingUtilities.isLeftMouseButton(e)) {
                    handleLeftClick(e, clickedObject);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (draggedBox != null) {
                    board.updateConnections(draggedBox);
                    draggedBox = null;
                }
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (draggedBox != null) {
                    draggedBox.move(e.getX() - dragOffsetX, e.getY() - dragOffsetY);
                    board.updateConnections(draggedBox);
                    repaint();
                }
            }
        });
    }

    private Object getClickedObject(int clickX, int clickY) {
        DrawableBox box = board.getBoxAt(clickX, clickY);
        if (box != null) {
            // Check decorators first
            for (Decorator decorator : box.getDecorators()) {
                int decoratorScreenX = clickX - (box.getX() + DrawableBox.BOX_SIZE + 10);
                int decoratorScreenY = clickY - (box.getY() + (DrawableBox.BOX_SIZE - Decorator.CIRCLE_SIZE) / 2);
                if (decorator.contains(decoratorScreenX, decoratorScreenY)) {
                    return decorator;
                }
            }
            return box;
        }
        return null;
    }

    public void setConnectorMode(String connectorType) {
        this.currentConnectorType = connectorType;
        this.connectionMode = true;
        this.selectedFirstObject = null;
    }

    private void handleLeftClick(MouseEvent e, Object clickedObject) {
        if (connectionMode && clickedObject != null) {
            if (selectedFirstObject == null) {
                selectedFirstObject = clickedObject;
            } else {
                if (selectedFirstObject instanceof Decorator && clickedObject instanceof Decorator) {
                    // Connect the two decorators
                    Decorator firstDecorator = (Decorator) selectedFirstObject;
                    Decorator secondDecorator = (Decorator) clickedObject;
                    firstDecorator.addConnectedDecorator(secondDecorator);
                    secondDecorator.addConnectedDecorator(firstDecorator);
                    repaint();
                } else if (selectedFirstObject instanceof DrawableBox && clickedObject instanceof DrawableBox) {
                    // Connect the two boxes as before
                    board.addConnection(selectedFirstObject, clickedObject, false);
                    repaint();
                }
                selectedFirstObject = null;
                connectionMode = false;
            }
            return;
        }

        if (clickedObject instanceof DrawableBox) {
            draggedBox = (DrawableBox) clickedObject;
            dragOffsetX = e.getX() - draggedBox.getX();
            dragOffsetY = e.getY() - draggedBox.getY();
        } else if (clickedObject == null) {
            board.handleClick(e.getX(), e.getY());
            repaint();
        }
    }


    private void handleRightClick(MouseEvent e, Object clickedObject) {
        if (clickedObject instanceof DrawableBox) {
            DrawableBox box = (DrawableBox) clickedObject;
            JPopupMenu popupMenu = new JPopupMenu();

            // Rename option
            JMenuItem renameItem = new JMenuItem("Rename");
            renameItem.addActionListener(ev -> {
                String newName = JOptionPane.showInputDialog(
                        this,
                        "Enter new name for the class:",
                        box.getName()
                );
                if (newName != null && !newName.trim().isEmpty()) {
                    box.changeName(newName.trim());
                    updateCodeGeneration();
                    repaint();
                }
            });
            popupMenu.add(renameItem);
            popupMenu.addSeparator();

            // Decorator options
            String[] decoratorTypes = {
                    "Observer", "Observable", "Singleton", "Decoration",
                    "Decorator", "Chain Member", "Strategy", "Factory", "Product"
            };

            for (String decoratorType : decoratorTypes) {
                JMenuItem item = new JMenuItem("Add " + decoratorType);
                item.addActionListener(ev -> {
                    box.addDecorator(decoratorType);
                    updateCodeGeneration();
                    repaint();
                });
                popupMenu.add(item);
            }

            popupMenu.show(this, e.getX(), e.getY());
        }
    }

    private void updateCodeGeneration() {
        if (codeViewer != null) {
            CodeGenerator generator = new CodeGenerator(board);
            Map<String, String> generatedFiles = generator.generateProjectCode();
            codeViewer.updateGeneratedCode(generatedFiles);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (Connection conn : board.getConnections()) {
            conn.draw(g);
        }
        for (DrawableBox box : board.getBoxes()) {
            box.draw(g);
        }
    }

    public void setBoard(Board board) {
        this.board = board;
        repaint();
    }
}
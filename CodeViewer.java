import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.util.Map;

class CodeViewer extends JPanel {
    private JTree fileTree;
    private JTextArea codeArea;
    private Map<String, String> generatedFiles;
    private DefaultMutableTreeNode root;

    public CodeViewer() {
        setLayout(new BorderLayout());

        // Create the split pane
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

        // Create file tree
        root = new DefaultMutableTreeNode("src");
        fileTree = new JTree(root);
        fileTree.setRootVisible(true);
        JScrollPane treeScroll = new JScrollPane(fileTree);
        treeScroll.setPreferredSize(new Dimension(200, 0));

        // Create code area
        codeArea = new JTextArea();
        codeArea.setEditable(false);
        codeArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        codeArea.setBackground(new Color(45, 45, 45));
        codeArea.setForeground(Color.WHITE);
        JScrollPane codeScroll = new JScrollPane(codeArea);

        // Add components to split pane
        splitPane.setLeftComponent(treeScroll);
        splitPane.setRightComponent(codeScroll);

        // Add split pane to panel
        add(splitPane, BorderLayout.CENTER);

        // Add tree selection listener
        fileTree.addTreeSelectionListener(e -> {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode)
                    fileTree.getLastSelectedPathComponent();

            if (node == null || node == root) return;

            String fileName = node.toString();
            if (generatedFiles != null && generatedFiles.containsKey(fileName)) {
                codeArea.setText(generatedFiles.get(fileName));
                codeArea.setCaretPosition(0);
            }
        });
    }

    public void updateGeneratedCode(Map<String, String> files) {
        this.generatedFiles = files;

        // Clear existing nodes
        root.removeAllChildren();

        // Add file nodes
        for (String fileName : files.keySet()) {
            root.add(new DefaultMutableTreeNode(fileName));
        }

        // Refresh tree
        ((DefaultTreeModel)fileTree.getModel()).reload();

        // Expand root
        fileTree.expandRow(0);
    }
}

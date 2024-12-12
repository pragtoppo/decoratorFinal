import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Main extends JFrame {
	private Board board;
	private BoardPanel boardPanel;
	private JTabbedPane tabbedPane;
	private JTextArea codeArea;
	private CodeViewer codeViewer;


	public Main() {
		setTitle("Design Pattern Visualizer");
		setSize(1000, 800);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Initialize board and panel
		board = new Board();
		boardPanel = new BoardPanel(board);

		// Create tabbed pane
		tabbedPane = new JTabbedPane();

		// Initialize code viewer
		codeViewer = new CodeViewer();
		boardPanel.setCodeViewer(codeViewer);  // Add this setter to BoardPanel

		tabbedPane.addTab("Draw Area", boardPanel);
		tabbedPane.addTab("Code", codeViewer);

		add(tabbedPane);
		createMenuBar();
	}

	private void createMenuBar() {
		JMenuBar menuBar = new JMenuBar();

		// File Menu
		JMenu fileMenu = new JMenu("File");
		JMenuItem newItem = new JMenuItem("New");
		JMenuItem openItem = new JMenuItem("Open...");
		JMenuItem saveItem = new JMenuItem("Save");
		JMenuItem saveAsItem = new JMenuItem("Save As...");
		JMenuItem exitItem = new JMenuItem("Exit");

		newItem.addActionListener(e -> createNewDiagram());
		openItem.addActionListener(e -> loadDiagram());
		saveItem.addActionListener(e -> saveDiagram());
		saveAsItem.addActionListener(e -> saveAsDiagram());
		exitItem.addActionListener(e -> System.exit(0));

		fileMenu.add(newItem);
		fileMenu.add(openItem);
		fileMenu.add(saveItem);
		fileMenu.add(saveAsItem);
		fileMenu.addSeparator();
		fileMenu.add(exitItem);

		// Connector Menu
		JMenu connectorMenu = new JMenu("Connectors");
		String[] connectorTypes = {
				"Association",
				"Aggregation",
				"Composition",
				"Inheritance",
				"Realization",
				"Dependency"
		};

		for (String connectorType : connectorTypes) {
			JMenuItem connectorItem = new JMenuItem(connectorType);
			connectorItem.addActionListener(e -> {
				boardPanel.setConnectorMode(connectorType);
				JOptionPane.showMessageDialog(this,
						"Click on first box, then second box to create " + connectorType + " connection.");
			});
			connectorMenu.add(connectorItem);
		}
		JMenu toolsMenu = new JMenu("Tools");
		JMenuItem generateCodeItem = new JMenuItem("Generate Code");
		generateCodeItem.addActionListener(e -> {
			CodeGenerator generator = new CodeGenerator(board);
			Map<String, String> generatedFiles = generator.generateProjectCode();
			codeViewer.updateGeneratedCode(generatedFiles);  // Use stored reference
			tabbedPane.setSelectedComponent(codeViewer);  // Switch to code tab
		});

		toolsMenu.add(generateCodeItem);
		menuBar.add(toolsMenu);

		menuBar.add(fileMenu);
		menuBar.add(connectorMenu);
		setJMenuBar(menuBar);
	}

	private void createNewDiagram() {
		int confirm = JOptionPane.showConfirmDialog(this,
				"Are you sure you want to create a new diagram? Unsaved changes will be lost.",
				"New Diagram",
				JOptionPane.YES_NO_OPTION);

		if (confirm == JOptionPane.YES_OPTION) {
			board = new Board();
			boardPanel.setBoard(board);  // Add this method to BoardPanel
			codeViewer.updateGeneratedCode(new HashMap<>());
			repaint();
		}
	}

	private void loadDiagram() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
			public boolean accept(File f) {
				return f.isDirectory() || f.getName().toLowerCase().endsWith(".dpv");
			}
			public String getDescription() {
				return "Design Pattern Visualizer Files (*.dpv)";
			}
		});

		int result = fileChooser.showOpenDialog(this);
		if (result == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(selectedFile))) {
				board = (Board) ois.readObject();
				boardPanel.setBoard(board);
				repaint();
				JOptionPane.showMessageDialog(this, "Diagram loaded successfully!");
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(this,
						"Error loading diagram: " + ex.getMessage(),
						"Error",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private File currentFile = null;

	private void saveDiagram() {
		if (currentFile == null) {
			saveAsDiagram();
		} else {
			saveToFile(currentFile);
		}
	}

	private void saveAsDiagram() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
			public boolean accept(File f) {
				return f.isDirectory() || f.getName().toLowerCase().endsWith(".dpv");
			}
			public String getDescription() {
				return "Design Pattern Visualizer Files (*.dpv)";
			}
		});

		int result = fileChooser.showSaveDialog(this);
		if (result == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			if (!selectedFile.getName().toLowerCase().endsWith(".dpv")) {
				selectedFile = new File(selectedFile.getAbsolutePath() + ".dpv");
			}
			currentFile = selectedFile;
			saveToFile(selectedFile);
		}
	}

	private void saveToFile(File file) {
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
			oos.writeObject(board);
			JOptionPane.showMessageDialog(this, "Diagram saved successfully!");
		} catch (IOException ex) {
			JOptionPane.showMessageDialog(this,
					"Error saving diagram: " + ex.getMessage(),
					"Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			Main frame = new Main();
			frame.setLocationRelativeTo(null);
			frame.setVisible(true);
		});
	}
}

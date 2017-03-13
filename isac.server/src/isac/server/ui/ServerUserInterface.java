package isac.server.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

/**
 * Simple server user interface.
 * 
 * @author acco
 *
 */
public class ServerUserInterface extends JFrame {

	private static final long serialVersionUID = 1L;

	public ServerUserInterface() {

		this.setTitle("Server-side - Virtual car parking");
		this.setSize(new Dimension(800, 600));

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.setLayout(new BorderLayout());

		JTabbedPane tabbedPane = new JTabbedPane();

		this.add(tabbedPane, BorderLayout.CENTER);

		ParkViewer parkViewer = new ParkViewer();
		tabbedPane.addTab("Park status", parkViewer);

		GraphViewer graphViewer = new GraphViewer();
		tabbedPane.add("Graph", graphViewer);

		StatusBar statusbar = new StatusBar();
		this.add(statusbar, BorderLayout.SOUTH);

		this.setVisible(true);

	}

}

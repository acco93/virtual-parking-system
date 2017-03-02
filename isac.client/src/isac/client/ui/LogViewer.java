package isac.client.ui;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import isac.core.log.ILogger;

public class LogViewer extends JPanel implements ILogger {

	private static final long serialVersionUID = 1L;
	private StyledDocument doc;
	private Style errStyle;
	private Style infoStyle;

	public LogViewer() {

		this.setLayout(new BorderLayout());

		JTextPane log = new JTextPane();
		log.setEditable(false);
		log.setOpaque(false);
		doc = log.getStyledDocument();		
		
		DefaultCaret caret = (DefaultCaret) log.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

		errStyle = log.addStyle("Error style", null);
		StyleConstants.setForeground(errStyle, Color.red);
		StyleConstants.setBold(errStyle, true);

		infoStyle = log.addStyle("Info style", null);
		StyleConstants.setForeground(infoStyle, Color.black);

		JScrollPane scroll = new JScrollPane(log);
		scroll.setOpaque(false);
		scroll.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		this.add(scroll, BorderLayout.CENTER);

	}

	private void append(String text, Style style) {
		try {
			doc.insertString(doc.getLength(), text + "\n", style);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void info(String text) {
		SwingUtilities.invokeLater(() -> {
			this.append(text, this.infoStyle);
		});
	}

	@Override
	public void error(String text) {
		SwingUtilities.invokeLater(() -> {
			this.append(text, this.errStyle);
		});
	}

}

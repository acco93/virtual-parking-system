package acco.isac.server.ui;

import java.awt.event.KeyEvent;

import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JRadioButtonMenuItem;

public class MenuBar extends JMenuBar {

	private static final long serialVersionUID = 1L;

	public MenuBar() {
		JMenu menu = new JMenu("Server");
		this.add(menu);
		
		ButtonGroup group = new ButtonGroup();
		JRadioButtonMenuItem enabled = new JRadioButtonMenuItem("Enable");
		JRadioButtonMenuItem disabled = new JRadioButtonMenuItem("Disable");
		enabled.setSelected(true);

		group.add(enabled);
		group.add(disabled);
		menu.add(enabled);
		menu.add(disabled);
		
	}

}

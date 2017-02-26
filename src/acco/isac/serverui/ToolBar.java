package acco.isac.serverui;

import javax.swing.JButton;
import javax.swing.JToolBar;

public class ToolBar extends JToolBar{

	private static final long serialVersionUID = 1L;

	
	public ToolBar(){

		this.setFloatable(false);
		
		JButton showPark = new JButton("Show park");
		JButton showGraph = new JButton("Show graph");
		
		this.add(showPark);
		this.add(showGraph);
	}
	
}

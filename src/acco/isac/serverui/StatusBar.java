package acco.isac.serverui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

public class StatusBar extends JPanel {

	private static final long serialVersionUID = 1L;

	public StatusBar() {

		JLabel currentTime = new JLabel("Loading ...");

		Timer time = new javax.swing.Timer(1000, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				java.util.Date now = new java.util.Date();
				String ss = DateFormat.getDateTimeInstance().format(now);
				currentTime.setText(ss);

			}
		});
		time.start();

		this.add(currentTime);

	}

}

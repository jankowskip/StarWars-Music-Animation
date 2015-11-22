import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.Year;

import javax.imageio.ImageIO;
import javax.sound.midi.*;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Player {
	static JFrame ramka = new JFrame("Music video");
	static PanelGraficzny panel;
	static BufferedImage img;
	static BufferedImage img2;
	static BufferedImage img3;
	static BufferedImage img4;
	Sequencer sekwenser;
	boolean doesItPlay ;

	static final int[] NOTES = { 45, 45, 45, 41, 48, 45, 41, 48, 45, 52, 52,
			52, 53, 48, 45, 41, 48 };// ,45 };
	static final int[] VELOCITY_NOTE_ON = { 1, 19, 37, 55, 71, 73, 91, 107,
			109, 145, 163, 181, 199, 215, 217, 235, 251 };// , 253 };
	static final int[] VELOCITY_NOTE_OFF = { 17, 35, 53, 71, 73, 89, 107, 109,
			125, 161, 179, 197, 215, 217, 233, 251, 253 };// , 269 };
	static int x = 0;

	public static void main(String[] args) {
		Player jo = new Player();
		jo.GuiKofngiruacja();

	}

	public void GuiKofngiruacja() {

		JButton b = null;
		JButton b2 = null;
		ramka.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		panel = new PanelGraficzny();
		JLabel start = new JLabel("   Start");
		JLabel stop = new JLabel("   Stop ");
		JPanel jpanel = new JPanel();
		jpanel.setBackground(Color.WHITE);
		jpanel.setLayout(new BoxLayout(jpanel, BoxLayout.Y_AXIS));
		BufferedImage buttonIcon;
		BufferedImage buttonIcon2;
		try {
			buttonIcon = ImageIO.read(new File("darthvader.gif"));
			b = new JButton(new ImageIcon(buttonIcon));
			b.setBorder(BorderFactory.createEmptyBorder());
			b.setContentAreaFilled(false);
			b.addActionListener(new StartListener());

			buttonIcon2 = ImageIO.read(new File("stop.png"));
			b2 = new JButton(new ImageIcon(buttonIcon2));
			b2.setBorder(BorderFactory.createEmptyBorder());
			b2.setContentAreaFilled(false);
			b2.addActionListener(new StopListener());

			img = ImageIO.read(new File("bg.jpg"));
			img2 = ImageIO.read(new File("deathstar.png"));
			img3 = ImageIO.read(new File("xwing.png"));

		} catch (IOException e) {
			e.printStackTrace();
		}

		jpanel.add(b);
		jpanel.add(start);
		jpanel.add(b2);
		jpanel.add(stop);
		ramka.getContentPane().add(BorderLayout.EAST, jpanel);
		ramka.getContentPane().add(BorderLayout.CENTER, panel);
		ramka.setBounds(30, 30, 300, 300);
		ramka.setVisible(true);

	}

	public void start() {
		try {
			sekwenser = MidiSystem.getSequencer();
			sekwenser.open();
			int[] zdarzenieObslugiwane = { 127 };
			sekwenser.addControllerEventListener(panel, zdarzenieObslugiwane);
			Sequence sekw = new Sequence(Sequence.PPQ, 4);
			Track sciezka = sekw.createTrack();
			// AAA FCA FCA - EEE FCA FCA - AAA FCA FCA - EEE FCA FCA
			for (int i = 0; i < 17; i++, x++) {
				sciezka.add(createMidiEvent(144, 1, NOTES[x], 100,
						VELOCITY_NOTE_ON[x]));
				sciezka.add(createMidiEvent(176, 0, 127, 0, VELOCITY_NOTE_ON[x]));
				sciezka.add(createMidiEvent(128, 1, NOTES[x], 100,
						VELOCITY_NOTE_OFF[x]));

			}
			sekwenser.setSequence(sekw);
			sekwenser.setTempoInBPM(350); // 220
			sekwenser.setLoopCount(Sequencer.LOOP_CONTINUOUSLY);
			sekwenser.start();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static MidiEvent createMidiEvent(int plc, int kanal, int jeden,
			int dwa, int takt) {
		MidiEvent zdarzenie = null;
		try {
			ShortMessage a = new ShortMessage();
			a.setMessage(plc, kanal, jeden, dwa);
			zdarzenie = new MidiEvent(a, takt);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return zdarzenie;
	}

	public class PanelGraficzny extends JPanel implements
			ControllerEventListener {
		boolean komunikat = false;

		public void controlChange(ShortMessage event) {
			komunikat = true;
			repaint();
		}

		public void paintComponent(Graphics g) {
			if (komunikat) {
				super.paintComponent(g);
				int x = (int) (Math.random() * 180 + 10);
				int y = (int) (Math.random() * 180 + 10);
				double z = Math.random();
				if (z > 0.5) {
					g.drawImage(img, 0, 0, this);
					g.drawImage(img2, x, y, this);
				} else {
					g.drawImage(img, 0, 0, this);
					g.drawImage(img3, x, y, this);
				}

			}
		}
	}

	public class JPanelBackground extends JPanel {
		public void paintComponent(Graphics g) {
			g.drawImage(img4, 0, 0, this);
		}
	}

	public class StartListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (doesItPlay == false) {
				start();
				doesItPlay = true;
			}

		}

	}

	public class StopListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (doesItPlay) {
				sekwenser.stop();
				x = 0;
				doesItPlay = false;
			}

		}

	}

}
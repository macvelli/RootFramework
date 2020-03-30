

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;


public class Console extends JFrame implements KeyListener {

	private PipedInputStream piOut;
	private PipedInputStream piErr;
	private PipedOutputStream poOut;
	private PipedOutputStream poErr;
	private JTextArea textArea;


	public Console(final String name) {
		super(name);

		try {
			// Set up System.out
			piOut = new PipedInputStream();
			poOut = new PipedOutputStream(piOut);
			System.setOut(new PrintStream(poOut, true));

			// Set up System.err
			piErr = new PipedInputStream();
			poErr = new PipedOutputStream(piErr);
			System.setErr(new PrintStream(poErr, true));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		// Add a scrolling text area
		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setRows(25);
		textArea.setColumns(80);
		getContentPane().add(textArea, BorderLayout.CENTER);
		pack();

		// Position the JFrame in the center of the screen
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = new Dimension((int)(screenSize.width / 2), (int)(screenSize.height / 2));
		int x = (int)(frameSize.width / 2);
		int y = (int)(frameSize.height / 2);
		this.setBounds(x, y, frameSize.width, frameSize.height);

		setVisible(true);

		// Create reader threads
		new ReaderThread(piOut).start();
		new ReaderThread(piErr).start();
	}


	public void clear() {
		textArea.setText("");
	}


	public void keyPressed(KeyEvent e) {
	// TODO Auto-generated method stub

	}


	public void keyReleased(KeyEvent e) {
	// TODO Auto-generated method stub

	}


	public void keyTyped(KeyEvent e) {
	// TODO Auto-generated method stub

	}

	class ReaderThread extends Thread {

		private PipedInputStream pi;


		ReaderThread(PipedInputStream pi) {
			this.pi = pi;
		}


		public void run() {
			final byte[] buf = new byte[1024];

			try {
				while (true) {
					final int len = pi.read(buf);

					if (len == -1) {
						break;
					}

					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							textArea.append(new String(buf, 0, len));

							// Make sure the last line is always visible
							textArea.setCaretPosition(textArea.getDocument().getLength());
						}
					});
				}
			} catch (IOException e) {
			}
		}

	} // End ReaderThread

} // End Console
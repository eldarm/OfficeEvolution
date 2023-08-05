package OfficeEvolution;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class MainScreen extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7658783780868366268L;
	private JPanel paintPanel;
	private JPanel controlPanel;

	public MainScreen() {
		final int width = 800;
		final int height = 500;
		final int controlIndent = 10;
		final int controlWidth = 120;
		setTitle("Incremental Growth Model");
		setSize(width, height);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);  
	
       JPanel globalPanel = new JPanel(/*new FlowLayout()*/);
       globalPanel.setLayout(null);
       globalPanel.setSize(new Dimension(width, height));
       getContentPane().add(globalPanel);

       controlPanel = new JPanel(new FlowLayout());
       controlPanel.setSize(new Dimension(controlWidth, height));
       controlPanel.setBackground(Color.BLUE);
       globalPanel.add(controlPanel);
       //panel.setLayout(null);

       /*
       // Parameters:
       JLabel growthLabel = new JLabel("Growth per gen:");
       growthLabel.setForeground(Color.YELLOW);
       controlPanel.add(growthLabel);
       JTextField growthTextField = new JTextField("  0.05");
       controlPanel.add(growthTextField);
       */
       
       // Run button.
       JButton runButton = new JButton("Run");
       runButton.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent event) {
        	   RunExperiment();
          }
       });
       controlPanel.add(runButton);		
       
       // Quit button.
       JButton quitButton = new JButton("Quit");
       quitButton.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent event) {
               System.exit(0);
          }
       });
       controlPanel.add(quitButton);		
		
       paintPanel = new JPanel();
       final int paintIndent = controlPanel.getWidth() + controlIndent;
       paintPanel.setBounds(paintIndent, 10, width - paintIndent - controlIndent, height - 40);
       paintPanel.setLayout(null);
       paintPanel.setBackground(Color.lightGray);
       globalPanel.add(paintPanel);					

	}
	
	private void RunExperiment() {
		final int elementSize = 5;
		Graphics g;
	    g = paintPanel.getGraphics();
	    Dimension d = paintPanel.getSize();
	    g.clearRect(0, 0, d.width, d.height);
	    Generation gen = new GenerationCommon(d.width / elementSize, 50);
	    for (int i = 0; i < d.height / elementSize; i++) {
	    	gen.Draw(g, i * elementSize, elementSize);
	    	if ((i + 1) % 12 == 0) {
	    		g.setColor(Color.GRAY);
	    		g.drawLine(0, i * elementSize, d.width, i * elementSize);
	    		g.drawString(gen.GetStats().toString(), d.width - 100, i * elementSize - 2);
	    	}
	    	gen.Mutate();
	    }
	}

	public static void main(String[] args) {

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				MainScreen ms = new MainScreen();
				ms.setVisible(true);
			}
		});
	}
}

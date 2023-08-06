package OfficeEvolution;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
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
	private static final long serialVersionUID = 7658583780168366268L;
	private JPanel paintPanel;
	private JPanel controlPanel;
	private JTextField attritionTextField;
	private JTextField minDistanceTextField;
	private JTextField managementErrorsTextField;
	private JLabel hiringCostLabel;
	private JLabel devTargetLabel;
	private JLabel devMajorityLabel;

	private final String HIRING_COST_LABEL = "Hiring cost: %d₿";
	private final String DEV_TARGET_LABEL = "Deviation from target: %f";
	private final String DEV_MAJORITY_LABEL = "Deviation from majority: %f";

	public MainScreen() {
		final int width = 900;
		final int height = 500;
		final int controlIndent = 10;
		final int controlWidth = 220;
		setTitle("Office Evolution Model");
		setSize(width, height);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		JPanel globalPanel = new JPanel(/* new FlowLayout() */);
		globalPanel.setLayout(null);
		globalPanel.setSize(new Dimension(width, height));
		getContentPane().add(globalPanel);

		// controlPanel = new JPanel(new FlowLayout());
		//controlPanel = new JPanel(/* new FlowLayout() */);
		//controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
		controlPanel = new JPanel(new GridLayout(0,1));
		controlPanel.setSize(new Dimension(controlWidth, height));
		controlPanel.setBackground(Color.BLUE);
		globalPanel.add(controlPanel);

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

		// Parameters: attrition 0.0..1.0, 0.0 = None.
		JLabel attritionLabel = new JLabel("Attrition per gen\n (0.0..1.0):");
		attritionLabel.setForeground(Color.YELLOW);
		controlPanel.add(attritionLabel);
		attritionTextField = new JTextField("  0.00");
		controlPanel.add(attritionTextField);

		// Parameters: minDistance 0.0..1.0, 1.0 = None
		JLabel minDistanceLabel = new JLabel("Min distance\n(0.0..1.0):");
		minDistanceLabel.setForeground(Color.YELLOW);
		controlPanel.add(minDistanceLabel);
		minDistanceTextField = new JTextField("  1.00");
		controlPanel.add(minDistanceTextField);

		// Parameters: managementErrors 0.0..1.0
		JLabel managementErrorsLabel = new JLabel("Management errors (0.0..1.0):");
		managementErrorsLabel.setForeground(Color.YELLOW);
		controlPanel.add(managementErrorsLabel);
		managementErrorsTextField = new JTextField("  0.00");
		controlPanel.add(managementErrorsTextField);

		// Output: Hiring cost in tugrics.
		hiringCostLabel = new JLabel(String.format(HIRING_COST_LABEL, 0));
		hiringCostLabel.setForeground(Color.YELLOW);
		controlPanel.add(hiringCostLabel);

		// Output: deviation from the market target.
		devTargetLabel = new JLabel(String.format(DEV_TARGET_LABEL, 0.0));
		devTargetLabel.setForeground(Color.YELLOW);
		controlPanel.add(devTargetLabel);

		// Output: deviation from majority opinion.
		devMajorityLabel = new JLabel(String.format(DEV_MAJORITY_LABEL, 0.0));
		devMajorityLabel.setForeground(Color.YELLOW);
		controlPanel.add(devMajorityLabel);

		// Placeholder to fix Java problem with layouts.
		controlPanel.add(new JLabel("---"));

		// Main panel to draw the results.
		paintPanel = new JPanel();
		final int paintIndent = controlPanel.getWidth() + controlIndent;
		paintPanel.setBounds(paintIndent, 10, width - paintIndent - controlIndent, height - 40);
		paintPanel.setLayout(null);
		paintPanel.setBackground(Color.lightGray);
		globalPanel.add(paintPanel);
	}

	private void RunExperiment() {
		RGB target = new RGB();
		double attrition = Double.valueOf(attritionTextField.getText());
		double minDistance = Double.valueOf(minDistanceTextField.getText());
		double managementErrors = Double.valueOf(managementErrorsTextField.getText());
		int replaced = 0;

		Graphics g;
		g = paintPanel.getGraphics();
		Dimension d = paintPanel.getSize();
		g.clearRect(0, 0, d.width, d.height);

		int wDraw = d.width - 20;
		int targetOffset = wDraw + 2;
		int wTargetEnd = targetOffset + 18;
		Generation gen = new Generation(wDraw, target);

		for (int row = 0; row < d.height; row++) {
			//System.out.printf("%d\n", row);
			RGB[] points = gen.getPoints();
			int wPoints = points.length < wDraw ? points.length : wDraw;
			int pointsOffset = (wDraw - wPoints) / 2;
			for (int col = 0; col < wPoints; col++) {
				g.setColor(points[col].getColor());
				g.fillRect(col + pointsOffset, row, 1, 1);
			}
			g.setColor(target.getColor());
			for (int col = targetOffset; col < wTargetEnd; col++) {
				g.fillRect(col, row, 1, 1);
			}
			replaced += gen.lostThisGeneration;
			gen.nextGeneration(attrition, minDistance, managementErrors);
			//System.out.printf("Added new layer costs: %d\n", replaced);
			devTargetLabel.setText(String.format(DEV_TARGET_LABEL, gen.deviationFromTarget));
			devMajorityLabel.setText(String.format(DEV_MAJORITY_LABEL, gen.deviationFromMajority));
		}
		hiringCostLabel.setText(String.format(HIRING_COST_LABEL, replaced));
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

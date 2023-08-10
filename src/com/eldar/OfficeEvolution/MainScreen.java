package OfficeEvolution;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class MainScreen extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7658583780168366268L;
	private JPanel controlPanel;
	private Canvas paintPanel;
	// private Canvas paintPanel;
	private JTextField attritionTextField;
	private JTextField managementErrorsTextField;
	private JTextField mutationRateTextField;
	private JTextField changeTargetEveryTextField;
	private JTextField layoffVolumeTextField;
	private JTextField recoveryIncTextField;
	private JTextField marketPressureTextField;
	private JComboBox<String> presetList;
	private JComboBox<String> memberList;
	private JLabel hiringCostLabel;
	private JLabel devTargetLabel;
	private JLabel devMajorityLabel;
	private JPanel majorityToTargetPanel;
	private JLabel majorityToTargetLabel;

	private final static String HIRING_COST_LABEL = "Hiring cost: %d 🍄";
	private final static String DEV_TARGET_LABEL = "Deviation from target: %f";
	private final static String DEV_MAJORITY_LABEL = "Deviation from majority: %f";
	private final static String MAJORITY_TO_TARGET_LABEL = "Majority - target distance: %f";

	private final static Dimension minSize = new Dimension(0, 300); // ?

	private JTextField addParam(String text, String defValue,
			JTextField textField) {
		var panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		panel.setBackground(Color.BLUE);
		var label = new JLabel(text);
		label.setForeground(Color.YELLOW);
		panel.add(label);
		textField = new JTextField(defValue);
		textField.setHorizontalAlignment(JTextField.LEFT);
		textField.setMinimumSize(minSize); // ?
		panel.add(textField);
		controlPanel.add(panel);
		return textField;
	}

	public MainScreen() {
		final int width = 900;
		final int height = 500;
		final int controlIndent = 10;
		final int controlWidth = 220;
		setTitle("Office Evolution Model");
		setSize(width, height);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		JSplitPane globalPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		// JPanel globalPanel = new JPanel(/* new FlowLayout() */);
		globalPanel.setSize(new Dimension(width, height));
		getContentPane().add(globalPanel);

		{
			controlPanel = new JPanel(new GridLayout(0, 1));
			controlPanel.setLocation(1, 1);
			controlPanel.setSize(new Dimension(controlWidth, height));
			// controlPanel.setBounds(0, 0, controlWidth, height);
			controlPanel.setBackground(Color.BLUE);
			globalPanel.add(controlPanel);
		}

		// Run button.
		{
			var runButton = new JButton("Run");
			runButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent event) {
					RunExperiment();
				}
			});
			controlPanel.add(runButton);
		}

		// Quit button.
		{
			var quitButton = new JButton("Quit");
			quitButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent event) {
					System.exit(0);
				}
			});
			controlPanel.add(quitButton);
		}

		// Pick predefined configuration.
		{
			var presetPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
			presetPanel.setBackground(Color.BLUE);
			var presetLabel = new JLabel("Pick a preset company:");
			presetLabel.setForeground(Color.YELLOW);
			presetPanel.add(presetLabel);
			presetList = new JComboBox<String>(Generation.COMPANY_LABELS);
			presetList.setSelectedIndex(Generation.COMPANY_HEAVEN);
			presetList.addActionListener(event -> {
				JComboBox companyList = (JComboBox) event.getSource();
				setParams(Generation
						.getPresetCompany(companyList.getSelectedIndex()));
				controlPanel.repaint();
				System.out.println(
						"Selected Item  = " + companyList.getSelectedItem());
				System.out.println(
						"Selected Index  = " + companyList.getSelectedIndex());
				System.out.println(
						"Action Command = " + event.getActionCommand());
			});
			presetPanel.add(presetList);
			controlPanel.add(presetPanel);
		}

		// Parameters: attrition 0.0..1.0, 0.0 = None.
		attritionTextField = addParam("Attrition per gen:", "    0.00",
				attritionTextField);
		// Parameters: managementErrors 0.0..1.0
		managementErrorsTextField = addParam("Management errors:", "    0.00",
				managementErrorsTextField);

		// How new members are selected.
		{
			var selectionCriteriaPanel = new JPanel(
					new FlowLayout(FlowLayout.RIGHT));
			selectionCriteriaPanel.setBackground(Color.BLUE);
			var selectionCriteriaLabel = new JLabel("Selection to replace:");
			selectionCriteriaLabel.setForeground(Color.YELLOW);
			selectionCriteriaPanel.add(selectionCriteriaLabel);
			presetList = new JComboBox<String>(Generation.SELECTION_LABELS);
			presetList.setSelectedIndex(Generation.SELECTION_TARGET);
//			selList.addActionListener(event -> {
//				// JComboBox comboBox1 = (JComboBox) event.getSource();
//				JComboBox comboBox1 = selList;
//				System.out.println("Selected Item  = " + comboBox1.getSelectedItem());
//				System.out.println("Selected Index  = " + comboBox1.getSelectedIndex());
//				System.out.println("Action Command = " + event.getActionCommand());
//			});
			selectionCriteriaPanel.add(presetList);
			controlPanel.add(selectionCriteriaPanel);
		}

		// How new members are formed.
		{
			var newMembersCriteriaPanel = new JPanel(
					new FlowLayout(FlowLayout.RIGHT));
			newMembersCriteriaPanel.setBackground(Color.BLUE);
			var newMembersCriteriaLabel = new JLabel("Base for replacement:");
			newMembersCriteriaLabel.setForeground(Color.YELLOW);
			newMembersCriteriaPanel.add(newMembersCriteriaLabel);
			memberList = new JComboBox<String>(Generation.SELECTION_LABELS);
			memberList.setSelectedIndex(Generation.SELECION_NONE);
			newMembersCriteriaPanel.add(memberList);
			controlPanel.add(newMembersCriteriaPanel);
		}

		// Mutation rate for new members.
		mutationRateTextField = addParam("Mutation rate:", "    0.00",
				mutationRateTextField);
		// Change the target every N generations.
		changeTargetEveryTextField = addParam("Change target every:", "      0",
				changeTargetEveryTextField);
		// Do the layoffs volume at the time of the market change.
		layoffVolumeTextField = addParam("Layoffs size:", "    0.00",
				layoffVolumeTextField);
		// Change the layoffs volume at the time of the market change.
		recoveryIncTextField = addParam("Recovery increment:", "    0.00",
				recoveryIncTextField);
		// Market pressure.
		marketPressureTextField = addParam("Market pressure:", "    0.00",
				marketPressureTextField);

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

		// Output: majority to target distance.
		{
			majorityToTargetPanel = new JPanel();
			majorityToTargetLabel = new JLabel(
					String.format(MAJORITY_TO_TARGET_LABEL, 0.0));
			majorityToTargetLabel.setForeground(Color.YELLOW);
			majorityToTargetPanel.add(majorityToTargetLabel);
			controlPanel.add(majorityToTargetPanel);
		}

		// Placeholder to fix Java problem with layouts.
		// controlPanel.add(new JLabel("---"));

		{
		// Main panel to draw the results.
		//paintPanel = new JPanel(new BorderLayout());
		paintPanel = new Canvas();
		final int paintIndent = controlPanel.getWidth() + controlIndent;
		paintPanel.setBounds(paintIndent, 10,
				width - paintIndent - controlIndent, height - 40);
		System.out.println(paintPanel.getSize());
		// paintPanel.setLayout(null);
		//paintPanel.setLayout(new BorderLayout());
		paintPanel.setBackground(Color.lightGray);
		globalPanel.add(paintPanel);
		}
	}

	private double paramToDouble(String name, JTextField v, double deflt) {
		double res;
		try {
			res = Double.valueOf(v.getText().trim());
		} catch (java.lang.NumberFormatException e) {
			System.out.println("Attrition is not set: " + e);
			JOptionPane.showInternalMessageDialog(null,
					name + ": " + v.getText() + " bad format, replaced to "
							+ Double.toString(deflt),
					"Format error", JOptionPane.ERROR_MESSAGE);
			v.setText("    " + Double.toString(deflt));
			res = deflt;
		}
		return res;
	}

	private int paramToInt(String name, JTextField v, int deflt) {
		int res;
		try {
			res = Integer.valueOf(v.getText().trim());
		} catch (java.lang.NumberFormatException e) {
			System.out.println("Attrition is not set: " + e);
			JOptionPane.showInternalMessageDialog(null,
					name + ": " + v.getText() + " bad format, replaced to "
							+ Integer.toString(deflt),
					"Format error", JOptionPane.ERROR_MESSAGE);
			v.setText("    " + Integer.toString(deflt));
			res = deflt;
		}
		return res;
	}

	private Generation.ExperimentParams getParams() {
		var res = new Generation.ExperimentParams();
		res.attrition = paramToDouble("Attrition", attritionTextField, 0.0);
		res.managementErrors = paramToDouble("Management Erros",
				managementErrorsTextField, 0.0);
		res.mutationRate = paramToDouble("Mutation Rate", mutationRateTextField,
				0.0);
		res.selectionMode = presetList.getSelectedIndex();
		res.mutationSource = memberList.getSelectedIndex();
		res.changeTargetEvery = paramToInt("Change Target Every",
				changeTargetEveryTextField, 0);
		res.layoffVolume = paramToDouble("Layoff Volume", layoffVolumeTextField,
				0.0);
		res.recoveryInc = paramToDouble("Recovery Increment",
				recoveryIncTextField, 0);
		res.marketPressure = paramToDouble("Marker Pressure",
				marketPressureTextField, 0);
		return res;
	}

	private void setParams(Generation.ExperimentParams p) {
		attritionTextField.setText(Double.toString(p.attrition));
		managementErrorsTextField.setText(Double.toString(p.managementErrors));
		mutationRateTextField.setText(Double.toString(p.mutationRate));
		presetList.setSelectedIndex(p.selectionMode);
		memberList.setSelectedIndex(p.mutationSource);
		changeTargetEveryTextField
				.setText(Integer.toString(p.changeTargetEvery));
		layoffVolumeTextField.setText(Double.toString(p.layoffVolume));
		recoveryIncTextField.setText(Double.toString(p.recoveryInc));
		marketPressureTextField.setText(Double.toString(p.marketPressure));
	}

	private void RunExperiment() {
		int replaced = 0;

		Graphics g;
		g = paintPanel.getGraphics();
		Dimension d = paintPanel.getSize();
		g.clearRect(0, 0, d.width, d.height);

		int wDraw = d.width - 20;
		int targetOffset = wDraw + 2;
		int wTargetEnd = targetOffset + 18;
		Generation gen = new Generation(wDraw, getParams());

		for (int row = 0; row < d.height && gen.getSize() > 0; row++) {
			// System.out.printf("%d\n", row);
			RGB[] points = gen.getPoints();
			int pSize = gen.getSize();
			int wPoints = pSize < wDraw ? pSize : wDraw;
			int pointsOffset = (wDraw - wPoints) / 2;
			for (int col = 0; col < wPoints; col++) {
				g.setColor(points[col].getColor());
				g.fillRect(col + pointsOffset, row, 1, 1);
			}
			g.setColor(gen.target.getColor());
			for (int col = targetOffset; col < wTargetEnd; col++) {
				g.fillRect(col, row, 1, 1);
			}
			replaced += gen.lostThisGeneration;
			gen.nextGeneration();
			// System.out.printf("Added new layer costs: %d\n", replaced);
			devTargetLabel.setText(
					String.format(DEV_TARGET_LABEL, gen.deviationFromTarget));
			devMajorityLabel.setText(String.format(DEV_MAJORITY_LABEL,
					gen.deviationFromMajority));
		}
		hiringCostLabel.setText(String.format(HIRING_COST_LABEL, replaced));
		double dev = gen.average.Distance(gen.target);
		majorityToTargetLabel
				.setText(String.format(MAJORITY_TO_TARGET_LABEL, dev));
		majorityToTargetPanel.setBackground(dev < 0.1 ? new Color(0, 200, 0)
				: dev < 0.2 ? new Color(70, 180, 0)
						: dev < 0.3 ? new Color(140, 110, 0)
								: dev < 0.4 ? new Color(210, 60, 0)
										: new Color(255, 0, 0));
		majorityToTargetPanel.repaint();

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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

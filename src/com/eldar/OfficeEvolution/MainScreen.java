package OfficeEvolution;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
	private JPanel paintPanel;
	private JPanel controlPanel;
	private JTextField attritionTextField;
	private JTextField minDistanceTextField;
	private JTextField managementErrorsTextField;
	private JTextField mutationRateTextField;
	private JTextField changeTargetEveryTextField;
	private JComboBox<String> presetList;
	private JComboBox<String> memberList;
	private JLabel hiringCostLabel;
	private JLabel devTargetLabel;
	private JLabel devMajorityLabel;

	private final static String HIRING_COST_LABEL = "Hiring cost: %d🍄 ₿";
	private final static String DEV_TARGET_LABEL = "Deviation from target: %f";
	private final static String DEV_MAJORITY_LABEL = "Deviation from majority: %f";

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

		controlPanel = new JPanel(new GridLayout(0, 1));
		controlPanel.setLocation(1, 1);
		controlPanel.setSize(new Dimension(controlWidth, height));
		// controlPanel.setBounds(0, 0, controlWidth, height);
		controlPanel.setBackground(Color.BLUE);
		globalPanel.add(controlPanel);

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
			var presetPanel = new JPanel(new FlowLayout());
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
		{
			var attritionPanel = new JPanel(new FlowLayout());
			attritionPanel.setBackground(Color.BLUE);
			var attritionLabel = new JLabel("Attrition per gen:");
			attritionLabel.setForeground(Color.YELLOW);
			attritionPanel.add(attritionLabel);
			attritionTextField = new JTextField("    0.00");
			attritionPanel.add(attritionTextField);
			controlPanel.add(attritionPanel);
		}

		// Parameters: minDistance 0.0..1.0, 1.0 = None
		{
			var minDistancePanel = new JPanel(new FlowLayout());
			minDistancePanel.setBackground(Color.BLUE);
			var minDistanceLabel = new JLabel("Min distance:");
			minDistanceLabel.setForeground(Color.YELLOW);
			minDistancePanel.add(minDistanceLabel);
			minDistanceTextField = new JTextField("    1.00");
			minDistancePanel.add(minDistanceTextField);
			controlPanel.add(minDistancePanel);
		}

		// Parameters: managementErrors 0.0..1.0
		{
			var managemntErrorsPanel = new JPanel(new FlowLayout()); // new
																		// JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
			managemntErrorsPanel.setBackground(Color.BLUE);
			var managementErrorsLabel = new JLabel("Management errors:");
			managementErrorsLabel.setForeground(Color.YELLOW);
			managemntErrorsPanel.add(managementErrorsLabel);
			managementErrorsTextField = new JTextField("  0.00");
			managemntErrorsPanel.add(managementErrorsTextField);
			controlPanel.add(managemntErrorsPanel);
		}

		// How new members are selected.
		{
			var selectionCriteriaPanel = new JPanel(new FlowLayout());
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
			var newMembersCriteriaPanel = new JPanel(new FlowLayout());
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
		{
			var mutationRatePanel = new JPanel(new FlowLayout());
			mutationRatePanel.setBackground(Color.BLUE);
			var mutationRateLabel = new JLabel("Mutation rate:");
			mutationRateLabel.setForeground(Color.YELLOW);
			mutationRatePanel.add(mutationRateLabel);
			mutationRateTextField = new JTextField("  0.00");
			mutationRatePanel.add(mutationRateTextField);
			controlPanel.add(mutationRatePanel);
		}

		// Change the target every N generations.
		{
			var changeTargetEveryPanel = new JPanel(new FlowLayout());
			changeTargetEveryPanel.setBackground(Color.BLUE);
			var changeTargetEveryLabel = new JLabel("Change target every:");
			changeTargetEveryLabel.setForeground(Color.YELLOW);
			changeTargetEveryPanel.add(changeTargetEveryLabel);
			changeTargetEveryTextField = new JTextField("  0");
			changeTargetEveryPanel.add(changeTargetEveryTextField);
			controlPanel.add(changeTargetEveryPanel);
		}

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
		// controlPanel.add(new JLabel("---"));

		// Main panel to draw the results.
		paintPanel = new JPanel();
		final int paintIndent = controlPanel.getWidth() + controlIndent;
		paintPanel.setBounds(paintIndent, 10,
				width - paintIndent - controlIndent, height - 40);
		paintPanel.setLayout(null);
		paintPanel.setBackground(Color.lightGray);
		globalPanel.add(paintPanel);
	}

	private double paramToDouble(String name, JTextField v, double deflt) {
		double res;
		try {
			res = Double.valueOf(v.getText());
		} catch (java.lang.NumberFormatException e) {
			System.out.println("Attrition is not set: " + e);
			JOptionPane.showInternalMessageDialog(null,
					name + " bad format, replaced to " + Double.toString(deflt),
					"Format error", JOptionPane.ERROR_MESSAGE);
			v.setText("    " + Double.toString(deflt));
			res = deflt;
		}
		return res;
	}

	private Generation.ExperimentParams getParams() {
		var res = new Generation.ExperimentParams();
		res.attrition = paramToDouble("Attrition", attritionTextField, 0.0);
		res.minDistance = paramToDouble("Minimum distance",
				minDistanceTextField, 0.0);
		res.managementErrors = paramToDouble("Management erros",
				managementErrorsTextField, 0.0);
		res.mutationRate = paramToDouble("Mutation rate",
				changeTargetEveryTextField, 0.0);
		res.selectionMode = presetList.getSelectedIndex();
		res.mutationSource = memberList.getSelectedIndex();
		res.changeTargetEvery = Integer
				.valueOf(changeTargetEveryTextField.getText());
		return res;
	}

	private void setParams(Generation.ExperimentParams p) {
		attritionTextField.setText(Double.toString(p.attrition));
		minDistanceTextField.setText(Double.toString(p.minDistance));
		managementErrorsTextField.setText(Double.toString(p.managementErrors));
		changeTargetEveryTextField.setText(Double.toString(p.mutationRate));
		presetList.setSelectedIndex(p.selectionMode);
		memberList.setSelectedIndex(p.mutationSource);
		changeTargetEveryTextField
				.setText(Integer.toString(p.changeTargetEvery));
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

		for (int row = 0; row < d.height; row++) {
			// System.out.printf("%d\n", row);
			RGB[] points = gen.getPoints();
			int wPoints = points.length < wDraw ? points.length : wDraw;
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

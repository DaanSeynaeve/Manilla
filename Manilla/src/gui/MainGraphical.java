package gui;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

import AI.ArtificialPlayer;
import AI.ExampleIntelligence;
import AI.Intelligence;
import core.Match;
import exception.InvalidCardException;

import javax.swing.JTextField;
import javax.swing.JSeparator;

import player.Player;

import java.awt.Color;

public class MainGraphical extends JFrame {

	private static final long serialVersionUID = 8701125966647077641L;
	private JPanel contentPane;
	
	@SuppressWarnings("unchecked")
	private static final Class<? extends Intelligence>[] ais = new Class[] {
		ExampleIntelligence.class
	};
	
	private JTextField namePlayer1;
	private JTextField namePlayer2;
	private JTextField namePlayer3;
	private JTextField namePlayer4;
	
	private JComboBox<String> aiPlayer1;
	private JComboBox<String> aiPlayer2;
	private JComboBox<String> aiPlayer3;
	private JComboBox<String> aiPlayer4;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainGraphical frame = new MainGraphical();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainGraphical() {
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 510, 305);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Welcome to Manilla v1.0!");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setHorizontalTextPosition(SwingConstants.LEFT);
		lblNewLabel.setBounds(46, 11, 414, 25);
		contentPane.add(lblNewLabel);
		
		JPanel panel = new JPanel();
		panel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel.setBounds(46, 47, 414, 176);
		contentPane.add(panel);
		panel.setLayout(null);
		
		List<String> options = new ArrayList<>();
		for ( Class<? extends Intelligence> c : ais ) {
			try { options.add(c.newInstance().identify()); } catch (Exception e) {}
		}
		String[] optArray = options.toArray(new String[options.size()]);
		
		aiPlayer1 = new JComboBox<>(optArray);
		aiPlayer1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		aiPlayer1.setBounds(96, 11, 153, 26);
		panel.add(aiPlayer1);
		
		aiPlayer2 = new JComboBox<>(optArray);
		aiPlayer2.setFont(new Font("Tahoma", Font.PLAIN, 14));
		aiPlayer2.setBounds(96, 50, 153, 26);
		panel.add(aiPlayer2);
		
		aiPlayer3 = new JComboBox<>(optArray);
		aiPlayer3.setFont(new Font("Tahoma", Font.PLAIN, 14));
		aiPlayer3.setBounds(96, 100, 153, 26);
		panel.add(aiPlayer3);
		
		aiPlayer4 = new JComboBox<>(optArray);
		aiPlayer4.setFont(new Font("Tahoma", Font.PLAIN, 14));
		aiPlayer4.setBounds(96, 137, 153, 26);
		panel.add(aiPlayer4);
		
		JLabel lblPlayer1 = new JLabel("Player 1:");
		lblPlayer1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblPlayer1.setBounds(10, 9, 76, 26);
		panel.add(lblPlayer1);
		
		JLabel lblPlayer2 = new JLabel("Player 2:");
		lblPlayer2.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblPlayer2.setBounds(10, 48, 76, 26);
		panel.add(lblPlayer2);
		
		JLabel lblPlayer3 = new JLabel("Player 3:");
		lblPlayer3.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblPlayer3.setBounds(10, 100, 76, 26);
		panel.add(lblPlayer3);
		
		JLabel lblPlayer4 = new JLabel("Player 4:");
		lblPlayer4.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblPlayer4.setBounds(10, 137, 76, 26);
		panel.add(lblPlayer4);
		
		namePlayer1 = new JTextField();
		namePlayer1.setText("Freddy");
		namePlayer1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		namePlayer1.setBounds(259, 11, 145, 26);
		panel.add(namePlayer1);
		namePlayer1.setColumns(10);
		
		namePlayer2 = new JTextField();
		namePlayer2.setText("Patrick");
		namePlayer2.setFont(new Font("Tahoma", Font.PLAIN, 14));
		namePlayer2.setColumns(10);
		namePlayer2.setBounds(259, 50, 145, 26);
		panel.add(namePlayer2);
		
		namePlayer3 = new JTextField();
		namePlayer3.setText("Bertha");
		namePlayer3.setFont(new Font("Tahoma", Font.PLAIN, 14));
		namePlayer3.setColumns(10);
		namePlayer3.setBounds(259, 100, 145, 26);
		panel.add(namePlayer3);
		
		namePlayer4 = new JTextField();
		namePlayer4.setText("Joeri");
		namePlayer4.setFont(new Font("Tahoma", Font.PLAIN, 14));
		namePlayer4.setColumns(10);
		namePlayer4.setBounds(259, 137, 145, 26);
		panel.add(namePlayer4);
		
		JSeparator separator = new JSeparator();
		separator.setForeground(Color.LIGHT_GRAY);
		separator.setBounds(10, 87, 394, 2);
		panel.add(separator);
		
		JButton btnStart = new JButton("Start");
		btnStart.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnStart.setBounds(353, 232, 107, 34);
		btnStart.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					Player p1 = new ArtificialPlayer(namePlayer1.getText(),ais[aiPlayer1.getSelectedIndex()].newInstance());
					Player p2 = new ArtificialPlayer(namePlayer2.getText(),ais[aiPlayer2.getSelectedIndex()].newInstance());
					Player p3 = new ArtificialPlayer(namePlayer3.getText(),ais[aiPlayer3.getSelectedIndex()].newInstance());
					Player p4 = new ArtificialPlayer(namePlayer4.getText(),ais[aiPlayer4.getSelectedIndex()].newInstance());
					Match m = new Match(p1,p2,p3,p4);
					m.run();
				} catch (InvalidCardException | InstantiationException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		});
		contentPane.add(btnStart);
	}
}

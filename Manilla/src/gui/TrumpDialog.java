package gui;

import java.awt.BorderLayout;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;

import core.Suit;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Simple dialog to ask the user to choose a trump suit.
 * @author Daan
 */
public class TrumpDialog extends JDialog {

	private static final long serialVersionUID = -4107699456428530066L;

	public TrumpDialog(JFrame parent, boolean modal) {
		super(parent, modal);
		setResizable(false);
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		initComponents();
	    setLocationRelativeTo(parent);
	    setVisible(true);
	}
	
	/**********************************************************************
	 * TRUMP
	 **********************************************************************/
	
	private Suit trump;
	
	public Suit getTrump() {
		return trump;
	}
	
	/**********************************************************************
	 * COMPONENTS
	 **********************************************************************/
	
	private void initComponents() {
		initializeImages();
		this.setTitle("Trump choice");
		JPanel contentPanel = new JPanel();
		setBounds(100, 100, 216, 256);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(0, 30, 210, 190);
		contentPanel.add(panel);
		panel.setLayout(null);
		
		JButton heartButton = new JButton();
		heartButton.setBounds(20, 10, 80, 80);
		heartButton.add(new JLabel(getSuitImage(Suit.HEART)));
		heartButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				trump = Suit.HEART;
				submitChoice();
			}
		});
		panel.add(heartButton);
		
		JButton spadeButton = new JButton();
		spadeButton.setBounds(110, 10, 80, 80);
		spadeButton.add(new JLabel(getSuitImage(Suit.SPADE)));
		spadeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				trump = Suit.SPADE;
				submitChoice();
			}
		});
		panel.add(spadeButton);
		
		JButton clubButton = new JButton();
		clubButton.add(new JLabel(getSuitImage(Suit.CLUB)));
		clubButton.setBounds(20, 100, 80, 80);
		clubButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				trump = Suit.CLUB;
				submitChoice();
			}
		});
		panel.add(clubButton);
		
		JButton diamondButton = new JButton();
		diamondButton.add(new JLabel(getSuitImage(Suit.DIAMOND)));
		diamondButton.setBounds(110, 100, 80, 80);
		diamondButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				trump = Suit.DIAMOND;
				submitChoice();
			}
		});
		panel.add(diamondButton);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBounds(0, 0, 210, 30);
		contentPanel.add(panel_1);
		{
			JLabel lblPleaseChooseA = new JLabel("Please choose a trump:",JLabel.CENTER);
			panel_1.add(lblPleaseChooseA);
			lblPleaseChooseA.setFont(new Font("Tahoma", Font.PLAIN, 14));
		}
	}
	
	private void submitChoice() {
		this.dispose();
	}
	
	private void initializeImages() {
		try {
			allSuits = ImageIO.read(new File("img/allSuits100.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**********************************************************************
	 * IMAGES
	 **********************************************************************/

	private static final int SUIT_SIZE = 50;
	private BufferedImage allSuits;
	
	private ImageIcon getSuitImage(Suit suit) {
		switch(suit) {
		case SPADE: return new ImageIcon(allSuits.getSubimage(0, 0, SUIT_SIZE, SUIT_SIZE));
		case HEART: return new ImageIcon(allSuits.getSubimage(SUIT_SIZE, 0, SUIT_SIZE, SUIT_SIZE));
		case DIAMOND: return new ImageIcon(allSuits.getSubimage(0, SUIT_SIZE, SUIT_SIZE, SUIT_SIZE));
		case CLUB: return new ImageIcon(allSuits.getSubimage(SUIT_SIZE, SUIT_SIZE, SUIT_SIZE, SUIT_SIZE));
		}
		return null;
	}

}

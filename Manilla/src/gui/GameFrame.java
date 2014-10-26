package gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import core.Card;
import core.Suit;

public class GameFrame extends JFrame {

	private static final long serialVersionUID = -2612918951205849060L;
	
	private final int CARD_WIDTH = 71;
	private final int CARD_HEIGHT = 96;
	
	private final int BOX_SPACING = 2;
	private final int BORDER_WIDTH = 2;

	/**
	 * Create the frame.
	 */
	public GameFrame() {
		loadImages();
		handMap = new HashMap<>();
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 816, 548);
		setMinimumSize(new Dimension(816, 548));
		
		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel center = new JPanel() {
			private static final long serialVersionUID = 1L;

			@Override
			protected void paintComponent(Graphics g)
			{
				super.paintComponent(g);
				g.drawImage(feltBack, 0, 0, getWidth(), getHeight(), null);
			}
		};
		center.setBackground(new Color(0, 128, 0));
		contentPane.add(center, BorderLayout.CENTER);
		center.setLayout(new GridBagLayout());
		
		JPanel east = new JPanel();
		contentPane.add(east, BorderLayout.EAST);
		east.setLayout(new GridBagLayout());
		
		//----------------------------------------
		
		JPanel left = new JPanel();
		left.setOpaque(false);
		left.setBounds(0, 26, 656, 484);
		left.setPreferredSize(new Dimension(656,484));
		center.add(left);
		left.setLayout(null);
		
		//----------------------------------------
		
		JPanel right = new JPanel();
		right.setPreferredSize(new Dimension(144,484));
		GridBagConstraints gbc_right = new GridBagConstraints();
		gbc_right.weighty = 1.0;
		gbc_right.anchor = GridBagConstraints.NORTH;
		east.add(right, gbc_right);
		right.setLayout(null);
		
		/**********************************************************************
		 * MENU BAR
		 **********************************************************************/
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setPreferredSize(new Dimension(800,26));
		contentPane.add(menuBar, BorderLayout.NORTH);
		
		JMenu settingsMenu = new JMenu("Settings");
		menuBar.add(settingsMenu);
		
		JCheckBoxMenuItem chckbxFullscreen = new JCheckBoxMenuItem("FullScreen");
		settingsMenu.add(chckbxFullscreen);
		chckbxFullscreen.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				if ( e.getStateChange() == ItemEvent.SELECTED ) {
					(GameFrame.this).setExtendedState(Frame.MAXIMIZED_BOTH); 
				} else {
					(GameFrame.this).setBounds(100, 100, 816, 548);
				}
			}
			
		});
		
		/**********************************************************************
		 * FIELD PANEL
		 **********************************************************************/
		
		JPanel fieldPanel = new JPanel();
		fieldPanel.setOpaque(false);
		fieldPanel.setBounds(182, 61, 295, 230);
		fieldPanel.setLayout(null);
		left.add(fieldPanel);
		
		EmptyBorder padding = new EmptyBorder(BOX_SPACING,BOX_SPACING,BOX_SPACING,BOX_SPACING);
		topField = Box.createHorizontalBox();
		topField.setBounds(109, 10, CARD_WIDTH + (BOX_SPACING+BORDER_WIDTH)*2, CARD_HEIGHT + (BOX_SPACING+BORDER_WIDTH)*2);
		topField.setBorder(BorderFactory.createCompoundBorder(new LineBorder(new Color(0, 96, 32), BORDER_WIDTH), padding));
		
		leftField = Box.createHorizontalBox();
		leftField.setBounds(24, 68, CARD_WIDTH + (BOX_SPACING+BORDER_WIDTH)*2, CARD_HEIGHT + (BOX_SPACING+BORDER_WIDTH)*2);
		leftField.setBorder(BorderFactory.createCompoundBorder(new LineBorder(new Color(0, 96, 32), BORDER_WIDTH), padding));
		
		rightField = Box.createHorizontalBox();
		rightField.setBounds(194, 68, CARD_WIDTH + (BOX_SPACING+BORDER_WIDTH)*2, CARD_HEIGHT + (BOX_SPACING+BORDER_WIDTH)*2);
		rightField.setBorder(BorderFactory.createCompoundBorder(new LineBorder(new Color(0, 96, 32), BORDER_WIDTH), padding));
		
		bottomField = Box.createHorizontalBox();
		bottomField.setBounds(109, 120, CARD_WIDTH + (BOX_SPACING+BORDER_WIDTH)*2, CARD_HEIGHT + (BOX_SPACING+BORDER_WIDTH)*2);
		bottomField.setBorder(BorderFactory.createCompoundBorder(new LineBorder(new Color(0, 96, 32), BORDER_WIDTH), padding));
		
		fieldPanel.add(leftField);
		fieldPanel.add(rightField);
		fieldPanel.add(topField);
		fieldPanel.add(bottomField);
		
		/**********************************************************************
		 * PLAYER NAMES
		 **********************************************************************/
		
		rightNameLabel = new JLabel("Freddy", SwingConstants.CENTER);
		rightNameLabel.setForeground(Color.WHITE);
		rightNameLabel.setFont(new Font("Tahoma", Font.BOLD, 20));
		rightNameLabel.setBounds(479, 157, 180, 27);
		left.add(rightNameLabel);
		
		topNameLabel = new JLabel("Freddy", SwingConstants.CENTER);
		topNameLabel.setForeground(Color.WHITE);
		topNameLabel.setFont(new Font("Tahoma", Font.BOLD, 20));
		topNameLabel.setBounds(240, 23, 180, 27);
		left.add(topNameLabel);
		
		bottomNameLabel = new JLabel("Freddy", SwingConstants.CENTER);
		bottomNameLabel.setForeground(Color.WHITE);
		bottomNameLabel.setFont(new Font("Tahoma", Font.BOLD, 20));
		bottomNameLabel.setBounds(240, 302, 180, 27);
		left.add(bottomNameLabel);
		
		leftNameLabel = new JLabel("Freddy", JLabel.CENTER);
		leftNameLabel.setFont(new Font("Tahoma", Font.BOLD, 20));
		leftNameLabel.setForeground(Color.WHITE);
		leftNameLabel.setBounds(0, 157, 180, 27);
		left.add(leftNameLabel);
		
		/**********************************************************************
		 * HAND PANEL
		 **********************************************************************/
		
		handPanel = new JPanel();
		handPanel.setOpaque(false);
		handPanel.setBounds(8, 365, 639, 108);
		handPanel.setLayout(null);
		left.add(handPanel);
		
		/**********************************************************************
		 * ...
		 **********************************************************************/
		
		JLabel lblTrump = new JLabel("Trump:",JLabel.CENTER);
		lblTrump.setForeground(Color.DARK_GRAY);
		lblTrump.setBounds(9, 11, 125, 17);
		lblTrump.setFont(new Font("Tahoma", Font.PLAIN, 14));
		right.add(lblTrump);
		
		trumpPanel = new JPanel();
		trumpPanel.setBorder(new LineBorder(new Color(128, 128, 128)));
		trumpPanel.setLayout(new GridBagLayout());
		trumpPanel.setBounds(9, 30, 125, 88);
		right.add(trumpPanel);
		
		btnPreviousTrick = new JButton("<html><center>Previous Trick</center></html>");
		btnPreviousTrick.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnPreviousTrick.setBounds(9, 388, 125, 37);
		btnPreviousTrick.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				displayPreviousTrick();				
			}
		});
		btnPreviousTrick.setEnabled(false);
		
		right.add(btnPreviousTrick);
		
		JLabel lblMultText = new JLabel("Multiplier:",JLabel.CENTER);
		lblMultText.setForeground(Color.DARK_GRAY);
		lblMultText.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblMultText.setBounds(9, 158, 125, 17);
		right.add(lblMultText);
		
		JPanel multPanel = new JPanel();
		multPanel.setForeground(Color.GRAY);
		multPanel.setBorder(new LineBorder(Color.GRAY));
		multPanel.setBounds(9, 177, 125, 37);
		right.add(multPanel);
		multPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		lblMultNumber = new JLabel("x1");
		lblMultNumber.setForeground(Color.GRAY);
		lblMultNumber.setFont(new Font("Tahoma", Font.BOLD, 20));
		multPanel.add(lblMultNumber);
		
		dealerPanel = new JPanel();
		dealerPanel.setForeground(Color.GRAY);
		dealerPanel.setBorder(new LineBorder(new Color(128, 128, 128)));
		dealerPanel.setBounds(9, 115, 125, 32);
		right.add(dealerPanel);
		dealerPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		lblDealer = new JLabel("Freddy");
		lblDealer.setForeground(Color.GRAY);
		lblDealer.setFont(new Font("Tahoma", Font.BOLD, 18));
		dealerPanel.add(lblDealer);
		
		JButton undoButton = new JButton("<html><center>Undo</center></html>");
		undoButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
		undoButton.setBounds(9, 436, 125, 37);
		undoButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent ae) {
				JOptionPane.showMessageDialog(GameFrame.this,
						"Sorry, the table is sticky.",
						"Undo",
						JOptionPane.INFORMATION_MESSAGE);
			}
			
		});
		right.add(undoButton);
		
		JLabel lblPoolScores = new JLabel("Pool scores:", SwingConstants.CENTER);
		lblPoolScores.setForeground(Color.DARK_GRAY);
		lblPoolScores.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblPoolScores.setBounds(9, 225, 125, 17);
		right.add(lblPoolScores);
		
		JPanel poolScorePanelAlly = new JPanel();
		poolScorePanelAlly.setBorder(new TitledBorder(new LineBorder(new Color(128, 128, 128)), "Ally", TitledBorder.CENTER, TitledBorder.BOTTOM, null, Color.DARK_GRAY));
		poolScorePanelAlly.setBounds(9, 244, 61, 50);
		right.add(poolScorePanelAlly);
		poolScorePanelAlly.setLayout(new CardLayout(0, 0));
		
		JPanel poolScorePanelEnemy = new JPanel();
		poolScorePanelEnemy.setBorder(new TitledBorder(new LineBorder(new Color(128, 128, 128)), "Enemy", TitledBorder.CENTER, TitledBorder.BOTTOM, null, Color.DARK_GRAY));
		poolScorePanelEnemy.setBounds(73, 244, 61, 50);
		right.add(poolScorePanelEnemy);
		poolScorePanelEnemy.setLayout(new CardLayout(0, 0));
		
		lblEnemyPoolScore = new JLabel("0",JLabel.CENTER);
		lblEnemyPoolScore.setForeground(Color.GRAY);
		lblEnemyPoolScore.setFont(new Font("Tahoma", Font.BOLD, 18));
		poolScorePanelEnemy.add(lblEnemyPoolScore, "name_28394980645032");
		
		lblAllyPoolScore = new JLabel("0",JLabel.CENTER);
		lblAllyPoolScore.setForeground(Color.GRAY);
		lblAllyPoolScore.setFont(new Font("Tahoma", Font.BOLD, 18));
		poolScorePanelAlly.add(lblAllyPoolScore, "name_28357213157586");
		
		JLabel lblTotalScores = new JLabel("Total scores:", SwingConstants.CENTER);
		lblTotalScores.setForeground(Color.DARK_GRAY);
		lblTotalScores.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblTotalScores.setBounds(9, 299, 125, 17);
		right.add(lblTotalScores);
		
		JPanel totalScorePanelAlly = new JPanel();
		totalScorePanelAlly.setBorder(new TitledBorder(new LineBorder(new Color(128, 128, 128)), "Ally", TitledBorder.CENTER, TitledBorder.BOTTOM, null, Color.DARK_GRAY));
		totalScorePanelAlly.setBounds(9, 317, 61, 50);
		right.add(totalScorePanelAlly);
		totalScorePanelAlly.setLayout(new CardLayout(0, 0));
		
		JPanel totalScorePanelEnemy = new JPanel();
		totalScorePanelEnemy.setBorder(new TitledBorder(new LineBorder(new Color(128, 128, 128)), "Enemy", TitledBorder.CENTER, TitledBorder.BOTTOM, null, Color.DARK_GRAY));
		totalScorePanelEnemy.setBounds(73, 317, 61, 50);
		right.add(totalScorePanelEnemy);
		totalScorePanelEnemy.setLayout(new CardLayout(0, 0));
		
		lblAllyTotalScore = new JLabel("0", SwingConstants.CENTER);
		lblAllyTotalScore.setForeground(Color.GRAY);
		lblAllyTotalScore.setFont(new Font("Tahoma", Font.BOLD, 18));
		totalScorePanelAlly.add(lblAllyTotalScore, "name_29314576817384");
		
		lblEnemyTotalScore = new JLabel("0", SwingConstants.CENTER);
		lblEnemyTotalScore.setForeground(Color.GRAY);
		lblEnemyTotalScore.setFont(new Font("Tahoma", Font.BOLD, 18));
		totalScorePanelEnemy.add(lblEnemyTotalScore, "name_29401204335232");
	}
	
	/**********************************************************************
	 * LOCK
	 **********************************************************************/
	
	private Object lock;
	
	public void attach(Object lock) {
		this.lock = lock;
	}
	
	/**********************************************************************
	 * HAND
	 **********************************************************************/
	
	private JPanel handPanel;
	
	private Map<JLabel,Card> handMap;
	private JLabel selected;
	private boolean handActive = false;
	private Object handActiveLock = new Object();
	
	private static final int HAND_PADDING = 4;
	private static final int HAND_X_OFFSET = 18;
	private static final int HAND_Y_OFFSET = 4;
	private static final int SELECTION_THICKNESS = 2;
	private static final Color SELECTION_VALID = new Color(50, 255, 50);
	private static final Color SELECTION_INVALID = new Color(255, 50, 50);
	
	public void activateHand() {
		synchronized(handActiveLock) {
			this.handActive = true;
		}
	}
	
	public void deactivateHand() {
		synchronized(handActiveLock) {
			this.handActive = false;
		}
	}
	
	public void updateHand(List<Card> hand, final List<Card> valid) {
		Collections.sort(hand, new Comparator<Card>() {
			@Override
			public int compare(Card c1, Card c2) {
				if (c1.getSuit().equals(c2.getSuit())) {
					return c1.compareTo(c2);
				} else {
					return c1.getSuit().compareTo(c2.getSuit());
				}
			}
		});
		clearSelectedCard();
		handMap.clear();
		handPanel.removeAll();
		int cardNumber = 0;
		int x_offset = HAND_X_OFFSET + (CARD_WIDTH + HAND_PADDING)*(8-hand.size())/2;
		for ( Card card : hand ) {
			JLabel cardLabel = new JLabel(getCardImage(card),JLabel.CENTER);
			int width = CARD_WIDTH + SELECTION_THICKNESS*2;
			int height = CARD_HEIGHT + SELECTION_THICKNESS*2;
			cardLabel.setBounds(x_offset + (CARD_WIDTH + HAND_PADDING)*cardNumber, HAND_Y_OFFSET, width, height);
			cardLabel.addMouseListener(new MouseListener() {
				@Override
				public void mouseEntered(MouseEvent e) {
					synchronized(handActiveLock) {
						if ( handActive ) {
							selected = ((JLabel) e.getSource());
							unborderAllHandCards();
							Color selectionColor = (valid.contains(handMap.get(selected))) ? SELECTION_VALID : SELECTION_INVALID;
							selected.setBorder(BorderFactory.createLineBorder(selectionColor,SELECTION_THICKNESS));
						}
					}
				}

				@Override
				public void mouseExited(MouseEvent e) {
					synchronized(handActiveLock) {
						if ( handActive ) { 
							unborderAllHandCards();
						}
					}
				}

				@Override
				public void mousePressed(MouseEvent e) {}

				@Override
				public void mouseReleased(MouseEvent e) {
					synchronized(handActiveLock) {
						if ( handActive && e.getButton() == MouseEvent.BUTTON1) {
							submitCard();
						}
					}
				}

				@Override
				public void mouseClicked(MouseEvent e) {}
			});
			handPanel.add(cardLabel);
			handMap.put(cardLabel, card);
			cardNumber++;
		}
		handPanel.revalidate();
		handPanel.repaint();
	}
	
	private void unborderAllHandCards() {
		for ( JLabel cl : handMap.keySet() ) {
			cl.setBorder(BorderFactory.createEmptyBorder());
		}
	}
	
	private void submitCard() {
		synchronized(lock) {
			if ( lock != null ) {
				lock.notify();
			}
		}
	}
	
	public Card getSelectedCard() {
		return handMap.get(selected);
	}
	
	private void clearSelectedCard() {
		this.selected = null;
	}
	
	/**********************************************************************
	 * FIELD
	 **********************************************************************/
	
	private Box topField;
	private Box bottomField;
	private Box leftField;
	private Box rightField;

	public void updateField(List<Card> field, int offset) {
		Box[] fieldBoxes = { topField, rightField, bottomField, leftField };
		topField.removeAll();
		bottomField.removeAll();
		leftField.removeAll();
		rightField.removeAll();
		for ( int i = 0 ; i < Math.min(field.size(),4) ; i++ ) {
			fieldBoxes[(i-offset+2+4) % 4].add(new JLabel(getCardImage(field.get(i))));
		}
		for ( Box b : fieldBoxes ) {
			b.revalidate();
			b.repaint();
		}
	}
	
	/**********************************************************************
	 * TRUMP, DEALER & MULTIPLIER
	 **********************************************************************/
	
	private JPanel trumpPanel;
	private JLabel lblMultNumber;
	private JPanel dealerPanel;
	private JLabel lblDealer;
	
	public void updateTrump(Suit trump) {
		trumpPanel.removeAll();
		if ( trump != null ) {
			trumpPanel.add(new JLabel(getSuitImage(trump),JLabel.CENTER));
		} else {
			JLabel x = new JLabel("X",JLabel.CENTER);
			x.setFont(new Font("Tahoma",Font.BOLD,40));
			x.setForeground(Color.GRAY);
			trumpPanel.add(x);
		}
		trumpPanel.revalidate();
		trumpPanel.repaint();
	}
	
	public void clearTrump() {
		trumpPanel.removeAll();
		trumpPanel.revalidate();
		trumpPanel.repaint();
	}
	
	public void updateDealerName(String dealerName) {
		lblDealer.setText(dealerName);
		lblDealer.revalidate();
	}
	
	public void updateMultiplier(int multiplier) {
		lblMultNumber.setText("x" + multiplier);
		lblDealer.revalidate();
	}
	
	/**********************************************************************
	 * SCORES
	 **********************************************************************/
	
	private JLabel lblEnemyPoolScore;
	private JLabel lblAllyPoolScore;
	private JLabel lblEnemyTotalScore;
	private JLabel lblAllyTotalScore;
	
	public void updatePoolScores(int ally, int enemy) {
		lblAllyPoolScore.setText(Integer.toString(ally));
		lblAllyPoolScore.setForeground(ally > 30 ? new Color(50, 205, 50) : Color.GRAY);
		lblEnemyPoolScore.setText(Integer.toString(enemy));
		lblEnemyPoolScore.setForeground(enemy > 30 ? Color.RED: Color.GRAY);
		lblAllyPoolScore.revalidate();
		lblEnemyPoolScore.revalidate();
	}
	
	public void updateTotalScores(int ally, int enemy) {
		lblEnemyTotalScore.setText(Integer.toString(enemy));
		lblAllyTotalScore.setText(Integer.toString(ally));
		lblEnemyTotalScore.revalidate();
		lblAllyTotalScore.revalidate();
	}
	
	/**********************************************************************
	 * PREVIOUS TRICK
	 **********************************************************************/
	
	private JPanel trickCache = new JPanel();
	private JButton btnPreviousTrick;
	
	public void updatePreviousTrick(Card[] trick) {
		synchronized(trickCache) {
			trickCache.removeAll();
			for ( Card card : trick ) {
				trickCache.add(new JLabel(getCardImage(card)));
			}
			trickCache.revalidate();
		}
		btnPreviousTrick.setEnabled(true);
	}
	
	public void displayPreviousTrick() {
		synchronized(trickCache) {
			JOptionPane.showMessageDialog(this,trickCache,"Previous Trick",JOptionPane.PLAIN_MESSAGE);
		}		
	}
	
	/**********************************************************************
	 * PLAYER LABELS
	 **********************************************************************/
	
	private JLabel rightNameLabel;
	private JLabel topNameLabel;
	private JLabel bottomNameLabel;
	private JLabel leftNameLabel;
	
	public void updateNames(String guiPlayerName, String leftName, String topName, String rightName) {
		this.rightNameLabel.setText(rightName);
		this.leftNameLabel.setText(leftName);
		this.topNameLabel.setText(topName);
		this.bottomNameLabel.setText(guiPlayerName);
	}
	
	/**********************************************************************
	 * IMAGES
	 **********************************************************************/
	
	private BufferedImage allCards;
	private BufferedImage allSuits;
	private BufferedImage feltBack;
	
	private void loadImages() {
		ClassLoader cl = this.getClass().getClassLoader();
		try {
			allCards = ImageIO.read(cl.getResource("resource/allCards.jpg"));
			allSuits = ImageIO.read(cl.getResource("resource/allSuits100.png"));
			feltBack = ImageIO.read(cl.getResource("resource/felt.png"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	private ImageIcon getCardImage(Card card) {
		int suit = 0;
		switch(card.getSuit()) {
			case CLUB: suit = 0; break;
			case SPADE: suit = 1; break;
			case HEART: suit = 2; break;
			case DIAMOND: suit = 3; break;
		}
		int x = 1 + (CARD_WIDTH + 2)*card.getSymbol().getStrength();
		int y = 1 + (CARD_HEIGHT + 2)*suit;
		return new ImageIcon(allCards.getSubimage(x, y, CARD_WIDTH, CARD_HEIGHT));
	}
	
	private static final int SUIT_SIZE = 50;
	
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

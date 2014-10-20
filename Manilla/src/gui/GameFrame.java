package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import core.Card;
import core.Suit;

import javax.swing.SwingConstants;

public class GameFrame extends JFrame {

	private static final long serialVersionUID = -2612918951205849060L;

	private JPanel contentPane;
	
	private final int CARD_WIDTH = 71;
	private final int CARD_HEIGHT = 96;
	
	private final int BOX_SPACING = 2;
	private final int BORDER_WIDTH = 2;

	/**
	 * Create the frame.
	 */
	public GameFrame() {
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 778, 518);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		loadImages();

		handMap = new HashMap<>();
		
		JPanel left = new JPanel();
		left.setBorder(null);
		left.setBackground(new Color(0, 128, 0));
		left.setBounds(0, 26, 659, 464);
		contentPane.add(left);
		left.setLayout(null);
		
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
		topField.setBorder(BorderFactory.createCompoundBorder(new LineBorder(new Color(0, 96, 0), BORDER_WIDTH), padding));
		
		leftField = Box.createHorizontalBox();
		leftField.setBounds(24, 68, CARD_WIDTH + (BOX_SPACING+BORDER_WIDTH)*2, CARD_HEIGHT + (BOX_SPACING+BORDER_WIDTH)*2);
		leftField.setBorder(BorderFactory.createCompoundBorder(new LineBorder(new Color(0, 96, 0), BORDER_WIDTH), padding));
		
		rightField = Box.createHorizontalBox();
		rightField.setBounds(194, 68, CARD_WIDTH + (BOX_SPACING+BORDER_WIDTH)*2, CARD_HEIGHT + (BOX_SPACING+BORDER_WIDTH)*2);
		rightField.setBorder(BorderFactory.createCompoundBorder(new LineBorder(new Color(0, 96, 0), BORDER_WIDTH), padding));
		
		bottomField = Box.createHorizontalBox();
		bottomField.setBounds(109, 120, CARD_WIDTH + (BOX_SPACING+BORDER_WIDTH)*2, CARD_HEIGHT + (BOX_SPACING+BORDER_WIDTH)*2);
		bottomField.setBorder(BorderFactory.createCompoundBorder(new LineBorder(new Color(0, 96, 0), BORDER_WIDTH), padding));
		
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
		handPanel.setBounds(10, 345, 639, 108);
		handPanel.setLayout(null);
		left.add(handPanel);
		
		/**********************************************************************
		 * ...
		 **********************************************************************/
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 772, 26);
		contentPane.add(menuBar);
		
		// JMenu mnGame = new JMenu("Game");
		// menuBar.add(mnGame);
		
		// JMenuItem mntmSettings = new JMenuItem("Settings");
		// mnGame.add(mntmSettings);
		
		JPanel right = new JPanel();
		right.setBounds(658, 26, 114, 464);
		contentPane.add(right);
		right.setLayout(null);
		
		JLabel lblTrump = new JLabel("Trump:");
		lblTrump.setBounds(33, 11, 46, 17);
		lblTrump.setFont(new Font("Tahoma", Font.PLAIN, 14));
		right.add(lblTrump);
		
		trumpPanel = new JPanel();
		trumpPanel.setBorder(new LineBorder(Color.GRAY));
		trumpPanel.setLayout(new GridBagLayout());
		trumpPanel.setBounds(9, 30, 96, 96);
		right.add(trumpPanel);
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
	
	private static final int HAND_PADDING = 4;
	private static final int HAND_X_OFFSET = 18;
	private static final int HAND_Y_OFFSET = 4;
	private static final int SELECTION_THICKNESS = 2;
	private static final Color SELECTION_COLOR = Color.WHITE;
	
	public void activateHand() {
		this.handActive = true;
	}
	
	public void deactivateHand() {
		this.handActive = false;
	}
	
	public void updateHand(List<Card> hand) {
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
					if ( handActive ) {
						selected = ((JLabel) e.getSource());
						unborderAllHandCards();
						selected.setBorder(BorderFactory.createLineBorder(SELECTION_COLOR,SELECTION_THICKNESS));
					}
				}

				@Override
				public void mouseExited(MouseEvent e) {
					if ( handActive ) { 
						unborderAllHandCards();
					}
				}

				@Override
				public void mousePressed(MouseEvent e) {}

				@Override
				public void mouseReleased(MouseEvent e) {
					if ( handActive && e.getButton() == MouseEvent.BUTTON1) {
						submitCard();
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
	 * TRUMP
	 **********************************************************************/
	
	private JPanel trumpPanel;
	
	public void updateTrump(Suit trump) {
		trumpPanel.removeAll();
		trumpPanel.add(new JLabel(getSuitImage(trump),JLabel.CENTER));
		trumpPanel.revalidate();
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
	
	private void loadImages() {
		try {
			allCards = ImageIO.read(new File("img/allCards.jpg"));
			allSuits = ImageIO.read(new File("img/allSuits100.png"));
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

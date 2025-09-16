import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class BlackJack {
    private class Card {
        String value;
        String type;

        Card(String value, String type){
            this.value = value;
            this.type = type;
        }

        public String toString() {
            return value + "-" + type;
        }

        public int getValue() {
            if ("AJQK".contains(value)) {
                if (value == "A") {
                    return 11;
                } 
                return 10;
            }
            return Integer.parseInt(value);
        }

        public boolean isAce() {
            return value == "A";
        }

        public String getImagePath() {
            return "/cards/" + toString() + ".png";
        }
    }
    
    ArrayList<Card> deck;
    Random random = new Random();

    //dealer 
    Card hiddenCard;
    ArrayList<Card> dealerHand;
    int dealerSum;
    int dealerAceCount; 

    //player
    ArrayList<Card> playerHand;
    int playerSum;
    int playerAceCount;

    //window
    int boardWidth = 600;
    int boardHeight = boardWidth;

    int cardWidth = 110; //ratio should 1/1.4
    int cardHeight = 154;

    JFrame frame = new JFrame("Black Jack");
    JPanel gamePanel = new JPanel() {
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);

        try {
            // draw hidden card
            Image hiddenCardImg = new ImageIcon(getClass().getResource("/cards/BACK.png")).getImage();
            if (!stayButton.isEnabled()) {
                hiddenCardImg = new ImageIcon(getClass().getResource("/cards/" + hiddenCard.toString() + ".png")).getImage();
            }
            g.drawImage(hiddenCardImg, 20, 20, cardWidth, cardHeight, null);

            // draw dealer hand
            for(int i = 0; i < dealerHand.size(); i++) {
                Card card = dealerHand.get(i);
                Image cardImg = new ImageIcon(getClass().getResource("/cards/" + card.toString() + ".png")).getImage();
                g.drawImage(cardImg, cardWidth + 25 + (cardWidth + 5)*i, 20, cardWidth, cardHeight, null);
            }

            //draw player's hand
            for (int i = 0; i < playerHand.size(); i++) {
                Card card = playerHand.get(i);
                Image cardImg = new ImageIcon(getClass().getResource("/cards/" + card.toString() + ".png")).getImage();
                g.drawImage(cardImg, 20 + (cardWidth + 5)*i, 320, cardWidth, cardHeight, null);
            }
            
            if (!hitButton.isEnabled()) {
                String message = "";
                if (playerSum > 21) {
                    message = "You Lose!";
                }

                g.setFont(new Font("Arial", Font.PLAIN, 30));
                g.setColor(Color.white);
                g.drawString(message, 220, 250);
            }

            if (!stayButton.isEnabled()) {
                dealerSum = reduceDealerAce();
                playerSum = reducePlayerAce();

                String message = "";
                if (playerSum > 21) {
                    message = "You Lose!";
                }
                else if (dealerSum > 21) {
                    message = "You Win!";
                }
                //both you and dealer <= 21
                else if (playerSum == dealerSum) {
                    message = "Tie!";
                }
                else if (playerSum > dealerSum) {
                    message = "You Win!";
                }
                else if (playerSum < dealerSum) {
                    message = "You Lose!";
                }

                g.setFont(new Font("Arial", Font.PLAIN, 30));
                g.setColor(Color.white);
                g.drawString(message, 220, 250);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        }
    };
    JPanel buttonPanel = new JPanel();
    JButton hitButton = new JButton("Hit");
    JButton stayButton = new JButton("Stay");
    JButton dealButton = new JButton("Deal");

    BlackJack() {
        startGame();

        frame.setVisible(true);
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        gamePanel.setLayout(new BorderLayout());
        gamePanel.setBackground(new Color(53, 101, 77));
        frame.add(gamePanel);

        hitButton.setFocusable(false);
        buttonPanel.add(hitButton);
        stayButton.setFocusable(false);
        buttonPanel.add(stayButton);
        dealButton.setFocusable(false);
        buttonPanel.add(dealButton);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        dealButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Reset game state and start new turn
                hitButton.setEnabled(true);
                stayButton.setEnabled(true);
                startGame();
                gamePanel.repaint();
            }
        });

        hitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                checkAndReshuffle(); // Check if shoe needs reshuffling
                Card card = deck.remove(deck.size()-1);
                playerSum += card.getValue();
                playerAceCount += card.isAce() ? 1 : 0;
                playerHand.add(card);
                if (reducePlayerAce() > 21) { //A + 2 + J --> 1 + 2 + J
                    hitButton.setEnabled(false); 
                }
                gamePanel.repaint();
            }
        });

        stayButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                hitButton.setEnabled(false);
                stayButton.setEnabled(false);

                while (dealerSum < 17) {
                    checkAndReshuffle(); // Check if shoe needs reshuffling
                    Card card = deck.remove(deck.size()-1);
                    dealerSum += card.getValue();
                    dealerAceCount += card.isAce() ? 1 : 0;
                    dealerHand.add(card);
                }
                gamePanel.repaint();
            }
        });


        gamePanel.repaint();
    }

    public void startGame() {
        //deck - only build and shuffle if deck is empty or null
        if (deck == null || deck.isEmpty()) {
            buildDeck();
            shuffleDeck();
        }

        //dealer 
        dealerHand = new ArrayList<Card>();
        dealerSum = 0;
        dealerAceCount = 0;

        checkAndReshuffle(); // Check if shoe needs reshuffling
        hiddenCard = deck.remove(deck.size()-1);
        dealerSum += hiddenCard.getValue();
        dealerAceCount += hiddenCard.isAce() ? 1:0;

        checkAndReshuffle(); // Check if shoe needs reshuffling
        Card card = deck.remove(deck.size()-1);
        dealerSum += card.getValue();
        dealerAceCount += card.isAce() ? 1:0;
        dealerHand.add(card);

        //player
        playerHand = new ArrayList<Card>();
        playerSum = 0; 
        playerAceCount = 0;

        for(int i = 0; i < 2; i++){
            checkAndReshuffle(); // Check if shoe needs reshuffling
            card = deck.remove(deck.size()-1);
            playerSum += card.getValue();
            playerAceCount += card.isAce() ? 1:0;
            playerHand.add(card);
        }

    }

    public void buildDeck() {
        deck = new ArrayList<Card>();
        String[] values = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};
        String[] types = {"C", "D", "H", "S"};

        // Create 6 decks (312 cards total) like a real blackjack shoe
        for(int deckNum = 0; deckNum < 6; deckNum++) {
            for(int i = 0; i < types.length; i++) {
                for(int j = 0; j < values.length; j++) {
                    Card card = new Card(values[j], types[i]);
                    deck.add(card);
                }
            }
        }
    }

    public void shuffleDeck() {
        for(int i = 0; i < deck.size(); i++){
            int j = random.nextInt(deck.size());
            Card currCard = deck.get(i);
            Card randomCard = deck.get(j);
            deck.set(i, randomCard);
            deck.set(j, currCard);
        }
    }

    public void checkAndReshuffle() {
        // Reshuffle when about 75% of cards have been dealt (like real casinos)
        // With 6 decks (312 cards), reshuffle when we have about 78 cards left
        if (deck.size() < 78) {
            System.out.println("Shoe running low! Reshuffling...");
            buildDeck();
            shuffleDeck();
        }
    }

    public int reducePlayerAce() {
        while (playerSum > 21 && playerAceCount > 0) {
            playerSum -= 10;
            playerAceCount -= 1;
        }
        return playerSum;
    }

    public int reduceDealerAce() {
        while (dealerSum > 21 && dealerAceCount > 0) {
            dealerSum-= 10;
            dealerAceCount -= 1;
        }
        return dealerSum;
    }
}

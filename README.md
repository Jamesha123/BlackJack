# 🃏 Blackjack Game

A fully-featured Blackjack game built with Java Swing, featuring a realistic 6-deck shoe system and complete card graphics.

## 🎮 Features

- **Realistic Casino Experience**: 6-deck shoe (312 cards) like real blackjack tables
- **Automatic Shoe Management**: Auto-reshuffles when ~75% of cards are dealt
- **Complete Card Graphics**: High-quality card images for all 52 cards + jokers
- **Standard Blackjack Rules**: 
  - Dealer hits on 16, stands on 17
  - Ace counts as 11 or 1 automatically
  - Blackjack detection
- **Interactive GUI**: Clean, intuitive interface with hit, stay, and deal buttons
- **Executable JAR**: Ready-to-run game file

## 🚀 Quick Start

### Option 1: Run the JAR File (Recommended)
```bash
java -jar Blackjack.jar
```

### Option 2: Compile and Run from Source
```bash
# Compile the project
javac -d bin src/*.java

# Copy card images to bin directory
xcopy "src\cards" "bin\cards" /E /I /Y

# Run the game
java -cp bin App
```

## 🎯 How to Play

1. **Start**: The game automatically deals initial cards (2 to player, 2 to dealer with 1 hidden)
2. **Hit**: Take another card (click "Hit" button)
3. **Stay**: Keep your current hand (click "Stay" button)
4. **Deal**: Start a new hand (click "Deal" button)
5. **Win Conditions**:
   - Get 21 or closer to 21 than dealer without going over
   - Dealer busts (goes over 21)
   - Get blackjack (21 with first 2 cards)

## 🏗️ Project Structure

```
Blackjack/
├── src/
│   ├── App.java              # Main entry point
│   ├── BlackJack.java        # Game logic and GUI
│   └── cards/                # Card image resources
│       ├── A-C.png to K-S.png # All 52 standard cards
│       ├── J-B.png, J-R.png  # Joker cards
│       └── BACK.png          # Card back image
├── bin/                      # Compiled classes
├── Blackjack.jar            # Executable JAR file
└── README.md                # This file
```

## 🔧 Technical Details

- **Language**: Java
- **GUI Framework**: Java Swing
- **Card System**: 6-deck shoe with automatic reshuffling
- **Image Loading**: Optimized for JAR file distribution
- **Memory Management**: Efficient deck handling between hands

## 🎲 Game Mechanics

### Shoe System
- **6 Decks**: 312 total cards (6 × 52 cards)
- **Reshuffle Point**: When ~78 cards remain (75% dealt)
- **Automatic**: No manual intervention required

### Card Values
- **Number Cards**: Face value (2-10)
- **Face Cards**: 10 points (J, Q, K)
- **Ace**: 11 points (reduces to 1 if busting)

### Dealer AI
- **Hit**: On 16 or below
- **Stand**: On 17 or above
- **Ace Handling**: Automatic soft/hard ace calculation

## 📋 Requirements

- **Java**: Version 8 or higher
- **Operating System**: Windows, macOS, or Linux
- **Memory**: Minimal requirements (game is lightweight)

## 🎨 Customization

The game is easily customizable:
- **Card Images**: Replace files in `src/cards/` directory
- **Deck Count**: Modify the loop in `buildDeck()` method
- **Reshuffle Point**: Adjust the threshold in `checkAndReshuffle()` method
- **UI Colors**: Change colors in the `gamePanel.setBackground()` call

**Enjoy playing Blackjack! 🎰**

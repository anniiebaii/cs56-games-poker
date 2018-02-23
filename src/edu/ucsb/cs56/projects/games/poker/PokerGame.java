package edu.ucsb.cs56.projects.games.poker;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.*;
import java.lang.String;
import java.lang.System;
import java.net.URL;
import java.util.ArrayList;

/**
 * Class that represents a Texas Holdem' Style Poker Game.
 */
public class PokerGame {
    // PokerGame States

    /**
     * enum representing the game winner
     */
    public enum Winner {
        PLAYER, OPPONENT, TIE, NEW_GAME
            };
    public boolean Fold = false;

    /**
     * enum representing the game step
     */
    public enum Step {
        BLIND, FLOP, TURN, RIVER, SHOWDOWN
            };

    /**
     * enum representing which player's turn it is
     */
    public enum Turn {
        PLAYER, OPPONENT
            };


    // PokerGame GUI


    // Properties of Poker Game

    /**
     * ArrayList holding the game's current players
     */
    //protected ArrayList<Player> players;

    /**
     * The user playing the game
     */
    protected Player player;

    /**
     * The opponent (currently an AI)
     */
    protected Player opponent;

    /**
     * The deck used for the game
     */
    protected Deck deck;

    /**
     * The cards currently on the table
     */
    protected TableCards table;

    /**
     * The current pot
     */
    protected int pot;

    // Variables maybe used ---- Eventually this should not be in existence
    
    /**
     * The current bet
     */
    protected int bet = 0;

    /**
     * Information/instructions displayed to the user
     */
    protected String message, prompt;

    /**
     * Image of the back of the card
     */
    protected ImageIcon backCardImage;

    /**
     * The round winner
     */
    protected Winner winnerType = Winner.NEW_GAME;
    /**
     * Variable representing the game winner
     * winner = 0 : players[0] is the winner
     * winner = 1 : players[1] is the winner
     * etc.
     */
    //int winner;

    /**
     * The current game step
     */
    protected Step step;

    /**
     * Which player's turn it is
     */
    protected Turn turn;
    /**
     * Variable representing which player's turn it is
     * turn = 0 : players[0] turn
     * turn = 1 : players[1] turn
     * etc
     */
    //int turn;

    /**
     * The back of a card
     */
    protected Card backCard;

    // Booleans variable

    /**
     * Whether or not one player is responding to the other player's move
     */
    protected boolean responding = false;

    /**
     * The game status
     */
    protected boolean gameOver = false;

    /**
     * Whether or not someone has gone all in
     */
    protected boolean allIn = false;
    
    /** TODO: Change for multiplayer
     * No arg constructor that initializes a new deck.
     */
    // FIX THIS OR FIX setUp() METHOD
    public PokerGame() {
        this.deck = new Deck();
        this.player = new User(500, deck);
        this.opponent = new OpponentAI(500, deck);
        this.table = new TableCards(deck);
        pot = 0;
    }
    
    // Getters and setters for various members

    /**
     * Returns the current pot
     * @return the current pot
     */
    public int getPot() {
        return pot;
    }

    /**
     * Adds a bet to the current pot
     * @param pot the pot's new value
     */
    public void addToPot(int bet) {
        this.pot += bet;
    }

    /**
     * Returns the current bet
     * @return the current bet
     */
    public int getCurrentBet() {
        return bet;
    }

    /**
     * Sets the current bet
     * @param bet the number to set the current bet to
     */
    public void setBet(int bet) {
        this.bet = bet;
    }

    /**
     * Returns the game's current step
     * @return the current step
     */
    public Step getStep() {
        return step;
    }

    /**
     * Returns if the player is responding to the move
     * @return whether or not the player is responding
     */
    public boolean isResponding() {
        return responding;
    }

    /**
     * Sets responding to true or false
     * @param responding responding's new value
     */
    public void setResponding(boolean responding) {
        this.responding = responding;
    }

    /**
     * Returns whether or not it is all-in in the current game
     * @return the boolean value of allIn
     */
    public boolean isAllIn() {
        return allIn;
    }

    /**
     * Sets the current message being displayed
     * @param message the new message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /** TODO: Change for multiplayer
     * Sets up the player's and opponent's hand.
     */
    public void setUp() {
        player.setDelegate(this);
        opponent.setDelegate(this);
        if (player.getChips() >= 5 && opponent.getChips() >= 5) {
            player.bet(5);
            opponent.bet(5);
            pot += 10;
            message = "Ante of 5 chips set.";
        }
        else {
            gameOver = true;
            showWinnerAlert();
        }

        backCard = new Card(100, "B");
        String dir = "Cards/";
        String cardFile = "B.png";
        URL url = getClass().getResource(dir + cardFile);
        backCardImage = new ImageIcon(url);
    }

    /**
     * Returns an ImageIcon by using the URL class in order to make the
     * ImageIcon web compatible
     * @param c whose image is to be retrieved
     * @return ImageIcon
     */
    public ImageIcon getCardImage(Card c) {
        String dir = "Cards/";
        String cardFile = c.toString() + ".png";
        URL url = getClass().getResource(dir + cardFile);
        return new ImageIcon(url);
    }

    /** TODO: Change for multiplayer
     * PlayerDelegate method handles folding
     */
    // COULD IMPROVE IMPLEMENTATION
    public void fold() {
        if (turn == Turn.PLAYER) {
            winnerType = Winner.OPPONENT;
            opponent.win();
        } else {
            winnerType = Winner.PLAYER;
            player.win();
        }
        collectPot();
	Fold = true;
        showWinnerAlert();
	// Reset player win flag

        // deck.reShuffle();
        winnerType = Winner.NEW_GAME;
    }

    /** TODO: Change for multiplayer
     * Function that transfers winnings to a player
     */
    protected void collectPot() {
        if (winnerType == Winner.PLAYER) {
	    // Player won
            System.out.println("Player");
            System.out.println(String.format("%d", pot));
            int playerChips = player.getChips();
            playerChips += pot;
            player.setChips(playerChips);
            player.win();
        } else if (winnerType == Winner.OPPONENT) {
            // Player lost
            System.out.println("OPPONENT");
            System.out.println(String.format("%d", pot));
            int opponentChips = opponent.getChips();
            opponentChips += pot;
            opponent.setChips(opponentChips);
            opponent.win();
        } else if (winnerType == Winner.TIE) {
            // Tie, split pot, should be extremely rare
            System.out.println("Tie");
            System.out.println(String.format("%d", pot));
            int opponentChips = opponent.getChips();
            opponentChips += (pot / 2);
            opponent.setChips(opponentChips);
            pot -= (pot / 2);
            int playerChips = player.getChips();
            playerChips += pot;
            player.setChips(playerChips);
        } else {
            // New game, should never be called
            pot = 0;
        }
    }
	
    /** TODO: Change for multiplayer
     * Method to determine the winner of the game
     */
    // Possibly fix this
 
    public void determineWinner() {
        CompareHands comparison = new CompareHands(player, opponent, table);
        int winner = comparison.compareHands();

        if (winner == 1) {
            player.win();
            winnerType = Winner.PLAYER;
            if (winner == 1) {
                player.win();
                winnerType = Winner.PLAYER;
            } else if (winner == 0) {
                winnerType = Winner.OPPONENT;
                opponent.win();
            }
        } else {
                winnerType = Winner.TIE;
            }
    }

    public String winningHandMessage(){
	CompareHands comparison = new CompareHands(player, opponent, table);
	return comparison.compareMessage();
    }
    /**
     * Method to pass the turn from opponent to player, or player to opponent
     * Must be overridden!!!
     */
    public void changeTurn() { }

    /**
     * Method used to advance the game from each step to the next.
     * Specifically, in the order Blind-Flop-Turn-River-Showdown
     */
    public void nextStep() {
	//Now implemented in PokerSinglePlayer
	/*   if (step == Step.BLIND) { // Most like able to skip/remove this step
            step = Step.FLOP;
        } else if (step == Step.FLOP) {
            step = Step.TURN;
        } else if (step == Step.TURN) {
            step = Step.RIVER;
        } else {
            step = Step.SHOWDOWN;
            message = "All bets are in.";
            prompt = "Determine Winner: ";
	    controlButtons();
	}*/
    }

    /**
     * This state is for the turn
     * Purpose is to be overriden in PokerClient
     */
    protected void checkPassTurnUpdate() {
        changeTurn();
    }

    /**
     * Method shows winner and exits game.
     * Overridden to actually do stuff in children
     */
    public void showWinnerAlert() { }

    /**
     * Method that updates the panels in the GUI based on the current game state
     * Must be overriden (preferrably in the GUI class)
     */
    public void updateFrame() { }

    /**
     * Called when it is a user's turn
     * Must be overridden
     */
    public void userTurn() { }

    /**
     * Restarts the timer
     * Must be overridden
     */
    public void restartTimer() { }

}

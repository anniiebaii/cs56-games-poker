package edu.ucsb.cs56.projects.games.poker;

import java.io.Serializable;
import java.util.*;


/**
   Abstract class that represents a poker player
*/
public abstract class Player implements Serializable {
    /**
     * The player's current hand
     */
    private Hand pokerHand;

    /**
     * The amount of chips the player currently has
     */
    private int chips;

    /**
     * The number of wins the player has in the current round
     */
    private int wins;

    /**
     * The current PokerGame the player is a part of
     */
    public PokerGameMult delegate;

    /**
     * Whether the player is active or folded
     * 0 = folded, 1 = active
     */
    public int status;
    public boolean winStatus;
    public boolean turn;
    public int type;
    protected int index;

    /**
     *Creates a Poker player with a set hand
     * @param hand the hand you have
     */
    public Player(Hand hand){
        pokerHand = hand;
        chips = 100;
        wins = 0;
	winStatus = false;
	status = 1;
    }

    /**
     * Creates a Poker player with a designated number of chips
     * @param chips number of chips
     * @param deck of Cards
     */
    public Player(int chips, Deck deck) {
        this.pokerHand = new Hand(deck);
        this.chips = chips;
        this.wins = 0;
      	status = 1;
      	winStatus = false;
    }

    /**
     * Get Player's Hand
     * @return Hand
     */
    public Hand getHand() {
        return pokerHand;
    }


    /**
     * Get Player's Chips
     * @return int
     */
    public int getChips() {
        return chips;
    }

    /**
     * Set number of chips player has
     * @param chips number of chips to give to player
     */
    // Might never need to be used---Check later
    public void setChips(int chips) {
        this.chips = chips;
    }

    /**
     * Get number of player wins
     * @return int
     */
    public int getWins() {
        return wins;
    }

    /**
     * Increment player's wins by one
     */
    public void win() {
        wins += 1;
    }

    public void setWins(int Wins) {
	wins = Wins;
    }
    public void setIndex(int idx)
    {
      index = idx;
    }

    /**
     * Bet chips
     * @param chipsBet number of chips to bet
     */
    public void bet(int chipsBet) {
        if (chipsBet <= 0)
	    ; // Assert an Error "Not Valid"
        else if (chipsBet > chips)
            ; // Assert an Error "Not Enough Chips"
        else
            chips -= chipsBet;
    }

    /**
     * Fold hand, calls delegate fold method
     */
    // Might never need to be used---Check later
    public void foldHand() {
	      status = 0;
        delegate.fold();
    }

    public void setDelegate(PokerGameMult game) {
        this.delegate = game;
    }

    public void setWin() {
	winStatus = true;
    }

    public int getType() {
	return type;
    }

    /**
     * Performs a player's turn
     * This behavior changes depending on if the player is human or AI
     * and must be implemented by the subclass (User or OpponentAI)
     */
    public abstract void takeTurn();
}

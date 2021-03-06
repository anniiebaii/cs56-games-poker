package edu.ucsb.cs56.projects.games.poker;

import java.io.Serializable;
import java.util.*;
import java.util.ArrayList;

/**
 * Class that Compares Hands between Players
 */
public class CompareHands implements Serializable{
    /**
     * ArrayList to hold each Player's hand, index = player number
    */
    private ArrayList<Card> cardHand1;
    private ArrayList<Card> cardHand2;
    private ArrayList<ArrayList<Card>> hands;

    private ArrayList<Player> players;

    private int ties;
    /**
     * Multiplayer Compare Hands Constructor
     * supports dynamic arraylist for any number of players
     * @param ArrayList of all participating Players
     * @param table the cards that are on the table
     *
     */
    public CompareHands(ArrayList<Player> Players, TableCards table) {
    	// initialize ArrayLists
    	// nested ArrayList; hands[0] = cards of player 0
    	// EX: hands = [ [card1, card2...], [card1, card2...] ]
    	hands = new ArrayList<ArrayList<Card>>(Players.size());
      players = Players;
      // for each player, get their hand and put it into ArrayList hands
    	for (int i = 0; i < Players.size(); i ++) {
          // collection to store cards
          ArrayList<Card> cards = new ArrayList<Card>();
    	    // get player at current index
    	    Player current = Players.get(i);

          // get current player's hand and add to cards
          cards.addAll(current.getHand());
    	    cards.addAll(table.getFlopCards());
          cards.add(table.getTurnCard());
          // add player's cards into their hand
          hands.add(cards);
    	}


    }

    /**
     * compareHands multiplayer function
     * Returns winner's player index (ex: winner = player 3, returns 2 because of indexing)
     * Returns 0 if there is a tie for the highest hand value
    **/
    public int compareHands() {
    	int max_value = -1;
    	int winner = 0;
      ties = 0;

    	for (ArrayList<Card> hand : hands) {
    	    int value = calculateValue(hand);
          int idx = hands.indexOf(hand);
              if (value > max_value) {
        		// reset max value
            		max_value = value;
        		// replace previous winner
                if (players.get(idx).status == 1) {
                  winner = hands.indexOf(hand);

                }
                // if exceeded highest hand, tie = 0
                ties = 0;

        	    }
        	    // if equal highest hand, add to ties
        	    else if (value == max_value) {
        		      winner = 0 ;
                  ties++;
        	    }

    	}
    	return winner;

    }
    // returns number of players tie
    public int numberOfTies()
    {
      return ties;
    }

    /**
     * This method is used if both hands are the same type
     * Hand is either this or the otherhand
     * recursion: Either 1 if called 1st, 2 if called 2nd
     * func won't work as intended if given a different number
     * @param c this is the array of cards
     * @param recursion MUST BE A 1 OR A 2 IF YOU WANT THIS TO WORK
     * @return int returns an integer
     */
    private int sameHandUpdated(ArrayList<Card> c, int recursion) {
        ArrayList<Card> cards_tmp = new ArrayList<Card>();
        Card yourCard = c.get(0);
        if (recursion==1) {
            if (c.get(1).getValue()>yourCard.getValue())
                yourCard = c.get(1);
            cards_tmp.add(yourCard);
            for (int i=2; i<c.size(); i++)
            cards_tmp.add(c.get(i));
            return calculateValue(cards_tmp);
        }
        if (recursion==2) {
            if (c.get(1).getValue()<yourCard.getValue())
            yourCard = c.get(1);
            cards_tmp.add(yourCard);
            for(int i=2; i<c.size(); i++)
            cards_tmp.add(c.get(i));
            return calculateValue(cards_tmp);
        }
        return -1; // Should only pass in 1 or 2 in parameter
    }

    /**
     * Method that finds the type of hand the player has
     * @param player the cards that belong to the players
     * @return int
     */

    public int calculateValue(ArrayList<Card> player) {
        if(isStraightFlush(player))
            return 8;
        else if(isFourOfAKind(player))
            return 7;
        else if(isFullHouse(player))
            return 6;
        else if(isFlush(player))
            return 5;
        else if(isStraight(player))
            return 4;
        else if(isThreeOfAKind(player))
            return 3;
        else if(isTwoPair(player))
            return 2;
        else if(isOnePair(player))
            return 1;
        else
            return 0;
    }

   /**
    * Method that explicitly names the player's hand.
    * @param player: the cards belonging to the player.
    * @return String
    */

    public String calculateValueToString(ArrayList<Card> player){
	if(isStraightFlush(player))
            return ("Straight Flush");
        else if(isFourOfAKind(player))
            return ("Four of a Kind");
        else if(isFullHouse(player))
            return ("Full House");
        else if(isFlush(player))
            return ("Flush");
        else if(isStraight(player))
            return ("Straight");
        else if(isThreeOfAKind(player))
            return ("Three of a Kind");
        else if(isTwoPair(player))
            return ("Two Pair");
        else if(isOnePair(player))
            return ("Pair");
	else
            return ("Mix");
    }

    /**
     * compareMessage for multiplayer
     **/
    public String compareMessage(){
	// get winner's index in hands ArrayList
    	int winner = this.compareHands();
    	ArrayList<Card> winning_hand = hands.get(winner);
    	String message;

	/** if it is not a TIE **/
    	if (winner >= 0) {
    	    // get winner's cards
	    if (players.size() == 2) {
		    message = calculateValueToString(winning_hand) + " beats " + calculateValueToString(hands.get(1-winner)) + "!";
	    }
	    else
    	         message = calculateValueToString(winning_hand) + " beats all!";
    	}
    	else {
    	    message = "It's a tie with " + calculateValueToString(winning_hand);
    	}

    	return message;
    }

    /**
     * @param player the cards belonging to the player
     * @return sortedHand
    */
    public ArrayList<Integer> sortHand(ArrayList<Card> player) {
        ArrayList<Integer> sortedHand = new ArrayList<Integer>();
        for (int i = 0; i < player.size(); i++) {
            sortedHand.add(player.get(i).getValue());
        }
        Collections.sort(sortedHand);
        return sortedHand;
    }

    /**
       Returns boolean for if the hand is a straight flush
       * @param player Cards belonging to the player
       * @return boolean
    */
    private boolean isStraightFlush(ArrayList<Card> player){
        int straightFlushCounter=0;
        int spadeCounter=0;
        int clubsCounter=0;
        int heartCounter=0;
        int diamondCounter=0;
        ArrayList<Integer> spades=new ArrayList<Integer>();
        ArrayList<Integer> clubs=new ArrayList<Integer>();
        ArrayList<Integer> diamonds=new ArrayList<Integer>();
        ArrayList<Integer> hearts=new ArrayList<Integer>();
        for(Card c: player){ //Sort the hand into seperate arrayLists defined by suits.
            if(c.getSuit()=="S"){
            spadeCounter++;
            spades.add(c.getValue());
            }
            else if(c.getSuit()=="C"){
            clubsCounter++;
            clubs.add(c.getValue());
            }
            else if(c.getSuit()=="D"){
            diamondCounter++;
            diamonds.add(c.getValue());
            }
            else if(c.getSuit()=="H"){
            heartCounter++;
            hearts.add(c.getValue());
            }
        }
        int i=0;
        removeDuplicates(spades);
        removeDuplicates(clubs);
        removeDuplicates(diamonds);
        removeDuplicates(hearts);

        if(spadeCounter>=5){
            Collections.sort(spades);
            if (spades.get(spades.size()-1 ) == 14) //if the top value is an ace, use the method below
            if (isLowestRankingStraight(spades))
                return true;
            for (i = 0; i < spadeCounter-5; i++) {
            if(spades.get(i)==(spades.get(i+1)-1) &&
               spades.get(i)==(spades.get(i+2)-2) &&
               spades.get(i)==(spades.get(i+3)-3) &&
               spades.get(i)==(spades.get(i+4)-4))
                straightFlushCounter=4;
            }
        }
        else if(clubsCounter>=5){
            Collections.sort(clubs);
            if (clubs.get(clubs.size()-1 ) == 14) //if the top value is an ace, use the method below
            if (isLowestRankingStraight(clubs))
                return true;
            for (i = 0; i < clubsCounter-5; i++) {
            if(clubs.get(i)==(clubs.get(i+1)-1) &&
               clubs.get(i)==(clubs.get(i+2)-2) &&
               clubs.get(i)==(clubs.get(i+3)-3) &&
               clubs.get(i)==(clubs.get(i+4)-4))
                straightFlushCounter=4;
            }
        }
        else if(heartCounter>=5){
            if (hearts.get(hearts.size()-1 ) == 14) //if the top value is an ace, use the method below
            if (isLowestRankingStraight(hearts))
                return true;
                Collections.sort(hearts);
            for (i = 0; i < heartCounter-5; i++) {
            if(spades.get(i)==(hearts.get(i+1)-1) &&
               hearts.get(i)==(hearts.get(i+2)-2) &&
               hearts.get(i)==(hearts.get(i+3)-3) &&
               hearts.get(i)==(hearts.get(i+4)-4))
                straightFlushCounter=4;
            }
        }
        else if(diamondCounter>=5){
            if (diamonds.get(diamonds.size()-1 ) == 14) //if the top value is an ace, use the method below
            if (isLowestRankingStraight(diamonds))
                return true;
                    Collections.sort(diamonds);
            for (i = 0; i < diamondCounter-5; i++) {
            if(diamonds.get(i)==(diamonds.get(i+1)-1) &&
               diamonds.get(i)==(diamonds.get(i+2)-2) &&
               diamonds.get(i)==(diamonds.get(i+3)-3) &&
               diamonds.get(i)==(diamonds.get(i+4)-4))
                straightFlushCounter=4;
            }
        }
        else
            return false;

        if(straightFlushCounter==4)
            return true;
        else
            return false;
    }

    /**
       Returns boolean for if the hand has a four of a kind.
       * @param player the cards belonging to the player
       * @return boolean
    */
    private boolean isFourOfAKind(ArrayList<Card> player) {
        ArrayList<Integer> sortedHand=sortHand(player);
        int quadCounter=0;
        for(int i=0;i<player.size()-3;i++) {
            if(sortedHand.get(i)==sortedHand.get(i+1) && sortedHand.get(i+1)
               ==sortedHand.get(i+2) && sortedHand.get(i+2)==sortedHand.get(i+3)) {
            quadCounter=3;
            }
        }
        if(quadCounter==3)
            return true;
        else
            return false;
        }

        /**
           Returns boolean for if the hand is a full house.
           * @param player the cards belonging to the player
           * @return boolean
        */
    private boolean isFullHouse(ArrayList<Card> player) {
        ArrayList<Integer> sortedHand=sortHand(player);
        int doubleCounter=0;
        int tripleCounter=0;
        for(int i=0;i<player.size()-1;i++) {
            if(sortedHand.get(i)==sortedHand.get(i+1)) {
            if(tripleCounter==1) {
                sortedHand.remove(i+1);
                sortedHand.remove(i);
                tripleCounter++;
                break;
            }
            else {
                if(i==1)
                tripleCounter=0;
                else
                tripleCounter++;
            }
            }
            else
            tripleCounter=0;
        }

        if(tripleCounter==2) {
            sortedHand.trimToSize();
            int size=sortedHand.size();
            for(int i=0;i<(size-1);i++) {
            if(sortedHand.get(i)==sortedHand.get(i+1))
                doubleCounter++;
            }
        }
        else
            return false;
        if(doubleCounter>=1)
            return true;
        else
            return false;
    }

    /**
       Returns boolean for if the hand is a flush.
       * @param player the cards belonging to the player
       * @return boolean
    */
    private boolean isFlush(ArrayList<Card> player) {
        int spadeCounter=0;
        int cloverCounter=0;
        int heartCounter=0;
        int diamondCounter=0;
        for(Card c: player){
            if(c.getSuit()=="S")
            spadeCounter++;
            else if(c.getSuit()=="C")
            cloverCounter++;
            else if(c.getSuit()=="D")
            diamondCounter++;
            else
            heartCounter++;
        }
        if(spadeCounter>=5 || cloverCounter>=5 || diamondCounter>=5
           || heartCounter>=5)
            return true;
        else
            return false;
    }

    /**
       Returns boolean for if the hand is a straight.
       * @param player the card belonging to the player
       * @return boolean
    */
    private boolean isStraight(ArrayList<Card> player) {
        ArrayList<Integer> sortedHand=sortHand(player);
        removeDuplicates(sortedHand);
        if (sortedHand.get(sortedHand.size()-1 ) == 14) //if the top value is an ace, use the method below
            if (isLowestRankingStraight(sortedHand))
            return true;
        int straightCounter=0;
        for(int i=0;i<player.size()-4;i++) {
            if(sortedHand.get(i)==(sortedHand.get(i+1)-1) &&
               sortedHand.get(i)==(sortedHand.get(i+2)-2) &&
               sortedHand.get(i)==(sortedHand.get(i+3)-3) &&
               sortedHand.get(i)==(sortedHand.get(i+4)-4))
                {
                straightCounter=4;
                }
        }
        return(straightCounter==4);
    }

    /**
     *Used when checking for Straights. Ace has a value of 14
     * but technically it should also have a value of one. This method covers this.
     * @param sortedHand a hand that is already sorted from lowest to highest
     * @return boolean
     */
    private boolean isLowestRankingStraight(ArrayList<Integer> sortedHand) {
        for (int i = 0; i<sortedHand.size()-3; i++) {
            if (sortedHand.get(0) == 2  && //has 2
            sortedHand.get(1) == 3 && //has 3
            sortedHand.get(2) == 4  && //has 4
                sortedHand.get(3) == 5 )  //has 5
            return true;
        }
        return false;
    }

    /**
     * Method that removes duplicate integers from an arrayList.
     * If the hand is sorted, it stays sorted.
     * @param sortedHand it is sorted, but it doesn't have to be.
     * @return sortedHand a hand without duplicates.
     */
    private ArrayList<Integer> removeDuplicates(ArrayList<Integer> sortedHand) {
        ArrayList<Integer> deDupList = new ArrayList<>();
        for(Integer i : sortedHand){
            if(! (deDupList.contains(i) ) ){
            deDupList.add(i);
            }
        }
        sortedHand = deDupList;
        return sortedHand;
    }

    /**
       Returns boolean for if the hand has three of a kind.
       * @param player the cards belonging to the player
       * @return boolean
    */
    private boolean isThreeOfAKind(ArrayList<Card> player) {
        if(isFullHouse(player)){
            return false;
        }

        ArrayList<Integer> sortedHand= sortHand(player);
        int tripleCounter=0;
        for(int i=0;i<player.size()-1;i++) {
            if(sortedHand.get(i)==sortedHand.get(i+1))
                tripleCounter++;
            else {
                if(tripleCounter==1)
                tripleCounter=0;
            }
        }
        if(tripleCounter==2)
            return true;
        else
            return false;
    }

    /**
       Returns boolean for if the hand has two pairs.
       * @param player the cards that belong to the player
       * @return boolean
    */
    private boolean isTwoPair(ArrayList<Card> player) {
        ArrayList<Integer> sortedHand=new ArrayList<Integer>();
        sortedHand=sortHand(player);
        int pair1Counter=0;
        int pair2Counter=0;
        int pair1Int=100;
        int pair2Int=100;
        for(int i=0;i<player.size()-1;i++) {
            if(sortedHand.get(i)==sortedHand.get(i+1)){
                if(sortedHand.get(i)==pair1Int || sortedHand.get(i)==pair2Int){
                    return false;
                }
                else if(pair1Counter==1){
                    pair2Counter++;
                    pair2Int=sortedHand.get(i);
                }
                else{
                    pair1Counter++;
                    pair1Int=sortedHand.get(i);
                }
            }
        }
        return(pair1Counter==1 && pair2Counter>=1);
    }

    /**
       Returns boolean for if the hand has only one pair.
       * @param player cards that belong to the player
       * @return boolean
    */
    private boolean isOnePair(ArrayList<Card> player) {
        ArrayList<Integer> sortedHand=new ArrayList<Integer>();
        sortedHand=sortHand(player);
        int pairCounter=0;
        for(int i=0;i<player.size()-1;i++) {
            if(sortedHand.get(i)==sortedHand.get(i+1))
            pairCounter++;
        }
        if(pairCounter==1)
            return true;
        else
            return false;
    }

    /**
     * Returns suit occurring the most in a hand
     * @return char representing the suit
     */
    private char getMostCommonSuit(ArrayList<Card> hand) {
        int heartsCounter = 0;
        int diamondsCounter = 0;
        int spadesCounter = 0;
        int clubsCounter = 0;

        for (int i = 0; i < hand.size(); i++) {
            switch (hand.get(i).getSuit().charAt(0)) {
                case 'H':
                    heartsCounter++;
                    break;
                case 'D':
                    diamondsCounter++;
                    break;
                case 'S':
                    spadesCounter++;
                    break;
                case 'C':
                    clubsCounter++;
                    break;
            }
        }

        int max_occurrences = Math.max(heartsCounter, Math.max(diamondsCounter, Math.max(spadesCounter, clubsCounter)));

        if (max_occurrences == heartsCounter) {
            return 'H';
        }
        else if (max_occurrences == diamondsCounter) {
            return 'D';
        }
        else if (max_occurrences == spadesCounter) {
            return 'S';
        }
        return 'C';
    }

    /**
     * Determines the better hand of two straight flushes
     * @return 1 if the player wins, 0 if the opponent wins, 2 if it is a tie
     */
    private int straightFlushTie() {
        ArrayList<Integer> sortedHand1 = sortHand(cardHand1);
        ArrayList<Integer> sortedHand2 = sortHand(cardHand2);
        char mostCommonSuit1 = getMostCommonSuit(cardHand1);
        char mostCommonSuit2 = getMostCommonSuit(cardHand2);
        int maxCardHand1 = 0;
        int maxCardHand2 = 0;

        for (int i = cardHand1.size() - 1; i >= 0; i--) {
            if (cardHand1.get(i).getSuit().charAt(0) == mostCommonSuit1 && cardHand1.get(i).getValue() > maxCardHand1) {
                maxCardHand1 = cardHand1.get(i).getValue();
            }
            if (cardHand2.get(i).getSuit().charAt(0) == mostCommonSuit2 && cardHand2.get(i).getValue() > maxCardHand2) {
                maxCardHand2 = cardHand2.get(i).getValue();
            }
        }

        if (maxCardHand1 > maxCardHand2)
            return 1;
        else if (maxCardHand2 > maxCardHand1)
            return 0;
        else
            return 2;
    }

    /**
     * Determines the better hand of two four of a kinds
     * @return 1 if the player wins, 0 if the opponent wins
     */
    private int fourOfAKindTie() {
        int fourOfAKind1 = 0;
        int fourOfAKind2 = 0;
        ArrayList<Integer> sortedHand1 = sortHand(cardHand1);
        ArrayList<Integer> sortedHand2 = sortHand(cardHand2);

        for (int i = 0; i < sortedHand1.size(); i++) {
            if (Collections.frequency(sortedHand1, sortedHand1.get(i)) == 4)
                fourOfAKind1 = sortedHand1.get(i);
            if (Collections.frequency(sortedHand2, sortedHand2.get(i)) == 4)
                fourOfAKind2 = sortedHand2.get(i);
        }

        if (fourOfAKind1 > fourOfAKind2)
            return 1;
        return 0;
    }

    /**
     * Determines the better hand of two full houses
     * @return 1 if the player wins, 0 if the opponent wins
     */
    private int fullHouseTie() {
        return threeOfAKindTie();
    }

    /**
     * Determines the better hand of two flushes
     * @return 1 if the player wins, 0 if the opponent wins, 2 if it is a tie
     */
    private int flushTie() {
        char mostCommonSuit1 = getMostCommonSuit(cardHand1);
        char mostCommonSuit2 = getMostCommonSuit(cardHand2);
        ArrayList<Integer> flushCards1 = new ArrayList<Integer>();
        ArrayList<Integer> flushCards2 = new ArrayList<Integer>();

        for (int i = 0; i < cardHand1.size(); i++) {
            if (cardHand1.get(i).getSuit().charAt(0) == mostCommonSuit1)
                flushCards1.add(cardHand1.get(i).getValue());
            if (cardHand2.get(i).getSuit().charAt(0) == mostCommonSuit2)
                flushCards2.add(cardHand2.get(i).getValue());
        }

        Collections.sort(flushCards1);
        Collections.sort(flushCards2);

        int flush1_index = flushCards1.size() - 1;
        int flush2_index = flushCards2.size() - 1;

        while ((flush1_index >= 0) && (flush2_index >= 0)) {
            if (flushCards1.get(flush1_index) > flushCards2.get(flush2_index)) {
                return 1;
            }
            else if (flushCards2.get(flush2_index) > flushCards1.get(flush1_index)) {
                return 0;
            }
            else {
                flush1_index--;
                flush2_index--;
            }
        }

        return 2; // players have exact same cards for flush
    }

    /**
     * Determines the better hand of two straights
     * @return 1 if the player wins, 0 if the opponent wins, 2 if it is a tie
     */
    private int straightTie() {
        ArrayList<Integer> sortedHand1 = sortHand(cardHand1);
        ArrayList<Integer> sortedHand2 = sortHand(cardHand2);
        removeDuplicates(sortedHand1);
        removeDuplicates(sortedHand2);
        int straightEndIndex1 = 0;
        int straightEndIndex2 = 0;

        for (int i = 0; i < sortedHand1.size() - 4; i++) {
            if(sortedHand1.get(i)==(sortedHand1.get(i + 1) - 1) &&
               sortedHand1.get(i)==(sortedHand1.get(i + 2) - 2) &&
               sortedHand1.get(i)==(sortedHand1.get(i + 3) - 3) &&
               sortedHand1.get(i)==(sortedHand1.get(i + 4) - 4))
                {
                straightEndIndex1 = i + 4;
                }
            if(sortedHand2.get(i)==(sortedHand2.get(i + 1) - 1) &&
               sortedHand2.get(i)==(sortedHand2.get(i + 2) - 2) &&
               sortedHand2.get(i)==(sortedHand2.get(i + 3) - 3) &&
               sortedHand2.get(i)==(sortedHand2.get(i + 4) - 4))
                {
                straightEndIndex2 = i + 4;
                }
        }

        if (sortedHand1.get(straightEndIndex1) > sortedHand2.get(straightEndIndex2))
            return 1;
        else if (sortedHand2.get(straightEndIndex2) > sortedHand2.get(straightEndIndex2))
            return 0;
        return 2;
    }

    /**
     * Determines the better hand of two three of a kinds
     * @return 1 if the player wins, 0 if the opponent wins, 2 if it is a tie
     */
    private int threeOfAKindTie() {
        int threeOfAKind1 = 0;
        int threeOfAKind2 = 0;
        ArrayList<Integer> sortedHand1 = sortHand(cardHand1);
        ArrayList<Integer> sortedHand2 = sortHand(cardHand2);

        for (int i = 0; i < sortedHand1.size(); i++) {
            if (Collections.frequency(sortedHand1, sortedHand1.get(i)) == 3 && sortedHand1.get(i) > threeOfAKind1)
                threeOfAKind1 = sortedHand1.get(i);
            if (Collections.frequency(sortedHand2, sortedHand2.get(i)) == 3 && sortedHand2.get(i) > threeOfAKind2)
                threeOfAKind2 = sortedHand2.get(i);
        }

        if (threeOfAKind1 > threeOfAKind2)
            return 1;
        return 0;
    }

    /**
     * Determines the better hand of two two pairs
     * @return 1 if the player wins, 0 if the opponent wins, 2 if it is a tie
     */
    private int twoPairTie() {
        ArrayList<Integer> sortedHand1 = sortHand(cardHand1);
        ArrayList<Integer> sortedHand2 = sortHand(cardHand2);
        ArrayList<Integer> pairsHand1 = new ArrayList<Integer>(); // each entry represents a pair (a 4 means a pair of 4's)
        ArrayList<Integer> pairsHand2 = new ArrayList<Integer>();

        for (int i = 0; i < sortedHand1.size(); i++) {
            if ((Collections.frequency(sortedHand1, sortedHand1.get(i)) == 2) && (!pairsHand1.contains(sortedHand1.get(i)))) {
                pairsHand1.add(sortedHand1.get(i));
            }
            if ((Collections.frequency(sortedHand2, sortedHand2.get(i)) == 2) && (!pairsHand2.contains(sortedHand2.get(i)))) {
                pairsHand2.add(sortedHand2.get(i));
            }
        }

        int pairsHand1_index = pairsHand1.size() - 1;
        int pairsHand2_index = pairsHand2.size() - 1;
        int maxNumPairs = 2; // only want to compare two highest pairs from each hand

        while (maxNumPairs > 0) {
            if (pairsHand1.get(pairsHand1_index) > pairsHand2.get(pairsHand2_index)) {
                return 1;
            }
            else if (pairsHand2.get(pairsHand2_index) > pairsHand1.get(pairsHand1_index)) {
                return 0;
            }
            else {
                pairsHand1_index--;
                pairsHand2_index--;
            }
        }

        // if both pairs are the same, check the fifth card in each hand
        int fifthCardHand1 = 0;
        int fifthCardHand2 = 0;

        for (int i = sortedHand1.size() - 1; i >= 0; i--) {
            if (!pairsHand1.contains(sortedHand1.get(i))) {
                fifthCardHand1 = sortedHand1.get(i);
            }
            if (!pairsHand2.contains(sortedHand2.get(i))) {
                fifthCardHand2 = sortedHand2.get(i);
            }
        }

        if (fifthCardHand1 > fifthCardHand2)
            return 1;
        else if (fifthCardHand2 > fifthCardHand1)
            return 0;
        return 2;
    }

    /**
     * Determines the better hand of two pairs
     * @return 1 if the player wins, 0 if the opponent wins, 2 if it is a tie
     */
    private int pairTie() {
        ArrayList<Integer> sortedHand1 = sortHand(cardHand1);
        ArrayList<Integer> sortedHand2 = sortHand(cardHand2);
        int pair1 = 0;
        int pair2 = 0;

        for (int i = sortedHand1.size() - 1; i >= 0; i--) {
            if ((Collections.frequency(sortedHand1, sortedHand1.get(i)) == 2) && (pair1 == 0)) {
                pair1 = sortedHand1.get(i);
            }
            if ((Collections.frequency(sortedHand2, sortedHand2.get(i)) == 2) && (pair2 == 0)) {
                pair2 = sortedHand2.get(i);
            }
        }

        if (pair1 > pair2)
            return 1;
        else if (pair2 > pair1)
            return 0;

        sortedHand1.remove(new Integer(pair1));
        sortedHand1.remove(new Integer(pair1));
        sortedHand2.remove(new Integer(pair2));
        sortedHand2.remove(new Integer(pair2));

        int hand1_index = sortedHand1.size() - 1;
        int hand2_index = sortedHand2.size() - 1;
        int cardsExamined = 0;

        while ((hand1_index >= 0) && (hand2_index >= 0) && (cardsExamined < 3)) {
            if (sortedHand1.get(hand1_index) > sortedHand2.get(hand2_index)) {
                return 1;
            }
            else if (sortedHand2.get(hand2_index) > sortedHand1.get(hand1_index)) {
                return 0;
            }
            else {
                hand1_index--;
                hand2_index--;
                cardsExamined++;
            }
        }

        return 2;
    }

    /**
     * Determines the winner when there is no clear hand using the high card
     * @return 1 if the player wins, 0 if the opponent wins, 2 if it is a tie
     */
    private int highCardTie() {
        ArrayList<Integer> sortedHand1 = sortHand(cardHand1);
        ArrayList<Integer> sortedHand2 = sortHand(cardHand2);

        for (int i = sortedHand1.size() - 1; i > sortedHand1.size() - 6; i--) {
            if (sortedHand1.get(i) > sortedHand2.get(i))
                return 1;
            else if (sortedHand2.get(i) > sortedHand1.get(i))
                return 0;
        }

        return 2;
    }
}

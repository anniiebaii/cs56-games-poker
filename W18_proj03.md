Annie Bai  Hyewon Seong

Suggested Issues
=================

1. **[Improve the ComputerAI algorithm](https://github.com/ucsb-cs56-projects/cs56-games-poker/issues/55) (300 pts)** - The Computer AI is very rudimentary. Its moves are decided from a series of if else statements and it’s very easy to figure out to make the computer forfeit every time with enough plays through the game. Improve the Algorithm, maybe by having the computer base its decisions on not just what the human player does, but also on the cards the AI possesses.
2. **[Over All-in bug] (https://github.com/ucsb-cs56-projects/cs56-games-poker/issues/74) (75 pts) ** - Fix the bug where, if you bid more chips than the opponent has, it doesn't take any chips away from the opponent if they lose. All their chips should be added to the pot.
3. ** [Create multiplayer client functionality] (https://github.com/ucsb-cs56-projects/cs56-games-poker/issues/69) (200 pts) ** - This assumes that the server is set up and that the game has been refactored such that multiplayer is possible (no longer assumes the game is Player vs. AI). This issue relates to how each user's client interacts with the server hosting the game.

New Issues
==========

4. ** [Refactor CompareHands.java] (https://github.com/ucsb-cs56-projects/cs56-games-poker/issues/83) (200 pts) ** - Currently the CompareHands.java doesn't calculate the winner when two hands are the same (ie: pair vs pair) and instead declares it a tie or says the player wins. Also figure out how to make it consider players that have folded and not calculate their hand when determining the winning hand.

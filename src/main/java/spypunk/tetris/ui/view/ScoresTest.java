package spypunk.tetris.ui.view;

import static org.junit.jupiter.api.Assertions.*;

class ScoresTest {

    @org.junit.jupiter.api.Test
    //Testing the add method for the high score table
    //sending 10 different players with different scores
    //checking if there has been any issue adding a player
    void add() {
        Scores s = new Scores();
        int minScore = 1000;
        for (int i = 1; i <= 10; i++) {
            s.add(new GameEntry("a", minScore + (i*10)));
        }

        for (GameEntry player: s.entities()) {
            assertNotNull(player);
        }
    }

    @org.junit.jupiter.api.Test
    //method below is used to display the highest score in the main page
    void highestScore() {
        Scores s = new Scores();

        String playerName = "User";
        int playerScore = 1000;

        s.add(new GameEntry("a", 1000));

        assertEquals(playerScore, s.highestScore().getScore());
    }

    @org.junit.jupiter.api.Test
    //method below is used to check if a user's score at the end of the game is enough for the player to be in the high score table
    //if it is, a form pops up for the player to enter his/her name
    //if there is a single player, it is expected for the method to return null
    //therefore, it shows the program there are empty slots and it is not important to check how high user's score is
    void lowestScore() {
        Scores s = new Scores();

        String playerName = "User";
        int playerScore = 100;

        s.add(new GameEntry("a", 100));

        assertNull(s.lowestScore().getScore());
    }
    
}
package spypunk.tetris.ui.view;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

public class TetrisScoresTest {

    private TetrisScoresView tsv;
    @BeforeEach                                         
    public void setUp(){
        tsv=new TetrisScoresView();
    }
    @Test
    public void testGetMin(){
        tsv.hashMap.put("A",2);
        tsv.hashMap.put("B",1);
        tsv.hashMap.put("C",3);
        assertEquals(1,tsv.getMinScore());
    }
    @Test
    public void testGetMinWithEmptyList(){
        //If list is empty then minimum score is 0.
        assertEquals(0,tsv.getMinScore());
    }
    @Test
    public void testPutScoreAndName(){
        //When the number of elements in the list is less than 5.
        tsv.putScoreAndName("A",5);
        assertTrue(tsv.hashMap.containsKey("A"));
    }
    @Test
    public void testPutScoreAndNameWithFullList(){
        //We expect that when list is full, if F is added then the element 
        // has minimum score is removed.
        tsv.hashMap.put("A",1);
        tsv.hashMap.put("B",2);
        tsv.hashMap.put("C",3);
        tsv.hashMap.put("D",4);
        tsv.hashMap.put("E",5);
        tsv.putScoreAndName("F",6);
        assertTrue(tsv.hashMap.containsKey("F"));
        assertFalse(tsv.hashMap.containsKey("A"));
    }
    @Test
    public void testIsFull(){
        //When the number of elements in the list is equal to 5.
        tsv.hashMap.put("A",1);
        tsv.hashMap.put("B",2);
        tsv.hashMap.put("C",3);
        tsv.hashMap.put("D",4);
        tsv.hashMap.put("E",5);
        assertTrue(tsv.isFull());
    }
    @Test
    public void testRemoveMinScoreElement(){
        tsv.hashMap.put("A",1);
        tsv.hashMap.put("B",2);
        tsv.hashMap.put("C",3);
        tsv.removeMinScoreElement();
        assertFalse(tsv.hashMap.containsKey("A"));
    }

}

package spypunk.tetris;

import org.junit.jupiter.api.Test;
import spypunk.tetris.model.Tetris;
import spypunk.tetris.model.TetrisInstance;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class AchievementTest {

    @Test
    void testAchievementNotUnlocked() {

        Tetris t = mock(Tetris.class);
        TetrisInstance ti = mock(TetrisInstance.class);

        assertEquals(ti.getAchievementCount(), 0);
        assertFalse(ti.isAchievementUnlocked_ROW());
        assertFalse(ti.isAchievementUnlocked_SCORE());

    }

    @Test
    void testAchievementSCORE() {

    }

}
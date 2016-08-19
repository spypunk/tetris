package spypunk.tetris.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import spypunk.tetris.factory.ShapeFactory;
import spypunk.tetris.model.Shape;
import spypunk.tetris.model.Tetris;
import spypunk.tetris.model.TetrisInstance;
import spypunk.tetris.model.TetrisInstance.State;

@RunWith(MockitoJUnitRunner.class)
public class TetrisServiceImplTest {

    @Mock
    private ShapeFactory shapeFactory;

    @Mock
    private Shape shape;

    private TetrisService tetrisService;

    @Before
    public void setUp() {
        Mockito.when(shapeFactory.createRandomShape()).thenReturn(shape);

        tetrisService = new TetrisServiceImpl(shapeFactory);
    }

    @Test
    public void testNewInstance() {
        final Tetris tetris = Tetris.Builder.instance().build();

        tetrisService.newInstance(tetris);

        final TetrisInstance tetrisInstance = tetris.getTetrisInstance();

        Assert.assertNotNull(tetrisInstance);
        Assert.assertNull(tetrisInstance.getCurrentShape());
        Assert.assertNull(tetrisInstance.getNextShape());
        Assert.assertEquals(48, tetrisInstance.getSpeed());
        Assert.assertEquals(State.NEW, tetrisInstance.getState());
    }
}

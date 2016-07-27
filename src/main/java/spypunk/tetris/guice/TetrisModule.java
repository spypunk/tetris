package spypunk.tetris.guice;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import com.google.inject.AbstractModule;

import spypunk.tetris.controller.TetrisController;
import spypunk.tetris.controller.TetrisControllerImpl;
import spypunk.tetris.factory.BlockImageFactory;
import spypunk.tetris.factory.BlockImageFactoryImpl;
import spypunk.tetris.factory.ContainerFactory;
import spypunk.tetris.factory.ContainerFactoryImpl;
import spypunk.tetris.factory.FontFactory;
import spypunk.tetris.factory.FontFactoryImpl;
import spypunk.tetris.factory.ImageFactory;
import spypunk.tetris.factory.ImageFactoryImpl;
import spypunk.tetris.factory.ShapeFactory;
import spypunk.tetris.factory.ShapeFactoryImpl;
import spypunk.tetris.factory.ShapeTypeFactory;
import spypunk.tetris.factory.ShapeTypeFactoryImpl;
import spypunk.tetris.factory.TetrisFactory;
import spypunk.tetris.factory.TetrisFactoryImpl;
import spypunk.tetris.service.TetrisService;
import spypunk.tetris.service.TetrisServiceImpl;
import spypunk.tetris.view.TetrisCanvas;
import spypunk.tetris.view.TetrisCanvasImpl;
import spypunk.tetris.view.TetrisFrame;
import spypunk.tetris.view.TetrisFrameImpl;
import spypunk.tetris.view.TetrisRenderer;
import spypunk.tetris.view.TetrisRendererImpl;

public class TetrisModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(ScheduledExecutorService.class).toInstance(Executors.newSingleThreadScheduledExecutor());
        bind(TetrisService.class).to(TetrisServiceImpl.class);
        bind(ShapeFactory.class).to(ShapeFactoryImpl.class);
        bind(TetrisController.class).to(TetrisControllerImpl.class);
        bind(TetrisFrame.class).to(TetrisFrameImpl.class);
        bind(TetrisCanvas.class).to(TetrisCanvasImpl.class);
        bind(TetrisRenderer.class).to(TetrisRendererImpl.class);
        bind(ImageFactory.class).to(ImageFactoryImpl.class);
        bind(BlockImageFactory.class).to(BlockImageFactoryImpl.class);
        bind(ShapeTypeFactory.class).to(ShapeTypeFactoryImpl.class);
        bind(TetrisFactory.class).to(TetrisFactoryImpl.class);
        bind(FontFactory.class).to(FontFactoryImpl.class);
        bind(ContainerFactory.class).to(ContainerFactoryImpl.class);
    }
}

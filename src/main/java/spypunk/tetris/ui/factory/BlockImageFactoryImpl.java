package spypunk.tetris.ui.factory;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;

import spypunk.tetris.exception.TetrisException;
import spypunk.tetris.factory.ShapeTypeFactory;
import spypunk.tetris.model.ShapeType;

@Singleton
public class BlockImageFactoryImpl implements BlockImageFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(BlockImageFactoryImpl.class);

    private static final File BLOCKS_FOLDER = new File("src/main/resources/img/blocks");

    private final Map<ShapeType, Image> imagesCache = Maps.newHashMap();

    @Inject
    public BlockImageFactoryImpl(ImageFactory imageFactory, ShapeTypeFactory shapeTypeFactory) {
        List<ShapeType> shapeTypes = shapeTypeFactory.createAll();

        try {
            for (ShapeType shapeType : shapeTypes) {
                String fileName = String.format("%s.png", shapeType.getId());
                Image image = imageFactory.createImage(new File(BLOCKS_FOLDER, fileName));
                imagesCache.put(shapeType, image);
            }
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new TetrisException(e);
        }
    }

    @Override
    public Image createBlockImage(ShapeType shapeType) {
        return imagesCache.get(shapeType);
    }

}

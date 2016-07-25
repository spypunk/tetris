package spypunk.tetris.util;

import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SwingUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(SwingUtils.class);

    private SwingUtils() {
        throw new IllegalAccessError();
    }

    public static void doInAWTThread(Runnable runnable) {
        doInAWTThread(runnable, false);
    }

    public static void doInAWTThread(Runnable runnable, boolean wait) {
        if (wait) {
            try {
                SwingUtilities.invokeAndWait(runnable);
            } catch (final InvocationTargetException e) {
                LOGGER.error(e.getMessage(), e);
            } catch (final InterruptedException e) {
                Thread.currentThread().interrupt();
                LOGGER.error(e.getMessage(), e);
            }
        } else {
            SwingUtilities.invokeLater(runnable);
        }
    }
}

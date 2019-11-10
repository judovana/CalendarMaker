package org.judovana.calendarmaker;

import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;

public class ScreenFinder {

    public static GraphicsDevice getCurrentScreen() {
        Point p = MouseInfo.getPointerInfo().getLocation();
        return getScreenOnCoords(p);

    }

    public static Rectangle getCurrentScreenSizeWithoutBounds() {
        try {
            Point p = MouseInfo.getPointerInfo().getLocation();
            return getScreenOnCoordsWithoutBounds(p);
        } catch (HeadlessException ex) {
            ex.printStackTrace();
            return new Rectangle(800, 600);
        }

    }

    public static void centerWindowsToCurrentScreen(Window w) {
        Rectangle bounds = getCurrentScreenSizeWithoutBounds();
        w.setLocation(bounds.x + (bounds.width - w.getWidth()) / 2,
                bounds.y + (bounds.height - w.getHeight()) / 2);

    }

    public static GraphicsDevice getScreenOnCoords(Point point) {
        GraphicsEnvironment e = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] devices = e.getScreenDevices();
        GraphicsDevice result = null;
        //now get the configuration(s) for each device
        for (GraphicsDevice device : devices) {
            //GraphicsConfiguration[] configurations = device.getConfigurations();
            //or?
            GraphicsConfiguration[] configurations = new GraphicsConfiguration[]{device.getDefaultConfiguration()};
            for (GraphicsConfiguration config : configurations) {
                Rectangle gcBounds = config.getBounds();
                if (gcBounds.contains(point)) {
                    result = device;
                }
            }
        }
        if (result == null) {
            //not found, get the default display
            result = e.getDefaultScreenDevice();
        }
        return result;
    }

    public static Rectangle getScreenOnCoordsWithoutBounds(Point p) {
        try {
            GraphicsDevice device = getScreenOnCoords(p);
            Rectangle screenSize = device.getDefaultConfiguration().getBounds();
            Insets insets = Toolkit.getDefaultToolkit().getScreenInsets(device.getDefaultConfiguration());
            return new Rectangle((int) screenSize.getX() + insets.left, (int) screenSize.getY() + insets.top, (int) screenSize.getWidth() - insets.left, (int) screenSize.getHeight() - insets.bottom);
        } catch (HeadlessException | IllegalArgumentException ex) {
            ex.printStackTrace();
            return new Rectangle(800, 600);
        }
    }

}

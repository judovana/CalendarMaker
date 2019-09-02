package org.judovana.calendarmaker;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class PhotoFrame {

    private final BufferedImage data;
    private final String src;

    public PhotoFrame(String src) throws IOException {
        this.data = ImageIO.read(new File(src));
        this.src=src;
    }


    public Image getImage() {
        return data;
    }
}

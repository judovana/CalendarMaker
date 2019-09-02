package org.judovana.calendarmaker;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

public class MainFrame extends JFrame {

    public MainFrame() throws IOException {
        this.setSize(800, 600);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        List<Date> dates = new ArrayList<>();

        for (int x = 0 ; x < 7; x++){
            dates.add(new Date(new Date().getTime()+24*60*60*1000*x));
        }

        dates = new ArrayList<>();
        for (int x = 0 ; x < 30; x++){
            dates.add(new Date(new Date().getTime()+24*60*60*1000*x));
        }
        this.add(new PageView(new CalendarPage(dates, new PhotoFrame(getRandomImage("/usr/share/backgrounds")))));
    }

    private static List<String> all ;
    private String getRandomImage(String... paths) throws IOException {
        if (all == null) {
            all = new ArrayList<>(1000);
            for (String s : paths) {
                Files.walkFileTree(new File(s).toPath(), new FileVisitor<Path>() {
                    @Override
                    public FileVisitResult preVisitDirectory(Path path, BasicFileAttributes basicFileAttributes) throws IOException {
                        return FileVisitResult.CONTINUE;
                    }

                    @Override
                    public FileVisitResult visitFile(Path path, BasicFileAttributes basicFileAttributes) throws IOException {
                        File f = path.toFile();
                        if (f.isFile()) {
                            String ff = path.toString().toLowerCase();
                            if (ff.endsWith(".png") || ff.endsWith(".jpg") || ff.endsWith(".jpeg") || ff.endsWith(".gif")) {
                                all.add(f.getAbsolutePath());
                            }
                        }
                        return FileVisitResult.CONTINUE;
                    }

                    @Override
                    public FileVisitResult visitFileFailed(Path path, IOException e) throws IOException {
                        return FileVisitResult.CONTINUE;
                    }

                    @Override
                    public FileVisitResult postVisitDirectory(Path path, IOException e) throws IOException {
                        return FileVisitResult.CONTINUE;
                    }
                });
            }
        }
        //we need next/prev feature
        Collections.sort(all);
        return all.get(new Random().nextInt(all.size()));
    }

}

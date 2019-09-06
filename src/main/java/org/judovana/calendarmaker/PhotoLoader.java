package org.judovana.calendarmaker;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class PhotoLoader {

    private final String[] paths;
    private List<String> all;

    public PhotoLoader(String... paths) throws IOException {
        this.paths = paths;
    }

    public String getRandomImage() throws IOException {
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

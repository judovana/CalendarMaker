package org.judovana.calendarmaker;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

public class PhotoLoader {

    private final String[] paths;
    private List<String> all;

    public PhotoLoader(String... paths) {
        this.paths = paths;
    }

    public PhotoLoader(Collection<String> l) {
        this(l.toArray(new String[l.size()]));
    }

    public String getRandomImage() throws IOException {
        initData();
        //we need next/prev feature
        Collections.sort(all);
        return all.get(new Random().nextInt(all.size()));
    }

    private void initData() throws IOException {
        if (all == null) {
            all = new ArrayList<>(1000);
            for (String s : paths) {
                Files.walkFileTree(new File(s).toPath(), new TreeSet<>(Arrays.asList(FileVisitOption.FOLLOW_LINKS)), Integer.MAX_VALUE, new FileVisitor<Path>() {
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
    }

    public String getPrev(String src) throws IOException {
        initData();
        //we need next/prev feature
        Collections.sort(all);
        File thisOne = new File(src);
        for (int i = 1; i < all.size(); i++) {
            File current = new File(all.get(i));
            if (current.equals(thisOne)) {
                return all.get(i - 1);
            }
        }
        return src;
    }

    public String getNext(String src) throws IOException {
        initData();
        //we need next/prev feature
        Collections.sort(all);
        File thisOne = new File(src);
        for (int i = 0; i < all.size() - 1; i++) {
            File current = new File(all.get(i));
            if (current.equals(thisOne)) {
                return all.get(i + 1);
            }
        }
        return src;
    }

    public List<String> getSrcs() {
        return Arrays.asList(paths);
    }
}

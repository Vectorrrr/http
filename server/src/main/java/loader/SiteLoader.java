package loader;

import holder.ProcessorHolder;
import org.apache.log4j.Logger;
import util.CheckerCorrectScheme;

import java.io.File;
import java.util.Scanner;

/**
 * @author Gladush Ivan
 * @since 08.04.16.
 */
public class SiteLoader {
    private static final Logger log = Logger.getLogger(SiteLoader.class);
    private static final String NOT_CORRECT_SCHEME = "Jar contains not correct scheme.";
    public static final String FILE_IN_DEFAULT_DIRECTORY = "%s/%s";
    private String pathToScheme = PropertyLoader.property("path.to.scheme");
    private String manifestName = PropertyLoader.property("manifest.name");
    private String tempDir = PropertyLoader.property("default.unpack.dir");

    public boolean download() {
        System.out.println("Input path to file");
        Scanner sc = new Scanner(System.in);
        String path = sc.nextLine();
        unPackJar(path);
        File files = new File(tempDir);
        for (File file : files.listFiles()) {
            if (manifestName.equals(file.getName())) {
                if (!CheckerCorrectScheme.isCorrectFile(file.getAbsolutePath(), pathToScheme)) {
                    log.error(NOT_CORRECT_SCHEME);
                    return false;
                }
                ProcessorHolder.addLoadNewFile(new File(String.format(FILE_IN_DEFAULT_DIRECTORY, tempDir, file.getName())));
                break;
            }

        }
//        deleteTempDir(new File(tempDir));
        return true;
    }

    private void unPackJar(String path) {
        File f = new File(tempDir);
        f.mkdirs();
        new UnPackJar().unpack(tempDir, path);
    }

    private void deleteTempDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                File f = new File(dir, children[i]);
                deleteTempDir(f);
            }
            dir.delete();
        } else dir.delete();
    }


    public static void main(String[] args) {
        System.out.println(new SiteLoader().download());
    }
}

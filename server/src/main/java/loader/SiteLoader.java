package loader;

import org.apache.log4j.Logger;
import util.CheckerCorrectScheme;

import java.io.File;

/**
 * This class takes a jar file and unzip it.
 * Checks for configuration. If the configuration is,
 * it begins to create handlers page copies, after
 * copies all pages in your store.
 * After processing deletes all files obtained by unpacking
 * @author Gladush Ivan
 * @since 08.04.16.
 */
public class SiteLoader {
    private static final Logger log = Logger.getLogger(SiteLoader.class);
    private static final String NOT_CORRECT_SCHEME = "Jar contains not correct scheme.";
    private static final String FILE_IN_DEFAULT_DIRECTORY = "%s/%s";
    private static final PropertyLoader PROPERTY_LOADER = PropertyLoader.getPropertyLoader("server.configuration.properties");
    private static final String FILE_FOR_NEW_PAGES = PROPERTY_LOADER.property("directory.for.site.pages");

    private String pathToScheme = PROPERTY_LOADER.property("path.to.scheme");
    private String manifestName = PROPERTY_LOADER.property("manifest.name");
    private String tempDir = PROPERTY_LOADER.property("default.unpack.dir");

    /**
     * The method downloads the site in memory of the server
     * receiving this website in the specified path. The method
     * returns whether successful or not loading
     * */
    public boolean download(String path) {
        String siteName = getName(path);
        unPackJar(path);
        File mainDir = new File(tempDir);
        File[] files = mainDir.listFiles();
        if (files == null) {
            return false;
        }
        if (loadProcessClass(files, siteName)) {
            loadPages(files, siteName);
        }
        deleteTempDir(mainDir);
        return true;
    }

    private String getName(String path) {
        String[] temp = path.split("/");
        return temp[temp.length - 1].split("\\.")[0];

    }

    private void loadPages(File[] files, String siteName) {
        File dirForSite = new File(String.format(FILE_IN_DEFAULT_DIRECTORY, FILE_FOR_NEW_PAGES, siteName));
        dirForSite.mkdirs();
        for (File file : files) {
            if ("txt".equals(file.getName().split("\\.")[1])) {
                file.renameTo(new File(String.format(FILE_IN_DEFAULT_DIRECTORY, dirForSite.getAbsolutePath(), file.getName())));
            }
        }
    }

    private Boolean loadProcessClass(File[] files, String siteName) {
        for (File file : files) {
            if (manifestName.equals(file.getName())) {
                if (!CheckerCorrectScheme.isCorrectFile(file.getAbsolutePath(), pathToScheme)) {
                    log.error(NOT_CORRECT_SCHEME);
                    return false;
                }
                ClassManager.addLoadNewFile(getFile(file), siteName);
                break;
            }

        }
        return true;
    }

    private File getFile(File file) {
        return new File(String.format(FILE_IN_DEFAULT_DIRECTORY, tempDir, file.getName()));
    }

    private void unPackJar(String path) {
        File f = new File(tempDir);
        f.mkdirs();
        new UnPackJar().unpack(tempDir, path);
    }

    private void deleteTempDir(File dir) {
        File[] files = dir.listFiles();
        if (files != null) {
            for (File children : files) {
                if (children.isDirectory()) {
                    deleteTempDir(children);
                }
                children.delete();
            }
        }
    }
}

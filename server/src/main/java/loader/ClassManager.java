package loader;

import holder.ProcessorHolder;
import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;
import page.PageProcessor;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.*;

/**
 * The class stores all the sites
 * that have been uploaded to the
 * server, after restarting the server,
 * it can restore the state to a state
 * equal before stopping
 * @author Gladush Ivan
 * @since 12.04.16.
 */
public class ClassManager {
    private static final Logger log = Logger.getLogger(ClassManager.class);
    private static final PropertyLoader PROPERTY_LOADER = PropertyLoader.getPropertyLoader("server.configuration.properties");

    private static final String EXCEPTION_IN_PARSE_FILE = "Exception in pars file %s";
    private static final String EXCEPTION_ADD_CLASS = "When you add class you have error %s";

    private static final String DIRECTORY_FOR_STORE_FILE = PROPERTY_LOADER.property("directory.for.store.class");
    private static final String PATH_IN_DIRECTORY = DIRECTORY_FOR_STORE_FILE + "/%s";
    private static final String DEFAULT_UNPACK_DIR = PROPERTY_LOADER.property("default.unpack.dir");
    private static final String MANIFEST_FILE_NAME = PROPERTY_LOADER.property("class.manager.file");

    private static final String CANT_CREATE_PROCESS_HOLDER = "Can init processor holder because %s";
    private static final String CANT_CREATE_FILE = "I can't create new file %s";

    private static final String SPACE = " ";
    private static final String ONE_LINE = "%s %s\n";
    private static final String SITE_URL = "/%s%s";
    private static final String CLASS = ".class";

    private static Integer countClass = 0;
    private static boolean manifestExist = false;
    private static String siteName;

    /**
     * Create a directory to save classes
     */
    static {
        new File(DIRECTORY_FOR_STORE_FILE).mkdirs();
    }

    /**
     * The method takes the name of the URL class,
     * which it processes and the content of the class.
     * Information is recorded in this class server
     * manifest, as well as the contents of the class
     * is stored in a specific location on the server
     */
    public static void addClass(String url, String className, byte[] classContent) {
        try {
            writeInManifest(url, className);
            writeClass(classContent);
        } catch (IOException e) {
            log.error(String.format(EXCEPTION_ADD_CLASS, e.getMessage()));
        }
    }

    /**
     * The method of reading the manifesto and on the basis
     * of its content adds new processes in ProcessorHolder
     */
    public static void initProcessorHolder() {
        try {
            if (createManifest()) {
                parseManifest();
            }
        } catch (IOException e) {
            log.error(String.format(CANT_CREATE_PROCESS_HOLDER, e.getMessage()));
        }
    }

    /**
     * Method writes class server class storage while increasing countClass 1
     */
    private static void writeClass(byte[] classContent) throws IOException {
        File f = new File(getPathInDirectory(getCountClass()));
        createFile(f);
        try (FileOutputStream fw = new FileOutputStream(f)) {
            fw.write(classContent);
        }
        countClass++;
    }

    private static String getCountClass() {
        return countClass + CLASS;
    }

    private static void createFile(File f) throws IOException {
        if (!f.createNewFile()) {
            log.warn(String.format(CANT_CREATE_FILE, f.getName()));
        }
    }

    private static File getManifestFile() {
        return new File(getPathInDirectory(MANIFEST_FILE_NAME));
    }

    private static void writeInManifest(String url, String className) throws IOException {
        createManifest();
        try (FileWriter fw = new FileWriter(getPathInDirectory(MANIFEST_FILE_NAME), true)) {
            fw.write(String.format(ONE_LINE, url, className));
        }
    }

    /**
     * Are returns to the calling file exists or not
     */
    private static boolean createManifest() throws IOException {
        if (!manifestExist) {
            File f = getManifestFile();
            if (!f.exists()) {
                manifestExist = true;
                createFile(f);
                return false;
            }
        }
        return true;
    }


    /**
     * The method parses the server and on the basis of
     * a manifesto and it adds objects ProcessorHolder
     */
    private static void parseManifest() throws IOException {
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(getManifestFile())))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] comp = line.split(SPACE);
                ProcessorHolder.addNewProcessor(comp[0], getInstance(comp[1]));
            }
        }
    }

    private static PageProcessor getInstance(String className) {
        PageProcessor o = new FileClassLoader().getInstance(getPathInDirectory(getCountClass()), className);
        countClass++;
        return o;
    }

    private static String getPathInDirectory(String name) {
        return String.format(PATH_IN_DIRECTORY, name);
    }

    /**
     * Method shows whether the class
     * is successfully loaded or not
     */
    public static boolean addLoadNewFile(File f, String siteNam) {
        siteName = siteNam;
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            SaxHandler handler = new SaxHandler();
            parser.parse(f, handler);
        } catch (Exception e) {
            log.error(String.format(EXCEPTION_IN_PARSE_FILE, e.getMessage()));
            return false;
        }
        return true;
    }

    private static final class SaxHandler extends DefaultHandler {
        public static final String EXCEPTION_CRETE_FILE = "I can't create instance class %s, because %s";
        public FileClassLoader fileClassLoader = new FileClassLoader();

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attrs) {
            if ("bean".equals(qName)) {
                String classPath = attrs.getValue("classPath");
                String url = String.format(SITE_URL, siteName, attrs.getValue("url"));
                try {
                    ProcessorHolder.addNewProcessor(url, getInstance(classPath));
                    ClassManager.addClass(url, classPath, fileClassLoader.fetchClassFromFS(getDefaultClassPath(classPath)));
                } catch (Exception e) {
                    log.error(String.format(EXCEPTION_CRETE_FILE, classPath, e.getMessage()));
                }
            }
        }

        private PageProcessor getInstance(String classPath) {
            return fileClassLoader.getInstance(getDefaultClassPath(classPath), classPath);
        }

        private String getDefaultClassPath(String classPath) {
            return String.format("%s/%s.class", DEFAULT_UNPACK_DIR, classPath);
        }
    }

}

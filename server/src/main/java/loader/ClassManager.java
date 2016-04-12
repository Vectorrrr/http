package loader;

import holder.ProcessorHolder;
import org.apache.log4j.Logger;
import page.PageProcessor;

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
    private static final String CLASS = ".class";
    private static final PropertyLoader PROPERTY_LOADER = PropertyLoader.getPropertyLoader("server.configuration.properties");
    private static final String DIRECTORY_FOR_STORE_FILE = PROPERTY_LOADER.property("directory.for.store.class");
    private static final String MANIFEST_FILE_NAME = PROPERTY_LOADER.property("class.manager.file");
    private static final String ONE_LINE = "%s %s\n";
    //todo rewrite name
    private static final String DIRECTORY_FORMAT = "%s/%s";
    private static final String EXCEPTION_ADD_CLASS = "When you add class you have error %s";
    private static final String CANT_CREATE_PROCESS_HOLDER = "Can init processor holder because %s";
    private static final String SPACE = " ";
    private static Integer countClass = 0;
    private static boolean manifestExist = false;

    private static final String CANT_CREATE_DEFAUlT_DIRRECTORY = "I can not create a directory to store the new classes";

    /**
     * Create a directory to save classes
     */
    static {
            new File(DIRECTORY_FOR_STORE_FILE).mkdirs();
    }

    public  static void addClass(String url, String className, byte[] classContent) {
        try {
            writeInManifest(url, className);
            writeClass(classContent);
        } catch (IOException e) {
            log.error(String.format(EXCEPTION_ADD_CLASS, e.getMessage()));
        }

    }

    private static void writeClass(byte[] classContent) throws IOException {
        File f = new File(String.format(DIRECTORY_FORMAT, DIRECTORY_FOR_STORE_FILE, countClass.toString() + CLASS));
        f.createNewFile();
        try (FileOutputStream fw = new FileOutputStream(f)) {
            fw.write(classContent);
        }
        countClass++;
    }
    /**
     * Are returns to the calling file exists or not
     * */
    private static boolean createManifest() throws IOException {
        boolean answer = true;
        if (!manifestExist) {
            File f = getManifestFile();
            if (!f.exists()) {
                answer = false;
                f.createNewFile();
            }
            manifestExist = true;
        }
        return answer;
    }

    private static File getManifestFile() {
        return new File(getFormat());
    }



    private static void writeInManifest(String url, String className) throws IOException {
        createManifest();
        try (FileWriter fw = new FileWriter(getFormat(), true)) {
            fw.write(String.format(ONE_LINE, url, className));
        }
    }
    private static String getFormat() {
        return String.format(DIRECTORY_FORMAT, DIRECTORY_FOR_STORE_FILE, MANIFEST_FILE_NAME);
    }

    public static void initProcessorHolder() {
        try {
            if (createManifest()) {
                parseManifest();
            }
        } catch (IOException e) {
            log.error(String.format(CANT_CREATE_PROCESS_HOLDER, e.getMessage()));
        }
    }

    private static void parseManifest() throws IOException {
        try(BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(new FileInputStream(getManifestFile())))){
            String line;
            while((line=bufferedReader.readLine())!=null){
                String[] comp=line.split(SPACE);
                ProcessorHolder.addNewProcessor(comp[0], getInstance(comp[1]));
            }
        }
    }

    private static PageProcessor getInstance(String className) {
        PageProcessor o= new FileClassLoader().getInstance(String.format(DIRECTORY_FORMAT,DIRECTORY_FOR_STORE_FILE,countClass.toString()+CLASS),className);
        countClass++;
        return o;
    }

}

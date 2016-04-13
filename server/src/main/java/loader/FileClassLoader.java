package loader;

import org.apache.log4j.Logger;
import page.PageProcessor;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Class lets you create a class by
 * loading the class information from the file.
 * @author Gladush Ivan
 * @since 11.04.16.
 */
public class FileClassLoader extends ClassLoader {
    private static final Logger log = Logger.getLogger(FileClassLoader.class);
    private static final String THE_LARGE_FILE = "The file %s you are trying to download is too large";
    private static final String EXCEPTION_READ_FILE = "Exception in read file %s";
    private static final String EXCEPTION_CREATE_INSTANCE = "I can't load file %s, because %s";


    public PageProcessor getInstance(String fileName, String className) {
        byte b[] = fetchClassFromFS(fileName);
        try {
            return (PageProcessor) defineClass(className, b, 0, b.length).newInstance();
        } catch (Exception e) {
            throw new IllegalArgumentException(String.format(EXCEPTION_CREATE_INSTANCE, fileName, e.getMessage()));
        }
    }


    /**
     * The method of reading the contents
     * of the file returning it as an array of bytes
     */
    public byte[] fetchClassFromFS(String path) {
        File f = new File(path);

        try (InputStream is = new FileInputStream(f)) {
            long length = f.length();
            if (length > Integer.MAX_VALUE) {
                log.warn(String.format(THE_LARGE_FILE, path));
                return new byte[0];
            }
            byte[] bytes = new byte[(int) length];
            int offset = 0;
            int numRead;
            while (offset < bytes.length
                    && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
                offset += numRead;
            }
            return bytes;

        } catch (IOException e) {
            log.error(String.format(EXCEPTION_READ_FILE, e.getMessage()));
        }
        return new byte[0];
    }
}

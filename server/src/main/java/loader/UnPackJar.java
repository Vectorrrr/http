package loader;

import org.apache.log4j.Logger;

import java.io.*;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * This file is intended to decompress jar archive.
 * He unpacks all the content from subfolders in
 * the specified folder
 * @author Gladush Ivan
 * @since 08.04.16.
 */
public class UnPackJar {
    private static final Logger log = Logger.getLogger(UnPackJar.class);
    private static final String EXCTRACTING_MESSAGE = "Extracting file: %s";
    private static final String UNPACK_ERROR = "When there is a file decompression error has occurred %s";
    private static final String ERROR_WRITE_FILE = "An error occurred while writing the file %s";

    private final int BUFFER = 2_048;
    private File destFile;

    public void unpack(String destinationDirectory, String nameJar) {
        try (JarFile jFile = new JarFile(new File(nameJar))) {
            File unzipDir = new File(destinationDirectory);
            Enumeration<JarEntry> jarFileEntries = jFile.entries();

            while (jarFileEntries.hasMoreElements()) {
                JarEntry entry = jarFileEntries.nextElement();
                String entryname = entry.getName();
                log.info(String.format(EXCTRACTING_MESSAGE, entryname));
                destFile = new File(unzipDir, entryname);


                if (!entry.isDirectory()) {
                    writeFile(jFile, entry,destinationDirectory);
                }
            }

        } catch (IOException e) {
            log.error(String.format(UNPACK_ERROR, e.getMessage()));
        }
    }

    private void writeFile(JarFile jFile, JarEntry entry,String destinationDirectory) {
        int currentByte;
        byte data[] = new byte[BUFFER];
        try (BufferedInputStream is = new BufferedInputStream(jFile.getInputStream(entry));
             FileOutputStream fos = new FileOutputStream(String.format("%s/%s",destinationDirectory,destFile.getName()));
             BufferedOutputStream dest = new BufferedOutputStream(fos, BUFFER)) {
            while ((currentByte = is.read(data, 0, BUFFER)) > 0) {
                dest.write(data, 0, currentByte);
            }
            dest.flush();
        } catch (IOException e) {
            log.error(String.format(ERROR_WRITE_FILE, e.getMessage()));

        }
    }
}


package holder;


import loader.ClassManager;
import loader.FileClassLoader;
import loader.PropertyLoader;
import loader.page.PageHolder;
import model.DefaultProcessHandler;
import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;
import page.PageProcessor;
import request.Request;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * The class reads all the files and the URL
 * of the processing of their properties and,
 * if necessary, returns a URL object classes
 * processor data
 * @author Gladush Ivan
 * @since 29.03.16.
 */
public class ProcessorHolder {
    private static final Logger log = Logger.getLogger(ProcessorHolder.class);
    private static final String EXCEPTION_IN_PARSE_FILE = "Exception in pars file %s";
    private static final PropertyLoader PROPERTY_LOADER = PropertyLoader.getPropertyLoader("server.configuration.properties");
    private static final String defaultDir = PROPERTY_LOADER.property("default.unpack.dir");
    private static final String SITE_URL = "/%s%s";

    private static Map<String, PageProcessor> processors = new HashMap<>();
    private static String siteName;

    public static String process(Request request) {
        String url = request.getUrl();
        PageProcessor processHandler = processors.get(url);
        if (processHandler == null) {
            return DefaultProcessHandler.notFound();
        }
        return processHandler.process(request, PageHolder.getPage(url));
    }

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
                String url=String.format(SITE_URL, siteName, attrs.getValue("url"));
                try {
                    processors.put(url, getInstance(classPath));
                    ClassManager.addClass(url,classPath,fileClassLoader.fetchClassFromFS(getDefaultClassPath(classPath)));
                } catch (Exception e) {
                    log.error(String.format(EXCEPTION_CRETE_FILE, classPath, e.getMessage()));
                }
            }
        }

        private PageProcessor getInstance(String classPath) {
            return fileClassLoader.getInstance(getDefaultClassPath(classPath),classPath);
        }

        private String getDefaultClassPath(String classPath) {
            return String.format("%s/%s.class",defaultDir,classPath);
        }
    }
    public static void addNewProcessor(String url,PageProcessor pageProcessor){
        processors.put(url,pageProcessor);
    }
}
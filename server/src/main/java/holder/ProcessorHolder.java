package holder;


import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;
import page.PageProcessor;

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
    private static final String EXCEPTION_CREATE_PROCESSOR_HOLDER = "I can't create file %s";
    private static final String EXCEPTION_CREATE_FILE = "I can't create new instance of class. Check exist it file. %s";
    private static final String NOT_FOUND_DEFUALT_URL = "I can not find a class to handle unknown URLs";
    public static final String DONT_FOUND_PROCESSOR = "I don't found processor %s";
    public static final String UNKNOWN_PROCESSOR = "I don't know this processor";
    public static final String EXCEPTION_IN_PARCE_FILE = "Exception in pars file %s";
    private static Map<String,PageProcessor> processors=new HashMap<>();



    public static boolean addLoadNewFile(File f) {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            SaxHandler handler = new SaxHandler();
            parser.parse(f, handler);
        } catch (Exception e) {
            log.error(String.format(EXCEPTION_IN_PARCE_FILE, e.getMessage()));
            return false;
        }
        return true;
    }

    public static PageProcessor getProcessor(String processor) {
        PageProcessor pageProcessor=processors.get(processor);
          if(pageProcessor!=null){
              return pageProcessor;
          }
        log.warn(String.format(DONT_FOUND_PROCESSOR,processor));
        throw new IllegalArgumentException(UNKNOWN_PROCESSOR);
    }


    private static final class SaxHandler extends DefaultHandler {

        public static final String EXCEPTION_CRETE_FILE = "I can't create instance class %s, because %s";

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attrs) {
            if ("bean".equals(qName)) {
                String classPath=attrs.getValue("classPath");
                try {
                    processors.put(attrs.getValue("url"), getInstance(classPath));
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    e.printStackTrace();
                    log.error(String.format(EXCEPTION_CRETE_FILE,classPath,e.getMessage()));
                }
            }
        }

        private PageProcessor getInstance(String classPath) throws Exception {
            ClassLoader.getSystemClassLoader().loadClass(classPath);
            return (PageProcessor) Class.forName(classPath).newInstance();
        }


    }
    public static void main(String[] args) throws Exception {
        System.out.println(new File("./").getAbsolutePath());
        System.out.println(System.getProperty("user.dir"));
        File file=new File("tempdir");
        for(File f:file.listFiles()){
            System.out.println(f.getName().split("\\.")[1]);
            if( f.getName().split("\\.")[1].equals("class")){
                Class.forName(f.getAbsolutePath().split("\\.")[0]).newInstance();
            }
            System.out.println(f);
        }
//        ClassLoader.getSystemClassLoader().loadClass("/home/igladush/IdeaProjects/http/server/target/classes/ExitPage");
         Class.forName("holder.ProcessorHolder").newInstance();
    }

}
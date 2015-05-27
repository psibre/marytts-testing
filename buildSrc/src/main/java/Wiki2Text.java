
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import java.io.*;
import edu.jhu.nlp.wikipedia.*;

public class Wiki2Text implements PageCallbackHandler {

    public String root_dir;
    public int page_id;
    
    public Wiki2Text(String area, String save_dir) {
        root_dir = save_dir + "/" + area;
        (new File(root_dir)).mkdirs();
        page_id = 1;
	}

    public String clean(String text) {
        String clean_text = text.replaceAll("&nbsp;", " ");
        clean_text = clean_text.replaceAll("^\\|.*", " ");
        clean_text = clean_text.replaceAll("==+([^=]+)==+", "section: $1");
        Pattern section_patt = Pattern.compile(".*?[0-9]{10}.*");
        return clean_text;
    }

    public void process(WikiPage page)
    {
        String text = clean(page.getText());
        if (text.startsWith("#REDIRECT"))
            return;
        
        try
        {
            BufferedWriter out = new BufferedWriter(new FileWriter(root_dir + "/page_" + page_id + ".txt"));
            out.write(text);
            out.close();
        }
        catch (IOException ex)
        {
            // FIXME: indicate the error!
        }
        finally
        {
            page_id++;
        }
    }
    
	public void extractLocale(File xml_file)
    {
        try
        {
            // Load the parser
            WikiXMLParser wxsp = WikiXMLParserFactory.getSAXParser(xml_file.getPath());
            
            // We are the handler
            wxsp.setPageCallback(this);

            // And we parse
            wxsp.parse();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        } 		
	}
}

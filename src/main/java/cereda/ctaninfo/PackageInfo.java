package cereda.ctaninfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.JOptionPane;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class PackageInfo {

    private String title;
    private String description;
    private List<String> sources;
    private List<String> documentation;
    private List<String> versions;
    private List<String> licenses;
    private List<String> copyright;
    private List<String> maintainers;
    private List<String> distributions;
    private List<String> topics;

    public void fetch(String name) {

        try {
            Document document = Jsoup.connect("http://ctan.org/pkg/".concat(name)).get();

            title = clean(document.getElementsByTag("h1").first().html());
            description = clean(document.getElementsByTag("p").first().html());

            Element table = document.getElementsByClass("entry").first();

            sources = getTableEntry(table, "Sources");
            documentation = getTableEntry(table, "Documentation");
            versions = getTableEntry(table, "Version");
            licenses = getTableEntry(table, "License");
            copyright = getTableEntry(table, "Copyright");
            maintainers = getTableEntry(table, "Maintainer");
            distributions = getTableEntry(table, "Contained in");
            topics = getTableEntry(table, "Topics");
        } catch (IOException ioe) {
            JOptionPane.showMessageDialog(null, "I'm sorry, something bad happened. (" + name + ")\n\n".concat(ioe.getMessage()), "Oh no!", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static String clean(String html) {
        html = html.replaceAll("&shy;", "");
        return Jsoup.parse(html).text();
    }

    private static List<String> getList(String text) {
        List<String> result = new ArrayList<String>();
        List<String> temp = Arrays.asList(text.split("<br />"));
        for (String t : temp) {
            result.add(clean(t));
        }
        return result;
    }

    private List<String> getTableEntry(Element table, String name) {
        List<String> result = new ArrayList<String>();
        Elements entries = table.getElementsByTag("tr");
        for (int i = 0; i < entries.size(); i++) {
            if (clean(entries.get(i).getElementsByTag("td").first().html()).equals(name)) {
                result = getList(entries.get(i).getElementsByTag("td").get(1).html());
                if (name.equals("Contained in")) {
                    if ((i + 1) < entries.size()) {
                        if (clean(entries.get(i + 1).getElementsByTag("td").first().html()).isEmpty()) {
                            result.add(clean(entries.get(i + 1).getElementsByTag("td").get(1).html()));
                        }
                    }
                }
                break;
            }
        }
        return result;
    }
}

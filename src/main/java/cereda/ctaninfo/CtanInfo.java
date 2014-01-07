package cereda.ctaninfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class CtanInfo {

    private List<PackageInfo> infos;

    public boolean fetch(String author) {
        String name = "001";
        try {
            Document document = Jsoup.connect("http://ctan.org/author/".concat(author)).get();
            Elements pkgs = document.getElementsByClass("dt");
            if (pkgs.isEmpty()) {
                JOptionPane.showMessageDialog(null, "I'm sorry, no packages found.", "Oh no!", JOptionPane.WARNING_MESSAGE);
            } else {

                infos = new ArrayList<PackageInfo>();
                for (Element pkg : pkgs) {
                    name = clean(pkg.html()).toLowerCase();
                    PackageInfo info = new PackageInfo();
                    info.fetch(name);
                    infos.add(info);
                }
            }
            return true;
        } catch (IOException ioe) {
            JOptionPane.showMessageDialog(null, "I'm sorry, something bad happened. (" + name + ")\n\n".concat(ioe.getMessage()), "Oh no!", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    private static String clean(String html) {
        html = html.replaceAll("&shy;", "");
        return Jsoup.parse(html).text();
    }

}

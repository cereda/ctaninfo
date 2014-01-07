package cereda.ctaninfo;

import com.thoughtworks.xstream.XStream;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JOptionPane;

public class Start {

    public static void main(String[] args) {
        String author = JOptionPane.showInputDialog("Type the author ID as seen in CTAN:");
        if ((author != null) && (!author.trim().isEmpty())) {
            CtanInfo ci = new CtanInfo();
            if (ci.fetch(author.trim())) {
                serialize(author.trim().concat(".xml"), ci);
                JOptionPane.showMessageDialog(null, "The metadata was extracted.", "Woohoo!", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private static void serialize(String filename, CtanInfo info) {
        XStream xstream = new XStream();
        xstream.alias("ctan", CtanInfo.class);
        xstream.alias("package", PackageInfo.class);
        xstream.aliasField("packages", CtanInfo.class, "infos");
        FileWriter fw;
        try {
            fw = new FileWriter(filename);
            xstream.toXML(info, fw);
        } catch (IOException ioe) {
            JOptionPane.showMessageDialog(null, "I'm sorry, something bad happened.\n\n".concat(ioe.getMessage()), "Oh no!", JOptionPane.ERROR_MESSAGE);
        }
    }

}

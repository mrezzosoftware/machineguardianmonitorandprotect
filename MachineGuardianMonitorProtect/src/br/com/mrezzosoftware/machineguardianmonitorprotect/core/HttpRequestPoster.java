package br.com.mrezzosoftware.machineguardianmonitorprotect.core;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.*;
import java.net.URLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;

public class HttpRequestPoster {

    public static void main(String args[]) {
        try {

            //URL url = new URL("http://localhost/geo/geo.html");
            URL url = new URL("http://google.com");
            URLConnection con = (URLConnection) url.openConnection();

            con.setDoInput(true);
            con.setDoOutput(true);
            con.setUseCaches(false);
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            DataOutputStream printout = new DataOutputStream(con.getOutputStream());

            String content = "email=jad939933@hotmail.com&pass=577383";
            //printout.writeBytes(content);
            printout.flush();
            printout.close();

            try {
                Thread.sleep(10000);
            } catch (InterruptedException ex) {
                Logger.getLogger(HttpRequestPoster.class.getName()).log(Level.SEVERE, null, ex);
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            System.out.println("sb.toString()" + sb.toString());
            String htmlString = sb.toString();


            JEditorPane jEditorPane = new JEditorPane();

            // make it read-only
            jEditorPane.setEditable(false);

            // create a scrollpane; modify its attributes as desired
            JScrollPane scrollPane = new JScrollPane(jEditorPane);

            // add an html editor kit
            HTMLEditorKit kit = new HTMLEditorKit();
            jEditorPane.setEditorKit(kit);

            // add some styles to the html
            StyleSheet styleSheet = kit.getStyleSheet();
            styleSheet.addRule("body {color:#000; font-family:times; margin: 4px; }");
            styleSheet.addRule("h1 {color: blue;}");
            styleSheet.addRule("h2 {color: #ff0000;}");
            styleSheet.addRule("pre {font : 10px monaco; color : black; background-color : #fafafa; }");

            // create some simple html as a string
//        String htmlString = "<html>\n"
//                          + "<body>\n"
//                          + "<h1>Welcome!</h1>\n"
//                          + "<h2>This is an H2 header</h2>\n"
//                          + "<p>This is some sample text</p>\n"
//                          + "<p><a href=\"http://devdaily.com/blog/\">devdaily blog</a></p>\n"
//                          + "</body>\n";

            // create a document, set it on the jeditorpane, then add the html
            Document doc = kit.createDefaultDocument();
            jEditorPane.setDocument(doc);
            jEditorPane.setText(htmlString);

            // now add it all to a frame
            JFrame j = new JFrame("HtmlEditorKit Test");
            j.getContentPane().add(scrollPane, BorderLayout.CENTER);

            // make it easy to close the application
            j.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            // display the frame
            j.setSize(new Dimension(300, 200));

            // pack it, if you prefer
            //j.pack();

            // center the jframe, then make it visible
            j.setLocationRelativeTo(null);
            j.setVisible(true);


        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
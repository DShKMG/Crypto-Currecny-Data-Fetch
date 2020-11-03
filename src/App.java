import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.*;

/*PLEASE ADD THE LIBRARY FILE LIB FOLDER INSIDE THE PROJECT IN ORDER TO RUN THE SPECIFIC LIBRARY DEPENDENT PACKAGES BELOW*/
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTableCell;

/*PLEASE NOTE THAT ALL FILES UNDER THE  LIB DIRECTORY IN THE LIBRARY FOLDER HAS TO BE INCLUDED*/
public class App {

    // function to sort hashmap by values ----> Taken from geeksforgeeks
    public static HashMap<String, Float> sortByValue(HashMap<String, Float> hm) {
        // Create a list from elements of HashMap
        List<Map.Entry<String, Float>> list = new LinkedList<Map.Entry<String, Float>>(hm.entrySet());

        // Sort the list
        Collections.sort(list, new Comparator<Map.Entry<String, Float>>() {
            public int compare(Map.Entry<String, Float> o1, Map.Entry<String, Float> o2) {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });

        // put data from sorted list to hashmap
        HashMap<String, Float> temp = new LinkedHashMap<String, Float>();
        for (Map.Entry<String, Float> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }

    public static void main(String[] args) throws FailingHttpStatusCodeException, MalformedURLException, IOException {
        System.out.println("This program is going to give you 3 output file where you can find details in there\n");
        System.out.println(
                "File names are :\n1 - Fulltable.TXT\n2 - Sorted Symbols.TXT\n3 - Circulating Supply Sort.TXT\n");
        String url = "https://coinmarketcap.com/all/views/all/";
        WebClient wc = new WebClient();
        wc.getOptions().setUseInsecureSSL(true);
        wc.getOptions().setCssEnabled(false);
        wc.getOptions().setJavaScriptEnabled(false);
        HtmlPage pg = wc.getPage(url);
        System.out.println("Fetched data from : " + pg.getTitleText());
        HtmlTableCell c;
        HtmlAnchor a;
        HtmlDivision d;
        List<Object> temp = pg.getByXPath("//tr/td");
        System.out.println("Total Size of template : " + temp.size());
        String fulline = "";
        String sharecoin = "";
        File fobj1 = new File("Outputs\\Fulltable.TXT");
        File fobj2 = new File("Outputs\\Sorted Symbols.TXT");
        File fobj3 = new File("Outputs\\Circulating Supply Sort.TXT");
        if (fobj1.exists())
            fobj1.delete();
        if (fobj2.exists())
            fobj2.delete();
        if (fobj3.exists())
            fobj3.delete();
        fobj1.createNewFile();
        fobj2.createNewFile();
        fobj3.createNewFile();
        PrintWriter pw1 = new PrintWriter(fobj1);
        PrintWriter pw2 = new PrintWriter(fobj2);
        PrintWriter pw3 = new PrintWriter(fobj3);
        String[] name = new String[200];
        String[] symbolCoin = new String[200];
        String[] cs = new String[200];
        double[] circulatingsupply = new double[200];
        HashMap<String, Float> hm = new HashMap<String, Float>();
        int j = 0;
        pw1.println("Name\tSymbol\t\t\t\tCirculating Supply");
        pw1.flush();
        for (int i = 0; i < 2200; i += 11) {

            fulline = "";
            c = (HtmlTableCell) temp.get(i + 1);
            a = (HtmlAnchor) c.getChildNodes().get(0).getChildNodes().get(1);
            fulline += a.asText() + "\t"; // SAVES THE COIN NAME
            name[j] = a.asText();

            c = (HtmlTableCell) temp.get(i + 2);
            HtmlDivision o = (HtmlDivision) c.getChildNodes().get(0);
            fulline += o.asText() + "\t\t\t"; // SAVES THE COIN SYMBOL
            symbolCoin[j] = o.asText(); // Coin Symbol is saved to make alphabetical list

            c = (HtmlTableCell) temp.get(i + 5);
            d = (HtmlDivision) c.getChildNodes().get(0);
            fulline += d.asText() + "\t\t\t\t"; // SAVES THE Circulating Supply
            String str_cs = d.asText();
            str_cs = str_cs.replaceAll("[^\\d.]", "");
            str_cs = str_cs.replaceAll(",", "");
            // System.out.println(str_cs+"\n"); //In Order To check if we get CS values.
            double supplyval = Double.parseDouble(str_cs); // Converts string value of CS to double to sort it
            circulatingsupply[j] = supplyval; // Adding supplyval inside the array value to sort it

            /// Iterator and File Saver
            pw1.println(fulline);
            pw1.flush();
            j++;

        }

        // Sorting for the 3rd task by getting index sorting according to the
        // circulatingsupply array
        Integer[] indexes = IntStream.range(0, circulatingsupply.length).boxed().toArray(Integer[]::new);
        Arrays.sort(indexes, Comparator.<Integer>comparingDouble(i -> circulatingsupply[i]));

        for (int i = 0; i < cs.length; i++) {
            cs[i] = (circulatingsupply[indexes[i]] + " - " + name[indexes[i]] + " - Index Number : " + indexes[i]);
            // System.out.println(circulatingsupply[indexes[i]] + " - " + indexes[i] + " - "
            // + name[indexes[i]]); //Control
        }

        Arrays.sort(name);
        Arrays.sort(symbolCoin);
        HashMap<String, Float> hm0 = sortByValue(hm);
        for (int i = 0; i < 200; i++) {
            pw1.println(name[i]);
            pw1.flush();
        }

        HashMap<String, Float> hm1 = sortByValue(hm);
        for (int i = 0; i < 200; i++) {
            pw2.println(symbolCoin[i]);
            pw2.flush();
        }

        HashMap<String, Float> hm2 = sortByValue(hm);
        for (int i = 0; i < 200; i++) {
            // System.out.println(shares[j]);
            pw3.println(cs[i]);
            pw3.flush();
        }
    }
}

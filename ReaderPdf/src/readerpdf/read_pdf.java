
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.PDFTextStripperByArea;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class read_pdf {

    public List<String> lines = new ArrayList<String>();
    public String path_name = "C:\\Users\\sabah\\Desktop\\Muhammed\\dosyalar";
    public String path = "\\";
    static List<String> üniversiteler = new ArrayList<String>();
    static List<String> üniversiteler2 = new ArrayList<String>();
    static List<String> mailler = new ArrayList<String>();

    public static void main(String[] args) throws JSONException {

        read_pdf a = new read_pdf();
        a.get_filename();

        for (int i = 0; i < üniversiteler.size(); i++) {

            System.out.println(i + "  " + üniversiteler.get(i));

        }
        System.out.println("");
        System.out.println("");
        System.out.println("");
        System.out.println("");
        for (int i = 0; i < mailler.size(); i++) {
            System.out.println(i + "  " + mailler.get(i));
        }
        System.out.println("");
        System.out.println("");
        System.out.println("");
        System.out.println("");
        int sayac = 0;
        boolean kontrol = true;

        int i = 0;
        while (kontrol) {
            for (int j = 0; j < üniversiteler.size(); j++) {
                if (üniversiteler.get(i).equals(üniversiteler.get(j))) {
                    sayac++;

                }
            }
            String üni_deger = üniversiteler.get(i) + " yayin_sayisi:" + sayac;
            üniversiteler2.add(üni_deger);
            i++;
            sayac = 0;
            if (i == üniversiteler.size()) {
                kontrol = false;
            }
        }
        LinkedHashSet<String> hashSet = new LinkedHashSet<>(üniversiteler2);
        ArrayList<String> kopya_olmayan_sonuclar = new ArrayList<>(hashSet);

        System.out.println("");
        System.out.println("");

        System.out.println("");
        System.out.println("");
        for (int j = 0; j < kopya_olmayan_sonuclar.size(); j++) {

            System.out.println(kopya_olmayan_sonuclar.get(j));

        }
        System.out.println("");
        System.out.println("");

        System.out.println("");
        System.out.println("");

        Scanner k = new Scanner(System.in);
        int idler[] = new int[kopya_olmayan_sonuclar.size()];
        for (int j = 0; j < kopya_olmayan_sonuclar.size(); j++) {
            System.out.println(kopya_olmayan_sonuclar.get(j) + " nin id sini giriniz");
            int giriş = k.nextInt();
            idler[j] = giriş;
        }
        System.out.println("Lütfen bir id giriniz");
        int gir = k.nextInt();

        String sonuc = idsorgulama(gir, idler, kopya_olmayan_sonuclar);
        System.out.println(sonuc);
        
        String jsonDataString = "{\"results\":[{\"PDF\":\"ÜNİVERSİTE\",\"YAZAR MAİLLERİ\":\"YAYIN SAYILARI\" }, { \"PDF\":\"ÜNİVERSİTE\", \"YAZAR MAİLLERİ\":\"YAYIN SAYILARI\"}]}";
        JSONObject mainObject = new JSONObject(jsonDataString);
        JSONObject valuesObject = new JSONObject();
        JSONArray list = new JSONArray();

        valuesObject.put("ÜNİVERSİTELER", üniversiteler);
        valuesObject.put("MAİLLLER", mailler);
        valuesObject.put("YAYIN SAYISI", kopya_olmayan_sonuclar);

        list.put(valuesObject);
        mainObject.accumulate("values", list);
        System.out.println(mainObject);

    }

    public static String idsorgulama(int veri, int id[], ArrayList<String> üniler) {
        for (int i = 0; i < id.length; i++) {
            if (veri == id[i]) {
                return üniler.get(i);

            }

        }
        return "Böyle bir id yok";
    }

    public void get_filename() {
        try {
            File folder = new File(path_name);
            File[] listOfFiles = folder.listFiles();

            for (int i = 0; i < listOfFiles.length; i++) {
                if (listOfFiles[i].isFile()) {
                    find_university_name(listOfFiles[i].getName());
                    mail_name(listOfFiles[i].getName());
//                    
                } else if (listOfFiles[i].isDirectory()) {
//                    
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void find_university_name(String filename) {
        try {
            String ifade = "0";
            String text = read_pdf(path_name + path + filename);

            String[] lines = text.split(System.getProperty("line.separator"));

            for (int i = 0; i < lines.length; i++) {
                String line = lines[i];
                if (line.contains("ÜNİVERSİTESİ") || line.contains("niversitesi")) {
                    ifade = line;
                    break;
                }
            }

            for (int i = 0; i < ifade.split(",").length; i++) {
                String temp = ifade.split(",")[i];
                if (temp.contains("ÜNİVERSİTESİ") || temp.contains("niversitesi")) {
                    temp = temp.replace("*", "");
                    if (üniversiteler.contains(temp)) {
                        System.out.println(temp + " üniversitesi listede var");
                    }
                    üniversiteler.add(temp.trim().toUpperCase().replace(" – ", "-"));
                }

            }

        } catch (Exception e) {
        }
    }

    public void mail_name(String filename) {
        try {

            String ifade = "0";
            String text = read_pdf(path_name + path + filename);

            String[] lines = text.split(System.getProperty("line.separator"));
            String temp = "1";

            for (int i = 0; i < lines.length; i++) {
                String line = lines[i];
                if (line.contains(".com")) {
                    ifade = line;

                    for (int j = 0; j < ifade.split(",").length; j++) {
                        temp = ifade.split(",")[j];
                        if (temp.contains(".com")) {

                            mailler.add(temp.trim());
                        }
                    }

                }

            }
        } catch (Exception e) {
        }
    }

    public String read_pdf(String pdf_local_adress) {
        String text = "0";
        try {

            PDDocument document = null;
            try {
                File local_full_path = new File(pdf_local_adress);
                // System.out.println("f = " + f);

                try {

                    document = PDDocument.load(local_full_path);
                    document.getClass();

                } catch (Exception e) {

                }
                PDFTextStripperByArea stripper = new PDFTextStripperByArea();
                stripper.setSortByPosition(true);
                PDFTextStripper stripper2 = new PDFTextStripper();

                if (document != null) {
                    try {
                        text = stripper2.getText(document);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    text = "0";
                }
            } catch (Exception e) {
                System.out.println("PDF Okuma PROBLEMİ");
            }
            document.close();
        } catch (Exception e) {
        }
        return text;
    }

    public void read_line(String file_path) {
        try {
            PDDocument document = null;
            String fileName = file_path;
            try {
                document = PDDocument.load(new File(fileName));
                PDFTextStripper stripper = new PDFTextStripper();
                stripper.setSortByPosition(true);
                stripper.setStartPage(0);
                stripper.setEndPage(document.getNumberOfPages());

                Writer dummy = new OutputStreamWriter(new ByteArrayOutputStream());
                stripper.writeText(document, dummy);
                System.out.println(lines.size());
                // print lines
                for (String line : lines) {
                    System.out.println(line);
                }
            } finally {
                if (document != null) {
                    document.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

import javafx.util.Pair;
import sun.misc.Regexp;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    public static void main(String[] args) {

        long startTime = System.currentTimeMillis();

        ReadFile readFile = new ReadFile();
        Parser parser = new Parser();

        ArrayList<File> files = new ArrayList<>();
        readFile.getListOfAllFiles("C:\\Users\\USER\\Desktop\\corpus\\corpus", files);

        //ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        for (File file: files) {
            ArrayList<Pair<String, String>> pairs = readFile.getDocsFromFile(file);

            for (Pair p: pairs) {
                //executorService.execute(new Runnable() {
                    //public void run() {
                        //List<String> stringList = parser.parseString((String)p.getValue());
                char[] docArray = ((String)p.getValue()).toCharArray();
                List<String> stringList = parser.parse(docArray);
                //System.out.println("HI");
                    //}
                //});
            }

        }

        //executorService.shutdown();

        long stopTime = System.currentTimeMillis();
        System.out.println(stopTime - startTime);

    }

}

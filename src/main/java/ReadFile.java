import javafx.util.Pair;

import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

public class ReadFile {

    public void getListOfAllFiles(String directoryName, ArrayList<File> files) {

        File directory = new File(directoryName);

        //get all the files from a directory in a recursive way
        File[] fList = directory.listFiles();
        assert fList != null;
        for (File file : fList) {
            if (file.isFile()) {
                files.add(file);
            } else if (file.isDirectory()) {
                getListOfAllFiles(file.getAbsolutePath(), files);
            }
        }
    }

    public ArrayList<Pair<String, String>> getDocsFromFile(File file){

        ArrayList<Pair<String, String>> pairs = new ArrayList<>();

        try(BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line = br.readLine();

            while (line != null) {

                String docNo;
                StringBuilder text = new StringBuilder();

                //check if DOCNO
                if (line.contains("<DOCNO>"))
                {
                    //new doc
                    docNo = line.substring(7, line.length() - 7);
                    line = br.readLine();

                    while (line != null){

                        //looking for text
                        if (line.contains("<TEXT>")){

                            line = br.readLine();

                            //reading all the text
                            while (line != null && !line.contains("</TEXT>")) {

                                text.append(line);
                                line = br.readLine();

                            }

                            break;

                        }

                        line = br.readLine();

                    }

                    //add to pairs
                    pairs.add(new Pair<>(docNo, text.toString()));
                }

                line = br.readLine();
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

        return pairs;

    }

    public static void main(String[] args) {

        long startTime = System.currentTimeMillis();

        ReadFile readFile = new ReadFile();

        ArrayList<File> files = new ArrayList<>();
        readFile.getListOfAllFiles("C:\\corpus\\corpus", files);

        int filesSize = files.size();
        for (int i = 0; i < filesSize; i++) {
            File file = files.get(i);
            ArrayList<Pair<String, ArrayList<String>>> pairs = readFile.getDocsFromFile2(file);
        }

        long stopTime = System.currentTimeMillis();
        System.out.println(stopTime - startTime);

    }

    /*
    public void getDocs(File file) throws IOException {

        final FileChannel      channel;
        final MappedByteBuffer buffer;
        FileInputStream        fin;

        fin     = new FileInputStream(file);
        channel = fin.getChannel();
        buffer  = channel.map(FileChannel.MapMode.READ_ONLY, 0, file.length());

    }
    */

    public ArrayList<Pair<String, ArrayList<String>>> getDocsFromFile2(File file){

        ArrayList<Pair<String, ArrayList<String>>> pairs = new ArrayList<>();
        ArrayList<String> doc = new ArrayList<>();

        try(BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line = br.readLine();

            while (line != null) {

                String docNo;
                //StringBuilder text = new StringBuilder();

                //check if DOCNO
                if (line.indexOf("<DOCNO>") != -1)
                {
                    //new doc
                    docNo = line.substring(7, line.length() - 7);
                    doc.clear();
                    line = br.readLine();

                    while (line != null){

                        //looking for text
                        if (line.indexOf("<TEXT>") != -1){

                            line = br.readLine();

                            //reading all the text
                            while (line != null && line.indexOf("</TEXT>") != -1) {

                                doc.add(line);
                                line = br.readLine();

                            }

                            break;

                        }

                        line = br.readLine();

                    }

                    //add to pairs
                    pairs.add(new Pair<>(docNo, doc));
                }

                line = br.readLine();
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

        return pairs;

    }

}

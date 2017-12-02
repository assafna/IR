import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {

    private HashMap<String, String> dates;
    private char[] docArray;
    private int index = 0;
    private int arrayLength;

    public Parser() {

        dates = new HashMap<>();

        dates.put("January", "01");
        dates.put("February", "02");
        dates.put("March", "03");
        dates.put("April", "04");
        dates.put("May", "05");
        dates.put("June", "06");
        dates.put("July", "07");
        dates.put("August", "08");
        dates.put("September", "09");
        dates.put("October", "10");
        dates.put("November", "11");
        dates.put("December", "12");

        dates.put("january", "01");
        dates.put("february", "02");
        dates.put("march", "03");
        dates.put("april", "04");
        dates.put("may", "05");
        dates.put("june", "06");
        dates.put("july", "07");
        dates.put("august", "08");
        dates.put("september", "09");
        dates.put("october", "10");
        dates.put("november", "11");
        dates.put("december", "12");

        dates.put("JANUARY", "01");
        dates.put("FEBRUARY", "02");
        dates.put("MARCH", "03");
        dates.put("APRIL", "04");
        dates.put("MAY", "05");
        dates.put("JUNE", "06");
        dates.put("JULY", "07");
        dates.put("AUGUST", "08");
        dates.put("SEPTEMBER", "09");
        dates.put("OCTOBER", "10");
        dates.put("NOVEMBER", "11");
        dates.put("DECEMBER", "12");

        dates.put("Jan", "01");
        dates.put("Feb", "02");
        dates.put("Mar", "03");
        dates.put("Apr", "04");
        dates.put("May", "05");
        dates.put("Jun", "06");
        dates.put("Jul", "07");
        dates.put("Aug", "08");
        dates.put("Sep", "09");
        dates.put("Oct", "10");
        dates.put("Nov", "11");
        dates.put("Dec", "12");

        dates.put("jan", "01");
        dates.put("feb", "02");
        dates.put("mar", "03");
        dates.put("apr", "04");
        dates.put("may", "05");
        dates.put("jun", "06");
        dates.put("jul", "07");
        dates.put("aug", "08");
        dates.put("sep", "09");
        dates.put("oct", "10");
        dates.put("nov", "11");
        dates.put("dec", "12");

        dates.put("JAN", "01");
        dates.put("FEB", "02");
        dates.put("MAR", "03");
        dates.put("APR", "04");
        dates.put("MAY", "05");
        dates.put("JUN", "06");
        dates.put("JUL", "07");
        dates.put("AUG", "08");
        dates.put("SEP", "09");
        dates.put("OCT", "10");
        dates.put("NOV", "11");
        dates.put("DEC", "12");

    }


    /**
     * parse the doc array according to the given rules
     * @param array char array to parse
     * @return terms in the doc
     */
    public ArrayList<String> parse(char[] array){
        index=0;
        docArray = array;
        arrayLength = docArray.length;
        ArrayList<String> terms = new ArrayList<>();
        int arrayLength = docArray.length;
        String term;
        while(index < arrayLength){
            if(isDigit(docArray[index])) {
                term = findNumber();
                terms.add(term);
                skipWhiteSpace();
                if(index < arrayLength && isPercent())
                    terms.add(term + " percent");
            }
            else if(isCapitalLetter())
                terms.add(findExpressions());

            index++;

        }
        return terms;

    }

    /**
     * find the number from the given array. tThe number can contain the characters: 0-9, ',, '.'
     * @return string that represents the number
     */
    private String findNumber(){
        boolean isNumber = true;
        boolean isDecimalNumber = false;
        StringBuilder str = new StringBuilder();

        //not decimal numbers
        while (index < arrayLength && isNumber && !isDecimalNumber) {
            while (index < arrayLength && isDigit(docArray[index])) {
                str.append(docArray[index]);
                index++;
            }

            if (index < arrayLength && isComma(docArray[index])) {
                index++;
            }
            else
                break;
        }

        //decimal numbers
        if (index < arrayLength && isDot(docArray[index]) && index < arrayLength-1 && isDigit(docArray[index+1])) {
            str.append(docArray[index]);
            index++;
            for (int i = 0; i < 3 && index < arrayLength; i++) {
                if (isDigit(docArray[index])) {
                    isDecimalNumber = true;
                    str.append(docArray[index]);
                    index++;
                }
                else
                    break;
            }

            //skip the next digits
            while (index < arrayLength && isDigit(docArray[index]))
                index++;

            if (isDecimalNumber) {
                DecimalFormat df = new DecimalFormat("#.##");
                double d = Double.parseDouble(str.toString());
                return Double.valueOf(df.format(d)).toString();
            }
        }
        //  index++;


        //   }
        if(!isDecimalNumber && isNumber && str.length()<=2){
            isDate(str);

        }

        return str.toString();
    }

    private boolean isDate(StringBuilder str){
        return false;
    }

    /**
     * check if the char is a digit
     * @param c digit to check
     * @return true if the char is a digit
     */
    private boolean isDigit(char c){
        if(c >= 48 && c<=57)
            return true;
        return false;
    }

    /**
     * check if the char is a dot
     * @param c digit to check
     * @return true if the char is a dot
     */
    private boolean isDot(char c){
        if(c == 46)
            return true;
        return false;
    }

    /**
     * check if the char is a comma
     * @param c digit to check
     * @return true if the char is a comma
     */
    private boolean isComma(char c){
        if(c == 44)
            return true;
        return false;
    }

    /**
     * skipping the white spaces chars is the array
     * @return true, if there is a space
     */
    private boolean skipWhiteSpace(){
        boolean ans = false;
        while(index < arrayLength && docArray[index] == 32) {
            index++;
            ans = true;
        }
        return ans;
    }

    /**
     * check if the char '%' is the next char or if the word "percent" is the next term in the doc
     * @return true, if the char is '%' or if the next word is "percent".
     */
    private boolean isPercent(){
        if(docArray[index] == 37) //check if the char is '%'
            return true;
        //check if the next word is "percent"
        if(index < docArray.length-7 && docArray[index] == 'p' && docArray[index+1] == 'e' && docArray[index+2] == 'r' &&
                docArray[index+3] == 'c' && docArray[index+4] == 'e' && docArray[index+5] == 'n' && docArray[index+6] == 't' ){
            index = index + 7;
            while(index < arrayLength && docArray[index] != 32)//skip until the end of the word
                index++;
            return true;
        }
        return false;
    }

    /**
     * check if the next char in the doc is capital letter
     * @return true, if the next char in the doc is capital letter
     */
    private boolean isCapitalLetter(){
        if(docArray[index] >= 65 && docArray[index] <= 90)
            return true;
        return false;
    }

    /**
     * check if the next char in the doc is lowercase letter
     * @return true, if the next char in the doc is lowercase letter
     */
    private boolean isLowerCaseLetter(){
        if(docArray[index] >= 97 && docArray[index] <= 122)
            return true;
        return false;
    }

    /**
     * find the next expression in the doc.
     * The expression can be a sequence of words that starts with capital letters,
     * or an expression that contains capital letters only
     * @return the next expression in the doc
     */
    private String findExpressions(){
        boolean isAllCapitalLetters = false;
        StringBuilder str = new StringBuilder();

        //check if the word start with capital letter
        while(index < arrayLength && isCapitalLetter()) {
            index++;
            //if the expression contains only capital letters, stop when it is lowercase letter
            if(isAllCapitalLetters && index < arrayLength && isLowerCaseLetter()) {
                index = index - 2;
                break;
            }
            else
                str.append(Character.toLowerCase(docArray[index-1]));

            //append to the expression all the lowercase letters (only if it is not a full capital letters expression).
            while (index < arrayLength && !isAllCapitalLetters && isLowerCaseLetter()) {
                str.append(docArray[index]);
                index++;
            }
            //if the expression contains only capital letters
            while (index < arrayLength && isCapitalLetter()) {
                isAllCapitalLetters =true;
                str.append(Character.toLowerCase(docArray[index]));
                index++;
            }
            if(skipWhiteSpace())
                str.append(' ');
        }
        return str.toString();

    }





    public List<String> findNumbers(String input) {

        List<String> result = new ArrayList<>();
        DecimalFormat df = new DecimalFormat("#.##");

        //numbers
        Matcher matcher = Pattern.compile("((?:\\d+|\\d{1,3}(?:,\\d{3})+)(?:(\\.|,)\\d+)?)\\s*(%|percent|percentage)?").matcher(input);
        while (matcher.find()) {

            String number = matcher.group(1);
            String entireTerm = matcher.group();

            //check if contains commas, if so, remove them
            //if (number.contains(","))
            number = number.replaceAll(",", "");

            //then, check if decimal, round it
            if (number.contains(".")) {
                double d = Double.parseDouble(number);
                double ex = Double.valueOf(df.format(d));
                number = ex + "";
            }

            //then, check if contains percent
            if (entireTerm.contains("%") || entireTerm.contains("percent") || entireTerm.contains("percentage"))
                number = number + " percent";

            //add the number
            result.add(number);
        }

        //fractions
        matcher = Pattern.compile("^/(\\d+)[\\/](\\d+)^/").matcher(input);
        while (matcher.find()) {
            double d = Double.parseDouble(matcher.group(1)) / Double.parseDouble(matcher.group(2));
            double ex = Double.valueOf(df.format(d));

            //add the number
            result.add(ex + "");
        }

        return result;

    }

    public List<String> parseString(String s) {

        List<String> result = new ArrayList<>();

        //seperate the string into words
        String pattern = "(?!\\.|,|%|\\/)\\W+";
        String[] words = s.split(pattern);

        //for each word
        int wordsLength = words.length;
        for (int i = 0; i < wordsLength; i++) {
            String w = words[i];

            //check if a number
            if (Pattern.compile("((?:\\d+|\\d{1,3}(?:,\\d{3})+)(?:(\\.|,)\\d+)?)\\s*(%|percent|percentage)?").matcher(w).matches()) {

                //check if next word is a month
                String[] answer = addFollowingWord(words, i);

                result.add(parseDate(answer[0]));
                i = Integer.parseInt(answer[1]);
                continue;

            }

            //check if there is a percent after
            String wordToSend;
            if (i < wordsLength - 1 && words[i + 1].indexOf("percent") != -1) {
                wordToSend = w + words[i + 1];
                result.add(parseNumber(wordToSend));
                i++;
                continue;
            }



            //check if a fraction
            Matcher matcher;
            if ((matcher = Pattern.compile("^/(\\d+)[\\/](\\d+)^/").matcher(w)).matches()) {
                result.add(parseFraction(matcher));
                continue;
            }

            //check if word is number and "th", "st", "nd", "rd"
            if ((matcher = Pattern.compile("\\b\\d{1,2}(st|nd|rd|th)\\b").matcher(w)).matches()) {

                //check if next word is a month
                String[] answer = addFollowingWord(words, i);

                result.add(parseDate(answer[0]));
                i = Integer.parseInt(answer[1]);
                continue;

            }

            //check if word is month
            if (dates.containsKey(w)) {

                //check if

            }

        }


        return result;

    }

    private String[] addFollowingWord(String[] words, int i) {

        String wordToSend = null;

        //check if next word is a month
        int wordsLength = words.length;
        if (i < wordsLength - 1 && dates.containsKey(words[i + 1])) {

            wordToSend = words[i] + " " + words[i + 1];
            i++;

            //check if there is a next next word
            if (i < wordsLength - 2) {
                wordToSend = wordToSend + " " + words[i + 2];
                i++;
            }

        }

        return new String[]{wordToSend, i + ""};

    }

    private String parseNumber(String s) {

        DecimalFormat df = new DecimalFormat("#.##");

        //numbers
        Matcher matcher = Pattern.compile("((?:\\d+|\\d{1,3}(?:,\\d{3})+)(?:(\\.|,)\\d+)?)\\s*(%|percent|percentage)?").matcher(s);
        if (matcher.find()) {

            String number = matcher.group(1);
            String entireTerm = matcher.group();

            //check if contains commas, if so, remove them
            //if (number.contains(","))
            number = number.replaceAll(",", "");

            //then, check if decimal, round it
            if (number.contains(".")) {
                double d = Double.parseDouble(number);
                double ex = Double.valueOf(df.format(d));
                number = ex + "";
            }

            //then, check if contains percent
            if (entireTerm.contains("%") || entireTerm.contains("percent") || entireTerm.contains("percentage"))
                number = number + " percent";

            return number;

        }

        return "";

    }

    private String parseFraction(Matcher matcher) {

        DecimalFormat df = new DecimalFormat("#.##");

        //fractions
        //Matcher matcher = Pattern.compile("^/(\\d+)[\\/](\\d+)^/").matcher(s);

        if (matcher.find()) {
            double d = Double.parseDouble(matcher.group(1)) / Double.parseDouble(matcher.group(2));
            double ex = Double.valueOf(df.format(d));

            //add the number
            return ex + "";
        }

        return "";

    }

    private String parseDate(String s){

        return null;
    }

}
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import edu.mit.jwi.Dictionary;
import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.IIndexWord;
import edu.mit.jwi.item.ISynset;
import edu.mit.jwi.item.IWord;
import edu.mit.jwi.item.IWordID;
import edu.mit.jwi.item.POS;
import edu.mit.jwi.morph.WordnetStemmer;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import weka.core.Stopwords;

/**
 *
 * @author uqgzhu1
 */
/**
 *
 */
public class MultiStopwordsTest {
    private static util.Stemmer myS=null;
    static String[] followWordsTable = {"give_up", "get_out"};

    static HashSet <String> SaveWordsTable =new HashSet <String> ();

    static String[] stopwordsTable = {"a", "about", "above", "above", "across", "after", "afterwards", "again", "against", "all", "almost",
        "alone", "along", "already", "also", "although", "always", "am", "among", "amongst", "amoungst", "amount", "an", "and",
        "another", "any", "anyhow", "anyone", "anything", "anyway", "anywhere", "are", "around", "as", "at", "back", "be", "became",
        "because", "become", "becomes", "becoming", "been", "before", "beforehand", "behind", "being", "below", "beside", "besides",
        "between", "beyond", "bill", "both", "bottom", "but", "by", "call", "can", "cannot", "cant", "can't", "can\'t", "co", "con", "could", "couldnt", "couldn't",
        "cry", "de", "describe", "detail", "do", "doesn't", "done", "down", "due", "during", "each", "eg", "eight", "either", "eleven", "else",
        "elsewhere", "empty", "enough", "etc", "even", "ever", "every", "everyone", "everything", "everywhere", "except", "few",
        "fifteen", "fify", "fill", "find", "fire", "first", "five", "for", "former", "formerly", "forty", "found", "four", "from",
        "front", "full", "further", "get", "give", "go", "had", "has", "hasnt",
        "have", "haven't", "he", "hence", "her", "here", "hereafter", "hereby", "herein", "hereupon", "hers", "herself",
        "him", "himself", "his", "how", "however", "hundred", "ie", "if", "i'm", "in", "inc", "indeed", "interest", "into",
        "is", "it", "its", "it's", "itself", "keep", "last", "latter", "latterly", "least", "less", "ltd", "made", "many",
        "may", "me", "meanwhile", "might", "mill", "mine", "more", "moreover", "most", "mostly", "move", "much", "must",
        "my", "myself", "name", "namely", "neither", "never", "nevertheless", "next", "nine", "no", "nobody", "none",
        "noone", "nor", "not", "nothing", "now", "nowhere", "of", "off", "often", "on", "once", "one", "only", "onto",
        "or", "other", "others", "otherwise", "our", "ours", "ourselves", "out", "over", "own", "part", "per", "perhaps",
        "please", "put", "rather", "re", "same", "see", "seem", "seemed", "seeming", "seems", "serious", "several", "she",
        "should", "show", "side", "since", "sincere", "six", "sixty", "so", "some", "somehow", "someone", "something",
        "sometime", "sometimes", "somewhere", "still", "such", "system", "take", "ten", "than", "that", "that's", "that’s", "the", "their",
        "them", "themselves", "then", "thence", "there", "thereafter", "thereby", "therefore", "therein", "thereupon",
        "these", "they", "thickv", "thin", "third", "this", "this's", "this’s", "those", "though", "three", "through", "throughout", "thru",
        "thus", "thx", "to", "together", "too", "top", "toward", "towards", "twelve", "twenty", "two", "un", "under", "until",
        "up", "upon", "us", "very", "via", "was", "we", "well", "were", "what", "whatever", "when", "whence", "whenever",
        "where", "whereafter", "whereas", "whereby", "wherein", "whereupon", "wherever", "whether", "which", "while",
        "whither", "who", "whoever", "whole", "whom", "whose", "why", "will", "with", "within", "without", "would", "yet",
        "you", "your", "yours", "yourself", "yourselves",
        "terms", "CONDITIONS", "conditions", "values", "interested.", "care", "sure", "$", "%", "^", "&", "*", "(", ")", "{", "}", "[", "]", ":", ";", ",", "<", ".", ">", "/", "?", "_", "-", "+", "=",
        "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z",
        "contact", "grounds", "buyers", "tried", "said,", "plan", "value", "principle.", "forces", "sent:", "is,", "was", "like",
        "discussion", "tmus", "diffrent.", "layout", "area.", "thanks", "thankyou", "hello", "bye", "rise", "fell", "fall", "psqft.", "km", "miles"};

    private static IDictionary dict;
    //private static Map<String, Integer> Word_map = new HashMap<String, Integer>();

    private static Integer ONE = new Integer(1);
    private static String wordNetDirectory = "/home/uqgzhu1/Java/weka-3-7-12";
    
    private static WordnetStemmer stemmer=null;
    
    public  static String TestWordNet(String inputStr) throws IOException {
        if (inputStr== null) {
            return null;
        }
        // look up first sense of the word "dog "
/*        IIndexWord idxWord = dict.getIndexWord(inputStr, POS.NOUN);
         if (idxWord == null) {
         return -1;
         }
         IWordID wordID = idxWord.getWordIDs().get(0);
         IWord word = dict.getWord(wordID);
         ISynset synset = word.getSynset();
        
         String LexFileName = synset.getLexicalFile().getName();
         //            System.out.println("Lexical Name : "+ LexFileName);            
         //          System.out.println("Id = " + wordID);
         //System.out.println(" Lemma = " + word.getLemma());      
         //      System.out.println(" Gloss = " + synset.getGloss());
         if (synset.getType() != 1) {
         System.out.println(" Lemma = " + word.getLemma());
         }
         */
        if (dict == null) {
            String path = wordNetDirectory + File.separator + "dict";
            URL url = new URL("file", null, path);

            //construct the Dictionary object and open it
            dict = new Dictionary(url);
            dict.open();
        }
        if (stemmer==null)           stemmer = new WordnetStemmer(dict);
        
        List<String> test = null;// = stemmer.findStems(inputStr, POS.NOUN);
        try {
            test = stemmer.findStems(inputStr, POS.VERB);
            if (test.size() < 1) {
                test = stemmer.findStems(inputStr, POS.NOUN);
            }

            if (test.size() < 1) {
                test = stemmer.findStems(inputStr, POS.ADJECTIVE);
            }

            if (test.size() < 1) {
                test = stemmer.findStems(inputStr, POS.ADVERB);
            }
        } catch (java.lang.IllegalArgumentException ex) {
            //System.err.println(inputStr + ":" + ex.getMessage());
            return null;
        }
        if (test.size() < 1) {
//                if (myS==null)              myS=new Stemmer();
  //                  myS.addString(inputStr.trim());
    //                myS.stem();
      //              inputStr=myS.toString();
                    return inputStr;
        }
        // System.out.println(test.get(0));
        
        String temp = test.get(0);
        int tempInt = (int) temp.charAt(0);
        if (tempInt == 10) {
            System.err.println(temp);
            temp = test.get(1);

        }
/*        if (inputStr.compareTo("thing")==0){
            //System.out.println("------------------->"+inputStr +"-->"+test.get(1));
            temp=inputStr;
        }
        if (inputStr.compareTo("advertis")==0){
            //System.out.println("------------------->"+inputStr +"-->"+test.get(1));
            temp=inputStr;
        }        
*/        
        return temp;
    }

    public static void main(String[] args) {
        try {
            MultiStopwordsTest MyStopDo= new MultiStopwordsTest();
            String path = wordNetDirectory + File.separator + "dict";
            URL url = new URL("file", null, path);

            //construct the Dictionary object and open it
            dict = new Dictionary(url);
            dict.open();

            Scanner fip1 = new Scanner(new File("/home/uqgzhu1/NetBeansProjects/Guesslocation/src/text/simple3lines1.txt"));

            Map<String, Integer> Word_map = new TreeMap<String, Integer>();
            while (fip1.hasNext()) {
                int flag = 1;
                String s1 = fip1.nextLine();
                String[] temps = s1.split(" ");
                ProcessStringLen(temps, Word_map);
            }
            // words to process?
            System.out.println();
            //System.out.println(map);

            for (String s1 : Word_map.keySet()) {
                System.out.println(s1 + "\t:" + MyStopDo.TestWordNet(s1));
            }
            //System.out.println("give up"+"\t:"+TestWordNet("give-up"));
            Word_map.clear();

        } catch (Exception ex) {
            Logger.getLogger(MultiStopwordsTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static char[] ProcessChar(char[] tempArry) {
        for (int i = 0; i < tempArry.length; i++) {
            if (tempArry[i] > 0xa00) { //8099
                //System.out.println();
                tempArry[i] = ' ';
            }
            if (tempArry[i] >= '0' && tempArry[i] <= '9') {
                int loc = i;
                if (i > 0 && tempArry[i - 1] == '~') {
                    break;
                }
                while (loc < tempArry.length && (tempArry[loc] >= '0' && tempArry[loc] <= '9' || tempArry[loc] == '.')) {
                    loc++;
                }
                //38th  //23.8°c 0.3mm

                if (loc < tempArry.length) {
                    if (tempArry[loc] == 't' || tempArry[loc] == 's') {
                        i = loc + 1;
                        continue;
                    }
                    if (tempArry[loc] == '°') {
                        loc++;
                        if (loc < tempArry.length) {
                            if (tempArry[loc] == 'c') {
                                i = loc + 1;
                                continue;
                            }
                        }
                    }
                }
                for (int k = i; k < loc && k < tempArry.length; k++) {
                    tempArry[k] = ' ';
                }
            }

            int tempHex = (int) tempArry[i];
            if (tempHex == 10) { // System.out.println("find 0xa "+tempArry[i]); 
                tempArry[i] = ' ';
                return null;
            };

            switch (tempArry[i]) {
                case '\"':
                case '-':
                case '\n':
                case '\r':
                case '♥':
                case '…':
                case '❤':
                case '«':
                case '\t':
                case '(':
                case '‘':
                case ')':
                case '[':
                case ']':
                case '{':
                case '}':
                case '?':
                case '>':
                case '<':
                case '_':
                case '|':
                case '@':                    
                    tempArry[i] = ' ';
                    break;
                case '\'':  //I'm  it's  
                    if ((i + 1) < tempArry.length) {
                        if ((i + 3) <= tempArry.length) {   //i'll
                            tempArry[i++] = ' ';
                            tempArry[i++] = ' ';
                            tempArry[i] = ' ';
                        } else {
                            if ((i + 2) <= tempArry.length) {
                                tempArry[i++] = ' ';
                                tempArry[i] = ' ';
                            }
                        }
                    } else {
                        tempArry[i] = ' ';
                    }
                    break;
                case '\\':
                    if ((i + 1) != tempArry.length) {
                        if (tempArry[i + 1] != '\'') {
                            tempArry[i] = ' ';
                        }
                    } else {
                        tempArry[i] = ' ';
                    }
                    break;
                case ',':
                    tempArry[i] = ' ';
                    break;
                case 'h':   ////"http://"
                    if ((i + 6) < tempArry.length) {
                        if (tempArry[i + 1] == 't') {
                            if (tempArry[i + 2] == 't') {
                                if (tempArry[i + 3] == 'p') {
                                    int loc = i + 4;
                                    if (tempArry[loc] == 's') {
                                        loc++;
                                    }
                                    if (tempArry[loc] == ':') {
                                        loc++;
                                    }
                                    if (tempArry[loc] == '/') {
                                        loc++;
                                    }
                                    if (tempArry[loc] == '/') {
                                        loc++;
                                    }
                                    for (int temploc = i; temploc < loc; temploc++) {
                                        tempArry[temploc] = ' ';
                                        i = loc - 2;
                                    }

                                }
                            }
                        }
                    }
                    break;

                case ':':
                    if ((i + 1) < tempArry.length) {
                        if (tempArry[i + 1] == '(' || tempArry[i + 1] == ')') {
                            i++;
                            continue;
                        }
                    }
                    tempArry[i] = ' ';
                    break;
                case ';':       //;-)
                    if ((i + 2) < tempArry.length) {
                        if (tempArry[i + 1] == '-') {
                            if (tempArry[i + 2] == ')') {
                                i++;
                                i++;
                                continue;
                            }
                        }
                    }
                    tempArry[i] = ' ';
                    break;
                case '#':
                case '.':   //…                  
                    if ((i + 1) < tempArry.length) {
                        if (tempArry[i + 1] == '.'||tempArry[i + 1] == '#') {   //...
                            int loc = i + 1;
                            while (loc < tempArry.length && (tempArry[loc] == '.'||tempArry[loc] == '#')) {
                                loc++;
                            }
                            while (i < loc) {
                                tempArry[i++] = ' ';
                            }
                            i--;
                            break;
                        } else if (tempArry[i + 1] < 'a' || tempArry[i + 1] > 'z') {
                            tempArry[i] = ' ';
                        }
                    } else {
                        tempArry[i] = ' ';
                    }
                    break;
                case '!':
                    tempArry[i] = ' ';
                    break;
            }
        }
        return tempArry;
    }

    public static void ProcessStringLen(String[] inputString, Map<String, Integer> Word_map) {

        for (int j = 0; j < inputString.length; j++) {

            String s_index = inputString[j].toLowerCase().trim();
            //s_index.indexOf(":)");

            char[] tempArry = s_index.toCharArray();

            tempArry = ProcessChar(tempArry);
            if (tempArry == null) {
                continue;
            }

            s_index = String.copyValueOf(tempArry).trim();
            if (s_index.length() < 1) {
                continue;
            }

            String ArrayStr[] = s_index.split(" ");
            for (String s2 : ArrayStr) {
                boolean flag = false;
                if (s2.length() < 1) {
                    continue;
                }
                tempArry = s2.toCharArray();
                tempArry = ProcessChar(tempArry);
                if (tempArry == null) {
                    continue;
                }
                s2 = String.copyValueOf(tempArry).trim();

                if (s2.length() < 1) {
                    continue;
                }
                boolean SaveFlag=true;
                if (SaveWordsTable!= null) {
                        SaveFlag=false;
//                         if (s2.charAt(0)=='@') 
  //                          SaveFlag = true;
//                         else if (s2.charAt(0)=='#') 
  //                          SaveFlag = true;
//                        else
                        if (SaveWordsTable.contains(s2))
                            SaveFlag = true;                            
                }
                flag = true;
                for (int i = 0; i < stopwordsTable.length; i++) {                    
                    if (s2.equals(stopwordsTable[i])) {
                        flag = false;
                        break;
                    }
                }

                if (flag&&SaveFlag) { //it is not stop words  and in Save words list                    
                    //System.out.println("add "+s2);
                    Integer frequency = Word_map.get(s2);
                    if (frequency == null) {
                        frequency = ONE;
                    } else {
                        int value = frequency.intValue();
                        frequency = new Integer(value + 1);
                    }
                    Word_map.put(s2, frequency);
                }
            }
        }
    }

    public static String ProcessStringOnlyOneTweet(String v_tweet_text) {
        String[] temps = v_tweet_text.split(" ");
//        Word_map.clear();
        Map<String, Integer> Word_map = new HashMap<String, Integer>();

        ProcessStringLen(temps, Word_map);

        StringBuffer tempStr = new StringBuffer();

        for (String s1 : Word_map.keySet()) {
            try {
                if (s1.length() < 1) {
                    continue;
                }
                String temp = TestWordNet(s1);
                if (temp != null) {
                    tempStr.append(temp);
                    tempStr.append(" ");
                }
            } catch (IOException ex) {
                Logger.getLogger(MultiStopwordsTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        Word_map.clear();
        return tempStr.toString();

    }

    public static void addSaveWords(String x) {
        SaveWordsTable.add(x);
    }
    public static void PrintSaveWords() {
        for (String x: SaveWordsTable)
            System.out.println(x);
    }

}

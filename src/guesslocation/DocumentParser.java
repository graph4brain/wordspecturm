/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package guesslocation;

import util.TfIdf;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.MultiStopwordsTest;
import util.MyStringArray;

/**
 *
 * @author uqgzhu1
 */
class DocumentParser {

    //This variable will hold all terms of each document in an array.

    private List<int[]> termsDocsArray = new ArrayList<int[]>();
    private List<String> DocsNameArray = new ArrayList<String>(); //to hold all terms;
    private List<String> allTerms = new ArrayList<String>(); //to hold all terms

    private Vector<Integer> DocHasTermsNumber = new Vector<Integer>(); //to hold all terms how many iterms in a city or post code

    TfIdf TempC = null;
//    private List<double[]> tfidfDocsVector = new ArrayList<double[]>();
    private int TextIndex = 1;
    private int DocIndex = 0;
    private int HourIndex = 0;
    private MultiStopwordsTest  MyStopDo=new MultiStopwordsTest();

    /**
     * Method to read files and store in array.
     *
     * @param filePath : source file path
     * @throws FileNotFoundException
     * @throws IOException
     */
    /*    public void parseFiles(String filePath) throws FileNotFoundException, IOException {
     File[] allfiles = new File(filePath).listFiles();
     BufferedReader in = null;
     for (File f : allfiles) {
     if (f.getName().endsWith(".txt")) {
     in = new BufferedReader(new FileReader(f));
     StringBuilder sb = new StringBuilder();
     String s = null;
     while ((s = in.readLine()) != null) {
     sb.append(s);
     }
     String[] tokenizedTerms = sb.toString().replaceAll("[\\W&&[^\\s]]", "").split("\\W+");   //to get individual terms
                
     for (String term : tokenizedTerms) {
     if (!allTerms.contains(term)) {  //avoid duplicate entry
     allTerms.add(term);
     }
     }
     termsDocsArray.add(tokenizedTerms);
                
     }
     }
     }
     */
    public void parseCity(String fileName, boolean HeadFlag) {
        BufferedReader in = null;
        {
            String ZipOutput = null;
            try {
                in = new BufferedReader(new FileReader(fileName));
                String s = null;
                if (HeadFlag) {
                    s = in.readLine();
                    String[] tempS = s.split("\t");
                    for (int i = 0; i < tempS.length; i++) {
                        if (tempS[i].equalsIgnoreCase("code")) {
                            this.DocIndex = i;
                        } else if (tempS[i].equalsIgnoreCase("text")) {
                            this.TextIndex = i;
                        }
                        if (tempS[i].equalsIgnoreCase("hour")) {
                            this.HourIndex = i;
                        }

                    }
                    System.out.println("Doc index=" + this.DocIndex + "\t Text index" + this.TextIndex);
                }

                while ((s = in.readLine()) != null) {
                    //sb.append(s);
                    String[] tempS = s.split("\t");
                    if (tempS.length < (TextIndex + 1)) {
                        continue;
                    }
//                    MyLine++;
//                    if (MyLine==212) System.out.println(MyLine+":\t"+tempS[0]); 
//                    if (tempS.length!=2) { System.err.println(MyLine+":\t"+tempS[0]); continue; }
                    ZipOutput = tempS[DocIndex];
                    this.addTrainLine(ZipOutput, 2015, 5, 21, Integer.valueOf(tempS[this.HourIndex]), tempS[TextIndex]);
                }
                System.out.println("finish read..................");
            } catch (FileNotFoundException ex) {
                Logger.getLogger(DocumentParser.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                System.err.println(ex);
            } catch (ArrayIndexOutOfBoundsException ex) {
                System.err.println(ZipOutput + "\t" + ex);
            }

        }

    }

    /**
     * Method to create termVector according to its tfidf score.
     */
    public void tf_IdfCalculator() {
        double tf; //term frequency
        double idf; //inverse document frequency
        double tfidf; //term requency inverse document frequency        
        TempC = new TfIdf(allTerms, DocsNameArray);
        System.out.println("Total terms=" + allTerms.size());
        System.out.println("Total docs=" + termsDocsArray.size());

        for (int i = 0; i < termsDocsArray.size(); i++) {
            String rowName = DocsNameArray.get(i);
            for (int temp : termsDocsArray.get(i)) {
                String terms = allTerms.get(temp);
                tf = TempC.tfCalculator(rowName, terms);
            }
        }

        for (String x : allTerms) {
            idf = TempC.idfCalculator(x);
//            tfidf = tf * idf;
        }
    //    TempC.TfMatrix();
//        System.out.println("----------------------------");
  //      TempC.getIdfVector();

        /*        
         double[] tfidfvectors = new double[allTerms.size()];
                
         for (String terms:allTerms)
         System.out.print("\t\t"+terms);
         System.out.println();
        
         for (int i=0;i<tfidfDocsVector.size();i++ ){
         String []tempx=termsDocsArray.get(i);
         System.out.print(tempx[0]  +" "+ tempx[1]+" "+ tempx[2]+"\t");
         for (double x: tfidfDocsVector.get(i)) System.out.printf("\t%1.3f",x);
         System.out.println();
         }
         */
    }


    void getCosineSimilarity() {
        TempC.getCosineSimilarity();
    }

    void TfIdffMatrix(String[] Location) {

        int[] intArray = new int[DocHasTermsNumber.size()]; //(Integer [])DocHasTermsNumber.toArray();

        for (int i = 0; i < DocHasTermsNumber.size(); i++) {
            intArray[i] = DocHasTermsNumber.get(i);
        }
        double [][]OutputMatrix=TempC.getTfIdffMatrix(Location, intArray);
        
        
        System.out.print("Code");
        for (String x:Location){
            System.out.print("\t"+x);
        }
        System.out.println();
        
        for (int i=0;i<DocsNameArray.size();i++){
            System.out.print(DocsNameArray.get(i));
            
            for (double y: OutputMatrix[i])
                System.out.print("\t"+y);
            System.out.println();
        }
        
        intArray = null;
    }

    void addTrainLine(String instring, int Year, int Month, int Day, int Hour, String new_york_times) {
        int DocIndex = 0;
        boolean found = false;
        
        for (DocIndex = 0; DocIndex < DocsNameArray.size(); DocIndex++) {
            if (DocsNameArray.get(DocIndex).compareTo(instring) == 0) {
                found = true;
                break;
            }
        }
        StringBuilder sb = new StringBuilder(new_york_times);
        
        String[] tokenizedTerms = sb.toString().replaceAll("[\\W&&[^\\s]]", "").split("\\W+");   //to get individual terms

        
        
        
        int TermsLoc[]=new int[tokenizedTerms.length];
        
        for (int j=0;j<tokenizedTerms.length;j++) {           
             boolean noTfound=true;
            for (int i=0;i<allTerms.size();i++)
                if (allTerms.get(i).compareToIgnoreCase(tokenizedTerms[j].toLowerCase())==0) {  //avoid duplicate entry
                        TermsLoc[j]=i;noTfound=false;
                        break;
                }
            if (noTfound) {TermsLoc[j]=allTerms.size();
                    allTerms.add(tokenizedTerms[j]);                                
                           
                           }
        }

        if (found == false) {
            DocsNameArray.add(instring);
            termsDocsArray.add(TermsLoc);
            DocHasTermsNumber.add(tokenizedTerms.length);

        } else {
            int[] temp = termsDocsArray.get(DocIndex);
            Integer x = DocHasTermsNumber.get(DocIndex);
            DocHasTermsNumber.set(DocIndex, x + tokenizedTerms.length);
            int[] allStateCapitals = MyStringArray.concatenate(temp, TermsLoc);
            termsDocsArray.set(DocIndex, allStateCapitals);
            temp = null;
        }

    }

    void addTestLine(String instring, int Year, int Month, int Day, int Hour, String new_york_times) {
        int DocIndex = 0;
        boolean found = false;
        
        for (DocIndex = 0; DocIndex < TestDocsNameArray.size(); DocIndex++) {
            if (TestDocsNameArray.get(DocIndex).compareTo(instring) == 0) {
                found = true;
                break;
            }
        }
        StringBuilder sb = new StringBuilder(new_york_times);

        String[] tokenizedTerms = sb.toString().replaceAll("[\\W&&[^\\s]]", "").split("\\W+");   //to get individual terms
        for (int j=0;j<tokenizedTerms.length;j++){
            try {
                tokenizedTerms[j]=MyStopDo.TestWordNet(tokenizedTerms[j].toLowerCase());
            } catch (IOException ex) {
                Logger.getLogger(DocumentParser.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        int TermsLoc[]=new int[tokenizedTerms.length];
        
        for (int j=0;j<tokenizedTerms.length;j++) {           
             boolean noTfound=true;
            for (int i=0;i<TestallTerms.size();i++)
                if (TestallTerms.get(i).compareToIgnoreCase(tokenizedTerms[j])==0) {  //avoid duplicate entry
                        TermsLoc[j]=i;noTfound=false;
                        break;
                }
            if (noTfound) {TermsLoc[j]=TestallTerms.size();
                    TestallTerms.add(tokenizedTerms[j]);                                
                           
                           }
        }

        if (found == false) {
            TestDocsNameArray.add(instring);
            TesttermsDocsArray.add(TermsLoc);
            TestDocHasTermsNumber.add(tokenizedTerms.length);

        } else {
            int[] temp = TesttermsDocsArray.get(DocIndex);
            Integer x = TestDocHasTermsNumber.get(DocIndex);
            TestDocHasTermsNumber.set(DocIndex, x + tokenizedTerms.length);
            int[] allStateCapitals = MyStringArray.concatenate(temp, TermsLoc);
            TesttermsDocsArray.set(DocIndex, allStateCapitals);
            temp = null;
        }

        
    }

    String[] getTestCosineSimilarity() {
        int nrow=TestDocsNameArray.size();
        int ncol=TestallTerms.size();
        testtfidfMatrix=null;
       // System.out.println("test nrow="+nrow+"\t"+ncol);
        testtfidfMatrix=new double[nrow][ncol];
        
        
        for (int i=0;i<TestDocsNameArray.size();i++){
            int []inputcol=TesttermsDocsArray.get(i);
            testtfidfMatrix=TempC.gettfNoSave(i, inputcol, testtfidfMatrix);            
        }
        String []tempS=new String[TestallTerms.size()];
                
         for (int i=0;i<TestallTerms.size();i++) tempS[i]=TestallTerms.get(i);

         
        double[] idfFromTrain=TempC.getIdfVector(tempS);
        
        
        for (int i=0;i<TestDocsNameArray.size();i++){
            for (int j=0;j<idfFromTrain.length;j++)
                testtfidfMatrix[i][j]*=idfFromTrain[j]/TestallTerms.size();            
        }
//            for (int j=0;j<testtfidfMatrix[0].length;j++){
  //              System.out.print(testtfidfMatrix[0][j]+"\t");
    //        }
            System.out.println();
        
        
        
        return TempC.getCosineSimilarity4Test(tempS, testtfidfMatrix);
    }
    private List <String> TestDocsNameArray=new ArrayList <String>();    
    private List <String> TestallTerms=new ArrayList <String>();    
    private List<int[]> TesttermsDocsArray = new ArrayList<int[]>();
    private Vector<Integer> TestDocHasTermsNumber = new Vector<Integer>(); //to hold all terms how many iterms in a city or post code
    double testtfidfMatrix[][]=null;
}

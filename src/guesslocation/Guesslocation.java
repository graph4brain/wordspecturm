/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package guesslocation;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import static util.MultiStopwordsTest.ProcessStringOnlyOneTweet;

/**
 *
 * @author uqgzhu1
 */
public class Guesslocation {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

            // TODO code application logic here
        String []Location={
        "adelaide","airport","beach","bed","breakfast","brisbane","campu","coast","course",
            "dinner","dog","god","gold","griffith","gym","hill","hotel",
                "bbc","humidity","lucia","lunch","mainland","melbourne","night",
                "nsw","perth","queensland","qut","rain","sleep","storm","sydney","temperature",
                "train","university","vic","wind"}; 
       // String[] Location = {"new", "post", "times", "york"};

        DocumentParser dp = new DocumentParser();
            //dp.parseFiles("/home/uqgzhu1/NetBeansProjects/Guesslocation/src/text/"); // give the location of source file
          dp.parseCity("/home/uqgzhu1/Documents/R/bustransport/city_tweet_au_code702.csv",true);
        //dp.addTrainLine("2000", 2015, 5, 15, 3, "new york times");
        //dp.addTrainLine("2001", 2015, 5, 15, 3, "new york post");
        //dp.addTrainLine("2003", 2015, 5, 15, 3, "los angeles times");
        //dp.addTrainLine("2003", 2015, 5, 15, 3, "naztradamixgabrielle lois lane batgirl hope definitely poison ivy");

        dp.tf_IdfCalculator(); //calculates tfidf
        // String outputfile="/home/uqgzhu1/Documents/R/bustransport/tempDraw.txt";
        //System.setOut(setOutputFile(outputfile));
        //System.setErr(outputFile(args(2));            

          dp.TfIdffMatrix(Location);
        //dp.getCosineSimilarity(); //calculates cosine similarity   3175
//            String tempx=ProcessStringOnlyOneTweet("Too Much Love Will Kill You by Queen is #nowplaying in Genesis Dandedong, Dandenong.  Download it now at http://t.co/zr0I4Npssr");
        //          String tempx1=ProcessStringOnlyOneTweet("A beautiful afternoon for it #wilier #outsideisfree #cycling #cyclinglife @ Westerfolds - Yarra Trails https://t.co/87aQOEIxyk");
        //        String temp=  dp.getCosineSimilarity(tempx1); //calculates cosine similarity   
          //stay away from east tce theres staries everywhere and theres a seige
        //  dp.addTestLine("2003",2015,5,15,3,"like brisbane");
//","sueieraci free chiro  kids cancer low scroll down page little", "naztradamixgabrielle lois lane batgirl hope definitely poison ivy",  "like brisbane")
               //dp.addTestLine("2003",2015,5,15,3,"like brisbane");
        //dp.addTestLine("2013", 2015, 5, 15, 3, "beautiful day darwin welcome to my garden honeyeaters  light fire alice sparrow darwin beautifuldryseason superdibble holiday home town staycation darwinnt proactive bowel cancer cure rate");
/*        dp.addTestLine("15183045", 2015, 5, 15, 3, "Txt msg from Alex to Mazen Hourari Put 5k in toilet just read out at #TURC");
        dp.addTestLine("15183045", 2015, 5, 15, 3, "I nearly typed put 5k DOWN the toilet had to correct the toilet joke #TURC");
        dp.addTestLine("15183045", 2015, 5, 15, 3, "Ah well we all know what the #TURC headline will be in the media tonight and tomorrow");
        dp.addTestLine("15183045", 2015, 5, 15, 3, "What comes across at #TURC is that Darren is very eager to go visit George Alex at his home - many many times");
        dp.addTestLine("15183045", 2015, 5, 15, 3, "One Alex txt to Darren mentions toilet first drawer Darren says that may be where he picked up biz documents from George Alex  #turc");
        dp.addTestLine("15183045", 2015, 5, 15, 3, "Another Alex text refers to 2b left in toilet under sink It seems a v. pop drop off point in Alex home.Maybe ppl washed their hands");
*/      dp.addTestLine("2227797445", 2015, 5, 15, 3,"@WilliamShatner In the fan fiction... Right...? The one with really high production values... Fan fiction.");
        dp.addTestLine("2227797445", 2015, 5, 15, 3, "\"The final @BBC_TopGear with Clarkson, Hammond and May airs on Sunday 28 June on BBC Two\". Should make it the SERIES final but anyway...");
        dp.addTestLine("2227797445", 2015, 5, 15, 3, "@PierceBrosnan It saddens me that the Kia ad had a better script than the last 3 Bond movies you were shackled with...");
        dp.addTestLine("15183045", 2015, 5, 15, 3, "Darren is straight-faced when he tells #TURC it is a serious answer when he says he picked up biz papers from Alex in the toilet");
        dp.addTestLine("15183045", 2015, 5, 15, 3, "Is it ur honest answer you've never seen cash in toilet is it ur honest answer that documents were left  in the toilet drawer #TURC");
       

        String[] temp = dp.getTestCosineSimilarity();
        System.out.print("Test zip is :");
        for (String x : temp) {
            System.out.print("\t" + x);
        }
         
        System.out.println();

    }

    private static PrintStream setOutputFile(String outputfile) {
        try {
            return new PrintStream(new BufferedOutputStream(new FileOutputStream(outputfile)));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Guesslocation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return System.out;
    }

}

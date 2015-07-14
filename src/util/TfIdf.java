/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import guesslocation.CosineSimilarity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author uqgzhu1
 */
public class TfIdf {

    private TFIDF_TYPE tfidf_Type = TFIDF_TYPE.WEIGHT;
    TreeMap<String, Integer> TermsMap = null;
    TreeMap<String, Integer> DocMap = null;

    List<String[]> TestingSet = new ArrayList<String[]>();

    int TermMatrix[][] = null;
    double IdfVector[] = null;
//     double TfIdfMatrix[][]=null;     

    /**
     * Calculates the tf of term termToCheck
     *
     * @param totalterms : Array of all the words under processing document
     * @param termToCheck : term of which tf is to be calculated.
     * @return tf(term frequency) of term termToCheck
     */
    public TfIdf(List<String> Terms, List<String> Docs) {
        int nRows = Docs.size();
        int nCols = Terms.size();
        System.out.println("rows" + nRows);
        System.out.println("nCols" + nCols);
        TermsMap = new TreeMap<String, Integer>();
        DocMap = new TreeMap<String, Integer>();
        for (int i = 0; i < Terms.size(); i++) {
            TermsMap.put(Terms.get(i), i);
        }
        for (int i = 0; i < Docs.size(); i++) {
            DocMap.put(Docs.get(i), i);
        }

        TermMatrix = new int[nRows][nCols];
        IdfVector = new double[nCols];

    }

    public double tfCalculator(String DocName, String termToCheck) {
        //double count = 0;  //to count the overall occurrence of the term termToCheck

        Integer col = TermsMap.get(termToCheck);
        Integer row = DocMap.get(DocName);
//        if (Col==null) return 0;

        TermMatrix[row][col]++;
        return TermMatrix[row][col] / TermsMap.size();
    }

    public double idfCalculator(String termToCheck) {
        int count = 0;
        Integer Col = TermsMap.get(termToCheck);
        int mycol = Col.intValue();
        for (int i = 0; i < DocMap.size(); i++) {
            if (TermMatrix[i][mycol] > 0) {
                count++;
            }
        }
        double tempx = DocMap.size() / (double) count;
        //System.out.println(termToCheck+"\t:"+mycol+":\t"+count+"\t"+tempx);        
        if (count != 0) {
            IdfVector[mycol] = (Math.log(tempx)) / Math.log(2);
        }

        //      for (int i=0;i<DocMap.size();i++)  this.TfIdfMatrix[i][mycol]= TermMatrix[i][mycol]*IdfVector[mycol];
        return IdfVector[mycol]; // 1 +
    }

    public double[][] gettfNoSave(int row, int inputcol[], double inputmatrix[][]) {
        for (int i = 0; i < inputcol.length; i++) {
            inputmatrix[row][inputcol[i]]++;
        }
        return inputmatrix;
    }

    public double[] tfIdfCalculatorNoSave(String[] totalterms) {
        int[] tempintArray = new int[this.TermsMap.size()];  //TF matrix
        int maxValue = 0;
        for (String s : totalterms) {
            Integer Col = TermsMap.get(s);
            if (Col == null) {
                System.out.println("no this words, should be training..." + s);
                continue;
            }
            int col = Col.intValue();
            tempintArray[col]++;
            if (maxValue < tempintArray[col]) {
                maxValue = tempintArray[col];
            }
        }

        double[] tempArray = new double[this.TermsMap.size()];       //tfIDF matrix
        for (int i = 0; i < tempintArray.length; i++) {
            if (tempintArray[i] > 0) {
                if (tfidf_Type == TFIDF_TYPE.WEIGHT) {
                    tempArray[i] = Math.log((double) tempintArray[i] + 1) * Math.log(this.IdfVector[i]);
                } else {
                    tempArray[i] = (this.IdfVector[i]) * (double) tempintArray[i];
                }
            }
        }
        tempintArray = null;
        return tempArray;
    }

    /**
     * Calculates idf of term termToCheck
     *
     * @param allTerms : all the terms of all the documents
     * @param termToCheck
     * @return idf(inverse document frequency) score
     */
    /*    public double idfCalculator(List <String []>  DocNumberArray, String termToCheck) {
     double count = 0;
     for (String[] ss : DocNumberArray) {
     for (String s : ss) {
     if (s.equalsIgnoreCase(termToCheck)) {
     count++;
     break;
     }
     }
     }
        
     //   System.out.printf(" C/A=%2.0f / %2d = %4.3f\n",count,allTerms.size(),(Math.log(allTerms.size() / count)/Math.log(2)));
     return (Math.log(DocNumberArray.size() / count))/Math.log(2); // 1 +
     }
     */
    public static String asciiToHex(String asciiValue) {
        char[] chars = asciiValue.toCharArray();
        StringBuffer hex = new StringBuffer();
        for (int i = 0; i < chars.length; i++) {
            hex.append(chars[i]);
            hex.append(':');
            hex.append(Integer.toHexString((int) chars[i]));
            //  hex.append("\t");
        }
        return hex.toString();
    }

    public void TfMatrix() {

        //System.out.print(mat);
        for (String x : TermsMap.keySet()) {
            System.out.print("\t" + x);
        }
        System.out.println();
        Set<String> tempSet = DocMap.keySet();
        Iterator<String> tempIter = tempSet.iterator();
        while (tempIter.hasNext()) {
            String RowName = tempIter.next();
            System.out.print(RowName + "\t");
            int row = DocMap.get(RowName);
            for (String x : TermsMap.keySet()) {
                int col = TermsMap.get(x);
                System.out.print(TermMatrix[row][col] + "\t");
            }
            System.out.println();
        }
    }

    public double[] getIdfVector() {
        for (String x : TermsMap.keySet()) {
            System.out.print(x + "\t");
        }
        System.out.println();
        for (String x : TermsMap.keySet()) {
            int col = TermsMap.get(x).intValue();
            System.out.printf("%4.3f\t", IdfVector[col]);
        }
        System.out.println();
        return IdfVector;
    }

    public void TfIdffMatrix() {

        for (String x : TermsMap.keySet()) {
            System.out.print("\t" + x);
        }
        System.out.println();
        Set<String> tempSet = DocMap.keySet();
        Iterator<String> tempIter = tempSet.iterator();
        while (tempIter.hasNext()) {
            String RowName = tempIter.next();
            System.out.print(RowName + "\t");
            int row = DocMap.get(RowName);
            for (String x : TermsMap.keySet()) {
                int col = TermsMap.get(x);
                System.out.printf("%4.3f\t", TermMatrix[row][col] * IdfVector[col]);
            }
            System.out.println();
        }

    }

    public void getCosineSimilarity() {

        double rowMat[] = new double[this.TermMatrix.length];
        double colMat[] = new double[this.TermMatrix.length];
        //      for (int i=0;i<DocMap.size();i++)  this.TfIdfMatrix[i][mycol]= TermMatrix[i][mycol]*IdfVector[mycol];
        for (int i = 0; i < this.TermMatrix.length; i++) {
            for (int mycol = 0; mycol < DocMap.size(); mycol++) {
                rowMat[mycol] = TermMatrix[i][mycol] * IdfVector[mycol];
            }

            for (int j = 0; j < this.TermMatrix.length; j++) {
                for (int mycol = 0; mycol < DocMap.size(); mycol++) {
                    colMat[mycol] = TermMatrix[j][mycol] * IdfVector[mycol];
                }

                double temp = new CosineSimilarity().cosineSimilarity(rowMat, colMat);
                System.out.println("between " + i + " and " + j + "  =  "
                        + temp);
            }
        }
    }

    public double[][] getTfIdffMatrix(String[] Location, int[] DocHasItermNumbers) {

        //System.out.print("Code");
        int[] ColArray = new int[Location.length];
        int col = 0;
        for (String x : Location) {
            Integer temp = TermsMap.get(x);
            if (temp == null) {
                System.err.print("\t"+x+"\t");
                col++;
                continue;
            }
            ColArray[col++] = temp.intValue();
        }

        //System.out.println();
        double outMatrix[][] = new double[DocMap.size()][Location.length];
        Set<String> tempSet = DocMap.keySet();
        Iterator<String> tempIter = tempSet.iterator();
        while (tempIter.hasNext()) {
            String RowName = tempIter.next();
//         System.out.print(RowName);
            int row = DocMap.get(RowName);

  //       int niterms1=DocHasItermNumbers[row];
//         if (niterms1<1)niterms1=1;
            for (col = 0; col < ColArray.length; col++) {
              //System.out.printf("\t%4.3f",TermMatrix[row][ColArray[col]]);         
                //System.out.printf("%4.3f\t",TermMatrix[row][col]*IdfVector[col]);         
                int tmpCol = ColArray[col];
                double tempV = 0;//(double)TermMatrix[row][tmpCol]*IdfVector[tmpCol];
                if (TermMatrix[row][tmpCol] > 0) {
                    if (tfidf_Type == TFIDF_TYPE.WEIGHT) {
                        outMatrix[row][col] = Math.log(TermMatrix[row][tmpCol] + 1) * Math.log(this.IdfVector[tmpCol]);
                    } else {
                        outMatrix[row][col] = TermMatrix[row][tmpCol] * this.IdfVector[tmpCol];
                    }
                }

//             System.out.printf("\t%4.3f",tempV);         
            }
            //       System.out.println();
        }
        return outMatrix;
    }

    public String[] getCosineSimilarity4Test(String[] tempS, double[][] testtfMatrix) {

        double[][] TrainMatrix = getTfIdffMatrix(tempS, null);
        double minV = 0;
        String tempV = null;
        String []returnStr=new String[testtfMatrix.length];
        Set<String> tempSet = DocMap.keySet();
        for (int k = 0; k < testtfMatrix.length; k++) {
            Iterator<String> tempIter = tempSet.iterator();
            while (tempIter.hasNext()) {
                String RowName = tempIter.next();
                //         System.out.print(RowName);
                int row = DocMap.get(RowName);

                double value = new CosineSimilarity().cosineSimilarity(
                        testtfMatrix[k],
                        TrainMatrix[row]
                );
                if (minV < value) {
                    minV = value;
                    tempV = RowName;
                }
            }
            //System.out.println("between input   and " + RowName + "  =  "+ value);
            returnStr[k]=tempV;
        }
        TrainMatrix = null;
        return returnStr;
    }

    public double[] getIdfVector(String[] Location) {
        int[] ColArray = new int[Location.length];
        int col = 0;
        for (String x : Location) {
            Integer temp = TermsMap.get(x);
            if (temp == null) {
                System.err.print("\t"+x+"\t");
                col++;
                continue;
            }
            ColArray[col++] = temp.intValue();
        }
        double[] tempR = new double[Location.length];
        for (col = 0; col < ColArray.length; col++) {
            tempR[col] = this.IdfVector[ColArray[col]];
        }
        ColArray = null;
        return tempR;
    }

}

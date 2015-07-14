/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Rpackage;

import util.MultiStopwordsTest;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author uqgzhu1
 */
public class MysqlExport {

    // JDBC driver name and database URL

    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    //static final String DB_URL = "jdbc:mysql://10.33.0.161/twitter";
    static final String DB_URL = "jdbc:mysql://127.0.0.1/twitter";

    //  Database credentials
    static final String USER ="root";// "root";
    static final String PASS = "1234321";
    private static Map<String, ArrayList<String>> City_Word_map = new HashMap<String, ArrayList<String>>();
    
    private    Connection conn = null;
    private    Statement stmt = null;

     public MysqlExport(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();                
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MysqlExport.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(MysqlExport.class.getName()).log(Level.SEVERE, null, ex);
        }
     }
     public boolean Close(){
        try {
                if (stmt != null) {
                    stmt.close();
                }
            
            
        } catch (SQLException ex) {
            Logger.getLogger(MysqlExport.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
                return false;
            }//end finally try            
        }
        return true;
     }
     
    public static void main(String[] args) {
        MysqlExport temp= new MysqlExport();
//        temp.Export("US");
        Long tempL=Long.valueOf(800);
        String [] y=temp.getAuStateCity(tempL);
        System.out.println(y[0]+"\t:\t"+y[1]);
        
        tempL=Long.valueOf(4350);
        y=temp.getAuStateCity(tempL);
        System.out.println(y[0]+"\t:\t"+y[1]);
        
        temp.Close();
        System.exit(0);        
        System.out.println("Goodbye!");
    }//end main

    public String[] getAuStateCity(Long cityName){
        try {
            String sql="select * from postcodes_geo where postcode="+cityName;
            ResultSet rs = stmt.executeQuery(sql);
            String returnStr[]=new String [2];
            if (rs.next()) {
                //Retrieve by column name
                returnStr[1] = rs.getString("suburb");
                returnStr[0]= rs.getString("state");

            }
            rs.close();
            return returnStr;
        } catch (SQLException ex) {
            Logger.getLogger(MysqlExport.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
        
    }
    void Export(String country_Code) {
        try {
            City_Word_map.clear();
            String sql;
            sql = "SELECT tweet_Place_Name, tweet_ID, tweet_text FROM tweets_stream_usa_word_analysis where tweet_Place_Type like 'city' and tweet_Place_Name>'A'";
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                //Retrieve by column name
                String id = rs.getString("tweet_ID");
                String City_Name = rs.getString("tweet_Place_Name");
                String tweet_text = rs.getString("tweet_text");

                String newv_tweet_text = MultiStopwordsTest.ProcessStringOnlyOneTweet(tweet_text);

                ArrayList<String> tempList = City_Word_map.get(City_Name);

                if (tempList == null) {
                    tempList = new ArrayList<String>();
                }

                tempList.add(newv_tweet_text);

                City_Word_map.put(City_Name, tempList);

            }
            rs.close();
     
            System.out.println("Code\tText");
            for (String cityName : City_Word_map.keySet()) {
                //if (cityName.compareTo("7264df6741aeb031")==0)
                {
                    System.out.print(cityName + "\t");
                    ArrayList<String> tempValueList = City_Word_map.get(cityName);
                    for (String tempx : tempValueList) {
//                             String tempHex=asciiToHex(tempx);
                        System.out.print(tempx + " ");
                    }
                    System.out.println();
                }
                //System.out.printf("%s\t%s\t%f\t%f\t%d\t%s\n",v_tweet_Place_Name,v_tweet_Country_Code,v_Latitude,v_Longtitude,v_tweet_ID,newv_tweet_text);
            }


        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        } 
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Rpackage;

import util.MultiStopwordsTest;
import guesslocation.*;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import static java.util.regex.Pattern.CASE_INSENSITIVE;


/**
 *
 * @author uqgzhu1
 */
public class MongoExoirt {

    public static void main(String[] args) {

        {
		String base = System.getProperty("user.dir") + "/data/";
		String name = "test";
		char[] options = { 'f', 'i', 'o', 'p', 's' };
		String savefilelist = base + "/savewordlist.txt";
		String stopfile = base + "/stoplist.txt";
            
            String country_Code = "AU";
            int Server_Code = 0;                  //0 monogodb
            if (args.length > 0) {
                for (int i = 0; i < args.length; i++) {
                    if (args[i].contains("-c")) {
                        country_Code = args[i + 1];
                    }
                    if (args[i].contains("-s")) {
                        Server_Code = 1;
                    }
                }
            }
            ReadSaveStopFile(savefilelist);
            
            if (Server_Code > 0) {
                MysqlExport tempSql = new MysqlExport();
                tempSql.Export(country_Code);
                return;
            }
            
            
            
            MysqlExport MySQLDB=new  MysqlExport();
            
            try {

    //            String locationStr="44.3,37.2";
                //              searchVenues(locationStr);
//                System.exit(0);
                // Connect to mongodb
                MongoClient mongo = new MongoClient("localhost", 27017);

                // get database
                // if database doesn't exists, mongodb will create it for you
                DB db = mongo.getDB("test");

                // get collection
                // if collection doesn't exists, mongodb will create it for you
                DBCollection collection = db.getCollection("twitter_Stream");
                DBCollection Outcollection = db.getCollection("user_tw_Stream");
                DBCursor cursor;

        //------------------------------------
                // ( 1 ) collection.find() --> get all document
                //query = new BasicDBObject("$and: [ {\"Record_Loc\":\"USA\"}, {\"tweet_Country\":{\"$exists\":true}} ]"); //2015-05-12T15:15:31Z
                //DBObject clause1 = new BasicDBObject("Record_Loc", "AU");  
                //              System.out.println(args.length+":"+country_Code);
//                System.exit(0);
                DBObject clause1 = new BasicDBObject("R_Country_Code", country_Code);
                DBObject clause2 = new BasicDBObject("R_Post_Code", new BasicDBObject("$gt", "0"));
                DBObject clause3 = new BasicDBObject("tweet_Lan", "en");
                DBObject clause4 = new BasicDBObject("GMT_Date", new BasicDBObject("$gt", 25));    

                BasicDBList or = new BasicDBList();
                or.add(clause1);
                or.add(clause2);
                or.add(clause3);
                or.add(clause4);                
                DBObject query = new BasicDBObject("$and", or);

                cursor = collection.find(query);
//                System.out.println("( 1 ) .find()");
//                System.out.println("results --> " + cursor.count());
//                MySQLDB.Close();
//                System.exit(0);
                
                Map<Long, TweetByCityUser> City_User_Word_map = new TreeMap<Long, TweetByCityUser>();
  //              Map<Long, String> City_Name_MAP = new TreeMap<Long, String>();
                
                try {
                    BasicDBObject IDquery = new BasicDBObject(); //2015-05-12T15:15:31Z
                    int nCount = 0;
                    while (cursor.hasNext()) {
                        nCount++;
                        DBObject data = cursor.next();
                        Long v_user_Id = (Long) data.get("user_Id");
                        if (v_user_Id == null) {
                            continue;
                        }
                        //String v_tweet_Place_Name =  (String)data.get("tweet_Place_Name");
                        String Tempv_tweet_PostCode = (String) data.get("R_Post_Code");
                        if (Tempv_tweet_PostCode == null) {
                            continue;
                        }
                        String Tempv_tweet_City = (String) data.get("R_City");
                        if (Tempv_tweet_City!=null) Tempv_tweet_City=Tempv_tweet_City.trim();
                        Tempv_tweet_PostCode = Tempv_tweet_PostCode.replaceAll("\\D+","");
                        Long v_tweet_PostCode = Long.valueOf(Tempv_tweet_PostCode);
                        String tweet_Place_ID = (String) data.get("tweet_Place_ID");

                        Long v_tweet_ID = (Long) data.get("tweet_ID");
                        String v_tweet_text = (String) data.get("tweet_text");
                        Double v_Latitude = (Double) data.get("Latitude");
                        Double v_Longtitude = (Double) data.get("Longitude");
                        String v_tweet_Country_Code = (String) data.get("tweet_Country_Code");
                        Integer v_Year = (Integer) data.get("GMT_Year");
                        Integer v_Month = (Integer) data.get("GMT_Month");
                        Integer v_Day = (Integer) data.get("GMT_Day");
                        Integer v_Hour = (Integer) data.get("GMT_Hour");

                        String newv_tweet_text = MultiStopwordsTest.ProcessStringOnlyOneTweet(v_tweet_text);
//                        if (newv_tweet_text==null) {
                        //                          System.err.println(v_tweet_ID+"\t"+v_tweet_text);
                        //                        continue;
                        //                  }
                        if (newv_tweet_text==null) continue;
                        
                        TweetByCityUser tempList = City_User_Word_map.get(v_tweet_PostCode);
                        
                        if (tempList == null) {
                            tempList = new TweetByCityUser();
                        }

                        //tempList.put(v_user_Id, newv_tweet_text);
                        tempList.put(v_user_Id, v_tweet_ID, v_Year, v_Month, v_Day, v_Hour, newv_tweet_text,Tempv_tweet_City);
                        
              //          if (tweet_Place_ID.compareTo("7264df6741aeb031")==0){
                        //                int tempInt=(int) newv_tweet_text.charAt(0);                            
                        //                    System.out.println(tempInt+":"+newv_tweet_text);
                        //                  System.out.println("->"+v_tweet_text);
                        //        }
                        City_User_Word_map.put(v_tweet_PostCode, tempList);
/*                        String tempCityName=City_Name_MAP.get(v_tweet_PostCode);
                        
                        if (tempCityName!=null&&Tempv_tweet_City!=null){
                            if (tempCityName.compareTo(Tempv_tweet_City)!=0)
                            System.err.println("error on insert two city:"+Tempv_tweet_City+"\t:\t"+tempCityName+" into  "+v_tweet_PostCode);
                        }
                            if (Tempv_tweet_City==null)
                                City_Name_MAP.put(v_tweet_PostCode, "null");
                            else
                                if (Tempv_tweet_City.length()<2)
                                    City_Name_MAP.put(v_tweet_PostCode, "null");
                                else
                                    City_Name_MAP.put(v_tweet_PostCode, Tempv_tweet_City);
*/                        
                        //System.out.printf("%s\t%s\t%f\t%f\t%d\t%s\n",v_tweet_Place_Name,v_tweet_Country_Code,v_Latitude,v_Longtitude,v_tweet_ID,newv_tweet_text);
                    }
                } finally {

                    cursor.close();
                    
                }
                System.out.println("Code\tHour\tUser_Number\tTweet_Number\tStateName\tCityName\tText");
                for (Long cityName : City_User_Word_map.keySet()) {
                    //if (cityName.compareTo("7264df6741aeb031")==0)
                    {
                        
                        TweetByCityUser tempValueList = City_User_Word_map.get(cityName);
  //                      String tempCityName=City_Name_MAP.get(cityName);
//                        String tempStateName=City_Name_MAP.get(cityName);
                        String []tempStateCity=MySQLDB.getAuStateCity(cityName);
                        for (Integer x : tempValueList.keySet()) {
                            
                            TweetByCityByHour tempx = tempValueList.getTweetByHour(x);
                            if (tempx == null) {
                                continue;
                            }
                            System.out.print(cityName + "\t");// post code
                            System.out.println(x + "\t" + tempx.getUserSize() + "\t" + tempx.getTweetSize() +"\t" +tempStateCity[0]+ "\t" +tempStateCity[1]+"\t"+tempx.getAllTweet());
                        }
//                          System.out.print(tempValueList.getUserSize()+"\t");                          
                    }
                    //System.out.printf("%s\t%s\t%f\t%f\t%d\t%s\n",v_tweet_Place_Name,v_tweet_Country_Code,v_Latitude,v_Longtitude,v_tweet_ID,newv_tweet_text);
                }
                //System.out.println("---------------------------------");
                
                MySQLDB.Close();
                System.exit(0);

            } catch (UnknownHostException ex) {                
                Logger.getLogger(MongoExoirt.class.getName()).log(Level.SEVERE, null, ex);
                MySQLDB.Close();
            }

        }

    }
    /*
     public static void searchVenues(String ll) throws FoursquareApiException {
     // First we need a initialize FoursquareApi. 
      
     //          https://api.foursquare.com/v2/venues/search?client_id=SV5TE3OAE5CSFSPV4BH520SW2RPXCK24CARNTRTSCBEBMKGQ&client_secret=DNUHTOBW3XJGSLKRNH51GDUUJZCBWRB515X15QJGKHXHVBXK&ll=40.7,-74&query=sushi&v=20140806&m=foursquare
      
     FoursquareApi foursquareApi = new FoursquareApi("SV5TE3OAE5CSFSPV4BH520SW2RPXCK24CARNTRTSCBEBMKGQ", "DNUHTOBW3XJGSLKRNH51GDUUJZCBWRB515X15QJGKHXHVBXK", "https://acupressure4toowoomba.wordpress.com/");
    
     // After client has been initialized we can make queries.
    
     Result<VenuesSearchResult> result = foursquareApi.venuesSearch(ll, null, null, null, null, null, null, null, null, null, null);
    
     if (result.getMeta().getCode() == 200) {
     // if query was ok we can finally we do something with the data
     for (CompactVenue venue : result.getResult().getVenues()) {
     // TODO: Do something we the data
     System.out.println(venue.getName());
     }
     } else {
     // TODO: Proper error handling
     System.out.println("Error occured: ");
     System.out.println("  code: " + result.getMeta().getCode());
     System.out.println("  type: " + result.getMeta().getErrorType());
     System.out.println("  detail: " + result.getMeta().getErrorDetail()); 
     }
     }
     */

    private static boolean ReadSaveStopFile(String savefilelist) {
                BufferedReader reader = null;

		try {
                    
			reader = new BufferedReader(new FileReader(new File(savefilelist)));

			String line = null;
			while ((line = reader.readLine()) != null) {                            
                            String [] tempS=line.split(",");
                            if (tempS==null) continue;
                            for (String x:tempS){
                                    String y=x.trim();
                                    if (y.isEmpty()) continue;
                                    if (y.charAt(0)=='[') break;
                                    if (y==null) continue;
                                    MultiStopwordsTest.addSaveWords(y);
                            }                            
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
                        return false;
		} catch (IOException e) {
			e.printStackTrace();
                        return false;
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
                return true;
    }
}

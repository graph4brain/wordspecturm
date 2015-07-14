/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Rpackage;

import util.YelpAPI;
import util.FoursquareAPI_backup;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.Bytes;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import static java.util.regex.Pattern.CASE_INSENSITIVE;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author uqgzhu1
 */
public class MongoFixPostCode {

    public static void main(String[] args) {

        YelpAPI yelpApi = new YelpAPI();
        JSONParser parser = new JSONParser();
        try {

            // Connect to mongodb
            MongoClient mongo = new MongoClient("10.33.2.142", 27017);

            // get database
            // if database doesn't exists, mongodb will create it for you
            DB db = mongo.getDB("test");

            // get collection
            // if collection doesn't exists, mongodb will create it for you
            DBCollection collection = db.getCollection("twitter_Stream");

            DBCursor cursor;
            BasicDBObject query;
            //------------------------------------
            // ( 1 ) collection.find() --> get all document
            
                DBObject clause1 = new BasicDBObject("Latitude", new BasicDBObject("$exists", true));  
                DBObject clause2 = new BasicDBObject("tweet_Country_Code", new BasicDBObject("$exists", true));    
                DBObject clause3 = new BasicDBObject("R_Post_Code", new BasicDBObject("$exists", false));    
/*{
  "$and":[
    {
      "tweet_Country_Code":{
        "$exists":true
      }
    },
    {
      "Latitude":{
        "$exists":false
      }
    },
    {
      "tweet_Country_Code":"AU"
    }
  ]
}                
*/                
                BasicDBList or = new BasicDBList();
                or.add(clause1);
                or.add(clause2);                
                or.add(clause3);                
                query = new BasicDBObject("$and", or);
            
            //{  "$and":[    {      "Latitude":{        "$exists":true      }    },    {      "R_Post_Code":{        "$exists":false      }    }  ]}            
            cursor = collection.find(query).addOption(Bytes.QUERYOPTION_NOTIMEOUT)  //
                    .addOption(Bytes.QUERYOPTION_AWAITDATA);
            
            System.out.println("( 1 ) .get the user id within  latitide");
            System.out.println("results --> " + cursor.count());
            FoursquareAPI_backup qui4squreApi = new FoursquareAPI_backup();
            try {
                while (cursor.hasNext()) {
                    DBObject data = cursor.next();
                    String v_user_Name = (String) data.get("user_name");
                    Long v_twitte_id = (Long) data.get("tweet_ID");
                    String v_twitte_text = (String) data.get("tweet_text");
                    Long v_user_Id = (Long) data.get("user_Id");
                    Double v_Latitude = (Double) data.get("Latitude");
                    Double v_Longtitude = (Double) data.get("Longitude");
                    String v_tweet_Place_Name = (String) data.get("tweet_Place_Name");
                    String v_tweet_Country_Code = (String) data.get("tweet_Country_Code");

                    if (v_user_Id == null) {
                        /*                        System.out.print("update:" +v_user_Name+"/status/"+ v_twitte_id);
                         try{
                         Status status = twitter.showStatus(v_twitte_id);
                         BasicDBObject jo = GetMongoRecord(status);
                         System.out.println( "-->" + status.getId() + " : " + jo.getString("Re_user_screenName") + ":" + jo.getString("tweet_text"));
                         collection.update(new BasicDBObject("tweet_ID", v_twitte_id), jo); // set the document in the DB to the new document for Jo                     
                         }catch (TwitterException ex){
                         if (ex.getStatusCode()==144) continue;
                         }
                         */
                        continue;
                    }
                    JSONObject businesses = yelpApi.search4Yelp("city", v_Latitude, v_Longtitude);//-27.497835,153.017472);                      
                    boolean searchAgain = false;
                    
                    if (businesses == null) searchAgain = true;
                    else if (businesses.size() < 1) searchAgain = true;
                    
                    if (searchAgain) {
                        System.out.println("La:" + v_Latitude + "\tLo:" + v_Longtitude);
                        businesses = qui4squreApi.search4Square("city", v_Latitude, v_Longtitude);
                        searchAgain=false;
                    }
                    if (businesses == null) searchAgain = true;
                    else if (businesses.size() < 1) searchAgain = true;
                    
                   if (searchAgain) {
                            businesses = qui4squreApi.searchGoogleMap("city", v_Latitude, v_Longtitude);
                            if (businesses == null) {                                
                                System.out.println("\t" + v_tweet_Country_Code + "\t:" + v_tweet_Place_Name);
                                continue;                               
                            } else if (businesses.size() < 1) {
                                System.out.println("\t" + v_tweet_Country_Code + "\t:" + v_tweet_Place_Name);
                                continue;
                            }                    
                   }

                    String country_code = (String) businesses.get("country_code");
                    String city_code = (String) businesses.get("city_name");
                    String state_code = (String) businesses.get("state_code");
                    String post_code = (String) businesses.get("post_code");

        
                    BasicDBObject setNewFieldQuery = new BasicDBObject().append("$set",
                            new BasicDBObject().append("R_Country_Code", country_code)
                            .append("R_State_Code", state_code)
                            .append("R_City", city_code)
                            .append("R_Post_Code", post_code)
                    );
                    
                    collection.update(new BasicDBObject().append("tweet_ID", v_twitte_id), setNewFieldQuery); // set the document in the DB to the new document for Jo                                             
                    
                    setNewFieldQuery.clear();
                    setNewFieldQuery = null;
                }
            } finally {
                cursor.close();
            }

            System.out.println("---------------------------------");

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (MongoException e) {
            e.printStackTrace();
        }

    }

    /*
     private static BasicDBObject GetMongoRecord(Status tweet) {
     BasicDBObject basicObj = new BasicDBObject();
     basicObj.put("user_Rname", tweet.getUser().getName());
     basicObj.put("user_name", tweet.getUser().getScreenName());
     basicObj.put("retweet_count", tweet.getRetweetCount());
     basicObj.put("tweet_followers_count", tweet.getUser().getFollowersCount());

     UserMentionEntity[] mentioned = tweet.getUserMentionEntities();
     basicObj.put("tweet_mentioned_count", mentioned.length);
     basicObj.put("tweet_ID", tweet.getId());
     basicObj.put("tweet_text", tweet.getText());
     Status temp1 = tweet.getRetweetedStatus();
     if (temp1 != null) {
     basicObj.put("Re_user_ID", temp1.getUser().getId());
     basicObj.put("Re_user_screenName", temp1.getUser().getScreenName());
     basicObj.put("Re_tweet_ID", temp1.getId());
     }
     Long ReplayTwitter = tweet.getInReplyToStatusId();
     if (ReplayTwitter > 0) {
     basicObj.put("ReP_user_ID", tweet.getInReplyToUserId());
     basicObj.put("ReP_user_screenName", tweet.getInReplyToScreenName());
     basicObj.put("ReP_tweet_ID", ReplayTwitter);
     }

     GeoLocation loc = tweet.getGeoLocation();
     if (loc != null) {
     basicObj.put("Latitude", loc.getLatitude());
     basicObj.put("Longitude", loc.getLongitude());
     }
     basicObj.put("CreateTime", tweet.getCreatedAt());
     basicObj.put("FavoriteCount", tweet.getFavoriteCount());
     basicObj.put("user_Id", tweet.getUser().getId());
     String mylocation = tweet.getUser().getLocation();
     if (mylocation != null) {
     basicObj.put("user_location", mylocation);
     }

     if (tweet.getUser().getTimeZone() != null) {
     basicObj.put("UsertimeZone", tweet.getUser().getTimeZone());
     }
     if (tweet.getUser().getStatus() != null) {
     basicObj.put("UserStatus", tweet.getUser().getStatus());
     }
     //basicObj.put("tweetLocation", tweet.getPlace().getGeometryCoordinates());
     //String U_Loc=tweet.getUser().getLocation();                if (U_Loc!=null) basicObj.put("userLocation", U_Loc);
     basicObj.put("number_of_rt", tweet.getRetweetCount());
     //basicObj.put("isRetweet", tweet.getPlace().getGeometryCoordinates());                
     //basicObj.put("POS", tweet.getWithheldInCountries());
     if (mentioned.length > 0) {
     basicObj.append("mentions", pickMentions(mentioned));
     }

     Place temPL = tweet.getPlace();
     if (temPL != null) {
     basicObj.put("tweet_Street", temPL.getStreetAddress());
     basicObj.put("tweet_Country", temPL.getCountry());
     //basicObj.put("tweet_Country", temPL.getBoundingBoxCoordinates());                    
     }
     basicObj.put("tweet_Lan", tweet.getLang());
     basicObj.put("user_Lan", tweet.getUser().getLang());
     basicObj.put("user_CreateTime", tweet.getUser().getCreatedAt());

     return basicObj;

     }

     private static Object pickMentions(UserMentionEntity[] mentioned) {
     LinkedList<String> friends = new LinkedList<String>();

     for (UserMentionEntity x : mentioned) {
     //String  y=x.getName();// Long.toString(x.getId());
     friends.add(x.getScreenName());
     }
     String a[] = {};
     return friends.toArray(a);

     }
     */
}

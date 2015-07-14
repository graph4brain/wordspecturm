/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package guesslocation;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

import java.net.UnknownHostException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import static java.util.regex.Pattern.CASE_INSENSITIVE;

/**
 *
 * @author uqgzhu1
 */
public class MongoQuery {

    public static void main(String[] args) {

        {

            try {

                // Connect to mongodb
                MongoClient mongo = new MongoClient("localhost", 27017);

        // get database
                // if database doesn't exists, mongodb will create it for you
                DB db = mongo.getDB("test");

        // get collection
                // if collection doesn't exists, mongodb will create it for you
                DBCollection collection = db.getCollection("twitter");
                DBCollection Outcollection = db.getCollection("user_tw");
                DBCursor cursor;
                BasicDBObject query;
        //------------------------------------
                // ( 1 ) collection.find() --> get all document
                cursor = collection.find();
                System.out.println("( 1 ) .find()");
                System.out.println("results --> " + cursor.count());
                
                try {
                    BasicDBObject IDquery = new BasicDBObject(); //2015-05-12T15:15:31Z
                    while (cursor.hasNext()) {
                        DBObject data = cursor.next();
                        Long v_user_Id = (Long) data.get("user_Id");
                        if (v_user_Id == null) {
                            continue;
                        }
                        
                        IDquery.append("user_Id", v_user_Id);
                        DBCursor IDcursor = Outcollection.find(IDquery);
                        if (IDcursor.hasNext() == false) {
                            BasicDBObject basicObj = GetUserRecord(v_user_Id, data);
                            try {
                                Outcollection.insert(basicObj);                                
                            } catch (Exception e) {
                                System.err.println("error on insert "+v_user_Id);
                            }
                            basicObj = null;
                            Thread.sleep(100);
                            Outcollection.ensureIndex(new BasicDBObject("user_Id", 1), new BasicDBObject("unique", true));
                        }
                        IDcursor.close();
                        IDquery.clear();
                    }
                } catch (InterruptedException ex) {
                    Logger.getLogger(MongoQuery.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    cursor.close();
                }

                System.out.println("---------------------------------");
                System.exit(0);

            } catch (UnknownHostException ex) {
                Logger.getLogger(MongoQuery.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

    /*  CREATE TABLE IF NOT EXISTS `users` (
     `profile_image_url` varchar(200) DEFAULT NULL,
     `url` varchar(200) DEFAULT NULL,
     `description` varchar(200) DEFAULT NULL,
     `created_at` datetime NOT NULL,
     `friends_count` int(10) unsigned DEFAULT NULL,
     `statuses_count` int(10) unsigned DEFAULT NULL,
     `time_zone` varchar(40) DEFAULT NULL,
     `last_update` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP 
     ON UPDATE CURRENT_TIMESTAMP,
     PRIMARY KEY (`user_id`),
     KEY `user_name` (`name`),
     KEY `last_update` (`last_update`),
     KEY `screen_name` (`screen_name`),
     FULLTEXT KEY `description` (`description`)
     ) ENGINE=MyISAM DEFAULT CHARSET=utf8;
     */
    private static BasicDBObject GetUserRecord(Long v_user_id, DBObject data) {
        BasicDBObject basicObj = new BasicDBObject();

        String v_real_user_Name = (String) data.get("user_Rname");

        Integer v_user_followers_count = (Integer) data.get("tweet_followers_count");  //user_followers_count
        Integer v_FavoriteCount = (Integer) data.get("FavoriteCount");

        String mylocation = (String) data.get("user_location");
        if (mylocation != null) {
            basicObj.put("user_location", mylocation);
        }

        basicObj.put("user_Id", v_user_id);
        basicObj.put("user_screen_name", data.get("user_name"));
        basicObj.put("user_real_name", data.get("user_Rname"));
        basicObj.put("follows_count", v_user_followers_count);
        basicObj.put("favorite_count", v_FavoriteCount);

        String v_UsertimeZone = (String) data.get("UsertimeZone");
        if (v_UsertimeZone != null) {
            basicObj.put("UsertimeZone", v_UsertimeZone);
        }

        Date v_CreateTime = (Date) data.get("CreateTime");

        String Lan = (String) data.get("user_Lan");
        if (Lan != null) {
            basicObj.put("user_Lan", Lan);
        }

        Date CreateTime = (Date) data.get("user_CreateTime");
        if (CreateTime != null) {
            basicObj.put("user_CreateTime", Lan);
        }

        Date dateobj = new Date();
        basicObj.put("update_date", dateobj.toString());

        //System.out.println(v_user_id + ":");
        return basicObj;
    }
}

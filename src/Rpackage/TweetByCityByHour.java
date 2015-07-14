/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Rpackage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import util.TfIdf;

/**
 *
 * @author uqgzhu1
 */
public class TweetByCityByHour    implements Map {
        private ArrayList < String > content_List;
        private ArrayList < Long>   User_ID_List;    //length is number of user
        private ArrayList < Long>   Tweet_ID_List;    //length is number of user        
        private Integer tweet_Number; 
        private int Year; 
        private int Month; 
        private int Day; 
        private int Hour; 
        
        TweetByCityByHour(){
            super();
            content_List=new ArrayList < String >();
            User_ID_List=new ArrayList < Long>();
            Tweet_ID_List=new ArrayList < Long>();
                    tweet_Number=0;
                    Year=0;
                    Month=0;
                    Day=0;
                    Hour=0;
        }
        
        @Override
        public int size() {
            return tweet_Number;
        }

        @Override
        public boolean isEmpty() {
            if (tweet_Number==0) return true;
            return false;
        }

        @Override
        public boolean containsKey(Object o) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public boolean containsValue(Object o) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Object get(Object o) {
            Long  tempUser=(Long)o;
            boolean notfound=true;
            int i=0;
            for (i=0;i<User_ID_List.size()&&notfound;i++)
                if (User_ID_List.get(i)==tempUser) notfound=false;             
            if (notfound){ return null;
            }else{
                return (content_List.get(i));                
            }            
        }

        public Long  getUserID(int index){
             if (index>=User_ID_List.size()) return Long.MIN_VALUE;
             
            return User_ID_List.get(index);
        }
        @Override
        public Object put(Object k, Object v) {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        public Object put(Long tempUser, String v) {

            char []ArrayTemp=TfIdf.asciiToHex(v).toCharArray();
            for (char x: ArrayTemp){
                System.out.print(x);
                System.out.print('\t');
            }
            tweet_Number++;            
            boolean notfound=true;
            int i=0;
            for (i=0;i<User_ID_List.size();i++){
                //System.out.println(User_ID_List.get(i));
                if (User_ID_List.get(i).longValue()==tempUser) notfound=false;             
                if (!notfound) break;
            }
            if (notfound){
                //System.out.println("new key="+tempUser);
                User_ID_List.add(tempUser);
                content_List.add(v);
            }else{
                //System.out.println(i+"old key="+tempUser);
                String temps=content_List.get(i);                
                content_List.set(i, temps.concat((String)v));
            }
            return content_List;
        }

        public Object Add(Long tempUser, Long tweet_ID, int year, int month, int day, int hour, String v) {
            tweet_Number++;            
            boolean notfound=true;
            Tweet_ID_List.add(tweet_ID);
            this.Year=year;
            this.Month=month;
            this.Day=day;
            this.Hour=hour;
            int i=0;
            for (i=0;i<User_ID_List.size();i++){
                //System.out.println(User_ID_List.get(i));
                if (User_ID_List.get(i).longValue()==tempUser) notfound=false;             
                if (!notfound) break;
            }
            if (notfound){
                //System.out.println("new key="+tempUser);
                User_ID_List.add(tempUser);
                content_List.add(v);
            }else{
                //System.out.println(i+"old key="+tempUser);
                String temps=content_List.get(i);                
                content_List.set(i, temps.concat((String)v));
            }
            return content_List;
        }
        
        @Override
        public Object remove(Object o) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void putAll(Map map) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void clear() {
            content_List.clear();
            User_ID_List.clear();    //length is number of user            
        }

        @Override
        public Set keySet() {
            HashSet<Long> foo = new HashSet< Long>(User_ID_List);            
            return foo;
        }

        @Override
        public Collection values() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Set entrySet() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

    int getUserSize() {
        return User_ID_List.size();
    }

    int getTweetSize() {
       return this.tweet_Number;
    }

    String getAllTweet() {
            StringBuffer tempS= new StringBuffer();            
    /*        for (int i=0;i<Tweet_ID_List.size(); i++){
                    Long  tempTID=Tweet_ID_List.get(i);
                    tempS.append(tempTID.toString());
                    tempS.append(',');
            }
*/            
            for (int i=0;i<content_List.size(); i++){
                    String temps1=content_List.get(i);        
                    tempS.append(temps1);
            }
          return tempS.toString();
      }

    /**
     * @return the Year
     */
    public int getYear() {
        return Year;
    }

    /**
     * @return the Month
     */
    public int getMonth() {
        return Month;
    }

    /**
     * @return the Day
     */
    public int getDay() {
        return Day;
    }

    /**
     * @return the Hour
     */
    public int getHour() {
        return Hour;
    }
}

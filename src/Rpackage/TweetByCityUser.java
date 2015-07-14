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
import static util.TfIdf.asciiToHex;

/**
 *
 * @author uqgzhu1
 */
    public class TweetByCityUser  {
//        private ArrayList < String > content_List;
  //      private ArrayList < Long>   User_ID_List;    //length is number of user
    //    private ArrayList < Long>   Tweet_ID_List;    //length is number of user        
        private ArrayList <TweetByCityByHour> EachHourList;
        
        private ArrayList <Integer> hourList;                
        private int tweet_Number_by_eachCity; 
        
        TweetByCityUser(){
                    super();
                    EachHourList= new ArrayList <TweetByCityByHour> ();
                    hourList=new ArrayList <Integer> ();
                    tweet_Number_by_eachCity=0;
        }
        
        
        public int size() {
            return hourList.size();
        }

        
        public boolean isEmpty() {
            if (tweet_Number_by_eachCity==0) return true;
            return false;
        }

        //intput  hour,  return all user's tweet_Id at hour

        

        
        

        public void clear() {
            EachHourList.clear();
        }

        public Set <Integer> keySet() {
            HashSet<Integer> foo = new HashSet< Integer>(hourList);            
            return foo;
        }


/*
    this will be mistaken if statistal by hour
    int getUserSize() {
        int sum=0;
        for (int i=0;i<EachHourList.size();i++)
            sum+=EachHourList.get(i).getUserSize();
        return sum;
    }
*/
    int getTweetSize() {
       return this.tweet_Number_by_eachCity;
    }
    
    int getHowmanyHour() {
       return this.hourList.size();
    }

    String getAllTweet() {
            StringBuffer tempS= new StringBuffer();            
            for (int i=0;i<EachHourList.size();i++){
                String tempS1=EachHourList.get(i).getAllTweet();           
                    tempS.append(tempS1);
            }
          return tempS.toString();
      }
    
     public static void main(String a[]){
        TweetByCityUser hm = new TweetByCityUser ();
        //add key-value pair to TreeMap
        
//        hm.put(Long.valueOf(301686620),Long.valueOf(301686619), 2007,5,6,1,"1temple มาขอพร nan tien t.co/vaqlehqx ");        
  //      hm.put(Long.valueOf(301686620),Long.valueOf(301686621), 2007,5,6,7, "2SECOND INSERTED");
    //    hm.put(Long.valueOf(301686621),Long.valueOf(301686622), 2007,5,6,1,"3THIRD INSERTED3 ");
      //  hm.put(Long.valueOf(301686621),Long.valueOf(301686623), 2007,5,6,7,"4 INSERTED3 ");

        //hm.put(Long.valueOf(301686620),Long.valueOf(301686624), 2007,5,6,1,"5 INSERTED3 ");
        //hm.put(Long.valueOf(301686621),Long.valueOf(301686625), 2007,5,6,7,"6 INSERTED3 ");

        //hm.put(Long.valueOf(301686620),Long.valueOf(301686626), 2007,5,6,3,"7 INSERTED3 ");
        //hm.put(Long.valueOf(301686621),Long.valueOf(301686627), 2007,5,6,7,"8 INSERTED3 ");
        
        System.out.println(hm);
        //getting value for the given key from TreeMap
        System.out.println("Is TreeMap empty? "+hm.isEmpty());
    //    hm.remove("third");
        System.out.println(hm);
//        System.out.println("Size of the User: "+hm.getUserSize());
        System.out.println("Size of the Twtitter: "+hm.getTweetSize());
        System.out.println("output each hour tweet:"+hm.getHowmanyHour());
        for (Integer x: hm.keySet() ){
            TweetByCityByHour tempx=hm.getTweetByHour(x);
            if(tempx==null) continue;
            System.out.println(x+"\t:\t"+tempx.getUserSize()+"\t"+tempx.getTweetSize()+":\t"+tempx.getAllTweet());                    
        }
        
    }

    public TweetByCityByHour getTweetByHour(Integer x) {
                boolean notfound=true;
            int i=0;
            for (i=0;i<hourList.size();i++){
                if (hourList.get(i).intValue()==x.intValue()) notfound=false;             
                if (!notfound) break;
            }
            if (notfound){ return null;
            }else{
                return EachHourList.get(i);                
            }            

    }

    public boolean put(Long v_user_Id, Long v_tweet_ID, int v_Year, int v_Month, int v_Day, int v_Hour, String newv_tweet_text, String Tempv_tweet_City) {
                tweet_Number_by_eachCity++;            
            boolean notfound=true;
            int i=0;
            for (i=0;i<hourList.size();i++){
                //System.out.println(User_ID_List.get(i));
                if (hourList.get(i).intValue()==v_Hour) notfound=false;
                if (!notfound) break;
            }
            if (notfound){  //this hour no date
                TweetByCityByHour temp=new TweetByCityByHour();
                temp.Add(v_user_Id, v_tweet_ID, v_Year, v_Month, v_Day, v_Hour, newv_tweet_text);
                EachHourList.add(temp);
                hourList.add(v_Hour);
            }else{
                //System.out.println(i+"old key="+tempUser);
                TweetByCityByHour temp=EachHourList.get(i);
                temp.Add(v_user_Id, v_tweet_ID, v_Year, v_Month, v_Day, v_Hour, newv_tweet_text);
            }
            return true;
}

    
    };
    
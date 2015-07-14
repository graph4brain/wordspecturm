library(plyr)
library(rmongodb)

gameids = data.frame(stringsAsFactors = F)

MDB<-mongo.create()
mongo.is.connected(MDB)
json_qry<-'{Place_Type:{"$exist":true}}'

fields = mongo.bson.buffer.create()
mongo.bson.buffer.append(fields, "Record_Loc", 1L)
mongo.bson.buffer.append(fields, "tweet_Country_Code", 1L)
mongo.bson.buffer.append(fields, "UsertimeZone", 1L)
mongo.bson.buffer.append(fields, "user_name", 1L)
mongo.bson.buffer.append(fields, "tweet_text", 1L)
mongo.bson.buffer.append(fields, "tweet_Id", 0L)


buf <- mongo.bson.buffer.create()
mongo.bson.buffer.start.object(buf, "name.first")
mongo.bson.buffer.append(buf, "$in", c("Alex", "Horst"))
mongo.bson.buffer.finish.object(buf)

criteria <- mongo.bson.from.buffer(buf)

MyQuery=list('{"tweet_Country_Code":{"$exists":true}}','{"Record_Loc":"USA"}')
MyQuery='{ $and: [ {"Record_Loc":"USA"}, {"tweet_Country":{"$exists":true}} ] }'


MyQuery='{"tweet_Country":{"$exists":true}}'
cur<-mongo.find(MDB,"test.twitter_Stream",query=MyQuery,limit = 150L)

res <- mongo.cursor.to.data.frame(cur, stringsAsFactors=FALSE)

export = data.frame(stringAsFactors = FALSE)
i = 1
while(mongo.cursor.next(cur))
{
    tmp = mongo.bson.to.list(mongo.cursor.value(cur))
    tmp.df = as.data.frame(t(unlist(tmp)), stringAsFactors = FALSE)
    export = rbind.fill(export, tmp.df)
i = i + 1
}


summary(res$tweet_Country)
summary(res$Place_Country_Code)

with(res, table(tweet_Place_Name))
tempResCity=with(res, table(tweet_Place_Name))
plot(tempResCity)





 today = format(Sys.time(), "%Y-%m-%d")
 buf = mongo.bson.buffer.create()
 mongo.bson.buffer.append.string(buf,"CreateTime",today)
 query = mongo.bson.from.buffer(buf)
 todays.readings.cursor = mongo.find(MDB,"test.twitter_Stream",query)
res <- mongo.cursor.to.data.frame(todays.readings.cursor)



wodups<-res$Place_Name
wodups <- wodups[which(!duplicated(res$Place_Name))]


















while(mongo.cursor.next(cur)){
    Myvalue=mongo.cursor.value(cur)
    tmp = mongo.bson.to.list(Myvalue)
    tmp.df = as.data.frame(t(unlist(tmp)), stringsAsFactors = F)
    # bind to the master dataframe
    gameids = rbind.fill(gameids, tmp.df)
}
mongo.cursor.destroy(cur)


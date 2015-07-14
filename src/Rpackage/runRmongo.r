library(RMongo)
mg1 <- mongoDbConnect('test')
print(dbShowCollections(mg1))

query <- dbGetQuery(mg1, 'test', "{'AGE': {'$lt': 10}, 'LIQ': {'$gte': 0.1}, 'IND5A': {'$ne': 1}}")

query <- dbGetQuery(mg1, 'twitter_Stream','{"tweet_Country":{"$exists":true}}')

query <- dbGetQuery(mg1, 'twitter_Stream','{ $and: [ {"tweet_Country":{"$exists":true}}, {"tweet_Country_Code":"AU"},{"tweet_Lan":"en"} ]}')

data1 <- query[c('tweet_text','tweet_Place_Name')]

data1[,1] = apply(data1[-2], 1, as.character)

City_Tweet=aggregate(data1[-2], by=list(data1$tweet_Place_Name), c)
# remove retweet entities
some_txt = gsub("(RT|via)((?:\\b\\W*@\\w+)+)", "", City_Tweet$tweet_text)
# remove punctuation
some_txt = gsub("[[:punct:]]", "", some_txt)
# remove html links
some_txt = gsub("http\\w+", "", some_txt)
# remove unnecessary spaces
some_txt = gsub("[ \t]{2,}", "", some_txt)
some_txt = gsub("^\\s+|\\s+$", "", some_txt)
# lower case using try.error with sapply 
some_txt = sapply(some_txt, try.error)
some_txt = some_txt[!is.na(some_txt)]
names(some_txt) = NULL



summary(data1)

data1 <- query$tweet_text

query$tweet_Country_Code

with(query, table(tweet_Country_Code,tweet_Place_Name))

tempResCity=with(query, table(tweet_Place_Name))
plot(tempResCity)





# define "tolower error handling" function 
try.error = function(x)
{
   # create missing value
   y = NA
   # tryCatch error
   try_error = tryCatch(tolower(x), error=function(e) e)
   # if not an error
   if (!inherits(try_error, "error"))
   y = tolower(x)
   # result
   return(y)
}

# lower case using try.error with sapply 
some_txt = sapply(some_txt, try.error)
some_txt = some_txt[!is.na(some_txt)]
names(some_txt) = NULL

corpus2<-Corpus(VectorSource(some_txt))
reuters <- tm_map(corpus2, stripWhitespace)
dtm <- DocumentTermMatrix(reuters)
inspect(dtm[1:20, 1:15])

reuters <- tm_map(corpus2, stripWhitespace)
reuters <- tm_map(reuters, tolower)


reuters <- tm_map(reuters, removeNumbers)
reuters <- tm_map(reuters, removeWords, stopwords("english"))



dtm <- DocumentTermMatrix(reuters,control = list(weighting =function(x)
                                         weightTfIdf(x, normalize =FALSE),stopwords = TRUE))


DocumentTermMatrix(reuters, list(dictionary = c("prices", "crude", "oil"))))

inspect(dtm[5:10, 34:37])
findFreqTerms(dtm[3,],2)

findFreqTerms(dtm, 5)
findAssocs(dtm, "opec", 0.8)
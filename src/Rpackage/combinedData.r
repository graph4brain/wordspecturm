dataset1 <- data.frame(A = c("This is a test A", "This is a test B","This is a test C","This is a test A1","This is a test C2"), 
                       F = c("F1", "F2", "F3","F1","F3" ))

dataset1[1] = apply(dataset1[-2], 1, as.character)
City_Tweet=aggregate(dataset1[-2], by=list(dataset1$F), c)


library(tm)
documents <- c('People are so quick to give-up & say someone is "too much to handle".. Have you ever thought about what else they could be dealing with ?')
#documents <- c("the quick brown fox jumps over the lazy dog", "i am the walrus")
corpus <- Corpus(VectorSource(documents))
matrix <- DocumentTermMatrix(corpus,control=list(stopwords=TRUE))


matrix <- DocumentTermMatrix(corpus,control=list(tokenize=scan_tokenizer,stopwords=TRUE))
colnames(matrix)




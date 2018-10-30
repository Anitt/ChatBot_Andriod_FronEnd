# ChatBot_Andriod_FronEnd
A chat-bot that uses Natural Language processing and Machine learning to recommend best restaurants based on user reviews
# FeedMe

## Project description

Yelp user reviews and ratings clearly play a major role in determining the fortune of a business as two professors Michael Anderson and Jeremy Magruder from university of California, Berkeley found that “restaurants with a rating improved by just a half a star on a scale of 1 to 5 was much more likely to be full at peak dining times”. However, it gets difficult and time consuming for users to read each review manually and identify the restaurants based on their preference. Furthermore, this leads user to make decision based on star ratings which can be misleading as ratings doesn’t provide the context and features on which a restaurant is rated. 

Feed Me is an Android application that recommends restaurants to users by learning and analyzing the user reviews in
YELP dataset. YELP is an open source dataset containing large number of reviews about the restaurants, the
application mainly aims to assist the users in finding the best restaurant under different categories such as ambienece, discount and deals based on the authentic user reviews through voice commands. We have implemented Sentimental Analysis and Topic modelling to predict the star rating of each restaurants. FeedMe app uses built-in speech recoginition API from Google SDK to attain speech to text capabilities. 

## Technology

**Language**: Java, Python, MySQL

**Tools/Frameworks**: Android Studio, pandas, numpy, scikit-learn, flask, RESTful API, IBM DB2

## Architecture

![alt text](https://github.com/Anitt/ChatBot_Andriod_FronEnd/blob/master/Architecture.JPG "Architecture")

Android Application : https://github.com/Anitt/ChatBot_Andriod_FrontEnd

Web Service: https://github.com/Anitt/Chatbot_WebServiceCall

Preprocessing scripts: https://github.com/Anitt/ChatBot_MachineLearning

## Dataset

**Link**: https://www.yelp.com/dataset

![alt text](https://github.com/Anitt/ChatBot_Andriod_FronEnd/blob/master/images/datasetSchema.png)

## Data preprocessing

Yelp dataset had a total of 5.2 million reviews for 1.7 million businesses. We did not have enough resources to build a model that can process 5.2 million reviews, so we decided to use a subset of the reviews in order to build our model. In order to get a subset of the reviews, we performed three different filters in our dataset. Below we have briefly explained the three filtering mechanisms applied on Yelp dataset.

### Category based filter

Our problem statement concentrated only on restaurants but Yelp dataset contained data about other businesses like shopping market, grocery shops, etc. First step was to filter out the data which were not related to restaurant domain. We made use of “categories” attributes present in the “Business” table to filter the unnecessary categories. We observed that there were a total of 1264 unique categories. We manually went through each and every category and made a list which contained categories specific to restaurants. Using the list, we found the “business_id” for each restaurant from the “business” table. “business_id” is a unique identifier(primary key) assigned to each business in the Yelp dataset. We made use of “business_id” attribute to filter data from other tables.

### Country-based filter

Even after filtering the data based on categories, we still had 3.2 million reviews and 75,000 businesses. This was still huge data to process, so we decided to filter based on country and make use of the restaurants which were located in Canada. Our dataset did not provide any details about the country of the restaurants, but they contained details like city, latitude and longitude. We performed reverse geocoding using the latitude and longitude to find the country of the restaurants. Once we had obtained the country of each restaurant, we filtered out the restaurants which were not located in Canada.

### Language-based filter

After applying Country and Category based filter, we had a total of 25,124 restaurants and approximately 8,000,000 reviews. Reviews tables consisted reviews of various different languages because of this we were not able to get accurate results, so we decided to concentrate on reviews which are in English language alone. We followed a stopword based approach to find the language of each review. We found the number of stopwords present in each review for each language supported by NLTK. We assume that the language which has the most number of stopwords is the language of the review. This method was quite effective and it accurately predicted the language for most of the reviews. Though this approach was not successful for some scenarios where reviews did not contain any stopwords, we manually went through those reviews and filtered it. Once we had detected the language, we filtered out non-English reviews. After all the filtering was done, we had a total of 6,65,000 reviews and 25,124 restaurants.

We made use of IBM DB2 Warehouse and Python packages like pandas, geopy to perform the above-mentioned filters. After filtering, we had uploaded our dataset to IBM cloud Database using batch script.

## Sentimental Analysis

Sentimental Analysis is a process of extracting the emotions expressed by a human via text unit. Emotions expressed by the author are most widely classified into three different categories namely positive, negative and neutral. In our project, we have applied sentimental analysis on restaurant reviews to identify the emotions of the users towards the restaurant. We rank and recommend restaurants based on the data generated via sentimental analysis of the reviews. 
		
We started off with a approach where we tried to find the sentiment of reviews based on the number of positive and negative words present in the review. We made use of the lexicon released as a part of the research paper titled “Generating Recommendation Dialogs by Extracting Information from User Reviews”. The lexicon contained a total of 1435 positive and 570 negative words related to restaurant domain. We observed that this approach did not yield appropriate results, we found that it was because we were taking into sentiments of words without considering the words surrounding them. 

For example, a review like “The food was not that tasty.” would fetch a positive sentiment in this approach as tasty is the only positive word present in the review. As we were not considering the words around “tasty”, it gave us wrong results. In order to overcome the drawback of this approach, we considered taking into account two words at a time but that approach also did not yield very good results. So we decided to find the sentiment of each sentence present in the review. 

We made use of “TextBlob” and “NLTK” to determine the sentiment of sentences. TextBlob and NLTK give us a score between -1 to 1, -1 would mean that it is a very negative sentiment and 1 would mean that is a positive sentiment. TextBlob names it as polarity score whereas NLTK names it as compound score. We find the sentiment of each sentence in the review and add the scores to find the overall sentiment score of the review. In order to normalize the sentiment scores, we divide the score by number of sentences in the review. We don’t take into account the sentences which give us a score of 0, which means it is subjective sentence and it has neutral sentiment. 

Once we had calculated the sentiment score of each reviews, we group reviews for each restaurants and calculate the sentiment score for the restaurant. We add up the sentiment score of each review and normalize it by dividing the score by number of reviews present for each restaurant. Once we had calculated both NLTK score(compound) and TextBlob score(polarity) we convert it to a score between 1-5. We find the average of star values generated from NLTK and TextBlob. Average value would act as the star rating for the restaurant. We used the generated star rating as a measure to determine the quality of the restaurants.


## Topic modelling

Based on sentimental analysis of the reviews, we were able to obtain a star rating for each restaurant in the dataset. Even though star rating will give a good idea about the restaurant to the user, we felt that it would be more useful if we can give some more information regarding the restaurant to the user. So we came up with an idea where we would give a star rating for four important aspects namely food, ambience, deals and discounts and service. We made use of LDA model to obtain details about the four categories from the reviews.

Given a set of documents, Latent Dirichlet Allocation(LDA) helps in finding the topics which are related to the documents. We applied LDA model on the reviews to extract the topics related to the  reviews. We grouped the reviews by restaurants and we performed LDA for each restaurant reviews. Each review for a restaurant acted as a document and the list of reviews(documents) were the input to LDA. Before applying LDA, we had to process the reviews in order to get better outputs from the model. 

As a first step, we tokenized the reviews into a list of words by using NLTK word tokenizer. We then removed punctuations and stopwords from the word tokenized list. As a next step, we performed lemmatization for each word and reduced the inflected words to their base words. Once all the cleansing had been done, we converted the data into a document term matrix using python “gensim” package. We then applied the LDA model on the document term matrix, we set the Number of Topics parameter to 10 and number of words per topic parameter to 5. By setting those parameter, we would get an output of 10 topics and each topic would contain 5 words. 

LDA model now gave us a list of words which contributed to the topic of the reviews. We iterated through the list and removed the duplicate words from the list. We then made use of the list from a github repository[8] to create a bag of words for each category(food, ambience, deals and discounts and service). Now we had a set of words which contribute to the topic of the reviews and a bag of words for each category. We performed a intersection between set of words and the bag of words of each category. If intersected set was empty, we came to a conclusion that reviews did not speak about the category. If the set was not empty then we took the intersected list of words and found the sentiment score of the each sentences which contained the intersected word. We added up the sentiment score and divided it by the number of sentences in which it appeared to get a normalized sentiment score of the category. We then converted the score into a star rating ranging from 1-5. By using this approach we were able to assign a star rating for the four categories.



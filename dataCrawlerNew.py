from pymongo import MongoClient
import requests
import urllib
import urllib2
import os
from bs4 import BeautifulSoup
from time import gmtime, strftime

forbiden=["<",">",":","/","\\","|","?","*"]
url="http://www.bbc.co.uk"
r=requests.get(url+"/news")
soup=BeautifulSoup(r.content,"lxml")
temp=soup.find_all("a",{"class":"navigation-wide-list__link"})
NewsTopics=[]
NewsTopicsLinks=[]

client = MongoClient()
db = client.articles
collection = db.articles_collection
dictionary={}
#dynamodb = boto3.resource('dynamodb', region_name='us-west-2', endpoint_url="http://localhost:8000")
#table = dynamodb.Table('articles')
alpha=True
countname=0
while True:
    CurrentTime=strftime("%H:%M", gmtime())
    if alpha == True or str(CurrentTime)=="00:01" or str(CurrentTime)=="04:00"or str(CurrentTime)=="08:00"or str(CurrentTime)=="12:00"or str(CurrentTime)=="16:00"or str(CurrentTime)=="20:00":
        alpha=False
        if str(CurrentTime)=="00:01":
            db.articles_collection.drop()
            countname=0
        for i in temp:
            NewsTopics.append(i.text)
            NewsTopicsLinks.append(i.get("href"))
        temp=[]
        count=0
        for i in NewsTopicsLinks:
            r=requests.get(url+i)
            soup=BeautifulSoup(r.content,"lxml")
            temp=soup.find_all("a",{"class":"title-link"})
            
            for j in temp:
                if not("http" in j.get("href") ):
                    r=requests.get(url+j.get("href"))
                    soup=BeautifulSoup(r.content,"lxml")

                    articleContext=soup.find_all("div",{"class":"story-body"})
                    category=NewsTopics[count].replace("\n","")
                    title=j.text.replace("\n","")
                    maintext=""
                    date=""
                    author="BBC news"
                    for k in articleContext:
                        #me=k.text.encode("utf-8")
                        for q in k.find_all("p"):
                            maintext=maintext+q.text
                            
                        dates=k.find_all("div",{"class":"date date--v2"})
                        authors=k.find_all("span",{"class":"byline__name"})

                        for k in authors:
                            if len(k)==None:
                                author="BBC news"
                            else:
                                author=k.text.replace("By ","")
                        if len(dates)==0:
                            date=strftime("%Y-%d-%m", gmtime())
                        else:
                            for q in dates:
                                date=q['data-datetime']

                        images =k.find_all("img",{"class":"js-image-replace"})
                        imagename=""
                        for image in images:
                            urllib.urlretrieve(image['src'], filename="/home/panagiotis/Documents/ceGroup Project/Group6project/images/"+str(countname))
                            imagename=image['alt']
                        countname=countname+1
                           
                        dictionary={'category':category,
                                    'title': title,
                                    'author':author,
                                    'maintext':maintext,
                                    'date':str(date),
                                    'ImageLocation':imagename
                            }
                        collection.insert(dictionary)




            count=count+1
                    
                
                      
        

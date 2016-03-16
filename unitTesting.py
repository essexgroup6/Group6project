from pymongo import MongoClient
import requests
import urllib
import urllib2
import os
import unittest
from bs4 import BeautifulSoup
from time import gmtime, strftime

def testfunction():
    forbiden=["<",">",":","/","\\","|","?","*"]
    url="http://www.bbc.co.uk"
    r=requests.get(url+"/news")
    soup=BeautifulSoup(r.content,"lxml")
    temp=soup.find_all("a",{"class":"navigation-wide-list__link"})
    NewsTopics=[]
    NewsTopicsLinks=[]

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
                        imagename=str(image['src']).replace('/320/','/660/')
                        
                       
                    dictionary={'category':category,
                                'title': title,
                                'author':author,
                                'maintext':maintext,
                                'date':str(date),
                                'ImageLocation':imagename
                        }
                                

        count=count+1
        if count == 1:
            return len(dictionary)
                                
class MyTest(unittest.TestCase):
    def test_cube(self):
        self.assertEqual(testfunction(),6)
unittest.main()
        

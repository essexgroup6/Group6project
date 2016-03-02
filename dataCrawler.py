import requests
import urllib
import urllib2
import os
from bs4 import BeautifulSoup
from time import gmtime, strftime
#directory="M:\group"
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
            #print("The count is : "+str(count)+" "+"The len of the list is :"+str(len(NewsTopics)))
            n=NewsTopics[count].replace("\n","")
            nn=j.text.replace("\n","")
            for h in forbiden:
                nn=nn.replace(h," ")
            if not os.path.exists(n):
                os.makedirs(n)
                
            #If it is windows
            #f=open(n+"\\"+nn+".txt","w")
                
            #if it is Linux
                
            f=open(n+"/"+nn+".txt","w")
            paragraphList=[]

            for k in articleContext:
                #me=k.text.encode("utf-8")
                
                #for q in k.find_all("p"):
                    #f.write(q)
                date=""
                dates=k.find_all("div",{"class":"date date--v2"})
                if len(dates)==0:
                    print(strftime("%Y-%d-%m", gmtime()))
                else:
                    for q in dates:
                        print(q['data-datetime'])

                #for date in dates:
                    #print(date)
                images =k.find_all("img",{"class":"js-image-replace"})
                #for image in images:
                    #urllib.urlretrieve(image['src'], filename=n+"/"+nn+image['alt'])
                
            print("----------------------------------------------")

            f.close()


    count=count+1
                
                
                      
        

import requests
import os
from bs4 import BeautifulSoup
#directory="M:\group"
forbiden=["<",">",":","/","\\","|","?","*"]
url="http://www.bbc.co.uk"
r=requests.get(url+"/news")
soup=BeautifulSoup(r.content)
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
    soup=BeautifulSoup(r.content)
    temp=soup.find_all("a",{"class":"title-link"})
    count=count+1
    for j in temp:
        if not("http" in j.get("href") ):
            r=requests.get(url+j.get("href"))
            soup=BeautifulSoup(r.content)

            articleContext=soup.find_all("div",{"class":"story-body"})
            n=NewsTopics[count].replace("\n","")
            nn=j.text.replace("\n","")
            for h in forbiden:
                nn=nn.replace(h," ")
            if not os.path.exists(n):
                os.makedirs(n)
            
            f=open(n+"\\"+nn+".txt","w")

            for k in articleContext:
                f.write(k.text)
                
                      
        

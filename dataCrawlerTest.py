from pymongo import MongoClient
client = MongoClient()
db = client.articles
collection = db.articles_collection
collection.drop()
p=collection.find()


num=collection.count()

print(num)

for i in p:        
    print(i['ImageLocation'])

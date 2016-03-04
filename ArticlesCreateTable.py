from __future__ import print_function # Python 2/3 compatibility
import boto3

dynamodb = boto3.resource('dynamodb', region_name='us-west-2a')#, endpoint_url="http://localhost:8000") 

table = dynamodb.create_table(
    TableName='articles',
    KeySchema=[
        {
            'AttributeName': 'title',
            'KeyType': 'HASH'  #Partition key
        },
        {
            'AttributeName': 'category',
            'KeyType': 'RANGE'  #Sort key
        }

    ],
    AttributeDefinitions=[
        {
            'AttributeName': 'title',
            'AttributeType': 'S'
        },
                {
            'AttributeName': 'category',
            'AttributeType': 'S'
        }

    ],
    ProvisionedThroughput={
        'ReadCapacityUnits': 10,
        'WriteCapacityUnits': 10
    }
)
# Wait until the table exists.
table.meta.client.get_waiter('table_exists').wait(TableName='users')

# Print out some data about the table.
print("Table status:", table.table_status)

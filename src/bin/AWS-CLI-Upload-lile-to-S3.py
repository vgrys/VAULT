import boto3

# Let's use Amazon S3
s3 = boto3.resource("s3")

# Upload a new file
data = open('C:\Scripts\*', 'rb')
s3.Bucket('vygrys').put_object(Key='*', Body=data)

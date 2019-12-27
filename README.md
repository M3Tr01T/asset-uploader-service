## **Asset Uploader Service**

This service has been created following a Ports & Adapters architecture (aka Hexagonal).

Three environment variables need to be set to execute this app:
* AWS_REGION
* AWS_ACCESS_KEY_ID
* AWS_SECRET_ACCESS_KEY

Indicating the respective information from the AWS account.

An instance of MongoDB has to be running in the machine where the app is run. A docker-compose.yml file is included on the repo. It can be executed running
`docker-compose up` from the command line.

Three endpoints are provided:
* **POST** `/asset` with an empty body to create a new asset
* **PUT** `/asset/{assetId}` with body required 
* **GET** `/asset/{assetId}`

A postman collection is attached (folder `/postman`) to ease the testing of the service. Note that swagger can also be used for testing (visiting `/swagger-ui.html`).

The flow of execution is the following:
1. Send a **POST** request to `/asset` with an empty body to create a new asset.
   
   Sample response
   
   `{ “upload_url”: <s3-signed-url-for-upload>, “id”: <asset-id> }`
   
2. Send a **PUT** request to `<s3-signed-url-for-upload>` with the content to be uploaded to S3.
      
3. Send a **PUT** request to `/asset/{assetId}` with the following body
   
   `{ “Status”: “uploaded” }`
   
   to update the status of the asset.
     
   Sample response
      
   `Asset with id 7d1acb4e-bb3e-40c6-ab01-a2569fc10984 updated successfully with status "uploaded"`
      
4. Send a **GET** request to `/asset/{assetId}?timeout=100` to get a URL for downloading the asset uploaded on step 2. If timeout is not specified 60 seconds would be assumed.
   
   Sample response
      
   `{ “Download_url”: <s3-signed-url-for-download> }`  
    
5. Send a **GET** request to `<s3-signed-url-for-download>` to get the content of the file uploaded to S3 on step 3.
   
There are aditional configuration parameters which can be configured:
* **signatureDuration.upload**: Sets the TTL of the presigned URL for uploading. If empty, defaults to `60` seconds.
* **s3.bucketName**: Sets the bucket name on AWS. If empty, defaults to `assetservicedatastax`

#### ORIGINAL ASSIGNMENT:

Create an S3 asset uploader API as described below:
1.	The service should have an HTTP POST endpoint to upload a new asset.

        `POST /asset
        Body: empty`

        Sample response
        
        `
        {
        “upload_url”: <s3-signed-url-for-upload>,
        “id”: <asset-id>
}`
2.	The user should be able to make a POST call to the s3 signed URL to upload the asset
3.	To mark the upload operation as complete, the service should provide a PUT endpoint as follows:
    
        `PUT /asset/<asset-id>
        Body:
        {
        “Status”: “uploaded”
        }`
4.	When a Get request is made on the asset, the service should return an s3 signed URL for download with the timeout in seconds as a URL parameter. If the timeout is not specified assume 60 seconds.
    `GET /asset/<asset-id>?timeout=100`
    
        Sample response
    
        `{
        “Download_url”: <s3-signed-url-for-upload>
        }`
5.	A get call made on an asset that has not been set to “status: uploaded” should return an error.
6.	The uploaded asset should be fetched successfully using the returned signed URL.


##### Notes:
1. Any choice of language/stack is allowed
2. Code should have adequate unit test coverage
3. Code should be submitted with adequate instructions on how we can set up and execute you submission.
4. Please use your own AWS account for this exercise. You can sign up for a free trial here: https://aws.amazon.com/free/start-your-free-trial/        



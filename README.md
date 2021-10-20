# Collager
A service to allow for storing, searching, and catagerizing images


## Architecture
![Collager Architecture](https://github.com/aerwin3/collager/blob/main/docs/collager.svg?raw=true)


## API Docs

For all requests an `X-Account-Id` header is required.

-------------------------------
## GET /images
View all images for an account
Return all counters for a specific account.

#### Query Parameters
Name | Description | Required | Type
--- | --- | --- | ---
objects | Comma seperated list of object names to filter results. | false | string

#### Example
```bash
Request
curl -H 'X-Account-Id: acct1' http://localhost:8080/images

Response
{
    "items": [
        {
            "id": "92834d90-a4ab-463c-966d-13ef80f46674",
            "label": "51c97b2ecf6357252a0a2bc576e21ba1662d3560d7fb03e2f1f9a83ab2490e09",
            "url": "https://storage.googleapis.com/download/storage/v1/b/aerwin-test-collager/o/92834d90-a4ab-463c-966d-13ef80f46674?generation=1634712407416780&alt=media",
            "objects": "icon"
        },
        {
            "id": "947e584f-ef7a-4293-8b73-59fccc6f7450",
            "label": "579fa51dad84821993c55eb62ca9ebdbddffa5426e71f22f85f46a180c445d67",
            "url": "https://storage.googleapis.com/download/storage/v1/b/aerwin-test-collager/o/947e584f-ef7a-4293-8b73-59fccc6f7450?generation=1634704276510756&alt=media",
            "objects": "icon"
        }
    ]
}
```
-------------------------------
## GET /images/{id}
View metadata for an image based on the id.

#### Path Parameters
Name | Description | Required | Type
--- | --- | --- | ---
id | Unique id associated with an image | true | string

#### Example
```bash
Request
curl -H 'X-Account-Id: acct1' http://localhost:8080/images/92834d90-a4ab-463c-966d-13ef80f46674

Response
{
    "id": "92834d90-a4ab-463c-966d-13ef80f46674",
    "label": "51c97b2ecf6357252a0a2bc576e21ba1662d3560d7fb03e2f1f9a83ab2490e09",
    "url": "https://storage.googleapis.com/download/storage/v1/b/aerwin-test-collager/o/92834d90-a4ab-463c-966d-13ef80f46674?generation=1634712407416780&alt=media",
    "objects": "icon"
}
```
-------------------------------
## DELETE /images/{id}
Remove image by id

#### Path Parameters
Name | Description | Required | Type
--- | --- | --- | ---
id | Comma seperated list of object names to filter results. | false | string


#### Example
```bash
Request
curl -XDELETE -H 'X-Account-Id: acct1' http://localhost:8080/images/92834d90-a4ab-463c-966d-13ef80f46674
```

-------------------------------
## POST /images
Upload an image

#### Form Parameters
Name | Description | Required | Type
--- | --- | --- | ---
file | Multipart file path. | false | string
url  | Url to an image that is to be uploaded. | false | string
label | Label for the image | false | string
detection | Run object detection on the provided image | false | boolean

#### Example
```bash
Request
curl -H 'X-Account-Id: acct1' http://localhost:8080/images 
-F "label=happy-face"
-F "url=https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSEk8VkUdiVga98u36sJuPA9en55G7LD8Q1sQ&usqp=CAU" 
-F "detection=true"

Response
{
    "id": "92834d90-a4ab-463c-966d-13ef80f46674",
    "label": "51c97b2ecf6357252a0a2bc576e21ba1662d3560d7fb03e2f1f9a83ab2490e09",
    "url": "https://storage.googleapis.com/download/storage/v1/b/aerwin-test-collager/o/92834d90-a4ab-463c-966d-13ef80f46674?generation=1634712407416780&alt=media",
    "objects": "icon"
}
```

## Requirement
In order to run the collager the following will be needed installed.
- Maven
- Docker
- Docker-compose 

## Pre-Running application
In order to run this application there are a couple pre-reqs
- An account must be created with [Imagga](https://imagga.com), update the `docker-compse.yml` to include the key and secret
- A key.json file for a service account for an application in [GCP](https://console.cloud.google.com/apis/credentials/serviceaccountkey) must be place in the root of the project with the name `gcp-credentials.json`
- Update the docker-compose.yml with a name for the gcp bucket that will hold the images

##Build and running steps
- Package the spring boot app `mvn package -DskipTests` at the root of the project.
- Build the docker image `docker build . --tag collager.v1`
- Standup the application `docker-compose up -d` (This will run all the containers needed to support the application)

## License
[MIT](https://choosealicense.com/licenses/mit/)

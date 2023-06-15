# BeBi-API

## Project Description
BeBi-API (Beli Bijak API) is an application that helps customers with budgeting and provides product recommendations based on their budget. It is built using Node.js and can be easily deployed using Docker and Cloud Run.

## Prerequisites
- Docker installed on your machine
- Docker Hub account (optional, if you want to push your Docker image to Docker Hub)

## Getting Started

### Clone the Repository
```
git clone https://github.com/yourusername/BeBi-API.git
cd BeBi-API
```

### Build the Docker Image
```
docker build -t bebi-api .
```

### Run the Docker Container Locally
```
docker run -p 8080:8080 bebi-api
```

### Access the API
The API will be available at http://localhost:8080. You can use tools like cURL or Postman to interact with the endpoints.

## Deploying on Cloud Run

### Push the Docker Image to a Container Registry
If you want to deploy the application on Cloud Run, you need to push the Docker image to a container registry. Here's an example using Docker Hub:

1. Tag the Docker image:
```
docker tag bebi-api yourdockerusername/bebi-api
```

2. Push the image to Docker Hub:
```
docker push yourdockerusername/bebi-api
```

### Deploy on Cloud Run
1. Open the [Google Cloud Console](https://console.cloud.google.com/).

2. Create a new project or select an existing one.

3. Enable the Cloud Run API for your project.

4. In the Cloud Console, go to Cloud Run.

5. Click "Create Service" and select the following options:
   - Container Image: Select "Custom".
   - Service Name: Enter a name for your service.
   - Deployment Platform: Select "Managed".

6. In the "Container" section:
   - Container Image URL: Enter the URL of your Docker image (e.g., `yourdockerusername/bebi-api`).
   - Port: Enter `8080`.

7. Click "Create" to deploy the application on Cloud Run.

8. Once the deployment is complete, you will see the URL where your API is accessible.

### Accessing the Deployed API
You can access the deployed API using the URL provided by Cloud Run. Make sure to prepend `https://` to the URL.

## Contributing
If you'd like to contribute to the BeBi-API project, please follow the guidelines in [CONTRIBUTING.md](CONTRIBUTING.md).

## License
This project is licensed under the [MIT License](LICENSE).

## Contact
If you have any questions or suggestions, feel free to contact the project maintainer at [maharaniswastika6@gmail.com](mailto:maharaniswastika6@gmail.com).
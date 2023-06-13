FROM python:3.11-slim

ENV PYTHONUNBUFFERED=True
ENV PYTHONDONTWRITEBYTECODE=True

# Copy requirements.txt to the docker image and install packages
COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

# Copy the rest of the application code
COPY . /app

# Set the working directory
WORKDIR /app

# Expose port 8080
EXPOSE 8080

# Use gunicorn as the entrypoint
CMD ["flask", "run", "--host=0.0.0.0", "--port=8080"]

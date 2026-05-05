pipeline {
    agent any

    environment {
        PROJECT_ID = "sync-service-dev-2604"
        REGION = "asia-south1"
        SERVICE = "sync-service"
        IMAGE = "asia-south1-docker.pkg.dev/$PROJECT_ID/sync-service-repo/sync-service"
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean package'
            }
        }

        stage('Test') {
            steps {
                sh 'mvn test'
            }
        }

        stage('Build Docker Image') {
            when {
                not { changeRequest() }
            }
            steps {
                sh "docker build -t $IMAGE:$BUILD_NUMBER ."
            }
        }

        stage('Push Image') {
            when {
                not { changeRequest() }
            }
            steps {
                sh "docker push $IMAGE:$BUILD_NUMBER"
            }
        }

        stage('Deploy') {
            when {
                branch 'qa' || branch 'staging' || branch 'main'
            }
            steps {
                script {
                    if (env.BRANCH_NAME == 'main') {
                        input "Approve production deployment?"
                    }

                    sh """
                    gcloud run deploy $SERVICE \
                      --image $IMAGE:$BUILD_NUMBER \
                      --region $REGION \
                      --platform managed \
                      --allow-unauthenticated
                    """
                }
            }
        }
    }
}

pipeline {
    agent any

    environment {
        PROJECT_ID = "sync-service-dev-2604"
        REGION = "asia-south1"
        SERVICE = "sync-service"
        IMAGE = "asia-south1-docker.pkg.dev/sync-service-dev-2604/sync-service-repo/sync-service"
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build & Test') {
            steps {
                dir('spring-app') { 
                    sh 'mvnw clean package'
                    sh 'mvnw test'
		}
            }
        }

        stage('Auth to GCP') {
            steps {
                withCredentials([file(credentialsId: 'gcp-key', variable: 'GOOGLE_APPLICATION_CREDENTIALS')]) {
                    sh '''
                    gcloud auth activate-service-account --key-file=$GOOGLE_APPLICATION_CREDENTIALS
                    gcloud config set project sync-service-dev-2604
                    gcloud auth configure-docker asia-south1-docker.pkg.dev --quiet
                    '''
                }
            }
        }

        stage('Build Docker Image') {
            when {
                not {
                    changeRequest()
                }
            }
            steps {
                dir('spring-app') {
                sh "docker build -t $IMAGE:$BUILD_NUMBER ."
		}
            }
        }

        stage('Push Image') {
            when {
                not {
                    changeRequest()
                }
            }
            steps {
                sh "docker push $IMAGE:$BUILD_NUMBER"
            }
        }

        stage('Deploy') {
            when {
                anyOf {
                    branch 'qa'
                    branch 'staging'
                    branch 'main'
                }
            }
            steps {
                script {
                    if (env.BRANCH_NAME == 'main') {
                        input message: "Approve production deployment?"
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

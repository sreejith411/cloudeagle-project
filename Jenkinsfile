pipeline {
    agent any

    environment {
        PROJECT_ID = "sync-service-dev-2604"
        REGION = "asia-south1"
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
                withEnv(["JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64", "PATH=/usr/lib/jvm/java-17-openjdk-amd64/bin:${env.PATH}"]) {
                    dir('spring-app') {
                        sh '''
                        mvn clean package
                        mvn test
                        '''
                    }
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
                not { changeRequest() }
            }
            steps {
                dir('spring-app') {
                    sh "docker build -t $IMAGE:$BUILD_NUMBER ."
                }
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
                anyOf {
                    branch 'qa'
                    branch 'staging'
                    branch 'main'
                }
            }
            steps {
                script {

                    def serviceName = ""
                    def envName = ""

                    if (env.BRANCH_NAME == 'qa') {
                        serviceName = "sync-service-qa"
                        envName = "qa"
                    } else if (env.BRANCH_NAME == 'staging') {
                        serviceName = "sync-service-staging"
                        envName = "staging"
                    } else if (env.BRANCH_NAME == 'main') {
                        input message: "Approve production deployment?"
                        serviceName = "sync-service-prod"
                        envName = "prod"
                    }

                    sh """
                    gcloud run deploy $serviceName \
                      --image $IMAGE:$BUILD_NUMBER \
                      --region $REGION \
                      --platform managed \
                      --allow-unauthenticated \
                      --set-env-vars ENV=$envName \
		      --set-secrets MONGO_URI=mongo-uri-$envName:latest
                    """
                }
            }
        }
    }
}

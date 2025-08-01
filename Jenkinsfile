pipeline {
    agent any

    environment {
        IMAGE_NAME = "quarkusapi"
        DOCKERFILE_PATH = "src/main/docker/Dockerfile.jvm"
        AZURE_APP_NAME = "noelia-app"           // Cambia por el nombre real de tu App Service
        AZURE_RESOURCE_GROUP = "rg-adapted-serval"
        AZURE_REGISTRY = "quarkusappacr123.azurecr.io"
        AZURE_SERVICE_PRINCIPAL = credentials('azure-service-principal') // JSON con clientId, clientSecret, tenantId
    }

    stages {
        stage('Clonar Repositorio') {
            steps {
                git url: 'https://github.com/NoeliaGiron/QuarkusAPI.git', branch: 'main'
            }
        }

        stage('Compilar Proyecto Quarkus') {
            agent {
                docker {
                    image 'maven:3.9-eclipse-temurin-17'
                    args '-v /var/run/docker.sock:/var/run/docker.sock'
                }
            }
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Construir Imagen Docker') {
            steps {
                sh "docker build -f ${DOCKERFILE_PATH} -t ${IMAGE_NAME}:latest ."
            }
        }

        stage('Login a Azure Container Registry') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'acr-credentials', usernameVariable: 'ACR_USER', passwordVariable: 'ACR_PASS')]) {
                    sh """
                      echo \$ACR_PASS | docker login ${AZURE_REGISTRY} --username \$ACR_USER --password-stdin
                    """
                }
            }
        }

        stage('Taggear y Push de Imagen') {
            steps {
                sh "docker tag ${IMAGE_NAME}:latest ${AZURE_REGISTRY}/${IMAGE_NAME}:latest"
                sh "docker push ${AZURE_REGISTRY}/${IMAGE_NAME}:latest"
            }
        }

        stage('Desplegar en Azure App Service') {
            agent {
                docker {
                    image 'mcr.microsoft.com/azure-cli'
                    args '-v /var/run/docker.sock:/var/run/docker.sock'
                }
            }
            steps {
                script {
                    def sp = readJSON text: env.AZURE_SERVICE_PRINCIPAL
                    sh """
                        az login --service-principal -u ${sp.clientId} -p ${sp.clientSecret} --tenant ${sp.tenantId}
                        az webapp config container set --name ${AZURE_APP_NAME} --resource-group ${AZURE_RESOURCE_GROUP} --docker-custom-image-name ${AZURE_REGISTRY}/${IMAGE_NAME}:latest
                        az webapp restart --name ${AZURE_APP_NAME} --resource-group ${AZURE_RESOURCE_GROUP}
                    """
                }
            }
        }
    }

    post {
        success {
            echo '✅ Despliegue exitoso en Azure App Service'
        }
        failure {
            echo '❌ Hubo un error en el pipeline'
        }
    }
}

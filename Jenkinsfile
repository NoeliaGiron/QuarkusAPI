pipeline {
    agent any

    environment {
        IMAGE_NAME = "quarkusapi"
        DOCKERFILE_PATH = "src/main/docker/Dockerfile.jvm"
        AZURE_APP_NAME = "noelia-app"
        AZURE_RESOURCE_GROUP = "rg-adapted-serval"
        AZURE_REGISTRY = "quarkusappacr123.azurecr.io"
        AZURE_SERVICE_PRINCIPAL = credentials('azure-service-principal') // JSON con clientId, clientSecret, tenantId
        ACR_CREDENTIALS = credentials('acr-credentials') // usuario y pass para ACR
    }

    stages {
        stage('Clonar Repositorio') {
            steps {
                git url: 'https://github.com/NoeliaGiron/QuarkusAPI.git', branch: 'main'
            }
        }

        stage('Compilar Proyecto Quarkus') {
            steps {
                // Usamos docker container para build con Maven + Java 21
                script {
                    docker.image('maven:3.9.4-eclipse-temurin-21').inside {
                        sh 'chmod +x mvnw'
                        sh './mvnw clean package -DskipTests'
                    }
                }
            }
        }

        stage('Construir Imagen Docker') {
            steps {
                sh "docker build -f ${DOCKERFILE_PATH} -t ${IMAGE_NAME}:latest ."
            }
        }

        stage('Login a Azure Container Registry') {
            steps {
                script {
                    sh """
                      echo '${ACR_CREDENTIALS_PSW}' | docker login ${AZURE_REGISTRY} --username ${ACR_CREDENTIALS_USR} --password-stdin
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
            steps {
                script {
                    def sp = readJSON text: env.AZURE_SERVICE_PRINCIPAL
                    // Usamos contenedor azure-cli para ejecutar comandos az
                    docker.image('mcr.microsoft.com/azure-cli').inside {
                        sh """
                            az login --service-principal -u ${sp.clientId} -p ${sp.clientSecret} --tenant ${sp.tenantId}
                            az webapp config container set --name ${AZURE_APP_NAME} --resource-group ${AZURE_RESOURCE_GROUP} --docker-custom-image-name ${AZURE_REGISTRY}/${IMAGE_NAME}:latest
                            az webapp restart --name ${AZURE_APP_NAME} --resource-group ${AZURE_RESOURCE_GROUP}
                        """
                    }
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

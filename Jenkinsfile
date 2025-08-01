pipeline {
    agent any

    environment {
        IMAGE_NAME = "quarkusapi"
        DOCKERFILE_PATH = "src/main/docker/Dockerfile.jvm"
        AZURE_APP_NAME = "tu-app-service-name"
        AZURE_RESOURCE_GROUP = "tu-resource-group"
        AZURE_REGISTRY = "tu-registry.azurecr.io"
        AZURE_SERVICE_PRINCIPAL = credentials('azure-service-principal') // JSON credencial almacenada en Jenkins
    }

    stages {
        stage('Clonar Repositorio') {
            steps {
                git url: 'https://github.com/NoeliaGiron/QuarkusAPI.git', branch: 'main'
            }
        }

        stage('Compilar Proyecto Quarkus') {
            steps {
                sh 'chmod +x mvnw'
                sh './mvnw clean package -DskipTests'
            }
        }

        stage('Construir Imagen Docker') {
            steps {
                script {
                    sh "docker build -f ${DOCKERFILE_PATH} -t ${IMAGE_NAME}:latest ."
                }
            }
        }

        stage('Login a Azure Container Registry') {
            steps {
                script {
                    // Extraemos del JSON las credenciales con jq
                    def sp = readJSON text: env.AZURE_SERVICE_PRINCIPAL
                    sh """
                      echo '${sp.clientSecret}' | docker login ${AZURE_REGISTRY} --username ${sp.clientId} --password-stdin
                    """
                }
            }
        }

        stage('Taggear y Push de Imagen') {
            steps {
                script {
                    sh "docker tag ${IMAGE_NAME}:latest ${AZURE_REGISTRY}/${IMAGE_NAME}:latest"
                    sh "docker push ${AZURE_REGISTRY}/${IMAGE_NAME}:latest"
                }
            }
        }

        stage('Desplegar en Azure App Service') {
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

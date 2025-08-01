pipeline {
    agent any

    environment {
        IMAGE_NAME = "quarkusapi"
        DOCKER_COMPOSE_PATH = "src/main/docker"
    }

    stages {
        stage('Clonar Repositorio') {
            steps {
                git url: 'https://github.com/NoeliaGiron/QuarkusAPI.git', branch: 'main'
            }
        }

        stage('Compilar Proyecto Quarkus') {
            steps {
                sh './mvnw clean package -DskipTests'
            }
        }

        stage('Construir Imagen Docker') {
            steps {
                script {
                    sh "docker build -f ${DOCKER_COMPOSE_PATH}/Dockerfile.jvm -t ${IMAGE_NAME}:latest ."
                }
            }
        }

        stage('Levantar contenedores') {
            steps {
                dir("${DOCKER_COMPOSE_PATH}") {
                    sh 'docker compose up -d'
                }
            }
        }
    }

    post {
        success {
            echo 'Aplicación Quarkus construida y ejecutada exitosamente.'
        }
        failure {
            echo 'La construcción falló.'
        }
    }
}

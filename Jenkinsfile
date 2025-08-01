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
                // Solucionar permisos para mvnw
                sh 'chmod +x mvnw'
                sh './mvnw clean package -DskipTests'
            }
        }

        stage('Verificar acceso a Docker') {
            steps {
                script {
                    sh '''#!/bin/bash
                    echo "Usuario y grupos dentro del contenedor Jenkins:"
                    id
                    groups

                    echo "Probando docker ps:"
                    docker ps

                    echo "Ajustando permisos al socket de Docker (temporal para pruebas)"
                    sudo chmod 666 /var/run/docker.sock || true
                    '''
                }
            }
        }

        stage('Construir Imagen Docker') {
            steps {
                script {
                    sh '''#!/bin/bash
                    docker build -f ${DOCKER_COMPOSE_PATH}/Dockerfile.jvm -t ${IMAGE_NAME}:latest .
                    '''
                }
            }
        }

stage('Levantar contenedores') {
    steps {
        dir("${DOCKER_COMPOSE_PATH}") {
            sh 'docker-compose up -d'
        }
    }
}

    }

    post {
        success {
            echo '✅ Aplicación Quarkus construida y ejecutada exitosamente.'
        }
        failure {
            echo '❌ La construcción falló.'
        }
    }
}

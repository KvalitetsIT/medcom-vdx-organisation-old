pipeline {
    agent {
        docker { image "maven:3-jdk-11" }
    }

    stages {
        stage('Initialize') {
            steps{
                script {
                    currentBuild.displayName = "$currentBuild.displayName-${env.GIT_COMMIT}"
                }
            }
        }
        stage('Build And Test') {
            steps {
                sh 'mvn install'
            }
        }
        stage('Tag Docker Image And Push') {
            steps {
                docker.withRegistry('https://kitdocker.kvalitetsit.dk/') {
                    docker.image("kvalitetsit/medcom-vdx-organization:${env.GIT_COMMIT}").push("${env.GIT_COMMIT}")
                }
            }
        }
        stage('Deploy') {
            steps {
                echo 'Deploying.... ${env.GIT_COMMIT}'
            }
        }
    }
}
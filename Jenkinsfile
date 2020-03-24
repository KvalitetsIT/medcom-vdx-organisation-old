pipeline {
    agent {
        docker  { image "maven:3-jdk-11" }
    }

    stages {
        stage('Initialize') {
            steps{
                script {
                    currentBuild.displayName = "$currentBuild.displayName-${env.GIT_COMMIT}"
                }
            }
        }
        stage('Build') {
            steps {
                sh 'mvn install'
            }
        }
        stage('Test') {
            steps {
                echo 'Testing.. ${env.GIT_COMMIT}'
            }
        }
        stage('Deploy') {
            steps {
                echo 'Deploying.... ${env.GIT_COMMIT}'
            }
        }
    }
}
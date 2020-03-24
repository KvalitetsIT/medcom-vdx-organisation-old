pipeline {
    agent any

    stages {
        stage('Initialize') {
            currentBuild.displayName = "$currentBuild.displayName-${env.GIT_COMMIT}"
        }
        stage('Build') {
            steps {
                echo 'Building.. ${env.GIT_COMMIT}'
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
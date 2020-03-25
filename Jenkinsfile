pipeline {
    agent any

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
                script {
                    def maven = docker.image('maven:3-jdk-11')
                    maven.pull()
                    maven.inside {
                        sh 'mvn install'
                    }
                }
            }
        }
        stage('Tag Docker Image And Push') {
            steps {
                script {
                    docker.withRegistry('https://kitdocker.kvalitetsit.dk/') {
                        docker.image("kvalitetsit/medcom-vdx-organisation:${env.GIT_COMMIT}").push("${env.GIT_COMMIT}")
                    }
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
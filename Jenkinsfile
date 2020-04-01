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
                    docker.build("build-medcom-vdx-organisation-resources", "-f ./integrationtest/docker/Dockerfile-resources --no-cache ./integrationtest/docker")

                    def resources = docker.image('build-medcom-vdx-organisation-resources')
                    resources.run("--name medcom-vdx-organisation-resources --rm")

                    def maven = docker.image('maven:3-jdk-11')
                    maven.pull()
                    maven.inside("--volumes-from medcom-vdx-organisation-resources") {
                        sh 'mvn install -Pdocker-test'
                    }

                    junit '**/target/surefire-reports/*.xml'
                    jacoco changeBuildStatus: true, maximumLineCoverage: '0', minimumLineCoverage: '0', exclusionPattern: '**/some/pattern**/*.*'
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
    post {
        always {
            script {
                sh 'docker stop medcom-vdx-organisation-resources'
            }
        }
    }
}
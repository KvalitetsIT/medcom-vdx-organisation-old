pipeline {
podTemplate(
   containers: [containerTemplate(image: 'docker', name: 'docker', command: 'cat', ttyEnabled: true)],
   volumes: [hostPathVolume(hostPath: '/var/run/docker.sock', mountPath: '/var/run/docker.sock')],
)
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
                    resources.run("--name medcom-vdx-organisation-resources")

                    def maven = docker.image('maven:3-jdk-11')
                    maven.pull()
                    maven.inside("--volumes-from medcom-vdx-organisation-resources") {
                        sh 'mvn install -Pdocker-test'
                    }

                    junit '**/target/surefire-reports/*.xml,**/target/failsafe-reports/*.xml'
                    jacoco changeBuildStatus: true, maximumLineCoverage: '80', minimumLineCoverage: '60', exclusionPattern: '**/org/openapitools/**/*.*'
                }
            }
        }
        stage('Tag Docker Images And Push') {
            steps {
                script {
                    image = docker.image("kvalitetsit/medcom-vdx-organisation:${env.GIT_COMMIT}")
                    image.push("${env.GIT_COMMIT}")
                    image.push("dev")

                    if(env.TAG_NAME != null && env.TAG_NAME.matches("^v[0-9]*\\.[0-9]*\\.[0-9]*")) {
                        echo "Tagging version"
                        image.push(env.TAG_NAME.substring(1))
                        image.push("latest")
                    }

                    docimage = docker.image("kvalitetsit/medcom-vdx-organisation-documentation:${env.GIT_COMMIT}")
                    docimage.push("${env.GIT_COMMIT}")
                    docimage.push("dev")

                    if(env.TAG_NAME != null && env.TAG_NAME.matches("^v[0-9]*\\.[0-9]*\\.[0-9]*")) {
                        echo "Tagging version"
                        docimage.push(env.TAG_NAME.substring(1))
                        docimage.push("latest")
                    }
                }
            }
        }
    }
    post {
        always {
            script {
                sh 'docker stop medcom-vdx-organisation-resources || true'
                sh 'docker rm medcom-vdx-organisation-resources || true'
            }
        }
    }
}
}
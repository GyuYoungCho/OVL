pipeline {
    agent none
    options { skipDefaultCheckout(true) }
    stages {
        stage('Build and Test') {
            agent {
                docker {
                    image 'maven:3-alpine'
                    args '-v /root/.m2:/root/.m2'
                }
            }
            options { skipDefaultCheckout(false) }
            steps {
                sh 'mvn -f /var/jenkins_home/workspace/ovl/backend/pom.xml -B -DskipTests clean package'
            }
        }
        stage('delete') {
            agent any
            steps {
                sh 'docker ps -f name=backend -q | xargs --no-run-if-empty docker container stop'

       	    sh 'docker container ls -a -fname=name=backend -q | xargs -r docker container rm'

            }
        }
        stage('Docker build') {
            agent any
            steps {
	       sh 'docker build -t backend:latest /var/jenkins_home/workspace/ovl/backend'
            }
        }
        stage('Docker run') {
            agent any
            steps {
       sh 'docker images -f dangling=true && docker rmi -f $(docker images -f dangling=true -q)' 

       sh 'docker run -d --name backend -p 8080:8080 backend:latest'
            }
        }
    }
}
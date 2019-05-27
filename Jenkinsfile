pipeline{
	
	agent{
		dockerfile {
			filename 'Dockerfile.build'
			dir 'build'
			args '-v $HOME/.m2:/root/.m2'
		}
	}
	
	environment {
        DOCKER_IMAGE_TAG = "notification-service:${env.BUILD_ID}"
    }
	
	stages{
		stage('Build'){
			steps{
				echo 'Executing Build step..'
				sh 'mvn -B -DskipTests clean package'
			}
		}
		stage('Test'){
			steps{
				sh 'mvn test'
			}
			post {
                always {
                    junit 'target/surefire-reports/*.xml' 
                }
            }
		}
		/*stage('sonar'){
			steps{
				withSonarQubeEnv('Sonar-Qube'){
					sh 'sonar-scanner'
				}
			}
		}*/
		stage('Build Docker Image'){
			steps{
				script{
					dockerImage = docker.build("${env.DOCKER_IMAGE_TAG}",  '-f ./Dockerfile .')
				}
			}
		}
	}
}
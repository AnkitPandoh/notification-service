pipeline{
	
	agent{
		dockerfile {
			filename 'Dockerfile.build'
			dir 'build'
			label 'jenkins-pipeline'
		}
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
		stage('sonar'){
			steps{
				withSonarQubeEnv('Sonar-Qube'){
					sh 'sonar-scanner'
				}
			}
		}
	}
}
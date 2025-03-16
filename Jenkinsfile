@Library('threepoints-sharedlib@main') _

pipeline {
    agent any
    stages {
        stage('Code Quality Analysis') {
            steps {
                script {
                    codeQualityAnalysis()
                }
            }
        }
    }
}

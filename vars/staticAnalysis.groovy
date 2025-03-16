def call(boolean qualityGateFail = false, boolean abortPipeline = false) {
    agent any
    stages {
        stage('Code Quality Analysis') {
            steps {
                script {
                    try {
                        timeout(time: 5, unit: 'MINUTES') {
                            sh 'echo "Ejecución de las pruebas de calidad de código"'
                        }
                    } catch (err) {
                        echo 'Timeout reached while waiting for the code quality analysis to complete.'
                        if (abortPipeline) {
                            error 'Pipeline aborted due to timeout.'
                        }
                    }

                    // Simulación de evaluación del QualityGate
                    if (qualityGateFail) {
                        echo 'QualityGate failed.'
                        if (abortPipeline) {
                            error 'Pipeline aborted due to QualityGate failure.'
                        }
                    } else {
                        echo 'QualityGate passed.'
                    }
                }
            }
        }
    }
}

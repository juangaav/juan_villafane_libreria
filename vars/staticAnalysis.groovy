def call(boolean qualityGateFail = false, boolean abortPipeline = false) {
    try {
        def scannerHome = tool 'Sonar-scanner'
        withSonarQubeEnv('Sonar Local') {
            bat "${scannerHome}/bin/sonar-scanner"
        }
        timeout(time: 1, unit: 'MINUTES') {
            waitForQualityGate abortPipeline: true
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
            abortPipeline: true
        }
    } else {
        echo 'QualityGate passed.'
    }
}

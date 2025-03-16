def call(boolean qualityGateFail = false, boolean abortPipeline = false) {
    echo "Ejecutando staticAnalysis.groovy"

    def currentBranch = "${env.GIT_BRANCH.split("/")[1]}"

    echo "Current Git Branch: ${currentBranch}"
    
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
        abortPipelineIfRequired(currentBranch, abortPipeline)
    } else {
        echo 'QualityGate passed.'
    }
}

def abortPipelineIfRequired(String branchName, boolean abortPipeline) {
    if (abortPipeline) {
        error 'Pipeline aborted due to QualityGate failure.'
    } else {
        if (branchName == 'main' || branchName.startsWith('hotfix')) {
            error 'Pipeline aborted due to QualityGate failure on critical branch.'
        }
    }
}

def call(boolean qualityGateFail = false, boolean abortPipeline = false) {
    echo "Ejecutando staticAnalysis.groovy"

    def currentBranch = "${env.GIT_BRANCH.split("/")[1]}"

    echo "Rama actual de Git: ${currentBranch}"
    abortPipelineIfRequired(currentBranch, abortPipeline)
    try {
        timeout(time: 5, unit: 'MINUTES') {
            sh 'echo "Ejecución de las pruebas de calidad de código"'
        }
    } catch (err) {
        echo 'Timeout ocurrido mientras se esperaban pruebas de calidad.'
        if (abortPipeline) {
            error 'Se interrumpe pipeline por timeout.'
            currentBuild.result = 'ABORTED'
        }
    }

    if (qualityGateFail) {
        echo 'QualityGate fallido.'
    } else {
        echo 'QualityGate completado exitosamente.'
    }
}

def abortPipelineIfRequired(String branchName, boolean abortPipeline) {
    if (abortPipeline) {
        error 'Pipeline interrumpido debido a fallo de QualityGate.'
        currentBuild.result = 'ABORTED'
    } else {
        if (branchName == 'main' || branchName.startsWith('hotfix')) {
            error 'Pipeline interrumpido debido a fallo de QualityGate en rama critica.'
            currentBuild.result = 'ABORTED'
        }
    }
}

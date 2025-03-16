def call(boolean qualityGateFail = false, boolean abortPipeline = false) {
    echo "Ejecutando staticAnalysis"

    // Obtener el nombre de la rama actual desde el entorno de Jenkins o manualmente
    def branchName = env.BRANCH_NAME
    if (!branchName) {
        branchName = sh(script: 'git rev-parse --abbrev-ref HEAD', returnStdout: true).trim()
    }
    
    // Obtener el nombre de la rama actual desde el entorno de Jenkins
    def branchName = env.BRANCH_NAME
    
    echo "Nombre de la rama: ${branchName}"
    
    try {
        timeout(time: 5, unit: 'MINUTES') {
            sh 'echo "Ejecución de las pruebas de calidad de código"'
        }
    } catch (err) {
        echo 'Timeout mientras se esperaba el analisis de código.'
        if (abortPipeline) {
            error 'Pipeline terminado por timeout.'
        }
    }

    if (qualityGateFail) {
        echo 'Fallo de QualityGate.'
        abortPipelineIfRequired(branchName, abortPipeline)
    } else {
        echo 'QualityGate exitoso.'
    }
}

def abortPipelineIfRequired(String branchName, boolean abortPipeline) {
    if (abortPipeline) {
        error 'Pipeline terminado por fallo en QualityGate'
    } else {
        if (branchName == 'master' || branchName.startsWith('hotfix')) {
            error 'Pipeline terminado por nombre de branch invalido (master o hotfix).'
        }
    }
}

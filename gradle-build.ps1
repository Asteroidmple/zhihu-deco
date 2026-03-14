# Gradle Wrapper 启动脚本（绕过批处理文件限制）

$env:GRADLE_OPTS = "-Xmx2048m -Dorg.gradle.daemon=false"
$env:JAVA_OPTS = "-Xmx2048m"

Write-Host "========================================"
Write-Host "  Zhihu-deco Gradle Build Tool"
Write-Host "========================================"
Write-Host ""

# Check Java
Write-Host "Checking Java environment..."
try {
    $javaVer = java -version 2>&1 | Select-String "version" | Select-Object -First 1
    Write-Host "Java found: $javaVer" -ForegroundColor Green
} catch {
    Write-Host "ERROR: Java not found. Please set JAVA_HOME" -ForegroundColor Red
    exit 1
}

Write-Host ""

if ($args.Count -eq 0) {
    Write-Host "Available commands:" -ForegroundColor Cyan
    Write-Host "  .\gradle-build.ps1 assembleFullRelease   - Build full release APK" -ForegroundColor White
    Write-Host "  .\gradle-build.ps1 assembleLiteRelease   - Build lite release APK" -ForegroundColor White
    Write-Host "  .\gradle-build.ps1 assembleFullDebug     - Build full debug APK" -ForegroundColor White
    Write-Host "  .\gradle-build.ps1 clean                 - Clean build cache" -ForegroundColor White
    Write-Host ""
    Write-Host "Please specify a build task." -ForegroundColor Yellow
    exit 0
}

Write-Host "Starting Gradle build: $($args -join ' ')" -ForegroundColor Yellow
Write-Host ""

# Setup classpath
$classpath = ".\gradle\wrapper\gradle-wrapper.jar"

# Execute Gradle using Java directly
java -classpath "$classpath" `
     "-Dorg.gradle.appname=gradlew" `
     "-Dorg.gradle.daemon=false" `
     "org.gradle.wrapper.GradleWrapperMain" $args

if ($LASTEXITCODE -eq 0) {
    Write-Host ""
    Write-Host "========================================" -ForegroundColor Green
    Write-Host "  BUILD SUCCESS!" -ForegroundColor Green
    Write-Host "========================================" -ForegroundColor Green
    Write-Host ""
    Write-Host "APK output location:" -ForegroundColor Cyan
    
    $task = $args[0]
    if ($task -like "*FullRelease*") {
        Write-Host "  app/build/outputs/apk/full/release/app-full-release.apk" -ForegroundColor White
    } elseif ($task -like "*LiteRelease*") {
        Write-Host "  app/build/outputs/apk/lite/release/app-lite-release.apk" -ForegroundColor White
    } elseif ($task -like "*FullDebug*") {
        Write-Host "  app/build/outputs/apk/full/debug/app-full-debug.apk" -ForegroundColor White
    } elseif ($task -like "*LiteDebug*") {
        Write-Host "  app/build/outputs/apk/lite/debug/app-lite-debug.apk" -ForegroundColor White
    }
} else {
    Write-Host ""
    Write-Host "========================================" -ForegroundColor Red
    Write-Host "  BUILD FAILED!" -ForegroundColor Red
    Write-Host "========================================" -ForegroundColor Red
    Write-Host "Please check the error messages above." -ForegroundColor Yellow
    exit $LASTEXITCODE
}

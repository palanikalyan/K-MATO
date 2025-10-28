# Update EC2 with Latest Docker Images
# Run this script after GitHub Actions completes building new images

Write-Host "================================================" -ForegroundColor Cyan
Write-Host "  K-MATO Deployment Update Script" -ForegroundColor Cyan
Write-Host "================================================" -ForegroundColor Cyan
Write-Host ""

# Set Terraform path
$terraformPath = "$env:USERPROFILE\terraform\terraform.exe"
$terraformDir = "c:\Users\Asus\IdeaProjects\Fooddelivery\infra\terraform"

# Check if GitHub Actions completed
Write-Host "BEFORE running this script, make sure:" -ForegroundColor Yellow
Write-Host "  1. GitHub Actions build completed successfully" -ForegroundColor Yellow
Write-Host "  2. Check: https://github.com/palanikalyan/K-MATO/actions" -ForegroundColor Yellow
Write-Host ""

$continue = Read-Host "Have you confirmed the build is complete? (y/n)"
if ($continue -ne "y") {
    Write-Host "Exiting. Run this script after the build completes." -ForegroundColor Red
    exit
}

Write-Host ""
Write-Host "Step 1: Destroying current infrastructure..." -ForegroundColor Green
Set-Location $terraformDir
& $terraformPath destroy -auto-approve

Write-Host ""
Write-Host "Step 2: Creating new infrastructure with updated images..." -ForegroundColor Green
& $terraformPath apply -auto-approve

Write-Host ""
Write-Host "================================================" -ForegroundColor Cyan
Write-Host "  Deployment Complete!" -ForegroundColor Cyan
Write-Host "================================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "Your application URLs:" -ForegroundColor Green
& $terraformPath output

Write-Host ""
Write-Host "Wait 2-3 minutes for containers to start, then access your app!" -ForegroundColor Yellow
Write-Host ""

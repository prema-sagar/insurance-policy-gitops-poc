param(
    [string] $ImageName = 'prema064/insurance-health-component',
    [string] $Tag = 'latest'
)

$full = "${ImageName}:${Tag}"
Write-Host "Building image $full..."

docker build -t $full .
if ($LASTEXITCODE -ne 0) { Write-Error "docker build failed"; exit $LASTEXITCODE }

Write-Host "Pushing image $full to Docker Hub..."
docker push $full
if ($LASTEXITCODE -ne 0) { Write-Error "docker push failed"; exit $LASTEXITCODE }

Write-Host "Done. Image pushed: $full"
param(
    [string] $Image = 'prema064/insurance-health-component:latest',
    [string] $ContainerName = 'insurance',
    [int] $HostPort = 9080,
    [string] $Context = '/insurance',
    [string] $Service = 'HealthPolicySoapService'
)

Write-Host "Pulling image $Image..."
docker pull $Image
if ($LASTEXITCODE -ne 0) { Write-Error "docker pull failed"; exit $LASTEXITCODE }

Write-Host "Removing any existing container named $ContainerName..."
docker rm -f $ContainerName 2>$null | Out-Null

Write-Host "Running container $ContainerName mapping host port $HostPort -> container 9080..."
docker run -d --name $ContainerName -p "$HostPort:9080" $Image | Out-Null
if ($LASTEXITCODE -ne 0) { Write-Error "docker run failed"; exit $LASTEXITCODE }

Write-Host "Waiting 8 seconds for server to start..."
Start-Sleep -Seconds 8

$wsdlUrl = "http://localhost:$HostPort$Context/$Service?wsdl"
Write-Host "Attempting to fetch WSDL from: $wsdlUrl"

try {
    $resp = Invoke-WebRequest -Uri $wsdlUrl -UseBasicParsing -TimeoutSec 30
    $content = $resp.Content
    $outFile = "wsdl.xml"
    $content | Out-File -FilePath $outFile -Encoding utf8
    Write-Host "WSDL written to $outFile; first 400 characters:"
    Write-Host ($content.Substring(0,[Math]::Min(400,$content.Length)))
} catch {
    Write-Error "Failed to fetch WSDL: $_"
    Write-Host "Printing container logs (last 200 lines) to help debug:"
    docker logs --tail 200 $ContainerName
    exit 1
}

Write-Host "WSDL fetch successful. You can inspect wsdl.xml or open the URL in a browser: $wsdlUrl"
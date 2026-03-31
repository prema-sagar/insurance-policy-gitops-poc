# insurance-health-component — Docker build & test

This small guide contains commands and scripts to build the Docker image (with the fixed `server.xml`), push it to Docker Hub, run the container locally, and verify the SOAP WSDL is available.

Prerequisites
- Docker Desktop (or Docker Engine) installed and running on your machine.
- PowerShell (Windows PowerShell or PowerShell Core).
- You must be logged into Docker Hub (`docker login`) if you want to push the image.

Files added
- `scripts\rebuild-and-push.ps1` — builds the image and pushes to Docker Hub.
- `requests\request-soap11.xml` — example SOAP 1.1 request you can POST to the service.

Usage
1) Build and push the image (from the project root):

```powershell
# build and push
.\scripts\rebuild-and-push.ps1 -ImageName prema064/insurance-health-component -Tag latest
```

2) Pull and run the container locally, then POST the SOAP request saved in `requests\request-soap11.xml`:

```powershell
# pull and run container
docker pull prema064/insurance-health-component:latest
docker rm -f insurance 2>$null
docker run -d --name insurance -p 9080:9080 prema064/insurance-health-component:latest
Start-Sleep -Seconds 8

# POST the provided SOAP request (PowerShell)
Invoke-RestMethod -Uri 'http://localhost:9080/insurance-health-component/HealthPolicySoapService' -Method Post -ContentType 'text/xml; charset=utf-8' -Body (Get-Content .\requests\request-soap11.xml -Raw)
```

What the scripts do
- `rebuild-and-push.ps1` runs `docker build` and `docker push`.
Note: the repository previously contained a helper script to fetch the WSDL automatically; that script was removed to keep the repo minimal. Use the commands above to run the container and POST the SOAP request.

Troubleshooting
- If the WSDL isn't available, inspect container logs:

```powershell
docker logs -f insurance
```

- If you see `It is not possible to start two applications called insurance-health-component` in the logs, rebuild the image (so the container contains the corrected `server.xml`) and restart.

Notes
- The `server.xml` in this repo has been fixed to set `contextRoot="/insurance"` so the WSDL should be reachable at `/insurance/HealthPolicySoapService?wsdl`.
- If the application still deploys at `/insurance-health-component/`, the WAR may contain its own context root or an existing deployed copy was present inside the base image — rebuilding and pushing the image from the corrected repo is the reliable fix.

If you'd like, I can also add an automated test (small Java client) that hits the WSDL and calls an operation. Tell me if you prefer that next.

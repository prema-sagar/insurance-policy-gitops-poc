# insurance-health-component — Docker build & test

This small guide contains commands and scripts to build the Docker image (with the fixed `server.xml`), push it to Docker Hub, run the container locally, and verify the SOAP WSDL is available.

Prerequisites
- Docker Desktop (or Docker Engine) installed and running on your machine.
- PowerShell (Windows PowerShell or PowerShell Core).
- You must be logged into Docker Hub (`docker login`) if you want to push the image.

Files added
- `scripts\rebuild-and-push.ps1` — builds the image and pushes to Docker Hub.
- `scripts\check-wsdl.ps1` — pulls/runs the image and fetches the WSDL to verify the SOAP endpoint.

Usage
1) Build and push the image (from the project root):

```powershell
# build and push
.\scripts\rebuild-and-push.ps1 -ImageName prema064/insurance-health-component -Tag latest
```

2) Pull, run and check the WSDL:

```powershell
# pull, run container, and fetch WSDL
.\scripts\check-wsdl.ps1 -Image 'prema064/insurance-health-component:latest' -HostPort 9080 -Context '/insurance' -Service 'HealthPolicySoapService'
```

What the scripts do
- `rebuild-and-push.ps1` runs `docker build` and `docker push`.
- `check-wsdl.ps1` pulls the image, removes any previous container with the same name, starts a new container mapping the host port to container port 9080, waits for the server to start, then attempts to fetch the WSDL URL and writes the WSDL to `wsdl.xml` in the current directory.

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

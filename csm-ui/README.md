# CSM UI

# Build Docker Image

To build to the local Docker daemon, run the following command:

```bash
gradle jibDockerBuild
```

To build and push to a Docker registry, run the following command:

```bash
gradle jib --image=<registry>/csm-ui
```

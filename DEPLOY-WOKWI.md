# Free Wokwi connection through Render

This deployment gives the Spring Boot API a public HTTPS address. Wokwi's free Public IoT Gateway can send HTTP requests to that address.

## Deploy the backend

1. Create a free GitHub repository and upload the complete contents of this `skyshield-spring-boot` folder. Keep `Dockerfile` and `render.yaml` in the repository root.
2. Create a free account at [Render](https://render.com/), connect GitHub, then select **New** → **Blueprint**.
3. Choose the GitHub repository, keep `render.yaml` as the Blueprint path, select the free plan, and approve the deployment.
4. When deployment is live, copy the URL Render gives you. It has this format: `https://skyshield-air-monitor-xxxx.onrender.com`.

## Connect Wokwi

Open `wokwi/sketch.ino` in the Wokwi project. Replace only this line:

```cpp
const char* API_URL = "https://YOUR-RENDER-SERVICE.onrender.com/api/readings";
```

Replace `YOUR-RENDER-SERVICE` with the URL from Render, without the final `/`.

Start the Wokwi simulation. It should print `POST status: 201`. The backend creates `WOKWI-01` automatically on its first reading.

## Important limitation

The Render free plan may spin down after inactivity. The first Wokwi request after a sleep can take time or fail while the service wakes. Start the Render dashboard first when demonstrating. The included H2 database is temporary, so sensor history clears when the Render service restarts; use PostgreSQL for permanent cloud data.

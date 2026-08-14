# Deployment & DevOps Guide: SkyVault

**System:** SkyVault – AI-Based Intelligent Cloud Flight Recorder with Blockchain-Based Data Integrity Verification  
**Module:** Deployment & Infrastructure Management

---

## 1. Local Containerized Setup (Docker Compose)

### Prerequisites
* Docker Desktop 24+ & Docker Compose v2+
* Git

### Step-by-Step Execution
```bash
# 1. Clone repository
git clone https://github.com/vithull-m/SkyVault.git
cd SkyVault

# 2. Build and launch all 4 microservices
docker-compose -f deployment/docker-compose.yml up --build -d

# 3. Check service container statuses
docker-compose -f deployment/docker-compose.yml ps
```

* **Frontend Dashboard**: `http://localhost:3000` (or `http://localhost:80`)
* **Spring Boot API**: `http://localhost:8080/api/v1`
* **FastAPI AI Docs**: `http://localhost:8082/docs`

---

## 2. Cloud Platform Deployments

### 2.1 Render Blueprint Deployment (`deployment/render.yaml`)
1. Connect GitHub repository to Render.
2. Select **New +** ➔ **Blueprint**.
3. Render automatically provisions PostgreSQL, Spring Boot, FastAPI, and Nginx React SPA.

### 2.2 Railway Deployment (`deployment/railway.json`)
1. Run `railway init` and link PostgreSQL plugin.
2. Bind environment variables (`SPRING_DATASOURCE_URL=${{Postgres.DATABASE_URL}}`).
3. Run `railway up`.

### 2.3 Production AWS Infrastructure Architecture
* **Frontend**: React static SPA hosted on **AWS S3** behind **CloudFront CDN**.
* **Microservices**: Containers hosted on **AWS ECS Fargate** behind an **Application Load Balancer (ALB)**.
* **Database**: Encrypted **AWS RDS PostgreSQL** (Multi-AZ).

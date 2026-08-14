from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from loguru import logger
from app.core.settings import settings
from app.core.logging import configure_logger
from app.api.v1.router import router as api_v1_router

# Initialize Loguru Logging
configure_logger()

app = FastAPI(
    title="SkyVault AI Anomaly Detection Engine",
    description="Real-Time Intelligent Flight Telemetry Anomaly Detection & Risk Analysis Service",
    version="1.0.0",
    docs_url="/docs",
    redoc_url="/redoc",
)

# Enable CORS for Frontend & Backend services
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# Register API Router
app.include_router(api_v1_router)


@app.on_event("startup")
def startup_event():
    logger.info("🚀 SkyVault AI Anomaly Detection Service initialized and listening on port " + str(settings.app_port))


@app.get("/health", tags=["Health Check"])
def health_check():
    return {"status": "HEALTHY", "service": "skyvault-ai-service", "environment": settings.app_env}


if __name__ == "__main__":
    import uvicorn
    uvicorn.run("app.main:app", host=settings.app_host, port=settings.app_port, reload=True)

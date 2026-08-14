from pydantic_settings import BaseSettings


class Settings(BaseSettings):
    """
    Centralized application configuration loaded from the .env file.
    Any key defined here can be overridden by environment variables.
    """
    app_host: str = "0.0.0.0"
    app_port: int = 8082
    app_env: str = "development"
    log_level: str = "INFO"
    backend_url: str = "http://localhost:8080"

    class Config:
        env_file = ".env"
        env_file_encoding = "utf-8"


# Singleton instance used across the entire application
settings = Settings()

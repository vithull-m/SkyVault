import sys
from loguru import logger
from app.core.settings import settings


def configure_logger() -> None:
    """
    Removes the default Loguru sink and replaces it with a structured,
    coloured console logger plus a rotating file logger for persistent audit trails.
    """
    logger.remove()  # Remove default handler

    # Console handler with coloured level tags
    logger.add(
        sys.stdout,
        level=settings.log_level,
        colorize=True,
        format=(
            "<green>{time:YYYY-MM-DD HH:mm:ss}</green> | "
            "<level>{level: <8}</level> | "
            "<cyan>{name}</cyan>:<cyan>{function}</cyan>:<cyan>{line}</cyan> - "
            "<level>{message}</level>"
        ),
    )

    # Rotating file handler for persistent anomaly audit logs
    logger.add(
        "logs/skyvault_ai_{time:YYYY-MM-DD}.log",
        level="INFO",
        rotation="1 day",
        retention="30 days",
        compression="zip",
        format="{time:YYYY-MM-DD HH:mm:ss} | {level} | {name}:{function}:{line} - {message}",
    )

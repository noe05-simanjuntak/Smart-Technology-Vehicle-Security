from fastapi import FastAPI
from app.mqtt_handler import start_mqtt
from app.routes import router
from dotenv import load_dotenv
import os

load_dotenv()

app = FastAPI()

@app.on_event("startup")
async def startup_event():
    print("🚀 FastAPI & MQTT Listener aktif...")
    start_mqtt()

app.include_router(router)

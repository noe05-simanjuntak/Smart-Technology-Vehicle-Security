from pymongo.mongo_client import MongoClient
from pymongo.server_api import ServerApi
from app.config import MONGO_URI
from datetime import datetime

client = MongoClient(MONGO_URI, server_api=ServerApi('1'))
db = client.Vehicle_Security

def insert_sensor_data(data: dict):
    # Tambahkan waktu sekarang dalam format ISO
    data["created_at"] = datetime.utcnow().isoformat() + "Z"
    result = db.sensor_data.insert_one(data)
    return str(result.inserted_id)

def get_all_sensor_data():
    logs = db.sensor_data.find().sort("created_at", -1)  # Urut terbaru dulu
    return [log for log in logs]

import json
import random
from datetime import datetime
from paho.mqtt import client as mqtt_client
from app.mongo_handler import insert_sensor_data
from app.fcm_handler import send_push_notification
from pymongo import MongoClient
from app.config import MONGO_URI

broker = "broker.emqx.io"
port = 1883
topic = "iot/vehicle/security"

# Setup koneksi ke MongoDB
client_mongo = MongoClient(MONGO_URI)
db = client_mongo.Vehicle_Security

def on_connect(client, userdata, flags, rc):
    if rc == 0:
        print("✅ MQTT terhubung")
        client.subscribe(topic)
    else:
        print("❌ Gagal terhubung ke MQTT, code:", rc)

def on_message(client, userdata, msg):
    try:
        payload = msg.payload.decode()
        data = json.loads(payload)
        data['created_at'] = datetime.utcnow().isoformat()

        print("📩 Data diterima:", data)
        insert_sensor_data(data)

        alert_type = data.get("alert")
        value = data.get("value")

        # Ambil semua user yang punya token
        users_with_token = db.users.find({"fcm_token": {"$exists": True}})

        for user in users_with_token:
            token = user.get("fcm_token")
            plat = user.get("vehicle_plate", "Kendaraan")

            # Vibration: Guncangan
            if alert_type == "Vibration" and token:
                send_push_notification(
                    token=token,
                    title="🚨 Guncangan Terdeteksi!",
                    body=f"{plat} kamu terguncang!"
                )

            # Seat Status: Jok terbuka
            if alert_type == "Seat Status" and value == "Open" and token:
                send_push_notification(
                    token=token,
                    title="🚨 Jok Terbuka!",
                    body=f"{plat} kamu dibuka oleh seseorang!"
                )

    except Exception as e:
        print("❌ ERROR saat proses MQTT:", e)

def start_mqtt():
    client_id = f'fastapi-mqtt-{random.randint(0, 1000)}'
    client = mqtt_client.Client(client_id)
    client.on_connect = on_connect
    client.on_message = on_message
    client.connect(broker, port)
    client.loop_start()

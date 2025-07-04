from fastapi import APIRouter, HTTPException
from pydantic import BaseModel
from app.mongo_handler import db
from app.models import (
    UserRegister, UserLogin, TokenResponse,
    UpdateVehicleRequest, UpdatePasswordRequest
)
from app.auth import hash_password, verify_password
from typing import Any
import logging

router = APIRouter()

# ------------------- Register -------------------
@router.post("/register")
def register(user: UserRegister) -> dict:
    existing_user = db.users.find_one({"email": user.email})
    if existing_user:
        raise HTTPException(status_code=400, detail="Email sudah terdaftar")

    hashed_pw = hash_password(user.password)

    user_data = {
        "name": user.name,
        "email": user.email,
        "password": hashed_pw,
        "vehicle_plate": user.vehicle_plate,
        "vehicle_type": user.vehicle_type,
        "vehicle_brand": user.vehicle_brand,
        "vehicle_color": user.vehicle_color,
        "fcm_token": None  # Default None, nanti diisi saat user mengirim token
    }

    db.users.insert_one(user_data)
    return {"message": "Registrasi berhasil"}


# ------------------- Login -------------------
@router.post("/login")
def login(credentials: UserLogin) -> dict:
    print(f"LOGIN DARI ANDROID: {credentials.email}")
    user = db.users.find_one({"email": credentials.email})
    if not user:
        raise HTTPException(status_code=401, detail="Email atau password salah")

    logging.warning(f"DEBUG: Input password = {credentials.password}")
    logging.warning(f"DEBUG: Hashed from DB = {user['password']}")
    logging.warning(f"DEBUG: verify_password result = {verify_password(credentials.password, user['password'])}")

    if not verify_password(credentials.password, user["password"]):
        raise HTTPException(status_code=401, detail="Email atau password salah")

    return {
        "message": "Login berhasil",
        "user": {
            "name": user["name"],
            "email": user["email"],
            "vehiclePlate": user.get("vehicle_plate", ""),
            "vehicleType": user.get("vehicle_type", ""),
            "vehicleBrand": user.get("vehicle_brand", ""),
            "vehicleColor": user.get("vehicle_color", "")
        }
    }


# ------------------- FCM Token -------------------
class FcmTokenRequest(BaseModel):
    email: str
    token: str

@router.post("/fcm-token")
def save_fcm_token(data: FcmTokenRequest):
    result = db.users.update_one(
        {"email": data.email},
        {"$set": {"fcm_token": data.token}}
    )

    if result.matched_count == 0:
        raise HTTPException(status_code=404, detail="User tidak ditemukan")

    return {"message": "FCM token disimpan"}


# ------------------- Sensor Data -------------------
@router.get("/sensor-data")
def get_sensor_data_for_android() -> Any:
    data = list(db.sensor_data.find({}, {"_id": 0}))
    return data


# ------------------- Update Kendaraan -------------------
@router.put("/user/vehicle")
def update_vehicle(data: UpdateVehicleRequest):
    result = db.users.update_one(
        {"email": data.email},
        {"$set": {
            "vehicle_type": data.vehicle_type,
            "vehicle_brand": data.vehicle_brand,
            "vehicle_color": data.vehicle_color
        }}
    )

    if result.matched_count == 0:
        raise HTTPException(status_code=404, detail="User tidak ditemukan")

    return {"message": "Data kendaraan berhasil diperbarui"}


# ------------------- Update Password -------------------
@router.put("/user/password")
def update_password(data: UpdatePasswordRequest):
    user = db.users.find_one({"email": data.email})
    if not user or not verify_password(data.old_password, user["password"]):
        raise HTTPException(status_code=401, detail="Password lama salah")

    new_hashed = hash_password(data.new_password)
    db.users.update_one({"email": data.email}, {"$set": {"password": new_hashed}})
    return {"message": "Password berhasil diperbarui"}

# ------------------- Debug Password -------------------
@router.post("/debug-password")
def debug_password_check(password: str, hashed: str):
    result = verify_password(password, hashed)
    return {"match": result}
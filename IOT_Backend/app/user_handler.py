# user_handler.py
from app.mongo_handler import db
from app.auth import hash_password, verify_password
from bson.objectid import ObjectId

user_collection = db["users"]

def create_user(user_data):
    user_data["password"] = hash_password(user_data["password"])
    result = user_collection.insert_one(user_data)
    return str(result.inserted_id)

def get_user_by_email(email):
    return user_collection.find_one({"email": email})

import os
from dotenv import load_dotenv

load_dotenv()

# MongoDB
MONGO_URI = os.getenv("MONGO_URI")

# Firebase
FCM_CREDENTIAL_PATH = os.path.join("app", "firebase", "serviceAccountKey.json")

# Optional: Token untuk testing
FCM_TEST_TOKEN = os.getenv("FCM_TEST_TOKEN")

# JWT
SECRET_KEY = os.getenv("SECRET_KEY")
ALGORITHM = "HS256"

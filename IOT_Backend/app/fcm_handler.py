import firebase_admin
from firebase_admin import credentials, messaging
from app.config import FCM_CREDENTIAL_PATH

# Inisialisasi Firebase hanya sekali
if not firebase_admin._apps:
    cred = credentials.Certificate(FCM_CREDENTIAL_PATH)
    firebase_admin.initialize_app(cred)

def send_push_notification(token: str, title: str, body: str):
    try:
        message = messaging.Message(
            notification=messaging.Notification(title=title, body=body),
            data={
                "title": title, "body": body},
            token=token,
        )
        response = messaging.send(message)
        print("✅ Notifikasi terkirim:", response)
    except Exception as e:
        print("❌ Gagal kirim notifikasi:", e)

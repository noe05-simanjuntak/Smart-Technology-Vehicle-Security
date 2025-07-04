from pydantic import BaseModel, EmailStr, Field


class UserRegister(BaseModel):
    name: str
    email: EmailStr
    password: str
    vehicle_plate: str = Field(..., alias="vehiclePlate")
    vehicle_type: str = Field(..., alias="vehicleType")
    vehicle_brand: str = Field(..., alias="vehicleBrand")
    vehicle_color: str = Field(..., alias="vehicleColor")

    class Config:
        allow_population_by_field_name = True


class UserLogin(BaseModel):
    email: EmailStr
    password: str


class TokenResponse(BaseModel):
    access_token: str
    token_type: str = "bearer"


class UpdateVehicleRequest(BaseModel):
    email: EmailStr
    vehicle_type: str = Field(..., alias="vehicleType")
    vehicle_brand: str = Field(..., alias="vehicleBrand")
    vehicle_color: str = Field(..., alias="vehicleColor")

    class Config:
        allow_population_by_field_name = True


class UpdatePasswordRequest(BaseModel):
    email: EmailStr
    old_password: str
    new_password: str

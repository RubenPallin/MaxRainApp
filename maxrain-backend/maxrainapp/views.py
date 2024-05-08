import json
import secrets
from django.shortcuts import render
from django.views.decorators.csrf import csrf_exempt
from django.http import JsonResponse
from maxrainapp.models import Usuario, Familia, Marca, Articulo, UserSession


# Create your views here.


def sesiones(request):
    if request.method != "POST":
        return JsonResponse({"error": "Not supported HTTP method"}, status=405)
    
    body_json = json.loads(request.body)
    required_params = ["email", "password"]
    missing_params = [param for param in required_params if param not in body_json]

    if missing_params:
        return JsonResponse({"error": "You are missing a parameter"}, status=400)

    json_email = body_json["email"]
    json_password = body_json["password"]

    try:
        db_user = Usuario.objects.get(email=json_email)

    except Usuario.DoesNotExist:
        return JsonResponse({"error": "User not in database"}, status=404)

    if bcrypt.checkpw(json_password.encode("utf8"), db_user.password.encode("utf8")):
        random_token = secrets.token_hex(10)
        session = UserSession(user=db_user, token=random_token)
        session.save()
        return JsonResponse({"token": random_token}, status=201)

    return JsonResponse({"error": "Invalid password"}, status=401)

@csrf_exempt
def registro(request):
    if request.method != "POST":
        return JsonResponse({"error": "Unsupported HTTP method"}, status=405)

    body_json = json.loads(request.body)
    required_params = ["name", "email", "password"]

    if not all(param in body_json for param in required_params):
        return JsonResponse({"error": "Missing parameter in body request"}, status=400)

    json_name = body_json["name"]
    json_email = body_json["email"]
    json_password = body_json["password"]

    if Usuario.objects.filter(name=json_name).exists():
        return JsonResponse({"error": "Username already exist"}, status=400)

    if "@" not in json_email or len(json_email) < 5:
        return JsonResponse({"error": "Not valid email"}, status=400)

    if Usuario.objects.filter(email=json_email).exists():
        return JsonResponse({"error": "User already registered."}, status=401)

    salted_and_hashed_pass = bcrypt.hashpw(
        json_password.encode("utf8"), bcrypt.gensalt()
    ).decode("utf8")

    user_object = Usuario(
        name=json_name, email=json_email, password=salted_and_hashed_pass
    )
    user_object.save()

    # Generamos un token para el usuario
    token = secrets.token_hex(16)
    return JsonResponse({"token": token}, status=200)


@csrf_exempt
def lista_familias(request):
    if request.method != 'GET':
        return JsonResponse({'error': 'Unsupported HTTP method'}, status=405)

    familias = Familia.objects.all()
    data = [{'codigo_familia': familia.codigo_familia, 'descripcion_familia': familia.descripcion_familia} for familia in familias]
    return JsonResponse(data, safe=False)


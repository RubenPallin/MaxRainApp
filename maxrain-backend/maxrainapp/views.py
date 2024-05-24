import json
import secrets
from django.shortcuts import render
from django.views.decorators.csrf import csrf_exempt
from django.http import Http404, JsonResponse
from maxrainapp.models import Usuario, Familia, Marca, Articulo, UserSession


# Create your views here.


def login_sesion(request):
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

    try:
        familias = Familia.objects.all()
    except Familia.DoesNotExist:
        return JsonResponse({'error': 'Familia no encontrada.'}, status=404)
    
    return JsonResponse([familia.to_json() for familia in familias], safe=False, status=200)

@csrf_exempt
def obtener_subfamilias(request, codigo_familia_principal):
    try:
        # Obtener la familia principal
        familia_principal = Familia.objects.get(codigo_familia=codigo_familia_principal)

        # Filtrar las subfamilias de la familia principal
        subfamilias = familia_principal.subfamilias.all()

        # Convertir las subfamilias a un formato JSON
        subfamilias_json = [{'codigo_familia': subfamilia.codigo_familia, 'descripcion_familia': subfamilia.descripcion_familia} for subfamilia in subfamilias]

        return JsonResponse(subfamilias_json, safe=False)
    except Familia.DoesNotExist:
        raise Http404("Familia principal no encontrada")
    

@csrf_exempt
def get_articulos(request, codigo_familia):
    if request.method != 'GET':
        return JsonResponse({'Error': 'Método HTTP no soportado'}, status=405)
    try:
        # Filtrar los artículos por el código de familia
        articulos = Articulo.objects.filter(familia__codigo_familia=codigo_familia)
        articulos_list = list(articulos.values())
        return JsonResponse(articulos_list, safe=False)
    except Articulo.DoesNotExist:
        return JsonResponse({'error': 'Articulo no encontrado.'}, status=404)
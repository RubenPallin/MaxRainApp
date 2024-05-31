import json
import secrets
import bcrypt
from django.shortcuts import render
from django.views.decorators.csrf import csrf_exempt
from django.http import Http404, JsonResponse
from maxrainapp.models import Usuario, Familia, Marca, Articulo, UserSession, Carrito, CarritoItem


# Create your views here.

@csrf_exempt
def login_sesion(request):
    if request.method != "POST":
        return JsonResponse({"error": "Not supported HTTP method"}, status=405)
    
    body_json = json.loads(request.body)
    required_params = ["email", "contraseña"]
    missing_params = [param for param in required_params if param not in body_json]

    if missing_params:
        return JsonResponse({"error": "You are missing a parameter"}, status=400)

    json_email = body_json["email"]
    json_contraseña = body_json["contraseña"]

    try:
        db_user = Usuario.objects.get(email=json_email)

    except Usuario.DoesNotExist:
        return JsonResponse({"error": "User not in database"}, status=404)

    if bcrypt.checkpw(json_contraseña.encode("utf8"), db_user.contraseña.encode("utf8")):
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
    required_params = ["nombre", "apellido", "email", "contraseña"]

    if not all(param in body_json for param in required_params):
        return JsonResponse({"error": "Missing parameter in body request"}, status=400)

    json_nombre = body_json["nombre"]
    json_apellido = body_json["apellido"]
    json_email = body_json["email"]
    json_contraseña = body_json["contraseña"]

    if Usuario.objects.filter(nombre=json_nombre).exists():
        return JsonResponse({"error": "Username already exist"}, status=400)

    if "@" not in json_email or len(json_email) < 5:
        return JsonResponse({"error": "Not valid email"}, status=400)

    if Usuario.objects.filter(email=json_email).exists():
        return JsonResponse({"error": "User already registered."}, status=401)

    salted_and_hashed_pass = bcrypt.hashpw(
        json_contraseña.encode("utf8"), bcrypt.gensalt()
    ).decode("utf8")

    user_object = Usuario(
        nombre=json_nombre, apellido=json_apellido, email=json_email, contraseña=salted_and_hashed_pass
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
        if articulos_list:
            return JsonResponse(articulos_list, safe=False)
        else:
            return JsonResponse({'error': 'Articulos no encontrados para el código de familia proporcionado.'}, status=404)
    except Articulo.DoesNotExist:
        return JsonResponse({'error': 'Articulo no encontrado.'}, status=404)
    

@csrf_exempt
def dar_like(request, codigo_familia):
    try:
        user_token = request.headers.get('user-token')
        if not user_token:
            return JsonResponse({"error": "User token is required"}, status=400)

        user_session = get_object_or_404(UserSession, token=user_token)
        articulo = get_object_or_404(Articulo, codigo_familia=codigo_familia)

        user_db = user_session.user
        articulo_db = articulo

        data = json.loads(request.body)
        liked = data.get('liked', False)

        if liked:
            articulo_db.liked_by.add(user_db)
            return JsonResponse({"message": "The article has been liked"}, status=200)
        else:
            articulo_db.liked_by.remove(user_db)
            return JsonResponse({"message": "The article has been unliked"}, status=200)

    except Articulo.DoesNotExist:
        return JsonResponse({"error": "Article not found"}, status=404)

    except UserSession.DoesNotExist:
        return JsonResponse({"error": "User session not found"}, status=404)

    except json.JSONDecodeError:
        return JsonResponse({"error": "Invalid JSON data in request body"}, status=400)

    except Exception as e:
        return JsonResponse({"error": str(e)}, status=400)
    
def get_authenticated_user(token):
    try:
        session = UserSession.objects.get(token=token)
        return session.user
    except UserSession.DoesNotExist:
        return None

@csrf_exempt
def carrito(request):
    token = request.headers.get('token')
    user = get_authenticated_user(token)
    if user is None:
        return JsonResponse({"error": "Unauthorized: Invalid session token"}, status=401)

    if request.method == 'GET':
        try:
            carrito = Carrito.objects.get(usuario=user)
            items = CarritoItem.objects.filter(carrito=carrito)
            items_data = [{"articulo": item.articulo.to_json_articulos(), "cantidad": item.cantidad} for item in items]
            return JsonResponse({"items": items_data}, status=200)
        except Carrito.DoesNotExist:
            return JsonResponse({"items": []}, status=200)

    elif request.method == 'POST':
        try:
            body_json = json.loads(request.body.decode('utf-8'))
            codigo_articulo = body_json.get('codigo_articulo')
            cantidad = body_json.get('cantidad')

            if not codigo_articulo or cantidad is None:
                return JsonResponse({"error": "Bad Request - 'codigo_articulo' o 'cantidad' no está en el cuerpo de la petición"}, status=400)

            articulo = Articulo.objects.get(codigo_articulo=codigo_articulo)
            carrito, created = Carrito.objects.get_or_create(usuario=user)
            carrito_item, created = CarritoItem.objects.get_or_create(carrito=carrito, articulo=articulo, defaults={"cantidad": cantidad})
            
            if not created:
                carrito_item.cantidad += cantidad
                carrito_item.save()

            return JsonResponse({"message": "Artículo añadido al carrito"}, status=200)
        except Articulo.DoesNotExist:
            return JsonResponse({"error": "Bad Request - El artículo no existe"}, status=400)
        except KeyError:
            return JsonResponse({"error": "Bad Request - El 'codigo_articulo' no está en el cuerpo de la petición"}, status=400)

@csrf_exempt
def carrito_item(request, codigo_articulo):
    token = request.headers.get('token')
    user = get_authenticated_user(token)
    if user is None:
        return JsonResponse({"error": "Unauthorized: Invalid session token"}, status=401)

    if request.method == 'DELETE':
        try:
            carrito = Carrito.objects.get(usuario=user)
            articulo = Articulo.objects.get(codigo_articulo=codigo_articulo)
            carrito_item = CarritoItem.objects.get(carrito=carrito, articulo=articulo)
            carrito_item.delete()
            return JsonResponse({"message": "Artículo eliminado del carrito"}, status=200)
        except Carrito.DoesNotExist:
            return JsonResponse({"error": "Bad Request - El carrito no existe"}, status=400)
        except Articulo.DoesNotExist:
            return JsonResponse({"error": "Bad Request - El artículo no existe"}, status=400)
        except CarritoItem.DoesNotExist:
            return JsonResponse({"error": "Bad Request - El artículo no está en el carrito"}, status=400)
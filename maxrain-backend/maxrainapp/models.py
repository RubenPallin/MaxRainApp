from django.db import models

# Create your models here.

class Usuario(models.Model):
    nombre = models.CharField(max_length=255)
    apellido = models.CharField(max_length=255)
    contraseña = models.CharField(max_length=255)
    email = models.EmailField(unique= True)
    telefono = models.CharField(max_length=20, blank=True, null=True)
    
    def __str__(self):
        return self.name


class UserSession(models.Model):
    user = models.ForeignKey(Usuario, on_delete=models.CASCADE)
    token = models.CharField(unique=True, max_length=45)

class Familia(models.Model):
    codigo_familia = models.CharField(max_length=20, primary_key=True)
    descripcion_familia = models.CharField(max_length=100)
    codigo_padre = models.ForeignKey('self', on_delete=models.CASCADE, null=True, blank=True, related_name='subfamilias')

    def __str__(self):
        return self.descripcion_familia
    

    def to_json(self):
        return {
            "codigo_familia": self.codigo_familia,
            "descripcion_familia": self.descripcion_familia,
        }
    

class Marca(models.Model):
    codigo_marca = models.CharField(max_length=10)
    descripcion = models.TextField()

    def __str__(self):
        return self.descripcion


class Articulo(models.Model):
    codigo_articulo = models.CharField(max_length=20, unique=True)
    descripcion = models.CharField(max_length=255)
    familia = models.ForeignKey(Familia, on_delete=models.CASCADE)
    marca = models.ForeignKey(Marca, on_delete=models.CASCADE)
    enabled = models.BooleanField(default=True)
    ecotasa = models.DecimalField(max_digits=10, decimal_places=2, default=0.00)
    multiplo_cantidad = models.PositiveIntegerField(default=1)
    liquidacion = models.BooleanField(default=False)
    novedad = models.BooleanField(default=False)
    oferta = models.BooleanField(default=False)
    destacado = models.BooleanField(default=False)
    codigo_ean = models.CharField(max_length=13, blank=True, null=True)
    tag = models.CharField(max_length=100, blank=True)
    precio = models.DecimalField(max_digits=10, decimal_places=2, null=True, blank=True)

    def __str__(self):
        return self.descripcion

def to_json(self):
    return {
        'id': self.id,
        'codigo_articulo': self.codigo_articulo,
        'descripcion': self.descripcion,
        'familia': self.familia.descripcion_familia, 
        'marca': self.marca.descripcion,  
        'enabled': self.enabled,
        'ecotasa': float(self.ecotasa),
        'multiplo_cantidad': self.multiplo_cantidad,
        'liquidacion': self.liquidacion,
        'novedad': self.novedad,
        'oferta': self.oferta,
        'destacado': self.destacado,
        'codigo_ean': self.codigo_ean,
        'tag': self.tag,
        'precio': float(self.precio)
    }
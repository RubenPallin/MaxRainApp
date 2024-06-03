from django.contrib import admin

# Register your models here.
from import_export import resources
from import_export.fields import Field
from import_export.admin import ImportExportModelAdmin
from .models import Familia, Marca, Articulo, UserSession, Usuario, Carrito, CarritoItem

class MarcaResource(resources.ModelResource):
    codigo_marca = Field(attribute='codigo_marca', column_name='CODIGO_MARCA')
    descripcion = Field(attribute='descripcion', column_name='DESCRIPCION')

    class Meta:
        model = Marca
        fields = ('codigo_marca', 'descripcion') 
        export_order = ('codigo_marca', 'descripcion')

@admin.register(Marca)
class MarcaAdmin(ImportExportModelAdmin):
    resource_class = MarcaResource


@admin.register(Articulo)
class Articulo(ImportExportModelAdmin):
    pass

@admin.register(Familia)
class Familia(ImportExportModelAdmin):
    pass

@admin.register(UserSession)
class Familia(ImportExportModelAdmin):
    pass

@admin.register(Usuario)
class Familia(ImportExportModelAdmin):
    pass

@admin.register(Carrito)
class Familia(ImportExportModelAdmin):
    pass

@admin.register(CarritoItem)
class CarritoItem(ImportExportModelAdmin):
    pass
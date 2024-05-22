import json
from django.core.management.base import BaseCommand
from maxrainapp.models import Articulo, Familia, Marca  # Asegúrate de que el nombre de tu aplicación es correcto

class Command(BaseCommand):
    help = 'Carga lista de artículos desde un archivo JSON'

    def add_arguments(self, parser):
        parser.add_argument('json_file', type=str, help='Ruta completa al archivo JSON')

    def handle(self, *args, **kwargs):
        file_path = kwargs['json_file']
        try:
            with open(file_path, 'r', encoding='utf-8') as file:
                articulos_data = json.load(file)
        except json.JSONDecodeError as e:
            self.stdout.write(self.style.ERROR(f"Error al leer el archivo JSON: {e}"))
            return
        except FileNotFoundError:
            self.stdout.write(self.style.ERROR(f"Archivo no encontrado: {file_path}"))
            return

        for data in articulos_data:
            try:
                familia = Familia.objects.get(codigo_familia=data['CODIGO_FAMILIA'])
            except Familia.DoesNotExist:
                self.stdout.write(self.style.ERROR(f"Familia con código {data['CODIGO_FAMILIA']} no existe."))
                continue

            try:
                marca = Marca.objects.get(codigo_marca=data['CODIGO_MARCA'])
            except Marca.DoesNotExist:
                self.stdout.write(self.style.ERROR(f"Marca con código {data['CODIGO_MARCA']} no existe."))
                continue

            liquidacion = data['LIQUIDACION'] == 'S'
            novedad = data['NOVEDAD'] == 'S'
            oferta = data['OFERTA'] == 'S'
            destacado = data['DESTACADO'] == 'S'

            Articulo.objects.update_or_create(
                codigo_articulo=data['CODIGO_ARTICULO'],
                defaults={
                    'descripcion': data['DESCRIPCION'],
                    'familia': familia,
                    'marca': marca,
                    'enabled': bool(data['ENABLED']),
                    'ecotasa': data['ECOTASA'],
                    'multiplo_cantidad': data['MULTIPLO_CANTIDAD'],
                    'liquidacion': liquidacion,
                    'novedad': novedad,
                    'oferta': oferta,
                    'destacado': destacado,
                    'codigo_ean': data.get('CODIGO_EAN', None),
                    'tag': data.get('TAG', ''),
                    'precio': data.get('PRECIO', None)
                }
            )

        self.stdout.write(self.style.SUCCESS(f'Datos importados exitosamente desde {file_path}'))

import json
from django.core.management.base import BaseCommand
from maxrainapp.models import Familia 

class Command(BaseCommand):
    help = 'Carga lista de familias desde un archivo JSON'

    def add_arguments(self, parser):
        parser.add_argument('json_file', type=str, help='Ruta completa al archivo JSON')

    def handle(self, *args, **kwargs):
        file_path = kwargs['json_file']
        with open(file_path, 'r', encoding='utf-8') as file:
            familias_data = json.load(file)
            familias_dict = {}

            # Primera pasada: crear todos los objetos sin asignar padres
            for data in familias_data:
                familia, created = Familia.objects.update_or_create(
                    codigo_familia=data['CODIGO_FAMILIA'],
                    defaults={
                        'descripcion_familia': data['DESCRIPCION_FAMILIA'],
                        'codigo_padre': None  # Asegurarse de que el padre se asigna en una segunda pasada
                    }
                )
                familias_dict[data['CODIGO_FAMILIA']] = familia

            # Segunda pasada: actualizar los objetos con sus padres
            for data in familias_data:
                familia = familias_dict[data['CODIGO_FAMILIA']]
                # Asegurarse de que 'CODIGO_PADRE' exista en el diccionario antes de intentar acceder a él
                if 'CODIGO_PADRE' in data and data['CODIGO_PADRE']:
                    padre = familias_dict.get(data['CODIGO_PADRE'])
                    if padre:
                        familia.codigo_padre = padre
                        familia.save()

        self.stdout.write(self.style.SUCCESS(f'Datos importados exitosamente desde {file_path}'))
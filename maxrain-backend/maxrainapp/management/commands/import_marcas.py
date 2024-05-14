import json
from django.core.management.base import BaseCommand
from maxrainapp.models import Marca  

class Command(BaseCommand):
    help = 'Carga lista de marcas desde un archivo JSON'

    def add_arguments(self, parser):
        # Añadir un argumento para la ruta del archivo JSON
        parser.add_argument('json_file', type=str, help='Ruta completa al archivo JSON')

    def handle(self, *args, **kwargs):
        file_path = kwargs['json_file']
        with open(file_path, 'r', encoding='utf-8') as file:
            data = json.load(file)
            for entry in data:
                Marca.objects.create(
                    codigo_marca=entry['CODIGO_MARCA'],
                    descripcion=entry['DESCRIPCION']
                )
        self.stdout.write(self.style.SUCCESS(f'Datos importados exitosamente desde {file_path}'))
# Generated by Django 5.0.4 on 2024-05-08 14:22

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('maxrainapp', '0001_initial'),
    ]

    operations = [
        migrations.AlterField(
            model_name='articulo',
            name='precio',
            field=models.DecimalField(blank=True, decimal_places=2, max_digits=10, null=True),
        ),
    ]

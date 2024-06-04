"""
URL configuration for backend project.

The `urlpatterns` list routes URLs to views. For more information please see:
    https://docs.djangoproject.com/en/5.0/topics/http/urls/
Examples:
Function views
    1. Add an import:  from my_app import views
    2. Add a URL to urlpatterns:  path('', views.home, name='home')
Class-based views
    1. Add an import:  from other_app.views import Home
    2. Add a URL to urlpatterns:  path('', Home.as_view(), name='home')
Including another URLconf
    1. Import the include() function: from django.urls import include, path
    2. Add a URL to urlpatterns:  path('blog/', include('blog.urls'))
"""
from django.contrib import admin
from django.urls import path
from maxrainapp import views
from django.urls import re_path

urlpatterns = [
    path('admin/', admin.site.urls),
    path('sesion/', views.login_sesion),
    path('registro/', views.registro),
    path('familias/', views.lista_familias), 
    # Patrón de URL para la vista obtener_subfamilias
    re_path(r'^subfamilias/(?P<codigo_familia_principal>\w+)/$', views.obtener_subfamilias),
    path('articulos/<str:codigo_familia>/', views.get_articulos),
    path('like/<str:codigo_familia>/', views.dar_like),
    path('carrito/', views.carrito), 
    path('carrito/<str:codigo_articulo>', views.carrito_item),
    path('usuario/', views.usuario_datos),
    path('favoritos/', views.get_favoritos)
]
from django.urls import path, include
from rest_framework import routers
from rest_framework.authtoken.views import obtain_auth_token

from .views import PostListView, PostRetrieveView, PostModelViewSet, signup_api

app_name = 'posts'

router_post = routers.DefaultRouter()
router_post.register('', PostModelViewSet, basename='posts')

urlpatterns = [
    path('signup/', signup_api, name='signup'),
    path('login/', obtain_auth_token, name='login'),

    # path('', PostListView.as_view()),
    # path('<int:pk>/', PostRetrieveView.as_view()),

    path('', include(router_post.urls)),
]
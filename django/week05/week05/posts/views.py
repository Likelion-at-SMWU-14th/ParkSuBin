from django.shortcuts import render
from rest_framework import generics
from rest_framework.viewsets import ModelViewSet
from rest_framework.decorators import api_view
from rest_framework.response import Response
from rest_framework.permissions import IsAuthenticatedOrReadOnly
from django.contrib.auth import get_user_model

from .models import Post
from .serializers import (
    PostModelSerializer,
    PostListSerializer,
    PostRetrieveSerializer,
    PostCreateSerializer,
)

User = get_user_model()


@api_view(['POST'])
def signup_api(request):
    username = request.data.get('username')
    password = request.data.get('password')
    
    # 아이디 중복 체크
    if User.objects.filter(username=username).exists():
        return Response({'error': '이미 존재하는 아이디입니다.'}, status=400)
    
    # 유저 생성 및 저장
    user = User.objects.create_user(username=username, password=password)
    return Response({'message': '회원가입 완료!', 'user_id': user.id}, status=201)



class PostModelViewSet(ModelViewSet):
    queryset = Post.objects.all()
    
    # 로그인한(토큰을 가진) 사용자만 생성/수정/삭제 가능, 목록 조희는 누구나 가능
    permission_classes = [IsAuthenticatedOrReadOnly] 

    def get_serializer_class(self):
        if self.action == 'list':
            return PostListSerializer
        elif self.action == 'retrieve':
            return PostRetrieveSerializer
        elif self.action in ['create', 'update', 'partial_update']:
            return PostCreateSerializer
        return PostListSerializer


class PostListView(generics.ListAPIView, generics.CreateAPIView):
    queryset = Post.objects.all()

    def get_serializer_class(self):
        if self.request.method == 'POST':
            return PostCreateSerializer
        return PostListSerializer

class PostModelViewSet(ModelViewSet):
    queryset = Post.objects.all()

    def get_serializer_class(self):
        if self.action == 'list':
            return PostListSerializer
        elif self.action == 'retrieve':
            return PostRetrieveSerializer
        elif self.action in ['create', 'update', 'partial_update']:
            return PostCreateSerializer
        return PostListSerializer

class PostRetrieveView(
    generics.RetrieveAPIView,
    generics.UpdateAPIView,
    generics.DestroyAPIView
):
    queryset = Post.objects.all()

    def get_serializer_class(self):
        if self.request.method in ["PUT", "PATCH"]:
            return PostCreateSerializer
        return PostRetrieveSerializer
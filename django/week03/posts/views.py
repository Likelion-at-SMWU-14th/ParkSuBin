from django.shortcuts import render, redirect,get_object_or_404
from django.http import HttpResponse,JsonResponse
from django.views import View
from django.views.generic import ListView
from .models import Post,Comment
from .forms import PostBasedForm, PostModelForm, CommentModelForm

def post_delete_view(request, id):
    post = get_object_or_404(Post, id=id)
    if request.method == "POST":
        post.delete()
        return redirect('posts:post-list')
    context = {'post' : post}
    return render(request, 'post_delete_confirm.html', context)

def post_detail_view(request, id):
    post = Post.objects.get(id=id)
    context = {'post':post}
    return render(request, 'post_detail.html',context)

def post_model_form_view(request):
    if request.method == "GET":
        form = PostModelForm()
        context = {'form' : form}
        return render(request, 'post_model_form.html', context)
    else:
        form = PostModelForm(request.POST, request.FILES)
        if form.is_valid():
            form.save()
        else:
            print(form.errors)
            return render(request, 'post_modelform.html',{'form':form})
        return redirect('posts:post-list')

def post_form_view(request):
    if request.method == "GET":
        form=PostBasedForm()
        context = {'form':form}
        return render(request, 'post_form.html',context)
    else:
        form = PostBasedForm(request.POST, request.FILES)
        if form.is_valid():
            Post.objects.create(
                image=form.cleaned_data['image'],
                content=form.cleaned_data['content']
            )
        else:
            print(form.errors)
            return render(request, 'post_form.html',{'form':form})
        return redirect('posts:post-list')
def post_list_view(request):
    posts =Post.objects.all()
    context={'posts':posts}
    return render(request,'post_list.html',context)

def url_view(request):
    data = {'code' : 200, 'message':'url_view'}
    return JsonResponse(data)

def url_parameter_view(request,username):
    age = request.GET.get('age',None)
    print(username)
    print(request.GET)
    return HttpResponse(f"사용자 이름은 {username} 입니다.나이는 {age}세 입니다.")

def function_view(request):
    print(f'request.method: {request.method}')
    print(f'request.GET: {request.GET}')
    print(f'request.POST: {request.POST}')

    context={
        "view_type":"Fuction Based View",
    }

    return render(request,'view.html',context)
class class_view(View):

    context = {
            "view_type": "Class Based View",
        }

    def get(self, request):
        print(f'request.method: {request.method}')
        print(f'request.GET: {request.GET}')
        return render(request,"view.html" ,self.context)

    def post(self, request):
        print(f'request.method: {request.method}')
        print(f'request.POST: {request.POST}')
        return render(request,"view.html", self.context)

class class_view2(ListView):
    model = Post
    template_name = 'cbv_view.html'
def home_view(request):
    return render(request, "home.html")

def post_update_view(request, id):
    post = Post.objects.get(id=id)    
    if request.method == "GET":
        form = PostModelForm(instance=post)
        context = {'form' : form, 'post': post}
        return render(request, 'post_update.html', context)
    else:
        form = PostModelForm(request.POST, request.FILES, instance=post)
        if form.is_valid():
            form.save()
        else:
            print(form.errors)
            return render(request, 'post_update.html', {'form' : form})
        return redirect('posts:post-detail', id=id)
    
def comment_create_view(request, id): # 여기서 id는 댓글이 달릴 '게시글의 id'입니다.
    post = get_object_or_404(Post, id=id)

    if request.method == "GET":
        # GET 요청: 빈 빈칸(Form)을 화면에 띄워줍니다.
        form = CommentModelForm()
        context = {'form': form}
        return render(request, 'comment_form.html', context)
        
    else:
        # POST 요청: 사용자가 입력한 데이터를 받아와 검증합니다.
        form = CommentModelForm(request.POST)
        if form.is_valid(): # 데이터가 유효하다면 [2]
            # 💡 핵심: DB에 바로 저장하지 않고 임시 저장(commit=False) 상태로 둡니다.
            comment = form.save(commit=False)
            
            # 비어있는 '게시글 정보'와 '작성자 정보'를 채워줍니다.
            comment.post = post
            if request.user.is_authenticated: # 3주차 1번 과제(회원가입/로그인)와 연계
                comment.writer = request.user 
                
            # 이제 완벽해졌으니 진짜로 DB에 저장합니다!
            comment.save() 
            
            # 생성이 끝나면 해당 게시글 상세 페이지로 돌려보냅니다.
            return redirect('posts:post-detail', id=post.id)
    
def comment_update_view(request, id):
    # 수정할 기존 댓글 객체를 DB에서 찾아옵니다.
    comment = get_object_or_404(Comment, id=id) 
    
    if request.method == "GET":
        # GET 요청: 기존 댓글 내용(instance=comment)이 미리 채워진 폼을 만들어 화면에 넘겨줍니다.
        form = CommentModelForm(instance=comment)
        context = {'form': form, 'comment': comment}
        return render(request, 'comment_update.html', context)
        
    else:
        # POST 요청: 사용자가 제출한 수정 데이터(request.POST)를 폼에 담고 검사합니다.
        form = CommentModelForm(request.POST, instance=comment)
        if form.is_valid():  # 데이터에 문제가 없다면
            form.save()      # DB에 변경된 내용을 바로 저장합니다!
            
            # 수정 완료 후 해당 댓글이 원래 있던 게시글 상세 페이지 등으로 돌려보냅니다.
            # (아래 'post-detail' 부분은 본인이 urls.py에 지정한 이름에 맞게 수정하세요)
            return redirect('posts:post-detail', id=comment.post.id)     


# Create your views here.

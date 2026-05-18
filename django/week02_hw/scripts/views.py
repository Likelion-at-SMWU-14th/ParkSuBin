from django.shortcuts import render

from django.views.generic import ListView
from .models import BookQuote  

class QuoteListView(ListView):
    model = BookQuote
    template_name = 'quote_list.html'

def about_view(request):
    if request.method == "POST":
        title = request.POST.get('title')
        dialogue = request.POST.get('dialogue')
        
        BookQuote.objects.create(
            title=title,
            dialogue=dialogue
        )
        return redirect('quote_list')
        

    return render(request, 'about.html')


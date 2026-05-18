from django.db import models

class BookQuote(models.Model):
    title = models.CharField(max_length=200, verbose_name='책 제목')
    quote = models.TextField(verbose_name='책 대사')
    created_at = models.DateTimeField(auto_now_add=True, verbose_name='작성일')
    def __str__(self):
        return self.title
# Create your models here.

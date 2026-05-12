from django.contrib import admin
from .models import Blog

#admin.site.register(Blog)
@admin.register(Blog)
class BlogAdmin(admin.ModelAdmin):
    list_display = ('id', 'title', 'created_at', 'has_image')
    
    list_display_links = ('title',)
    
    list_filter = ('created_at',)
    
    search_fields = ('title',)

    def has_image(self, obj):
        return bool(obj.image)
    has_image.short_description = '사진 유무'
    has_image.boolean = True

# Register your models here.

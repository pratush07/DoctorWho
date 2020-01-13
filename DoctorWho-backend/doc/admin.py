from django.contrib import admin
from .models import Specialist
from .models import User
from .models import Comment
from .models import Doclist
from .models import Doctor
from .models import Rate
from .models import AverageRate
from .models import AverageComRate
from .models import DoctorAppTime

admin.site.register(User)
admin.site.register(Specialist)
admin.site.register(Comment)
admin.site.register(Doclist)
admin.site.register(Doctor)
admin.site.register(Rate)
admin.site.register(AverageRate)
admin.site.register(AverageComRate)
admin.site.register(DoctorAppTime)
from django.db import models
from datetime import datetime
class User(models.Model):
	firstName = models.CharField(max_length=30)
	lastName = models.CharField(max_length=30)
	email = models.EmailField(max_length=70,blank=True, null=True)
	gender=models.CharField(max_length=1,null= True)
	profilePic=models.CharField(max_length=300,null=True)

class Doctor(models.Model):
	firstName = models.CharField(max_length=30)
	lastName = models.CharField(max_length=30)
	email = models.EmailField(max_length=70,blank=True, null=True)
	gender=models.CharField(max_length=1,null= True)
	profilePic=models.CharField(max_length=300,null=True)
	qualification=models.CharField(max_length=40,null=True)
	rate=models.DecimalField(null=True,max_digits=5, decimal_places=2)
	consultation=models.IntegerField(null= True)
	address=models.CharField(max_length=50,null=True)

class Specialist(models.Model):
	cat=models.CharField(max_length=20)
	catID=models.IntegerField(null= True)

class Doclist(models.Model):
	catID=models.IntegerField(null= True)
	usrID = models.IntegerField(null=True)

class Comment(models.Model):
	com_from=models.IntegerField(null= True)
	com_to=models.IntegerField(null= True)
	com=models.CharField(max_length=500)
	com_type=models.CharField(max_length=12,null=True)

class Rate(models.Model):
	rate_from=models.IntegerField(null= True)
	rate_to=models.IntegerField(null= True)
	rate=models.FloatField(null= True)

class AverageRate(models.Model):
	doc_id=models.IntegerField(null= True)
	rate=models.FloatField(null= True)

class AverageComRate(models.Model):
	doc_id=models.IntegerField(null= True)
	rate=models.FloatField(null= True)

class DoctorAppTime(models.Model):
	doc_id=models.IntegerField(null= True)
	pat_id=models.IntegerField(null= True)
	appt_date = models.DateField(blank=True, null=True)
	appt_time = models.TimeField(blank=True, null=True)
	accepted = models.IntegerField(null= True)
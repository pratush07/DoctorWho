from rest_framework import serializers

from doc.models import User
from doc.models import Specialist
from doc.models import Comment
from doc.models import Doclist
from doc.models import Doctor
from doc.models import Rate
from doc.models import AverageRate
from doc.models import AverageComRate
from doc.models import DoctorAppTime


class UserSerializer(serializers.ModelSerializer):

	class Meta:
		model = User
		fields = ('id', 'firstName', 'lastName','email','gender','profilePic')

class DoctorSerializer(serializers.ModelSerializer):

	class Meta:
		model = Doctor
		fields = ('id', 'firstName', 'lastName','email','gender','profilePic','qualification','rate','consultation','address')

class SpecialistSerializer(serializers.ModelSerializer):
	class Meta:
		model = Specialist
		fields = ('cat','catID')

class CommentSerializer(serializers.ModelSerializer):
	class Meta:
		model = Comment
		fields = ('com_from','com_to','com','com_type')

class DoclistSerializer(serializers.ModelSerializer):
	class Meta:
		model = Doclist
		fields = ('catID','usrID')

class RateSerializer(serializers.ModelSerializer):
	class Meta:
		model = Rate
		fields = ('rate_from','rate_to','rate')

class AverageRateSerializer(serializers.ModelSerializer):
	class Meta:
		model = AverageRate
		fields = ('doc_id','rate')

class AverageComRateSerializer(serializers.ModelSerializer):
	class Meta:
		model = AverageComRate
		fields = ('doc_id','rate')

class DoctorAppTimeSerializer(serializers.ModelSerializer):
	class Meta:
		model = DoctorAppTime
		fields = ('id','doc_id','pat_id','appt_date','appt_time','accepted')
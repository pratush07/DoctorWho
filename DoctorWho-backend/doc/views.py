from __future__ import division
from rest_framework import status
from alchemyapi import AlchemyAPI
from rest_framework.decorators import api_view
from rest_framework.response import Response
from doc.models import User
from doc.models import Doclist
from doc.models import Doctor
from doc.models import Comment
from doc.models import Specialist
from doc.models import Rate
from doc.models import AverageRate
from doc.models import AverageComRate
from doc.models import DoctorAppTime
from doc.serializers import UserSerializer
from doc.serializers import SpecialistSerializer
from doc.serializers import CommentSerializer
from doc.serializers import DoclistSerializer
from doc.serializers import DoctorSerializer
from doc.serializers import RateSerializer
from doc.serializers import AverageRateSerializer
from doc.serializers import AverageComRateSerializer
from doc.serializers import DoctorAppTimeSerializer
from django.db.models import F
import json
from django.http import QueryDict
from django.core.mail import send_mail

@api_view(['GET', 'POST'])
def user_list(request):
    """
    List all tasks, or create a new task.
    """
    if request.method == 'GET':
        users = User.objects.all()
        serializer = UserSerializer(users, many=True)
        return Response(serializer.data)

    elif request.method == 'POST':
        serializer = UserSerializer(data=request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data, status=status.HTTP_201_CREATED)
        else:
            return Response(
                serializer.errors, status=status.HTTP_400_BAD_REQUEST)


@api_view(['GET', 'PUT', 'DELETE'])
def user_detail(request, pk):
    """
    Get, udpate, or delete a specific task
    """
    try:
        user = User.objects.get(pk=pk)
    except User.DoesNotExist:
        return Response(status=status.HTTP_404_NOT_FOUND)

    if request.method == 'GET':
        serializer = UserSerializer(user)
        return Response(serializer.data)

    elif request.method == 'PUT':
        serializer = UserSerializer(user, data=request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data)
        else:
            return Response(
                serializer.errors, status=status.HTTP_400_BAD_REQUEST)

    elif request.method == 'DELETE':
        user.delete()
        return Response(status=status.HTTP_204_NO_CONTENT)

@api_view(['GET'])
def user_email(request,email):
    "method is for users who reinstalled the app"
    try:
        user=User.objects.get(email=email)
    except User.DoesNotExist:
        return Response(status=status.HTTP_404_NOT_FOUND)

    if request.method == 'GET':
        serializer=UserSerializer(user)
        return Response(serializer.data)

@api_view(['GET', 'POST'])
def doctor_list(request):
    """
    List all tasks, or create a new task.
    """
    if request.method == 'GET':
        doctors = Doctor.objects.all()
        serializer = DoctorSerializer(doctors, many=True)
        return Response(serializer.data)

    elif request.method == 'POST':
        serializer = DoctorSerializer(data=request.data)
        if serializer.is_valid():
            serializer.save()
            getid=Doctor.objects.filter(email=request.data["email"])
            dict1 = {'doc_id':getid[0].id, 'rate':'0', }   
            qdict = QueryDict('',mutable=True)
            qdict.update(dict1)
            serializer2=AverageRateSerializer(data=qdict)
            if serializer2.is_valid():
                serializer2.save()

            dict2 = {'doc_id':getid[0].id, 'rate': '0',}
            qdict = QueryDict('',mutable=True)
            qdict.update(dict2)
            serializer2=AverageComRateSerializer(data=dict2)
            if serializer2.is_valid():
                serializer2.save()
                
            return Response(serializer.data, status=status.HTTP_201_CREATED)
        else:
            return Response(
                serializer.errors, status=status.HTTP_400_BAD_REQUEST)


@api_view(['GET', 'PUT', 'DELETE'])
def doctor_detail(request, pk):
    """
    Get, udpate, or delete a specific task
    """
    try:
        doctor = Doctor.objects.get(pk=pk)
    except Doctor.DoesNotExist:
        return Response(status=status.HTTP_404_NOT_FOUND)

    if request.method == 'GET':
        serializer = DoctorSerializer(doctor)
        return Response(serializer.data)

    elif request.method == 'PUT':
        serializer = DoctorSerializer(doctor, data=request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data)
        else:
            return Response(
                serializer.errors, status=status.HTTP_400_BAD_REQUEST)

    elif request.method == 'DELETE':
        doctor.delete()
        return Response(status=status.HTTP_204_NO_CONTENT)

@api_view(['GET'])
def doctor_email(request,email):
    "method is for users who reinstalled the app"
    try:
        doctor=Doctor.objects.get(email=email)
    except Doctor.DoesNotExist:
        return Response(status=status.HTTP_404_NOT_FOUND)

    if request.method == 'GET':
        serializer=DoctorSerializer(doctor)
        return Response(serializer.data)

@api_view(['GET'])
def specialist_cat(request,pattern):
    "method to search for a category"
    try:
        catg = Specialist.objects.filter(cat__contains=pattern)
    except Specialist.DoesNotExist:
        return Response(status=status.HTTP_404_NOT_FOUND)
    serializer=SpecialistSerializer(catg,many=True)
    return Response(serializer.data)

@api_view(['POST'])
def comment_store(request):
    avgcom=0
    "method is for analyzing and storing comments"
    alchemyapi = AlchemyAPI()
    response = alchemyapi.sentiment("text",request.data["com"])
    dict1 = {'com_from':request.data["com_from"], 'com_to': request.data["com_to"],'com':request.data["com"],'com_type': response["docSentiment"]["type"]}
    qdict = QueryDict('',mutable=True)
    qdict.update(dict1)
    serializer = CommentSerializer(data=qdict)
    if serializer.is_valid():
        serializer.save()
        doccomrate=Comment.objects.filter(com_to=request.data["com_to"])
        doccomratepos=Comment.objects.filter(com_to=request.data["com_to"],com_type='positive')
        avgcom=(len(doccomratepos)/float(len(doccomrate)))*10
        try:
            temp=AverageComRate.objects.get(doc_id=request.data["com_to"])
        except AverageComRate.DoesNotExist:
            dict2 = {'doc_id':request.data["com_to"], 'rate': avgcom,}
            qdict = QueryDict('',mutable=True)
            qdict.update(dict2)
            serializer2=AverageComRateSerializer(data=dict2)
            if serializer2.is_valid():
                serializer2.save()
                return Response(serializer2.data, status=status.HTTP_201_CREATED)
        
        avgcomupdate=AverageComRate.objects.filter(doc_id=request.data["com_to"]).update(rate=avgcom)
        temp2=AverageComRate.objects.get(doc_id=request.data["com_to"])
        serializer3=AverageComRateSerializer(temp2)
        return Response(serializer3.data, status=status.HTTP_201_CREATED)
  
    else:
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

@api_view(['GET'])
def comment_ret(request,pk):
    "method for retrieving comments for id"
    try:
        comment = Comment.objects.filter(com_to=pk)
    except Comment.DoesNotExist:
        return Response(status=status.HTTP_404_NOT_FOUND)
    serializer = CommentSerializer(comment, many=True)
    return Response(serializer.data)


@api_view(['POST'])
def doc_catsave(request):
    "method for saving docid and catid"
    serializer = DoclistSerializer(data=request.data)
    if serializer.is_valid():
        serializer.save()
        return Response(serializer.data, status=status.HTTP_201_CREATED)
    else:
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

@api_view(['PUT'])
def doc_catupdate(request,pk):
    "method for updating docid with catid in case user deletes the app by mistake"
    try:
        doc = Doclist.objects.get(usrID=pk)
    except Doclist.DoesNotExist:
        return Response(status=status.HTTP_404_NOT_FOUND)
    serializer = DoclistSerializer(doc, data=request.data)
    if serializer.is_valid():
        serializer.save()
        return Response(serializer.data)
    else:
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

@api_view(['GET'])
def get_doclist(request,catID):
    "method to get list of doctors for a given category ID"
    try:
        doc=Doclist.objects.filter(catID=catID)
    except Doclist.DoesNotExist:
        return Response(status=status.HTTP_404_NOT_FOUND)
    out_id=[]
    for x in range(0,len(doc)): 
        out_id.append(doc[x].usrID)
    doctor=Doctor.objects.filter(pk__in=out_id)
    serializer=DoctorSerializer(doctor,many=True)
    return Response(serializer.data)

@api_view(['POST'])
def rate(request,pk):
    "rate a doctor with id pk"
    avg=0
    serializer = RateSerializer(data=request.data)
    if serializer.is_valid():
        serializer.save()
        doctor=Rate.objects.filter(rate_to=pk)
        for x in range(0,len(doctor)):
            avg+=doctor[x].rate
        avg=avg/len(doctor)
        
        try:
            doc=AverageRate.objects.get(doc_id=pk)
        except AverageRate.DoesNotExist:
            dict1 = {'doc_id':int(pk), 'rate': avg, }
            qdict = QueryDict('',mutable=True)
            qdict.update(dict1)
            serializer2=AverageRateSerializer(data=qdict)
            if serializer2.is_valid():
                serializer2.save()
                return Response(serializer2.data, status=status.HTTP_201_CREATED)
        doctorupdate=AverageRate.objects.filter(doc_id=pk).update(rate=avg)
        doc=AverageRate.objects.get(doc_id=pk)
        serializer3=AverageRateSerializer(doc)
        return Response(serializer3.data, status=status.HTTP_201_CREATED)
    else:
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

@api_view(['GET'])
def averagerate(request,pk): 
    "get average of comment and rate" 
    c=AverageComRate.objects.filter(doc_id=pk)
    r=AverageRate.objects.filter(doc_id=pk)
    avg=c[0].rate+r[0].rate
    avg/=2;
    doc=Doctor.objects.filter(id=pk).update(rate=avg)
    doc=Doctor.objects.get(id=pk)
    serializer=DoctorSerializer(doc)
    return Response(serializer.data, status=status.HTTP_201_CREATED)

@api_view(['GET', 'POST','DELETE'])
def bookappuser(request,pk): 
    "booking or getting a list of appointments for user"
    if request.method == 'GET':
        appts = DoctorAppTime.objects.filter(pat_id=pk)
        serializer = DoctorAppTimeSerializer(appts, many=True)
        return Response(serializer.data)

    elif request.method == 'POST':
        serializer = DoctorAppTimeSerializer(data=request.data)
        if serializer.is_valid():
            serializer.save()
            #send_mail('Appointment booked', 'your appointment has been booked.We will confirm the appointment shortly.', 'pratushpandita07@gmail.com',['pratushpandita07@gmail.com'], fail_silently=False)
            return Response(serializer.data, status=status.HTTP_201_CREATED)
        else:
            return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

    elif request.method == 'DELETE':
        apptuser=DoctorAppTime.objects.get(id=pk)
        apptuser.delete()
        return Response(status=status.HTTP_204_NO_CONTENT)

@api_view(['GET', 'PUT','DELETE'])
def bookappdoc(request,pk):
    "getting/updating a list of appointments for doctor"
    if request.method == 'GET':
        appts = DoctorAppTime.objects.filter(doc_id=pk)
        serializer = DoctorAppTimeSerializer(appts, many=True)
        return Response(serializer.data)

    elif request.method == 'PUT':
        apptuser=DoctorAppTime.objects.filter(id=pk).update(accepted=request.data['accepted'])
        appts = DoctorAppTime.objects.filter(doc_id=pk)
        serializer = DoctorAppTimeSerializer(appts, many=True)
        return Response(serializer.data)
        
    elif request.method == 'DELETE':
        apptuser=DoctorAppTime.objects.get(id=pk)
        apptuser.delete()
        return Response(status=status.HTTP_204_NO_CONTENT)
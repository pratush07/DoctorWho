# DoctorWho

## Introduction

The backend APIs for DoctorWho are written in python. These APIs will be called by the mobile app made in Android. Please note that AlchemyAPI was used for sentiment analysis which has been now archived. https://github.com/AlchemyAPI/alchemyapi_python

## Prerequisite

1. Knowledge of python language (Python 3)
2. Knowledge of Django Framework

## Basic Setup
1. Create a new environment . *virtual -p python3 env*
4. activate the environment. *source env/bin/activate*
5. Make sure this repository and environment folder are at the same subdirectory level.
5. Install all the required dependencies from the requirements.txt using this : *pip install -r requirements.txt*

## Running the application

1. Add schema to postgres db using this : *python manage.py migrate*
2. Run the development server: *python manage.py runserver* . This will run the application on 8000 port on default.
3. Try hitting this api from postman.

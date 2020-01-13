from django.conf.urls import url
from doc.views import user_list,user_detail,user_email,specialist_cat,comment_store,doc_catsave,doc_catupdate,get_doclist,doctor_list,doctor_email,doctor_detail,rate,comment_ret,averagerate,bookappuser,bookappdoc
urlpatterns = [
    url(r'^user/$', user_list),
    url(r'^user/(?P<pk>[0-9]+)$',user_detail),
    url(r'^user/(?P<email>[\w.%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,4})/$',user_email),
    url(r'^doctor/$', doctor_list),
    url(r'^doctor/(?P<pk>[0-9]+)$',doctor_detail),
    url(r'^doctor/(?P<email>[\w.%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,4})/$',doctor_email),
    url(r'^specialist/(?P<pattern>[A-Za-z]+)$',specialist_cat),
    url(r'^comment/$',comment_store),
    url(r'^comment/(?P<pk>[0-9]+)$',comment_ret),
    url(r'^doccat/',doc_catsave),
    url(r'^updatecat/(?P<pk>[0-9]+)$',doc_catupdate),
    url(r'^getdoclist/(?P<catID>[0-9]+)$',get_doclist),
    url(r'^ratedoc/(?P<pk>[0-9]+)$',rate),
    url(r'^averagerate/(?P<pk>[0-9]+)$',averagerate),
    url(r'^bookappuser/(?P<pk>[0-9]+)$',bookappuser),
    url(r'^bookappdoc/(?P<pk>[0-9]+)$',bookappdoc),
]
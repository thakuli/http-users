FROM openshift/base-centos7
MAINTAINER tero.hakuli@nokia.com

LABEL io.k8s.description="Platform for building and running Clojure apps with Leiningen" \
      io.k8s.display-name="Leiningen 0.1" \
      io.openshift.expose-services="8080:http" \
      io.openshift.tags="builder,leiningen,clojure" \
      io.openshift.s2i.destination="/opt/s2i/destination"

EXPOSE 8080

ENV http_proxy http://10.144.1.10:8080
ENV https_proxy https://10.144.1.10:8080
ENV LEIN_ROOT true

#RUN useradd -r -u 1101 leiningen 
#USER leiningen

RUN yum -y update && yum clean all

RUN yum install -y wget && yum install -y java-1.8.0-openjdk-devel.x86_64
RUN wget https://raw.githubusercontent.com/technomancy/leiningen/stable/bin/lein -o /tmp/lein-download.log -O /usr/bin/lein && chmod a+x /usr/bin/lein 

RUN mkdir -p /opt/s2i/destination
COPY ./s2i/bin/ $STI_SCRIPTS_PATH

#USER leiningen
CMD $STI_SCRIPTS_PATH/usage

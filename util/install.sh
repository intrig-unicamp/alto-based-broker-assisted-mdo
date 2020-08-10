#!/usr/bin/env bash

strDIR="${PWD}"

#########################################
# escape
#########################################

cd ${strDIR}/util/

#if [ ! -d ../escape ]; then
echo "=== Clone escape module ==="
git clone git@213.16.101.140:escape/escape.git ../escape
#git submodule add git@213.16.101.140:escape/escape.git ../escape

cp ./wait-for-it.sh ../escape

cd ${strDIR}/escape/

echo "=== escape: Managing the dependencies ==="
./install-dep.sh -p ericsson

#ENTRYPOINT ["python", "escape.py"]
sed -i "s/ENTRYPOINT/#ENTRYPOINT/g" ./Dockerfile

#CMD ["--debug", "--rosapi", "--config", "config/escape-static-dummy.config"]
sed -i "s/CMD/#CMD/g" ./Dockerfile
#fi

echo "BGPLSRestAdapter: Change REST API name: virtualizer -> topologyFromALTO"
sed -i "s/'virtualizer'/'topologyFromALTO'/g" ./escape/escape/adapt/adapters.py

echo "escape-bgp-ls-test.config: Change Remote URL"
sed -i "s/localhost:8087/172.25.0.20:8088/g" ./config/escape-bgp-ls-test.config

#########################################
#########################################
#########################################

#########################################
# Maven
#########################################
cd /opt/

echo "=== Install maven ==="
#sudo apt-get install maven
sudo wget http://www-eu.apache.org/dist/maven/maven-3/3.3.9/binaries/apache-maven-3.3.9-bin.tar.gz
sudo tar -xvzf apache-maven-3.3.9-bin.tar.gz

sudo update-alternatives --remove-all maven
sudo update-alternatives --install /usr/bin/mvn maven /opt/apache-maven-3.3.9/bin/mvn 1001

#########################################
#########################################
#########################################

#########################################
# opendaylight: Edit your ~/.m2/settings.xml
# https://wiki.opendaylight.org/view/GettingStarted:Development_Environment_Setup
#########################################
if [ ! -d ~/.m2 ]; then
  echo "Creating .m2 directory"
  mkdir ~/.m2
fi

if [ -f ~/.m2/settings.xml ]; then
  echo "Creating a settings.xml file copy"
  cp -n ~/.m2/settings.xml{,.orig}
fi

echo "opendaylight: Edit ~/.m2/settings.xml"
wget -q -O - https://raw.githubusercontent.com/opendaylight/odlparent/master/settings.xml > ~/.m2/settings.xml

#########################################
#########################################
#########################################

#########################################
# TADS
#########################################

cd ${strDIR}/netphony-topology/

echo "Maven install: netphony-topology"
mvn clean package -P generate-full-jar

#########################################
#########################################
#########################################

#########################################
# ALTO-AGGREGATOR
#########################################

cd ${strDIR}/alto-aggregator-module/

echo "Maven install: alto-aggregator-module"
mvn clean package -P generate-full-jar

#########################################
#########################################
#########################################

#########################################
# ALTO
#########################################

cd ${strDIR}/alto-module/

echo "Maven install: alto-module"
mvn clean package -P generate-full-jar

#########################################
#########################################
#########################################

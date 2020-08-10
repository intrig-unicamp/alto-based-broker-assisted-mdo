import logging
level = logging.INFO
formatter = logging.Formatter('%(asctime)s - %(name)s - %(levelname)s - %(message)s')
handler = logging.StreamHandler()
handler.setFormatter(formatter)
handler.setLevel(level)
logging.getLogger().addHandler(handler)
logging.getLogger().setLevel(level)
logger = logging.getLogger(__name__)

################################################################
import os
import site
import sys
PROJECT_ROOT = os.path.abspath(os.path.dirname(__file__))
site.addsitedir(sitedir=PROJECT_ROOT)
sys.path.remove(PROJECT_ROOT)
sys.path.insert(0, PROJECT_ROOT + "/virt-neo4j")
sys.path.insert(0, PROJECT_ROOT + "/virtualizer")

#sys.path.insert(0, PROJECT_ROOT + "/util")
#from domain import *
#from misc import unicode_to_str

import requests
import json
import signal
################################################################

from neo4j_virtualizer import Neo4jVirt
from virtualizer import Virtualizer

import time

import socket
import sys

#def signal_handler(signal, frame):
#    print('You pressed Ctrl+C!')
#    sys.exit(0)

if __name__ == "__main__":

    time.sleep(int(sys.argv[4]))
    #print('After 20')

    #_base_url = 'http://172.27.0.10:8888'
    #_prefix = 'escape/'
    #a= RESTAdapter(_base_url, _prefix)
    #POST = "POST"
    #data = a.send_no_error(POST, 'get-config')

    EXTERNAL_MDO_META_NAME = 'unify-slor'
    PORTslor="8888"
    PORTcfor="8889"

    #print('http://'+str(sys.argv[1])+'/restconf/data/interDomainTopo/')
    #dataTemp = requests.get('http://127.0.0.1:8088/restconf/data/virtualizer')
    #dataTemp = requests.get('http://172.28.0.20:8088/restconf/data/interDomainTopo/')
    dataTemp = requests.get('http://'+str(sys.argv[1])+'/restconf/data/interDomainTopo/')

    data = dataTemp.json()
    #network_topo = json.loads(data.text, object_hook=unicode_to_str)
    #print (data.json())
    #v = Virtualizer.parse_from_json(data.text)

    for node in data['nodes']['node']:
        node_id = node['id']
        logger.info('Detected node: %s' % node_id)
        for meta in node['metadata']:
            logger.info("Add metadata to Infra node: %s" % meta)
            name=meta['key']
            value=meta['value']
            if name == EXTERNAL_MDO_META_NAME:
                mdoURL = str(value).replace("https","http").replace(PORTslor,PORTcfor)+ "/dov/get-config"
                #mdoURL = str(value).replace("https","http") + "/get-config"
                logger.info("MdO URL: %s" % mdoURL)
                neo4j = Neo4jVirt()
                data = requests.post(mdoURL)
                #print (data.text)
                logger.info('starting test 1')
                v = Virtualizer.parse_from_text(data.text)
                v.set_operation('create', recursive=True)

                strQuery = "MERGE (node1:DOMAIN {ASN:'%s'}) ON CREATE SET node1 = {ASN:'%s'} ON MATCH SET node1 = {ASN:'%s'}" % (node_id, node_id, node_id)
                neo4j.push_v2(strQuery)

                start = time.time()
                neo4j.load_v2(v, node_id)
                end = time.time()
                print(end-start)
                logger.info('finished test 1')

                neo4j.load_domain(v, node_id)

    # Create a TCP/IP socket
    sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    # Bind the socket to the port
    server_address = (str(sys.argv[2]), int(sys.argv[3]))
    print >>sys.stderr, 'starting up on %s port %s' % server_address
    sock.bind(server_address)
    # Listen for incoming connections
    sock.listen(1)

    while True:
        print >>sys.stderr, 'waiting for a connection'
        connection, client_address = sock.accept()
    connection.close()

    #signal.signal(signal.SIGINT, signal_handler)
    #print('Press Ctrl+C')
    #signal.pause()

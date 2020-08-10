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

def signal_handler(signal, frame):
    print('You pressed Ctrl+C!')
    sys.exit(0)

if __name__ == "__main__":

    node_id = str(sys.argv[1])

    mdoURL = str(sys.argv[2])
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

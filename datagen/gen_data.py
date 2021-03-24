#!/usr/bin/env python3

import sys
import json
import time
import random
from kafka import KafkaProducer

from datetime import datetime

producer = KafkaProducer(bootstrap_servers=['kafka:9094'])

def send_msg(msg):
    msg = json.dumps(msg).encode('ascii')
    future = producer.send('topic-fake-traffic', msg)
    print('sent: {}'.format(msg))
    future.get(timeout=3)

def gen_random_datetime(daynum):
    return datetime(2021, 3, daynum, random.randint(0, 23), random.randint(1, 59), random.randint(1, 59)).strftime("%Y-%m-%d %H:%M:%S")

def send_many(daynum):
    count = 1000
    times = [ gen_random_datetime(daynum) for _ in range(count) ]
    times.sort()
    for i in range(count):
        msg = {
                "number": str(i),
                "ts": times[i],
                "uid": str(random.randint(10000, 10010)),
                "src_h": '192.168.0.{}'.format(random.randint(1,255)),
                "src_p": str(random.randint(1,10000)),
                "dst_h": '192.168.0.{}'.format(random.randint(1,255)),
                "dst_p": str(random.randint(1,10000)),
                "proto": random.choice(['tcp', 'udp'])
            }
        send_msg(msg)
 
if __name__ == '__main__':
    daynum = sys.argv[-1]
    try:
        daynum = int(daynum)
    except:
        daynum = 1
    send_many(daynum)






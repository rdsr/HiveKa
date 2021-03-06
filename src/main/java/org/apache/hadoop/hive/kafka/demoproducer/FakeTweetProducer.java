/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.hadoop.hive.kafka.demoproducer;

import HiveKa.avro.Tweet;
import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.EncoderFactory;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Properties;

public class FakeTweetProducer {


  public static void main(String[] args) {
    Properties props = new Properties();
    String[] names = new String[] {"gwenshap","hkszehon","singhasdev"};


    String topic = args[0];
    int iters = Integer.parseInt(args[1]);
    props.put("metadata.broker.list", args[2]);
    props.put("request.required.acks", "-1");
    props.put("serializer.class", "kafka.serializer.DefaultEncoder");
    props.put("key.serializer.class", "kafka.serializer.StringEncoder");
    props.put("producer.type", "sync");

    BaseProducer demo = new BaseProducer(props);

    Tweet tweet = new Tweet();
    SentGen sent = new SentGen();

    for (int i = 1; i < iters; i++) {
      tweet.setUsername(names[i%names.length]);
      tweet.setTimestamp(System.currentTimeMillis() / 1000);
      tweet.setText(sent.nextSent());
      demo.publish(tweet, topic, tweet.getSchema());
    }
    demo.close();
  }
}

package org.mashaz

import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment

import java.util.Properties

object KafkaSourceDemo {
  private val TRANSACTION_GROUP = "org.mashaz.flink_test"

  def main(args: Array[String]): Unit = {
    if (args.length != 2) {
      System.err.println("USAGE:\nKafkaSourceDemo <kafka_broker> <kafka_topic>")
      return
    }
    val kafkaBroker = args(0)
    val kafkaTopic = args(1)

    val props = new Properties()
    props.setProperty("bootstrap.servers", kafkaBroker)
    props.setProperty("group.id", TRANSACTION_GROUP)

    val env = StreamExecutionEnvironment.getExecutionEnvironment

    val myConsumer = new FlinkKafkaConsumer[String](kafkaTopic, new SimpleStringSchema(), props)
    myConsumer.setStartFromEarliest()          // 从最新的记录开始
    //  myConsumer.setStartFromTimestamp(...)  // 从指定的时间开始（毫秒）
    //  myConsumer.setStartFromGroupOffsets()  // 默认的方法
    val stream = env.addSource(myConsumer)

    stream.print()
    env.execute("KafkaSourceDemo")
  }
}

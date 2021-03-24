package org.mashaz

import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment

object SocketTextStreamWordCount {

  def main(args: Array[String]): Unit = {
    if (args.length != 2) {
      System.err.println("USAGE:\nSocketTextStreamWordCount <hostname> <port>")
      return
    }

    val hostName = args(0)
    val port = args(1).toInt
    val env = StreamExecutionEnvironment.getExecutionEnvironment

    //Create streams for names and ages by mapping the inputs to the corresponding objects
    val text = env.socketTextStream(hostName, port)
    val counts: DataStream[(String, Int)] = text
      // split up the lines in pairs (2-tuples) containing: (word,1)
      .flatMap(_.toLowerCase.split("\\W+"))
      .filter(_.nonEmpty)
      .map((_, 1))
      // group by the tuple field "0" and sum up tuple field "1"
      .keyBy(_._1)
      .sum(1)
    counts.print()

    env.execute("Scala_SocketTextStreamWordCount_Example")
  }

}
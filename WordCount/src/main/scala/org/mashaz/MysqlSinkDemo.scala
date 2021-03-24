package org.mashaz

/*
1. create db
```
create database flink_sink default character set utf8mb4 collate utf8mb4_unicode_ci;
```

2. create table

```
use flink_sink;

DROP TABLE IF EXISTS `test`;
CREATE TABLE `test` (
  `word` text CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `count` int
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

SET FOREIGN_KEY_CHECKS = 1;
```
*/

import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment, createTypeInformation}

object MysqlSinkDemo {
  private val mysql_host = "jdbc:mysql://127.0.0.1:3306/flink_sink"
  private val mysql_user = "root"
  private val mysql_pass = "123456"
  def main(args: Array[String]): Unit = {
    if (args.length != 2) {
      System.err.println("USAGE:\nMysqlSinkDemo <hostname> <port>")
      return
    }
    val hostName = args(0)
    val port = args(1).toInt

    // new sink
    val sink = new MysqlSink(mysql_host, mysql_user, mysql_pass)
    val env = StreamExecutionEnvironment.getExecutionEnvironment

    //Create streams for names and ages by mapping the inputs to the corresponding objects
    val text = env.socketTextStream(hostName, port)
    // text.addSink(sink)

    val counts: DataStream[(String, Int)] = text
      // split up the lines in pairs (2-tuples) containing: (word,1)
      .flatMap(_.toLowerCase.split("\\W+"))
      .filter(_.nonEmpty)
      .map((_, 1))
      // group by the tuple field "0" and sum up tuple field "1"
      .keyBy(_._1)
      .sum(1)


    counts.map(data => WordCountClass(data._1, data._2).toString()).addSink(sink)
    // counts.print()

    // val myOutput: Iterator[(String, Int)] = DataStreamUtils.collect(counts.javaStream).asScala

    env.execute()
  }
}

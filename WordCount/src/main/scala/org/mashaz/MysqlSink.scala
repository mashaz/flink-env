package org.mashaz

import java.sql.{Connection, DriverManager}
import com.google.gson.Gson
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.functions.sink.{RichSinkFunction, SinkFunction}
import org.apache.flink.streaming.api.scala.DataStream

class MysqlSink(url: String, user: String, pwd: String) extends RichSinkFunction[String] {

  var conn: Connection = _

  override def open(parameters: Configuration): Unit = {
    super.open(parameters)
    Class.forName("com.mysql.jdbc.Driver")
    conn = DriverManager.getConnection(url, user, pwd)
    conn.setAutoCommit(false)
  }
  override def invoke(value: String, context: SinkFunction.Context[_]): Unit = {
     val g = new Gson()
     val s = g.fromJson(value, classOf[WordCountClass])
    println(s)
    val p = conn.prepareStatement("insert into test(word,count) values(?,?)")
    p.setString(1, s.word)
    p.setInt(2, s.count)
    p.execute()
    conn.commit()
  }
  override def close(): Unit = {
    super.close()
    conn.close()
  }
}
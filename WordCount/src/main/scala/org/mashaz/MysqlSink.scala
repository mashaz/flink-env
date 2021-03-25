package org.mashaz

import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction

import org.apache.flink.streaming.api.functions.sink.SinkFunction

import com.google.gson.Gson

import java.sql.{Connection, DriverManager}

class MysqlSink(url: String, user: String, pwd: String) extends RichSinkFunction[String] {

  var conn: Connection = _
  override def open(parameters: Configuration): Unit = {
    super.open(parameters)
    Class.forName("com.mysql.jdbc.Driver")
    conn = DriverManager.getConnection(url, user, pwd)
    conn.setAutoCommit(false)
  }

  override def invoke(value: String, context: SinkFunction.Context): Unit = {
    println(value)
    println("==================================")
    val g = new Gson()
    val s = g.fromJson(value, classOf[WordCountCaseClass])

    val updateStmt = conn.prepareStatement("update test set count = ? where word = ?")
    updateStmt.setInt(1, s.count)
    updateStmt.setString(2, s.word)
    updateStmt.execute()
    if ( updateStmt.getUpdateCount == 0 ) {
      println("execute insert sql")
      val p = conn.prepareStatement("insert into test(word,count) values(?,?)")
      p.setString(1, s.word)
      p.setInt(2, s.count)
      p.execute()
    }
    conn.commit()
  }

  override def close(): Unit = {
    super.close()
    conn.close()
  }
}

case class WordCountCaseClass(word: String, count: Int)
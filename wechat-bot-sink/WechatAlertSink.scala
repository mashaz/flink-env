package org.mashaz.wechat

import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.functions.sink.{RichSinkFunction, SinkFunction}

class WechatAlertSink(webhook: String) extends RichSinkFunction[String] {
  var webhookDefault = ""
  override def open(parameters: Configuration): Unit = {
    super.open(parameters)
    webhookDefault = webhook
  }

  override def invoke(value: String, context: SinkFunction.Context): Unit = {
    WechatClient.sendWechatTextMsg(value, webhook)
  }

  override def close(): Unit = {
    super.close()
  }
}

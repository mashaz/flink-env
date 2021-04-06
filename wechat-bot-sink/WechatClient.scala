package org.mashaz.wechat

import org.apache.http.client.methods.HttpPost
import org.apache.http.impl.client.HttpClients
import com.google.gson.Gson
import com.google.gson.JsonParser
import org.apache.http.entity.StringEntity
import org.apache.http.util.EntityUtils

object WechatClient {
  def main(args: Array[String]): Unit = {
    val testWebhook = "https://qyapi.weixin.qq.com/cgi-bin/webhook/send?key=k"
    sendWechatTextMsg("foo\nbar", testWebhook)
  }

  def sendWechatTextMsg(text: String, webhook: String) = {
    val msg = Message("text", MsgText(text))
    val client = HttpClients.createDefault()
    val stringMsg = new Gson().toJson(msg)
    val post = new HttpPost(webhook)
    post.setEntity(new StringEntity(stringMsg, "utf-8"))
    post.setHeader("Content-Type", "application/json")
    val resp = client.execute(post)
    var sent = false
    if (resp.getStatusLine.getStatusCode == 200) {
      val ret = EntityUtils.toString(resp.getEntity)
      val t = JsonParser.parseString(ret).getAsJsonObject
      if (t.get("errcode").getAsInt.equals(0)) {
        sent = true
      }
    }
    println("sent", sent)
    resp.close()
  }
}

case class MsgText(content: String)
case class Message(msgtype: String, text: MsgText)

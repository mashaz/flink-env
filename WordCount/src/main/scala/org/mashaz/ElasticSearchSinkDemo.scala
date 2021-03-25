package org.mashaz


import org.apache.flink.api.common.functions.RuntimeContext
import org.apache.flink.streaming.connectors.elasticsearch.{ElasticsearchSinkFunction, RequestIndexer}
import org.apache.flink.streaming.connectors.elasticsearch7.ElasticsearchSink
import org.apache.http.HttpHost
import org.elasticsearch.action.index.IndexRequest
import org.elasticsearch.client.Requests

object ElasticSearchSinkDemo {
  def main(args: Array[String]): Unit = {
    val httpHosts = new java.util.ArrayList[HttpHost]
    httpHosts.add(new HttpHost("10.23.32.50", 9200, "http"))

    val esSinkBuilder = new ElasticsearchSink.Builder[String](
      httpHosts,
      new ElasticsearchSinkFunction[String] {
        def process(element: String, ctx: RuntimeContext, indexer: RequestIndexer) {
          val json = new java.util.HashMap[String, String]
          json.put("data", element)

          val rqst: IndexRequest = Requests.indexRequest
            .index("my-index")
            .source(json)

          indexer.add(rqst)
        }
      }
    )
  }
}

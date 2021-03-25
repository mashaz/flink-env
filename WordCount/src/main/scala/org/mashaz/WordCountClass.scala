package org.mashaz

case class WordCountClass(var word: String, var count: Int){
  override def toString: String = "{" + "word:" + "\""+ word+ "\"" + ",count:" + count + '}'
}
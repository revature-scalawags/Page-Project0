package KeplerDataReader

import java.io.{BufferedWriter, File, FileWriter}
import scala.jdk.CollectionConverters._
import scala.collection.mutable.{ArrayBuffer, StringBuilder}
import scala.io.BufferedSource

case class CSVHandler() {

  def getAllPlanets(data: BufferedSource): List[String] = {
    val planets = ArrayBuffer[String]()
    var count = 0

    //drop header, add rows
    for (line <- data.getLines.drop(46)) {
      planets += line
      count = count + 1
    }
    planets.toList
  }

  def buildNewCSVTable(rows: List[String], columnNums: Array[Int]): List[String] = {
    val table = ArrayBuffer[String]()
    for (row <- rows) {
      val sb = new StringBuilder()
      val cols = row.split(",").map(_.trim())

      sb.addAll(cols(0)).addOne(',').addAll(cols(1)).addOne(',')
      columnNums.map(elem => sb.addAll(cols(elem)).addOne(','))

      table.append(sb.toString.dropRight(1))
    }.asJava
    table.toList
  }

  def writeCSVFile(table: List[String]): Unit = {
    val file = new File("keplerSet.csv")
    val bw = new BufferedWriter(new FileWriter(file))
    for (row <- table) {
      bw.write(row)
      bw.newLine()
    }
    bw.close()
  }
}
package com.acervera.spatialnetworklabs.osm

import java.io.{InputStream, FileInputStream, BufferedInputStream}


object AnalysisOSM extends App {

  import org.clapper.argot.{ArgotConverters, ArgotParser}
  import org.clapper.argot.ArgotConverters._

  // Command line options.
  val commandLineParser = new ArgotParser("AnalisysOSM", preUsage = Some("Tool used to analisys of the pbf file."))
  val pbfFilePath = commandLineParser.option[String](List("f", "file"), "file path", "pbf file path")
  commandLineParser.parse(args)

  println("Reading: " + pbfFilePath.value.get)
  startAnalysis(pbfFilePath.value.get)

  /**
   * Accumulator to count the number of blocks read.
   */
  var blocksCounter: Long = 0L;

  /**
   * Accumulator to count the number of bytes read.
   */
  var bytesCounter: Long = 0L;

  def countBlocks(rawBlock: OsmPbfBlock): Unit = {
    blocksCounter += 1
    bytesCounter += rawBlock.raw.length
  }

  def startAnalysis(filePath: String): Unit = {

    var pbfIS: InputStream = null
    try {
      pbfIS = new BufferedInputStream(new FileInputStream(filePath))
      PbfRawIterator(pbfIS, countBlocks)
    } finally {
      if (pbfIS != null) pbfIS.close()
    }

    println("Num. of blocks : " + blocksCounter)
    println("Total bytes : " + bytesCounter)
    println("Avg. bytes per block : " + (bytesCounter / blocksCounter))

  }

}

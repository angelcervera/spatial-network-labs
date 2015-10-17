package com.acervera.spatialnetworklabs.osm

import java.io._

import org.apache.commons.codec.digest.DigestUtils
import org.scalatest.{WordSpec, FunSpec}


class PbfRawIteratorSpec extends WordSpec {

  /**
  var counter = 0L

  def writeBlocks(rawBlock: Array[Byte]): Unit = {
    var outFile : FileOutputStream = null
    var md5outFile : FileWriter = null
    try {
      outFile = new FileOutputStream("/home/angel/projects/spatial-network-labs/tmp/blocks/block_" + counter + ".osm.pbf")
      outFile.write(rawBlock)

      md5outFile = new FileWriter("/home/angel/projects/spatial-network-labs/tmp/blocks/block_" + counter + ".osm.pbf.md5")
      md5outFile.write(DigestUtils.md5Hex(rawBlock))
    } finally {
      if(outFile!=null) {
        outFile.flush()
        outFile.close()
      }
      if(md5outFile!=null) {
        md5outFile.flush()
        md5outFile.close()
      }
    }

    counter += 1
  }
  **/

  "The PbfRawIteratorSpec should" should {
    "Extract rigth raw data" in {

      val checksum = List("245015837825839004a4ebb5f2d0a7c9", "49693718ca400f7d19365b0faf04a8e8", "af6c0ac5b8f057aa8e0884b04fa5c739")
      var counter = 0
      def testBlocks(rawBlock: Array[Byte]): Unit = {
        assert(DigestUtils.md5Hex(rawBlock) == checksum(counter))
        counter += 1
      }

      var pbfIS: InputStream = null
      try {
        pbfIS = new BufferedInputStream(new FileInputStream("load-osm/src/test/resources/com/acervera/spatialnetworklabs/osm/three_blocks.osm.pbf"))
        PbfRawIterator(pbfIS, testBlocks)
      } finally {
        if (pbfIS != null) pbfIS.close()
      }
    }
  }

}

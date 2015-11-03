package com.acervera.spatialnetworklabs.osm

import java.io._
import org.apache.commons.codec.digest.DigestUtils
import org.openstreetmap.osmosis.pbf2.v0_6.impl.PbfFieldDecoder
import org.scalatest.{WordSpec, FunSpec}
import org.openstreetmap.osmosis.osmbinary.{Osmformat, Fileformat}


class OsmPbfBlockUtilsSpec extends WordSpec {


  //  "The PbfRawIteratorSpec should" should {
  //    "Extract rigth raw data" in {
  //      // val testFile = "/home/angel/workspaces/spatial/spatial-network-labs/tmp/ireland-and-northern-ireland-latest.osm.pbf"
  //      // val testFile = "/home/angel/projects/spatial-network-labs/tmp/spain-latest.osm.pbf"
  //      val testFile = "/home/angel/projects/spatial-network-labs/tmp/IE/IE"
  //      var counter = 0
  //      var pbfIS: InputStream = null
  //      try {
  //        pbfIS = new FileInputStream(testFile)
  //        PbfRawIterator(pbfIS, rawBlock => {
  //          val header = Fileformat.BlobHeader.parseFrom(rawBlock.header)
  //          val blob = Fileformat.Blob.parseFrom(rawBlock.blob)
  //
  //          if(blob.hasZlibData()) {
  //            println("-----------------------------------------")
  //            println("HEADER:")
  //            println("getType: " + header.getType)
  //            println("getDatasize: " + header.getDatasize + " - " + rawBlock.blob.length)
  //            println("getIndexdata: " + header.getIndexdata)
  //            println("getParserForType: " + header.getParserForType)
  //            println("getSerializedSize: " + header.getSerializedSize)
  //            println("getTypeBytes: " + header.getTypeBytes)
  //            println("BLOB:")
  //            println("getLzmaData: " + blob.getLzmaData)
  //            println("getDefaultInstanceForType: " + blob.getDefaultInstanceForType)
  //            println("getParserForType: " + blob.getParserForType)
  //            println("getRaw: " + blob.getRaw)
  //            println("getRawSize: " + blob.getRawSize)
  //            println("getZlibData: " + blob.getZlibData)
  //            println("hasLzmaData: " + blob.hasLzmaData)
  //            println("hasRaw: " + blob.hasRaw)
  //            println("hasRawSize: " + blob.hasRawSize)
  //            println("hasZlibData: " + blob.hasZlibData)
  //          }
  //
  //
  //
  //          val blockType = header.getType
  //          blockType match {
  //            case "OSMHeader" => {
  //              val headerBlock = Osmformat.HeaderBlock.parseFrom(rawBlock.blob)
  //              println("HEADER Block _______________ " )
  //              println(s"BBOX:  ${headerBlock.getBbox.getLeft},${headerBlock.getBbox.getTop},${headerBlock.getBbox.getRight},${headerBlock.getBbox.getBottom}" )
  ///*              headerBlock.getRequiredFeaturesList.forEach( (f:String) => {
  //                println(f)
  //              })*/
  //              println(headerBlock.getRequiredFeaturesList)
  //            }
  //            case "OSMData" => {
  //              // val headerBlock = Osmformat.PrimitiveBlock.parseFrom(rawBlock.raw)
  ////              println(s"BBOX:  ${headerBlock.getBbox.getLeft},${headerBlock.getBbox.getTop},${headerBlock.getBbox.getRight},${headerBlock.getBbox.getBottom}" )
  ////              headerBlock.getRequiredFeaturesList
  ////              println(headerBlock.getOsmosisReplicationTimestamp)
  //              //val primaryBlock = Osmformat.PrimitiveBlock.parseFrom(rawBlock.blob)
  //            }
  //
  //          }
  //
  //          counter += 1
  //        })
  //
  //        // assert(counter == 3, "There are 3 blocks!")
  //      } finally {
  //        if (pbfIS != null) pbfIS.close()
  //      }
  //    }


  def storeRaw(raw: Array[Byte], outPath: String): Unit = {
    var outFile: DataOutputStream = null
    try {
      outFile = new DataOutputStream(new FileOutputStream(outPath))
      outFile.write(raw)
    } finally {
      if (outFile != null) {
        outFile.flush()
        outFile.close()
      }
    }
  }

  /* Temporal code used to generate test data set. */
  "Extract right raw data" in {
    println("Running Extract right raw data")
    val osmPbfFile = "/home/angel/projects/spatial-network-labs/load-osm/src/test/resources/com/acervera/spatialnetworklabs/osm/three_blocks.osm.pbf"
    var counter = 0
    var pbfIS: InputStream = null
    try {
      pbfIS = new FileInputStream(osmPbfFile)
      PbfRawIterator(pbfIS, rawBlock => {
        storeRaw(rawBlock.raw, "/home/angel/projects/spatial-network-labs/tmp/blocks/block_" + counter + ".osm.pbf")
        counter += 1
      })

      println(">>>>>>>>>>>>>>>>>>>" + counter)
      //assert(counter == 3)
    } finally {
      if (pbfIS != null) pbfIS.close()


    }
  }

  /* Temporal code used to generate test data set. */
  "Extract different types of raw data" in {

    import scala.collection.JavaConversions._
    import scala.collection.JavaConverters._

    val MAX_DENSE_BLOCKS = 3;
    val MAX_NODES_BLOCKS = 3;
    val MAX_WAYS_BLOCKS = 3;
    val MAX_RELATIONS_BLOCKS = 3;

    println("Extract different types of raw data")

    val osmPbfFile = "/home/angel/projects/spatial-network-labs/tmp/ireland-and-northern-ireland-latest.osm.pbf"
    // val osmPbfFile = "/home/angel/projects/spatial-network-labs/tmp/spain-latest.osm.pbf"
    var counterNodes = 0
    var counterDense = 0
    var counterWays = 0
    var counterRelations = 0
    var pbfIS: InputStream = null
    try {
      pbfIS = new FileInputStream(osmPbfFile)
      PbfRawIterator(pbfIS, rawBlock => {

        if (rawBlock.blockType == "OSMData") {
          val primitiveBlock = OsmPbfPrimitives.fromRawBlob(rawBlock.blob)
          val y = primitiveBlock.getPrimitivegroupList.listIterator.foreach(primaryGroup => {
            if (primaryGroup.getDense().getIdCount > 0) {
              if (MAX_DENSE_BLOCKS > counterDense) {
                storeRaw(rawBlock.raw, "/home/angel/projects/spatial-network-labs/tmp/blocks/dense_block_" + counterDense + ".osm.pbf")
              }
              counterDense += 1
            }
            if (primaryGroup.getNodesCount > 0) {
              if (MAX_NODES_BLOCKS > counterNodes) {
                storeRaw(rawBlock.raw, "/home/angel/projects/spatial-network-labs/tmp/blocks/nodes_block_" + counterNodes + ".osm.pbf")
              }
              counterNodes += 1
            }
            if (primaryGroup.getWaysCount > 0) {
              if (MAX_WAYS_BLOCKS > counterWays) {
                storeRaw(rawBlock.raw, "/home/angel/projects/spatial-network-labs/tmp/blocks/ways_block_" + counterWays + ".osm.pbf")
              }
              counterWays += 1
            }
            if (primaryGroup.getRelationsCount > 0) {
              if (MAX_RELATIONS_BLOCKS > counterRelations) {
                storeRaw(rawBlock.raw, "/home/angel/projects/spatial-network-labs/tmp/blocks/relations_block_" + counterRelations + ".osm.pbf")
              }
              counterRelations += 1
            }
          })
        }




        //        var outFile : DataOutputStream = null
        //        try {
        //          outFile = new DataOutputStream( new FileOutputStream("/home/angel/projects/spatial-network-labs/tmp/blocks/block_"+counter+".osm.pbf") )
        //          outFile.write(rawBlock.raw)
        //        } finally {
        //          if(outFile!=null) {
        //            // outFile.write(1)
        //            outFile.flush()
        //            outFile.close()
        //          }
        //        }

      })

    } finally {
      if (pbfIS != null) pbfIS.close()
    }


    println("counterDense: " + counterDense)
    println("counterNodes: " + counterNodes)
    println("counterWays: " + counterWays)
    println("counterRelations: " + counterRelations)

  }

}

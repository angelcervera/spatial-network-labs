package com.acervera.spatialnetworklabs.osm

import java.util.Date
import java.util.zip.{DataFormatException, Inflater}

import org.openstreetmap.osmosis.core.OsmosisRuntimeException
import org.openstreetmap.osmosis.core.domain.v0_6._
import org.openstreetmap.osmosis.osmbinary.Osmformat.{DenseNodes, Info}
import org.openstreetmap.osmosis.osmbinary.{Osmformat, Fileformat}
import org.openstreetmap.osmosis.pbf2.v0_6.impl.PbfFieldDecoder
import scala.collection.JavaConversions._
import scala.collection.JavaConverters._


object OsmPbfPrimitives {

  val COORDINATE_SCALING_FACTOR: Double = 0.000000001
  val EMPTY_VERSION = -1
  val EMPTY_TIMESTAMP = new Date(0)
  val EMPTY_CHANGESET: Long = -1

  def fromRawBlob(rawBlob: Array[Byte]): Osmformat.PrimitiveBlock = {
    Osmformat.PrimitiveBlock parseFrom uncompressRawBlob(rawBlob)
  }

  def fromUncompressedRawBlob(rawBlob: Array[Byte]): Osmformat.PrimitiveBlock = {
    Osmformat.PrimitiveBlock parseFrom rawBlob
  }


  /**
   * Uncompress, if it is necessary, the raw blob data.
   *
   * @param rawBlob
   * @return
   */
  def uncompressRawBlob(rawBlob: Array[Byte]): Array[Byte] = {
    val blob = Fileformat.Blob.parseFrom(rawBlob)

    if (blob hasRaw) {
      blob getRaw() toByteArray
    } else if (blob hasZlibData) {
      val inflater = new Inflater()
      inflater.setInput(blob.getZlibData().toByteArray())
      val blobData = new Array[Byte](blob.getRawSize)
      try {
        inflater.inflate(blobData)
      } catch {
        case e: DataFormatException => throw new OsmosisRuntimeException("Unable to decompress PBF blob.", e)
      }
      if (!inflater.finished()) {
        throw new OsmosisRuntimeException("PBF blob contains incomplete compressed data.")
      }

      blobData

    } else {
      throw new OsmosisRuntimeException("PBF blob uses unsupported compression, only raw or zlib may be used.")
    }
  }

  private def buildTags(keys: List[Integer], values: List[Integer], fieldDecoder: PbfFieldDecoder): List[Tag] = {

    // Check coherence list of keys/values.
    if (keys.size != values.size)
      throw new OsmosisRuntimeException(s"Number of tag keys ($keys.size) and tag values ($values.size()) don't match")

    var tags: List[Tag] = Nil
    for (keyIdx <- keys.iterator; valueIdx <- values.iterator) {
      tags :+ new Tag(fieldDecoder.decodeString(keyIdx), fieldDecoder.decodeString(valueIdx))
    }

    tags
  }

  private def buildCommonEntityData(entityId: Long, keys: List[Integer], values: List[Integer], fieldDecoder: PbfFieldDecoder): CommonEntityData = {
    val entityData = new CommonEntityData(entityId, EMPTY_VERSION, EMPTY_TIMESTAMP, OsmUser.NONE, EMPTY_CHANGESET)
    entityData.getTags.addAll(buildTags(keys, values, fieldDecoder))
    entityData
  }

  private def buildCommonEntityData(entityId: Long, keys: List[Integer], values: List[Integer], info: Info, fieldDecoder: PbfFieldDecoder): CommonEntityData = {
    var user: OsmUser = null
    var entityData: CommonEntityData = null

    // Build the user, but only if one exists.
    if (info.hasUid() && info.getUid() >= 0 && info.hasUserSid()) {
      user = new OsmUser(info.getUid(), fieldDecoder.decodeString(info.getUserSid()))
    } else {
      user = OsmUser.NONE;
    }

    entityData = new CommonEntityData(entityId, info.getVersion(), fieldDecoder.decodeTimestamp(info.getTimestamp()), user, info.getChangeset())
    entityData.getTags.addAll(buildTags(keys, values, fieldDecoder))

    entityData
  }


  private def buildNode(node: Osmformat.Node, fieldDecoder: PbfFieldDecoder): Node = {
    var entityData: CommonEntityData = null
    if (node.hasInfo) {
      entityData = buildCommonEntityData(node.getId, node.getKeysList.asScala.toList, node.getValsList.asScala.toList, node.getInfo(), fieldDecoder)
    } else {
      entityData = buildCommonEntityData(node.getId, node.getKeysList.asScala.toList, node.getValsList.asScala.toList, fieldDecoder);
    }

    new org.openstreetmap.osmosis.core.domain.v0_6.Node(entityData, fieldDecoder.decodeLatitude(node.getLat()), fieldDecoder.decodeLatitude(node.getLon()));
  }

  /**
   * Extract Nodes from a DenseNodes and execute the visitNodeFn using every node extracted.
   *
   * @param denseNode
   * @param visitNodeFn
   */
  private def extractNodesFromDense(denseNode: DenseNodes, visitNodeFn: (Node) => Unit): Unit = {

    // extract information from the delta format.
    for {id <- denseNode.getIdList; lat <- denseNode.getLatList; lon <- denseNode.getLonList} {

    }
  }

  /**
   * Process original raw block (with the header/blob) data from the block.
   *
   * @param rawBlock Original (possible compressed)
   */
  def processPrimitives(
                         rawBlock: Array[Byte],
                         visitNodeFn: (Node) => Unit,
                         visitWayFn: (Way) => Unit,
                         visitRelationFn: (Relation) => Unit): Unit = {

    val block = OsmPbfBlock(rawBlock)
    if (block.blockType == "OSMData") {
      val primitiveBlock = fromRawBlob(block.blob)
      val pbfFieldDecoder = new PbfFieldDecoder(primitiveBlock)
      val y = primitiveBlock.getPrimitivegroupList.listIterator.foreach(primaryGroup => {

        // Iterate over all nodes.
        primaryGroup.getNodesList.asScala.toList.foreach(node => visitNodeFn(buildNode(node, pbfFieldDecoder)))

        // Iterate over all dense nodes.
        extractNodesFromDense(primaryGroup.getDense, visitNodeFn)

        // primaryGroup.getWaysList.listIterator.foreach( visitWayFn( buildWay( _ , pbfFieldDecoder) ) )
        // primaryGroup.getRelationsList.listIterator.foreach( visitRelationFn( buildRelation( _ , pbfFieldDecoder) ) )

      })
    } else {
      throw new OsmosisRuntimeException(s"Processiong OSMData, but block is for $block.blockType")
    }



    //    val primitiveBlock = fromRawBlob(block.blob)
    //    val pbfFieldDecoder = new PbfFieldDecoder(primitiveBlock)
    //    val y = primitiveBlock.getPrimitivegroupList.listIterator.foreach(primaryGroup => {
    //
    //      primaryGroup.getNodesList.asScala.toList.foreach( node =>  visitNodeFn( buildNode(node, pbfFieldDecoder ) ) )
    //
    //      // primaryGroup.getWaysList.listIterator.foreach( visitWayFn( buildWay( _ , pbfFieldDecoder) ) )
    //      // primaryGroup.getRelationsList.listIterator.foreach( visitRelationFn( buildRelation( _ , pbfFieldDecoder) ) )
    //
    //    })


  }

}



package processor
import fastparse.all._

class Parser(indent: Int) {
  val sequence: P[AST.sequence] = P(charSeqStart ~ elementSeq.rep ~ charSeqEnd)
  val mapping: P[AST.mapping] = P(charMapStart ~ elementMap.rep ~ charMapEnd)
  // TODO alternatives for indentation grammar
}

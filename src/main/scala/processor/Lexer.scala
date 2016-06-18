package processor
import fastparse.all._

object Lexer {

  // identifiers may not start with a digit
  val identifier: P[AST.identifier] = P((letter ~ character.rep)).!.map(AST.identifier)

  // val keywords = Set("cwlVersion", "inputs", "outputs", "class", "Workflow", "CommandLineTool")

  // start: indicators
  val charEntrySep = P(",")
  val charSeqStart = P("[")
  val charSeqEnd = P("]")
  val charMapStart = P("{")
  val charMapEnd = P("}")

  val charSeqEntry = P("-")
  val charMapKey = P("?")
  val charMapValue = P(":")

  val charComment = P("#")
  // end: indicators

  val comment = P(charComment ~ CharsWhile(_ != '\n', min = 0))

  // identifier characters
  val character = (letter | decDigit)
  val letter = P(CharIn('A' to 'Z', 'a' to 'z'))
  val decDigit = P(CharIn('0' to '9'))

  // whitespace
  val lf = P("\u000A")
  val cr = P("\u000D")
  val break = P(lf | cr)
  val space = P(" ")
  val ws = P((space | break).rep(1))

}

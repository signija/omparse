import fastparse.all._

object Lexical {
  val document = P(cwlDirective ~ content ~ End)
  val cwlDirective = P("cwlVersion: cwl:draft-3")
  val content = P(inputs ~ outputs ~ class)
  val class = P(steps | baseCommand)
  val baseCommand = P("baseCommand" ~ ws)

  // steps are essentially documents by themselves
  val steps = P("steps:" ~ ws ~ document.rep(1))

  // we ignore comments, so we don't capture any information
  val comment = P(charComment ~ (character | space).rep)

  val inputs = P(" ")
  val outputs = P(" ")

  // identifiers may not start with a digit
  val identifier = P((letter ~ character.rep).!)

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

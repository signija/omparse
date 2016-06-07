import fastparse.all._

object Main {

  val document = P(cwlDirective ~ content ~ End)
  val cwlDirective = P("cwlVersion: cwl:draft-3")
  val content = " "

  val printableChar = P(CharIn('\u0020' to '\u007E', '\u00A0' to '\uD7FF', '\uE000' to '\uFFFD'))

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

  val lf = P("\u000A")
  val cr = P("\u000D")
  val break = P(lf | cr)

  val decDigit = P(CharIn('0' to '9'))



  def main(args: Array[String]) : Unit = {
    println(document.parse("I'm not a valid string."))
    println(document.parse("cwlVersion: cwl:draft-3 "))
  }
}

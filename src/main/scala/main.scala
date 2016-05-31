//import fastparse.parsers.Combinators.Rule
import fastparse.all._

object Main {
  // Reminder @self: UTF-32 not currently included
  // Convention: charFoo represents a single special character. fooChar represents a range of characters.
  val printableChar = P(CharIn('\u0020' to '\u007E', '\u00A0' to '\uD7FF', '\uE000' to '\uFFFD')) // add 10000-10FFFF
  val nbJson = P("\u0009" | CharIn('\u0020' to '\uFFFD')) //add FFFD+ - 10FFFF -- can be deleted if not dealing with JSON
  val charBOM = P("\uFEFF") // byte order mark

  // start: indicators
  val charSeqEntry = P("-")
  val charMapKey = P("?")
  val charMapValue = P(":")
  // flow indicators
  val charEntrySep = P(",")
  val charSeqStart = P("[")
  val charSeqEnd = P("]")
  val charMapStart = P("{")
  val charMapEnd = P("}")
  val flowIndicators = List(charEntrySep, charSeqStart, charSeqEnd, charMapStart, charMapEnd)

  val charComment = P("#")
  val charAnchor = P("&")
  val charAlias = P("*")
  val charTag = P("!")
  val charLiteral = P("|")
  val charFolded = P(">")
  val charSQuote = P("\'")
  val charDQuote = P("\"")
  val charDirective = P("%")
  val charReserved = P("@" | "`")
  // end: indicators

  val lf = P("\u000A")
  val cr = P("\u000D")
  val break = P(lf | cr)

  val nbChar = P(printableChar.filter(x => (x != break) && (x != charBOM)))
  val space = P("\u0020")
  val tab = P("\u0009")
  val white = P(space | tab)
  val char = P(nbChar.filter(_ != white))
  val decDigit = P(CharIn('0' to '9'))
  val hexDigit = P(decDigit | CharIn('\u0041' to '\u0046', '\u0061' to '\u0066'))
  val asciiLetter = P(CharIn('\u0041' to '\u005A', '\u0061' to '\u007A'))
  val wordChar = P(decDigit | asciiLetter | "-")
  val uriChar = P("%" ~ hexDigit ~ hexDigit | wordChar | "#" | ";" | "/" | "?" | ":" | "@" | "&" | "=" | "+" | "$" | ","
                     | "_" | "." | "!" | "~" | "*" | "\'" | "(" | ")" | "[" | "]")
  val tagChar = P(uriChar.filter(x => (x != '!') && (flowIndicators.contains(x))))

  //TODO escaped characters
  //s-indent(n) = s-space x n
  val sepInline = P(white).rep // TODO rep+
  //TODO line prefixes, empty lines, line folding
  val commentText = P(charComment ~ nbChar.rep) //TODO rep*
  //val breakComment = P(break | EOF)
  val scalarComment = P((sepInline ~ commentText.?).? ~ break) // TODO check cuts
  //l-comment = s-separate-in-line c-nb-comment-text? b-comment
  //s-l-comments = (s-b-comment //start of line) l-comment*
  //TODO separation lines

  val directive = P(charDirective ~ (yamlDirective | tagDirective | reservedDirective)) // ~ s-l-comments)
  val reservedDirective = P(directiveName ~ (sepInline ~ directiveParameter).rep) // rep*
  val directiveName = P(char.rep) // rep+
  val directiveParameter = P(char.rep) //rep+
  val yamlDirective = P("YAML" ~ sepInline ~ yamlVersion) // 1 per document
  val yamlVersion = P(decDigit.rep ~ "." ~ decDigit.rep) //rep+
  val tagDirective = P("TAG" ~ ((sepInline ~ tagHandle) | (sepInline ~ tagPrefix)))
  val tagHandle = P(namedTagHandle | secondaryTagHandle | primaryTagHandle)
  val primaryTagHandle = P("!")
  val secondaryTagHandle = P("!!")
  val namedTagHandle = P("!" ~ wordChar.rep ~ "!") // rep+
  val tagPrefix = P(localTagPrefix | globalTagPrefix)
  val localTagPrefix = P("!" ~ uriChar.rep) // rep*
  val globalTagPrefix = P(charTag ~ uriChar.rep) // rep*

  def main(args: Array[String]): Unit = {
    import fastparse.all._
  }
}

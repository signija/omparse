import fastparse.parsers.Combinators.Rule

object Main {

  val c-printable = P(CharIn('\u0009', '\u000A', '\u000D', '\u0020' until '\u007E', '\u0085', '\u00A0' to '\uD7FF', '\uE000' to '\uFFFD')) // add 10000-10FFFF
  val nb-json = P('\u0009') // add 20-10FFFF
  val c-bom = P('\uFEFF') // byte order mark
  val c-seq-entry = P('-')
  val c-map-key = P('?')
  val c-map-value = P(':')
  val c-entry-sep = P(',')
  val c-seq-start = P('[')
  val c-seq-end = P(']')
  val c-map-start = P('{')
  val c-map-end = P('}')
  //val flow-indicators
  val c-comment = P('#')
  val c-anchor = P('&')
  val c-alias = P('*')
  val c-tag = P('!')
  val c-literal = P('|')
  val c-folded = P('>')
  val c-sq-quote = P('\'')
  val c-db-quote = P('\"')
  val c-directive = P('%')
  val c-reserved = P('@' | '`')
  //val indicators
  val b-lf = P('\u000A')
  val b-cr = P('\u000D')
  val b-char = P(b-lf | b-cr)
  //nb-char = c-printable - b-char - c-bom
  val s-space = P('\u0020')
  val s-tab = P('\u0009')
  val s-white = P(s-space | s-tab)
  //ns-char = nb-char - s-white
  val ns-dec-digit = P(CharIn(0 to 9))
  val ns-hex-digit = P(ns-dec-digit | CharIn('\u0041' to '\u0046', '\u0061' to '\u0066'))
  val ns-ascii-letter = P(CharIn('\u0041' to '\u005A', '\u0061' to '\u007A'))
  val ns-word-char = P(ns-dec-digit | ns-ascii-letter | '-')
  val ns-uri-char = P('%' ~ ns-hex-digit ~ ns-hex-digit | ns-word-char | '#' | ';' | '/' | '?' | ':' | '@' | '&' | '=' | '+' | '$' | ','
                     | '_' | '.' | '!' | '~' | '*' | '\'' | '(' | ')' | '[' | ']')
  //ns-tag-char = ns-uri-char - '!' - c-flow-indicators
  //TODO escaped characters
  //s-indent(n) = s-space x n
  //s-separate-in-line = s-white+
  //TODO line prefixes, empty lines, line folding
  //val c-nb-comment-text = P(c-comment ~ nb-char*)
  //b-comment = b-non-content
  //s-b-comment = (s-separate-in-line c-nb-comment-text?)? b-comment
  //l-comment = s-separate-in-line c-nb-comment-text? b-comment
  //s-l-comments = (s-b-comment //start of line) l-comment*
  //TODO separation lines
  val l-directive = P(l-directive ~ (ns-yaml-directive | ns-tag-directive | ns-reserved directive) ~ s-l-comments)
  val ns-reserved-directive = P(ns-directive-name ~ (s-separate-in-line ~ ns-directive-parameter).rep) // rep*
  val ns-directive-name = P(ns-char.rep) // rep+
  val ns-directive-parameter = P(ns-char.rep) //rep+
  val ns-yaml-directive = P("YAML" ~ s-separate-in-line ~ ns-yaml-version) // 1 per document
  val ns-yaml-version = P(ns-dec-digit.rep ~ "." ~ ns-dec-digit.rep) //rep+
  val ns-tag-directive = P("TAG" ~ ((s-separate-in-line ~ c-tag-handle) | (s-separate-in-line ~ ns-tag-prefix)))
  val c-tag-handle = P(c-named-tag-handle | c-secondary-tag-handle | c-primary-tag-handle)
  val c-primary-tag-handle = P("!")
  val c-secondary-tag-handle = P("!!")
  val c-named-tag-handle = P("!" ~ ns-word-char.rep ~ "!") // rep+
  val ns-tag-prefix = P(c-ns-local-tag-prefix | ns-global-tag-prefix)
  val c-ns-local-tag-prefix = P("!" ~ ns-uri-char.rep) // rep*
  val ns-global-tag-prefix = P(ns-tag-char ~ ns-uri-char.rep) // rep*

  def main(args: Array[String]): Unit = {
    import fastparse.all._
  }
}

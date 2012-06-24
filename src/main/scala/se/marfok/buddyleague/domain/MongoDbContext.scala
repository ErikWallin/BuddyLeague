package se.marfok.buddyleague.domain

import com.novus.salat._

package object when_necessary_context {

  implicit val ctx = new Context {
    val name = "When-Necessary-Context"

    override val typeHintStrategy = StringTypeHintStrategy(when = TypeHintFrequency.WhenNecessary, typeHint = TypeHint)
  }
}
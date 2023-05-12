import com.raquo.laminar.api.L.*
import org.scalajs.dom
import typings.mathjaxFull.jsAdaptorsBrowserAdaptorMod.browserAdaptor
import typings.mathjaxFull.jsCoreMathDocumentMod.MathDocument
import typings.mathjaxFull.jsHandlersHtmlMod.RegisterHTMLHandler
import typings.mathjaxFull.jsInputTexAllPackagesMod.AllPackages
import typings.mathjaxFull.jsInputTexConfigmacrosConfigMacrosConfigurationMod.ConfigMacrosConfiguration
import typings.mathjaxFull.jsInputTexMod.TeX
import typings.mathjaxFull.jsMathjaxMod.mathjax as MathJax
import typings.mathjaxFull.jsOutputChtmlMod.CHTML
import typings.mathjaxFull.jsUtilOptionsMod.OptionList

import scala.scalajs.js

object MathJax {
  val adaptor = browserAdaptor()
  RegisterHTMLHandler(adaptor)

  val inputJax = new TeX[dom.HTMLElement, dom.Text, dom.Document](
    new OptionList {
      val packages = AllPackages
      val inlineMath = js.Array(js.Array("$", "$"))
    }
  )
  val outputJax = new CHTML[dom.HTMLElement, dom.Text, dom.Document](
    new OptionList {
      val fontURL =
        "https://cdn.jsdelivr.net/npm/mathjax@3/es5/output/chtml/fonts/woff-v2"
    }
  )
  val docOptions = new OptionList {
    val InputJax = inputJax
    val OutputJax = outputJax

    val compileError =
      (math: MathDocument[dom.HTMLElement, dom.Text, dom.Document]) => {
        dom.console.log("Compile error")
        dom.console.log(math)
      }
    val typesetError =
      (math: MathDocument[dom.HTMLElement, dom.Text, dom.Document]) => {
        dom.console.log("Typeset error")
        dom.console.log(math)
      }
  }

  def apply(input: String) = {
    val mathDoc =
      MathJax
        .document(
          input,
          docOptions
        )
        .asInstanceOf[MathDocument[dom.HTMLElement, dom.Text, dom.Document]]

    mathDoc.render()

    val style = outputJax.styleSheet(mathDoc)
    val math = adaptor.root(mathDoc.document)

    div(
      cls := "overflow-x-hidden",
      foreignHtmlElement(style),
      foreignHtmlElement(math)
    )
  }

package io.github.lczx.journal

import play.api.libs.json.{JsValue, JsObject}

case class Issue(name: String, link: Option[String], text: String, meta: Issue.Metadata) {

  def html =
    s"""<section>
       |  <h1>$htmlCaption</h1>
       |  <details open>
       |    <summary>$htmlSummary</summary>
       |      <p>$text</p>
       |  </details>
       |</section>""".stripMargin

  private def htmlCaption =
    if (link.isDefined) s"""<a href="${link.get}">$name</a>""" else name

  private def htmlAuthor =
    if (meta.author.link.isDefined) s"""<a href="${meta.author.link.get}">${meta.author.name}</a>"""
    else meta.author.name

  private def htmlSummary =
    if (meta.milestone.isDefined) s"""Created by $htmlAuthor for ${meta.milestone.get}, ${meta.status}"""
    else s"""Created by $htmlAuthor, ${meta.status}"""

}

object Issue {

  case class Author(name: String, link: Option[String])
  case class Metadata(author: Author, status: String, milestone: Option[String])

  def fromJsObject(data: JsObject) = {
    val title = (data \ "title").as[String]
    val titleLink = (data \ "html_url").asOpt[String]
    val body = (data \ "body").as[String]

    Issue(title, titleLink, body, extractMetadata(data))
  }

  private def extractMetadata(data: JsObject) = {
    val status = (data \ "state").as[String]
    val milestone = (data \ "milestone" \ "title").asOpt[String]
    Metadata(extractAuthor(data \ "user"), status, milestone)
  }

  private def extractAuthor(data: JsValue) =
    Author((data \ "login").as[String], (data \ "html_url").asOpt[String])

}
package io.github.lczx.journal

import play.api.libs.json.{JsValue, JsObject}

class Issues(val issues: List[Issue]) {

  def html =
    s"""<!DOCTYPE html>
       |<html>
       |<head>
       |  <meta charset="utf-8">
       |  <link rel="stylesheet" href="user-stories_files/style.css">
       |</head>
       |<body>
       |  <ul>
       |    <li class="opt" id="open">Open all</li>
       |    <li class="opt" id="close">Close all</li>
       |  </ul>
       |  <script>
       |    document.querySelector('#open').addEventListener('click', function () {
       |        forAllIssues(function (e) { e.setAttribute('open', ''); });
       |    });
       |    document.querySelector('#close').addEventListener('click', function () {
       |        forAllIssues(function (e) { e.removeAttribute('open'); });
       |    });
       |    function forAllIssues(f) {
       |        [].forEach.call(document.querySelectorAll('details'), f);
       |    }
       |  </script>
       |$htmlBody
       |</body>""".stripMargin

  private def htmlBody = issues.map(_.html).mkString("\n")

}

object Issues {

  def fromJson(json: JsValue) =
    new Issues(json.as[List[JsObject]].map(Issue.fromJsObject))

}
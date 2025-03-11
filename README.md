# 2025年 新入社員研修 オープン講座 Java
## 開発演習 模範解答プロジェクト Tomcat10、JDK17対応版
## javaxからjakartaへ変更
### 1. javaxパッケージからjakartaパッケージへ変更
### 2. 旧JSTLからJakarta対応へ変更
1. アーカイブファイルは以下を使用
  - jakarta.servlet.jsp.jstl-3.0.1.jar
  - jakarta.servlet.jsp.jstl-api-3.0.2.jar
2. Taglibディレクティブのuri属性を以下に変更
<table border="1">
  <tr><th>Name</th><th>TagLib</th></tr>
  <tr><th>core</th><th> <%@ taglib prefix="c" uri="jakarta.tags.core" %> <tr>
  <tr><th>fmt</th><th>  <%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %> <tr>
  <tr><th>functions</th><th>  <%@ taglib prefix="fn" uri="jakarta.tags.functions" %> <tr>
</table>
2. ドキュメントURL
  - https://jakarta.ee/specifications/tags/3.0/tagdocs/

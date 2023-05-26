<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="constants.AttributeConst" %>

<c:if test="${errors != null}">
    <div id="flush_error">
        入力内容にエラーがあります。<br />
        <c:forEach var="error" items="${errors}">
            ・<c:out value="${error}" /><br />
        </c:forEach>
    </div>
</c:if>

<label>氏名:</label>
<input type="text" name="${AttributeConst.SEA_NAME.getValue()}" id="${AttributeConst.SEA_NAME.getValue()}" value="${search_name}" />
<br /><br />

<label>日付:</label>
<input type="date" name="${AttributeConst.SEA_DATE_FROM.getValue()}" id="${AttributeConst.SEA_DATE_FROM.getValue()}" value="<fmt:formatDate value='${search_date_from}' pattern='yyyy-MM-dd' />" />
<a> ～ </a>
<input type="date" name="${AttributeConst.SEA_DATE_TO.getValue()}" id="${AttributeConst.SEA_DATE_TO.getValue()}" value="<fmt:formatDate value='${search_date_to}' pattern='yyyy-MM-dd' />" />
<br /><br />

<%--
<label for="${AttributeConst.REP_TITLE.getValue()}">タイトル</label><br />
<input type="text" name="${AttributeConst.REP_TITLE.getValue()}" id="${AttributeConst.REP_TITLE.getValue()}" value="${report.title}" />
<br /><br />
--%>
<button type="submit">検索</button>

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


<%--<fmt:parseDate value="${report.reportDate}" pattern="yyyy-MM-dd" var="reportDay" type="date" />
<label for="${AttributeConst.REP_DATE.getValue()}">日付</label><br />
<input type="date" name="${AttributeConst.REP_DATE.getValue()}" id="${AttributeConst.REP_DATE.getValue()}" value="<fmt:formatDate value='${reportDay}' pattern='yyyy-MM-dd' />" />
<br /><br />
--%>
<label>氏名:</label>
<input type="text" name="${AttributeConst.SEARCH_NAME.getValue()}" id="${AttributeConst.SEARCH_NAME.getValue()}" value="${search_name}" />
<br /><br />
<%--
<label for="${AttributeConst.REP_TITLE.getValue()}">タイトル</label><br />
<input type="text" name="${AttributeConst.REP_TITLE.getValue()}" id="${AttributeConst.REP_TITLE.getValue()}" value="${report.title}" />
<br /><br />
--%>
<button type="submit">検索</button>

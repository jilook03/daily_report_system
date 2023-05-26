<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="constants.ForwardConst" %>

<c:set var="actRep" value="${ForwardConst.ACT_REP.getValue()}" />
<c:set var="commSea" value="${ForwardConst.CMD_SEARCH.getValue()}" />
<c:set var="commShow" value="${ForwardConst.CMD_SHOW.getValue()}" />
<c:set var="commShowUser" value="${ForwardConst.CMD_SHOW_USER.getValue()}" />

<c:import url="/WEB-INF/views/layout/app.jsp">
    <c:param name="content">
        <c:if test="${flush != null}">
            <div id="flush_error">
                <c:out value="${flush}"></c:out>
            </div>
        </c:if>

        <h2>日報検索</h2>

        <form method="POST" action="<c:url value='?action=${actRep}&command=${commSea}' />">
            <c:import url="_searchForm.jsp" />
        </form>
        <p></p>

        <c:if test="${reports != null}">
        <table id="report_list">
            <tbody>
                <tr>
                    <th class="report_name">氏名</th>
                    <th class="report_date">日付</th>
                    <th class="report_title">タイトル</th>
                    <th class="report_action">操作</th>
                </tr>
                <c:forEach var="report" items="${reports}" varStatus="status">
                    <fmt:parseDate value="${report.reportDate}" pattern="yyyy-MM-dd" var="reportDay" type="date" />

                    <tr class="row${status.count % 2}">
                        <td class="report_name"><a href="<c:url value='?action=${actRep}&command=${commShowUser}&id=${report.employee.id}' />"><c:out value="${report.employee.name}" /></a></td>
                        <td class="report_date"><fmt:formatDate value='${reportDay}' pattern='yyyy-MM-dd' /></td>
                        <td class="report_title">${report.title}</td>
                        <td class="report_action"><a href="<c:url value='?action=${actRep}&command=${commShow}&id=${report.id}' />">詳細を見る</a></td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
        <a class="rep_count">全 ${reports_count} 件</a><br />
        </c:if>
        <p>
            <a class="btn_link" href="<c:url value='?action=Report&command=index' />">一覧に戻る</a>
        </p>

    </c:param>
</c:import>
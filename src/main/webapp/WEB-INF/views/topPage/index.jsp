<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="constants.ForwardConst" %>

<c:set var="actTop" value="${ForwardConst.ACT_TOP.getValue()}" />
<c:set var="actEmp" value="${ForwardConst.ACT_EMP.getValue()}" />
<c:set var="actRep" value="${ForwardConst.ACT_REP.getValue()}" />

<c:set var="commShow" value="${ForwardConst.CMD_SHOW.getValue()}" />
<c:set var="commIdx" value="${ForwardConst.CMD_INDEX.getValue()}" />
<c:set var="commNew" value="${ForwardConst.CMD_NEW.getValue()}" />
<c:set var="commShowUser" value="${ForwardConst.CMD_SHOW_USER.getValue()}" />

<c:import url="../layout/app.jsp">
    <c:param name="content">
        <c:if test="${flush != null}">
            <div id="flush_success">
                <c:out value="${flush}"></c:out>
            </div>
        </c:if>
        <h2>日報管理システムへようこそ</h2>
        <p><a class="btn_link" href="<c:url value='?action=${actRep}&command=${commNew}' />">新規日報の登録</a></p>
        <h3 class="sub_title">自分の日報一覧</h3>
        <table id="report_list">
            <tbody>
                <tr>
                    <th class="report_date">日付</th>
                    <th class="report_title">タイトル</th>
                    <th class="report_action">操作</th>
                </tr>
                <c:forEach var="report" items="${reports}" varStatus="status">
                    <fmt:parseDate value="${report.reportDate}" pattern="yyyy-MM-dd" var="reportDay" type="date" />
                    <tr class="row${status.count % 2}">
                        <td class="report_date"><fmt:formatDate value='${reportDay}' pattern='yyyy-MM-dd' /></td>
                        <td class="report_title">${report.title}</td>
                        <td class="report_action"><a href="<c:url value='?action=${actRep}&command=${commShow}&id=${report.id}' />">詳細を見る</a></td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>

        <div id="pagination">
            <a class="rep_count">全 ${reports_count} 件</a><br />
            <c:forEach var="i" begin="1" end="${((reports_count - 1) / maxRow) + 1}" step="1">
                <c:choose>
                    <c:when test="${i == page}">
                        <a class="page_now"><c:out value="${i}" /></a>
                    </c:when>
                    <c:otherwise>
                        <c:set var="j" value="${follow_page}"></c:set>
                        <a class="page_link" href="<c:url value='?action=${actTop}&command=${commIdx}&page=${i}&follow_page=${j}' />"><c:out value="${i}" /></a>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </div>
        <p></p>

        <h3 class="sub_title">フォローしている社員の日報一覧</h3>
        <table id="report_list">
            <tbody>
                <tr>
                    <th class="report_name">氏名</th>
                    <th class="report_date">日付</th>
                    <th class="report_title">タイトル</th>
                    <th class="report_action">操作</th>
                </tr>
                <c:forEach var="report" items="${follow_reports}" varStatus="status">
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

        <div id="pagination">
            <a class="rep_count">全 ${follow_reports_count} 件</a><br />
            <c:forEach var="j" begin="1" end="${((follow_reports_count - 1) / maxRow) + 1}" step="1">
                <c:choose>
                    <c:when test="${j == follow_page}">
                        <a class="page_now"><c:out value="${j}" /></a>
                    </c:when>
                    <c:otherwise>
                        <c:set var="i" value="${page}"></c:set>
                        <a class="page_link" href="<c:url value='?action=${actTop}&command=${commIdx}&page=${i}&follow_page=${j}' />"><c:out value="${j}" /></a>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </div>

    </c:param>
</c:import>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="constants.ForwardConst" %>

<c:set var="actRep" value="${ForwardConst.ACT_REP.getValue()}" />
<c:set var="commIdx" value="${ForwardConst.CMD_INDEX.getValue()}" />
<c:set var="commShow" value="${ForwardConst.CMD_SHOW.getValue()}" />
<c:set var="commNew" value="${ForwardConst.CMD_NEW.getValue()}" />
<c:set var="commFol" value="${ForwardConst.CMD_FOLLOW.getValue()}" />
<c:set var="commUnFol" value="${ForwardConst.CMD_UNFOLLOW.getValue()}" />

<c:import url="/WEB-INF/views/layout/app.jsp">
    <c:param name="content">
        <c:if test="${flush != null}">
            <div id="flush_success">
                <c:out value="${flush}"></c:out>
            </div>
        </c:if>

        <div class="user_report_title">
            <div id="report_list_title"><c:out value="${employee.name}" /> さんの日報一覧</div>
            <div>
                <c:if test="${sessionScope.login_employee.id != employee.id}" >
                    <c:choose>
                        <c:when test="${is_follow == false}">
                            <a class="btn_fol" href="<c:url value='?action=${actRep}&command=${commFol}&id=${employee.id}' />" >フォローする</a>
                        </c:when>
                        <c:otherwise>
                            <a class="btn_unFol" href="<c:url value='?action=${actRep}&command=${commUnFol}&id=${employee.id}' />" >フォロー中</a>
                        </c:otherwise>
                    </c:choose>
                </c:if>
            </div>
        </div>

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
            （全 ${reports_count} 件）<br />
            <c:forEach var="i" begin="1" end="${((reports_count - 1) / maxRow) + 1}" step="1">
                <c:choose>
                    <c:when test="${i == page}">
                        <a class="page_now"><c:out value="${i}" /></a>
                    </c:when>
                    <c:otherwise>
                        <a class="page_link" href="<c:url value='?action=${actRep}&command=${commIdx}&page=${i}' />"><c:out value="${i}" /></a>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </div>
        <p>
            <a href="<c:url value='?action=${actRep}&command=${commIdx}' />">一覧に戻る</a>
        </p>
    </c:param>
</c:import>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page import="constants.ForwardConst" %>

<c:set var="actRep" value="${ForwardConst.ACT_REP.getValue()}" />
<c:set var="commIdx" value="${ForwardConst.CMD_INDEX.getValue()}" />
<c:set var="commEdt" value="${ForwardConst.CMD_EDIT.getValue()}" />
<c:set var="commLik" value="${ForwardConst.CMD_LIKE.getValue()}" />
<c:set var="commUnLik" value="${ForwardConst.CMD_UNLIKE.getValue()}" />

<c:import url="/WEB-INF/views/layout/app.jsp">
    <c:param name="content">

        <div class="user_report_title">
            <div id="report_list_title">
                日報 詳細ページ
            </div>
            <div>
                <c:choose>
                    <c:when test="${is_like == false}">
                        <a class="heart" href="<c:url value='?action=${actRep}&command=${commLik}&id=${report.id}' />" >♡</a>
                        <a class="like_count" ><c:out value="${like_count}"></c:out></a>
                    </c:when>
                    <c:otherwise>
                        <a class="heart_like" href="<c:url value='?action=${actRep}&command=${commUnLik}&id=${report.id}' />" >♥</a>
                        <a class="like_count" ><c:out value="${like_count}"></c:out></a>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>

        <table>
            <tbody>
                <tr>
                    <th>氏名</th>
                    <td><c:out value="${report.employee.name}" /></td>
                </tr>
                <tr>
                    <th>日付</th>
                    <fmt:parseDate value="${report.reportDate}" pattern="yyyy-MM-dd" var="reportDay" type="date" />
                    <td><fmt:formatDate value='${reportDay}' pattern='yyyy-MM-dd' /></td>
                </tr>
                <tr>
                    <th>内容</th>
                    <td><pre><c:out value="${report.content}" /></pre></td>
                </tr>
                <tr>
                    <th>出勤時刻</th>
                    <fmt:parseDate value="${report.begin}" pattern="HH:mm" var="begin" type="time" />
                    <td><fmt:formatDate value="${begin}" pattern="HH:mm" /></td>
                </tr>
                <tr>
                    <th>退勤時刻</th>
                    <fmt:parseDate value="${report.finish}" pattern="HH:mm" var="finish" type="time" />
                    <td><fmt:formatDate value="${finish}" pattern="HH:mm" /></td>
                </tr>
                <tr>
                    <th>登録日時</th>
                    <fmt:parseDate value="${report.createdAt}" pattern="yyyy-MM-dd'T'HH:mm:ss" var="createDay" type="date" />
                    <td><fmt:formatDate value="${createDay}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
                </tr>
                <tr>
                    <th>更新日時</th>
                    <fmt:parseDate value="${report.updatedAt}" pattern="yyyy-MM-dd'T'HH:mm:ss" var="updateDay" type="date" />
                    <td><fmt:formatDate value="${updateDay}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
                </tr>
            </tbody>
        </table>

        <c:if test="${sessionScope.login_employee.id == report.employee.id}">
            <p>
                <a class="btn_link" href="<c:url value='?action=${actRep}&command=${commEdt}&id=${report.id}' />">この日報を編集する</a>
            </p>
        </c:if>

        <p>
            <a class="btn_link" href="<c:url value='?action=${actRep}&command=${commIdx}' />">一覧に戻る</a>
        </p>
    </c:param>
</c:import>
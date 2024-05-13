<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://topjava.javawebinar.ru/functions" %>
<html>
<jsp:include page="fragments/headTag.jsp"/>
<body>
<script src="resources/js/topjava.common.js" defer></script>
<script src="resources/js/topjava.meals.js" defer></script>
<jsp:include page="fragments/bodyHeader.jsp"/>

<div class="jumbotron pt-4">
    <div class="container">
        <h3 class="text-center">Моя еда</h3>

        <div class="card border-dark">
            <div class="card-body pb-0">
                <form id="filter">
                    <div class="row">
                        <div class="col-2">
                            <label for="startDate">От даты (включая)</label>
                            <input type="date" class="form-control" name="startDate" id="startDate" autocomplete="off">
                        </div>
                        <div class="col-2">
                            <label for="endDate">До даты (включая)</label>
                            <input type="date" class="form-control" name="endDate" id="endDate" autocomplete="off">
                        </div>
                        <div class="offset-2 col-3">
                            <label for="startTime">От времени (включая)</label>
                            <input type="time" class="form-control" name="startTime" id="startTime" autocomplete="off">
                        </div>
                        <div class="col-3">
                            <label for="endTime">До времени (исключая)</label>
                            <input type="time" class="form-control" name="endTime" id="endTime" autocomplete="off">
                        </div>
                    </div>
                </form>
            </div>
            <div class="card-footer text-right">
                <button class="btn btn-danger" onclick="clearFilter()">
                    <span class="fa fa-remove"></span>
                    Отменить
                </button>
                <button class="btn btn-primary" onclick="filter()">
                    <span class="fa fa-filter"></span>
                    Отфильтровать
                </button>
            </div>
        </div>
        <br/>
        <button class="btn btn-primary" onclick="add()">
            <span class="fa fa-plus"></span>
            Добавить
        </button>
        <table class="table table-striped" id="datatable">
            <thead>
            <tr>
                <th>Дата/Время</th>
                <th>Описание</th>
                <th>Калории</th>
                <th></th>
                <th></th>
            </tr>
            </thead>
            <c:forEach items="${requestScope.meals}" var="meal">
                <jsp:useBean id="meal" type="ru.javawebinar.topjava.to.MealTo"/>
                <input type="hidden" id="excess" name="excess" value="${meal.excess}">
                <tr data-meal-excess="${meal.excess}" id="${meal.id}">
                    <td>
                            <%--${meal.dateTime.toLocalDate()} ${meal.dateTime.toLocalTime()}--%>
                            <%--<%=TimeUtil.toString(meal.getDateTime())%>--%>
                            <%--${fn:replace(meal.dateTime, 'T', ' ')}--%>
                            ${fn:formatDateTime(meal.dateTime)}
                    </td>
                    <td>${meal.description}</td>
                    <td>${meal.calories}</td>
                    <td><a href="meals/update?id=${meal.id}"><spring:message code="common.update"/></a></td>
                    <td><a class="delete"><span class="fa fa-remove"></span></a></td>
                </tr>
            </c:forEach>
        </table>
    </div>
</div>
<div class="modal fade" tabindex="-1" id="editRow">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title"><spring:message code="meal.add"/></h4>
                <button type="button" class="close" data-dismiss="modal" onclick="closeNoty()">&times;</button>
            </div>
            <div class="modal-body">
                <form id="detailsForm">
                    <div class="form-group">
                        <label for="dateTime" class="col-form-label">Дата/Время</label>
                        <input type="datetime-local" class="form-control" id="dateTime" name="dateTime">
                    </div>

                    <div class="form-group">
                        <label for="description" class="col-form-label">Описание</label>
                        <input type="text" class="form-control" id="description" name="description">
                    </div>

                    <div class="form-group">
                        <label for="calories" class="col-form-label">Калории</label>
                        <input type="number" class="form-control" id="calories" name="calories" placeholder="1000">
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal" onclick="closeNoty()">
                    <span class="fa fa-close"></span>
                    <spring:message code="common.cancel"/>
                </button>
                <button type="button" class="btn btn-primary" onclick="saveMeal()">
                    <span class="fa fa-check"></span>
                    <spring:message code="common.save"/>
                </button>
            </div>
        </div>
    </div>
</div>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
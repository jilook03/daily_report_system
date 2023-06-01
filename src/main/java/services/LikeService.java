package services;

import java.time.LocalDateTime;
import java.util.List;

import actions.views.EmployeeConverter;
import actions.views.EmployeeView;
import actions.views.ReportConverter;
import actions.views.ReportView;
import constants.JpaConst;
import models.Employee;
import models.Like;
import models.Report;

public class LikeService extends ServiceBase {

    public void like(EmployeeView ev, ReportView rv) {
        Like like = new Like();
        LocalDateTime ldt = LocalDateTime.now();
        like.setEmployee(EmployeeConverter.toModel(ev));
        like.setReport(ReportConverter.toModel(rv));
        like.setCreatedAt(ldt);
        createInternal(like);
    }

    public void unLike(EmployeeView ev, ReportView rv) {
        Employee emp = EmployeeConverter.toModel(ev);
        Report rep = ReportConverter.toModel(rv);
        delete(emp, rep);
    }

    @SuppressWarnings("unchecked")
    public boolean likeCheck(EmployeeView ev, ReportView rv) {
        boolean isLike = false;
        Employee emp = EmployeeConverter.toModel(ev);
        Report rep = ReportConverter.toModel(rv);
        List<Like> likes = em.createNamedQuery(JpaConst.Q_LIK_LIKECHECK)
                .setParameter(JpaConst.JPQL_PARM_EMPLOYEE, emp)
                .setParameter(JpaConst.JPQL_PARM_REPORT, rep)
                .getResultList();
        if (likes.size() != 0) {
            isLike = true;
        }
        return isLike;
    }

    public long likeCount(ReportView rv) {
        Report rep = ReportConverter.toModel(rv);
        long count = (long)em.createNamedQuery(JpaConst.Q_LIK_LIKECOUNT)
                .setParameter(JpaConst.JPQL_PARM_REPORT, rep)
                .getSingleResult();
        return count;
    }

    private void createInternal(Like like) {
        em.getTransaction().begin();
        em.persist(like);
        em.getTransaction().commit();
    }

    private void delete(Employee employee, Report report) {
        em.getTransaction().begin();
        em.createNamedQuery(JpaConst.Q_LIK_UNLIKE)
            .setParameter(JpaConst.JPQL_PARM_EMPLOYEE, employee)
            .setParameter(JpaConst.JPQL_PARM_REPORT, report)
            .executeUpdate();
        em.getTransaction().commit();
    }
}

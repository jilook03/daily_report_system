package services;

import java.time.LocalDateTime;
import java.util.List;

import constants.JpaConst;
import models.Like;

public class LikeService extends ServiceBase {

    public void like(int empId, int repId) {
        Like like = new Like();
        LocalDateTime ldt = LocalDateTime.now();
        like.setEmployeeId(empId);
        like.setReportId(repId);
        like.setCreatedAt(ldt);
        createInternal(like);
    }

    public void unLike(int empId, int repId) {
        delete(empId, repId);
    }

    @SuppressWarnings("unchecked")
    public boolean likeCheck(int empId, int repId) {
        boolean isLike = false;
        List<Like> likes = em.createNamedQuery(JpaConst.Q_LIK_LIKECHECK)
                .setParameter(JpaConst.JPQL_PARM_EMPLOYEE, empId)
                .setParameter(JpaConst.JPQL_PARM_REPORT, repId)
                .getResultList();
        if (likes.size() != 0) {
            isLike = true;
        }
        return isLike;
    }

    public long likeCount(int repId) {
        long count = (long)em.createNamedQuery(JpaConst.Q_LIK_LIKECOUNT)
                .setParameter(JpaConst.JPQL_PARM_REPORT, repId)
                .getSingleResult();
        return count;
    }

    private void createInternal(Like like) {
        em.getTransaction().begin();
        em.persist(like);
        em.getTransaction().commit();
    }

    private void delete(int empId, int repId) {
        em.getTransaction().begin();
        em.createNamedQuery(JpaConst.Q_LIK_UNLIKE)
            .setParameter(JpaConst.JPQL_PARM_EMPLOYEE, empId)
            .setParameter(JpaConst.JPQL_PARM_REPORT, repId)
            .executeUpdate();
        em.getTransaction().commit();
    }
}

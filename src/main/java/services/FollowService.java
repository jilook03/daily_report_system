package services;

import java.time.LocalDateTime;
import java.util.List;

import actions.views.FollowConverter;
import actions.views.FollowView;
import constants.JpaConst;
import models.Follow;

public class FollowService extends ServiceBase {

    public void follow(FollowView fv) {
        LocalDateTime ldt = LocalDateTime.now();
        fv.setCreatedAt(ldt);
        fv.setUpdatedAt(ldt);
        createInternal(fv);
    }

    public void unFollow(int followFromId, int followToId) {
        delete(followFromId, followToId);
    }

    public List<Integer> getFollowList(int followFromId) {
        List<Integer> followList = em.createNamedQuery(JpaConst.Q_FOL_GET_FOL, Integer.class)
                .setParameter(JpaConst.JPQL_PARM_FOLLOWFROM, followFromId)
                .getResultList();
    return followList;
    }

    public boolean followCheck(int followFromId, int followToId) {
        boolean isFollow = false;
        List<Follow> fc = em.createNamedQuery(JpaConst.Q_FOL_CHECK_FOLLOW, Follow.class)
                .setParameter(JpaConst.JPQL_PARM_FOLLOWFROM, followFromId)
                .setParameter(JpaConst.JPQL_PARM_FOLLOWTO, followToId)
                .getResultList();
        if (fc.size() > 0) {
            isFollow = true;
        }
    return isFollow;
    }

    //idを条件に1件FVを取得
    public FollowView findOne(int followFromId) {
        return FollowConverter.toView(findOneInternal(followFromId));
    }

    //idを条件に１件レコードを取得
    private Follow findOneInternal(int followFromId) {
        return em.find(Follow.class, followFromId);
    }

    private void createInternal(FollowView fv) {
        em.getTransaction().begin();
        em.persist(FollowConverter.toModel(fv));
        em.getTransaction().commit();
    }

    private void delete(int followFromId, int followToId) {
        em.getTransaction().begin();
        em.createNamedQuery(JpaConst.Q_FOL_UNFOLLOW)
            .setParameter(JpaConst.JPQL_PARM_FOLLOWFROM, followFromId)
            .setParameter(JpaConst.JPQL_PARM_FOLLOWTO, followToId)
            .executeUpdate();
        em.getTransaction().commit();
    }


}
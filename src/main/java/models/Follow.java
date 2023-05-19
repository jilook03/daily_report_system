package models;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import constants.JpaConst;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import models.Follow.PK;

@Table(name = JpaConst.TABLE_FOL)
@NamedQueries({
    @NamedQuery(
            name = JpaConst.Q_FOL_CHECK_FOLLOW,
            query = JpaConst.Q_FOL_CHECK_FOLLOW_DEF),
    @NamedQuery(
            name = JpaConst.Q_FOL_UNFOLLOW,
            query = JpaConst.Q_FOL_UNFOLLOW_DEF),
    @NamedQuery(
            name = JpaConst.Q_FOL_GET_FOL,
            query = JpaConst.Q_FOL_GET_FOL_DEF)
})

@IdClass(value=PK.class)
@Getter //全てのクラスフィールドについてgetterを自動生成する(Lombok)
@Setter //全てのクラスフィールドについてsetterを自動生成する(Lombok)
@NoArgsConstructor //引数なしコンストラクタを自動生成する(Lombok)
@AllArgsConstructor //全てのクラスフィールドを引数にもつ引数ありコンストラクタを自動生成する(Lombok)
@Entity
public class Follow implements Serializable {

    @Id
    @Column(name = JpaConst.FOL_COL_FROM_ID, nullable = false)
    private Integer followFromId;

    @Id
    @Column(name = JpaConst.FOL_COL_TO_ID, nullable = false)
    private Integer followToId;

    @Column(name = JpaConst.FOL_COL_CREATED_AT, nullable = false)
    private LocalDateTime createdAt;

    @Column(name = JpaConst.FOL_COL_UPDATED_AT, nullable = false)
    private LocalDateTime updatedAt;

    @Data
    public static class PK implements Serializable {
        private Integer followFromId;
        private Integer followToId;
    }
}

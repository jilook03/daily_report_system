package models;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import constants.JpaConst;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = JpaConst.TABLE_LIK)
@NamedQueries({
    @NamedQuery(
            name = JpaConst.Q_LIK_UNLIKE,
            query = JpaConst.Q_LIK_UNLIKE_DEF),
    @NamedQuery(
            name = JpaConst.Q_LIK_LIKECHECK,
            query = JpaConst.Q_LIK_LIKECHECK_DEF),
    @NamedQuery(
            name = JpaConst.Q_LIK_LIKECOUNT,
            query = JpaConst.Q_LIK_LIKECOUNT_DEF)
})

@Getter //全てのクラスフィールドについてgetterを自動生成する(Lombok)
@Setter //全てのクラスフィールドについてsetterを自動生成する(Lombok)
@NoArgsConstructor //引数なしコンストラクタを自動生成する(Lombok)
@AllArgsConstructor //全てのクラスフィールドを引数にもつ引数ありコンストラクタを自動生成する(Lombok)
@Entity
public class Like {

    @Id
    @Column(name = JpaConst.LIK_COL_ID)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = JpaConst.LIK_COL_EMP, nullable = false)
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = JpaConst.LIK_COL_REP, nullable = false)
    private Report report;

    @Column(name = JpaConst.LIK_COL_CREATED_AT, nullable = false)
    private LocalDateTime createdAt;

}

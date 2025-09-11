package sn.malcolm.demo.model;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import sn.malcolm.demo.view.View;

import java.util.Date;

@Setter
@Getter
@MappedSuperclass
@FilterDef(name = "deletedFilter", parameters = @ParamDef(name = "notDeleted", type = Integer.class))
@Filter(name = "deletedFilter", condition = "((status is null and :notDeleted = 1 ) or status = :notDeleted)")
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @JsonView({View.Public.class})
    protected Integer id;

    @JsonView({})
    @Column(name = "status", columnDefinition = "integer default 1", nullable = false)
    protected Integer status = 1;

    @JsonView({View.Public.class})
    @CreatedDate
    @Column(name = "createdDate")
    protected Date createdDate;

    @JsonView({View.ReadOnly.class})
    @LastModifiedDate
    @Column(name = "updatedDate")
    protected Date updatedDate;

    @JsonView({})
    @Column(name = "deletedDate")
    protected Date deletedDate;

    @JsonView({})
    @CreatedBy
    @Column(name = "createdBy")
    private String createdBy;

    @JsonView({})
    @LastModifiedBy
    @Column(name = "updatedBy")
    private String updatedBy;

    @JsonView({})
    @Column(name = "deletedBy")
    private String deletedBy;

    @JsonView({})
    public String getObjectDescription() {
        return this.getClass().getSimpleName();
    }


    @PrePersist
    public void prePersist() {
        this.updatedDate = null;
        this.updatedBy = null;
    }
}

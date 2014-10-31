package biz.suckow.fuel.business.app.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@MappedSuperclass
public class BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date ctime;

    @Temporal(TemporalType.TIMESTAMP)
    private Date utime;

    @PrePersist
    private void addCtime() {
	this.ctime = new Date();
    }

    @PreUpdate
    private void updateUtime() {
	this.utime = new Date();
    }

    public Date getCTime() {
	return ctime;
    }

    public Date getUtime() {
	return utime;
    }

}

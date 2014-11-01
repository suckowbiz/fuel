package biz.suckow.fuel.business.owner.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

import biz.suckow.fuel.business.app.entity.BaseEntity;
import biz.suckow.fuel.business.vehicle.entity.Vehicle;

import com.google.common.collect.Lists;

@Entity
@NamedQuery(name = Owner.QueryByOwnerameCaseIgnore.NAME, query = "SELECT o FROM Owner o "
	+ "WHERE LOWER(o.ownername) = LOWER(:"
	+ Owner.QueryByOwnerameCaseIgnore.PARAM_NAME + ")")
public class Owner extends BaseEntity {
    private static final long serialVersionUID = -2640121939957877859L;

    public static final class QueryByOwnerameCaseIgnore {
	public static final String NAME = "Owner.byOwnername";
	public static final String PARAM_NAME = "ownername";
    }

    @OneToMany(mappedBy = "owner")
    private List<Vehicle> vehicles;

    @Column(unique = true, nullable = false)
    private String ownername;

    public Owner() {
	this.vehicles = Lists.newArrayList();
    }

    public String getOwnername() {
	return ownername;
    }

    public void setOwnername(String ownername) {
	this.ownername = ownername;
    }

    public List<Vehicle> getVehicles() {
	return vehicles;
    }

}

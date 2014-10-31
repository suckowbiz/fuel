package biz.suckow.fuel.business.user.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

import biz.suckow.fuel.business.app.entity.BaseEntity;
import biz.suckow.fuel.business.vehicle.entity.Vehicle;

import com.google.common.collect.Lists;
import javax.persistence.Table;

@Entity
@Table(name = "\"User\"")
@NamedQuery(name = User.QueryByUsernameCaseIgnore.NAME, query = "FROM User u "
	+ "WHERE LOWER(u.username) = LOWER(:"
	+ User.QueryByUsernameCaseIgnore.PARAM_NAME + ")")
public class User extends BaseEntity {
    public static final class QueryByUsernameCaseIgnore {
	public static final String NAME = "User.byUsername";
	public static final String PARAM_NAME = "username";
    }

    @OneToMany
    private final List<Vehicle> vehicles;

    @Column(unique = true, nullable = false)
    private String username;

    public User() {
        this.vehicles = Lists.newArrayList();
    }

    public String getUsername() {
	return username;
    }

    public void setUsername(String username) {
	this.username = username;
    }

    public List<Vehicle> getVehicles() {
	return vehicles;
    }

}

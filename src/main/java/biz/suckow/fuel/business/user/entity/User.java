package biz.suckow.fuel.business.user.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

import biz.suckow.fuel.business.app.entity.BaseEntity;
import biz.suckow.fuel.business.vehicle.entity.Vehicle;

import com.google.common.collect.Lists;

@Entity
@NamedQuery(name = User.QUERY_BY_USERNAME_CASE_IGNORE.NAME, query = "FROM User u "
	+ "WHERE LOWER(u.username) = LOWER(:"
	+ User.QUERY_BY_USERNAME_CASE_IGNORE.PARAM_NAME + ")")
public class User extends BaseEntity {
    public static final class QUERY_BY_USERNAME_CASE_IGNORE {
	public static final String NAME = "User.byUsername";
	public static final String PARAM_NAME = "username";
    }

    @OneToMany
    private List<Vehicle> vehicles = Lists.newArrayList();

    @Column(unique = true, nullable = false)
    private String username;

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

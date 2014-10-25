package biz.suckow.fuel.business.user.entity;

import java.util.Collections;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

import biz.suckow.fuel.business.app.entity.BaseEntity;
import biz.suckow.fuel.business.consumption.entity.FuelConsumption;
import biz.suckow.fuel.business.refueling.entity.FuelStock;
import biz.suckow.fuel.business.refueling.entity.Refueling;

@Entity
@NamedQuery(name = User.QUERY_BY_USERNAME_CASE_IGNORE.NAME, query = "FROM User u WHERE LOWER(u.username) = LOWER(:"
	+ User.QUERY_BY_USERNAME_CASE_IGNORE.PARAM_NAME + ")")
public class User extends BaseEntity {

    public static final class QUERY_BY_USERNAME_CASE_IGNORE {
	public static final String NAME = "User.byUsername";
	public static final String PARAM_NAME = "username";
    }

    @Column(unique = true, nullable = false)
    private String username;

    @Column
    private FuelStock fuelStock;

    @OneToMany
    private List<Refueling> refuelings = Collections.emptyList();

    @OneToMany
    private List<FuelConsumption> fuelConsumptions = Collections.emptyList();

    public void addFuelConsuption(FuelConsumption consumption) {
	this.fuelConsumptions.add(consumption);
    }

    public void addRefueling(Refueling refueling) {
	this.refuelings.add(refueling);
    }

    public FuelStock getFuelStock() {
	return fuelStock;
    }

    public String getUsername() {
	return username;
    }

    public void setUsername(String username) {
	this.username = username;
    }

    public List<Refueling> getRefuelings() {
	return refuelings;
    }

    public List<FuelConsumption> getFuelConsumptions() {
	return fuelConsumptions;
    }

}

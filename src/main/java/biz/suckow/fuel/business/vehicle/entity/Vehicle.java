package biz.suckow.fuel.business.vehicle.entity;

import java.util.Collections;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import biz.suckow.fuel.business.app.entity.BaseEntity;
import biz.suckow.fuel.business.consumption.entity.FuelConsumption;
import biz.suckow.fuel.business.refueling.entity.FuelStock;
import biz.suckow.fuel.business.refueling.entity.Refueling;
import biz.suckow.fuel.business.user.entity.User;

// TEST
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "vehiclename",
	"user" }))
@NamedQueries({
@NamedQuery(name = Vehicle.ByUserAndVehicle.NAME, query = "FROM Vehicle v WHERE LOWER(v.vehiclename) = LOWER(:"
	+ Vehicle.ByUserAndVehicle.PARAM_VEHICLENAME_NAME
	+ ") AND LOWER(v.user.username = :"
	+ Vehicle.ByUserAndVehicle.PARAM_USERNAME_NAME + ")"),
	@NamedQuery(name = Vehicle.BY_MISSING_CONSUMPTION_OLDEST_FIRST, query = "FROM Refueling r WHERE r.isFillUp = false ORDER BY r.date ASC")})
public class Vehicle extends BaseEntity {
    public static final String BY_MISSING_CONSUMPTION_OLDEST_FIRST = "Vehicle.byMissingConsumptionOldestFirst";

    public static final class ByUserAndVehicle {
	public static final String NAME = "Vehicle.ByUserAndVehicle";
	public static final String PARAM_USERNAME_NAME = "username";
	public static final String PARAM_VEHICLENAME_NAME = "vehiclename";
    }

    @Column(nullable = false)
    private String vehiclename;

    @Column(nullable = false)
    private User user;

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

    public List<Refueling> getRefuelings() {
	return refuelings;
    }

    public List<FuelConsumption> getFuelConsumptions() {
	return fuelConsumptions;
    }
}

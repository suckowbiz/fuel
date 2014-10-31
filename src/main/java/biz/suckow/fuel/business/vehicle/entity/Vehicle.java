package biz.suckow.fuel.business.vehicle.entity;

import java.util.Collections;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import biz.suckow.fuel.business.app.entity.BaseEntity;
import biz.suckow.fuel.business.consumption.entity.FuelConsumption;
import biz.suckow.fuel.business.refueling.entity.FuelStock;
import biz.suckow.fuel.business.refueling.entity.Refueling;
import biz.suckow.fuel.business.user.entity.User;
import com.google.common.collect.Lists;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

// TODO test
@Entity
//@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "vehiclename",
//	"user.username" }))
@NamedQuery(name = Vehicle.QueryByUserAndVehicle.NAME, query = "FROM Vehicle v WHERE LOWER(v.vehiclename) = LOWER(:"
	+ Vehicle.QueryByUserAndVehicle.PARAM_VEHICLENAME_NAME
	+ ") AND LOWER(v.user.username) = :"
	+ Vehicle.QueryByUserAndVehicle.PARAM_USERNAME_NAME)
public class Vehicle extends BaseEntity {
    public static final class QueryByUserAndVehicle {
	public static final String NAME = "Vehicle.ByUserAndVehicle";
	public static final String PARAM_USERNAME_NAME = "username";
	public static final String PARAM_VEHICLENAME_NAME = "vehiclename";
    }

    @Column(nullable = false)
    private String vehiclename;

    @ManyToOne(optional = false)
    private User user;

    @OneToOne
    private FuelStock fuelStock;

    @OneToMany
    private List<Refueling> refuelings;

    @OneToMany
    private List<FuelConsumption> fuelConsumptions;

    public Vehicle() {
        this.fuelConsumptions = Lists.newArrayList();
        this.refuelings = Lists.newArrayList();
    }
    
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

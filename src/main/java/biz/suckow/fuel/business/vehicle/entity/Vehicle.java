package biz.suckow.fuel.business.vehicle.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import biz.suckow.fuel.business.app.entity.BaseEntity;
import biz.suckow.fuel.business.consumption.entity.FuelConsumption;
import biz.suckow.fuel.business.owner.entity.Owner;
import biz.suckow.fuel.business.refueling.entity.FuelStock;
import biz.suckow.fuel.business.refueling.entity.Refueling;

import com.google.common.collect.Lists;

// TODO test
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "vehiclename",
	"owner_id" }))
@NamedQuery(name = Vehicle.QueryByOwnerAndVehicle.NAME, query = "SELECT v FROM Vehicle v WHERE LOWER(v.vehiclename) = LOWER(:"
	+ Vehicle.QueryByOwnerAndVehicle.PARAM_VEHICLENAME_NAME
	+ ") AND LOWER(v.owner.ownername) = :"
	+ Vehicle.QueryByOwnerAndVehicle.PARAM_OWNERNAME_NAME)
public class Vehicle extends BaseEntity {
    private static final long serialVersionUID = -5360751385120611439L;

    public static final class QueryByOwnerAndVehicle {
	public static final String NAME = "Vehicle.ByOwnerAndVehicle";
	public static final String PARAM_OWNERNAME_NAME = "ownername";
	public static final String PARAM_VEHICLENAME_NAME = "vehiclename";
    }

    @Column(nullable = false)
    private String vehiclename;

    @ManyToOne(optional = false)
    private Owner owner;

    @OneToOne
    private FuelStock fuelStock;

    @OneToMany(mappedBy = "vehicle")
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

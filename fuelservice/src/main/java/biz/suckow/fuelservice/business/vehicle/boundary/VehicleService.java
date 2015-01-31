package biz.suckow.fuelservice.business.vehicle.boundary;

import biz.suckow.fuelservice.business.owner.boundary.OwnerService;
import biz.suckow.fuelservice.business.owner.entity.Owner;
import biz.suckow.fuelservice.business.vehicle.entity.Vehicle;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Stateless
public class VehicleService {

    @Inject
    private OwnerService ownerService;

    @Inject
    private EntityManager em;

    public void persist(Vehicle vehicle) {
        this.em.persist(vehicle);
    }

    public Set<String> getNamesOfOwnedVehicles(String name) {
        Set<String> result = new HashSet<>();
        result.add("duke-car");
        return result;
    }

    public void addVehicle(String email, String vehicleName) {
        Optional<Owner> possibleOwner = this.ownerService.locateByEmail(email);
        if (possibleOwner.isPresent()) {
            Vehicle vehicle = new Vehicle().setOwner(possibleOwner.get()).setVehicleName(vehicleName);
            this.em.persist(vehicle);
        }
    }
}

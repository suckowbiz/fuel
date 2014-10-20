package biz.suckow.fuel.business.refueling.boundary;

import javax.ejb.Stateless;
import javax.ws.rs.Path;

@Path("refuelings")
@Stateless
public class RefuelingResource {

    // 1. from station fills tank
    public void standardRefuel(String username, String kilometers,
	    String litres, String euros, String memo) {

    }

    // 2. from station fills tank partially
    public void partialRefuel(String username, String litres, String euros,
	    String memo) {

    }

    // 3. from station fills tank and stock
    public void overRefuel(String username, String kilometers,
	    String litresTank, String litresStock, String euros, String memo) {

    }

    // 4. from station fill stock
    public void refuelStock(String username, String litres, String euros,
	    String memo) {

    }

    // 5. from stock fills tank
    public void fromStockFullRefuel(String username, String litres,
	    String kilometres, String memo) {

    }

    // 6. from stock fills tank partially
    public void fromStockPartialRefuel(String username, String litres,
	    String memo) {

    }
}

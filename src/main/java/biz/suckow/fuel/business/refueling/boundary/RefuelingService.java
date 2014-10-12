package biz.suckow.fuel.business.refueling.boundary;

import javax.ejb.Singleton;
import javax.ws.rs.Path;

@Path("refuelings")
@Singleton
public class RefuelingService {

   /**
    * 1. from station fills tank
    * 2. from station fills tank partially
    * 3. from station fills tank and stock
    * 4. from station fill stock
    * 5. from stock fills tank
    * 6. from stock fills tank partially
    * 
    * @param id
    * @param email
    * @param kilometers
    * @param litres
    * @param euros
    */
    
    public void refuel(String id, String email, String kilometers,
	    String litres, String euros) {

    }

    public void overRefuel(String id, String email, String kilometers,
	    String litresTank, String litresStock, String euros) {

    }

    public void underRefuel(String id, String email, String litres, String euros) {
	
    }

    public void stockRefuel(String id, String email, String litres) {

    }
    
    public void releaseRefuel() {
	
    }

}

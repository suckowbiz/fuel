package biz.suckow.fuel.business.owner.boundary;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import biz.suckow.fuel.business.app.entity.BaseEntity;
import biz.suckow.fuel.business.owner.entity.Owner;

@Stateless
public class OwnerService extends BaseEntity {
    /**
     * 
     */
    private static final long serialVersionUID = -218334641369264690L;
    @PersistenceContext
    private EntityManager em;

    // TODO write test
    public Owner getOwner(String ownername) {
	Owner result = (Owner) this.em
		.createNamedQuery(Owner.QueryByOwnerameCaseIgnore.NAME)
		.setParameter(Owner.QueryByOwnerameCaseIgnore.PARAM_NAME,
			ownername).getSingleResult();
	return result;
    }

}

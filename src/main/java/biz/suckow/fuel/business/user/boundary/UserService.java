package biz.suckow.fuel.business.user.boundary;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import biz.suckow.fuel.business.app.entity.BaseEntity;
import biz.suckow.fuel.business.user.entity.User;

@Stateless
public class UserService extends BaseEntity {
    @PersistenceContext
    private EntityManager em;

    // TODO write test
    public User getUser(String username) {
	User result = (User) this.em
		.createNamedQuery(User.QueryByUsernameCaseIgnore.NAME)
		.setParameter(User.QueryByUsernameCaseIgnore.PARAM_NAME,
			username).getSingleResult();
	return result;
    }

}

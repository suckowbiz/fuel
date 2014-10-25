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

    public User getUser(String username) {
	// TODO 1. Implement using named query
	throw new UnsupportedOperationException();
    }

}

package com.irandubamodulo01.daoimpl;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.irandubamodulo01.annotation.Transactional;
import com.irandubamodulo01.dao.DAO;
import com.irandubamodulo01.util.JPAUtil;

public abstract class DAOimpl<T, I extends Serializable> implements DAO<T, I>{
	
	@Inject
    private EntityManager em;
	
	private Connection connection;
  
	@Override
	@Transactional
	public T save(T entity) {
		return em.merge(entity);
	}
	
	@Override
	@Transactional
	public void remove(T entity) {
		//getEntityManager().getTransaction().begin();
		//getEntityManager().remove(getEntityManager().merge(entity));
		//getEntityManager().getTransaction().commit();
		em.remove(em.merge(entity));
	}
	
	@Override
	public T getById(Class<T> classe, I pk) {
		try {
			return em.find(classe, pk);
		} catch (Exception e) {
			return null;
		}
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> getAll(Class<T> classe) {
		return em.createQuery("select  o from "+classe.getSimpleName()+" o").getResultList();
		
	}
	
	/*@Override
	public EntityManager getEntityManager() {
		if (em == null) {
			em = JPAUtil.getEntityManager();
		}
		return em;
	}*/
	
	@Override
	@Transactional
	public Connection getConnection(){
		
		/*try {
			
			//em.getTransaction().begin();
			con = em.unwrap(Connection.class);
			//em.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			//em.getTransaction().rollback();
		}*/
		
		Session session = em.unwrap(Session.class);
		
		session.doWork(new Work() {
			
			@Override
			public void execute(Connection arg0) throws SQLException {
				
				connection = arg0;
			}
		});
		
		return connection;
	}
	
}

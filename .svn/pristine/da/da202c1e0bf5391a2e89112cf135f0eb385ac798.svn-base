package com.irandubamodulo01.dao;

import java.io.Serializable;
import java.sql.Connection;
import java.util.List;

import javax.persistence.EntityManager;



public interface DAO<T, I extends Serializable> {

	public T save(T entity);
	public void remove(T entity);
	public T getById(Class<T> classe, I pk);
	public List<T> getAll(Class<T> classe);
	//public EntityManager getEntityManager();
	public Connection getConnection();
}

package com.irandubamodulo01.dao;

import java.util.List;

import com.irandubamodulo01.model.Camara;
import com.irandubamodulo01.model.PosicaoCamara;

public interface PosicaoCamaraDAO extends DAO<PosicaoCamara, Long>{

	public List<PosicaoCamara> getPosicoesByCamara(Camara camara);
	public List<PosicaoCamara> getPosicoesComplete();
}

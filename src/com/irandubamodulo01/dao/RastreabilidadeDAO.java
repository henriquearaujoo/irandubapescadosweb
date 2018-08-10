package com.irandubamodulo01.dao;

import java.util.Date;
import java.util.List;

import com.irandubamodulo01.model.Camara;
import com.irandubamodulo01.model.Rastreabilidade;
import com.irandubamodulo01.model.TipoPeixe;
import com.irandubamodulo01.util.Filtro;

public interface RastreabilidadeDAO extends DAO<Rastreabilidade, Long>{

	public List<Rastreabilidade> filtrados(Filtro filtro);
	public int quantidadeFiltrados(Filtro filtro);
	public List<Rastreabilidade> getRastreabilidadesPorPeriodo(Date dataInicio, Date dataFim);
}

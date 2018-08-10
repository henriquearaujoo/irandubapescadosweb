package com.irandubamodulo01.dao;

import java.math.BigDecimal;
import java.util.List;

import com.irandubamodulo01.model.Armazenamento;
import com.irandubamodulo01.model.Camara;
import com.irandubamodulo01.model.Peixe;
import com.irandubamodulo01.model.TipoPeixe;
import com.irandubamodulo01.util.EstoqueUtil;
import com.irandubamodulo01.util.Filtro;

public interface ArmazenamentoDAO extends DAO<Armazenamento, Long>{

	public Armazenamento obterArmazenamentoPorId(Long id);
	public List<Armazenamento> filtrados(Filtro filtro);
	public int quantidadeFiltrados(Filtro filtro);
	public BigDecimal getPesoDisponivelPorPeixe(Peixe peixe);
	public BigDecimal getPesoDisponivelPorPeixeTipo(Peixe peixe, TipoPeixe tipoPeixe);
	public List<EstoqueUtil> getEstoquePorPeixeCamaraTipo(Peixe peixe, Camara camara, TipoPeixe tipoPeixe);
}

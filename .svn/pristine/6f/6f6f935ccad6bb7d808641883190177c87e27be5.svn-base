package com.irandubamodulo01.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.irandubamodulo01.model.Compra;
import com.irandubamodulo01.model.Lote;
import com.irandubamodulo01.util.Filtro;
import com.irandubamodulo01.util.PeixePesoUtil;

public interface CompraDAO extends DAO<Compra, Long> {
	
	public int quantidadeFiltrados(Filtro filtro);
	
	public List<Compra> filtrados(Filtro filtro);
	
	public List<Lote> getListaLotesCompra(Compra compra);

	public Integer obterQtdeComprasAPagar();

	public Integer obterQtdeComprasAAutorizar();

	public BigDecimal obterValorTotalComprasPorData(Date date);

	public BigDecimal obterValorTotalComprasPorPeriodo(Date dataInicio, Date dataFim);

	public BigDecimal obterPesoTotalComprasPorData(Date date);

	public BigDecimal obterPesoTotalComprasNaoFinalizadasPorData(Date date);

	public BigDecimal obterPesoTotalComprasPorPeriodo(Date dataInicio, Date dataFim);

	public List<PeixePesoUtil> obterTop10TotalPeixesPesadosPorData(Date date);

}

package com.irandubamodulo01.dao;

import java.util.List;

import com.irandubamodulo01.model.Compra;
import com.irandubamodulo01.model.HistoricoAlteracaoValor;
import com.irandubamodulo01.model.Lote;

public interface HistoricoAlteracaoValorDao extends DAO<HistoricoAlteracaoValor, Long>{

	public void salvarHistoricoEmLotes(List<HistoricoAlteracaoValor> historicos);
	
	public List<HistoricoAlteracaoValor> getHistoricoPorCompra(Long id);

	public List<HistoricoAlteracaoValor> getHistorico(Compra compra);
	
	public List<HistoricoAlteracaoValor> getHistoricoPorTipoEcompra(Compra compra);
	
	public void removeAlteracaoLote(Lote lote);
	
	public void removeAlteracaoTotal(Compra compra);
}

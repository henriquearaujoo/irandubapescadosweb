package com.irandubamodulo01.dao;

import java.util.List;

import com.irandubamodulo01.model.*;

public interface ArquivoDAO extends DAO<Arquivo, Long>{

	public List<Arquivo> getArquivosPorPagamento(Pagamento pagamento);
	public List<Arquivo> getArquivosPorPeixe(Peixe peixe);
	public List<Arquivo> getArquivosPorPedido(Pedido pedido);
	public List<Arquivo> getArquivosPorAdiantamento(AdiantamentoFornecedor adiantamento);
}

package com.irandubamodulo01.request.repository;

import com.irandubamodulo01.model.Fornecedor;
import com.irandubamodulo01.repository.FornecedorRepository;

public class FornecedorCrud {

	private FornecedorRepository repository;
	
	public FornecedorCrud(FornecedorRepository repository){
		this.repository = repository;
	}
	
	public void salvar(Fornecedor fornec){
		if (fornec != null) {
			repository.salvar(fornec);
		}
		
	}
	
	
	
}

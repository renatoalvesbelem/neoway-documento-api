package com.neoway.documento.business;

public enum TipoDocumento {
	CPF(1), CNPJ(2);
	int value;
	TipoDocumento(int value) {
		this.value = value;
	}

	public Integer getValue(){
		return value;
	}
}

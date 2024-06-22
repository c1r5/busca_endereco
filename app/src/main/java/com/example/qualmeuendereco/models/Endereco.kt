package com.example.qualmeuendereco.models

data class Endereco(
    val cep: String?,
    val logradouro: String?,
    val complemento: String?,
    val unidade: String?,
    val bairro: String?,
    val localidade: String?,
    val uf: String?,
    val ibge: String?,
    val gia: String?,
    val ddd: String?,
    val siafi: String?
) {
    data class Builder(
        private var cep: String? = null,
        private var logradouro: String? = null,
        private var complemento: String? = null,
        private var unidade: String? = null,
        private var bairro: String? = null,
        private var localidade: String? = null,
        private var uf: String? = null,
        private var ibge: String? = null,
        private var gia: String? = null,
        private var ddd: String? = null,
        private var siafi: String? = null
    ) {
        fun cep(cep: String) = apply { this.cep = cep }
        fun logradouro(logradouro: String) = apply { this.logradouro = logradouro }
        fun complemento(complemento: String) = apply { this.complemento = complemento }
        fun unidade(unidade: String) = apply { this.unidade = unidade }
        fun bairro(bairro: String) = apply { this.bairro = bairro }
        fun localidade(localidade: String) = apply { this.localidade = localidade }
        fun uf(uf: String) = apply { this.uf = uf }
        fun ibge(ibge: String) = apply { this.ibge = ibge }
        fun gia(gia: String) = apply { this.gia = gia }
        fun ddd(ddd: String) = apply { this.ddd = ddd }
        fun siafi(siafi: String) = apply { this.siafi = siafi }
        fun build() = Endereco(
            cep,
            logradouro,
            complemento,
            unidade,
            bairro,
            localidade,
            uf,
            ibge,
            gia,
            ddd,
            siafi
        )
    }
}
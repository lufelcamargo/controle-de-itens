package com.example.controleitens.data.local.dao

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.Query
import androidx.room3.Update
import com.example.controleitens.data.local.entity.ItemModeloEntity

@Dao
interface ItemModeloDao {

    @Insert
    suspend fun inserir(itemModelo: ItemModeloEntity)

    @Update
    suspend fun atualizar(itemModelo: ItemModeloEntity)

    @Delete
    suspend fun excluir(itemModelo: ItemModeloEntity)

    @Query("SELECT * FROM itens_modelo WHERE id = :id")
    suspend fun buscarPorId(id: String): ItemModeloEntity?

    @Query("SELECT * FROM itens_modelo WHERE modeloId = :modeloId")
    suspend fun buscarPorModeloId(modeloId: String): List<ItemModeloEntity>

    @Query("DELETE FROM itens_modelo WHERE modeloId = :modeloId")
    suspend fun excluirPorModeloId(modeloId: String)
}
package com.example.controleitens.data.local.dao

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.Query
import androidx.room3.Update
import com.example.controleitens.data.local.entity.ItemEntity

@Dao
interface ItemDao {

    @Insert
    suspend fun inserir(item: ItemEntity)

    @Query("SELECT * FROM itens WHERE ativo = 1")
    suspend fun buscarAtivos(): List<ItemEntity>

    @Query("SELECT * FROM itens WHERE id = :id")
    suspend fun buscarPorId(id: String): ItemEntity?

    @Update
    suspend fun atualizar(item: ItemEntity)

    @Query("UPDATE itens SET ativo = 0 WHERE id = :id")
    suspend fun desativar(id: String)
}